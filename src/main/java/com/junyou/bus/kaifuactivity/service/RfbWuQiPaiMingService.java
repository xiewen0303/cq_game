package com.junyou.bus.kaifuactivity.service;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuWuQiConfig;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuWuQiConfigExportService;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuWuQiGroupConfig;
import com.junyou.bus.kaifuactivity.dao.KaifuActityDao;
import com.junyou.bus.kaifuactivity.dao.ZhanliBipinDao;
import com.junyou.bus.kaifuactivity.entity.KaifuActity;
import com.junyou.bus.kaifuactivity.entity.ZhanliBipin;
import com.junyou.bus.kaifuactivity.filter.ZhanLiBiPinFilter;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.service.AbstractActivityService;
import com.junyou.bus.rfbactivity.util.ActivityTimeType;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.wuqi.configure.export.XinShengJianJiChuConfig;
import com.junyou.bus.wuqi.configure.export.XinShengJianJiChuConfigExportService;
import com.junyou.bus.wuqi.dao.WuQiInfoDao;
import com.junyou.bus.wuqi.entity.WuQiInfo;
import com.junyou.bus.wuqi.export.WuQiExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.KaiFuPaiMingHDLogEvent;
import com.junyou.event.KaiFuPaiMingLingQuLogEvent;
import com.junyou.event.PaiMingLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.event.util.LogFormatUtils;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.public_.rank.export.RankExportService;
import com.junyou.public_.rank.vo.IWuqiRankVo;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 * 圣剑
 */
@Service
public class RfbWuQiPaiMingService  extends AbstractActivityService {

	@Override
	public boolean getChildFlag(long userRoleId,int subId) {
		
		KaiFuWuQiGroupConfig configs = KaiFuWuQiConfigExportService.getInstance().loadByMap(subId);
		if(configs == null){
			return false;
		}
		
		//处理数据
		Map<Integer, KaiFuWuQiConfig> configMap = configs.getConfigMap();
		
		//如果这两个同时存在,则不可领取奖励
		KaiFuWuQiConfig zhanliConfig =  configMap.get(1);//ID为1规定为阶段邮件奖励
		KaiFuWuQiConfig paihengConfig =  configMap.get(21);//判断是否有排行配置
		if(paihengConfig != null && zhanliConfig != null){
			return false;
		}
		
		//玩家翅膀
		WuQiInfo info = wuQiExportService.getWuQiInfo(userRoleId);
		if(info == null){
			return false;
		}
		
		for (KaiFuWuQiConfig config : configMap.values()) {
			if(config.getId() < 0){
				continue;
			}
			
			XinShengJianJiChuConfig wuqiConfig = xinShengJianJiChuConfigExportService.loadById(info.getZuoqiLevel());
			if(wuqiConfig == null || wuqiConfig.getLevel() < config.getYuJianLevel()){
				continue;
			}
			
			ZhanliBipin zhanlibipin = getZhanliBipin(userRoleId, subId);
			//判断是否已经领取奖励
			if(!isCanRecevieState(zhanlibipin.getLingquStatus(), config.getId())){
				continue;
			}
			
			return true;
		}
		return false;
	}
	
	@Autowired
	private ZhanliBipinDao zhanlibipinDao;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RankExportService rankExportService;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private KaifuActityDao kaifuActityDao;
	@Autowired
	private WuQiExportService wuQiExportService;
	@Autowired
	private XinShengJianJiChuConfigExportService xinShengJianJiChuConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private WuQiInfoDao wuQiInfoDao;
	
	/**武器排行相关 **/
	public void start(List<IWuqiRankVo> list) {
		Map<Integer,KaiFuWuQiGroupConfig> vo = KaiFuWuQiConfigExportService.getInstance().getAllConfig();
		if(vo == null || vo.size() <= 0){
			return;
		}
		for (Integer subId : vo.keySet()) {
			//判断活动是否结束
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
			long time = 10*60*1000;//结算间隔时间
			if(configSong != null && (System.currentTimeMillis() >configSong.getEndTimeByMillSecond() && System.currentTimeMillis() - configSong.getEndTimeByMillSecond() <= time)){
				zhanLiPaiXing(list,subId);
			}
		}
	}
	
	public void zhanLiPaiXing(List<IWuqiRankVo> userList,int subId) {
		//判断是否已经发过邮件
		List<KaifuActity> list = kaifuActityDao.dbLoadAll(subId);
		if(list != null && list.size() > 0 ){
			return;
		}
		
		KaiFuWuQiGroupConfig config = KaiFuWuQiConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return;
		}
		//处理数据
		Map<Integer, KaiFuWuQiConfig> configMap = config.getConfigMap();
		
		if(userList != null && userList.size() > 0){
			//记录排名日志
			try {
				JSONArray consumeItemArray = new JSONArray(); 
				parseJSONArray(userList,consumeItemArray);
				GamePublishEvent.publishEvent(new PaiMingLogEvent(LogPrintHandle.PAIMING_ACTITY_WUQI,consumeItemArray));
			} catch (Exception e) {
				ChuanQiLog.error(""+e);
			}
			
			for (int i = 21; i <= 40; i++) {
				KaiFuWuQiConfig zhanliConfig =  configMap.get(i);
				if(zhanliConfig == null){
					break;
				}
				int start = zhanliConfig.getMin();
				int end = zhanliConfig.getMax();
				if(zhanliConfig.getMax() > userList.size()){
					end = userList.size();
				}
				for (int j = start; j <= end; j++) {
					IWuqiRankVo vo =  userList.get(j-1);
					long userRoleId = vo.getUserRoleId();
					String title = EmailUtil.getCodeEmail(AppErrorCode.KAIFU_PAIMING_EMAIL_TITLE);
					String content = EmailUtil.getCodeEmail(AppErrorCode.KAIFU_WUQI_PAIMING,j+"");
					emailExportService.sendEmailToOne(userRoleId,title, content,GameConstants.EMAIL_TYPE_SINGLE, zhanliConfig.getEmailItem());
					//日志
					GamePublishEvent.publishEvent(new KaiFuPaiMingHDLogEvent(userRoleId, vo.getName(), j, zhanliConfig.getEmailItem(), LogPrintHandle.KAIFU_ACTITY_WUQI));
				}
			}
		}
		//发阶段奖励
		KaiFuWuQiConfig zhanliConfig =  configMap.get(1);//ID为1规定为阶段邮件奖励
		KaiFuWuQiConfig paihengConfig =  configMap.get(21);//判断是否有排行配置
		if(paihengConfig != null && zhanliConfig != null){
			int jie = zhanliConfig.getYuJianLevel() - 1;
			//查询所有大于等于该等级的角色ID
			List<Long> roleIds = wuQiInfoDao.getRoleIdByLevel(jie);
			String title = EmailUtil.getCodeEmail(AppErrorCode.KAIFU_JIEDUAN_TITIE);
			String content = EmailUtil.getCodeEmail(AppErrorCode.KAIFU_WUQI_JIEDUAN,jie+"");
			emailExportService.sendEmailToMany(roleIds,title, content, GameConstants.EMAIL_TYPE_SINGLE, zhanliConfig.getEmailItem());
		}		
		//记录数据
		inertKaiFuActity(subId);
	}
	
	private void inertKaiFuActity(int subId){
		
		KaifuActity actity = new KaifuActity();
		actity.setSubId(subId);
		actity.setCreateTime(new Timestamp(System.currentTimeMillis()));
		
		kaifuActityDao.dbInsert(actity);
	}
	
	/**
	 * 转换成日志对应的格式
	 * @param goodsMap
	 * @param receiveItems
	 */
	public static void parseJSONArray(List<IWuqiRankVo> userList,JSONArray receiveItems) {
		if(userList == null || userList.size() == 0){
			return;
		}
		for (int i = 0; i < userList.size();i++) {
			IWuqiRankVo vo =  userList.get(i);
			Map<String,Object> entity = new HashMap<>();
			entity.put("userRoleId", vo.getUserRoleId());
			entity.put("name",vo.getName());
			entity.put("rank",vo.getRank());
			entity.put("numer",vo.getUncertain());
			receiveItems.add(entity);
		}
	}
	
	
	
	
	public Object[] lingQuJiangLi(long userRoleId,Integer version, Integer configId,int subId) {
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		
		//版本不一样
		if(configSong.getClientVersion() != version){
			//处理数据变化:
			Object newSubHandleData = getRefbInfo(userRoleId, subId);
			Object[] data = new Object[]{subId,configSong.getClientVersion(),newSubHandleData};
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY,data );
			return null;
		}
				
		//判断配置
		KaiFuWuQiConfig config = KaiFuWuQiConfigExportService.getInstance().loadByKeyId(subId,configId);
		if(config== null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//玩家翅膀
		WuQiInfo info = wuQiExportService.getWuQiInfo(userRoleId);
		if(info == null){
			return AppErrorCode.KAIFU_TIAOJIAN_ERROR;
		}
		
		XinShengJianJiChuConfig wuqiConfig = xinShengJianJiChuConfigExportService.loadById(info.getZuoqiLevel());
		if(wuqiConfig == null || wuqiConfig.getLevel() < config.getYuJianLevel()){
			return AppErrorCode.KAIFU_TIAOJIAN_ERROR;
		}
		
		ZhanliBipin zhanlibipin = getZhanliBipin(userRoleId, subId);
		//判断是否已经领取奖励
		if(!isCanRecevieState(zhanlibipin.getLingquStatus(), configId)){
			return AppErrorCode.KAIFU_YI_LINGQU;
		}
		
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBagVo(config.getJiangitem(), userRoleId);
		if(bagCheck != null){
			return bagCheck;
		}
		
		
		//更新玩家领取状态
		zhanlibipin.setLingquStatus(chanageState(zhanlibipin.getLingquStatus(), configId));
		updateZhanLiBiPin(zhanlibipin);
		
		//发放奖励
		roleBagExportService.putGoodsVoAndNumberAttr(config.getJiangitem(), userRoleId, GoodsSource.GOODS_GET_CBPM, LogPrintHandle.GET_RFB_CHIBANGPAIMING, LogPrintHandle.GBZ_RFB_CHIBANGPAIMING, true);
		
		//check icon flag
		super.checkIconFlag(userRoleId, subId);
		
		//日志
		JSONArray receiveItems = new JSONArray();
		LogFormatUtils.parseJSONArrayVo(config.getJiangitem(), receiveItems);
		RoleWrapper loginRole = roleExportService.getLoginRole(userRoleId);
		GamePublishEvent.publishEvent(new KaiFuPaiMingLingQuLogEvent(userRoleId, loginRole.getName(), receiveItems, LogPrintHandle.KAIFU_ACTITY_LQ_WUQI));
		
		
		return new Object[]{1,subId,configId};
	}
	
	
	private ZhanliBipin getZhanliBipin(Long userRoleId,int subId){
		List<ZhanliBipin> list = zhanlibipinDao.cacheLoadAll(userRoleId, new ZhanLiBiPinFilter(subId));
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			ZhanliBipin libao = new ZhanliBipin();
			libao.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
			libao.setLingquStatus(0);
			libao.setSubId(subId);
			libao.setUserRoleId(userRoleId);
			libao.setCreateTime(new Timestamp(System.currentTimeMillis()));
			libao.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			
			zhanlibipinDao.cacheInsert(libao, userRoleId);
			
			return libao;
			
		}
	}
	
	private void updateZhanLiBiPin(ZhanliBipin zhanlibipin){
		zhanlibipin.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		zhanlibipinDao.cacheUpdate(zhanlibipin, zhanlibipin.getUserRoleId());
	}
	
	
	
	/**
	 * 修改状态
	 * @param state
	 * @return
	 */
	public static Integer chanageState(Integer state, Integer day){
		day = day.intValue() - 1;
		
		return (1 << day) | state;
	}
	
	/**
	 * 判断是否已经领取奖励
	 * @param state
	 * @param id
	 * @return true:可领取奖励
	 */
	public static Boolean isCanRecevieState(Integer state, Integer id){
		if(!state.equals(0)){
			id = id.intValue() - 1;
			
			return (state >> id & 1) == 0;
		}
		return true;
	}
	/**
	 * 判断是否已经领取奖励
	 * @param state
	 * @param id
	 * @return 0:未领取奖励，1已领
	 */
	public static int isCanRecevieStateRint(Integer state, Integer id){
		if(!state.equals(0)){
			id = id.intValue() - 1;
			
			if((state >> id & 1) == 0){
				return 0;
			}else{
				return 1;
			}
		}
		return 0;
	}
	
	/**
	 * 获取某个子活动的热发布某个活动信息
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		KaiFuWuQiGroupConfig config = KaiFuWuQiConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		
		//处理数据
		Map<Integer, KaiFuWuQiConfig> configMap = config.getConfigMap();
		List<Object[]> data = new ArrayList<>();
		//循环活动  检测
		updateJianCe(subId, userRoleId);
		
		
		if(configMap != null && configMap.size() > 0){
			ZhanliBipin bipin = getZhanliBipin(userRoleId, subId);
			for (int i = 1; i <= 20; i++) {//阶段奖励（1-20）
				KaiFuWuQiConfig zhanliConfig =  configMap.get(i);
				if(zhanliConfig == null){
					break;
				}
				Object[] myZlData = new Object[]{
					i,
					isCanRecevieStateRint(bipin.getLingquStatus(), i)
				};
				data.add(myZlData);
			}
		}
		
		Object[] one = rankExportService.getKuiShouInfoByType(GameConstants.WQ_TYPE);
		int myMingci = rankExportService.getMyOrderByType(userRoleId, GameConstants.WQ_TYPE);
		
		return new Object[]{
				config.getPic(),
				config.getDes(),
				config.getDes1(),
				config.getJdData().toArray(),
				config.getPxData().toArray(),
				config.getTzData().toArray(),
				new Object[]{one,myMingci,data.toArray()}
		};
	}
	
	private void updateJianCe(int subId,Long userRoleId){
		//判断活动是否结束
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		if(configSong.getTimeType() != ActivityTimeType.TIME_4_KAI_FU_LOOP && configSong.getTimeType() != ActivityTimeType.TIME_5_HE_FU_LOOP){
			return;
		}
		ZhanliBipin zhanlibipin = getZhanliBipin(userRoleId, subId);
		long startTime = configSong.getStartTimeByMillSecond();//活动开始时间
		long upTime = zhanlibipin.getUpdateTime().getTime();
		
		long dTime = GameSystemTime.getSystemMillTime();
		if(startTime  > upTime && startTime < dTime){//如果活动开始时间大于业务上次更新时间，清理业务数据
			zhanlibipin.setLingquStatus(0);
			zhanlibipin.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			
			zhanlibipinDao.cacheUpdate(zhanlibipin, userRoleId);
		}
	}
	
	/**
	 * 获取礼包信息
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getRefbLingQuStatus(Long userRoleId, Integer subId){
		KaiFuWuQiGroupConfig config = KaiFuWuQiConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		
		//处理数据
		Map<Integer, KaiFuWuQiConfig> configMap = config.getConfigMap();
		List<Object[]> data = new ArrayList<>();
		if(configMap != null && configMap.size() > 0){
			ZhanliBipin bipin = getZhanliBipin(userRoleId, subId);
			for (int i = 1; i <= 20; i++) {//阶段奖励（1-20）
				KaiFuWuQiConfig zhanliConfig =  configMap.get(i);
				if(zhanliConfig == null){
					break;
				}
				Object[] zlData = new Object[]{
					i,
					isCanRecevieStateRint(bipin.getLingquStatus(), i)
				};
				data.add(zlData);
			}
		}
		
		Object[] one = rankExportService.getKuiShouInfoByType(GameConstants.WQ_TYPE);
		int myMingci = rankExportService.getMyOrderByType(userRoleId, GameConstants.WQ_TYPE);
		
		return  new Object[]{subId,new Object[]{one,myMingci,data.toArray()}};
	}
	
}