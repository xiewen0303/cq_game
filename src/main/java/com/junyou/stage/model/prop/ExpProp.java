package com.junyou.stage.model.prop;

import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.utils.datetime.GameSystemTime;

public class ExpProp extends DefaultPropPool{
	
	/**计时开始时间*/
	private long startTime;
	
	public ExpProp(IRole _role, GoodsConfig goodsConfig,int count) {
		super(_role, goodsConfig,count);
		startTime = GameSystemTime.getSystemMillTime();
	}
	
	public ExpProp(IRole _role, GoodsConfig goodsConfig,long _remainValue) {
		super(_role, goodsConfig,1);
		setRemainValue(_remainValue);
		startTime = GameSystemTime.getSystemMillTime();
	}
	
	@Override
	public void initValue(int count) {
		long time = getGoodsConfig().getData1() * 60 * count;
		setRemainValue(time * 1000);
		setTotalValue(time * 1000);
	}
	
	@Override
	protected long getInnerInterval() {
		return getRemainValue();
	}

	@Override
	protected Short getInnerCommand() {
		return InnerCmdType.PROP_EFFECT_EXP_OVER;
	}

	@Override
	public void start() {
		if(getState()){//已经开启了
			return;
		}
		startTime = GameSystemTime.getSystemMillTime();
		super.start();
	}
	
	@Override
	public void stop() {
		if(!getState()){//已经停掉了
			return;
		}
		long cur = GameSystemTime.getSystemMillTime();
		Long cost = cur - startTime;
		startTime = cur;
		if(cost < 1){
			cost = 1l;
		}
		super.costValue(cost.intValue());
		super.stop();
	}

	@Override
	public String formatStr() {
		costValue(0);
		return super.formatStr();
	}
	
	@Override
	public boolean costValue(int costValue){
		long cur = GameSystemTime.getSystemMillTime();
		Long cost = cur - startTime;
		startTime = cur;
		return super.costValue(cost.intValue());
	}
	
	public void notifyPropChange(){
		BusMsgSender.send2One(getOwner().getId(), ClientCmdType.GET_ITEM_DUOBEI_INFO , new Object[]{getGoodsConfig().getData2() + 1,getRemainValue()});
	}

	@Override
	public boolean isSameProp(IProp prop) {
		return getGoodsConfig().getData2().equals(prop.getGoodsConfig().getData2());
	}

}
