package com.junyou.bus.zuoqi.easyaction;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.chibang.service.ChiBangService;
import com.junyou.bus.qiling.service.QiLingService;
import com.junyou.bus.role.service.TangbaoService;
import com.junyou.bus.tianyu.service.TianYuService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.wuqi.service.WuQiService;
import com.junyou.bus.xianjian.service.XianjianService;
import com.junyou.bus.zhanjia.service.ZhanJiaService;
import com.junyou.bus.zuoqi.service.ZuoQiService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.module.GameModType;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;
 
/**
 * 坐骑
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-1-15 下午5:15:01
 */
@Controller
@EasyWorker(moduleName = GameModType.ZUO_QI,groupName=EasyGroup.BUS_CACHE)
public class ZuoQiAction {
	
	@Autowired
	private ZuoQiService zuoQiService;
	@Autowired
	private ChiBangService chiBangService;
	@Autowired
	private XianjianService xianjianService;
	@Autowired
	private ZhanJiaService zhanJiaService;
	@Autowired
	private QiLingService qiLingService;
	@Autowired
	private TangbaoService tangbaoService;
	@Autowired
	private TianYuService tianYuService;
	@Autowired
	private WuQiService wuQiService;
	
	@EasyMapping(mapping = ClientCmdType.ZUOQI_SHOW)
	public void zuoqiShow(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result=zuoQiService.zuoqiShow(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ZUOQI_SHOW, result);
	}

	@EasyMapping(mapping = ClientCmdType.ZUOQI_JJ_UP)
	public void zuoqiJjUp(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		boolean isAutoGm = inMsg.getData();
		
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		Object[] result=zuoQiService.zuoQiSj(userRoleId,busMsgQueue,isAutoGm);
		busMsgQueue.flush();
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.ZUOQI_JJ_UP, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.ZUOQI_AUTO_UP)
	public void zuoqiAutoUp(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		boolean isAutoGm = inMsg.getData();
		
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		Object[] result=zuoQiService.autoZuoQiSj(userRoleId,busMsgQueue,isAutoGm);
		busMsgQueue.flush();
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.ZUOQI_AUTO_UP, result);
	}
	
	//下坐骑
	@EasyMapping(mapping = ClientCmdType.ZUOQI_DOWN)
	public void zuoqiDown(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		zuoQiService.zuoqiDown(userRoleId,false);
	}
	
	//上坐骑
	@EasyMapping(mapping = ClientCmdType.ZUOQI_UP)
	public void zuoqiUp(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		zuoQiService.zuoqiDown(userRoleId,true);
	}
	
	//坐骑外显更新
	@EasyMapping(mapping = ClientCmdType.ZUOQI_UPDATE_SHOW,kuafuType=EasyKuafuType.KF2S_HANDLE_TYPE)
	public void zuoqiUpdateShow(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		int showId = inMsg.getData();
		
		Object[] result = zuoQiService.zuoqiUpdateShow(userRoleId,showId,true);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ZUOQI_UPDATE_SHOW, result);
	} 
	
	//坐骑上下状态切换持久化
	@EasyMapping(mapping = InnerCmdType.ZUOQI_BUS_STATE,kuafuType=EasyKuafuType.KF2S_HANDLE_TYPE)
	public void zuoqiBusState(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		int state = inMsg.getData(); 
		
		zuoQiService.zuoqiBusState(userRoleId,state);
		 
	} 

	/**
	 * 更新坐骑战斗力
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_ZUOQI_ZPLUS_CHANGE,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void zuoqiZplusChange(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Long zplus = inMsg.getData();
		
		zuoQiService.updateZuoQiZplus(userRoleId, zplus);
	}
	/**
	 * 获取坐骑的属性，战力
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.ZUOQI_ATTR_CHANGE)
	public void getZuoqiAttrAndZplus(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Map<String ,Long> zuoQiAttrs = zuoQiService.getZuoqiAttr(userRoleId);
		if(zuoQiAttrs != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.ZUOQI_ATTR_CHANGE, zuoQiAttrs);
		}
	}
	/**
	 * 万能成长丹
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.WANNEG_CZD)
	public void wanNengCzd(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data= inMsg.getData();
		Integer type = (Integer)data[0];
		Long guid = LongUtils.obj2long(data[1]);
		Object[] result = null;
		if(type==GameConstants.COMMON_WANNENG_ZUOQI){
			result = zuoQiService.useCzdNew(userRoleId, guid);
		}else if(type==GameConstants.COMMON_WANNENG_CHIBANG){
			result = chiBangService.useCzdNew(userRoleId, guid);
		}else if(type==GameConstants.COMMON_WANNENG_XIANJIAN){
			result = xianjianService.useCzdNew(userRoleId, guid);
		}else if(type==GameConstants.COMMON_WANNENG_ZHANJIA){
			result = zhanJiaService.useCzdNew(userRoleId, guid);
		}else if(type==GameConstants.COMMON_WANNENG_QILING){
			result = qiLingService.useCzdNew(userRoleId, guid);
		}else if(type==GameConstants.COMMON_WANNENG_TANGBAO){
			result = tangbaoService.useCzdNew(userRoleId, guid);
		}else if(type==GameConstants.COMMON_WANNENG_TIANYU){
			result = tianYuService.useCzdNew(userRoleId, guid);
		}else if(type==GameConstants.COMMON_WANNENG_WUQI){
			result = wuQiService.useCzdNew(userRoleId, guid);
		}
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.WANNEG_CZD, result);
		}
	}
	
	/**
	 * 万能潜能丹
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.WANNEG_QND)
	public void wanNengQnd(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data= inMsg.getData();
		Integer type = (Integer)data[0];
		Long guid = LongUtils.obj2long(data[1]);
		Object[] result = null;
		if(type==GameConstants.COMMON_WANNENG_ZUOQI){
			result = zuoQiService.useQndNew(userRoleId, guid);
		}else if(type==GameConstants.COMMON_WANNENG_CHIBANG){
			result = chiBangService.useQndNew(userRoleId, guid);
		}else if(type==GameConstants.COMMON_WANNENG_XIANJIAN){
			result = xianjianService.useQndNew(userRoleId, guid);
		}else if(type==GameConstants.COMMON_WANNENG_ZHANJIA){
			result = zhanJiaService.useQndNew(userRoleId, guid);
		}else if(type==GameConstants.COMMON_WANNENG_QILING){
			result = qiLingService.useQndNew(userRoleId, guid);
		}else if(type==GameConstants.COMMON_WANNENG_TANGBAO){
			result = tangbaoService.useQndNew(userRoleId, guid);
		}else if(type==GameConstants.COMMON_WANNENG_TIANYU){
			result = tianYuService.useQndNew(userRoleId, guid);
		}else if(type==GameConstants.COMMON_WANNENG_WUQI){
			result = wuQiService.useQndNew(userRoleId, guid);
		}
		if(result!=null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.WANNEG_QND, result);
		}
	}
}
