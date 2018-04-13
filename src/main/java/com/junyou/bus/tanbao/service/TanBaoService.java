package com.junyou.bus.tanbao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.huoyuedu.enums.ActivityEnum;
import com.junyou.bus.huoyuedu.export.HuoYueDuExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.stage.model.stage.tanbao.TanBaoManager;

/**
 * @author LiuYu
 * 2015-6-19 上午10:22:55
 */
@Service
public class TanBaoService {
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private StageControllExportService stageControllExportService;
	@Autowired 
	private HuoYueDuExportService huoYueDuExportService;
	
	public Object[] enterXianGong(Long userRoleId){
		if(!TanBaoManager.getManager().isStarting()){
			return AppErrorCode.ACTIVE_IS_NOT_START;//活动尚未开启
		}
		if(stageControllExportService.inFuben(userRoleId)){
			//在副本中
			return AppErrorCode.FUBEN_IS_IN_FUBEN;
		}
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(role == null){
			//角色不存在
			return AppErrorCode.ROLE_IS_NOT_EXIST;
		}
		if(role.getLevel() < TanBaoManager.getManager().getLevel()){
			//等级不足
			return AppErrorCode.ROLE_LEVEL_NOT_ENOUGH;
		}
		//初始化信息
		TanBaoManager.getManager().checkRoleVo(role);
		//发送到场景进入地图
		DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(TanBaoManager.getManager().getMapId());
		int[] birthXy = dituCoinfig.getRandomBirth();
		Object[] applyEnterData = new Object[]{dituCoinfig.getId(),birthXy[0],birthXy[1], MapType.XIANGONG_TANBAO_MAP};
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
		//活跃度lxn
		huoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A22);
		return null;
	}
}
