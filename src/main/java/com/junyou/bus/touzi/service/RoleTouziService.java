package com.junyou.bus.touzi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.serverinfo.export.ServerInfoServiceManager;
import com.junyou.bus.touzi.MailContentTz;
import com.junyou.bus.touzi.configure.export.TouZiConfig;
import com.junyou.bus.touzi.configure.export.TouZiConfigExportService;
import com.junyou.bus.touzi.dao.RoleTouziDao;
import com.junyou.bus.touzi.dao.filter.RoleTouziFilter;
import com.junyou.bus.touzi.entity.RoleTouzi;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 * 投资计划Service
 *
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-6-15 下午9:47:27 
 */
@Service
public class RoleTouziService {

	@Autowired
	private RoleTouziDao roleTouziDao;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private TouZiConfigExportService touZiConfigExportService;
	@Autowired
	private EmailExportService emailExportService;
	
	
//	private Map<Integer, TouZiConfig> getTouZiConfigMap(){
//		return touZiConfigExportService.loadAll();
//	}
	
//	private TouZiConfig getTouZiConfig(Integer configId){
//		return touZiConfigExportService.loadById(configId,);
//	}
	
	private List<RoleTouzi> getRoleTouziList(Long userRoleId){
		return roleTouziDao.cacheLoadAll(userRoleId);
	}
	
	private List<RoleTouzi> getRoleTouziList(Long userRoleId,IQueryFilter<RoleTouzi> filter){
		return roleTouziDao.cacheLoadAll(userRoleId,filter);
	}
	
	private RoleTouzi getRoleTouzi(Long userRoleId, Integer configId){
		List<RoleTouzi> list = roleTouziDao.cacheLoadAll(userRoleId, new RoleTouziFilter(configId));
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		
		return null;
	}
//	/**
//	 * 获取投资数据
//	 * @param userRoleId
//	 * @param type
//	 * @return
//	 */
//	private Map<Integer, RoleTouzi> getRoleTouziData(Long userRoleId){
//		List<RoleTouzi> list = getRoleTouziList(userRoleId);
//		if(list == null || list.size() <= 0){
//			return null;
//		}
//		
//		Map<Integer, RoleTouzi> dataMap = new HashMap<>();
//		for (RoleTouzi roleTouzi : list) {
//			dataMap.put(roleTouzi.getConfigId(), roleTouzi);
//			
//			//判断是否跨天
//			if(!DateUtils.isSameDay(new Timestamp(roleTouzi.getUpdateTime()), new Timestamp(GameSystemTime.getSystemMillTime()))){
//				
//				//登陆天数+1
//				roleTouzi.setTzType(roleTouzi.getTzType() + 1);
//				updateRoleTouzi(roleTouzi);
//			}
//		}
//		
//		return dataMap;
//	}
	
	
	
	/**
	 * 获取投资数据
	 * @param userRoleId
	 * @param type
	 * @return
	 */
	private Map<Integer, RoleTouzi> getRoleTouziData(Long userRoleId,final int type){
		
		List<RoleTouzi> list = getRoleTouziList(userRoleId,new IQueryFilter<RoleTouzi>() {
			@Override
			public boolean stopped() {
				return false;
			}
			
			@Override
			public boolean check(RoleTouzi entity) {
				return entity.getTzType() ==  type;
			}
		});
		
		if(list == null || list.size() <= 0){
			return null;
		}
		
		Map<Integer, RoleTouzi> dataMap = new HashMap<>();
		for (RoleTouzi roleTouzi : list) {
			dataMap.put(roleTouzi.getConfigId(), roleTouzi);
			
//			//判断是否跨天
//			if(!DateUtils.isSameDay(new Timestamp(roleTouzi.getUpdateTime()), new Timestamp(GameSystemTime.getSystemMillTime()))){
//				
//				//登陆天数+1
//				roleTouzi.setTzType(roleTouzi.getTzType() + 1);
//				updateRoleTouzi(roleTouzi);
//			}
		}
		
		return dataMap;
	}
	
	
	
	
	
	private RoleTouzi createRoleTouzi(Long userRoleId, Integer configId, Long time,int tzType){
		RoleTouzi roleTouzi = new RoleTouzi();
		
		roleTouzi.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		roleTouzi.setTzType(tzType);//投资类型
		roleTouzi.setRecevieDay(0);//领取天数则默认为0
		roleTouzi.setUserRoleId(userRoleId);
		roleTouzi.setConfigId(configId);
		roleTouzi.setCreateTime(time);
		roleTouzi.setUpdateTime(time);
		
		roleTouziDao.cacheInsert(roleTouzi, userRoleId);
		return roleTouzi;
	}
	
//	private void updateRoleTouzi(RoleTouzi roleTouzi){
//		roleTouzi.setUpdateTime(GameSystemTime.getSystemMillTime());
//		roleTouziDao.cacheUpdate(roleTouzi, roleTouzi.getUserRoleId());
//	}
	
	public List<RoleTouzi> initRoleTouzi(Long userRoleId){
		return roleTouziDao.initRoleTouzi(userRoleId);
	}
	
	/**
	 * 上线处理
	 * @param userRoleId
	 */
	public void onlineTouzi(Long userRoleId){
		
		List<RoleTouzi> list = getRoleTouziList(userRoleId);
		if(list == null || list.size() <= 0){
			return;
		}
		
		long nowTime = DatetimeUtil.getNow();
		Map<Long,MailContentTz> tzResultMail = new HashMap<>();
		Map<Long,MailContentTz> jiJinResultMail = new HashMap<>();
		
		for (RoleTouzi roleTouzi : list) {
			TouZiConfig config = touZiConfigExportService.loadById(roleTouzi.getConfigId(), roleTouzi.getTzType());
			if(config == null){
				ChuanQiLog.error("===roleTouziConfigId:"+roleTouzi.getConfigId()+"===TzType:"+roleTouzi.getTzType());
				continue;
			}
			int maxDay = config.getMaxDay();
			for (int i = 1; i <= maxDay; i++) {
				//如果已领奖
				if(roleTouzi.getRecevieDay() >= i){
					continue;
				}
				
				long awardDay = DatetimeUtil.getTimeByTargetD(roleTouzi.getCreateTime(), i,5);
				if(awardDay > nowTime){
					break;
				}
				if(roleTouzi.getTzType() == GameConstants.JIJING_TYPE && config.getType() == GameConstants.DAY_JIJIN){
					MailContentTz  mContentTz = jiJinResultMail.get(awardDay);
					if(mContentTz == null){
						mContentTz = new MailContentTz();
						jiJinResultMail.put(awardDay, mContentTz);
					}
					
					Map<String, Integer> itemMap =  config.getItemMap(i);
					
					int money = 0;
					for ( Integer val : itemMap.values()) {
						 money = money + val ; 
						 break;
					}
				 
					mContentTz.addContent(new String[]{money+"", (maxDay - i)+""});
					mContentTz.addItemMap(config.getItemMap(i));
				}else if(roleTouzi.getTzType() == GameConstants.TOUZHI_TYPE){
					MailContentTz  mContentTz = tzResultMail.get(awardDay);
					if(mContentTz == null){
						mContentTz = new MailContentTz();
						tzResultMail.put(awardDay, mContentTz);
					}
					
					mContentTz.addContent(new String[]{config.getName(),(maxDay - i)+""});
					mContentTz.addItemMap(config.getItemMap(i));
				}
				roleTouzi.setRecevieDay(i);
			}
			roleTouzi.setUpdateTime(System.currentTimeMillis());
			roleTouziDao.cacheUpdate(roleTouzi, userRoleId);
		}
		
		//发放邮件 投资
		for (Entry<Long,MailContentTz> entry : tzResultMail.entrySet()) {
			long time = entry.getKey();
			MailContentTz mailContent = entry.getValue();
			
			String[] attachments = EmailUtil.getAttachments(mailContent.getItemMap());
			List<String[]> contentParams = new ArrayList<>();
			for (String[] contents : mailContent.getContent()) {
				String[] t1 = new String[contents.length +1];
				t1[0] = GameConstants.TOUZI_MAIL_MSG_CODE;
				System.arraycopy(contents, 0, t1, 1, contents.length);
				contentParams.add(t1);
			}
			String title = EmailUtil.getCodeEmail(GameConstants.TOUZI_MAIL_MSG_CODE_TITLE);
			String email = EmailUtil.getCodeEmail(contentParams);
			for (String attachment : attachments) {
				emailExportService.sendEmailToOneByCreateTime(userRoleId, title,email, GameConstants.EMAIL_TYPE_SINGLE, attachment, time);	
			}
		}
		
		//发放邮件 基金
		for (Entry<Long,MailContentTz> entry : jiJinResultMail.entrySet()) {
			long time = entry.getKey();
			MailContentTz mailContent = entry.getValue();
			
			String[] attachments = EmailUtil.getAttachments(mailContent.getItemMap());
			List<String[]> contentParams = new ArrayList<>();
			for (String[] contents : mailContent.getContent()) {
				String[] t1 = new String[contents.length +1];
				t1[0] = GameConstants.JIJIN_MAIL_MSG_CODE;
				System.arraycopy(contents, 0, t1, 1, contents.length);
				contentParams.add(t1);
			}
			String title = EmailUtil.getCodeEmail(GameConstants.JIJIN_MAIL_MSG_CODE_TITLE);
			String email = EmailUtil.getCodeEmail(contentParams);
			for (String attachment : attachments) {
				emailExportService.sendEmailToOneByCreateTime(userRoleId,title, email, GameConstants.EMAIL_TYPE_SINGLE, attachment, time);	
			}
		}
	} 
	
	/**
	 * 获取投资计划数据
	 * @param userRoleId
	 */
	public Object[] getTouziData(Long userRoleId,int touzhiType){
		Map<Integer, TouZiConfig> map =  touZiConfigExportService.loadAll(touzhiType);
		if(map == null || map.size() <= 0){
			return null;
		}
		
		List<Object[]> list = new ArrayList<>();
		//玩家投资数据
		Map<Integer, RoleTouzi> roleDataMap = getRoleTouziData(userRoleId,touzhiType);
		
		for (TouZiConfig config : map.values()) {
			
			//  [0:int(id基金id),1:Number(购买基金这一天的起始时间戳,0表示没有购买)]
			RoleTouzi roleTouzi = roleDataMap == null ? null : roleDataMap.get(config.getId());
			long buyTime =  roleTouzi != null ?  roleTouzi.getCreateTime() : 0;
			if(buyTime != 0 ){
				list.add(new Object[]{config.getId(), buyTime});	
			}
		}
		return list.size() <= 0 ? null : list.toArray();
	}
	
//	/**
//	 * 获取投资计划数据
//	 * @param userRoleId
//	 */
//	public Object[] getJiJinData(Long userRoleId){
//		Map<Integer, TouZiConfig> map = getTouZiConfigMap();
//		if(map == null || map.size() <= 0){
//			return null;
//		}
//		
//		List<Object[]> list = new ArrayList<>();
//		//玩家投资数据
//		Map<Integer, RoleTouzi> roleDataMap = getRoleTouziData(userRoleId,GameConstants.JIJING_TYPE);
//		
//		for (TouZiConfig config : map.values()) {
//			
//			//  [0:int(id基金id),1:Number(购买基金这一天的起始时间戳,0表示没有购买)]
//			RoleTouzi roleTouzi = roleDataMap == null ? null : roleDataMap.get(config.getId());
//			long buyTime =  roleTouzi != null ?  roleTouzi.getCreateTime() : 0;
//			list.add(new Object[]{config.getId(), buyTime});
//		}
//		return list.size() <= 0 ? null : list.toArray();
//	}
	
	/**
	 * 投资某计划
	 * @param userRoleId
	 * @param id
	 * @return
	 */
	public Object[] touziPlan(Long userRoleId, Integer id){
		Map<Integer, TouZiConfig> map = new HashMap<>();
		if(id == null){
			
			//如果为null则表示一键投资全部有效期之内的投资计划,否则为对应id的投资申请
			map = touZiConfigExportService.loadAll(GameConstants.TOUZHI_TYPE);
			if(map == null || map.size() <= 0){
				return AppErrorCode.CONFIG_ERROR;
			}
			
		}else{
			//单个投资
			TouZiConfig config = touZiConfigExportService.loadById(id,GameConstants.TOUZHI_TYPE);
			if(config != null){
				
				map.put(id, config);
			}
		}
		
		//判断是否有可投资
		if(map == null || map.size() <= 0){
			return AppErrorCode.NO_TOUZI_PLAN;
		}
		
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(role == null){
			return AppErrorCode.ROLE_NOTEXISTS;
		}
		
		//玩家投资数据
		Map<Integer, RoleTouzi> roleDataMap = getRoleTouziData(userRoleId,GameConstants.TOUZHI_TYPE);
		List<Object[]> list = new ArrayList<>();
		int descYb = 0;
		
		//处理投资
		int kfDays = ServerInfoServiceManager.getInstance().getKaifuDays();
		for (TouZiConfig config : map.values()) {
			
			//判断活动过期
			if(config.getStartDay() <= kfDays && (config.getEndDay() <= 0 || kfDays <= config.getEndDay())){
				//判断是否有投资
				if(roleDataMap == null || roleDataMap.size() <= 0 || roleDataMap.get(config.getId()) == null){
					
					//消耗元宝
					descYb = descYb + config.getGold();
					
					// [0:int(id投资id),1:[0:int(奖励领到第几天0-代表一天都没领),1:Number(投资这一天的起始时间戳),2:int(登录天数)]
					list.add(new Object[]{config.getId(), GameSystemTime.getSystemMillTime()});
				}
			}
		}
		
		//判断是否有可投资
		if(list == null || list.size() <= 0){
			return AppErrorCode.NO_TOUZI_PLAN;
		}
		
		//判断消耗的元宝是否满足
		if(descYb > 0){
			Object[] error = accountExportService.isEnought(GoodsCategory.GOLD, descYb, userRoleId);
			if(error != null){
				return error;
			}else{
				//消耗元宝
				accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, descYb, userRoleId, LogPrintHandle.CONSUME_TOUZI, true, LogPrintHandle.CBZ_TOUZI);
				if(PlatformConstants.isQQ()){
					BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,descYb,LogPrintHandle.CONSUME_TOUZI,QQXiaoFeiType.CONSUME_TOUZI,1});
				}
			}
		}
		
		for (Object[] obj : list) {
			
			Integer configId = Integer.parseInt(obj[0].toString());
			//创建投资数据
			createRoleTouzi(userRoleId, configId, Long.parseLong(obj[1].toString()),GameConstants.TOUZHI_TYPE);
			
			TouZiConfig config = map.get(configId);
			if(config != null){
				
				//发送公告
				Object[] notifyData = new Object[]{
						new Object[]{2, userRoleId, role.getName()},
						new Object[]{4, config.getClientItemMap()}
				};
				BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT6, new Object[]{AppErrorCode.NOTIFY_SEND_TZ, notifyData});
			}
		}
		
		return new Object[]{AppErrorCode.SUCCESS, list.toArray()};
	}
	
	
	
	/**
	 * 购买基金计划
	 * @param userRoleId
	 * @param id
	 * @return
	 */
	public Object[] jijinPlan(Long userRoleId, Integer id){
		//单个投资
		TouZiConfig config = touZiConfigExportService.loadById(id,GameConstants.JIJING_TYPE);
		
		//判断是否有可投资
		if(config == null){
			return AppErrorCode.NO_FIND_CONFIG;
		}
		
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(role == null){
			return AppErrorCode.ROLE_NOTEXISTS;
		}
		
		//玩家投资数据
		Map<Integer, RoleTouzi> roleDataMap = getRoleTouziData(userRoleId,GameConstants.JIJING_TYPE);
		Object[] resultObj = null;
		int descYb = 0;
		
		//处理投资
		int kfDays = ServerInfoServiceManager.getInstance().getKaifuDays();
			
		//判断活动过期
		if(config.getStartDay() <= kfDays && (config.getEndDay() <= 0 || kfDays <= config.getEndDay())){
			//判断是否有投资
			if(roleDataMap == null || roleDataMap.size() <= 0 || roleDataMap.get(config.getId()) == null){
				
				//消耗元宝
				descYb = descYb + config.getGold();
				
				// [0:int(id投资id),1:[0:int(奖励领到第几天0-代表一天都没领),1:Number(投资这一天的起始时间戳),2:int(登录天数)]
				resultObj = new Object[]{config.getId(), GameSystemTime.getSystemMillTime()};
			}
		}
		
		//判断是否有可投资
		if(resultObj == null){
			return AppErrorCode.NO_TOUZI_PLAN;
		}
		
		//判断消耗的元宝是否满足
		if(descYb > 0){
			Object[] error = accountExportService.isEnought(GoodsCategory.GOLD, descYb, userRoleId);
			if(error != null){
				return error;
			}else{
				//消耗元宝
				accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, descYb, userRoleId, LogPrintHandle.CONSUME_TOUZI, true, LogPrintHandle.CBZ_TOUZI);
				if(PlatformConstants.isQQ()){
					BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,descYb,LogPrintHandle.CONSUME_TOUZI,QQXiaoFeiType.CONSUME_TOUZI,1});
				}
			}
		}
		
		Integer configId = Integer.parseInt(resultObj[0].toString());
		
		//创建投资数据
		createRoleTouzi(userRoleId, configId, Long.parseLong(resultObj[1].toString()),GameConstants.JIJING_TYPE);
		
		if(config != null){
			//发送公告
			Object[] notifyData = new Object[]{
					new Object[]{2, userRoleId, role.getName()},
					new Object[]{4, config.getClientItemMap()}
			};
			BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT6, new Object[]{AppErrorCode.NOTIFY_SEND_TZ, notifyData});
		}
		
		/**
		 * 如果是等级基金，那么在投资的时候就直接发放奖励了。
		 */
		if(config.getType() == GameConstants.LEVEL_JIJIN){
			Map<String, Integer> goods = config.getItemMap();
			//发放奖励
			String content = EmailUtil.getCodeEmail(AppErrorCode.ZHUANPAN_EAMIL);
			roleBagExportService.putInBagOrEmail(goods, userRoleId, GoodsSource.GOODS_GET_TOUZI, true, content);
		}
		
		return new Object[]{AppErrorCode.SUCCESS, resultObj};
	}
	
//	/**
//	 * 领取奖励
//	 * @param userRoleId
//	 * @param id
//	 * @return
//	 */
//	public Object[] recevieTouzi(Long userRoleId, Integer id){
//		Map<Integer, RoleTouzi> roleDataMap = new HashMap<>();
//		if(id == null){
//			
//			//如果为null则表示一键投资全部有效期之内的投资计划,否则为对应id的投资申请
//			roleDataMap = getRoleTouziData(userRoleId);
//			if(roleDataMap == null || roleDataMap.size() <= 0){
//				return AppErrorCode.CONFIG_ERROR;
//			}
//			
//		}else{
//			//单个投资
//			RoleTouzi config = getRoleTouzi(userRoleId, id);
//			if(config != null){
//				
// 				roleDataMap.put(id, config);
//			}
//		}
//		
//		//判断是否有奖励可领取
//		if(roleDataMap == null || roleDataMap.size() <= 0){
//			return AppErrorCode.NO_RECEIVE_ERROR;
//		}
//		
//		//判断配置
//		Map<Integer, TouZiConfig> map = getTouZiConfigMap();
//		if(map == null || map.size() <= 0){
//			return AppErrorCode.CONFIG_ERROR;
//		}
//		
//		List<RoleTouzi> list = new ArrayList<>();
//		//所有奖励集合
//		Map<String, Integer> itemMap = new HashMap<>();
//		
//		//处理奖励
//		for (RoleTouzi roleTouzi : roleDataMap.values()) {
//			
//			TouZiConfig config = map.get(roleTouzi.getConfigId());
//			if(config != null){
//				
//				//判断奖励天数是否大于登陆天数
//				int reDay = roleTouzi.getRecevieDay().intValue();
//				int lDay = roleTouzi.getTzType().intValue();
//				if(reDay < lDay){
//					
//					//判断奖励
//					Map<String, Integer> cMap = config.getItemMap(reDay, lDay);
//					if(cMap != null && cMap.size() > 0){
//						
//						config.getCovItemMap(itemMap, cMap);
//					}
//					
//					//获取的奖励
//					list.add(roleTouzi);
//				}
//			}
//		}
//		
//		//判断是否有奖励可领取
//		if(list == null || list.size() <= 0){
//			return AppErrorCode.NO_RECEIVE_ERROR;
//		}
//		
//		//验证背包是否足够
//		Object[] error = roleBagExportService.checkPutGoodsAndNumberAttr(itemMap, userRoleId);
//		if(error != null){
//			return error;
//		}else{
//			
//			//放入背包     
//			roleBagExportService.putGoodsAndNumberAttr(itemMap, userRoleId, GoodsSource.GOODS_GET_TOUZI, LogPrintHandle.GET_TOUZI, LogPrintHandle.GBZ_TOUZI, true);
//		}
//		
//		Object[] result = new Object[list.size()];
//		int i = 0;
//		//修改投资数据
//		for (RoleTouzi roleTouzi : list) {
//			
//			roleTouzi.setRecevieDay(roleTouzi.getTzType());
//			updateRoleTouzi(roleTouzi);
//			
//			result[i++] = roleTouzi.getConfigId();
//		}
//		
//		return new Object[]{AppErrorCode.SUCCESS, result};
//	}
	
	
	
	
//	/**
//	 * 领取奖励
//	 * @param userRoleId
//	 * @param id
//	 * @return
//	 */
//	public Object[] recevieJiJin(Long userRoleId, Integer id){
//		Map<Integer, RoleTouzi> roleDataMap = new HashMap<>();
//		if(id == null){
//			
//			//如果为null则表示一键投资全部有效期之内的投资计划,否则为对应id的投资申请
//			roleDataMap = getRoleTouziData(userRoleId,GameConstants.JIJING_TYPE);
//			if(roleDataMap == null || roleDataMap.size() <= 0){
//				return AppErrorCode.CONFIG_ERROR;
//			}
//			
//		}else{
//			//单个投资
//			RoleTouzi config = getRoleTouzi(userRoleId, id);
//			if(config != null){
//				
// 				roleDataMap.put(id, config);
//			}
//		}
//		
//		//判断是否有奖励可领取
//		if(roleDataMap == null || roleDataMap.size() <= 0){
//			return AppErrorCode.NO_RECEIVE_ERROR;
//		}
//		
//		//判断配置
//		Map<Integer, TouZiConfig> map = getTouZiConfigMap();
//		if(map == null || map.size() <= 0){
//			return AppErrorCode.CONFIG_ERROR;
//		}
//		
//		List<RoleTouzi> list = new ArrayList<>();
//		//所有奖励集合
//		Map<String, Integer> itemMap = new HashMap<>();
//		
//		//处理奖励
//		for (RoleTouzi roleTouzi : roleDataMap.values()) {
//			
//			TouZiConfig config = map.get(roleTouzi.getConfigId());
//			if(config != null){
//				
//				//判断奖励天数是否大于登陆天数
//				int reDay = roleTouzi.getRecevieDay().intValue();
//				int lDay = roleTouzi.getTzType().intValue();
//				if(reDay < lDay){
//					
//					//判断奖励
//					Map<String, Integer> cMap = config.getItemMap(reDay, lDay);
//					if(cMap != null && cMap.size() > 0){
//						
//						config.getCovItemMap(itemMap, cMap);
//					}
//					
//					//获取的奖励
//					list.add(roleTouzi);
//				}
//			}
//		}
//		
//		//判断是否有奖励可领取
//		if(list == null || list.size() <= 0){
//			return AppErrorCode.NO_RECEIVE_ERROR;
//		}
//		
//		//验证背包是否足够
//		Object[] error = roleBagExportService.checkPutGoodsAndNumberAttr(itemMap, userRoleId);
//		if(error != null){
//			return error;
//		}else{
//			
//			//放入背包     
//			roleBagExportService.putGoodsAndNumberAttr(itemMap, userRoleId, GoodsSource.GOODS_GET_TOUZI, LogPrintHandle.GET_TOUZI, LogPrintHandle.GBZ_TOUZI, true);
//		}
//		
//		Object[] result = new Object[list.size()];
//		int i = 0;
//		//修改投资数据
//		for (RoleTouzi roleTouzi : list) {
//			
//			roleTouzi.setRecevieDay(roleTouzi.getTzType());
//			updateRoleTouzi(roleTouzi);
//			
//			result[i++] = roleTouzi.getConfigId();
//		}
//		
//		return new Object[]{AppErrorCode.SUCCESS, result};
//	}
}