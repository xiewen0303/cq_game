package com.junyou.stage.configure.export.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 * 
 * @description 
 *
 * @author LiuJuan
 * @date 2012-3-6 下午2:24:36
 */
@Component
public class BuffConfigExportService extends AbsClasspathConfigureParser {

	/**
	 * 配置名
	 */
	private String configureName = "XiaoGuoBiao.jat";
	
	private Map<String, BuffConfig> configs;

	@Override
	protected String getConfigureName() {
		
		return configureName;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {
		if(data == null){
			return;
		}
		Map<String, BuffConfig> configs = new HashMap<>();
		
		Object[] effectListData = GameConfigUtil.getResource(data);
		for (Object obj:effectListData) {
			Map<Object, Object> tmp = (Map<Object, Object>)obj;
			if (null != tmp) {
				BuffConfig effectConfig = getEffectConfig(tmp);
				configs.put(effectConfig.getEffectId(), effectConfig);
				
			}
		}
		this.configs = configs;
	}
	
	private BuffConfig getEffectConfig(Map<Object, Object> tmp) {
		BuffConfig buffConfig = new BuffConfig();
		buffConfig.setEffectId(CovertObjectUtil.object2String(tmp.get("id")));
		buffConfig.setEffectName(CovertObjectUtil.object2String(tmp.get("name")));
		
		buffConfig.setEffectType(CovertObjectUtil.object2String(tmp.get("type")));
		buffConfig.setLastTime(CovertObjectUtil.object2int(tmp.get("lasttime")));
		buffConfig.setCover(createBuffCover(tmp));
		buffConfig.setMaxStack(CovertObjectUtil.object2int(tmp.get("maxstack")));
		buffConfig.setEffectPro(createBuffAttributes(tmp));
		buffConfig.setBaseHit(CovertObjectUtil.object2Float(tmp.get("basehit")));
		buffConfig.setDeadClear(CovertObjectUtil.object2boolean(tmp.get("siwang")));
		buffConfig.setOfflineClear(CovertObjectUtil.object2boolean(tmp.get("xiaxian")));
		buffConfig.setChangeClear(CovertObjectUtil.object2boolean(tmp.get("changemap")));
		buffConfig.setMoveClear(CovertObjectUtil.object2boolean(tmp.get("yidong")));
		
		//不可被角色攻击
		buffConfig.setBeiAttackAble(CovertObjectUtil.object2boolean(tmp.get("attackable")));
		//不可被怪物攻击
		buffConfig.setBeiAttackAble(CovertObjectUtil.object2boolean(tmp.get("attackable1")));
		//不可移动
		buffConfig.setMoveAble(CovertObjectUtil.object2boolean(tmp.get("walkable")));
		//不可攻击
		buffConfig.setAttackAble(CovertObjectUtil.object2boolean(tmp.get("attackable2")));
		//不可骑乘
		buffConfig.setRideAble(CovertObjectUtil.object2boolean(tmp.get("radable")));
		//不可消耗
		buffConfig.setConsumAble(CovertObjectUtil.object2boolean(tmp.get("consumable")));
		//不可传送
		buffConfig.setTeleportAble(CovertObjectUtil.object2boolean(tmp.get("teleportable")));
		//是否只对普通怪物生效
		buffConfig.setMonsterNomal(CovertObjectUtil.object2boolean(tmp.get("rank")));
		
		
		String skill = CovertObjectUtil.object2String(tmp.get("skill"));
		if(!ObjectUtil.strIsEmpty(skill)){
			buffConfig.setSkill(skill);
			buffConfig.setInterval(CovertObjectUtil.object2int(tmp.get("interval")));
			buffConfig.setSpecialEffect("singleEffect");
		}
		
		return buffConfig;
	}
	
	/**
	 * 效果
	 * @param tmp
	 * @return
	 */
	public static List<String> createBuffCover(Map<Object, Object> tmp) {
		List<String> coverList = new ArrayList<String>();
		for (int i = 1; i <= 10; i++) {
			String cover = CovertObjectUtil.object2String(tmp.get("cover"+i));
			if (!ObjectUtil.strIsEmpty(cover)) {			
				coverList.add(cover);
			}
		}
		
		return coverList;
	}
	
	/**
	 * 
	 * @param tmp
	 * @return
	 */
	private static Map<String, Long> createBuffAttributes(Map<Object, Object> tmp){
		Map<String, Long> buffAttribute = new HashMap<>();
		for (int i = 1; i <= 10; i++) {
			String effectProId = CovertObjectUtil.object2String(tmp.get("pro"+i));
			if (!ObjectUtil.strIsEmpty(effectProId)) {
				long effectProValue = CovertObjectUtil.obj2long(tmp.get("provalue" +i));
				buffAttribute.put(effectProId, effectProValue);
			}
		}

		return buffAttribute;
	}
	
	public BuffConfig loadById(String buffId) {
		return configs.get(buffId);
	}


}
