/**
 * 
 */
package com.junyou.stage.model.core.attribute;

import java.util.Map;

import com.junyou.stage.model.core.skill.IHarm;


/**
 * @description 战斗属性接口
 * @author ShiJie Chi
 * @created 2011-11-25上午10:01:14
 */
public interface IFightAttribute {
	
	/**
	 * 设置属性并刷新公式
	 * @param type
	 * @param valMap
	 */
	public void setBaseAttribute(BaseAttributeType type,Map<String,Long> valMap);
	/**
	 * 设置属性但不会刷新公式，需另调用refresh方法刷新属性
	 * @param type
	 * @param valMap
	 */
	public void initBaseAttribute(BaseAttributeType type,Map<String,Long> valMap);
	
	public void incrBaseAttribute(BaseAttributeType type,Map<String,Long> valMap);
	
	public void descBaseAttribute(BaseAttributeType type,Map<String,Long> valMap);
	
	public void clearBaseAttribute(BaseAttributeType type,boolean refreshOrNot);
	
	/**
	 * 根据公式刷新发生改变的属性值
	 */
	public void refresh();
	
	/**
	 * 获取效果属性
	 * @param effectAttribute
	 */
	public long getEffectAttribute(String effectType);
	
	/**
	 * 获取基本属性
	 */
	public IBaseAttribute getBaseAttributeMap(BaseAttributeType type);
	

	/**
	 * 重置血量和灵力
	 */
	public void resetHpMp();
	
	/**
	 * @return 生命
	 */
	public long getMaxHp();

	/**
	 * @param 生命
	 */
	public void setMaxHp(long maxHp);
	
	/**
	 * @return 攻击 x11
	 */
	public long getAttack();
	
	/**
	 * @param attack 攻击  x11
	 */
	public void setAttack(long attack);
	
	/**
	 * @param noDef	无视防御
	 */
	public void setNoDef(long noDef);
	/**
	 * @return 无视防御
	 */
	public long getNoDef();
	/**
	 * @param shangAdd	伤害加成
	 */
	public void setShangAdd(long shangAdd);
	/**
	 * @return 伤害加成
	 */
	public long getShangAdd();
	/**
	 * @param shangDef	伤害减免
	 */
	public void setShangDef(long shangDef);
	/**
	 * @return 伤害减免
	 */
	public long getShangDef();
	/**
	 * @param zengShang	增伤
	 */
	public void setZengShang(long zengShang);
	/**
	 * @return 增伤
	 */
	public long getZengShang();
	/**
	 * @param jianShang	减伤
	 */
	public void setJianShang(long jianShang);
	/**
	 * @return 减伤
	 */
	public long getJianShang();
	
	/**
	 * @return 防御 
	 */
	public long getDefense();
	
	/**
	 * @param defense 防御 
	 */
	public void setDefense(long defense);
	
	
	/**
	 * @return 闪避几率 x32
	 */
	public long getShanBi();
	
	/**
	 * @param shanBi 闪避几率 x32
	 */
	public void setShanBi(long shanBi);
	
	/**
	 * @return 命中几率x31
	 */
	public long getMingZhong();
	
	/**
	 * @param mingZhong 命中几率x31
	 */
	public void setMingZhong(long mingZhong);
	
	/**
	 * @return 暴击几率  x51
	 */
	public long getBaoJi();
	
	/**
	 * @param baoJi 暴击几率  x51
	 */
	public void setBaoJi(long baoJi);
	
	/**
	 * @return 暴击伤害 x53
	 */
	public long getBaoJi1();
	
	/**
	 * @param baoJi1 暴击伤害 x53
	 */
	public void setBaoJi1(long baoJi1);
	/**
	 * @return 抗暴几率 x54
	 */
	public long getKangBao();
	
	/**
	 * @param kangBao 抗暴几率 x54
	 */
	public void setKangBao(long kangBao);
	
	/**
	 * @return 移动 x101
	 */
	public long getSpeed();
	
	/**
	 * @param speed 移动 x101
	 */
	public void setSpeed(long speed);
	
	/**
	 * @return 战力
	 */
	public long getZhanLi();
	
	/**
	 * @param zhanLi 战力
	 */
	public void setZhanLi(long zhanLi);
	
	/**
	 * @return 暴击倍率 x52
	 */
	public long getBaoJi2();
	
	/**
	 * @param baoJi2 暴击倍率 x52
	 */
	public void setBaoJi2(long baoJi2);
	/**
	 * PK惩罚 x48
	 * @return
	 */
	public long getChengfa();
	
	/**
	 * PK惩罚 x48
	 * @param chengfa
	 */
	public void setChengfa(long chengfa);
	
	/**
	 * 设置当前HP
	 * @return
	 */
	public long getCurHp();
	
	/**
	 * 设置当前HP
	 * @param curHp
	 */
	public void setCurHp(long curHp);

	/**
	 * 重置血量
	 */
	public void resetHp();

	/**
	 * 受伤
	 * @param
	 */
	public void hurt(IHarm harm);
	
	/**
	 * 治疗
	 * @param
	 */
	public void heal(IHarm harm);

	/**
	 * @description 接受伤害
	 */
	void acceptHarm(IHarm harm);
	
	public long getLevel();
	
	
	/**
	 * 增加监听器
	 */
	public void addListener(IAttributeListener listener);
	
	/**
	 * 是否是满血
	 * @return true:是
	 */
	public boolean isFullHp();

	
    // ---------------------------------神武装备属性-----------------------------//
    /**
     * 抵消伤害概率
     */
    public long getDidangRate();

    public void setDidangRate(long didangRate);

    /**
     * 抵消伤害比例
     */
    public long getDidangVal();

    public void setDidangVal(long didangVal);

    /**
     * 抵消伤害内置CD
     */
    public long getDidangCd();

    public void setDidangCd(long didangCd);

    /**
     * 记录抵挡时间
     */
    public long getDidangTime();

    public void setDidangTime(long ddTime);

    /**
     * 反伤概率
     */
    public long getFanshangRate();

    public void setFanshangRate(long fanshangRate);
    /**
     * 反伤概率
     */
    public long getKangFanshangRate();
    
    public void setKangFanshangRate(long fanshangRate);

    /**
     * 反伤比例
     */
    public long getFanshangVal();

    public void setFanshangVal(long fanshangVal);

    /**
     * 反伤内置CD
     */
    public long getFanshangCd();

    public void setFanshangCd(long fanshangCd);

    /**
     * 记录反伤时间
     */
    public long getFanshangTime();

    public void setFanshangTime(long fsTime);

    // ----------------------------五行属性-----------------------------//

    /**
     * 获取五行金属性的攻击伤害加成比例
     */
    public long getWxGoldAddRate();

    public void setWxGoldAddRate(long wxGoldAddRate);

    /**
     * 获取五行金属性的攻击伤害减少比例
     */
    public long getWxGoldSubRate();

    public void setWxGoldSubRate(long wxGoldSubRate);

    /**
     * 获取五行木属性的攻击伤害加成比例
     */
    public long getWxWoodAddRate();

    public void setWxWoodAddRate(long wxWoodAddRate);

    /**
     * 获取五行木属性的攻击伤害减少比例
     */
    public long getWxWoodSubRate();

    public void setWxWoodSubRate(long wxWoodSubRate);

    /**
     * 获取五行土属性的攻击伤害加成比例
     */
    public long getWxEarthAddRate();

    public void setWxEarthAddRate(long wxEarthAddRate);

    /**
     * 获取五行木属性的攻击伤害减少比例
     */
    public long getWxEarthSubRate();

    public void setWxEarthSubRate(long wxEarthSubRate);

    /**
     * 获取五行水属性的攻击伤害加成比例
     */
    public long getWxWaterAddRate();

    public void setWxWaterAddRate(long wxWaterAddRate);

    /**
     * 获取五行水属性的攻击伤害减少比例
     */
    public long getWxWaterSubRate();

    public void setWxWaterSubRate(long wxWaterSubRate);

    /**
     * 获取五行火属性的攻击伤害加成比例
     */
    public long getWxFireAddRate();

    public void setWxFireAddRate(long wxFireAddRate);

    /**
     * 获取五行火属性的攻击伤害减少比例
     */
    public long getWxFireSubRate();

    public void setWxFireSubRate(long wxFireSubRate);
 
}
