package com.junyou.gameconfig.goods.configure.export;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.junyou.bus.equip.configure.export.TiPinBiaoConfig;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.constants.EquipTypeSlot;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 * 
 * @description 物品
 *
 * @author LiuJuan
 * @created 2011-12-15 下午05:38:50
 */
public class GoodsConfig{

	//公用\材料
	private String id;
	private String name;
	private int firstType; //一级分类   GoodsCategory 中定义的一级分类
	private int category;//所属的类型
	private int rareLevel;//物品的稀有等级
	private int maxStack;//最大叠放数量
	private int recycle;//回收价格
	private int bangding;//是否为绑定
	
	private String des;//物品描述
	
	private boolean isBind;
	private boolean isNotify;//获取是否广播
	private int job;//使用物品的职业需求
	private int levelReq;//使用该物品的级别需求
	private int cuihui;//物品摧毁字段，1=不可摧毁
	private int needzhuansheng;//装备需要的转生等级
	
	private int xianZhiId;//每日使用次数限制ID： 不填：无限制
	private int cuilian; // 0或不填：不可淬炼  >0的整数：可淬炼
	
	private int daily;//是否打印掉落日志：1打印，0或不填不打印
	
	
	//道具
	private String id1;//物品内在标识，用于区分两个物品是否为同一物品
	private int durationDay;//过期间隔天数
	private int data1;//根据所属的类型(category)不同意思
	private Float data2;
	private String data3;
	private String data4;
	private int order;//道具排序规则
	
	//装备
	private int eqpart;//装备对应的部位
	private int sexReq;//性别需要
	private int guanzhiReq;//官职需要
	private boolean isJianding;//是否鉴定
	private int rlz;//装备回收时获得熔炉值
	
	private int maxDurability;//最大耐久度
	private int maxIntensify;//最大强化等级
	private List<Integer> activates;//强化激活属性表配置(是激活配置表唯一标识)
	private String nextid;//提升装备等级后的装备ID
	private Map<String,Integer> needItem;//提升装备等级时需要的物品
	//private String needItem;//提升装备等级时需要的物品
	private float odds;//提升装备等级时的成功率
	private Map<String, String> qianghuaSX;//强化加的属性
	private int jobshow;//装备职业倾向
	private int fenzu;//装备分组
	private String equipId;//装备后的id
	private String tipinId;//提品后的id
	
	
	
	private boolean record;//是否不记录该道具的唯一标识的操作日志    0或不填（记录）1（不记录）     
	
	
	private String cd;//物品使用cd
	
	
	private int itemLevel;//物品级别
	private boolean ownonly;
	private boolean obtainLocked;//拾取绑定
	private boolean splocked;//特殊绑定
	private boolean whetherCost;//是否可出售
	private int duration;//物品有效期
	private String monsterID;//卡片相关联的怪物ID

	//道具
	private String skill;//物品的功能执行类型
	private int biao;//物品的功能表
	private boolean useAble;//是否可直接使用
	private int collectType;//拾取类型
	private String summonId;//召唤出的怪物ID
	private int shuliandu;//技能书使用后增加的熟练度
	private int shenji;//技能书使用后学会的技能
	private int openGold;//打开收费礼盒需要花费的元宝
	private int guildLevelUp;//直接提升公会所到的等级
	
	private int serverUse;//服务器在获取物品后是否自动使用   1自动使用  0不使用
	private int useUser;//使用人物限制  0  都不可使用  1  主角可使用 2  伙伴可使用  3  主角 伙伴都可以使用
	
	
	//装备
	private boolean equipLocked;//装备绑定
	private boolean whetherRepair;//是否可修理
	private float repair;//修理价格基数
	private int maxSockets;//最大孔数
	private int suit;//所属套装
	
//	private Map<String, Float> attribute = new HashMap<String, Float>();
	
	//排序号
	private int sortNum;
	
	//宝石类型
	private String gemtype;
	 
	
	//功能11-宝石镶嵌所需要的最小装备等级
	private int equneed;
	//功能11-宝石增加的战斗属性
	private String proadd;
	//功能11-宝石增加的战斗属性的数值
	private float provalue;
	//宝石镶嵌后给普通攻击附加的效果id
	private String effectid;
	//宝石技能效果触发的几率
	private float effectodds;
	
	
	//装备基础属性
	private Map<String,Long> equipBaseAttr;
	//装备效果属性
	private Map<String,Long> equipEffectAttr;
	
	//装备强化增加的属性值
	private Map<String,Long> equipQianHuaAddAttr;
	/**
	 * 是否为神武装备
	 * @return                      
	 * @Description:
	 */
	public boolean swEquip(){
	    return EquipTypeSlot.isSwEquip(this.eqpart);
//	    return this.needzhuansheng > 0;
	}
	/*是否为暗金装备*/
    public boolean anjinEquip() {
        return this.rareLevel == 6;
    }
	
	public int getCuilian() {
		return cuilian;
	}
	public void setCuilian(int cuilian) {
		this.cuilian = cuilian;
	}
	public boolean isNotify() {
		return isNotify;
	}
	public void setNotify(boolean isNotify) {
		this.isNotify = isNotify;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	/**
	 * 获取道具限制ID
	 * @return
	 */
	public int getXianZhiId() {
		return xianZhiId;
	}
	public void setXianZhiId(int xianZhiId) {
		this.xianZhiId = xianZhiId;
	}
	public boolean isBind() {
		return isBind;
	}
	public void setBind(boolean isBind) {
		this.isBind = isBind;
	}
	/**
	 * 服务器在获取物品后是否自动使用   1自动使用  0不使用
	 * @return
	 */
	public int getServerUse() {
		return serverUse;
	}
	public void setServerUse(int serverUse) {
		this.serverUse = serverUse;
	}
	
	/**
	 * 使用人物限制  0  都不可使用  1  主角可使用 2  伙伴可使用  3  主角 伙伴都可以使用
	 * @return
	 */
	public int getUseUser() {
		return useUser;
	}
	public void setUseUser(int useUser) {
		this.useUser = useUser;
	}
	/**
	 * 物品名
	 * @return
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 物品级别
	 * @return
	 */
	public int getItemLevel() {
		return itemLevel;
	}
	public void setItemLevel(int itemLevel) {
		this.itemLevel = itemLevel;
	}
	
	
	/**
	 * 物品的功能表<br/>
			0:不对应其他表（不知道的填0)<br/>
			1.辅助物品(头像下功能)FuZhuGongNeng.xlsx<br/>
			2.胭脂水粉(府邸功能中)<br/>
	 * @return
	 */
	public int getBiao() {
		return biao;
	}
	public void setBiao(int biao) {
		this.biao = biao;
	}
	
	
	/**
	 * 消耗品所属的类型
	 * @return
	 */
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	/**
	 * 使用该物品的级别需求
	 * @return
	 */
	public int getLevelReq() {
		return levelReq;
	}
	public void setLevelReq(int levelReq) {
		this.levelReq = levelReq;
	}
	/**
	 * 物品的稀有等级
	 * @return
	 */
	public int getRareLevel() {
		return rareLevel;
	}
	public void setRareLevel(int rareLevel) {
		this.rareLevel = rareLevel;
	}
	/**
	 * 最大叠放数量
	 * @return
	 */
	public int getMaxStack() {
		return maxStack;
	}
	public void setMaxStack(int maxStack) {
		this.maxStack = maxStack;
	}
	/**
	 * 拥有唯一
	 * @return
	 */
	public boolean isOwnonly() {
		return ownonly;
	}
	public void setOwnonly(boolean ownonly) {
		this.ownonly = ownonly;
	}
	/**
	 * 拾取绑定
	 * @return
	 */
	public boolean isObtainLocked() {
		return obtainLocked;
	}
	public void setObtainLocked(boolean obtainLocked) {
		this.obtainLocked = obtainLocked;
	}
	/**
	 * 是否可出售
	 * @return
	 */
	public boolean isWhetherCost() {
		return whetherCost;
	}
	public void setWhetherCost(boolean whetherCost) {
		this.whetherCost = whetherCost;
	}
	/**
	 * 物品有效期(单位：分)
	 * @return
	 */
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	/**
	 * 物品有效期(单位:天)
	 * 0.永久
	 * >0的整数.物品过期天数
	 * @return
	 */
	public int getDurationDay() {
		return durationDay;
	}
	public void setDurationDay(int durationDay) {
		this.durationDay = durationDay;
	}
	/**
	 * 特殊绑定
	 * @return
	 */
	public boolean isSplocked() {
		return splocked;
	}
	public void setSplocked(boolean splocked) {
		this.splocked = splocked;
	}
	/**
	 * 回收价格
	 * @return
	 */
	public int getRecycle() {
		return recycle;
	}
	public void setRecycle(int recycle) {
		this.recycle = recycle;
	}
	/**
	 * 是否可直接使用
	 * @return
	 */
	public boolean isUseAble() {
		return useAble;
	}
	public void setUseAble(boolean useAble) {
		this.useAble = useAble;
	}
	/**
	 * 拾取类型
	 * @return
	 */
	public int getCollectType() {
		return collectType;
	}
	public void setCollectType(int collectType) {
		this.collectType = collectType;
	}
	/**
	 * 召唤出的怪物ID
	 * @return
	 */
	public String getSummonId() {
		return summonId;
	}
	public void setSummonId(String summonId) {
		this.summonId = summonId;
	}
	/**
	 * 卡片相关联的怪物ID
	 * @return
	 */
	public String getMonsterID() {
		return monsterID;
	}
	public void setMonsterID(String monsterID) {
		this.monsterID = monsterID;
	}
	/**
	 * 技能书使用后增加的熟练度
	 * @return
	 */
	public int getShuliandu() {
		return shuliandu;
	}
	public void setShuliandu(int shuliandu) {
		this.shuliandu = shuliandu;
	}
	/**
	 * 技能书使用后学会的技能
	 * @return
	 */
	public int getShenji() {
		return shenji;
	}
	public void setShenji(int shenji) {
		this.shenji = shenji;
	}
	/**
	 * 打开收费礼盒需要花费的元宝
	 * @return
	 */
	public int getOpenGold() {
		return openGold;
	}
	public void setOpenGold(int openGold) {
		this.openGold = openGold;
	}
	/**
	 * 直接提升公会所到的等级
	 * @return
	 */
	public int getGuildLevelUp() {
		return guildLevelUp;
	}
	public void setGuildLevelUp(int guildLevelUp) {
		this.guildLevelUp = guildLevelUp;
	}
	
	/**
	 * 获取物品排序号
	 * @return
	 */
	public int getSortNum() {
		return sortNum;
	}
	public void setSortNum(int sortNum) {
		this.sortNum = sortNum;
	}
	/**
	 * 使用物品的职业需求
	 * @return
	 */
	public int getJob() {
		return job;
	}
	public void setJob(int job) { 
		this.job = job; 
	}
	/**
	 * 性别需要
	 * @return
	 */
	public int getSexReq() {
		return sexReq;
	}
	public void setSexReq(int sexReq) {
		this.sexReq = sexReq;
	}
	/**
	 * 最大耐久度
	 * @return
	 */
	public int getMaxDurability() {
		return maxDurability;
	}
	public void setMaxDurability(int maxDurability) {
		this.maxDurability = maxDurability;
	}
	/**
	 * 装备绑定
	 * @return
	 */
	public boolean isEquipLocked() {
		return equipLocked;
	}
	public void setEquipLocked(boolean equipLocked) {
		this.equipLocked = equipLocked;
	}
	/**
	 * 是否可修理
	 * @return
	 */
	public boolean isWhetherRepair() {
		return whetherRepair;
	}
	public void setWhetherRepair(boolean whetherRepair) {
		this.whetherRepair = whetherRepair;
	}
	/**
	 * 修理价格基数
	 * @return
	 */
	public float getRepair() {
		return repair;
	}
	public void setRepair(float repair) {
		this.repair = repair;
	}
	/**
	 * 最大孔数
	 * @return
	 */
	public int getMaxSockets() {
		return maxSockets;
	}
	public void setMaxSockets(int maxSockets) {
		this.maxSockets = maxSockets;
	}
	/**
	 * 最大强化等级
	 * @return
	 */
	public int getMaxIntensify() {
		return maxIntensify;
	}
	public void setMaxIntensify(int maxIntensify) {
		this.maxIntensify = maxIntensify;
	}
	/**
	 * 所属套装
	 * @return
	 */
	public int getSuit() {
		return suit;
	}
	public void setSuit(int suit) {
		this.suit = suit;
	}

	/**
	 * 物品的功能类型(具体功能表id)
	 * @return
	 */
	public String getSkill() {
		return skill;
	}
	public void setSkill(String skill) {
		this.skill = skill;
	}
	/**
	 * 装备对应的部位
	 * @return
	 */
	public int getEqpart() {
		return eqpart;
	}
	public void setEqpart(int eqpart) {
		this.eqpart = eqpart;
	}
//	/**
//	 * 属性
//	 * @return
//	 */
//	public Map<String, Float> getAttribute() {
//		return attribute;
//	}
//
//	public void setAttribute(Map<String, Float> attribute) {
//		this.attribute = attribute;
//	}
	
//	public void setAttribute(String key, Float value) {
//		this.attribute.put(key, value);
//	}
	
	/**
	 * 宝石类型
	 * @return
	 */
	public String getGemtype() {
		return gemtype;
	}
	
	public void setGemtype(String gemtype) {
		this.gemtype = gemtype;
	}
	
	/**
	 * 合成时的下一级宝石ID 同时 还是 提升品质后的装备ID
	 * @return
	 */
	public String getNextid() {
		return nextid;
	}
	public void setNextid(String nextid) {
		this.nextid = nextid;
	}
	
	
	/**
	 * 宝石镶嵌所需要的最小装备等级
	 * @return
	 */
	public int getEquneed() {
		return equneed;
	}
	public void setEquneed(int equneed) {
		this.equneed = equneed;
	}
	
	/**
	 * 宝石增加的战斗属性
	 * @return
	 */
	public String getProadd() {
		return proadd;
	}
	public void setProadd(String proadd) {
		this.proadd = proadd;
	}
	
	/**
	 * 宝石增加的战斗属性的数值
	 * @return
	 */
	public float getProvalue() {
		return provalue;
	}
	public void setProvalue(float provalue) {
		this.provalue = provalue;
	}
	
	/**
	 * 宝石镶嵌后给普通攻击附加的效果id
	 * @return
	 */
	public String getEffectid() {
		return effectid;
	}
	public void setEffectid(String effectid) {
		this.effectid = effectid;
	}
	
	/**
	 * 宝石技能效果触发的几率
	 * @return
	 */
	public float getEffectodds() {
		return effectodds;
	}
	public void setEffectodds(float effectodds) {
		this.effectodds = effectodds;
	}
	
	/**
	 * 获取装备基础属性
	 * @return
	 */
	public Map<String, Long> getEquipBaseAttr() {
		return equipBaseAttr;
	}
	
	
	public Map<String,Long> getEquipRealAttr(int t111){
		Map<String,Long> t22 = new HashMap<>();
		GoodsConfig t1 = StageConfigureHelper.getGoodsConfigExportService().loadById(this.getId());
		if(t1 == null || t1.getCategory() != GoodsCategory.EQUIP_TYPE){
			return t22;
		}
		Map<String, Long> t11 = t1.getEquipBaseAttr();
		if(t111 <= 0){
			return t11;
		}
		int t12 =0;
	 
		if(EquipTypeSlot.isBodyEquip(t1.getEqpart())){
			t12 = GameConstants.EQUIP_TIPIN_TYPE_NOMAL;
			if (t1.getSuit() > 0) {
				t12 = GameConstants.EQUIP_TIPIN_TYPE_TAOZHUANG;
			}
		}else{
			t12 = GameConstants.EQUIP_TIPIN_TYPE_FUSHU;
			if (t1.getSuit() > 0) {
				t12 = GameConstants.EQUIP_TIPIN_TYPE_TAOZHUANG;
			}
		}
		 
		TiPinBiaoConfig t123 = StageConfigureHelper.getTiPinBiaoConfigExportService().loadById(t12, t1.getRareLevel());
		if (t123 == null || ObjectUtil.strIsEmpty(t1.getTipinId())) {
			return t22;
		}
	 
		GoodsConfig t121 = StageConfigureHelper.getGoodsConfigExportService().loadById(t1.getTipinId());
		if(t121 == null){
			return t11;
		}
		Map<String,Long> t212 = t121.getEquipBaseAttr();
		Map<String,Long> t112 = new HashMap<>();
		
		for (Entry<String,Long> t321 : t212.entrySet()) {
			long addV = t321.getValue() - CovertObjectUtil.obj2long(t11.get(t321.getKey()));
			if(addV >0){
				t112.put(t321.getKey(), addV);
			}
		}
		
		if(ObjectUtil.isEmpty(t112)){
			return t11;
		}
		
		Map<String,Long> t221 = new HashMap<>();
		for (Entry<String,Long>  t323 : t112.entrySet()) {
			String t132 = t323.getKey();
			long t332 = t323.getValue();
			long va = (long)Math.round((float)t332/t123.getXingshu() * (t111/t123.getStarMax()));
			t221.put(t132, va);
		}
		
		ObjectUtil.longMapAdd(t22, t11);
		ObjectUtil.longMapAdd(t22, t221);
		
		return t22;
	}
	
	
	public void setEquipBaseAttr(Map<String, Long> equipBaseAttr) {
		this.equipBaseAttr = equipBaseAttr;
	}
	
	
	/**
	 * 获取装备效果属性
	 * @return
	 */
	public Map<String, Long> getEquipEffectAttr() {
		return equipEffectAttr;
	}
	public void setEquipEffectAttr(Map<String, Long> equipEffectAttr) {
		this.equipEffectAttr = equipEffectAttr;
	}
	/**
	 * 获取装备强化增加属性
	 * @return
	 */
	public Map<String, Long> getEquipQianHuaAddAttr() {
		return equipQianHuaAddAttr;
	}
	public void setEquipQianHuaAddAttr(Map<String, Long> equipQianHuaAddAttr) {
		this.equipQianHuaAddAttr = equipQianHuaAddAttr;
	}
	
	
	/**
	 * 是否为绑定物品
	 * @return
	 */
	public int getBangding() {
		return bangding;
	}
	public void setBangding(int bangding) {
		this.bangding = bangding;
	}
	
	
	public String getId1() {
		return id1;
	}
	public void setId1(String id1) {
		this.id1 = id1;
	}
	public int getData1() {
		return data1;
	}
	public void setData1(int data1) {
		this.data1 = data1;
	}
	public Float getData2() {
		return data2;
	}
	public void setData2(Float data2) {
		this.data2 = data2;
	}
	public String getData3() {
		return data3;
	}
	public void setData3(String data3) {
		this.data3 = data3;
	}
	public String getData4() {
		return data4;
	}
	public void setData4(String data4) {
		this.data4 = data4;
	}
	public int getGuanzhiReq() {
		return guanzhiReq;
	}
	public void setGuanzhiReq(int guanzhiReq) {
		this.guanzhiReq = guanzhiReq;
	}
	public boolean isJianding() {
		return isJianding;
	}
	public void setIsJianding(boolean isJianding) {
		this.isJianding = isJianding;
	}
	public int getRlz() {
		return rlz;
	}
	public void setRlz(int rlz) {
		this.rlz = rlz;
	}
	public List<Integer> getActivates() {
		return activates;
	}
	public void setActivates(List<Integer> activates) {
		this.activates = activates;
	}
	public Map<String,Integer> getNeedItem() {
		return needItem;
	}
	public void setNeedItem(Map<String,Integer> needItem) {
		this.needItem = needItem;
	}
	public float getOdds() {
		return odds;
	}
	public void setOdds(float odds) {
		this.odds = odds;
	}
	public Map<String, String> getQianghuaSX() {
		return qianghuaSX;
	}
	public void setQianghuaSX(Map<String, String> qianghuaSX) {
		this.qianghuaSX = qianghuaSX;
	}
	public int getCuihui() {
		return cuihui;
	}
	public void setCuihui(int cuihui) {
		this.cuihui = cuihui;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public int getJobshow() {
		return jobshow;
	}
	public void setJobshow(int jobshow) {
		this.jobshow = jobshow;
	}
	public boolean isDaily() {
		return daily == 1;
	}
	public void setDaily(int daily) {
		this.daily = daily;
	}
	
	/**
	 * 是否不记录该道具的唯一标识的操作日志    0或不填（不记录）1（记录）     
	 * @return true 记录
	 */
	public boolean getRecord() {
		return record;
	}
	public void setRecord(boolean record) {
		this.record = record;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public int getFirstType() {
		return firstType;
	}
	public void setFirstType(int firstType) {
		this.firstType = firstType;
	}
	public int getFenzu() {
		return fenzu;
	}
	public void setFenzu(int fenzu) {
		this.fenzu = fenzu;
	}
	public String getCd() {
		return cd;
	}
	public void setCd(String cd) {
		this.cd = cd;
	}
	public String getEquipId() {
		return equipId;
	}
	public void setEquipId(String equipId) {
		this.equipId = equipId;
	}
	public String getTipinId() {
		return tipinId;
	}
	public void setTipinId(String tipinId) {
		this.tipinId = tipinId;
	}
	public int getNeedzhuansheng() {
		return needzhuansheng;
	}
	public void setNeedzhuansheng(int needzhuansheng) {
		this.needzhuansheng = needzhuansheng;
	} 
	
}
