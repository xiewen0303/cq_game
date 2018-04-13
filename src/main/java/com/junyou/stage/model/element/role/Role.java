/**
 * 
 */
package com.junyou.stage.model.element.role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.junyou.bus.chibang.manage.ChiBang;
import com.junyou.bus.qiling.manage.QiLing;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.shenqi.componentlistener.ShenqiListener;
import com.junyou.bus.skill.entity.RoleSkill;
import com.junyou.bus.tianyu.manage.TianYu;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.wuqi.manage.WuQi;
import com.junyou.bus.xianjian.manage.XianJian;
import com.junyou.bus.zhanjia.manage.ZhanJia;
import com.junyou.bus.zuoqi.manage.ZuoQi;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.export.PathNodeSize;
import com.junyou.gameconfig.publicconfig.configure.export.NuqiPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.TanBaoPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.TuChenExpPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.helper.PublicConfigureHelper;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.entity.TeamMember;
import com.junyou.stage.hundun.entity.HundunStage;
import com.junyou.stage.model.attribute.role.IRoleFightAttribute;
import com.junyou.stage.model.core.element.AbsFighter;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.fight.IFightStatistic;
import com.junyou.stage.model.core.hatred.IHatredManager;
import com.junyou.stage.model.core.help.GongGongServiceHelper;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.skill.IBuffManager;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IElementSearchFilter;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.PointTakeupType;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.state.IStateManager;
import com.junyou.stage.model.element.biaoche.Biaoche;
import com.junyou.stage.model.element.chongwu.Chongwu;
import com.junyou.stage.model.element.componentlistener.PetListener;
import com.junyou.stage.model.element.goods.Goods;
import com.junyou.stage.model.element.monster.Monster;
import com.junyou.stage.model.element.pet.Pet;
import com.junyou.stage.model.fight.BattleMode;
import com.junyou.stage.model.fight.SkillProcessHelper;
import com.junyou.stage.model.hatred.IRoleHatredManager;
import com.junyou.stage.model.prop.PropModel;
import com.junyou.stage.model.skill.PublicCdManager;
import com.junyou.stage.model.skill.SkillFactory;
import com.junyou.stage.model.skill.SkillManager;
import com.junyou.stage.model.stage.kuafuquanxianyan.KuaFuQunXianYanRank;
import com.junyou.stage.model.stage.kuafuquanxianyan.KuafuQunXianYanStage;
import com.junyou.stage.model.stage.kuafuyungong.KuafuYunGongStage;
import com.junyou.stage.model.stage.tanbao.TanBaoManager;
import com.junyou.stage.model.stage.tanbao.TanBaoRoleVo;
import com.junyou.stage.model.stage.territory.TerritoryStage;
import com.junyou.stage.model.stage.zhengbasai.HcZhengBaSaiStage;
import com.junyou.stage.model.state.DeadState;
import com.junyou.stage.schedule.StageControlRunable;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.stage.shenmo.entity.ShenMoStage;
import com.junyou.stage.tunnel.BufferedMsgWriter;
import com.junyou.stage.tunnel.DirectMsgWriter;
import com.junyou.stage.tunnel.IMsgWriter;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.stage.utils.StageHelper;
import com.junyou.utils.KuafuConfigPropUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.gen.id.IdFactory;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-11-30上午9:32:32
 */
public class Role extends AbsFighter implements IRole {
	

	private IRoleFightAttribute roleFightAttribute;
	
	private SkillFactory skillFactory;
	
	private IFightStatistic fightStatistic;
	
	private IStateManager stateManager;
	
	private IBuffManager buffManager;
	
	private IHatredManager hatredManager;
	
	private BusinessData roleBusinessData;
	
	private PublicCdManager cdManager;
	
	private PropModel propModel;
	
	private ZuoQi zuoQi;
	
	private Pet pet;
	
	private int yinshen;//隐身状态
	
	private int gm;
	
	private Object[] moveData;//移动坐标数据
	
	private boolean changing;
	
	private Biaoche biaoche;
	
	private Object[] otherData;//其他人查看的数据
	
	private Long collectId;//当前采集id
	
	private long collectFinishTime;//采集完成时间
	
	private Object[] msgData;//人物数据
	
	private Object[] sameMsgData;//同模人物数据
	
	private int nuqi;
	
	private Integer zyCamp;//阵营战中所属阵营
	
	private String peiou;//配偶名字
	private Integer xinwu;//结婚信物
	
	private Set<String> noticeSkills;//需要通知熟练度变化的技能
	
	private Map<String,RoleSkill> skillShulians = new HashMap<>();
	
    private int yaoshenMowenJie=0;//妖神魔纹阶数
	
	private boolean isYaoshen=false;
	
	private Integer shizhuang;
	
	private WuQi wuQi; //新圣剑
	
	private Long dazuoTime;//开始打坐时间戳
	
	public int yaoshenMowenJie() {
		return yaoshenMowenJie;
	}
	public void setYaoshenMowenJie(int yaoshenMowenJie) {
		this.yaoshenMowenJie = yaoshenMowenJie;
	}
	
	public boolean isYaoshen() {
		return isYaoshen;
	}

	public void setYaoshen(boolean isYaoshen) {
		this.isYaoshen = isYaoshen;
	}

	public Integer getZyCamp() {
		return zyCamp;
	}

	public void setZyCamp(Integer zyCamp) {
		this.zyCamp = zyCamp;
	}
	
	public boolean isYinshen() {
		return yinshen == 1;
	}

	public void setYinshen(int yinshen) {
		this.yinshen = yinshen;
	}

	public boolean isGm(){
		return gm == 1;
	}
	
	public void setIsGm(int gm){
		this.gm = gm;
	}

	/**
	 * @param id
	 */
	public Role(Long id,String name) {
		super(id,name);
	}


	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
		stateManager.addListener(new PetListener(this));
	}
	
	public void addShenqiListener(){
		stateManager.addListener(new ShenqiListener(this));
	}

	@Override
	public Object getStageData() {
		try {
			return DependentDataConvertUtil.stageDataOutput(this);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
			return null;
		}
	}
	
	public PathNodeSize getPathNodeSize(){
		return PathNodeSize._1X;
	}
	
	
	/**
	 *  0 int 角色配置标识 
		1 String 唯一标识Guid 
		2 String 主人名字 
		3 int x坐标 
		4 int y坐标 
	 * @return
	 *//*
	public Object getBoothMsgData(){
		
		return new Object[]{
				getBusinessData().getRoleConfigId()
				,getId()
				,getName()
				,getPosition().getX()
				,getPosition().getY()
		};
	}*/
	/**
	 * 获取同模数据
	 * @return
	 */
	public Object getSameMsgData(Short cmd){
		if(cmd == null){
			return null;
		}else if(cmd.equals(ClientCmdType.AOI_XIANGONG_SAME_CMD)){
			if(sameMsgData == null){
				sameMsgData = new Object[]{
						getId()
						,getFightAttribute().getCurHp()
						,getFightAttribute().getMaxHp()
						,getPosition().getX()
						,getPosition().getY()
						,getStateManager().getClientState()
						,getFightAttribute().getSpeed()
						,getBuffClientMsg()
				};
			}else{
				int index = 1;
				sameMsgData[index++] = getFightAttribute().getCurHp();
				sameMsgData[index++] = getFightAttribute().getMaxHp();
				sameMsgData[index++] = getPosition().getX();
				sameMsgData[index++] = getPosition().getY();
				sameMsgData[index++] = getStateManager().getClientState();
				sameMsgData[index++] = getFightAttribute().getSpeed();
				sameMsgData[index++] = getBuffClientMsg();
			}
		}else if(cmd.equals(ClientCmdType.AOI_WENQUAN_SAME_CMD)){
			if(sameMsgData == null){
				sameMsgData = new Object[]{
						getId()
						,getBusinessData().getRoleConfigId()
						,getPosition().getX()
						,getPosition().getY()
						,getFightAttribute().getSpeed()
						,getName()
						,getBusinessData().getGuildId()
						,getBusinessData().getGuildName()
						,getBusinessData().getLevel()
						,getFightAttribute().getCurHp()
						,getFightAttribute().getMaxHp()
						,getWuQiShowId()
						,getChiBangShowId()
						,getBusinessData().getVipLevel()
				};
			}else{
				int index = 1;
				sameMsgData[index++] = getBusinessData().getRoleConfigId();
				sameMsgData[index++] = getPosition().getX();
				sameMsgData[index++] = getPosition().getY();
				sameMsgData[index++] = getFightAttribute().getSpeed();
				sameMsgData[index++] = getName();
				sameMsgData[index++] = getBusinessData().getGuildId();
				sameMsgData[index++] = getBusinessData().getGuildName();
				sameMsgData[index++] = getBusinessData().getLevel();
				sameMsgData[index++] = getFightAttribute().getCurHp();
				sameMsgData[index++] = getFightAttribute().getMaxHp();
				sameMsgData[index++] = getWuQiShowId();
				sameMsgData[index++] = getChiBangShowId();
			}
		}else if(cmd.equals(ClientCmdType.AOI_HUNDUN_SAME_CMD)){
			if(sameMsgData == null){
				sameMsgData = new Object[]{
						getId()//0 String Guid 
						,getBusinessData().getRoleConfigId()//1 int 角色配置ID 
						,getFightAttribute().getCurHp()//2 int 当前血量 
						,getFightAttribute().getMaxHp()//3 int 最大血值 
						,getPosition().getX()//4 int x坐标 
						,getPosition().getY()//5 int y坐标 
						,getStateManager().getClientState()//6 int 状态 
						,getFightAttribute().getSpeed()//7 速度
						,getBuffClientMsg()//8buff
						
				};
			}else{
				int index = 2;
				sameMsgData[index++] = getFightAttribute().getCurHp();
				sameMsgData[index++] = getFightAttribute().getMaxHp();
				sameMsgData[index++] = getPosition().getX();
				sameMsgData[index++] = getPosition().getY();
				sameMsgData[index++] = getStateManager().getClientState();
				sameMsgData[index++] = getFightAttribute().getSpeed();
				sameMsgData[index++] = getBuffClientMsg();
			}
		}else{
			return null;
		}
		return sameMsgData;
	}
	
	@Override
	public Object getMsgData() {
		
		try {
			
//		Object[] buffList = null;
//		Collection<IBuff> buffs = getBuffManager().getBuffs();
//		if(buffs != null && buffs.size() > 0){
//			buffList = new Object[buffs.size()];
//			
//			int index = 0;
//			for (IBuff iBuff : buffs) {
//				buffList[index++] = iBuff.getClientMsg();
//			}
//		}
//		ChuanQiLog.error("玩家【"+this.getName()+"("+this.getId()+")】打坐对象ID:"+getBusinessData().getDazuoTargetRoleId());
			
			if(msgData == null){
				msgData = new Object[]{
						getBusinessData().getRoleConfigId(),//0 int 角色对照表配置id 
						getId(),//1 Number 角色标识guid 
						getStageName(),//2 String 名字 
						getLevel(),//3 int 等级 
						getBusinessData().getPlatformInfo(), //平台数据 
						getPosition().getX(),//5 int x坐标 
						getPosition().getY(),//6 int y坐标 
						getFightAttribute().getCurHp(),//7 int 当前血量 
						getFightAttribute().getMaxHp(),//8 int 最大血量 
						getStateManager().getClientState(),//9 Boolean true 活着 false 死亡 
						getBusinessData().getGuildId(),	//10 公会id
						getBusinessData().getGuildName(),	//11公会名字
						getBusinessData().getDazuoTargetRoleId(),//与谁打坐(guid)(单休:发自己的guid,双修:打坐人的guid,0为无打坐)
						getBusinessData().getServerId(), //玩家所在服务器ID
						getBusinessData().getHuiminTime() > 0, //14是否是灰名
						getZuoQiShowId(), //坐骑外显Id
						getFightAttribute().getSpeed(),	//速度
						getZuoQiState(), //坐骑状态
						getChiBangShowId(),//翅膀外显
						getShenqiId(),//神器id
						getBusinessData().getVipLevel(),//VIP等级
						getBusinessData().getPkValue(),//PK值
						getTeamId() //teamId,在阵营战中，这个值为阵营id
						,getBusinessData().getPetState(),	//糖宝状态
						getBuffClientMsg(), //当前buff列表
						getBusinessData().getChenghao(), // 当前佩戴称号
						isYaoshen(),//是否是妖神状态
						getBusinessData().getChongwuId()//出战宠物id
						,getShiZhuang()
						,getBusinessData().isHcZbsWinnerGuildLeader()//是否是争霸赛胜利帮派的门主
						,new Object[]{xinwu,peiou}
						,yaoshenMowenJie()//妖神魔纹
						,getQiLingShowId() //器灵
						,getBusinessData().getZhuanShengLevel()//转生等级
						,getTianYuShowId()//天羽
						,getBusinessData().getWuxingId()//五行附体ID
						,getBusinessData().isKfYunGongWinnerGuildLeader()//是否是跨服云宫之巅胜利帮派的门主
						,0//仙位ID
						,getWuQiShowId()
				};
			}else{
				int index = 0;
				msgData[index++] = getBusinessData().getRoleConfigId();//0 int 角色对照表配置id 
				index++;//1 Number 角色标识guid 永远不会变
				msgData[index++] = getStageName();//2 String 名字 
				msgData[index++] = getLevel();//3 int 等级 
				msgData[index++] = getBusinessData().getPlatformInfo(); //平台数据 
				msgData[index++] = getPosition().getX();//5 int x坐标 
				msgData[index++] = getPosition().getY();//6 int y坐标 
				msgData[index++] = getFightAttribute().getCurHp();//7 int 当前血量 
				msgData[index++] = getFightAttribute().getMaxHp();//8 int 最大血量 
				msgData[index++] = getStateManager().getClientState();//9 Boolean true 活着 false 死亡 
				msgData[index++] = getBusinessData().getGuildId();	//10 公会id
				msgData[index++] = getBusinessData().getGuildName();	//11公会名字
				msgData[index++] = getBusinessData().getDazuoTargetRoleId();//与谁打坐(guid)(单休:发自己的guid,双修:打坐人的guid,0为无打坐)
				msgData[index++] = getBusinessData().getServerId(); //玩家所在服务器ID
				msgData[index++] = getBusinessData().getHuiminTime() > 0; //是否是灰名
				msgData[index++] = getZuoQiShowId(); //坐骑外显Id
				msgData[index++] = getFightAttribute().getSpeed();	//速度
				msgData[index++] = getZuoQiState(); //坐骑状态
				msgData[index++] = getChiBangShowId();//翅膀外显
				msgData[index++] = getShenqiId();//神器id
				msgData[index++] = getBusinessData().getVipLevel();//VIP等级
				msgData[index++] = getBusinessData().getPkValue();//PK值
				msgData[index++] = getTeamId(); //teamId,在阵营战中，这个值为阵营id
				msgData[index++] = getBusinessData().getPetState();	//糖宝状态
				msgData[index++] = getBuffClientMsg(); //当前buff列表
				msgData[index++] = getBusinessData().getChenghao(); // 当前佩戴称号
				msgData[index++] = isYaoshen();//是否是妖神状态
				msgData[index++] = getBusinessData().getChongwuId();//出战宠物id
				msgData[index++] = getShiZhuang();//时装
				msgData[index++] = getBusinessData().isHcZbsWinnerGuildLeader();//是否是争霸赛胜利帮派的门主
				Object[] marryInfo = (Object[])msgData[index++];
				marryInfo[0] = xinwu;
				marryInfo[1] = peiou;
				msgData[index++] = yaoshenMowenJie();//妖神魔纹
				msgData[index++] = getQiLingShowId(); //器灵
				msgData[index++] = getBusinessData().getZhuanShengLevel(); //转生等级
				msgData[index++] = getTianYuShowId();//天羽
				msgData[index++] = getBusinessData().getWuxingId();//五行附体ID
				msgData[index++] = getBusinessData().isKfYunGongWinnerGuildLeader();//是否是跨服云宫之巅胜利帮派的门主
				msgData[index++] = 0;
				msgData[index++] = getWuQiShowId();
				
				
			}
		} catch (Exception e) {
			ChuanQiLog.error("",e);
			return null;
		}
		return msgData;
	}
	
	private Object[] getBuffClientMsg(){
		Collection<IBuff> buffs = getBuffManager().getBuffs();
		if(buffs == null || buffs.size() == 0){
			return null;
		}
		int buffSize = getBuffManager().getBuffs().size();
		Object[] ret = new Object[buffSize];
		int index = 0;
		for(IBuff e:buffs){
			ret[index] = e.getClientMsg();
			index++;
		}
		return ret;
	}
	public Integer getTeamId(){
		if(getStage() == null){
			return null;
		}
		if(getStage().getStageType() == StageType.CAMP){
			return getZyCamp();
		}else if(getStage().getStageType() == StageType.SHENMO_FUBEN){
			ShenMoStage stage = (ShenMoStage)getStage();
			return stage.getTeamId(getId());
		}else{
			if(getTeamMember() != null){
				return getTeamMember().getTeamId(); 
			}else{
				return null;
			}
		}

	}
	/**
	 * 获取糖宝仙剑id
	 * @return
	 */
	public Integer getXianJianShowId(){
		XianJian xianjian = getBusinessData().getXianjian();
		if(xianjian == null){
			return null;
		}else{
			return xianjian.getShowId();
		}
	}
	/**
	 * 获取糖宝战甲id
	 * @return
	 */
	public Integer getZhanJiaShowId(){
		ZhanJia xianjian = getBusinessData().getZhanjia();
		if(xianjian == null){
			return null;
		}else{
			return xianjian.getShowId();
		}
	}
	/**
	 * 坐骑外显ID
	 * @return
	 */
	public int getZuoQiShowId(){
		if(getZuoQi() == null){
			return -1;
		} 
		return  getZuoQi().getShowId();
	}
	/**
	 * 器灵外显ID
	 * @return
	 */
	public Integer getQiLingShowId(){
		QiLing qiling = this.getBusinessData().getQiling();
		if(qiling == null){
			return null;
		}
		return qiling.isGetOn() ? qiling.getShowId():null;
	}
	
	/**
	 * 天羽外显ID
	 * @return
	 */
	public Integer getTianYuShowId(){
		TianYu tianyu = this.getBusinessData().getTianyu();
		if(tianyu == null){
			return null;
		}
		return tianyu.isGetOn() ? tianyu.getShowId():null;
	}
	
	//坐骑状态
	public boolean getZuoQiState(){
		return getZuoQi()== null ? false:getZuoQi().isGetOn();
	}
	
	/**
	 * 翅膀外显Id
	 * @return
	 */
	public int getChiBangShowId(){
		ChiBang chiBang = this.getBusinessData().getChibang();
		if(chiBang == null){
			return -1;
		}
		return chiBang.isGetOn() ? chiBang.getShowId():-1;
	}
	
	/**
	 * 神器id
	 * @return
	 */
	private int getShenqiId(){
		Integer shenqiId = this.getBusinessData().getShenqiId();
		if(shenqiId == null || getStage() == null || !getStage().isCanUseShenQi()){
			return 0;
		}
		return shenqiId;
	}
	
	
	public boolean isRedRole(){
		return getBusinessData().getPkValue() > 0;
//		return getBusinessData().getPkValue() >= getBusinessData().getRedPkValue();
	}
	
	@Override
	public ElementType getElementType() {
		return ElementType.ROLE;
	}
	
	@Override
	public short getEnterCommand() {
		return ClientCmdType.AOI_ROLES_ENTER;
	}

	public String getName() {
		return roleBusinessData.getName();
	}
	public String getStageName() {
		if(getStage() != null){
			if(StageType.CAMP.equals(getStage().getStageType())){
				return null;
			}
		}
		return roleBusinessData.getName();
	}

	public int getLevel() {
		return roleBusinessData.getLevel();
	}

	@Override
	public IRoleFightAttribute getFightAttribute() {
		return roleFightAttribute;
	}

	@Override
	public IFightStatistic getFightStatistic() {
		return fightStatistic;
	}

	/**
	 * 设置技能工厂
	 * @param
	 */
	public void setSkillFactory(SkillFactory skillFactory) {
		this.skillFactory = skillFactory;
	}

	/**
	 * 获取技能工厂
	 * @param
	 */
	public SkillFactory getSkillFactory() {
		return skillFactory;
	}

	public void setRoleFightAttribute(IRoleFightAttribute roleFightAttribute) {
		this.roleFightAttribute = roleFightAttribute;
	}

	public void setFightStatistic(IFightStatistic fightStatistic) {
		this.fightStatistic = fightStatistic;
	}

	@Override
	public IStateManager getStateManager() {
		return stateManager;
	}

	public void setStateManager(IStateManager stateManager) {
		this.stateManager = stateManager;
	}

	@Override
	public IBuffManager getBuffManager() {
		return buffManager;
	}
	
	public void setBuffManager(IBuffManager buffManager){
		this.buffManager = buffManager;
	}

	@Override
	public IHatredManager getHatredManager() {
		return hatredManager;
	}

	public void setHatredManager(IRoleHatredManager roleHatredManager) {
		this.hatredManager = roleHatredManager;
	}

	@Override
	public Integer getCamp() {
		return GameConstants.ROLE_CAMP;
	}

	@Override
	public void setCamp(String camp) {
		
	}

	@Override
	public BusinessData getBusinessData() {
		return roleBusinessData;
	}

	@Override
	public void setBusinessData(BusinessData data) {
		this.roleBusinessData = data;
	}

	@Override
	public void setTeamMember(TeamMember member) {
		if(member != null){
			member.setRole(this);
		}
		this.roleBusinessData.setTeamMember(member);
	}
	
	@Override
	public TeamMember getTeamMember() {
		return getBusinessData().getTeamMember();
	}
	
	@Override
	public void deadHandle(IHarm harm) {
		notifyEnemyChange(harm);
		
		getHatredManager().clear();
		getBuffManager().clearBuffsByDead();
		getStateManager().add(new DeadState(harm));
//		getTiLiManager().stopRecover();
		
//		clearTraps();
		clearCollect();//取消采集状态
		
		//宠物清除
		Pet pet = getPet();
		if(pet != null){
			this.getStage().leave(pet);
		}
		//取消神器攻击
		cancelShenqiAttackSchedule();
		
		//阵营战角色死亡（并且攻击者是玩家）
		if(getZyCamp() != null && ElementType.isRole(harm.getAttacker().getElementType())){
			StageMsgSender.send2StageInner(harm.getAttacker().getId(), getStage().getId(), InnerCmdType.S_CAMP_DEAD, new Object[]{GameConstants.ROLE_DEAD,harm.getTarget().getId()});
		}
		//跨服大乱斗角色死亡（并且攻击者是玩家）
		if(getStage().getStageType() == StageType.KUAFUDALUANDOU && ElementType.isRole(harm.getAttacker().getElementType())){
			StageMsgSender.send2StageInner(harm.getAttacker().getId(), getStage().getId(), InnerCmdType.KUAFULUANDOU_ADD_JIFEN, null);
		}
		
		//跨服巅峰之战角色死亡（并且攻击者是玩家）
        if(getStage().getStageType() == StageType.KUAFUDIANFENG && ElementType.isRole(harm.getAttacker().getElementType())){
            StageMsgSender.send2StageInner(harm.getAttacker().getId(), getStage().getId(), InnerCmdType.KUAFU_DIANFENG_DEATH_PK, harm.getTarget().getId());
        }
		
		//跨服群仙宴角色死亡（并且攻击者是玩家）
		if(getStage().getStageType() == StageType.KUAFUQUNXIANYAN && ElementType.isRole(harm.getAttacker().getElementType())){
			StageMsgSender.send2StageInner(harm.getAttacker().getId(), getStage().getId(), InnerCmdType.KUAFUQUNXIANYAN_DEAD_JIFEN, harm.getTarget().getId());
		}
		if(getStage().getStageType() == StageType.KUAFUQUNXIANYAN){
			//死亡角色减少房间
			KuafuQunXianYanStage kStage = (KuafuQunXianYanStage) getStage();
			KuaFuQunXianYanRank mbRank = kStage.getMyKuaFuLuanDouRank(harm.getTarget().getId());
			mbRank.setDeadCount(mbRank.getDeadCount() + 1);
		}
		if(getStage().getStageType() == StageType.XIANGONG){
			dropDuoBaoJiFen();
		}
		
		if(getStage().getStageType() == StageType.TERRITORY_WAR){
			TerritoryStage tStage = (TerritoryStage)getStage();
			if(tStage.getFlagOwnerRole() != null){
				if(tStage.getFlagOwnerRole().getId().longValue() == getId().longValue()){
					StageMsgSender.send2StageInner(getId(), getStage().getId(), InnerCmdType.TERRITORY_FLAG_OWNER_DEAD,null);
				}
			}
			if(getBusinessData().getGuildId() != null){
				IFighter attacker = harm.getAttacker();
				if(ElementType.isRoleOrPet(harm.getAttacker().getElementType())){
					IRole attackerRole = (IRole) attacker.getOwner();
					if(attackerRole.getBusinessData().getGuildId()!=null){
						StageMsgSender.send2StageInner(attackerRole.getId(), getStage().getId(), InnerCmdType.TERRITORY_ADD_BANGGONG,null);
					}
				}
			}
		}else if(getStage().getStageType() == StageType.HCZBS_WAR){
			HcZhengBaSaiStage tStage = (HcZhengBaSaiStage)getStage();
			if(tStage.getFlagOwnerRole() != null){
				if(tStage.getFlagOwnerRole().getId().longValue() == getId().longValue()){
					if(!tStage.isFlagOccupyEnd()){
						StageMsgSender.send2StageInner(getId(), getStage().getId(), InnerCmdType.HCZBS_FLAG_OWNER_DEAD,null);
					}
				}
			}
//			if(getBusinessData().getGuildId() != null){
//				IFighter attacker = harm.getAttacker();
//				if(ElementType.isRoleOrPet(harm.getAttacker().getElementType())){
//					IRole attackerRole = (IRole) attacker.getOwner();
//					if(attackerRole.getBusinessData().getGuildId()!=null){
//						StageMsgSender.send2StageInner(attackerRole.getId(), getStage().getId(), InnerCmdType.HCZBS_ADD_BANGGONG,null);
//					}
//				}
//			}
			
		}else if(getStage().getStageType() == StageType.CHAOS){
			HundunStage stage = (HundunStage)getStage();
			stage.desrJifen(this);
		}else if(getStage().getStageType() == StageType.SHENMO_FUBEN){
			ShenMoStage stage = (ShenMoStage)getStage();
			stage.beKill(this, harm.getAttacker());
		}else if(getStage().getStageType() == StageType.KUAFU_YUNGONG){
		    KuafuYunGongStage stage = (KuafuYunGongStage)getStage();
		    if(stage.isOpen()){
		        IRole ownerRole = stage.getOwnerRole();
		        if(null != ownerRole && ownerRole.getId().equals(getId())){
		            StageMsgSender.send2StageInner(getId(), getStage().getId(), InnerCmdType.I_KUAFU_YUNGONG_OWNER_UPDATE,null);
		        }
		    }
        }else if(getStage().getStageType() == StageType.MGLY_STAGE){
            StageMsgSender.send2StageInner(getId(), getStage().getId(), InnerCmdType.I_MGLY_ROLE_DEAD, null);
        }
		
	}
	
	/**
	 * 掉落夺宝积分
	 */
	private void dropDuoBaoJiFen(){
		TanBaoPublicConfig config = GongGongServiceHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_TANBAO);
		if(config != null){
			TanBaoRoleVo roleVo = TanBaoManager.getManager().getRoleVo(getId());
			if(roleVo == null){
				return;
			}
			roleVo.setDeadTime(roleVo.getDeadTime() + 1);
			if(roleVo != null && roleVo.getScore() > 0){
				float drop = roleVo.getScore() * config.getDrop();
				if(drop > config.getMaxJf()){
					drop = config.getMaxJf();
				}
				int dropTime = (int)(drop / config.getMinJf());
				if(drop > dropTime * config.getMinJf()){
					dropTime++;
				}
				roleVo.setScore(roleVo.getScore() - dropTime * config.getMinJf());
				Goods[] goods = new Goods[dropTime];
				for (int i = 0; i < goods.length; i++) {
					goods[i] = new Goods(IdFactory.getInstance().generateNonPersistentId(), ModulePropIdConstant.TANBAO_GOODS_ID, 50, GameSystemTime.getSystemMillTime());
				}
				Object[] data = new Object[]{null, null, getPosition().getX(), getPosition().getY(), goods, 0, 0, null, getId()};
				StageMsgSender.send2StageInner(getId(),getStage().getId(),InnerCmdType.DROP_GOODS, data);
				if(KuafuConfigPropUtil.isKuafuServer()){
					KuafuMsgSender.send2One(getId(), ClientCmdType.GET_TANBAO_GOLD, roleVo.getScore());
				}else{
					StageMsgSender.send2One(getId(), ClientCmdType.GET_TANBAO_GOLD, roleVo.getScore());
				}
			}
		}
	}
	
	private void notifyEnemyChange(IHarm harm){
		try {
			
			Long attackId = null;
			IFighter fighter = harm.getAttacker();
			if(ElementType.isRoleOrPet(fighter.getElementType())){
				attackId = fighter.getOwner().getId();
			}
			
			if(isNotifyAddEnemy() && attackId != null){
				Long targetId = harm.getTarget().getId();
				if(KuafuConfigPropUtil.isKuafuServer()){
					KuafuMsgSender.send2KuafuSource(targetId, InnerCmdType.ADD_ENEMY, attackId);
				}else{
					StageMsgSender.send2Bus(targetId, InnerCmdType.ADD_ENEMY, attackId);
				}
			}
			
			if(attackId != null && getStage().isAddPk() && !isWicked()){
				StageMsgSender.send2Bus(attackId, InnerCmdType.ROLE_PK, null);
			}
			try {
				if(attackId != null){
					//击杀玩家成就推送
//					StageMsgSender.send2Public(attackId, StageCommands.ADD_CHENGJIU_TUISONG, attackId);
				}
			} catch (Exception e) {
				ChuanQiLog.error("", e);
			}
		} catch (Exception e) {
			ChuanQiLog.error("is RoleEnemy error",e);
		}
	}
	
	/**
	 * 是否通知添加仇人
	 * @return
	 */
	private boolean isNotifyAddEnemy(){
		IStage stage = getStage();
		if(stage == null){
			return false;
		}
		
		return true;
	}


	@Override
	public void leaveStageHandle(IStage stage) {
		
		getHatredManager().clear();
		getBuffManager().clearAll();
		getStateManager().clear();
		
		setTeamMember(null);
		
//		PropModel propModel = getPropModel();
//		propModel.stop();
		
		//取消回血定时
		getScheduler().cancelSchedule(getId().toString(), GameConstants.COMPONENT_HP);
		
		//处理打坐的下线业务
		StageHelper.getRoleBExportService().offOnlineDaZuo(this);

		//糖宝离开地图
		Pet pet = this.getPet();
		if(pet != null && pet.getStage() != null){
			pet.getBuffManager().clearAll();
			
			stage.leave(pet);
		}
		//宠物离开地图
		Chongwu chongwu = this.getBusinessData().getChongwu();
		if(chongwu != null){
			stage.leave(chongwu);
		}
	}
	
	@Override
	public void enterStageHandle(IStage stage) {
		
		getBuffManager().startReadyForRecoveredBuffsAll();
		
		PropModel propModel = getPropModel();
		propModel.start();
		
//		//宠物进地图
//		Pet pet = this.getPet();
//		if(pet != null){
//			Point point = stage.getSurroundValidPoint(getPosition(),false, pet.getTakeupType());
//			stage.enter(pet, point.getX(),point.getY());
//			
//			pet.getAi().interruptSchedule(10, TimeUnit.MILLISECONDS);
//			pet.getBuffManager().startReadyForRecoveredBuffsAll();
//		}
		//镖车进地图
		Biaoche biaoche = this.getBiaoche();
		if(biaoche != null && biaoche.isChangeMap()){
			Point point = stage.getSurroundValidPoint(getPosition(),false, biaoche.getTakeupType());
			stage.enter(biaoche, point.getX(),point.getY());
			
			biaoche.getAi().interruptSchedule(10, TimeUnit.MILLISECONDS);
			biaoche.setChangeMap(false);
		}
		
		
		//角色进入地图后，触发进入场景后需要处理的功能
		BusMsgSender.send2Stage(this.getId(), InnerCmdType.ENTER_STAGE_LATER, null);
		
		this.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		
		startNuqiSchedule(true);
		
		checkShenqiAttackSchedule();
		
	}
	
	
	
	public void checkShenqiAttackSchedule(){
		cancelShenqiAttackSchedule();
		//如果玩家有佩戴神器，那么此时开启神器的定时器
		int shenqiId = getShenqiId();
		if(getStage() != null && getStage().isCanUseShenQi() && shenqiId != 0){
			ISkill skill = SkillManager.getManager().getSkill(getBusinessData().getShenqiSkillId());
			if(skill != null){
				startShenqiAttackSchedule(skill.getDynamicCd(getPublicCdManager()) + GameConstants.SPRING_DINGSHI_ERRER_TIME_SHORT);
			}
		}
	}


	@Override
	public PointTakeupType getTakeupType() {
		return PointTakeupType.BEING;
	}


	public PropModel getPropModel() {
		return propModel;
	}

	public void setPropModel(PropModel propModel) {
		this.propModel = propModel;
	}
	public void startTuChenSchedule(TuChenExpPublicConfig tuChenExpPublicConfig){
		IStage stage = getStage();
		if(stage == null){
			return;
		}
		//是否开启 true:开启
		if(!tuChenExpPublicConfig.isOpen()){
			return;
		}
		
		//地图验证
		if(!stage.getMapId().equals(tuChenExpPublicConfig.getMapId())){
			return;
		}
		
		//先停止上一次的定时 
		getScheduler().cancelSchedule(GameConstants.COMPONENT_TUCHEN_EXP, GameConstants.COMPONENT_TUCHEN_EXP);
		
		//定时执行业务
//		int delay = tuChenExpPublicConfig.getAddExpTime();
//		getScheduler().schedule(getId().toString(), StageConstants.COMPONENT_TUCHEN_EXP, new StageTokenRunable(getId().toString(), getStage().getId(), StageCommands.INNER_TC_EXP, new Object[]{getId(),stage.getId()}), delay, TimeUnit.MILLISECONDS);
	}
	
	public void startNuqiSchedule(boolean first){
		IStage stage = getStage();
		if(stage == null){
			return;
		}
		
		//先停止上一次的定时 
		getScheduler().cancelSchedule(getId().toString(), GameConstants.COMPONENT_TIME_NUQI);
		
		NuqiPublicConfig config = PublicConfigureHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_NUQI);
		if(config == null){
			return;
		}
		
		if(nuqi >= config.getMaxNq()){
			return;
		}
		
		if(!first){
			int cur = nuqi + config.getTimeAdd();
			if(cur > config.getMaxNq()){
				setNuqiNotice(config.getMaxNq());
			}else{
				setNuqiNotice(cur);
			}
		}
		
		//定时执行业务
		getScheduler().schedule(getId().toString(), GameConstants.COMPONENT_TIME_NUQI, new StageTokenRunable(getId(), getStage().getId(), InnerCmdType.TIME_ADD_NUQI, null), GameConstants.NUQI_ADD_DELAY, TimeUnit.MILLISECONDS);
	}
	
	public void startTerritoryAddExpSchedule(int delay){
		IStage stage = getStage();
		if(stage == null){
			return;
		}
		//停掉上次定时
		getScheduler().cancelSchedule(getId().toString(), GameConstants.COMPONENT_TERRITORY_EXP);
		
		//开启下次定时
		getScheduler().schedule(getId().toString(), GameConstants.COMPONENT_TERRITORY_EXP, new StageTokenRunable(getId(), getStage().getId(), InnerCmdType.TERRITORY_ADD_EXP_ZHENQI, new Object[]{getId(),stage.getId()}), delay, TimeUnit.SECONDS);
	}
	/**
	 * 皇城爭霸賽增加真气
	 * @param delay
	 */
	public void startHcZhengBaSaiAddExpSchedule(int delay){
		IStage stage = getStage();
		if(stage == null){
			return;
		}
		//停掉上次定时
		getScheduler().cancelSchedule(getId().toString(), GameConstants.COMPONENT_HCZBS_EXP);
		
		//开启下次定时
		getScheduler().schedule(getId().toString(), GameConstants.COMPONENT_HCZBS_EXP, new StageTokenRunable(getId(), getStage().getId(), InnerCmdType.HCZBS_ADD_EXP_ZHENQI, new Object[]{getId(),stage.getId()}), delay, TimeUnit.SECONDS);
	}
	/**
	 * 皇城争霸赛取消加经验定时器
	 */
	public void cancelHcZhengBaSaiAddExpSchedule(){
		IStage stage = getStage();
		if(stage == null){
			return;
		}
		getScheduler().cancelSchedule(getId().toString(), GameConstants.COMPONENT_HCZBS_EXP);
	}
	
	
	@Override
    public void cancelKfYunGongAddExpZqSchedule() {
	    IStage stage = getStage();
        if(stage == null || !StageType.KUAFU_YUNGONG.equals(stage.getStageType())) {
            return;
        }
        getScheduler().cancelSchedule(getId().toString(), GameConstants.KUAFU_YUNGONG_ADD_EXP_ZQ_PRODUCE);
    }
	
    @Override
    public void startKfYunGongAddExpZqSchedule(int delay) {
        IStage stage = getStage();
        if(stage == null || !StageType.KUAFU_YUNGONG.equals(stage.getStageType())) {
            return;
        }
        //停掉上次定时
        getScheduler().cancelSchedule(getId().toString(), GameConstants.KUAFU_YUNGONG_ADD_EXP_ZQ_PRODUCE);
        //开启下次定时
        getScheduler().schedule(getId().toString(), GameConstants.KUAFU_YUNGONG_ADD_EXP_ZQ_PRODUCE, new StageTokenRunable(getId(), getStage().getId(), InnerCmdType.I_KUAFU_YUNGONG_ADD_EXP_ZQ, null), delay, TimeUnit.SECONDS);
    }
    
	/**
	 * 取消领地战加经验定时器
	 */
	public void cancelTerritoryAddExpSchedule(){
		IStage stage = getStage();
		if(stage == null){
			return;
		}
		getScheduler().cancelSchedule(getId().toString(), GameConstants.COMPONENT_TERRITORY_EXP);
	}
	
	public void deadTuChenExp(TuChenExpPublicConfig tuChenExpPublicConfig,Map<Long,String> map){
		IStage stage = getStage();
		if(stage == null){
			return;
		}
		if(map == null){
			return;
		}
		
		//是否开启 true:开启
		if(!tuChenExpPublicConfig.isOpen()){
			return;
		}
		
		//地图验证
		if(!stage.getMapId().equals(tuChenExpPublicConfig.getMapId())){
			return;
		}
		
		map.remove(getId());
	}
	/**
	 * 开启神器攻击定时
	 */
	private void startShenqiAttackSchedule(long time){
		StageTokenRunable runable = new StageTokenRunable(getId(), getStage().getId(), InnerCmdType.SHENQI_ATTACK, getId().toString());
		getScheduler().schedule(getId().toString(), GameConstants.COMPONENT_SHENQI_ATTACK, runable, time, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 取消神器攻击定时
	 */
	public void cancelShenqiAttackSchedule(){
		getScheduler().cancelSchedule(getId().toString(),GameConstants.COMPONENT_SHENQI_ATTACK);
	}
	
	/**
	 * 开启阵营战加经验定时
	 */
	public void startCampAddExpSchedule(long time){
		StageTokenRunable runable = new StageTokenRunable(getId(), getStage().getId(), InnerCmdType.S_CAMP_ADD_EXP, getId().toString());
		getScheduler().schedule(getId().toString(), GameConstants.COMPONENT_CAMP_ADD_EXP, runable, time * 1000, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 取消阵营战加经验定时
	 */
	public void cancelCampAddExpSchedule(){
		getScheduler().cancelSchedule(getId().toString(),GameConstants.COMPONENT_CAMP_ADD_EXP);
	}
	
	
	public void resetTuChenExp(TuChenExpPublicConfig tuChenExpPublicConfig,Map<Long,String> map){
		IStage stage = getStage();
		if(stage == null){
			return;
		}
		if(map == null){
			return;
		}
		
		//是否开启 true:开启
		if(!tuChenExpPublicConfig.isOpen()){
			return;
		}
		
		//地图验证
		if(!stage.getMapId().equals(tuChenExpPublicConfig.getMapId())){
			return;
		}
		
		startTuChenSchedule(tuChenExpPublicConfig);
	}
	
	public void startHpSchedule(int delay){
		IStage stage = getStage();
		
		if(stage != null){
//			getScheduler().schedule(getId().toString(), StageConstants.COMPONENT_HP, new StageTokenRunable(getId().toString(), getStage().getId(), StageCommands.INNER_HP_REVERT, new Object[]{getId(),stage.getId()}), delay, TimeUnit.MILLISECONDS);
		}
	}
	 
	@Override
	public Object getMoveData() {
		/**
		 0:Number(单位guid),
		 1:int(单位目标坐标x),
		 2:int(单位目标坐标y),
		 */
		
		if(moveData == null){
			moveData = new Object[3];
			moveData[0] = getId();
		}
		Point point = getPosition();
		moveData[1] = point.getX();
		moveData[2] = point.getY();
		return moveData;
	}

	@Override
	public PublicCdManager getPublicCdManager() {
		return cdManager;
	}

	public Long getDazuoTime() {
		return dazuoTime;
	}
	public void setDazuoTime(Long dazuoTime) {
		this.dazuoTime = dazuoTime;
	}
	@Override
	public void setZuoQi(ZuoQi zuoQi) {
		this.zuoQi = zuoQi;
	}

	@Override
	public ZuoQi getZuoQi() {
		return zuoQi;
	}
	
	public int getZuoQiLevel(){
		if (zuoQi == null) {
			return -1;
		}
		return zuoQi.getZuoQiLevel();
	}

	@Override
	public IRole getOwner() {
		return this;
	}

	@Override
	public boolean isChanging() {
		return changing;
	}

	@Override
	public void setChanging(boolean change) {
		changing = change;
	}

	public void setBiaoche(Biaoche biaoche) {
		this.biaoche = biaoche;
	}
	public Biaoche getBiaoche() {
		return biaoche;
	}

	@Override
	public Object[] getOtherData(Object[] equips) {
		if(otherData == null){
			otherData = new Object[]{1,null};
		}
		DependentDataConvertUtil.putOtherRoleInfo(this, otherData,equips);
		return otherData;
	}
	
	public void startCollect(Long collectId,long finishTime){
		this.collectId = collectId;
		this.collectFinishTime = finishTime;
	}
	
	/**
	 * 获取当前采集id（如果未完成采集则返回null)
	 * @return
	 */
	public Long getCollectId(){
		if(GameSystemTime.getSystemMillTime() < collectFinishTime - GameConstants.CJ_RONGCUO_TIME){
			return null;
		}
		return collectId;
	}
	
	/**
	 * 清除采集状态
	 */
	public void clearCollect(){
		collectId = null;
		collectFinishTime = 0;
	}

	public int getNuqi() {
		return nuqi;
	}

	/**
	 *	不带通知的设值
	 */
	public void setNuqi(int nuqi) {
		this.nuqi = nuqi;
	}
	/**
	 * 带通知的设值
	 * @param nuqi
	 */
	public void setNuqiNotice(int nuqi) {
		this.nuqi = nuqi;
		BusMsgSender.send2One(getId(), ClientCmdType.NUQI_CHANGE, nuqi);
	}

	public void setCdManager(PublicCdManager cdManager) {
		this.cdManager = cdManager;
	}
	
	public void nuqiSkill(String skillId){
		IBuff buff = buffManager.getBuff(skillId, GameConstants.NUQI_BUFF_GUID);
		if(buff == null){
			return;
		}
		buff.setLayer(buff.getLayer() + 1);
		ISkill skill = getSkill(skillId);
		if(skill == null){
			return;
		}
		Collection<IFighter> targets = getStage().getSurroundElements(getPosition(), ElementType.MONSTER, new IElementSearchFilter() {
			
			@Override
			public boolean isEnough() {
				return false;
			}
			
			@Override
			public boolean check(IStageElement target) {
				Monster monster = (Monster)target;
				//死亡状态不被选中
				if(monster.getStateManager().isDead()){
					return false;
				}
				if(monster.getCamp() == 2){
					return false;
				}
				return monster.getRank() < 1;
			}
		});
		IMsgWriter msgWriter = BufferedMsgWriter.getInstance();
		Object[] inMsgData = new Object[]{skillId,null,new Object[]{getPosition().getX(),getPosition().getY()},getId()};
		if(targets != null && targets.size() > 0){
			List<Object[]> harmMsgs = SkillProcessHelper.fight(this, skill, targets, msgWriter);
			if(null != harmMsgs && harmMsgs.size() > 0){
				inMsgData[1] = harmMsgs.toArray();
			}else{
				if(targets != null && targets.size() > 0){
					Object[] damages = new Object[targets.size()];
					
					int index = 0;
					for (IFighter fighter : targets) {
						damages[index++] = new Object[]{fighter.getId()};
					}
					
					inMsgData[1] = damages;
				}
			}
			
		}else{
			inMsgData[1] = null;
		}
		StageMsgSender.send2Many(getStage().getSurroundRoleIds(getPosition()), ClientCmdType.SKILL_FIRE, inMsgData);
		//消息刷出
		getFightStatistic().flushChanges(msgWriter);
		msgWriter.flush();
		NuqiPublicConfig publicConfig = GongGongServiceHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_NUQI);
		if(buff.getLayer() >= publicConfig.getBoshu()){
			return;//技能结束
		}
		//定时
		StageTokenRunable runable = new StageTokenRunable(getId(), getStage().getId(), InnerCmdType.NUQI_SKILL_SCHEDULE, skillId);
		getScheduler().schedule(skillId, GameConstants.COMPONENT_NUQI_SCHEDULE, runable, publicConfig.getJiange(), TimeUnit.SECONDS);
	}
	
	public void stopNuqiSkill(String skillId){
		getScheduler().cancelSchedule(skillId, GameConstants.COMPONENT_NUQI_SCHEDULE);
	}

	@Override
	public boolean isWicked() {
		return isRedRole() || getBusinessData().getHuiminTime() > 0;
	}
	public void startBackFuHuoSchedule(){
		IStage stage = getStage(); 
		if(stage != null && stage.getBackFuHuoCmd() != null){
			Short cmd = null;
			if(stage.getStageType().equals(StageType.CAMP)){
				//阵营战复活复活模式处理
				cmd = InnerCmdType.S_CAMP_FUHUO;
			}else if(stage.getStageType().equals(StageType.TERRITORY_WAR)){
				cmd = InnerCmdType.S_TERRITORY_FUHUO;
			}else if(stage.getStageType().equals(StageType.HCZBS_WAR)){
				cmd = InnerCmdType.S_HCZBS_FUHUO;
			}else {
				cmd = stage.getBackFuHuoCmd();
			}
			
			getScheduler().schedule(getId().toString(), stage.getBackFuhuoConstants(), new StageTokenRunable(getId(), getStage().getId(), cmd, null), stage.getQzFuhuoSecond(), TimeUnit.SECONDS);
		}
	}
	
	public void tanBaoExpSchedule(int delay){
		StageTokenRunable runnable = new StageTokenRunable(getId(), getStage().getId(), InnerCmdType.DINGSHI_TANBAO_EXP_ADD, null);
		getScheduler().schedule(getId().toString(), GameConstants.COMPONENET_DINGSHI_TANBAO, runnable, delay, TimeUnit.MILLISECONDS);
	}
	
	public void cancelBackFuhuoSchedule(){
		IStage stage = getStage(); 
		if(stage != null){
			getScheduler().cancelSchedule(getId().toString(), stage.getBackFuhuoConstants());
		}
	}
	/**
	 * 开启温泉加经验定时器
	 * @param delay
	 */
	public void startWenquanAddExpZhenqiSchedule(){
		StageTokenRunable runable = new StageTokenRunable(getId(), getStage().getId(), InnerCmdType.WENQUAN_ADD_EXP, getId().toString());
		getScheduler().schedule(getId().toString(), GameConstants.COMPONENT_WENQUAN_ADD_EXP, runable, GameConstants.DAZUO_AWARD_CD, TimeUnit.SECONDS);
	}
	
	public void startChenghaoExpireSchedule(Integer chenghaoId,Long delay){
		BusTokenRunable runable = new BusTokenRunable(getId(), InnerCmdType.INNER_CHENGHAO_EXPIRE, chenghaoId);
		getScheduler().schedule(getId().toString(), GameConstants.COMPONENT_CHENGHAO_EXPIRE + chenghaoId, runable, delay, TimeUnit.MILLISECONDS);
	}
	
	/**
     * 开启魔宫猎焰加经验定时器
     * @param delay
     */
	@Override
    public void startMglyAddExpZhenqiSchedule(int delay){
	    cancelMglyAddExpZhenqiSchedule();
        StageTokenRunable runable = new StageTokenRunable(getId(), getStage().getId(), InnerCmdType.I_MGLY_ADD_EXP_ZHENQI, null);
        getScheduler().schedule(getId().toString(), GameConstants.COMPONENT_MGLY_ADD_EXP_ZHENQI, runable, delay, TimeUnit.SECONDS);
    }
    
	/**
	 * 取消魔宫猎焰加经验定时器
	 */
	@Override
    public void cancelMglyAddExpZhenqiSchedule(){
        getScheduler().cancelSchedule(getId().toString(), GameConstants.COMPONENT_MGLY_ADD_EXP_ZHENQI);
    }
	
	/**
	 * 开启魔宫猎焰减少御魔值定时器
	 */
	@Override
    public void startMglyCutYumoValSchedule(int delay) {
	    StageTokenRunable runable = new StageTokenRunable(getId(), getStage().getId(), InnerCmdType.I_MGLY_CUT_YUMO_VAL, null);
        getScheduler().schedule(getId().toString(), GameConstants.COMPONENT_MGLY_CUT_YUMOVAL, runable, delay, TimeUnit.SECONDS);
    }
	
	/**
	 * 取消魔宫猎焰减少御魔值定时器
	 */
    @Override
    public void cancelMglyCutYumoValSchedule() {
        getScheduler().cancelSchedule(getId().toString(), GameConstants.COMPONENT_MGLY_CUT_YUMOVAL);
    }
    
    /**
     * 开启魔宫猎焰延迟退出场景定时器
     */
    @Override
    public void startMglyDelayExitStageSchedule(int delay) {
        cancelMglyDelayExitStageSchedule();
        StageTokenRunable runable = new StageTokenRunable(getId(), getStage().getId(), InnerCmdType.I_MGLY_EXIT_STAGE, null);
        getScheduler().schedule(getId().toString(), GameConstants.COMPONENT_MGLY_DELAY_KICK_OUT, runable, delay, TimeUnit.SECONDS);
    }
    
    /**
     * 取消魔宫猎焰延迟退出场景定时器
     */
    @Override
    public void cancelMglyDelayExitStageSchedule() {
        getScheduler().cancelSchedule(getId().toString(), GameConstants.COMPONENT_MGLY_DELAY_KICK_OUT);
    }
    
	/**
	 * 开启离开定时
	 */
	public void startLeaveSchedule(Long leaveTime){
		if(leaveTime == null || getStage() == null){
			ChuanQiLog.error("踢人定时异常，userRoleId：{},leaveTime：{},stage null:{}",getId(),leaveTime,getStage() == null);
			return;
		}
		Long delay = (leaveTime - GameSystemTime.getSystemMillTime()) / 1000;
		if(delay < 1){
			StageMsgSender.send2StageControl(getId(), InnerCmdType.S_APPLY_LEAVE_STAGE, null);
		}else{
			StageControlRunable stageRunable = new StageControlRunable(getId(), InnerCmdType.S_APPLY_LEAVE_STAGE, null);
			getScheduler().schedule(stageRunable, delay.intValue(), TimeUnit.SECONDS);
		}
	}

	@Override
	public Integer getShiZhuang() {
		return shizhuang;
	}

	@Override
	public void setShiZhuang(Integer shizhuangId) {
		this.shizhuang = shizhuangId;
	}
	public String getPeiou() {
		return peiou;
	}
	public void setPeiou(String peiou) {
		this.peiou = peiou;
	}
	public Integer getXinwu() {
		return xinwu;
	}
	public void setXinwu(Integer xinwu) {
		this.xinwu = xinwu;
	}
	public Set<String> getNoticeSkills() {
		return noticeSkills;
	}
	public void setNoticeSkills(Set<String> noticeSkills) {
		this.noticeSkills = noticeSkills;
	}
	public void addSkillShulian(RoleSkill roleSkill){
		skillShulians.put(roleSkill.getSkillId(), roleSkill);
	}
	public void addShulian(String skillId){
		RoleSkill roleSkill = skillShulians.get(skillId);
		if(roleSkill != null && roleSkill.getShulian() < roleSkill.getMaxShulian()){
			roleSkill.addShulian(1);
			if(roleSkill.getShulian() >= roleSkill.getMaxShulian()){
				StageMsgSender.send2One(getId(), ClientCmdType.SKILL_SHULIAN_MAX, skillId);
			}
		}
	}
	public List<RoleSkill> getRoleSkills(){
		return new ArrayList<>(skillShulians.values());
	}
	@Override
	public void setWuQi(WuQi wuQi) {
		this.wuQi = wuQi;
		
	}
	@Override
	public WuQi getWuQi() {
		return this.wuQi;
	}
	
	public int getWuQiLevel(){
		if (wuQi == null) {
			return -1;
		}
		return wuQi.getWuqiLevel();
	}
	public Object getWuQiShowId() {
		if (wuQi == null) {
			return -1;
		}
		return wuQi.getShowId();
	}
	
//	private BattleMode oldBattleMode;
//	public void setOldBattleMode(BattleMode oldBattleMode){
//		this.oldBattleMode = oldBattleMode;
//	}
	
//	@Override
//	public void resetBattleMode() {
//		if(oldBattleMode != null){
//			this.setBattleMode(oldBattleMode);
//			StageMsgSender.send2One(this.getId(), ClientCmdType.BATTLE_MODE, oldBattleMode.getVal());
//			this.oldBattleMode = null;
//		}
//	}
	
//	@Override 
//	public BattleMode getOldBattleMode(){
//		return this.oldBattleMode;
//	}
}
