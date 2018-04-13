package com.junyou.bus.platform.common.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.platform.common.dao.RolePlatformInfoDao;
import com.junyou.bus.platform.common.dao.RolePlatformRechargeDao;
import com.junyou.bus.platform.common.entity.RolePlatformInfo;
import com.junyou.bus.platform.common.entity.RolePlatformRecharge;
import com.junyou.bus.platform.configure.export.PlatformGongGongShuJuBiaoConfigExportService;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.configure.export.PtCommonPublicConfig;
import com.junyou.bus.platform.configure.export.PtCommonSuperVipConfigExportService;
import com.junyou.bus.platform.configure.export.SuperVipConfig;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.PlatformGiftRewardLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.math.BitOperationUtil;
import com.kernel.spring.container.DataContainer;

/**
 * 平台礼包领取 超级会员等相同业务类
 * @author lxn
 * 
 */
@Service
public class PtCommonService {

	@Autowired
	private PlatformGongGongShuJuBiaoConfigExportService platformGongGongShuJuBiaoConfigExportService;  	
	@Autowired
	private PtCommonSuperVipConfigExportService ptCommonSuperVipConfigExportService;
	@Autowired
	private RolePlatformRechargeDao rolePlatformRechargeDao;
	@Autowired
	private RoleBagExportService roleBagExportService; 
	@Autowired
	private RolePlatformInfoDao rolePlatformInfoDao;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private RoleExportService roleExportService;

	
	

	// *************************以下超级会员*******************************
	/**
	 * 判断超级会员活动是否关闭
	 */
	public int isCloseActivity() {

		SuperVipConfig config = ptCommonSuperVipConfigExportService.getVipBaseConfig();

		return config == null ? 0 : 1;
	}
	
	/**
	 * 获取VipBaseConfig
	 */
	public SuperVipConfig  getSuperVipConfig( ){
		
		return  ptCommonSuperVipConfigExportService.getVipBaseConfig();
	}
	/**
	 * 获取玩家充值信息
	 */
	public RolePlatformRecharge  getPlatformRecharge(Long userRoleId){
		
		return rolePlatformRechargeDao.cacheAsynLoad(userRoleId, userRoleId);
	}
	/**
	 * 打开超级会员面板请求的信息
	 * 
	 * @param userRoleId
	 * @return
	 */
	public Object[] getSuperVipInfo(Long userRoleId) {
		SuperVipConfig config = ptCommonSuperVipConfigExportService.getVipBaseConfig();
		if (config == null) {
			// 数据异常
			return AppErrorCode.PLATFORM_DATA_ERROR;
		}
		Object[] result = new Object[] { 1, 0, config.getPic(), config.getAllRecharge(), config.getOnceRecharge(), config.getQq() };
		RolePlatformRecharge rRecharge =getPlatformRecharge( userRoleId);
		if (rRecharge != null && this.isShowQq(rRecharge, config)) {
			result[1] = 1;
		}
		return result;
	}

	/**
	 * 会员充值后调用逻辑 充值金额满足条件推送信息给客户端 满足条件客户端就在超级会员面板显示qq
	 * @param userRoleId
	 * @return
	 */
	public void sendRechargeToClient(Long userRoleId, Long addYb) {
		// 判断活动是否存在
		if (this.ptCommonSuperVipConfigExportService.getVipBaseConfig() == null) {
			return;
		}
		short cmdType = getCmd(2); // 指令类别
		if(cmdType==0){return;}
		Long money = addYb / 10;// RMB
		boolean toClient = true; // 是否给客户端推送
		SuperVipConfig config = ptCommonSuperVipConfigExportService.getVipBaseConfig();
		RolePlatformRecharge rRecharge = getPlatformRecharge( userRoleId);
		if (rRecharge == null) {
			rRecharge = new RolePlatformRecharge();
			rRecharge.setUserRoleId(userRoleId);
			rRecharge.setRechargeMonth(money);
			rRecharge.setRechargeOnceMax(money);
			rRecharge.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));// 每次必更新
			rolePlatformRechargeDao.cacheInsert(rRecharge, rRecharge.getUserRoleId());
			if (money >= config.getOnceRecharge()) {
				BusMsgSender.send2One(userRoleId, cmdType, 1);
			}
			return;
		}
		// 存在充值记录
		if (DatetimeUtil.isSameMonth(rRecharge.getUpdateTime().getTime()) && this.isShowQq(rRecharge, config)) {
			// 如果之前已经推送过就不要重复推送 已存在的旧记录已经满足条件
			toClient = false;
		}
		// 更新累计充值和单笔数值
		if (DatetimeUtil.isSameMonth(rRecharge.getUpdateTime().getTime())) {// 如果是当月充值的
			rRecharge.setRechargeMonth(rRecharge.getRechargeMonth() + money);

			if (money > rRecharge.getRechargeOnceMax()) { // 更新单笔记录
				rRecharge.setRechargeOnceMax(money);
			}
		} else {
			rRecharge.setRechargeMonth(money);
			rRecharge.setRechargeOnceMax(money);
		}
		rRecharge.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));// 每次必更新
		rolePlatformRechargeDao.cacheUpdate(rRecharge, rRecharge.getUserRoleId());
		// 判断单笔充值和累充是否满足现实qq号
		if (toClient && isShowQq(rRecharge, config)) {
			BusMsgSender.send2One(userRoleId, cmdType, 1);
		}

	}

	/**
	 * 充值是否满足现实qq号条件
	 */
	private boolean isShowQq(RolePlatformRecharge recharge, SuperVipConfig config) {
		if (recharge.getRechargeOnceMax() >= config.getOnceRecharge() || recharge.getRechargeMonth() >= config.getAllRecharge()) {
			return true;
		}
		return false;
	}

	/**
	 * 接收web指令更新数据
	 */
	public void webOrderUpdateConfig(Map<String, Object> params) {
		// state -1关闭活动 0新增 1修改
		int state = CovertObjectUtil.object2Integer(params.get("state"));
		if (state == -1) {
			// 关闭活动
			this.ptCommonSuperVipConfigExportService.closeActivity();
			short cmdType = getCmd(1); // 指令类别
			// 通知所有在线玩家
			BusMsgSender.send2All(cmdType, 0);
		} else {
			// 更新缓存
			this.ptCommonSuperVipConfigExportService.updateConfig(params);
		}

	}
	 
	/**type=1超级会员活动是否关闭
	 * type=2充值是否达到显示qq号
	 * 获取对应的推送指令
	 * 37,迅雷，顺网还是走各自指令（之前的就没有并）
	 * 其他超级会员的都走公用的指令
	 */
	private short getCmd(int type){
		short cmdType = 0; // 指令类别
		//充值导致qq状态显示变化
		switch (ChuanQiConfigUtil.getPlatfromId()) {
		case PlatformPublicConfigConstants._37_PLAT_NAME:
			cmdType =type==1?ClientCmdType.GET_PLATFORM_37WAN_SUPER_VIP_CLOSED:ClientCmdType.GET_PLATFORM_37WAN_SHOWQQ; //37wan
			break;
		case PlatformPublicConfigConstants.XUNLEI_NAME:
			cmdType =type==1?ClientCmdType.GET_PLATFORM_XUNLEI_CLOSED:ClientCmdType.GET_PLATFORM_XUNLEI_SHOWQQ; //迅雷
			break;
		case PlatformPublicConfigConstants.SHUWANG_NAME:
			cmdType =type==1? ClientCmdType.GET_PLATFORM_SHUNWANG_CLOSED:ClientCmdType.GET_PLATFORM_SHUNWANG_SHOWQQ;//顺网
			break;
		//以上已经做好了就不走公用指令，之后加的都走公用的指令
		default:
			cmdType =type==1?ClientCmdType.GET_PLATFORM_SUPER_QQ_CLOSE:ClientCmdType.GET_PLATFORM_SHOWQQ;//搜狗
			break;
		}
		return cmdType;
	}
	
	/**
	 * 获取对应的config
	 * @param modName 对应的模块的名称
	 * @return
	 */
	public PtCommonPublicConfig getConfigByModName(String modName){
		return platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(modName);
	}
	/**
	 * 获取玩家在平台的相关信息
	 * @param userRoleId
	 * @param modelName
	 * @param args 需要的参数全部
	 * @return
	 */
	public Map<String, String> getRoleMap(Long userRoleId,String modelName) {
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if (role == null) {
				return null;
		}
		String roleId = userRoleId.toString();
		Map<String, String> infoMap = dataContainer.getData(modelName, roleId);
		ChuanQiLog.info("web后台：infoMap={}", infoMap);
		if (infoMap == null) {
			String infoStr = dataContainer.getData(GameConstants.PLATFORM_PARAM_COMPONENT_NAME, role.getUserId());
			ChuanQiLog.info("玩家登陆，平台id={},后台传过来的参数args={}", ChuanQiConfigUtil.getPlatfromId(),infoStr);
			if (infoStr == null || "".equals(infoStr)) {
				return null;
			}
			infoMap =  getInfoMap(infoStr);
			dataContainer.putData(modelName, roleId, infoMap);
			dataContainer.removeData(GameConstants.PLATFORM_PARAM_COMPONENT_NAME, role.getUserId());
			
		}
		return infoMap;
	}
	/**
	 * 获取玩家在平台的相关信息
	 * @param userRoleId
	 * @param modelName
	 * @param args 需要的参数全部
	 * @return
	 */
	public Map<String, String> getRoleMapTW(Long userRoleId,String modelName) {
		RoleWrapper role = roleExportService.getUserRoleFromDb(userRoleId);;
		if (role == null) {
			return null;
		}
		String roleId = userRoleId.toString();
		Map<String, String> infoMap = dataContainer.getData(modelName, roleId);
		ChuanQiLog.info("web后台：infoMap={}", infoMap);
		if (infoMap == null) {
			String infoStr = dataContainer.getData(GameConstants.PLATFORM_PARAM_COMPONENT_NAME, role.getUserId());
			ChuanQiLog.info("玩家登陆，平台id={},后台传过来的参数args={}", ChuanQiConfigUtil.getPlatfromId(),infoStr);
			if (infoStr == null || "".equals(infoStr)) {
				return null;
			}
			infoMap =  getInfoMap(infoStr);
			dataContainer.putData(modelName, roleId, infoMap);
			dataContainer.removeData(GameConstants.PLATFORM_PARAM_COMPONENT_NAME, role.getUserId());
			
		}
		return infoMap;
	}
	/*
	 * 解析web传来的一串数据格式：“XXX=YYY@@XXX=YYY”
	 */
	private static Map<String, String> getInfoMap(String info) {
		String[] arr = info.split(PlatformPublicConfigConstants.FLAG1);
		Map<String, String> map = new HashMap<String, String>();
		if(arr!=null && arr.length>0){
			for (int i = 0; i < arr.length; i++) {
				String val = arr[i];
				String[] arr2 = val.split(PlatformPublicConfigConstants.FLAG2);
				map.put(arr2[0], arr2[1]);
			}
		}
		return map;
	}
	
	//*************************************正常礼包领取和状态查询***************************************
	/**
	 * 直接返回礼包领取状况值
	 * @param userRoleId
	 * @return
	 */
	public Integer getStateByRoleId(Long userRoleId) {
		RolePlatformInfo rolePlatformGift = rolePlatformInfoDao.cacheAsynLoad(userRoleId, userRoleId);
		Integer state = 0;
		if (rolePlatformGift != null) {
			state = rolePlatformGift.getGiftState();
		}
		return state;
	}
	/**
	 * 返回礼包领取状况实体
	 * @param userRoleId
	 * @return
	 */
	public RolePlatformInfo getRolePlatformInfo(Long userRoleId) {
		
		return  rolePlatformInfoDao.cacheAsynLoad(userRoleId, userRoleId);
	}
	
	/**
	 * 领取奖励后处理逻辑
	 * @param userRoleId
	 * @param items  奖励
	 * @param position 要更新的二进制位置从第0位开始，调用放要加减好位置
	 * returnState 是否需要及时返回礼包状态 , String giftType
	 */
	public Object[] updateBagAndState(Long userRoleId, Map<String, Integer> items,int position,String giftType, boolean returnState) {
		Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(items, userRoleId);
		// 背包空间不足
		if (code != null) {
			return code;
		}
		RolePlatformInfo rolePlatformInfo = rolePlatformInfoDao.cacheAsynLoad(userRoleId, userRoleId);
		Integer state = 0;
		if( rolePlatformInfo != null ){
			
			if(updateStateStandby(giftType)){
				//备用字段存礼包领取情况
				 state  = rolePlatformInfo.getGiftStateStandby();
			}else{
				state  =  rolePlatformInfo.getGiftState();
			}
		}
		if (BitOperationUtil.calState(state, position)) {
			Integer newState = BitOperationUtil.chanageState(state, position);
			if (rolePlatformInfo == null) {
				rolePlatformInfo = new RolePlatformInfo();
				rolePlatformInfo.setGiftState(0);
				rolePlatformInfo.setGiftStateStandby(0);
				rolePlatformInfo.setUserRoleId(userRoleId);
				rolePlatformInfo.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
				rolePlatformInfo.setUpdateTime(GameSystemTime.getSystemMillTime());//没特殊需求跟创建时间一样
				rolePlatformInfo.setPlatformId(ChuanQiConfigUtil.getPlatfromId());
				rolePlatformInfoDao.cacheInsert(rolePlatformInfo, userRoleId);
			} 
			
			if(isUpateTime(giftType)){
				rolePlatformInfo.setUpdateTime(GameSystemTime.getSystemMillTime());	
			}
			
			if(updateStateStandby(giftType)){
				//备用字段存礼包领取情况
				rolePlatformInfo.setGiftStateStandby(newState);
			}else{
				rolePlatformInfo.setGiftState(newState);
			}
			rolePlatformInfoDao.cacheUpdate(rolePlatformInfo, userRoleId);
			// ****进背包****
			roleBagExportService.putGoodsAndNumberAttr(items, userRoleId, GoodsSource.GOODS_PLATFORM_GIFT, LogPrintHandle.GET_PLATFORM_GIFT, LogPrintHandle.GET_PLATFORM_GIFT, true);
			//日志
			position  = this.getLevel(giftType, position);
			JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(items, null);
			GamePublishEvent.publishEvent(new PlatformGiftRewardLogEvent(userRoleId, jsonArray,ChuanQiConfigUtil.getPlatfromId(),position,giftType));
			if(returnState){
				return new Object[] { AppErrorCode.SUCCESS,newState };
			}else {
				return new Object[] { AppErrorCode.SUCCESS };
			}
		} else {
			// 已经领取过不能再领取
			return new Object[]{2};
		}
	}
	/**
	 * 获取等级
	 */
	private int getLevel(String giftType,int position){
		int level = position;
		if(giftType.equals(PlatformPublicConfigConstants._360_TEQUAN_LB)){
			PtCommonPublicConfig commonConfig = this.getConfigByModName(PlatformPublicConfigConstants._360_TEQUAN_LEVEL);
			if(commonConfig!=null){
				String name = "tequan"+(position+1);
				  level = (Integer)commonConfig.getInfo().get(name); //等级
			}
		}
		return level;
	}
	/**giftState 
	 * fiftStateStandby 后期新增功能导致新增了已字段
	 * 原因：前期已经上了一部分，表已经存数据的情况下，新增的领取状态就放到giftStateStandby
	 * 后期新增领取礼包的功能,更新获取的字段不一样,需要用备用字段存礼包领取情况在这里加
	 */
	private boolean updateStateStandby(String giftType){
		if(giftType.equals(PlatformPublicConfigConstants._360_TEQUAN_LB)
				||giftType.equals(PlatformPublicConfigConstants._37_LINGPAI_LB)){
			//后期新增的功能
			return true;
		}
		return false;
	}
	
	/**特殊需求的平台  
	 * 比如：360特权
	 * 特权礼包只有这5个等级可领取：5、10、15、20、25。 5级礼包每天可领取一次
	 * 是否更新时间
	 */
	private boolean isUpateTime(String giftType){
		if(giftType.equals(PlatformPublicConfigConstants._360_TEQUAN_LB)){
			return true;
		}
		return false;
	}

}
