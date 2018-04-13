package com.junyou.gameconfig.publicconfig.configure.export;


/**
 * 人物基础属性配置数据
 * 
 *@author  LiNing
 *@created 2013-10-25下午5:01:05
 */
public class RoleBasePublicConfig extends AdapterPublicConfig{

	private int speed;//人物基础移动速度
	
	private int quanshenqianghuaNum;//激活全身强化所需装备数量
	
	private String weiDuanBuff;//微端BUFF
	
	private String mabiBuff;//麻痹BUFF
	
	private String ziqibuff;// 手套防御BUFF
	
	private int protectLevel;//新手保护等级
	
	
	private float ptHpBv;//普通复活后保留血量百分比
	private float tsHpBv;//特殊复活后保留血量百分比
	
	
	
	
	/**
	 * 普通复活后保留血量百分比
	 * @return
	 */
	public float getPtHpBv() {
		return ptHpBv;
	}
	
	/**
	 * 特殊复活后保留血量百分比
	 * @return
	 */
	public float getTsHpBv() {
		return tsHpBv;
	}

	public void setPtHpBv(float ptHpBv) {
		this.ptHpBv = ptHpBv;
	}

	public void setTsHpBv(float tsHpBv) {
		this.tsHpBv = tsHpBv;
	}


	/**
	 * 新手保护等级
	 * @return
	 */
	public int getProtectLevel() {
		return protectLevel;
	}

	public void setProtectLevel(int protectLevel) {
		this.protectLevel = protectLevel;
	}

	/**
	 * 麻痹BUFF
	 * @return
	 */
	public String getMabiBuff() {
		return mabiBuff;
	}

	public void setMabiBuff(String mabiBuff) {
		this.mabiBuff = mabiBuff;
	}

	/**
	 * 微端BUFF
	 * @return
	 */
	public String getWeiDuanBuff() {
		return weiDuanBuff;
	}

	public void setWeiDuanBuff(String weiDuanBuff) {
		this.weiDuanBuff = weiDuanBuff;
	}

	/**
	 * 激活全身强化所需装备数量
	 * @return
	 */
	public int getQuanshenqianghuaNum() {
		return quanshenqianghuaNum;
	}

	public void setQuanshenqianghuaNum(int quanshenqianghuaNum) {
		this.quanshenqianghuaNum = quanshenqianghuaNum;
	}

	/**
	 * 人物基础移动速度
	 * @return
	 */
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

    /**
     * 手套防御buff
     * 
     * @return
     * @Description:
     */
    public String getZiqibuff() {
        return ziqibuff;
    }

    public void setZiqibuff(String ziqibuff) {
        this.ziqibuff = ziqibuff;
    }
	
}
