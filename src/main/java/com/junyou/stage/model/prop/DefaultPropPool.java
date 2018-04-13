package com.junyou.stage.model.prop;

import java.util.concurrent.TimeUnit;

import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.kuafu.manager.KuafuManager;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.utils.KuafuConfigPropUtil;

public abstract class DefaultPropPool implements IProp {

	private GoodsConfig goodsConfig;
	
	private IRole role;
	
	private long remainValue;
	private long useValue;
	private long totalValue;
	
	//过期时间
	private long expireTime;
	
	protected boolean runState = false;//使用状态，是否暂停（false：暂停）

	
	
	public DefaultPropPool(IRole _role,GoodsConfig goodsConfig,int count) {
		this.role = _role;
		this.goodsConfig = goodsConfig;
		initValue(count);
	}
	
	/**
	 * 初始化值
	 */
	public abstract void initValue(int count);
	
	
	
	
	protected void setRemainValue(long remainValue) {
		this.remainValue = remainValue;
	}

	protected void setTotalValue(long totalValue) {
		this.totalValue = totalValue;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	@Override
	public void add() {
		totalValue += goodsConfig.getData1();
		remainValue += goodsConfig.getData1();
	}

	public void start() {
		runState = true;
		StageTokenRunable runnable = new StageTokenRunable(role.getId(), role.getStage().getId(), getInnerCommand(), getInnerData());
		if(!KuafuManager.kuafuIng(role.getId()) || KuafuConfigPropUtil.isKuafuServer()){
			role.getScheduler().schedule(role.getId().toString(), goodsConfig.getId(), runnable, getInnerInterval(), TimeUnit.MILLISECONDS);
		}
	}
	
	/**
	 * 定时间隔，单位毫秒
	 * @return
	 */
	protected abstract long getInnerInterval();
	
	protected Object getInnerData(){
		return null;
	}
	
	protected abstract Short getInnerCommand();
	
	public void stop() {
		if(!runState){
			return;
		}
		role.getScheduler().cancelSchedule(role.getId().toString(), goodsConfig.getId());
		runState = false;
	}
	
	public void end() {
//		StageMsgSender.send2One(role.getId(),  StagePropCommands.PROP_REMAIN_CHANGE,new Object[]{goodsConfig.getId1(),0});
	}
	
	public void notifyPropChange(){
//		StageMsgSender.send2One(role.getId(),  StagePropCommands.PROP_REMAIN_CHANGE,new Object[]{goodsConfig.getId1(),remainValue});
	}
	
	public String formatStr(){
		StringBuffer buff = new StringBuffer();
		buff.append(getGoodsId()).append(",").append(getRemainValue()).append(",").append(getExpireTime());
		return buff.toString();
	}
	

	public Object[] formatData(){
		return new Object[]{goodsConfig.getId1(),remainValue};
	}
	
	public IRole getOwner(){
		return role;
	}
	
	public void setOwner(IRole role){
		this.role = role;
	}
	
	
	public boolean costValue(int costValue){
		boolean change = false;
		remainValue = remainValue - costValue;
		if(remainValue <= 0){
			remainValue = 0;
			change = true;
		}
		return change;
	}
	
	@Override
	public String getGoodsId() {
		return goodsConfig.getId();
	}


	public int getCategory(){
		return goodsConfig.getCategory();
	}
	
	public GoodsConfig getGoodsConfig() {
		return goodsConfig;
	}

	@Override
	public long getRemainValue() {
		return remainValue;
	}
	
	protected long getExpireTime(){
		return expireTime;
	}
	
	public long getUseValue(){
		return useValue;
	}
	
	public long getTotalValue(){
		return totalValue;
	}
	
	public boolean getState(){
		return runState;
	}
	
	public void restart() {
		runState = true;
		start();
	}
	
	public void addValue(long add){
		remainValue += add;
		totalValue += add;
	}
	
	
}
