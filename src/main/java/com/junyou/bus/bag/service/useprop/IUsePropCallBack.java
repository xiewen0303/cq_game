package com.junyou.bus.bag.service.useprop;

/**
 * 使用道具要求
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-3-4 上午11:20:50 
 */
public interface IUsePropCallBack {
	/**
	 * 增加对应货币数值
	 * @param type {@link GameConstants(元宝，游戏币，礼券)}
	 */
	public void addCurrency(int type);
	
//	/**
//	 * 添加神器积分
//	 */
//	public void addShenQiValue();
	
	/**
	 * 加药（血蓝）
	 */
	public void addHpPropHandle();
	
	/**
	 * 百分比加药（血蓝）
	 */
	public void addHpPropHandleBaiFenBi();
	
	/**
	 * 增加角色经验带推送
	 */
	public void addRoleExpHandle();
	
	/**
	 * 实例消耗数量
	 * @return
	 */
	public Integer getConsumeCount();
	
	/**
	 * 获取消耗失败的Code
	 * @return 成功则为null
	 */
	public Object[] getErrorCode();
	
	/**
	 * 添加坐骑潜能丹
	 */ 
	public void addZuoQiQNDHandle();
	/**
	 * 添加坐骑潜能丹
	 * @return 成功则为null
	 */ 
	public void addZuoQiCZDHandle();
	
	/**
	 * 添加翅膀潜能丹
	 */ 
	public void addChiBangQNDHandle();
	/**
	 * 添加翅膀潜能丹
	 * @return 成功则为null
	 */ 
	public void addChiBangCZDHandle();
	/**
	 * 添加器灵潜能丹
	 */ 
	public void addQiLingQNDHandle();
	/**
	 * 添加器灵成长丹
	 * @return 成功则为null
	 */ 
	public void addQiLingCZDHandle();
	/**
	 * 添加天羽潜能丹
	 */ 
	public void addTianYuQNDHandle();
	/**
	 * 添加天羽成长丹
	 * @return 成功则为null
	 */ 
	public void addTianYuCZDHandle();
	/**
	 * 添加妖神潜能丹
	 */ 
	public void addYaoshenQNDHandle();
	/**
	 * 添加妖神潜能丹
	 * @return 成功则为null
	 */ 
	public void addYaoshenCZDHandle();
	/**
	 * 添加妖神魔纹潜能丹
	 */ 
	public void addYaoshenMowenQNDHandle();
	/**
	 * 添加妖神魔纹成长丹
	 * @return 成功则为null
	 */ 
	public void addYaoshenMowenCZDHandle();

	/**
	 * 添加妖神魂魄潜能丹
	 */ 
	public void addYaoshenHunpoQNDHandle();
	/**
	 * 添加妖神魂魄成长丹
	 * @return 成功则为null
	 */ 
	public void addYaoshenHunpoCZDHandle();
	/**
	 * 添加神魂丹
	 * @return 成功则为null
	 */ 
	public void addShenHunDanHandle();
	
	
	

	/**
	 * 添加妖神魔印潜能丹
	 */ 
	public void addYaoshenMoYinQNDHandle();
	/**
	 * 添加妖神魔印成长丹
	 * @return 成功则为null
	 */ 
	public void addYaoshenMoYinCZDHandle();
	
	/**
	 * 添加仙剑潜能丹
	 */ 
	public void addXianJianQNDHandle();
	/**
	 * 添加仙剑潜能丹
	 * @return 成功则为null
	 */ 
	public void addXianJianCZDHandle();
	
	/**
	 * 添加战甲潜能丹
	 */ 
	public void addZhanJiaQNDHandle();
	/**
	 * 添加战甲潜能丹
	 * @return 成功则为null
	 */ 
	public void addZhanJiaCZDHandle();
	
	/**
	 * 特殊效果类型道具  [血包,多倍经验]
	 */
	public void tsXiaoGuoPropHandle();
	
	/**
	 * 打开道具[固定礼包，随机礼包]
	 */
	public void openGiftCardPropHandler(int type);
	
	/**
	 * 增加固定经验值
	 */
	public void addExpHandle();
	
	/**
	 * 增加百分比经验值
	 */
	public void addPerExpHandle();
	
	/**
	 * 增加真气
	 */
	public void addZhenqiHandle();
	
	/**
	 * 使用糖宝资质丹
	 */ 
	public void tangbaoZizhiHandle();
	/**
	 * 使用眉心之血
	 */ 
	public void tangbaoMXZXHandle();
	/**
	 * 使用菩提
	 */ 
	public void tangbaoPuTiHandle();
	/**
	 * 激活糖宝
	 */ 
	public void activeTangbao();
	/**
	 * 激活神器
	 */ 
	public void activeShenqi();
	/**
	 * 升仙丹
	 */ 
	public void shengxiandan();
	/**
	 * 回城
	 */ 
	public void townPortal();
	/**
	 * 赎罪药水
	 */ 
	public void shuzuiyaoshui();
	/**
	 * 翅膀升阶丹药
	 */ 
	public void chibangSj();
	/**
	 * 五行升阶丹药
	 */ 
	public void wuXingSj();
	/**
	 * 糖宝战甲，天裳直升道具
	 */ 
	public void tbZhanjiaSj();
	/**
	 * 糖宝武器，天工直升道具
	 */ 
	public void tbWuqiSj();
	/**
	 * 御剑直升   道具
	 */ 
	public void zuoqiSj();
	/**
	 * 圣剑直升   道具
	 */ 
	public void wuqiSj();
	/**激活限时时装*/
	public void xianshiShizhuang();

	public void qilingSj();
	public void tianyuSj();
	
	public void addXiuwei();
	
	public void activateHuajuan();
	
	public void addChongwuExp();

	public void yaoShenSj();

	public void yaoShenMoYinSj();

	public void yaoShenMoWenSj();

	public void yaoShenHunPoSj();
	public void tangBaoXinWenSj();
	
	public void addVipExpHandle();
	
	/** 增加魔宫猎焰精华数 **/
	public void addMglyJinghuaHandler();
	
	public void addBossJifen();

	void addWuQiQNDHandle();

	public void addWuQiCZDHandle();
}
