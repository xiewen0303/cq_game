package com.junyou.bus.giftcard.filter;

import com.junyou.bus.giftcard.entity.GiftCardPlatform;
import com.kernel.data.dao.IQueryFilter;

/**
 * 数据过滤器
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-1-31 下午6:15:04 
 */
public class GiftCardPlatformFilterCardno implements IQueryFilter<GiftCardPlatform>{

	private String cardno;
	
	public GiftCardPlatformFilterCardno(String cardno){
		this.cardno = cardno;
	}
	@Override
	public boolean check(GiftCardPlatform entity) {
		if(entity.getCardno().equals(cardno)){
			return true;
		}
		return false;
	}

	@Override
	public boolean stopped() {
		return false;
	}

}
