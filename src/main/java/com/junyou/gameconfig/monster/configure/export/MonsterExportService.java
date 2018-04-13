package com.junyou.gameconfig.monster.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.constants.DropIdType;
import com.junyou.gameconfig.export.DropRule;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 怪物
 *
 * @author LiuJuan
 * @created 2011-12-12 上午11:20:00
 */
@Component
public class MonsterExportService extends AbsClasspathConfigureParser{
	
	
	/**
	 * 配置名
	 */
	private String configureName = "GuaiWuBiao.jat";
	
	/**
	 * 掉落类型
	 */
	private static String DROP_STARTSWITH = "@";
	/**
	 * 掉落次数
	 */
	private static String DROPSP_DROPCOUNT = "*";
	
	private static String SPLIT_STR = "\\|";
	
	private Map<String, MonsterConfig> configs;

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] monsterListData = GameConfigUtil.getResource(data);
		Map<String, MonsterConfig> configs = new HashMap<>();
		for (Object obj:monsterListData) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				MonsterConfig monsterConfig = new MonsterConfig();
				monsterConfig.setId(CovertObjectUtil.object2String(tmp.get("id")));
				monsterConfig.setMonsterType(CovertObjectUtil.object2int(tmp.get("monstertype")));
				monsterConfig.setName(CovertObjectUtil.object2String(tmp.get("name")));
				monsterConfig.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
				
				ReadOnlyMap<String, Long> effectMap = new ReadOnlyMap<>(ConfigAnalysisUtils.setAttributeVal(tmp));
				monsterConfig.setAttribute(effectMap);
				
				monsterConfig.setSkillList(createSkill(tmp));
				monsterConfig.setCamp(CovertObjectUtil.object2int(tmp.get("camp")));
				monsterConfig.setIfactive(CovertObjectUtil.object2boolean(tmp.get("mode")));
				monsterConfig.setEyeshot(CovertObjectUtil.object2int(tmp.get("shiye")));
				monsterConfig.setBasicExp(CovertObjectUtil.object2int(tmp.get("jiangexp")));
				
				//每秒回复的血量
				monsterConfig.setHuiFuHp(CovertObjectUtil.object2int(tmp.get("huifuhp")));
				
				monsterConfig.setLasttime(CovertObjectUtil.object2int(tmp.get("lasttime")));
				monsterConfig.setDropRule(createDropRules(tmp));
//				monsterConfig.setMoneyDropRules(createMoneyDropRules(tmp));
				
				monsterConfig.setMaxrange(CovertObjectUtil.object2int(tmp.get("zhuiji")));
				
				monsterConfig.setDisappearDuration(CovertObjectUtil.object2int(tmp.get("deadtime")));
				
				//怪物掉落物品的消失时间（秒）
				monsterConfig.setDropGoodsDuration(CovertObjectUtil.object2int(tmp.get("droptime")));
				//掉落保护时间（秒）
				monsterConfig.setDropProtectDuration(CovertObjectUtil.object2int(tmp.get("dropbaohu")));
				
				monsterConfig.setRank(CovertObjectUtil.object2int(tmp.get("rank")));
				monsterConfig.setHeartTime(CovertObjectUtil.object2int(tmp.get("xintiao")));
				if(monsterConfig.getHeartTime() < 300){
					//YDZ 2015-8-25
					monsterConfig.setHeartTime(300);
				}
				
				//不可移动
				monsterConfig.setNoMove(CovertObjectUtil.object2boolean(tmp.get("walkable")));
				//不可攻击其它人
				monsterConfig.setNoAttrack(CovertObjectUtil.object2boolean(tmp.get("attackable1")));
				//不可被攻击
				monsterConfig.setNoBeiAttrack(CovertObjectUtil.object2boolean(tmp.get("attackable")));
				//是否只攻击红名玩家
				monsterConfig.setAttriackRedRold(CovertObjectUtil.object2boolean(tmp.get("attackred")));
				//怪物死亡广播
				monsterConfig.setDeadNotify(CovertObjectUtil.object2boolean(tmp.get("guangbo")));
				//是否自动巡逻 true:自动巡逻 不区分主被动
				monsterConfig.setXunluo(CovertObjectUtil.object2Boolean(tmp.get("xunluo")));
				//怪物类型
				monsterConfig.setType(CovertObjectUtil.object2int(tmp.get("monstertype")));
				//是否没有受益人
				monsterConfig.setNoOwner(CovertObjectUtil.object2boolean(tmp.get("noowner")));
				//是否仅自己可以拾取
				monsterConfig.setOnlySelfPickup(CovertObjectUtil.object2boolean(tmp.get("onlyselfpickup")));
				String aiStr = CovertObjectUtil.obj2StrOrNull(tmp.get("aiscript"));
				if( aiStr != null ){
					monsterConfig.setAiscripts(aiStr);
				}
				//是否可被击退
				monsterConfig.setBeatback(CovertObjectUtil.object2int(tmp.get("beatback")));
				configs.put(monsterConfig.getId(), monsterConfig);
			}
		}
		this.configs = configs;
	}
	
//	private List<MoneyDropRule> createMoneyDropRules(Map<String, Object> tmp) {//金币掉落不单独计算  --by刘愚
//		List<MoneyDropRule> moneyDropRules = null;
//		
//		String basicmoney = CovertObjectUtil.object2String(tmp.get("jiangmoney"));
//		
//		if(null != basicmoney && !"".equals(basicmoney)){
//			
//			moneyDropRules = new ArrayList<MoneyDropRule>();
//			
//			String[] rules = basicmoney.split("\\;");
//			
//			for(String rule : rules){
//				
//				MoneyDropRule moneyDropRule = new MoneyDropRule();
//				
//				try {
//					String[] data = rule.split("\\|");
//					Integer count = Integer.parseInt(data[0]);
//					Integer jb = Integer.parseInt(data[1]);
//					Float odds = Float.parseFloat(data[2]);
//					
//					moneyDropRule.setDropCount(count);
//					moneyDropRule.setDropJb(jb);
//					moneyDropRule.setOdds(odds);
//					moneyDropRules.add(moneyDropRule);
//				} catch (Exception e) {
//					ChuanQiLog.error("basicmoney value:"+basicmoney,e);
//				}
//			}
//			
//		}
//		
//		return moneyDropRules;
//	}

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
	/**
	 * 技能
	 * @param map
	 * @return
	 */
	private static List<String> createSkill(Map<String, Object> map) {
		List<String> skillList = new ArrayList<String>();
		
		try {
			String skill1 = CovertObjectUtil.object2String(map.get("skill1"));
			if(null != skill1 && !"".equals(skill1)){
				skillList.add(skill1);
			}
			
			String skill2 = CovertObjectUtil.object2String(map.get("skill2"));
			if(null != skill2 && !"".equals(skill2)){
				skillList.add(skill2);
			}
			
			String skill3 = CovertObjectUtil.object2String(map.get("skill3"));
			if(null != skill3 && !"".equals(skill3)){
				skillList.add(skill3);
			}
			
			String skill4 = CovertObjectUtil.object2String(map.get("skill4"));
			if(null != skill4 && !"".equals(skill4)){
				skillList.add(skill4);
			}
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		
		
		return skillList;
	}
	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public MonsterConfig load(String monsterId) {
		return configs.get(monsterId);
	}

}
