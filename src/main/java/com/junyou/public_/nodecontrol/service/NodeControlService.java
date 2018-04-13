package com.junyou.public_.nodecontrol.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.client.io.export.ClientIoExportService;
import com.junyou.constants.GameConstants;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.kernel.spring.container.DataContainer;

@Service
public class NodeControlService {
	
	private boolean usecache = true;

	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private ClientIoExportService clientIoExportService;
	@Autowired
	private DataContainer dataContainer;
	
//	@Autowired
//	@Qualifier("publicCacheManager")
//	private CacheManager publicCacheManager;
	
	/**
	 * 设置一个角色为在线状态
	 * @param roleid
	 */
	public void change2online(Long roleid) {
		publicRoleStateExportService.change2PublicOnline(roleid);
	}

	/**
	 * 设置一个角色为离线状态
	 * @param roleid
	 */
	public void change2offline(Long roleid) {
		publicRoleStateExportService.change2PublicOffline(roleid);
	}

	
	/**
	 * 记录微端登录状态
	 * @param userRoleId
	 */
	public void addRoleWeiDuan(Long userRoleId){
		dataContainer.putData(GameConstants.WEI_DUAN_VALUE, userRoleId.toString(), true);
	}
	
	/**
	 * 下线处理微端登录
	 * @param userId
	 */
	private void removeRoleWeiDuan(Long userRoleId){
		try {
			dataContainer.removeData(GameConstants.WEI_DUAN_VALUE, userRoleId.toString());
		} catch (Exception e) {
			ChuanQiLog.error("removeRoleWeiDuan error !");
		}
	}
	/**
	 * 获取微端在线总数
	 * @return
	 */
	public int getWeiDuanCount(){
		return dataContainer.getComponentDataSize(GameConstants.WEI_DUAN_VALUE);
	}
	
	/**
	 * 是否是微端登录
	 * @param userRoleId
	 * @return true:是
	 */
	public boolean isWeiDuanLogin(Long userRoleId){
		Boolean isWeiDuan = dataContainer.getData(GameConstants.WEI_DUAN_VALUE, userRoleId.toString());
		if(isWeiDuan == null){
			return false;
		}
		
		return isWeiDuan;
	}
	
	/**
	 * 处理角色节点上线业务
	 * @param roleid
	 */
	public void nodeLogin(Long roleid,String ip) {
		
		// TODO 获取角色节点信息
//		RoleStageWrapper wrapper = stageExternalExportService.getRoleStageFromDb(roleid); 
//		String mapId = wrapper.getMapId();
		
//		NodeConfig nodeConfig = nodeConfigExportService.getNodeConfigByStageid(mapId);
//		if(nodeConfig == null){
//			DiTuConfig diTuConfig = diTuConfigExportService.loadFirstMainDiTu();
//			//异常找不到地图
//			nodeConfig = nodeConfigExportService.getNodeConfigByStageid(diTuConfig.getId());
//		}
		
//		String nodename = nodeConfig.getName();
//		// 在node swap 中注册node信息
//		nodeSwapExportService.regNodeInfo(roleid, nodename);
		
		// 通知目标节点上线准备
//		if(usecache){//Liuyu：取消公共缓存与角色的关联2015-04-20
//			publicCacheManager.activateRoleCache(roleid);
//		}
		
		clientIoExportService.roleIn(roleid,ip);
		
//		dataContainer.putData(GameModType.NODE_CONTROL, roleid.toString(), nodename);
		
	}


	public void nodeExit(Long roleid) {

		try{

			nodeExitHandle(roleid);
			
		}catch (Exception e) {
			ChuanQiLog.error("node exit error",e);
		}finally{
			
			// 通知节点下线
//			if(usecache){//Liuyu：取消公共缓存与角色的关联2015-04-20
//				try{
//					publicCacheManager.freezeRoleCache(roleid);
//				}catch (Exception e) {
//					ChuanQiLog.error("",e);
//				}
//				
//			}
			try{
				clientIoExportService.roleOut(roleid);
			}catch (Exception e) {
				ChuanQiLog.error("",e);
			}
			
			// 在node swap 中注销node信息
//			nodeSwapExportService.unregNodeInfo(roleid);
		}
		
		
		

	}

	public void nodeExitHandle(Long roleid){
		//微端登录
		removeRoleWeiDuan(roleid);

	}
	
	/**
	 * 服务器关闭时处理角色下线业务
	 * @param roleid
	 */
	public void nodeExitOnServerClose(Long roleid) {
		nodeExitHandle(roleid);
	}

	
}
