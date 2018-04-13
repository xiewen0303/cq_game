/**
 * 
 */
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
import com.junyou.bus.kaifuactivity.configure.export.KaiFuZhanLiBiPinConfig;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuZhanLiBiPinConfigExportService;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuZhanLiBiPinGroupConfig;
import com.junyou.bus.kaifuactivity.dao.KaifuActityDao;
import com.junyou.bus.kaifuactivity.dao.ZhanliBipinDao;
import com.junyou.bus.kaifuactivity.entity.KaifuActity;
import com.junyou.bus.kaifuactivity.entity.ZhanliBipin;
import com.junyou.bus.kaifuactivity.filter.ZhanLiBiPinFilter;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.util.ActivityTimeType;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoExportService;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoWrapper;
import com.junyou.bus.rolebusiness.dao.RoleBusinessInfoDao;
import com.junyou.bus.tunnel.BusMsgSender;
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
import com.junyou.public_.rank.vo.IFightingRankVo;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;



/**
 * @description
 * @author ZHONGDIAN
 * @created 2011-11-16上午10:29:07
 */
@Service
public class RfbZhanLiBiPinService { 
	
	@Autowired
	private ZhanliBipinDao zhanlibipinDao;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RankExportService rankExportService;
	@Autowired
	private RoleBusinessInfoExportService roleBusinessInfoExportService;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private KaifuActityDao kaifuActityDao;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleBusinessInfoDao roleBusinessInfoDao;
	
	public List<ZhanliBipin> initAll(long userRoleId){
		return zhanlibipinDao.initZhanliBipin(userRoleId);
	}
	
	
	/**战力排行相关 **/
	public void startZhanLi(List<IFightingRankVo> list) {
		Map<Integer,KaiFuZhanLiBiPinGroupConfig> vo = KaiFuZhanLiBiPinConfigExportService.getInstance().getAllConfig();
		if(vo == null || vo.size() <= 0){
			return;
		}
		for (Integer subId : vo.keySet()) {
			//判断活动是否结束
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
			long time = 10*60*1000;//结算间隔时间
			//TODO wind 测试时可以过滤掉时间
			if(configSong != null && (System.currentTimeMillis() >configSong.getEndTimeByMillSecond() && System.currentTimeMillis() - configSong.getEndTimeByMillSecond() <= time)){
				zhanLiPaiXing(list,subId);
			}
		}
		
	}
	
	public void zhanLiPaiXing(List<IFightingRankVo> userList,int subId) {
		//判断是否已经发过邮件
		List<KaifuActity> list = kaifuActityDao.dbLoadAll(subId);
		if(list != null && list.size() > 0 ){
			return;
		}
		
		KaiFuZhanLiBiPinGroupConfig config = KaiFuZhanLiBiPinConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return;
		}
		//处理数据
		Map<Integer, KaiFuZhanLiBiPinConfig> configMap = config.getConfigMap();
		
		if(userList != null && userList.size() > 0){
			//记录排名日志
			try {
				JSONArray consumeItemArray = new JSONArray(); 
				parseJSONArray(userList,consumeItemArray);
				GamePublishEvent.publishEvent(new PaiMingLogEvent(LogPrintHandle.PAIMING_ACTITY_ZHANLI,consumeItemArray));
			} catch (Exception e) {
				ChuanQiLog.error(""+e);
			}
			for (int i = 21; i <= 40; i++) {
				KaiFuZhanLiBiPinConfig zhanliConfig =  configMap.get(i);
				if(zhanliConfig == null){
					break;
				}
				int start = zhanliConfig.getMin();
				int end = zhanliConfig.getMax();
				if(zhanliConfig.getMax() > userList.size()){
					end = userList.size();
				}
				for (int j = start; j <= end; j++) {
					IFightingRankVo vo =  userList.get(j-1);
					long userRoleId = vo.getUserRoleId();
					String title = EmailUtil.getCodeEmail(AppErrorCode.KAIFU_PAIMING_EMAIL_TITLE);
					String content = EmailUtil.getCodeEmail(AppErrorCode.KAIFU_ZHANLI_PAIMING,j+"");
					emailExportService.sendEmailToOne(userRoleId, title,content,GameConstants.EMAIL_TYPE_SINGLE, zhanliConfig.getEmailItem());
				
					//日志
					GamePublishEvent.publishEvent(new KaiFuPaiMingHDLogEvent(userRoleId, vo.getName(), j, zhanliConfig.getEmailItem(), LogPrintHandle.KAIFU_ACTITY_ZHANLI));
				}
			
			}
		}
		
		//发阶段奖励
		KaiFuZhanLiBiPinConfig zhanliConfig =  configMap.get(1);//ID为1规定为阶段邮件奖励
		KaiFuZhanLiBiPinConfig paihengConfig =  configMap.get(21);//判断是否有排行配置
		if(paihengConfig != null && zhanliConfig != null){
			int jie = zhanliConfig.getZplus();
			//查询所有大于等于该等级的角色ID
			List<Long> roleIds = roleBusinessInfoDao.getRoleIdByLevel(jie);
			String title = EmailUtil.getCodeEmail(AppErrorCode.KAIFU_JIEDUAN_TITIE);
			String content = EmailUtil.getCodeEmail(AppErrorCode.KAIFU_ZHANLI_JIEDUAN,jie+"");
			emailExportService.sendEmailToMany(roleIds,title, content, GameConstants.EMAIL_TYPE_SINGLE, zhanliConfig.getEmailItem());
		}
		//记录数据
		inertKaiFuActity(subId);
	}
	
	/**
	 * 转换成日志对应的格式
	 * @param goodsMap
	 * @param receiveItems
	 */
	public static void parseJSONArray(List<IFightingRankVo> userList,JSONArray receiveItems) {
		if(userList == null || userList.size() == 0){
			return;
		}
		for (int i = 0; i < userList.size();i++) {
			IFightingRankVo vo =  userList.get(i);
			Map<String,Object> entity = new HashMap<>();
			entity.put("userRoleId", vo.getUserRoleId());
			entity.put("name",vo.getName());
			entity.put("rank",vo.getRank());
			entity.put("numer",vo.getUncertain());
			receiveItems.add(entity);
		}
	}
	
	private void inertKaiFuActity(int subId){
		
		KaifuActity actity = new KaifuActity();
		actity.setSubId(subId);
		actity.setCreateTime(new Timestamp(System.currentTimeMillis()));
		
		kaifuActityDao.dbInsert(actity);
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
		KaiFuZhanLiBiPinConfig config = KaiFuZhanLiBiPinConfigExportService.getInstance().loadByKeyId(subId,configId);
		if(config== null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//玩家战力
		RoleBusinessInfoWrapper wrapper = roleBusinessInfoExportService.getRoleBusinessInfoWrapper(userRoleId);
		if(wrapper.getCurFighter() < config.getZplus()){
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
		roleBagExportService.putGoodsVoAndNumberAttr(config.getJiangitem(), userRoleId, GoodsSource.GOODS_GET_ZLBP, LogPrintHandle.GET_RFB_ZHANLIBIPIN, LogPrintHandle.GBZ_RFB_ZHANLIBIPIN, true);

		//日志
		JSONArray receiveItems = new JSONArray();
		LogFormatUtils.parseJSONArrayVo(config.getJiangitem(), receiveItems);
		RoleWrapper loginRole = roleExportService.getLoginRole(userRoleId);
		GamePublishEvent.publishEvent(new KaiFuPaiMingLingQuLogEvent(userRoleId, loginRole.getName(), receiveItems, LogPrintHandle.KAIFU_ACTITY_LQ_ZHANLI));
		
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
		KaiFuZhanLiBiPinGroupConfig config = KaiFuZhanLiBiPinConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		
		
		//处理数据
		Map<Integer, KaiFuZhanLiBiPinConfig> configMap = config.getConfigMap();
	/*	List<Object[]> jdData = new ArrayList<>();
		List<Object[]> pxData = new ArrayList<>();
		List<Object[]> tzData = new ArrayList<>();*/
		List<Object[]> data = new ArrayList<>();
		//循环活动  检测
		updateJianCe(subId, userRoleId);
		
		if(configMap != null && configMap.size() > 0){
			ZhanliBipin bipin = getZhanliBipin(userRoleId, subId);
			for (int i = 1; i <= 20; i++) {//阶段奖励（1-20）
				KaiFuZhanLiBiPinConfig zhanliConfig =  configMap.get(i);
				if(zhanliConfig == null){
					break;
				}
				/*Object[] zlData = new Object[]{
					i,
					zhanliConfig.getDes(),
					zhanliConfig.getItemClientMap(),
					zhanliConfig.getZplus()
				};
				jdData.add(zlData)*/;
				
				Object[] myZlData = new Object[]{
					i,
					isCanRecevieStateRint(bipin.getLingquStatus(), i)
				};
				data.add(myZlData);
				
			}
			/*for (int i = 21; i <= 40; i++) {
				KaiFuZhanLiBiPinConfig zhanliConfig =  configMap.get(i);
				if(zhanliConfig == null){
					break;
				}
				Object[] zlData = new Object[]{
					i,
					zhanliConfig.getDes(),
					zhanliConfig.getItemClientMap()
				};
				pxData.add(zlData);
			}
			for (int i = 41; i <= 60; i++) {
				KaiFuZhanLiBiPinConfig zhanliConfig =  configMap.get(i);
				if(zhanliConfig == null){
					break;
				}
				Object[] zlData = new Object[]{
					zhanliConfig.getShowitem(),
					zhanliConfig.getDes()
				};
				tzData.add(zlData);
			}*/
		}
		
		Object[] one = rankExportService.getKuiShouInfoByType(GameConstants.ZL_TYPE);
		int myMingci = rankExportService.getMyOrderByType(userRoleId, GameConstants.ZL_TYPE);
		
		return new Object[]{
				config.getPic(),
				config.getDes(),
				config.getDes1(),
				config.getJdData().toArray(),
				config.getPxData().toArray(),
				config.getTzData().toArray(),
				/*jdData == null ? null : jdData.toArray(),
				pxData == null ? null : pxData.toArray(),
				tzData == null ? null : tzData.toArray(),*/
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
		KaiFuZhanLiBiPinGroupConfig config = KaiFuZhanLiBiPinConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		
		
		//处理数据
		Map<Integer, KaiFuZhanLiBiPinConfig> configMap = config.getConfigMap();
		List<Object[]> data = new ArrayList<>();
		if(configMap != null && configMap.size() > 0){
			ZhanliBipin bipin = getZhanliBipin(userRoleId, subId);
			for (int i = 1; i <= 20; i++) {//阶段奖励（1-20）
				KaiFuZhanLiBiPinConfig zhanliConfig =  configMap.get(i);
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
		
		Object[] one = rankExportService.getKuiShouInfoByType(GameConstants.ZL_TYPE);
		int myMingci = rankExportService.getMyOrderByType(userRoleId, GameConstants.ZL_TYPE);
		
		return  new Object[]{subId,new Object[]{one,myMingci,data.toArray()}};
	}
	
}