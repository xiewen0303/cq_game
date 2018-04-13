package com.junyou.bus.bag.entity;

 
import java.io.Serializable;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Primary;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_item")
public class RoleItem extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Primary
	@Column("id")
	private Long id;

	@Column("container_type")
	private Integer containerType;

	@Column("is_delete")
	private Integer isDelete;

	@Column("slot")
	private Integer slot;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("goods_id")
	private String goodsId;

	@Column("count")
	private Integer count;
	
	/**
	 * 耐久度
	 */
	@Column("current_durability")
	private Integer currentDurability;
	/**
	 * 过期时间
	 */
	@Column("expire_time")
	private Long expireTime;

	/**
	 * 强化等级
	 */
	@Column("qianhua_level")
	private Integer qianhuaLevel;
	
	/**
	 * 套装铸神等级
	 */
	@Column("zhushen_level")
	private Integer zhushenLevel;
	/**
	 * 套装铸神失败次数
	 */
	@Column("zhushen_fail_times")
	private Integer zhushenFailTimes;
	
	/**
	 * 创建者信息
	 */
	@Column("create_describe")
	private String createDescribe;
	
	/**
	 * 创建时间
	 */
	@Column("create_time")
	private Long createTime;
	
	/**
	 * 随机属性
	 */
	@Column("random_attrs")
	private Integer randomAttrs;
	
	/**
	 * 提品值
	 */
	@Column("tipin_value")
	private Integer tipinValue;
	
	/**
	 * 其他的数据存储
	 */
	@Column("other_data")
	private String otherData;
	
    /**
     * 宠物编号
     */
    @Column("chongwu_id")
    private Integer chongwuId;
    
    /**
     * 神器编号
     */
    @Column("shenqi_id")
    private Integer shenqiId;
    
	@EntityField
	private transient Map<Integer,Object> otherDataMap;

	public Map<Integer, Object> getOtherDataMap() {
		return otherDataMap;
	}

	public void setOtherDataMap(Map<Integer, Object> otherDataMap) {
		this.otherDataMap = otherDataMap;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public Integer getContainerType() {
		return containerType;
	}

	public void setContainerType(Integer containerType) {
		this.containerType = containerType;
	}
	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	public Integer getSlot() {
		return slot;
	}

	public void setSlot(Integer slot) {
		this.slot = slot;
	}
	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}
	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getCurrentDurability() {
		return currentDurability;
	}

	public void setCurrentDurability(Integer currentDurability) {
		this.currentDurability = currentDurability;
	}
	public Long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}
//	public Integer getJinyouLevel() {
//		return jinyouLevel;
//	}
//
//	public void setJinyouLevel(Integer jinyouLevel) {
//		this.jinyouLevel = jinyouLevel;
//	}
	public Integer getQianhuaLevel() {
		return qianhuaLevel;
	}

	public void setQianhuaLevel(Integer qianhuaLevel) {
		this.qianhuaLevel = qianhuaLevel;
	}
	 
//	public String getJianding() {
//		return jianding;
//	}
//
//	public void setJianding(String jianding) {
//		this.jianding = jianding;
//	}
//
//	public String getGems() {
//		return gems;
//	}
//
//	public void setGems(String gems) {
//		this.gems = gems;
//	}
//	public Integer getOpenKongCount() {
//		return openKongCount;
//	}
//
//	public void setOpenKongCount(Integer openKongCount) {
//		this.openKongCount = openKongCount;
//	}
	public String getCreateDescribe() {
		return createDescribe;
	}

	public void setCreateDescribe(String createDescribe) {
		this.createDescribe = createDescribe;
	}
	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	
	public String getOtherData() {
		return otherData;
	}

	public Integer getRandomAttrs() {
		return randomAttrs;
	}

	public void setRandomAttrs(Integer randomAttrs) {
		this.randomAttrs = randomAttrs;
	}

	public Integer getZhushenLevel() {
		return zhushenLevel;
	}

	public void setZhushenLevel(Integer zhushenLevel) {
		this.zhushenLevel = zhushenLevel;
	}

	public Integer getZhushenFailTimes() {
		return zhushenFailTimes;
	}

	public void setZhushenFailTimes(Integer zhushenFailTimes) {
		this.zhushenFailTimes = zhushenFailTimes;
	}

	public void setOtherData(String otherData) { 
		this.otherData = otherData;
		if(otherData!=null&&!"".equals(otherData)){
			otherDataMap = JSONObject.parseObject(otherData,Map.class);
		}
		this.otherData = otherData;
	}
	@Override	
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return getId();
	}
	
	public RoleItem copy(){
		RoleItem result = new RoleItem();
		result.setId(getId());
		result.setContainerType(getContainerType());
		result.setIsDelete(getIsDelete());
		result.setSlot(getSlot());
		result.setUserRoleId(getUserRoleId());
		result.setGoodsId(getGoodsId());
		result.setCount(getCount());
		result.setCurrentDurability(getCurrentDurability());
		result.setExpireTime(getExpireTime());
//		result.setJinyouLevel(getJinyouLevel());
		result.setQianhuaLevel(getQianhuaLevel());
//		result.setJianding(getJianding());
//		result.setGems(getGems());
//		result.setOpenKongCount(getOpenKongCount());
		result.setCreateDescribe(getCreateDescribe());
		result.setCreateTime(getCreateTime());
		result.setOtherData(getOtherData());
		result.setRandomAttrs(getRandomAttrs());
		result.setTipinValue(getTipinValue());
		result.setZhushenLevel(getZhushenLevel());
		result.setZhushenFailTimes(getZhushenFailTimes());
		result.setChongwuId(getChongwuId());
		result.setShenqiId(getShenqiId());
		return result;
	}

	public Integer getTipinValue() {
		return tipinValue;
	}

	public void setTipinValue(Integer tipinValue) {
		this.tipinValue = tipinValue;
	}
	
	

	public Integer getChongwuId() {
        return chongwuId;
    }

    public void setChongwuId(Integer chongwuId) {
        this.chongwuId = chongwuId;
    }

    public Integer getShenqiId() {
		return shenqiId;
	}

	public void setShenqiId(Integer shenqiId) {
		this.shenqiId = shenqiId;
	}

	/**
	 * 是否过期
	 * @return
	 */
	public boolean isExpireTimed() {
		if(expireTime > 0 && expireTime <= GameSystemTime.getSystemMillTime()){
			return true;
		}
		return false;
	}
 
}
