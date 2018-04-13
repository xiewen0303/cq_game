package com.junyou.bus.happycard.configure.export;

import java.util.Map;

public class HappyCardItem {
	private int index;
	private int weight;
	private int type;
	public final static int HAPPY_CARD_ITEM_TYPE_1 = 1;// 道具
	public final static int HAPPY_CARD_ITEM_TYPE_2 = 2;// 倍率
	private int multi;
	private Map<String, Integer> items;
	private String itemsStr;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getMulti() {
		return multi;
	}

	public void setMulti(int multi) {
		this.multi = multi;
	}

	public Map<String, Integer> getItems() {
		return items;
	}

	public void setItems(Map<String, Integer> items) {
		this.items = items;
	}
	
	public String getItemsStr() {
		return itemsStr;
	}

	public void setItemsStr(String itemsStr) {
		this.itemsStr = itemsStr;
	}

	public boolean isItem(){
		return type == HAPPY_CARD_ITEM_TYPE_1;
	}

}
