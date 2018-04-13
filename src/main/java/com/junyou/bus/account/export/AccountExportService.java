package com.junyou.bus.account.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.account.entity.RoleAccount;
import com.junyou.bus.account.entity.RoleAccountWrapper;
import com.junyou.bus.account.service.RoleAccountService;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;

/**
 * 玩家账号Export
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2014-11-28 下午4:47:01
 */
@Component
public class AccountExportService {

	@Autowired
	private RoleAccountService accountService;
	
	/**
	 * 初始化玩家角色货币信息
	 * @param userRoleId
	 * @return
	 */
	public RoleAccount initRoleAccount(Long userRoleId){
		return accountService.initRoleAccountFromDb(userRoleId);
	}
	/**
	 * 获取玩家角色货币信息
	 * @param userRoleId
	 * @return
	 */
	public RoleAccount getRoleAccount(Long userRoleId){
		return accountService.getRoleAccount(userRoleId);
	}
	
	/**获取玩家角色货币信息直接访问数据库**/
	public RoleAccount getRoleAccountFromDb(String userId,String serverId){
		return accountService.getRoleAccountFromDb(userId, serverId);
	}
	
	
	/**
	 * 创建玩家角色货币信息直接访问库
	 * @param userRoleId
	 */
	public void createRoleAccount(Long userRoleId,String userId,String serverId){
		accountService.createRoleAccount(userRoleId,userId,serverId);
	}
	
	/**
	 * 根据货币类型获取值
	 * @param type  {@link GameConstants}
	 * @param userRoleId
	 * @return
	 */
	public long getCurrency(int type, Long userRoleId) {
		return accountService.getCurrency(type, userRoleId);
	}
 
	
	/**
	 * 是否有足够货币
	 * @param type
	 * @param value
	 * @param userRoleId
	 * @return  返回null 表示足够
	 */
	public Object[] isEnought(int type,long value,Long userRoleId){ 
		return accountService.isEnought(type, value, userRoleId);
	}
	
	/**
	 * 是否有足够货币
	 * @param type
	 * @param value
	 * @param userRoleId
	 * @return  返回null 表示足够
	 */
	public Object[] isEnoughtValue(int type,long value,Long userRoleId){ 
		return accountService.isEnoughtValue(type, value, userRoleId);
	}
	
	/**
	 * 消耗对应货币  包含消耗后的消息推送
	 * @param type {@link GameConstants{金币，元宝等}}
	 * @param value
	 * @param userRoleId
	 * @param logType
	 * @param isNoXF 是否参与消费统计(true:是)
	 * @return null:消耗成功   其他的为code值
	 */
	public Object[] decrCurrencyWithNotify(int type, int value, Long userRoleId,int logType, boolean isNoXF,int beizhu){
		return accountService.decrCurrencyWithNotify(type, value, userRoleId, logType, isNoXF, beizhu);
	}
	
	/**
	 * 消耗对应货币  包含消耗后的消息推送
	 * @param type {@link GameConstants{金币，元宝等}}
	 * @param value
	 * @param userRoleId
	 * @param logType
	 * @param isNoXF 是否参与消费统计(true:是)
	 * @return null:消耗成功   其他的为code值
	 */
	public Object[] decrCurrencyForTradeWithNotify(int type, long value, Long userRoleId,int logType, boolean isNoXF,int beizhu){
		return accountService.decrCurrencyForTradeWithNotify(type, value, userRoleId, logType, isNoXF, beizhu);
	}
	
	/**
	 * 添加对应货币  包含添加后的消息推送
	 * @param type {@link GoodsCategory 这个类中的负数  {金币，元宝,绑定元宝等各种数值类型的添加}}
	 * @param inVal
	 * @param userRoleId
	 * @param logType{@link LogPrintHandle}
	 * @return 实际添加的值
	 */
	public long incrCurrencyWithNotify(int type, long inVal, long userRoleId, int logType, int  beizhu){
		return accountService.incrCurrencyWithNotify(type, inVal, userRoleId, logType, beizhu);
	}
	
	/**
	 * 充值到账号
	 * @param roleAccount
	 * @param addYb
	 * @param reType 充值类型
	 * @return
	 */
	public int revRecharge(RoleAccount roleAccount, Long addYb,int reType){
		return accountService.revRecharge(roleAccount, addYb, reType);
	}
	/**
	 * 设置账户金额（仅限GM功能使用）
	 * @param userRoleId
	 * @param type
	 * @param count
	 */
	public void setAccount(Long userRoleId,int type,long count){
		accountService.setAccount(userRoleId, type, count);
	}
	
	/**
	 * 获取账户信息
	 * @param userRoleId
	 * @return
	 */
	public RoleAccountWrapper getAccountWrapper(Long userRoleId){
		return accountService.getAccountWrapper(userRoleId);
	}
}