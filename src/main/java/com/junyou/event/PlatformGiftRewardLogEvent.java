package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.log.LogPrintHandle;

/**
 * 平台礼包领取
 * 
 * @author lxn
 * @date 2015-6-5
 */
public class PlatformGiftRewardLogEvent extends AbsGameLogEvent {
 
	private static final long serialVersionUID = 1L;

	private long userRoleId;
	private String platformid; // 平台id
	private int level; // 等级
	private int giftTyeNum; // 具体礼包类型
	private JSONArray item;// 获得物品

	private String giftType; // 领取的是那种类型的礼包
	
	public PlatformGiftRewardLogEvent(long userRoleId, JSONArray item, String platformId, int position, String giftType) {
		super(LogPrintHandle.PLATFORM_GIFT);
		this.userRoleId = userRoleId;
		this.item = item;
		this.platformid = platformId;
		this.giftType = giftType;
		updateGiftType(giftType, position);
	}

	/**
	 * 加了平台礼包就要在这加上对应的日志信息
	 * 
	 * @param platformId
	 * @param position 从0开始计算的
	 */
	private void updateGiftType(String giftType, int position) {
		switch (giftType) {

		case PlatformPublicConfigConstants.SOGOU_PIFU_LB:
			this.giftTyeNum = LogPrintHandle.PLATFORM_GIFT_SOGOU_PIFU;
			break;
		case PlatformPublicConfigConstants.SOGOU_DATING_LB:
			this.giftTyeNum = LogPrintHandle.PLATFORM_GIFT_SOGOU_DATING;
			break;

		case PlatformPublicConfigConstants._37_LEVEL_LB:
			this.level = position + 1;
			this.giftTyeNum = LogPrintHandle.PLATFORM_GIFT_37WAN_VIP;
			break;
		case PlatformPublicConfigConstants._37_LINGPAI_LB:
			this.giftTyeNum = LogPrintHandle.PLATFORM_GIFT_37WAN_LINGPAI;
			break;

		case PlatformPublicConfigConstants._360_JIASUQIU_LB:
			this.giftTyeNum = LogPrintHandle.PLATFORM_GIFT_360_JIASUQIU;

			break;
		case PlatformPublicConfigConstants._360_YOUXIDATING_LB:
			this.giftTyeNum = LogPrintHandle.PLATFORM_GIFT_360_DATING;
			break;
		case PlatformPublicConfigConstants._360_TEQUAN_LB:
			this.giftTyeNum = LogPrintHandle.PLATFORM_GIFT_360_Tequan;
			this.level  = position;
			break;
		case PlatformPublicConfigConstants._360_TEQUAN_SHARE_LB:
			this.giftTyeNum = LogPrintHandle.PLATFORM_GIFT_360_Tequan_Share_LB;
			break;
			
		case PlatformPublicConfigConstants.XUNLEI_VIP_LB:
			this.level = position - 1;
			this.giftTyeNum = LogPrintHandle.PLATFORM_GIFT_XUNLEI_VIP;
			break;

		case PlatformPublicConfigConstants.XUNLEI_BOX_LB:
			this.giftTyeNum = LogPrintHandle.PLATFORM_GIFT_XUNLEI_DATING;
			break;
		case PlatformPublicConfigConstants.XUNLEI_TEQUAN_LB:
			this.giftTyeNum = LogPrintHandle.PLATFORM_GIFT_XUNLEI_TEQUAN;
			break;
		case PlatformPublicConfigConstants.SHUNWANG_VIP_LB:
			this.level = position - 1;
			this.giftTyeNum = LogPrintHandle.PLATFORM_GIFT_SWJOY_VIP_LB;
			break;

		}
	}

	public String getGiftType() {
		return giftType;
	}

	public void setGiftType(String giftType) {
		this.giftType = giftType;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getGiftTyeNum() {
		return giftTyeNum;
	}
	public void setGiftTyeNum(int giftTyeNum) {
		this.giftTyeNum = giftTyeNum;
	}

	public String getPlatformid() {
		return platformid;
	}

	public void setPlatformid(String platformid) {
		this.platformid = platformid;
	}

	public long getUserRoleId() {
		return userRoleId;
	}

	public JSONArray getItem() {
		return item;
	}

	public void setItem(JSONArray item) {
		this.item = item;
	}

	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}

}