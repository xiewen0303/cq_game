package com.junyou.gameconfig.map.configure.export;

import java.util.List;

import com.junyou.context.GameServerContext;
import com.junyou.gameconfig.export.teleport.TeleportChecker;
import com.junyou.gameconfig.map.configure.util.DiTuConfigUtil;
import com.junyou.utils.lottery.Lottery;

/**
 * 地图信息配置表
 *@author  DaoZheng Yuan
 *@created 2013-10-16上午10:18:19
 */
public class DiTuConfig{
	
	//地图ID
	private int id;
	
	//地图传送条件
	private TeleportChecker teleportChecker;
	
	//随机坐标
	private List<int[]> randomPoints;
	
	//出生随机坐标
	private List<int[]> birthRandomPoints;
	
	/**
	 * 地图类型
	 *  0. 出生地图
	 *  1. 普通地图
		2. 副本地图
		3. 活动地图
		4. 主城地图
	 */
	private Integer type;
	
	//下线是否回城
	private Boolean isHuiChen;
	
	//是否不可骑乘
	private boolean isNoRideAble;
	
	//是否不可以随机传送
	private boolean isNoRandom;
	
	//是否随机出生
	private boolean isRandomBirth;
	
	//是否镖车可进入
	private boolean isBiaoCheCanEnter;
	
	//不可使用回城石
	private boolean isNoHuichengable;
	
	
	//地图名称
	private String name;
	
	//是否启动不创建 
	private boolean isBootNoInit;
	
	//死亡后复活的地图
	private Integer reviveMapId;
	
	
	//地图分线配置表
	private MapLineConfig mapLineConfig;
	
	//非活动时间经验倍率
	private float expNoBv;
	//非活动时间是否掉落
	private boolean isNoDrop;
	
	//攻城战期间杀人是不否加PK值
	private boolean isGcWarNoAddPk;
	
	//杀死后地图不加PK值,死亡后不掉落物品
	private boolean isNoPkValue;
	
	//是否新手保护
	private boolean isNewProtect;
	
	//是否安全地图（玩家之间不可攻击，可以打怪，怪可以打人）
	private boolean isSafeMap;
	
	//地图模式
	private int mode;
	//地图是否可摆摊
	private int baitan;
	//GM是否可以传送进入
	private boolean gmMove;
	
	private boolean isHelp;//是否呼救（1：是）
	
	private int fuhuoType;//地图复活模式  0或者不填 可使用元宝复活   1  只能回城复活
	
	private boolean isAddPk;//是否增加PK值
	
	private long kickTime;//踢出时间
	
	private boolean canCreateAoiStage;//可以创建野外场景
	
	/**
	 * 是否呼救（1：是）
	 * @return
	 */
	public boolean isHelp() {
		return isHelp;
	}

	public void setHelp(boolean isHelp) {
		this.isHelp = isHelp;
	}
	
	/**
	 * 是否启动不创建 
	 * @return true:不创建
	 */
	public boolean isBootNoInit() {
		return isBootNoInit;
	}

	/**
	 * 是否启动不创建 
	 * @return true:不创建
	 */
	public void setBootNoInit(boolean isBootNoInit) {
		this.isBootNoInit = isBootNoInit;
	}

	/**
	 * 活动时间内杀死后地图不加PK值,死亡后不掉落物品
	 * @return true:不加PK ,不掉落
	 */
	public boolean isNoPkValue() {
		return isNoPkValue;
	}

	public void setNoPkValue(boolean isNoPkValue) {
		this.isNoPkValue = isNoPkValue;
	}
	
	/**
	 * GM是否可以通过GM传送进入此地图中
	 * @return
	 */
	public boolean isGmMove() {
		return gmMove;
	}

	public void setGmMove(boolean gmMove) {
		this.gmMove = gmMove;
	}

	/**
	 * 不可使用回城石 
	 * @return true:不可使用
	 */
	public boolean isNoHuichengable() {
		return isNoHuichengable;
	}

	public void setNoHuichengable(boolean isNoHuichengable) {
		this.isNoHuichengable = isNoHuichengable;
	}

	/**
	 * 是否随机出生
	 * @return true:随机出生
	 */
	public boolean isRandomBirth() {
		return isRandomBirth;
	}

	public void setRandomBirth(boolean isRandomBirth) {
		this.isRandomBirth = isRandomBirth;
	}

	/**
	 * 是否新手保护
	 * @return true:保护
	 */
	public boolean isNewProtect() {
		return isNewProtect;
	}

	public void setNewProtect(boolean isNewProtect) {
		this.isNewProtect = isNewProtect;
	}

	/**
	 * 攻城战期间杀人是否加PK值
	 * @return true:不加PK值
	 */
	public boolean isGcWarNoAddPk() {
		return isGcWarNoAddPk;
	}

	public void setGcWarNoAddPk(boolean isGcWarNoAddPk) {
		this.isGcWarNoAddPk = isGcWarNoAddPk;
	}

	/**
	 * 是否镖车可进入
	 * @return true:可进入
	 */
	public boolean isBiaoCheCanEnter() {
		return isBiaoCheCanEnter && DiTuConfigUtil.isPublicMap(type);
	}



	public void setBiaoCheCanEnter(boolean isBiaoCheCanEnter) {
		this.isBiaoCheCanEnter = isBiaoCheCanEnter;
	}

	/**
	 * 是否不可骑乘
	 * @return true:不可骑
	 */
	public boolean isNoRideAble() {
		return isNoRideAble;
	}

	public void setNoRideAble(boolean isNoRideAble) {
		this.isNoRideAble = isNoRideAble;
	}

	/**
	 * 是否不可以随机传送
	 * @return true:不可以
	 */
	public boolean isNoRandom() {
		return isNoRandom;
	}

	public void setNoRandom(boolean isNoRandom) {
		this.isNoRandom = isNoRandom;
	}

	public float getExpNoBv() {
		return expNoBv;
	}

	public void setExpNoBv(float expNoBv) {
		this.expNoBv = expNoBv;
	}

	public boolean isNoDrop() {
		return isNoDrop;
	}

	public void setNoDrop(boolean isNoDrop) {
		this.isNoDrop = isNoDrop;
	}

	public Boolean getIsHuiChen() {
		return isHuiChen;
	}

	public void setIsHuiChen(Boolean isHuiChen) {
		this.isHuiChen = isHuiChen;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
		canCreateAoiStage = GameServerContext.getServerInfoConfig().isGmOpen() || DiTuConfigUtil.isCanCreateAoiStage(type);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<int[]> getBirthRandomPoints() {
		return birthRandomPoints;
	}
	
	public int[] getRandomBirth(){
		int randome = Lottery.roll(birthRandomPoints.size());
		return birthRandomPoints.get(randome);
	}

	public void setBirthRandomPoints(List<int[]> birthRandomPoints) {
		this.birthRandomPoints = birthRandomPoints;
	}

	public List<int[]> getRandomPoints() {
		return randomPoints;
	}

	public void setRandomPoints(List<int[]> randomPoints) {
		this.randomPoints = randomPoints;
	}

	public TeleportChecker getTeleportChecker() {
		return teleportChecker;
	}

	public void setTeleportChecker(TeleportChecker teleportChecker) {
		this.teleportChecker = teleportChecker;
	}

	public Integer getReviveMapId() {
		return reviveMapId;
	}

	public void setReviveMapId(Integer reviveMapId) {
		this.reviveMapId = reviveMapId;
	}

	public MapLineConfig getMapLineConfig() {
		return mapLineConfig;
	}

	public void setMapLineConfig(MapLineConfig mapLineConfig) {
		this.mapLineConfig = mapLineConfig;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getBaitan() {
		return baitan;
	}

	public void setBaitan(int baitan) {
		this.baitan = baitan;
	}
	/**
	 * 是否是摆摊地图
	 * @return
	 */
	public boolean isBaitan(){
		if(getBaitan() == 1){
			return true;
		}
		return false;
	}

	/**
	 * 是不是在活动期间内
	 * @return
	 */
	public boolean isInHuodongTime(){
		boolean check = true;
//		if(!CovertObjectUtil.isEmpty(startDay)){
//			try {
//				Date date = DateUtils.parseDate(startDay, new String[]{"yyyy-MM-dd"});
//				check = GameSystemTime.getSystemMillTime() >= date.getTime();
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//		}
//		if(check && !CovertObjectUtil.isEmpty(endDay)){
//			check = GameSystemTime.getSystemMillTime() < DateUtils.parseDateMillTime(endDay);
//		}
//		if(check && !CovertObjectUtil.isEmpty(startTime)){
//			String[] time = startTime.split(":");
//			Long configTime = DateUtils.getCalcTimeKuang(0, Integer.parseInt(time[0]),Integer.parseInt(time[1]));
//			check = configTime <= GameSystemTime.getSystemMillTime();
//		}
//		if(check && !CovertObjectUtil.isEmpty(endTime)){
//			String[] time = endTime.split(":");
//			Long configTime = DateUtils.getCalcTimeKuang(0, Integer.parseInt(time[0]),Integer.parseInt(time[1]));
//			check = configTime >= GameSystemTime.getSystemMillTime();
//		}
		
		return check;
	}
	
	/**
	 * 是否可以道具复活
	 * @return true:是
	 */
	public boolean isCanPropFuhuo(){
		return fuhuoType == 0;
	}
	
	public void setFuhuoType(int fuhuoType) {
		this.fuhuoType = fuhuoType;
	}

	public boolean isSafeMap() {
		return isSafeMap;
	}

	public void setSafeMap(boolean isSafeMap) {
		this.isSafeMap = isSafeMap;
	}

	public boolean isAddPk() {
		return isAddPk;
	}

	public void setAddPk(boolean isAddPk) {
		this.isAddPk = isAddPk;
	}

	public long getKickTime() {
		return kickTime;
	}

	public void setKickTime(long kickTime) {
		this.kickTime = kickTime;
	}

	public boolean isCanCreateAoiStage() {
		return canCreateAoiStage;
	}
	
	
}
