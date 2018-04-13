package com.junyou.bus.platform.qq.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.platform.qq.service.QqChargeService;
import com.junyou.bus.platform.qq.service.QqLanZuanGiftService;
import com.junyou.bus.platform.qq.service.TencentShangDianService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.QQ_PLATFORM_MODULE)
public class QqLanZuanAction {
	@Autowired
	private QqLanZuanGiftService qqLanZuanGigtService;
	@Autowired
	private TencentShangDianService tencentShangDianService;
	@Autowired
	private QqChargeService qqChargeService;
	
	@EasyMapping(mapping = ClientCmdType.GET_QQ_LANZUAN_GIFT_STATUS)
	public void getGiftStatus(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer ret = qqLanZuanGigtService.getGiftStatus(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.GET_QQ_LANZUAN_GIFT_STATUS, ret);
		}
	}

	@EasyMapping(mapping = ClientCmdType.GET_QQ_LANZUAN_XINSHOU_GIFT)
	public void getXinshouGift(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = qqLanZuanGigtService.getXinShouGift(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.GET_QQ_LANZUAN_XINSHOU_GIFT, ret);
		}
	}

	@EasyMapping(mapping = ClientCmdType.GET_QQ_LANZUAN_DENGJI_GIFT)
	public void getChengZhangGift(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer n = inMsg.getData();
		Object[] ret = qqLanZuanGigtService.getChengZhangGift(userRoleId,n);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_QQ_LANZUAN_DENGJI_GIFT,
					ret);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.GET_QQ_LANZUAN_MEIRI_GIFT_STATUS)
	public void getMeiRiGiftStatus(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Boolean ret = qqLanZuanGigtService.getMeiriGiftStatus(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_QQ_LANZUAN_MEIRI_GIFT_STATUS,
					ret);
		}
	}


	@EasyMapping(mapping = ClientCmdType.GET_QQ_LANZUAN_MEIRI_GIFT)
	public void getMeiriGift(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = qqLanZuanGigtService.getMeiriGift(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.GET_QQ_LANZUAN_MEIRI_GIFT, ret);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.GET_LINGQU_XUFEI_LIBAO)
	public void getLingQuXuFei(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = qqLanZuanGigtService.lingQuXuFei(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,ClientCmdType.GET_LINGQU_XUFEI_LIBAO, ret);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.GET_LANZUAN_ZHEKUO_SHANGDIAN)
	public void getLanZuanZheKouShangdian(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = tencentShangDianService.getInfo(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,ClientCmdType.GET_LANZUAN_ZHEKUO_SHANGDIAN, ret);
		}
	}
	@EasyMapping(mapping = ClientCmdType.GET_LANZUAN_SHANGDIAN_BUY)
	public void lanZuanShangDianBuy(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer id = inMsg.getData();
		Object[] ret = tencentShangDianService.buy(userRoleId, id);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,ClientCmdType.GET_LANZUAN_SHANGDIAN_BUY, ret);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.GET_LANZUAN_SONGLI_INFO)
	public void getLanZuanSongliInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = qqLanZuanGigtService.getLanZuanInfo(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,ClientCmdType.GET_LANZUAN_SONGLI_INFO, ret);
		}
	}
	@EasyMapping(mapping = ClientCmdType.GET_LANZUAN_SONGLI_TOKEN)
	public void getLanZuanSongliToken(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = qqChargeService.getLanZuanToken(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,ClientCmdType.GET_LANZUAN_SONGLI_TOKEN, ret);
		}
	}
	@EasyMapping(mapping = ClientCmdType.GET_LANZUAN_SONGLI_LINGQU)
	public void lingQuLanZuanSongLi(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] ret = qqLanZuanGigtService.lingquLanZuan(userRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId,ClientCmdType.GET_LANZUAN_SONGLI_LINGQU, ret);
		}
	}
}
