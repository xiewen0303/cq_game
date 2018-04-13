package com.junyou.bus.giftcard.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.giftcard.entity.GiftCard;
import com.junyou.bus.giftcard.entity.GiftCardPlatform;
import com.junyou.bus.giftcard.service.GiftCardService;


@Service
public class GiftCardExportService{
	@Autowired
	private GiftCardService giftCardService;
	
	/**
	 * 初始化
	 * @param userRoleId
	 * @return
	 */
	public List<GiftCard> initGiftCard(Long userRoleId) {
		return giftCardService.initGiftCard(userRoleId);
	}
	
	public List<GiftCardPlatform> initCardPlatform(Long userRoleId){
		return giftCardService.initGiftCardPlatform(userRoleId);
	}
}
