package com.junyou.bus.active.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.huoyuedu.enums.ActivityEnum;
import com.junyou.bus.huoyuedu.export.HuoYueDuExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfig;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfigExportService;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.kernel.spring.container.DataContainer;

/**
 * 阵营战Service
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-4-8 下午3:17:12 
 */
@Service
public class CampWarService {
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private StageControllExportService stageControllExportService;
	@Autowired
	private DingShiActiveConfigExportService dingShiActiveConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired 
	private HuoYueDuExportService huoYueDuExportService;
	
	/**
	 *进入 阵营战
	 * @param userRoleId
	 */
	public Object[] enterCampWar(Long userRoleId){
		//判断是否有活动
		Integer hdId = dataContainer.getData(GameConstants.COMPONENT_CAMP_WAR, GameConstants.COMPONENT_CAMP_WAR);
		if(hdId == null){
			return AppErrorCode.CAMP_WAR_NO_START;
		}
		
		//判断是否有配置
		DingShiActiveConfig config = dingShiActiveConfigExportService.getConfig(hdId);
		if(config == null || config.getMapId() == 0){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		//判断等级是否满足
		if(config.getMinLevel() > roleExportService.getUserRole(userRoleId).getLevel()){
			return AppErrorCode.ROLE_LEVEL_ERROR;
		}
		
		//判断是否在副本中
		if(stageControllExportService.inFuben(userRoleId)){
			return AppErrorCode.FUBEN_IS_IN_FUBEN;
		}
		
		DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(config.getMapId());
		if(dituCoinfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//设置状态
		dataContainer.putData(GameConstants.COMPONENT_CAMP, userRoleId.toString(), userRoleId);
		//发送到场景进入地图
		int birthPoint[] = dituCoinfig.getRandomBirth();
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, new Object[]{dituCoinfig.getId(),birthPoint[0],birthPoint[1],MapType.CAMP_WAR_MAP});
		//活跃度lxn
//		huoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A23);
		return null;
	}
	
	/**
	 * 离开阵营战
	 * @param userRoleId
	 */
	public Object[] levelCampWar(Long userRoleId){
		//判断是否有活动
		Integer hdId = dataContainer.getData(GameConstants.COMPONENT_CAMP_WAR, GameConstants.COMPONENT_CAMP_WAR);
		if(hdId == null){
			return AppErrorCode.CAMP_WAR_NO_START;
		}
		
		//判断是否有配置
		DingShiActiveConfig config = dingShiActiveConfigExportService.getConfig(hdId);
		if(config == null || config.getMapId() == 0){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		//判断是否在副本中
		if(!stageControllExportService.inFuben(userRoleId)){
			return AppErrorCode.FUBEN_NOT_IN_FUBEN;
		}
		
		//通知场景离开战场
		BusMsgSender.send2BusInit(userRoleId, InnerCmdType.S_APPLY_LEAVE_STAGE, null);
		return AppErrorCode.OK;
	}
	
}
