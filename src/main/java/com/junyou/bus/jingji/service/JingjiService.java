package com.junyou.bus.jingji.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemInput;
import com.junyou.bus.bag.utils.BagUtil;
import com.junyou.bus.chibang.export.ChiBangExportService;
import com.junyou.bus.hczbs.export.HcZhengBaSaiExportService;
import com.junyou.bus.huoyuedu.enums.ActivityEnum;
import com.junyou.bus.huoyuedu.export.HuoYueDuExportService;
import com.junyou.bus.jingji.dao.JingjiAttributeDao;
import com.junyou.bus.jingji.dao.JingjiReportDao;
import com.junyou.bus.jingji.dao.RoleJingjiAttributeDao;
import com.junyou.bus.jingji.dao.RoleJingjiDao;
import com.junyou.bus.jingji.dao.RoleJingjiDuihuanDao;
import com.junyou.bus.jingji.entity.JingJiManager;
import com.junyou.bus.jingji.entity.JingjiRobotConfig;
import com.junyou.bus.jingji.entity.PaiMingJiangLiConfig;
import com.junyou.bus.jingji.entity.PaiMingZaXiangConfig;
import com.junyou.bus.jingji.entity.RoleJingji;
import com.junyou.bus.jingji.entity.RoleJingjiAttribute;
import com.junyou.bus.jingji.entity.RoleJingjiDuihuan;
import com.junyou.bus.jingji.entity.RongYuDuiHuaniConfig;
import com.junyou.bus.jingji.export.JingjiRobotConfigExportService;
import com.junyou.bus.jingji.export.PaiMingJiangLiConfigExportService;
import com.junyou.bus.jingji.export.PaiMingZaXiangConfigExportService;
import com.junyou.bus.jingji.export.RankShowConfigExportService;
import com.junyou.bus.jingji.export.RongYuDuiHuanConfigExportService;
import com.junyou.bus.jingji.util.FightPowerType;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoExportService;
import com.junyou.bus.skill.entity.RoleSkill;
import com.junyou.bus.skill.export.RoleSkillExportService;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.territory.export.TerritoryExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.zuoqi.export.ZuoQiExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.JingjiDuihuanLogEvent;
import com.junyou.event.JingjiGiftLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.publicconfig.configure.export.ActivitieShall;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.configure.SkillFireType;
import com.junyou.stage.model.attribute.role.RoleFightAttribute;
import com.junyou.stage.model.core.attribute.BaseAttributeType;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.element.role.KuafuRoleUtil;
import com.junyou.stage.model.element.role.Role;
import com.junyou.stage.model.skill.SkillManager;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.state.jingji.entity.IJingjiFighter;
import com.junyou.state.jingji.entity.JingjiAttribute;
import com.junyou.state.jingji.entity.JingjiAttribute2;
import com.junyou.state.jingji.entity.JingjiFight;
import com.junyou.state.jingji.entity.JingjiFighter;
import com.junyou.state.jingji.entity.JingjiFighter2;
import com.junyou.utils.KuafuConfigPropUtil;
import com.junyou.utils.codec.SerializableUtil;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.junyou.utils.zip.CompressConfigUtil;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

@Service
public class JingjiService implements IFightVal {
		
		@Override
		public long getZplus(long userRoleId, int fightPowerType) {
			if(fightPowerType == FightPowerType.TOTAL_FIGHTVALUE){
				String stageId =  publicRoleStateExportService.getRolePublicStageId(userRoleId);
				//死亡状态不能使用道具 
				IStage stage = StageManager.getStage(stageId);
				if(stage == null){
					return 0;
				}
				
				IRole role = stage.getElement(userRoleId, ElementType.ROLE);
				if(role == null){
					return 0;
				}
				return role.getFightAttribute().getZhanLi();
			}
			return 0;
		}
		
	@Autowired
	private RoleJingjiDao roleJingjiDao;
	@Autowired
	private RoleJingjiAttributeDao roleJingjiAttributeDao;
	@Autowired
	private JingjiAttributeDao jingjiAttributeDao;
	@Autowired
	private StageControllExportService stageControllExportService;
//	@Autowired
//	private MingCiZhanShiConfigExportService mingCiZhanShiConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private PaiMingZaXiangConfigExportService paiMingZaXiangConfigExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RongYuDuiHuanConfigExportService rongYuDuiHuanConfigExportService;
	@Autowired
	private PaiMingJiangLiConfigExportService paiMingJiangLiConfigExportService;
	@Autowired
	private RoleBusinessInfoExportService roleBusinessInfoExportService;
	@Autowired
	private RoleJingjiDuihuanDao roleJingjiDuihuanDao;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private JingjiReportDao jingjiReportDao;
	@Autowired
	private ZuoQiExportService zuoQiExportService;
	@Autowired
	private ChiBangExportService chiBangExportService;
	@Autowired 
	private HuoYueDuExportService huoYueDuExportService;
	@Autowired
	private TerritoryExportService territoryExportService;
	@Autowired
	private HcZhengBaSaiExportService hcZhengBaSaiExportService;
	@Autowired
	private RoleSkillExportService roleSkillExportService;
	@Autowired 
	private  JingjiRobotConfigExportService jingjiRobotConfigExportService;
	@Autowired
	private RankShowConfigExportService rankShowConfigExportService;
	@Autowired
    private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	
	/**初始化*/
	public List<RoleJingjiDuihuan> initRoleJingjiDuihuan(Long userRoleId) {
		return roleJingjiDuihuanDao.initRoleJingjiDuihuan(userRoleId);
	}
	
	public void init(){
		if(KuafuConfigPropUtil.isKuafuServer()){//跨服服务器无需初始化
			return;
		}
		List<RoleJingji> list = roleJingjiDao.initAllRoleJingji();

		if(list.isEmpty() || list.size() < jingjiRobotConfigExportService.getRobotLen()){
			list.addAll(initRobot(list.size()));
		}

		list = JingJiManager.getManager().init(list);
		for (RoleJingji roleJingji : list) {
			roleJingjiDao.cacheUpdate(roleJingji, roleJingji.getUserRoleId());
		}
	}
	
	/**
	 * 初始化竞技场机器人
	 * @param count
	 * @return
	 */
	public List<RoleJingji> initRobot(int existCount){
		List<RoleJingji> roleJingjis = new ArrayList<>();
		PaiMingZaXiangConfig paiMingZaXiangConfig = paiMingZaXiangConfigExportService.getConfig();
		int maxTzCount = paiMingZaXiangConfig.getMaxTzCount();

		Map<Integer,JingjiRobotConfig> jingjiRobotConfigs = jingjiRobotConfigExportService.getConfigs();
		for (JingjiRobotConfig config : jingjiRobotConfigs.values()) {
			if(config.getRankId() <= existCount){
				continue;
			}

			long robotRoleId = - config.getRankId();
			RoleJingji robotJingJi = createRobot(robotRoleId,config.getRankId(),maxTzCount, config);
			roleJingjis.add(robotJingJi);
		}

		JingJiManager.getManager().init(roleJingjis);
		return roleJingjis;
	}

	private RoleJingji createRobot(long robotRoleId,int rank,int maxTzCount, JingjiRobotConfig config) {
		RoleJingji roleJingji = new RoleJingji();
		roleJingji = new RoleJingji();
		roleJingji.setUserRoleId(robotRoleId);
		roleJingji.setState(GameConstants.JINGJI_STATE_NORMAL);
		roleJingji.setGift(GameConstants.JINGJI_GIFT_RECIVED);
		roleJingji.setUpdateTime(GameSystemTime.getSystemMillTime());
		roleJingji.setLevel(config.getLevel());
		roleJingji.setName(config.getRobotName());
		roleJingji.setConfigId(config.getRoleConfigId());
		roleJingji.setRank(rank);
		roleJingji.setTzCount(maxTzCount);
		roleJingji.setCdTime(0l);
		roleJingji.setUsed(0);
		roleJingji.setZuoqi(config.getWuqiLevel());
		roleJingji.setChibang(config.getSwingLevel());
		roleJingji.setZplus(config.getRobotAttrs().get(EffectType.zplus.name()));
		roleJingjiDao.cacheInsert(roleJingji, robotRoleId);


		Role robot = new Role(robotRoleId, config.getRobotName());
//		//等级属性
		Map<String,Long> attribute = config.getRobotAttrs();
		//属性管理器
		RoleFightAttribute roleFightAttribute = new RoleFightAttribute(robot);
		robot.setRoleFightAttribute(roleFightAttribute);
		robot.getFightAttribute().initBaseAttribute(BaseAttributeType.LEVEL,attribute);


		JingjiAttribute2 jingjiAttribute2 = createRobotJingjiAttribute(robot,config.getSkillDatas(),attribute);

		RoleJingjiAttribute roleJingjiAttribute = new RoleJingjiAttribute();
		roleJingjiAttribute.setUserRoleId(robotRoleId);
		roleJingjiAttribute.setAttribute2(SerializableUtil.javaSerialize(jingjiAttribute2));
		roleJingjiAttributeDao.cacheInsert(roleJingjiAttribute, robotRoleId);

		return roleJingji;
	}

	/**
	 * 根据userRoleId获取竞技信息（如果为null，会创建信息）
	 * @param userRoleId
	 * @param create 如果没有，是否会创建新的
	 * @return
	 */
	private RoleJingji getRoleJingji(Long userRoleId,boolean create){
		RoleJingji roleJingji = JingJiManager.getManager().getRoleJingjiByRoleId(userRoleId);
		PaiMingZaXiangConfig paiMingZaXiangConfig = paiMingZaXiangConfigExportService.getConfig();
		if(roleJingji == null){
			if(!create){
				return null;
			}
			roleJingji = new RoleJingji();
			roleJingji.setUserRoleId(userRoleId);
			roleJingji.setState(GameConstants.JINGJI_STATE_NORMAL);
			roleJingji.setGift(GameConstants.JINGJI_GIFT_RECIVED);
			
			JingJiManager.getManager().add(roleJingji);
			
			roleJingji.setUpdateTime(GameSystemTime.getSystemMillTime());
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			roleJingji.setLevel(role.getLevel());
			roleJingji.setName(role.getName());
			roleJingji.setTzCount(paiMingZaXiangConfig.getMaxTzCount());
			roleJingji.setCdTime(0l);
			roleJingji.setConfigId(role.getConfigId());
			Integer zq = zuoQiExportService.getZuoQiInfoLevel(userRoleId);
			if(zq == null){
				zq = -1;
			}
			roleJingji.setZuoqi(zq);
			Integer cb = chiBangExportService.getChibangLevel(userRoleId);
			if(cb == null){
				cb = -1;
			}
			roleJingji.setChibang(cb);
			roleJingji.setUsed(0);
			roleJingji.setLastrefreshtime(0L);
			roleJingjiDao.cacheInsert(roleJingji, userRoleId);
		}else{
			if(!DatetimeUtil.dayIsToday(roleJingji.getUpdateTime())){
				roleJingji.setGift(GameConstants.JINGJI_GIFT_HAVE);
				roleJingji.setLastRank(roleJingji.getRank());
				roleJingji.setUpdateTime(GameSystemTime.getSystemMillTime());
				roleJingji.setTzCount(paiMingZaXiangConfig.getMaxTzCount());
				roleJingjiDao.cacheUpdate(roleJingji, userRoleId);
			}
		}
		return roleJingji;
	}
	/**
	 * 根据排名获取竞技信息（可能为null）
	 * @param rank
	 * @return
	 */
	private RoleJingji getRoleJingjiByRank(Integer rank){
		RoleJingji roleJingji = JingJiManager.getManager().getRoleJingjiByRank(rank);
		if(roleJingji != null && !DatetimeUtil.dayIsToday(roleJingji.getUpdateTime())){
			Long userRoleId = roleJingji.getUserRoleId();
			PaiMingZaXiangConfig paiMingZaXiangConfig = paiMingZaXiangConfigExportService.getConfig();
			roleJingji.setGift(GameConstants.JINGJI_GIFT_HAVE);
			roleJingji.setLastRank(roleJingji.getRank());
			roleJingji.setTzCount(paiMingZaXiangConfig.getMaxTzCount());
			roleJingji.setUpdateTime(GameSystemTime.getSystemMillTime());
			roleJingjiDao.cacheUpdate(roleJingji, userRoleId);
		}
		return roleJingji;
	}
	/**
	 * 获取竞技信息
	 * @param userRoleId
	 * @return
	 */
	public Object[] getJingjiInfo(Long userRoleId){
		String stageId = stageControllExportService.getCurStageId(userRoleId);
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return null;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return null;
		}
		RoleJingji roleJingji = getRoleJingji(userRoleId,true);
		JingjiAttribute2 jingjiAttribute = jingjiAttributeDao.loadFile2(userRoleId+"");
		boolean isFirst = false;
		if(jingjiAttribute == null){
			RoleJingjiAttribute roleJingjiAttribute = roleJingjiAttributeDao.cacheLoad(userRoleId, userRoleId);
			boolean create = false;
			if(roleJingjiAttribute != null){
				byte[] att = roleJingjiAttribute.getAttribute2();
				if(att != null && att.length > 1){
					jingjiAttribute = (JingjiAttribute2)SerializableUtil.javaDeserialize(att);
					create = true;
				}
			}
			if(!create){
				jingjiAttribute = createJingjiAttribute(role);
				if(roleJingjiAttribute == null){
					roleJingjiAttribute = new RoleJingjiAttribute();
					roleJingjiAttribute.setUserRoleId(userRoleId);
					roleJingjiAttribute.setAttribute2(SerializableUtil.javaSerialize(jingjiAttribute));
					roleJingjiAttributeDao.cacheInsert(roleJingjiAttribute, userRoleId);
				}else{
					roleJingjiAttribute.setAttribute2(SerializableUtil.javaSerialize(jingjiAttribute));
					roleJingjiAttributeDao.cacheUpdate(roleJingjiAttribute, userRoleId);
				}

				roleJingjiDao.cacheUpdate(roleJingji, userRoleId);
				isFirst = true;
			}
			roleJingji.setZplus(jingjiAttribute.getBuffZplus());
		}
		if(!isFirst){
			boolean update = false;
			if(!DatetimeUtil.dayIsToday(jingjiAttribute.getUpdateTime())){
				jingjiAttribute.setBuff(0);
				update = true;
			}
			if(role.getFightAttribute().getZhanLi() > jingjiAttribute.getZplus()){
				Map<Integer,Map<String,Long>> attribute = KuafuRoleUtil.getRoleAllAttributeExceptBuff(role);
				jingjiAttribute.setAttribute(attribute);
				jingjiAttribute.setZplus(role.getFightAttribute().getZhanLi());
				List<RoleSkill> skillList = roleSkillExportService.loadRoleSkill(userRoleId, GameConstants.SKILL_TYPE_ZHUDONG);
				if(skillList != null && skillList.size() > 0){
					jingjiAttribute.getSkills().clear();
					for (RoleSkill roleSkill : skillList) {
						ISkill skill = SkillManager.getManager().getSkill(roleSkill.getSkillId(), roleSkill.getLevel());
						if(skill != null && skill.getSkillConfig().getSkillFireType() == SkillFireType.NORMAL){
							jingjiAttribute.addSkill(skill.getId());
						}
					}
				}
				update = true;
			}
			if(update){
				jingjiAttribute.setUpdateTime(GameSystemTime.getSystemMillTime());
				jingjiAttributeDao.writeFile(jingjiAttribute, userRoleId+"");
				roleJingji.setZplus(jingjiAttribute.getBuffZplus());
				roleJingji.setName(role.getName());
				roleJingji.setLevel(role.getLevel());
				roleJingji.setConfigId(role.getBusinessData().getRoleConfigId());
				Integer zq = zuoQiExportService.getZuoQiInfoLevel(userRoleId);
				if(zq == null){
					zq = -1;
				}
				roleJingji.setZuoqi(zq);
				Integer cb = chiBangExportService.getChibangLevel(userRoleId);
				if(cb == null){
					cb = -1;
				}
				roleJingji.setChibang(cb);
				
				roleJingjiDao.cacheUpdate(roleJingji, userRoleId);
			}
		}
		int rank = roleJingji.getRank();


//		MingCiZhanShiConfig config = getMingCiZhanShiConfig(rank);
//		Integer one = rank - config.getOne();
//		Integer two = rank - config.getTwo();
//		Integer three = rank - config.getThree();
//		Object[] other = new Object[3];

        Object[] other = (Object[])getTargetRank(roleJingji,rank)[1];

//		RoleJingji roleOne = getRoleJingjiByRank(targetRanks[0]);
//		if(roleOne != null){
//			other[0] = roleOne.getInfo();
//		}
//		RoleJingji roleTwo = getRoleJingjiByRank(targetRanks[1]);
//		if(roleTwo != null){
//			other[1] = roleTwo.getInfo();
//		}
//		RoleJingji roleThree = getRoleJingjiByRank(targetRanks[2]);
//		if(roleThree != null){
//			other[2] = roleThree.getInfo();
//		}

		return new Object[]{1,roleJingji.getRank(),roleJingji.getTzCount(),roleJingji.getCdTime(),jingjiAttribute.getZplus(),jingjiAttribute.getBuff(),other};
	}

	private Object[]  getTargetRank(RoleJingji roleJingji,int rank){
        Object[] result = new Object[3];

		int[] targetIndexs = null;
		if(CovertObjectUtil.isEmpty(roleJingji.getHistoryrank())){
            targetIndexs = rankShowConfigExportService.getRanks(rank);
			roleJingji.setHistoryrank(JSONArray.toJSONString(targetIndexs));
			roleJingjiDao.cacheUpdate(roleJingji,roleJingji.getUserRoleId());
		}else{
            targetIndexs = ObjectUtil.convertToInt(JSONObject.parseArray(roleJingji.getHistoryrank(),Integer.class));
		}


        RoleJingji roleOne = getRoleJingjiByRank(targetIndexs[0]);
        if(roleOne != null){
            result[0] = roleOne.getInfo();
        }
        RoleJingji roleTwo = getRoleJingjiByRank(targetIndexs[1]);
        if(roleTwo != null){
            result[1] = roleTwo.getInfo();
        }
        RoleJingji roleThree = getRoleJingjiByRank(targetIndexs[2]);
        if(roleThree != null){
            result[2] = roleThree.getInfo();
        }

		return  new Object[]{1,result};
	}


//	/**
//	 * 获取竞技信息
//	 * @param userRoleId
//	 * @return
//	 */
//	public Object[] getRefreshJingjiInfo(Long userRoleId){
//		String stageId = stageControllExportService.getCurStageId(userRoleId);
//		IStage stage = StageManager.getStage(stageId);
//		if(stage == null){
//			return null;
//		}
//
//		RoleJingji roleJingji = getRoleJingji(userRoleId,true);
//		int rank = roleJingji.getRank();
//
//		MingCiZhanShiConfig config = getMingCiZhanShiConfig(rank);
//		Integer one = rank - config.getOne();
//		Integer two = rank - config.getTwo();
//		Integer three = rank - config.getThree();
//		Object[] other = new Object[3];
//		RoleJingji roleOne = getRoleJingjiByRank(one);
//		if(roleOne != null){
//			other[0] = roleOne.getInfo();
//		}
//		RoleJingji roleTwo = getRoleJingjiByRank(two);
//		if(roleTwo != null){
//			other[1] = roleTwo.getInfo();
//		}
//		RoleJingji roleThree = getRoleJingjiByRank(three);
//		if(roleThree != null){
//			other[2] = roleThree.getInfo();
//		}
//
//		return new Object[]{1,roleJingji.getRank(),roleJingji.getTzCount(),roleJingji.getCdTime(),jingjiAttribute.getZplus(),jingjiAttribute.getBuff(),other};
//	}
	
	private JingjiAttribute getJingjiAttribute(Long userRoleId){
		JingjiAttribute jingjiAttribute = jingjiAttributeDao.loadFile(userRoleId+"");
		if(jingjiAttribute == null){
			RoleJingjiAttribute roleJingjiAttribute = roleJingjiAttributeDao.cacheLoad(userRoleId, userRoleId);
			if(roleJingjiAttribute != null){
				jingjiAttribute = (JingjiAttribute)SerializableUtil.javaDeserialize(roleJingjiAttribute.getAttribute());
			}
		}else if(!DatetimeUtil.dayIsToday(jingjiAttribute.getUpdateTime())){
			jingjiAttribute.setBuff(0);
			jingjiAttribute.setUpdateTime(GameSystemTime.getSystemMillTime());
			jingjiAttributeDao.writeFile(jingjiAttribute, userRoleId+"");
		}
		return jingjiAttribute;
	}
	
	private JingjiAttribute2 getJingjiAttribute2(Long userRoleId){
		JingjiAttribute2 jingjiAttribute = jingjiAttributeDao.loadFile2(userRoleId+"");
		if(jingjiAttribute == null){
			RoleJingjiAttribute roleJingjiAttribute = roleJingjiAttributeDao.cacheLoad(userRoleId, userRoleId);
			if(roleJingjiAttribute != null){
				byte[] att = roleJingjiAttribute.getAttribute2();
				if(att != null && att.length > 1){
					jingjiAttribute = (JingjiAttribute2)SerializableUtil.javaDeserialize(att);
				}
			}
		}else if(!DatetimeUtil.dayIsToday(jingjiAttribute.getUpdateTime())){
			jingjiAttribute.setBuff(0);
			jingjiAttribute.setUpdateTime(GameSystemTime.getSystemMillTime());
			jingjiAttributeDao.writeFile(jingjiAttribute, userRoleId+"");
		}
		return jingjiAttribute;
	}
	
	/**
	 * 创建竞技场属性
	 * @param role
	 * @return
	 */
	private JingjiAttribute2 createJingjiAttribute(IRole role){
		Long userRoleId = role.getId();
		JingjiAttribute2 jingjiAttribute = new JingjiAttribute2();
		jingjiAttribute.setBuff(0);
		Map<Integer,Map<String,Long>> attribute = KuafuRoleUtil.getRoleAllAttributeExceptBuff(role);
		jingjiAttribute.setAttribute(attribute);
		List<RoleSkill> skillList = roleSkillExportService.loadRoleSkill(userRoleId, GameConstants.SKILL_TYPE_ZHUDONG);
		if(skillList != null && skillList.size() > 0){
			for (RoleSkill roleSkill : skillList) {
				ISkill skill = SkillManager.getManager().getSkill(roleSkill.getSkillId(), roleSkill.getLevel());
				if(skill != null && skill.getSkillConfig().getSkillFireType() == SkillFireType.NORMAL){
					jingjiAttribute.addSkill(skill.getId());
				}
			}
		}
		jingjiAttribute.setUpdateTime(GameSystemTime.getSystemMillTime());
		jingjiAttribute.setZplus(role.getFightAttribute().getZhanLi());
		jingjiAttributeDao.writeFile(jingjiAttribute, userRoleId+"");
		return jingjiAttribute;
	}
	
	/**
	 * 创建竞技场机器人属性
	 * @param role
	 * @return
	 */
	private JingjiAttribute2 createRobotJingjiAttribute(IRole role,String[] skillDatas,Map<String,Long> attrsData ){
		Long userRoleId = role.getId();
		JingjiAttribute2 jingjiAttribute = new JingjiAttribute2();
		jingjiAttribute.setBuff(0);


		Map<Integer,Map<String,Long>> attribute = new HashMap<>();
		attribute.put(BaseAttributeType.LEVEL.getVal(),attrsData);
		jingjiAttribute.setAttribute(attribute);
		
		for (String skillData : skillDatas) {
			jingjiAttribute.addSkill(skillData);
		}
		
		jingjiAttribute.setUpdateTime(GameSystemTime.getSystemMillTime());
		jingjiAttribute.setZplus(attrsData.get(EffectType.zplus.name()));
		jingjiAttributeDao.writeFile(jingjiAttribute, userRoleId+"");
		return jingjiAttribute;
	}
	
//	/**
//	 * 根据排名获取排名显示信息
//	 * @param rank
//	 * @return
//	 */
//	private MingCiZhanShiConfig getMingCiZhanShiConfig(int rank){
//		MingCiZhanShiConfig config = null;
//		int select = 1;
//		for (Integer mingci : mingCiZhanShiConfigExportService.getMingCiList()) {
//			if(rank < mingci){
//				break;
//			}
//			select = mingci;
//		}
//		config = mingCiZhanShiConfigExportService.loadByMingci(select);
//		return config;
//	}
	/**
	 * 根据排名获取排名奖励信息
	 * @param rank
	 * @return
	 */
	private PaiMingJiangLiConfig getPaiMingJiangLiConfig(int rank){
		PaiMingJiangLiConfig config = null;
		int select = 1;
		for (Integer mingci : paiMingJiangLiConfigExportService.getMingCiList()) {
			if(rank < mingci){
				break;
			}
			select = mingci;
		}
		config = paiMingJiangLiConfigExportService.loadByRank(select);
		return config;
	}
	/**
	 * 获取三仙信息
	 * @return
	 */
	public Object[] getTopThree(){
		Object[] answer = new Object[3];
		RoleJingji roleOne = getRoleJingjiByRank(1);
		if(roleOne != null){
			answer[0] = roleOne.getInfo();
		}
		RoleJingji roleTwo = getRoleJingjiByRank(2);
		if(roleTwo != null){
			answer[1] = roleTwo.getInfo();
		}
		RoleJingji roleThree = getRoleJingjiByRank(3);
		if(roleThree != null){
			answer[2] = roleThree.getInfo();
		}
		return answer;
	}
	/**
	 * 购买次数
	 * @param userRoleId
	 * @return
	 */
	public Object[] buyCount(Long userRoleId){
		RoleJingji roleJingji = getRoleJingji(userRoleId,true);
		PaiMingZaXiangConfig paiMingZaXiangConfig = paiMingZaXiangConfigExportService.getConfig();
		if(roleJingji.getTzCount() >= paiMingZaXiangConfig.getMaxTzCount()){
			return AppErrorCode.TIAOZHAN_COUNT_IS_MAX;//次数已满
		}
		Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, paiMingZaXiangConfig.getBuyCountGold(), userRoleId, LogPrintHandle.CONSUME_JINGJI, true, LogPrintHandle.CBZ_JINGJI_BUY_COUNT);
		if(result != null){
			return result;//元宝不足
		}else{
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,paiMingZaXiangConfig.getBuyCountGold(),LogPrintHandle.CONSUME_JINGJI,QQXiaoFeiType.CONSUME_JINGJI,1});
			}
		}
		roleJingji.setTzCount(roleJingji.getTzCount() + 1);
		roleJingjiDao.cacheUpdate(roleJingji, userRoleId);
		return new Object[]{1,roleJingji.getTzCount()};
	}
	/**
	 * 秒cd
	 * @param userRoleId
	 * @return
	 */
	public Object[] miaoCd(Long userRoleId){
		RoleJingji roleJingji = getRoleJingji(userRoleId,true);
		long time = roleJingji.getCdTime() - GameSystemTime.getSystemMillTime();
		if(time <= 0){
			return AppErrorCode.JINGJI_NOT_IN_CD;//不需要秒
		}
		int minute = (int)(time / 60000);
		if(time % 60000 > 0){
			minute ++;
		}
		PaiMingZaXiangConfig paiMingZaXiangConfig = paiMingZaXiangConfigExportService.getConfig();
		int gold = minute * paiMingZaXiangConfig.getClearCdGold(); 
		Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, gold, userRoleId, LogPrintHandle.CONSUME_JINGJI, true, LogPrintHandle.CBZ_JINGJI_MIAO_CD);
		if(result != null){
			return result;//元宝不足
		}else{
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,gold,LogPrintHandle.CONSUME_JINGJI,QQXiaoFeiType.CONSUME_JINGJI,1});
			}
		}
		roleJingji.setCdTime(GameSystemTime.getSystemMillTime());
		roleJingjiDao.cacheUpdate(roleJingji, userRoleId);
		return AppErrorCode.OK;
	}
	/**
	 * 鼓舞
	 * @param userRoleId
	 * @return
	 */
	public Object[] guwu(Long userRoleId){
		JingjiAttribute2 jingjiAttribute = getJingjiAttribute2(userRoleId);
		PaiMingZaXiangConfig paiMingZaXiangConfig = paiMingZaXiangConfigExportService.getConfig();
		int buff = jingjiAttribute.getBuff();
		if(buff >= paiMingZaXiangConfig.getGuwuCount()){
			return AppErrorCode.JINGJI_GUWU_IS_MAX;//鼓舞次数已满
		}
		int gold = paiMingZaXiangConfig.getGuwuGold();
		Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, gold, userRoleId, LogPrintHandle.CONSUME_JINGJI, true, LogPrintHandle.CBZ_JINGJI_GUWU);
		if(result != null){
			return result;//元宝不足
		}else{
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,gold,LogPrintHandle.CONSUME_JINGJI,QQXiaoFeiType.CONSUME_JINGJI,1});
			}
		}
		float gailv = paiMingZaXiangConfig.getGailv1();
		if(buff > paiMingZaXiangConfig.getGuwuCount2()){
			gailv = paiMingZaXiangConfig.getGailv2();
		}
		Object[] ans = AppErrorCode.JINGJI_GUWU_FIAL;
		if(Lottery.roll(gailv, Lottery.TENTHOUSAND)){
			ans = AppErrorCode.OK;
			buff++;
			jingjiAttribute.setBuff(buff);
			jingjiAttributeDao.writeFile(jingjiAttribute, userRoleId+"");
		}
		return ans;
	}
	/**
	 * 挑战
	 * @param userRoleId
	 * @param rank
	 * @return
	 */
	public Object[] tiaoZhan(Long userRoleId,int rank){
		//判断是否在挑战时间内
		if(!getIsTianZhanTime()){
			return AppErrorCode.JINGJI_JIESUAN_ZHONG;
		}
		RoleJingji roleJingji = getRoleJingji(userRoleId,true);
		if(!checkCanTiaoZhan(roleJingji, rank)){
			return AppErrorCode.JINGJI_TARGET_CANNOT_TIAOZHAN;//目标不可挑战
		}
		if(roleJingji.getState() == GameConstants.JINGJI_STATE_FIGHT){
			if(roleJingji.getFightTime() + GameConstants.FIGHT_TIME_OUT < GameSystemTime.getSystemMillTime()){
				roleJingji.setState(GameConstants.JINGJI_STATE_NORMAL);
			}else{
				return AppErrorCode.JINGJI_NOW_CANNOT_FIGHT;//正在战斗中
			}
		}
		PaiMingZaXiangConfig paiMingZaXiangConfig = paiMingZaXiangConfigExportService.getConfig();
		if(roleJingji.getCdTime() - GameSystemTime.getSystemMillTime() > paiMingZaXiangConfig.getMaxTime()){
			return AppErrorCode.JINGJI_NOW_IS_IN_CD;// 正在CD中
		}
		if(roleJingji.getTzCount() < 1){
			return AppErrorCode.JINGJI_TODAY_IS_NO_TZ_COUNT;//没有挑战次数
		}
		RoleJingji target = getRoleJingjiByRank(rank);
		if(target == null){
			return AppErrorCode.JINGJI_TARGET_CANNOT_TIAOZHAN;//目标不可挑战
		}
		if(target.getState() == GameConstants.JINGJI_STATE_FIGHT){
			if(target.getFightTime() + GameConstants.FIGHT_TIME_OUT < GameSystemTime.getSystemMillTime()){
				target.setState(GameConstants.JINGJI_STATE_NORMAL);
			}else{
				return AppErrorCode.JINGJI_TARGET_IS_FIGHTING;//正在战斗中
			}
		}
		if(stageControllExportService.inFuben(userRoleId)){
			return AppErrorCode.JINGJI_FUBEN_CANNOT_TIAOZHAN;//在副本中
		}
		//准备战斗
		roleJingji.setState(GameConstants.JINGJI_STATE_FIGHT);
		target.setState(GameConstants.JINGJI_STATE_FIGHT);
		roleJingji.setTargetRank(rank);
		//通知进入小黑屋
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.ENTER_SAFE_MAP, null);
		return null;
	}
	/**
	 * 场景通知可以进行战斗
	 * @param userRoleId
	 * @param rank
	 */
	public void fight(Long userRoleId){
		RoleJingji roleJingji = getRoleJingji(userRoleId,true);
		roleJingji.setWatching(true);
		int rank = roleJingji.getTargetRank();
		RoleJingji target = getRoleJingjiByRank(rank);
		if(target == null){
			roleJingji.setState(GameConstants.JINGJI_STATE_NORMAL);
			//数据异常,退出副本
			BusMsgSender.send2One(userRoleId, ClientCmdType.JINGJI_FIGHT_ERROR,null);
			return;
		}
		
		IJingjiFighter attFighter = createJingjiFighter(roleJingji);
		IJingjiFighter defFighter = createJingjiFighter(target);
		if(attFighter == null){
			roleJingji.setState(GameConstants.JINGJI_STATE_NORMAL);
			target.setState(GameConstants.JINGJI_STATE_NORMAL);
			//数据异常,退出副本
			BusMsgSender.send2One(userRoleId, ClientCmdType.JINGJI_FIGHT_ERROR,null);
			return;
		}
		PaiMingZaXiangConfig config = paiMingZaXiangConfigExportService.getConfig();
		//扣除次数
		roleJingji.setTzCount(roleJingji.getTzCount() - 1);
		long cdTime = roleJingji.getCdTime();
		if(cdTime > GameSystemTime.getSystemMillTime()){
			cdTime += config.getTime();
		}else{
			cdTime = GameSystemTime.getSystemMillTime() + config.getTime();
		}
		roleJingji.setCdTime(cdTime);
		roleJingjiDao.cacheUpdate(roleJingji, userRoleId);
		
		if(defFighter == null){//防守方无数据，自动算进攻方获胜
			if(changeRank(roleJingji, target)){
				roleJingjiDao.cacheUpdate(roleJingji, userRoleId);
				roleJingjiDao.cacheUpdate(target, target.getUserRoleId());
			}
			long exp = config.getWinExp();
			int rongyu = config.getWinRongyu();
			int answer = GameConstants.JINGJI_ANSWER_WIN;
			roleExportService.incrExp(userRoleId,exp);
			roleBusinessInfoExportService.addRongyu(userRoleId, rongyu);
			BusMsgSender.send2One(userRoleId, ClientCmdType.JINGJI_FIGHT_ANSWER, new Object[]{answer,roleJingji.getRank(),exp,rongyu});
			BusMsgSender.send2One(userRoleId, ClientCmdType.JINGJI_FIGHT_ERROR,null);
			return;
		}
		//战斗开始
		JingjiFight fight = new JingjiFight();
		fight.init(attFighter, defFighter);
		String respotr = fight.startFight();
		int exp = 0;
		int rongyu = 0;
		int answer = GameConstants.JINGJI_ANSWER_LOSE;
		if(userRoleId.equals(fight.getLoser().getId())){
			roleJingji.setState(GameConstants.JINGJI_STATE_NORMAL);
			target.setState(GameConstants.JINGJI_STATE_NORMAL);
			exp = config.getLoseExp();
			rongyu = config.getLoseRongyu();
		}else{
			if(changeRank(roleJingji, target)){
				roleJingjiDao.cacheUpdate(roleJingji, userRoleId);
				roleJingjiDao.cacheUpdate(target, target.getUserRoleId());
			}
			exp = config.getWinExp();
			rongyu = config.getWinRongyu();
			answer = GameConstants.JINGJI_ANSWER_WIN;
		}
		roleExportService.incrExp(userRoleId, exp*1l);
		roleBusinessInfoExportService.addRongyu(userRoleId, rongyu);
		
		//记录挑战记录
		createJingjiReport(roleJingji, target, answer, rank);
		
		List<Object[]> data = new ArrayList<>();
		data.add(new Object[]{ClientCmdType.JINGJI_TIAOZHAN, new Object[]{1,target.getUserRoleId(),target.getConfigId(),target.getName(),target.getLevel(),target.getZuoqi(),defFighter.getFightAttribute().getMaxHp(), respotr,target.getChibang(),attFighter.getFightAttribute().getMaxHp()}});
		data.add(new Object[]{ClientCmdType.JINGJI_FIGHT_ANSWER, new Object[]{answer,roleJingji.getRank(),exp,rongyu}});
		//打包数据发送
		Object[] obj = CompressConfigUtil.compressAddCheckObject(data.toArray());
		BusMsgSender.send2One(userRoleId, (short)obj[0], obj[1]);
		//活跃度lxn
		huoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A7);
	}

    /**
     * 交换双方排名
     * @param winner	挑战者
     * @param loser		失败者
     * @return 是否有交换
     */
    private boolean changeRank(RoleJingji winner,RoleJingji loser) {
        Map<Integer,RoleJingji> rankMap = JingJiManager.getManager().getRankMaps();
//		Map<Long,RoleJingji> roleMap = JingJiManager.getManager().getRoleMaps();

        boolean change = false;
        int rank = winner.getRank();
//        long robotLen = jingjiRobotConfigExportService.getRobotLen();

        if(rank > loser.getRank())
        {
//                //判定是否为机器人
//                if(loser.getUserRoleId() < 0 &&  loser.getRank() <= robotLen)
//                {
//
//                        if(winner.getRank() <=  robotLen)
//                        {
//                            winner.setRank(loser.getRank());
//                            rankMap.put(winner.getRank(), winner);
//
//                            //隐藏掉机器人的信息
//                            loser.setUsed(1);
//                            roleJingjiDao.cacheUpdate(loser, loser.getUserRoleId());
//
//                            //将玩家所在的机器人位置拉出来
//                            long  robotUid  = -loser.getRank();
//                            RoleJingji roleJingjiRobot = JingJiManager.getManager().getRoleJingjiByRoleId(robotUid);
//                            roleJingjiRobot.setUsed(0);
//                            roleJingjiDao.cacheUpdate(roleJingjiRobot, robotUid);
//
//                            rankMap.put(roleJingjiRobot.getRank(), roleJingjiRobot);
//                        }
//                        else if(winner.getRank() > robotLen)
//                        {
//                            PaiMingZaXiangConfig paiMingZaXiangConfig = paiMingZaXiangConfigExportService.getConfig();
//                            int maxTzCount = paiMingZaXiangConfig.getMaxTzCount();
//                            long robotRoleId = - IdFactory.getInstance().generateId(ServerIdType.COMMON);
//                            JingjiRobotConfig config = jingjiRobotConfigExportService.getLastConfig();
//
//                            //如果超出机器人范围了，将直接用第最后一名的机器人替代
//                            RoleJingji robotRoleJ = createRobot(robotRoleId,winner.getRank(),maxTzCount, config);
//                            rankMap.put(robotRoleJ.getRank(), robotRoleJ);
//                            roleMap.put(robotRoleJ.getUserRoleId(), robotRoleJ);
//
//                            //胜利者处理
//                            winner.setRank(loser.getRank());
//                            rankMap.put(winner.getRank(), winner);
//                        }
//                }
                //如果获胜者排名低则交换排名
                winner.setRank(loser.getRank());
                loser.setRank(rank);

				rankMap.put(winner.getRank(), winner);
				rankMap.put(loser.getRank(), loser);

				change = true;

				int[] targetIndexs = rankShowConfigExportService.getRanks(winner.getRank());
				winner.setHistoryrank(JSONArray.toJSONString(targetIndexs));
				roleJingjiDao.cacheUpdate(winner,winner.getUserRoleId());

        }
		//公告
        if(winner.getRank() == 1){
            BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{GameConstants.JINGJI_FIRST_NOTICE,new Object[]{winner.getName(),loser.getName()}});
        }else if(winner.getRank() <= 10){
            BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{GameConstants.JINGJI_TOP_10_NOTICE,new Object[]{winner.getName(),loser.getName(),winner.getRank()}});
        }

        winner.setState(GameConstants.JINGJI_STATE_NORMAL);
        loser.setState(GameConstants.JINGJI_STATE_NORMAL);
        return change;
    }


	/**
	 * 生成挑战记录
	 * @param att
	 * @param def
	 * @param answer
	 * @param targetRank
	 */
	private void createJingjiReport(RoleJingji att,RoleJingji def,int answer,int targetRank){
		Object[] report = new Object[]{1,def.getName(),null,att.getRank(),null};
		Object[] report2 = new Object[]{0,att.getName(),null,def.getRank(),null};
		if(answer == GameConstants.JINGJI_ANSWER_WIN){
			report[2] = 0;
			report2[2] = 1;
		}else{
			report[2] = 1;
			report2[2] = 0;
		}
		if(att.getRank() == targetRank){
			report[4] = 0;
			report2[4] = 2;
		}else{
			report[4] = 1;
			report2[4] = 1;
		}
		jingjiReportDao.writeFile(report, att.getUserRoleId()+"");
		jingjiReportDao.writeFile(report2, def.getUserRoleId()+"");
	}
	/**
	 * 玩家退出战斗
	 * @param userRoleId
	 */
	public void exitFight(Long userRoleId){
		RoleJingji roleJingji = getRoleJingji(userRoleId,true);
		if(!roleJingji.isWatching()){
			return;
		}
		roleJingji.setWatching(false);
		StageMsgSender.send2StageControl(userRoleId, InnerCmdType.S_APPLY_LEAVE_STAGE, userRoleId);
	}
	/**
	 * 创建竞技战斗角色
	 * @param roleJingji
	 * @return
	 */
	private IJingjiFighter createJingjiFighter(RoleJingji roleJingji){
		IJingjiFighter fighter = createJingjiFighter2(roleJingji);
		if(fighter != null){
			return fighter;
		}
		Long userRoleId = roleJingji.getUserRoleId();
		JingjiAttribute jingjiattribute = getJingjiAttribute(userRoleId);
		if(jingjiattribute == null){
			ChuanQiLog.error("role JingjiAttribute is null.userRoleId is " + userRoleId);
			return null;
		}
		JingjiFighter oldFighter = new JingjiFighter(userRoleId, roleJingji.getName());
		oldFighter.setJingjiattribute(jingjiattribute);
		return oldFighter;
	}
	/**
	 * 创建竞技战斗角色(新版)
	 * @param roleJingji
	 * @return
	 */
	private IJingjiFighter createJingjiFighter2(RoleJingji roleJingji){
		Long userRoleId = roleJingji.getUserRoleId();
		JingjiAttribute2 jingjiattribute = getJingjiAttribute2(userRoleId);
		if(jingjiattribute == null){
//			ChuanQiLog.error("role JingjiAttribute2 is null.userRoleId is " + userRoleId);
			return null;
		}
		JingjiFighter2 fighter = new JingjiFighter2(userRoleId, roleJingji.getName(),roleJingji.getConfigId());
		fighter.setJingjiattribute(jingjiattribute);
		return fighter;
	}
	
	
	/**
	 * 检测是否可以挑战目标
	 * @param rank
	 * @param targetRank
	 * @return
	 */
	private boolean checkCanTiaoZhan(RoleJingji roleJingji,int targetRank){
		int rank = roleJingji.getRank();
		if(rank == targetRank){
			return false;
		}
		if(targetRank < 4){
			return true;
		}

		List<Integer> targetRanks = JSONArray.parseArray(roleJingji.getHistoryrank(),Integer.class);
		if(targetRanks == null){
			return false;
		}
		for (Integer owerId : targetRanks) {
			if(owerId == targetRank){
				return true;
			}
		}

//		MingCiZhanShiConfig config = getMingCiZhanShiConfig(rank);
//		if(config == null){
//			return false;
//		}
//		int diff = rank - targetRank;
//		if(diff == config.getOne() || diff == config.getTwo() || diff == config.getThree()){
//			return true;
//		}
		return false;
	}
	/**
	 * 领取排名奖励
	 * @param userRoleId
	 * @return
	 */
	public Object[] reciveGift(Long userRoleId){
		RoleJingji roleJingji = getRoleJingji(userRoleId,true);
		if(roleJingji.getGift().intValue() == GameConstants.JINGJI_GIFT_RECIVED){
			return AppErrorCode.JINGJI_GIFT_IS_RECIVED;//奖励已领取过
		}
		PaiMingJiangLiConfig config = getPaiMingJiangLiConfig(roleJingji.getLastRank());
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		roleJingji.setGift(GameConstants.JINGJI_GIFT_RECIVED);
		roleJingji.setUpdateTime(GameSystemTime.getSystemMillTime());
		roleJingjiDao.cacheUpdate(roleJingji, userRoleId);
		
		//发放奖励
		roleExportService.incrExp(userRoleId, config.getExp()*1l);
		roleBusinessInfoExportService.addRongyu(userRoleId, config.getRongyu());
		accountExportService.incrCurrencyWithNotify(GoodsCategory.MONEY, config.getMoney(), userRoleId, LogPrintHandle.GET_JINGJI, LogPrintHandle.GBZ_JINGJI_RANK);
		
		//打印日志
		GamePublishEvent.publishEvent(new JingjiGiftLogEvent(userRoleId, roleJingji.getName(), roleJingji.getRank(), config.getExp(), config.getRongyu()));
		
		return AppErrorCode.OK;
	}
	/**
	 * 荣誉兑换道具
	 * @param userRoleId
	 * @param goodsId
	 * @return
	 */
	public Object[] duihuanItem(Long userRoleId,String goodsId){
		
		RongYuDuiHuaniConfig config = rongYuDuiHuanConfigExportService.loadByItemId(goodsId);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;//配置异常
		}
		RoleJingjiDuihuan duihuan = getRoleJingjiDuihuan(userRoleId, goodsId);
		if(duihuan.getCount() >= config.getMaxCount()){
			return AppErrorCode.JINGJI_DUIHUAN_NO_COUNT;//数量超过上限
		}
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if(roleWrapper.getLevel() < config.getNeedLevel()){
			return AppErrorCode.ROLE_LEVEL_NOT_ENOUGH;//角色等级不足 
		}
		if(!roleBusinessInfoExportService.isEnoughRongyu(userRoleId, config.getNeedRongyu())){
			return AppErrorCode.JINGJI_NO_ENOUGH_RONGYU;//荣誉值不足
		}
		//检测背包
		Object[] result = roleBagExportService.checkPutInBag(config.getItemId(), 1, userRoleId);
		if(result != null){
			return result;
		}
		
		roleBusinessInfoExportService.costRongyu(userRoleId, config.getNeedRongyu());
		duihuan.setCount(duihuan.getCount()+1);
		duihuan.setUpdataTime(GameSystemTime.getSystemMillTime());
		roleJingjiDuihuanDao.cacheUpdate(duihuan, userRoleId);
		//物品入背包
		RoleItemInput item = BagUtil.createItem(config.getItemId(), 1, 0);
		roleBagExportService.putInBag(item, userRoleId, GoodsSource.ROLE_JINGJI, true);
		
		//活跃度lxn
		huoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A8);
		//打印日志
		GamePublishEvent.publishEvent(new JingjiDuihuanLogEvent(userRoleId, roleWrapper.getName(), config.getNeedRongyu(), config.getItemId(), 1));
		return new Object[]{AppErrorCode.OK,goodsId};
	}
	/**
	 * 获取兑换信息
	 * @param userRoleId
	 * @param goodsId
	 * @return
	 */
	public RoleJingjiDuihuan getRoleJingjiDuihuan(Long userRoleId,String goodsId){
		final String gid = goodsId; 
		List<RoleJingjiDuihuan> list = roleJingjiDuihuanDao.cacheLoadAll(userRoleId, new IQueryFilter<RoleJingjiDuihuan>() {
			boolean find;
			@Override
			public boolean check(RoleJingjiDuihuan entity) {
				if(entity.getItemId().equals(gid)){
					find = true;
				}
				return find;
			}

			@Override
			public boolean stopped() {
				return find;
			}
		});
		RoleJingjiDuihuan duihuan = null;
		if(list != null && list.size() > 0){
			duihuan = list.get(0);
			calDuihuan(duihuan);
		}else{
			duihuan = new RoleJingjiDuihuan();
			duihuan.setId(IdFactory.getInstance().generateId(ServerIdType.JINGJI));
			duihuan.setCount(0);
			duihuan.setItemId(goodsId);
			duihuan.setUserRoleId(userRoleId);
			duihuan.setUpdataTime(GameSystemTime.getSystemMillTime());
			roleJingjiDuihuanDao.cacheInsert(duihuan, userRoleId);
		}
		return duihuan;
	}
	private void calDuihuan(RoleJingjiDuihuan duihuan){
		if(!DatetimeUtil.dayIsToday(duihuan.getUpdataTime())){
			duihuan.setCount(0);
			duihuan.setUpdataTime(GameSystemTime.getSystemMillTime());
			roleJingjiDuihuanDao.cacheUpdate(duihuan, duihuan.getUserRoleId());
		}
	}
	/**
	 * 获取兑换信息列表
	 * @param userRoleId
	 * @return
	 */
	public Object[] getJingjiDuihuanInfo(Long userRoleId){
		List<RoleJingjiDuihuan> list = roleJingjiDuihuanDao.cacheLoadAll(userRoleId);
		List<Object[]> result = new ArrayList<>();
		if(list != null && list.size() > 0){
			for (RoleJingjiDuihuan duihuan : list) {
				calDuihuan(duihuan);
				if(duihuan.getCount() > 0){
					result.add(new Object[]{duihuan.getItemId(),duihuan.getCount()});
				}
			}
		}
		return result.toArray();
	}
	/**
	 * 获取排名信息列表
	 * @param userRoleId
	 * @return
	 */
	public void getJingjiRankInfo(Long userRoleId,int rank){
		int maxRank = 0;
		for (Integer mingci : paiMingJiangLiConfigExportService.getMingCiList()){
			if(rank < mingci){
				maxRank = mingci;
				break;
			}
		}
		if(maxRank > 0){
			List<Object[]> list = new ArrayList<>();
			for (int i = rank; i < maxRank; i++) {
				RoleJingji jingji = getRoleJingjiByRank(i);
				if(jingji == null){
					break;
				}
				list.add(jingji.getInfo());
			}
			Object[] result = new Object[]{rank,list.toArray()};
			if(list.size() > 5){
				List<Object[]> data = new ArrayList<>();
				data.add(new Object[]{ClientCmdType.GET_JINGJI_PAIMING_INFO, result});
				Object[] obj = CompressConfigUtil.compressAddCheckObject(data.toArray());
				BusMsgSender.send2One(userRoleId, (short)obj[0], obj[1]);
			}else{
				BusMsgSender.send2One(userRoleId, ClientCmdType.GET_JINGJI_PAIMING_INFO, result);
			}
		}
	}
	/**
	 * 获取战斗记录
	 * @param userRoleId
	 * @return
	 */
	public Object[] getJingjiReports(Long userRoleId){
		JSONArray json = jingjiReportDao.loadFile(userRoleId+"");
		if(json == null){
			return new Object[0];
		}
		Object[] result = new Object[json.size()];
		int i = 0;
		for (Object object : json) {
			result[i++] = ((JSONArray)object).toArray();
		}
		return result;
	}
	
	public Object isReciveGift(Long userRoleId){
		RoleJingji roleJingji = getRoleJingji(userRoleId,false);
		if(roleJingji == null){
			return null;
		}
		return new Object[]{roleJingji.getLastRank(),roleJingji.getGift()};
		
	}
	
	
	/**
	 * 获取个人竞技的奖励状态值
	 * @param userRoleId
	 * @return
	 */
	private int getJingJiStateValue(Long userRoleId){
		//int(0)二进制   奖励 有为1，无为0  顺序为  个人竞技(0)
		int state = 0;
		RoleJingji roleJingji = getRoleJingji(userRoleId,false);
		if(roleJingji == null){
			//没有数据直接返回
			return state;
		}
		
		if(roleJingji.getGift().intValue() == GameConstants.JINGJI_GIFT_HAVE){
			return 1;//个人竞技(0) 2的0次方
		}else{
			return state;
		}
	}
	/**
	 * 活动大厅有无可领奖励
	 * @param userRoleId
	 * @return
	 */
	public int getHuoDongDaTing(Long userRoleId){
		//int(0)二进制   奖励 有为1，无为0  顺序为  个人竞技(0)
		int jingjiStage = getJingJiStateValue(userRoleId);
		int territoryStage = territoryExportService.getTerritoryRewardState(userRoleId);
		int hczbs = hcZhengBaSaiExportService.getHcZbsRewardState(userRoleId);
		return (hczbs<<2) | (territoryStage << 1 ) | jingjiStage;
	}
	
	private boolean getIsTianZhanTime(){
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			long time = System.currentTimeMillis();//当前时间
			long starTime = sim.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())+" 00:05:00").getTime();//开始时间
			if(time > starTime){
				return true;
			}
		} catch (ParseException e) {
			ChuanQiLog.error("",e);
		}
		
		return false;
	}
	
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	
	public int rankSoft(Long userRoleId) {
		long zhali  = 0;
		if(publicRoleStateExportService.isPublicOnline(userRoleId)){
			String stageId = stageControllExportService.getCurStageId(userRoleId);
			IStage stage = StageManager.getStage(stageId);
			if(stageId != null && stage != null){
				IRole role = stage.getElement(userRoleId, ElementType.ROLE);
				if(role != null){
					zhali = role.getFightAttribute().getZhanLi();
				}
			}
		}
		
		JingJiManager  jingJiManager = JingJiManager.getManager();
		int len = jingJiManager.getTotalCount() > 500 ? 500 : jingJiManager.getTotalCount();
		List<Long> result = new ArrayList<>();
		for (int i = 1; i <= len; i++) {
			RoleJingji roleJingji = JingJiManager.getManager().getRoleJingjiByRank(i);
			result.add(roleJingji.getZplus());
		}
		Collections.sort(result);
		int n = result.size();
		for (int i = result.size(); i >= 1 ; i--) {
			if(zhali > result.get(i-1)){
				n = result.size() - i + 1;
				break;
			}
		}
		int r = (int)(((float)(len - n + 800)) / ((float)(len+800))*100);
		return r;
	}

    public Object[] refreshTiaozhan(Long userRoleId) {
        RoleJingji roleJingji = getRoleJingji(userRoleId,true);

		ActivitieShall config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_ACTIVITIESHALL);
		if(config == null){
			ChuanQiLog.error("ActivitieShall config is null !");
			return AppErrorCode.CONFIG_ERROR;
		}
		long nowTime = System.currentTimeMillis();
        if(roleJingji.getLastrefreshtime() + config.getPvprefreshcd() > nowTime) {
        	return AppErrorCode.JINGJI_NOW_IS_IN_CD;
		}

        int[] targetIndexs = rankShowConfigExportService.getRanks(roleJingji.getRank());
        if(targetIndexs == null){
        	return AppErrorCode.CONFIG_ERROR;
		}
        roleJingji.setHistoryrank(JSONArray.toJSONString(targetIndexs));
		roleJingji.setLastrefreshtime(nowTime);
		roleJingjiDao.cacheUpdate(roleJingji,userRoleId);

        return getTargetRank(roleJingji,roleJingji.getRank());
    }
}
