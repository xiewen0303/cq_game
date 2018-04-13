package com.junyou.stage.model.prop;

import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.stage.model.element.role.IRole;

public class HpProp extends DefaultPropPool{
	/**回复间隔*/
	private int interval;
	
	public HpProp(IRole _role, GoodsConfig goodsConfig,int count) {
		super(_role, goodsConfig,count);
		
	}
	
	public HpProp(IRole _role, GoodsConfig goodsConfig,long _remainValue) {
		super(_role, goodsConfig,1);
		setRemainValue(_remainValue);
	}
	
	@Override
	public void initValue(int count) {
		long time = getGoodsConfig().getData1() * 60 * count;
		setRemainValue(time);
		setTotalValue(time);
		init();
	}
	
	@Override
	public boolean costValue(int costValue){
		return super.costValue(interval);
	}
	
	private void init(){
		interval = getGoodsConfig().getData2().intValue();
		if(interval < 5){
			interval = 5;
		}
	}

	@Override
	protected long getInnerInterval() {
		return interval * 1000L;
	}

	@Override
	protected Short getInnerCommand() {
		return InnerCmdType.PROP_EFFECT_HP;
	}
	
	public void notifyPropChange(){
		BusMsgSender.send2One(getOwner().getId(),  ClientCmdType.GET_ITEM_XUEBAO_INFO,new Object[]{getGoodsConfig().getId(),getRemainValue() * 1000});
	}

	@Override
	public boolean isSameProp(IProp prop) {
		String eff1 = getGoodsConfig().getData2() + "_" + getGoodsConfig().getData3() + "_" + getGoodsConfig().getData4();
		String eff2 = prop.getGoodsConfig().getData2() + "_" + prop.getGoodsConfig().getData3() + "_" + prop.getGoodsConfig().getData4();
		return eff1.equals(eff2);
	}
}
