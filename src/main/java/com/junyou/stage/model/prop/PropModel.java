package com.junyou.stage.model.prop;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.stage.model.element.role.IRole;

public class PropModel {
	
	private Map<String, IProp> propMap;
	
	private Map<Integer,String> propType;
	
	@SuppressWarnings("unused")
	private IRole role;
	
	public PropModel(IRole role){
		this.role = role;
	}
	
	public IProp getPropByGoodsId(String goodsId){
		if( propMap == null ){
			return null;
		}
		return propMap.get(goodsId);
	}
	
	public IProp getPropByCategory(int category){
		if( propType == null ){
			return null;
		}
		String goodsId = propType.get(category);
		return propMap.get(goodsId);
	}
	/**
	 * 是否需要开启
	 * @param prop
	 * @return
	 */
	public boolean add(IProp prop){
		if( propMap == null ){
			propMap = new HashMap<String, IProp>();
			
			propType = new HashMap<Integer, String>();
		}
		
		//同类型覆盖(或叠加)
		if(propType.containsKey(prop.getCategory())){
			String oldGoodsId = propType.get(prop.getCategory());
			IProp oldProp = propMap.get(oldGoodsId);
			if(oldProp.isSameProp(prop)){//效果叠加
				oldProp.costValue(0);
				oldProp.addValue(prop.getRemainValue());
				oldProp.notifyPropChange();
				return false;
			}
			propMap.remove(oldGoodsId);
			oldProp.stop();
		}
		
		//新道具入缓存
		propMap.put(prop.getGoodsId() , prop);
		propType.put(prop.getCategory(),prop.getGoodsId());
		return true;
	}
	
	public void remove(IProp prop){
		propMap.remove(prop.getGoodsId());
		
		propType.remove(prop.getCategory());
	}
	
	/**
	 * 获取多倍卡时间的倍率
	 * @return 几倍(例:双倍 2)
	 */
	public float getExpPropManyBie(){
		
		if( propMap != null ){
			Collection<IProp> props = propMap.values();
			if( props.size() > 0 ){
				for( IProp prop : props ){
					if(prop.getCategory() == GoodsCategory.MANY_BEI_EXP){
						if(prop.getState()){
							return prop.getGoodsConfig().getData2();
						}else{
							break;
						}
					}
				}
			}
		}
		
		return 0;
	}

//	/**
//	 * 请求血魔时剩余量
//	 * @return
//	 */
//	public Long getHpProp(){
//		
//		if( propMap != null ){
//			Collection<IProp> props = propMap.values();
//			if( props.size() > 0 ){
//				for( IProp prop : props ){
//					
//					if(prop.getCategory() == GoodsCategory.HP_POOL_TYPE){
//						return prop.getRemainValue();
//					}
//				}
//			}
//		}
//		
//		return null;
//	}
	
	public Object getManyBeiExpProp(){
		if( propMap != null ){
			Collection<IProp> props = propMap.values();
			if( props.size() > 0 ){
				for( IProp prop : props ){
					
					if(prop.getCategory() == GoodsCategory.MANY_BEI_EXP){
						
						return new Object[]{prop.getGoodsConfig().getData2().intValue(),prop.getRemainValue()};
					}
				}
			}
		}
		
		return null;
	}
	
	
	
	public String format(){
		
		StringBuilder builder = new StringBuilder();
		
		if( propMap != null ){
			Collection<IProp> props = propMap.values();
			if( props.size() > 0 ){
				for( IProp prop : props ){
					String format = prop.formatStr();
					if( format != null ){
						builder.append(PropFormatHelper.SPLIT2).append(format);
					}
				}
			}
		}
		if( builder.length() == 0 ){
			return "";
		}
		return builder.substring(1);
	}
	
	public Object[] sendClientData(){
		Object[] result = null;
		
		if( propMap != null && propMap.size() > 0){
			result = new Object[propMap.size()];
			
			int index = 0;
			Collection<IProp> props = propMap.values();
			for( IProp prop : props ){
				result[index++] = prop.formatData();
			}
		}
		
		return result;
	}
	
	
	
	/**
	 * 停止所有的buff定时
	 */
	public void stop(){
		if( propMap != null && propMap.size() > 0 ){
			for( IProp prop : propMap.values() ){
				prop.stop();
			}
		}
	}
	/**
	 * 开启所有定时
	 */
	public void start(){
		if( propMap != null && propMap.size() > 0 ){
			for( IProp prop : propMap.values() ){
				prop.start();
			}
		}
	}
	
	/**
	 * 通知前端道具状态
	 */
	public void noticeClient(){
		if( propMap != null && propMap.size() > 0 ){
			for( IProp prop : propMap.values() ){
				prop.notifyPropChange();
			}
		}
	}
}
