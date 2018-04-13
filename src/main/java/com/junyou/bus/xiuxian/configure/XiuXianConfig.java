package com.junyou.bus.xiuxian.configure;

/**
 * 修仙数据
 * @author DaoZheng Yuan
 * 2015年6月7日 下午2:32:47
 */
public class XiuXianConfig {
	
	//出售id
	private int id;
	
	//允许使用的货币类型类型
	private int moneyType;
	
	//出售物品ID
	private String goodsId;
	
	//出售物品数量
	private int goodsNum;
	
	//实际扣除的货币数量（真实价格）
	private int needValue;
	
	//原价（显示用）
	private int showNeedValue;
	
	//单人限制数量  0或者不填为无限制
	private int roleMaxCount;
	
	//全服限制数量	0或者不填为无限制
	private int serverMaxCount;
	
	/**
	 * 是否验证个人数量
	 * @return true:验证,false:不验证
	 */
	public boolean isCheckRoleCount(){
		if(roleMaxCount == 0){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 是否验证单服数量
	 * @return true:验证,false:不验证
	 */
	public boolean isCheckServerCount(){
		if(serverMaxCount == 0){
			return false;
		}else{
			return true;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(int moneyType) {
		this.moneyType = moneyType;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public int getNeedValue() {
		return needValue;
	}

	public void setNeedValue(int needValue) {
		this.needValue = needValue;
	}

	
	public int getShowNeedValue() {
		return showNeedValue;
	}

	public void setShowNeedValue(int showNeedValue) {
		this.showNeedValue = showNeedValue;
	}

	public int getRoleMaxCount() {
		return roleMaxCount;
	}

	public void setRoleMaxCount(int roleMaxCount) {
		this.roleMaxCount = roleMaxCount;
	}

	public int getServerMaxCount() {
		return serverMaxCount;
	}

	public void setServerMaxCount(int serverMaxCount) {
		this.serverMaxCount = serverMaxCount;
	}
}
