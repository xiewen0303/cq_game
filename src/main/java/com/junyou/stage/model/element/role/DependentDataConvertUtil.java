package com.junyou.stage.model.element.role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.junyou.stage.model.core.skill.IBuff;

/**
 * @description 输入数据转换
 * @author ChiShiJie
 * @date 2012-2-22 上午11:07:12 
 */
public class DependentDataConvertUtil {

	/**
	 * @description 场景数据输出
	 * @return [[roleId,mapId,mapX,mapY,hp,mp,maxHp,maxMp,buff,state,miaoFaBaoTime,miaoFaBaoCount,tiLi],[battleMode],[fabao]]
	 */
	public static Object stageDataOutput(Role role) {
		
		Collection<IBuff> buffs = role.getBuffManager().getBuffs();
		Object buffFormat = encodeBuffs(buffs);
		
		return new Object[]{
				new Object[]{
						role.getId(),
						role.getStage().getMapId(),
						role.getPosition().getX(),
						role.getPosition().getY(),
						(int)role.getFightAttribute().getCurHp(),
						(int)role.getFightAttribute().getCurHp(),
						(int)role.getFightAttribute().getMaxHp(),
						buffFormat,
						role.getStateManager().isDead() ? 1 : 0,
				        role.getBusinessData().getMiaoFaBaoTime(),
				        role.getBusinessData().getMiaoFaBaoCount(),
				        0,
				        role.getStage().getLineNo()
				},
				null,
				null
		};
	}
	
	/**
	 * buffs格式化
	 */
	private static Object encodeBuffs(Collection<IBuff> buffs) {
		
		if(null != buffs && buffs.size() > 0){
			
			StringBuffer result = new StringBuffer();
			
			for(IBuff buff : buffs){
				Object format = buffFormat(buff);
				result.append(format);
				
			}
			
			return result.toString();
			
		}
		
		return "";
	}

	/**
	 * buff格式化
	 * [category,level,starttime,attackerId,additionalData]
	 */
	private static Object buffFormat(IBuff buff) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(buff.getBuffCategory()).append(",");
		buffer.append(buff.getLevel()).append(",");
		buffer.append(buff.getStartTime()).append(",");
		buffer.append(buff.getAttackerId()).append(",");
		if(null !=buff.getAdditionalData()){
			Object addData = buff.getAdditionalData();
			buffer.append(addData);
		}else{
			buffer.append(" ");
		}
		
		buffer.append(";");
		
		return buffer.toString();
	}

	/**
	 * 反解buff信息
	 */
	public static List<Object[]> decodeBuffs(String buffStr) {
		
		List<Object[]> result = null;
		
		if(null != buffStr){
			
			result = new ArrayList<Object[]>();
			
			String[] buffs = buffStr.split(";");
			for(String buffInfo : buffs){
				if(!"".equals(buffInfo.trim())){
					result.add(buffInfo.split(","));
				}
					
			}
		}
		
		return result;
	}

	
	/**
	 * 获取其他角色信息
	 * @param role
	 * @param data
	 */
	public static void putOtherRoleInfo(Role role,Object[] out,Object[] equips){
		/**
		 * 1:Array[0:int(角色配置id),
                1:Number(角色guid),
                2:String(角色名字),
                3:int(角色战斗力),
                4:Array(身上装备列表)[Map(#ItemVO),Map(#ItemVO),...Map(#ItemVO)],
                5:int(角色等级),
                6:int(vip等级),
                   7:String(帮派),
                8:String(配偶),
                9:int(气血),
                10:int(真气),
                11:int(经验),
                12:int(攻击),
                13:int(防御),
                14:int(命中),
                15:int(闪避),
                16:int(暴击),
                17:int(韧性),
                18:int(pk值),
                19:int(移动速度),
                20:int(成就点数)
                21:int(当前气血),
                22:int(御剑ID),
                23:int(翅膀ID),
                24:int(圣剑ID)
                ]
		 */
		Object data = out[1];
		Object[] dataInfo = null; 
		if(data == null){
			dataInfo = new Object[26];
			out[1] = dataInfo;
		}else{
			dataInfo = (Object[])data;
		}
		int index = 0;
		dataInfo[index++] = role.getBusinessData().getRoleConfigId();
		dataInfo[index++] = role.getId();
		dataInfo[index++] = role.getName();
		dataInfo[index++] = role.getFightAttribute().getZhanLi();
		dataInfo[index++] = equips;
		dataInfo[index++] = role.getLevel();
		dataInfo[index++] = role.getBusinessData().getVipLevel();
		dataInfo[index++] = role.getBusinessData().getGuildName();
		dataInfo[index++] = null;//TODO 配偶
		dataInfo[index++] = role.getFightAttribute().getMaxHp();
		dataInfo[index++] = role.getBusinessData().getZhenqi();
		dataInfo[index++] = role.getBusinessData().getExp();
		dataInfo[index++] = role.getFightAttribute().getAttack();
		dataInfo[index++] = role.getFightAttribute().getDefense();
		dataInfo[index++] = role.getFightAttribute().getMingZhong();
		dataInfo[index++] = role.getFightAttribute().getShanBi();
		dataInfo[index++] = role.getFightAttribute().getBaoJi();
		dataInfo[index++] = role.getFightAttribute().getKangBao();
		dataInfo[index++] = role.getBusinessData().getPkValue();
		dataInfo[index++] = role.getFightAttribute().getSpeed();
		dataInfo[index++] = role.getBusinessData().getCjValue();//TODO 成就点数
		dataInfo[index++] = role.getFightAttribute().getCurHp();
		dataInfo[index++] = role.getZuoQiShowId();
		dataInfo[index++] = role.getChiBangShowId();
		dataInfo[index++] = role.getWuQiShowId();
		dataInfo[index++] = role.getPeiou();
		
	}
}
