package com.junyou.stage.model.prop;

import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.utils.common.ObjectUtil;

public class PropFormatHelper {
	
	private static final String SPLIT = ",";
	public static final String SPLIT2 = ";";
	
	public static String encodeOne(Object... objs){
		
		StringBuilder builder =  new StringBuilder();
		for( Object _obj : objs ){
			builder.append(SPLIT).append(_obj);
		}
		
		return builder.substring(1);
	}
	
	/**
	 * 解析
	 * @param strs [goodsId,remainValue,expireTime(不过期 -1)]
	 * @param role
	 * @return
	 */
	public static PropModel decodeAll(String strs, IRole role){
		PropModel model = new PropModel(role);
		
		if( !ObjectUtil.strIsEmpty(strs) ){
			String[] _strs = strs.split(SPLIT2);
			
			for( String str : _strs ){
				String[] props = str.split(SPLIT);
				String goodsId = props[0];
				
				GoodsConfig goodsConfig = StageConfigureHelper.getGoodsConfigExportService().loadById(goodsId);
				//如果物品不存在
				if( goodsConfig == null ){
					continue;
				}
				
				Long remainValue = null;
				Long exporeTime = null;
				if( props.length == 3 ){
					remainValue = Long.parseLong(props[1]);
					//若道具到期，则不添加至Model
					if(remainValue <= 0){
						continue;
					}
					exporeTime = Long.parseLong(props[2]);
				}
				
				//构建道具对象
				IProp prop = PropFactory.recover(role, goodsConfig,remainValue,exporeTime);
				
				model.add(prop);
			}
		}
		return model;
	}
	
}
