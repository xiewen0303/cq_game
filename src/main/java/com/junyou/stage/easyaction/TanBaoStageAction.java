package com.junyou.stage.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.stage.service.TanBaoStageService;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.TANBAO_MODULE,groupName = EasyGroup.STAGE)
public class TanBaoStageAction {
	
	@Autowired
	private TanBaoStageService tanBaoStageService;
	/**
	 * 初始化活动
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.DINGSHI_XIANGONG_INIT)
	public void initActive(Message inMsg){
		Integer id = inMsg.getData();
		tanBaoStageService.activeStart(id);
	}
	/**
	 * 退出探宝
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.EXIT_TANBAO,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void exitTanBao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		tanBaoStageService.leaveTanBao(userRoleId);
	}
	
	/**
	 * 生产箱子
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.DINGSHI_TANBAO_REFRESH_BOX)
	public void createBox(Message inMsg){
		Object[] data = inMsg.getData();
		tanBaoStageService.productBox(data);
	}
	
	/**
	 * 开始开箱
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TANBAO_COLLECT_START,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void startBox(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Long guid = LongUtils.obj2long(inMsg.getData());
		Object[] result = tanBaoStageService.startBox(userRoleId, stageId, guid);
		BusMsgSender.send2One(userRoleId, ClientCmdType.TANBAO_COLLECT_START, result);
	}
	/**
	 * 开启箱子
	 * @param inMsg
	 */
	@EasyMapping(mapping =  ClientCmdType.TANBAO_COLLECT_STOP,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void openBox(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Object[] result = tanBaoStageService.openBox(userRoleId, stageId);
		if(result != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.TANBAO_COLLECT_STOP, result);
		}
	}

	/**
	 * 增加跨服积分
	 * @param inMsg
	 */
	@EasyMapping(mapping =  InnerCmdType.ADD_TANBAO_SCORE)
	public void addTanBaoScore(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer score = inMsg.getData();
		tanBaoStageService.addScore(userRoleId, score);
	}
	
	/**
	 * 总探宝经验
	 * @param inMsg
	 */
	@EasyMapping(mapping =  ClientCmdType.GET_TANBAO_EXP,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void getTanBaoExp(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		tanBaoStageService.getTanBaoExp(userRoleId);
	}
	/**
	 * 前五名
	 * @param inMsg
	 */
	@EasyMapping(mapping =  ClientCmdType.SEND_TANBAO_TOP_FIVE,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void getTopFive(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		tanBaoStageService.getTopFive(userRoleId);
	}

	/**
	 * 自己信息
	 * @param inMsg
	 */
	@EasyMapping(mapping =  ClientCmdType.SELF_TANBAO_INFO,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void getSelfInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		tanBaoStageService.getSelfInfo(userRoleId);
	}
	
	/**
	 * 获取排行信息
	 * @param inMsg
	 */
	@EasyMapping(mapping =  ClientCmdType.GET_RANK_INFO,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void getRankInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer start = (Integer)data[0];
		Integer count = (Integer)data[1];
		tanBaoStageService.getRankInfo(userRoleId,start,count);
	}
	
	/**
	 * 增加经验
	 * @param inMsg
	 */
	@EasyMapping(mapping =  InnerCmdType.DINGSHI_TANBAO_EXP_ADD)
	public void addTanBaoExp(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		tanBaoStageService.addTanBaoExp(userRoleId, stageId);
	}
	
	/**
	 * 复活点复活
	 * @param inMsg
	 */
	@EasyMapping(mapping =  ClientCmdType.FUHUO_POINT_FUHUO)
	public void pointRevive(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		tanBaoStageService.pointRevive(userRoleId, stageId);
	}
	
}
