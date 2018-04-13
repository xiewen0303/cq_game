package com.junyou.bus.fuben.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.fuben.dao.PataCengInfoDao;
import com.junyou.bus.fuben.dao.PataInfoDao;
import com.junyou.bus.fuben.entity.PataCengInfo;
import com.junyou.bus.fuben.entity.PataCengInfoManager;
import com.junyou.bus.fuben.entity.PataConfig;
import com.junyou.bus.fuben.entity.PataInfo;
import com.junyou.bus.huoyuedu.enums.ActivityEnum;
import com.junyou.bus.huoyuedu.export.HuoYueDuExportService;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.PataPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * 	爬塔
 * @author LiuYu
 * 2015-6-10 下午6:47:24
 */
@Service
public class PataService {
	@Autowired
	private PataInfoDao pataInfoDao;
	@Autowired
	private StageControllExportService stageControllExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private PataConfigService pataConfigService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	@Autowired
	private PataCengInfoDao pataCengInfoDao;
	@Autowired 
	private HuoYueDuExportService huoYueDuExportService;
	
	private PataInfo getpataInfo(Long userRoleId){
		PataInfo pata = pataInfoDao.cacheAsynLoad(userRoleId, userRoleId);
		if(pata == null){
			pata = createPata(userRoleId);
		}else if(!DatetimeUtil.dayIsToday(pata.getUpdateTime())){
			pata.setBuyCount(0);
			pata.setCount(0);
			pata.setYuanzhuCount(0);
			pata.setUpdateTime(GameSystemTime.getSystemMillTime());
			pataInfoDao.cacheUpdate(pata, userRoleId);
		}
		return pata;
	}
	
	public int getPataMaxCeng(Long userRoleId){
		PataInfo pata = getpataInfo(userRoleId);
		return pata.getMaxCeng();
	}
	
	private PataInfo createPata(Long userRoleId){
		PataInfo pata = new PataInfo();
		pata.setUserRoleId(userRoleId);
		pata.setBestTime("");
		pata.setMaxCeng(0);
		pata.setBuyCount(0);
		pata.setCount(0);
		pata.setYuanzhuCount(0);
		pata.setUpdateTime(GameSystemTime.getSystemMillTime());
		pataInfoDao.cacheInsert(pata, userRoleId);
		return pata;
	}
	
	public Object[] getRolePataInfo(Long userRoleId){
		PataInfo pataInfo = getpataInfo(userRoleId);
		return new Object[]{pataInfo.getCount(),pataInfo.getBuyCount(),pataInfo.getMaxCeng()};
	}
	
	public Object[] getCengInfo(Long userRoleId,Integer cengId){
		String name = null;
		int time = 0;
		PataCengInfo pataCengInfo = PataCengInfoManager.getManager().getCengInfo(cengId);
		if(pataCengInfo != null){
			name = pataCengInfo.getLastRole();
			time = pataCengInfo.getLastTime();
		}
		PataInfo pata = getpataInfo(userRoleId);
		Integer bestTime = pata.getCengBestTime(cengId+"");
		return new Object[]{cengId,name,time,bestTime};
	}
	
	public void enterTiaozhan(Long userRoleId,Integer cengId, BusMsgQueue busMsgQueue){
		
		PataInfo pataInfo = getpataInfo(userRoleId);
		if(pataInfo.getState() != GameConstants.FUBEN_STATE_READY){
			//当前状态不可挑战副本
			busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_PATA, AppErrorCode.FUBEN_STATE_ERROR_TZ);
			return;
		}
		if(cengId - 1 > pataInfo.getMaxCeng()){
			//当前层不可挑战
			busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_PATA, AppErrorCode.FUBEN_NOW_CENG_CANNOT_TIAOZHAN);
			return;
		}
		PataPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_PATA);
		if(pataInfo.getBuyCount() + publicConfig.getFreeTime() <= pataInfo.getCount()){
			//没次数
			busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_PATA, AppErrorCode.FUBEN_NO_COUNT);
			return;
		}
		if(stageControllExportService.inFuben(userRoleId)){
			//在副本中
			busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_PATA, AppErrorCode.FUBEN_IS_IN_FUBEN);
			return;
		}
		if(pataConfigService.loadById(cengId) == null){
			//配置异常
			busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_PATA, AppErrorCode.CONFIG_ERROR);
			return;
		}
		pataInfo.setState(GameConstants.FUBEN_STATE_FIGHT);
		pataInfo.setEnterCeng(cengId);
		pataInfo.setEnterTime(GameSystemTime.getSystemMillTime());
		pataInfoDao.cacheUpdate(pataInfo, userRoleId);
		
		//发送到场景进入地图
		DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(publicConfig.getMapId());
		int[] birthXy = dituCoinfig.getRandomBirth();
		Object[] applyEnterData = new Object[]{dituCoinfig.getId(),birthXy[0],birthXy[1], MapType.PATA_FUBEN_MAP, cengId};
		busMsgQueue.addBusMsg(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
		//活跃度lxn
		huoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A20);
		//推送给客户端进入成功
		busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_PATA, new Object[]{AppErrorCode.SUCCESS, cengId});
	}
	
	public void finishPata(Long userRoleId){
		PataInfo pataInfo = getpataInfo(userRoleId);
		if(pataInfo.getState() != GameConstants.FUBEN_STATE_FIGHT){
			return;
		}
		int cengId = pataInfo.getEnterCeng();
		pataInfo.setState(GameConstants.FUBEN_STATE_READY);
		pataInfo.setCount(pataInfo.getCount() + 1);
		long time = GameSystemTime.getSystemMillTime() - pataInfo.getEnterTime();
		int curTime = (int)(time / 1000);
		Integer bestTime = pataInfo.getCengBestTime(cengId + "");
		boolean record = false;
		if(bestTime == null || bestTime > curTime){
			pataInfo.changeBestTime(cengId + "", curTime);
			record = true;
		}
		if(cengId > pataInfo.getMaxCeng()){
			pataInfo.setMaxCeng(cengId);
			try {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_XIANMOBANGCENG, pataInfo.getMaxCeng()});
				//修炼任务
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE_TS, new Object[] {XiuLianConstants.FUBEN_PATA, pataInfo.getMaxCeng()});
			} catch (Exception e) {
				ChuanQiLog.error("",e);
			}
		}
		pataInfoDao.cacheUpdate(pataInfo, userRoleId);
		
		PataConfig config = pataConfigService.loadById(cengId);
		if(config == null){
			return;
		}
		roleExportService.incrExp(userRoleId, config.getExp());
		roleExportService.addZhenqi(userRoleId, config.getZq());
		accountExportService.incrCurrencyWithNotify(GoodsCategory.MONEY, config.getMoney(), userRoleId, LogPrintHandle.GET_PATA_GIFT, LogPrintHandle.GBZ_PATA_GIFT);
		
		PataCengInfo pataCengInfo = PataCengInfoManager.getManager().getCengInfo(cengId);
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(pataCengInfo == null){
			PataCengInfoManager.getManager().initCengInfo(cengId, role.getName(), curTime);
			BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{GameConstants.PATA_FIRST_GONGGAO,new Object[]{role.getName(),cengId}});
		}else{
			if(pataCengInfo.getLastTime() > curTime){
				pataCengInfo.setLastRole(role.getName());
				pataCengInfo.setLastTime(curTime);
				BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{GameConstants.PATA_FAST_GONGGAO,new Object[]{role.getName(),cengId}});
			}
		}
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.PATA_ANSWER, new Object[]{1,curTime,record});
	}
	
	public void pataFail(Long userRoleId){
		PataInfo pataInfo = getpataInfo(userRoleId);
		if(pataInfo.getState() != GameConstants.FUBEN_STATE_FIGHT){
			return;
		}
		long time = GameSystemTime.getSystemMillTime() - pataInfo.getEnterTime();
		int curTime = (int)(time / 1000);
		pataInfo.setState(GameConstants.FUBEN_STATE_READY);
		pataInfoDao.cacheUpdate(pataInfo, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.PATA_ANSWER, new Object[]{0,curTime,false});
	}
	
	public Object[] buyCount(Long userRoleId){
		PataInfo pataInfo = getpataInfo(userRoleId);
		int maxCount = roleVipInfoExportService.getVipTequan(userRoleId, GameConstants.VIP_PATA_BUY_COUNT);
		if(pataInfo.getBuyCount() >= maxCount){
			return AppErrorCode.FUBEN_TODAY_NO_BUY_COUNT;
		}
		PataPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_PATA);
		if(publicConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		Object[] reslut = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, publicConfig.getBuyGold(), userRoleId, LogPrintHandle.CONSUME_PATA, true, LogPrintHandle.CBZ_BUY_PATA_COUNT);
		if(reslut != null){
			return reslut;
		}else{
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,publicConfig.getBuyGold(),LogPrintHandle.CONSUME_PATA,QQXiaoFeiType.CONSUME_PATA,1});
			}
		}
		pataInfo.setBuyCount(pataInfo.getBuyCount() + 1);
		pataInfoDao.cacheUpdate(pataInfo, userRoleId);
		
		return new Object[]{1,pataInfo.getBuyCount()};
	}
	
	public void exitPata(Long userRoleId){
		if(!stageControllExportService.inFuben(userRoleId)){
			return;
		}
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.S_EXIT_FUBEN, null);
	}
	
	public void hasExitPata(Long userRoleId){
		PataInfo pataInfo = getpataInfo(userRoleId);
		if(pataInfo.getState() == GameConstants.FUBEN_STATE_READY){
			return;
		}
		pataInfo.setState(GameConstants.FUBEN_STATE_READY);
		pataInfoDao.cacheUpdate(pataInfo, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.EXIT_PATA, null);
	}
	
	public void init(){
		Map<Integer, JSONObject> pataCengInfo = pataCengInfoDao.loadFile();
		PataCengInfoManager.getManager().init(pataCengInfo);
	}
	
	public void stopHandle(){
		pataCengInfoDao.writeFile(PataCengInfoManager.getManager().getPataCengInfo());
	}
	
}
