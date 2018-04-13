package com.junyou.bus.role.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.chongwu.service.export.ChongwuExportService;
import com.junyou.bus.role.IncrRoleResp;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.common.ObjectUtil;

/**
 * 场景到业务
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-4-9 下午6:59:24
 */
@Service
public class Stage2BusService{
	
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private ChongwuExportService chongwuExportService;
	
	public void innerDazuoAward(int addExp, int addzhenqi,long roleId,short cmd) {
		roleBagExportService.incrNumberWithNotify(GoodsCategory.EXP, addExp, roleId, LogPrintHandle.GET_DAZUO, LogPrintHandle.GBZ_DAZUO); 
		roleBagExportService.incrNumberWithNotify(GoodsCategory.ZHENQI, addzhenqi, roleId, LogPrintHandle.GET_DAZUO, LogPrintHandle.GBZ_DAZUO);
		
		StageMsgSender.send2One(roleId, cmd, new Object[]{addExp,addzhenqi});
	}
	
	public void addExp(Long userRoleId,Long addExp,Short cmd){
		IncrRoleResp resp = roleExportService.incrExp(userRoleId, addExp); 
		if(cmd != null && resp != null && resp.getIncrExp() > 0){
			BusMsgSender.send2One(userRoleId,  cmd, addExp);
		}
	}
	public void addChongwuExp(Long userRoleId,Long addExp){
		chongwuExportService.addChongwuExp(userRoleId, addExp);
	}
	
	public void territoryAddExp_Zhenqi(Long userRoleId,Long exp,Long addZhenqi){
		Long incrExp = 0L;
		if(exp > 0){
			IncrRoleResp resp = roleExportService.incrExp(userRoleId, exp); 
			if(resp != null){
				incrExp = resp.getIncrExp();
			}
		}
		if(addZhenqi > 0){
			roleExportService.addZhenqi(userRoleId, addZhenqi);
		}
		BusMsgSender.send2One(userRoleId,ClientCmdType.TERRITORY_NOTICE_EXP_ZHENQI, new Object[]{incrExp,addZhenqi});
	}
	public void hczbsAddExp_Zhenqi(Long userRoleId,Long exp,Long addZhenqi){
		Long incrExp = 0L;
		if(exp > 0){
			IncrRoleResp resp = roleExportService.incrExp(userRoleId, exp); 
			if(resp != null){
				incrExp = resp.getIncrExp();
			}
		}
		if(addZhenqi > 0){
			roleExportService.addZhenqi(userRoleId, addZhenqi);
		}
		BusMsgSender.send2One(userRoleId,ClientCmdType.HC_ZBS_ADD_ZHENQI, new Object[]{incrExp,addZhenqi});
	}
	
    public void kuafuYunGongAddExpZq(Long userRoleId, Long exp, Long addZhenqi) {
        Long incrExp = 0L;
        if (exp > 0) {
            IncrRoleResp resp = roleExportService.incrExp(userRoleId, exp);
            if (resp != null) {
                incrExp = resp.getIncrExp();
            }
        }
        if (addZhenqi > 0) {
            roleExportService.addZhenqi(userRoleId, addZhenqi);
        }
        BusMsgSender.send2One(userRoleId, ClientCmdType.KF_YUNGONG_ADD_EXP_ZQ, new Object[] { incrExp, addZhenqi });
    }
	
	public void clearNuqi(Long userRoleId){
		String stageId = publicRoleStateExportService.getRolePublicStageId(userRoleId);
		if(ObjectUtil.strIsEmpty(stageId)){
			return;
		}
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		role.setNuqi(0);
	}
	
}