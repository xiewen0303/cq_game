/**
 * 
 */
package com.junyou.stage.model.core.attribute;

import java.util.concurrent.ConcurrentHashMap;

import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.InnerCmdType;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.hundun.entity.HundunStage;
import com.junyou.stage.model.core.attributeformula.AttributeFormulaSearch;
import com.junyou.stage.model.core.attributeformula.IAttributeFormula;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.skill.harm.HarmType;
import com.junyou.utils.lottery.Lottery;
import com.junyou.utils.lottery.MathUtils;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-2下午4:29:41
 */
public abstract class AbsFightAttribute implements IFightAttribute {

	private long curHp = 0,maxHp = 0,attack = 0,mingZhong = 0,defense = 0,chengfa = 0;
	private long baoJi1 = 0,speed = 0,zhanLi = 0,shanBi = 0,baoJi = 0,baoJi2 = 0,kangBao = 0;
	private long noDef = 0,shangAdd = 0,shangDef = 0,zengShang = 0,jianShang = 0;
	private long fuhuoTime = 0; 
	private long didangRate = 0, didangVal = 0, didangCd = 0, didangTime = 0;
	private long fanshangRate = 0,kangfanshangRate = 0, fanshangVal = 0, fanshangCd = 0, fanshangTime = 0;
	private long wxGoldAddRate = 0, wxGoldSubRate = 0, wxWoodAddRate = 0, wxWoodSubRate = 0, wxEarthAddRate = 0, 
	             wxEarthSubRate = 0, wxWaterAddRate = 0, wxWaterSubRate = 0, wxFireAddRate = 0, wxFireSubRate = 0;
	/**
	 * 改变属性影响的属性公式
	 */
	private ConcurrentHashMap<String,IAttributeFormula> referenceFormulas = new ConcurrentHashMap<String, IAttributeFormula>();
	
	/**
	 * 属性监听
	 */
	private FightAttributeListenerHelper fightAttributeListenerHelper = new FightAttributeListenerHelper();
	
	@Override
	public void addListener(IAttributeListener listener) {
		fightAttributeListenerHelper.addListener(listener);
	}

	/**
	 * 刷新发生改变的属性值
	 */
	public void refresh() {
		
		if(referenceFormulas.size() == 0) return;
		
		for(IAttributeFormula tmp : referenceFormulas.values()){
			tmp.refreshAttribute(this);
		}
		
		referenceFormulas.clear();
	}
	
	/**
	 * 保存值发生改变的attributeType
	 * @param attributeType {@link EffectType}
	 */
	protected void addChangedBaseAttribute(String attributeType){
		
		IAttributeFormula formula =	AttributeFormulaSearch.findReference(attributeType);
		if (null != formula) {			
			referenceFormulas.put(formula.getClass().getSimpleName(), formula);
		}
	}


	protected IAttributeListener getAttributeListener() {
		return fightAttributeListenerHelper;
	}
	
	@Override
	public long getMaxHp() {
		
		return MathUtils.max(maxHp, 0);
	}
	
	@Override
	public long getKangBao(){
		return MathUtils.max(kangBao,0);
	}
	
	@Override
	public long getShanBi(){
		return MathUtils.max(shanBi,0);
	}
	
	@Override
	public long getMingZhong(){
		return MathUtils.max(mingZhong,0);
	}
	
	@Override
	public long getBaoJi(){
		return MathUtils.max(baoJi,0);
	}
	
	@Override
	public long getBaoJi1(){
		return MathUtils.max(baoJi1,0);
	}
	
	@Override
	public long getBaoJi2(){
		return MathUtils.max(baoJi2,0);
	}
	
	@Override
	public long getSpeed(){
		return MathUtils.max(speed,0);
	}
	
	@Override
	public long getZhanLi(){
		return MathUtils.max(zhanLi,0);
	}

	@Override
	public void setShangAdd(long shangAdd) {
		this.shangAdd = shangAdd;
	}

	@Override
	public long getShangAdd() {
		return shangAdd;
	}

	@Override
	public void setShangDef(long shangDef) {
		this.shangDef = shangDef;
	}

	@Override
	public long getShangDef() {
		return shangDef;
	}

	@Override
	public void setZengShang(long zengShang) {
		this.zengShang = zengShang;
	}

	@Override
	public long getZengShang() {
		return zengShang;
	}

	@Override
	public void setJianShang(long jianShang) {
		this.jianShang = jianShang;
	}

	@Override
	public long getJianShang() {
		return jianShang;
	}

	@Override
	public void setMaxHp(long maxHp) {
		this.maxHp = maxHp;
		getAttributeListener().setMaxHp(maxHp);
	}
	
	@Override
	public long getChengfa() {
		return chengfa;
	}
	
	@Override
	public void setChengfa(long chengfa) {
		this.chengfa = chengfa;
		getAttributeListener().setChengfa(chengfa);
	}

	@Override
	public void setKangBao(long kangBao){
		this.kangBao = kangBao;
		getAttributeListener().setKangBao(kangBao);
	}
	
	@Override
	public void setShanBi(long shanBi){
		this.shanBi = shanBi;
		getAttributeListener().setShanBi(shanBi);
	}
	
	@Override
	public void setMingZhong(long mingZhong){
		this.mingZhong = mingZhong;
		getAttributeListener().setMingZhong(mingZhong);
	}
	
	@Override
	public void setBaoJi(long baoJi){
		this.baoJi = baoJi;
		getAttributeListener().setBaoji(baoJi);
	}
	
	@Override
	public void setSpeed(long speed){
		this.speed = speed;
		getAttributeListener().setSpeed(speed);
	}
	
	@Override
	public void setZhanLi(long zhanLi){
		this.zhanLi = zhanLi;
		getAttributeListener().setZhanLi(zhanLi);
	}
	
	@Override
	public long getCurHp() {
		
		return curHp;
	}

	@Override
	public void setCurHp(long curHp) {
		this.curHp = curHp;
		if (this.curHp > getMaxHp()) {
			this.curHp = getMaxHp();
		}
		
		getAttributeListener().setCurHp(curHp);
	}
	
	@Override
	public void hurt(IHarm harm) {
		long d = harm.getVal();
		fightAttributeListenerHelper.hurt(harm);
		
		/**
		 * 伤害结算
		 *   乙当前生命值为Hp
		 *   Hp = max(0, Hp - finalDMG)  如果Hp = 0，乙死亡
		 */
		long hp = MathUtils.max(0, curHp - d);
		setCurHp(hp);
		
		if(!getFighter().getStateManager().isDead() && curHp == 0){
			if(checkFuHuo(getFighter())){
				float fhhp = getFighter().getFightAttribute().getEffectAttribute(EffectType.x53.name());
				long fhcd = getFighter().getFightAttribute().getEffectAttribute(EffectType.x57.name()) * 1000;
				long time = System.currentTimeMillis();
				long fhTime = getFuhuoTime();
				if(time - fhTime >= fhcd){
					setFuHuoTime(time);

					fhhp = fhhp / 10000;//配置表填的是放大1W倍的,拿到之后先放小1W倍
					long sheng = (long) (getMaxHp() * fhhp);
					if(sheng > getMaxHp()){
						setCurHp(getMaxHp());
					}else{
						setCurHp(sheng);
					}
					BusMsgSender.send2Stage(getFighter().getId(), InnerCmdType.ZHUANGBEI_FUHOU,null);
				}else{
					getFighter().deadHandle(harm);
					if(getFighter().getStage() != null && getFighter().getStage().getStageType() == StageType.CHAOS){
						try{
							if(ElementType.isRole(harm.getAttacker().getElementType())){
								HundunStage stage = (HundunStage)getFighter().getStage();
								stage.addRoleJifen(harm.getAttacker().getId(), getFighter().getElementType(), harm.getAttacker().getName());
							}
						}catch (Exception e) {
							ChuanQiLog.error("混沌战场死亡处理异常。");
						}
					}
				}	
			}else{
				getFighter().deadHandle(harm);
				if(getFighter().getStage() != null && getFighter().getStage().getStageType() == StageType.CHAOS){
					try{
						if(ElementType.isRole(harm.getAttacker().getElementType())){
							HundunStage stage = (HundunStage)getFighter().getStage();
							stage.addRoleJifen(harm.getAttacker().getId(), getFighter().getElementType(), harm.getAttacker().getName());
						}
					}catch (Exception e) {
						ChuanQiLog.error("混沌战场死亡处理异常。");
					}
				}
			}
			
		}
	}
	
	/**
	 * 死亡时是否触发复活
	 * @param target   被攻击方
	 * @return true:复活
	 */
	private boolean checkFuHuo(IFighter target) {
		//复活几率
		float x52 = target.getFightAttribute().getEffectAttribute(EffectType.x52.name());
		x52 = x52 / 10000;//配置表填的是放大1W倍的,拿到之后先放小1W倍
		if(x52 > 0 && Lottery.roll(x52, Lottery.TENTHOUSAND)){
			return true;
		}
		return false;
	}
	
	@Override
	public void heal(IHarm harm) {
		
		long curHp = getCurHp();
		long maxHp = getMaxHp();
		long reslt = curHp + harm.getVal();
		if (reslt > maxHp) {
			reslt = maxHp;
		}
		setCurHp(reslt);
		
//		getFighter().getFightStatistic().addHarm(harm);
		fightAttributeListenerHelper.heal(harm);
	}

	@Override
	public void acceptHarm(IHarm harm) {
		if(HarmType.isZhiLiaoHarmType(harm.getType())){
			heal(harm);
		}else{
			hurt(harm);
		}
	}
	
	protected abstract IFighter getFighter();

	@Override
	public void resetHp() {
		setCurHp(getMaxHp());
	}

	@Override
	public long getLevel() {
		return getFighter().getLevel();
	}
	
	/**
	 * 重置血量和灵力
	 */
	public void resetHpMp() {
		setCurHp(getMaxHp());
	}
	
	public boolean isFullHp(){
		return getCurHp() == getMaxHp();
	}

	@Override
	public long getAttack() {
		return MathUtils.max(attack, 0);
	}
	
	@Override
	public long getNoDef() {
		return MathUtils.max(noDef, 0);
	}
	
	@Override
	public void setNoDef(long noDef) {
		this.noDef = noDef;
	}

	@Override
	public void setAttack(long attack) {
		this.attack = attack;
		getAttributeListener().setAttack(attack);
	}

	@Override
	public long getDefense() {
		return MathUtils.max(defense, 0);
	}

	@Override
	public void setDefense(long defense) {
		this.defense = defense;
		getAttributeListener().setDefense(defense);
	}

	@Override
	public void setBaoJi1(long baoJi1) {
		this.baoJi1 = baoJi1;
	}

	@Override
	public void setBaoJi2(long baoJi2) {
		this.baoJi2 = baoJi2;
	}
	
	public long getFuhuoTime(){
		return fuhuoTime;
	}
	
	public void setFuHuoTime(long time){
		this.fuhuoTime = time;
	}

    public long getDidangRate() {
        return didangRate;
    }

    @Override
    public void setDidangRate(long didangRate) {
        this.didangRate = didangRate;
    }

    @Override
    public long getDidangVal() {
        return didangVal;
    }

    @Override
    public void setDidangVal(long didangVal) {
        this.didangVal = didangVal;
    }

    @Override
    public long getDidangCd() {
        return didangCd;
    }

    @Override
    public void setDidangCd(long didangCd) {
        this.didangCd = didangCd;
    }

    @Override
    public long getDidangTime() {
        return this.didangTime;
    }

    @Override
    public void setDidangTime(long ddTime) {
        this.didangTime = ddTime;
    }

    @Override
    public long getFanshangRate() {
        return fanshangRate;
    }

    @Override
    public void setFanshangRate(long fanshangRate) {
        this.fanshangRate = fanshangRate;
    }
    @Override
    public long getKangFanshangRate() {
    	return kangfanshangRate;
    }
    
    @Override
    public void setKangFanshangRate(long kangfanshangRate) {
    	this.kangfanshangRate = kangfanshangRate;
    }

    @Override
    public long getFanshangVal() {
        return fanshangVal;
    }

    @Override
    public void setFanshangVal(long fanshangVal) {
        this.fanshangVal = fanshangVal;
    }

    @Override
    public long getFanshangCd() {
        return fanshangCd;
    }

    @Override
    public void setFanshangCd(long fanshangCd) {
        this.fanshangCd = fanshangCd;
    }

    @Override
    public long getFanshangTime() {
        return fanshangTime;
    }

    @Override
    public void setFanshangTime(long fanshangTime) {
        this.fanshangTime = fanshangTime;
    }

    @Override
    public long getWxGoldAddRate() {
        return wxGoldAddRate;
    }

    @Override
    public void setWxGoldAddRate(long wxGoldAddRate) {
        this.wxGoldAddRate = wxGoldAddRate;
    }

    @Override
    public long getWxGoldSubRate() {
        return wxGoldSubRate;
    }

    @Override
    public void setWxGoldSubRate(long wxGoldSubRate) {
        this.wxGoldSubRate = wxGoldSubRate;
    }

    @Override
    public long getWxWoodAddRate() {
        return wxWoodAddRate;
    }

    @Override
    public void setWxWoodAddRate(long wxWoodAddRate) {
        this.wxWoodAddRate = wxWoodAddRate;
    }

    @Override
    public long getWxWoodSubRate() {
        return wxWoodSubRate;
    }

    @Override
    public void setWxWoodSubRate(long wxWoodSubRate) {
        this.wxWoodSubRate = wxWoodSubRate;
    }

    @Override
    public long getWxEarthAddRate() {
        return wxEarthAddRate;
    }

    @Override
    public void setWxEarthAddRate(long wxEarthAddRate) {
        this.wxEarthAddRate = wxEarthAddRate;
    }

    @Override
    public long getWxEarthSubRate() {
        return wxEarthSubRate;
    }

    @Override
    public void setWxEarthSubRate(long wxEarthSubRate) {
        this.wxEarthSubRate = wxEarthSubRate;
    }

    @Override
    public long getWxWaterAddRate() {
        return wxWaterAddRate;
    }

    @Override
    public void setWxWaterAddRate(long wxWaterAddRate) {
        this.wxWaterAddRate = wxWaterAddRate;
    }

    @Override
    public long getWxWaterSubRate() {
        return wxWaterSubRate;
    }

    @Override
    public void setWxWaterSubRate(long wxWaterSubRate) {
        this.wxWaterSubRate = wxWaterSubRate;
    }

    @Override
    public long getWxFireAddRate() {
        return wxFireAddRate;
    }

    @Override
    public void setWxFireAddRate(long wxFireAddRate) {
        this.wxFireAddRate = wxFireAddRate;
    }

    @Override
    public long getWxFireSubRate() {
        return wxFireSubRate;
    }

    @Override
    public void setWxFireSubRate(long wxFireSubRate) {
        this.wxFireSubRate = wxFireSubRate;
    }
 
}
