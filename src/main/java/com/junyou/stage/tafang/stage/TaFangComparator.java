package com.junyou.stage.tafang.stage;

import java.util.Comparator;

public class TaFangComparator implements Comparator<TaFangMonster>{

	@Override
	public int compare(TaFangMonster o1, TaFangMonster o2) {
		return o2.getStep() - o1.getStep();
	}

}
