package com.junyou.bus.happycard.service.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.happycard.entity.RefabuHappyCard;
import com.junyou.bus.happycard.entity.RefabuHappyCardItem;
import com.junyou.bus.happycard.service.HappyCardService;
import com.junyou.log.ChuanQiLog;

@Service
public class HappyCardExportService {
	@Autowired
	private HappyCardService happyCardService;

	public List<RefabuHappyCardItem> initRefabuHappyCardItem(Long userRoleId) {
		return happyCardService.initRefabuHappyCardItem(userRoleId);
	}

	public List<RefabuHappyCard> initRefabuHappyCard(Long userRoleId) {
		return happyCardService.initRefabuHappyCard(userRoleId);
	}

	public Object[] getInfo(Long userRoleId, int subId) {
		return happyCardService.getInfo(userRoleId, subId);
	}

	public Object[] getInfo2(Long userRoleId, int subId) {
		return happyCardService.getInfo2(userRoleId, subId);
	}
	
	public void onlineHandle(Long userRoleId){
		try{
			happyCardService.onlineHandle(userRoleId);
		}catch (Exception e) {
			ChuanQiLog.error("上线业务异常，",e);
		}
	}
}
