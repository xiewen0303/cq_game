package com.junyou.gameconfig.constants;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;


/**   
 *
 * @author LiNing  
 * @version  2014-1-13 上午11:38:30  
 * 
 */
public class ItemType {
	/**
	 * 1：场景中怪物掉落
	 */
	public static final int ITEM_STATE_MONSTER = 1;
	
	/**
	 * 2：神器积分兑换
	 */
	public static final int ITEM_SHENQI_DH = 2;
	
	/**
	 * 3：寻宝
	 */
	public static final int ITEM_XUBAO = 3;
	
	/**
	 * 4：签到奖励
	 */
	public static final int ITEM_SIGN = 4;
	
	/**
	 * 5：在线奖励
	 */
	public static final int ITEM_ONLINE = 5;
	
	/**
	 * 6：等级礼包
	 */
	public static final int ITEM_LEVEL_LIBAO = 6;
	
	/**
	 * 7：激活码兑奖
	 */
	public static final int ITEM_GIFT_CODE = 7;
	
	/**
	 * 8：首充礼包
	 */
	public static final int ITEM_FIRST_RECHARGE = 8;
	
	/**
	 * 9：七日登陆礼包
	 */
	public static final int ITEM_QIRI_LOGIN = 9;
	
	/**
	 * 10：开服活动 -- 疯狂冲刺（等级礼包）
	 */
	public static final int ITEM_KAIFU_LEVEL = 10;
	
	/**
	 * 11：开服活动 -- 炫酷坐骑
	 */
	public static final int ITEM_KAIFU_ZUOQI = 11;
	
	/**
	 * 12：开服活动 -- 城主
	 */
	public static final int ITEM_KAIFU_CHENGZHU = 12;
	
	/**
	 * 13：开服活动 -- 翅膀
	 */
	public static final int ITEM_KAIFU_CHIBANG = 13;
	
	/**
	 * 14：开服活动 -- VIP
	 */
	public static final int ITEM_KAIFU_VIP = 14;
	
	/**
	 * 15：开服活动 -- 战力
	 */
	public static final int ITEM_KAIFU_ZHANLI = 15;
	
	/**
	 * 16：主线任务
	 */
	public static final int ITEM_ZX_TASK = 16;
	
	/**
	 * 17：日常任务 -- 除妖任务
	 */
	public static final int ITEM_CHUYAO_TASK = 17;
	
	/**
	 * 18：日常任务 -- 擒贼擒王任务
	 */
	public static final int ITEM_BOSS_TASK = 18;
	
	/**
	 * 19：爬塔任务 -- 通天塔
	 */
	public static final int ITEM_PATA_TTT = 19;
	
	/**
	 * 20：爬塔任务 -- 镇魔塔
	 */
	public static final int ITEM_PATA_ZMT = 20;
	
	/**
	 * 21：累积充值活动 -- 优惠折扣
	 */
	public static final int ITEM_LJ_BUYGOODS = 21;
	
	/**
	 * 22：累积充值活动 -- 充值好礼
	 */
	public static final int ITEM_LJ_RECHARGE = 22;
	
	/**
	 * 23：累积充值活动 -- 光翼特惠
	 */
	public static final int ITEM_LJ_GUANGYI = 23;
	
	/**
	 * 24：累积充值活动 -- 超值兑换
	 */
	public static final int ITEM_LJ_CZDH = 24;
	
	/**
	 * 25：累积充值活动 -- 坐骑特惠
	 */
	public static final int ITEM_LJ_ZUOQI = 25;
	
	/**
	 * 26：累积充值活动 -- 武魂特惠
	 */
	public static final int ITEM_LJ_WUHUN = 26;
	
	/**
	 * 27：累积充值活动 -- 宝石特惠
	 */
	public static final int ITEM_LJ_BAOSHI = 27;
	
	/**
	 * 28：邮件附件
	 */
	public static final int ITEM_EMAIL = 28;
	
	/**
	 * 29：装备升阶
	 */
	public static final int ITEM_EQUIPSJ = 29;
	
	/**
	 * 30：NPC商城
	 */
	public static final int ITEM_NPC_MALL = 30;
	
	/**
	 * 31：商城
	 */
	public static final int ITEM_MALL = 31;
	
	/**
	 * 32：开服活动 -- 官印
	 */
	public static final int ITEM_KAIFU_GUANYIN = 32;
	
	/**
	 * 33: 限时抢购
	 */
	public static final int ITEM_XIANSHI_MALL = 33;
	
	/**
	 * 34：再充礼包
	 */
	public static final int ITEM_AGIN_RECHARGE = 34;
	
	/**
	 * 35：豪充礼包
	 */
	public static final int ITEM_HAO_RECHARGE = 35;
	
	/**
	 * 36: 春节活动 -- 坐骑升阶
	 */
	public static final int ITEM_CJHD_ZQCJ = 36;
	
	/**
	 * 37：每日充值大礼包
	 */
	public static final int ITEM_DAY_RECHAGE = 37;

	/**
	 * 38: 春节活动 -- 在线奖励
	 */
	public static final int ITEM_CJHD_ONLINE_FULI = 38;
	
	/**
	 * 39：合服活动 -- 充值好礼
	 */
	public static final int ITEM_HEFU_RECHAGE = 39;
	
	/**
	 * 40: GM指令获得道具
	 */
	public static final int ITEM_GM = 40;
	
	/**
	 * 41：合服活动 -- 炫酷坐骑
	 */
	public static final int ITEM_HEFU_ZUOQI = 41;
	
	/**
	 * 42：合服活动 -- 城主
	 */
	public static final int ITEM_HEFU_CHENGZHU = 42;
	
	/**
	 * 43：合服活动 -- 翅膀
	 */
	public static final int ITEM_HEFU_CHIBANG = 43;
	
	/**
	 * 44：合服活动 -- VIP
	 */
	public static final int ITEM_HEFU_VIP = 44;
	
	/**
	 * 45：合服活动 -- 战力
	 */
	public static final int ITEM_HEFU_ZHANLI = 45;
	
	/**
	 * 46：合服活动 -- 幸运转盘
	 */
	public static final int ITEM_HEFU_XYZP = 46;
	
	/**
	 * 47：合服活动 -- 等级礼包
	 */
	public static final int ITEM_HEFU_LEVEL = 47;
	
	/**
	 * 48: 合服神秘商店
	 */
	public static final int ITEM_HEFU_SMSD = 48;
	
	/**
	 * 49: 物品打开
	 */
	public static final int ITEM_GOODS_OPEN = 49;
	
	/**
	 * 50: VIP等级礼包
	 */
	public static final int ITEM_VIP_GIFT = 50;
	
	/**
	 * 51: VIP限时礼包
	 */
	public static final int ITEM_VIP_EXPIRE_GIFT = 51;
	/**
	 * 52: 单笔充值
	 */
	public static final int ITEM_LJ_DBCZ = 52;
	/**
	 * 53: 精炼堂
	 */
	public static final int ITEM_JINGLIANTANG = 53;
	/**
	 * 54: 灵石摘除
	 */
	public static final int ITEM_LINGSHI_ZHAICHU = 54;
	
	/**
	 * 55: 寻宝积分兑换
	 */
	public static final int ITEM_XB_JIFEN = 55;
	
	/**
	 * 56：开服活动 -- 武魂排名
	 */
	public static final int ITEM_KAIFU_WUHUN = 56;
	/**
	 * 57：开服活动 -- 勋章排名
	 */
	public static final int ITEM_KAIFU_XUNZHANG = 57;
	/**
	 * 58：开服活动 -- 灵石战力排名
	 */
	public static final int ITEM_KAIFU_LINGSHI = 58;
	/**
	 * 59：累积充值活动 -- 武魂特惠
	 */
	public static final int ITEM_LJ_XUNZHANG = 59;
	/**
	 * 60：累积充值活动 -- 灵石特惠
	 */
	public static final int ITEM_LJ_LINGSHI = 60;
	/**
	 * 61: 热发布神秘商店
	 */
	public static final int ITEM_RFB_SMSD = 61;
	/**
	 * 62: 热发布转盘
	 */
	public static final int ITEM_RFB_XYZP = 62;
	/**
	 * 63: 节日礼包
	 */
	public static final int ITEM_JIERI_GIFT = 63;
	
	/**
	 * 64:塔防副本免费送
	 */
	public static final int ITEM_TAFANG_FB = 64;
	
	/**
	 * 65:领取手机礼包
	 */
	public static final int ITEM_SJ_LIBAO = 65;
	
	/**
	 * 66:购买物品
	 */
	public static final int ITEM_BUY_GOODS = 66;
	
	/**
	 * 67:多选宝箱
	 */
	public static final int ITEM_LQ_BX = 67;
	
	/**
	 * 68:领取王城奖励(七彩宝箱)
	 */
	public static final int ITEM_WAR_BX = 68;
	
	/**
	 * 69:每日充值礼包
	 */
	public static final int ITEM_DAY_RECHARGE = 69;
	
	/**
	 * 70:摆摊购买
	 */
	public static final int ITEM_BAITAN_BUY = 70;
	
	/**
	 * 71:摆摊出售
	 */
	public static final int ITEM_BAITAN_SELL = 71;
	
	/**
	 * 72:新手等级礼包
	 */
	public static final int ITEM_NEW_LEVEL = 72;
	
	/**
	 * 73:物品礼包
	 */
	public static final int ITEM_GOODS = 73;
	
	/**
	 * 74:组包
	 */
	public static final int ITEM_ZUBAO = 74;
	
	/**
	 * 75:背包删除
	 */
	public static final int ITEM_BAG_DELETE = 75;
	
	/**
	 * 76:拆分物品
	 */
	public static final int ITEM_SPLIT_GOODS = 76;
	
	/**
	 * 77:被拆分物品
	 */
	public static final int ITEM_BEI_SPLIT_GOODS = 77;
	
	/**
	 * 78:丢弃物品
	 */
	public static final int ITEM_DISCARD_GOODS = 78;
	
	/**
	 * 79:道具消耗
	 */
	public static final int ITEM_USR_GOODS = 79;
	
	/**
	 * 80:交易物品
	 */
	public static final int ITEM_TRADE_GOODS = 80;
	
	/**
	 * 81:被交易物品
	 */
	public static final int ITEM_BEI_TRADE_GOODS = 81;
	
	/**
	 * 82:整理背包
	 */
	public static final int ITEM_MERGE_BAG = 82;
	
	/**
	 * 83:采集
	 */
	public static final int ITEM_COLLECT = 83;
	
	/**
	 * 84:拾取
	 */
	public static final int ITEM_PICKUP = 84;
	
	/**
	 * 85:材料合成
	 */
	public static final int ITEM_CLHC = 85;
	
	/**
	 * 86:羽毛合成
	 */
	public static final int ITEM_YMHC = 86;
	
	/**
	 * 87:宝石合成
	 */
	public static final int ITEM_BSHC = 87;
	
	/**
	 * 88:喇叭消耗
	 */
	public static final int ITEM_LABA = 88;
	
	/**
	 * 89:日常副本
	 */
	public static final int ITEM_DAY_FUBEN = 89;
	
	/**
	 * 90:挑战副本
	 */
	public static final int ITEM_PUBLIC_FUBEN = 90;
	
	/**
	 * 91:武魂升级
	 */
	public static final int ITEM_SJ_WUHUN = 91;
	
	/**
	 * 92:官职升级
	 */
	public static final int ITEM_SJ_GUANZHI = 92;
	
	/**
	 * 93:神器升级
	 */
	public static final int ITEM_SJ_SHENQI = 93;
	
	/**
	 * 94:寻宝兑换
	 */
	public static final int ITEM_XUNBAO_DH = 94;
	
	/**
	 * 95:出售物品
	 */
	public static final int ITEM_SELL_GOODS = 95;
	
	/**
	 * 96:道具合并
	 */
	public static final int ITEM_GOODS_HB = 96;
	
	/**
	 * 97：QQ等级礼包
	 */
	public static final int ITEM_HUANGZUAN_LIBAO = 97;
	/**
	 * 98:开服在线奖励
	 */
	public static final int ITEM_KF_ONLINE = 98;
	
	/**
	 * 99: QQ分享礼包
	 */
	public static final int ITEM_QQ_FENXIANG = 99;
	/**
	 * 100: QQ邀请
	 */
	public static final int ITEM_QQ_YAOQING = 100;
	
	/**
	 * 102:公会仓库
	 */
	public static final int ITEM_GUILD_SLOT = 102;
	
	/**
	 * 101:成就系统
	 */
	public static final int ITEM_GOODS_CHENG_JIU = 101;

	/**
	 * 103:彩蛋
	 */
	public static final int ITEM_CAIDAN = 103;

	/**
	 * 104:彩蛋兑换
	 */
	public static final int ITEM_CAIDAN_DUIHUAN = 104;
	
	/**
	 * 104:凝聚经验玉
	 */
	public static final int ITEM_JINGYANYU = 104;

	/**
	 * 105：合服首充礼包
	 */
	public static final int ITEM_HF_FIRST_RECHARGE = 105;
	
	/**
	 * 106：再充礼包
	 */
	public static final int ITEM_HF_AGIN_RECHARGE = 106;
	
	/**
	 * 107：豪充礼包
	 */
	public static final int ITEM_HF_HAO_RECHARGE = 107;
	
	/**
	 * 108: 开服限时抢购
	 */
	public static final int ITEM_KF_XIANSHI_MALL = 108;

	/**
	 * 109:经验找回
	 */
	public static final int ITEM_FINDEXP = 109;
	/**
	 * 110:NPC兑换
	 */
	public static final int ITEM_NPC_DUIHUAN = 110;
	/**
	 * 111:功勋兑换
	 */
	public static final int ITEM_GONGXUN = 111;
	/**宝箱采集*/
	public static final int ITEM_COLLECT_BOX = 112;
	
	public static JSONArray createUniqueList(int itemType, String itemId, String userName){
		JSONArray unqiueArr = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("type", itemType);
		map.put("goodsId", itemId);
		if(userName != null){
			map.put("userName", userName);
		}
		
		unqiueArr.add(map);
		return unqiueArr;
	}
	
	public static JSONArray setUnqiueList(JSONArray beforeUnqiue, int itemType, String itemId, String afterGoodsId, String userName){
		if(beforeUnqiue == null || beforeUnqiue.isEmpty()){
			beforeUnqiue = new JSONArray();
		}
		
		if(beforeUnqiue.size() >= 10){
			beforeUnqiue.remove(0);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("type", itemType);
		if(itemId != null){
			map.put("goodsId", itemId);
		}
		if(afterGoodsId != null){
			map.put("afterGoodsId", afterGoodsId);
		}
		if(userName != null){
			map.put("userName", userName);
		}
		
		beforeUnqiue.add(map);
		return beforeUnqiue;
	}
}