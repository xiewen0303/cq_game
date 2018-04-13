package com.junyou.bus.kuafu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.kuafu.manager.KuafuSessionManager;
import com.junyou.kuafu.share.util.KuafuConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.kernel.spring.container.DataContainer;

@Service
public class KuafuBusService {
	
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	
	public void enterKuafu(Long roleId,Object data){
		ChuanQiLog.deployInfo("roleId->"+roleId+" 成功进入了跨服!");
		
		dataContainer.putData(GameConstants.COMPONENET_KUAFU_DATA, roleId+"", data);
		
		//获取活动id
//		Object[] kuafuData = (Object[]) data;
//		Integer activeId = (Integer) kuafuData[1];
		
		//TODO 获取活动配置，人物进入活动地图
		int mapId = 1001;
		
		DiTuConfig diTuConfig = diTuConfigExportService.loadDiTu(mapId);
		if(diTuConfig == null){
			ChuanQiLog.error(" enterKuafuHd diTuConfig is error mapId:"+mapId);
			return;
		}
		
		int[] birthXy = diTuConfig.getRandomBirth();
		
		BusMsgSender.send2BusInner(roleId, InnerCmdType.INNER_SC_KUAFU_COPY, new Object[]{mapId,birthXy[0],birthXy[1],MapType.KUAFU_FUBEN_MAP});
	}
	
	public void leaveKuafu(Long roleId){
		ChuanQiLog.info("roleId->"+roleId+" 成功退出了跨服!");
		//删除角色会话
		KuafuSessionManager.removeUserRoleId(roleId);
		
		//移除跨服场景中的玩家
		BusMsgSender.send2BusInner(roleId, InnerCmdType.KUAFU_OFFLINE1, null);
	}
	
	/**
	 * 原服接收到跨服通知活动开启
	 * @param activeId	已开启的活动id
	 */
	public void kfActiveStart(Integer activeId){
		KuafuConfigUtil.kfActiveStart(activeId);
	}
	
	/**
	 * 原服接收到跨服通知活动关闭
	 * @param activeId	已关闭的活动id（暂时无用）
	 */
	public void kfActiveEnd(Integer activeId){
		KuafuConfigUtil.kfActiveEnd();
	}
}
