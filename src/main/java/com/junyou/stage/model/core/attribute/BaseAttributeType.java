package com.junyou.stage.model.core.attribute;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public enum BaseAttributeType {

	/**
	 * 1-等级
	 */
	LEVEL(1)
	/**
	 * 2-装备基础属性
	 */
	, EQUIP_BASE(2)
	/**
	 * 3-buff
	 */
	, BUFF(3)
	/**
	 * 4-VIP
	 */
	, VIP(4)
	/**
	 * 5-TEAM
	 */
	, TEAM(5)
	/**
	 * 6-主动技能
	 */
	, SKILL(6)
	/**
	 * 7-被动技能
	 */
	, BEIDONG_SKILL(7)
	/**
	 * 8-坐骑属性
	 */
	, ZUOQI(8)
	/**
	 * 9-坐骑移动速度
	 */
	, ZUOQI_SEED(9)
	/**
	 * 10-背包开格子增加的属性
	 */
	, BAG_SLOG(10)
	/**
	 * 11-公会增加的属性
	 */
	, GUILD(11)
	/**
	 * 全身强化
	 */
	, EQUIP_QSQH(12)
	/**
	 * 随机属性
	 */
	, EQUIP_RANDOM_ATTR(13),
	/**
	 * 套装
	 */
	EQUIP_SUIT_ATTR(14),
	/**
	 * 糖宝
	 */
	PET_EAT(15),
	/**
	 * 翅膀
	 */
	CHIBANG(16),
	/**
	 * 红名惩罚
	 */
	HONGMING(17),
	/**
	 * 神器
	 */
	SHENQI(18),
	/**
	 * 仙剑
	 */
	XIANJIAN(19),
	/**
	 * 神器祝福
	 */
	SHENQI_ZHUFU(20),
	/**
	 * 战甲
	 */
	ZHANJIA(21),
	/**
	 * 灵火
	 */
	LINGHUO(22),
	/**
	 * 灵境
	 */
	LINGJING(23),
	/**
	 * 称号
	 */
	CHENGHAO(24),
	/**
	 * 门派技能
	 */
	GUILD_SKILL(25)
	/**
	 * 坐骑装备基础属性
	 */
	, ZUOQI_EQUIP_BASE(26)
	/**
	 * 坐骑装备随机属性
	 */
	, ZUOQI_EQUIP_RANDOM_ATTR(27)
	/**
	 * 翅膀装备基础属性
	 */
	, CHIBANG_EQUIP_BASE(28)
	/**
	 * 翅膀装备随机属性
	 */
	, CHIBANG_EQUIP_RANDOM_ATTR(29)
	/**
	 * 糖宝装备基础属性
	 */
	, TANGBAO_EQUIP_BASE(30)
	/**
	 * 糖宝装备随机属性
	 */
	, TANGBAO_EQUIP_RANDOM_ATTR(31)
	/**
	 * 天工装备基础属性
	 */
	, TIANGONG_EQUIP_BASE(32)
	/**
	 * 天工装备随机属性
	 */
	, TIANGONG_EQUIP_RANDOM_ATTR(33)
	/**
	 * 天裳装备基础属性
	 */
	, TIANSHANG_EQUIP_BASE(34)
	/**
	 * 天裳装备随机属性
	 */
	, TIANSHANG_EQUIP_RANDOM_ATTR(35),
	/**
	 * 妖神
	 */
	YAOSHEN(36),
	/**
	 * 门派勋章-领地战
	 */
	GUILD_XUANZHANG_TERRITORY(37),
	/**
	 * 成就
	 */
	CHENGJIU(38),
	/**
	 * 宝石
	 */

	JEWEL(39),
	/**
	 * 时装激活
	 */
	SHIZHUANG_ACTIVE(40),
	/**
	 * 时装升级
	 */
	SHIZHUANG_SHENGJI(41),
	/**
	* 宠物
	*/
	CHONGWU(42), 
	/**门派勋章
	 * 皇城争霸赛
	 */
	GUILD_XUANZHANG_HCZBS(43),
	/**
	* 结婚信物
	*/
	XINWU(44),
	/**
	* 龙凤呈祥
	*/
	LONGFENG(45),
	/**
	* 坐骑技能
	*/
	ZUOQI_SKILL(46),
	/**
	* 翅膀技能
	*/
	CHIBANG_SKILL(47),
	/**
	* 天工技能
	*/
	TIANGONG_SKILL(48),
	/**
	* 天裳技能
	*/
	TIANSHANG_SKILL(49),
	/**
	 * 妖神魔纹
	 */
	YAOSHEN_MOWEN(50),
	/**
	 * 器灵 
	 */
	 QILING(51),
	/**
	 * 妖神魂魄
	 */
	YAOSHEN_HUNPO(52),
	/**
	 * 妖神魂魄胎光镶嵌
	 */
	YAOSHEN_HUNPO_POSHEN(53),
	/**
	 * 妖神魔印
	 */
	YAOSHEN_MOYIN(54)
	/**
	 * 器灵装备基础属性
	 */
	, QILING_EQUIP_BASE(55)
	/**
	 * 器灵装备随机属性
	 */
	, QILING_EQUIP_RANDOM_ATTR(56)
	/**
	* 器灵技能
	*/
	, QILING_SKILL(57)
	/**
	* 成神称号获得属性加成
	*/
	, CHENG_SHEN(58)
	/**
	* 糖宝心纹
	*/
	, TANGBAO_XINWEN(59)
	/**
	* 转生属性
	*/
	, ZHUAN_SHENG(60)
	/**
	* 通天之路
	*/
	, TONGTIAN_ROAD(61)
	/**
	* 妖神附魔
	*/
	, YAOSHEN_FUMO(62)
		/**
	 * 画卷
	 */
	, HUAJUAN(63)
	/**
	 * 天羽
	 */
	 ,TIANYU(64)
	 /**
	 * 天羽装备基础属性
	 */
	, TIANYU_EQUIP_BASE(65)
	/**
	 * 天羽装备随机属性
	 */
	, TIANYU_EQUIP_RANDOM_ATTR(66)
	/**
	* 天羽技能
	*/
	,TIANYU_SKILL(67)
	/**
	 * 68-五行属性
	 */
	, WUXING(68)
    /**
     * 全身神武装备强化
     */
    , SW_EQUIP_QSQH(69)
    /**
     * -五行技能属性
     */
    , WUXING_SKILL(70)
    /**
     * -五行技能副本属性
     */
    , WUXING_SKILL_FUBEN(71)
    /**
     * -五行魔神精魄属性
     */
    , WUXING_JP(72)
	/**
     * -糖宝五行属性
     */
    , TB_WUXING(73)
    /**
     * -糖宝五行技能属性
     */
    , TB_WUXING_SKILL(74)
    /**
	 * 糖宝被动技能
	 */
	, TB_BEIDONG_SKILL(75)
	/**
     * 76- 心魔属性
     */
    , XINMO(76)
    /**
     * 77- 心魔-魔神属性
     */
    , XINMO_MOSHEN(77)
    /**
     * 78- 心魔-魔神噬体属性
     */
    , XINMO_MOSHEN_SHITI(78)
     /**
     * 79- 心魔-魔神被动技能属性
     */
    , XINMO_SKILL(79)
    /**
     * 80- 心魔-洗练属性
     */
    , XINMO_XILIAN(80)
   /**
    * 宠物技能属性
    */
    ,CHONGWU_SKILL(81)
    /**
     * 宠物装备基础属性
     */
    , CHONGWU_EQUIP_BASE(82)
    /**
     * 宠物装备随机属性
     */
    , CHONGWU_EQUIP_RANDOM_ATTR(83)
    /**
     * 画卷2属性
     */
    , HUAJUAN2(84)
    /**
     * 灵火祝福属性
     */
    , LINGHUO_BLESS(85)
    /**
     * 仙洞属性
     */
    , XIANDONG(86)
    /**
     * 仙器觉醒属性
     */
    , XIANQIJUEXING(87)
    /**
     * 仙缘飞化属性
     */
    ,XIAYUANFEIHUA(88)
    /**
	 * 时装升级
	 */
	,SHIZHUANG_JINJIE(89)
	/**
	 * 神器进阶属性
	 */
	,SHENQI_JINJIE(90)
	 /**
     * 神器装备基础属性
     */
    , SHENQI_EQUIP_BASE(91)
    /**
     * 神器装备随机属性
     */
    , SHENQI_EQUIP_RANDOM_ATTR(92)
    /**
     * 套装象位属性
     */
    , SUIT_XIANGWEI_ATTR(93)
      /**
     * boss积分属性
     */
    , BOSS_JIFEN_ATTR(94)
    
    /**
	 * 95-新圣剑属性
	 */
	, WUQI(95)
	/**
	 * 96-新圣剑装备基础属性
	 */
	, WUQI_EQUIP_BASE(96)
	/**
	 * 97-新圣剑装备随机属性
	 */
	, WUQI_EQUIP_RANDOM_ATTR(97)
	/**
	 * 98-押镖人物
	 */
	, JB_BIAOCE_ATTR(98)
	/**
	* 天羽技能
	*/
	,WUQI_SKILL(99)
    ;
	public final Integer val;

	/**
	 * 场景人物属性效果枚举类
	 * 
	 * @param value
	 *            属性效果类型值
	 */
	private BaseAttributeType(Integer value) {
		this.val = value;
	}

	public Integer getVal() {
		return val;
	}

	public static BaseAttributeType getBaseAttributeTypeByVal(int value) {
		for (BaseAttributeType type : BaseAttributeType.values()) {
			if (type.getVal() == value) {
				return type;
			}
		}
		return null;
	}

	static {
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (BaseAttributeType b : BaseAttributeType.values()) {
			String fName = b.name();
			int value = b.getVal();
			if (map.get(value) != null)
				throw new RuntimeException(MessageFormat.format(
						"duplicate baseAttribute error:[{0},{1}]", fName,
						map.get(value)));
			else
				map.put(value, fName);
		}
	}
}
