package com.junyou.bus.cuxiao.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.junyou.bus.cuxiao.configure.export.JingJieFanLiConfig;
import com.junyou.bus.cuxiao.configure.export.JingJieFanLiConfigExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.xianjian.export.XianJianExportService;
import com.junyou.bus.xinwen.export.XinwenExportService;
import com.junyou.bus.zhanjia.export.ZhanJiaExportService;
import com.junyou.constants.GameConstants;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.utils.common.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.chibang.export.ChiBangExportService;
import com.junyou.bus.cuxiao.configure.export.CuxiaoConfig;
import com.junyou.bus.cuxiao.configure.export.CuxiaoConfigExportService;
import com.junyou.bus.cuxiao.constants.CuxiaoConstants;
import com.junyou.bus.cuxiao.dao.RoleCuxiaoDao;
import com.junyou.bus.cuxiao.entity.RoleCuxiao;
import com.junyou.bus.task.export.TaskExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.wuqi.export.WuQiExportService;
import com.junyou.bus.zuoqi.export.ZuoQiExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.CuxiaoRewardGetLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

@Service
public class CuxiaoService {

	@Autowired
	private RoleCuxiaoDao roleCuxiaoDao;
	@Autowired
	private CuxiaoConfigExportService cuxiaoConfigExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private ZuoQiExportService zuoQiExportService;
	@Autowired
	private ChiBangExportService chiBangExportService;
	@Autowired
	private TaskExportService taskExportService;
	@Autowired
	private WuQiExportService wuQiExportService;
	@Autowired
	private JingJieFanLiConfigExportService jingJieFanLiConfigExportService;
	@Autowired
	private EmailExportService emailExportService;
    @Autowired
    private XianJianExportService xianJianExportService;
	@Autowired
	private ZhanJiaExportService zhanJiaExportService;

	

	/**
	 * 玩家登陆推送client那些任务已领了奖励
	 */
	public List<RoleCuxiao> initRoleCuxiao(Long userRoleId) {
		List<RoleCuxiao> data = roleCuxiaoDao.initRoleCuxiao(userRoleId);
		Object[] clientData = null;
		if (data.size() > 0) {
			clientData = new Object[data.size()];
			for (int i = 0; i < data.size(); i++) {
				clientData[i] = data.get(i).getConfigId();
			}
			BusMsgSender.send2One(userRoleId, ClientCmdType.CUXIAO_NOTICE_TO_CLIENT, clientData);
		}
		return data;
	}

	/**
	 * 领取促销奖励 奖励2选1的
	 */
	public Object[] getRewards( Long userRoleId, int configId) {

		CuxiaoConfig cuxiaoConfig = cuxiaoConfigExportService.getConfig(configId);
		if (cuxiaoConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		//获取玩家完成的主线任务 促销配置表里面配置完成任务都是主线任务，只要达到了这个任务就弹出奖励面板，不需要完成这个任务
		Object[] taskData = taskExportService.getTask(userRoleId); 
		if(taskData==null){
			return  AppErrorCode.TASK_NOT_FINISH;
		}
		int taskId = (int)taskData[0];
		if(taskId<cuxiaoConfig.getTaskId()){
			return  AppErrorCode.TASK_NOT_FINISH;
		}
		RoleCuxiao roleCuxiao = getCuxiaosByTaskId(userRoleId,configId);
		if(roleCuxiao!=null){
			return  AppErrorCode.GET_ALREADY;
		}
		
		if(cuxiaoConfig.getRewards()!=null){
			Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(cuxiaoConfig.getRewards(), userRoleId);
			// 背包空间不足 请先清理背包
			if (code != null) {
				return code;
			}
		}
		
		int state = CuxiaoConstants.ORDINARY_GET;
		if(cuxiaoConfig.getId1()==CuxiaoConstants.GOLD_GET){
			//检查元宝
			Object[] result = accountExportService.isEnought(GoodsCategory.GOLD,cuxiaoConfig.getConsumeGold(), userRoleId);
			if (result != null) {
				return result;
			}
			state =CuxiaoConstants.GOLD_GET;
			//御剑直升
			if(cuxiaoConfig.getType()==CuxiaoConstants.SJ_YUJIAN){
				Integer level = CovertObjectUtil.object2Integer(cuxiaoConfig.getData1());
				if(level!=null){
				  Object[] ret = zuoQiExportService.sjByCuxiao(userRoleId, level); //等级从0开始
				  if(ret!=null){
					  return ret;
				  }
				}
			}
			//翅膀直升
			if(cuxiaoConfig.getType()==CuxiaoConstants.SJ_CHIBANG){
				Integer level =  CovertObjectUtil.object2Integer(cuxiaoConfig.getData1());
				if(level!=null){
				  Object[] ret = chiBangExportService.sjByCuxiao(userRoleId, level); //等级从0开始
				  if(ret!=null){
					  return ret;
				  }
				}
			}
			//圣剑
			if(cuxiaoConfig.getType()==CuxiaoConstants.SJ_SHENGJIAN){
				Integer level =  CovertObjectUtil.object2Integer(cuxiaoConfig.getData1());
				if(level!=null){
				  Object[] ret = wuQiExportService.sjByCuxiao(userRoleId, level); //Id从0开始
				  if(ret!=null){
					  return ret;
				  }
				}
			}
			
		} 
		
		roleCuxiao  = new RoleCuxiao();
		roleCuxiao.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		roleCuxiao.setUserRoleId(userRoleId);
		roleCuxiao.setState(state);
		roleCuxiao.setTaskId(cuxiaoConfig.getTaskId());
		roleCuxiao.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		roleCuxiao.setConfigId(cuxiaoConfig.getId());
		roleCuxiaoDao.cacheInsert(roleCuxiao, userRoleId);
		// 进背包 消耗元宝 日志
		if(state == CuxiaoConstants.GOLD_GET){
		accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, cuxiaoConfig.getConsumeGold(), userRoleId, LogPrintHandle.CONSUME_CUXIAO_GET_REWARD, true, LogPrintHandle.CBZ_CUXIAO_GET_REWARD);
		}
		if(cuxiaoConfig.getRewards()!=null && cuxiaoConfig.getRewards().size()>0){
			roleBagExportService.putGoodsAndNumberAttr(cuxiaoConfig.getRewards(), userRoleId, GoodsSource.CUXIAO_GET, LogPrintHandle.GET_CUXIAO_REWARD, LogPrintHandle.GBZ_CUXIAO_REWARD_LB, true);
		}
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(cuxiaoConfig.getRewards(), null);
		GamePublishEvent.publishEvent(new CuxiaoRewardGetLogEvent(userRoleId,cuxiaoConfig.getId(),cuxiaoConfig.getType(), jsonArray, cuxiaoConfig.getTaskId(),cuxiaoConfig.getId1()));

		return new Object[]{1,configId};
	}
	/**
	 * 获取促销entity
	 * @param userRoleId
	 * @param taskId
	 * @return
	 */
	private  RoleCuxiao  getCuxiaosByTaskId(final Long userRoleId,final int configId) {
		List<RoleCuxiao> list = roleCuxiaoDao.cacheLoadAll(userRoleId, new IQueryFilter<RoleCuxiao>() {
			private boolean stop  = false;
			@Override
			public boolean check(RoleCuxiao info) {
				if(userRoleId.longValue()== info.getUserRoleId().longValue() && configId ==info.getConfigId()){
					stop  = true;
					return true;
				}
				return   false;
			}

			@Override
			public boolean stopped() {
				return stop;
			}
		});
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}






	/**
	 * 领取促销奖励 奖励2选1的
	 */
	public Object[] getRewards2(Long userRoleId, int configId) {

		JingJieFanLiConfig config = jingJieFanLiConfigExportService.getConfig(configId);
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}

		//检查元宝
		Object[] result = accountExportService.isEnought(GoodsCategory.GOLD,config.getXprice(), userRoleId);
		if (result != null) {
			return result;
		}
		int level = config.getLevel();

		//坐骑直升
		if(config.getType()==CuxiaoConstants.ZUOQI){
			Object[] ret = zuoQiExportService.sjByCuxiao(userRoleId, level);
			if(ret!=null){
				return ret;
			}
		}

		//翅膀直升
		if(config.getType() == CuxiaoConstants.SWING){
			Object[] ret = chiBangExportService.sjByCuxiao(userRoleId, level); //等级从0开始
			if(ret!=null){
				return ret;
			}
		}

		//圣剑
		if(config.getType()==CuxiaoConstants.WUQI) {
			Object[] ret = wuQiExportService.sjByCuxiao(userRoleId, level); //等级从0开始
			if (ret != null) {
				return ret;
			}
		}

		//魔剑
		if(config.getType()==CuxiaoConstants.TANGBAO_WUQI) {
            Object[] ret = xianJianExportService.sjByCuxiao(userRoleId,level); //等级从0开始
			if (ret != null) {
				return ret;
			}
		}

		//魔甲
		if(config.getType()==CuxiaoConstants.TANGBAO_YIFU) {
			Object[] ret = zhanJiaExportService.sjByCuxiao(userRoleId, level); //等级从0开始
			if (ret != null) {
				return ret;
			}
		}

		//  消耗元宝
		accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, config.getXprice(), userRoleId, LogPrintHandle.CONSUME_CUXIAO_GET_REWARD, true, LogPrintHandle.CBZ_CUXIAO_GET_REWARD);

		//邮件发放

		sendEmail(userRoleId,GameConstants.JINJIE_FIANLI_EMAIL_TITLE,GameConstants.JINJIE_FIANLI_EMAIL_CONTENT,config.getRewards());

		return new Object[]{1,configId};
	}

	private void sendEmail(Long userRoleId,String titleCode,String contentCode,Map<String,Integer> items){
		String content = EmailUtil.getCodeEmail(contentCode);
		String[] attachments = EmailUtil.getAttachments(items);
		String title = EmailUtil.getCodeEmail(titleCode);
		for (String attachment : attachments) {
			emailExportService.sendEmailToOne(userRoleId,title, content, GameConstants.EMAIL_TYPE_SINGLE, attachment);
		}
	}

}
