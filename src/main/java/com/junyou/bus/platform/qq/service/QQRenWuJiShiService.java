package com.junyou.bus.platform.qq.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.chibang.configure.export.ChiBangJiChuConfig;
import com.junyou.bus.chibang.configure.export.ChiBangJiChuConfigExportService;
import com.junyou.bus.chibang.entity.ChiBangInfo;
import com.junyou.bus.chibang.export.ChiBangExportService;
import com.junyou.bus.daytask.entity.TaskDay;
import com.junyou.bus.daytask.export.TaskDayExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.platform.qq.confiure.export.RenWuJiShiConfig;
import com.junyou.bus.platform.qq.confiure.export.RenWuJiShiConfigExportService;
import com.junyou.bus.platform.qq.dao.RenwujishiDao;
import com.junyou.bus.platform.qq.entity.Renwujishi;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoExportService;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoWrapper;
import com.junyou.bus.yabiao.entity.Yabiao;
import com.junyou.bus.yabiao.service.export.YabiaoExportService;
import com.junyou.bus.zuoqi.configure.export.YuJianJiChuConfig;
import com.junyou.bus.zuoqi.configure.export.YuJianJiChuConfigExportService;
import com.junyou.bus.zuoqi.entity.ZuoQiInfo;
import com.junyou.bus.zuoqi.export.ZuoQiExportService;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.public_.share.export.PublicRoleStateExportService;


@Service
public class QQRenWuJiShiService {

	
	@Autowired
	private RenwujishiDao renwujishiDao;
	@Autowired
	private RenWuJiShiConfigExportService renWuJiShiConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private RoleBusinessInfoExportService roleBusinessInfoExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private ZuoQiExportService zuoQiExportService;
	@Autowired
	private YuJianJiChuConfigExportService yuJianJiChuConfigExportService;
	@Autowired
	private ChiBangExportService chiBangExportService;
	@Autowired
	private ChiBangJiChuConfigExportService chiBangJiChuConfigExportService;
	@Autowired
	private YabiaoExportService yabiaoExportService;
	@Autowired
	private TaskDayExportService taskDayExportService;
	
	
	
	private Renwujishi getRenWuJiShi(Long userRoleId){
		List<Renwujishi> list = renwujishiDao.cacheLoadAll(userRoleId);
		if(list == null || list.size() <= 0 ){
			Renwujishi renwu = new Renwujishi();
			renwu.setUserroleid(userRoleId);
			renwu.setLingquStatus("");
			renwu.setCreateTime(new Timestamp(System.currentTimeMillis()));
			renwu.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			
			renwujishiDao.cacheInsert(renwu, userRoleId);
			return renwu;
		}
		
		return list.get(0);
	}
	
	public Object[] getRenWuStatus(Long userRoleId,String renWuId,int bu) {
		RenWuJiShiConfig config = renWuJiShiConfigExportService.getRenWuByIdAndBu(renWuId, bu);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		Object[] obj = new Object[]{false,0};
		if(!publicRoleStateExportService.isPublicOnline(userRoleId)){
			return obj;
		}
		
		switch (config.getType()) {
			case GameConstants.RWJS_DAY_TASK:
				TaskDay task = taskDayExportService.getTaskDay(userRoleId, GameConstants.TASK_DAY);
				if(task != null && task.getRenwuCount() >= config.getCishu()){
					obj = new Object[]{true,1};
				}
				break;
		                   
			case GameConstants.RWJS_ROLE_LEVEL:
				RoleWrapper userRole = roleExportService.getLoginRole(userRoleId);
				if(userRole.getLevel() >= config.getCishu()){
					obj = new Object[]{true,1};
				}
				break;
				
			case GameConstants.RWJS_GUSHENG:
				
				break;
				
			case GameConstants.RWJS_YA_BIAO:
				Yabiao yabiao = yabiaoExportService.getYaBiao(userRoleId);
				if(yabiao != null && yabiao.getRenwuCount() >= config.getCishu()){
					return new Object[]{true,1};
				}
				break;
				
			case GameConstants.RWJS_DUOREN_FUBEN:
				
				break;
				
			case GameConstants.RWJS_ZHAN_LI:
				RoleBusinessInfoWrapper roleBus = roleBusinessInfoExportService.getRoleBusinessInfoWrapper(userRoleId);
				if(roleBus.getCurFighter() >= config.getCishu()){
					obj = new Object[]{true,1};
				}
				break;
				
			case GameConstants.RWJS_ZUO_QI:
				//玩家坐骑
				ZuoQiInfo zuoqi = zuoQiExportService.getZuoQiInfo(userRoleId);
				if(zuoqi == null ){
					obj = new Object[]{false,0};
				}
				YuJianJiChuConfig yujianConfig = yuJianJiChuConfigExportService.loadById(zuoqi.getZuoqiLevel());
				if(yujianConfig != null && yujianConfig.getLevel() >= config.getCishu()){
					obj = new Object[]{true,0};
				}else{
					obj = new Object[]{false,0};
				}
				break;
				
			case GameConstants.RWJS_CHIBANG:
				//玩家翅膀
				ChiBangInfo chibang = chiBangExportService.getChiBangInfo(userRoleId);
				if(chibang == null ){
					obj = new Object[]{false,0};
				}
				ChiBangJiChuConfig chibangConfig = chiBangJiChuConfigExportService.loadById(chibang.getChibangLevel());
				if(chibangConfig != null && chibangConfig.getLevel() >= config.getCishu()){
					obj = new Object[]{true,0};
				}else{
					obj = new Object[]{false,0};
				}
				break;
				
			default:
				break;
		}
		
		
		return obj;
	}

	public List<Renwujishi> initAllRenWuJiShi(Long userRoleId) {
		return renwujishiDao.initRenwujishi(userRoleId);
	}

	public Integer lingQu(Long userRoleId, String renWuId, int bu,String goodsId) {
		Renwujishi renwu = getRenWuJiShi(userRoleId);
		RenWuJiShiConfig config = renWuJiShiConfigExportService.getRenWuByIdAndBu(renWuId, bu);
		//已领取过奖励
		if(getLingQuStatus(renwu.getLingquStatus(), config.getConfigid()+"")){
			return 3;
		}
		GoodsConfig good = goodsConfigExportService.loadById(goodsId);
		if(good == null){
			return 102;//奖励发放失败
		}
		//发放奖励
		rechargeEmail(userRoleId, goodsId);
		
		if(renwu.getLingquStatus() == null || "".equals(renwu.getLingquStatus())){
			renwu.setLingquStatus(config.getConfigid()+"");
		}else{
			renwu.setLingquStatus(renwu.getLingquStatus()+","+config.getConfigid());
		}
		renwujishiDao.cacheUpdate(renwu, userRoleId);
		
		return 0;
	}

	
	/**
	 * 判断ID是否已领取过
	 * @param lingqu
	 * @param id
	 * @return
	 */
	private boolean getLingQuStatus(String lingqu,String id){
		
		if(lingqu == null || "".equals(lingqu)){
			return false;
		}
		String[] s = lingqu.split(",");
		for (int i = 0; i < s.length; i++) {
			if(s[i].equals(id)){
				return true;
			}
		}
		return false;
	}
	
	 /**
	  * 邮件发送奖励
	  * @param roleNames
	  */
	 private void rechargeEmail(Long userRoleId,String goodsId){
			
			String attachment = goodsId+":"+1;
			String content = EmailUtil.getCodeEmail(AppErrorCode.RWJS_YOUJIAN);
			String title = EmailUtil.getCodeEmail(AppErrorCode.RWJS_YOUJIAN_EMAIL_TITLE);
			emailExportService.sendEmailToOne(userRoleId,title, content,GameConstants.EMAIL_TYPE_SINGLE, attachment);
	}
	 
	 public Integer renWuJiShiLingQu(String openId,String serverId,String cmd,String renWuId,Integer step,String payItem){
		 UserRole role = getRoleWrapper(openId, serverId);
			if(role == null){
				return GameConstants.RWJS_NOT_ROLE;
			}
			if(GameConstants.RWJS_CHECK.equals(cmd)){//查询是否已完成
				Object[] obj = getRenWuStatus(role.getId(), renWuId, step);
				if((Boolean)obj[0]){
					return GameConstants.RWJS_SUCCESS;
				}else{
					return GameConstants.RWJS_UNFINISHED;
				}
			}else if(GameConstants.RWJS_CHECKAWARD.equals(cmd)){//查询并发货
				Object[] obj = getRenWuStatus(role.getId(), renWuId, step);
				if((Boolean)obj[0]){
					return lingQu(role.getId(), renWuId, step, payItem);
				}else{
					return GameConstants.RWJS_UNFINISHED;
				}
			}else if(GameConstants.RWJS_AWARD.equals(cmd)){//直接发货
				
				return lingQu(role.getId(), renWuId, step, payItem);
			}
			
			return null;
	 }
	
	 private UserRole getRoleWrapper(String userId,String serverId){
		 UserRole userRole = roleExportService.getRoleFromDb(userId, serverId);
		 return userRole;
	 }
		
}
