package com.junyou.bus.yabiao.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.stage.model.core.attribute.BaseAttributeType;
import com.junyou.stage.model.element.role.IRole;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.huoyuedu.enums.ActivityEnum;
import com.junyou.bus.huoyuedu.export.HuoYueDuExportService;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.resource.export.RoleResourceBackExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.bus.yabiao.YabiaoConstants;
import com.junyou.bus.yabiao.configure.export.BiaoCheShuaXinConfig;
import com.junyou.bus.yabiao.configure.export.BiaoCheShuaXinConfigExportService;
import com.junyou.bus.yabiao.configure.export.YaBiaoConfig;
import com.junyou.bus.yabiao.configure.export.YaBiaoConfigExportService;
import com.junyou.bus.yabiao.configure.export.YaBiaoJiangLiConfig;
import com.junyou.bus.yabiao.configure.export.YaBiaoJiangLiConfigExportService;
import com.junyou.bus.yabiao.dao.YabiaoDao;
import com.junyou.bus.yabiao.entity.Yabiao;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfigExportService;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.YabiaoPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.ScopeType;
import com.junyou.stage.model.element.biaoche.Biaoche;
import com.junyou.stage.model.element.role.Role;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.utils.active.ActiveUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.kernel.spring.container.DataContainer;

/**
 * 押镖Service
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-3-13 上午11:10:21 
 */
@Service
public class YabiaoService {
	@Autowired
	private YabiaoDao yabiaoDao;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private YaBiaoConfigExportService yaBiaoConfigExportService;
	@Autowired
	private YaBiaoJiangLiConfigExportService yaBiaoJiangLiConfigExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private DingShiActiveConfigExportService dingShiActiveConfigExportService;
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	
	@Autowired
	private BiaoCheShuaXinConfigExportService biaoCheShuaXinConfigExportService;
	
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	
	private static final String flag = "\\|";
	@Autowired 
	private HuoYueDuExportService huoYueDuExportService;
	@Autowired
	private RoleResourceBackExportService roleResourceBackExportService;
	
	
	
	
	private Object[] defaultYabiaoInfoObject = null;
	
	/**
	 * 判断当前是否有双倍押镖活动
	 * @return true:有
	 *//*
	private boolean isDoubleYabiaoActivity(){
		List<DingShiActiveConfig>  list = dingShiActiveConfigExportService.getConfig(ActivityType.DOUBLE_YABIAO.getVal());
		if(list == null || list.size() <= 0){
			return false;
		}
		for (DingShiActiveConfig config : list) {
			if(config.inHuoDongTime()){
				return true;
			}
		}
		
		return false;
	}
	*/
	/**
	 * 获取押镖信息
	 * @param userRoleId
	 * @return
	 */
	public Yabiao getYabiao(Long userRoleId){
		Yabiao yabiao = yabiaoDao.cacheLoad(userRoleId, userRoleId);
		if(yabiao != null){
			//不是同一天，清理数据
			if(!DateUtils.isSameDay(new Timestamp(yabiao.getUpdateTime()), new Timestamp(GameSystemTime.getSystemMillTime()))){
//				yabiao.setYbTimes(0);//押镖次数
//				yabiao.setJbTimes(0);//劫镖次数
//				updateYabiao(yabiao);
				yabiaoKuaTian(yabiao);
			}
		}
		return yabiao;
	}
	
	private void updateYabiao(Yabiao yabiao){
		yabiao.setUpdateTime(GameSystemTime.getSystemMillTime());
		yabiaoDao.cacheUpdate(yabiao, yabiao.getUserRoleId());
	}
	
	/**
	 * 获取玩家信息（在线玩家）
	 * @param userRoleId
	 * @return
	 */
	private RoleWrapper getRoleWrapper(Long userRoleId){
		return roleExportService.getLoginRole(userRoleId);
	}
	
	/**
	 * 创建镖车（未入库）
	 * @param userRoleId
	 * @return
	 */
	private Yabiao createYabiao(Long userRoleId){
		Yabiao yabiao = new Yabiao();
		
		yabiao.setJbTimes(0);
		yabiao.setYbTimes(0);
		yabiao.setCurrentBiaoChe(getYabiaoPublicConfig().getDefaultBiaoCheId());
		yabiao.setRefreshTimes(0);
		yabiao.setUserRoleId(userRoleId);
		yabiao.setUpdateTime(GameSystemTime.getSystemMillTime());
		yabiao.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		yabiao.setRenwuCount(0);
		
		return yabiao;
	}
	
	/**
	 * 获取押镖表配置
	 * @param configId
	 * @return
	 */
	private YaBiaoConfig getYabiaoConfigById(Integer configId){
		return yaBiaoConfigExportService.getYabiaoConfigById(configId);
	}
	
	/**
	 * 根据玩家的等级获取押镖奖励
	 * @param configId
	 * @param level
	 * @return
	 */
	private YaBiaoJiangLiConfig getYaBiaoJiangLiConfig(Integer configId, Integer level){
		return yaBiaoJiangLiConfigExportService.loadById(configId, level);
	}
	
	/**
	 * 获取押镖公共配置
	 * @return
	 */
	private YabiaoPublicConfig getYabiaoPublicConfig(){
		return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_YABIAO);
	}
	
	/**
	 * 检验物品的大类Id是否为押镖物品
	 * @param needItem
	 * @return true:是
	 */
	private boolean checkGoodsConfig(String needItem){
		List<GoodsConfig> list = goodsConfigExportService.loadById1(needItem);
		if(list == null || list.size() <= 0){
			return false;
		}
		
		for (GoodsConfig goodsConfig : list) {
			if(!(goodsConfig.getCategory() == GoodsCategory.DART_TO)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 镖车死亡劫镖者业务处理
	 * @param benefitRoleId 劫镖Id
	 * @param bRole 
	 * @param bcConfigId
	 * @param userRoleId
	 */
	private void bcDeadJbHandle(Long benefitRoleId, RoleWrapper bRole, Integer bcConfigId, RoleWrapper uRole){
		YabiaoPublicConfig ybPublicConfig = getYabiaoPublicConfig();
		if(ybPublicConfig == null){
			return;
		}
		
		YaBiaoConfig config = yaBiaoConfigExportService.getYabiaoConfigById(bcConfigId);
		String ybName = config == null ? LogPrintHandle.LINE_CHAR : config.getName();//镖车名字
		
		//劫镖者发送公告（劫镖成功公告）
		if(bRole != null && bRole.getName() != null){
			BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT2, new Object[]{
					AppErrorCode.XJ_BIAOCHE, 
					new Object[]{new Object[]{2, benefitRoleId, bRole.getName()}, new Object[]{2, uRole.getId(), uRole.getName()}, ybName}
					});
		}
		//判断劫镖次数
		Yabiao beneYB = getYabiao(benefitRoleId);
		if(beneYB != null && beneYB.getJbTimes() >= ybPublicConfig.getMaxJbTime()){
			//劫镖者发送公告（劫镖成功公告，但是次数已用完）
			if(bRole != null && bRole.getName() != null && uRole != null && uRole.getName() != null){
				BusMsgSender.send2One(benefitRoleId, ClientCmdType.NOTIFY_CLIENT_ALERT, new Object[]{
						AppErrorCode.NO_JB_BIAOCHE_EXP, 
						new Object[]{new Object[]{2,uRole.getId(), uRole.getName()}, ybName}
						});
			}
			return;
		}
		
		if(beneYB == null){
			beneYB = createYabiao(benefitRoleId);
			beneYB.setJbTimes(beneYB.getJbTimes() + 1);
			
			yabiaoDao.cacheInsert(beneYB, benefitRoleId);
		}else{
			
			beneYB.setJbTimes(beneYB.getJbTimes() + 1);
			updateYabiao(beneYB);
		}
		
		YaBiaoJiangLiConfig ybJlConfig = getYaBiaoJiangLiConfig(bcConfigId, uRole == null ? 0 : uRole.getLevel());
		long incrExp = 0l;
		Integer incrZhenqi = 0;
		Integer incrMoney = 0;
		long isD = ActiveUtil.getYabiaoBei();
		if(ybJlConfig != null){
			/*//双倍押镖活动
			if(isDoubleYabiaoActivity()){
				isD = 2l;
			}*/
			
			incrExp = (long) (ybJlConfig.getJiangexp() * ybPublicConfig.getJbExp() * isD);
			incrZhenqi = (int) (ybJlConfig.getJiangzhenqi() * ybPublicConfig.getJbExp() * isD);
			incrMoney = (int) (ybJlConfig.getJiangmoney() * ybPublicConfig.getJbExp() * isD);
		}
		
		//加经验
		if(incrExp > 0){
			roleExportService.incrExp(benefitRoleId, incrExp);
		}
		//加真气
		if(incrZhenqi > 0){
			roleExportService.addZhenqi(benefitRoleId, incrZhenqi);
		}
		//加money
		if(incrMoney > 0){
			accountExportService.incrCurrencyWithNotify(GoodsCategory.MONEY, incrMoney, benefitRoleId, LogPrintHandle.GET_YABIAO_MONEY, LogPrintHandle.GBZ_YABIAO_MONEY);
		}		
		//劫镖者发送公告（劫镖成功后，获取劫镖经验）
		if(bRole != null && bRole.getName() != null && uRole != null && uRole.getName() != null){
			BusMsgSender.send2One(benefitRoleId, ClientCmdType.NOTIFY_CLIENT_ALERT, new Object[]{
					AppErrorCode.JB_BIAOCHE_EXP, 
					new Object[]{new Object[]{2, uRole.getId(), uRole.getName()}, ybName, incrExp,incrZhenqi,incrMoney}
					});
		}
	}
	
	private Integer getCurrentBiaoche(Yabiao yabiao){
		if (yabiao.getCurrentBiaoChe() == null){
			return getYabiaoPublicConfig().getDefaultBiaoCheId();
		}else{
			return yabiao.getCurrentBiaoChe().intValue();
		}
	}
	
	/**
	 * 获取押镖信息
	 * @param userRoleId
	 * @return
	 */
	public Object[] getYabiaoInfo(Long userRoleId){
		Yabiao yabiao = getYabiao(userRoleId);
		if(yabiao != null){
			// [0:int(今日已押镖次数),1:int(今日已劫镖次数)]为null则没有数据
			return new Object[]{yabiao.getYbTimes(), yabiao.getJbTimes(),getCurrentBiaoche(yabiao)};
		}
		if(defaultYabiaoInfoObject == null){
			defaultYabiaoInfoObject = new Object[]{0,0,getYabiaoPublicConfig().getDefaultBiaoCheId()};
		}
		return defaultYabiaoInfoObject;
	}
	
	/**
	 * 接受押镖任务
	 * @param userRoleId
	 * @param configId 镖车配置Id
	 * @return
	 */
	public Object[] receiveYabiao(Long userRoleId){
		YabiaoPublicConfig ybPublicConfig = getYabiaoPublicConfig();
		if(ybPublicConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		//判断玩家等级
		RoleWrapper role = getRoleWrapper(userRoleId);
		if(role == null || ybPublicConfig.getNeedLevel() > role.getLevel()){
			return AppErrorCode.ROLE_LEVEL_NOT_ENOUGH;
		}
		
		Yabiao yabiao = getYabiao(userRoleId);
		//判断押镖次数
		if(yabiao != null && yabiao.getYbTimes() >= ybPublicConfig.getMaxYbTime()){
			return AppErrorCode.YB_MAX_TIMES;
		}
		
		//判断玩家当前是否正在进行押镖任务
		if(yabiao != null && yabiao.getState() == GameConstants.YJ_YB_STATE){
			return AppErrorCode.YABIAO_ING;
		}
		
		Integer configId = null;
		if(yabiao == null){
			configId = ybPublicConfig.getDefaultBiaoCheId();
		}else{
			configId = getCurrentBiaoche(yabiao);
		}
//		YaBiaoConfig ybConfig = yaBiaoConfigExportService.getYabiaoConfigById(configId);
		//判断押镖所需的道具是否足够
//		if(!CovertObjectUtil.isEmpty(ybConfig.getUseitem())){
//			
//			if(!checkGoodsConfig(ybConfig.getUseitem())){
//				return AppErrorCode.NO_YB_GOODS;
//			}
//			
//			Map<String, Integer> goods = new HashMap<>();
//			goods.put(ybConfig.getUseitem(), 1);
//			Object[] code = roleBagExportService.checkRemoveBagItemByGoodsTypes(goods, userRoleId);
//			if(code != null){
//				return code;
//			}else{
//				roleBagExportService.removeBagItemByGoodsTypes(goods, userRoleId, GoodsSource.YABIAO, true, true);
//			}
//		}
		
		//存数据
		if(yabiao == null){
			yabiao = createYabiao(userRoleId);
			yabiao.setBcId(configId);
			yabiao.setYbTimes(yabiao.getYbTimes() + 1);
			yabiao.setRenwuCount(yabiao.getRenwuCount()+1);
			yabiao.setState(GameConstants.YJ_YB_STATE);
			
			yabiaoDao.cacheInsert(yabiao, userRoleId);
		}else{
			yabiao.setBcId(configId);
			yabiao.setState(GameConstants.YJ_YB_STATE);
			yabiao.setYbTimes(yabiao.getYbTimes() + 1);
			yabiao.setRenwuCount(yabiao.getRenwuCount()+1);
			updateYabiao(yabiao);
		}
		
		//创建镖车
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.S_CREATE_BIAOCHE, configId);
		
		//成功[0:int(1),1:int(接取成功的镖车配置id)]
		//活跃度lxn
		huoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A24);
		//lxn:橙色镖车发送公告给其他人
		if(yabiao.getBcId()==5){
 			BusMsgSender.send2All(ClientCmdType.BIAOC_ORANGE_NOTICE,role.getName());
 			//橙镖成就
 			try {
 				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_CHENGBIAO, 1});
			} catch (Exception e) {
				ChuanQiLog.error("",e);
			}
 			//修炼任务
 			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.REFRESH_BIAOSHI_CHENGSE, null});
		}
		
		return new Object[]{AppErrorCode.SUCCESS, configId};
	}
	 
	/**
	 * 完成押镖任务（领取奖励）
	 * @param userRoleId
	 * @return
	 */
	public Object[] finishYabiao(Long userRoleId){
		//判断玩家是否有正在进行押镖任务
		Yabiao yabiao = getYabiao(userRoleId);
		if(yabiao == null){
			return AppErrorCode.NOT_YABIAO_ING;
		}
		
		//判断押镖任务是否接
		if(yabiao.getState() == GameConstants.WJ_YB_STATE){
			return AppErrorCode.NOT_YABIAO_ING;
		}
		
		//判断押镖任务是否已经完成
		if(yabiao.getState() == GameConstants.COMPLETE_YB_STATE){
			return AppErrorCode.YB_TASK_PLAY;
		}
		
		RoleWrapper role = getRoleWrapper(userRoleId);
		YaBiaoJiangLiConfig ybJlConfig = getYaBiaoJiangLiConfig(yabiao.getBcId(), role == null ? 0 : role.getLevel());
		if(ybJlConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		Biaoche biaoche = dataContainer.getData(GameConstants.COMPONENT_BIAOCHE_NMAE, userRoleId+"");
		if(biaoche == null){
			//成功[0:int(2)]镖车时间到了，任务自动置为失败
			return new Object[]{2};
		}
		int maxGz = getYabiaoPublicConfig().getMaxCell();
		String stageId = publicRoleStateExportService.getRolePublicStageId(userRoleId);
		IStage statge = StageManager.getStage(stageId);
		if(statge == null){
			return new Object[]{2};
		}
		Role stageRole = statge.getElement(userRoleId, ElementType.ROLE);
		if(!biaoche.getStage().equals(statge)){
			return AppErrorCode.BIAO_CHE_TOO_FAR;
		}
		if(!stageRole.getStage().inScope(stageRole.getPosition(), biaoche.getPosition(), maxGz, ScopeType.GRID)){
			return AppErrorCode.BIAO_CHE_TOO_FAR;
		}
		int isD = ActiveUtil.getYabiaoBei();
		/*//双倍押镖活动
		if(isDoubleYabiaoActivity()){
			isD = 2;
		}*/
		Integer jlExp = ybJlConfig.getJiangexp() * isD;
		Integer jlZhenqi = ybJlConfig.getJiangzhenqi() * isD;
		Integer jlMoney = ybJlConfig.getJiangmoney() * isD;
		//加经验
		if(jlExp > 0){
			roleExportService.incrExp(userRoleId, jlExp *  1l);
		}
		//加真气
		if(jlZhenqi > 0){
			roleExportService.addZhenqi(userRoleId, jlZhenqi);
		}
		//加money
		if(jlMoney > 0){
			accountExportService.incrCurrencyWithNotify(GoodsCategory.MONEY, jlMoney, userRoleId, LogPrintHandle.GET_YABIAO_MONEY, LogPrintHandle.GBZ_YABIAO_MONEY);
		}
		//修改数据
		yabiao.setCurrentBiaoChe(getYabiaoPublicConfig().getDefaultBiaoCheId());
		yabiao.setState(GameConstants.COMPLETE_YB_STATE);
		yabiao.setRefreshTimes(0);
		updateYabiao(yabiao);
		
		//通知场景删除镖车
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.S_BIAOCHE_CLEAN, "-1");

		//成功[0:int(1),1:String(劫镖者名字,无此值是表示未被劫镖),2:exp:(Number押镖获得的经验)]
		return new Object[]{AppErrorCode.SUCCESS, null, jlExp,jlZhenqi,jlMoney};
	}

	
	/**
	 * 镖车死亡
	 * @param userRoleId 押镖者id
	 * @param bcConfigId 镖车Id
	 * @param benefitRoleId 劫镖者Id
	 */
	public void yabiaoDead(Long userRoleId, Integer bcConfigId, Long benefitRoleId){
		//判断玩家是否有正在进行押镖任务
		Yabiao yabiao = getYabiao(userRoleId);
		if(yabiao == null){
			return;
		}
		
		//判断押镖任务是否接
		if(yabiao.getState() == GameConstants.WJ_YB_STATE){
			return;
		}
		
		//判断镖车任务是否已经完成
		if(yabiao.getState() == GameConstants.RECEIVE_YB_STATE){
			return;
		}
		
		//劫镖者并领取奖励发送公告
		RoleWrapper bRole = getRoleWrapper(benefitRoleId);//劫镖者
		RoleWrapper uRole = getRoleWrapper(userRoleId);//押镖者
		bcDeadJbHandle(benefitRoleId, bRole, bcConfigId, uRole);
		
		//修改数据
		yabiao.setState(GameConstants.WJ_YB_STATE);
		yabiao.setRefreshTimes(0);
		yabiao.setCurrentBiaoChe(getYabiaoPublicConfig().getDefaultBiaoCheId());
		updateYabiao(yabiao);
		
		//通知场景删除镖车
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.S_BIAOCHE_CLEAN, uRole == null ? null : uRole.getName());
		//通知劫镖者劫镖成功
		BusMsgSender.send2One(benefitRoleId, ClientCmdType.JIE_BIAOCHE, null);
		
		YaBiaoJiangLiConfig ybJlConfig = getYaBiaoJiangLiConfig(bcConfigId, uRole == null ? 0 : uRole.getLevel());
		//给被劫镖者的奖励
		Long incrExp = (long) (ybJlConfig.getJiangexp() * ActiveUtil.getYabiaoBei() * (1-getYabiaoPublicConfig().getJbExp()));
		Integer incrZhenqi =(int) ( ybJlConfig.getJiangzhenqi() * ActiveUtil.getYabiaoBei() * (1-getYabiaoPublicConfig().getJbExp()));
		Integer incrMoney = (int) (ybJlConfig.getJiangmoney() * ActiveUtil.getYabiaoBei()* (1-getYabiaoPublicConfig().getJbExp()));
		//加经验
		if(incrExp > 0){
			roleExportService.incrExp(uRole.getId(), incrExp);
		}
		//加真气
		if(incrZhenqi > 0){
			roleExportService.addZhenqi(uRole.getId(), incrZhenqi);
		}
		//加money
		if(incrMoney > 0){
			accountExportService.incrCurrencyWithNotify(GoodsCategory.MONEY, incrMoney, uRole.getId(), LogPrintHandle.GET_YABIAO_MONEY, LogPrintHandle.GBZ_YABIAO_MONEY);
		}			
		//押镖者发送公告
		String name = bRole == null ? null : bRole.getName();
		String leftPercent = (int)((1-getYabiaoPublicConfig().getJbExp())*100)+"%";
		if(name != null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.NOTIFY_CLIENT_ALERT, new Object[]{AppErrorCode.DJ_BIAOCHE, new Object[]{ new Object[]{2, benefitRoleId, name},leftPercent}});
		}
		//活跃度lxn
//		huoYueDuExportService.completeActivity(benefitRoleId, ActivityEnum.A25);  
		BusMsgSender.send2BusInner(benefitRoleId, InnerCmdType.INNER_HUOYUEDU_COUNT, new Object[] {
				ActivityEnum.A25, benefitRoleId});
		
		//lxn:劫镖成功,发邮件通知押镖人
		String title = EmailUtil.getCodeEmail(GameConstants.EMAIL_YABIAO_JIEBIAO_CODE_TITLE);
		String content = EmailUtil.getCodeEmail(GameConstants.EMAIL_YABIAO_JIEBIAO_CODE, bRole.getName(),leftPercent);
		emailExportService.sendEmailToOne(uRole.getId(),title, content, GameConstants.EMAIL_TYPE_SINGLE, null);
		
	}
	/**
	 * 修改镖车状态
	 */
	public void yabiaoStatus(Long userRoleId){
		//判断玩家是否有正在进行押镖任务
		Yabiao yabiao = getYabiao(userRoleId);
		if(yabiao == null){
			return;
		}
		
		//修改数据
		yabiao.setState(GameConstants.WJ_YB_STATE);
		updateYabiao(yabiao);

		//通知场景删除镖车
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.S_BIAOCHE_CLEAN, "-1");
	}
	
	public Object[] refreshBiaoche(Long userRoleId,Integer type){
		Integer biaocheId = null;
		Yabiao yabiao = getYabiao(userRoleId);
		if(yabiao == null){
			yabiao = createYabiao(userRoleId);
			yabiaoDao.cacheInsert(yabiao, userRoleId);
		}else{
			Integer currentBiaoche = getCurrentBiaoche(yabiao);
			if(currentBiaoche.intValue() == yaBiaoConfigExportService.getMaxYabiaoId()){
				return AppErrorCode.MAX_LEVEL_CAN_NOT_REFRESH;
			}
		}
		if(type.intValue() == YabiaoConstants.REFRESH_TYPE_3){
			Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, getYabiaoPublicConfig().getNeedgold(), userRoleId, LogPrintHandle.CONSUME_MAXLEVEL_BIAOCHE, true, LogPrintHandle.CBZ_MAXLEVEL_BIAOCHE);
			if(result != null){
				return result;
			}else{
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,getYabiaoPublicConfig().getNeedgold(),LogPrintHandle.CONSUME_MAXLEVEL_BIAOCHE,QQXiaoFeiType.CONSUME_MAXLEVEL_BIAOCHE,1});
			}
			biaocheId =  yaBiaoConfigExportService.getMaxYabiaoId();
		}else{
			//判断道具是否足够
			List<String> goodsIds = goodsConfigExportService.loadIdsById1(getYabiaoPublicConfig().getNeedId());
			String goodsId = null;
			Object[] checkResult = AppErrorCode.CONFIG_ERROR;
			for(String e:goodsIds){
				checkResult = roleBagExportService.checkRemoveBagItemByGoodsId(e, 1, userRoleId);
				if(checkResult == null){
					goodsId = e;
					break;
				}
			}
			if(goodsId == null){
				if(type.intValue() == YabiaoConstants.REFRESH_TYPE_2){
					return checkResult;
				}
				if(type.intValue() == YabiaoConstants.REFRESH_TYPE_1){
					int needBGold = getYabiaoPublicConfig().getNeedbgold();
					Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.BGOLD, needBGold, userRoleId, LogPrintHandle.CONSUME_REFRESH_BIAOCHE, true, LogPrintHandle.CBZ_REFRESH_BIAOCHE);
					if (result != null){
						int needGold = getYabiaoPublicConfig().getNeedgold1();
						result = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, needGold, userRoleId, LogPrintHandle.CONSUME_REFRESH_BIAOCHE, true, LogPrintHandle.CBZ_REFRESH_BIAOCHE);
						if (result != null){
							return result;
						}else{
							if(PlatformConstants.isQQ()){
								BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,getYabiaoPublicConfig().getNeedgold(),LogPrintHandle.CONSUME_REFRESH_BIAOCHE,QQXiaoFeiType.CONSUME_REFRESH_BIAOCHE,1});
							}
						}
					}else{
						if(PlatformConstants.isQQ()){
							BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_BYB,getYabiaoPublicConfig().getNeedgold(),LogPrintHandle.CONSUME_REFRESH_BIAOCHE,QQXiaoFeiType.CONSUME_REFRESH_BIAOCHE,1});
						} 
					}
				}
			}else{
				roleBagExportService.removeBagItemByGoodsId(goodsId, 1, userRoleId, GoodsSource.RERESH_BIAO_CHE, true, true);
			}
			if(yabiao.getRefreshTimes() != null && yabiao.getRefreshTimes().intValue() >= getYabiaoPublicConfig().getMaxcishu() - 1){
				biaocheId =  yaBiaoConfigExportService.getMaxYabiaoId();
			}else{
				int vip = roleVipInfoExportService.getRoleVipInfo(userRoleId)
						.getVipLevel().intValue();
				biaocheId = randomBiaoche(getCurrentBiaoche(yabiao), vip);
			}
		}
		if(yabiao.getRefreshTimes() == null){
			yabiao.setRefreshTimes(1);
		}else{
			yabiao.setRefreshTimes(yabiao.getRefreshTimes() + 1);
		}
		yabiao.setCurrentBiaoChe(biaocheId);
		updateYabiao(yabiao);
		//修炼任务
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.REFRESH_BIAOSHI, null});
		return new Object[]{1,biaocheId};
	}
	
	private Integer randomBiaoche(Integer currentBiaocheId,Integer vip){
		BiaoCheShuaXinConfig biaoCheShuaXinConfig = biaoCheShuaXinConfigExportService.getByVipAndRarelevel(vip, currentBiaocheId);
		return Lottery.getRandomKeyByInteger(biaoCheShuaXinConfig.getMap(), biaoCheShuaXinConfig.getTotal());
	}
	
	public void offilineHandler(Long userRoleId){
		Yabiao yabiao = getYabiao(userRoleId);
		if(yabiao != null && yabiao.getState() == GameConstants.YJ_YB_STATE){
			yabiao.setRefreshTimes(0);
			yabiao.setState(GameConstants.WJ_YB_STATE);
			yabiao.setCurrentBiaoChe(getYabiaoPublicConfig().getDefaultBiaoCheId());
			updateYabiao(yabiao);
		}
	}
	
	public Yabiao initYabiao(Long userRoleId) {
		List<Yabiao> list = yabiaoDao.initYabiao(userRoleId);
		if(list != null && list.size() > 0 ){
			return list.get(0);
		}
		return null;
	}
	
	

	//TODO ----以下方法供资源找回模块使用---------
	/**
	 * 计算押镖资源情况
	 */
	public void calDayFubenResource(Map<String,Map<String,Integer>> map,Long userRoleId){
		Yabiao yabiao = yabiaoDao.cacheLoad(userRoleId, userRoleId);
		if(yabiao == null){
			return;
		}
		calDayFubenResource(map, yabiao);
	}

	private void calDayFubenResource(Map<String, Map<String, Integer>> map,Yabiao yabiao) {
		int day = DatetimeUtil.twoDaysDiffence(yabiao.getUpdateTime());
		if(day < 1){
			return;
		}
		RoleWrapper role = getRoleWrapper(yabiao.getUserRoleId());
		if(role == null){
			return;
		}
		YabiaoPublicConfig publicConfig = getYabiaoPublicConfig();
		if(publicConfig == null){
			return;
		}
		YaBiaoJiangLiConfig config = getYaBiaoJiangLiConfig(GameConstants.RESOURCE_BACK_BIAOCHE_ID, role.getLevel());
		if(config == null){
			return;
		}
		if(day <= GameConstants.RESOURCE_BACK_MAX_DAY && yabiao.getYbTimes() > 0){
			int count = publicConfig.getMaxYbTime() - yabiao.getYbTimes();
			if(count > 0){
				Map<String,Integer> dayMap = new HashMap<>();
				dayMap.put(ModulePropIdConstant.EXP_GOODS_ID, config.getJiangexp() * count);
				dayMap.put(ModulePropIdConstant.MONEY_GOODS_ID, config.getJiangmoney() * count);
				dayMap.put(ModulePropIdConstant.MONEY_ZHENQI_ID, config.getJiangzhenqi() * count);
				map.put(day+"", dayMap);
			}
			day--;
		}else if(day > GameConstants.RESOURCE_BACK_MAX_DAY){
			day = GameConstants.RESOURCE_BACK_MAX_DAY;
		}
		
		if(day > 0){
			Map<String,Integer> dayMap = new HashMap<>();
			dayMap.put(ModulePropIdConstant.EXP_GOODS_ID, config.getJiangexp() * publicConfig.getMaxYbTime());
			dayMap.put(ModulePropIdConstant.MONEY_GOODS_ID, config.getJiangmoney() * publicConfig.getMaxYbTime());
			dayMap.put(ModulePropIdConstant.MONEY_ZHENQI_ID, config.getJiangzhenqi() * publicConfig.getMaxYbTime());
			for (; day > 0; day--) {
				map.put(day+"", new HashMap<>(dayMap));
			}
		}
		yabiao.setYbTimes(0);//押镖次数
		yabiao.setJbTimes(0);//劫镖次数
		updateYabiao(yabiao);
	}
	
	public void yabiaoKuaTian(Yabiao yabiao){
		try{
			Map<String,Map<String,Integer>> map = new HashMap<>();
			calDayFubenResource(map, yabiao);
			roleResourceBackExportService.changeTypeMap(yabiao.getUserRoleId(), map, GameConstants.RESOURCE_TYPE_YABIAO_ACTIVE);
		}catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}

    public int getBiaocStatu(Long userRoleId) {
		Biaoche biaoche = dataContainer.getData(GameConstants.COMPONENT_BIAOCHE_NMAE, userRoleId+"");
		if(biaoche == null){
			return -1;
		}

		if(biaoche.getBiaoceState() == 2 && System.currentTimeMillis() - biaoche.getLastHurtTime() <= 10*1000) {
			return 2;
		}else if(biaoche.isYuanLi()){
			return 3;
		}
		return 1;
    }
}