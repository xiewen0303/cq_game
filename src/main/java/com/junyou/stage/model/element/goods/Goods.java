package com.junyou.stage.model.element.goods;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.export.PathNodeSize;
import com.junyou.stage.model.core.element.AbsElement;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.PointTakeupType;
import com.junyou.stage.model.core.state.IStateManager;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.utils.datetime.GameSystemTime;

public class Goods extends AbsElement {
	
	private boolean firstDrop = true;
	
	private Long id;
	private String goodsId;
	private int count;
	private Long dropGuid;//掉落者Guid
	private long dropTime;//掉落时间
	private long expireTime;//过期时间
	private int qianhuaLevel; 
	private int currentDurability;//耐久度
	private long itemCreateTime;//物品创建时间
	private String itemDesc;//物品描述 (创建人,怎么获得,以及其他的获得信息)
	private Integer randomAttrIds;//随机ids属性id
	private Map<Integer,Object> otherData;//其他属性信息 
	private Integer ownTeamId;
	private Long ownerId;
	private Integer protect = 0;//保护时间(s) 
	private Integer duration = 60;//s 
	private long protectExpireTime;//保护过期时间(ms)	
	private boolean isOnlySelfPickup;//是否仅仅自己可以拾取(false=自己和队伍都可以拾取;true=仅自己可以拾取)
	
	
	public Goods(Long id, String goodsId, int count, long itemCreateTime) {
		super(id, null);
		this.id = id;
		this.goodsId = goodsId;
		this.count = count;
		this.itemCreateTime = itemCreateTime;
	}
	
	/**
	 * 掉落者GUID
	 * @return
	 */
	public Long getDropGuid() {
		return dropGuid;
	}



	public void setDropGuid(Long dropGuid) {
		this.dropGuid = dropGuid;
	}



	public PathNodeSize getPathNodeSize(){
		return PathNodeSize._1X;
	}
	
	@Override
	public Integer getCamp() {
		return 0;
	}

	@Override
	public ElementType getElementType() {
		return ElementType.GOODS;
	}
	
	@Override
	public short getEnterCommand() {
		return ClientCmdType.AOI_GOODS_ENTER;
	}

	@Override
	public Object getStageData() {
		return null;
	}

	@Override
	public Object getMsgData() {
//		0 String 掉落物品标识GUID 
//		1 String 掉落物品的物品配置ID，如果是金钱，用特殊标识”MONEY” 
//		2 int 坐标x 
//		3 int 坐标y 
//		4 String 掉落物品所有者ID 
//		5 String 掉落物品所属队伍ID 
//		6 Number 掉落物品的掉落保护结束的时间戳 
//		7 int 掉落数量 
//		8 Boolean 是否为刚刚掉落，true为刚刚掉落 false或者没有此位为早已掉落 

		Object[] result = new Object[]{
				getId(),
				getGoodsId(),
				getPosition().getX(),
				getPosition().getY(),
				getOwnerId(),
				getOwnTeamId(),
				getProtectExpireTime(),
				getCount(),
				firstDrop ? dropGuid : null
		};
		
		return result;
	}

	@Override
	public IStateManager getStateManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void enterStageHandle(IStage stage) {
		
		firstDrop = false;
		
		StageTokenRunable runable = new StageTokenRunable(null, stage.getId(), InnerCmdType.GOODS_DISAPPEAR, getId());
		this.getScheduler().schedule(id.toString(), GameConstants.COMPONENT_DROP_GOODS, runable,duration, TimeUnit.SECONDS); 
	}
	
	@Override
	public void leaveStageHandle(IStage stage) {
		
		//TODO
		
	}

	public String getGoodsId() {
		return goodsId;
	}
 
	public int getCount() {
		return count;
	}
   
	public void setOwnTeamId(Integer ownTeamId) {
		this.ownTeamId = ownTeamId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

   
	/**
	 * 获取保护时间(秒)
	 * @return
	 */
	public Integer getProtect() {
		return protect;
	}

	public void setProtect(Integer protect) {
		this.protect = protect;
	}

	 
	public boolean checkIdentity(IRole role) {
		
		boolean check = false;
		
		if(inProtectRule()){
			//保护时间内，个人保护有队伍则进行队伍保护
		    if(isOnlySelfPickup()){// 只能自己拾取
		        check = null != getOwnerId() && role.getId().equals(getOwnerId());
		    }else{// 自己和同一队伍都可以拾取
		        check = (null == getOwnerId()) || role.getId().equals(getOwnerId());
		        check = check || ((null != getOwnTeamId()) && role.getTeamMember() != null && getOwnTeamId().equals(role.getTeamMember().getTeamId()));
		    }
		}else{
			return true;
		}
		
		return check;
	}
	
	/**
	 * 保护时间内验证
	 * @return true:保护时间内
	 */
	private boolean inProtectRule(){
		if(protect <= 0){
			return false;
		} 
		
		return getProtectExpireTime() > GameSystemTime.getSystemMillTime();
	}


	public void setDropTime(long dropTime) {
		this.dropTime = dropTime;
		
		this.protectExpireTime = dropTime + (protect * 1000);
	}
	
	public long getProtectExpireTime(){
		if(protectExpireTime == 0){
			protectExpireTime = dropTime + (protect * 1000);
		}
		
		return protectExpireTime;
	}

	public long getDropTime() {
		return dropTime;
	}
 

	public void setDuration(Integer duration) {
		
		if(duration > 0){
			this.duration = duration;
		}
	}

	@Override
	public PointTakeupType getTakeupType() {
		return PointTakeupType.GOODS;
	}

	public boolean isFirstDrop() {
		return firstDrop;
	}

	public void setFirstDrop(boolean firstDrop) {
		this.firstDrop = firstDrop;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	public int getQianhuaLevel() {
		return qianhuaLevel;
	}

	public void setQianhuaLevel(int qianhuaLevel) {
		this.qianhuaLevel = qianhuaLevel;
	}

	public int getCurrentDurability() {
		return currentDurability;
	}

	public void setCurrentDurability(int currentDurability) {
		this.currentDurability = currentDurability;
	}

	public long getItemCreateTime() {
		return itemCreateTime;
	}

	public void setItemCreateTime(long itemCreateTime) {
		this.itemCreateTime = itemCreateTime;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public Integer getRandomAttrIds() {
		return randomAttrIds;
	}

	public void setRandomAttrIds(Integer randomAttrIds) {
		this.randomAttrIds = randomAttrIds;
	}

	public Map<Integer, Object> getOtherData() {
		return otherData;
	}

	public void setOtherData(Map<Integer, Object> otherData) {
		this.otherData = otherData;
	}

	public Integer getOwnTeamId() {
		return ownTeamId;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setProtectExpireTime(long protectExpireTime) {
		this.protectExpireTime = protectExpireTime;
	}

    public boolean isOnlySelfPickup() {
        return isOnlySelfPickup;
    }

    public void setOnlySelfPickup(boolean isOnlySelfPickup) {
        this.isOnlySelfPickup = isOnlySelfPickup;
    }

    @Override
	public void deadHandle(IHarm harm) {
		// TODO Auto-generated method stub
		
	} 
}
