package com.junyou.stage.model.hatred;

import com.junyou.stage.model.core.hatred.IHatred;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.utils.datetime.GameSystemTime;

public class Hatred implements IHatred {
	
	public static final int OUT_6_RULE_TIME = 12000;

	private Long id ;
	private ElementType elementType;
	private long updateTime;
	private int val;
	
	public Hatred(Long id,ElementType elementType) {
		this.id = id;
		this.elementType = elementType;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}

	@Override
	public void add(int i) {
		this.updateTime = GameSystemTime.getSystemMillTime();
		this.val = this.val + i;
	}

	@Override
	public boolean out6sRule() {
		return ((GameSystemTime.getSystemMillTime() - updateTime) > OUT_6_RULE_TIME);
	}

	@Override
	public boolean equals(Object obj) {
		
		if(!(obj instanceof IHatred))
			return false;
		
		
		return getId().equals(((IHatred)obj).getId());
	}

	@Override
	public int getVal() {
		return val;
	}

	@Override
	public boolean expired(int expireTime) {
		return ((GameSystemTime.getSystemMillTime() - updateTime) >= expireTime);
	}

	@Override
	public long getlasttime() {
		return updateTime;
	}
	
	

}
