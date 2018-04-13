package com.junyou.bus.active.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.active.service.DingShiActiveService;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.DINGSHI_ACTIVE_MODULE)
public class DingShiActiveAction {

	@Autowired
	private DingShiActiveService dingShiActiveService;
	
	@EasyMapping(mapping = InnerCmdType.DINGSHI_DAZUO_START)
	public void dazuoActiveStart(Message inMsg) {
		Integer id = inMsg.getData();
		dingShiActiveService.dazuoActiveStart(id);
	}
	
	@EasyMapping(mapping = InnerCmdType.DINGSHI_DAZUO_STOP)
	public void dazuoActiveStop(Message inMsg) {
		dingShiActiveService.dazuoActiveStop();
	}
	
	@EasyMapping(mapping = InnerCmdType.DINGSHI_KILL_MONSTER_START)
	public void killMonsterActiveStart(Message inMsg) {
		Integer id = inMsg.getData();
		dingShiActiveService.killMonsterActiveStart(id);
	}
	
	@EasyMapping(mapping = InnerCmdType.DINGSHI_KILL_MONSTER_STOP)
	public void killMonsterActiveStop(Message inMsg) {
		dingShiActiveService.killMonsterActiveStop();
	}
	@EasyMapping(mapping = InnerCmdType.DINGSHI_YABIAO_MONSTER_START)
	public void yaBiaoActiveStart(Message inMsg) {
		Integer id = inMsg.getData();
		dingShiActiveService.yaBiaoActiveStart(id);
	}
	
	@EasyMapping(mapping = InnerCmdType.DINGSHI_YABIAO_MONSTER_STOP)
	public void yaBiaoActiveStop(Message inMsg) {
		dingShiActiveService.yaBiaoActiveStop();
	}
	
	@EasyMapping(mapping = InnerCmdType.DINGSHI_COLLECT_BOX_START)
	public void collectBoxActivityStart(Message inMsg) {
		Integer id = inMsg.getData();
		dingShiActiveService.collectBoxActivityStart(id);
	}
	
	@EasyMapping(mapping = InnerCmdType.DINGSHI_COLLECT_BOX_STOP)
	public void collectBoxActivityStop(Message inMsg) {
		dingShiActiveService.collectBoxActivityEnd();
	}
	
	@EasyMapping(mapping = InnerCmdType.DINGSHI_XIANGONG_START)
	public void xianGongActiveStart(Message inMsg) {
		Integer id = inMsg.getData();
		dingShiActiveService.xianGongActiveStart(id);
	}
	
	@EasyMapping(mapping = InnerCmdType.DINGSHI_XIANGONG_STOP)
	public void xianGongActiveStop(Message inMsg) {
		dingShiActiveService.xianGongActiveStop();
	}
	
	@EasyMapping(mapping = InnerCmdType.DINGSHI_CAMP_WAR_START)
	public void campWarStart(Message inMsg) {
		Object[] data = inMsg.getData();
		Integer hdId = (Integer) data[0];
		String stageId = (String) data[1];
		dingShiActiveService.campWarActivityStart(stageId,hdId);
	}
	@EasyMapping(mapping = InnerCmdType.DINGSHI_TERRITORY_START)
	public void terrirotyStart(Message inMsg) {
		Integer hdId = inMsg.getData();
		dingShiActiveService.territoryActivityStart(hdId);
	}
	
	@EasyMapping(mapping = InnerCmdType.DINGSHI_WENQUAN_START)
	public void wenquanStart(Message inMsg) {
		Integer hdId = inMsg.getData();
		dingShiActiveService.wenquanActivityStart(hdId);
	}
	@EasyMapping(mapping = InnerCmdType.DINGSHI_ZHENGBASAI_START)
	public void hcZhengBaSaiStart(Message inMsg) {
		Integer hdId = inMsg.getData();
		dingShiActiveService.zhengbasaiActivityStart(hdId);
	}
	
//	@EasyMapping(mapping = InnerCmdType.DINGSHI_BOSS_START)
//	public void dingShiBossStart(Message inMsg) {
//		Integer id = inMsg.getData();
//		dingShiActiveService.bossActiveStart(id);
//	}
	
	
	@EasyMapping(mapping = InnerCmdType.DINGSHI_ACTIVE_NOTICE)
	public void dingshiActiveNotice(Message inMsg) {
		Integer activityId = inMsg.getData();
		dingShiActiveService.dingshiActiveNotice(activityId);
	}
}