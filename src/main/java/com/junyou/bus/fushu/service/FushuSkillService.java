package com.junyou.bus.fushu.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.chibang.export.ChiBangExportService;
import com.junyou.bus.fushu.config.FuShuSkillConfig;
import com.junyou.bus.fushu.config.FuShuSkillConfigExportService;
import com.junyou.bus.fushu.config.JiNengSuoDingGoldConfig;
import com.junyou.bus.fushu.config.JiNengSuoDingGoldConfigExportService;
import com.junyou.bus.fushu.dao.FushuSkillDao;
import com.junyou.bus.fushu.entity.FushuSkill;
import com.junyou.bus.qiling.export.QiLingExportService;
import com.junyou.bus.tianyu.export.TianYuExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.wuqi.export.WuQiExportService;
import com.junyou.bus.xianjian.export.XianJianExportService;
import com.junyou.bus.zhanjia.export.ZhanJiaExportService;
import com.junyou.bus.zuoqi.export.ZuoQiExportService;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;
import com.junyou.stage.model.core.attribute.BaseAttributeType;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.lottery.Lottery;
import com.junyou.utils.math.BitOperationUtil;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 * @author LiuYu
 * 2015-8-27 上午10:09:42
 */
@Service
public class FushuSkillService {

	@Autowired
	private FushuSkillDao fushuSkillDao;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private FuShuSkillConfigExportService fuShuSkillConfigExportService;
	@Autowired
	private JiNengSuoDingGoldConfigExportService jiNengSuoDingGoldConfigExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private ZuoQiExportService zuoQiExportService;
	@Autowired
	private ChiBangExportService chiBangExportService;
	@Autowired
	private XianJianExportService xianJianExportService;
	@Autowired
	private ZhanJiaExportService zhanJiaExportService;
	@Autowired
	private QiLingExportService qiLingExportService;
	@Autowired
	private TianYuExportService tianyuExportService;
	@Autowired
	private WuQiExportService wuqiExportService;
	
	public List<FushuSkill> initFushuSkill(Long userRoleId) {
		return fushuSkillDao.initFushuSkill(userRoleId);
	}
	
	private boolean checkType(int type,int goodsCategory){
		switch (type) {
		case GameConstants.FUSHU_SKILL_TYPE_ZUOQI:
			return goodsCategory == GoodsCategory.YUJIAN_SKILL_BOOK;
		case GameConstants.FUSHU_SKILL_TYPE_CHIBANG:
			return goodsCategory == GoodsCategory.CHIBANG_SKILL_BOOK;
		case GameConstants.FUSHU_SKILL_TYPE_TIANGONG:
			return goodsCategory == GoodsCategory.TIANGONG_SKILL_BOOK;
		case GameConstants.FUSHU_SKILL_TYPE_TIANSHANG:
			return goodsCategory == GoodsCategory.TIANSHANG_SKILL_BOOK; 
		case GameConstants.FUSHU_SKILL_TYPE_QILING:
			return goodsCategory == GoodsCategory.QILING_SKILL_BOOK; 
		case GameConstants.FUSHU_SKILL_TYPE_TIANYU:
			return goodsCategory == GoodsCategory.TIANYU_SKILL_BOOK; 
		case GameConstants.FUSHU_SKILL_TYPE_WUQI:
			return goodsCategory == GoodsCategory.WUQI_SKILL_BOOK; 

		default:
			return false;
		}
	}
	
	private int getSkillMaxCount(Long userRoleId,int type){
		switch (type) {
		case GameConstants.FUSHU_SKILL_TYPE_ZUOQI:
			return zuoQiExportService.getSkillMaxCount(userRoleId);
		case GameConstants.FUSHU_SKILL_TYPE_CHIBANG:
			return chiBangExportService.getSkillMaxCount(userRoleId);
		case GameConstants.FUSHU_SKILL_TYPE_TIANGONG:
			return xianJianExportService.getSkillMaxCount(userRoleId);
		case GameConstants.FUSHU_SKILL_TYPE_TIANSHANG:
			return zhanJiaExportService.getSkillMaxCount(userRoleId);
		case GameConstants.FUSHU_SKILL_TYPE_QILING:
			return qiLingExportService.getSkillMaxCount(userRoleId);
		case GameConstants.FUSHU_SKILL_TYPE_TIANYU:
			return tianyuExportService.getSkillMaxCount(userRoleId);
		case GameConstants.FUSHU_SKILL_TYPE_WUQI:
			return wuqiExportService.getSkillMaxCount(userRoleId);
		default:
			return 0;
		}
	}
	
	private BaseAttributeType getAttributeType(int type){
		switch (type) {
		case GameConstants.FUSHU_SKILL_TYPE_ZUOQI:
			return BaseAttributeType.ZUOQI_SKILL;
		case GameConstants.FUSHU_SKILL_TYPE_CHIBANG:
			return BaseAttributeType.CHIBANG_SKILL;
		case GameConstants.FUSHU_SKILL_TYPE_TIANGONG:
			return BaseAttributeType.TIANGONG_SKILL;
		case GameConstants.FUSHU_SKILL_TYPE_TIANSHANG:
			return BaseAttributeType.TIANSHANG_SKILL;
		case GameConstants.FUSHU_SKILL_TYPE_QILING:
			return BaseAttributeType.QILING_SKILL;
		case GameConstants.FUSHU_SKILL_TYPE_TIANYU:
			return BaseAttributeType.TIANYU_SKILL;
		case GameConstants.FUSHU_SKILL_TYPE_WUQI:
			return BaseAttributeType.WUQI_SKILL;
		default:
			return null;
		}
	}
	
	private void changeSkill(FushuSkill skill,String skillId,int sort){
		switch (sort) {
		case 0:
			skill.setSkill1(skillId);
			break;
		case 1:
			skill.setSkill2(skillId);
			break;
		case 2:
			skill.setSkill3(skillId);
			break;
		case 3:
			skill.setSkill4(skillId);
			break;
		case 4:
			skill.setSkill5(skillId);
			break;
		case 5:
			skill.setSkill6(skillId);
			break;
		default:
			break;
		}
		Map<String,Long> atts = getSkillAttribute(skill);
		BusMsgSender.send2Stage(skill.getUserRoleId(), InnerCmdType.INNER_CHANGE_ROLE_ATT, new Object[]{getAttributeType(skill.getType()).val,atts});
	}
	
	private Map<String,Long> getSkillAttribute(FushuSkill skill){
		Map<String,Long> map = new HashMap<>();
		List<String> skills = new ArrayList<>();
		skills.add(skill.getSkill1());
		skills.add(skill.getSkill2());
		skills.add(skill.getSkill3());
		skills.add(skill.getSkill4());
		skills.add(skill.getSkill5());
		skills.add(skill.getSkill6());
		
		for (String skillId : skills) {
			if(ObjectUtil.strIsEmpty(skillId)){
				continue;
			}
			FuShuSkillConfig skillConfig = fuShuSkillConfigExportService.loadById(skillId);
			if(skillConfig != null){
				ObjectUtil.longMapAdd(map, skillConfig.getAtts());
			}
		}
		
		return map;
	}
	
	private FushuSkill getFushuSkill(Long userRoleId,final int type){
		List<FushuSkill> list = fushuSkillDao.cacheLoadAll(userRoleId, new IQueryFilter<FushuSkill>() {
			@Override
			public boolean check(FushuSkill entity) {
				return entity.getType().equals(type);
			}
			@Override
			public boolean stopped() {
				return false;
			}
		});
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 学习附属技能
	 * @param userRoleId
	 * @param guid	物品id
	 * @param type	附属类型
	 * @param maxCount	最大技能条数
	 * @param lockInfo	锁定技能信息
	 */
	public Object[] xuexiSkill(Long userRoleId,Long guid,int type,int lockInfo){
		RoleItemExport item = roleBagExportService.getBagItemByGuid(userRoleId, guid);
		if(item == null || item.getCount() < 1){
			return AppErrorCode.NOT_FOUND_GOOODS;
		}
		GoodsConfig config = goodsConfigExportService.loadById(item.getGoodsId());
		if(config == null){
			return AppErrorCode.GOODS_NOT_ENOUGH;
		}
		if(!checkType(type, config.getCategory())){
			return AppErrorCode.GOODS_NOT_ENOUGH;
		}
		FuShuSkillConfig skillConfig = fuShuSkillConfigExportService.loadById(config.getData3());
		if(skillConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		List<Integer> rollList = new ArrayList<>();
		int lockNum = 0;
		int maxCount = getSkillMaxCount(userRoleId, type);
		for (int i = 0; i < maxCount; i++) {
			if(BitOperationUtil.calState(lockInfo, i)){
				rollList.add(i);
			}else{
				lockNum++;
			}
		}
		if(rollList.size() < 1){
			return AppErrorCode.FUSHU_SKILL_NO_GEZI;//所有格位已锁定
		}
		int sdGold = 0;
		if(lockNum > 0){
			JiNengSuoDingGoldConfig suodingConfig = jiNengSuoDingGoldConfigExportService.loadByCount(lockNum);
			if(suodingConfig == null){
				return AppErrorCode.FUSHU_SKILL_SUOXING_MAX;//锁定数量超过上限
			}
			sdGold = suodingConfig.getGold();
			Object[] reslut = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, sdGold, userRoleId, LogPrintHandle.CONSUME_FUSHU_SKILL, true, LogPrintHandle.CBZ_FUSHU_SKILL_SUODING);
			if(reslut != null){
				return reslut;
			}
		}
		roleBagExportService.removeBagItemByGuid(guid, 1, userRoleId, GoodsSource.GOODS_LEARN_FUSHU_SKILL, true, true);
		int index = Lottery.roll(rollList.size());
		Integer sort = rollList.get(index);
		FushuSkill skill = getFushuSkill(userRoleId, type);
		boolean insert = false;
		if(skill == null){
			insert = true;
			skill = new FushuSkill();
			skill.setUserRoleId(userRoleId);
			skill.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
			skill.setType(type);
		}
		changeSkill(skill, config.getData3(), sort);
		if(insert){
			fushuSkillDao.cacheInsert(skill, userRoleId);
		}else{
			fushuSkillDao.cacheUpdate(skill, userRoleId);
		}
		return new Object[]{1,config.getData3(),sort,sdGold};
	}
	
	public Map<BaseAttributeType,Map<String,Long>> getAllSkillAttribute(Long userRoleId){
		List<FushuSkill> list = fushuSkillDao.cacheLoadAll(userRoleId);
		if(list != null && list.size() > 0){
			Map<BaseAttributeType,Map<String,Long>> map = new HashMap<>();
			for (FushuSkill fushuSkill : list) {
				Map<String,Long> att = getSkillAttribute(fushuSkill);
				if(att != null && att.size() > 0){
					map.put(getAttributeType(fushuSkill.getType()), att);
				}
			}
			return map;
		}
		return null;
	}
	
	public Object[] getFushuSkillInfo(Long userRoleId,int type){
		FushuSkill skill = getFushuSkill(userRoleId, type);
		if(skill == null){
			return null;
		}
		return skill.getClientMsg();
	}
}
