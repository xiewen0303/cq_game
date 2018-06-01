package com.junyou.bus.job;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.active.export.DingShiActiveExportService;
import com.junyou.bus.chenghao.service.export.ChenghaoExportService;
import com.junyou.bus.danfuchargerank.service.export.DanfuChargeRankExportService;
import com.junyou.bus.equip.export.EquipExportService;
import com.junyou.bus.hongbao.export.HongbaoExportService;
import com.junyou.bus.kuafu_dianfeng.export.KuafuDianFengExportService;
import com.junyou.bus.kuafu_qunxianyan.export.KuafuQunXianYanExportService;
import com.junyou.bus.kuafu_yungong.export.KuafuYunGongExportService;
import com.junyou.bus.kuafuchargerank.service.export.KuafuChargeRankExportService;
import com.junyou.bus.kuafuluandou.export.KuaFuDaLuanDouExportService;
import com.junyou.bus.kuafuxiaofei.service.export.KuafuXiaoFeiExportService;
import com.junyou.bus.lianyuboss.export.LianyuBossExortService;
import com.junyou.bus.online.export.RoleOnlineExportService;
import com.junyou.bus.platform.qq.service.export.TencentLuoPanExportService;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.rfbactivity.configure.export.ReFaBuGxConfigExportService;
import com.junyou.bus.rfbflower.export.FlowerCharmRankExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.tuangou.export.TuanGouExportService;
import com.junyou.bus.xiaofei.export.RefabuXiaoFeiExportService;
import com.junyou.event.OnlineCountLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.io.global.GameSessionManager;
import com.junyou.kuafu.manager.KuafuSessionManager;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.public_.guild.export.GuildExportService;
import com.junyou.public_.nodecontrol.service.NodeControlService;
import com.junyou.public_.rank.export.RankExportService;
import com.junyou.public_.tunnel.PublicMsgQueue;
import com.junyou.utils.KuafuConfigPropUtil;
import com.junyou.utils.lottery.Lottery;
import com.kernel.pool.executor.ThreadNameFactory;

/**
 * 定时器管理 [时间频率的配置在spring-task.xml里统一管理 ]
 * @author DaoZheng Yuan
 * 2014年11月24日 下午3:42:50
 */
@Component
public class JobManager {

	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private HongbaoExportService hongbaoExportService;
	@Autowired
	private FlowerCharmRankExportService flowerCharmRankExportService;
	@Autowired
	private RankExportService rankExportService;
	@Autowired
	private DingShiActiveExportService dingShiActiveExportService;
	@Autowired
	private NodeControlService nodeControlService;
	@Autowired
	private RoleOnlineExportService roleOnlineExportService;
	@Autowired
	private GuildExportService guildExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RefabuXiaoFeiExportService refabuXiaoFeiExportService;
	@Autowired
	private ChenghaoExportService chenghaoExportService;
	@Autowired
	private KuafuChargeRankExportService kuafuChargeRankExportService;
	@Autowired
	private KuafuXiaoFeiExportService kuafuXiaoFeiExportService;
	@Autowired
	private DanfuChargeRankExportService danfuChargeRankExportService;
	@Autowired
	private TencentLuoPanExportService tencentLuoPanExportService;
	@Autowired
	private LianyuBossExortService lianyuBossExortService;
	@Autowired
	private TuanGouExportService tuanGouExportService;
	@Autowired
	private KuaFuDaLuanDouExportService kuaFuDaLuanDouExportService;
	@Autowired
	private KuafuQunXianYanExportService kuafuQunXianYanExportService;
	@Autowired
    private KuafuDianFengExportService kuafuDianFengExportService;
	@Autowired
	private EquipExportService equipExportService;
	@Autowired
	private KuafuYunGongExportService kuafuYunGongExportService;
	
	private ExecutorService executorService = Executors.newFixedThreadPool(2, new ThreadNameFactory("spring-job-"));
	
	public void fetchDingZhiChenghao(){
		//跨服务器不执行
		if(!KuafuConfigPropUtil.isKuafuServer()){
			chenghaoExportService.fetchDingZhiChenghao();
		}
	}
	
	/**
	 * 定时清理过期邮件，每天23点59分59秒执行
	 */
	public void emailJob(){
		//跨服务器不执行
		if(!KuafuConfigPropUtil.isKuafuServer()){
			PublicMsgQueue publicMsgQueue = new PublicMsgQueue();
			emailExportService.emailQuartzDelete(publicMsgQueue);
			publicMsgQueue.flush();
		}
	}
	/**
	 * 凌晨两点
	 */
	public void clearHongbaoJob(){
		if(!KuafuConfigPropUtil.isKuafuServer()){
			hongbaoExportService.clearExpireHongbao();
		}
	}
	/**
	 * 凌晨过2分钟结算鲜花魅力榜
	 */
	public void flowerCharmRankJob(){
		if(!KuafuConfigPropUtil.isKuafuServer()){
			flowerCharmRankExportService.flowerCharmRankJieSuan();
		}
	}
	 
	
	/**刷新排行榜*/
	public void refreshRankJob(){
		//跨服务器不执行
		if(!KuafuConfigPropUtil.isKuafuServer()){
			rankExportService.refreshRank();
		}
	}
	
	/**消费排名发送邮件*/
	public void xiaofeiEmailJob(){
		//跨服务器不执行
		if(!KuafuConfigPropUtil.isKuafuServer()){
			refabuXiaoFeiExportService.xfJiangLiEmail();
		}
	}
	/**大乱斗海选赛发送邮件*/
	public void luanDouHaiXuan(){
		//跨服务器不执行
		if(!KuafuConfigPropUtil.isKuafuServer()){
			kuaFuDaLuanDouExportService.luanDouHaiXuanSai();
		}
	}
	/**群仙宴跨天清空数据*/
	public void qunxianyan(){
		//跨服务器不执行
		if(!KuafuConfigPropUtil.isKuafuServer()){
			kuafuQunXianYanExportService.clearQunXianYan();
		}
	}
	/**大乱斗结束发送邮件*/
	public void luanDouJieSuan(){
		//跨服务器不执行
		if(!KuafuConfigPropUtil.isKuafuServer()){
			kuaFuDaLuanDouExportService.luanDouJieSuanEmail();
		}
	}
	
	
	
	
	/**单服充值排名*/
	public void danFuChargeRankJob(){
		//跨服务器不执行
		if(!KuafuConfigPropUtil.isKuafuServer()){
			try{
				danfuChargeRankExportService.danfuChargeRankDayJob();
			}catch (Exception e) {
				ChuanQiLog.error("danfu charge rank",e);
			}
		}
		
	}
	
	/**跨服充值排名*/
	public void chargeRankJob(){
		//跨服务器不执行
		if(!KuafuConfigPropUtil.isKuafuServer()){
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					try{
						sleepCurrentThread(5000, "kfchargeRankJob");
						kuafuChargeRankExportService.kuafuChargeRankDayJob();
					}catch (Exception e) {
						ChuanQiLog.error("kuafu charge rank",e);
					}
				}
			});
		}
		
	}
	
	/**跨服消费排名*/
	public void xiaoFeiRankJob(){
		//跨服务器不执行
		if(!KuafuConfigPropUtil.isKuafuServer()){
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					try{
						sleepCurrentThread(5000, "kfchargeRankJob");
						kuafuXiaoFeiExportService.kuafuXiaoFeiRankDayJob();
					}catch (Exception e) {
						ChuanQiLog.error("kuafu xiaofei rank",e);
					}
				}
			});
		}
	}
	
	/**
	 * 休眠当前线程指定时间
	 * @param mills
	 * @param logMethodName
	 */
	private void sleepCurrentThread(int mills, String logMethodName) {
		int sleepMillSecond = Lottery.roll(mills) + 1;
		try {
			Thread.sleep(sleepMillSecond);
		} catch (InterruptedException e) {
			ChuanQiLog.info("{} method sleepMillSecond={}", logMethodName, sleepMillSecond, e);
		}
	}
	
	/**
	 * 启动定时活动
	 */
	public void dingshiActiveJob(){
		dingShiActiveExportService.initDayActive();
	}
	
	/**
	 * 在线人数日志
	 */
	public void onlineLog(){
		int sessionCounts = 0;
		if(!KuafuConfigPropUtil.isKuafuServer()){
			sessionCounts = GameSessionManager.getInstance().getOnlineCount();
		}else{
			sessionCounts = KuafuSessionManager.getAllUserRoleId().size();
		}
		int ipCounts = GameSessionManager.getInstance().getSessionIps();
		int wdCounts = nodeControlService.getWeiDuanCount();
		GamePublishEvent.publishEvent(new OnlineCountLogEvent(sessionCounts, ipCounts, wdCounts));
		//跨服务器不执行，并且是腾讯平台
		if(!KuafuConfigPropUtil.isKuafuServer() && PlatformConstants.isQQ()){
			tencentLuoPanExportService.tencentLuoPanOssRenShu(sessionCounts);
		}
	}
	/**
	 * 腾讯大区日志上报(每日23点上报)
	 */
	public void qzoneDaquShangBao(){
		//跨服务器不执行，并且是腾讯平台
		if(!KuafuConfigPropUtil.isKuafuServer() && PlatformConstants.isQQ()){
			tencentLuoPanExportService.tencentLuoPanOssDaQu();
		}
	}
	
	/**
	 * 玩家在线时长
	 */
	public void roleOnlineLog(){
		//跨服务器不执行
		if(!KuafuConfigPropUtil.isKuafuServer()){
			roleOnlineExportService.quartzLog();
		}
	}
	
	/**
	 * 每日热发布关 系表重新解析
	 */
	public void reFbGxEveryDayAnalysis(){
		//跨服务器不执行
		if(!KuafuConfigPropUtil.isKuafuServer()){
			ReFaBuGxConfigExportService.getInstance().changeAllConfigureDataResolve();
		}
	}
	
	/**
	 * 整点刷新公会
	 */
	public void refreshGuild(){
		//跨服务器不执行
		if(!KuafuConfigPropUtil.isKuafuServer()){
			guildExportService.refreshOnlineMembers();
		}
	}
	

	/**
	 * 凌晨过一秒清理门派boss公共缓存数据
	 */
	public void clearGuildLianyuBoss(){
	    //跨服务器不执行
	    if(!KuafuConfigPropUtil.isKuafuServer()){
	        lianyuBossExortService.clearMap();//清理门派公共数据
	    }
	}
	/**
	 * pps每日用户数据推送
	 */
	public void ppsEveryDayRoleDatas(){
		//跨服务器不执行
		if(!KuafuConfigPropUtil.isKuafuServer()){
			//不是pps平台不执行
			if(PlatformConstants.isPPs()){
				
				Thread ppsExcuteThread = new Thread(new Runnable() {
					@Override
					public void run() {
						int sleepMillSecond = Lottery.roll(50000)+1;
						
						try {
							Thread.sleep(sleepMillSecond);
						} catch (InterruptedException e) {
							ChuanQiLog.error("ppsEveryDayRoleDatas method sleepMillSecond={}",sleepMillSecond,e);
						}
						roleExportService.getPPsDayRoleDatas();
					}
				},"ppsEveryDayRoleDataThread");
				
				ppsExcuteThread.start();
			}
		}
	}
	
	

	/**
	 * 定时清空团购活动数据
	 */
	public void qingkongTuanGouData(){
		//跨服务器不执行
		if(!KuafuConfigPropUtil.isKuafuServer()){
			tuanGouExportService.qingkongData();
		}
	}
	/**
	 * 团购活动发送邮件
	 */
	public void tuanGouEmail(){
		//跨服务器不执行
		if(!KuafuConfigPropUtil.isKuafuServer()){
			tuanGouExportService.jieSuanEmail();
		}
	}
	
	/**
	 * 定时推送创角信息
	 */
	public void sendCreateInfoJob(){
		//跨服务器不执行
		if(!KuafuConfigPropUtil.isKuafuServer()){
			roleExportService.sendCreateInfoJob();
		}
	}
	
	//巅峰之战是否开启(true=开启;false=不开启)
	private boolean dianfengShow(){
	    // 跨服不执行
	    if(KuafuConfigPropUtil.isKuafuServer()){
	        return false;
	    }
	    // 越南不执行
	    if(PlatformConstants.isYueNan()){
	        return false;
	    }
	    return true;
	}
	
    /**
     * 清除跨服大乱斗和巅峰之战排名数据
     */
    public void kuafuRankClear() {
        if (dianfengShow()) {
            kuaFuDaLuanDouExportService.luanDouClearRankData();
            kuafuDianFengExportService.clearDianFengResultData();
        }
    }

    /**
     * 巅峰之战战斗
     */
    public void dianfengFight() {
        if (dianfengShow()) {
            kuafuDianFengExportService.startDianFengLoop();
        }
    }

    /**
     * 巅峰之战发奖
     */
    public void dianfengReward() {
        if (dianfengShow()) {
            kuafuDianFengExportService.rewardDianFeng();
        }
    }
	
    /**
     * 每日23:59:35统计暗金和神武装备
     */
    public void calcEquipJob(){
        // 本服执行,跨服不执行
        if(!KuafuConfigPropUtil.isKuafuServer()){
            equipExportService.startCalcEquip();
        }
    }
    
    /**
     * 跨服云宫之巅活动定时器
     */
    public void kuafuYunGongActiveJob(){
        kuafuYunGongExportService.init();
    }

   

}
