package com.junyou.bus.platform.qq.easyaction;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.platform.qq.service.QqChargeService;
import com.junyou.bus.platform.qq.service.QqService;
import com.junyou.bus.platform.qq.service.TencentYaoQingService;
import com.junyou.bus.platform.qq.service.export.TencentLuoPanExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.common.CovertObjectUtil;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.QQ_PLATFORM_MODULE)
public class QqAction {
	@Autowired
	private QqService qqService;
	@Autowired
	private QqChargeService qqChargeService;
	@Autowired
	private TencentLuoPanExportService tencentLuoPanExportService;
	@Autowired
	private TencentYaoQingService tencentYaoQingService;
	
	@EasyMapping(mapping = ClientCmdType.GET_QQ_PLATFORM_INFO)
	public void getRolePlatformInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Map<Integer, Object> info = qqService.getRoleQQInfo(userRoleId,false);
		if (info != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_QQ_PLATFORM_INFO,info);
		}
	}
	
	@EasyMapping(mapping = ClientCmdType.QINGQIU_RECHARGE)
	public void rachargeQQ(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] obj =inMsg.getData();
		Integer type = (Integer) obj[0];
		String tupianurl = (String) obj[1];
		
		Object[] re = null;
		//if(PlatformConstants.isQQ()){
			re = qqChargeService.buyChargeInfo(type, tupianurl, userRoleId);
			BusMsgSender.send2One(userRoleId,  ClientCmdType.QINGQIU_RECHARGE, re);
		//}
		
	}
	@EasyMapping(mapping = ClientCmdType.QIANDIAN_ZHIGOU)
	public void qDianZhiGou(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] obj =inMsg.getData();
		String id = (String) obj[0];
		String tupianurl = (String) obj[1];
		
		Object[] re = null;
		//if(PlatformConstants.isQQ()){
		re = qqChargeService.buyGoodSInfo(id, tupianurl, userRoleId);
		BusMsgSender.send2One(userRoleId,  ClientCmdType.QIANDIAN_ZHIGOU, re);
		//}
		
	}
	@EasyMapping(mapping = ClientCmdType.QIANDIAN_ZHIGOU_COUNT)
	public void qDianZhiGouCount(Message inMsg){
		Long userRoleId = inMsg.getRoleId();

		Object[] re = qqChargeService.getGoodsCount(userRoleId);
		BusMsgSender.send2One(userRoleId,  ClientCmdType.QIANDIAN_ZHIGOU_COUNT, re);
		
	}
	@EasyMapping(mapping = InnerCmdType.INNER_TENCENT_LM_LUOPAN)
	public void tencentLuoPanLm(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] obj =inMsg.getData();
		Integer yb =  CovertObjectUtil.object2int(obj[0]);
		String billno = (String) obj[1];
		
		tencentLuoPanExportService.tencentLuoPanLmRecharge(userRoleId, yb, billno);
	}
	
	@EasyMapping(mapping = InnerCmdType.TENCENT_LUOPAN_TONGYONG)
	public void tencentLuoPanTongYong(Message inMsg){
		Object[] obj =inMsg.getData();
		Long userRoleId = (Long) obj[0];
		String lType = (String) obj[1];
		tencentLuoPanExportService.tencentLuoPanTongYong(userRoleId, lType);
	}
	
	@EasyMapping(mapping = InnerCmdType.TENCENT_GAOQIAN_LOG)
	public void gaoqianLog(Message inMsg){
		Long userRoleId =inMsg.getData();
		tencentLuoPanExportService.gaoqianLog(userRoleId);
	}
	@EasyMapping(mapping = InnerCmdType.TENCENT_REGISTER_LUOPAN)
	public void tencentRegisterLuoPan(Message inMsg){
		Long userRoleId =inMsg.getData();
		tencentLuoPanExportService.tencentRegisterLuoPan(userRoleId);
	}
	
	@EasyMapping(mapping = InnerCmdType.TENCENT_RECHAGE_LUOPAN)
	public void tencentRechageLuoPan(Message inMsg){
		Object[] obj =inMsg.getData();
		Long userRoleId = (Long) obj[0];
		String lType = (String) obj[1];
		Integer yb =  CovertObjectUtil.object2int(obj[2]);
		tencentLuoPanExportService.tencentRechageLuoPan(userRoleId, lType, yb);
	}
	
	@EasyMapping(mapping = InnerCmdType.TENCENT_LUOPANLM_ZHUCE)
	public void tencentLuoPanLmZhuCe(Message inMsg){
		Long userRoleId =inMsg.getData();
		tencentLuoPanExportService.tencentLuoPanLmZhuCe(userRoleId);
	}
	
	@EasyMapping(mapping = InnerCmdType.TENCENT_LUOPANLM_LOGIN)
	public void tencentLuoPanLmLogin(Message inMsg){
		Long userRoleId =inMsg.getData();
		tencentLuoPanExportService.tencentLuoPanLmLogin(userRoleId);
	}
	
	@EasyMapping(mapping = InnerCmdType.TENCENT_LUOPANLM_LOGIN_CHUAN)
	public void tencentLuoPanLmLoginChuan(Message inMsg){
		Object[] obj =inMsg.getData();
		String userId = (String) obj[0];
		String ip = (String) obj[1];
		tencentLuoPanExportService.tencentLuoPanLmLoginChuan(userId,ip);
	}
	
	@EasyMapping(mapping = InnerCmdType.TENCENT_VIA_USER)
	public void tencentViaUser(Message inMsg){
		Long userRoleId =inMsg.getData();
		tencentLuoPanExportService.tencentViaUser(userRoleId);
	}
	
	
	@EasyMapping(mapping = InnerCmdType.TENCENT_LUOPAN_OSS_ZHUCE)
	public void tencentLuoPanOssZhuCe(Message inMsg){
		Long userRoleId =inMsg.getData();
		tencentLuoPanExportService.tencentLuoPanOssZhuCe(userRoleId);
	}
	
	@EasyMapping(mapping = InnerCmdType.TENCENT_LUOPAN_OSS_LOGIN)
	public void tencentLuoPanOssLogin(Message inMsg){
		Long userRoleId =inMsg.getData();
		tencentLuoPanExportService.tencentLuoPanOssLoign(userRoleId);
	}
	@EasyMapping(mapping = InnerCmdType.TENCENT_LUOPANLM_OSS_CHARGE)
	public void tencentLuoPanOssRecharge(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] obj =inMsg.getData();
		Integer yb =  CovertObjectUtil.object2int(obj[0]);
		String billno = (String) obj[1];
		Integer qDian = CovertObjectUtil.object2int(obj[2]);
		tencentLuoPanExportService.tencentLuoPanOssRecharge(userRoleId, qDian, yb, billno);
	}
	
	
	@EasyMapping(mapping = InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI)
	public void tencentLuoPanOssXiaoFei(Message inMsg){
		Long userRoleId =inMsg.getRoleId();
		Object[] obj = inMsg.getData();
		String zhiFuType = (String) obj[0];
		Integer value = (Integer) obj[1];
		Object goodsId =  obj[2];
		String goodName = (String) obj[3];
		Integer count = (Integer) obj[4];
		tencentLuoPanExportService.tencentLuoPanOssXiaofei(userRoleId, zhiFuType, value, goodsId.toString(), goodName, count);
	}
	
	@EasyMapping(mapping = ClientCmdType.GET_YAOQONG_LINGQU_INFO)
	public void tencentGetYaoQingInfo(Message inMsg){
		Long userRoleId =inMsg.getRoleId();
		Object[] rs = tencentYaoQingService.getLingQuInfo(userRoleId);
		BusMsgSender.send2One(userRoleId,  ClientCmdType.GET_YAOQONG_LINGQU_INFO, rs);
	}
	
	@EasyMapping(mapping = ClientCmdType.GET_YAOQING_SECCESS_COUNT)
	public void tencentGetYaoQingCount(Message inMsg){
		Long userRoleId =inMsg.getRoleId();
		Integer rs = tencentYaoQingService.getYaoQingCount(userRoleId);
		BusMsgSender.send2One(userRoleId,  ClientCmdType.GET_YAOQING_SECCESS_COUNT, rs);
	}
	
	@EasyMapping(mapping = ClientCmdType.LINGQU_YAOQING_JITEM)
	public void tencentLingQuItem(Message inMsg){
		Long userRoleId =inMsg.getRoleId();
		Integer id = inMsg.getData();
		Object[] rs = tencentYaoQingService.lingquYaoQing(userRoleId, id);
		BusMsgSender.send2One(userRoleId,  ClientCmdType.LINGQU_YAOQING_JITEM, rs);
	}
	
	
}
