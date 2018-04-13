package com.junyou.stage.model.prop;

import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.stage.model.element.role.IRole;

public class PropFactory {

	public static IProp create(IRole role,GoodsConfig goodsConfig,int count){
		
		IProp prop = null;
		
		if(GoodsCategory.MANY_BEI_EXP == goodsConfig.getCategory()) {
			prop = new ExpProp(role, goodsConfig,count);
		}else if(GoodsCategory.HP_POOL_TYPE == goodsConfig.getCategory()){
			prop = new HpProp(role,goodsConfig,count);
		}
			
		return prop;
	}
	
	
	public static IProp recover(IRole role,GoodsConfig goodsConfig,Long remainValue,Long expireTime){
		
		IProp prop = null;
		
		if(GoodsCategory.MANY_BEI_EXP == goodsConfig.getCategory()) {
			prop = new ExpProp(role, goodsConfig,remainValue);
		}else if(GoodsCategory.HP_POOL_TYPE == goodsConfig.getCategory()){
			prop = new HpProp(role,goodsConfig,remainValue);
		}
		
		return prop;
	}
	
}
