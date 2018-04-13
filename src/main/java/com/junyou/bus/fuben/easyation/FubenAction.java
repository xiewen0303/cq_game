package com.junyou.bus.fuben.easyation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.fuben.service.FubenService;
import com.junyou.bus.fuben.service.PataService;
import com.junyou.bus.fuben.service.WuxingFubenService;
import com.junyou.bus.fuben.service.WuxingShilianFubenService;
import com.junyou.bus.fuben.service.WuxingSkillFubenService;
import com.junyou.bus.fuben.service.XinmoDouchangFubenService;
import com.junyou.bus.fuben.service.XinmoFubenService;
import com.junyou.bus.fuben.service.XinmoShenyuanFubenService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.module.GameModType;
import com.junyou.stage.model.core.stage.DeadDisplay;
import com.junyou.utils.parameter.RequestParameterUtil;
import com.kernel.pool.executor.Message;
import com.kernel.token.annotation.TokenCheck;

/**
 * 副本Action
 * @author LiuYu
 * @date 2015-3-13 下午3:59:39
 */
@Controller
@EasyWorker(moduleName = GameModType.FUBEN_MODULE)
public class FubenAction {
	@Autowired
	private FubenService fubenService;
	@Autowired
	private PataService pataService;
	@Autowired
	private WuxingFubenService wxFubenService;
	@Autowired
	private WuxingSkillFubenService wxSkillFubenService;
	@Autowired
	private WuxingShilianFubenService wxShilianFubenService;
	@Autowired
    private XinmoFubenService xmFubenService;
    @Autowired
    private XinmoShenyuanFubenService xmShenyuanFubenService;
    @Autowired
    private XinmoDouchangFubenService xmDouchangFubenService;
	
	@EasyMapping(mapping = ClientCmdType.GET_FUBEN_COUNT)
	public void getFubenCount(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = fubenService.getFubenInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_FUBEN_COUNT, result);
	}
	@EasyMapping(mapping = ClientCmdType.GET_VIP_FUBEN_COUNT)
	public void getVipFubenCount(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] result = fubenService.getVipFubenInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_VIP_FUBEN_COUNT, result);
	}
	
//	@EasyMapping(mapping = ClientCmdType.ENTER_FUBEN)
//	public void enterFuben(Message inMsg){
//		Long userRoleId = inMsg.getRoleId();
//		Object[] datas = inMsg.getData();
//		int fubenId = (int)datas[0];
//		int deadType = (int)datas[1];
//		DeadDisplay dstate = DeadDisplay.EXIT; 
//		if(deadType == 1){
//			dstate =DeadDisplay.NOEXIT;
//		}
//		BusMsgQueue busMsgQueue = new BusMsgQueue();
//		
//		fubenService.enterFuben(userRoleId, fubenId,dstate, busMsgQueue);
//		busMsgQueue.flush();
//	}
	
	@EasyMapping(mapping = ClientCmdType.ENTER_FUBEN)
	public void enterFuben(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[]  datas = inMsg.getData();
		int fubenId = (int)datas[0];
		int deadType = (int)datas[1];
		DeadDisplay dstate = DeadDisplay.EXIT; 
		if(deadType == 1){
			dstate =DeadDisplay.NOEXIT;
		}
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		
		fubenService.enterFuben(userRoleId, fubenId,dstate, busMsgQueue);
		busMsgQueue.flush();
	}
	
	
	@EasyMapping(mapping = InnerCmdType.S_FUBEN_FINISH)
	public void fubenFinish(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		fubenService.finishFuben(userRoleId);
	}
	
	@EasyMapping(mapping = ClientCmdType.RECEIVE_FUBEN_REWARD)
	public void receiveReward(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = fubenService.receiveFubenReward(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.RECEIVE_FUBEN_REWARD, result);
		
//		//奖励领取成功则发送退出副本
//		if(result[0].equals(1)){
//			BusMsgSender.send2BusInner(userRoleId, ClientCmdType.LEAVE_FUBEN, null);
//		}
	}
	
	@TokenCheck(component = GameConstants.COMPONENT_FUBEN_FORCED_LEAVE)
	@EasyMapping(mapping = InnerCmdType.B_EXIT_FUBEN)
	public void sLeaveFuben(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		
		fubenService.leaveFuben(userRoleId, busMsgQueue);
		busMsgQueue.flush();
	}
	
	@EasyMapping(mapping = ClientCmdType.LEAVE_FUBEN)
	public void leaveFuben(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		
		fubenService.leaveFuben(userRoleId, busMsgQueue);
		busMsgQueue.flush();
	}
	
	@EasyMapping(mapping = ClientCmdType.LEAVE_VIP_FUBEN)
	public void leaveVipFuben(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		
		fubenService.leaveVipFuben(userRoleId, busMsgQueue);
		busMsgQueue.flush();
	}
	/**
	 * 场景退出副本通知业务层重置副本状态
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.HAS_EXIT_FUBEN)
	public void hasLeaveFuben(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		fubenService.hasLeaveFuben(userRoleId);
	}
	
	@EasyMapping(mapping = InnerCmdType.B_FINISH_SAODANG_FUBEN)
	public void finishSaodang(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		fubenService.finishSaodao(userRoleId);
	}
	
	@EasyMapping(mapping = ClientCmdType.FUBEN_SAODANG_ONCE)
	public void saodangOnce(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer fubenId = inMsg.getData();
		
		Object[] result = fubenService.saodangOnce(userRoleId, fubenId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.FUBEN_SAODANG_ONCE, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.FUBEN_SAODANG_ALL)
	public void saodangAll(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = fubenService.saodangAll(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.FUBEN_SAODANG_ALL, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.FUBEN_SHOUHU_INFO)
	public void getShouhuInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = fubenService.getShouhuInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.FUBEN_SHOUHU_INFO, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.FUBEN_SHOUHU_ENTER)
	public void enterShouhuFuben(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		fubenService.enterShouhuFuben(userRoleId,busMsgQueue);
		busMsgQueue.flush();
	}
	
	@EasyMapping(mapping = InnerCmdType.SYN_SHOUHU_FUBEN_GUAN)
	public void synShouhuFuben(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer guan = inMsg.getData();
		fubenService.synShouhuFuben(userRoleId, guan);
	}
	
	@EasyMapping(mapping = ClientCmdType.FUBEN_SHOUHU_EXIT)
	public void shouhuExit(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		fubenService.leaveShouhuFuben(userRoleId, busMsgQueue);
		busMsgQueue.flush();
	}
	
	@EasyMapping(mapping = ClientCmdType.FUBEN_SHOUHU_RESET)
	public void resetShouhu(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = fubenService.restartShouhu(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.FUBEN_SHOUHU_RESET, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.FUBEN_SHOUHU_GIFT)
	public void getShouHuGift(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer id = inMsg.getData();
		
		Object[] result = fubenService.getTongguanGift(userRoleId, id);
		BusMsgSender.send2One(userRoleId, ClientCmdType.FUBEN_SHOUHU_GIFT, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.FUBEN_SHOUHU_SAODANG)
	public void saodangShouhuFuben(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = fubenService.saodangShouHu(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.FUBEN_SHOUHU_SAODANG, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.ENTER_VIP_FUBEN)
	public void enterVipFuben(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer fubenType = inMsg.getData();
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		
		fubenService.enterVipFuben(userRoleId, fubenType, busMsgQueue);
		busMsgQueue.flush();
	}
	
	
	@EasyMapping(mapping = ClientCmdType.SHOUHU_STATE_VALUE)
	public void getShouhuStateValue(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object result = fubenService.getShouhuStateValue(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.SHOUHU_STATE_VALUE, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.GET_PATA_INFO)
	public void getPataInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object result = pataService.getRolePataInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PATA_INFO, result);
	}
	@EasyMapping(mapping = ClientCmdType.GET_PATA_CENG_INFO)
	public void getPataCengInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer cengId = inMsg.getData();
		
		Object result = pataService.getCengInfo(userRoleId, cengId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_PATA_CENG_INFO, result);
	}
	@EasyMapping(mapping = ClientCmdType.ENTER_PATA)
	public void enterPata(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer cengId = inMsg.getData();
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		pataService.enterTiaozhan(userRoleId, cengId, busMsgQueue);
		busMsgQueue.flush();
	}
	@EasyMapping(mapping = ClientCmdType.BUY_PATA_COUNT)
	public void buyPataCount(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object result = pataService.buyCount(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.BUY_PATA_COUNT, result);
	}
	@EasyMapping(mapping = ClientCmdType.EXIT_PATA)
	public void exitPata(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		pataService.exitPata(userRoleId);
	}
	@EasyMapping(mapping = InnerCmdType.HAS_EXIT_PATA_FUBEN)
	public void hasExitPataFuben(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		pataService.hasExitPata(userRoleId);
	}
	@EasyMapping(mapping = InnerCmdType.PATA_FINISH)
	public void pataFinish(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		pataService.finishPata(userRoleId);
	}
	@EasyMapping(mapping = InnerCmdType.PATA_FAIL)
	public void pataFail(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		pataService.pataFail(userRoleId);
	}
	//获取云浮剑冢副本信息
	@EasyMapping(mapping = ClientCmdType.JIANZHONG_FUBEN_INFO)
	public void getJianzhongInfo(Message inMsg){
	   Long userRoleId = inMsg.getRoleId();
	   Object[] ret = fubenService.getJianzhongInfo(userRoleId);
	   BusMsgSender.send2One(userRoleId, ClientCmdType.JIANZHONG_FUBEN_INFO, ret);
	}
	//进入云浮剑冢副本 
	@EasyMapping(mapping = ClientCmdType.JIANZHONG_FUBEN_ENTER)
	public void enterJianzhongFuben(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		fubenService.enterJianzhongFuben(userRoleId,busMsgQueue);
		busMsgQueue.flush();
	}
	//云浮剑冢副本时间到
	@TokenCheck(component = GameConstants.COMPONENT_FUBEN_FORCED_LEAVE)
	@EasyMapping(mapping = InnerCmdType.B_EXIT_JIANZHONG_FUBEN)
	public void sLeaveJianzhongFuben(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		
		fubenService.jianzhongFubenTimeOver(userRoleId, busMsgQueue);
		busMsgQueue.flush();
	}
	//玩家主动退出云浮剑冢副本
	@EasyMapping(mapping = ClientCmdType.JIANZHONG_FUBEN_EXIT)
	public void jianzhongExit(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		fubenService.leaveJianzhongFuben(userRoleId, busMsgQueue);
		busMsgQueue.flush();
	}
	/**
	 * 场景退出副本通知业务层重置副本状态
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.HAS_EXIT_JIANZHONG_FUBEN)
	public void hasLeaveJianzhongFuben(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		fubenService.hasLeaveJianzhongFuben(userRoleId);
	}
	
    // ------------------------------五行副本客户端命令---------------------------//
    @EasyMapping(mapping = ClientCmdType.GET_WUXING_FUBEN_INFO)
    public void getWxFubenInfo(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = wxFubenService.getRoleWxFubenInfo(userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.GET_WUXING_FUBEN_INFO, result);
    }

    @EasyMapping(mapping = ClientCmdType.ENTER_WUXING_FUBEN)
    public void enterWxFuben(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Integer fubenId = inMsg.getData();
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        wxFubenService.enterWxFuben(userRoleId, fubenId, busMsgQueue);
        busMsgQueue.flush();
    }

    @EasyMapping(mapping = ClientCmdType.EXIT_WUXING_FUBEN)
    public void exitWxFuben(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        wxFubenService.exitWxFuben(userRoleId, busMsgQueue);
        busMsgQueue.flush();
    }

    @EasyMapping(mapping = ClientCmdType.BUY_WUXING_FUBEN_COUNT)
    public void buyWxFubenCount(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = wxFubenService.buyCount(userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.BUY_WUXING_FUBEN_COUNT, result);
    }

    @EasyMapping(mapping = ClientCmdType.RECEIVE_WUXING_FUBEN)
    public void receiveWxFubenReward(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = wxFubenService.receiveReward(userRoleId, false);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.RECEIVE_WUXING_FUBEN, result);
        }
    }

    // ------------------------------五行副本内部命令---------------------------//
    @EasyMapping(mapping = InnerCmdType.B_EXIT_WUXING_FUBEN)
    public void innerExitWxFuben(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        wxFubenService.innerExitWxFuben(userRoleId);
    }

    @EasyMapping(mapping = InnerCmdType.S_WUXING_FUBEN_FINISH)
    public void innerWxFubenFinish(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        wxFubenService.finishFuben(userRoleId);
    }

    // ------------------------------五行技能副本客户端命令---------------------------//
    @EasyMapping(mapping = ClientCmdType.WUXING_SKILL_FUBEN_INFO)
    public void getWxSkillFubenInfo(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = wxSkillFubenService.getRoleWxSkillFubenInfo(userRoleId);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.WUXING_SKILL_FUBEN_INFO, result);
        }
    }

    @EasyMapping(mapping = ClientCmdType.WUXING_SKILL_FUBEN_ENTER)
    public void enterWxSkillFuben(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        int fubenId = inMsg.getData();
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        wxSkillFubenService.enterWxSkillFuben(userRoleId, fubenId, busMsgQueue);
        busMsgQueue.flush();
    }

    @EasyMapping(mapping = ClientCmdType.WUXING_SKILL_FUBEN_EXIT)
    public void exitWxSkillFuben(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        wxSkillFubenService.exitWxSkillFuben(userRoleId, busMsgQueue);
        busMsgQueue.flush();
    }

    @EasyMapping(mapping = ClientCmdType.WUXING_SKILL_FUBEN_RECEIVE)
    public void receiveWxSkillFubenReward(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        int receiveType = inMsg.getData();
        Object[] result = wxSkillFubenService.receiveReward(userRoleId, receiveType);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.WUXING_SKILL_FUBEN_RECEIVE, result);
        }
    }

    @EasyMapping(mapping = ClientCmdType.WUXING_SKILL_FUBEN_CLEAR)
    public void clearWxSkillFuben(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = wxSkillFubenService.clearWxSkillFuben(userRoleId);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.WUXING_SKILL_FUBEN_CLEAR, result);
        }
    }

    @EasyMapping(mapping = ClientCmdType.WUXING_SKILL_FUBEN_NEXT)
    public void nextWxSkillFuben(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        int fubenId = inMsg.getData();
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        wxSkillFubenService.nextWxSkillFuben(userRoleId, fubenId, busMsgQueue);
        busMsgQueue.flush();
    }

    // ------------------------------五行技能副本内部命令---------------------------//
    @EasyMapping(mapping = InnerCmdType.B_EXIT_WUXING_SKILL_FUBEN)
    public void innerExitWxSkillFuben(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        wxSkillFubenService.innerExitWxSkillFuben(userRoleId);
    }

    @EasyMapping(mapping = InnerCmdType.S_WUXING_SKILL_FUBEN_FINISH)
    public void innerWxSkillFubenFinish(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        wxSkillFubenService.finishWxSkillFuben(userRoleId);
    }

    // ------------------------------五行试炼副本客户端命令---------------------------//
    @EasyMapping(mapping = ClientCmdType.WUXING_SHILIAN_FUBEN_INFO)
    public void getWxShilianFubenInfo(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        wxShilianFubenService.getRoleWxShilianFubenInfo(userRoleId, busMsgQueue);
        busMsgQueue.flush();
    }
    
    @EasyMapping(mapping = ClientCmdType.WUXING_SHILIAN_FUBEN_ENTER)
    public void enterWxShilianFuben(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        wxShilianFubenService.enterWxShilianFuben(userRoleId, busMsgQueue);
        busMsgQueue.flush();
    }
    
    @EasyMapping(mapping = ClientCmdType.WUXING_SHILIAN_FUBEN_EXIT)
    public void exitWxShilianFuben(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        wxShilianFubenService.exitWxShilianFuben(userRoleId, busMsgQueue);
        busMsgQueue.flush();
    }

    // ------------------------------五行试炼副本内部命令---------------------------//
    @EasyMapping(mapping = InnerCmdType.S_WUXING_SHILIAN_FUBEN_FINISH)
    public void innerWxShilianFubenFinish(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        wxShilianFubenService.finishWxShilianFuben(userRoleId);
    }
    
    @EasyMapping(mapping = InnerCmdType.B_EXIT_WUXING_SHILIAN_FUBEN)
    public void innerExitWxShilianFuben(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        wxShilianFubenService.innerExitWxShilianFuben(userRoleId);
    }
    
    
    // -------------------------------挑战心魔魔神副本--------------------------------//
    @EasyMapping(mapping = ClientCmdType.XM_FUBEN_GET_INFO)
    public void getXmFubenInfo(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object result = xmFubenService.getXmFubenInfo(userRoleId);
        if (null != result) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.XM_FUBEN_GET_INFO, result);
        }
    }
    
    @EasyMapping(mapping = ClientCmdType.XM_FUBEN_ENTER)
    public void enterXmFuben(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Integer xinmoId = RequestParameterUtil.object2Integer(inMsg.getData());
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        xmFubenService.enterXmFuben(userRoleId, xinmoId, busMsgQueue);
        busMsgQueue.flush();
    }
    
    @EasyMapping(mapping = ClientCmdType.XM_FUBEN_EXIT)
    public void exitXmFuben(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        xmFubenService.exitXmFuben(userRoleId, busMsgQueue);
        busMsgQueue.flush();
    }
    
    @EasyMapping(mapping = InnerCmdType.XM_FUBEN_CUT_FUHUA)
    public void xmFubenFuHuaCutHandle(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        xmFubenService.xmFubenFuHuaCutHandle(userRoleId);
    }
    
    @EasyMapping(mapping = InnerCmdType.S_XINMO_FUBEN_FINISH)
    public void innerXmFubenFinish(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        xmFubenService.innerXmFubenFinish(userRoleId);
    }
    
    @EasyMapping(mapping = InnerCmdType.B_EXIT_XM_FUBEN)
    public void innerXmFubenExit(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        xmFubenService.innerXmFubenExit(userRoleId);
    }
    
    // -------------------------------心魔深渊副本:请求指令--------------------------------//
    
    @EasyMapping(mapping = ClientCmdType.XM_SHENYUAN_GET_INFO)
    public void getXmShenyuanFubenInfo(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object result = xmShenyuanFubenService.getXmShenyuanFubenInfo(userRoleId);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.XM_SHENYUAN_GET_INFO, result);
        }
    }
    
    @EasyMapping(mapping = ClientCmdType.XM_SHENYUAN_ENTER)
    public void xmShenyuanFubenEnter(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Integer fubenId = RequestParameterUtil.object2Integer(inMsg.getData());
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        xmShenyuanFubenService.xmShenyuanFubenEnter(userRoleId, fubenId,busMsgQueue);
        busMsgQueue.flush();
    }
    
    @EasyMapping(mapping = ClientCmdType.XM_SHENYUAN_EXIT)
    public void exitXmShenyuanFuben(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        xmShenyuanFubenService.exitXmShenyuanFuben(userRoleId, busMsgQueue);
        busMsgQueue.flush();
    }
    
    @EasyMapping(mapping = ClientCmdType.XM_SHENYUAN_CLEAR_CD)
    public void clearXmShenyuanFubenCd(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        Object[] result = xmShenyuanFubenService.clearXmShenyuanFubenCd(userRoleId);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.XM_SHENYUAN_CLEAR_CD, result);
        }
    }
    
    // -------------------------------心魔深渊副本:内部处理指令--------------------------------//
    @EasyMapping(mapping = InnerCmdType.XM_SHENYUAN_FUBEN_COLING)
    public void xmShenyuanFubenColingHandle(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        xmShenyuanFubenService.xmShenyuanFubenColingHandle(userRoleId);
    }
    
    @EasyMapping(mapping = InnerCmdType.S_XM_SHENYUAN_FUBEN_FINISH)
    public void innerXmShenyuanFubenFinish(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        xmShenyuanFubenService.innerXmShenyuanFubenFinish(userRoleId);
    }
    
    @EasyMapping(mapping = InnerCmdType.B_XM_SHENYUAN_FUBEN_EXIT)
    public void innerXmShenyuanFubenExit(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        xmShenyuanFubenService.innerXmShenyuanFubenExit(userRoleId);
    }
    
    
    // -------------------------------心魔斗场副本:请求指令--------------------------------//
    
    @EasyMapping(mapping = ClientCmdType.XM_DOUCHANG_GET_INFO)
    public void getXmDouchangFubenInfo(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object result = xmDouchangFubenService.getXmDouchangFubenInfo(userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.XM_DOUCHANG_GET_INFO, result);
    }
    
    @EasyMapping(mapping = ClientCmdType.XM_DOUCHANG_ENTER)
    public void enterXmDouchangFuben(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        xmDouchangFubenService.enterXmDouchangFuben(userRoleId, busMsgQueue);
        busMsgQueue.flush();
    }
    
    @EasyMapping(mapping = ClientCmdType.XM_DOUCHANG_EXIT)
    public void exitXmDouchangFuben(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        xmDouchangFubenService.exitXmDouchangFuben(userRoleId, busMsgQueue);
        busMsgQueue.flush();
    }
    
    @EasyMapping(mapping = ClientCmdType.XM_DOUCHANG_BUY_COUNT)
    public void buyXmDouchangFubenCount(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = xmDouchangFubenService.buyXmDouchangFubenCount(userRoleId);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.XM_DOUCHANG_BUY_COUNT, result);
        }
    }
    
    // -------------------------------心魔斗场副本:内部处理指令--------------------------------//
    
    @EasyMapping(mapping = InnerCmdType.S_XM_DOUCHANG_FUBEN_FINISH)
    public void innerXmDouchangFubenFinish(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        xmDouchangFubenService.innerXmDouchangFubenFinish(userRoleId);
    }
    
    @EasyMapping(mapping = InnerCmdType.B_XM_DOUCHANG_FUBEN_EXIT)
    public void innerXmDouchangFubenExit(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Integer killNum = RequestParameterUtil.object2Integer(inMsg.getData());
        xmDouchangFubenService.innerXmDouchangFubenExit(userRoleId, killNum);
    }
    
    @EasyMapping(mapping = InnerCmdType.I_XM_DOUCHANG_ADD_BUFF)
    public void innerxmDouchangAddBuff(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        String monsterId = inMsg.getData();
        xmDouchangFubenService.killMonsterAddBuff(userRoleId, monsterId);
    }

}
