package com.junyou.stage.model.core.stage;

import java.util.ArrayList;
import java.util.List;


public enum ElementType {
	
	ROLE(1),MONSTER(2), COLLECT(4), GOODS(5), TRAP(6),PET(7),DIAOXIANG(8),BIAOCHE(9),FIGHTER(10),KUANG(11),PARTITE(13),TANWEI(14),TUTENG(15),CHONGWU(16);
	
	//客户端需要的哪些周边元素
	public static List<ElementType> elementClient = new ArrayList<>();
	static
	{
		elementClient.add(MONSTER);
		elementClient.add(COLLECT);
		elementClient.add(PET);
		elementClient.add(BIAOCHE);
		elementClient.add(GOODS);
		elementClient.add(TRAP);
		elementClient.add(PARTITE);
		elementClient.add(TANWEI);
		elementClient.add(TUTENG);
		elementClient.add(CHONGWU);
	}
	
	private Integer val;
	
	private ElementType(Integer val) {
		this.val = val;
	}
	
	public Integer getVal() {
		return val;
	}

	/**
	 * 是否为玩家角色元素
	 * @param
	 */
	public static boolean isRole(ElementType elementType) {
		return ROLE.equals(elementType);
	}
	/**
	 * 是否为玩家所属元素（玩家，宠物，镖车）
	 * @param
	 */
	public static boolean isRoleOrPet(ElementType elementType) {
		return ROLE.equals(elementType) || PET.equals(elementType) || BIAOCHE.equals(elementType);
	}

	/**
	 * 是否为怪物
	 * @param
	 */
	public static boolean isMonster(ElementType elementType) {
		return MONSTER.equals(elementType) || TUTENG.equals(elementType);
	}
	
	/**
	 * 是否为Pet
	 * @param
	 */
	public static boolean isPet(ElementType elementType) {
		return PET.equals(elementType);
	}
	/**
	 * 是否为镖车
	 * @param
	 */
	public static boolean isBiaoChe(ElementType elementType) {
		return BIAOCHE.equals(elementType);
	}
	/**
	 * 是否为图腾
	 * @param
	 */
	public static boolean isTuTeng(ElementType elementType) {
		return TUTENG.equals(elementType);
	}
	
	/**
	 * 是否是雕像
	 * @param elementType
	 * @return
	 */
	public static boolean isDiaoXiang(ElementType elementType) {
		return DIAOXIANG.equals(elementType);
	}
	
	
	/**
	 * 是否为可攻击元素
	 */
	public static boolean isFighter(ElementType elementType) {
		return ROLE.equals(elementType) || MONSTER.equals(elementType) || PET.equals(elementType) || BIAOCHE.equals(elementType) || TUTENG.equals(elementType);
	}

	/**
	 * 是否为物品
	 */
	public static boolean isGoods(ElementType elementType) {
		return GOODS.equals(elementType);
	}
	/**
	 * 是否为可采集筘
	 */
	public static boolean isCollects(ElementType elementType) {
		return COLLECT.equals(elementType);
	}
	
	/**
	 * 是否为水晶
	 * @param elementType
	 * @return
	 */
	public static boolean isCrystals(ElementType elementType) {
		return PARTITE.equals(elementType);
	}
	
	/**
	 * 是否为陷阱
	 */
	public static boolean isTrap(ElementType elementType) {
		return TRAP.equals(elementType);
	}
	
	
	/**
	 * 是否为摊位
	 */
	public static boolean isTanwei(ElementType elementType) {
		return TANWEI.equals(elementType);
	}
	
	/**
	 * 是否为宠物
	 */
	public static boolean isChongwu(ElementType elementType) {
		return CHONGWU.equals(elementType);
	}
	
	
	/**
	 * 类型转换
	 */
	public static ElementType convert(Integer elementType){
		if(ROLE.getVal().equals(elementType)){
			return ROLE;
		}
		if(MONSTER.getVal().equals(elementType)){
			return MONSTER;
		}
		if(PET.getVal().equals(elementType)){
			return PET;
		}
		if(COLLECT.getVal().equals(elementType)){
			return COLLECT;
		}
		if(TRAP.getVal().equals(elementType)){
			return TRAP;
		}
		if(BIAOCHE.getVal().equals(elementType)){
			return BIAOCHE;
		}
		if(DIAOXIANG.getVal().equals(elementType)){
			return DIAOXIANG;
		}
		if(PARTITE.getVal().equals(elementType)){
			return PARTITE;
		}
		if(TANWEI.getVal().equals(elementType)){
			return TANWEI;
		}
		if(TUTENG.getVal().equals(elementType)){
			return TUTENG;
		}
		if(CHONGWU.getVal().equals(elementType)){
			return CHONGWU;
		}
		throw new NullPointerException("no ElementType for this val :" + elementType);
	}

}
