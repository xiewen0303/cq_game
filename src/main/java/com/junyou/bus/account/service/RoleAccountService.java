package com.junyou.bus.account.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.dao.RoleAccountDao;
import com.junyou.bus.account.entity.RoleAccount;
import com.junyou.bus.account.entity.RoleAccountWrapper;
import com.junyou.bus.huoyuedu.enums.ActivityEnum;
import com.junyou.bus.huoyuedu.export.HuoYueDuExportService;
import com.junyou.bus.platform.export.PlatformExportService;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.rfbactivity.export.RefabuActivityExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.err.AppErrorCode;
import com.junyou.event.ComsumeNumLogEvent;
import com.junyou.event.GetNumLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.trade.TradeData;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.spring.container.DataContainer;
import com.kernel.sync.annotation.Sync;

/**
 * 玩家角色货币Service
 *  
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2014-11-28 下午5:04:26 
 */
@Service
public class RoleAccountService {
	
	private static final String TEST_PT_ID = "hwhqg";
	private static final String TEST_17_PT_ID = "17kaifu";
	private static final long YB_10W = 100000L;

	@Autowired
	private RoleAccountDao roleAccountDao;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private RefabuActivityExportService refabuActivityExportService;
	@Autowired 
	private HuoYueDuExportService huoYueDuExportService;
	@Autowired
	private PlatformExportService platformExportService;
	
	
	/**获取玩家角色货币信息直接访问缓存**/
	public RoleAccount getRoleAccount(Long userRoleId){
		return roleAccountDao.cacheLoad(userRoleId, userRoleId);
	}
	
	/**获取玩家角色货币信息直接访问数据库**/
	public RoleAccount getRoleAccountFromDb(String userId,String serverId){
		return roleAccountDao.getRoleAccountFromDb(userId, serverId);
	}
	
	/**是否会超过最大值    @return true:会**/
	@Sync(component = GameConstants.COMPONENT_BUS_SHARE,indexes = {1})
	private boolean isOverMax(Long userRoleId, long addValue){
		long ybCount = getCurrency(GoodsCategory.GOLD, userRoleId);
		
		if(GameConstants.YB_MAX - ybCount < addValue){
			return true;
		}else{
			return false;
		}
	}
	
	/**初始化玩家角色货币信息直接访问库**/
	public RoleAccount initRoleAccountFromDb(Long userRoleId){
		return roleAccountDao.initRoleAccountFromDb(userRoleId);
	}
	
	
	/**创建玩家角色货币信息直接访问库**/
	public void createRoleAccount(Long userRoleId,String userId,String serverId){
		RoleAccount roleAccount = initRoleAccountFromDb(userRoleId);
		if(roleAccount != null){
			return;
		}
		
		roleAccount = new RoleAccount();
		roleAccount.setUserId(userId);
		roleAccount.setServerId(serverId);
		roleAccount.setUserRoleId(userRoleId);
		roleAccount.setJb(0l);
		
		String platformId = GameServerContext.getGameAppConfig().getPlatformId();
		Long noReYb = 0L;
		if(TEST_PT_ID.equals(platformId) || TEST_17_PT_ID.equals(platformId)){
			noReYb = YB_10W;
		}
		roleAccount.setNoReYb(noReYb);
		roleAccount.setReYb(0l);
		roleAccount.setUserType(GameConstants.USER_TYPE_PLAYER);
		roleAccount.setUpdateTime(GameSystemTime.getSystemMillTime());
		roleAccount.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		
		roleAccountDao.insertRoleAccountFromDb(roleAccount);
	}
	
	/**根据类型获取玩家货币值（包括金币，绑定元宝，元宝）**/
	@Sync(component = GameConstants.COMPONENT_BUS_SHARE,indexes = {1})
	public long getCurrency(int type, Long userRoleId) {

		RoleAccount roleAccount = getRoleAccount(userRoleId);
		if(roleAccount == null){
			return 0l;
		}
		
		switch (type) {

		case GoodsCategory.MONEY://金币
			return roleAccount.getJb();

		case GoodsCategory.BGOLD://绑定元宝
			return roleAccount.getBindYb();

		case GoodsCategory.GOLD://元宝
			return roleAccount.getReYb() + roleAccount.getNoReYb();

		default:
			return 0l;
		}
	}
	
	/**增加对应货币 包含增加后的消息推送（包括金币，绑定元宝，元宝）**/
	public long incrCurrencyWithNotify(int type, long inVal, long userRoleId, int logType, int  beizhu){
		if(inVal < 0){
			return 0l;
		}
		RoleAccount roleAccount = getRoleAccount(userRoleId);
		if(roleAccount == null){
			return 0l;
		}
		
		//判断是否会超过最大值
		if(type == GoodsCategory.GOLD && isOverMax(userRoleId, inVal)){
			return 0l;
		}
		
		short cmd = 0;
		Long consuNoReYb = null;//添加充值元宝中非充值元宝
		long beforeVal = 0l;//添加前值
		long afterVal = 0l;//添加后值
		
		switch (type) {
		case GoodsCategory.MONEY:
			double chenmiRate = roleExportService.getChenmiIncomeRate(userRoleId);//游戏币防沉迷处理
			cmd = ClientCmdType.MONEY_CHANGE;
			afterVal = (long) (roleAccount.getJb() + inVal * chenmiRate * 1l);
			beforeVal = roleAccount.getJb();
			
			roleAccount.setJb(afterVal);
			//成就 
			try {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_YINLIANG, (int) afterVal});
				//roleChengJiuExportService.tuisongChengJiu(userRoleId, GameConstants.CJ_YINLIANG, (int) afterVal);
			} catch (Exception e) {
				ChuanQiLog.error("",e);
			}
			break;

		case GoodsCategory.BGOLD:
			cmd = ClientCmdType.BANG_YB_CHANGE;
			afterVal = roleAccount.getBindYb() + inVal * 1l;
			beforeVal = roleAccount.getBindYb();
			
			roleAccount.setBindYb(afterVal);
			break;

		case GoodsCategory.GOLD:
			cmd = ClientCmdType.YUANBAO_CHANGE;
			beforeVal = roleAccount.getYb();
			
			roleAccount.setNoReYb(roleAccount.getNoReYb() + inVal);
			consuNoReYb = inVal;
			
			afterVal = roleAccount.getYb();
			break;
			
		default:
			return 0l;
		}
		
		roleAccountDao.cacheUpdate(roleAccount, userRoleId);
		BusMsgSender.send2One(userRoleId, cmd, afterVal);//发送给客户端数值变化
		
		String userName = roleExportService.getLoginRole(userRoleId).getName();
		GamePublishEvent.publishEvent(new GetNumLogEvent(logType, userRoleId, userName, type, inVal, null, consuNoReYb, beforeVal, afterVal, beizhu));
		return afterVal - beforeVal;
	}
	
	/**
	 * 修改玩家角色货币信息直接访问库
	 * @param account
	 */
	public void updateRoleAccountFromDb(RoleAccount account) {
		roleAccountDao.updateRoleAccountFromDb(account);
	}
	/**
	 * 修改玩家角色货币信息直接访问库
	 * @param account
	 */
	public void updateRoleAccountFromCache(RoleAccount account) {
		roleAccountDao.update(account, account.getUserRoleId(), roleAccountDao.getAccessType());
	}
	
	/**
	 * 充值到账号
	 * @param roleAccount
	 * @param addYb
	 * @param reType 充值类型
	 * @return
	 */
	public int revRecharge(RoleAccount roleAccount, Long addYb,int reType){
		if(GameConstants.YB_MAX - roleAccount.getNoReYb() - roleAccount.getReYb() < addYb){
			return AppErrorCode.ERROR_YB_OVER_MAX;
		}
		if(reType == GameConstants.PLAYER_RECHARGE){
			roleAccount.setReYb(roleAccount.getReYb() + addYb);
		}else{
			roleAccount.setNoReYb(roleAccount.getNoReYb() + addYb);
			
			//如果是普通玩家账号接收到内部充值，则标记为托
			if(roleAccount.getUserType() == GameConstants.USER_TYPE_PLAYER && reType == GameConstants.INNER_RECHARGE){
				roleAccount.setUserType(GameConstants.USER_TYPE_TUO);
			}
			ChuanQiLog.error("非正常充值：玩家id[{}],玩家类型[{}],充值类型[{}]",roleAccount.getUserRoleId(),roleAccount.getUserType(),reType);
		}
		
		roleAccountDao.cacheUpdate(roleAccount, roleAccount.getUserRoleId());
		//通知元宝变化
		BusMsgSender.send2One(roleAccount.getUserRoleId(), ClientCmdType.YUANBAO_CHANGE, roleAccount.getYb());//发送给客户端数值变化
		
		return AppErrorCode.SUCCESS;
	}
	
	/**
	 * 设置账户金额（仅限GM功能使用）
	 * @param userRoleId
	 * @param type
	 * @param count
	 */
	public void setAccount(Long userRoleId,int type,long count){
		if(count > GameConstants.YB_MAX){
			return;
		}
		RoleAccount roleAccount = getRoleAccount(userRoleId);
		short cmd = 0;
		switch (type) {
		case GoodsCategory.MONEY://金币
			roleAccount.setJb(count);
			cmd = ClientCmdType.MONEY_CHANGE;
			break;
		case GoodsCategory.BGOLD://绑定元宝
			roleAccount.setBindYb(count);
			cmd = ClientCmdType.BANG_YB_CHANGE;
			break;
		case GoodsCategory.GOLD://元宝
			roleAccount.setNoReYb(count);
			count = roleAccount.getYb();
			cmd = ClientCmdType.YUANBAO_CHANGE;
			break;
		default	:
			return;
		}
		
		roleAccountDao.cacheUpdate(roleAccount, userRoleId);
		//通知元宝变化
		BusMsgSender.send2One(roleAccount.getUserRoleId(), cmd, count);//发送给客户端数值变化
	}
	
	/**
	 * 获取账户信息
	 * @param userRoleId
	 * @return
	 */
	public RoleAccountWrapper getAccountWrapper(Long userRoleId){
		RoleAccount roleAccount = getRoleAccount(userRoleId);
		if(roleAccount != null){
			return new RoleAccountWrapper(roleAccount);
		}
		return null;
	}
	
	/**
	 * 是否有足够货币（验证交易状态）
	 * @param type  {@link GameConstants{金币，元宝等}}
	 * @param value
	 * @param userRoleId
	 * @return 返回null足够，否则[0,code]
	 */
	public Object[] isEnought(int type,long value,Long userRoleId){
		if(value < 0){
			return AppErrorCode.NUMBER_ERROR;
		}
		if(GoodsCategory.GOLD == type){
			TradeData data = dataContainer.getData(GameConstants.COMPONENET_TREAD_NAME, userRoleId.toString());
			if(data != null){
				return AppErrorCode.TRADE_YB_ERROR;
			}
		}
		
		Object[] enought = isEnoughtValue(type, value, userRoleId);
		if(null != enought){
			return enought;
		}
		return null;
	}
	
	/**
	 * 是否有足够货币（不验证交易状态）
	 * @param type  {@link GameConstants{金币，元宝等}}
	 * @param value
	 * @param userRoleId
	 * @return true:足够,false:不够
	 */
	public Object[] isEnoughtValue(int type,long value,Long userRoleId){
		if(value < 0){
			return AppErrorCode.NUMBER_ERROR;
		}
		long val = getCurrency(type, userRoleId);
		if(val < value){
			return getEnoughtCode(type);
		}
		return null;
	}
	
	/**
	 * 根据获取类型获取Code
	 * @param type
	 * @return
	 */
	private Object[] getEnoughtCode(int type){
		switch (type) {

		case GoodsCategory.MONEY://金币
			return AppErrorCode.JB_ERROR;

		case GoodsCategory.BGOLD://绑定元宝
			return AppErrorCode.LQ_ERROR;

		case GoodsCategory.GOLD://元宝
			return AppErrorCode.YB_ERROR;

		default:
			return AppErrorCode.ENOUGHT_ERROR;
		}
	}
	
	/**
	 * 消耗对应货币  包含消耗后的消息推送(仅供交易使用)
	 * @param type {@link GameConstants{金币，元宝等}}
	 * @param deVal
	 * @param userRoleId
	 * @param logType{@link LogPrintHandle}
	 * @param isNoXF 是否参与消费统计(true:是)
	 * @return  返回null足够，否则[0,code]
	 */
	public Object[] decrCurrencyForTradeWithNotify(int type, long deVal, long userRoleId,int logType, boolean isNoXF, int beizhu){
		if(deVal < 0){
			return AppErrorCode.NUMBER_ERROR;
		}
		Object[] isOb = isEnoughtValue(type, deVal, userRoleId);
		if(isOb != null){
			return isOb;
		}
		
		return decrCurrency(type, deVal, userRoleId, logType, isNoXF, beizhu);
	}
	
	/**
	 * 消耗对应货币  包含消耗后的消息推送
	 * @param type {@link GameConstants{金币，元宝等}}
	 * @param deVal
	 * @param userRoleId
	 * @param logType{@link LogPrintHandle}
	 * @param isNoXF 是否参与消费统计(true:是)
	 * @return  返回null足够，否则[0,code]
	 */
	public Object[] decrCurrencyWithNotify(int type, long deVal, long userRoleId,int logType, boolean isNoXF, int beizhu){
		if(deVal < 0){
			return AppErrorCode.NUMBER_ERROR;
		}
		Object[] isOb = isEnought(type, deVal, userRoleId);
		if(isOb != null){
			return isOb;
		}
		
		return decrCurrency(type, deVal, userRoleId, logType, isNoXF, beizhu);
	}
	
	private Object[] decrCurrency(int type, long deVal, long userRoleId,int logType, boolean isNoXF, int beizhu){
		if(deVal < 0){
			return AppErrorCode.NUMBER_ERROR;
		}
		RoleAccount roleAccount = getRoleAccount(userRoleId);
		if(roleAccount == null){
			return getEnoughtCode(type);
		}
		short cmd = 0;
		Long consuReYb = null;//消耗充值元宝
		Long consuNoReYb = null;//消耗充值元宝中非充值元宝
		long beforeVal = 0l;//消耗前值
		
		switch (type) {
		case GoodsCategory.MONEY:
			cmd = ClientCmdType.MONEY_CHANGE;
			beforeVal = roleAccount.getJb();
			
			roleAccount.setJb(beforeVal - deVal);
			//修炼任务
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.XIAOHAO_YINGLIANG, deVal});
			break;

		case GoodsCategory.BGOLD:
			cmd = ClientCmdType.BANG_YB_CHANGE;
			beforeVal = roleAccount.getBindYb();
			
			roleAccount.setBindYb(beforeVal - deVal);
			//活跃度lxn
			huoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A12);
			//星空宝藏
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XKBZ_XIAOFEI_BGOLD,deVal);
			break;

		case GoodsCategory.GOLD:
			cmd = ClientCmdType.YUANBAO_CHANGE;
			long val = roleAccount.getNoReYb() - deVal;//发放元宝-扣除元宝数，例如（0-20=-20）
			long beReYb = roleAccount.getReYb();
			long beNoReYb = roleAccount.getNoReYb();
			beforeVal = beReYb + beNoReYb;
			
			roleAccount.setNoReYb(val < 0 ? 0 : val);//先消耗充值元宝中的非充值元宝（返利等）
			roleAccount.setReYb(val > 0 ? roleAccount.getReYb() : roleAccount.getReYb() + val);//发放元宝不足（-20）则在扣除充值元宝
			
			consuReYb = beReYb - roleAccount.getReYb();
			consuNoReYb = beNoReYb - roleAccount.getNoReYb();
			
			//消费元宝统计
			if(isNoXF){
				xiaofeiYbMonitor(userRoleId, deVal);
			}
			//活跃度lxn
			huoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A11);
			//360V计划统计元宝消耗
			platformExportService._360VplanRoleRecharge(userRoleId, deVal);
			//37V计划统计元宝消耗
			platformExportService._37VplanRoleRecharge(userRoleId, deVal);
			break;
			
		default:
			return AppErrorCode.ENOUGHT_ERROR;
		}
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		String userName = role.getName();
		int level = role.getLevel();
		GamePublishEvent.publishEvent(new ComsumeNumLogEvent(logType, userRoleId, userName, type, deVal, consuReYb, consuNoReYb, beforeVal, beforeVal - deVal, beizhu,level));
		roleAccountDao.cacheUpdate(roleAccount, userRoleId);
		BusMsgSender.send2One(userRoleId, cmd, beforeVal - deVal);//发送给客户端数值变化
		
		return null;
	}
	
	/**
	 * 元宝消费监听器处理业务
	 * @param userRoleId
	 * @param addYb
	 */
	private void xiaofeiYbMonitor(Long userRoleId,Long yb){
		try {
			refabuActivityExportService.xfYbRefbMonitorHandle(userRoleId, yb);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		try {
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_XIAOFEIYUANBAO,  yb.intValue()});
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.XIAOFEI_ZUANSHI, yb.intValue()});
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		try {
			//腾讯罗盘数据
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_RECHAGE_LUOPAN, new Object[]{userRoleId, QqConstants.CONSUME, yb.intValue()*10});
				//tencentLuoPanExportService.tencentRechageLuoPan(userRoleId, QqConstants.CONSUME, yb);
			}
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		
	}
	
}