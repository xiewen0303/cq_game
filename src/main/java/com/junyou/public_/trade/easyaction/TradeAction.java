package com.junyou.public_.trade.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.module.GameModType;
import com.junyou.public_.trade.TradeOutPutWrapper;
import com.junyou.public_.trade.TradeParam;
import com.junyou.public_.trade.service.TradeChangeService;
import com.junyou.public_.trade.service.TradeService;
import com.junyou.public_.tunnel.PublicMsgSender;
import com.junyou.utils.common.CovertObjectUtil;
import com.kernel.pool.executor.Message;
 
/**
 * 交易
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-1-15 下午5:15:01
 */
@Controller
@EasyWorker(moduleName = GameModType.TRADE,groupName=EasyGroup.PUBLIC)
public class TradeAction {
	
	@Autowired
	private TradeService tradeService;
	@Autowired
	private TradeChangeService tradeChangeService;
	
	/**
	 * 发起交易
	 * @param context
	 */
	@EasyMapping(mapping = ClientCmdType.TRADE_LAUNCH)
	public void launch(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Long otherRoleId = CovertObjectUtil.obj2long(inMsg.getData());
		
		//参数有效验证
		if(otherRoleId.longValue() <= 0){
			return;
		}
		
		Object[] result = tradeService.launch(userRoleId, otherRoleId);
		if((Boolean)result[0]){ 
			//通知被交易者请求
			PublicMsgSender.send2One(otherRoleId, ClientCmdType.TRADE_LAUNCH, result[1]);
//			//通知交易发起在,邀请成功
//			PublicMsgSender.send2One(userRoleId, ClientCmdType.TRADE_LAUNCH_SUCCESS, null);
		}else{
			//请求失败 通知自己
			PublicMsgSender.send2One(userRoleId, ClientCmdType.TRADE_REJECT,result[1]);
		}
	}
	
	/**
	 * 对方同意交易   
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TRADE_AGREE)
	public void agree(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Long otherRoleId = CovertObjectUtil.object2Long(inMsg.getData());
		//参数有效验证
		if(otherRoleId == null || otherRoleId == 0l|| userRoleId.equals(otherRoleId)){
			return;
		}
		
		Object[] result = tradeService.agree(userRoleId, otherRoleId);
		if((Boolean)result[0]){
			//通知被交易者请求
			PublicMsgSender.send2One(otherRoleId, ClientCmdType.TRADE_AGREE, result[1]);
			
//			//通知自己交易请求成功
//			PublicMsgSender.send2One(userRoleId, ClientCmdType.TRADE_SELF_AGREE,new Object[]{1});
		}else{
//			//请求失败 通知自己 
//			PublicMsgSender.send2One(userRoleId, ClientCmdType.TRADE_SELF_AGREE,new Object[]{0,result[1]});
		 
			//请求失败 通知对方
			PublicMsgSender.send2One(otherRoleId, ClientCmdType.TRADE_REJECT, new Object[]{result[1]});
		}
	}
	
	/**
	 * 主动拒绝交易
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TRADE_REJECT)
	public void reject(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Long otherRoleId = CovertObjectUtil.obj2long(inMsg.getData());
		//参数有效验证
		if(otherRoleId.longValue() <= 0l){
			return;
		}
		
		Object[] result = tradeService.reject(userRoleId, otherRoleId);
		
		//通知交易发起者 被拒绝
		PublicMsgSender.send2One(otherRoleId, ClientCmdType.TRADE_REJECT, result);
	}
	
	/**
	 * 修改金钱 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TRADE_MODIFY_MONEY)
	public void modifyMoney(Message inMsg){
		Long userRoleId = inMsg.getRoleId(); 
		
		boolean flag = tradeService.isActive(userRoleId);
		if(!flag) return;
		
		
		Long yb = CovertObjectUtil.object2Long(inMsg.getData());
		
		if(yb > GameConstants.YB_MAX){
			yb =GameConstants.YB_MAX;
		}
		
		if(yb <0 ){
			yb = 0L;
		}
		
		Object[] result = tradeService.modifyMoney(userRoleId, yb);
		if((Boolean)result[0]){
			//修改 成功
			Long otherRoleId = (Long) result[1];
			Object finalMoneyData =  result[2];
			//通知另一个交易玩家交易金钱变化
			PublicMsgSender.send2One(otherRoleId, ClientCmdType.TRADE_MODIFY_MONEY, finalMoneyData);
		}else{
			Object notifyData = result[1];
			Long otherRoleId = tradeService.cancel(userRoleId);
			//通知自己修改失败
			PublicMsgSender.send2One(userRoleId, ClientCmdType.TRADE_CANCEL, notifyData);
			
			if(otherRoleId != null){
				//通知对方修改失败
				PublicMsgSender.send2One(otherRoleId, ClientCmdType.TRADE_CANCEL, notifyData);
			}
		}
	}
	
	
	/**
	 * 修改物品 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TRADE_MODIFY_GOODS)
	public void modifyGoods(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] data = inMsg.getData();
		int tradeIndex = (Integer) data[0];
		Long guid =CovertObjectUtil.object2Long(data[1]);
		Integer goodsCount = (Integer) data[2];
		
		boolean flag = tradeService.isActive(userRoleId);
		if(!flag) return;
		Object[] result = tradeService.modifyGoods(userRoleId,tradeIndex,guid,goodsCount);
		if((Boolean) result[0]){
			//修改 成功
			Long otherRoleId = (Long) result[1];
			Object[] outData = (Object[]) result[2];
			//通知另一个交易玩家交易物品变化
			PublicMsgSender.send2One(otherRoleId, ClientCmdType.TRADE_MODIFY_GOODS, outData);
			
		}else{
			Object notifyData = result[1];
			if(!(notifyData instanceof Object[])){
				notifyData = new Object[]{notifyData};
			}
		 
			//通知交易中断
			PublicMsgSender.send2One(userRoleId, ClientCmdType.TRADE_CANCEL,notifyData);
			
			Long otherRoleId = tradeService.cancel(userRoleId);
			if(otherRoleId != null){
				//通知交易中断
				PublicMsgSender.send2One(otherRoleId, ClientCmdType.TRADE_CANCEL,notifyData);
			}
		}
	}
	 
	
	
	/**
	 * 客户端主动取消交易
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TRADE_CANCEL)
	public void cancel(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Long otherRoleId = tradeService.cancel(userRoleId);
		if(otherRoleId != null){
			//通知另一个交易玩家交易取消
			String name = tradeService.getRoleName(userRoleId);
			PublicMsgSender.send2One(otherRoleId, ClientCmdType.TRADE_CANCEL, AppErrorCode.createError(AppErrorCode.TRADE_CANCEL_CODE,name));
		}
	}
	
	/**
	 * 确认
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TRADE_CONFIRM1)
	public void confirm1(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] confirmCheck = tradeService.confirm1(userRoleId);
		if(!(Boolean) confirmCheck[0]){
			Object notifyData = confirmCheck[1];
			if(!(notifyData instanceof Object[])){
				notifyData = new Object[]{notifyData};
			}
			
			//通知交易中断
			PublicMsgSender.send2One(userRoleId, ClientCmdType.TRADE_CANCEL, notifyData);
			
			Long otherRoleId = tradeService.cancel(userRoleId);
			if(otherRoleId != null){
				//通知交易中断
				PublicMsgSender.send2One(otherRoleId, ClientCmdType.TRADE_CANCEL, notifyData);
			}
			return;
		}
		
		Long otherRoleId = (Long) confirmCheck[1];
		//通知另一个交易玩家  确认交易
		PublicMsgSender.send2One(otherRoleId, ClientCmdType.TRADE_CONFIRM1, null);
	}
	
	/**
	 * 确认2(客户端得知双方按下确认按钮后发送确认消息)
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TRADE_CONFIRM2)
	public void confirm2(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] confirmCheck = tradeService.confirm2(userRoleId);
		if((Boolean) confirmCheck[0]){
			Long otherRoleId = (Long) confirmCheck[1];
			//通知另一个交易玩家  确认交易
			PublicMsgSender.send2One(otherRoleId, ClientCmdType.TRADE_CONFIRM2, null);
		}else{
			Object notifyData = confirmCheck[1];
			
			if(!(notifyData instanceof Object[])){
				notifyData = new Object[]{notifyData};
			}
			//通知交易中断
			PublicMsgSender.send2One(userRoleId, ClientCmdType.TRADE_CANCEL,notifyData);
			
			Long otherRoleId = tradeService.cancel(userRoleId);
			if(otherRoleId != null){
				//通知交易中断
				PublicMsgSender.send2One(otherRoleId, ClientCmdType.TRADE_CANCEL, notifyData);
			}
		}
	}
	
	
	/**
	 * 确认3(反馈收到对方确认2)
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TRADE_CONFIRM3)
	public void confirm3(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] confirmCheck = tradeService.confirm3(userRoleId);
		if(!(Boolean) confirmCheck[0]){
			Object notifyData = confirmCheck[1];
			
			if(!(notifyData instanceof Object[])){
				notifyData = new Object[]{notifyData};
			}
			//通知交易中断
			PublicMsgSender.send2One(userRoleId, ClientCmdType.TRADE_CANCEL, notifyData);
			
			Long otherRoleId = tradeService.cancel(userRoleId);
			if(otherRoleId != null){
				//通知交易中断
				PublicMsgSender.send2One(otherRoleId, ClientCmdType.TRADE_CANCEL, notifyData);
			}
			return;
		}
		
		Long otherRoleId = (Long) confirmCheck[1];
		
		/**
		 * ==== 开始数据交换====
		 */
		
		//双方都确认  
		if(confirmCheck.length == 3){
			TradeParam tradeParam = new TradeParam();
			
			tradeChangeService.tradeChangeData(userRoleId, otherRoleId,tradeParam);
			if(!tradeParam.isSuccess()){
				tradeService.cancel(userRoleId);
				
				//通知自己修改失败
				PublicMsgSender.send2One(userRoleId, ClientCmdType.TRADE_CANCEL, tradeParam.getErrorCode());
				PublicMsgSender.send2One(otherRoleId, ClientCmdType.TRADE_CANCEL, tradeParam.getErrorCode());
				
				return;
			}
			
			//组装提示信息参数
			Object[] selfParam = TradeOutPutWrapper.finishTrade(tradeParam.getSelfClientParams(), tradeParam.getSelfMoney(), tradeParam.getOtherName());
			Object[] otherParam = TradeOutPutWrapper.finishTrade(tradeParam.getOtherClientParams(), tradeParam.getOtherMoney(), tradeParam.getSelfName());
			
			//通知双方玩家交易完成
			PublicMsgSender.send2One(userRoleId, ClientCmdType.TRADE_FINISH, selfParam);
			PublicMsgSender.send2One(otherRoleId, ClientCmdType.TRADE_FINISH, otherParam);
		}
	}
}
