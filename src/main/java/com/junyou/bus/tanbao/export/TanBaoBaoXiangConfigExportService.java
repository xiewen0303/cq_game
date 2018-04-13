package com.junyou.bus.tanbao.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.tanbao.entity.TanBaoBaoXiangConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.constants.DropIdType;
import com.junyou.gameconfig.export.DropRule;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;


/**
 * @author LiuYu
 * 2015-6-17 下午10:15:14
 */
@Service
public class TanBaoBaoXiangConfigExportService extends AbsClasspathConfigureParser{
	/**
	 * 配置名
	 */
	private String configureName = "BaoXiangBiao.jat";
	
	/**
	 * 掉落类型
	 */
	private static String DROP_STARTSWITH = "@";
	/**
	 * 掉落次数
	 */
	private static String DROPSP_DROPCOUNT = "*";
	
	private static String SPLIT_STR = "\\|";
	
	private Map<Integer, TanBaoBaoXiangConfig> configs;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] listData = GameConfigUtil.getResource(data);
		Map<Integer, TanBaoBaoXiangConfig> configs = new HashMap<>();
		for (Object obj:listData) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			TanBaoBaoXiangConfig config = createTanBaoBaoXiangConfig(tmp);
			if(config != null){
				configs.put(config.getId(), config);
			}
		}
		this.configs = configs;
	}
	
	private TanBaoBaoXiangConfig createTanBaoBaoXiangConfig(Map<String, Object> tmp){
		String zuobiaos = CovertObjectUtil.obj2StrOrNull(tmp.get("zuobiao"));
		if(zuobiaos == null){
			return null;
		}
		TanBaoBaoXiangConfig config = new TanBaoBaoXiangConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		List<Integer[]> zuobiaoList = new ArrayList<>();
		for (String zuobiaoStr : zuobiaos.split(SPLIT_STR)) {
			String[] info = zuobiaoStr.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
			zuobiaoList.add(new Integer[]{Integer.parseInt(info[0]),Integer.parseInt(info[1])});
		}
		config.setZuobiao(zuobiaoList);
		config.setDropGoodsDuration(CovertObjectUtil.object2int(tmp.get("droptime")));
		config.setDropProtectDuration(CovertObjectUtil.object2int(tmp.get("dropbaohu")));
		config.setDropRule(createDropRules(tmp));
		config.setGonggao(CovertObjectUtil.obj2StrOrNull(tmp.get("gonggao")));
		config.setGonggao1(CovertObjectUtil.obj2StrOrNull(tmp.get("gonggao1")));
		return config;
	}
	
	/**
	 * 掉落
	 * @param map
	 * @return
	 */
	private List<DropRule> createDropRules(Map<String, Object> map) {
		List<DropRule> dropRuleList = new ArrayList<DropRule>();
		try {
		
			for (int i = 1; i <= 45; i++) {
				String drop = CovertObjectUtil.object2String(map.get("drop"+i));
				float dropOdds = CovertObjectUtil.object2Float(map.get("dropodds"+i));
				String dropSp = CovertObjectUtil.object2String(map.get("dropsp"+i));
				
				DropRule dropRule = createRule(drop, dropSp, dropOdds);
				if(null != dropRule){
					dropRuleList.add(dropRule);
				}
			}
			
		} catch (Exception e) {
			ChuanQiLog.error("",e);
			System.out.println(map.get("id"));
		}
		
		
		return dropRuleList;
	}
	
	private DropRule createRule(String drop,String dropSp,float dropOdds){
		
		DropRule dropRule = null;
		
		if (drop != null && !"".equals(drop)) {
			
			dropRule = new DropRule();
			
			boolean dropBln = drop.startsWith(DROP_STARTSWITH);
			if (dropBln) {
				dropRule.setDropIdType(DropIdType.COMPONENTID);
				dropRule.setComponentDropId(drop);
			} else {
				dropRule.setDropIdType(DropIdType.GOODSID);
				dropRule.setDropId(drop);
			}
		
			dropRule.setDropRate(dropOdds);
			
			if (dropSp != null) {				
				String[] dropSpStr = dropSp.split(SPLIT_STR);
				for (int j = 0; j < dropSpStr.length; j++) {
					String value = dropSpStr[j];
					if (value.indexOf(DROPSP_DROPCOUNT) != -1) {
						dropRule.setDropCount(CovertObjectUtil.object2int(value.substring(1, value.length())));
					}
				}
			}
			
			return dropRule;
		}
		
		return dropRule;
		
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	/**
	 * 获取所有配置
	 * @return
	 */
	public Map<Integer, TanBaoBaoXiangConfig> getConfigs(){
		return configs;
	}
	
	/**
	 * 根据id获取配置
	 * @param id
	 * @return
	 */
	public TanBaoBaoXiangConfig getConfigById(Integer id){
		return configs.get(id);
	}
	
}
