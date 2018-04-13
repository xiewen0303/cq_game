/**
 * 
 */
package com.junyou.stage.easyaction;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.activityboss.export.ActivityBossExportService;
import com.junyou.bus.activityboss.export.DingShiShuaGuaiExportService;
import com.junyou.bus.chibang.entity.ChiBangInfo;
import com.junyou.bus.qiling.entity.QiLingInfo;
import com.junyou.bus.tianyu.entity.TianYuInfo;
import com.junyou.bus.wuqi.entity.WuQiInfo;
import com.junyou.bus.xianjian.entity.XianJianInfo;
import com.junyou.bus.zhanjia.entity.ZhanJiaInfo;
import com.junyou.bus.zuoqi.entity.ZuoQiInfo;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.module.GameModType;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.role.Role;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.service.RoleBehaviourService;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.KuafuConfigPropUtil;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;
import com.kernel.token.annotation.TokenCheck;

/**
 * 场景基本动作
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2014-12-27 下午3:54:06
 */
@Controller
@EasyWorker(groupName = EasyGroup.STAGE, moduleName = GameModType.STAGE)
public class RoleBehaviourAction {
	
	@Autowired
	private RoleBehaviourService roleBehaviourService;
	@Autowired
	private ActivityBossExportService activityBossExportService; 
	@Autowired
	private DingShiShuaGuaiExportService dingShiShuaGuaiExportService;
	
	@EasyMapping(mapping = ClientCmdType.BEHAVIOR_MOVE,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void move(Message inMsg){

		Object[] data = inMsg.getData();
		
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		int x = (Integer)data[0];
		int y = (Integer)data[1];
		
		roleBehaviourService.move(stageId,userRoleId,x,y);
		
	}
	
	//@EasyMapping(mapping = ClientCmdType.BEHAVIOR_JUMP_TO,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void jump(Message inMsg){
		
		Object[] data = inMsg.getData();
		
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		int eX = (Integer)data[0];
		int eY = (Integer)data[1];
		
		roleBehaviourService.jump(stageId,userRoleId,eX,eY);
		
	}
	@EasyMapping(mapping = ClientCmdType.BEHAVIOR_CHOOSE_TARGET,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void chooseTarget(Message inMsg){
		
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Long targetId = LongUtils.obj2long(inMsg.getData());
		
		roleBehaviourService.chooseTarget(stageId,userRoleId,targetId);
		
	}
	
	
	@EasyMapping(mapping = ClientCmdType.BEHAVIOR_FACE_TO,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void changeFaceTo(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		
		Object[] roleIds = roleBehaviourService.getSurroundRoleId(stageId,roleId);
		if(null != roleIds && roleIds.length > 0){
			StageMsgSender.send2Many(roleIds, ClientCmdType.BEHAVIOR_FACE_TO, inMsg.getData());
		}
	}
	
	
	@EasyMapping(mapping = ClientCmdType.GOODS_PICKUP,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void pickup(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Long id = CovertObjectUtil.obj2long(inMsg.getData());
		
		if(!KuafuConfigPropUtil.isKuafuServer()){
			roleBehaviourService.pickup(id,stageId,roleId);
		}else{
			roleBehaviourService.pickupKuafu(id, stageId, roleId);
		}
	}
	
	/**
	 * 获取目标属性面板
	 */
	@EasyMapping(mapping = ClientCmdType.GET_OTHER_ATTRIBUTE_INFO)
	public void getTargetAttribute(Message inMsg){
		
		Long notifyRoleId = CovertObjectUtil.object2Long(inMsg.getData());
		Long roleId = inMsg.getRoleId();
		
		Object result = roleBehaviourService.getTargetRoleInfo(notifyRoleId);
		StageMsgSender.send2One(roleId, ClientCmdType.GET_OTHER_ATTRIBUTE_INFO, result);
	}
	/**
	 * 公会变更
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.GUILD_CHANGE)
	public void guildChange(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Object[] data = inMsg.getData();
		Long guildId = 0l;
		String guildName = "";
		if(data != null && data.length > 1){
			guildId = CovertObjectUtil.object2Long(data[0]);
			guildName = (String)data[1];
		}
		roleBehaviourService.guildChange(stageId, userRoleId, guildId, guildName);
	}
	/**
	 * 公会等级变更
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.GUILD_LEVEL_CHANGE)
	public void guildLevelChange(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Integer level = inMsg.getData();
		roleBehaviourService.guildLevelChange(stageId, userRoleId, level);
	}
	//打坐
	@EasyMapping(mapping = ClientCmdType.DAZUO,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void daZuo(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		
		Object[] result = roleBehaviourService.daZuo(roleId,stageId,roleId);
		
		StageMsgSender.send2One(roleId, ClientCmdType.DAZUO, result);
		
	}
	
	
	//打坐双休
	//@EasyMapping(mapping = ClientCmdType.DAZUO_SX,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void daZuoSX(Message inMsg){
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Long targetRoleId = CovertObjectUtil.object2Long(inMsg.getData());
 
		roleBehaviourService.daZuo(roleId, stageId,targetRoleId);
	}
	
	//打坐取消
	@EasyMapping(mapping = ClientCmdType.DAZUO_CANCEL,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void daZuoCancel(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		
		roleBehaviourService.dazuoTaskCancel(roleId, stageId);
	}
	
	//内部取消打坐
	@EasyMapping(mapping = InnerCmdType.INNER_DAZUO_CANCEL,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void innerDazuoCancel(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		
		roleBehaviourService.dazuoTaskCancel(roleId, stageId);
	}
	
	
	//获得打坐奖励处理 
	@TokenCheck(component = GameConstants.COMPONENT_DAZUO)
	@EasyMapping(mapping = InnerCmdType.DAZUO_AWARD,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void dazuoAward(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		
		roleBehaviourService.dazuoAward(roleId,stageId,false);
	}
	
	//自动从单休变为双休打坐
	@EasyMapping(mapping = InnerCmdType.DAZUO_2_SX,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void dazuo2SX(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		
		roleBehaviourService.dazuo2SX(roleId,stageId); 
	}
	
	//跳闪值
	@EasyMapping(mapping = InnerCmdType.ADD_TIAOSHAN,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void addTiaoShan(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		
		roleBehaviourService.addTiaoShan(userRoleId,stageId);  
	}
	
	//跳闪状态
	@EasyMapping(mapping = InnerCmdType.STAGE_TIAOSHAN,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void stageTiaoShan(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		 
		
		roleBehaviourService.stageTiaoShan(userRoleId,stageId);  
	}
	
	//跳闪状态
	@EasyMapping(mapping = InnerCmdType.ENTER_STAGE_LATER,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void enterStageLater(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		
		IStage stage = StageManager.getStage(stageId);
		 
		if(stage==null)return;
		
		Role role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null)return;
		
		
		roleBehaviourService.onlineJump(role);  
	}
	
	//上下坐骑
	@EasyMapping(mapping = InnerCmdType.ZUOQI_STATE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void zuoqiState(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		boolean state = inMsg.getData();
		
		roleBehaviourService.zuoqiState(roleId, stageId,state);
	}
	
	//刷新坐骑属性
	@EasyMapping(mapping = InnerCmdType.INNER_ZUOQI_CHANGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void changeZuoqiAttrs(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Object[] datas = inMsg.getData();
		ZuoQiInfo zuoQiInfo = (ZuoQiInfo)datas[0];
		Object[] zqEquips = (Object[])datas[1];
		Integer activatedShenqiNum = (Integer)datas[2];
		List<Integer> huanhuaConfigList = (List<Integer>)datas[3];
		roleBehaviourService.changeZuoqiAttrs(roleId, stageId,zuoQiInfo,huanhuaConfigList,zqEquips,activatedShenqiNum);
	}
	
	//刷新五行属性
	@EasyMapping(mapping = InnerCmdType.INNER_WUXING_CHANGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void changeWuXingAttrs(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		List<Integer> list = inMsg.getData();
		roleBehaviourService.changeWuXingAttrs(roleId, stageId,list);
	}
	
    // 刷新五行技能属性
    @EasyMapping(mapping = InnerCmdType.INNER_WX_SKILL_CHARGE, kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
    public void changeWuXingSkillAttrs(Message inMsg) {
        Long roleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        Map<String, Long> wxSkillAttrMap = inMsg.getData();
        roleBehaviourService.changeWuXingSkillAttrs(roleId, stageId, wxSkillAttrMap);
    }
    
    // 刷新五行精魄属性
    @EasyMapping(mapping = InnerCmdType.INNER_WX_JP_CHARGE, kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
    public void changeWuXingJpAttrs(Message inMsg) {
        Long roleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        Map<String, Long> wxJpAttrMap = inMsg.getData();
        roleBehaviourService.changeWuXingJpAttrs(roleId, stageId, wxJpAttrMap);
    }
    
    //刷新糖宝五行属性
    @EasyMapping(mapping = InnerCmdType.INNER_TB_WUXING_CHANGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
    public void changeTbWuXingAttrs(Message inMsg){
        Long roleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        Map<String, Long> tbWxAttrMap = inMsg.getData();
        roleBehaviourService.changeTbWuXingAttrs(roleId, stageId, tbWxAttrMap);
    }
    
    // 刷新糖宝五行技能属性
    @EasyMapping(mapping = InnerCmdType.INNER_TB_WX_SKILL_CHARGE, kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
    public void changeTbWuXingSkillAttrs(Message inMsg) {
        Long roleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        Map<String, Long> tbWxSkillAttrMap = inMsg.getData();
        roleBehaviourService.changeTbWuXingSkillAttrs(roleId, stageId, tbWxSkillAttrMap);
    }
    
    // 刷新心魔属性
    @EasyMapping(mapping = InnerCmdType.INNER_XINMO_CHANGE, kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
    public void changeXinmoAttrs(Message inMsg) {
        Long roleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        Map<String, Long> xinmoAttrMap = inMsg.getData();
        roleBehaviourService.changeXinmoAttrs(roleId, stageId, xinmoAttrMap);
    }
    
    // 刷新心魔-魔神属性
    @EasyMapping(mapping = InnerCmdType.INNER_XM_MOSHEN_CHARGE, kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
    public void changeXinmoMoshenAttrs(Message inMsg) {
        Long roleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        Map<String, Long> xinmoMoshenAttrMap = inMsg.getData();
        roleBehaviourService.changeXinmoMoshenAttrs(roleId, stageId, xinmoMoshenAttrMap);
    }
    
    
    //刷新仙缘飞化属性
    @EasyMapping(mapping = InnerCmdType.INNER_XIANYUAN_FEIHUA_CHANGE, kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
    public void changeXianYuanFeiHuaAttrs(Message inMsg) {
    	Long roleId = inMsg.getRoleId();
    	String stageId = inMsg.getStageId();
    	Map<String, Long> attrMap = inMsg.getData();
    	roleBehaviourService.changeXianYuanFeiHuaAttrs(roleId, stageId, attrMap);
    }
    
    // 刷新boss积分属性
    @EasyMapping(mapping = InnerCmdType.INNER_BOSS_JIFEN_CHANGE, kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
    public void changeBossJifenAttrs(Message inMsg) {
    	Long roleId = inMsg.getRoleId();
    	String stageId = inMsg.getStageId();
    	Map<String, Long> attrMap = inMsg.getData();
    	roleBehaviourService.changeBossJifenAttrs(roleId, stageId, attrMap);
    }
    
    //刷新套装象位属性(套装象位属性绑定装备部位,和装备穿戴无关,所以单独处理属性)
    @EasyMapping(mapping = InnerCmdType.INNER_XIANGWEI_CHANGE, kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
    public void changeSuitXiangweiAttrs(Message inMsg) {
    	Long roleId = inMsg.getRoleId();
    	String stageId = inMsg.getStageId();
    	Map<String, Long> attrMap = inMsg.getData();
    	roleBehaviourService.changeSuitXiangweiAttrs(roleId, stageId, attrMap);
    }
    
    //刷新神器进阶属性
    @EasyMapping(mapping = InnerCmdType.SHENQI_JINJIE_ATTR_CHANGE, kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
    public void changeShenQiJinJieAttrs(Message inMsg) {
    	Long roleId = inMsg.getRoleId();
    	String stageId = inMsg.getStageId();
    	Map<String, Long> attrMap = inMsg.getData();
    	roleBehaviourService.changeShenQiJinJieAttrs(roleId, stageId, attrMap);
    }
    
    // 刷新心魔-魔神噬体属性
    @SuppressWarnings("unchecked")
    @EasyMapping(mapping = InnerCmdType.INNER_XM_MOSHEN_SHITI_CHARGE, kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
    public void changeXinmoMoshenShitiAttrs(Message inMsg) {
        Long roleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        Object[] obj = inMsg.getData();
        Boolean clearFlag = (Boolean) obj[0];
        Map<String, Long> xinmoMoshenShitiAttrMap = (Map<String, Long>)obj[1];
        roleBehaviourService.changeXinmoMoshenShitiAttrs(roleId, stageId, clearFlag, xinmoMoshenShitiAttrMap);
    }
    
    // 刷新心魔-魔神技能属性
    @EasyMapping(mapping = InnerCmdType.INNER_XM_SKILL_CHARGE, kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
    public void changeXinmoSkillAttrs(Message inMsg) {
        Long roleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        Map<String, Long> xinmoSkillAttrMap = inMsg.getData();
        roleBehaviourService.changeXinmoSkillAttrs(roleId, stageId, xinmoSkillAttrMap);
    }
    
    // 刷新心魔-洗练属性
    @EasyMapping(mapping = InnerCmdType.INNER_XM_XILIAN_CHARGE, kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
    public void changeXinmoXilianAttrs(Message inMsg) {
        Long roleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        Map<String, Long> xmXilianAttrMap = inMsg.getData();
        roleBehaviourService.changeXinmoXilianAttrs(roleId, stageId, xmXilianAttrMap);
    }
    
    // 刷新仙洞属性
    @EasyMapping(mapping = InnerCmdType.INNER_XIANDONG_CHANGE, kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
    public void changeXiandongAttrs(Message inMsg) {
        Long roleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        Map<String, Long> xiandongAttrMap = inMsg.getData();
        roleBehaviourService.changeXiandongAttrs(roleId, stageId, xiandongAttrMap);
    }
    
    // 刷新仙器觉醒属性
    @EasyMapping(mapping = InnerCmdType.INNER_XIANQIJUEXING_CHANGE, kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
    public void changeXianqiJuexingAttrs(Message inMsg) {
        Long roleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        Map<String, Long> xianqiJuexingAttrMap = inMsg.getData();
        roleBehaviourService.changeXianqiJuexingAttrs(roleId, stageId, xianqiJuexingAttrMap);
    }
    
	//等级升级刷新坐骑属性
	@EasyMapping(mapping = InnerCmdType.INNER_ZUOQI_REFRESH,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void refreshZqAttrs(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		
		roleBehaviourService.refreshZqAttrs(roleId, stageId);
	}
	
	//人物角色等级升级刷新圣剑属性
	@EasyMapping(mapping = InnerCmdType.INNER_WUQI_REFRESH,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void refreshWqAttrs(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		
		roleBehaviourService.refreshWqAttrs(roleId, stageId);
	}
	
	
	//坐骑外显变化
	@EasyMapping(mapping = InnerCmdType.INNER_SHOW_UPDATE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void zuoqiShowUdate(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		int showId = inMsg.getData();
		
		roleBehaviourService.zuoqiShowUdate(roleId, stageId,showId);
	}
	
	//圣剑外显变化
	@EasyMapping(mapping = InnerCmdType.WUQI_SHOW_UPDATE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void wuqiShowUdate(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		int showId = inMsg.getData();
		
		roleBehaviourService.wuqiShowUpdate(roleId, stageId,showId);
	}
	
	//翅膀外显变化 
	@EasyMapping(mapping = InnerCmdType.CHIBANG_SHOW_UPDATE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void chibangShowUdate(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		int showId = inMsg.getData();
		
		roleBehaviourService.chiBangShowUdate(roleId, stageId,showId);
	}
	//器灵外显变化 
	@EasyMapping(mapping = InnerCmdType.QILING_SHOW_UPDATE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void qilingShowUdate(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		int showId = inMsg.getData();
		
		roleBehaviourService.qilingShowUdate(roleId, stageId,showId);
	}
	//天羽外显变化 
	@EasyMapping(mapping = InnerCmdType.TIANYU_SHOW_UPDATE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void tianyuShowUdate(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		int showId = inMsg.getData();
		
		roleBehaviourService.qilingShowUdate(roleId, stageId,showId);
	}
	
	//仙剑外显变化
	@EasyMapping(mapping = InnerCmdType.XIANJIAN_SHOW_UPDATE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void xianjianShowUdate(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		int showId = inMsg.getData();
		
		roleBehaviourService.xianjianShowUdate(roleId, stageId,showId);
	}
	//战甲外显变化
	@EasyMapping(mapping = InnerCmdType.ZHANJIA_SHOW_UPDATE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void zhanjiaShowUdate(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		int showId = inMsg.getData();
		
		roleBehaviourService.zhanjiaShowUdate(roleId, stageId,showId);
	}
	
//	//第一次获得坐骑时，将坐骑信息加入场景中
//	@EasyMapping(mapping = InnerCmdType.INNER_ZUOQI_ADD,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
//	public void innerZuoQiAdd(Message inMsg){
//		
//		Long roleId = inMsg.getRoleId();
//		String stageId = inMsg.getStageId(); 
//		ZuoQi zuoQi = inMsg.getData();
//		
//		roleBehaviourService.innerZuoQiAdd(roleId, stageId,zuoQi);
//	}
	
	@EasyMapping(mapping = InnerCmdType.INNER_DSBOSS_RANK)
	public void innerDSBossRank(Message inMsg) {
		long monsterId = inMsg.getData();
		
		String stageId = inMsg.getStageId();
		IStage stage = StageManager.getStage(stageId);
		if(stage == null) return;
		
		activityBossExportService.innerDSBossRank(monsterId,stage);
	}
	
	@EasyMapping(mapping = InnerCmdType.CLOSE_BOSS_HURT_RANK)
	public void activityBossClose(Message inMsg){
		String stageId = inMsg.getStageId();
		IStage stage = StageManager.getStage(stageId);
		if(stage == null) return;
		long monsterId = inMsg.getData();
		
		activityBossExportService.closeBossRank(monsterId, stage);
		
	}
	
	@EasyMapping(mapping = InnerCmdType.RETRIEVE_BOSS)
	public void retrieveBoss(Message inMsg) {
		long monsterId = inMsg.getData();
		
		activityBossExportService.retrieveBoss(monsterId);
	}
	
	
	@EasyMapping(mapping = InnerCmdType.INNER_DS_BOSS_MONSTER)
	public void dsBossMonster(Message inMsg) {
		
		Object[] datas = inMsg.getData();
		
		int bossId = (int)datas[0];
		int line = (int)datas[1]; 
		int disappearDuration = (int)datas[2]; 
		int mapId = (int)datas[3]; 
		String stageId = inMsg.getStageId(); 
		IStage stage = StageManager.getStage(stageId);
		
		if(stage==null)return;
		dingShiShuaGuaiExportService.dsBossMonster(bossId, mapId, line,  stage, disappearDuration);
	}
	
	@EasyMapping(mapping = InnerCmdType.INNER_DS_GUILD_HURT_MONSTER)
	public void dsGuildHurtMonster(Message inMsg) {
		
		Object[] datas = inMsg.getData();
		
		int bossId = (int)datas[0];
		int line = (int)datas[1]; 
		int disappearDuration = (int)datas[2]; 
		
		String stageId = inMsg.getStageId(); 
		IStage stage = StageManager.getStage(stageId);
		
		if(stage==null)return;
		
		dingShiShuaGuaiExportService.dsGuildHurtMonster(bossId, line,  stage, disappearDuration);
	}
	
	@EasyMapping(mapping = InnerCmdType.INNER_DS_MONSTER)
	public void dsMonster(Message inMsg) {
		 
		Object[] datas = inMsg.getData();
		
		int bossId = (int)datas[0];
		int line = (int)datas[1]; 
		int disappearDuration = (int)datas[2]; 
		
		String stageId = inMsg.getStageId(); 
		IStage stage = StageManager.getStage(stageId);
		
		if(stage==null)return;
		
		dingShiShuaGuaiExportService.dsMonster(bossId, line,  stage, disappearDuration);
	}
	
    @EasyMapping(mapping = InnerCmdType.I_MGLY_DS_REFRESH_BOSS)
    public void dsMglyBossMonster(Message inMsg) {
        Object[] datas = inMsg.getData();
        int bossId = (int) datas[0];
        int line = (int) datas[1];
        int disappearDuration = (int) datas[2];
        String stageId = inMsg.getStageId();
        IStage stage = StageManager.getStage(stageId);
        if (stage == null) return;
        dingShiShuaGuaiExportService.dsMglyBossMonster(bossId, line, stage, disappearDuration);
    }

//	//第一次获得翅膀时，将坐骑信息加入场景中
//	@EasyMapping(mapping = InnerCmdType.INNER_CHIBANG_ADD,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
//	public void innerChiBangAdd(Message inMsg){
//		
//		Long roleId = inMsg.getRoleId();
//		String stageId = inMsg.getStageId(); 
//		ChiBang chibang = inMsg.getData();
//		
//		roleBehaviourService.innerChiBangAdd(roleId, stageId,chibang);
//	}
	
	//刷新翅膀属性
	@EasyMapping(mapping = InnerCmdType.INNER_CHIBANG_CHANGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void changeChiBangAttrs(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Object[] datas = inMsg.getData();
		ChiBangInfo zuoQiInfo = (ChiBangInfo)datas[0];
		Object[] zqEquips = (Object[])datas[1];
		Integer activatedShenqiNum = (Integer)datas[2];
		List<Integer> huanhuaConfigList = (List<Integer>)datas[3];
		roleBehaviourService.changeChiBangAttrs(roleId, stageId, zuoQiInfo,huanhuaConfigList, zqEquips,activatedShenqiNum);
	}
	//刷新器灵属性
	@EasyMapping(mapping = InnerCmdType.INNER_QILING_CHANGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void changeQiLingAttrs(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Object[] datas = inMsg.getData();
		QiLingInfo info = (QiLingInfo)datas[0];
		Object[] zqEquips = (Object[])datas[1];
		Integer activatedShenqiNum = (Integer)datas[2];
		List<Integer> huanhuaConfigList = (List<Integer>)datas[3];
		roleBehaviourService.changeQiLingAttrs(roleId, stageId, info,huanhuaConfigList, zqEquips,activatedShenqiNum);
	}
	//刷新天羽属性
	@EasyMapping(mapping = InnerCmdType.INNER_TIANYU_CHANGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void changeTianYuAttrs(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Object[] datas = inMsg.getData();
		TianYuInfo info = (TianYuInfo)datas[0];
		Object[] zqEquips = (Object[])datas[1];
		Integer activatedShenqiNum = (Integer)datas[2];
		List<Integer> huanhuaConfigList = (List<Integer>)datas[3];
		roleBehaviourService.changeTianYuAttrs(roleId, stageId, info,huanhuaConfigList, zqEquips,activatedShenqiNum);
	}
	
	//上下翅膀
	@EasyMapping(mapping = InnerCmdType.INNER_CHIBANG_STATE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void chiBangState(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		boolean state = inMsg.getData();
		
		roleBehaviourService.chibangState(roleId, stageId,state);
	}


	/**
	 * 新增宠物
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.PET_ADD_PET,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void addPet(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		Map<String, Long> attribute = inMsg.getData();
		
		roleBehaviourService.addPet(roleId, stageId,attribute);
	}
	
	/**
	 * 定时增加怒气
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.TIME_ADD_NUQI,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void timeAddNuqi(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		
		roleBehaviourService.timeAddNuqi(roleId, stageId);
	}
	/**
	 * 刷新玩家神器
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.SHENQI_REFRESH,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void shenqiRefresh(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		Integer shenqiId = inMsg.getData();
		roleBehaviourService.shenqiRefresh(roleId, stageId,shenqiId);
	}
	/**
	 * 刷新玩家神器
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.WUXING_FUTI_CHARGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void wuxingfuti(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		Object[] obj = inMsg.getData();
		Integer id = (Integer) obj[0];
		Integer wuxingType = (Integer) obj[1];
		roleBehaviourService.wuxingfuti(roleId, stageId,id,wuxingType);
	}
	
	/**
	 * 玩家激活神器
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.SHENQI_ACTIVATE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void shenqiActivate(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		Object[] data = inMsg.getData();
		Integer shenqiId = (Integer)data[0]; 
		Integer shenqiNum = (Integer)data[1];
		roleBehaviourService.shenqiActivate(roleId, stageId, shenqiId,shenqiNum);
	}
	/**
	 * 怒气大招定时
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.NUQI_SKILL_SCHEDULE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void nuqiSkillSchedule(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		String skillId = inMsg.getData();
		
		roleBehaviourService.nuqiSkillSchedule(roleId, stageId,skillId);
	}
	//刷新仙剑属性
	@EasyMapping(mapping = InnerCmdType.INNER_XIANJIAN_CHANGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void changeXianJianAttrs(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Object[] datas = inMsg.getData();
		XianJianInfo zuoQiInfo = (XianJianInfo)datas[0];
		Object[] zqEquips = (Object[])datas[1];
		Integer activatedShenqiNum = (Integer)datas[2];
		List<Integer> huanhuaConfigList = (List<Integer>)datas[3];
		roleBehaviourService.changeXianJianAttrs(roleId, stageId, zuoQiInfo,huanhuaConfigList, zqEquips,activatedShenqiNum);
	}
	
	//上下仙剑
	@EasyMapping(mapping = InnerCmdType.INNER_XIANJIAN_STATE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void xianJianState(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		boolean state = inMsg.getData();
		
		roleBehaviourService.xianjianState(roleId, stageId,state);
	}
	//刷新战甲属性
	@EasyMapping(mapping = InnerCmdType.INNER_ZHANJIA_CHANGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void changeZhanJiaAttrs(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Object[] datas = inMsg.getData();
		ZhanJiaInfo zuoQiInfo = (ZhanJiaInfo)datas[0];
		Object[] zqEquips = (Object[])datas[1];
		Integer activatedShenqiNum = (Integer)datas[2];
		List<Integer> huanhuaConfigList = (List<Integer>)datas[3];
		roleBehaviourService.changeZhanJiaAttrs(roleId, stageId, zuoQiInfo,huanhuaConfigList, zqEquips,activatedShenqiNum);
	}
	
	//上下战甲
	@EasyMapping(mapping = InnerCmdType.INNER_ZHANJIA_STATE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void zhanJiaState(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		boolean state = inMsg.getData();
		
		roleBehaviourService.zhanjiaState(roleId, stageId,state);
	}
	
	//刷新灵火属性
	@EasyMapping(mapping = InnerCmdType.INNER_LINGHUO_CHANGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void changeLingHuoAttrs(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Integer lingHuoId = inMsg.getData();
		
		roleBehaviourService.changeLingHuoAttrs(roleId, stageId, lingHuoId);
	}
	
    // 刷新灵火祝福属性
    @EasyMapping(mapping = InnerCmdType.I_LINGHUO_BLESS_CHARGE, kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
    public void changeLingHuoBlessAttrs(Message inMsg) {
        Long roleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        Map<String, Long> linghuoBlessAttrMap = inMsg.getData();
        roleBehaviourService.changeLingHuoBlessAttrs(roleId, stageId, linghuoBlessAttrMap);
    }
    
	//刷新成就属性
	@EasyMapping(mapping = InnerCmdType.INNET_CHENGJIU_CHANGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void changeChengJiuAttrs(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		String cjId = inMsg.getData();
		
		roleBehaviourService.changeChengJiuAttrs(roleId, stageId,cjId);
	}
	
	/**
	 * 灵境属性变更
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_LINGJING_CHANGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void changeLingJingAttrs(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Map<String,Long> lingjingAttribute = inMsg.getData();
		
		roleBehaviourService.changeLingJingAttrs(roleId, stageId, lingjingAttribute);
	}
	
	/**
	 * 通知跨服血量回复
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_KF_HUIFU_HP,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void kuafuHpHuifu(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Integer hp = inMsg.getData();
		
		roleBehaviourService.kuafuHpHuifu(roleId, stageId, hp);
	}
	/**
	 * 玩家激活称号
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_CHENGHAO_ACTIVATE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void chenghaoActivate(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		Integer chenghaoId = inMsg.getData();
		roleBehaviourService.chenghaoActivate(roleId, stageId, chenghaoId);
	}
	/**
	 * 玩家称号穿卸
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_CHENGHAO_REFRESH,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void chenghaoRefresh(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		Object[] data = inMsg.getData();
		Boolean wear = (Boolean) data[0];
		Integer oldChenghaoId =  (Integer) data[1];
		Integer chenghaoId = (Integer) data[2];
		String chenghaoRes = (String) data[3];
		Object[] refreshData = (Object[]) data[4];
		roleBehaviourService.chenghaoRefresh(roleId, stageId,wear,oldChenghaoId, chenghaoId,chenghaoRes, refreshData);
	}
	/**
	 * 玩家称号穿卸
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_CHENGHAO_REMOVE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void chenghaoRemove(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		Integer chenghaoId= inMsg.getData();
		roleBehaviourService.chenghaoRemove(roleId, stageId, chenghaoId);
	}
	/**
	 * 妖神属性变化
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_YAOSHEN_ATTR_CHANGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void yaoshenAttrChange(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		Object[] data= inMsg.getData();
		Integer jie = (Integer)data[0];
		Integer ceng = (Integer)data[1];
		Integer qndNum = (Integer)data[2];
		Integer czdNum = (Integer)data[3];
		roleBehaviourService.yaoshenAttrChange(roleId, stageId,jie,ceng,qndNum,czdNum);
	}
	/**
	 * 妖神魔纹属性变化
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_YAOSHEN_MOWEN_CHANGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void yaoshenMowenAttrChange(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		Object[] data= inMsg.getData();
		Integer jie = (Integer)data[0];
		Integer ceng = (Integer)data[1];
		Integer qndNum = (Integer)data[2];
		Integer czdNum = (Integer)data[3];
		roleBehaviourService.yaoshenMowenAttrChange(roleId, stageId,jie,ceng,qndNum,czdNum);
	}
	/**
	 * 糖宝心纹属相变化
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_TANGBAO_XINWEN_ATTR_CHANGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void tangbaoXinwenAttrChange(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		Object[] data= inMsg.getData();
		Integer jie = (Integer)data[0];
		Integer ceng = (Integer)data[1];
		Integer qndNum = (Integer)data[2];
		Integer czdNum = (Integer)data[3];
		roleBehaviourService.tangbaoXinwenAttrChange(roleId, stageId,jie,ceng,qndNum,czdNum);
	}
	/**
	 * 妖神魂魄属性变化
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_YAOSHEN_HUNPO_CHANGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void yaoshenHunpoAttrChange(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		Object[] data= inMsg.getData();
		Integer jie = (Integer)data[0];
		Integer ceng = (Integer)data[1];
		Integer qndNum = (Integer)data[2];
		Integer czdNum = (Integer)data[3];
		roleBehaviourService.yaoshenHunpoAttrChange(roleId, stageId,jie,ceng,qndNum,czdNum);
	}
	/**
	 * 妖神魔印属性变化
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_YAOSHEN_MOYIN_ATTR_CHANGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void yaoshenMoYinAttrChange(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		Object[] data= inMsg.getData();
		Integer jie = (Integer)data[0];
		Integer ceng = (Integer)data[1];
		Integer qndNum = (Integer)data[2];
		Integer czdNum = (Integer)data[3];
		roleBehaviourService.yaoshenMoYinAttrChange(roleId, stageId,jie,ceng,qndNum,czdNum);
	}
	

	/**
	 * 妖神附魔属性变化
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_YAOSHEN_HUMO_ATTR_CHANGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void yaoshenFumoAttrChange(Message inMsg){
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		 Map<String, Long> attr = inMsg.getData();
		roleBehaviourService.yaoshenFumoAttrChange(roleId, stageId,attr );
	}
	
	/**
	 * 升级成神
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_CHENG_SHEN_SJ_ATTR_CHANGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void chengShenAttrChange(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		int level = inMsg.getData(); 
		roleBehaviourService.chengShenAttrChange(roleId, stageId,level);
	}
	/**
	 * 通天之路获取属性
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.TONGTIAN_ROAD_GET_ATTR,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void tongtianLoadAttrChange(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		Map<String, Long> attr= inMsg.getData(); 
		roleBehaviourService.tongtianLoadAttrChange(roleId, stageId,attr);
	}
	/**
	 * 妖神魄神镶嵌属性变化
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_YAOSHEN_HUNPO_POSHEN_CHANGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void yaoshenHunpoPoshenAttrChange(Message inMsg){ 
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		roleBehaviourService.yaoshenHunpoAttrChange(roleId, stageId );
	}
	/**
	 * 掐换妖神外显
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_YAOSHEN_SHOW_CHANGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void yaoshenShowChange(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		Integer isShow= inMsg.getData();
		roleBehaviourService.yaoshenShowChange(roleId, stageId,isShow);
	}
	/**
	 * 宝石属性变化
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_JEWEL_ATTR_CHANGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void jewelShowChange(Message inMsg){
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		Object[]  data  = inMsg.getData();
		int type = (Integer)data[0];
		Map<String, Long> attr= (Map<String, Long>)data[1];
		roleBehaviourService.jewelAttrChange(type, attr,roleId,stageId);
	}
	/**
	 * 洗练属性变化
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_SHENQI_WASH_ATTR_CHANGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void shenQiWashChange(Message inMsg){
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		Map<String, Long> attr = inMsg.getData();
		roleBehaviourService.shenQiWashChange(attr,roleId,stageId);
	}

    /**
     * 宠物属性变化
     * 
     * @param inMsg
     */
    @EasyMapping(mapping = InnerCmdType.CHONGWU_ATTR_CHANGE, kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
    public void chongwuAttrChange(Message inMsg) {
        Long roleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        Map<String, Long> attr = inMsg.getData();
        roleBehaviourService.chongwuAttrChange(roleId, stageId, attr);
    }

    /**
     * 宠物技能属性变化
     * 
     * @param inMsg
     */
    @EasyMapping(mapping = InnerCmdType.CHONGWU_Skill_ATTR_CHANGE, kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
    public void chongwuSkillAttrChange(Message inMsg) {
        Long roleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        Map<String, Long> attr = inMsg.getData();
        roleBehaviourService.chongwuSkillAttrChange(roleId, stageId, attr);
    }
    
	/**
	 * 宠物切换
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.CHONGWU_SHOW_UPDATE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void chongwuShowUpdate(Message inMsg){
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Long id = null;
		String name = null;
		Integer configId =null;
		Integer speed = null;
		if(inMsg.getData()!=null){
			Object[] dataArray = inMsg.getData();
			id = (Long)dataArray[0];
			name = (String)dataArray[1];
			configId = (Integer)dataArray[2];
			speed = (Integer)dataArray[3];
		}
		roleBehaviourService.chongwuShowUpdate(roleId, stageId,id,name,configId,speed);
	}
	/**
	 * 时装激活属性变化
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_CHANGE_SHIZHUANG_ACTIVE_ATT,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void shizhuangActiveChange(Message inMsg){
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		Map<String, Long> attr= inMsg.getData();
		roleBehaviourService.shizhuangActiveChange(attr,roleId,stageId);
	}
	/**
	 * 时装升级属性变化
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_CHANGE_SHIZHUANG_SHENGJI_ATT,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void shizhuangLevelChange(Message inMsg){
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		Map<String, Long> attr= inMsg.getData();
		roleBehaviourService.shizhuangLevelChange(attr,roleId,stageId);
	}
	/**
	 * 时装升级属性变化
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_CHANGE_SHIZHUANG_JINJIE_ATT,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void shizhuangJinJieChange(Message inMsg){
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		Map<String, Long> attr= inMsg.getData();
		roleBehaviourService.shizhuangJinjieChange(attr,roleId,stageId);
	}
	/**
	 * 时装变装
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_CHANGE_SHIZHUANG,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void shizhuangChange(Message inMsg){
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId(); 
		Integer id = inMsg.getData();
		roleBehaviourService.shizhuangChange(roleId,stageId,id);
	}
	/**
	 * 宠物移动同步
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.CHONGWU_MOVE_SYN,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void chongwuMoveSyn(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Object[] position = inMsg.getData();
		Integer x = (Integer)position[0];
		Integer y = (Integer)position[1];
		Long chongwuGuid = LongUtils.obj2long(position[2]);
		roleBehaviourService.chongwuMoveSyn(userRoleId, stageId,x, y,chongwuGuid);
	}
	
	/**
	 * 人物属性变化
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_CHANGE_ROLE_ATT,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void changeRoleAtt(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Object[] data = inMsg.getData();
		Integer type = (Integer)data[0];
		Map<String,Long> atts = (Map<String,Long>)data[1];
		roleBehaviourService.changeRoleAtt(userRoleId, stageId, type, atts);
	}
	/**
	 * 配偶变化
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MARRY_CHANGE_PEIOU,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void changePeiou(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		String peiou = inMsg.getData();
		roleBehaviourService.changePeiou(userRoleId, stageId, peiou);
	}
	/**
	 * 信物变化
	 * 
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MARRY_CHANGE_XINWU,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void changeXinwu(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Integer xinwu = inMsg.getData();
		roleBehaviourService.changeXinwu(userRoleId, stageId, xinwu);
	}
	/**
	 * 取消技能熟练度监听
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.ROLE_CANCEL_NOTICESKILLS,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void cancelNoticeSkills(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		String skillId = inMsg.getData();
		roleBehaviourService.cancelNoticeSkills(userRoleId, stageId, skillId);
	}
	/**
	 * 增加技能熟练度监听
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.ROLE_ADD_NOTICESKILLS,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void addNoticeSkills(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		List<String> skillIds = inMsg.getData();
		roleBehaviourService.addNoticeSkills(userRoleId, stageId, skillIds);
	}
	/**
	 * 转生等级变化
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.ZHUANSHENG_LEVEL_CHANGE,kuafuType = EasyKuafuType.KFING_S2KF_TYPE)
	public void zhuanshengLevelChange(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Integer level = inMsg.getData();
		roleBehaviourService.zhuanshengLevelChange(userRoleId, stageId, level);
	}
	//刷新画卷属性
	@EasyMapping(mapping = InnerCmdType.HUAJUAN_ATTR_CHANGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void huajuanAttrChange(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Map<String, Long> attr  = inMsg.getData();
		roleBehaviourService.huajuanAttrChange(roleId,stageId,attr);
	}
	
	/**
	 * 刷新画卷2属性
	 * @param inMsg
	 */
    @EasyMapping(mapping = InnerCmdType.I_HUANJUAN2_CHARGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
    public void huajuan2AttrChange(Message inMsg){
        Long roleId = inMsg.getRoleId();
        String stageId = inMsg.getStageId();
        Map<String, Long> attr  = inMsg.getData();
        roleBehaviourService.huajuan2AttrChange(roleId,stageId,attr);
    }
    
    /**
     * 刷新圣剑属性
     * @param inMsg
     */
  	@EasyMapping(mapping = InnerCmdType.INNER_WUQI_CHANGE,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
  	public void changeWuqiAttrs(Message inMsg){
  		Long roleId = inMsg.getRoleId();
  		String stageId = inMsg.getStageId();
  		Object[] datas = inMsg.getData();
  		WuQiInfo zuoQiInfo = (WuQiInfo)datas[0];
  		Object[] zqEquips = (Object[])datas[1];
  		Integer activatedShenqiNum = (Integer)datas[2];
  		List<Integer> huanhuaConfigList = (List<Integer>)datas[3];
  		roleBehaviourService.changeWuqiAttrs(roleId, stageId,zuoQiInfo,huanhuaConfigList,zqEquips,activatedShenqiNum);
  	}
}
