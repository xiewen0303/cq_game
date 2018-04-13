package com.junyou.bus.email.utils;

import java.util.Date;

import com.junyou.bus.email.entity.EmailRelation;
import com.junyou.constants.GameConstants;
import com.junyou.public_.email.entity.Email;
import com.junyou.utils.common.ObjectUtil;

public class EmailOutPutWarpper {
	
	public static Object[] email2InfoVo(Email email,EmailRelation emailRelation){
		/**
		 * 	0  Number(guid)
			1  Date(date 过期时间)
			2  String(content 内容)
			3  Array(attachment 附件 按[0:Map(#ItemVO),1:Map(#ItemVO)]
		 */
		return new Object[]{emailRelation.getId(),
				new Date(email.getExpireTime()),
				email.getContent(),
				getAttArray(email.getAttachment())
		};
	}
	
	public static Object[] email2TitleInfoVo(Email email,EmailRelation emailRelation){
		/**
		 * 	0  Number(guid)
			1  Date(date 发送邮件时间)
			2  String(标题 内容)
			3  int(邮件领取状态1未读，2已读，3已领取)
			4  int(是否显示附件0不显示 1显示)
		 */
		int showFuJian = 0;
		if(!ObjectUtil.strIsEmpty(email.getAttachment()) && emailRelation.getStatus() != GameConstants.EMAIL_STATUS_YILINGQU){
			showFuJian = 1;
		}
		return new Object[]{
				emailRelation.getId(),
				new Date(email.getCreateTime().getTime()),
				email.getTitle(),
				emailRelation.getStatus(),
				showFuJian
		};
	}
	
	private static Object[] getAttArray(String attStr){
		if(ObjectUtil.strIsEmpty(attStr)){
			return null;
		}else{
			String[] attInfo = attStr.split(GameConstants.FUJIAN_SPLIT_ONE);
			Object[] ret = new Object[attInfo.length];
			for (int i = 0; i < ret.length; i++) {
				String[] info = attInfo[i].split(GameConstants.FUJIAN_SPLIT_TWO);
				/**
				 * 	0 String 物品ID(道具表/装备表ID字段) 
					1 Number 物品GUID(物品全局唯一标识） 
					2 int 物品slot槽位值 
					3 int 物品数量(如果不传，代表有1个这个物品) 
					4 Number 物品过期时间戳 
				 */
				Object[] itemVo = new Object[6];
				itemVo[0] = info[0];
				itemVo[3] = Integer.parseInt(info[1]);
				if(info.length > 2){
					itemVo[5] = Integer.parseInt(info[2]);
				}
				ret[i] = itemVo;
			}
			return ret;
		}
	}
}
