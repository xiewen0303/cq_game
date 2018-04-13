package com.junyou.bus.kaifuactivity.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.kaifuactivity.service.RfbChiBangPaiMingService;
import com.junyou.bus.kaifuactivity.service.RfbQiLingPaiMingService;
import com.junyou.bus.kaifuactivity.service.RfbTangBaoService;
import com.junyou.bus.kaifuactivity.service.RfbTangBaoXinWenPaiMingService;
import com.junyou.bus.kaifuactivity.service.RfbWuQiPaiMingService;
import com.junyou.bus.kaifuactivity.service.RfbXianZhuangQiangHuaService;
import com.junyou.bus.kaifuactivity.service.RfbYaoMoPaiMingService;
import com.junyou.bus.kaifuactivity.service.RfbYaoShenHunpoPaiMingService;
import com.junyou.bus.kaifuactivity.service.RfbYaoShenMoYinPaiMingService;
import com.junyou.bus.kaifuactivity.service.RfbYaoShenPaiMingService;
import com.junyou.bus.kaifuactivity.service.RfbYuJianFeiXingService;
import com.junyou.bus.kaifuactivity.service.RfbZhanJiaPaiMingService;
import com.junyou.bus.kaifuactivity.service.RfbZhanLiBiPinService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 热发布战力比拼运营活动
 * @description 
 * @author ZHONGDIAN
 * @date 2015-5-5 下午4:46:05
 */
@Controller
@EasyWorker(moduleName = GameModType.ACTIVITY)
public class RefabuZLBPAction {

	@Autowired
	private RfbZhanLiBiPinService rfbZhanLiBiPinService;
	@Autowired
	private RfbChiBangPaiMingService rfbChiBangPaiMingService;
	@Autowired
	private RfbYuJianFeiXingService	rfbYuJianFeiXingService;
	@Autowired
	private RfbXianZhuangQiangHuaService rfbXianZhuangQiangHuaService;
	@Autowired
	private RfbTangBaoService rfbTangBaoService;
	@Autowired
	private RfbZhanJiaPaiMingService rfbZhanJiaPaiMingService;
	@Autowired
	private RfbYaoShenPaiMingService rfbYaoShenPaiMingService;
	@Autowired
	private RfbYaoMoPaiMingService rfbYaoMoPaiMingService;
	@Autowired
	private RfbQiLingPaiMingService rfbQiLingPaiMingService;
	@Autowired
	private RfbYaoShenHunpoPaiMingService rfbYaoShenHunpoPaiMingService;
	@Autowired
	private RfbYaoShenMoYinPaiMingService rfbYaoShenMoYinPaiMingService;
	@Autowired
	private RfbTangBaoXinWenPaiMingService rfbTangBaoXinWenPaiMingService;
	@Autowired
	private RfbWuQiPaiMingService rfbWuQiPaiMingService;
	/**
	 * 请求领取礼包
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECEVIE_ACTIVITY_ZLBP)
	public void lingquZLBPbao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer configId = (Integer) data[2];
		
		Object[] result = rfbZhanLiBiPinService.lingQuJiangLi(userRoleId, version, configId, subId);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_ACTIVITY_ZLBP, result);
		}
	}
	/**
	 * 请求领取礼包
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECEVIE_ACTIVITY_YJFX)
	public void lingquYJFXLibao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer configId = (Integer) data[2];
		
		Object[] result = rfbYuJianFeiXingService.lingQuJiangLi(userRoleId, version, configId, subId);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_ACTIVITY_YJFX, result);
		}
	}
	/**
	 * 请求领取礼包
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECEVIE_ACTIVITY_YAOSHEN)
	public void lingquYaoShenLibao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer configId = (Integer) data[2];
		
		Object[] result = rfbYaoShenPaiMingService.lingQuJiangLi(userRoleId, version, configId, subId);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_ACTIVITY_YAOSHEN, result);
		}
	}
	/**
	 * 请求领取礼包
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECEVIE_ACTIVITY_YAOMO)
	public void lingquYaoMoLibao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer configId = (Integer) data[2];
		
		Object[] result = rfbYaoMoPaiMingService.lingQuJiangLi(userRoleId, version, configId, subId);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_ACTIVITY_YAOMO, result);
		}
	}
	/**
	 * 请求领取礼包
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECEVIE_ACTIVITY_XJYY)
	public void lingquXJYYLibao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer configId = (Integer) data[2];
		
		Object[] result = rfbChiBangPaiMingService.lingQuJiangLi(userRoleId, version, configId, subId);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_ACTIVITY_XJYY, result);
		}
	}
	
	/**
	 * 请求领取礼包
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECEVIE_ACTIVITY_XZQH)
	public void lingquXZQHLibao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer configId = (Integer) data[2];
		
		Object[] result = rfbXianZhuangQiangHuaService.lingQuJiangLi(userRoleId, version, configId, subId);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_ACTIVITY_XZQH, result);
		}
	}
	
	/**
	 * 请求领取礼包
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECEVIE_ACTIVITY_TANGBAO)
	public void lingquTBXJLibao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer configId = (Integer) data[2];
		
		Object[] result = rfbTangBaoService.lingQuJiangLi(userRoleId, version, configId, subId);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_ACTIVITY_TANGBAO, result);
		}
	}
	/**
	 * 请求领取礼包
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECEVIE_ACTIVITY_TBZJ)
	public void lingquTBZJLibao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer configId = (Integer) data[2];
		
		Object[] result = rfbZhanJiaPaiMingService.lingQuJiangLi(userRoleId, version, configId, subId);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_ACTIVITY_TBZJ, result);
		}
	}
	/**
	 * 请求领取礼包
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECEVIE_ACTIVITY_QILING)
	public void lingquQiLing(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer configId = (Integer) data[2];
		
		Object[] result = rfbQiLingPaiMingService.lingQuJiangLi(userRoleId, version, configId, subId);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_ACTIVITY_QILING, result);
		}
	}
	/**
	 * 请求领取礼包
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECEVIE_ACTIVITY_YAOPO)
	public void lingquYaoShenHunPo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer configId = (Integer) data[2];
		
		Object[] result = rfbYaoShenHunpoPaiMingService.lingQuJiangLi(userRoleId, version, configId, subId);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_ACTIVITY_YAOPO, result);
		}
	}
	
	/**
	 * 请求领取礼包
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECEVIE_ACTIVITY_MOYIN)
	public void lingquYaoShenMoYin(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer configId = (Integer) data[2];
		
		Object[] result = rfbYaoShenMoYinPaiMingService.lingQuJiangLi(userRoleId, version, configId, subId);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_ACTIVITY_MOYIN, result);
		}
	}
	
	/**
	 * 请求领取礼包
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECEVIE_ACTIVITY_TANGBAOXINWEN)
	public void lingquTangBaoXinWen(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer configId = (Integer) data[2];
		
		Object[] result = rfbTangBaoXinWenPaiMingService.lingQuJiangLi(userRoleId, version, configId, subId);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.RECEVIE_ACTIVITY_TANGBAOXINWEN, result);
		}
	}
	
	/**
	 * 请求领取礼包
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.SHENG_JIAN_LINGJIANG)
	public void lingquShenJianLibao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer subId = (Integer) data[0];
		Integer version = (Integer) data[1];
		Integer configId = (Integer) data[2];
		
		Object[] result = rfbWuQiPaiMingService.lingQuJiangLi(userRoleId, version, configId, subId);
		if(result != null){//返回null,则版本号不一致，处理配置数据
			BusMsgSender.send2One(userRoleId, ClientCmdType.SHENG_JIAN_LINGJIANG, result);
		}
	}
	
}
