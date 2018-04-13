/**
 * 
 */
package com.junyou.stage.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.ContainerType;
import com.junyou.bus.bag.utils.BagUtil;
import com.junyou.bus.chenghao.configure.export.ChengHaoPeiZhiConfig;
import com.junyou.bus.chenghao.configure.export.ChengHaoPeiZhiConfigExportService;
import com.junyou.bus.chengshen.export.ChengShenExportService;
import com.junyou.bus.chibang.entity.ChiBangInfo;
import com.junyou.bus.chibang.manage.ChiBang;
import com.junyou.bus.chibang.util.ChiBangUtil;
import com.junyou.bus.equip.export.EquipExportService;
import com.junyou.bus.qiling.entity.QiLingInfo;
import com.junyou.bus.qiling.manage.QiLing;
import com.junyou.bus.qiling.util.QiLingUtil;
import com.junyou.bus.shenqi.ShenQiConstants;
import com.junyou.bus.shenqi.configure.export.ShenQiShuXingConfig;
import com.junyou.bus.shenqi.configure.export.ShenQiShuXingConfigExportService;
import com.junyou.bus.shenqi.configure.export.ShuXingZhuFuConfig;
import com.junyou.bus.shenqi.configure.export.ShuXingZhuFuConfigExportService;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.territory.entity.Territory;
import com.junyou.bus.territory.export.TerritoryExportService;
import com.junyou.bus.tianyu.entity.TianYuInfo;
import com.junyou.bus.tianyu.manage.TianYu;
import com.junyou.bus.tianyu.util.TianYuUtil;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.entity.VipConfig;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.bus.vip.export.VipConfigExportService;
import com.junyou.bus.wuqi.WuQiUtil;
import com.junyou.bus.wuqi.configure.export.XinShengJianJiChuConfig;
import com.junyou.bus.wuqi.entity.WuQiInfo;
import com.junyou.bus.wuqi.manage.WuQi;
import com.junyou.bus.xianjian.entity.XianJianInfo;
import com.junyou.bus.xianjian.manage.XianJian;
import com.junyou.bus.xianjian.util.XianjianUtil;
import com.junyou.bus.xinwen.export.XinwenExportService;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.bus.yaoshen.constants.YaoshenConstants;
import com.junyou.bus.yaoshen.service.export.YaoshenExportService;
import com.junyou.bus.yaoshen.service.export.YaoshenHunpoExportService;
import com.junyou.bus.yaoshen.service.export.YaoshenMoYinExportService;
import com.junyou.bus.zhanjia.entity.ZhanJiaInfo;
import com.junyou.bus.zhanjia.manage.ZhanJia;
import com.junyou.bus.zhanjia.util.ZhanJiaUtil;
import com.junyou.bus.zhuansheng.configure.ZhuanShengConfig;
import com.junyou.bus.zhuansheng.configure.ZhuanShengConfigExportService;
import com.junyou.bus.zuoqi.ZuoQiUtil;
import com.junyou.bus.zuoqi.configure.export.YuJianJiChuConfig;
import com.junyou.bus.zuoqi.entity.ZuoQiInfo;
import com.junyou.bus.zuoqi.manage.ZuoQi;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.dazuo.configure.export.DaZuoConfig;
import com.junyou.gameconfig.export.ExpConfig;
import com.junyou.gameconfig.export.ExpConfigExportService;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.gameconfig.monster.configure.export.MonsterExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.KuafuYunGongPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.PKPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.WenquanPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.io.export.SessionManagerExportService;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.guild.configure.GuildConfigService;
import com.junyou.public_.guild.entity.GuildConfig;
import com.junyou.public_.guild.export.GuildExportService;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.StageOutputWrapper;
import com.junyou.stage.configure.HcZhengBaSaiConfig;
import com.junyou.stage.configure.TerritoryConfig;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.configure.export.impl.HcZhengBaSaiConfigExportService;
import com.junyou.stage.configure.export.impl.PkDebuffConfig;
import com.junyou.stage.configure.export.impl.PkDebuffConfigExportService;
import com.junyou.stage.configure.export.impl.TerritoryConfigExportService;
import com.junyou.stage.dao.RoleInfoDao;
import com.junyou.stage.model.core.attribute.BaseAttributeType;
import com.junyou.stage.model.core.element.IElement;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.OtherRolesSearchFilter;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.ScopeType;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.state.StateEventType;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.chongwu.Chongwu;
import com.junyou.stage.model.element.goods.Goods;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.stage.model.element.monster.ai.IAi;
import com.junyou.stage.model.element.pet.Pet;
import com.junyou.stage.model.element.pet.PetFactory;
import com.junyou.stage.model.element.role.BusinessData;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.element.role.Role;
import com.junyou.stage.model.element.role.RoleFactoryHelper;
import com.junyou.stage.model.element.role.business.Equip;
import com.junyou.stage.model.element.role.business.RoleEquipData;
import com.junyou.stage.model.fight.zhanli.ZhanLiCalculateHelper;
import com.junyou.stage.model.skill.SkillManager;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.JianzhongFubenStage;
import com.junyou.stage.model.stage.kuafudianfeng.KuafuDianFengStage;
import com.junyou.stage.model.stage.kuafuyungong.KuafuYunGongStage;
import com.junyou.stage.model.stage.wenquan.WenquanManager;
import com.junyou.stage.model.state.AiBackState;
import com.junyou.stage.model.state.DaZuoState;
import com.junyou.stage.model.state.ZuoQiState;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.stage.tunnel.DirectMsgWriter;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.stage.utils.StageHelper;
import com.junyou.utils.KuafuConfigPropUtil;
import com.junyou.utils.active.ActiveUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.spring.container.DataContainer;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-11-29下午3:25:30
 */
@Service
public class RoleBehaviourService{
	
	@Autowired
	private MonsterExportService monsterExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private ExpConfigExportService expConfigExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private StageControllExportService stageControllExportService;
	@Autowired
	private RoleInfoDao roleInfoDao; 
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private EquipExportService equipExportService;
	@Autowired
	private GuildConfigService guildConfigService;
	@Autowired
	private VipConfigExportService vipConfigExportService;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	@Autowired
	private ShenQiShuXingConfigExportService shenQiShuXingConfigExportService;
	@Autowired
	private ShuXingZhuFuConfigExportService shuXingZhuFuConfigExportService;
	@Autowired
	private PkDebuffConfigExportService pkDebuffConfigExportService;
	@Autowired
	private TerritoryExportService territoryExportService;
	@Autowired
	private TerritoryConfigExportService territoryConfigExportService;
	@Autowired
	private ChengHaoPeiZhiConfigExportService chengHaoPeiZhiConfigExportService;
	@Autowired 
	private YaoshenExportService yaoshenExportService;
	@Autowired 
	private XinwenExportService xinwenExportService;
	@Autowired 
	private YaoshenHunpoExportService yaoshenHunpoExportService;
	@Autowired 
	private YaoshenMoYinExportService yaoshenMoYinExportService;
	@Autowired
	private GuildExportService guildExportService;
	@Autowired
	private HcZhengBaSaiConfigExportService hcZhengBaSaiConfigExportService;
	@Autowired
	private SessionManagerExportService sessionManagerExportService;
	@Autowired
	private ChengShenExportService chengShenExportService;
	@Autowired
	private ZhuanShengConfigExportService zhuanShengConfigExportService;
	
	
	
	public void move(String stageId,Long userRoleId, int x, int y) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId,ElementType.ROLE);
		if(null != role && !role.getStateManager().isDead()){
			if(!StageType.isSafeStage(stage.getStageType())){//安全场景不拉回
				boolean isCanMove = role.getStateManager().isDead() || stage.isScopeGeZi(role.getPosition(),x ,y,GameConstants.MOVE_CHECK_MAX_GZ);
				boolean dfCanMove = true;
				if(stage.getStageType().equals(StageType.KUAFUDIANFENG)){
				    KuafuDianFengStage dfStage = (KuafuDianFengStage) stage;
				    dfCanMove = dfStage.isCanAttack();
				}
				//人物不可移动
				if(!isCanMove || role.getStateManager().isForbidden(StateEventType.MOVE) || !dfCanMove){
					StageMsgSender.send2One(userRoleId, ClientCmdType.BEHAVIOR_MOVE_ERROR, new Object[]{role.getPosition().getX(),role.getPosition().getY()});
					return;
				}
			}else if(x > 100 || y > 200){
				ChuanQiLog.error("{}在安全场景内移动,目标坐标[{}:{}]",userRoleId,x,y);
				x = 46;
				y = 80;
				//非法移动坐标，重置目标点
			}
			
			//移动打断隐身
			yinShenHandle(role);
			
			stage.moveTo(role,x,y);
			
			//移动至怪物视野范围内增加怪物仇恨
			hatredHandle(role,stage);
			
			//角色进入安全区,清除角色仇恨
			safeClearRoleHatred(role);
			
			daZuoCancelHandle((Role)role);
			
			//消息发送
			StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.BEHAVIOR_MOVE, role.getMoveData());
		}
		
		
	}
	public void jump(String stageId,Long userRoleId, int eX, int eY) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId,ElementType.ROLE);
		if(null != role && !role.getStateManager().isDead()){
			//如果跳闪值不够,则不能跳跃
			int tiaoShan = role.getBusinessData().getTiaoShan();
			if(tiaoShan <= 0){
				//业务处理:TODO
			}
			if(!StageType.isSafeStage(stage.getStageType())){//安全场景不拉回
				boolean isCanMove = stage.isCanJump() && stage.isScopeGeZi(role.getPosition(),eX ,eY, GameConstants.JUMP_CHECK_MAX_GZ);
				//人物不可移动
				if(!isCanMove || role.getStateManager().isForbidden(StateEventType.MOVE)){
					StageMsgSender.send2One(userRoleId, ClientCmdType.BEHAVIOR_MOVE_ERROR, new Object[]{role.getPosition().getX(),role.getPosition().getY()});
					return;
				}
			}else if(eX < 0 || eY < 0 || eX > 100 || eY > 200){
				ChuanQiLog.error("{}在安全场景内跳跃,目标坐标[{}:{}]",userRoleId,eX,eY);
				eX = 46;
				eY = 80;
				//非法跳跃坐标，重置目标点
			}
			yinShenHandle(role);
			stage.moveTo(role,eX,eY);
			hatredHandle(role,stage);
			
			//角色进入安全区,清除角色仇恨
			safeClearRoleHatred(role);
			
			//消息发送 
			StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition(),new OtherRolesSearchFilter(role.getId())), ClientCmdType.BEHAVIOR_JUMP_TO, new Object[]{role.getId(),eX,eY});
 
			//开启time时间结束后,然后移除跳闪状态
			tiaoShanStageTask((Role)role, GameConstants.JUMP_TIME);
			//取消打坐
			daZuoCancelHandle((Role)role);
		}
		
		
	}
	
	private void tiaoShanStageTask(Role role,int time){
		role.getBusinessData().setJump(true);
		
		StageTokenRunable task = new StageTokenRunable(role.getId(), role.getStage().getId(), InnerCmdType.STAGE_TIAOSHAN, null);
		role.getScheduler().schedule(role.getId().toString(), GameConstants.COMPONENT_JUMP_STAGE, task, time, TimeUnit.MILLISECONDS);
		
	}
	
	
	/**
	 * 角色进入安全区,清除角色仇恨
	 * @param role
	 */
	private void safeClearRoleHatred(IRole role){
		if(role.getStage() == null){
			ChuanQiLog.error("玩家{}当前没有场景",role.getId());
			return;
		}
		boolean isClear = role.getStage().isNoAttackePoint(role.getPosition().getX(), role.getPosition().getY());
		if(isClear){
			role.getHatredManager().clear();
		}
	}
	
	/**
	 * 隐身业务处理 移到打断隐身
	 * @param role
	 */
	private void yinShenHandle(IRole role){
		if(role.getStateManager().isYinShen()){
			role.getStateManager().remove(StateType.YINSHEN);
		}
	}
	

	/**
	 * 移动相关事件处理(激活怪物仇恨)
	 */
	private void hatredHandle(IRole role,IStage stage) {
		/*	//如果是在塔防副本，角色移动不处理仇恨
		if(stage instanceof TFFubenStage){
			return;
		}
		*/
		//移动到主动怪物的视野内，会触发主动怪物仇恨增加
		Collection<IStageElement> aroundMonsters = stage.getAroundEnemies(role);
		if(null != aroundMonsters && aroundMonsters.size() > 0){
			
			for(IStageElement tmp : aroundMonsters){
				IMonster fither = (IMonster)tmp;
				
				if(!ElementType.isMonster(fither.getElementType()) && fither.getStateManager().isDead()){
					continue;
				}
				
				IMonster monster = (IMonster)tmp;
				MonsterConfig monsterConfig = monsterExportService.load(monster.getMonsterId());
				
				//角色移动攻击红名怪物不处理仇恨
				if(monsterConfig.isAttriackRedRold() || monsterConfig.getIfactive()){
					continue;
				}
				
				int eyeshot = monsterConfig.getEyeshot();//视野
				
				if(stage.inScope(role.getPosition(), monster.getPosition(), eyeshot,ScopeType.GRID)){
					
					//判断目标是否在仇恨列表内
//					if(!monster.getHatredManager().checkTargetHatred(role)){
//					}
					monster.getHatredManager().addActiveHatred(role,1);
					monster.getFightStatistic().flushChanges( DirectMsgWriter.getInstance() );
//					if(monster.getStateManager().contains(StateType.XUNLUO)){
//						//宠物仇恨
//						Pet pet = role.getPet();
//						if(pet != null){
//							//伙伴死亡
//							if(pet.getStateManager().isDead()){
//								continue;
//							}
//							if(pet.getPosition() == null || monster.getPosition() == null){
//								continue;
//							}
//							
//							if(stage.inScope(pet.getPosition(), monster.getPosition(), monsterConfig.getMaxrange(),ScopeType.GRID)){
//								monster.getHatredManager().addActiveHatred(pet,1);
//							}
//							
//						}
//					}
				}
				
			}
			
		}
		
		//宠物跟随
		Pet pet = role.getPet();
		if(pet != null && pet.getStateManager().contains(StateType.XUNLUO)){
			//如果宠物在巡逻状态，玩家移动激活心跳
			pet.getAi().interruptSchedule(IAi.CRITICAL_RESPONSE_TIME, TimeUnit.MILLISECONDS);
			pet.getStateManager().add(new AiBackState());
		}
		
	}
	
	public void pickupKuafu(Long id,String stageId,Long roleId){
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IRole role = (IRole)stage.getElement(roleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		Goods goods = (Goods)stage.getElement(id, ElementType.GOODS);
		if(null != goods){
			//所属权判断
			if(goods.checkIdentity(role)){
				stage.leave(goods);
				
				if(ModulePropIdConstant.KF_JF_GOODS_ID.equals(goods.getGoodsId())){
					//跨服战场积分不处理
					return;
				}else{
					GoodsConfig config = getGoodsConfig(goods.getGoodsId());
					if(GoodsCategory.TANBAO == config.getCategory()){
						StageMsgSender.send2Stage(roleId, InnerCmdType.ADD_TANBAO_SCORE, goods.getCount());
					}else if(config.getCategory() < 0){
						KuafuMsgSender.send2KuafuSource(roleId, InnerCmdType.TAKE_GOODS_KUAFU, new Object[]{goods.getGoodsId(),goods.getCount()});
					}else{
						KuafuMsgSender.send2KuafuSource(roleId, InnerCmdType.TAKE_GOODS, BagUtil.cover2RoleItemInput(goods));
					}
					
				}
			}else{
				//无法拾取
//				StageMsgSender.send2One(roleId, StageCommands.GOODS_PICKUP, new Object[]{0,StageErrorCode.NO_PICK_UP});
			}
		}else{
			KuafuMsgSender.send2One(roleId, ClientCmdType.PICKUP_GOODS_IS_NOT_EXIST, id);
		}
	}

	public Object pickup(Long id,String stageId,Long roleId) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return null;
		}
		IRole role = (IRole)stage.getElement(roleId, ElementType.ROLE);
		if(role == null){
			return null;
		}
		
		Goods goods = (Goods)stage.getElement(id, ElementType.GOODS);
		
		if(null != goods){
			
			//所属权判断
			if(goods.checkIdentity(role)){
				stage.leave(goods);
				
				Integer numberType = GoodsCategory.getNumberType(goods.getGoodsId());
				 
				if(ModulePropIdConstant.KF_JF_GOODS_ID.equals(goods.getGoodsId())){
					//跨服战场积分不处理
				}else if(numberType != null){
					if(numberType.equals(GoodsCategory.TANBAO)){
						StageMsgSender.send2Stage(roleId, InnerCmdType.ADD_TANBAO_SCORE, goods.getCount());
					}else{
					}
					accountExportService.incrCurrencyWithNotify(numberType, goods.getCount(), roleId, LogPrintHandle.GET_GOODS_PICKUP, LogPrintHandle.GBZ_GOODS_PICKUP);
//					else{
//						ChuanQiLog.error("暂时不支持对应的属性拾取,\t  goodsId:"+moneyType);
//					}
				}else{
					//统计云浮剑冢副本获取精气道具的数量
					if(stage instanceof JianzhongFubenStage){
						JianzhongFubenStage  jianzhongFubenStage  = (JianzhongFubenStage)stage;
						GoodsConfig goodsConfig = goodsConfigExportService.loadById(goods.getGoodsId());
						
						if(!jianzhongFubenStage.isGameOver()){
							if (goodsConfig.getCategory() == GoodsCategory.YAOSHEN_HUNPO_ITEM) {
								jianzhongFubenStage.addJingqiItemNum(1);
							}
							
							StageMsgSender.send2Bus(roleId, InnerCmdType.TAKE_GOODS, BagUtil.cover2RoleItemInput(goods));
						}
						
					}else{
						StageMsgSender.send2Bus(roleId, InnerCmdType.TAKE_GOODS, BagUtil.cover2RoleItemInput(goods));
					}
				}
				return null;
			}else{
				//无法拾取
				StageMsgSender.send2One(roleId, ClientCmdType.GOODS_PICKUP, AppErrorCode.NO_PICK_UP[1]);
			}
		}else{
			StageMsgSender.send2One(roleId, ClientCmdType.PICKUP_GOODS_IS_NOT_EXIST, id);
		}
		
		
		
//		if(null != goods){
//			
//			//所属权判断
//			if(goods.checkIdentity(role)){
//				stage.leave(goods);
//				
//				if(ModulePropIdConstant.MONEY_GOODS_ID.equals(goods.getGoodsId())){
//					
////					accountExportService.incrCurrencyWithChenMi(AccountType.TONGQIAN, goods.getCount(), roleId,LogChuanQiUtil.GET_PICKUP);
//					
////					StageMsgSender.send2One(roleId, StageCommands.GOODS_PICKUP, new Object[]{1,goods.getCount()});
//				}else if(ModulePropIdConstant.KF_JF_GOODS_ID.equals(goods.getGoodsId())){
//					//跨服战场积分不处理
//					return null;
//				}else{
////					GoodsConfig config = getGoodsConfig(goods.getGoodsId());
////					boolean isRecord = config == null ? false : config.getRecord();
////					
////					Object data = StageBusOutputWrapper.goods(goods, ItemType.ITEM_PICKUP, isRecord, role.getName());
////					StageMsgSender.send2Bus(roleId, StageCommands.INNER_ADD_GOODS, data);
//				}
//				
//				return null;
//			}else{
//				//无法拾取
////				StageMsgSender.send2One(roleId, StageCommands.GOODS_PICKUP, new Object[]{0,StageErrorCode.NO_PICK_UP});
//			}
//			
//			
//		}
		
		return null;
		
	}

	
	/**
	 * 角色名称修改
	 * @param stageId
	 * @param userRoleId
	 * @param newName
	 */
	public void roleNameChange(Long userRoleId,String newName){
		String stageId = publicRoleStateExportService.getRolePublicStageId(userRoleId);
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		//角色名称更改
//		role.getBusinessData().changeRoleName(newName);
	}

	
	/**
	 * 角色转职修改
	 * @param userRoleId
	 * @param changeConfigId
	 */
	public void roleJobChange(String userRoleId,Integer changeConfigId){
		
	}
	

	public void changeEquip(String stageId,Long userRoleId, Object[] equips,Object[] goodAndSort) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		//换装属性业务
		changeEquipHandle(role, stage, equips, goodAndSort);
	}
	
	public void changeOtherEquip(String stageId,Long userRoleId, Object[] equips,ContainerType type) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		//换装属性业务
		changeOtherEquipHandle(role, equips, type);
		Pet pet = role.getPet();
		if(pet != null){
			changePetEquipHandle(pet, equips, type);
		}
	}
	
	
	
    /**
     * 换装属性业务
     * 
     * @param role
     * @param stage
     */
    private void changeEquipHandle(IRole role, IStage stage, Object[] equips, Object[] goodAndSort) {

        BusinessData businessData = role.getBusinessData();
        // 更新缓存值
        RoleEquipData equipData = businessData.getEquipData();
        equipData.setEquips(equips);

        /**
         * 清除属性 BaseAttributeType.EQUIP_BASE BaseAttributeType.EQUIP_QSQH
         * BaseAttributeType.EQUIP_RANDOM_ATTR BaseAttributeType.EQUIP_SUIT_ATTR
         * BaseAttributeType.SW_EQUIP_QSQH
         */
        clearAttrs(role, false, BaseAttributeType.EQUIP_BASE, BaseAttributeType.EQUIP_QSQH, BaseAttributeType.EQUIP_RANDOM_ATTR, BaseAttributeType.EQUIP_SUIT_ATTR, BaseAttributeType.SW_EQUIP_QSQH);

        // 加上新装备属性
        if (equipData.getEquips() != null && equipData.getEquips().size() > 0) {
            // Map<String, Integer> xyAttrs = new HashMap<>();
            // Integer zplus = 0;
            Map<String, Long> equipAttrs = new HashMap<>();
            for (Equip equip : equipData.getEquips()) {
                Map<String, Long> tempAttrs = RoleFactoryHelper.getEquipBaseAttrs(equip, ContainerType.BODYTITEM);
                if (tempAttrs != null) {
                    ObjectUtil.longMapAdd(equipAttrs, tempAttrs);
                }
            }

            if (equipAttrs.size() > 0) {
                role.getFightAttribute().initBaseAttribute(BaseAttributeType.EQUIP_BASE, equipAttrs);
            }

            // 添加全身强化属性
            Map<String, Long> qsqhAttrs = equipExportService.getQSColorAddAttrs(equipData.getEquips());
            if (qsqhAttrs != null && qsqhAttrs.size() > 0) {
                role.getFightAttribute().initBaseAttribute(BaseAttributeType.EQUIP_QSQH, qsqhAttrs);
            }

            // 添加随机属性信息
            Map<String, Long> equipRandomAttrs = equipExportService.getEquipRandomAttrs(equipData.getEquips());
            if (equipRandomAttrs != null && equipRandomAttrs.size() > 0) {
                role.getFightAttribute().initBaseAttribute(BaseAttributeType.EQUIP_RANDOM_ATTR, equipRandomAttrs);
            }

            // 套装
            Map<String, Long> equipSuitAttrs = equipExportService.getEquipSuitAttrs(equipData.getEquips());
            if (equipSuitAttrs != null && equipSuitAttrs.size() > 0) {
                role.getFightAttribute().initBaseAttribute(BaseAttributeType.EQUIP_SUIT_ATTR, equipSuitAttrs);
            }

            // 添加全身神武装备强化属性
            Map<String, Long> swqsqhAttrs = equipExportService.getSWQSAddAttrs(equipData.getEquips());
            if (swqsqhAttrs != null && swqsqhAttrs.size() > 0) {
                role.getFightAttribute().initBaseAttribute(BaseAttributeType.SW_EQUIP_QSQH, swqsqhAttrs);
            }

            // TODO 其他属性,今后做到了在加

        }

        // // 当前战斗力
        // Integer totalFighteValue =
        // ZhanLiCalculateHelper.calculateZhuanLi(role);
        //
        // roleBusinessInfoExportService.roleSaveFighter(userRoleId,
        // totalFighteValue);
        // role.getBusinessData().setCurFightValue(totalFighteValue);
        // /**角色战力变化(推送主角)*/
        // StageMsgSender.send2One(userRoleId, ClientCmdType.ZL_CHANGE,
        // totalFighteValue);

        // if(goodAndSort != null){
        // String goodsId = (String) goodAndSort[0];
        // int sort = (Integer) goodAndSort[1];
        // //源装备格位小于0脱
        // if(sort < 0 ){
        // StageMsgSender.send2Many(role.getStage().getSurroundRoleIds(role.getPosition()),
        // ClientCmdType.TUO_EQUIPS,new Object[]{userRoleId,sort});
        // }else{
        // StageMsgSender.send2Many(role.getStage().getSurroundRoleIds(role.getPosition()),
        // ClientCmdType.CHANGE_EQUIPS,new Object[]{userRoleId,goodsId});
        // }
        // }

        // 输出变化
        role.getFightAttribute().refresh();
        role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
    }
	/**
	 * 换装属性业务
	 * @param role
	 * @param stage
	 */
	private void changeOtherEquipHandle(IRole role ,Object[] equips,ContainerType type){
		BaseAttributeType equipAtt = null;
		BaseAttributeType randomAtt = null;
		if(ContainerType.ZUOQIITEM.equals(type)){
			equipAtt = BaseAttributeType.ZUOQI_EQUIP_BASE;
			randomAtt = BaseAttributeType.ZUOQI_EQUIP_RANDOM_ATTR;
		}else if(ContainerType.CHIBANGITEM.equals(type)){
			equipAtt = BaseAttributeType.CHIBANG_EQUIP_BASE;
			randomAtt = BaseAttributeType.CHIBANG_EQUIP_RANDOM_ATTR;
		}else if(ContainerType.TANGBAOITEM.equals(type)){
			equipAtt = BaseAttributeType.TANGBAO_EQUIP_BASE;
			randomAtt = BaseAttributeType.TANGBAO_EQUIP_RANDOM_ATTR;
		}else if(ContainerType.TIANGONGITEM.equals(type)){
			equipAtt = BaseAttributeType.TIANGONG_EQUIP_BASE;
			randomAtt = BaseAttributeType.TIANGONG_EQUIP_RANDOM_ATTR;
		}else if(ContainerType.TIANSHANGITEM.equals(type)){
			equipAtt = BaseAttributeType.TIANSHANG_EQUIP_BASE;
			randomAtt = BaseAttributeType.TIANSHANG_EQUIP_RANDOM_ATTR;
		}else if(ContainerType.QILINGITEM.equals(type)){
			equipAtt = BaseAttributeType.QILING_EQUIP_BASE;
			randomAtt = BaseAttributeType.QILING_EQUIP_RANDOM_ATTR;
		}else if(ContainerType.TIANYUITEM.equals(type)){
			equipAtt = BaseAttributeType.TIANYU_EQUIP_BASE;
			randomAtt = BaseAttributeType.TIANYU_EQUIP_RANDOM_ATTR;
		}else if(ContainerType.CHONGWUITEM.equals(type)){
            equipAtt = BaseAttributeType.CHONGWU_EQUIP_BASE;
            randomAtt = BaseAttributeType.CHONGWU_EQUIP_RANDOM_ATTR;
        }else if(ContainerType.SHENQIITEM.equals(type)){
            equipAtt = BaseAttributeType.SHENQI_EQUIP_BASE;
            randomAtt = BaseAttributeType.SHENQI_EQUIP_RANDOM_ATTR;
        }else if(ContainerType.WUQI.equals(type)){
            equipAtt = BaseAttributeType.WUQI_EQUIP_BASE;
            randomAtt = BaseAttributeType.WUQI_EQUIP_RANDOM_ATTR;
        }
		if(equipAtt == null || randomAtt == null){
			return;
		}
		
		//清除属性
		clearAttrs(role,false,equipAtt,randomAtt);
		
		//加上新装备属性
		if(equips != null && equips.length >0){
			Map<String, Long> equipAttrs = new HashMap<>();
			List<Equip> equipsList = new ArrayList<>();
			for(Object tmp : equips){
				Equip equip = Equip.convert2Equip( (Object[])tmp );
				equipsList.add(equip);
				Map<String,Long> tempAttrs = RoleFactoryHelper.getEquipBaseAttrs(equip,type);
				if(tempAttrs != null){
					ObjectUtil.longMapAdd(equipAttrs, tempAttrs);
				}
			}
			
			if(equipAttrs.size() >0){
				role.getFightAttribute().initBaseAttribute(equipAtt, equipAttrs);
			}
			//添加随机属性信息
			Map<String, Long> equipRandomAttrs=equipExportService.getEquipRandomAttrs(equipsList);
			if(equipRandomAttrs !=null && equipRandomAttrs.size() >0){
				role.getFightAttribute().initBaseAttribute(randomAtt,equipRandomAttrs);
			}
			
		}
		//输出变化
		role.getFightAttribute().refresh();
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
	/**
	 * 糖宝换装属性业务
	 * @param role
	 * @param stage
	 */
	private void changePetEquipHandle(Pet pet,Object[] equips,ContainerType type){
		BaseAttributeType equipAtt = null;
		BaseAttributeType randomAtt = null;
		if(ContainerType.TANGBAOITEM.equals(type)){
			equipAtt = BaseAttributeType.TANGBAO_EQUIP_BASE;
			randomAtt = BaseAttributeType.TANGBAO_EQUIP_RANDOM_ATTR;
		}else if(ContainerType.TIANGONGITEM.equals(type)){
			equipAtt = BaseAttributeType.TIANGONG_EQUIP_BASE;
			randomAtt = BaseAttributeType.TIANGONG_EQUIP_RANDOM_ATTR;
		}else if(ContainerType.TIANSHANGITEM.equals(type)){
			equipAtt = BaseAttributeType.TIANSHANG_EQUIP_BASE;
			randomAtt = BaseAttributeType.TIANSHANG_EQUIP_RANDOM_ATTR;
		}
		if(equipAtt == null || randomAtt == null){
			return;
		}
		
		//清除属性
		clearFighterAttrs(pet,false,equipAtt,randomAtt);
		
		//加上新装备属性
		if(equips != null && equips.length >0){
			Map<String, Long> equipAttrs = new HashMap<>();
			List<Equip> equipsList = new ArrayList<>();
			for(Object tmp : equips){
				Equip equip = Equip.convert2Equip( (Object[])tmp );
				equipsList.add(equip);
				Map<String,Long> tempAttrs = RoleFactoryHelper.getEquipBaseAttrs(equip,type);
				if(tempAttrs != null){
					ObjectUtil.longMapAdd(equipAttrs, tempAttrs);
				}
			}
			
			if(equipAttrs.size() >0){
				pet.getFightAttribute().initBaseAttribute(equipAtt, equipAttrs);
			}
			//添加随机属性信息
			Map<String, Long> equipRandomAttrs=equipExportService.getEquipRandomAttrs(equipsList);
			if(equipRandomAttrs !=null && equipRandomAttrs.size() >0){
				pet.getFightAttribute().initBaseAttribute(randomAtt,equipRandomAttrs);
			}
			
		}
		//输出变化
		pet.getFightAttribute().refresh();
		pet.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
	
	/**
	 * 清除玩家身上各类型属性
	 * @param role 角色
	 * @param attributeType  @{link BaseAttributeType}  属性类型
	 */
	private void clearAttrs(IRole role,boolean isRefresh,BaseAttributeType... attributeType) {
		for (BaseAttributeType baseAttributeType : attributeType) {
			RoleFactoryHelper.clearEquip(role, baseAttributeType,isRefresh); //清除装备基本属性
		} 
	}
	private void clearFighterAttrs(IFighter fighter,boolean isRefresh,BaseAttributeType... attributeType) {
		for (BaseAttributeType baseAttributeType : attributeType) {
			fighter.getFightAttribute().clearBaseAttribute(baseAttributeType, isRefresh); //清除装备基本属性
		} 
	}

	
	
	
	private GoodsConfig getGoodsConfig(String goodsId){
		return goodsConfigExportService.loadById(goodsId);
	}
	
	public void vipUpgrade(String stageId,Long roleId,int id){
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;
		}
		IRole role = (IRole)stage.getElement(roleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		VipConfig config = vipConfigExportService.getVipConfigById(id);
				
		BusinessData businessData =	role.getBusinessData();
		businessData.setVipLevel(config.getLevel());
		businessData.setDazuoExp(config.getTequanMap().get(GameConstants.VIP_DAZUO_EXP));
		
		role.getFightAttribute().setBaseAttribute(BaseAttributeType.VIP, config.getAttribute());
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
	
	public void upgrade(String stageId,Long roleId,int level) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(roleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		BusinessData businessData =	role.getBusinessData();
		businessData.setLevel(level);
		
		//等级属性
		ExpConfig expConfig = expConfigExportService.loadById(level);
		if(expConfig != null){
			
			Map<String,Long> attribute = expConfig.getAttribute();
			
			role.getFightAttribute().setBaseAttribute(BaseAttributeType.LEVEL,attribute);
		}
		
		//升级后满血满蓝
		role.getFightAttribute().resetHpMp();
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		
		Object[] data = new Object[]{roleId,level};
		//[ 0：角色GUID,1: level ]
		if(KuafuConfigPropUtil.isKuafuServer()){
			KuafuMsgSender.send2Many(stage.getNoSelfSurroundRoleIds(role.getPosition(),roleId), ClientCmdType.LEVEL_UPGRADE, data);
			KuafuMsgSender.send2One(roleId, ClientCmdType.LEVEL_UPGRADE, data);
			
			//刷新糖宝属性
			KuafuMsgSender.send2KuafuSource(roleId, InnerCmdType.PET_ATTRIBUTE_REFRESH,null);
		}else{
			StageMsgSender.send2Many(stage.getNoSelfSurroundRoleIds(role.getPosition(),roleId), ClientCmdType.LEVEL_UPGRADE, data);
			StageMsgSender.send2One(roleId, ClientCmdType.LEVEL_UPGRADE, data);
			
			//刷新糖宝属性
			StageMsgSender.send2Bus(roleId, InnerCmdType.PET_ATTRIBUTE_REFRESH,null);
		}
		
//		//刷新坐骑属性 
//		StageMsgSender.send2StageInner(roleId, stageId, InnerCmdType.INNER_ZUOQI_REFRESH,null);
		
		//刷新新圣剑属性 
		StageMsgSender.send2StageInner(roleId, stageId, InnerCmdType.INNER_WUQI_REFRESH,null);
		
		//绑定session的角色等级(前期升级较快先不同步，后面再同步,不影响业务)
		if(level >= 10){
			sessionManagerExportService.saveSessin4Level(roleId, level);
		}
	}


	public Object getTargetAttribute(Long roleId) {
		if(publicRoleStateExportService.isPublicOnline(roleId)){
			String stageId = stageControllExportService.getCurStageId(roleId);
			if(stageId == null){
				return null;
			}
			IStage stage = StageManager.getStage(stageId);
			if(stage == null){
				return null;
			}
			IRole role = stage.getElement(roleId, ElementType.ROLE);
			if(role == null){
				return null;
			}
			Object[] equips = equipExportService.getAllEquips(roleId);
			return role.getOtherData(equips);
		}else{
			return roleInfoDao.loadRoleInfoFromFileDb(roleId + "");
		}
	}
	
	   public Object getTargetRoleInfo(Long roleId) {
	        if(publicRoleStateExportService.isPublicOnline(roleId)){
	            String stageId = stageControllExportService.getCurStageId(roleId);
	            if(stageId == null){
	                return null;
	            }
	            IStage stage = StageManager.getStage(stageId);
	            if(stage == null){
	                return null;
	            }
	            IRole role = stage.getElement(roleId, ElementType.ROLE);
	            if(role == null){
	                return null;
	            }
	            Object[] equips = equipExportService.getAllEquips(roleId);
	            return role.getOtherData(equips);
	        }else{
	            Object info = roleInfoDao.loadRoleInfoFromFileDb(roleId + "");
	            if(null == info){
	                return AppErrorCode.ROLE_INFO_NOT_FOUND;
	            }else{
	                return info;
	            }
	        }
	    }
	
	public Object getAttribute(String stageId, Long roleId) {
		
		IStage stage = StageManager.getStage(stageId);
		IRole role = (IRole)stage.getElement(roleId, ElementType.ROLE);
		
		return StageOutputWrapper.getAttribute(role);
	}

	public Object[] getSurroundRoleId(String stageId, Long roleId) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return null;
		}
		IRole role = (IRole)stage.getElement(roleId, ElementType.ROLE);
		if(role == null){
			return null;
		}
		
		return stage.getSurroundRoleIds(role.getPosition());
	}

	public void teamChange(String stageId, Long roleId, String teamId,
			Long[] teamMembers) {
		
		IStage stage = StageManager.getStage(stageId);
		IRole role = (IRole)stage.getElement(roleId, ElementType.ROLE);
		if(role != null){
//			role.getBusinessData().setTeam(teamId,teamMembers);//TODO
		}
		
//		StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), StageCommands.TEAM_INFO_CHANGE, new Object[]{role.getId(),teamId});
	}

	public Object teamMemberInfos(String stageId, Long roleId,
			Object[] teamMembers) {
		
		IStage stage = StageManager.getStage(stageId);
		
		List<Object> result = null;
		
		if(null != teamMembers){
			
			result = new ArrayList<Object>(teamMembers.length);
			
			for(Object tmp : teamMembers){
				
				Long targetId = (Long)tmp;
				IElement element = stage.getElement(targetId, ElementType.ROLE);
				if(null != element){
					result.add(new Object[]{element.getId(),element.getPosition().getX(),element.getPosition().getY()});
				}
				
			}
		}
		
		
		return result;
	}

	public Object teamMemberPostion(String stageId, Long roleId) {
		
		IStage stage = StageManager.getStage(stageId);
		IRole role = (IRole)stage.getElement(roleId, ElementType.ROLE);
		
		return new Object[]{role.getId(),role.getPosition().getX(),role.getPosition().getY()};
	}
	
	
	public void roleFighterChange(String stageId,Long roleId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(roleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		//总战斗力
//		Integer totalFighteValue = calcRoleFighteValue(role);
//		role.getBusinessData().setCurFightValue(totalFighteValue);
		
		//推送业务模块保存战斗力值
//		StageMsgSender.send2Bus(roleId, StageCommands.INNER_BUS_FIGHTER_CHANGE, totalFighteValue);
	}
	
	public int calcRoleFighteValue(IRole role){
		//总战斗力
		Integer totalFighteValue = ZhanLiCalculateHelper.calculateZhuanLi(role);
		
		return totalFighteValue;
	}

	public void guildChange(String stageId, Long roleId, Long guildId,String guildName) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IRole role = (IRole)stage.getElement(roleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		role.getBusinessData().setGuildId(guildId);
		role.getBusinessData().setGuildName(guildName);
	}
	
	public void guildLevelChange(String stageId, Long roleId,Integer level) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IRole role = (IRole)stage.getElement(roleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		GuildConfig config = guildConfigService.getGuildConfig(level);
		if(config != null){
			role.getFightAttribute().setBaseAttribute(BaseAttributeType.GUILD,config.getAttribute());
		}else{
			role.getFightAttribute().clearBaseAttribute(BaseAttributeType.GUILD, true);
		}
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}

	
	
	public void rolePkValueChange(String stageId,Long roleId,int pkValue){
		IStage stage = StageManager.getStage(stageId);
		if(stage != null){
			IRole role = (IRole)stage.getElement(roleId, ElementType.ROLE);
			if(role != null){
				role.getBusinessData().setPkValue(pkValue);
				PKPublicConfig pKPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_PK);
				role.getBusinessData().setRedPkValue(pKPublicConfig.getNeedRed());
				PkDebuffConfig pkconfig = pkDebuffConfigExportService.loadByPk(pkValue);
				if(pkconfig != null && pkconfig.getAttribute() != null){
					role.getFightAttribute().initBaseAttribute(BaseAttributeType.HONGMING, pkconfig.getAttribute());
				}else{
					role.getFightAttribute().clearBaseAttribute(BaseAttributeType.HONGMING, true);
				}
				role.getFightAttribute().refresh();
				//广播AOI
				StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.PK_CHANGE, new Object[]{roleId,pkValue});
			}
		}
	}
	
	public void roleHuiminValueChange(String stageId,Long roleId,int huiminTime){
		IStage stage = StageManager.getStage(stageId);
		if(stage != null){
			IRole role = (IRole)stage.getElement(roleId, ElementType.ROLE);
			if(role != null){
				role.getBusinessData().setHuiminTime(huiminTime);
				
				boolean isHui = false;
				if(huiminTime > 0){
					isHui = true;
				}
				//广播AOI
				StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.HUIMING_CHANGE, new Object[]{roleId,isHui});
			}
		}
	}
	
	
	/**
	 * 获取角色战力
	 */
	public Object getRolesZl(String stageId,Long roleId,Object[] rolesGuid){
		if(rolesGuid == null || rolesGuid.length == 0){
			return null;
		}
		
		List<Object> outData = new ArrayList<Object>();
		for (int i = 0; i < rolesGuid.length; i++) {
			Long otherRoleId = Long.parseLong(rolesGuid[i].toString());
			
			String otherStageId = publicRoleStateExportService.getRolePublicStageId(otherRoleId);
			if(otherStageId == null){
				continue;
			}
			
			IStage stage = StageManager.getStage(otherStageId);
			if(null != stage){
				IRole role = (IRole)stage.getElement(otherRoleId, ElementType.ROLE);
				if(role != null){
					outData.add(new Object[]{role.getId(),role.getBusinessData().getCurFightValue()});
				}
			}
		}
		
		if(outData.size() == 0){
			return null;
		}else{
			return outData.toArray();
		}
	}
	
	
	/**
	 * 进入场景后，激活主动怪物仇恨
	 * @param stageId
	 * @param userRoleId
	 */
	public void enterStageActivateHatred(String stageId,Long userRoleId){
		IStage stage = StageManager.getStage(stageId);
		IRole role = (IRole)stage.getElement(userRoleId,ElementType.ROLE);
		
		hatredHandle(role, stage);
	}

	public void saveRoleInfo(Long userRoleId) {
		String stageId = stageControllExportService.getCurStageId(userRoleId);
		if(stageId == null){
			return;
		}
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IRole role = (IRole)stage.getElement(userRoleId,ElementType.ROLE);
		if(role == null || role.getLevel() < 40){
			return;
		}
		
		Object[] equips = equipExportService.getAllEquips(userRoleId);
		roleInfoDao.writeRoleInfoFromFileDb(role.getOtherData(equips), userRoleId.toString());
	}
	
	/**
	 * 打坐
	 * @param roleId
	 * @return
	 */
	public Object[] daZuo(Long roleId,String stageId, Long targetRoleId) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null || !stage.isCanDazuo()){
			return AppErrorCode.DAZUO_ERROR;
		}
		Role role = stage.getElement(roleId,ElementType.ROLE);
		if(role == null ){
			return AppErrorCode.DAZUO_ERROR;
		}
		
		if(role.getStateManager().isDead()){
			return AppErrorCode.DAZUO_ERROR;
		}
		
		if(targetRoleId.longValue() == 0l){
			return AppErrorCode.DAZUO_ERROR;
		}
		
		/**
		 * 如果是双休变成了单休,发送的是这个指令,直接返回失败
		 */
		if(role.getBusinessData().getDazuoTargetRoleId() != 0l && role.getBusinessData().getDazuoTargetRoleId() != roleId.longValue()){
			Role targetRole = stage.getElement(targetRoleId, ElementType.ROLE);
			ChuanQiLog.error("错误================该玩家已经是双休状态了(已经是双休状态的对象：["+role.getName()+"]("+role.getBusinessData().getDazuoTargetRoleId()+")) 不需要再与别人打坐或自己打坐了(selfId:"+role.getId()+")\t targetId:["+targetRole.getName()+"]("+targetRoleId+")");
			return AppErrorCode.DAZUO_STATE_ERROR;
		}
		
		
//		//如果是双休打坐，将对方的信息也修改掉
//		if(!roleId.equals(targetRoleId)){
//			Role targetRole = stage.getElement(targetRoleId, ElementType.ROLE);
//			if(targetRole == null){
//				return AppErrorCode.DAZUO_ERROR;
//			}
//			//将对方变成双休状态
//			BusinessData targetBusiness = targetRole.getBusinessData();
//			
//			if(targetBusiness.getDazuoTargetRoleId() != targetRole.getId().longValue()){
//				return AppErrorCode.DAZUO_SX_DAZUOING;
//			}
//	 
//			StageMsgSender.send2One(roleId, ClientCmdType.DAZUO_SX, new Object[]{1,targetRoleId});
//			
//			targetBusiness.setDazuoTargetRoleId(roleId);
//			
//			//取消寻找双休打坐的定时器 
//			role.getScheduler().cancelSchedule(roleId+"", GameConstants.COMPONENT_DAZUO_SX);
//			targetRole.getScheduler().cancelSchedule(targetRole.getId()+"",  GameConstants.COMPONENT_DAZUO_SX);
//			
//			//通知场景中其他玩家自己已经进入打坐状态
//			StageMsgSender.send2Many(targetRole.getStage().getSurroundRoleIds(role.getPosition()), ClientCmdType.NOTITY_DAZUO_STAGE,new Object[]{targetRole.getId(),roleId});
//
////			System.out.println("玩家["+role.getName()+"]与玩家【"+targetRole.getName()+"】已进入双休状态");
// 		}
		
		
		
		
		
		BusinessData roleBusinessData =	role.getBusinessData();
		roleBusinessData.setDazuoTargetRoleId(targetRoleId);
		
		StageTokenRunable runable = new StageTokenRunable(roleId, stage.getId(), InnerCmdType.DAZUO_AWARD, null); 
		role.getScheduler().schedule(roleId.toString(), GameConstants.COMPONENT_DAZUO, runable, GameConstants.DAZUO_AWARD_CD, TimeUnit.SECONDS);
		
		//通知场景中其他玩家自己已经进入打坐状态
		StageMsgSender.send2Many(role.getStage().getSurroundRoleIds(role.getPosition()), ClientCmdType.NOTITY_DAZUO_STAGE,new Object[]{role.getId(),roleBusinessData.getDazuoTargetRoleId()});
		
		//如果是单休打坐
		if(roleBusinessData.getDazuoTargetRoleId() == roleId.longValue()){
			//System.out.println("如果是单休打坐,切换双休："+roleId.longValue());
			StageTokenRunable runableSX = new StageTokenRunable(roleId, stage.getId(), InnerCmdType.DAZUO_2_SX, null);
			
			role.getScheduler().schedule(roleId.toString(), GameConstants.COMPONENT_DAZUO_SX, runableSX, GameConstants.DAZUO_2_SX_CD, TimeUnit.SECONDS);
		}
		
		//添加打坐状态
		role.getStateManager().add(new DaZuoState());
		//修炼任务记录打坐时间
		role.setDazuoTime(GameSystemTime.getSystemMillTime());
		return new Object[]{1};
	}
	
	public void dazuoAward(Long roleId, String stageId,boolean isInWenquan) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		Role role = (Role)stage.getElement(roleId,ElementType.ROLE);
		if(role == null ){
			return;
		}
		
		int level = role.getLevel();
		long targetRoleId = role.getBusinessData().getDazuoTargetRoleId(); 
		
		DaZuoConfig  dazuoConfig = StageConfigureHelper.getDaZuoConfigExportService().loadById(level);
		if(dazuoConfig == null) {
			ChuanQiLog.error("打坐配置表错误,level:"+level);
			return ;
		}
		
		int addExp = 0;
		int addzhenqi = 0;
		
		if(!isInWenquan){
			if(targetRoleId == roleId.longValue()){
				addExp = dazuoConfig.getSkill1exp();
				addzhenqi = dazuoConfig.getZhenqi();
			}else if(targetRoleId != 0l){
				addExp = dazuoConfig.getSxexp();
				addzhenqi = dazuoConfig.getSxzhenqi();
			}else{
				//如果发现不是打坐状态了 ,直接清除打坐奖励定时器
				role.getScheduler().cancelSchedule(roleId.toString(), GameConstants.COMPONENT_DAZUO);
				return;
			}
		}else{
			addExp = dazuoConfig.getSkill1exp();
			addzhenqi = dazuoConfig.getZhenqi();
		}
		
		//VIP特权经验加成
		int dazuo = role.getBusinessData().getDazuoExp();
		if(dazuo == 0){//未知加成
			dazuo = roleVipInfoExportService.getVipTequan(roleId, GameConstants.VIP_DAZUO_EXP);
			role.getBusinessData().setDazuoExp(dazuo);
			dazuo += 100;
			addExp = addExp * dazuo / 100;
			addzhenqi = addzhenqi * dazuo / 100;
		}else if(dazuo > 100){
			addExp = addExp * dazuo / 100;
			addzhenqi = addzhenqi * dazuo / 100;
		}
		
		//处理活动奖励倍率
		Float active = ActiveUtil.getDazuoBei(stage.getMapId());
		if(active > 1){
			addExp = (int)(addExp * active);
			addzhenqi = (int)(addzhenqi * active);
		}
		if(!KuafuConfigPropUtil.isKuafuServer()){
			if(StageType.TERRITORY_WAR.equals(stage.getStageType())){
				TerritoryConfig config = territoryConfigExportService.getConfigByMapId(stage.getMapId());
				if(config != null){
					addExp = (int)(addExp * (1f+config.getAddexp()));
					addzhenqi = (int)(addzhenqi * (1f+config.getAddzhenqi()));
				}
			}
			//帮派领地 打坐加成
			Long guildId = role.getBusinessData().getGuildId();
			if(guildId != null){
				Territory territory = territoryExportService.loadTerritoryByMapId(stage.getMapId());
				if(territory != null && territory.getGuildId() != null && territory.getGuildId().longValue() != 0){
					if(territory.getGuildId().longValue() == guildId.longValue()){
						int[] add = guildExportService.getGuildMapExpZhenqiAdd(guildId);
						if(add != null){
							addExp = (addExp * (100 + add[0])) / 100;
							addzhenqi = (addzhenqi * (100 + add[1])) / 100;
						}
					}
				}
			}
			
			//帮派争霸撒 打坐加成
			if(StageType.HCZBS_WAR.equals(stage.getStageType())){
				HcZhengBaSaiConfig config = hcZhengBaSaiConfigExportService.loadPublicConfig();
				if(config != null){
					addExp = (int)(addExp * (1f+config.getAddexp()));
					addzhenqi = (int)(addzhenqi * (1f+config.getAddzhenqi()));
				}
			}
		}else{
		    // 跨服云宫之巅场景中,打坐增加获得经验和真气的倍率
            if (StageType.KUAFU_YUNGONG.equals(stage.getStageType())) {
                KuafuYunGongStage ygStage = (KuafuYunGongStage) stage;
                KuafuYunGongPublicConfig config = ygStage.getConfig();
                if (null != config) {
                    addExp = (int) (addExp * (1f + config.getAddexp()));
                    addzhenqi = (int) (addzhenqi * (1f + config.getAddzhenqi()));
                }
            }
		}
		
		if(isInWenquan){
			if(ActiveUtil.isWenquan()){
				WenquanPublicConfig wenquanPublicConfig= gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_WENQUAN);
				int wenquanBeishu = 1;
				if(WenquanManager.getManager().isInHighArea(roleId)){
					wenquanBeishu = wenquanPublicConfig.getBeishu1();
				}else{
					wenquanBeishu = wenquanPublicConfig.getBeishu();
				}
				addExp = addExp *wenquanBeishu;
				addzhenqi = addzhenqi * wenquanBeishu;
				WenquanManager.getManager().addJingyan(roleId, addExp * 1L);
				WenquanManager.getManager().addJingyan(roleId, addzhenqi * 1L);
			}
		}
		short cmd = ClientCmdType.DAZUO_EXP_ADD;
		if(isInWenquan){
			cmd = ClientCmdType.WENQUAN_EXP_ZHENQI;
		}
		if(KuafuConfigPropUtil.isKuafuServer()){
			KuafuMsgSender.send2KuafuSource(roleId, InnerCmdType.INNER_DAZUO_AWARD, new Object[]{addExp,addzhenqi,cmd});
		}else{
			StageMsgSender.send2Bus(roleId, InnerCmdType.INNER_DAZUO_AWARD, new Object[]{addExp,addzhenqi,cmd}); 
		}
		
		if(!isInWenquan){
			//添加打坐的信息
			StageTokenRunable runable = new StageTokenRunable(roleId, stage.getId(), InnerCmdType.DAZUO_AWARD, null);
			role.getScheduler().schedule(roleId.toString(), GameConstants.COMPONENT_DAZUO, runable, GameConstants.DAZUO_AWARD_CD, TimeUnit.SECONDS);
		}
	}
	 
	
	/**
	 * 取消打坐
	 * @param roleId
	 * @param stageId
	 */
	public void dazuoTaskCancel(long roleId, String stageId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		Role role = (Role)stage.getElement(roleId,ElementType.ROLE);
		if(role == null ){
			return;
		}
		
			
		BusinessData businessData = role.getBusinessData();
		//如果已经取消打坐了,就不往下操作了。
		if(businessData.getDazuoTargetRoleId() == 0l) return;
		
		role.getStateManager().remove(StateType.DAZUO);
		
		//如果从双休变为单休
		if(businessData.getDazuoTargetRoleId() != roleId ){
			
			IStage tarstage = StageManager.getStage(stageId);
			if(tarstage == null){
				return;
			}
			Role tarRole = (Role)stage.getElement(businessData.getDazuoTargetRoleId(),ElementType.ROLE);
			if(tarRole == null ){
				return;
			}
			BusinessData tarBusinessData = tarRole.getBusinessData();
			
			tarBusinessData.setDazuoTargetRoleId(tarRole.getId());
			
			//通知场景中其他玩家自己已经进入打坐状态
			StageMsgSender.send2Many(tarRole.getStage().getSurroundRoleIds(tarRole.getPosition()), ClientCmdType.NOTITY_DAZUO_STAGE,new Object[]{tarRole.getId(),tarRole.getId()});
//				 System.out.println("玩家【"+role.getName()+"】取消打坐，玩家【"+tarRole.getName()+"】变成单休");
		}
		
		//取消加奖励的定时器
		role.getScheduler().cancelSchedule(role.getId()+"", GameConstants.COMPONENT_DAZUO);
		role.getScheduler().cancelSchedule(role.getId()+"", GameConstants.COMPONENT_DAZUO_SX);
		
		businessData.setDazuoTargetRoleId(0);
		
		StageMsgSender.send2One(roleId, ClientCmdType.DAZUO_CANCEL, null);
		
		//通知场景中其他玩家自己取消打坐状态
		StageMsgSender.send2Many(role.getStage().getSurroundRoleIds(role.getPosition()), ClientCmdType.NOTITY_DAZUO_STAGE,new Object[]{role.getId(),0});
		try {
			long startTime = role.getDazuoTime();
			if(startTime > 0){
				int fen = (int) ((GameSystemTime.getSystemMillTime() - startTime)/1000/60);
				BusMsgSender.send2BusInner(roleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE_TS, new Object[] {XiuLianConstants.DAZUO, fen});
				role.setDazuoTime(0l);
			}
		} catch (Exception e) {
			ChuanQiLog.error(""+e);
		}
//		System.out.println("玩家【"+role.getName()+"】取消打坐");
	}
	
	/**
	 * 下线处理打坐信息
	 */
	public void offOnlineDaZuo(Role role){
		if(role == null ){
			return;
		}
		//先取消打坐
		dazuoTaskCancel(role.getId(), role.getStage().getId());
		
//		Role role = getStageRoleByRoleId(roleId);
		
		//判断自己是否为双休，如果为双休,将对方的双休状态置为单休
		if(role.getBusinessData().getDazuoTargetRoleId() != role.getId().longValue() && role.getBusinessData().getDazuoTargetRoleId()!=0l){
			
			long targetRoleId = role.getBusinessData().getDazuoTargetRoleId(); 
			
			String targetStageId =  publicRoleStateExportService.getRolePublicStageId(targetRoleId);
			IStage targetStage = StageManager.getStage(targetStageId);
			if(targetStage == null){
				return;
			}
			Role targetRole = (Role)targetStage.getElement(targetRoleId,ElementType.ROLE);
			if(targetRole == null ){
				return;
			}
			
			BusinessData tarBusinessData = targetRole.getBusinessData();
			
			tarBusinessData.setDazuoTargetRoleId(targetRole.getId());
			 
			//通知场景中其他玩家自己已经进入单休状态
			StageMsgSender.send2Many(targetRole.getStage().getSurroundRoleIds(targetRole.getPosition()), ClientCmdType.NOTITY_DAZUO_STAGE,new Object[]{targetRole.getId(),targetRole.getId()});
		}
		
		/**
		 * 下线时清除的定时任务
		 */
		role.getScheduler().cancelSchedule(role.getId()+"", GameConstants.COMPONENT_DAZUO);
		role.getScheduler().cancelSchedule(role.getId()+"", GameConstants.COMPONENT_DAZUO_SX);
	}
	 
	
	/**
	 * 
	 * @param roleId
	 * @param stageId
	 */
	public void dazuo2SX(Long roleId, String stageId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		if(!stage.isCanDazuo()){
			return;
		}
		Role role = (Role)stage.getElement(roleId,ElementType.ROLE);
		if(role == null ){
			return;
		}
		
		//寻早单休打坐的玩家
		Role targetRole = null;
		Collection<Role> roles =  role.getStage().getSurroundElements(role.getPosition(), ElementType.ROLE);
		for (Role  roundRole : roles) {
			if(roundRole.getId().longValue() == roundRole.getBusinessData().getDazuoTargetRoleId() && roleId.longValue() != roundRole.getId().longValue()){
				targetRole = roundRole;
				break;
			}
		}
		
		
		boolean isGoNO= true;
		if(targetRole!=null){//如果找到就进入双休  
			//TODO wind  屏蔽掉打坐  StageMsgSender.send2One(roleId, ClientCmdType.DAZUO_AUTO_SX,targetRole.getId());
		}
		
		if(isGoNO){//如果没有找到单休的玩家，继续寻找
			
			StageTokenRunable runableSX = new StageTokenRunable(roleId, stage.getId(), InnerCmdType.DAZUO_2_SX, null);
			
			role.getScheduler().schedule(roleId.toString(), GameConstants.COMPONENT_DAZUO_SX, runableSX, GameConstants.DAZUO_2_SX_CD, TimeUnit.SECONDS);
		}
	}
	
	/**
	 * 检查某人是否要取消打坐状态
	 * @param userRoleId
	 */
	public void daZuoCancelHandle(Role role){
		if(role == null) return;
		if(role.getBusinessData().getDazuoTargetRoleId() != 0){
			//取消打坐
			dazuoTaskCancel(role.getId(), role.getStage().getId());
		}
	}
	
	/**
	 * 添加跳闪值
	 * @param userRoleId
	 * @param stageId
	 */
	public void addTiaoShan(Long userRoleId, String stageId) {
		
		IStage stage = StageManager.getStage(stageId);
	 
		if(stage==null)return;
		
		Role role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null)return;
		
		if(role.getBusinessData().getTiaoShan() >=GameConstants.TIAOSHAN_MAX_VAL){
			role.getScheduler().cancelSchedule(role.getId()+"", GameConstants.COMPONENT_JUMP_VAL);
		}else{
			
			role.getBusinessData().setTiaoShan(role.getBusinessData().getTiaoShan()+1);
			
//			if(!KuafuConfigPropUtil.isKuafuServer()){//跨服不同步数据   TODO
//				roleBusinessInfoExportService.addTiaoShan(userRoleId, 1);
//			}
			
			StageMsgSender.send2One(userRoleId, ClientCmdType.TIAO_SHAN, role.getBusinessData().getTiaoShan());
			//添加跳闪任务  添加跳闪值
			addJumpTask(role);
		}
	}
	
	
	private void addJumpTask(Role role){ 
		if(role.getBusinessData().getTiaoShan() < GameConstants.TIAOSHAN_MAX_VAL){
			StageTokenRunable runableJump = new StageTokenRunable(role.getId(), role.getStage().getId(), InnerCmdType.ADD_TIAOSHAN, null);
			role.getScheduler().schedule(role.getId().toString(), GameConstants.COMPONENT_JUMP_VAL, runableJump, GameConstants.JUMP_VAL_CD, TimeUnit.SECONDS);
		}
	}

	//上线处理跳闪值
	public void onlineJump(Role role) {
		addJumpTask(role);
	}
	
	private Role getStageRoleByRoleId(long userRoleId){
		String stageId =  publicRoleStateExportService.getRolePublicStageId(userRoleId);
		if(stageId == null) {
			return null;	
		}
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return null;
		}
		Role role = (Role)stage.getElement(userRoleId,ElementType.ROLE);
		if(role == null ){
			return null;
		}
		return role;
	}
	
	/**
	 * 设置跳闪结束
	 * @param userRoleId
	 * @param stageId
	 */
	public void stageTiaoShan(Long userRoleId, String stageId) {
		IStage stage = StageManager.getStage(stageId);
		 
		if(stage==null)return;
		
		Role role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null)return;
		role.getBusinessData().setJump(false);
	}
	
	/**
	 * 跳闪消耗
	 * @param target
	 * @return
	 */
	public boolean jumpMissHandle(Role role) {
		return false;//liuyu 暂时关闭
		
//		//如果不是跳闪 或则跳闪值为0 那就走正常的攻击流程 
//		BusinessData businessData = role.getBusinessData();
//		if(!businessData.isJump()||businessData.getTiaoShan() <=0){
//			return false;
//		}
//		
//		//消耗跳闪值
//		businessData.setTiaoShan(businessData.getTiaoShan()-1);
//		roleBusinessInfoExportService.costTiaoShan(role.getId(), 1);
//		
//		//开启增加跳闪值的定时器
//		if(role.getBusinessData().getTiaoShan() < GameConstants.TIAOSHAN_MAX_VAL){
//			StageTokenRunable runableJump = new StageTokenRunable(role.getId(), role.getStage().getId(), InnerCmdType.ADD_TIAOSHAN, null);
//			role.getScheduler().schedule(role.getId().toString(), GameConstants.COMPONENT_JUMP_VAL, runableJump, GameConstants.JUMP_VAL_CD, TimeUnit.SECONDS);
//		}
//		
//		return true;
	}
	
	/**
	 * 上下坐骑
	 * @param roleId
	 * @param stageId
	 * @param isGetOn
	 */
	public void zuoqiState(Long userRoleId, String stageId, boolean isGetOn) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return ;
		}
		Role role = (Role)stage.getElement(userRoleId,ElementType.ROLE);
		if(role == null ){
			return ;
		}
		
		if(role.getZuoQi() == null)return;
		
		if(isGetOn){
			if(!stage.isCanFeijian()){
				return;
			}
			int  showId = role.getZuoQi().getShowId(); 
			role.getStateManager().add(new ZuoQiState(role)); 
			
			StageMsgSender.send2Many(role.getStage().getSurroundRoleIds(role.getPosition()), ClientCmdType.ZUOQI_UP, new Object[]{userRoleId,showId});
		}else{
			role.getStateManager().remove(StateType.ZUOQI);
			StageMsgSender.send2Many(role.getStage().getSurroundRoleIds(role.getPosition()), ClientCmdType.ZUOQI_DOWN, userRoleId);
		}
		role.getZuoQi().setGetOn(isGetOn);
	}
	
	public void changeZuoqiAttrs(Long userRoleId, String stageId, ZuoQiInfo zuoQiInfo,List<Integer> huanhuaConfigIdList, Object[] zqEquips,Integer activatedShenqiNum) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		clearAttrs(role,false,BaseAttributeType.ZUOQI);
		
		ZuoQi zuoQi =  ZuoQiUtil.coverToZuoQi(zuoQiInfo,huanhuaConfigIdList, zqEquips);
		role.setZuoQi(zuoQi);
		Map<String, Long>  zuoQiAttrs = StageHelper.getZuoQiExportService().getZuoQiAttrs(userRoleId,zuoQi,role.getLevel());
		if(zuoQiAttrs != null){
			//通知前段坐骑属性变化
			BusMsgSender.send2One(userRoleId, ClientCmdType.ZUOQI_ATTR_CHANGE, zuoQiAttrs);
			//重置坐骑属性
			RoleFactoryHelper.setRoleBaseAttrs(zuoQiAttrs, role, BaseAttributeType.ZUOQI);
			//重置神器祝福属性
			changeShenqiZhuAttr(role,activatedShenqiNum);
			role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
			
			if(role.getStateManager().contains(StateType.ZUOQI)){
				role.getStateManager().remove(StateType.ZUOQI);
				role.getStateManager().add(new ZuoQiState(role));
			}
			
			Long configZplus = zuoQiAttrs.get(EffectType.zplus.name());
			Long busZplus = zuoQiInfo.getZplus();
			if(!busZplus.equals(configZplus)){
				//抛出一个异步指令让bus存储坐骑战斗力
				if(KuafuConfigPropUtil.isKuafuServer()){
					KuafuMsgSender.send2KuafuSource(userRoleId, InnerCmdType.INNER_ZUOQI_ZPLUS_CHANGE, configZplus);
				}else{
					StageMsgSender.send2Bus(userRoleId, InnerCmdType.INNER_ZUOQI_ZPLUS_CHANGE, configZplus);
				}
			}
		}
	}
	
	private void changeShenqiZhuAttr(IRole role,Integer activatedShenqiNum){
		Map<String,Long> shenqiZhufuMap = new HashMap<String,Long>();
		if(role.getZuoQi() != null){
			int zuoqiLevel = role.getZuoQiLevel();
			ShuXingZhuFuConfig config = shuXingZhuFuConfigExportService.getByTypeAndId(ShenQiConstants.ZHUFU_TYPE_ZUOQI, zuoqiLevel);
			if(config != null){
				ObjectUtil.longMapAdd(shenqiZhufuMap, config.getAttrs());
			}
		}
		if(role.getBusinessData().getChibang() != null){
			int chibangLevel = role.getBusinessData().getChibang().getZuoQiLevel();
			ShuXingZhuFuConfig config = shuXingZhuFuConfigExportService.getByTypeAndId(ShenQiConstants.ZHUFU_TYPE_CHIBANG, chibangLevel);
			if(config != null){
				ObjectUtil.longMapAdd(shenqiZhufuMap, config.getAttrs());
			}
		}
		if(role.getBusinessData().getXianjian() != null){
			int xianjianLevel = role.getBusinessData().getXianjian().getXianjianLevel();
			ShuXingZhuFuConfig config = shuXingZhuFuConfigExportService.getByTypeAndId(ShenQiConstants.ZHUFU_TYPE_XIANJIAN, xianjianLevel);
			if(config != null){
				ObjectUtil.longMapAdd(shenqiZhufuMap, config.getAttrs());
			}
		}
		if(role.getBusinessData().getZhanjia() != null){
			int xianjianLevel = role.getBusinessData().getZhanjia().getXianjianLevel();
			ShuXingZhuFuConfig config = shuXingZhuFuConfigExportService.getByTypeAndId(ShenQiConstants.ZHUFU_TYPE_ZHANJIA, xianjianLevel);
			if(config != null){
				ObjectUtil.longMapAdd(shenqiZhufuMap, config.getAttrs());
			}
		}
		
		//新增御剑祝福属性
		if(role.getWuQi() != null){
			int wuqiLevel = role.getWuQiLevel();
			ShuXingZhuFuConfig config = shuXingZhuFuConfigExportService.getByTypeAndId(ShenQiConstants.ZHUFU_TYPE_WUQI, wuqiLevel);
			if(config != null){
				ObjectUtil.longMapAdd(shenqiZhufuMap, config.getAttrs());
			}
		}
		
		ObjectUtil.longMapTimes(shenqiZhufuMap, activatedShenqiNum);
		RoleFactoryHelper.setRoleBaseAttrs(shenqiZhufuMap, role, BaseAttributeType.SHENQI_ZHUFU);
	}
	
	public void changeChiBangAttrs(Long userRoleId, String stageId, ChiBangInfo chiBangInfo,List<Integer> huanhuaConfigIdList, Object[] zqEquips,Integer activatedShenqiNum) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		clearAttrs(role,false,BaseAttributeType.CHIBANG);
		
		ChiBang chibang =  ChiBangUtil.coverToChiBang(chiBangInfo,huanhuaConfigIdList, zqEquips);
		role.getBusinessData().setChibang(chibang);
 
		Map<String, Long>  chiBangAttrs = StageHelper.getChiBangExportService().getChiBangAttrs(userRoleId,chibang);
		
		if(chiBangAttrs != null){
			//通知前段翅膀属性变化
			BusMsgSender.send2One(userRoleId, ClientCmdType.CHIBANG_ATTR_CHANGE, chiBangAttrs);
			//重置翅膀属性
			RoleFactoryHelper.setRoleBaseAttrs(chiBangAttrs, role, BaseAttributeType.CHIBANG);
			//重置神器祝福属性
			changeShenqiZhuAttr(role,activatedShenqiNum);
			role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
			
			Long configZplus = chiBangAttrs.get(EffectType.zplus.name());
			Long busZplus = chiBangInfo.getZplus();
			if(!busZplus.equals(configZplus)){
				//抛出一个异步指令让bus存储翅膀战斗力
				if(KuafuConfigPropUtil.isKuafuServer()){
					KuafuMsgSender.send2KuafuSource(userRoleId, InnerCmdType.INNER_ZPLUS_CHIBANG, configZplus);
				}else{
					StageMsgSender.send2Bus(userRoleId, InnerCmdType.INNER_ZPLUS_CHIBANG, configZplus);
				}
			}
		}
	}
	//器灵
	public void changeQiLingAttrs(Long userRoleId, String stageId, QiLingInfo qiLingInfo,List<Integer> huanhuaConfigIdList, Object[] zqEquips,Integer activatedShenqiNum) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		clearAttrs(role,false,BaseAttributeType.QILING);
		
		QiLing chibang =  QiLingUtil.coverToQiLing(qiLingInfo,huanhuaConfigIdList, zqEquips);
		role.getBusinessData().setQiling(chibang);
 
		Map<String, Long>  qiLingAttrs = StageHelper.getQiLingExportService().getQiLingAttrs(userRoleId,chibang);
		
		if(qiLingAttrs != null){
			//通知前段器灵属性变化
			BusMsgSender.send2One(userRoleId, ClientCmdType.QILING_ATTR_CHANGE, qiLingAttrs);
			//重置器灵属性
			RoleFactoryHelper.setRoleBaseAttrs(qiLingAttrs, role, BaseAttributeType.QILING);
			//重置神器祝福属性
			changeShenqiZhuAttr(role,activatedShenqiNum);
			role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
			
			Long configZplus = qiLingAttrs.get(EffectType.zplus.name());
			Long busZplus = qiLingInfo.getZplus();
			if(!busZplus.equals(configZplus)){
				//抛出一个异步指令让bus存储器灵战斗力
				if(KuafuConfigPropUtil.isKuafuServer()){
					KuafuMsgSender.send2KuafuSource(userRoleId, InnerCmdType.INNER_ZPLUS_QILING, configZplus);
				}else{
					StageMsgSender.send2Bus(userRoleId, InnerCmdType.INNER_ZPLUS_QILING, configZplus);
				}
			}
		}
	}
	//天羽
	public void changeTianYuAttrs(Long userRoleId, String stageId, TianYuInfo tianyuInfo,List<Integer> huanhuaConfigIdList,  Object[] zqEquips,Integer activatedShenqiNum) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		clearAttrs(role,false,BaseAttributeType.TIANYU);
		
		TianYu chibang =  TianYuUtil.coverToTianYu(tianyuInfo,huanhuaConfigIdList, zqEquips);
		role.getBusinessData().setTianyu(chibang);
		
		Map<String, Long>  tianYuAttrs = StageHelper.getTianYuExportService().getTianYuAttrs(userRoleId,chibang);
		
		if(tianYuAttrs != null){
			//通知前段天羽属性变化
			BusMsgSender.send2One(userRoleId, ClientCmdType.TIANYU_ATTR_CHANGE, tianYuAttrs);
			//重置天羽属性
			RoleFactoryHelper.setRoleBaseAttrs(tianYuAttrs, role, BaseAttributeType.TIANYU);
			//重置神器祝福属性
			changeShenqiZhuAttr(role,activatedShenqiNum);
			role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
			
			Pet pet = role.getPet();
			if(pet != null){
				pet.getFightAttribute().clearBaseAttribute(BaseAttributeType.TIANYU, false);
				//重置翅膀属性
				pet.getFightAttribute().setBaseAttribute(BaseAttributeType.TIANYU, tianYuAttrs);
				pet.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
			}
			
			Long configZplus = tianYuAttrs.get(EffectType.zplus.name());
			Long busZplus = tianyuInfo.getZplus();
			if(!busZplus.equals(configZplus)){
				//抛出一个异步指令让bus存储器灵战斗力
				if(KuafuConfigPropUtil.isKuafuServer()){
					KuafuMsgSender.send2KuafuSource(userRoleId, InnerCmdType.INNER_ZPLUS_TIANYU, configZplus);
				}else{
					StageMsgSender.send2Bus(userRoleId, InnerCmdType.INNER_ZPLUS_TIANYU, configZplus);
				}
			}
		}
	}
	public void changeXianJianAttrs(Long userRoleId, String stageId, XianJianInfo xianJianInfo,List<Integer> huanhuaConfigIdList, Object[] zqEquips,Integer activatedShenqiNum) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		clearAttrs(role,false,BaseAttributeType.XIANJIAN);
		
		XianJian xianjian =  XianjianUtil.coverToXianjian(xianJianInfo,huanhuaConfigIdList, zqEquips);
		role.getBusinessData().setXianjian(xianjian);
 
		Map<String, Long>  xianJianAttrs = StageHelper.getXianJianExportService().getXianJianAttrs(userRoleId,xianjian);

		if(xianJianAttrs != null){
			//通知前段仙剑属性变化
			BusMsgSender.send2One(userRoleId, ClientCmdType.XIANJIAN_ATTR_CHANGE, xianJianAttrs);
			//重置玩家翅膀属性
			RoleFactoryHelper.setRoleBaseAttrs(xianJianAttrs, role, BaseAttributeType.XIANJIAN);
			//重置神器祝福属性
			changeShenqiZhuAttr(role,activatedShenqiNum);
			role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
			
			Pet pet = role.getPet();
			if(pet != null){
				pet.getFightAttribute().clearBaseAttribute(BaseAttributeType.XIANJIAN, false);
				//重置翅膀属性
				pet.getFightAttribute().setBaseAttribute(BaseAttributeType.XIANJIAN, xianJianAttrs);
				pet.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
			}
			
			Long configZplus = xianJianAttrs.get(EffectType.zplus.name());
			Long busZplus = xianJianInfo.getZplus();
			if(!busZplus.equals(configZplus)){
				//抛出一个异步指令让bus存储坐骑战斗力
				if(KuafuConfigPropUtil.isKuafuServer()){
					KuafuMsgSender.send2KuafuSource(userRoleId, InnerCmdType.INNER_ZPLUS_XIANJIAN, configZplus);
				}else{
					StageMsgSender.send2Bus(userRoleId, InnerCmdType.INNER_ZPLUS_XIANJIAN, configZplus);
				}
			}
		}
	}
	
	public void changeZhanJiaAttrs(Long userRoleId, String stageId, ZhanJiaInfo xianJianInfo,List<Integer> huanhuaConfigIdList, Object[] zqEquips,Integer activatedShenqiNum) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		clearAttrs(role,false,BaseAttributeType.ZHANJIA);
		
		ZhanJia xianjian =  ZhanJiaUtil.coverToXianjian(xianJianInfo, huanhuaConfigIdList,zqEquips);
		role.getBusinessData().setZhanjia(xianjian);
		
		Map<String, Long>  xianJianAttrs = StageHelper.getZhanJiaExportService().getXianJianAttrs(userRoleId,xianjian);
		
		if(xianJianAttrs != null){
			//通知前段仙剑属性变化
			BusMsgSender.send2One(userRoleId, ClientCmdType.ZHANJIA_ATTR_CHANGE, xianJianAttrs);
			//重置玩家翅膀属性
			RoleFactoryHelper.setRoleBaseAttrs(xianJianAttrs, role, BaseAttributeType.ZHANJIA);
			//重置神器祝福属性
			changeShenqiZhuAttr(role,activatedShenqiNum);
			role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
			
			Pet pet = role.getPet();
			if(pet != null){
				pet.getFightAttribute().clearBaseAttribute(BaseAttributeType.ZHANJIA, false);
				//重置翅膀属性
				pet.getFightAttribute().setBaseAttribute(BaseAttributeType.ZHANJIA, xianJianAttrs);
				pet.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
			}
			
			Long configZplus = xianJianAttrs.get(EffectType.zplus.name());
			Long busZplus = xianJianInfo.getZplus();
			if(!busZplus.equals(configZplus)){
				//抛出一个异步指令让bus存储坐骑战斗力
				if(KuafuConfigPropUtil.isKuafuServer()){
					KuafuMsgSender.send2KuafuSource(userRoleId, InnerCmdType.INNER_ZPLUS_ZHANJIA, configZplus);
				}else{
					StageMsgSender.send2Bus(userRoleId, InnerCmdType.INNER_ZPLUS_ZHANJIA, configZplus);
				}
			}
		}
	}
	public void changeLingHuoAttrs(Long userRoleId, String stageId,Integer lingHuoId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		clearAttrs(role,false,BaseAttributeType.LINGHUO);
		
		role.getBusinessData().setLingHuo(lingHuoId);
		
		Map<String, Long>  lingHuoAttrs = StageHelper.getLingHuoExportService().getLingHuoAttrs(userRoleId,lingHuoId);
		
		if(lingHuoAttrs != null){
			//重置玩家灵火属性
			RoleFactoryHelper.setRoleBaseAttrs(lingHuoAttrs, role, BaseAttributeType.LINGHUO);
			role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
			
		}
	}
	public void changeChengJiuAttrs(Long userRoleId, String stageId,String cjId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		clearAttrs(role,false,BaseAttributeType.CHENGJIU);
		
		Map<String, Long>  chengjiuAttrs = StageHelper.getRoleChengJiuExportService().getChengJiuAttrsKf(userRoleId,cjId);
		
		if(chengjiuAttrs != null){
			//重置玩家灵火属性
			RoleFactoryHelper.setRoleBaseAttrs(chengjiuAttrs, role, BaseAttributeType.CHENGJIU);
			role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
			
		}
	}
	public void changeLingJingAttrs(Long userRoleId, String stageId,Map<String,Long> lingjingAttribute) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		role.getFightAttribute().setBaseAttribute(BaseAttributeType.LINGJING, lingjingAttribute);
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
	
	/**
	 * 上下翅膀
	 * @param roleId
	 * @param stageId
	 * @param isGetOn
	 */
	public void chibangState(Long userRoleId, String stageId, boolean isGetOn) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return ;
		}
		Role role = (Role)stage.getElement(userRoleId,ElementType.ROLE);
		if(role == null ){
			return ;
		}
		
		if(role.getBusinessData().getChibang() == null) { return; }
		
//		if(isGetOn) {
//			int  showId = role.getBusinessData().getChibang().getShowId();
//			StageMsgSender.send2Many(role.getStage().getSurroundRoleIds(role.getPosition()), ClientCmdType.ZUOQI_UP, new Object[]{userRoleId,showId});
//		}else {
//			StageMsgSender.send2Many(role.getStage().getSurroundRoleIds(role.getPosition()), ClientCmdType.ZUOQI_DOWN, userRoleId);
//		}
		
		role.getBusinessData().getChibang().setGetOn(isGetOn);
	}
	
	/**
	 * 上下仙剑
	 * @param roleId
	 * @param stageId
	 * @param isGetOn
	 */
	public void xianjianState(Long userRoleId, String stageId, boolean isGetOn) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return ;
		}
		Role role = (Role)stage.getElement(userRoleId,ElementType.ROLE);
		if(role == null ){
			return ;
		}
		
		if(role.getBusinessData().getXianjian() == null) { return; }
		
//		if(isGetOn) {
//			int  showId = role.getBusinessData().getChibang().getShowId();
//			StageMsgSender.send2Many(role.getStage().getSurroundRoleIds(role.getPosition()), ClientCmdType.ZUOQI_UP, new Object[]{userRoleId,showId});
//		}else {
//			StageMsgSender.send2Many(role.getStage().getSurroundRoleIds(role.getPosition()), ClientCmdType.ZUOQI_DOWN, userRoleId);
//		}
		
		role.getBusinessData().getXianjian().setGetOn(isGetOn);
	}
	
	/**
	 * 上下战甲
	 * @param roleId
	 * @param stageId
	 * @param isGetOn
	 */
	public void zhanjiaState(Long userRoleId, String stageId, boolean isGetOn) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return ;
		}
		Role role = (Role)stage.getElement(userRoleId,ElementType.ROLE);
		if(role == null ){
			return ;
		}
		
		if(role.getBusinessData().getZhanjia() == null) { return; }
		
		role.getBusinessData().getZhanjia().setGetOn(isGetOn);
	}
	
	
	/**
	 * 糖宝属性变化
	 * @param stageId
	 * @param userRoleId
	 * @param attrs
	 */
	public void petBaseAttrChange(String stageId, Long userRoleId, Map<String,Long> attrs) {
		if(attrs == null || attrs.size() ==0){
			return;
		}
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		Pet pet = role.getPet();
		if(pet == null){
			return;
		}
		Long harm = attrs.remove(GameConstants.PET_HARM_KEY);
		if(harm == null || harm < 1){
			harm = 1l;
		}
		pet.setHarm(harm);
		pet.getFightAttribute().setBaseAttribute(BaseAttributeType.LEVEL, attrs);
		pet.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		StageMsgSender.send2One(role.getId(), ClientCmdType.TANGBAO_MAX_HP, pet.getFightAttribute().getMaxHp());
	}
	
	/**
	 * 糖宝给主人增加属性变化
	 * @param stageId
	 * @param userRoleId
	 * @param attrs
	 */
	public void petOwnerAttChange(String stageId, Long userRoleId, Map<String,Long> attrs) {
		if(attrs == null || attrs.size() ==0){
			return;
		}
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		RoleFactoryHelper.setRoleBaseAttrs(attrs, role, BaseAttributeType.PET_EAT);
		//输出变化
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
	
	/**
	 * 背包仓库开格子增加属性
	 * @param stageId
	 * @param userRoleId
	 * @param slot
	 */
	public void innerSlotAttrs(String stageId, Long userRoleId, Map<String,Long> attrs) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		if(attrs == null || attrs.size() ==0) return;
		
		RoleFactoryHelper.setRoleBaseAttrs(attrs, role, BaseAttributeType.BAG_SLOG);
		//输出变化
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
	
	/**
	 * 坐骑外显信息变化
	 * @param roleId
	 * @param stageId
	 * @param showId
	 */
	public void zuoqiShowUdate(Long userRoleId, String stageId, int showId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		//修改坐骑外显
		role.getZuoQi().setShowId(showId);
		
		//通知周边坐骑外显信息变化
		if(role.getZuoQi().isGetOn()){ 
			StageMsgSender.send2Many(role.getStage().getSurroundRoleIds(role.getPosition()), ClientCmdType.ZUOQI_UP, new Object[]{userRoleId,showId});
		}
		
	}
	
	/**
	 * 圣剑外显信息变化
	 * @param roleId
	 * @param stageId
	 * @param showId
	 */
	public void wuqiShowUpdate(Long userRoleId, String stageId, int showId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		//修改坐骑外显
		role.getWuQi().setShowId(showId);
		
//		//通知周边坐骑外显信息变化 看配置,是否开启通知
//		StageMsgSender.send2Many(role.getStage().getSurroundRoleIds(role.getPosition()), ClientCmdType.WUQI_SHOWID, new Object[]{userRoleId,showId});
	}
	
	/**
	 * 翅膀外显信息变化
	 * @param roleId
	 * @param stageId
	 * @param showId
	 */
	public void chiBangShowUdate(Long userRoleId, String stageId, int showId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		ChiBang chibang = role.getBusinessData().getChibang();
		if(chibang == null) { return;}
		
		//修改坐骑外显
		chibang.setShowId(showId);
		
//		//通知周边坐骑外显信息变化
//		if(chibang.isGetOn()){ 
//			StageMsgSender.send2Many(role.getStage().getSurroundRoleIds(role.getPosition()), ClientCmdType.CHIBANG_UP, new Object[]{userRoleId,showId});
//		}
	}
	
	/**
	 * 	器灵外显信息变化
	 * @param roleId
	 * @param stageId
	 * @param showId
	 */
	public void qilingShowUdate(Long userRoleId, String stageId, int showId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		QiLing qiling = role.getBusinessData().getQiling();
		if(qiling == null) { return;}
		
		//修改器灵外显
		qiling.setShowId(showId);
		//通知周边器灵外显信息变化
		if(role.getBusinessData().getQiling().isGetOn() && role.getPet() != null){ 
			StageMsgSender.send2Many(role.getStage().getSurroundRoleIds(role.getPosition()), ClientCmdType.QILING_SHOW_REFRESH, new Object[]{userRoleId,showId});
		}
	}
	/**
	 * 	天羽外显信息变化
	 * @param roleId
	 * @param stageId
	 * @param showId
	 */
	public void tianyuShowUdate(Long userRoleId, String stageId, int showId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		TianYu tianyu = role.getBusinessData().getTianyu();
		if(tianyu == null) { return;}
		
		//修改天羽外显
		tianyu.setShowId(showId);
		//通知周边天羽外显信息变化
		if(role.getBusinessData().getTianyu().isGetOn() && role.getPet() != null){ 
			StageMsgSender.send2Many(role.getStage().getSurroundRoleIds(role.getPosition()), ClientCmdType.TIANYU_SHOW_REFRESH, new Object[]{userRoleId,showId});
		}
	}
	/**
	 * 仙剑外显信息变化
	 * @param roleId
	 * @param stageId
	 * @param showId
	 */
	public void xianjianShowUdate(Long userRoleId, String stageId, int showId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		//修改仙剑外显
		role.getBusinessData().getXianjian().setShowId(showId);
		
		//通知周边仙剑外显信息变化
		if(role.getBusinessData().getXianjian().isGetOn() && role.getPet() != null){ 
			StageMsgSender.send2Many(role.getStage().getSurroundRoleIds(role.getPosition()), ClientCmdType.XIANJIAN_SHOW_REFRESH, new Object[]{role.getPet().getId(),showId});
		}
		
	}
	/**
	 * 战甲外显信息变化
	 * @param roleId
	 * @param stageId
	 * @param showId
	 */
	public void zhanjiaShowUdate(Long userRoleId, String stageId, int showId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		//修改仙剑外显
		role.getBusinessData().getZhanjia().setShowId(showId);
		
		//通知周边仙剑外显信息变化
		if(role.getBusinessData().getZhanjia().isGetOn() && role.getPet() != null){ 
			StageMsgSender.send2Many(role.getStage().getSurroundRoleIds(role.getPosition()), ClientCmdType.ZHANJIA_SHOW_REFRESH, new Object[]{role.getPet().getId(),showId});
		}
		
	}
	
	/**
	 * 刷新圣剑属性
	 * @param roleId
	 * @param stageId
	 */
	public void refreshWqAttrs(Long roleId, String stageId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(roleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		WuQi wuQi = role.getWuQi();
		if(wuQi != null){
			//如果有属性变化才刷新
			XinShengJianJiChuConfig shengJianConfig = StageConfigureHelper.getXinShengJianJiChuConfigExportService().loadById(wuQi.getWuqiLevel());
			Integer maxLevel = shengJianConfig.getLevelmax();
			
			if(role.getLevel() <= maxLevel){
				Map<String, Long>  wuQiAttrs = StageHelper.getWuQiExportService().getWuQiAttrs(roleId,wuQi,role.getLevel());
				if(wuQiAttrs != null){
					//重置坐骑属性
					RoleFactoryHelper.setRoleBaseAttrs(wuQiAttrs, role, BaseAttributeType.WUQI);
					role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
					
					Long configZplus = wuQiAttrs.get(EffectType.zplus.name());
					Long busZplus = wuQi.getZplus();
					if(!busZplus.equals(configZplus)){
						//抛出一个异步指令让bus存储新圣剑战斗力
						if(KuafuConfigPropUtil.isKuafuServer()){
							KuafuMsgSender.send2KuafuSource(roleId, InnerCmdType.INNER_WUQI_ZPLUS_CHANGE, configZplus);
						}else{
							StageMsgSender.send2Bus(roleId, InnerCmdType.INNER_WUQI_ZPLUS_CHANGE, configZplus);
						}
					}
				}
			} 
			
		}
	}
	
	/**
	 * 刷新坐骑属性
	 * @param roleId
	 * @param stageId
	 */
	public void refreshZqAttrs(Long roleId, String stageId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(roleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		ZuoQi zuoQi = role.getZuoQi();
		if(zuoQi != null){
			//如果有属性变化才刷新
			YuJianJiChuConfig yuJianConfig = StageConfigureHelper.getYuJianJiChuConfigExportService().loadById(zuoQi.getZuoQiLevel());
			Integer maxLevel = yuJianConfig.getLevelmax();
			
			if(role.getLevel() <= maxLevel){
				Map<String, Long>  zuoQiAttrs = StageHelper.getZuoQiExportService().getZuoQiAttrs(roleId,zuoQi,role.getLevel());
				if(zuoQiAttrs != null){
					//重置坐骑属性
					RoleFactoryHelper.setRoleBaseAttrs(zuoQiAttrs, role, BaseAttributeType.ZUOQI);
					role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
					
					Long configZplus = zuoQiAttrs.get(EffectType.zplus.name());
					Long busZplus = zuoQi.getZplus();
					if(!busZplus.equals(configZplus)){
						//抛出一个异步指令让bus存储坐骑战斗力
						if(KuafuConfigPropUtil.isKuafuServer()){
							KuafuMsgSender.send2KuafuSource(roleId, InnerCmdType.INNER_ZUOQI_ZPLUS_CHANGE, configZplus);
						}else{
							StageMsgSender.send2Bus(roleId, InnerCmdType.INNER_ZUOQI_ZPLUS_CHANGE, configZplus);
						}
					}
				}
			} 
			
		}
	}
	
	/**
	 * 新增宠物
	 * @param userRoleId
	 */
	public void addPet(Long userRoleId, String stageId,Map<String, Long> attribute){
		if(attribute == null || attribute.size() < 1){
			return;
		}

		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		if(role.getPet() != null){
			return;
		}
		Map<String,Long> xianjianAttr = null;
		if(role.getBusinessData().getXianjian()!=null){
			xianjianAttr = StageHelper.getXianJianExportService().getXianJianAttrs(userRoleId,role.getBusinessData().getXianjian());
		}
		Map<String,Long> zhanjiaAttr = null;
		if(role.getBusinessData().getZhanjia()!=null){
			zhanjiaAttr = StageHelper.getZhanJiaExportService().getXianJianAttrs(userRoleId,role.getBusinessData().getZhanjia());
		}
		Map<String,Long> xinwenAttr = null;
		if(role.getBusinessData().getTangbaoXinwenJie()>0){
			xinwenAttr = StageHelper.getXinwenExportService().getXinwenAttributeAfterLogin(userRoleId);
		}
		Map<String,Long> tianyuAttr = null;
		if(role.getBusinessData().getTianyu()!=null){
			tianyuAttr = StageHelper.getTianYuExportService().getTianYuAttrs(userRoleId, role.getBusinessData().getTianyu());
		}
        Pet pet = PetFactory.create(role, attribute, true, null);
        // 添加糖宝仙剑属性
        if (null != xianjianAttr) {
            pet.getFightAttribute().initBaseAttribute(BaseAttributeType.XIANJIAN, xianjianAttr);
        }
        // 添加糖宝战甲属性
        if (null != zhanjiaAttr) {
            pet.getFightAttribute().initBaseAttribute(BaseAttributeType.ZHANJIA, zhanjiaAttr);
        }
        // 添加糖宝心纹属性
        if (null != xinwenAttr) {
            pet.getFightAttribute().initBaseAttribute(BaseAttributeType.TANGBAO_XINWEN, xinwenAttr);
        }
        // 添加糖宝天羽属性
        if (null != tianyuAttr) {
            pet.getFightAttribute().initBaseAttribute(BaseAttributeType.TIANYU, tianyuAttr);
        }
        pet.getFightAttribute().refresh();
		dataContainer.putData(GameConstants.COMPONENT_NAME_PET, role.getId().toString(),pet.getPetVo());
		role.setPet(pet);
		if(stage.isCanHasTangbao()){
			stage.enter(pet, role.getPosition().getX(), role.getPosition().getY());
		}
	}
	/**
	 * 定时增加怒气
	 * @param userRoleId
	 */
	public void timeAddNuqi(Long userRoleId, String stageId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		role.startNuqiSchedule(false);
	}
	/**
	 * 刷新玩家神器
	 * @param userRoleId
	 */
	public void shenqiRefresh(Long userRoleId, String stageId,Integer shenqiId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		if(role.getBusinessData().getShenqiId() != null && role.getBusinessData().getShenqiId() > 0){
			ISkill skill = SkillManager.getManager().getSkill(role.getBusinessData().getShenqiSkillId());
			if(skill != null){
				role.removeSkill(skill.getCategory());
			}
			ISkill skill2 = SkillManager.getManager().getSkill(role.getBusinessData().getShenqiSkillId2());
			if(skill2 != null){
				role.removeSkill(skill2.getCategory());
			}
		}
		role.getBusinessData().setShenqiId(shenqiId);
		if(shenqiId.intValue() != 0 ){
			ShenQiShuXingConfig shenqiConfig = shenQiShuXingConfigExportService.loadById(shenqiId);
			if(shenqiConfig != null){
				role.getBusinessData().setShenqiSkillId(shenqiConfig.getSkill());
				ISkill skill = SkillManager.getManager().getSkill(shenqiConfig.getSkill());
				if(skill != null){
					role.addSkill(skill);
				}
				role.getBusinessData().setShenqiSkillId2(shenqiConfig.getSkill2());
				ISkill skill2 = SkillManager.getManager().getSkill(shenqiConfig.getSkill2());
				if(skill2 != null){
					role.addSkill(skill2);
				}
			}
		}else{
			role.getBusinessData().setShenqiSkillId(null);
			role.cancelShenqiAttackSchedule();
		}
		StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.REFRESH_SHENQI, new Object[]{userRoleId,shenqiId});
	}
	/**
	 * 刷新玩家五行属性
	 * @param userRoleId
	 */
	public void wuxingfuti(Long userRoleId, String stageId,Integer id,Integer wuxingType){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		role.getBusinessData().setWuxingId(id);
		role.getBusinessData().setWuxingType(wuxingType);

		//StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.REFRESH_SHENQI, new Object[]{userRoleId,shenqiId});
	}
	/**
	 * 怒气大招定时
	 * @param userRoleId
	 */
	public void nuqiSkillSchedule(Long userRoleId, String stageId,String skillId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		role.nuqiSkill(skillId);
	}
	
	/**
	 * 玩家激活神器，属性加成
	 * @param userRoleId
	 */
	public void shenqiActivate(Long userRoleId, String stageId,Integer shenqiId,Integer shenqiNum){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		Map<String,Long> shenqiZhufuMap = new HashMap<String,Long>();
		if(role.getZuoQi() != null){
			int zuoqiLevel = role.getZuoQiLevel();
			ShuXingZhuFuConfig config = shuXingZhuFuConfigExportService.getByTypeAndId(ShenQiConstants.ZHUFU_TYPE_ZUOQI, zuoqiLevel);
			if(config != null){
				ObjectUtil.longMapAdd(shenqiZhufuMap, config.getAttrs());
			}
		}
		if(role.getBusinessData().getChibang() != null){
			int chibangLevel = role.getBusinessData().getChibang().getZuoQiLevel();
			ShuXingZhuFuConfig config = shuXingZhuFuConfigExportService.getByTypeAndId(ShenQiConstants.ZHUFU_TYPE_CHIBANG, chibangLevel);
			if(config != null){
				ObjectUtil.longMapAdd(shenqiZhufuMap, config.getAttrs());
			}
		}
		if(role.getBusinessData().getXianjian() != null){
			int xianjianLevel = role.getBusinessData().getXianjian().getXianjianLevel();
			ShuXingZhuFuConfig config = shuXingZhuFuConfigExportService.getByTypeAndId(ShenQiConstants.ZHUFU_TYPE_XIANJIAN, xianjianLevel);
			if(config != null){
				ObjectUtil.longMapAdd(shenqiZhufuMap, config.getAttrs());
			}
		}
		if(role.getBusinessData().getZhanjia() != null){
			int xianjianLevel = role.getBusinessData().getZhanjia().getXianjianLevel();
			ShuXingZhuFuConfig config = shuXingZhuFuConfigExportService.getByTypeAndId(ShenQiConstants.ZHUFU_TYPE_ZHANJIA, xianjianLevel);
			if(config != null){
				ObjectUtil.longMapAdd(shenqiZhufuMap, config.getAttrs());
			}
		}
		if(role.getWuQi() != null){
			int wuqiLevel = role.getWuQiLevel();
			ShuXingZhuFuConfig config = shuXingZhuFuConfigExportService.getByTypeAndId(ShenQiConstants.ZHUFU_TYPE_WUQI, wuqiLevel);
			if(config != null){
				ObjectUtil.longMapAdd(shenqiZhufuMap, config.getAttrs());
			}
		}
		
		ObjectUtil.longMapTimes(shenqiZhufuMap, shenqiNum);
		RoleFactoryHelper.setRoleBaseAttrs(shenqiZhufuMap, role, BaseAttributeType.SHENQI_ZHUFU);
		//此神器的属性加成
		ShenQiShuXingConfig config = shenQiShuXingConfigExportService.loadById(shenqiId);
		role.getFightAttribute().incrBaseAttribute(BaseAttributeType.SHENQI, config.getAttrs());
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
	
	public void chooseTarget(String stageId,Long userRoleId, Long targetId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		role.getBusinessData().setShenqiTargetId(targetId);
	}
	
	public void kuafuHpHuifu(Long userRoleId,String stageId, Integer hp){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		role.getFightAttribute().setCurHp(role.getFightAttribute().getCurHp() + hp);
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
	
	/**
	 * 玩家激活称号，属性加成
	 * @param userRoleId
	 */
	public void chenghaoActivate(Long userRoleId, String stageId,Integer chenghaoId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		ChengHaoPeiZhiConfig config = chengHaoPeiZhiConfigExportService.loadById(chenghaoId);
		if(config == null){
			return;
		}
		role.getFightAttribute().incrBaseAttribute(BaseAttributeType.CHENGHAO, config.getAttrs());
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
	
	/**
	 * 玩家称号穿戴
	 * @param userRoleId
	 */
	public void chenghaoRefresh(Long userRoleId, String stageId,Boolean wear,Integer oldChenghaoId,Integer chenghaoId,String res, Object[] refreshData){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		ChengHaoPeiZhiConfig config = chengHaoPeiZhiConfigExportService.loadById(chenghaoId);
		if(config == null){
			return;
		}
		if(wear){
			if(role.getBusinessData().addChenghao(chenghaoId,res)){
				if(config.getExtraAttrs() != null){
					role.getFightAttribute().incrBaseAttribute(BaseAttributeType.CHENGHAO, config.getExtraAttrs());
				}
				if(oldChenghaoId != null){
					role.getBusinessData().removeChenghao(oldChenghaoId);
					ChengHaoPeiZhiConfig oldConfig = chengHaoPeiZhiConfigExportService.loadById(oldChenghaoId);
					if(oldConfig.getExtraAttrs() != null){
						role.getFightAttribute().descBaseAttribute(BaseAttributeType.CHENGHAO, oldConfig.getExtraAttrs());
					}
				}
			}
		}else{
			if(role.getBusinessData().removeChenghao(chenghaoId)){
				if(config.getExtraAttrs()!=null){
					role.getFightAttribute().descBaseAttribute(BaseAttributeType.CHENGHAO, config.getExtraAttrs());
				}
			}
		}
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		
        if (null != refreshData) {
            StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.REFRESH_CHENGHAO, refreshData);
        }
	}
	/**
	 * 玩家称号过期，属性减少
	 * @param userRoleId
	 */
	public void chenghaoRemove(Long userRoleId, String stageId,Integer chenghaoId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		ChengHaoPeiZhiConfig config = chengHaoPeiZhiConfigExportService.loadById(chenghaoId);
		if(config == null){
			return;
		}
		role.getFightAttribute().descBaseAttribute(BaseAttributeType.CHENGHAO, config.getAttrs());
		if(role.getBusinessData().removeChenghao(chenghaoId)){
			if(config.getExtraAttrs()!=null){
				role.getFightAttribute().descBaseAttribute(BaseAttributeType.CHENGHAO, config.getExtraAttrs());
			}
		}
        role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
	
	public void yaoshenAttrChange(Long userRoleId,String stageId,Integer jie,Integer ceng,Integer qndNum,Integer czdNum){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		Map<String,Long> attr =  yaoshenExportService.getYaoshenAttribute(jie, ceng,qndNum,czdNum);
		if(attr == null){
			return ;
		}
		role.getFightAttribute().setBaseAttribute(BaseAttributeType.YAOSHEN, attr);
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_ATTR_CHANGE, attr);
	}
	/**
	 * 妖神魔纹
	 * @param userRoleId
	 * @param stageId
	 * @param jie
	 * @param ceng
	 * @param qndNum
	 * @param czdNum
	 */
	public void yaoshenMowenAttrChange(Long userRoleId,String stageId,Integer jie,Integer ceng,Integer qndNum,Integer czdNum){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		Map<String,Long> attr =  yaoshenExportService.getYaoshenMowenAttribute(jie, ceng,qndNum,czdNum);
		if(attr == null){
			return ;
		}
		role.getFightAttribute().setBaseAttribute(BaseAttributeType.YAOSHEN_MOWEN, attr);
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_MOWEN_ATTR_CHANGE, attr);
	}
	
	/**
	 * 糖宝心纹
	 * @param userRoleId
	 * @param stageId
	 * @param jie
	 * @param ceng
	 * @param qndNum
	 * @param czdNum
	 */
	public void tangbaoXinwenAttrChange(Long userRoleId,String stageId,Integer jie,Integer ceng,Integer qndNum,Integer czdNum){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		} 
		Map<String,Long> attr =  xinwenExportService.getXinwenAttribute(jie, ceng,qndNum,czdNum);
		if(attr == null){
			return ;
		}
		Pet pet = role.getPet();
		if(pet != null){
			pet.getFightAttribute().clearBaseAttribute(BaseAttributeType.TANGBAO_XINWEN, false);
			//重置糖宝心纹属性
			pet.getFightAttribute().setBaseAttribute(BaseAttributeType.TANGBAO_XINWEN, attr);
			pet.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		}
		role.getBusinessData().setTangbaoXinwenJie(jie);//设置阶数
		role.getFightAttribute().setBaseAttribute(BaseAttributeType.TANGBAO_XINWEN, attr);
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		BusMsgSender.send2One(userRoleId, ClientCmdType.XINWEN_ATTR_CHANGE, attr);
	}
	
	/**
	 * 妖神魂魄属性总和
	 * @param userRoleId
	 * @param stageId
	 * @param jie
	 * @param ceng
	 * @param qndNum
	 * @param czdNum
	 */
	public void yaoshenHunpoAttrChange(Long userRoleId,String stageId,Integer jie,Integer ceng,Integer qndNum,Integer czdNum){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		Map<String,Long> attr =  yaoshenHunpoExportService.getYaoshenHunpoAttribute(jie, ceng,qndNum,czdNum);
		if(attr == null){
			return ;
		}
		role.getFightAttribute().setBaseAttribute(BaseAttributeType.YAOSHEN_HUNPO, attr);
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_HUNPO_ATTR_CHANGE, attr);
	}
	/**
	 * 妖神魔印属性总和
	 * @param userRoleId
	 * @param stageId
	 * @param jie
	 * @param ceng
	 * @param qndNum
	 * @param czdNum
	 */
	public void yaoshenMoYinAttrChange(Long userRoleId,String stageId,Integer jie,Integer ceng,Integer qndNum,Integer czdNum){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}  
		Map<String,Long> attr = yaoshenMoYinExportService.getYaoshenHunpoAttribute(jie, ceng,qndNum,czdNum);
		if(attr == null){
			return ;
		}
		role.getFightAttribute().setBaseAttribute(BaseAttributeType.YAOSHEN_MOYIN, attr);
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_MOYIN_ATTR_CHANGE, attr);
	}
	/**
	 * 妖神附魔属性总和
	 * @param userRoleId
	 * @param stageId
	 * @param jie
	 * @param ceng
	 * @param qndNum
	 * @param czdNum
	 */
	public void yaoshenFumoAttrChange(Long userRoleId,String stageId, Map<String, Long> attr){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		} 
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		} 
		if(attr == null || attr.isEmpty()){
			return ;
		}
		role.getFightAttribute().setBaseAttribute(BaseAttributeType.YAOSHEN_FUMO, attr);
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_FUMO_ATTR_CHANGE, attr);
	}
	
	/**
	 * 成神升级
	 * @param userRoleId
	 * @param stageId
	 * @param level
	 */
	public void chengShenAttrChange(Long userRoleId,String stageId,int level){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		} 
		Map<String,Long> attr = chengShenExportService.getAttrMapByLevel(level);
		if(attr == null){
			return ;
		}
		role.getFightAttribute().setBaseAttribute(BaseAttributeType.CHENG_SHEN, attr);
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
	/**
	 * 通天之路
	 * @param userRoleId
	 * @param stageId
	 * @param level
	 */
	public void tongtianLoadAttrChange(Long userRoleId,String stageId,Map<String, Long> attr){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		} 
		role.getFightAttribute().setBaseAttribute(BaseAttributeType.TONGTIAN_ROAD, attr);
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
	/**
	 * 魄神所有属性总和
	 * @param userRoleId
	 * @param stageId
	 */
	public void yaoshenHunpoAttrChange(Long userRoleId,String stageId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		Map<String,Long> attr =  yaoshenHunpoExportService.getHunpoAllPoshenAttribute(userRoleId);
		if(attr == null){
			return ;
		}
		role.getFightAttribute().setBaseAttribute(BaseAttributeType.YAOSHEN_HUNPO_POSHEN, attr);
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
//		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_HUNPO_TAIGUANG_ATTR_CHANGE, attr);	
	}
	
	public void yaoshenShowChange(Long userRoleId,String stageId,Integer isShow){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		boolean isYaoshen = isShow == YaoshenConstants.YAOSHEN_SHOW_YES;
		role.setYaoshen(isYaoshen);
		StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.YAOSHEN_SHOW_UPDATE, new Object[]{userRoleId,isShow.intValue()==YaoshenConstants.YAOSHEN_SHOW_YES});
	}
	public void jewelAttrChange(int type, Map<String, Long> attr,Long userRoleId,String stageId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		if(type==0){//卸下宝石
			role.getFightAttribute().descBaseAttribute(BaseAttributeType.JEWEL,attr);
		}else{ //嵌上宝石
			role.getFightAttribute().incrBaseAttribute(BaseAttributeType.JEWEL, attr);
		}
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
	public void shenQiWashChange(Map<String, Long> attr,Long userRoleId,String stageId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		if(attr==null || attr.size()<0){
			return;
		}
		role.getFightAttribute().setBaseAttribute(BaseAttributeType.SHENQI, attr);
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		
	}
	
	public void shizhuangActiveChange(Map<String, Long> attr,Long userRoleId,String stageId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		if(null == attr){
            return;
        }
		role.getFightAttribute().setBaseAttribute(BaseAttributeType.SHIZHUANG_ACTIVE, attr);
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
	public void shizhuangLevelChange(Map<String, Long> attr,Long userRoleId,String stageId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		if(null == attr){
		    return;
		}
		role.getFightAttribute().setBaseAttribute(BaseAttributeType.SHIZHUANG_SHENGJI, attr);
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
	
	public void shizhuangJinjieChange(Map<String, Long> attr,Long userRoleId,String stageId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		if(null == attr){
		    return;
		}
		role.getFightAttribute().setBaseAttribute(BaseAttributeType.SHIZHUANG_JINJIE, attr);
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
	public void shizhuangChange(Long userRoleId,String stageId,Integer id) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		role.setShiZhuang(id);

		StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.ROLE_SHOW_CHANGE, new Object[]{userRoleId,id});
	}
	
	public void chongwuAttrChange(Long roleId,String stageId,Map<String, Long> attr){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(roleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		if(attr == null){
			return ;
		}
		role.getFightAttribute().setBaseAttribute(BaseAttributeType.CHONGWU, attr);
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
	
	public void chongwuSkillAttrChange(Long roleId, String stageId, Map<String, Long> attr) {
        IStage stage = StageManager.getStage(stageId);
        if (stage == null) {
            return;
        }

        IRole role = (IRole) stage.getElement(roleId, ElementType.ROLE);
        if (role == null) {
            return;
        }
        if (attr == null) {
            return;
        }
        role.getFightAttribute().setBaseAttribute(BaseAttributeType.CHONGWU_SKILL, attr);
        role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
    }
	
	public void chongwuShowUpdate(Long roleId,String stageId,Long id,String name,Integer configId,Integer speed){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IRole role = (IRole)stage.getElement(roleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		Chongwu oldCw = role.getBusinessData().getChongwu();
		if(id == null){
			if(oldCw!=null){
				stage.leave(oldCw);
			}
			role.getBusinessData().setChongwu(null);
		}else{
			Chongwu cw = new Chongwu(id, name);
			cw.setConfigId(configId);
			cw.setStage(stage);
			cw.setSpeed(speed);
			cw.setRole(role);
			int x = role.getPosition().getX();
			int y = role.getPosition().getY();
			if(oldCw!=null){
				Point oldCwPoint = oldCw.getPosition();
				if(oldCwPoint!=null){
					x = oldCwPoint.getX();
					y = oldCwPoint.getY();
				}
				stage.leave(oldCw);
			}
			if(stage.isCanHasChongwu()){
				stage.enter(cw,x, y);
			}
			role.getBusinessData().setChongwu(cw);
		}
	}
	
	public void chongwuMoveSyn(Long userRoleId,String stageId, Integer x, Integer y,Long chongwuGuid) {
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;
		}
		Chongwu cw = role.getBusinessData().getChongwu();
		if ( cw == null) {
			return;
		} 
		if(cw.getStage()==null){
			return;
		}
		if(!cw.getStage().getId().equals(stageId)){
			return;
		}
		if(cw.getRole()==null){
			return;
		}
		if(!cw.getId().equals(chongwuGuid)){
			ChuanQiLog.error("chongwu owner error {} {}",cw.getId(),chongwuGuid);
			return;
		}
		stage.moveTo(cw, x, y);
		if(KuafuConfigPropUtil.isKuafuServer()){
			KuafuMsgSender.send2Many(stage.getSurroundRoleIds(cw.getPosition()), ClientCmdType.BEHAVIOR_MOVE, new Object[]{cw.getId(),x,y});
		}else{
			StageMsgSender.send2Many(stage.getSurroundRoleIds(cw.getPosition()), ClientCmdType.BEHAVIOR_MOVE, new Object[]{cw.getId(),x,y});
		}
		
	}
	
	public void changeRoleAtt(Long userRoleId,String stageId,Integer type,Map<String,Long> atts){
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;
		}
		BaseAttributeType baseAttributeType = BaseAttributeType.getBaseAttributeTypeByVal(type);
		if(baseAttributeType == null){
			return;
		}
		if(atts == null || atts.size() < 1){
			role.getFightAttribute().clearBaseAttribute(baseAttributeType, true);
		}else{
			role.getFightAttribute().setBaseAttribute(baseAttributeType, atts);
		}
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
	
	public void changePeiou(Long userRoleId,String stageId,String peiou){
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;
		}
		role.setPeiou(peiou);
	}
	public void changeXinwu(Long userRoleId,String stageId,Integer xinwu){
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;
		}
		role.setXinwu(xinwu);
	}
	
	public void cancelNoticeSkills(Long userRoleId,String stageId,String skillId){
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;
		}
		Set<String> set = role.getNoticeSkills();
		if(set == null){
			return;
		}
		set.remove(skillId);
	}
	
	public void addNoticeSkills(Long userRoleId,String stageId,List<String> skillIds){
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;
		}
		Set<String> set = role.getNoticeSkills();
		if(set == null){
			return;
		}
		for (String skillId : skillIds) {
			set.add(skillId);
		}
	}
	
	
	public void zhuanshengLevelChange(Long userRoleId,String stageId,Integer level){
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;
		}
		ZhuanShengConfig config = zhuanShengConfigExportService.loadByLevel(level);
		if(config == null){
			return;
		}
		role.getBusinessData().setZhuanShengLevel(level);
		role.getFightAttribute().setBaseAttribute(BaseAttributeType.ZHUAN_SHENG, config.getAttribute());
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
	/**
	 * 
	 * @param userRoleId
	 * @param stageId
	 * @param jie
	 * @param ceng
	 * @param qndNum
	 * @param czdNum
	 */
	public void huajuanAttrChange(Long userRoleId,String stageId, Map<String, Long> attr){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		} 
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		} 
		if(attr == null || attr.isEmpty()){
			return ;
		}
		role.getFightAttribute().setBaseAttribute(BaseAttributeType.HUAJUAN, attr);
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		BusMsgSender.send2One(userRoleId, ClientCmdType.HUAJUAN_ATTR, attr);
	}
	
	/**
	 * 刷新画卷2的属性变化 
	 * 
	 * @param userRoleId
	 * @param stageId
	 * @param attr
	 */
    public void huajuan2AttrChange(Long userRoleId,String stageId, Map<String, Long> attr){
        IStage stage = StageManager.getStage(stageId);
        if(stage == null){
            return;
        } 
        
        IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
        if(role == null){
            return;
        } 
        if(attr == null){
            return ;
        }
        role.getFightAttribute().setBaseAttribute(BaseAttributeType.HUAJUAN2, attr);
        role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
    }
	
	/**
	 * 角色转职修改
	 * @param userRoleId
	 * @param changeConfigId
	 */
	public void roleJobChange(Long userRoleId,Integer changeConfigId){
		String stageId = publicRoleStateExportService.getRolePublicStageId(userRoleId);
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		role.getBusinessData().setRoleConfigId(changeConfigId);
	}

	//-----------------------------------------五行-----------------------------------
	/**
     * @Description    刷新五行属性
     * @param userRoleId
     * @param stageId
     * @param list
     */
    public void changeWuXingAttrs(Long userRoleId, String stageId, List<Integer> list) {
        IStage stage = StageManager.getStage(stageId);
        if (stage == null) {
            return;
        }

        IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
        if (role == null) {
            return;
        }

        clearAttrs(role, false, BaseAttributeType.WUXING);

        role.getBusinessData().setWuxingList(list);

        Map<String, Long> wuxingAttrs = StageHelper.getWuXingMoShenExportService().getWuXingAttrs(userRoleId, list);
        if (wuxingAttrs != null) {
            /*
             * //通知前段坐骑属性变化 BusMsgSender.send2One(userRoleId,
             * ClientCmdType.ZUOQI_ATTR_CHANGE, wuxingAttrs);
             */
            // 重置坐骑属性
            RoleFactoryHelper.setRoleBaseAttrs(wuxingAttrs, role, BaseAttributeType.WUXING);
            role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
        }
    }

    /**
     * @Description 刷新五行技能属性
     * @param roleId
     * @param stageId
     */
    public void changeWuXingSkillAttrs(Long userRoleId, String stageId, Map<String, Long> wxSkillAttrMap) {
        IStage stage = StageManager.getStage(stageId);
        if (stage == null) {
            return;
        }
        
        IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
        if (role == null) {
            return;
        }
        
        if (null == wxSkillAttrMap) {
            return;
        }
        clearAttrs(role, false, BaseAttributeType.WUXING_SKILL);
        RoleFactoryHelper.setRoleBaseAttrs(wxSkillAttrMap, role, BaseAttributeType.WUXING_SKILL);
        role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
    }
    
    /**
     * @Description 刷新五行精魄属性
     * @param roleId
     * @param stageId
     */
    public void changeWuXingJpAttrs(Long userRoleId, String stageId, Map<String, Long> wxJpAttrMap) {
        IStage stage = StageManager.getStage(stageId);
        if (stage == null) {
            return;
        }
        
        IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
        if (role == null) {
            return;
        }
        
        if(null == wxJpAttrMap){
            return;
        }
        
        clearAttrs(role, false, BaseAttributeType.WUXING_JP);
        RoleFactoryHelper.setRoleBaseAttrs(wxJpAttrMap, role, BaseAttributeType.WUXING_JP);
        role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
    }
    
    //-----------------------------------------糖宝五行-----------------------------------
    /**
     * @Description    刷新糖宝五行属性
     * @param userRoleId
     * @param stageId
     * @param list
     */
    public void changeTbWuXingAttrs(Long userRoleId, String stageId, Map<String, Long> tbWxAttrMap) {
        IStage stage = StageManager.getStage(stageId);
        if (stage == null) {
            return;
        }

        IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
        if (role == null) {
            return;
        }
        
        if(null == tbWxAttrMap){
            return;
        }
        
        BaseAttributeType attrType = BaseAttributeType.TB_WUXING;
        clearAttrs(role, false, attrType);
        RoleFactoryHelper.setRoleBaseAttrs(tbWxAttrMap, role, attrType);
        role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
        
        Pet pet = role.getPet();
        if(null == pet){
            return ;
        }
        // 重置糖宝的糖宝五行属性
        clearFighterAttrs(pet,false,attrType);
        pet.getFightAttribute().setBaseAttribute(attrType, tbWxAttrMap);
        pet.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
        StageMsgSender.send2One(role.getId(), ClientCmdType.TANGBAO_MAX_HP, pet.getFightAttribute().getMaxHp());
    }
    
    /**
     * @Description 刷新糖宝五行技能属性
     * @param roleId
     * @param stageId
     */
    public void changeTbWuXingSkillAttrs(Long userRoleId, String stageId, Map<String, Long> tbWxSkillAttrMap) {
        IStage stage = StageManager.getStage(stageId);
        if (stage == null) {
            return;
        }
        
        IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
        if (role == null) {
            return;
        }
        
        if(null == tbWxSkillAttrMap){
            return;
        }
        
        BaseAttributeType attrType = BaseAttributeType.TB_WUXING_SKILL;
        
        // 重置人物的糖宝五行技能属性
        clearAttrs(role, false, attrType);
        RoleFactoryHelper.setRoleBaseAttrs(tbWxSkillAttrMap, role, attrType);
        role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
        
        Pet pet = role.getPet();
        if(null == pet){
            return ;
        }
        // 重置糖宝的糖宝五行技能属性
        clearFighterAttrs(pet,false,attrType);
        pet.getFightAttribute().setBaseAttribute(attrType, tbWxSkillAttrMap);
        pet.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
        StageMsgSender.send2One(role.getId(), ClientCmdType.TANGBAO_MAX_HP, pet.getFightAttribute().getMaxHp());
    }
    
    /**
     * @Description 刷新心魔属性
     * @param roleId
     * @param stageId
     * @param xinmoAttrMap
     */
    public void changeXinmoAttrs(Long userRoleId, String stageId, Map<String, Long> xinmoAttrMap) {
        IStage stage = StageManager.getStage(stageId);
        if (stage == null) {
            return;
        }
        
        IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
        if (role == null) {
            return;
        }
        
        if(null == xinmoAttrMap){
            return;
        }
        
        clearAttrs(role, false, BaseAttributeType.XINMO);
        RoleFactoryHelper.setRoleBaseAttrs(xinmoAttrMap, role, BaseAttributeType.XINMO);
        role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
    }
    
    /**
     * @Description 刷新心魔-魔神属性
     * @param roleId
     * @param stageId
     * @param xinmoMoshenAttrMap
     */
    public void changeXinmoMoshenAttrs(Long userRoleId, String stageId, Map<String, Long> xinmoMoshenAttrMap) {
        IStage stage = StageManager.getStage(stageId);
        if (stage == null) {
            return;
        }
        
        IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
        if (role == null) {
            return;
        }
        
        if(null == xinmoMoshenAttrMap){
            return;
        }
        
        clearAttrs(role, false, BaseAttributeType.XINMO_MOSHEN);
        RoleFactoryHelper.setRoleBaseAttrs(xinmoMoshenAttrMap, role, BaseAttributeType.XINMO_MOSHEN);
        role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
    }
    
    /**
     * @Description 刷新心魔-魔神噬体属性
     * @param roleId
     * @param stageId
     * @param clearFlag 
     * @param xinmoMoshenShitiAttrMap
     */
    public void changeXinmoMoshenShitiAttrs(Long userRoleId, String stageId, Boolean clearFlag, Map<String, Long> xinmoMoshenShitiAttrMap) {
        if (null == clearFlag) {
            return;
        }
        IStage stage = StageManager.getStage(stageId);
        if (stage == null) {
            return;
        }
        IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
        if (role == null) {
            return;
        }
        if (clearFlag) {
            clearAttrs(role, true, BaseAttributeType.XINMO_MOSHEN_SHITI);
            role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
        } else if (!ObjectUtil.isEmpty(xinmoMoshenShitiAttrMap)) {
            clearAttrs(role, false, BaseAttributeType.XINMO_MOSHEN_SHITI);
            RoleFactoryHelper.setRoleBaseAttrs(xinmoMoshenShitiAttrMap, role, BaseAttributeType.XINMO_MOSHEN_SHITI);
            role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
        }
    }
    
    /**
     * @Description 刷新心魔-魔神技能属性
     * @param roleId
     * @param stageId
     * @param xinmoSkillAttrMap
     */
    public void changeXinmoSkillAttrs(Long userRoleId, String stageId, Map<String, Long> xinmoSkillAttrMap) {
        IStage stage = StageManager.getStage(stageId);
        if (stage == null) {
            return;
        }
        
        IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
        if (role == null) {
            return;
        }
        
        if(null == xinmoSkillAttrMap){
            return;
        }
        
        clearAttrs(role, false, BaseAttributeType.XINMO_SKILL);
        RoleFactoryHelper.setRoleBaseAttrs(xinmoSkillAttrMap, role, BaseAttributeType.XINMO_SKILL);
        role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
    }
    
    /**
     * @Description 刷新心魔-洗练属性
     * @param roleId
     * @param stageId
     * @param xmXilianAttrMap
     */
    public void changeXinmoXilianAttrs(Long userRoleId, String stageId, Map<String, Long> xmXilianAttrMap) {
        IStage stage = StageManager.getStage(stageId);
        if (stage == null) {
            return;
        }
        
        IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
        if (role == null) {
            return;
        }
        
        if(null == xmXilianAttrMap){
            return;
        }
        
        clearAttrs(role, false, BaseAttributeType.XINMO_XILIAN);
        RoleFactoryHelper.setRoleBaseAttrs(xmXilianAttrMap, role, BaseAttributeType.XINMO_XILIAN);
        role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
    }
    
    /**
     * 刷新场景中灵火祝福属性变化
     * 
     * @param userRoleId
     * @param stageId
     * @param linghuoBlessAttrMap
     */
    public void changeLingHuoBlessAttrs(Long userRoleId, String stageId, Map<String, Long> linghuoBlessAttrMap) {
        IStage stage = StageManager.getStage(stageId);
        if (stage == null) {
            return;
        }
        IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
        if (role == null) {
            return;
        }
        if (null == linghuoBlessAttrMap) {
            return;
        }
        clearAttrs(role, false, BaseAttributeType.LINGHUO_BLESS);
        RoleFactoryHelper.setRoleBaseAttrs(linghuoBlessAttrMap, role, BaseAttributeType.LINGHUO_BLESS);
        role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
    }
    
    /**
     * @Description 刷新仙洞属性
     * @param roleId
     * @param stageId
     * @param xiandongAttrMap
     */
    public void changeXiandongAttrs(Long userRoleId, String stageId, Map<String, Long> xiandongAttrMap) {
        IStage stage = StageManager.getStage(stageId);
        if (stage == null) {
            return;
        }
        IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
        if (role == null) {
            return;
        }
        if(null == xiandongAttrMap){
            return;
        }
        clearAttrs(role, false, BaseAttributeType.XIANDONG);
        RoleFactoryHelper.setRoleBaseAttrs(xiandongAttrMap, role, BaseAttributeType.XIANDONG);
        role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
    }
    
    /**
     * @Description 刷新仙器觉醒属性
     * @param roleId
     * @param stageId
     * @param xianqiJuexingAttrMap
     */
    public void changeXianqiJuexingAttrs(Long userRoleId, String stageId, Map<String, Long> xianqiJuexingAttrMap) {
        IStage stage = StageManager.getStage(stageId);
        if (stage == null) {
            return;
        }
        IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
        if (role == null) {
            return;
        }
        if(null == xianqiJuexingAttrMap){
            return;
        }
        clearAttrs(role, false, BaseAttributeType.XIANQIJUEXING);
        RoleFactoryHelper.setRoleBaseAttrs(xianqiJuexingAttrMap, role, BaseAttributeType.XIANQIJUEXING);
        role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
    }
    
	public void changeXianYuanFeiHuaAttrs(Long userRoleId, String stageId, Map<String, Long> attrMap) {
		IStage stage = StageManager.getStage(stageId);
        if (stage == null) {
            return;
        }
        IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
        if (role == null) {
            return;
        }
        if(attrMap ==null){
        	return;
        }
        
        clearAttrs(role, false, BaseAttributeType.XIAYUANFEIHUA);
        RoleFactoryHelper.setRoleBaseAttrs(attrMap, role, BaseAttributeType.XIAYUANFEIHUA);
        role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
	
	public void changeBossJifenAttrs(Long userRoleId, String stageId, Map<String, Long> attrMap) {
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;
		}
		IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;
		}
		if(attrMap ==null){
			return;
		}
		
		clearAttrs(role, false, BaseAttributeType.BOSS_JIFEN_ATTR);
		RoleFactoryHelper.setRoleBaseAttrs(attrMap, role, BaseAttributeType.BOSS_JIFEN_ATTR);
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
	
	public void changeShenQiJinJieAttrs(Long userRoleId, String stageId, Map<String, Long> attrMap) {
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;
		}
		IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;
		}
		if(attrMap ==null){
			return;
		}
		
		clearAttrs(role, false, BaseAttributeType.SHENQI_JINJIE);
		RoleFactoryHelper.setRoleBaseAttrs(attrMap, role, BaseAttributeType.SHENQI_JINJIE);
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
	
	public void changeSuitXiangweiAttrs(Long userRoleId, String stageId, Map<String, Long> attrMap) {
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;
		}
		IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;
		}
		if(attrMap ==null){
			return;
		}
		
		clearAttrs(role, false, BaseAttributeType.SUIT_XIANGWEI_ATTR);
		RoleFactoryHelper.setRoleBaseAttrs(attrMap, role, BaseAttributeType.SUIT_XIANGWEI_ATTR);
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
	
	
	
	
	public void changeWuqiAttrs(Long userRoleId, String stageId, WuQiInfo wuQiInfo,List<Integer> huanhuaConfigIdList, Object[] zqEquips,Integer activatedShenqiNum) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		clearAttrs(role,false,BaseAttributeType.WUQI);
		
		WuQi wuQi =  WuQiUtil.coverToWuQi(wuQiInfo,huanhuaConfigIdList, zqEquips);
		role.setWuQi(wuQi);
		
		Map<String, Long>  wuQiAttrs = StageHelper.getWuQiExportService().getWuQiAttrs(userRoleId,wuQi,role.getLevel());
		if(wuQiAttrs != null){
			//通知前端圣剑属性变化
			BusMsgSender.send2One(userRoleId, ClientCmdType.WUQI_ATTR_CHANGE, wuQiAttrs);
			//重置圣剑属性
			RoleFactoryHelper.setRoleBaseAttrs(wuQiAttrs, role, BaseAttributeType.WUQI);
//			//重置神器祝福属性
			changeShenqiZhuAttr(role,activatedShenqiNum);
			
			role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
			
//			if(role.getStateManager().contains(StateType.ZUOQI)){
//				role.getStateManager().remove(StateType.ZUOQI);
//				role.getStateManager().add(new ZuoQiState(role));
//			}
			
			Long configZplus = wuQiAttrs.get(EffectType.zplus.name());
			Long busZplus = wuQiInfo.getZplus();
			if(!busZplus.equals(configZplus)){
				//抛出一个异步指令让bus存储坐骑战斗力
				if(KuafuConfigPropUtil.isKuafuServer()){
					KuafuMsgSender.send2KuafuSource(userRoleId, InnerCmdType.INNER_WUQI_ZPLUS_CHANGE, configZplus);
				}else{
					StageMsgSender.send2Bus(userRoleId, InnerCmdType.INNER_WUQI_ZPLUS_CHANGE, configZplus);
				}
			}
		}
	}
}
