package com.junyou.bus.dati.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.dati.configure.DaTiJiangLiConfig;
import com.junyou.bus.dati.configure.DaTiPublicConfig;
import com.junyou.bus.dati.configure.TiMuConfig;
import com.junyou.bus.dati.export.DaTiJiangLiConfigExportService;
import com.junyou.bus.dati.export.TiKuConfigExportService;
import com.junyou.bus.dati.vo.AnswerIntegral;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.huoyuedu.enums.ActivityEnum;
import com.junyou.bus.huoyuedu.export.HuoYueDuExportService;
import com.junyou.bus.resource.export.RoleResourceBackExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfig;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;

@Service
public class DaTiService {
	@Autowired
	private TiKuConfigExportService tiKuConfigExportService;
	@Autowired
	private DingShiActiveConfigExportService dingShiActiveConfigExportService;
	@Autowired
	private DaTiJiangLiConfigExportService daTiJiangLiConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private BusScheduleExportService scheduleExportService;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongShuJuBiaoConfigExportService;
	@Autowired
	private RoleResourceBackExportService roleResourceBackExportService;
	@Autowired 
	private HuoYueDuExportService huoYueDuExportService;
	/**答题公共配置数据*/
	private DaTiPublicConfig daTiPublicConfig;
	
	private DingShiActiveConfig dingShiActiveConfig ;
	/**
	 * 当前活动题目 key:题目ID
	 * */
	private Map<Integer, TiMuConfig> currActivityTimu;
	/** 答题积分榜 */
	private List<AnswerIntegral> answerIntegrals;
	/**
	 * 玩家积分索引结构 key:userroleid
	 * */
	private Map<Long, AnswerIntegral> auxiliary;
	/** 直接领奖过的玩家 */
	private Set<Long> receivedPlayer = Collections.synchronizedSet(new HashSet<Long>());
	/**
	 * 上次刷新排行时间
	 */
	private long lastRefreshTiem = 0;
	/** 活动进行标识 */
	private volatile boolean activityUnderway = false;
	
	
	/** 活动开始 */
	public void datiActivityStart(int activityId) {
		
		if (daTiPublicConfig == null) {
			daTiPublicConfig = gongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_DATI);
		}
		
		// 初始积分榜
		this.answerIntegrals = Collections.synchronizedList(new ArrayList<AnswerIntegral>());
		
		// 初始辅助结构
		this.auxiliary = Collections.synchronizedMap(new HashMap<Long, AnswerIntegral>());
		
		// 选题
		xuanti(activityId);
		
		// 开启结束定时
		this.dingShiActiveConfig = dingShiActiveConfigExportService.getConfig(activityId);
		long endTime = DatetimeUtil.getTheTime(dingShiActiveConfig.getEndTime1()[0],dingShiActiveConfig.getEndTime1()[1]);
		Long delay = endTime - GameSystemTime.getSystemMillTime();
		if (delay > 0) {
			scheduleExportService.schedule(GameConstants.DEFAULT_ROLE_ID.toString(), 
					GameConstants.COMPONENET_DINGSHI_ACTIVE + activityId, 
					new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID,InnerCmdType.DINGSHI_DATI_STOP, null),
					delay.intValue(),
					TimeUnit.MILLISECONDS);
		}
		
		activityUnderway = true;
		
		ChuanQiLog.debug("答题活动开始,当次活动随机题目:" + JSONArray.toJSONString(currActivityTimu.keySet().toArray()));
	}

	/** 活动结束 */
	public void datiActivityEnd() {

		activityUnderway = false;
		ChuanQiLog.debug("答题活动结束");
		
		//刷新排行
		rerank();

		int limit = Math.min(answerIntegrals.size(),daTiPublicConfig.getSettlementLimit());
		// 结算
		for (int i = 0; i < limit; i++) {

			AnswerIntegral answerIntegral = answerIntegrals.get(i);
			// 结算奖励
			DaTiJiangLiConfig daTiJiangLiConfig = daTiJiangLiConfigExportService.getConfigByLevel(answerIntegral.getLevel());

			if (daTiJiangLiConfig == null) {
				// 无该等级奖励
				continue;
			}

			String award = null;
			if (answerIntegral.getRank() == 1) {
				award = daTiJiangLiConfig.getDiyi();
				//成就
				try {
					BusMsgSender.send2BusInner(answerIntegral.getUserRoleId(), InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_DATI, answerIntegral.getRank()});
					//roleChengJiuExportService.tuisongChengJiu(answerIntegral.getUserRoleId(), GameConstants.CJ_DATI, answerIntegral.getRank());
				} catch (Exception e) {
					ChuanQiLog.error("",e);
				}
			} else if (answerIntegral.getRank() == 2) {
				award = daTiJiangLiConfig.getDier();
				//成就
				try {
					BusMsgSender.send2BusInner(answerIntegral.getUserRoleId(), InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_DATI, answerIntegral.getRank()});
					//roleChengJiuExportService.tuisongChengJiu(answerIntegral.getUserRoleId(), GameConstants.CJ_DATI, answerIntegral.getRank());
				} catch (Exception e) {
					ChuanQiLog.error("",e);
				}
			} else if (answerIntegral.getRank() == 3) {
				award = daTiJiangLiConfig.getDisan();
				//成就
				try {
					BusMsgSender.send2BusInner(answerIntegral.getUserRoleId(), InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_DATI, answerIntegral.getRank()});
					//roleChengJiuExportService.tuisongChengJiu(answerIntegral.getUserRoleId(), GameConstants.CJ_DATI, answerIntegral.getRank());
				} catch (Exception e) {
					ChuanQiLog.error("",e);
				}
			} else {
				award = daTiJiangLiConfig.getPu();
			}
			if (award == null) {
				break;
			}

			// 发送邮件通知答题奖励
			Map<String, Integer> awards = new HashMap<String, Integer>();
			awards.put(award, 1);

			String[] attachments = EmailUtil.getAttachments(awards);
			
			String title = EmailUtil.getCodeEmail(GameConstants.DATI_MAIL_MSG_CODE_TITLE);
			String content = EmailUtil.getCodeEmail(GameConstants.DATI_MAIL_MSG_CODE,	String.valueOf(answerIntegral.getRank()));
			
			for (String attachment : attachments) {
				emailExportService.sendEmailToOne(answerIntegral.getUserRoleId(),title, content,GameConstants.EMAIL_TYPE_SINGLE, attachment);
			}

		}
		// 释放活动数据
		currActivityTimu = null;
		answerIntegrals = null;
		receivedPlayer.clear();
		auxiliary = null;

	}

	/** 从题库选题 */
	private void xuanti(int activityId) {
		
		Map<Integer, TiMuConfig> tiKuConfigs = tiKuConfigExportService.getTiKuConfig();
		DingShiActiveConfig dingShiActiveConfig = dingShiActiveConfigExportService.getConfig(activityId);
		// 每次活动出题量
		int size = daTiPublicConfig.getTiSum();

		currActivityTimu = new HashMap<Integer, TiMuConfig>(size);

		// 题库的题目索引
		Integer[] timuIds = new Integer[tiKuConfigs.size()];
		tiKuConfigs.keySet().toArray(timuIds);

		long activityStartTime = DatetimeUtil.getTheDayTheTime(
				dingShiActiveConfig.getStartTime1()[0],
				dingShiActiveConfig.getStartTime1()[1],
				GameSystemTime.getSystemMillTime());
		ChuanQiLog.error("活动开始时间:{}",activityStartTime);
		Random random = new Random();
		int number = 0;
		TiMuConfig lastTi = null;
		while (currActivityTimu.size() < size) {
			int timuId = timuIds[random.nextInt(timuIds.length)];
			if (!currActivityTimu.containsKey(timuId)) {

				try {
					TiMuConfig tiMuConfig = tiKuConfigs.get(timuId).clone();
					// 构造每道题的开始时间和结束时间
					long timuStartTime = 0;
					long timuEndTime = 0;
			
					// 题目开始时间=活动开始时间+当前题目数*每道题总时间
					timuStartTime = currActivityTimu.size() * daTiPublicConfig.getEachSumTime() * 1000 + activityStartTime;
					
					// 题目结束时间=题目开始时间+每道题总时间
					timuEndTime = timuStartTime + daTiPublicConfig.getEachSumTime() * 1000;

					tiMuConfig.setStartTime(timuStartTime);
					tiMuConfig.setEndTime(timuEndTime);
					// 设置第几题
					tiMuConfig.setNumber(++number);
					// 构造链型关系
					if (lastTi == null) {
						// 声明第一题
						currTi = tiMuConfig;
					} else {
						lastTi.setNextTiId(tiMuConfig.getId());
					}
					lastTi = tiMuConfig;
					currActivityTimu.put(timuId, tiMuConfig);
				} catch (Exception e) {
					ChuanQiLog.error("构造题目异常,题目ID" + timuId, e);
				}
			}
		}
	}

	/** 刷新排名 */
	private void rerank() {
		// 积分排序
		Collections.sort(answerIntegrals);
		// 构造并列
		int limit = Math.min(daTiPublicConfig.getSettlementLimit(),	answerIntegrals.size());
		AnswerIntegral last = null;
		int i = 0;
		for (; i < limit; i++) {
			AnswerIntegral tmp = answerIntegrals.get(i);
			if (last == null) { // 第一名
				tmp.setRank(1);
			} else if (tmp.getIntegral() == last.getIntegral()) {// 与上名并列
				tmp.setRank(last.getRank());
			} else {// 排位名次
				tmp.setRank(i + 1);
			}
			last=tmp;
		}
		for (; i < answerIntegrals.size(); i++) {
			AnswerIntegral tmp = answerIntegrals.get(i);
			tmp.setRank(0);
		}
	}

	/**
	 * 提交答案
	 * 
	 * @param userRoleId
	 * @param titleId
	 *            :题目ID
	 * @param opt
	 *            :答案选项
	 * @param useDouble
	 *            :是否使用双倍经验卡 1是 0否
	 **/
	public Object[] submitOpt(Long userRoleId, int titleId, int opt,
			int useDouble) {

		if (!isActivityUnderway()) {
			return null;
		}

		TiMuConfig tiMuConfig = currActivityTimu.get(titleId);
		if (tiMuConfig == null) {
			// 非法指令
			return null;
		}
		
		if(	auxiliary.get(userRoleId).isAnswered(titleId)){
			//2:已答过此题
			 return new Object[] { 2,auxiliary.get(userRoleId).getIntegral(),tiMuConfig.getRight() };
		}
		
		// 判断是否在答题时间内
		long currTime = GameSystemTime.getSystemMillTime();
		if (currTime <= tiMuConfig.getStartTime() || currTime >= tiMuConfig.getEndTime()) {
			// 不在答题时间内
			return new Object[] { AppErrorCode.FAIL,auxiliary.get(userRoleId).getIntegral(),tiMuConfig.getRight() };
		}
		
		//记录答题历史
		auxiliary.get(userRoleId).addAnswered(titleId);
		//活跃度lxn
		huoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A21);
		
		DaTiJiangLiConfig daTiJiangLiConfig = daTiJiangLiConfigExportService.getConfigByLevel(auxiliary.get(userRoleId).getLevel());
		if (daTiJiangLiConfig == null) {
			return new Object[] { AppErrorCode.FAIL,auxiliary.get(userRoleId).getIntegral(),tiMuConfig.getRight() };
		}
		
		if (opt == tiMuConfig.getRight()) {

			AnswerIntegral answerIntegral = auxiliary.get(userRoleId);
			
			// 加经验
			long addExp = (long) daTiJiangLiConfig.getExp();
			roleExportService.incrExp(userRoleId, addExp);
			
			// 加真气
			roleExportService.addZhenqi(userRoleId,daTiJiangLiConfig.getZhenqi());
			
			// 加积分(基础分+每秒积分*剩余秒)
			int addJifen = (int) (tiMuConfig.getEndTime() - currTime) / 1000
					* daTiJiangLiConfig.getMiao()
					+ daTiJiangLiConfig.getJifen();
			// 是否使用双倍积分卡
			if (useDouble == 1 && answerIntegral.isHaveDoublePointCard()) {
				addJifen *= 2;
				answerIntegral.useDoublePointCard();
			}
			answerIntegral.addIntegral(addJifen);
			//修炼任务
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE_TS, new Object[] {XiuLianConstants.DATI, 1});
			return new Object[] { AppErrorCode.SUCCESS,	answerIntegral.getIntegral(), tiMuConfig.getRight() };
		}else{
			//答错奖励
			// 加经验
			long addExp = (long) daTiJiangLiConfig.getExp1();
			roleExportService.incrExp(userRoleId, addExp);
			// 加真气
			roleExportService.addZhenqi(userRoleId,daTiJiangLiConfig.getZhenqi1());
			
			if(useDouble == 1){
				AnswerIntegral answerIntegral = auxiliary.get(userRoleId);
				answerIntegral.useDoublePointCard();
			}
		}
		//修炼任务
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE_TS, new Object[] {XiuLianConstants.DATI, 1});
		return new Object[] { AppErrorCode.FAIL,auxiliary.get(userRoleId).getIntegral(), tiMuConfig.getRight() };
	}

	/** 使用排除符 */
	public Object[] useExcludeCard(Long userRoleId, int titleId) {
		if (!isActivityUnderway()) {
			return null;
		}

		TiMuConfig tiMuConfig = currActivityTimu.get(titleId);
		if (tiMuConfig == null) {
			// 非法指令
			return null;
		}

		// 判断是否在答题时间内
		long currTime = GameSystemTime.getSystemMillTime();
		if (currTime <= tiMuConfig.getStartTime() || currTime >= tiMuConfig.getEndTime()) {
			return AppErrorCode.ERR;
		}

		AnswerIntegral answerIntegral = auxiliary.get(userRoleId);
		
		if(answerIntegral.isAnswered(titleId)){
			return AppErrorCode.ERR;
		}
		
		if (answerIntegral.isHaveExcludeCard()) {
			List<Integer> wrongs = new ArrayList<Integer>(2);
			Random random = new Random();
			while (wrongs.size() < 2) {
				// 题库中答案索引从1开始
				int answersIndex = random.nextInt(tiMuConfig.getAnswers().size()) + 1;
				if (answersIndex != tiMuConfig.getRight() && !wrongs.contains(answersIndex)) {
					wrongs.add(answersIndex);
				}
			}
			answerIntegral.useExcludeCard();
			return new Object[] { AppErrorCode.SUCCESS, wrongs.get(0),wrongs.get(1) };

		}
		return AppErrorCode.ERR;
	}

	/**
	 * 客户端请求当前题目的结算积分排名
	 * 
	 * */
	public Object[] showRank(Long userRoleId, int titleId) {
		if (!isActivityUnderway()) {
			return null;
		}

		TiMuConfig tiMuConfig = currActivityTimu.get(titleId);
		if (tiMuConfig == null) {
			// 非法指令
			return null;
		}

		// 刷新排名时间左节点（题目结束时间前3秒） ,右节点 endTime
		long reTime = tiMuConfig.getEndTime() - 1000 * 3;
		// 上次刷新时间如果不在本题的刷新时间内
		if (lastRefreshTiem < reTime
				|| lastRefreshTiem >= tiMuConfig.getEndTime()) {
			long currTime = GameSystemTime.getSystemMillTime();
			// 请求时间是否在本题的刷新时间内
			if (currTime >= reTime && currTime < tiMuConfig.getEndTime()) {
				rerank();
			}
		}

		AnswerIntegral answerIntegral1 = null;
		AnswerIntegral answerIntegral2 = null;
		AnswerIntegral answerIntegral3 = null;
		AnswerIntegral answerIntegralMe = null;
		if (answerIntegrals.size() > 2) {
			answerIntegral1 = answerIntegrals.get(0);
			answerIntegral2 = answerIntegrals.get(1);
			answerIntegral3 = answerIntegrals.get(2);
		} else if (answerIntegrals.size() > 1) {
			answerIntegral1 = answerIntegrals.get(0);
			answerIntegral2 = answerIntegrals.get(1);
		} else if (answerIntegrals.size() > 0) {
			answerIntegral1 = answerIntegrals.get(0);
		}

		answerIntegralMe = auxiliary.get(userRoleId);

		return new Object[] {

				answerIntegralMe != null ? answerIntegralMe.getRank()
						: daTiPublicConfig.getSettlementLimit() + 1,
				new Object[] {
						answerIntegral1 != null ? new Object[] {
								answerIntegral1.getName(),
								answerIntegral1.getIntegral() } : null,
						answerIntegral2 != null ? new Object[] {
								answerIntegral2.getName(),
								answerIntegral2.getIntegral() } : null,
						answerIntegral3 != null ? new Object[] {
								answerIntegral3.getName(),
								answerIntegral3.getIntegral() } : null }

		};

	}

	/** 当前题目 */
	private TiMuConfig currTi;

	/** 请求题目 */
	public Object[] getTimu(Long userRoleId) {
		if (!isActivityUnderway()) {
			return null;
		}
		boolean bl = initPlayerActivityData(userRoleId);
		
		if(!bl){
			return null;
		}
		

		TiMuConfig sendTimu = null;

		long currTime = GameSystemTime.getSystemMillTime();
		for (int i = 0; i < currActivityTimu.size(); i++) {
			if (currTime < currTi.getEndTime()) {
				sendTimu = currTi;
				break;
			}else {
				if (currTi.getNextTiId() != 0) {
					currTi = currActivityTimu.get(currTi.getNextTiId());
				} else {
					break;
				}
			}
		}
/*		while (true) {
			if (currTime >= currTi.getStartTime()&& currTime < currTi.getEndTime()) {
				sendTimu = currTi;
				break;
			} else {
				if (currTi.getNextTiId() != 0) {
					currTi = currActivityTimu.get(currTi.getNextTiId());
				} else {
					break;
				}
			}
		}*/

		if (sendTimu == null) {
			int id = -1;
			int number = -1;
			int nextId = -1;
			if(currTi != null){
				id = currTi.getId();
				number = currTi.getNumber();
				nextId = currTi.getNextTiId();
			}
			ChuanQiLog.error("{}获取题目时失败，当前题目id:{},当前时间:{},当前序号:{},下一题id:{}",userRoleId,id,currTime,number,nextId);
			return null;
		}
		
		if(auxiliary.get(userRoleId).isAnswered(sendTimu.getId())){
			//TODO 客户端需要题目状态再加
		}

		return sendTimu.getMsgData();
	}

	/** 初始化玩家答题活动数据 */
	private boolean initPlayerActivityData(Long userRoleId) {
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		
		if(roleWrapper.getLevel()<dingShiActiveConfig.getMinLevel()){
			return false;
		} 
		
		if (!auxiliary.containsKey(userRoleId)) {
			AnswerIntegral answerIntegral = new AnswerIntegral(userRoleId,
					roleWrapper.getName(),
					daTiPublicConfig.getInitDoublePointCardNum(),
					daTiPublicConfig.getInitExcludeCard(),
					daTiPublicConfig.getSettlementLimit() + 1,// 默认排名结算名次+1
					roleWrapper.getLevel()
					);
			// 加入管理容器
			auxiliary.put(userRoleId, answerIntegral);
			// 加入排行
			answerIntegrals.add(answerIntegral);
			try{
				//玩家参与了今天答题
				roleResourceBackExportService.updateDaTiTime(userRoleId);
			}catch (Exception e) {
				ChuanQiLog.error("",e);
			}
		}
		return true;
	}

	/** 直接领奖 */
	public Object[] promptlyAward(Long userRoleId) {
		if (auxiliary.containsKey(userRoleId)) {
			// 已参加过活动不能直接领奖
			return AppErrorCode.DATI_CANNOT_RECEIVE;
		}
		if (receivedPlayer.contains(userRoleId)) {
			// 已领取过不能再领
			return AppErrorCode.DATI_RECEIVED;
		}

		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);

		DaTiJiangLiConfig daTiJiangLiConfig = daTiJiangLiConfigExportService.getConfigByLevel(roleWrapper.getLevel());
		
		if (daTiJiangLiConfig == null) {
			return AppErrorCode.DATI_LEVEL_NOT_ENOUGH;
		}

		if (roleWrapper.getLevel() >= daTiJiangLiConfig.getMinLevel()&& roleWrapper.getLevel() <= daTiJiangLiConfig.getMaxLevel()) {
			Object[] error = accountExportService.isEnought(GoodsCategory.GOLD,	daTiJiangLiConfig.getNeedGold(), userRoleId);
			if (error != null) {
				return error;
			}
			// 加入背包*/
			Map<String, Integer> jiangItem = daTiJiangLiConfig.getJiangItem();
			roleBagExportService.putGoodsAndNumberAttr(jiangItem, userRoleId,
					GoodsSource.DIRECT_GET_DATI_AWARD,
					LogPrintHandle.GET_DATI_AWARD,
					LogPrintHandle.GBZ_DATI_AWARD, true);
			// 消耗元宝
			accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD,
					daTiJiangLiConfig.getNeedGold(), userRoleId,
					LogPrintHandle.GET_DATI_AWARD, true,
					LogPrintHandle.GBZ_DATI_AWARD);
			// 记录领奖状态
			receivedPlayer.add(userRoleId);
			return AppErrorCode.OK;

		}
		
		return AppErrorCode.DATI_LEVEL_NOT_ENOUGH;
	}

	/** 参与活动 */
	public Object[] joinDati(Long userRoleId) {

		if (!isActivityUnderway()) {
			return AppErrorCode.ERR;
		}
		boolean bl = initPlayerActivityData(userRoleId);
		if(!bl){
			return AppErrorCode.ERR;
		}
		return AppErrorCode.OK;
	}

	/** 玩家当前活动数据 */
	public Object[] currentItem(Long userRoleId) {
		if (!isActivityUnderway()) {
			return null;
		}
		boolean bl = initPlayerActivityData(userRoleId);
		if(!bl){
			return null;
		}
		AnswerIntegral answerIntegral = auxiliary.get(userRoleId);

		return new Object[] { answerIntegral.getDoublePointCard(),answerIntegral.getExcludeCard(),answerIntegral.getIntegral()};
	}

	/** 活动是否进行中 */
	public boolean isActivityUnderway() {
		return activityUnderway;
	}

}
