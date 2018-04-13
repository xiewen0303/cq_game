package com.junyou.bus.lingjing.service;

import java.util.List;
import java.util.Map;

import com.junyou.bus.jingji.service.IFightVal;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.utils.common.CovertObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.branchtask.BranchEnum;
import com.junyou.bus.branchtask.service.TaskBranchService;
import com.junyou.bus.chibang.export.ChiBangExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.lingjing.dao.RoleLingjingDao;
import com.junyou.bus.lingjing.entity.LingJingShuXingCengConfig;
import com.junyou.bus.lingjing.entity.LingJingShuXingConfig;
import com.junyou.bus.lingjing.entity.RoleLingjing;
import com.junyou.bus.lingjing.export.LingJingShuXingConfigServiceExport;
import com.junyou.bus.qiling.export.QiLingExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoExportService;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoWrapper;
import com.junyou.bus.shenqi.export.ShenQiExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.wuqi.export.WuQiExportService;
import com.junyou.bus.xianjian.export.XianJianExportService;
import com.junyou.bus.zhanjia.export.ZhanJiaExportService;
import com.junyou.bus.zuoqi.export.ZuoQiExportService;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.math.BitOperationUtil;

/**
 * 灵境业务
 * @author LiuYu
 * 2015-6-29 下午3:08:38
 */
@Service
public class LingJingService implements IFightVal{
	@Override
	public long getZplus(long userRoleId, int fightPowerType) {
		Map<String,Long>  data = getAttribute(userRoleId);
		if(data == null){
			return 0;
		}
		return CovertObjectUtil.getZplus(data);
	}

	@Autowired
	private LingJingShuXingConfigServiceExport lingJingShuXingConfigServiceExport;
	@Autowired
	private RoleLingjingDao roleLingjingDao;
	@Autowired
	private RoleBagExportService roleBagExportService;
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
	private RoleExportService roleExportService;
	@Autowired
	private RoleBusinessInfoExportService roleBusinessInfoExportService;
	@Autowired
	private ShenQiExportService shenQiExportService;
	@Autowired
	private WuQiExportService wuQiExportService;
	
	public List<RoleLingjing> initRoleLingjing(Long userRoleId) {
		return roleLingjingDao.initRoleLingjing(userRoleId);
	}

	private RoleLingjing getRoleLingjing(Long userRoleId){
		return roleLingjingDao.cacheAsynLoad(userRoleId, userRoleId);
	}
	
	/**
	 * 通知场景变更属性
	 * @param userRoleId
	 */
	private void noticeStageChangeAttribute(Long userRoleId,Map<String,Long> attribute){
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_LINGJING_CHANGE, attribute);
	}
	

	
	
//	if(config.getType() == GameConstants.LINGJING_TYPE_YUJIAN){
//		value = zuoQiExportService.getZuoQiInfoLevel(userRoleId) + 1;
//	}else 
//	if(config.getType() == GameConstants.LINGJING_TYPE_CHIBANG){
//		value = chiBangExportService.getChibangLevel(userRoleId) + 1;
//	}else
//	if(config.getType() == GameConstants.LINGJING_TYPE_XIANJIAN){
//		value = xianJianExportService.getXianJianLevel(userRoleId) + 1;
//	}else
//	if(config.getType() == GameConstants.LINGJING_TYPE_ZHANJIA){
//		value = zhanJiaExportService.getXianJianLevel(userRoleId) + 1;
//	}else
//	if(config.getType() == GameConstants.LINGJING_TYPE_QILING){
//		value = qiLingExportService.getQiLingLevel(userRoleId) + 1;
//	}else 
//	if(config.getType() == GameConstants.LINGJING_TYPE_SHENQI){
//		value = shenQiExportService.getActivatedShenqiNum(userRoleId);
//	}else 
//	if(config.getType() == GameConstants.LINGJING_TYPE_LEVEL){
//		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
//		if(role != null){
//			value = role.getLevel();
//		}
//	}else if(config.getType() == GameConstants.LINGJING_TYPE_ZPLUS){
//		RoleBusinessInfoWrapper role = roleBusinessInfoExportService.getRoleBusinessInfoWrapper(userRoleId);
//		if(role != null){
//			value = role.getCurFighter();
//		}
//	}else if(config.getType() == GameConstants.LINGJING_TYPE_QSQH){
//		value = roleBagExportService.getAllEquipsQHLevel(userRoleId);
//	}
	
	
	private boolean checkLingJingType(Long userRoleId,LingJingShuXingConfig config){
		long value = -1;
		RoleWrapper role = null;
		switch(config.getType()){
			case GameConstants.LINGJING_TYPE_YUJIAN:
				value = zuoQiExportService.getZuoQiInfoLevel(userRoleId) + 1;
				break;
			case GameConstants.LINGJING_TYPE_SHENGJIAN:
				value = wuQiExportService.getWuQiInfoLevel(userRoleId) + 1;
				break;
			case GameConstants.LINGJING_TYPE_CHIBANG:
				value = chiBangExportService.getChibangLevel(userRoleId) + 1;
				break;
			case GameConstants.LINGJING_TYPE_XIANJIAN:
				value = xianJianExportService.getXianJianLevel(userRoleId) + 1;
				break;
			case GameConstants.LINGJING_TYPE_ZHANJIA:
				value = zhanJiaExportService.getXianJianLevel(userRoleId) + 1;
				break;
			case GameConstants.LINGJING_TYPE_LEVEL:
				role = roleExportService.getLoginRole(userRoleId);
				if(role != null){
					value = role.getLevel();
				}
				break;
			case GameConstants.LINGJING_TYPE_QILING:
				value = qiLingExportService.getQiLingLevel(userRoleId) + 1;
				break;
			case GameConstants.LINGJING_TYPE_SHENQI:
				value = shenQiExportService.getActivatedShenqiNum(userRoleId);
				break;
			case GameConstants.LINGJING_TYPE_ZPLUS:
				RoleBusinessInfoWrapper roleB = roleBusinessInfoExportService.getRoleBusinessInfoWrapper(userRoleId);
				if(roleB != null){
					value = roleB.getCurFighter();
				}
				break;
			case GameConstants.LINGJING_TYPE_QSQH:
				value = roleBagExportService.getAllEquipsQHLevel(userRoleId);
				break;
				
			default:
				ChuanQiLog.error("战力突破类型不存在,type:"+config.getType());
		}
		
		
		
		
		
		return value >= config.getNeed();
	}
	
	private RoleLingjing createRoleLingjing(Long userRoleId){
		RoleLingjing roleLingjing = new RoleLingjing();
		roleLingjing.setUserRoleId(userRoleId);
		roleLingjing.setRank(1);
		roleLingjing.setState(0);
		return roleLingjing;
	}
	
	public Map<String,Long> getAttribute(Long userRoleId){
		RoleLingjing roleLingjing = getRoleLingjing(userRoleId);
		if(roleLingjing == null){
			return null;
		}
		LingJingShuXingCengConfig config = lingJingShuXingConfigServiceExport.getCengConfig(roleLingjing.getRank());
		if(config == null){
			return null;
		}
		return config.getAttribute(roleLingjing.getState());
	}
	
	public Object[] activeLingJing(Long userRoleId,Integer type){
		int index = type - 1;
		RoleLingjing roleLingjing = getRoleLingjing(userRoleId);
		if(roleLingjing == null){
			roleLingjing = createRoleLingjing(userRoleId);
			roleLingjingDao.cacheInsert(roleLingjing, userRoleId);
		}else if(!BitOperationUtil.calState(roleLingjing.getState(), index)){
			return AppErrorCode.LINGJING_TYPE_IS_ACTIVED;//已激活
		}
		LingJingShuXingCengConfig cengConfig = lingJingShuXingConfigServiceExport.getCengConfig(roleLingjing.getRank());
		if(cengConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		LingJingShuXingConfig config = cengConfig.getConfig(type);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		if(!checkLingJingType(userRoleId, config)){
			return AppErrorCode.LINGJING_NO_ACTIVE_TIAOJIAN;
		}
		int state = BitOperationUtil.chanageState(roleLingjing.getState(), index);
		roleLingjing.setState(state);
		roleLingjingDao.cacheUpdate(roleLingjing, userRoleId);
		
		//通知场景变更属性
		noticeStageChangeAttribute(userRoleId,cengConfig.getAttribute(state));
		return config.getSuccessMsg();
	}
	
	/**
	 * 突破
	 * @return
	 */
	public Object[] tupo(Long userRoleId){
		RoleLingjing roleLingjing = getRoleLingjing(userRoleId);
		if(roleLingjing == null){
			return AppErrorCode.LINGJING_NO_TUPO_TIAOJIAN;//条件不足
		}
		if(roleLingjing.getRank() > lingJingShuXingConfigServiceExport.getMaxRank()){
			return AppErrorCode.LINGJING_IS_MAX_CENG;//达到最大等级
		}
		
		int rank = roleLingjing.getRank();
		LingJingShuXingCengConfig  lingJingShuXingCengConfig  = lingJingShuXingConfigServiceExport.getCengConfig(rank);
		List<Integer> types = lingJingShuXingCengConfig.getTypes();
		for (Integer type : types) {
			if(BitOperationUtil.calState(roleLingjing.getState(), type -1)){
				return AppErrorCode.LINGJING_NO_TUPO_TIAOJIAN;//条件不足
			}
		}
		
		roleLingjing.setRank(rank + 1);
		roleLingjing.setState(0);
		roleLingjingDao.cacheUpdate(roleLingjing, userRoleId);
		
		LingJingShuXingCengConfig config = lingJingShuXingConfigServiceExport.getCengConfig(rank);
		LingJingShuXingCengConfig nextConfig = lingJingShuXingConfigServiceExport.getCengConfig(roleLingjing.getRank());
		//发放奖励
		String content = EmailUtil.getCodeEmail(GameConstants.LINGSHI_TUPO_EMAIL_CODE);
		roleBagExportService.putInBagOrEmail(config.getItems(), userRoleId, GoodsSource.LINGSHI_TUPO, true, content);
		//通知场景变更属性
		noticeStageChangeAttribute(userRoleId,nextConfig.getAttribute(roleLingjing.getState()));
		
		//支线任务
		taskBranchService.completeBranch(userRoleId, BranchEnum.B12, rank -1);
		
		return config.getSuccessMsg();
	}
	@Autowired
	private TaskBranchService taskBranchService;
	
	/**
	 * 获取灵境信息
	 * @param userRoleId
	 * @return
	 */
	public Object[] getLingJingInfo(Long userRoleId){
		RoleLingjing roleLingjing = getRoleLingjing(userRoleId);
		if(roleLingjing == null){
			roleLingjing = createRoleLingjing(userRoleId);
			roleLingjingDao.cacheInsert(roleLingjing, userRoleId);
		}
		return new Object[]{roleLingjing.getRank(),roleLingjing.getState()};
	}
}
