/**
 * 
 */
package com.junyou.bus.tuangou.service;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tuangou.configure.RfbTuangouConfigExportService;
import com.junyou.bus.tuangou.configure.TuanGouGroupConfig;
import com.junyou.bus.tuangou.dao.RefbRoleTuangouDao;
import com.junyou.bus.tuangou.entity.RefbRoleTuangou;
import com.junyou.bus.tuangou.filter.TuanGouFilter;
import com.junyou.bus.tuangou.utils.TuanGouUtils;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.TuanGouDuiHuanLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.io.export.SessionManagerExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;



/**
 * @description
 * @author ZHONGDIAN
 * @created 2011-11-16上午10:29:07
 */
@Service
public class TuanGouService { 
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RefbRoleTuangouDao refbRoleTuangouDao;
	@Autowired
	private EmailExportService emailExportService;
    @Autowired
    private SessionManagerExportService sessionManagerExportService;
    @Autowired
    private RoleExportService roleExportService;

	 
	public void quartTuanGou() {
	/*	List<Integer> list = refbRoleTuangouDao.selectAllDianShuBySubId(1111);
		TuanGouUtils.initNumberList(1111, list,100, 0);*/
		Map<Integer, TuanGouGroupConfig> groups = RfbTuangouConfigExportService.getInstance().loadAll();
		if(groups.size() == 0){
			return;
		}
		
		//循环充值礼包配置数据
		for(Map.Entry<Integer, TuanGouGroupConfig> entry : groups.entrySet()){
			//是否在有这个活动或者是否在时间内
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(entry.getKey());
			if(configSong == null || !configSong.isRunActivity()){
				continue;
			}
			TuanGouGroupConfig config = RfbTuangouConfigExportService.getInstance().loadById(entry.getKey());
			if(config == null){
				continue;
			}
			
			List<Integer> list = refbRoleTuangouDao.selectAllDianShuBySubId(entry.getKey());
			TuanGouUtils.initNumberList(entry.getKey(), list,config.getCount(), 0);
		}
	}
	
	public List<RefbRoleTuangou> initRefbRoleTuangou(Long userRoleId){
		return refbRoleTuangouDao.initRefbRoleTuangou(userRoleId);
	}
	
	private RefbRoleTuangou createRefbRoleTuangou(Long userRoleId,int subId,int dianshu){
		RefbRoleTuangou tuangou = new RefbRoleTuangou();
		
		tuangou.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		tuangou.setUserRoleId(userRoleId);
		tuangou.setDianShu(dianshu);
		tuangou.setSubId(subId);
		tuangou.setCreateTime(new Timestamp(System.currentTimeMillis()));
		
		return tuangou;
	}
	
	/**
	 *  活动规则
	   		时间点1--时间点2，不限制玩家购买数量				
			时间点2--时间点3，玩家不可购买商品				
			时间点3，公布幸运大奖名单，并将奖励通过邮箱发送				
	*/
	
	/**
	 * 购买
	 * @param userRoleId
	 * @param subId
	 * @return
	 * @throws ParseException
	 */
	public Object[] buy(Long userRoleId,int subId) throws ParseException{
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		TuanGouGroupConfig config = RfbTuangouConfigExportService.getInstance().loadById(subId);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());//当前日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");  
		long time1 = sdf.parse(date + " " + config.getTime1()).getTime();
		long time2 = sdf.parse(date + " " + config.getTime2()).getTime();
		//只有在活动配置时间点1到时间点2之间才可购买
		long time = GameSystemTime.getSystemMillTime();
		if(time < time1 || time > time2){
			return AppErrorCode.TUANGOU_TIME_ERROR;
		}
		//获取全服剩余可购买次数
		int sCount = TuanGouUtils.getLeftCount(subId);
		if(sCount <= 0){
			return new Object[]{2,subId};//和前端协议，在没有购买次数的时候返回2，和子活动ID
		}
		//判断玩家直接的购买次数
		int uCount = getUserBuyConut(userRoleId, subId);
		int cCount = config.getCishu();//配置的玩家最大可购买次数
		if(uCount >= cCount){
			return AppErrorCode.TUANGOU_COUNT_ERROR;
		}
		//判断元宝
		int yb = config.getPrice2();
		if(yb <= 0){
			return AppErrorCode.CONFIG_ERROR;
		}
		//判断元宝是否足够
		Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD,yb, userRoleId);
		if(null != goldError){ 
			return goldError;
		}
		//判断背包
		Map<String, Integer> itemMap = config.getJiangitem1();
		if(ObjectUtil.isEmpty(itemMap)){
		    Object[] bagCheck = roleBagExportService.checkPutInBag(itemMap, userRoleId);
		    if(bagCheck != null){
		        return bagCheck;
		    }
		}
		//消耗金钱
		//消耗元宝
		roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, yb, userRoleId, LogPrintHandle.CONSUME_RFBTG, true,LogPrintHandle.CBZ_RFBTG);
		if(PlatformConstants.isQQ()){
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,yb,LogPrintHandle.CONSUME_RFBTG,QQXiaoFeiType.CONSUME_RFB_TUANGOU,1});
		}
		//发放物品
		roleBagExportService.putInBag(itemMap, userRoleId, GoodsSource.RFB_TG, true);
		
		//给玩家roll一个点数,保存玩家数据
		int dianshu = TuanGouUtils.getRollNumber(subId);
		RefbRoleTuangou tg = createRefbRoleTuangou(userRoleId, subId, dianshu);
		
		refbRoleTuangouDao.cacheInsert(tg, userRoleId);
		
		// 日志打印
        try {
            RoleWrapper role = roleExportService.getLoginRole(userRoleId);
            GamePublishEvent.publishEvent(new TuanGouDuiHuanLogEvent(userRoleId, role.getName(), yb, LogPrintHandle.getLogGoodsParam(ObjectUtil.isEmpty(itemMap) ? new HashMap<String, Integer>() : itemMap, null)));
        } catch (Exception e) {
            ChuanQiLog.error("打印热发布团购活动获得日志错误:{}", e);
        }
        
 		return new Object[]{1,subId,dianshu};
	}
	
	public Object[] getScount(Long userRoleId,int subId){
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		TuanGouGroupConfig config = RfbTuangouConfigExportService.getInstance().loadById(subId);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//获取全服剩余可购买次数
		int sCount = TuanGouUtils.getLeftCount(subId);
		return new Object[]{subId,config.getCount()-sCount};
	}
	
	/**
	 * 获取玩家自己的购买次数
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	private int getUserBuyConut(Long userRoleId,Integer subId){
		List<RefbRoleTuangou> list = refbRoleTuangouDao.cacheLoadAll(userRoleId, new TuanGouFilter(subId));
		if(list == null || list.size() <= 0){
			return 0;
		}
		return list.size();
	}
	
	/**
	 * 获取玩家已roll的数字
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	private Object[] getRoleJidBySubId(Long userRoleId,Integer subId){
		List<RefbRoleTuangou> list = refbRoleTuangouDao.cacheLoadAll(userRoleId, new TuanGouFilter(subId));
		if(list == null || list.size() <= 0){
			return null;
		}
		List<Integer> rList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			RefbRoleTuangou t = list.get(i);
			rList.add(t.getDianShu());
		}
		return rList.toArray();
		
	}
	
	/**
	 * 获取某个子活动的热发布某个活动信息
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		TuanGouGroupConfig config = RfbTuangouConfigExportService.getInstance().loadById(subId);
		if(config == null){
			return null;
		}
		if(TuanGouUtils.getIsQuert(subId)==null){
			quartTuanGou();
		}
		int sCount = TuanGouUtils.getLeftCount(subId);//获取剩余次数
		return new Object[]{
				config.getDes(),
				config.getTips(),
				config.getPic(),
				config.getTime1(),
				config.getTime2(),
				config.getTime3(),
				config.getCount(),
				config.getCount() - sCount,//全服已购次数
				TuanGouUtils.getJmapJid(subId),
				config.getCishu(),
				getRoleJidBySubId(userRoleId, subId),
				new Object[]{config.getJianLiClientMap(),config.getPrice()},
				new Object[]{config.getJianLiClientMap1(),config.getPrice2()}
		};
	}
	
	public void initTuanGou(int subId) {
		//是否在有这个活动或者是否在时间内
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		if(configSong == null || !configSong.isRunActivity()){
			return;
		}
		TuanGouGroupConfig config = RfbTuangouConfigExportService.getInstance().loadById(subId);
		if(config == null){
			return;
		}
		
		List<Integer> list = refbRoleTuangouDao.selectAllDianShuBySubId(subId);
		TuanGouUtils.initNumberList(subId, list,config.getCount(), 0);
	}
	
	public void jieSuanEmail(){
		Map<Integer, TuanGouGroupConfig> groups = RfbTuangouConfigExportService.getInstance().loadAll();
		if(groups.size() == 0){
			return;
		}
		for(Map.Entry<Integer, TuanGouGroupConfig> entry : groups.entrySet()){
			//是否在活动中
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(entry.getKey());
			if(configSong == null){
				continue;
			}
			TuanGouGroupConfig config = entry.getValue();
			if(config == null){
				ChuanQiLog.error("配置怎么找不到了？子活动ID："+entry.getKey());
				continue;
			}
			//1-100之间roll个数字
			Integer dianshu = (int) (Math.random() * config.getCount())+1;
			//保存中奖点数并通知客户端
			TuanGouUtils.setJmapByJid(entry.getKey(), dianshu);
			BusMsgSender.send2All(ClientCmdType.TUANGOU_TUISONG_CILENT, new Object[]{entry.getKey(),dianshu});
			//根据点数查中奖玩家
			List<RefbRoleTuangou> list = refbRoleTuangouDao.getTuanGouBySubIdAndNum(entry.getKey(), dianshu);
			if(list == null || list.size() <= 0){
				continue;
			}
			for (int i = 0; i < list.size(); i++) {
				RefbRoleTuangou tg = list.get(i);
				long userRoleId = tg.getUserRoleId();
				String title = EmailUtil.getCodeEmail(AppErrorCode.TUANGOU_EMAIL_CONTENT_TITLE);
				String content = EmailUtil.getCodeEmail(AppErrorCode.TUANGOU_EMAIL_CONTENT);
				emailExportService.sendEmailToOne(userRoleId,title,content,GameConstants.EMAIL_TYPE_SINGLE, config.getJiangitem());
			}
		}
	}
	
	/**
	 * 清空数据
	 */
	public void qingkongData(){
		List<RefbRoleTuangou> list = refbRoleTuangouDao.getRefbRoleTuangou();
		if(list != null && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				try {
					RefbRoleTuangou tg = list.get(i);
					if(sessionManagerExportService.isOnline(tg.getUserRoleId())){
						refbRoleTuangouDao.cacheDelete(tg.getId(), tg.getUserRoleId());
					}
				} catch (Exception e) {
					ChuanQiLog.error("删除团购缓存数据失败");
				}
			}
		}
		refbRoleTuangouDao.dbDeleteBySubId();//清空数据库所有数据
		TuanGouUtils.clearnData();//清空内存所有数据
	}
	
}