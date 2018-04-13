package com.junyou.stage.model.element.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.bus.chibang.manage.ChiBang;
import com.junyou.bus.qiling.manage.QiLing;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tianyu.manage.TianYu;
import com.junyou.bus.xianjian.manage.XianJian;
import com.junyou.bus.zhanjia.manage.ZhanJia;
import com.junyou.stage.entity.TeamMember;
import com.junyou.stage.model.element.chongwu.Chongwu;
import com.junyou.stage.model.element.role.business.RoleEquipData;


/**
 * @description 业务关联数据
 * @author ShiJie Chi
 * @date 2012-4-9 下午3:07:49 
 */
public class BusinessData{
	
	

	public BusinessData() {
		// TODO Auto-generated constructor stub
	}
	
	
	public BusinessData(RoleWrapper loginRole) {
		this.loginRole = loginRole;
		this.level = loginRole.getLevel();
	}
	
	public BusinessData(BusinessData businessData) {
		this.loginRole = businessData.loginRole;
		this.level = loginRole.getLevel();
	}
	
	private int level;
	private RoleWrapper loginRole;
	
	//装备
	private RoleEquipData equipData;
	
	private Integer zuoJi = 0;//坐骑等级
	
	private Integer zuoRideLevel = 0;//当前骑的坐骑等级，没骑传0
	
	private Long miaoFaBaoTime;
	private int miaoFaBaoCount;
	
	private TeamMember member;
	
	private Long guildId;
	private String guildName;
	
	private Integer curFightValue = 0;//主角当前战斗力
	private Integer maxFightValue = 0;//主角最大战斗力（曾经达到过的最大值 ）
	
	private int vipLevel = 0;
	private int zhuanShengLevel;
	
	private int gzLevel;
	private int gyLevel;
	
	private int pkValue;
	private int redPkValue;
	
	private long huiminTime;
	
	private int dazuoExp;//打坐经验加成百分比
	
	//武魂配置ID
	private int wuhunLevel = 0;
	
	//关心自己蓝的 所有 角色列表
	private List<Long> attentionRoleIds;
	
	//自己关心角色id
	private Long selfAttentionRoleId;
	
	private int qsqhId;

	private boolean isSkillTeleport = false;
	
	private boolean isBooth = false;
	
	private String titleRes;//称号的动画res
	
	private int yellowLevel;//黄钻等级
	private int nianYellowVip;//年费黄钻动态
	
	private Object[] zlDataArr;//(真龙配置id,当前真龙战斗力)
	private Object[] knDataArr;//(暗器阶数,暗器星数,当前暗器战斗力)
	
	private int kfJf = 1;//跨服积分
	
	private long dazuoTargetRoleId;// 双休打坐对象(如果是单休这个值为自己roleId，如果是双休这个值是别人roleId，如果没有进入打坐为0)
	 
	private int tiaoShan;//跳起来被击中了消耗一点
	
	private boolean isJump;//是否在跳跃状态中
	 
	private ChiBang chibang;//翅膀
	private QiLing qiling;//器灵
	private TianYu tianyu;//天羽
	
	private XianJian xianjian;//仙剑
	private ZhanJia zhanjia;//战甲
	
	private int tangbaoXinwenJie; //糖宝心纹的阶数
	private int petState;//0未开启或已激活，1激活中
	
	private Integer shenqiId;//神器Id
	
	private Long shenqiTargetId;//神器Id
	
	private String shenqiSkillId;//神器SkillId
	
	private String shenqiSkillId2;//神器SkillId2
	
	private Map<Integer,String> chenghaoMap;//佩戴的称号
	
	private Map<Integer,Object> platformInfo ;//平台数据
	
    private Integer wuxingType;// 附体的五行type
    private Integer wuxingId;
    private List<Integer> wuxingList;

    private boolean isHcZbsWinnerGuildLeader = false;// 是否是皇城战胜利帮派门主

    public boolean isHcZbsWinnerGuildLeader() {
        return isHcZbsWinnerGuildLeader;
    }

    public void setHcZbsWinnerGuildLeader(boolean isHcZbsWinnerGuildLeader) {
        this.isHcZbsWinnerGuildLeader = isHcZbsWinnerGuildLeader;
    }

    private boolean isKfYunGongWinnerGuildLeader = false;// 是否是跨服云宫之巅胜利帮派门主

    public boolean isKfYunGongWinnerGuildLeader() {
        return isKfYunGongWinnerGuildLeader;
    }

    public void setKfYunGongWinnerGuildLeader(boolean isKfYunGongWinnerGuildLeader) {
        this.isKfYunGongWinnerGuildLeader = isKfYunGongWinnerGuildLeader;
    }

	private Chongwu chongwu;
	

	public Integer getWuxingId() {
		return wuxingId;
	}


	public void setWuxingId(Integer wuxingId) {
		this.wuxingId = wuxingId;
	}


	public int getTangbaoXinwenJie() {
		return tangbaoXinwenJie;
	}
	public void setTangbaoXinwenJie(int tangbaoXinwenJie) {
		this.tangbaoXinwenJie = tangbaoXinwenJie;
	}
	public int getChongwuId(){
		if(chongwu == null){
			return 0;
		}
		return chongwu.getConfigId();
	}
	public Chongwu getChongwu() {
		return chongwu;
	}


	public void setChongwu(Chongwu chongwu) {
		this.chongwu = chongwu;
	}


	public Map<Integer, Object> getPlatformInfo() {
		return platformInfo;
	}
	public void setPlatformInfo(Map<Integer, Object> platformInfo) {
		this.platformInfo = platformInfo;
	}

	public Map<Integer,String> getChenghaoMap() {
		return chenghaoMap;
	}


	public List<Integer> getWuxingList() {
		return wuxingList;
	}


	public void setWuxingList(List<Integer> wuxingList) {
		this.wuxingList = wuxingList;
	}

	public void setChenghaoMap(Map<Integer,String> chenghaoMap) {
		this.chenghaoMap = chenghaoMap;
	}

	public boolean addChenghao(Integer chenghaoId,String res){
		if(chenghaoMap== null){
			chenghaoMap = new HashMap<Integer,String>();
		}
		if(chenghaoMap.containsKey(chenghaoId)){
			return false;
		}
		chenghaoMap.put(chenghaoId, res);
		return true;
	}
	public boolean removeChenghao(Integer chenghaoId){
		if(chenghaoMap == null){
			return false;
		}
		return chenghaoMap.remove(chenghaoId) != null;
	}
	
	public Object[] getChenghao(){
		if(chenghaoMap == null || chenghaoMap.size() == 0){
			return null;
		}
		Object [] ret = new Object[chenghaoMap.size()];
		int index =0;
		for(Integer e:chenghaoMap.keySet()){
			Object[] obj = new Object[]{e,chenghaoMap.get(e)};
			ret[index] = obj;
			index ++;
		}
		return ret;
	}
	
	private int lingHuo;//灵火
	
	private int cjValue;//成就点数
	
	public int getCjValue() {
		return cjValue;
	}


	public void setCjValue(int cjValue) {
		this.cjValue = cjValue;
	}


	public int getLingHuo() {
		return lingHuo;
	}


	public void setLingHuo(int lingHuo) {
		this.lingHuo = lingHuo;
	}


	/**
	 * 是否在跳跃状态中
	 * @return
	 */
	public boolean isJump() {
		return isJump;
	}


	public void setJump(boolean isJump) {
		this.isJump = isJump;
	}


	public int getTiaoShan() {
		return tiaoShan;
	}


	public void setTiaoShan(int tiaoShan) {
		this.tiaoShan = tiaoShan;
	}


	public long getDazuoTargetRoleId() {
		return dazuoTargetRoleId;
	}

	public void setDazuoTargetRoleId(long dazuoTargetRoleId) {
		this.dazuoTargetRoleId = dazuoTargetRoleId;
	}
	
	
	public int getKfJf() {
		return kfJf;
	}


	public void setKfJf(int kfJf) {
		this.kfJf = kfJf;
	}


	public Object[] getZlDataArr() {
		return zlDataArr;
	}

	public void setZlDataArr(Object[] zlDataArr) {
		this.zlDataArr = zlDataArr;
	}

	public Object[] getKnDataArr() {
		return knDataArr;
	}

	public void setKnDataArr(Object[] knDataArr) {
		this.knDataArr = knDataArr;
	}
	
	public Integer getKnivesId(){
		if(knDataArr != null && knDataArr.length > 0){
			
			Integer knviesId = (Integer) knDataArr[0];
			return knviesId == null ? null : knviesId;
		}
		return null;
	}

	/**
	 * 是否是摆摊状态
	 * @return
	 */
	public boolean isBooth() {
		return isBooth;
	}


	public void changeBooth(){
		isBooth = true;
	}
	
	public void changeNoBooth(){
		isBooth = false;
	}
	/**
	 * 获取自己关心的角色
	 * @return
	 */
	public Long getSelfAttentionRoleId() {
		return selfAttentionRoleId;
	}

	/**
	 * 设置自己关心的角色
	 * @param selfAttentionRoleId
	 */
	public void setSelfAttentionRoleId(Long selfAttentionRoleId) {
		this.selfAttentionRoleId = selfAttentionRoleId;
	}


	/**
	 * 获取监听自己的 roleIds
	 * @return
	 */
	public Long[] getAttentionRoleIds() {
		if(attentionRoleIds == null || attentionRoleIds.size() == 0){
			return null;
		}else{
			return attentionRoleIds.toArray(new Long[attentionRoleIds.size()]);
		}
	}


	/**
	 * 增加一个监听自己的roleId
	 * @param roleId
	 */
	public void addAttentionRoleId(Long roleId) {
		if(this.attentionRoleIds == null){
			this.attentionRoleIds = new ArrayList<Long>();
		}

		if(!this.attentionRoleIds.contains(roleId)){
			this.attentionRoleIds.add(roleId);
		}
	}
	
	/**
	 * 清空自己的监听列表
	 */
	public void removeAttentionRoleId(Long roleId){
		if(this.attentionRoleIds != null){
			attentionRoleIds.remove(roleId);
		}
	}
	

	public String getUserId(){
		return loginRole.getUserId();
	}
	
	public String getServerId(){
		return loginRole.getServerId();
	}
	
	/**
	 * 当前骑的坐骑等级，没骑传0
	 * @return
	 */
	public Integer getZuoRideLevel() {
		return zuoRideLevel;
	}


	public void setZuoRideLevel(Integer zuoRideLevel) {
		this.zuoRideLevel = zuoRideLevel;
	}

	/**
	 * 获取武魂等级
	 * @return
	 */
	public int getWuhunLevel() {
		return wuhunLevel;
	}


	public void setWuhunLevel(int wuhunLevel) {
		this.wuhunLevel = wuhunLevel;
	}

	/**
	 * 角色配置ID
	 * @return
	 */
	public Integer getRoleConfigId() {
		return loginRole.getConfigId();
	}

	
	public int getGzLevel() {
		return gzLevel;
	}


	public void setGzLevel(int gzLevel) {
		this.gzLevel = gzLevel;
	}

	

	public int getGyLevel() {
		return gyLevel;
	}


	public void setGyLevel(int gyLevel) {
		this.gyLevel = gyLevel;
	}


	public int getPkValue() {
		return pkValue;
	}


	public void setPkValue(int pkValue) {
		this.pkValue = pkValue;
	}
	
	


	public int getRedPkValue() {
		return redPkValue;
	}


	public void setRedPkValue(int redPkValue) {
		this.redPkValue = redPkValue;
	}


	public long getHuiminTime() {
		return huiminTime;
	}
	

	public void setHuiminTime(long huiminTime) {
		this.huiminTime = huiminTime;
	}


	public int getVipLevel() {
		return vipLevel;
	}


	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}


	public Long getRoleId() {
		return loginRole.getId();
	}
	public String getName() {
		return loginRole.getName();
	}
	
	public void changeRoleName(String changeName){
		//TODO 角色使用改名卡
//		loginRole.setName(changeName);
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public long getExp(){
		return loginRole.getExp();
	}
	

	public int getZhuanShengLevel() {
		return zhuanShengLevel;
	}


	public void setZhuanShengLevel(int zhuanShengLevel) {
		this.zhuanShengLevel = zhuanShengLevel;
	}


	public RoleEquipData getEquipData() {
		return equipData;
	}
	public void setEquipData(RoleEquipData equipData) {
		this.equipData = equipData;
	}

	public void setZuoJi(Integer zuoJiData) {
		this.zuoJi = zuoJiData;
	}
	public Object getZuoJi() {
		return zuoJi;
	}
	
	public void setMiaoFaBaoTime(Long miaoFaBaoTime) {
		this.miaoFaBaoTime = miaoFaBaoTime;
	}
	public void setMiaoFaBaoCount(int miaoFaBaoCount) {
		this.miaoFaBaoCount = miaoFaBaoCount;
	}
	public Long getMiaoFaBaoTime() {
		return miaoFaBaoTime;
	}
	public int getMiaoFaBaoCount() {
		return miaoFaBaoCount;
	}
	
	
	public void setTeamMember(TeamMember member) {
		this.member = member;
	}
	public TeamMember getTeamMember() {
		return member;
	}
	
	public Long getGuildId() {
		return guildId;
	}
	public void setGuildId(Long guildId) {
		this.guildId = guildId;
	}
	public String getGuildName() {
		return guildName;
	}
	public void setGuildName(String guildName) {
		this.guildName = guildName;
	}



	public Integer getCurFightValue() {
		return curFightValue;
	}


	public void setCurFightValue(Integer curFightValue) {
		this.curFightValue = curFightValue;
		
		if(curFightValue > maxFightValue){
			setMaxFightValue(curFightValue);
		}
	}


	public Integer getMaxFightValue() {
		return maxFightValue;
	}


	public void setMaxFightValue(Integer maxFightValue) {
		this.maxFightValue = maxFightValue;
	}


	/**
	 * 是否可以瞬移
	 * @return true:可以
	 */
	public boolean isSkillTeleport() {
		return isSkillTeleport;
	}


	public void setSkillTeleport(boolean isSkillTeleport) {
		this.isSkillTeleport = isSkillTeleport;
	}


	public int getQsqhId() {
		return qsqhId;
	}


	public void setQsqhId(int qsqhId) {
		this.qsqhId = qsqhId;
	}

	public String getTitleRes() {
		return titleRes;
	}
	public void setTitleRes(String titleRes) {
		this.titleRes = titleRes;
	}
	
	public int getYellowLevel() {
		return yellowLevel;
	}


	public void setYellowLevel(int yellowLevel) {
		this.yellowLevel = yellowLevel;
	}
	public int getNianYellowVip() {
		return nianYellowVip;
	}

	public void setNianYellowVip(int nianYellowVip) {
		this.nianYellowVip = nianYellowVip;
	}
	
	public Integer getTeamId(){
		if(getTeamMember() == null){
			return null;
		} 
		return this.getTeamMember().getTeamId();
	}

	public ChiBang getChibang() {
		return chibang;
	}

	public void setChibang(ChiBang chibang) {
		this.chibang = chibang;
	}
	
	public QiLing getQiling() {
		return qiling;
	}
	public void setQiling(QiLing qiling) {
		this.qiling = qiling;
	}
	
	public TianYu getTianyu() {
		return tianyu;
	}

	public void setTianyu(TianYu tianyu) {
		this.tianyu = tianyu;
	}


	public XianJian getXianjian() {
		return xianjian;
	}


	public void setXianjian(XianJian xianjian) {
		this.xianjian = xianjian;
	}
	public ZhanJia getZhanjia() {
		return zhanjia;
	}


	public void setZhanjia(ZhanJia zhanjia) {
		this.zhanjia = zhanjia;
	}

	public Long getZhenqi(){
		return loginRole.getZhenqi();
	}

	public int getDazuoExp() {
		return dazuoExp;
	}

	public void setDazuoExp(int dazuoExp) {
		this.dazuoExp = dazuoExp + 100;
	}


	public Integer getShenqiId() {
		return shenqiId;
	}
	

	public Integer getWuxingType() {
		return wuxingType;
	}


	public void setWuxingType(Integer wuxingType) {
		this.wuxingType = wuxingType;
	}


	public void setShenqiId(Integer shenqiId) {
		this.shenqiId = shenqiId;
	}
	
	public String getShenqiSkillId() {
		return shenqiSkillId;
	}

	public void setShenqiSkillId(String shenqiSkillId) {
		this.shenqiSkillId = shenqiSkillId;
	}

	public String getShenqiSkillId2() {
		return shenqiSkillId2;
	}


	public void setShenqiSkillId2(String shenqiSkillId2) {
		this.shenqiSkillId2 = shenqiSkillId2;
	}


	public Long getShenqiTargetId() {
		return shenqiTargetId;
	}


	public void setShenqiTargetId(Long shenqiTargetId) {
		this.shenqiTargetId = shenqiTargetId;
	}


	public void initDazuoExp(int dazuoExp) {
		this.dazuoExp = dazuoExp;
	}

	public int getPetState() {
		return petState;
	}

	public void setPetState(int petState) {
		this.petState = petState;
	}
	
	/**
	 * 转职
	 * @param jobConfigId
	 */
	public void setRoleConfigId(int jobConfigId){
		this.loginRole.setConfigId(jobConfigId);
	}
	
}