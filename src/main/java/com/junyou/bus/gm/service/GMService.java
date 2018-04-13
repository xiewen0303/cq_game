package com.junyou.bus.gm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.entity.RoleAccountWrapper;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemInput;
import com.junyou.bus.bag.utils.BagUtil;
import com.junyou.bus.recharge.export.RechargeExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.task.export.TaskExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.bus.wuxing.export.WuXingMoShenExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.gameconfig.map.configure.export.MapConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.NuqiPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.public_.share.service.PublicRoleStateService;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.role.Role;
import com.junyou.stage.model.stage.StageManager;

@Service
public class GMService {
	@Autowired
	private RoleBagExportService roleBagExportService; 
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private MapConfigExportService mapConfigExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private TaskExportService taskExportService;
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private PublicRoleStateService publicRoleStateService;
	@Autowired
	private WuXingMoShenExportService wuXingMoShenExportService;
	@Autowired
	private RechargeExportService rechargeExportService;
	
	public void createItem(Long userRoleId, String goodsId, Integer count,Integer qhLevel) {
		Object[] errorCode=roleBagExportService.checkPutInBag(goodsId,count, userRoleId);
		if(errorCode==null){
			RoleItemInput goods=BagUtil.createItem(goodsId, count, qhLevel);
			roleBagExportService.putInBag(goods, userRoleId, GoodsSource.GM_FOR_DEBUG, true);
		}
	}
	/**
	 * 增加金币
	 * @param userRoleId
	 * @param count
	 */
	public void setGold(Long userRoleId, Long count) {
		accountExportService.setAccount(userRoleId, GoodsCategory.MONEY, count);
	}
	/**
	 * 增加元宝
	 * @param userRoleId
	 * @param count
	 */
	public void setYB(Long userRoleId, Long count) {
		accountExportService.setAccount(userRoleId, GoodsCategory.GOLD, count);
	}
	/**
	 * 增加绑定元宝
	 * @param userRoleId
	 * @param count
	 */
	public void setBangYB(Long userRoleId, Long count) {
		accountExportService.setAccount(userRoleId, GoodsCategory.BGOLD, count);
	}
	/**
	 * 添加经验
	 * @param userRoleId
	 * @param addVal
	 */
	public void addExp(Long userRoleId, long addVal){
		roleExportService.incrExp(userRoleId, addVal);
	}
	/**
	 * 设置怒气
	 * @param userRoleId
	 * @param val
	 */
	public void setNuqi(Long userRoleId, int val){
		String stageId = publicRoleStateService.getRolePublicStageId(userRoleId);
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return ;
		}
		Role role = stage.getElement(userRoleId, ElementType.ROLE);
		NuqiPublicConfig nuqiPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_NUQI);
		if(nuqiPublicConfig.getMaxNq() < val){
			role.setNuqiNotice(nuqiPublicConfig.getMaxNq());
		}else{
			role.setNuqiNotice(val);
		}
	}
	
	/**
	 * GM goto跳地图
	 * @param userRoleId
	 * @param toMapId
	 */
	public void gmGoto(Long userRoleId,Integer toMapId){
		MapConfig mapConfig = mapConfigExportService.load(toMapId);
		
		boolean configError = false;
		StringBuffer alertStr = new StringBuffer("@策划-地图id:");
		alertStr.append(toMapId);
		
		if(mapConfig == null){
			configError = true;
			alertStr.append("-地图编辑器里没有配置-");
		}
		DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(toMapId);
		if(dituCoinfig == null){
			configError = true;
			alertStr.append("-地图表里没有配置-");
		}
		
		if(configError){
			alertStr.append("请配置了再试试!");
			//提示地图表里没有配置
			BusMsgSender.send2One(userRoleId, ClientCmdType.NOTIFY_CLIENT_ALERT7, new Object[]{alertStr.toString()});
			return;
		}
		int[] birthXy = dituCoinfig.getRandomBirth();
		Object[] applyEnterData = new Object[]{toMapId, birthXy[0], birthXy[1]};
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
	}
	/**
	 * 增加真气
	 * @param userRoleId
	 * @param value
	 */
	public void addZhenqi(Long userRoleId,Long value){
		roleExportService.addZhenqi(userRoleId, value);
	}
	/**
	 * 修改任务
	 * @param userRoleId
	 * @param taskId
	 */
	public void changeTask(Long userRoleId,Integer taskId){
		taskExportService.gmChangeTask(userRoleId, taskId);
	}
	/**
	 * 修改等级
	 * @param userRoleId
	 * @param level
	 */
	public void changeLevel(Long userRoleId,Integer level){
		roleExportService.gmChangeLevel(userRoleId, level);
	}
	/**
	 * 修改VIP等级
	 * @param userRoleId
	 * @param level
	 */
	public void setVipLevel(Long userRoleId,Integer level){
		roleVipInfoExportService.setVipLevel(userRoleId, level);
	}
	
    /**
     * 设置魔神精华
     * @param userRoleId
     * @param count
     */
    public void setJinghua(Long userRoleId, Long count) {
        wuXingMoShenExportService.setWuxingMoshenJinghua(userRoleId, count);
    }
    
	public void addCharge(Long userRoleId, Long yb) {
		if(yb > Integer.MAX_VALUE){
			yb = (long)Integer.MAX_VALUE;
		}
		RoleAccountWrapper roleAccount = accountExportService.getAccountWrapper(userRoleId);
		rechargeExportService.rechage(roleAccount.getUserId(), userRoleId, roleAccount.getServerId(),"gmcharge",  System.currentTimeMillis()+"", (double)yb/10, yb);
	}
}
