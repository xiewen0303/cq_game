package com.junyou.public_.rank.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.junyou.public_.rank.service.RankService;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.RANK_MODULE,groupName = EasyGroup.PUBLIC)
public class RankAction {
	
	@Autowired
	private RankService rankService;
	
	
	
	/**
	 * 拉取魁首榜信息
	 */
	@EasyMapping(mapping = ClientCmdType.KUISHOU_INFO)
	public void getKuiShouRankInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] types = inMsg.getData();
		
		Object[] reuslt = rankService.getKuiShouRankInfo(types);
		BusMsgSender.send2One(userRoleId, ClientCmdType.KUISHOU_INFO, reuslt);
	}
	/**
	 * 拉取排行榜信息
	 */
	@EasyMapping(mapping = ClientCmdType.RANK_INFO)
	public void getRankInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Integer type = inMsg.getData();
		
		Object[] reuslt = rankService.getRankInfo(userRoleId,type);
		BusMsgSender.send2One(userRoleId, ClientCmdType.RANK_INFO, reuslt);
	}
	
	/**
	 * 拉取等级排行榜信息
	 */
	@EasyMapping(mapping = ClientCmdType.LEVEL_RANK)
	public void getLevelRankInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] data = inMsg.getData();

		int startIndex = (Integer) data[0];
		int needCount = (Integer) data[1];
		
		Object[] reuslt = rankService.getLevelRankInfo(startIndex, needCount);
		BusMsgSender.send2One(userRoleId, ClientCmdType.LEVEL_RANK, reuslt);
	}
	
	
	/**
	 * 拉取战力排行榜信息
	 */
	@EasyMapping(mapping = ClientCmdType.FIGHTING_RANK)
	public void getZhanLiRankInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] data = inMsg.getData();
		
		int startIndex = (Integer) data[0];
		int needCount = (Integer) data[1];
		
		Object[] reuslt = rankService.getZhanLiRankInfo(startIndex, needCount);
		BusMsgSender.send2One(userRoleId, ClientCmdType.FIGHTING_RANK, reuslt);
	}
	
	/**
	 * 拉取坐骑排行榜信息
	 */
	@EasyMapping(mapping = ClientCmdType.ZUOQI_RANK)
	public void getZuoQiRankInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] data = inMsg.getData();
		
		int startIndex = (Integer) data[0];
		int needCount = (Integer) data[1];
		
		Object[] reuslt = rankService.getZuoQiRankInfo(startIndex, needCount);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ZUOQI_RANK, reuslt);
	}
	/**
	 * 拉取翅膀排行榜信息
	 */
	@EasyMapping(mapping = ClientCmdType.CHIBANG_RANK)
	public void getChiBangRankInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] data = inMsg.getData();
		
		int startIndex = (Integer) data[0];
		int needCount = (Integer) data[1];
		
		Object[] reuslt = rankService.getChiBangRankInfo(startIndex, needCount);
		BusMsgSender.send2One(userRoleId, ClientCmdType.CHIBANG_RANK, reuslt);
	}
	/**
	 * 拉取仙剑排行榜信息
	 */
	@EasyMapping(mapping = ClientCmdType.XIANJIAN_RANK)
	public void getXianJianRankInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] data = inMsg.getData();
		
		int startIndex = (Integer) data[0];
		int needCount = (Integer) data[1];
		
		Object[] reuslt = rankService.getXianJiaRankInfo(startIndex, needCount);
		BusMsgSender.send2One(userRoleId, ClientCmdType.XIANJIAN_RANK, reuslt);
	}
	/**
	 * 拉取战甲排行榜信息
	 */
	@EasyMapping(mapping = ClientCmdType.ZHANJIA_RANK)
	public void getZhanJiaRankInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] data = inMsg.getData();
		
		int startIndex = (Integer) data[0];
		int needCount = (Integer) data[1];
		
		Object[] reuslt = rankService.getZhanJiaRankInfo(startIndex, needCount);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ZHANJIA_RANK, reuslt);
	}
	
	/**
	 * 拉取妖神排行榜信息
	 */
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_RANK)
	public void getYaoShenRankInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] data = inMsg.getData();
		
		int startIndex = (Integer) data[0];
		int needCount = (Integer) data[1];
		
		Object[] reuslt = rankService.getYaoShenRankInfo(startIndex, needCount);
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_RANK, reuslt);
	}
	/**
	 * 拉取妖神魔纹排行榜信息
	 */
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_MOWEN_RANK)
	public void getYaoShenMowenRankInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] data = inMsg.getData();
		
		int startIndex = (Integer) data[0];
		int needCount = (Integer) data[1];
		
		Object[] reuslt = rankService.getYaoShenMowenRankInfo(startIndex, needCount);
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_MOWEN_RANK, reuslt);
	}
	/**
	 * 拉取妖神魔印排行榜信息
	 */
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_MOYIN_RANK)
	public void getYaoShenMoyinRankInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] data = inMsg.getData();
		
		int startIndex = (Integer) data[0];
		int needCount = (Integer) data[1];
		
		Object[] reuslt = rankService.getYaoShenMoyinRankInfo(startIndex, needCount);
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_MOYIN_RANK, reuslt);
	}
	/**
	 * 拉取妖神魂魄排行榜信息
	 */
	@EasyMapping(mapping = ClientCmdType.YAOSHEN_HUNPO_RANK)
	public void getYaoShenHunpoRankInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] data = inMsg.getData();
		
		int startIndex = (Integer) data[0];
		int needCount = (Integer) data[1];
		
		Object[] reuslt = rankService.getYaoShenHunpoRankInfo(startIndex, needCount);
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_HUNPO_RANK, reuslt);
	}
	

	/**
	 * 拉取糖宝心纹排行榜信息
	 */
	@EasyMapping(mapping = ClientCmdType.TANGBAO_XINWEN_RANK)
	public void getTangbaoXinwenRankInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] data = inMsg.getData();
		
		int startIndex = (Integer) data[0];
		int needCount = (Integer) data[1];
		Object[] reuslt = rankService.getTangbaoXinwenRankInfo(startIndex, needCount);
		BusMsgSender.send2One(userRoleId, ClientCmdType.TANGBAO_XINWEN_RANK, reuslt);
	}
	
	/**
	 * 拉取器灵排行榜信息
	 */
	@EasyMapping(mapping = ClientCmdType.QILING_RANK)
	public void getQilingRankInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] data = inMsg.getData();
		
		int startIndex = (Integer) data[0];
		int needCount = (Integer) data[1];
		
		Object[] reuslt = rankService.getQilingRankInfo(startIndex, needCount);
		BusMsgSender.send2One(userRoleId, ClientCmdType.QILING_RANK, reuslt);
	}
	
	
	/**
	 * 拉取新圣剑排行榜信息
	 */
	@EasyMapping(mapping = ClientCmdType.SHENGJIAN_RANK)
	public void getShengJianRankInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] data = inMsg.getData();
		
		int startIndex = (Integer) data[0];
		int needCount = (Integer) data[1];
		Object[] reuslt = rankService.getShengjianRankInfo(startIndex, needCount);
		BusMsgSender.send2One(userRoleId, ClientCmdType.SHENGJIAN_RANK, reuslt);
	}
	
	/**按页查询的排行*/
	@EasyMapping(mapping = 9999)//TODO cmd 暂不定
	public void showRank(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		int rankType=(Integer) data[0];
		int page=inMsg.getData();
		rankService.showRank(userRoleId,rankType, page);
	}
	
	/**按指定索引查询的排行*/
	@EasyMapping(mapping = 8888)//TODO cmd  cmd 暂不定
	public void showRank1(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		int rankType=(Integer) data[0];

		int startIndex = (Integer) data[1];
		int count = (Integer) data[2];
		rankService.showRank(rankType,startIndex,count);
	}

}
