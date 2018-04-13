package com.junyou.bus.xiulianzhilu.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xiulianzhilu.configure.export.XiuLianJiangLiConfig;
import com.junyou.bus.xiulianzhilu.configure.export.XiuLianJiangLiConfigExportService;
import com.junyou.bus.xiulianzhilu.configure.export.XiuLianRenWuConfig;
import com.junyou.bus.xiulianzhilu.configure.export.XiuLianRenWuConfigExportService;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.bus.xiulianzhilu.dao.RoleXiulianJifenDao;
import com.junyou.bus.xiulianzhilu.dao.RoleXiulianTaskDao;
import com.junyou.bus.xiulianzhilu.entity.RoleXiulianJifen;
import com.junyou.bus.xiulianzhilu.entity.RoleXiulianTask;
import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;


@Service
public class XiuLianService {

	@Autowired
	private RoleXiulianJifenDao roleXiulianJifenDao;
	@Autowired
	private RoleXiulianTaskDao roleXiulianTaskDao;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private XiuLianJiangLiConfigExportService xiuLianJiangLiConfigExportService;
	@Autowired
	private XiuLianRenWuConfigExportService xiuLianRenWuConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private AccountExportService accountExportService;
	
	public List<RoleXiulianJifen> initRoleXiulianJifens(Long userRoleId){
		return roleXiulianJifenDao.initRoleXiulianJifen(userRoleId);
	}
	
	public List<RoleXiulianTask> initRoleXiulianTasks(Long userRoleId){
		return roleXiulianTaskDao.initRoleXiulianTask(userRoleId);
	}
	
	//判断是否在时间内
	private boolean isOnTime(Long userRoleId){
		RoleWrapper wrapper = roleExportService.getLoginRole(userRoleId);
		XiuLianJiangLiConfig config = xiuLianJiangLiConfigExportService.loadById(-1);//-1配置包含活动天数
		if(config == null || config.getTime() <= 0){//不在时间内
			return false;
		}
		int day = DatetimeUtil.twoDaysDiffence(wrapper.getCreateTime());
		if(day <= config.getTime()){
			return true;
		}
		return false;
	}
	
	//获取是第几天
	private int getTheDay(Long userRoleId){
		RoleWrapper wrapper = roleExportService.getLoginRole(userRoleId);
		XiuLianJiangLiConfig config = xiuLianJiangLiConfigExportService.loadById(-1);//-1配置包含活动天数
		if(config == null || config.getTime() <= 0){//不在时间内
			return 0;
		}
		int day = DatetimeUtil.twoDaysDiffence(wrapper.getCreateTime())+1;
		return day;
	}
	
	private RoleXiulianTask getRoleXiulianTask(Long userRoleId){
		List<RoleXiulianTask> list = roleXiulianTaskDao.cacheLoadAll(userRoleId);
		RoleXiulianTask roleTask = null;
		if(list == null || list.size() <= 0){
			roleTask = new RoleXiulianTask();
			roleTask.setUserRoleId(userRoleId);
			roleTask.setCompleteTaskId("");
			roleTask.setCreateTime(GameSystemTime.getSystemMillTime());
			roleTask.setUpdateTime(GameSystemTime.getSystemMillTime());
			
			roleXiulianTaskDao.cacheInsert(roleTask, userRoleId);
		}else{
			roleTask = list.get(0);
			if(!DatetimeUtil.dayIsToday(roleTask.getUpdateTime())){//跨天
				//roleTask.setCompleteTaskId("");
				roleTask.setUpdateTime(GameSystemTime.getSystemMillTime());
				//roleTask.setDayJson(null);
				roleXiulianTaskDao.cacheUpdate(roleTask, userRoleId);
				//以下是跨天了处理当天的累计任务进度
				/*int day = getTheDay(userRoleId);
				List<XiuLianRenWuConfig> configList = xiuLianRenWuConfigExportService.getConfigByDay(day);
				if(configList != null && configList.size() > 0){
					for (int i = 0; i < list.size(); i++) {
						XiuLianRenWuConfig config = configList.get(i);
						if(config.getTimeType().intValue() == XiuLianConstants.XIULIAN_TIME_TYPE_LEIJI){
							JSONObject roleJson = roleTask.getLeijiJson();
							Integer roleCompleteNum = 0; //检查玩家之前做了几次
							if (roleJson.get(config.getId().toString()) != null) {
								roleCompleteNum = (Integer) roleJson.get(config.getId().toString());
							}
							//是否达到条件                                             
							if(roleCompleteNum >= config.getData1().intValue()){
								//标记完成并且奖积分
								completeTask(userRoleId, config.getId());
							}
						}
					}
				}*/
			}
		}
		return roleTask;
	}
	
	private RoleXiulianJifen getRoleXiulianJifen(Long userRoleId){
		List<RoleXiulianJifen> list = roleXiulianJifenDao.cacheLoadAll(userRoleId);
		RoleXiulianJifen jifen = null;
		if(list == null || list.size() <= 0){
			jifen =  new RoleXiulianJifen();
			jifen.setUserRoleId(userRoleId);
			jifen.setAllJifen(0);
			jifen.setCanUseJifen(0);
			jifen.setCreateTime(GameSystemTime.getSystemMillTime());
			jifen.setDayStatus("");
			jifen.setExchangeId("");
			jifen.setJackpotLevel(1);
			jifen.setChenghaoStatus(0);
			jifen.setUpdateTime(GameSystemTime.getSystemMillTime());
			
			roleXiulianJifenDao.cacheInsert(jifen, userRoleId);
		}else{
			jifen = list.get(0);
			/*if(!DatetimeUtil.dayIsToday(jifen.getUpdateTime())){//跨天
				jifen.setDayStatus(0);
				jifen.setUpdateTime(GameSystemTime.getSystemMillTime());
				roleXiulianJifenDao.cacheUpdate(jifen, userRoleId);
			}*/
		}
		return jifen;
	}
	
	/**
	 * 状态数据中 是否包含 id数据
	 * @param status
	 * @param id
	 * @return true 不存在，false 存在
	 */
	private boolean isContains(String status,int id){
		if(status == null || status.equals("")){
			return true;
		}
		String[] str = status.split(",");
		for (int i = 0; i < str.length; i++) {
			if(id == Integer.parseInt(str[i])){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 完成任务发积分
	 * @param userRoleId
	 * @param configId
	 */
	private void completeTask(Long userRoleId,Integer configId){
		RoleXiulianTask roleTask = getRoleXiulianTask(userRoleId);
		//判断任务是否已经完成
		if(!isContains(roleTask.getCompleteTaskId(), configId)){
			return;
		}
		//发积分，记状态
		XiuLianRenWuConfig taskConfig = xiuLianRenWuConfigExportService.loadById(configId);
		if(taskConfig == null){
			return;
		}
		int jifen = taskConfig.getJifen();
		RoleXiulianJifen roleJifen = getRoleXiulianJifen(userRoleId);
		roleJifen.setAllJifen(roleJifen.getAllJifen() + jifen);
		roleJifen.setCanUseJifen(roleJifen.getCanUseJifen() + jifen);
		roleJifen.setUpdateTime(GameSystemTime.getSystemMillTime());
		
		if(roleTask.getCompleteTaskId() == null || "".equals(roleTask.getCompleteTaskId())){
			roleTask.setCompleteTaskId(configId+"");
		}else{
			roleTask.setCompleteTaskId(roleTask.getCompleteTaskId() + ","+configId);
		}
		roleTask.setUpdateTime(GameSystemTime.getSystemMillTime());
		roleXiulianTaskDao.cacheUpdate(roleTask, userRoleId);
		roleXiulianJifenDao.cacheUpdate(roleJifen, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.XIULIAN_JIFEN_CHARGE, new Object[]{jifen,configId});
	}
	
	public void taskChargeTxType(Long userRoleId,Integer type,Object data){
		if(!isOnTime(userRoleId)){
			return;
		}
		int day = getTheDay(userRoleId);
		//先看类型在不在
		List<XiuLianRenWuConfig> configList = xiuLianRenWuConfigExportService.getConfigByType(type);
		if(configList == null || configList.size() <= 0){
			return;
		}
		int timeType = xiuLianRenWuConfigExportService.getTimeType(type);
		RoleXiulianTask roleTask = getRoleXiulianTask(userRoleId);
		for (int i = 0; i < configList.size(); i++) {
			if(type.intValue() == XiuLianConstants.JINGJI_RANK){
				XiuLianRenWuConfig config = configList.get(i);
				//特殊类型 判断是否达到要求
				Integer roleCompleteNum = Integer.parseInt(data.toString());
				if(roleCompleteNum<= 0){
					continue;
				}
				JSONObject roleJson = null;
				if(timeType == XiuLianConstants.XIULIAN_TIME_TYPE_LEIJI){
					roleJson = roleTask.getLeijiJson();
				}else{
					roleJson = roleTask.getDayJson();
				}
				Integer roleNum = 0; 
				if (roleJson.get(config.getId().toString()) != null) {
					roleNum = (Integer) roleJson.get(config.getId().toString());
				}
				if(roleNum > 0 && roleCompleteNum >= roleNum){
					continue;
				}
				roleJson.put(config.getId().toString(), roleCompleteNum);//保存每个任务ID的当前进度
				if(timeType == XiuLianConstants.XIULIAN_TIME_TYPE_LEIJI){
					roleTask.setLeijiJson(roleJson);
				}else{
					roleTask.setDayJson(roleJson);
				}
				roleXiulianTaskDao.cacheUpdate(roleTask, userRoleId);
				/*//判断今天是否有这个任务
				if(day >= config.getDay().intValue()){
					//是否达到条件
					if(roleCompleteNum <= config.getData1().intValue()){
						//标记完成并且奖积分
						completeTask(userRoleId, config.getId());
					}
				}*/
			}else{

				XiuLianRenWuConfig config = configList.get(i);
				//特殊类型 判断是否达到要求
				Integer roleCompleteNum = Integer.parseInt(data.toString());
				JSONObject roleJson = null;
				if(timeType == XiuLianConstants.XIULIAN_TIME_TYPE_LEIJI){
					roleJson = roleTask.getLeijiJson();
				}else{
					roleJson = roleTask.getDayJson();
				}
				if(type.intValue() == XiuLianConstants.DAZUO || type.intValue() == XiuLianConstants.FUBEN_NANWUYUE){//只能往上升的类型
					Integer roleNum = 0; 
					if (roleJson.get(config.getId().toString()) != null) {
						roleNum = (Integer) roleJson.get(config.getId().toString());
					}
					if(roleCompleteNum <= roleNum){
						continue;
					}
				}
				if(roleCompleteNum > config.getData1()){
					roleCompleteNum = config.getData1();
				}
				roleJson.put(config.getId().toString(), roleCompleteNum);//保存每个任务ID的当前进度
				if(timeType == XiuLianConstants.XIULIAN_TIME_TYPE_LEIJI){
					roleTask.setLeijiJson(roleJson);
				}else{
					roleTask.setDayJson(roleJson);
				}
				roleXiulianTaskDao.cacheUpdate(roleTask, userRoleId);
				/*//判断今天是否有这个任务
				if(day >= config.getDay().intValue()){
					//是否达到条件
					if(roleCompleteNum >= config.getData1().intValue()){
						//标记完成并且奖积分
						completeTask(userRoleId, config.getId());
					}
				}*/
			
			}
		}
		return;
	}
	public void taskCharge(Long userRoleId,Integer type,Object data){
		if(!isOnTime(userRoleId)){
			return;
		}
		int day = getTheDay(userRoleId);
		//先看类型在不在
		List<XiuLianRenWuConfig> configList = xiuLianRenWuConfigExportService.getConfigByType(type);
		if(configList == null || configList.size() <= 0){
			return;
		}
		RoleXiulianTask roleTask = getRoleXiulianTask(userRoleId);
		//根据类型获取时间类型
		int timeType = xiuLianRenWuConfigExportService.getTimeType(type);
		if(timeType == XiuLianConstants.XIULIAN_TIME_TYPE_LEIJI){
			for (int i = 0; i < configList.size(); i++) {
				boolean isNext = true;
				boolean piliang = false;
				XiuLianRenWuConfig config = configList.get(i);
				//特殊类型 判断是否达到要求
				switch (type.intValue()) {
				case XiuLianConstants.QIANGHUA_LEVEL:
					if(Integer.parseInt(data.toString()) < Integer.parseInt(config.getData2())){
						isNext = false;
					}
					break;
				case XiuLianConstants.EQUIP_RONGLIAN:
				case XiuLianConstants.RECHARGE_ZUANSHI:
				case XiuLianConstants.XIAOFEI_ZUANSHI:
				case XiuLianConstants.XUNBAO:
				case XiuLianConstants.OPEN_BAG:
				case XiuLianConstants.TASK_RICHANG:
				case XiuLianConstants.TASK_GUILD:
					piliang = true;
					break;
				case XiuLianConstants.KILL_JY_MONSTER:
					if(!data.toString().equals(config.getData2())){
						isNext = false;
					}
					break;
				default:
					break;
				}
				if(!isNext){
					continue;
				}
				JSONObject roleJson = roleTask.getLeijiJson();
				Integer roleCompleteNum = 0; //检查玩家之前做了几次
				if (roleJson.get(config.getId().toString()) != null) {
					roleCompleteNum = (Integer) roleJson.get(config.getId().toString());
				}
				if (roleCompleteNum >= config.getData1()) {
					continue;//已完成的任务,不再累计
				}
				Integer countNum = roleCompleteNum;
				if(piliang){
					countNum += Integer.parseInt(data.toString());
				}else{
					countNum +=1;
				}
				if(countNum > config.getData1()){
					countNum = config.getData1();
				}
				roleJson.put(config.getId().toString(), countNum);//保存每个任务ID的当前进度
				roleTask.setLeijiJson(roleJson);
				roleXiulianTaskDao.cacheUpdate(roleTask, userRoleId);
				/*//判断今天和之前是否有这个任务
				if(day >= config.getDay().intValue()){
					//是否达到条件
					if(countNum >= config.getData1().intValue()){
						//标记完成并且奖积分
						completeTask(userRoleId, config.getId());
					}
				}*/
			}
		}else if(timeType == XiuLianConstants.XIULIAN_TIME_TYPE_DAY){
			List<XiuLianRenWuConfig> config2List = xiuLianRenWuConfigExportService.getConfigByDayAndType(day, type);
			if(config2List == null || config2List.size() <= 0){
				return;
			}
			for (int i = 0; i < config2List.size(); i++) {
				XiuLianRenWuConfig config = config2List.get(i);
				boolean isNext = true;
				boolean piliang = false;
				//特殊类型 判断是否达到要求
				switch (type.intValue()) {
				case XiuLianConstants.GUILD_GONGXIAN_VALUE:
				case XiuLianConstants.XIAOHAO_ZHENQI:
				case XiuLianConstants.XIAOHAO_YINGLIANG:
					piliang = true;
					break;
				default:
					break;
				}
				if(!isNext){
					continue;
				}
				JSONObject roleJson = roleTask.getDayJson();
				Integer roleCompleteNum = 0; //检查玩家之前做了几次
				if (roleJson.get(config.getId().toString()) != null) {
					roleCompleteNum = (Integer) roleJson.get(config.getId().toString());
				}
				if (roleCompleteNum >= config.getData1()) {
					continue; //已完成的任务
				}
				Integer countNum = roleCompleteNum;
				if(piliang){
					countNum += Integer.parseInt(data.toString());
				}else{
					countNum +=1;
				}
				if(countNum > config.getData1()){
					countNum = config.getData1();
				}
				roleJson.put(config.getId().toString(), countNum);//保存每个任务ID的当前进度
				roleTask.setDayJson(roleJson);
				roleXiulianTaskDao.cacheUpdate(roleTask, userRoleId);
				//是否达到条件
				/*if(countNum >= config.getData1().intValue()){
					//标记完成并且奖积分
					completeTask(userRoleId, config.getId());
				}*/
			}
		}
		return;
	}
	
	
	public Object[] lingquJifen(long userRoleId,Integer id){
		RoleXiulianTask roleTask = getRoleXiulianTask(userRoleId);
		//判断任务是否已经完成
		if(!isContains(roleTask.getCompleteTaskId(), id)){
			return AppErrorCode.XIULIAN_YI_LINGQU_JIFEN;
		}
		//发积分，记状态
		XiuLianRenWuConfig taskConfig = xiuLianRenWuConfigExportService.loadById(id);
		if(taskConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		Integer roleCompleteNum = 0; //检查玩家之前做了几次
		JSONObject roleJson = null;
		if(taskConfig.getTimeType().intValue() == XiuLianConstants.XIULIAN_TIME_TYPE_LEIJI){
			roleJson = roleTask.getLeijiJson();
		}else{
			roleJson = roleTask.getDayJson();
		}
		if (roleJson.get(id.toString()) != null) {
			roleCompleteNum = (Integer) roleJson.get(id.toString());
		}
		if(taskConfig.getMissiontype().intValue() == XiuLianConstants.JINGJI_RANK){
			if(roleCompleteNum > taskConfig.getData1().intValue()){
				return AppErrorCode.XIULIAN_WEI_TIAOJIAN;
			}
		}else{
			if(roleCompleteNum < taskConfig.getData1().intValue()){
				return AppErrorCode.XIULIAN_WEI_TIAOJIAN;
			}
		}
		int jifen = taskConfig.getJifen();
		RoleXiulianJifen roleJifen = getRoleXiulianJifen(userRoleId);
		roleJifen.setAllJifen(roleJifen.getAllJifen() + jifen);
		roleJifen.setCanUseJifen(roleJifen.getCanUseJifen() + jifen);
		roleJifen.setUpdateTime(GameSystemTime.getSystemMillTime());
		
		if(roleTask.getCompleteTaskId() == null || "".equals(roleTask.getCompleteTaskId())){
			roleTask.setCompleteTaskId(id+"");
		}else{
			roleTask.setCompleteTaskId(roleTask.getCompleteTaskId() + ","+id);
		}
		roleTask.setUpdateTime(GameSystemTime.getSystemMillTime());
		roleXiulianTaskDao.cacheUpdate(roleTask, userRoleId);
		roleXiulianJifenDao.cacheUpdate(roleJifen, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.XIULIAN_JIFEN_CHARGE, new Object[]{jifen,id});
		return new Object[]{1,id,jifen};
	}
	
	/**
	 * 积分兑换奖励
	 * @param userRoleId
	 * @param jcLevel 奖池等级
	 * @param few  第几个奖励 (-1表示大奖)
	 * @return
	 */
	public Object[] jifenDuihuan(long userRoleId,int jcLevel,int few){
		if(!isOnTime(userRoleId)){
			return AppErrorCode.XIULIAN_NOT_TIME;
		}
		//判断当前奖池等级是否一致
		RoleXiulianJifen roleJifen = getRoleXiulianJifen(userRoleId);
		if(roleJifen.getJackpotLevel().intValue() != jcLevel){
			return AppErrorCode.XIULIAN_JC_LEVEL_NO;
		}
		//判断奖励是否兑换过
		if(!isContains(roleJifen.getExchangeId(), few)){
			return AppErrorCode.XIULIAN_YI_DUIHUAN;
		}
		//判断积分是否足够，如果是大奖（-1） 则判断其他是否全领取了
		XiuLianJiangLiConfig config = xiuLianJiangLiConfigExportService.getConfigByJcLevelAndType(jcLevel, XiuLianConstants.XIULIAN_JIANGLI_TYPE_JIANGCHI);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		Map<Integer, String[]> jlMap = config.getPtItem();
		if(jlMap == null || jlMap.size() <= 0){
			return AppErrorCode.CONFIG_ERROR;
		}
		int jifen = 0;//需消耗的积分
		Map<String, Integer> itemMap =null;//可获得的奖励
		if(few == -1){
			//判断其他的是否都领了
			for (Integer key: jlMap.keySet()) {
				if(isContains(roleJifen.getExchangeId(), key)){//如果有一个没兑换，则不可兑换大奖
					return AppErrorCode.XIULIAN_NOT_JIFEN_DUIHUAN;
				}
			}
			//判断是否还有下一层
			XiuLianJiangLiConfig nextConfig = xiuLianJiangLiConfigExportService.getConfigByJcLevel(jcLevel+1);
			if(nextConfig ==null){//最后1个了，判断称号是否兑换过
				if(roleJifen.getChenghaoStatus() == 1){
					return AppErrorCode.XIULIAN_YI_CHENGHAO;
				}
			}
			itemMap = config.getSpItem();
		}else{
			//判断积分是否足够
			String[] itemStr = jlMap.get(few);
			if(itemStr == null || itemStr.length < 3){
				return AppErrorCode.DATA_ERROR;
			}
			int itemJf = Integer.parseInt(itemStr[2]);
			if(roleJifen.getCanUseJifen() < itemJf){
				return AppErrorCode.XIULIAN_NOT_JIFEN;
			}
			jifen = itemJf;
			itemMap = new HashMap<>();
			itemMap.put(itemStr[0], Integer.parseInt(itemStr[1]));
		}
		
		if(itemMap== null || itemMap.size() <= 0){
			return AppErrorCode.DATA_ERROR;
		}
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBag(itemMap, userRoleId);
		if(bagCheck != null){
			return bagCheck;
		}
		//消耗积分
		if(jifen > 0){
			roleJifen.setCanUseJifen(roleJifen.getCanUseJifen() - jifen);
		}
		//修改状态
		if(few == -1){
			//判断是否还有下一层
			XiuLianJiangLiConfig nextConfig = xiuLianJiangLiConfigExportService.getConfigByJcLevel(jcLevel+1);
			if(nextConfig !=null){
				roleJifen.setExchangeId("");
				//奖池等级升级
				roleJifen.setJackpotLevel(nextConfig.getJlLevel());
			}else{//没有下一个等级了，标记最终大奖领取状态
				roleJifen.setChenghaoStatus(1);
				if(roleJifen.getExchangeId() == null || "".equals(roleJifen.getExchangeId())){
					roleJifen.setExchangeId(few+"");
				}else{
					roleJifen.setExchangeId(roleJifen.getExchangeId()+","+few);
				}
			}
		}else{
			if(roleJifen.getExchangeId() == null || "".equals(roleJifen.getExchangeId())){
				roleJifen.setExchangeId(few+"");
			}else{
				roleJifen.setExchangeId(roleJifen.getExchangeId()+","+few);
			}
		}
		roleJifen.setUpdateTime(GameSystemTime.getSystemMillTime());
		roleXiulianJifenDao.cacheUpdate(roleJifen, userRoleId);
		//发放奖励
		roleBagExportService.putGoodsAndNumberAttr(itemMap, userRoleId, GoodsSource.XIULIAN_DUIHUAN, LogPrintHandle.GET_XIULIAN_DUIHUAN, LogPrintHandle.GBZ_XIULIAN_DUIHUAN, true);
		
		return new Object[]{1,jcLevel,few,roleJifen.getCanUseJifen()};
	}
	
	
	/**
	 * 领取每日任务完成奖励
	 * @param userRoleId
	 * @return
	 */
	public Object[] lingquDay(Long userRoleId,int day){
		if(!isOnTime(userRoleId)){
			return AppErrorCode.XIULIAN_NOT_TIME;
		}
		RoleXiulianJifen roleJifen = getRoleXiulianJifen(userRoleId);
		if(!isContains(roleJifen.getDayStatus(), day)){
			return AppErrorCode.XIULIAN_YI_LINGQU_DAY;
		}
		RoleXiulianTask roleTask = getRoleXiulianTask(userRoleId);
		//判断任务是否都完成的
		List<XiuLianRenWuConfig> taskList = xiuLianRenWuConfigExportService.getConfigByDay(day);
		if(taskList == null || taskList.size() <= 0){
			return AppErrorCode.CONFIG_ERROR;
		}
		for (int i = 0; i < taskList.size(); i++) {
			XiuLianRenWuConfig task = taskList.get(i);
			if(isContains(roleTask.getCompleteTaskId(), task.getId())){
				return AppErrorCode.XIULIAN_XIAN_TASK;
			}
		}
		XiuLianJiangLiConfig jlConfig = xiuLianJiangLiConfigExportService.getConfigByDayAndType(day, XiuLianConstants.XIULIAN_JIANGLI_TYPE_DAY);
		if(jlConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBag(jlConfig.getSpItem(), userRoleId);
		if(bagCheck != null){
			return bagCheck;
		}
		if(roleJifen.getDayStatus() == null || "".equals(roleJifen.getDayStatus())){
			roleJifen.setDayStatus(day+"");
		}else{
			roleJifen.setDayStatus(roleJifen.getDayStatus()+","+day);
		}
		roleJifen.setUpdateTime(GameSystemTime.getSystemMillTime());
		roleXiulianJifenDao.cacheUpdate(roleJifen, userRoleId);
		
		roleBagExportService.putGoodsAndNumberAttr(jlConfig.getSpItem(), userRoleId, GoodsSource.XIULIAN_DAY, LogPrintHandle.GET_XIULIAN_DAY, LogPrintHandle.GBZ_XIULIAN_DAY, true);
		
		return new Object[]{1,day};
	}
	
	
	public Object[] getChengHao(Long userRoleId){
		if(!isOnTime(userRoleId)){
			return AppErrorCode.XIULIAN_NOT_TIME;
		}
		int day = getTheDay(userRoleId);//判断是否是最后1天
		XiuLianJiangLiConfig dConfig = xiuLianJiangLiConfigExportService.getConfigByDayAndType(day, XiuLianConstants.XIULIAN_JIANGLI_TYPE_DAY);
		if(dConfig == null){
			return AppErrorCode.XIULIAN_NOT_TIME;
		}
		XiuLianJiangLiConfig nConfig = xiuLianJiangLiConfigExportService.getConfigByDayAndType(day+1, XiuLianConstants.XIULIAN_JIANGLI_TYPE_DAY);
		if(nConfig != null){
			return AppErrorCode.XIULIAN_NO_DUIHUAN_TIME;
		}
		//判断是否兑换过称号
		RoleXiulianJifen roleJifen = getRoleXiulianJifen(userRoleId);
		if(roleJifen.getChenghaoStatus() == 1){
			return AppErrorCode.XIULIAN_YI_CHENGHAO;
		}
		//判断积分
		XiuLianJiangLiConfig config = xiuLianJiangLiConfigExportService.loadById(-1);//-1配置包含活动天数
		if(config == null || config.getTime() <= 0 || config.getChenghaoJifen() <= 0){//不在时间内
			return AppErrorCode.CONFIG_ERROR;
		}
		int needJifen = config.getChenghaoJifen();
		int needGlod = 0; //所需补充的元宝
		if(roleJifen.getCanUseJifen() < needJifen){
			needGlod = (needJifen - roleJifen.getCanUseJifen()) * config.getJfJiaZhi();
		}
		//判断需补充的元宝
		if(needGlod > 0){
			Object[] ret = accountExportService.isEnought(GoodsCategory.GOLD, needGlod, userRoleId);
			if(ret != null){
				return ret;
			}
		}
		XiuLianJiangLiConfig jlConfig = xiuLianJiangLiConfigExportService.getConfigByMaxJcLevel(XiuLianConstants.XIULIAN_JIANGLI_TYPE_JIANGCHI);
		if(jlConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBag(jlConfig.getSpItem(), userRoleId);
		if(bagCheck != null){
			return bagCheck;
		}
		roleBagExportService.putGoodsAndNumberAttr(jlConfig.getSpItem(), userRoleId, GoodsSource.XIULIAN_DUIHUAN, LogPrintHandle.GET_XIULIAN_DUIHUAN, LogPrintHandle.GBZ_XIULIAN_DUIHUAN, true);
		
		roleJifen.setChenghaoStatus(1);
		roleJifen.setUpdateTime(GameSystemTime.getSystemMillTime());
		
		roleXiulianJifenDao.cacheUpdate(roleJifen, userRoleId);
		
		return new Object[]{1};
	}
	/**
	 * 获取信息
	 * @param userRoleId
	 * @return
	 */
	public Object[] getInfo(Long userRoleId){
		if(!isOnTime(userRoleId)){
			return null;
		}
		RoleXiulianJifen roleJifen = getRoleXiulianJifen(userRoleId);
		RoleXiulianTask roleTask = getRoleXiulianTask(userRoleId);
		int day = getTheDay(userRoleId);
		List<Object[]> list = new ArrayList<>();
		for (int i = 1; i <= day; i++) {
			if(!isContains(roleJifen.getDayStatus(), i)){//已全部完成
				continue;
			}
			//获取这天的全部任务
			List<XiuLianRenWuConfig> configList = xiuLianRenWuConfigExportService.getConfigByDay(i);
			if(configList == null || configList.size() <= 0){
				continue;
			}
			List<Object[]> taskList = new ArrayList<>();
			for (int j = 0; j < configList.size(); j++) {
				XiuLianRenWuConfig config = configList.get(j);
				//判断奖励是否已经领取过
				if(!isContains(roleTask.getCompleteTaskId(), config.getId())){
					continue;
				}
				JSONObject roleJson = null;
				if(config.getTimeType() == XiuLianConstants.XIULIAN_TIME_TYPE_LEIJI){
					roleJson = roleTask.getLeijiJson();
				}else{
					roleJson = roleTask.getDayJson();
				}
				Integer roleNum = 0; 
				if (roleJson.get(config.getId().toString()) != null) {
					roleNum = (Integer) roleJson.get(config.getId().toString());
				}
				taskList.add(new Object[]{config.getId(),roleNum});
			}
			list.add(new Object[]{i,taskList.toArray()});
		}
		
		return new Object[]{
				roleJifen.getJackpotLevel()
				,CovertObjectUtil.isEmpty(roleJifen.getExchangeId())?null:roleJifen.getExchangeId().split(",")
				,list.toArray()
				,roleJifen.getAllJifen()
				,roleJifen.getCanUseJifen()
		};
	}

}
