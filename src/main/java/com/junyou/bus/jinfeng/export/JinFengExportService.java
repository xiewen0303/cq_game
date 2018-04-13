package com.junyou.bus.jinfeng.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.jinfeng.service.FengHaoService;
import com.junyou.bus.jinfeng.service.FengIpService;
import com.junyou.bus.jinfeng.service.JinYanService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.constants.GameConstants;

@Component
public class JinFengExportService{
	
	@Autowired
	private JinYanService jinYanService;
	@Autowired
	private FengHaoService fengHaoService;
	@Autowired
	private FengIpService fengIpService;

	public void onlineHandle(Long roleId) {
		jinYanService.onlineHandle(roleId);
	}
	
	public void offlineHandle(Long roleId) {
		jinYanService.offlineHandle(roleId);
	}

	public String shutUp(Long userRoleId,int keepTime) {
		BusMsgQueue msgQueue = new BusMsgQueue();
		String result = jinYanService.shutUp(userRoleId, msgQueue,keepTime);
		msgQueue.flush();
		return result;
	}

	public String removeShutUp(Long userRoleId) {
		BusMsgQueue msgQueue = new BusMsgQueue();
		String result = jinYanService.removeShutUp(userRoleId, msgQueue);
		msgQueue.flush();
		return result;
	}

	public String freeze(String userId, String serverId,int sort,String reasons,int keepTime) {
		String result = fengHaoService.freeze(userId, serverId,sort,reasons,keepTime);
		return result;
	}

	public String removeFreeze(Long roleId) {
		return fengHaoService.removeFreeze(roleId);
	}

	public boolean isShutUp(Long roleId) {
		return jinYanService.isJinYan(roleId);
	}
	
	/**
	 * 获取客户端需要的禁言数据
	 * @param roleId
	 * @return
	 */
	public Object getJinYanData(Long roleId){
		return jinYanService.getJinYanData(roleId);
	}
	
	public boolean isFengHao(Long userRoleId) {
		return fengHaoService.isFengHao(userRoleId);
	}

	public boolean isFengIp(String ip){
		return fengIpService.isFengIp(ip);
	}
	
	public String httpJinfeng(Long userRoleId,int type,int keepTime,int sort,String reasons){
		if(type == GameConstants.JINFENG_TYPE_JIN){
			return shutUp(userRoleId, keepTime);
		}else if(type == GameConstants.JINFENG_TYPE_JIEJIN){
			return removeShutUp(userRoleId);
		}else if(type == GameConstants.JINFENG_TYPE_FENG){
			return fengHaoService.freeze(userRoleId, sort, reasons, keepTime);
		}else if(type == GameConstants.JINFENG_TYPE_JIEFENG){
			return removeFreeze(userRoleId);
		}else if(type == GameConstants.JINFENG_TYPE_FENGIP){
			return fengIpService.fengIp(userRoleId, keepTime,reasons);
		}
		return null;
	}
	
	public void httpJieIp(String ip){
		fengIpService.removeFengIpByIp(ip);
	}
}
