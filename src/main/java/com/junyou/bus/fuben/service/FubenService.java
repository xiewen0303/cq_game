package com.junyou.bus.fuben.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemInput;
import com.junyou.bus.bag.utils.BagUtil;
import com.junyou.bus.branchtask.BranchEnum;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.fuben.dao.FubenDao;
import com.junyou.bus.fuben.dao.JianzhongFubenDao;
import com.junyou.bus.fuben.dao.RoleFubenDao;
import com.junyou.bus.fuben.dao.ShouhuFubenDao;
import com.junyou.bus.fuben.entity.DayFubenConfig;
import com.junyou.bus.fuben.entity.Fuben;
import com.junyou.bus.fuben.entity.JianzhongFuben;
import com.junyou.bus.fuben.entity.JianzhongFubenConfig;
import com.junyou.bus.fuben.entity.RoleFuben;
import com.junyou.bus.fuben.entity.ShouhuFuben;
import com.junyou.bus.fuben.entity.ShouhuFubenConfig;
import com.junyou.bus.fuben.entity.VipFubenConfig;
import com.junyou.bus.huoyuedu.enums.ActivityEnum;
import com.junyou.bus.huoyuedu.export.HuoYueDuExportService;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.resource.export.RoleResourceBackExportService;
import com.junyou.bus.role.IncrRoleResp;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.bus.vip.util.RoleVipWrapper;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.FubenLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.FubenPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.ShouhuPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.YaoshenHunpoPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.public_.share.service.PublicRoleStateService;
import com.junyou.stage.model.core.stage.DeadDisplay;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.JianzhongFubenStage;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.math.BitOperationUtil;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

@Service
public class FubenService {
	@Autowired
	private FubenDao fubenDao;
	@Autowired
	private DayFubenConfigService dayFubenConfigService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private StageControllExportService stageControllExportService;
	@Autowired
	private RoleFubenDao roleFubenDao;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private BusScheduleExportService scheduleExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	@Autowired
	private ShouhuFubenConfigService shouhuFubenConfigService;
	@Autowired
	private JianzhongConfigService jianzhongConfigService;
	@Autowired
	private ShouhuFubenDao shouhuFubenDao;
	@Autowired
	private VipFubenConfigService vipFubenConfigService;
	@Autowired 
	private HuoYueDuExportService huoYueDuExportService;
	@Autowired
	private RoleResourceBackExportService roleResourceBackExportService;
	@Autowired
	private JianzhongFubenDao jianzhongFubenDao;
	@Autowired
	private PublicRoleStateService publicRoleStateService;
	
	
	/**
	 * 初始化
	 * @param userRoleId
	 * @return
	 */
	public List<Fuben> initFuben(Long userRoleId){
		return fubenDao.initFuben(userRoleId);
	}
	
	/**
	 * 结算守护副本奖励
	 * @param fuben
	 */
	private void calShouhuFubenGitf(ShouhuFuben fuben){
		if(fuben.getStartId() < fuben.getNowId()){
			long exp = 0;
			long money = 0;
			long zhenqi = 0;
			for (int i = fuben.getStartId(); i < fuben.getNowId(); i++) {
				ShouhuFubenConfig config = shouhuFubenConfigService.loadById(i);
				if(config != null){
					exp += config.getExp();
					money += config.getMoney();
					zhenqi += config.getZhenqi();
				}
			}
			Long userRoleId = fuben.getUserRoleId();
			fuben.setStartId(fuben.getNowId());
			shouhuFubenDao.cacheUpdate(fuben, userRoleId);
			
			roleExportService.incrExp(userRoleId, exp);
			accountExportService.incrCurrencyWithNotify(GoodsCategory.MONEY, money, userRoleId, LogPrintHandle.GET_FUBEN, LogPrintHandle.GBZ_SHOUHU_FUBEN);
			roleExportService.addZhenqi(userRoleId, zhenqi);
		}
	}
	
	/**
	 * 上线业务
	 * @param userRoleId
	 */
	public void onlineHandle(Long userRoleId){
		fubenOnlineHandle(userRoleId);
		shouhuOnlineHandle(userRoleId);
	}
	
	/**
	 * 个人副本上线业务
	 * @param userRoleId
	 */
	public void fubenOnlineHandle(Long userRoleId){
		RoleFuben roleFuben = roleFubenDao.cacheAsynLoad(userRoleId, userRoleId);
		if(roleFuben == null){
			return;
		}
		if(roleFuben.getState() == GameConstants.FUBEN_STATE_SAODANG){
			//扫荡处理
			int cd = roleVipInfoExportService.getVipTequan(userRoleId, GameConstants.VIP_FUBEN_CD_TIME) * 1000;
			roleFuben.setCd(cd);
			Long need = roleFuben.getSaodangEnd() - GameSystemTime.getSystemMillTime();
			if(need > 0){//是否需要开启定时
				roleFubenDao.cacheUpdate(roleFuben, userRoleId);
				startSchedule(userRoleId, need.intValue());
			}else{
				finishSaodao(userRoleId);
			}
		}else{
			roleFuben.setState(GameConstants.FUBEN_STATE_READY);
			roleFubenDao.cacheUpdate(roleFuben, userRoleId);
		}
	}
	/**
	 * 下线业务
	 * @param userRoleId
	 */
	public void offlineHandle(Long userRoleId){
		RoleFuben roleFuben = roleFubenDao.cacheAsynLoad(userRoleId, userRoleId);
		if(roleFuben == null){
			return;
		}
		if(roleFuben.getState() == GameConstants.FUBEN_STATE_SAODANG){
			//取消扫荡定时
			cancelSchedule(userRoleId);
		}
	}
	/**
	 * 获取副本状态信息
	 * @param userRoleId
	 * @return
	 */
	public Object[] getFubenInfo(Long userRoleId){
		List<Fuben> list = fubenDao.cacheLoadAll(userRoleId);
		List<Object[]> out = new ArrayList<>();
		if(list != null && list.size() > 0){
			for (Fuben fuben : list) {
				if(GameConstants.FUBEN_TYPE_DAY == fuben.getType()){
					calFuben(fuben);
					out.add(new Object[]{fuben.getFubenId(),fuben.getCount()});
				}
			}
		}
		RoleFuben roleFuben = getRoleFuben(userRoleId);
		return new Object[]{out.toArray(),roleFuben.getSaodangEnd()};
	}
	/**
	 * 获取Vip副本状态信息
	 * @param userRoleId
	 * @return
	 */
	public Object[] getVipFubenInfo(Long userRoleId){
		List<Fuben> list = fubenDao.cacheLoadAll(userRoleId);
		List<Object[]> out = new ArrayList<>();
		if(list != null && list.size() > 0){
			for (Fuben fuben : list) {
				if(GameConstants.FUBEN_TYPE_VIP == fuben.getType()){
					calFuben(fuben);
					out.add(new Object[]{fuben.getFubenId(),fuben.getCount()});
				}
			}
		}
		return out.toArray();
	}
	/**
	 * 处理副本跨天业务
	 * @param fuben
	 */
	private void calFuben(Fuben fuben){
		if(!DatetimeUtil.dayIsToday(fuben.getUpdateTime())){
			if(GameConstants.FUBEN_TYPE_DAY == fuben.getType()){
				fubenKuaTian(fuben.getUserRoleId());
			}
			fuben.setCount(0);
			fuben.setUpdateTime(GameSystemTime.getSystemMillTime());
			fubenDao.cacheUpdate(fuben, fuben.getUserRoleId());
		}
	}
	/**
	 * 获取副本信息（可能为null，已处理跨天）
	 * @param userRoleId
	 * @param fubenId
	 * @return
	 */
	private Fuben getFuben(Long userRoleId,Integer fubenId,Integer type){
		final Integer id = fubenId;
		final Integer ftype = type;
		List<Fuben> fubens = fubenDao.cacheLoadAll(userRoleId, new IQueryFilter<Fuben>() {
			private boolean stop = false;
			@Override
			public boolean check(Fuben fuben) {
				if(fuben.getFubenId().equals(id) && fuben.getType().equals(ftype)){
					stop = true;
					return true;
				}else{
					return false;
				}
			}
			@Override
			public boolean stopped() {
				return stop;
			}
		});
		if(fubens != null && fubens.size() > 0){
			Fuben fuben = fubens.get(0);
			calFuben(fuben);
			return fuben;
		}
		return null;
	}
	/**
	 * 进入副本
	 * @param userRoleId
	 * @param fubenId
	 * @param dstate 
	 */
	public void enterFuben(Long userRoleId,Integer fubenId, DeadDisplay dstate, BusMsgQueue busMsgQueue){
		
		RoleFuben roleFuben = getRoleFuben(userRoleId);
		if(roleFuben.getState() != GameConstants.FUBEN_STATE_READY){
			//当前状态不可挑战副本
			busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_FUBEN, AppErrorCode.FUBEN_STATE_ERROR_TZ);
			return;
		}
		DayFubenConfig config = dayFubenConfigService.loadById(fubenId);
		if(config == null){
			//配置异常
			busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_FUBEN, AppErrorCode.CONFIG_ERROR);
			return ;
		}
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(role == null){
			//角色不存在
			busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_FUBEN, AppErrorCode.ROLE_IS_NOT_EXIST);
			return ;
		}
		if(role.getLevel() < config.getLevel()){
			//等级不足
			busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_FUBEN, AppErrorCode.ROLE_LEVEL_NOT_ENOUGH);
			return;
		}
		if(stageControllExportService.inFuben(userRoleId)){
			//在副本中
			busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_FUBEN, AppErrorCode.FUBEN_IS_IN_FUBEN);
			return;
		}
		Fuben fuben = getFuben(userRoleId, fubenId,GameConstants.FUBEN_TYPE_DAY);
		if(fuben != null && fuben.getCount() >= config.getCount()){
			//次数不足
			busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_FUBEN, AppErrorCode.FUBEN_NO_COUNT);
			return;
		}
		roleFuben.setFubenId(fubenId);
		roleFuben.setType(config.getFubenType());
		roleFuben.setState(GameConstants.FUBEN_STATE_FIGHT);
		roleFubenDao.cacheUpdate(roleFuben, userRoleId);
		//发送到场景进入地图
		DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(config.getMapId());
		int[] birthXy = dituCoinfig.getRandomBirth();
		Object[] applyEnterData = new Object[]{dituCoinfig.getId(),birthXy[0],birthXy[1], MapType.FUBEN_MAP, config.getId(),null,dstate};
		busMsgQueue.addBusMsg(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
		//活跃度lxn
		huoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A16);
		//推送给客户端进入成功
		busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_FUBEN, new Object[]{AppErrorCode.SUCCESS, config.getId()});
		//修炼任务
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.FUBEN_DANREN, null});
	}
	/**
	 * 进入VIP副本
	 * @param userRoleId
	 * @param fubenId
	 */
	public void enterVipFuben(Long userRoleId,Integer fubenId, BusMsgQueue busMsgQueue){
		RoleFuben roleFuben = getRoleFuben(userRoleId);
		if(roleFuben.getState() != GameConstants.FUBEN_STATE_READY){
			//当前状态不可挑战副本
			busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_VIP_FUBEN, AppErrorCode.FUBEN_STATE_ERROR_TZ);
			return;
		}
		VipFubenConfig config = vipFubenConfigService.loadById(fubenId);
		if(config == null){
			//配置异常
			busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_VIP_FUBEN, AppErrorCode.CONFIG_ERROR);
			return ;
		}
		RoleVipWrapper vip = roleVipInfoExportService.getRoleVipInfo(userRoleId);
		if(vip == null || vip.getVipLevel() < config.getVip()){
			//等级不足
			busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_VIP_FUBEN, AppErrorCode.VIP_NOT_ENOUGH_LEVEL);
			return;
		}
		if(stageControllExportService.inFuben(userRoleId)){
			//在副本中
			busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_VIP_FUBEN, AppErrorCode.FUBEN_IS_IN_FUBEN);
			return;
		}
		Fuben fuben = getFuben(userRoleId, fubenId,GameConstants.FUBEN_TYPE_VIP);
		if(fuben != null && fuben.getCount() >= config.getCount()){
			//次数不足
			busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_VIP_FUBEN, AppErrorCode.FUBEN_NO_COUNT);
			return;
		}
		roleFuben.setFubenId(fubenId);
		roleFuben.setType(config.getFubenType());
		roleFuben.setState(GameConstants.FUBEN_STATE_FIGHT);
		roleFubenDao.cacheUpdate(roleFuben, userRoleId);
		//发送到场景进入地图
		DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(config.getMapId());
		int[] birthXy = dituCoinfig.getRandomBirth();
		Object[] applyEnterData = new Object[]{dituCoinfig.getId(),birthXy[0],birthXy[1], MapType.FUBEN_MAP, config.getId()};
		busMsgQueue.addBusMsg(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
		//推送给客户端进入成功
		busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_VIP_FUBEN, new Object[]{AppErrorCode.SUCCESS, config.getId()});
	}
	private RoleFuben getRoleFuben(Long userRoleId){
		RoleFuben roleFuben = roleFubenDao.cacheAsynLoad(userRoleId, userRoleId);
		if(roleFuben == null){
			roleFuben = new RoleFuben();
			roleFuben.setUserRoleId(userRoleId);
			roleFuben.setState(GameConstants.FUBEN_STATE_READY);
			roleFubenDao.cacheInsert(roleFuben, userRoleId);
		}
		return roleFuben;
	}
	
	/**
	 * 副本完成
	 * @param userRoleId
	 */
	public void finishFuben(Long userRoleId){
		RoleFuben roleFuben = getRoleFuben(userRoleId);
		if(roleFuben.getType().equals(GameConstants.FUBEN_TYPE_DAY)){
			//日常副本修改状态
			roleFuben.setState(GameConstants.FUBEN_STATE_FINISH);
			roleFubenDao.cacheUpdate(roleFuben, userRoleId);
		}else if(roleFuben.getType().equals(GameConstants.FUBEN_TYPE_VIP)){
			//VIP副本直接算领取奖励
			receiveVipFubenReward(roleFuben);
			//成就
			try {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_SANSHENGFUBENCOUNT, 1});
				//roleChengJiuExportService.tuisongChengJiu(userRoleId, GameConstants.CJ_SANSHENGFUBENCOUNT, 1);
			} catch (Exception e) {
				ChuanQiLog.error("",e);
			}
		}
	}
	/**
	 * 领取奖励
	 * @param userRoleId
	 * @return
	 */
	public Object[] receiveFubenReward(Long userRoleId){
		RoleFuben roleFuben = getRoleFuben(userRoleId);
		if(roleFuben.getState() != GameConstants.FUBEN_STATE_FINISH){
			return AppErrorCode.FUBEN_NOT_FINISH;//副本尚未完成
		}
		
		Fuben fuben = getFuben(userRoleId, roleFuben.getFubenId(),GameConstants.FUBEN_TYPE_DAY);
		
		int type = roleFuben.getType();
		boolean first = false;
		if(fuben == null){
			fuben = createFuben(userRoleId, roleFuben.getFubenId(),type);
			first = true;
		}
		long money = 0;
		long exp = 0;
		Map<String,Integer> items = new HashMap<>();
		boolean dayFirst = false;
		DayFubenConfig config = null;
		if(MapType.FUBEN_MAP.equals(type)){
			config = dayFubenConfigService.loadById(roleFuben.getFubenId());
			money = config.getMoney();
			exp = config.getExp();
			ObjectUtil.mapAdd(items, config.getProp());
			if(first){
				ObjectUtil.mapAdd(items, config.getFirst());
			}else if(fuben.getCount() < 1){
				ObjectUtil.mapAdd(items, config.getDayFirst());
				dayFirst = true;
			}
		}
		roleFuben.setState(GameConstants.FUBEN_STATE_READY);
		roleFubenDao.cacheUpdate(roleFuben, userRoleId);
		
		fuben.setCount(fuben.getCount() + 1);
		fuben.setUpdateTime(GameSystemTime.getSystemMillTime());
		fubenDao.cacheUpdate(fuben, userRoleId);
		if(money > 0){
			money = accountExportService.incrCurrencyWithNotify(GoodsCategory.MONEY, money, userRoleId, LogPrintHandle.GET_FUBEN, LogPrintHandle.GBZ_FUBEN);
		}
		if(exp > 0){
			IncrRoleResp incrRoleResp = roleExportService.incrExp(userRoleId, exp);
			if(incrRoleResp == null){
				exp = 0;
			}else{
				exp = incrRoleResp.getIncrExp();
			}
		}
		if(roleBagExportService.checkPutInBag(items, userRoleId) == null){
			roleBagExportService.putInBag(items, userRoleId, GoodsSource.FUBEN, true);
		}else{
			sendEmail(userRoleId, GameConstants.FUBEN_EMAIL_CONTENT_CODE, items);
		}
		//打印日志
		printLog(config, userRoleId, first, dayFirst, LogPrintHandle.FUBEN_TYPE_TIAOZHAN);
		//成就
		if(fuben.getFubenId().intValue() == 1 && fuben.getType().intValue() == GameConstants.FUBEN_TYPE_DAY){
			try {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_GOUYUFUBENCOUNT, 1});
				//roleChengJiuExportService.tuisongChengJiu(userRoleId, GameConstants.CJ_GOUYUFUBENCOUNT, 1);
			} catch (Exception e) {
				ChuanQiLog.error("",e);
			}
		}
		return new Object[]{AppErrorCode.SUCCESS,items, money, exp};
	}
	/**
	 * VIP副本完成
	 * @param userRoleId
	 * @return
	 */
	public void receiveVipFubenReward(RoleFuben roleFuben){
		Long userRoleId = roleFuben.getUserRoleId();
		
		Fuben fuben = getFuben(userRoleId, roleFuben.getFubenId(),GameConstants.FUBEN_TYPE_VIP);
		
		int type = roleFuben.getType();
		if(fuben == null){
			fuben = createFuben(userRoleId, roleFuben.getFubenId(),type);
		}
		fuben.setCount(fuben.getCount() + 1);
		fuben.setUpdateTime(GameSystemTime.getSystemMillTime());
		fubenDao.cacheUpdate(fuben, userRoleId);
	}
	
	private Fuben createFuben(Long userRoleId,Integer fubenId,Integer fubenType){
		Fuben fuben = new Fuben();
		fuben.setUserRoleId(userRoleId);
		fuben.setType(fubenType);
		fuben.setFubenId(fubenId);
		fuben.setId(IdFactory.getInstance().generateId(ServerIdType.FUBEN));
		fuben.setCount(0);
		fuben.setUpdateTime(GameSystemTime.getSystemMillTime());
		fubenDao.cacheInsert(fuben, userRoleId);
		return fuben;
	}
	
	private void sendEmail(Long userRoleId,String contentCode,Map<String,Integer> items){
		String content = EmailUtil.getCodeEmail(contentCode);
		String[] attachments = EmailUtil.getAttachments(items);
		String title = EmailUtil.getCodeEmail(GameConstants.FUBEN_EMAIL_CONTENT_CODE_TITLE);
		for (String attachment : attachments) {
			emailExportService.sendEmailToOne(userRoleId,title, content, GameConstants.EMAIL_TYPE_SINGLE, attachment);
		}
	}
	
	public void leaveFuben(Long userRoleId, BusMsgQueue busMsgQueue){
		if(!stageControllExportService.inFuben(userRoleId)){
			//不在副本中
			busMsgQueue.addMsg(userRoleId, ClientCmdType.LEAVE_FUBEN, AppErrorCode.FUBEN_NOT_IN_FUBEN);
			return;
		}
		RoleFuben roleFuben = getRoleFuben(userRoleId);
		roleFuben.setState(GameConstants.FUBEN_STATE_READY);
		roleFubenDao.cacheUpdate(roleFuben, userRoleId);
		
		//发送到场景处理退出副本
		busMsgQueue.addStageMsg(userRoleId, InnerCmdType.S_EXIT_FUBEN, null);
//		//推送给客户端退出副本成功	Liuyu:改为退出场景后发送
//		busMsgQueue.addMsg(userRoleId, ClientCmdType.LEAVE_FUBEN, AppErrorCode.OK);
	}
	
	public void leaveVipFuben(Long userRoleId, BusMsgQueue busMsgQueue){
		if(!stageControllExportService.inFuben(userRoleId)){
			//不在副本中
			busMsgQueue.addMsg(userRoleId, ClientCmdType.LEAVE_VIP_FUBEN, AppErrorCode.FUBEN_NOT_IN_FUBEN);
			return;
		}
		
		//发送到场景处理退出副本
		busMsgQueue.addStageMsg(userRoleId, InnerCmdType.S_EXIT_FUBEN, null);
	}
	
	public void hasLeaveFuben(Long userRoleId){
		RoleFuben roleFuben = getRoleFuben(userRoleId);
		//日常副本修改状态
		roleFuben.setState(GameConstants.FUBEN_STATE_READY);
		roleFubenDao.cacheUpdate(roleFuben, userRoleId);
		if(roleFuben.getType().equals(GameConstants.FUBEN_TYPE_DAY)){
			//推送给客户端退出副本成功
			BusMsgSender.send2One(userRoleId, ClientCmdType.LEAVE_FUBEN, AppErrorCode.OK);
		}else if(roleFuben.getType().equals(GameConstants.FUBEN_TYPE_VIP)){
			//推送给客户端退出副本成功
			BusMsgSender.send2One(userRoleId, ClientCmdType.LEAVE_VIP_FUBEN, AppErrorCode.OK);
		}
	}
	/**
	 * 单次扫荡
	 * @param userRoleId
	 * @param fubenId
	 */
	public Object[] saodangOnce(Long userRoleId,int fubenId){
		RoleFuben roleFuben = getRoleFuben(userRoleId);
		if(roleFuben.getState() != GameConstants.FUBEN_STATE_READY){
			//当前状态不可扫荡副本
			return AppErrorCode.FUBEN_STATE_ERROR_SD;
		}
		Fuben fuben = getFuben(userRoleId, fubenId,GameConstants.FUBEN_TYPE_DAY);
		if(fuben == null){
			//副本尚未通关过
			return AppErrorCode.FUBEN_NOT_TONGGUAN;
		}
		DayFubenConfig config = dayFubenConfigService.loadById(fubenId);
		if(config == null){
			//配置异常
			return  AppErrorCode.CONFIG_ERROR;
		}
		int lastCount = config.getCount() - fuben.getCount();
		if(lastCount < 1){
			//今日已无次数
			return AppErrorCode.FUBEN_NO_COUNT;
		}
		FubenPublicConfig fubenPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_FUBEN);
		if(fubenPublicConfig == null){
			//配置异常
			ChuanQiLog.error("副本公共数据表不存在");
			return AppErrorCode.CONFIG_ERROR;
		}
		//判断VIP是否瞬秒
		int cd = roleVipInfoExportService.getVipTequan(userRoleId, GameConstants.VIP_FUBEN_CD_TIME) * 1000;
		roleFuben.setCd(cd);
		
		String saodangId = fubenId + "";
		roleFuben.setSaodangIds(saodangId);
		roleFuben.setSaodangStart(GameSystemTime.getSystemMillTime());
		roleFuben.setState(GameConstants.FUBEN_STATE_SAODANG);
		roleFubenDao.cacheUpdate(roleFuben, userRoleId);
		if(cd > 0){
			startSchedule(userRoleId,cd);
		}else{//瞬间完成
			finishSaodao(userRoleId);
		}
		//活跃度lxn
		huoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A16);
		//成就
		if(fuben.getFubenId().intValue() == 1 && fuben.getType().intValue() == GameConstants.FUBEN_TYPE_DAY){
			try {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_GOUYUFUBENCOUNT, 1});
				//roleChengJiuExportService.tuisongChengJiu(userRoleId, GameConstants.CJ_GOUYUFUBENCOUNT, 1);
			} catch (Exception e) {
				ChuanQiLog.error("",e);
			}
		}
		//修炼任务
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.FUBEN_DANREN, null});
		return new Object[]{1,roleFuben.getSaodangEnd(),fubenId};
	}
	/**
	 * 一键扫荡
	 * @param userRoleId
	 */
	public Object[] saodangAll(Long userRoleId){
		FubenPublicConfig fubenPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_FUBEN);
		if(fubenPublicConfig == null){
			//配置异常
			ChuanQiLog.error("副本公共数据表不存在");
			return AppErrorCode.CONFIG_ERROR;
		}
		//判断VIP等级是否满足
		
		if(roleVipInfoExportService.getVipTequan(userRoleId, GameConstants.VIP_FUBEN_ALL_SAODANG) < 1){
			return AppErrorCode.VIP_NOT_ENOUGH_LEVEL;
		}
		
		RoleFuben roleFuben = getRoleFuben(userRoleId);
		if(roleFuben.getState() != GameConstants.FUBEN_STATE_READY){
			//当前状态不可扫荡副本
			return AppErrorCode.FUBEN_NOT_TONGGUAN;
		}
		List<Fuben> list = fubenDao.cacheLoadAll(userRoleId);
		StringBuffer saodangIds = new StringBuffer();
		for (Fuben fuben : list) {
			calFuben(fuben);
			if(fuben.getCount() < 1){
				saodangIds.append(GameConstants.SAODANG_FUBENIDS_SPLIT).append(fuben.getFubenId());
			}
		}
		if(saodangIds.length() < 1){
			//没有可扫荡的
			return AppErrorCode.FUBEN_NO_CAN_SD;
		}
		//判断VIP是否瞬秒
		int cd = roleVipInfoExportService.getVipTequan(userRoleId, GameConstants.VIP_FUBEN_CD_TIME) * 1000;
		roleFuben.setCd(cd);

		roleFuben.setSaodangIds(saodangIds.substring(GameConstants.SAODANG_FUBENIDS_SPLIT.length()));
		roleFuben.setState(GameConstants.FUBEN_STATE_SAODANG);
		roleFuben.setSaodangStart(GameSystemTime.getSystemMillTime());
		roleFubenDao.cacheUpdate(roleFuben, userRoleId);
		
		if(cd > 0){
			startSchedule(userRoleId,cd);
		}else{//瞬间完成
			finishSaodao(userRoleId);
		}
		//活跃度lxn
		huoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A16);
		
		return new Object[]{1,roleFuben.getSaodangEnd()};
	}
	
	/**
	 * 启动扫荡定时
	 * @param userRoleId
	 * @param time
	 */
	private void startSchedule(Long userRoleId,int time){
		BusTokenRunable runable = new BusTokenRunable(userRoleId, InnerCmdType.B_FINISH_SAODANG_FUBEN, userRoleId);
		scheduleExportService.schedule(userRoleId.toString(), GameConstants.COMPONENT_FUBEN_SAODANG, runable, time, TimeUnit.MILLISECONDS);
	}
	/**
	 * 取消扫荡定时
	 * @param userRoleId
	 */
	private void cancelSchedule(Long userRoleId){
		scheduleExportService.cancelSchedule(userRoleId.toString(), GameConstants.COMPONENT_FUBEN_SAODANG);
	}
	
	/**
	 * 完成扫荡
	 * @param userRoleId
	 */
	public void finishSaodao(Long userRoleId){
		if(!publicRoleStateExportService.isPublicOnline(userRoleId)){
			return;
		}
		RoleFuben roleFuben = getRoleFuben(userRoleId);
		if(roleFuben.getState() != GameConstants.FUBEN_STATE_SAODANG){
			return;
		}
		roleFuben.setState(GameConstants.FUBEN_STATE_READY);
		long start = roleFuben.getSaodangStart();
		roleFuben.setSaodangStart(0l);
		String[] fubenIds = roleFuben.getSaodangIds().split(GameConstants.SAODANG_FUBENIDS_SPLIT);
		roleFuben.setSaodangIds("");
		roleFubenDao.cacheUpdate(roleFuben, userRoleId);
		boolean today = DatetimeUtil.dayIsToday(start);
		Map<String,Integer> item = new HashMap<>();
		int money = 0;
		int exp = 0;
		for (String fubenId : fubenIds) {
			Integer fid = Integer.parseInt(fubenId);
			DayFubenConfig config = dayFubenConfigService.loadById(fid);
			if(config == null){
				continue;
			}
			if(today){
				//修改今日次数
				Fuben fuben = getFuben(userRoleId, fid,GameConstants.FUBEN_TYPE_DAY);
				if(fuben == null){
					continue;
				}
				if(fuben.getCount() > 0){
					continue;
				}
				fuben.setCount(1);
				fuben.setUpdateTime(GameSystemTime.getSystemMillTime());
				fubenDao.cacheUpdate(fuben, userRoleId);
				
				//成就
				if(fuben.getFubenId().intValue() == 1 && fuben.getType().intValue() == GameConstants.FUBEN_TYPE_DAY){
					try {
						BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_GOUYUFUBENCOUNT, 1});
						//roleChengJiuExportService.tuisongChengJiu(userRoleId, GameConstants.CJ_GOUYUFUBENCOUNT, 1);
					} catch (Exception e) {
						ChuanQiLog.error("",e);
					}
				}
				//修炼任务
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.FUBEN_DANREN, null});
			}
			ObjectUtil.mapAdd(item, config.getDayFirst());
			ObjectUtil.mapAdd(item, config.getProp());
			money += config.getMoney();
			exp += config.getExp();
			//打印日志
			printLog(config, userRoleId, false, true, LogPrintHandle.FUBEN_TYPE_SAODANG);
		}
		if(money > 0){
			item.put(ModulePropIdConstant.MONEY_GOODS_ID, money);
		}
		if(exp > 0){
			item.put(ModulePropIdConstant.EXP_GOODS_ID, exp);
		}
		
		
//		String content = EmailUtil.getCodeEmail(GameConstants.FUBEN_SAODANG_EMAIL_CONTENT_CODE);
//		roleBagExportService.putInBagOrEmailWithNumber(item, userRoleId, GoodsSource.DAY_FUBEN_GET, LogPrintHandle.GET_FUBENDAY_SHAODANG, LogPrintHandle.GBZ_FUBENDAY_SAODANG, true, content);
//		
//		
		sendEmail(userRoleId, GameConstants.FUBEN_SAODANG_EMAIL_CONTENT_CODE, item);
		BusMsgSender.send2One(userRoleId, ClientCmdType.FUBEN_SAODANG_FINISH, fubenIds);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.FUBEN_SAODANG_FINISH, new Object[]{fubenIds,cover2ClientVo(item)});
	}
	
	private Object cover2ClientVo(Map<String,Integer> item){
		if(item == null){
			return null;
		}
		List<Object[]> result = new ArrayList<>();
		for (Entry<String,Integer> entry : item.entrySet()) {
			result.add(new Object[]{entry.getKey(),entry.getValue()});
		}
		return result.toArray();
	}
	
	/**
	 * 日志打印
	 * @param config	副本配置
	 * @param userRoleId	角色id
	 * @param first	是否是首次
	 * @param dayFirst	是否是当日首次
	 * @param type	挑战、扫荡
	 */
	private void printLog(DayFubenConfig config,long userRoleId,boolean first,boolean dayFirst,int type){
		try{
			if(config == null)return;
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			JSONArray goods = LogPrintHandle.getLogGoodsParam(config.getProp(), null);
			if(first){
				goods = LogPrintHandle.getLogGoodsParam(config.getFirst(), goods);
			}else if(dayFirst){
				goods = LogPrintHandle.getLogGoodsParam(config.getDayFirst(), goods);
			}
			GamePublishEvent.publishEvent(new FubenLogEvent(userRoleId, role.getName(), config.getId(),goods , type, config.getExp(), config.getMoney()));
		}catch (Exception e) {
			ChuanQiLog.error(userRoleId+",",e);
		}
	}
	
	/**
	 * 获取守护副本信息（不回为null）
	 * @param userRoleId
	 * @return
	 */
	private ShouhuFuben getShouhuFuben(Long userRoleId){
		ShouhuFuben fuben = shouhuFubenDao.cacheAsynLoad(userRoleId, userRoleId);
		if(fuben == null){
			fuben = new ShouhuFuben();
			fuben.setMaxId(1);
			fuben.setNowId(1);
			fuben.setStartId(1);
			fuben.setRestartTime(0);
			fuben.setUpdateTime(GameSystemTime.getSystemMillTime());
			fuben.setUserRoleId(userRoleId);
			shouhuFubenDao.cacheInsert(fuben, userRoleId);
		}else if(!DatetimeUtil.dayIsToday(fuben.getUpdateTime())){
			shouhuKuaTian(fuben);
		}
		return fuben;
	}
	
	public int getShouhuFubenMax(Long userRoleId){
		ShouhuFuben fuben = getShouhuFuben(userRoleId);
		return fuben.getMaxId() - 1 ;
	}
	
	public Object[] getShouhuInfo(Long userRoleId){
		ShouhuFuben fuben = getShouhuFuben(userRoleId);
		Object[] state = fuben.getState() == null ? new Object[]{0} : fuben.getState().toArray();
		return new Object[]{state,fuben.getMaxId(),fuben.getNowId(),fuben.getRestartTime()};
	}
	
	/**
	 * 申请进入守护副本
	 * @param userRoleId
	 */
	public void enterShouhuFuben(Long userRoleId, BusMsgQueue busMsgQueue){
		ShouhuFuben fuben = getShouhuFuben(userRoleId);
		ShouhuFubenConfig config = shouhuFubenConfigService.loadById(fuben.getNowId());
		if(config == null){
			//配置异常
			busMsgQueue.addMsg(userRoleId, ClientCmdType.FUBEN_SHOUHU_ENTER, AppErrorCode.CONFIG_ERROR);
			return ;
		}
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(role == null){
			//角色不存在
			busMsgQueue.addMsg(userRoleId, ClientCmdType.FUBEN_SHOUHU_ENTER, AppErrorCode.ROLE_IS_NOT_EXIST);
			return ;
		}
		if(role.getLevel() < config.getLevel()){
			//等级不足
			busMsgQueue.addMsg(userRoleId, ClientCmdType.FUBEN_SHOUHU_ENTER, AppErrorCode.ROLE_LEVEL_NOT_ENOUGH);
			return;
		}
		if(stageControllExportService.inFuben(userRoleId)){
			//在副本中
			busMsgQueue.addMsg(userRoleId, ClientCmdType.FUBEN_SHOUHU_ENTER, AppErrorCode.FUBEN_IS_IN_FUBEN);
			return;
		}
		fuben.setStartId(fuben.getNowId());
		shouhuFubenDao.cacheUpdate(fuben, userRoleId);
		
		ShouhuPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_SHOUHU);
		//发送到场景进入地图
		DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(publicConfig.getMapId());
		int[] birthXy = dituCoinfig.getRandomBirth();
		Object[] applyEnterData = new Object[]{dituCoinfig.getId(),birthXy[0],birthXy[1], MapType.SAVE_FUBEN_MAP, config.getId()};
		busMsgQueue.addBusMsg(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
		//活跃度lxn
		huoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A19);
		//推送给客户端进入成功
		busMsgQueue.addMsg(userRoleId, ClientCmdType.FUBEN_SHOUHU_ENTER, AppErrorCode.OK);
	}
	/**
	 * 同步已完成关卡
	 * @param userRoleId
	 * @param id
	 */
	public void synShouhuFuben(Long userRoleId,Integer id){
		ShouhuFuben fuben = getShouhuFuben(userRoleId);
		if(fuben.getMaxId() < id){
			fuben.setMaxId(id);
			//成就
			try {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_NANWUYUEFUBENCOUNT,  id-1});
				//修炼任务
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE_TS, new Object[] {XiuLianConstants.FUBEN_NANWUYUE, id-1});
			} catch (Exception e) {
				ChuanQiLog.error("",e);
			}
		}
		fuben.setNowId(id);
		shouhuFubenDao.cacheUpdate(fuben, userRoleId);
		
		//支线记录 异族入侵
        StageMsgSender.send2Bus(userRoleId, InnerCmdType.INNER_BRANCH_COUNT, new Object[]{BranchEnum.B9,id-1});
	}
	/**
	 * 守护副本上线业务
	 * @param userRoleId
	 */
	public void shouhuOnlineHandle(Long userRoleId){
		ShouhuFuben fuben = getShouhuFuben(userRoleId);
		calShouhuFubenGitf(fuben);
		try {
			if(fuben.getMaxId() <= 1){
				BusMsgSender.send2One(userRoleId, ClientCmdType.FUWU_TUISONG_NANWUYUE, 0);
			}
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
	
	public void leaveShouhuFuben(Long userRoleId, BusMsgQueue busMsgQueue){
		if(!stageControllExportService.inFuben(userRoleId)){
			//不在副本中
			busMsgQueue.addMsg(userRoleId, ClientCmdType.FUBEN_SHOUHU_EXIT, AppErrorCode.FUBEN_NOT_IN_FUBEN);
			return;
		}
		ShouhuFuben fuben = getShouhuFuben(userRoleId);
		calShouhuFubenGitf(fuben);
		
		//发送到场景处理退出副本
		busMsgQueue.addStageMsg(userRoleId, InnerCmdType.S_EXIT_FUBEN, null);
//		//推送给客户端退出副本成功	Liuyu：改为场景内通知
//		busMsgQueue.addMsg(userRoleId, ClientCmdType.FUBEN_SHOUHU_EXIT, AppErrorCode.OK);
	}
	
	public Object[] restartShouhu(Long userRoleId){
		ShouhuFuben fuben = getShouhuFuben(userRoleId);
		boolean free = false;
		if(fuben.getRestartTime() < 1){
			free = true;
		}
		if(!free){
			int addTime = roleVipInfoExportService.getVipTequan(userRoleId, GameConstants.VIP_FUBEN_SHOUHU_BUY);
			if(addTime < fuben.getRestartTime()){
				return AppErrorCode.FUBEN_NO_RESET_COUNT;//次数不足
			}
		}
		if(!free){
			ShouhuPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_SHOUHU);
			Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, publicConfig.getNeedGold(), userRoleId, LogPrintHandle.CONSUME_FUBEN, true, LogPrintHandle.CBZ_FUBEN_SHOUHU);
			if(result != null){
				return result;
			}else{
				if(PlatformConstants.isQQ()){
					BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,publicConfig.getNeedGold(),LogPrintHandle.CONSUME_FUBEN,QQXiaoFeiType.CONSUME_FUBEN,1});
				}
			}
		}
		fuben.setRestartTime(fuben.getRestartTime() + 1);
		fuben.setNowId(1);
		shouhuFubenDao.cacheUpdate(fuben, userRoleId);
		
		return AppErrorCode.OK;
	}
	
	/**
	 * 获取守护副本首次通关奖励
	 * @param userRoleId
	 * @param index
	 * @return
	 */
	public Object[] getTongguanGift(Long userRoleId,Integer id){
		ShouhuFuben fuben = getShouhuFuben(userRoleId);
		if(fuben.getMaxId() <= id){
			return AppErrorCode.FUBEN_HAS_NO_TONGGUAN;//尚未通关
		}
		ShouhuFubenConfig config = shouhuFubenConfigService.loadById(id);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		Integer index = shouhuFubenConfigService.getGiftIndexById(id);
		if(index == null){
			return AppErrorCode.FUBEN_CUR_NO_TONGGUAN_GIFT;//当前关卡无通关奖励
		}
		int page = index / GameConstants.PER_STATE_MAX;
		index = index % GameConstants.PER_STATE_MAX;
		List<Integer> states = fuben.getState();
		while(page >= states.size()){
			states.add(0);
		}
		int state = states.get(page);
		if(!BitOperationUtil.calState(state, index)){
			return AppErrorCode.FUBEN_TONGGUAN_GIFT_IS_RECIVED;//已领取过
		}
		Object[] result = roleBagExportService.checkPutInBag(config.getItemId(), 1, userRoleId);
		if(result != null){
			return result;
		}
		
		state = BitOperationUtil.chanageState(state, index);
		states.remove(page);
		states.add(page, state);
		fuben.refreshState();
		shouhuFubenDao.cacheUpdate(fuben, userRoleId);

		RoleItemInput item = BagUtil.createItem(config.getItemId(), 1, 0);
		roleBagExportService.putInBag(item, userRoleId, GoodsSource.SH_FB_FIRST_GIST, true);
		
		return AppErrorCode.OK;
	}
	
	/**
	 * 扫荡守护副本
	 * @param userRoleId
	 * @return
	 */
	public Object[] saodangShouHu(Long userRoleId){
		ShouhuFuben fuben = getShouhuFuben(userRoleId);
		int count = fuben.getMaxId() - fuben.getNowId();
		if(count < 1){
			return AppErrorCode.FUBEN_NO_CAN_SD;//没有可扫荡关卡
		}
		if(stageControllExportService.inFuben(userRoleId)){
			//在副本中
			return AppErrorCode.FUBEN_IS_IN_FUBEN;
		}
		
		int costMoney = (int)(Math.round(Math.pow(count, 0.9) * 6.5) * 10000);//公式计算消耗
		Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY, costMoney, userRoleId, LogPrintHandle.CONSUME_FUBEN, true, LogPrintHandle.CBZ_FUBEN_SHOUHU_SD);
		if(result != null){
			return result;
		}
		
		long exp = 0;
		long money = 0;
		long zhenqi = 0;
		for (int i = fuben.getNowId(); i < fuben.getMaxId(); i++) {
			ShouhuFubenConfig config = shouhuFubenConfigService.loadById(i);
			if(config != null){
				exp += config.getExp();
				money += config.getMoney();
				zhenqi += config.getZhenqi();
			}
		}
		
		fuben.setNowId(fuben.getMaxId());
		shouhuFubenDao.cacheUpdate(fuben, userRoleId);
		
		roleExportService.incrExp(userRoleId, exp);
		accountExportService.incrCurrencyWithNotify(GoodsCategory.MONEY, money, userRoleId, LogPrintHandle.GET_FUBEN, LogPrintHandle.GBZ_SHOUHU_FUBEN);
		roleExportService.addZhenqi(userRoleId, zhenqi);
		//活跃度lxn
		huoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A19);
		return AppErrorCode.OK;
	}
	
	
	/**
	 * 获取单人副本是否领取过
	 * @param userRoleId
	 * @return
	 */
	public int getShouhuStateValue(Long userRoleId){
		// int(0)二进制   奖励 有为1，无为0  顺序为  守护南无月
		int state = 0;
		ShouhuFuben fuben = shouhuFubenDao.cacheAsynLoad(userRoleId, userRoleId);
		if(fuben == null){
			//没有守护南无月 数据直接返回
			return state;
		}
		List<Integer> ids = shouhuFubenConfigService.getGuanKaIds();
		if(ids == null || ids.size() == 0){
			return state;
		}
		
		//记录的最高关卡(需要减1)
		int maxGq = fuben.getMaxId() - 1;
		int firstGiftGuan = ids.get(0);//第一个奖励关卡
		if(firstGiftGuan > maxGq){
			//没有打过，数据直接返回
			return state;
		}
		
		List<Integer> states = fuben.getState();
		
		int lastPage = states.size() - 1;
		if(lastPage < 0){
			return 1;//守护南无月(0) 2的0次方
		}
		if(lastPage > 0){
			for (int i = 0; i < lastPage; i++) {
				int tmp = states.get(i);
				if(tmp != Integer.MAX_VALUE){
					return 1;//守护南无月(0) 2的0次方
				}
			}
		}
		int tmpState = states.get(lastPage);
		int start = 31 * lastPage;
		for (int i = 0 ; i < 31; i++) {
			int index = i + start;
			if(index >= ids.size()){
				return state;
			}
			int configId = ids.get(index);
			if(maxGq >= configId){
				//有奖励的二进制索引号
				if(BitOperationUtil.calState(tmpState, i)){
					return 1;//守护南无月(0) 2的0次方
				}
			}else{
				//已通关的奖励已全部领取完
				return state;
			}
		}
		
		return state;
	}
	
	
	
	//TODO ----以下方法供资源找回模块使用---------
	/**
	 * 计算日常副本资源情况
	 */
	public void calDayFubenResource(Map<String,Map<String,Integer>> map,Long userRoleId){
		List<Fuben> list = fubenDao.cacheLoadAll(userRoleId);
		if(list != null && list.size() > 0){
			for (Fuben fuben : list) {
				int day = DatetimeUtil.twoDaysDiffence(fuben.getUpdateTime());
				if(day > 0){
					if(fuben.getType().equals(GameConstants.FUBEN_TYPE_DAY)){
						DayFubenConfig config = dayFubenConfigService.loadById(fuben.getFubenId());
						
						if(day <= GameConstants.RESOURCE_BACK_MAX_DAY && fuben.getCount() > 0){
							Map<String,Integer> dayMap = new HashMap<>();
							int count = config.getCount() - fuben.getCount();
							dayMap.put(ModulePropIdConstant.EXP_GOODS_ID, config.getExp() * count);
							dayMap.put(ModulePropIdConstant.MONEY_GOODS_ID, config.getMoney() * count);
							Map<String,Integer> oldMap = map.get(day+"");
							if(oldMap != null){
								ObjectUtil.mapAdd(oldMap, dayMap);
							}else{
								map.put(day+"", dayMap);
							}
							day--;
						}else if(day > GameConstants.RESOURCE_BACK_MAX_DAY){
							day = GameConstants.RESOURCE_BACK_MAX_DAY;
						}
						if(day > 0){
							Map<String,Integer> dayMap = new HashMap<>();
							dayMap.put(ModulePropIdConstant.EXP_GOODS_ID, config.getExp() * config.getCount());
							dayMap.put(ModulePropIdConstant.MONEY_GOODS_ID, config.getMoney() * config.getCount());
							for (; day > 0; day--) {
								Map<String,Integer> oldMap = map.get(day+"");
								if(oldMap != null){
									ObjectUtil.mapAdd(oldMap, dayMap);
								}else{
									map.put(day+"", dayMap);
								}
							}
						}
					}
					fuben.setCount(0);
					fuben.setUpdateTime(GameSystemTime.getSystemMillTime());
					fubenDao.cacheUpdate(fuben, fuben.getUserRoleId());
				}
			}
		}
	}
	/**
	 * 计算守护副本资源情况
	 */
	public void calShouHuFubenResource(Map<String,Map<String,Integer>> map,Long userRoleId){
		ShouhuFuben fuben = shouhuFubenDao.cacheAsynLoad(userRoleId, userRoleId);
		if(fuben != null){
			calShouHuFubenResource(map,fuben);
		}
	}

	private void calShouHuFubenResource(Map<String, Map<String, Integer>> map,ShouhuFuben fuben) {
		int day = DatetimeUtil.twoDaysDiffence(fuben.getUpdateTime());
		if(day > 0){
			if(fuben.getRestartTime() > 0){
				day--;
			}
			if(day > 0 && fuben.getMaxId() > 1){
				if(day > GameConstants.RESOURCE_BACK_MAX_DAY){
					day = GameConstants.RESOURCE_BACK_MAX_DAY;
				}
				int exp = 0;
				int money = 0;
				int zhenqi = 0;
				for (int i = 1; i < fuben.getMaxId(); i++) {
					ShouhuFubenConfig config = shouhuFubenConfigService.loadById(i);
					if(config != null){
						exp += config.getExp();
						money += config.getMoney();
						zhenqi += config.getZhenqi();
					}
				}
				Map<String,Integer> dayMap = new HashMap<>();
				dayMap.put(ModulePropIdConstant.EXP_GOODS_ID, exp);
				dayMap.put(ModulePropIdConstant.MONEY_GOODS_ID, money);
				dayMap.put(ModulePropIdConstant.MONEY_ZHENQI_ID, zhenqi);
				for (; day > 0; day--) {
					map.put(day+"", new HashMap<>(dayMap));
				}
			}
			fuben.setRestartTime(0);
			fuben.setUpdateTime(GameSystemTime.getSystemMillTime());
			shouhuFubenDao.cacheUpdate(fuben, fuben.getUserRoleId());
		}
	}
	
	public void fubenKuaTian(Long userRoleId){
		try{
			Map<String,Map<String,Integer>> map = new HashMap<>();
			calDayFubenResource(map,userRoleId);
			roleResourceBackExportService.changeTypeMap(userRoleId, map, GameConstants.RESOURCE_TYPE_DAY_FUBEN);
		}catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
	
	public void shouhuKuaTian(ShouhuFuben fuben){
		try{
			Map<String,Map<String,Integer>> map = new HashMap<>();
			calShouHuFubenResource(map, fuben);
			roleResourceBackExportService.changeTypeMap(fuben.getUserRoleId(), map, GameConstants.RESOURCE_TYPE_SHOUHU_FUBEN);
		}catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
 
	/**
	 * 获取云浮剑冢信息
	 */
	public Object[] getJianzhongInfo(Long userRoleId){
		JianzhongFuben jianzhongFuben =  getJianzhongFuben(userRoleId);
		boolean isTiaoZhan = true;
		if(jianzhongFuben.getState().intValue()==1){
			if(DatetimeUtil.dayIsToday(jianzhongFuben.getUpdateTime())){
				isTiaoZhan  = false;
			}else{
				//跨天
				jianzhongFuben.setUpdateTime(GameSystemTime.getSystemMillTime());
				jianzhongFuben.setState(0);
				jianzhongFubenDao.cacheUpdate(jianzhongFuben, userRoleId);
			}
		} 
		return new Object[]{jianzhongFuben.getMaxKillMonster(),jianzhongFuben.getMaxJingqi(),isTiaoZhan};
	}
	
	
	/**
	 * 申请进入剑冢副本
	 * @param userRoleId
	 */
	public void enterJianzhongFuben(Long userRoleId, BusMsgQueue busMsgQueue){
		JianzhongFuben fuben = getJianzhongFuben(userRoleId);
		JianzhongFubenConfig config = jianzhongConfigService.loadJianZhongConfig();
		if(config == null){
			//配置异常
			busMsgQueue.addMsg(userRoleId, ClientCmdType.JIANZHONG_FUBEN_ENTER, AppErrorCode.CONFIG_ERROR);
			return ;
		}
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(role == null){
			//角色不存在
			busMsgQueue.addMsg(userRoleId, ClientCmdType.JIANZHONG_FUBEN_ENTER, AppErrorCode.ROLE_IS_NOT_EXIST);
			return ;
		}
		if(role.getLevel() < config.getOpenLevel()){
			//等级不足
			busMsgQueue.addMsg(userRoleId, ClientCmdType.JIANZHONG_FUBEN_ENTER, AppErrorCode.ROLE_LEVEL_NOT_ENOUGH);
			return;
		}
		if(stageControllExportService.inFuben(userRoleId)){
			//在副本中
			busMsgQueue.addMsg(userRoleId, ClientCmdType.JIANZHONG_FUBEN_ENTER, AppErrorCode.FUBEN_IS_IN_FUBEN);
			return;
		}
		//一天只能进一次
		if(fuben.getState().intValue()==1 && DatetimeUtil.dayIsToday(fuben.getUpdateTime())){
			busMsgQueue.addMsg(userRoleId, ClientCmdType.JIANZHONG_FUBEN_ENTER, AppErrorCode.FUBEN_NO_COUNT);
			return;
		} 
		fuben.setState(1);
		fuben.setUpdateTime(GameSystemTime.getSystemMillTime());
		jianzhongFubenDao.cacheUpdate(fuben, userRoleId);
		
		YaoshenHunpoPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_YAOSHEN_HUNPO);
		//发送到场景进入地图
		DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(publicConfig.getMapId());
		int[] birthXy = dituCoinfig.getRandomBirth();
		Object[] applyEnterData = new Object[]{dituCoinfig.getId(),birthXy[0],birthXy[1], MapType.JIANZHONG_FUBEN_MAP,GameConstants.HUNPO_FUBEN_ID};
		busMsgQueue.addBusMsg(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
		//推送给客户端进入成功
		busMsgQueue.addMsg(userRoleId, ClientCmdType.JIANZHONG_FUBEN_ENTER, new Object[]{1,GameSystemTime.getSystemMillTime()+publicConfig.getTime()*1000} );
	}
	/**
	 * 剑冢副本时间到
	 * @param userRoleId
	 * @param busMsgQueue
	 */
	public void jianzhongFubenTimeOver(Long userRoleId, BusMsgQueue busMsgQueue){
		if(!stageControllExportService.inFuben(userRoleId)){
			return;
		}
		String stageId = publicRoleStateService.getRolePublicStageId(userRoleId);
		JianzhongFubenStage stage = (JianzhongFubenStage)StageManager.getStage(stageId);
		if(stage==null){
			return ;
		}
		updateJianzhongFubenData(userRoleId, stage.getKillMonsterNum(),stage.getJingqiItemNum());
		//通关奖励
		YaoshenHunpoPublicConfig config  = getyYaoshenHunpoPublicConfig();
		Map<String,Integer > jiangliItem = config.getJiangItem();
		roleBagExportService.putInBag(jiangliItem, userRoleId, GoodsSource.JIANZHONG_FUBEN_GET, true);
		
		//此次副本击杀数和道具获取数
//		BusMsgSender.send2One(userRoleId, ClientCmdType.JIANZHONG_FUBEN_RESULT, new Object[]{stage.getKillMonsterNum(),stage.getJingqiItemNum()});
		//通知场景副本时间到
		BusMsgSender.send2Stage(userRoleId,stage.getFinishCmd(), null);
	}
	private void updateJianzhongFubenData(Long userRoleId,int killMonsterNum,int jingqiNum){
		JianzhongFuben jianzhongFuben = getJianzhongFuben(userRoleId);
		boolean isUpdate  =  false;
		if(killMonsterNum > jianzhongFuben.getMaxKillMonster()){
			jianzhongFuben.setMaxKillMonster(killMonsterNum);
			isUpdate  = true;
		}
		if(jingqiNum > jianzhongFuben.getMaxJingqi()){
			jianzhongFuben.setMaxJingqi(jingqiNum);
			isUpdate  = true;
		}
		if(isUpdate){
			jianzhongFubenDao.cacheUpdate(jianzhongFuben, userRoleId);
		}
	}
	/**
	 * 玩家主动退出副本
	 * @param userRoleId
	 * @param busMsgQueue
	 */
	public void leaveJianzhongFuben(Long userRoleId, BusMsgQueue busMsgQueue){
		if(!stageControllExportService.inFuben(userRoleId)){
			//不在副本中
			busMsgQueue.addMsg(userRoleId, ClientCmdType.JIANZHONG_FUBEN_EXIT, AppErrorCode.FUBEN_NOT_IN_FUBEN);
			return;
		}
		
		String stageId = publicRoleStateService.getRolePublicStageId(userRoleId);
		JianzhongFubenStage stage = (JianzhongFubenStage)StageManager.getStage(stageId);
		 
		updateJianzhongFubenData(userRoleId, stage.getKillMonsterNum(), stage.getJingqiItemNum());
		//立马回收
		BusMsgSender.send2Stage(userRoleId,InnerCmdType.S_FORCE_EXIT_FUBEN, null);
		
	}
	public void hasLeaveJianzhongFuben(Long userRoleId){
		//更新数据
//		updateJianzhongFubenData(userRoleId, killMonsterNum, jingqiNum);
//		BusMsgSender.send2One(userRoleId, ClientCmdType.JIANZHONG_FUBEN_RESULT, new Object[]{killMonsterNum,jingqiNum});
		//推送给客户端退出副本成功
//		BusMsgSender.send2One(userRoleId, ClientCmdType.JIANZHONG_FUBEN_EXIT, AppErrorCode.OK);
		//立马回收场景清场踢人
		BusMsgSender.send2Stage(userRoleId,InnerCmdType.S_FORCE_EXIT_FUBEN, null);
		 
	}
	/**
	 * 妖神魂魄配置
	 * @return
	 */
	private YaoshenHunpoPublicConfig getyYaoshenHunpoPublicConfig(){
		YaoshenHunpoPublicConfig yaoshenHunpoPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_YAOSHEN_HUNPO);
		return  yaoshenHunpoPublicConfig;
	}
	/**
	 * 获取云浮剑冢副本信息entity
	 */
	private JianzhongFuben  getJianzhongFuben(Long userRoleId){
		JianzhongFuben entity  = jianzhongFubenDao.cacheAsynLoad(userRoleId, userRoleId);
		if(entity==null){
			entity  = new JianzhongFuben();
			entity.setUserRoleId(userRoleId);
			entity.setMaxJingqi(0);
			entity.setMaxKillMonster(0);
			entity.setUpdateTime(GameSystemTime.getSystemMillTime());
			entity.setState(0);
			jianzhongFubenDao.cacheInsert(entity, userRoleId);
		}
		return entity;
	}
	
}
