package com.junyou.stage.easyaction;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.bag.ContainerType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.stage.service.RoleBehaviourService;
import com.kernel.pool.executor.Message;

/**
 * @description 业务场景交互指令
 * @author ShiJie Chi
 * @date 2012-4-9 上午11:04:08 
 */
@Controller
@EasyWorker(groupName = EasyGroup.STAGE)
public class Bus2StageAction {
	
	@Autowired
	private RoleBehaviourService roleBehaviourService;

	
	/**
	 * 换装
	 * @param [0:equip[id,goodsId,map,jingYouLevel,QiangHuaLevel,xiLiang],1:weapon[]]
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_CHANGE_EQUIP,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void changeEquip(Message inMsg){
		
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		
		Object[] equipInfos = (Object[])data[0];
		Object[] goodAndSort = (Object[])data[1];
		
		
		roleBehaviourService.changeEquip(stageId,userRoleId,equipInfos,goodAndSort);
	}
	/**
	 * 换装
	 * @param [0:equip[id,goodsId,map,jingYouLevel,QiangHuaLevel,xiLiang],1:weapon[]]
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_CHANGE_ZUOQI_EQUIP,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void changeZuoqiEquip(Message inMsg){
		
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		Object[] equipInfos = inMsg.getData();
		
		
		roleBehaviourService.changeOtherEquip(stageId,userRoleId,equipInfos,ContainerType.ZUOQIITEM);
	}
	/**
	 * 换装
	 * @param [0:equip[id,goodsId,map,jingYouLevel,QiangHuaLevel,xiLiang],1:weapon[]]
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_CHANGE_CHIBANG_EQUIP,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void changeChibangEquip(Message inMsg){
		
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		Object[] equipInfos = inMsg.getData();
		
		
		roleBehaviourService.changeOtherEquip(stageId,userRoleId,equipInfos,ContainerType.CHIBANGITEM);
	}
	
	/**
	 * 换装
	 * @param [0:equip[id,goodsId,map,jingYouLevel,QiangHuaLevel,xiLiang],1:weapon[]]
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_CHANGE_WUQI_EQUIP,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void changeWuQiEquip(Message inMsg){
		
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		Object[] equipInfos = inMsg.getData();
		
		
		roleBehaviourService.changeOtherEquip(stageId,userRoleId,equipInfos,ContainerType.WUQI);
	}
	
	/**
	 * 换装
	 * @param [0:equip[id,goodsId,map,jingYouLevel,QiangHuaLevel,xiLiang],1:weapon[]]
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_CHANGE_QILING_EQUIP,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void changeQiLingEquip(Message inMsg){
		
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		Object[] equipInfos = inMsg.getData();
		roleBehaviourService.changeOtherEquip(stageId,userRoleId,equipInfos,ContainerType.QILINGITEM);
	}
	/**
	 * 换装
	 * @param [0:equip[id,goodsId,map,jingYouLevel,QiangHuaLevel,xiLiang],1:weapon[]]
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_CHANGE_TIANYU_EQUIP,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void changeTianYuEquip(Message inMsg){
		
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		Object[] equipInfos = inMsg.getData();
		roleBehaviourService.changeOtherEquip(stageId,userRoleId,equipInfos,ContainerType.TIANYUITEM);
	}
	
	/**
	 * 换装
	 * @param [0:equip[id,goodsId,map,jingYouLevel,QiangHuaLevel,xiLiang],1:weapon[]]
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_CHANGE_TANGBAO_EQUIP,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void changeTangBaoEquip(Message inMsg){
		
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		Object[] equipInfos = inMsg.getData();
		
		
		roleBehaviourService.changeOtherEquip(stageId,userRoleId,equipInfos,ContainerType.TANGBAOITEM);
	}
	/**
	 * 换装
	 * @param [0:equip[id,goodsId,map,jingYouLevel,QiangHuaLevel,xiLiang],1:weapon[]]
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_CHANGE_TIANGONG_EQUIP,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void changeTianGongEquip(Message inMsg){
		
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		Object[] equipInfos = inMsg.getData();
		
		
		roleBehaviourService.changeOtherEquip(stageId,userRoleId,equipInfos,ContainerType.TIANGONGITEM);
	}
	/**
	 * 换装
	 * @param [0:equip[id,goodsId,map,jingYouLevel,QiangHuaLevel,xiLiang],1:weapon[]]
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_CHANGE_TIANSHANG_EQUIP,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void changeTianShangEquip(Message inMsg){
		
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		Object[] equipInfos = inMsg.getData();
		
		
		roleBehaviourService.changeOtherEquip(stageId,userRoleId,equipInfos,ContainerType.TIANSHANGITEM);
	}
	
	/**
     * 宠物换装
     * @param [0:equip[id,goodsId,map,jingYouLevel,QiangHuaLevel,xiLiang],1:weapon[]]
     */
    @EasyMapping(mapping = InnerCmdType.INNER_CHANGE_CHONGWU_EQUIP,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
    public void changeChongwuEquip(Message inMsg){
        String stageId = inMsg.getStageId();
        Long userRoleId = inMsg.getRoleId();
        Object[] equipInfos = inMsg.getData();
        roleBehaviourService.changeOtherEquip(stageId,userRoleId,equipInfos,ContainerType.CHONGWUITEM);
    }
    
    /**
     * 神器换装
     * @param [0:equip[id,goodsId,map,jingYouLevel,QiangHuaLevel,xiLiang],1:weapon[]]
     */
    @EasyMapping(mapping = InnerCmdType.INNER_CHANGE_SHENQI_EQUIP,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
    public void changeShenQiEquip(Message inMsg){
        String stageId = inMsg.getStageId();
        Long userRoleId = inMsg.getRoleId();
        Object[] equipInfos = inMsg.getData();
        roleBehaviourService.changeOtherEquip(stageId,userRoleId,equipInfos,ContainerType.SHENQIITEM);
    }
    
	
	@EasyMapping(mapping = InnerCmdType.LEVEL_UPGRADE , kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void upgrade(Message inMsg){
		
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Integer level = inMsg.getData();
		roleBehaviourService.upgrade(stageId, userRoleId,level);
	}
	
	@EasyMapping(mapping = InnerCmdType.VIP_LEVEL_UPGRADE , kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void vipUpgrade(Message inMsg){
		
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Integer level = inMsg.getData();
		roleBehaviourService.vipUpgrade(stageId, userRoleId,level);
	}
	
	
	@EasyMapping(mapping = InnerCmdType.INNER_SLOT_ATTRS , kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void innerSlotAttrs(Message inMsg){
		
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		
		Map<String,Long> attrs = inMsg.getData();
		
		roleBehaviourService.innerSlotAttrs(stageId, userRoleId,attrs);
	}
	
	@EasyMapping(mapping = InnerCmdType.PET_OWNER_ATTRIBUTE_CHANGE , kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void petOwnerAttChange(Message inMsg){
		
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		
		Map<String,Long> attrs = inMsg.getData();
		
		roleBehaviourService.petOwnerAttChange(stageId, userRoleId,attrs);
	}
	
	@EasyMapping(mapping = InnerCmdType.PET_BASE_ATTRIBUTE_CHANGE , kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void petBaseAttrChange(Message inMsg){
		
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		
		Map<String,Long> attrs = inMsg.getData();
		
		roleBehaviourService.petBaseAttrChange(stageId, userRoleId,attrs);
	}
	
	@EasyMapping(mapping = InnerCmdType.PK_HUI, kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void pkHuiTime(Message inMsg){
		String stageId = inMsg.getStageId();
		Long roleId = inMsg.getRoleId();
		
		Integer huiminTime = inMsg.getData();
		
		roleBehaviourService.roleHuiminValueChange(stageId, roleId, huiminTime);
	}
	
	@EasyMapping(mapping = InnerCmdType.ADD_PK_CHANGE, kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void pkChange(Message inMsg){
		String stageId = inMsg.getStageId();
		Long roleId = inMsg.getRoleId();
		
		Integer pkValue = inMsg.getData();
		
		roleBehaviourService.rolePkValueChange(stageId, roleId, pkValue);
	}
	
	/**
	 * 角色转职
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_ROLE_CHANGE_JOB, kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void roleJobChange(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer changeConfigId = inMsg.getData();
		
		roleBehaviourService.roleJobChange(userRoleId, changeConfigId);
	}
}
