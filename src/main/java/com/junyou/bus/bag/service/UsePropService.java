package com.junyou.bus.bag.service;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.BagOutputWrapper;
import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.configure.export.DuoXuanLiXiangBiaoConfig;
import com.junyou.bus.bag.configure.export.DuoXuanLiXiangBiaoConfigExportService;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.bag.service.useprop.IUsePropCallBack;
import com.junyou.bus.bag.service.useprop.rule.IUsePropCheckRule;
import com.junyou.bus.bag.service.useprop.rule.UsePropCheckerFactory;
import com.junyou.bus.boss_jifen.service.export.RoleBossJifenExportService;
import com.junyou.bus.branchtask.BranchEnum;
import com.junyou.bus.branchtask.service.TaskBranchService;
import com.junyou.bus.chengshen.export.ChengShenExportService;
import com.junyou.bus.chibang.export.ChiBangExportService;
import com.junyou.bus.chongwu.service.export.ChongwuExportService;
import com.junyou.bus.huajuan.service.export.HuajuanExportService;
import com.junyou.bus.mogonglieyan.export.RoleMoGongLieYanExportService;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.qiling.export.QiLingExportService;
import com.junyou.bus.role.IncrRoleResp;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.role.export.TangbaoExportService;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoExportService;
import com.junyou.bus.shenqi.export.ShenQiExportService;
import com.junyou.bus.shizhuang.export.RoleShiZhuangExportService;
import com.junyou.bus.tianyu.export.TianYuExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.bus.wuqi.export.WuQiExportService;
import com.junyou.bus.wuxing.export.WuXingMoShenExportService;
import com.junyou.bus.xianjian.export.XianJianExportService;
import com.junyou.bus.xinwen.export.XinwenExportService;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.bus.yaoshen.service.export.YaoshenExportService;
import com.junyou.bus.yaoshen.service.export.YaoshenHunpoExportService;
import com.junyou.bus.yaoshen.service.export.YaoshenMoYinExportService;
import com.junyou.bus.zhanjia.export.ZhanJiaExportService;
import com.junyou.bus.zuoqi.export.ZuoQiExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.export.ExpConfig;
import com.junyou.gameconfig.export.ExpConfigExportService;
import com.junyou.gameconfig.export.ZuBaoConfigExportService;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.gameconfig.map.configure.export.MapConfigExportService;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.kuafu.manager.KuafuManager;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.prop.IProp;
import com.junyou.stage.model.prop.PropFactory;
import com.junyou.stage.model.prop.PropModel;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.tunnel.DirectMsgWriter;
import com.junyou.utils.KuafuConfigPropUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-3-4 下午3:14:04 
 */
@Service
public class UsePropService {
//	@Autowired
//	private BagService bagService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private GoodsUseLimitService goodsUseLimitService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private ZuoQiExportService zuoQiExportService;
	@Autowired
	private WuQiExportService wuQiExportService;
	@Autowired
	private ZuBaoConfigExportService zuBaoConfigExportService;
	@Autowired
	private ExpConfigExportService expConfigExportService;
	@Autowired
	private DuoXuanLiXiangBiaoConfigExportService duoXuanLiXiangBiaoConfigExportService;
	@Autowired
	private TangbaoExportService tangbaoExportService;
	@Autowired
	private MapConfigExportService mapConfigExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private RoleBusinessInfoExportService roleBusinessInfoExportService;
	@Autowired
	private ShenQiExportService shenQiExportService;
	@Autowired
	private ChiBangExportService chiBangExportService; 
	@Autowired
	private QiLingExportService qingLingExportService; 
	@Autowired
	private TianYuExportService tianYuExportService; 
	@Autowired
	private ChengShenExportService chengShenExportService; 
	@Autowired
	private RoleShiZhuangExportService roleShiZhuangExportService; 
	
	@Autowired
	private YaoshenExportService yaoshenExportService;
	@Autowired
	private YaoshenHunpoExportService yaoshenHunpoExportService;
	@Autowired
	private YaoshenMoYinExportService yaoshenMoYinExportService;
	@Autowired
	private XianJianExportService xianJianExportService;
	@Autowired
	private ZhanJiaExportService zhanJiaExportService;
	@Autowired
	private XinwenExportService xinwenExportService;
	@Autowired
	private HuajuanExportService huajuanExportService; 
	@Autowired
	private ChongwuExportService chongwuExportService;
    @Autowired
    private RoleVipInfoExportService roleVipInfoExportService;
    @Autowired
    private RoleMoGongLieYanExportService roleMoGongLieYanExportService;
    @Autowired
    private WuXingMoShenExportService wuXingMoShenExportService;
    @Autowired
    private RoleBossJifenExportService roleBossJifenExportService;
    @Autowired
    private TaskBranchService taskBranchService;
    
    
	public Object[] useBagItem(Long userRoleId, long goodsPkId, int count) {
		//根据slotId拉取物品
		RoleItemExport roleItem = roleBagExportService.getBagItemByGuid(userRoleId, goodsPkId);
		if(roleItem == null){
			return new Object[]{AppErrorCode.NOT_FOUND_GOOODS[1], goodsPkId};
		}
		
		//数量不足够
		if(count <= 0 || roleItem.getCount() < count){
			return new Object[]{AppErrorCode.ITEM_COUNT_ENOUGH[1], goodsPkId};
		}
		
		//过期道具判断
		if(roleItem.isExpireTime()){
			return new Object[]{AppErrorCode.IS_EXPIRE_GOODS[1], goodsPkId};
		}
		
		String stageId =  publicRoleStateExportService.getRolePublicStageId(userRoleId);
		//死亡状态不能使用道具 
		IRole role = getRole(stageId, userRoleId);
		if(role == null){
			return new Object[]{AppErrorCode.DEAD_NO_USE_PROP[1], goodsPkId};
		}
		
		//道具使用次数验证
		Object[] errorCode = goodsUseLimitService.checkXzcsUse(userRoleId, roleItem.getGoodsId(), count);
		if(errorCode != null){
			return errorCode;
		}
		
		//获取物品配置
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(roleItem.getGoodsId());
		if(goodsConfig == null){
			return new Object[]{AppErrorCode.NOT_FOUND_GOOODS[1], goodsPkId};
		}
		
		//验证物品使用cd
		String cd = goodsConfig.getCd();
		if(!ObjectUtil.strIsEmpty(cd)){
			if(role.getPublicCdManager().isCding(cd)){
				return new Object[]{AppErrorCode.GOODS_IS_IN_CD[1], goodsPkId};
			}
		}
		
		//使用道具需求等级不足
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if(roleWrapper == null || roleWrapper.getLevel() < goodsConfig.getLevelReq()){
			return new Object[]{AppErrorCode.ROLE_LEVEL_NOT_ENOUGH[1],goodsPkId};
		}
		
		//获取使用道具对应类型验证模板
		IUsePropCheckRule checkRole = UsePropCheckerFactory.getUsePropRoleByType(goodsConfig.getCategory());
		if(checkRole == null){
			return new Object[]{AppErrorCode.NO_FIND_CONFIG[1],goodsPkId};
		}
		IUsePropCallBack callback = new UsePropCallBack(stageId, userRoleId, goodsConfig, count);
		if(checkRole != null){
			checkRole.propHandle(callback);
		}
		//判断是否使用成功
		if(callback.getErrorCode() != null){
			return new Object[]{callback.getErrorCode()[1], goodsPkId};
		}
		
		//消耗物品，并打印日志
		BagSlots bagSlots=roleBagExportService.removeBagItemByGuid(goodsPkId, callback.getConsumeCount(), userRoleId, GoodsSource.USE_ITEM,true,true);
		if(!bagSlots.isSuccee()){
			return new Object[]{bagSlots.getErrorCode()[1], goodsPkId};
		}
		
		//添加物品使用次数
		goodsUseLimitService.useXzcsRecord(userRoleId, roleItem.getGoodsId(), callback.getConsumeCount());
		//物品进入冷却
		if(!ObjectUtil.strIsEmpty(cd)){
			role.getPublicCdManager().toCd(cd);
			BusMsgSender.send2One(userRoleId, ClientCmdType.ITEM_ENTER_CD, cd);
		}
		return null;
	}
	
	/**
	 * 获取场景角色
	 * @param stageId
	 * @param userRoleId
	 * @return	场景角色
	 */
	private IRole getRole(String stageId,Long userRoleId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return null;
		}
		
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return null;
		}
		
		if(role.getStateManager().isDead()){
			return null;
		}
		
		return role;
	}
	
	/**
	 * 使用物品回调
	 * @author LiNing
	 * @email anne_0520@foxmail.com
	 * @date 2015-3-4 下午2:31:49 
	 */
	private class UsePropCallBack implements IUsePropCallBack {

		private GoodsConfig goodsConfig;
		private int consumeCount;
		private Long userRoleId;
		private Object[] errorCode;
		private String stageId;
		
		public UsePropCallBack(String stageId, Long userRoleId,GoodsConfig goodsConfig,int goodsCount) {
			this.userRoleId = userRoleId;
			this.goodsConfig = goodsConfig;
			this.consumeCount = goodsCount;
			this.stageId = stageId;
		}
		
		public Integer getConsumeCount(){
			return consumeCount;
		}
		
		public Object[] getErrorCode(){
			return errorCode;
		}
		
		@Override
		public void addCurrency(int type) {
			long addVal = goodsConfig.getData1() * consumeCount;
			if(addVal > 0){
				addVal = accountExportService.incrCurrencyWithNotify(type, addVal, userRoleId, LogPrintHandle.GET_USE_PROP,LogPrintHandle.GBZ_USE_PROP);
				if(addVal == 0) errorCode = AppErrorCode.NUMBER_MAX;
			}
		}

//		@Override
//		public void addShenQiValue() {
//			int addVal = goodsConfig.getData1() * goodsCount;
//			if(addVal > 0){
//				errorCode = roleBusinessInfoExportService.incrSQJFWithNotify(addVal, userRoleId, LogPrintHandle.GET_USE_PROP, LogPrintHandle.GBZ_USE_PROP);
//			}
//		}

		@Override
		public void addRoleExpHandle() {
			long addVal = goodsConfig.getData1() * consumeCount;
			if(addVal > 0){
				addVal = roleExportService.incrExp(userRoleId, addVal).getIncrExp();
				if(addVal != 0) errorCode = AppErrorCode.NUMBER_MAX;
			}
		}

		@Override
		public void addHpPropHandle() {
			IStage stage = StageManager.getStage(stageId);
			if(stage == null){
				errorCode = AppErrorCode.STAGE_IS_NOT_EXIST;
				return;
			}
			
			IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
			if(role == null){
				errorCode = AppErrorCode.ROLE_IS_NULL;
				return;
			}
			
			//死亡状态不能使用
			if(role.getStateManager().isDead()){
				errorCode = new Object[]{0, AppErrorCode.DEAD_NO_USE_PROP};
				return;
			}
			
			int addHpVal = goodsConfig.getData1() * consumeCount;
			
			if(addHpVal > 0 && !role.getFightAttribute().isFullHp()){
				role.getFightAttribute().setCurHp(role.getFightAttribute().getCurHp() + addHpVal);
				if(KuafuManager.kuafuIng(userRoleId)){
					KuafuMsgSender.send2KuafuServer(userRoleId,userRoleId, InnerCmdType.INNER_KF_HUIFU_HP, addHpVal);
				}else{
					role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
				}
			}
		}
		@Override
		public void addHpPropHandleBaiFenBi() {
			IStage stage = StageManager.getStage(stageId);
			if(stage == null){
				errorCode = AppErrorCode.STAGE_IS_NOT_EXIST;
				return;
			}
			
			IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
			if(role == null){
				errorCode = AppErrorCode.ROLE_IS_NULL;
				return;
			}
			
			//死亡状态不能使用
			if(role.getStateManager().isDead()){
				errorCode = new Object[]{0, AppErrorCode.DEAD_NO_USE_PROP};
				return;
			}
			
			int addHpVal = (int) (role.getFightAttribute().getMaxHp() *  goodsConfig.getData1() / 100);
			
			if(addHpVal > 0 && !role.getFightAttribute().isFullHp()){
				if((role.getFightAttribute().getCurHp() + addHpVal) > role.getFightAttribute().getMaxHp()){
					role.getFightAttribute().setCurHp(role.getFightAttribute().getMaxHp());
				}else{
					role.getFightAttribute().setCurHp(role.getFightAttribute().getCurHp() + addHpVal);
				}
				if(KuafuManager.kuafuIng(userRoleId)){
					KuafuMsgSender.send2KuafuServer(userRoleId,userRoleId, InnerCmdType.INNER_KF_HUIFU_HP, addHpVal);
				}else{
					role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
				}
			}
		}

		@Override
		public void addZuoQiQNDHandle() {
			this.errorCode = zuoQiExportService.useZuoqiQND(userRoleId,consumeCount);
		}
		
		@Override
		public void addZuoQiCZDHandle() {
			this.errorCode = zuoQiExportService.addZuoQiCZD(userRoleId,consumeCount);
		}
		
		@Override
		public void addWuQiQNDHandle() {
			this.errorCode = wuQiExportService.useWuQiQND(userRoleId,consumeCount);
		}
		@Override
		public void addWuQiCZDHandle() {
			this.errorCode = wuQiExportService.addWuQiCZD(userRoleId,consumeCount);
		}
		
		@Override
		public void addChiBangQNDHandle() {
			this.errorCode = chiBangExportService.useChiBangQND(userRoleId,consumeCount);
		}

		@Override
		public void addChiBangCZDHandle() {
			this.errorCode = chiBangExportService.addChiBangCZD(userRoleId,consumeCount);
		}

		@Override
		public void addQiLingQNDHandle() {
			this.errorCode = qingLingExportService.useQiLingQND(userRoleId,consumeCount);
			
		}

		@Override
		public void addQiLingCZDHandle() {
			this.errorCode = qingLingExportService.addQiLingCZD(userRoleId,consumeCount);
			
		}
		
		@Override
		public void addTianYuQNDHandle() {
			this.errorCode = tianYuExportService.useTianYuQND(userRoleId,consumeCount);
			
		}
		
		@Override
		public void addTianYuCZDHandle() {
			this.errorCode = tianYuExportService.addTianYuCZD(userRoleId,consumeCount);
			
		}
		
		@Override
		public void addXianJianQNDHandle() {
			this.errorCode = xianJianExportService.useXianJianQND(userRoleId,consumeCount);
		}

		@Override
		public void addXianJianCZDHandle() {
			this.errorCode = xianJianExportService.addXianJianCZD(userRoleId,consumeCount);
		}
		@Override
		public void addZhanJiaQNDHandle() {
			this.errorCode = zhanJiaExportService.useXianJianQND(userRoleId,consumeCount);
		}
		
		@Override
		public void addZhanJiaCZDHandle() {
			this.errorCode = zhanJiaExportService.addXianJianCZD(userRoleId,consumeCount);
		}

		@Override
		public void tsXiaoGuoPropHandle() {
			IStage stage = StageManager.getStage(stageId);
			if(stage == null){
				errorCode = AppErrorCode.STAGE_IS_NOT_EXIST;
				return;
			}
			IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
			if(role == null){
				errorCode = AppErrorCode.ROLE_IS_NULL;
				return;
			}
			
			PropModel propModel = role.getPropModel();
			IProp prop = PropFactory.create(role, goodsConfig,consumeCount);
			if(propModel.add(prop)){
				//开启定时
				prop.start();
				
				if(!KuafuConfigPropUtil.isKuafuServer() && KuafuManager.kuafuIng(userRoleId)){
					//通知跨服服务器场景给玩家加Buff
					KuafuMsgSender.send2KuafuServer(userRoleId,userRoleId, InnerCmdType.PROP_EFFECT_NOTICE_KUAFU, new Object[]{goodsConfig.getId(),consumeCount});
				}else{
					prop.notifyPropChange();
				}
			}
			if(GoodsCategory.MANY_BEI_EXP == goodsConfig.getCategory()){
				//修炼任务
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.USE_JINGYANDAN, null});
			}
			
		}

        @Override
        public void openGiftCardPropHandler(int type) {
            // 消耗类型
            int needs = goodsConfig.getData1();
            int consumeType = needs == 0 ? GoodsCategory.GOLD : GoodsCategory.MONEY;
            // 消耗值
            Float consume = goodsConfig.getData2();
            int consumeVal = null == consume ? 0 : consume.intValue() * consumeCount;
            if (consumeVal > 0) {
                // 元宝或银两数量是否足够
                Object[] error = accountExportService.isEnoughtValue(consumeType, consumeVal, userRoleId);
                if (error != null) {
                    errorCode = error;
                    return;
                }
            }
            Map<String, Integer> goodsMap = null;
            if (type == GoodsCategory.FIXED_GIFT_CARD) {
                goodsMap = ConfigAnalysisUtils.getConfigMap(goodsConfig.getData3());
                if (goodsMap == null || goodsMap.isEmpty()) {
                    errorCode = AppErrorCode.CONFIG_ERROR;
                    return;
                }
                ObjectUtil.mapTimes(goodsMap, consumeCount);
                // 能否进入背包
                if (roleBagExportService.checkPutInBag(goodsMap, userRoleId) != null) {
                    errorCode = AppErrorCode.BAG_NOEMPTY;
                    return;
                }
            } else {
                // 预留一个空格
//                int emptySize = roleBagExportService.getBagEmptySize(userRoleId);
//                if (emptySize < consumeCount) {
//                    errorCode = AppErrorCode.BAG_NOEMPTY;
//                    return;
//                }
                goodsMap = new HashMap<>();
                for (int i = 0; i < consumeCount; i++) {
                    Object[] goods = zuBaoConfigExportService.componentRoll(goodsConfig.getData3());
                    String key = (String) goods[0];
                    int value = (Integer) goods[1];
                    if (goodsMap.containsKey(key)) {
                        goodsMap.put(key, goodsMap.get(key) + value);
                    } else {
                        goodsMap.put(key, value);
                    }
                }
                
                if (roleBagExportService.checkPutInBag(goodsMap, userRoleId) != null) {
                    errorCode = AppErrorCode.BAG_NOEMPTY;
                    return;
                }
            }
            // 消耗元宝
            if (consumeVal > 0) {
                accountExportService.decrCurrencyWithNotify(consumeType, consumeVal, userRoleId, LogPrintHandle.CONSUME_OPEN_GIFT, true, LogPrintHandle.CBZ_OPEN_GIFT);
                // 腾讯OSS消费上报
                if (PlatformConstants.isQQ()) {
                    if (consumeType == GoodsCategory.GOLD) {
                        BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_YB, consumeVal, LogPrintHandle.CONSUME_OPEN_GIFT, QQXiaoFeiType.CONSUME_OPEN_GIFT, 1 });
                    } else if (consumeType == GoodsCategory.BGOLD) {
                        BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_BYB, consumeVal, LogPrintHandle.CONSUME_OPEN_GIFT, QQXiaoFeiType.CONSUME_OPEN_GIFT, 1 });
                    }
                }
            }

            if (null != goodsMap) {
                // 进入背包
                roleBagExportService.putGoodsAndNumberAttr(goodsMap, userRoleId, GoodsSource.OPEN_GIFT_CARD, LogPrintHandle.GET_OPEN_GIFT, LogPrintHandle.GBZ_OPEN_GIFT, true);
            }
            
            if(goodsMap != null){
            	Object result = BagOutputWrapper.formart(goodsMap);
            	//推送961协议
                BusMsgSender.send2One(userRoleId, ClientCmdType.GOODS_SHOW_ZHANSHI, result);
            }
        }

		@Override
		public void addExpHandle() {
			roleExportService.incrExp(userRoleId, goodsConfig.getData1() * 1l * consumeCount);
		}

		@Override
		public void addPerExpHandle() {
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			int level = role.getLevel();
			ExpConfig config = expConfigExportService.loadById(level);
			if(config == null){
				errorCode = AppErrorCode.CONFIG_ERROR;
			}
			long exp = config.getNeedexp() * goodsConfig.getData1() / EffectType.getAttBase();
			for (int i = 0; i < consumeCount; i++) {
				IncrRoleResp incrRoleResp = roleExportService.incrExp(userRoleId, exp);
				if(incrRoleResp != null && incrRoleResp.isUpgrade()){
					level++;
					config = expConfigExportService.loadById(level);
					if(config == null){
						consumeCount = i + 1;
						break;
					}
					exp = config.getNeedexp() * goodsConfig.getData1() / EffectType.getAttBase();
				}
			}
			
		}

		@Override
		public void addZhenqiHandle() {
			long zhenqi = goodsConfig.getData1() * consumeCount;
			roleExportService.addZhenqi(userRoleId, zhenqi);
		}

		@Override
		public void tangbaoZizhiHandle() {
			Integer[] consume = new Integer[]{consumeCount};
			errorCode = tangbaoExportService.eatZizhiDan(userRoleId, consume);
			consumeCount = consume[0];
		}

		@Override
		public void tangbaoMXZXHandle() {
			Integer[] consume = new Integer[]{consumeCount};
			errorCode = tangbaoExportService.eatMeiXinZhiXue(userRoleId, consume);
			consumeCount = consume[0];
		}
		@Override
		public void tangbaoPuTiHandle(){
			Integer[] consume = new Integer[]{consumeCount};
			errorCode = tangbaoExportService.eatPuTi(userRoleId, consume, goodsConfig);
			consumeCount = consume[0];
			
			//支线统计
			taskBranchService.completeBranch(userRoleId, BranchEnum.B7, consumeCount);
		}

		@Override
		public void activeTangbao() {
			tangbaoExportService.activeTangbao(userRoleId);
		}
		@Override
		public void activeShenqi() {
			shenQiExportService.activateShenqiByItem(userRoleId, goodsConfig.getData1());
		}

		@Override
		public void townPortal() {
			int mapId = goodsConfig.getData1();
			MapConfig mapConfig = mapConfigExportService.load(mapId);
			
			if(mapConfig == null){
				errorCode = AppErrorCode.CONFIG_ERROR;
				return;
			}
			DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(mapId);
			if(dituCoinfig == null){
				errorCode = AppErrorCode.CONFIG_ERROR;
				return;
			}
			
			int[] birthXy = dituCoinfig.getRandomBirth();
			Object[] applyEnterData = new Object[]{mapId, birthXy[0], birthXy[1]};
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
		}

		@Override
		public void shengxiandan() {
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			int maxLevel = goodsConfig.getData1();
			int count = consumeCount;
			if(role.getLevel() < maxLevel){
				int level = maxLevel - role.getLevel();
				if(level > count){
					level = count;
				}
				count = count - level;
				roleExportService.addLevel(userRoleId, level);
			}
			if(count > 0){
				roleExportService.incrExp(userRoleId, goodsConfig.getData2().longValue() * count);
			}
			
		}

		@Override
		public void shuzuiyaoshui() {
			if(consumeCount > 1){
				for (int i = 0; i < consumeCount - 1; i++) {
					if(!roleBusinessInfoExportService.clearPkValue(userRoleId, goodsConfig.getData1(), false)){
						consumeCount = i;
						return;
					}
				}
			}
			
			if(!roleBusinessInfoExportService.clearPkValue(userRoleId, goodsConfig.getData1(), true)){
				consumeCount--;
			}
		}

		@Override
		public void chibangSj() {
			if(consumeCount>1){
				errorCode = AppErrorCode.NUMBER_ERROR;
				return;
			}
			//TODO 
			errorCode = chiBangExportService.sjByItem(userRoleId, goodsConfig.getData1(),goodsConfig.getData2().intValue(), Integer.parseInt(goodsConfig.getData3()));
		}
		
		@Override
		public void qilingSj() {
			if(consumeCount>1){
				errorCode = AppErrorCode.NUMBER_ERROR;
				return;
			}
			errorCode = qingLingExportService.sjByItem(userRoleId, goodsConfig.getData1(),goodsConfig.getData2().intValue(), Integer.parseInt(goodsConfig.getData3()));
		}
		@Override
		public void tianyuSj() {
			if(consumeCount>1){
				errorCode = AppErrorCode.NUMBER_ERROR;
				return;
			}
			errorCode = tianYuExportService.sjByItem(userRoleId, goodsConfig.getData1(),goodsConfig.getData2().intValue(), Integer.parseInt(goodsConfig.getData3()));
		}
		@Override
		public void yaoShenSj() {
			if(consumeCount>1){
				errorCode = AppErrorCode.NUMBER_ERROR;
				return;
			}
			errorCode = yaoshenExportService.sjByItem(userRoleId, goodsConfig.getData1(),goodsConfig.getData2().intValue(), Integer.parseInt(goodsConfig.getData3()));
		}
		
		@Override
		public void wuXingSj() {
			if(consumeCount>1){
				errorCode = AppErrorCode.NUMBER_ERROR;
				return;
			}
			errorCode = wuXingMoShenExportService.sjByItem(userRoleId, goodsConfig.getCategory(),goodsConfig.getData1(),goodsConfig.getData2().intValue(), goodsConfig.getData3());
		}
		
		@Override
		public void yaoShenMoWenSj() {
			if(consumeCount>1){
				errorCode = AppErrorCode.NUMBER_ERROR;
				return;
			}
			errorCode = yaoshenExportService.sjByItemMoWen(userRoleId, goodsConfig.getData1(),goodsConfig.getData2().intValue(), Integer.parseInt(goodsConfig.getData3()));
		}
		@Override
		public void yaoShenHunPoSj() {
			if(consumeCount>1){
				errorCode = AppErrorCode.NUMBER_ERROR;
				return;
			}
			errorCode = yaoshenHunpoExportService.sjByItem(userRoleId, goodsConfig.getData1(),goodsConfig.getData2().intValue(), Integer.parseInt(goodsConfig.getData3()));
		}
		@Override
		public void tangBaoXinWenSj() {
			if(consumeCount>1){
				errorCode = AppErrorCode.NUMBER_ERROR;
				return;
			}
			errorCode = xinwenExportService.sjByItem(userRoleId, goodsConfig.getData1(),goodsConfig.getData2().intValue(), Integer.parseInt(goodsConfig.getData3()));
		}
		@Override
		public void yaoShenMoYinSj() {
			if(consumeCount>1){
				errorCode = AppErrorCode.NUMBER_ERROR;
				return;
			}
			errorCode = yaoshenMoYinExportService.sjByItem(userRoleId, goodsConfig.getData1(),goodsConfig.getData2().intValue(), Integer.parseInt(goodsConfig.getData3()));
		}
		
		@Override
		public void addXiuwei(){
			long value = consumeCount * 1l * goodsConfig.getData1();
			roleBusinessInfoExportService.addXiuwei(userRoleId, value);
		}
		
		@Override
		public void activateHuajuan() {
			if(consumeCount>1){
				errorCode = AppErrorCode.HUAJUAN_CAN_NOT_BATCH;
				return;
			}
			errorCode = huajuanExportService.activateHuajuan(userRoleId, goodsConfig.getData2().intValue());
		}

		
		@Override
		public void addShenHunDanHandle() {
			errorCode = chengShenExportService.useChengShenSHD(userRoleId, goodsConfig.getData1(),consumeCount);
		}

		@Override
		public void addYaoshenQNDHandle() {
			errorCode = yaoshenExportService.useYaoshenQND(userRoleId,consumeCount);
			
		}
		
		@Override
		public void addYaoshenCZDHandle() {
			errorCode = yaoshenExportService.addYaoshenCZD(userRoleId,consumeCount);
		}
		@Override
		public void addYaoshenMowenQNDHandle() {
			
			errorCode = yaoshenExportService.addYaoshenMowenQND(userRoleId,consumeCount);
		}

		@Override
		public void addYaoshenMowenCZDHandle() {
			errorCode = yaoshenExportService.addYaoshenMowenCZD(userRoleId,consumeCount);
			
		}

		@Override
		public void addYaoshenHunpoQNDHandle() {
			errorCode = yaoshenHunpoExportService.useHunpoQND(userRoleId,consumeCount);
		}

		@Override
		public void addYaoshenHunpoCZDHandle() {
			errorCode = yaoshenHunpoExportService.useHunpoCZD(userRoleId,consumeCount);
		}
		
		@Override
		public void addYaoshenMoYinCZDHandle() {
			errorCode = yaoshenMoYinExportService.useCZD(userRoleId,consumeCount);
		}
		
		@Override
		public void addYaoshenMoYinQNDHandle() {
			errorCode = yaoshenMoYinExportService.useQND(userRoleId,consumeCount);
		}
		
		@Override
		public void tbZhanjiaSj() {
			if(consumeCount>1){
				errorCode = AppErrorCode.NUMBER_ERROR;
				return;
			}
			errorCode  =  zhanJiaExportService.sjByItem(userRoleId, goodsConfig.getData1(),goodsConfig.getData2().intValue(), Integer.parseInt(goodsConfig.getData3()));
			
		}
		@Override
		public void tbWuqiSj() {
			if(consumeCount>1){
				errorCode = AppErrorCode.NUMBER_ERROR;
				return;
			}
			errorCode  =  xianJianExportService.sjByItem(userRoleId, goodsConfig.getData1(),goodsConfig.getData2().intValue(), Integer.parseInt(goodsConfig.getData3()));
			
		}
		@Override
		public void zuoqiSj() {
			if(consumeCount>1){
				errorCode = AppErrorCode.NUMBER_ERROR;
				return;
			}
			//TODO  这个功能暂时不开放
			errorCode  =  zuoQiExportService.sjByItem(userRoleId, goodsConfig.getData1(),goodsConfig.getData2().intValue(), Integer.parseInt(goodsConfig.getData3()));
			
		}
		
		public void xianshiShizhuang(){
			if(consumeCount != 1){
				errorCode = AppErrorCode.NUMBER_ERROR;
			}
			errorCode = roleShiZhuangExportService.activeXianshiShizhuang(userRoleId, goodsConfig.getData1(), goodsConfig.getData2().intValue());
		}

		@Override
		public void addChongwuExp() {
			errorCode = chongwuExportService.addChongwuExp(userRoleId, Long.parseLong(goodsConfig.getData3())*consumeCount);
			
		}

		public void addVipExpHandle(){
		    long addVipExp = 1L * consumeCount * goodsConfig.getData1();
		    roleVipInfoExportService.rechargeVipExp(userRoleId, addVipExp);
		}

        @Override
        public void addMglyJinghuaHandler() {
            long addJinghua = 1L * consumeCount * goodsConfig.getData1();
            errorCode = roleMoGongLieYanExportService.addMglyJinghua(userRoleId, addJinghua);
        }

		@Override
		public void addBossJifen() {
			long addJifen = 1L * consumeCount * goodsConfig.getData1();
			errorCode = roleBossJifenExportService.addJifen(userRoleId,addJifen);
		}

		@Override
		public void wuqiSj() {
			if(consumeCount>1){
				errorCode = AppErrorCode.NUMBER_ERROR;
				return;
			}
			//TODO  这个功能暂时不开放
			errorCode  =  wuQiExportService.sjByItem(userRoleId, goodsConfig.getData1(),goodsConfig.getData2().intValue(), Integer.parseInt(goodsConfig.getData3()));
		}
	}
	

	/**
	 * 领取宝箱奖励
	 * @param userRoleId
	 * @param id
	 * @param paixu
	 * @return
	 */
	public Object[] lingQuBaoXiang(Long userRoleId, Long guId, Integer paixu) {
		//根据slotId拉取物品
		RoleItemExport roleItem = roleBagExportService.getBagItemByGuid(userRoleId, guId);
		if(roleItem == null){
			return AppErrorCode.NOT_FOUND_GOOODS;
		}
		
		if(roleItem.isExpireTime()){
			return new Object[]{0, AppErrorCode.IS_EXPIRE_GOODS};
		}
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(roleItem.getGoodsId());
		if(goodsConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		if(goodsConfig.getCategory() != GoodsCategory.DUOXUAN_BOX){
			return AppErrorCode.CONFIG_ERROR;
		}
		DuoXuanLiXiangBiaoConfig config = duoXuanLiXiangBiaoConfigExportService.loadById(goodsConfig.getData1());
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		//道具使用次数验证
		Object[] errorCode = goodsUseLimitService.checkXzcsUse(userRoleId, roleItem.getGoodsId(), 1);
		if(errorCode != null){
			return errorCode;
		}
		int hb = config.getGoldMap().get(paixu);//需消费的货币
		Integer type = config.getMoneytypeMap().get(paixu);//需要消耗货币的类型
		if(type == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//货币是否足够
		Object[] error = accountExportService.isEnought(type, hb, userRoleId);
		if(error != null){
			return error;
		}
		//检查物品可否添加到背包
		Object[] result = roleBagExportService.checkPutInBag(config.getConfigMap().get(paixu), userRoleId);
		if(result != null){
			return result;
		}
		//消耗物品，并打印日志
		BagSlots bagSlots=roleBagExportService.removeBagItemByGuid(guId, 1, userRoleId, GoodsSource.USE_ITEM,true,true);
		if(!bagSlots.isSuccee()){
		}
		//消耗元宝
		accountExportService.decrCurrencyWithNotify(type, hb, userRoleId, LogPrintHandle.CONSUME_LQDXBX, true, LogPrintHandle.CBZ_LQDXBX);
		//腾讯OSS消费上报
		if(PlatformConstants.isQQ()){
			if(type == GoodsCategory.GOLD){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,hb,LogPrintHandle.CONSUME_LQDXBX,QQXiaoFeiType.CONSUME_LQDXBX,1});
			}else if(type == GoodsCategory.BGOLD){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_BYB,hb,LogPrintHandle.CONSUME_LQDXBX,QQXiaoFeiType.CONSUME_LQDXBX,1});
			}
		}
		roleBagExportService.putInBag(config.getConfigMap().get(paixu), userRoleId, GoodsSource.DXLX_GET, true);
		//添加物品使用次数
		goodsUseLimitService.useXzcsRecord(userRoleId, roleItem.getGoodsId(), 1);
		
		return new Object[]{AppErrorCode.SUCCESS,paixu};
	}
	

}
