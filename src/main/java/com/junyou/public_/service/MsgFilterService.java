package com.junyou.public_.service;

import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.cmd.ClientCmdType;
import com.junyou.gameconfig.goods.configure.export.GongNengFenBuConfigExportService;
import com.junyou.io.modelfilter.MsgFilterManage;
import com.junyou.public_.tunnel.PublicMsgSender;

/**
 * 模块动态开关
 * @author wind
 */
@Service
public class MsgFilterService  {

	@Autowired
	private GongNengFenBuConfigExportService gongNengFenBuConfigExportService;
	
	/**
	 * 动态关闭模块
	 * @param modelIdcodes   key:modelId   value:msgCodes
	 * @return
	 */ 
	public boolean closeModelMsgCode(String modName) {
		if(modName == null){
			return false;
		}
		
		Integer modId = gongNengFenBuConfigExportService.getModIdByModName(modName);
		
		//功能临时关闭
		MsgFilterManage.add(modName,modId); 
		if(modId != null){
			//广播通知玩家
			PublicMsgSender.send2All(ClientCmdType.MODEL_CLOSE, modId);
		}
		return true;
	}
	
	/**
	 * 动态关闭指令
	 * @param cmd
	 * @return
	 */
	public boolean closeCmdMsgCode(Short cmd){
		if(cmd == null || cmd < 0){
			return false;
		}
		
		MsgFilterManage.addCmd(cmd);
		return true;
	}
	
	/**
	 * 动态打开指令
	 * @param cmd
	 * @return
	 */
	public boolean openCmdMsgCode(Short cmd){
		if(cmd == null || cmd < 0){
			return false;
		}
		
		MsgFilterManage.removeCmd(cmd);
		return true;
	}

	/**
	 * 动态开启已经关闭模块（modelId）
	 * @param msgCodes
	 * @return
	 */  
	public boolean openModelMsgCode(String modName) {
		if(modName == null){
			return false;
		}
		
		//临时关闭的模块正常开启
		Integer modId = MsgFilterManage.removeMod(modName);
		if(modId == null){
			return false;
		}
		//广播通知玩家
		PublicMsgSender.send2All(ClientCmdType.MODEL_OPEN, modId);
		return true;
	}

	/**
	 * 查看被关闭的所有模块信息
	 * @return
	 */ 
	public Set<String> queryCloseModelInfos(){
		return MsgFilterManage.getCloseModelInfos(); 
	}
	
	/**
	 * 查看被关闭的所有指令信息
	 * @return
	 */ 
	public Set<Short> getCloseCmdInfos(){
		return MsgFilterManage.getCloseCmdInfos();
	}
	
	/**
	 * 获取已关闭的模块功能分布表id
	 * @return
	 */
	public Collection<Integer> getCloseModelClientIds(){
		return MsgFilterManage.getCloseModelClientIds();
	}
}
