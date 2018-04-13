package com.kernel.check.db;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Primary;
import com.kernel.check.db.annotation.Table;


/**
 * 检查实体和表的映射关系的工具
 * @author zl
 * @author ydz
 */
public class CheckEntityTableTool {
	
	private static Logger logger = LoggerFactory.getLogger(CheckEntityTableTool.class);
	
	private JdbcCheckBean jdbcCheckBean;
	
	private Connection getConnection(){
		try{
			
			String driver = "com.mysql.jdbc.Driver";
			String url = jdbcCheckBean.getJdbcUrl();
			String username = jdbcCheckBean.getUser();
			String password = jdbcCheckBean.getPassword();
			
			Class.forName(driver);
			return DriverManager.getConnection(url, username, password);  // 得到数据库连接
		}catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	/** 获取数据库中所有表和列名的对应关系 */
	private Map<String, TableMetaData> getAllTableColumns(){
		Map<String, TableMetaData> tableMetaDatas = new HashMap<>();
		
		Connection connection = null;
		try{
			connection = getConnection();
			DatabaseMetaData dbMetaData = connection.getMetaData();
			
			//get tableMetaDatas
			ResultSet tableRs = dbMetaData.getTables(null, null, "%", new String[]{"TABLE"});
			while(tableRs.next()){
				String tableName = tableRs.getString("TABLE_NAME");
				String tableCat = tableRs.getString("TABLE_CAT");
				TableMetaData tMetaData = new TableMetaData(dbMetaData,tableCat,tableName);
				tableMetaDatas.put(tableName, tMetaData);
			}
		}catch (Exception e) {
			logger.error("获取数据库中表的映射关系出错", e);
		}finally{
			if( connection != null ){
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("", e);
				}
			}
		}
		return tableMetaDatas;
	}
		
	
	/**
	 * 校验数据库
	 */
	public void check(JdbcCheckBean jdbcCheckBean){
		this.jdbcCheckBean = jdbcCheckBean;
		
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new AnnotationTypeFilter(Table.class));
		Set<BeanDefinition> candidates = provider.findCandidateComponents("com.junyou.bus");
		logger.info("共有[{}]个数据库实体", candidates.size());
		
		boolean allisOk = true;
		
		//获取表和字段
		Map<String, TableMetaData> mapColumnsMap = getAllTableColumns();
		
		//遍历出每一个Table的实体
		for (BeanDefinition beanDefinition : candidates) {
			String className = beanDefinition.getBeanClassName();
			Class<?> cls = null;
			try{
				cls = Class.forName(className);
			}catch (Exception e) {
				logger.error("", e);
			}
			Table table = cls.getAnnotation(Table.class);
			if( table == null )continue;
			
			String tableName = table.value();
			
			logger.debug("开始校验表[{}]", tableName);
			Field[] srcFields = cls.getDeclaredFields();
			
			//过滤掉 @EntityField 注解的字段
			List<Field> fieldList = new ArrayList<>();
			for( Field field : srcFields ){
				if( field.getAnnotation(EntityField.class) == null ){
					fieldList.add(field);
				}
			}
			
			//找不到相应的表
			TableMetaData tableMetaData = mapColumnsMap.get(tableName);
			if( tableMetaData == null ){
				logger.error("数据库错误,[{}]实体对应的表[{}]找不到", cls.getSimpleName(), tableName);
				allisOk = false;
				continue;
			}

			//没有主键
			if( tableMetaData.getPrimaryColumnsCount() == 0 ){
				logger.error("数据库错误,[{}]表没有主键，程序即将退出", tableName);
				allisOk = false;
				continue;
			}
			
			//数据库表字段和实体属性的数量不一致
			if( tableMetaData.getColumnCount() != fieldList.size() ){
				logger.warn("警告警告,数据库表字段和实体属性的数量不一致,[{}]实体的属性个数为[{}], [{}]表的字段个数为[{}]", cls.getSimpleName(), fieldList.size(), tableName, tableMetaData.getColumnCount());
			}
			
			for( Field field : fieldList ){
				
				Column column = field.getAnnotation(Column.class);
				if( column == null ){
					logger.warn("警告警告,实体[{}]的字段[{}]未加column注解,如果不对应数据库中的字段，请加@EntityField注解", cls.getSimpleName(), field.getName());
					continue;
				}
				
				Primary primary = field.getAnnotation(Primary.class);
				if( primary != null && !tableMetaData.primaryKeyExist(column.value())){
					logger.error("实体[{}]的主键[{}]不是表[{}]中的主键", cls.getSimpleName(), column.value(), tableName);
				}
				
				if( !tableMetaData.contains(column.value()) ){
					logger.error("数据库错误,实体中的字段[{}]在表[{}]中不存在，程序即将退出", column.value(), tableName);
					allisOk = false;
					break;
				}
				
			}
			
			if( !allisOk ){
				throw new RuntimeException("数据库校验不通过，请检查数据库");
			}
		}
		
	}
	
}
class TableMetaData {
	/** 过滤的数据库字段 */
	private static Set<String> columnFilters = new HashSet<>();
	static{
		columnFilters.add("log_update_time");
	}
	
	private DatabaseMetaData dbMetaData;
	
	private String tableName;
	private int columnCount = 0;
	
	
	private Map<String, ColumnMetaData> primaryColumns = new HashMap<>();
	
	private Map<String, ColumnMetaData> otherColumns = new HashMap<>();
	
	public TableMetaData(DatabaseMetaData dbMetaData,String catagory,String tableName) {
		this.dbMetaData = dbMetaData;
		this.tableName = tableName;
		try {
			initialize();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void initialize() throws SQLException {
		
		//get primarykeys
		List<String> primaryKeys = new ArrayList<String>();
		ResultSet primaryColumnRs = dbMetaData.getPrimaryKeys(null, null, tableName);
		while(primaryColumnRs.next()){
			String cName = primaryColumnRs.getString("COLUMN_NAME");
			primaryKeys.add(cName);
		}
		
		//build all columns
		ResultSet columnRs = dbMetaData.getColumns(null, null, tableName, "%");
		while(columnRs.next()){
			String column = columnRs.getString("COLUMN_NAME");
			//过滤掉不需要搭理的列
			if( columnFilters.contains(column) )continue;
			
			int sqlType = columnRs.getInt("DATA_TYPE");
			String sqlTypeName = columnRs.getString("TYPE_NAME");
			int columnSize = columnRs.getInt("COLUMN_SIZE");
			String defaultValue = columnRs.getString("COLUMN_DEF");
			ColumnMetaData cMetaData = new ColumnMetaData(column,columnSize, sqlType, sqlTypeName,defaultValue);
			
			if(primaryKeys.contains(column)){
				primaryColumns.put(column, cMetaData);
			}else{
				otherColumns.put(column, cMetaData);
			}
			columnCount++;
		}
		
	}
	/** 获取主键数量 */
	public int getPrimaryColumnsCount() {
		return primaryColumns.size();
	}
	/** 获取主键 */
	public Set<String> getPrimaryKeys(){
		return primaryColumns.keySet();
	}
	/** 判断主键是否存在 */
	public boolean primaryKeyExist(String columnName){
		return primaryColumns.containsKey(columnName);
	}
	/** 获取所有列的数量 */
	public int getColumnCount(){
		return columnCount;
	}
	/** 判断列名在表中是否存在 */
	public boolean contains(String columnName){
		return primaryColumns.containsKey(columnName) || otherColumns.containsKey(columnName);
	}
}

class ColumnMetaData {
	
	private String column;
	
	private int columnSize;
	
	private int sqlType;
	
	private String sqlTypeName;
	
	private Object defaultValue;
	
	/**
	 * @param column
	 * @param sqlType {@link java.sql.Types}
	 */
	public ColumnMetaData(String column,int columnSize,int sqlType,String sqlTypeName,Object defaultValue) {
		this.column = column;
		this.columnSize = columnSize;
		this.sqlType = sqlType;
		this.sqlTypeName = sqlTypeName;
		this.defaultValue = defaultValue;
	}
	
	public String getColumn() {
		return column;
	}
	
	public int getSqlType() {
		return sqlType;
	}
	
	public String getSqlTypeName() {
		return sqlTypeName;
	}
	
	public int getColumnSize() {
		return columnSize;
	}
	
	public Object getDefaultValue() {
		return defaultValue;
	}
	
}
