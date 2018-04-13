package com.junyou.gameconfig.monster.configure.export;

import java.util.ArrayList;
import java.util.List;

import com.junyou.gameconfig.export.DropRule;
import com.junyou.gameconfig.export.MoneyDropRule;
import com.junyou.utils.collection.ReadOnlyMap;

/**
 * 怪物
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2014-12-25 下午5:49:11
 */
public class MonsterConfig{

	private String id;
	private int monsterType;
	private String name;
	private int level;
	private ReadOnlyMap<String, Long> attribute;
	private List<String> skillList;
	private Integer camp;
	private boolean ifactive;//是否是被动怪，true是
	private int eyeshot;
	private int basicExp;
	private int basicMoney;
	private int lasttime;
	private List<DropRule> dropRule = new ArrayList<DropRule>();
	private List<MoneyDropRule> moneyDropRules;
	//怪物掉落物品的消失时间（秒）
	private Integer dropGoodsDuration;
	// 掉落保护时间（秒）
	private Integer dropProtectDuration;
	private Integer disappearDuration;
	//每秒回复的血量
	private Integer huiFuHp;
	private int maxrange;
	private String aiscripts;
	private int rank;
	//战斗状态时的心跳时间(毫秒)
	private int heartTime;
	//是否是不可移动
	private boolean isNoMove = false;
	//是否是不可攻击其它人
	private boolean isNoAttrack = false;
	//是否是不可被攻击
	private boolean isNoBeiAttrack = false;
	//是否只攻击红名玩家
	private boolean isAttriackRedRold = false;
	//死亡是否广播
	private boolean isDeadNotify = false;
	//是否自动巡逻
	private boolean isXunluo; 
	private int type;
	//是否没有受益人
	private boolean isNoOwner = false;
	//是否仅自己可以拾取
	private boolean isOnlySelfPickup;
	//是否可被击退
	private int beatback;
	
	/**
	 * 死亡是否广播
	 * @return true:是
	 */
	public boolean isDeadNotify() {
		return isDeadNotify;
	}


	public void setDeadNotify(boolean isDeadNotify) {
		this.isDeadNotify = isDeadNotify;
	}
	

	/**
	 * 是否只攻击红名玩家
	 * @return true:只攻击红名玩家
	 */
	public boolean isAttriackRedRold() {
		return isAttriackRedRold;
	}



	public void setAttriackRedRold(boolean isAttriackRedRold) {
		this.isAttriackRedRold = isAttriackRedRold;
	}

	
	/**
	 * 每秒回复的血量
	 * @return
	 */
	public Integer getHuiFuHp() {
		return huiFuHp;
	}


	public void setHuiFuHp(Integer huiFuHp) {
		this.huiFuHp = huiFuHp;
	}


	/**
	 * 战斗状态时的心跳时间(毫秒)
	 * @return
	 */
	public int getHeartTime() {
		return heartTime;
	}


	public void setHeartTime(int heartTime) {
		this.heartTime = heartTime;
	}


	/**
	 *  掉落保护时间（秒）
	 * @return
	 */
	public Integer getDropProtectDuration() {
		return dropProtectDuration;
	}


	public void setDropProtectDuration(Integer dropProtectDuration) {
		this.dropProtectDuration = dropProtectDuration;
	}


	/**
	 * 是否是不可移动
	 * @return true:不可移动
	 */
	public boolean isNoMove() {
		return isNoMove;
	}

	
	/**
	 * 是否是不可移动
	 * @param isNoMove
	 */
	public void setNoMove(boolean isNoMove) {
		this.isNoMove = isNoMove;
	}

	
	/**
	 * 是否是不可攻击其它人
	 * @return true:不可攻击其它人
	 */
	public boolean isNoAttrack() {
		return isNoAttrack;
	}

	public void setNoAttrack(boolean isNoAttrack) {
		this.isNoAttrack = isNoAttrack;
	}

	/**
	 * 是否是不可被攻击
	 * @return true:不可被攻击
	 */
	public boolean isNoBeiAttrack() {
		return isNoBeiAttrack;
	}

	public void setNoBeiAttrack(boolean isNoBeiAttrack) {
		this.isNoBeiAttrack = isNoBeiAttrack;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getMaxrange() {
		return maxrange;
	}

	public void setMaxrange(int maxrange) {
		this.maxrange = maxrange;
	}

	/**
	 * 怪物掉落
	 * @return
	 */
	public List<DropRule> getDropRule() {
		return dropRule;
	}

	public void setDropRule(List<DropRule> dropRule) {
		this.dropRule = dropRule;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 怪物类型
	 * @return
	 */
	public int getMonsterType() {
		return monsterType;
	}

	public void setMonsterType(int monsterType) {
		this.monsterType = monsterType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 怪物等级
	 * @return
	 */
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public ReadOnlyMap<String, Long> getAttribute() {
		return attribute;
	}


	public void setAttribute(ReadOnlyMap<String, Long> attribute) {
		this.attribute = attribute;
	}

	public List<String> getSkillList() {
		return skillList;
	}

	public void setSkillList(List<String> skillList) {
		this.skillList = skillList;
	}

	/**
	 * 所属阵营
	 * @return
	 */
	public Integer getCamp() {
		return camp;
	}

	public void setCamp(Integer camp) {
		this.camp = camp;
	}

	/**
	 * 主被动
	 * @return
	 */
	public boolean getIfactive() {
		return ifactive;
	}

	public void setIfactive(boolean ifactive) {
		this.ifactive = ifactive;
	}

	/**
	 * 视野
	 * @return
	 */
	public int getEyeshot() {
		return eyeshot;
	}

	public void setEyeshot(int eyeshot) {
		this.eyeshot = eyeshot;
	}

	/**
	 * 基本经验值
	 * @return
	 */
	public int getBasicExp() {
		return basicExp;
	}

	public void setBasicExp(int basicExp) {
		this.basicExp = basicExp;
	}

	/**
	 * 掉落金币
	 * @return
	 */
	public int getBasicMoney() {
		return basicMoney;
	}

	public void setBasicMoney(int basicMoney) {
		this.basicMoney = basicMoney;
	}

	/**
	 * 怪物存活时间(秒）
	 * @return
	 */
	public int getLasttime() {
		return lasttime;
	}

	public void setLasttime(int lasttime) {
		this.lasttime = lasttime;
	}
	/**
	 * s
	 */
	public Integer getDropGoodsDuration(){
		return dropGoodsDuration;
	}
	
	/**
	 * s
	 */
	public Integer getDisappearDuration(){
		return disappearDuration;
	}

	public void setDropGoodsDuration(Integer dropGoodsDuration) {
		this.dropGoodsDuration = dropGoodsDuration;
	}

	public void setDisappearDuration(Integer disappearDuration) {
		this.disappearDuration = disappearDuration;
	}

	public List<MoneyDropRule> getMoneyDropRules() {
		return moneyDropRules;
	}

	public void setMoneyDropRules(List<MoneyDropRule> moneyDropRules) {
		this.moneyDropRules = moneyDropRules;
	}

	public String getAiscripts() {
		return aiscripts;
	}

	public void setAiscripts(String aiscripts) {
		this.aiscripts = aiscripts;
	}

	public boolean isXunluo() {
		return isXunluo;
	}

	public void setXunluo(boolean isXunluo) {
		this.isXunluo = isXunluo;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isNoOwner() {
		return isNoOwner;
	}

	public void setNoOwner(boolean isNoOwner) {
		this.isNoOwner = isNoOwner;
	}


    public boolean isOnlySelfPickup() {
        return isOnlySelfPickup;
    }


    public void setOnlySelfPickup(boolean isOnlySelfPickup) {
        this.isOnlySelfPickup = isOnlySelfPickup;
    }

	public int getBeatback() {
		return beatback;
	}
	public boolean isBeatback() {
		return beatback == 1;
	}

	public void setBeatback(int beatback) {
		this.beatback = beatback;
	}
}
