package com.junyou.io.easyaction;

import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.cmd.InnerCmdType;
import com.junyou.configure.ConfigDownloadManager;
import com.junyou.constants.GameConstants;
import com.junyou.io.Message.IoMessage;
import com.junyou.log.ChuanQiLog;
import com.junyou.module.GameModType;
import com.junyou.stop.ChuanQiStopHelper;
import com.junyou.utils.ServerProtect;
import com.junyou.utils.md5.Md5Utils;


@Controller
@EasyWorker(moduleName = GameModType.TSSERVER_MODULE)
public class ServerManageAction {

	@EasyMapping(mapping = InnerCmdType.INNER_TS_SERVER)
	public void serverManage(IoMessage message){
		Object[] data = message.getData();
		if(data == null || data.length != 2){
			ChuanQiLog.error("=======Warning: server is attacked!");
			return;
		}
		
		String d = (String)data[0];
		String s = (String)data[1];
		
		if(check(d, s)){
			serverState(d);
		}else{
			ChuanQiLog.error("=======Warning: server is attacked!");
		}
	}
	
	private void serverState(String data){
		try {
			
			if(data != null){
				
				if(ServerProtect.isCanChange(data)){
					ServerProtect.changeState();
				}else if(ServerProtect.isCanDone(data)){
					ConfigDownloadManager.getInstance().writeBootFile();
					ChuanQiStopHelper.stop(" java protect done server! ");
				}
			}
			
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
	
	
	private boolean check(String data,String sign){
		if(data == null || "".equals(data)){
			return false;
		}
		
		StringBuffer souStr = new StringBuffer();
		souStr.append("data=").append(data).append(GameConstants.SERVER_MANAGE_KEY);
		String md5Value = Md5Utils.md5To16(souStr.toString());
		
		return md5Value.equals(sign);
	}
}
