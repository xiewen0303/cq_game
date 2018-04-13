package com.junyou.gameconfig.goods.configure.export;

 
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;
 

/**
 * 道具类型排序表
  *@author: wind
  *@email: 18221610336@163.com
  *@version: 2014-12-18下午2:43:38
  *@Description:
 */
@Component
public class GoodsTypeOrderConfigExportService  extends AbsClasspathConfigureParser{
	 
	 
	/**
	  * configFileName
	 */
	private String configureName = "WuPinLeiXingPaiXuBiao.jat";
	
	
	private Map<String,Integer> typeOrders;
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public Integer getOrderByType(String type){
		return typeOrders.get(type);
	}

	protected void configureDataResolve(byte[] data) {
		if(data == null){
			return;
		}
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<String,Integer> typeOrders = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				typeOrders.put(CovertObjectUtil.object2String(tmp.get("name")),CovertObjectUtil.object2int(tmp.get("order")));
			}
		} 
		this.typeOrders = typeOrders;
	}
}