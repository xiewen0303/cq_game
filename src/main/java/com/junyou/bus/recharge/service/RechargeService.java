package com.junyou.bus.recharge.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.entity.RoleAccount;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.danfuchargerank.vo.DanfuChargeRankVo;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.platform.configure.export.PlatformGongGongShuJuBiaoConfigExportService;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.export.PlatformExportService;
import com.junyou.bus.platform.qq.confiure.export.QqRechargePublicConfig;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.qq.service.export.QqExportService;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.platform.yuenan.dao.RechargeYuenanDao;
import com.junyou.bus.platform.yuenan.entity.RechargeYuenan;
import com.junyou.bus.recharge.dao.RechargeDao;
import com.junyou.bus.recharge.entity.Recharge;
import com.junyou.bus.rfbactivity.export.RefabuActivityExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.FirstRechargeLogEvent;
import com.junyou.event.GetNumLogEvent;
import com.junyou.event.QDianZhiGouLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.io.export.SessionManagerExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.data.accessor.AccessType;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;
import com.kernel.sync.annotation.Sync;

/**
 * 充值
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2014-12-15 上午11:05:38 
 */
@Service
public class RechargeService {
	@Autowired
	private RechargeDao rechargeDao;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	@Autowired
	private RefabuActivityExportService refabuActivityExportService;
	@Autowired
	private RechargeYuenanDao rechargeYuenanDao;
	@Autowired
	private PlatformGongGongShuJuBiaoConfigExportService platformGongGongShuJuBiaoConfigExportService;
	@Autowired 
	private PlatformExportService platformExportService;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private SessionManagerExportService sessionManagerExportService;
	@Autowired
	private QqExportService qqExportService;
	
	private void createRechargeFromDb(String userId, String serverId,Long userRoleId, String platformType, String orderId, double reRmb, long yb, int reState,int reType){
		Recharge recharge = new Recharge();
		
		recharge.setYb(yb);
		recharge.setReType(reType);
		recharge.setIsCalc(0);
		recharge.setRmb(reRmb);
		recharge.setUserId(userId);
		recharge.setReState(reState);
		recharge.setOrderId(orderId);
		recharge.setUserRoleId(userRoleId);
		recharge.setServerId(serverId);
		recharge.setPlatformType(platformType);
		recharge.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		recharge.setId(IdFactory.getInstance().generateId(ServerIdType.RECHARGE));
		
		rechargeDao.insertRechargeFromDb(recharge);
	}
	
	private void createRechargeFromDbYn(String userId, String serverId,Long userRoleId, String platformType, String orderId, double reRmb, long yb, int reState){
		RechargeYuenan recharge = new RechargeYuenan();
		
		recharge.setYb(yb);
		recharge.setRmb(reRmb);
		recharge.setUserId(userId);
		recharge.setReState(reState);
		recharge.setOrderId(orderId);
		recharge.setUserRoleId(userRoleId);
		recharge.setServerId(serverId);
		recharge.setPlatformType(platformType);
		recharge.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		recharge.setId(IdFactory.getInstance().generateId(ServerIdType.RECHARGE));
		
		rechargeYuenanDao.insertRechargeYuenanFromDb(recharge);
	}
	/**
	 * 验证充值的订单号是否重复 
	 * @param orderId
	 * @return true重复
	 */
	private boolean checkOrderRepeat(String orderId){
		List<Recharge> reList = rechargeDao.getRechargeFormDbByOrderId(orderId);
		if(reList != null && reList.size() > 0){
			return true;
		}
		return false;
	}
	/**
	 * 验证充值的订单号是否重复 (越南)
	 * @param orderId
	 * @return true重复
	 */
	private boolean checkOrderRepeatYn(String orderId){
		List<RechargeYuenan> reList = rechargeYuenanDao.getRechargeFormDbByOrderId(orderId);
		if(reList != null && reList.size() > 0){
			return true;
		}
		return false;
	}
	
	public int webRechage(Long userRoleId,Integer reType, String orderId, Double reRmb, Long yb){
		int reState = AppErrorCode.RECHARGE_SUCCESS;
		String userId = "";
		String serverId = "";
		try {
			
			//验证数值
			if(reRmb == null || yb == null || reRmb <= 0d || yb <= 0){
				reState = AppErrorCode.LESS_THAN_ZERO;
			}
			RoleAccount account = accountExportService.initRoleAccount(userRoleId);
			//验证账号是否存在
			if(account == null){
				reState = AppErrorCode.ACCOUNT_NOT_EXSIT;
			}
			
			//验证订单是否重复
			if(checkOrderRepeat(orderId)){
				reState = AppErrorCode.ORDER_REPEAT;
			}
			userId = account.getUserId();
			serverId = account.getServerId();
			
		} catch (Exception e) {
			reState = AppErrorCode.BUS_EXCEPTION;
			ChuanQiLog.error("Role is rechage error", e);
		}finally{
			//插入充值数据
			createRechargeFromDb(userId, serverId,userRoleId, "gmtools", orderId, reRmb, yb, reState,reType);
		}
		
		if(reState == AppErrorCode.RECHARGE_SUCCESS && publicRoleStateExportService.isPublicOnline(userRoleId)){
			//如果玩家在线，通知业务领取元宝
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.REV_RECHARGE, null);
		}
		
		if(reState ==AppErrorCode.RECHARGE_SUCCESS){
			BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.KUAFU_CHARGE_RANK, new Object[]{GameSystemTime.getSystemMillTime(),yb,userRoleId});
			BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DANFU_CHARGE_RANK, new Object[]{GameSystemTime.getSystemMillTime(),yb,userRoleId});
			BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.HAPPY_CARD_CHARGE, new Object[]{yb,userRoleId});
		}
		
		return reState;
	}
	public int rechage(String userId, Long userRoleId, String serverId, String platformType, String orderId, Double reRmb, Long yb){
		int reState = AppErrorCode.RECHARGE_SUCCESS;
		try {
			
			//验证数值
			if(reRmb == null || yb == null || reRmb <= 0d || yb <= 0){
				reState = AppErrorCode.LESS_THAN_ZERO;
			}
			//验证账号是否存在
			RoleAccount account = accountExportService.getRoleAccountFromDb(userId, serverId);
			if(account == null){
				reState = AppErrorCode.ACCOUNT_NOT_EXSIT;
			}else if(userRoleId > 0){
				if(!account.getUserRoleId().equals(userRoleId)){
					reState = AppErrorCode.ACCOUNT_NOT_THIS_ROLE;
				}
			}else{
				userRoleId = account.getUserRoleId();
			}
			
			//验证订单是否重复
			if(checkOrderRepeat(orderId)){
				reState = AppErrorCode.ORDER_REPEAT;
			}
			
		} catch (Exception e) {
			reState = AppErrorCode.BUS_EXCEPTION;
			ChuanQiLog.error("Role is rechage error", e);
		}finally{
			//插入充值数据
			createRechargeFromDb(userId, serverId,userRoleId, platformType, orderId, reRmb, yb, reState,GameConstants.PLAYER_RECHARGE);
		}
		
		if(reState == AppErrorCode.RECHARGE_SUCCESS && publicRoleStateExportService.isPublicOnline(userRoleId)){
			//如果玩家在，通知业线务领取元宝
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.REV_RECHARGE, null);
		}
		
		if(reState == AppErrorCode.RECHARGE_SUCCESS){
			BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.KUAFU_CHARGE_RANK, new Object[]{GameSystemTime.getSystemMillTime(),yb,userRoleId});
			BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DANFU_CHARGE_RANK, new Object[]{GameSystemTime.getSystemMillTime(),yb,userRoleId});
			BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.HAPPY_CARD_CHARGE, new Object[]{yb,userRoleId});
		}
		
		return reState;
	}
	
	public int rechageYueNan(String userId,String serverId, String platformType, String orderId, Double reRmb, Long yb){
		Integer reState = AppErrorCode.RECHARGE_SUCCESS;
		Long userRoleId = 0l;
		try {
			//验证数值
			if(reRmb == null || yb == null || reRmb <= 0d || yb <= 0){
				reState = AppErrorCode.LESS_THAN_ZERO;
			}
			//验证账号是否存在
			RoleAccount account = accountExportService.getRoleAccountFromDb(userId, serverId);
			if(account == null){
				reState = AppErrorCode.ACCOUNT_NOT_EXSIT;
			}else{
				userRoleId = account.getUserRoleId();
				
				//验证订单是否重复
				if(checkOrderRepeatYn(orderId)){
					reState = AppErrorCode.ORDER_REPEAT;
				}
				
				if(reState == AppErrorCode.RECHARGE_SUCCESS){
					 Map<String, Integer> awards  = new HashMap<String, Integer>();
					 awards.put(ModulePropIdConstant.GOLD_GOODS_ID, yb.intValue());
					  
					 String[] attachments = EmailUtil.getAttachments(awards);
					 
					 String content = EmailUtil.getCodeEmail(GameConstants.YUENAN_RECHARGE_CODE);
					 String title = EmailUtil.getCodeEmail(GameConstants.YUENAN_RECHARGE_CODE_TITLE);
					 for (String attachment : attachments) {
						emailExportService.sendEmailToOne(userRoleId,title, content, GameConstants.EMAIL_TYPE_SINGLE, attachment);
					 }
						
				}
			}
			
		} catch (Exception e) {
			reState = AppErrorCode.BUS_EXCEPTION;
			ChuanQiLog.error("Role is rechage error", e);
		}finally{
			//插入充值数据
			createRechargeFromDbYn(userId, serverId,userRoleId, platformType, orderId, reRmb, yb, reState);
		}
		
		return reState;
	}
	
	
	private RoleWrapper getRoleWrapper(Long userRoleId){
		if(publicRoleStateExportService.isPublicOnline(userRoleId)){
			//在线处理
			RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
			return roleWrapper;
		}else{
			RoleWrapper roleWrapper = roleExportService.getUserRoleFromDb(userRoleId);
			return roleWrapper;
		}
	}
	
	public int rechageQQ(String serverId,Long userRoleId,String billno,String platformType,String itemXinxi,String amt,String pubacctPayamtCoins){
		Integer reState = AppErrorCode.RECHARGE_SUCCESS;
		String userId = "user";
		int yyy = 0;//记录是否需要通知领取元宝状态
		try {
			//验证角色是否存在
			RoleWrapper role = getRoleWrapper(userRoleId);
			if(role != null){
				userId = role.getUserId();
				//roleId = (String) checkRole[1];
				String[] item = itemXinxi.split("\\*"); //物品信息 格式为 物品ID*单价*数量
				if(item == null || item.length <= 0){
					return AppErrorCode.BUS_EXCEPTION;
				}
				//验证订单是否重复
				if(checkOrderRepeat(billno)){
					reState = AppErrorCode.ORDER_REPEAT;
				}
				
				String goodsId = item[0];
				if(goodsId != null &&  goodsId.startsWith("gold")){
					QqRechargePublicConfig publicConfig = platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QQ_RECHARGE);
					if(publicConfig == null){
						return AppErrorCode.BUS_EXCEPTION;
					}
					//物品信息
					Map<String, String> chargeMap = publicConfig.getChargeMap();
					//每个物品所获得的元宝
					int yb = Integer.parseInt(chargeMap.get(goodsId));
					yb = yb * Integer.parseInt(item[2]);//总元宝
					
					double qDian =  Integer.parseInt(amt)/10f; 
					double reRmb = qDian + Integer.parseInt(pubacctPayamtCoins);
					//插入充值数据
					createRechargeFromDb(userId, serverId,userRoleId, platformType, billno, reRmb, yb, reState,GameConstants.PLAYER_RECHARGE);
					//腾讯罗盘充值数据上报
					if(PlatformConstants.isQQ()){
						//tencentLuoPanExportService.tencentRechageLuoPan(userRoleId, QqConstants.RECHARGE, yb);
						BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_RECHAGE_LUOPAN, new Object[]{userRoleId, QqConstants.RECHARGE, amt});
						BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_TENCENT_LM_LUOPAN, new Object[]{amt,billno});
						BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPANLM_OSS_CHARGE, new Object[]{yb,billno,qDian});
					}
					
					if(reState == AppErrorCode.RECHARGE_SUCCESS){
						BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.KUAFU_CHARGE_RANK, new Object[]{GameSystemTime.getSystemMillTime(),yb,userRoleId});
						BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.DANFU_CHARGE_RANK, new Object[]{GameSystemTime.getSystemMillTime(),yb,userRoleId});
						BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.HAPPY_CARD_CHARGE, new Object[]{yb,userRoleId});
					}
				}else{
					yyy = 1;
					String goodsItem = goodsId+":"+item[2];
					String title = EmailUtil.getCodeEmail(AppErrorCode.QDIAN_ZHIGOU_EMAIL_TITLE);
					String content = EmailUtil.getCodeEmail(AppErrorCode.QDIAN_ZHIGOU);
					emailExportService.sendEmailToOne(userRoleId, title,content,GameConstants.EMAIL_TYPE_SINGLE, goodsItem);
					//增加购买次数
					qqExportService.setRoleQdianCount(userRoleId, goodsId);
					//腾讯罗盘充值数据上报
					if(PlatformConstants.isQQ()){
						double qDian =  Integer.parseInt(amt)/10f; 
						double reRmb = qDian + Integer.parseInt(pubacctPayamtCoins);
						
						BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_RECHAGE_LUOPAN, new Object[]{userRoleId, QqConstants.RECHARGE, amt});
						BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_TENCENT_LM_LUOPAN, new Object[]{amt,billno});
					}
					//日志打印 
					try {
						GamePublishEvent.publishEvent(new QDianZhiGouLogEvent(userRoleId, Integer.parseInt(amt)/10,Integer.parseInt(pubacctPayamtCoins),getRoleName(userRoleId), goodsId, Integer.parseInt(item[2])));
					} catch (Exception e) {
						ChuanQiLog.error("",e);
					}
				}
				
			}else{
				return AppErrorCode.ACCOUNT_NOT_EXSIT;//角色不存在
			}
		} catch (Exception e) {
			reState = AppErrorCode.BUS_EXCEPTION;
			ChuanQiLog.error("Role is rechage error", e);
		}
		if(reState == AppErrorCode.RECHARGE_SUCCESS && yyy == 0 && publicRoleStateExportService.isPublicOnline(userRoleId)){
			//如果玩家在线，通知业务领取元宝
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.REV_RECHARGE, null);
		}
		
		return reState;
	}
	
	private String getRoleName(Long userRoleId){
		RoleWrapper role = null;
		try {
			
			if(sessionManagerExportService.isOnline(userRoleId)){
				role = roleExportService.getLoginRole(userRoleId);
				return role.getName();
			}else{
				role = roleExportService.getUserRoleFromDb(userRoleId);
				return role.getName();
			}
		} catch (Exception e) {
			role = roleExportService.getUserRoleFromDb(userRoleId);
		}
		return role.getName();
	}
	/**
	 * 处理充值到账
	 * @param userId
	 * @param orderId
	 */
	@Sync(component = GameConstants.COMPONENT_BUS_SHARE,indexes = {0})
	public void finishRecharge(Long userRoleId){
		List<Recharge> list = rechargeDao.getEnableRecharges(userRoleId);
		finishRecharge(userRoleId, list);
	}
	/**
	 * 上线处理处理充值到账
	 * @param userId
	 * @param orderId
	 */
	@Sync(component = GameConstants.COMPONENT_BUS_SHARE,indexes = {0})
	public void onlineHandlefinishRecharge(Long userRoleId){
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		List<Recharge> list = rechargeDao.getEnableRecharges(role.getUserId(),role.getServerId());
		finishRecharge(userRoleId, list);
	}
	
	private void finishRecharge(Long userRoleId, List<Recharge> list) {
		Map<String,Object> successOrderIds = new HashMap<>();
		Map<String,Long[]> rechargeData = new HashMap<>();
		RoleAccount account = accountExportService.getRoleAccount(userRoleId);
		long addYb = 0;
		long beforeVal = account.getYb();
		for (Recharge recharge : list) {
			String orderId = recharge.getOrderId();
			if(successOrderIds.containsKey(orderId)){
				recharge.setReState(AppErrorCode.ORDER_REPEAT);
			}else{
				successOrderIds.put(orderId, null);
				long yb = recharge.getYb();
				int result = accountExportService.revRecharge(account, yb, recharge.getReType());
				if(result == AppErrorCode.SUCCESS){
					addYb += yb;
					recharge.setReState(AppErrorCode.RECIVE_SUCCESS);
				}else{
					ChuanQiLog.error(userRoleId+"充值后元宝超过上限。到账失败订单号："+orderId);
					continue;
				}
				
				//记录可增加的元宝和充值时间
				rechargeData.put(recharge.getOrderId(), new Long[]{recharge.getCreateTime().getTime(),yb});
			}
			rechargeDao.update(recharge, userRoleId, AccessType.getDirectDbType());
		}
		
		if(addYb > 0){
			//通知客户端元宝变化
			BusMsgSender.send2One(userRoleId, ClientCmdType.YUANBAO_CHANGE, account.getYb());
			//通知前端帐元宝提示
			BusMsgSender.send2One(userRoleId, ClientCmdType.YUANBAO_CHARGE, addYb);
			
			
			//如果第一次充值，打印日志
			if(roleExportService.isFirstRecharge(userRoleId)){
				if(GameConstants.USER_TYPE_PLAYER == account.getUserType()){//非正常玩家不打印
					RoleWrapper role = roleExportService.getLoginRole(userRoleId);
					GamePublishEvent.publishEvent(new FirstRechargeLogEvent(userRoleId, role.getLevel()));
				}
				//首冲发红包
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.HONGBAO_SEND,null);
				//首冲返利
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_FIRST_CHARGER_REBATE, (int)addYb);
			}
			
			roleVipInfoExportService.rechargeVipExp(userRoleId, addYb);
			
			//元宝充值监听器处理业务
			rechargeYbMonitor(userRoleId, addYb,rechargeData);
			
			//打印元宝获得日志
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			GamePublishEvent.publishEvent(new GetNumLogEvent(LogPrintHandle.GET_RECHARGE, userRoleId, role.getName(), GoodsCategory.GOLD, addYb, null, 0l, beforeVal, account.getYb(), LogPrintHandle.GBZ_RECHARGE));
		}
	}
	
	
	/**
	 * 元宝充值监听器处理业务
	 * @param userRoleId
	 * @param addYb
	 * @param rechargeData 
	 */
	private void rechargeYbMonitor(Long userRoleId,Long addYb, Map<String, Long[]> rechargeData){
		try {
			refabuActivityExportService.chargeYbRefbMonitorHandle(userRoleId, addYb,rechargeData);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		
		try {
			//超级会员
			platformExportService.roleRecharge(userRoleId, addYb);
		} catch (Exception e) {
			ChuanQiLog.error("充值调用超级会员逻辑出错:",e);
		}
		//成就
		try {
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_DANCHONGZHIYUANBAO, addYb.intValue()});
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_LEICHONGZHIYUANBAO, addYb.intValue()});
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.RECHARGE_ZUANSHI, addYb.intValue()});
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		
	}
	/**获取该玩家的一段时间的充值记录**/
	public int getTotalRechargesByTime(long userRoleId,long startTime,long endTime){
		return rechargeDao.getTotalRechargesByTime(userRoleId, startTime, endTime);
	}
	
	/**获取该玩家的一段时间的充值记录**/
	public List<DanfuChargeRankVo> getTotalRechargesByTime(long startTime,long endTime,int limit,int minCharge){
		return rechargeDao.getTotalRechargesByTime(startTime, endTime,limit,minCharge);
	}
	
	/**
	 * 获取总充值额
	 * @return
	 */
	public long getTotalRechargeSum(){
		return rechargeDao.getTotalRechargeSum();
	}
	
	/**
	 * 获取时间区间内总充值额
	 * @return
	 */
	public long getTimeRechargeSum(long startTime,long endTime){
		return rechargeDao.getTimeRechargeSum(startTime, endTime);
	}
}