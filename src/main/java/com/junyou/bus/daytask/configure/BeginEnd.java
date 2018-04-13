package com.junyou.bus.daytask.configure;

/**
 * 自定义键
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-4-21 下午4:28:36
 */
public class BeginEnd {
	private int beginIndex;
	private int endIndex;
	private int type;

	public BeginEnd(int beginIndex, int endIndex) {
		this.beginIndex = beginIndex;
		this.endIndex = endIndex;
	}

	public BeginEnd(int beginIndex, int endIndex, int type) {
		super();
		this.beginIndex = beginIndex;
		this.endIndex = endIndex;
		this.type = type;
	}
	
	public int getBeginIndex() {
		return beginIndex;
	}
	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}
	public int getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object arg0) {
		if(arg0 instanceof BeginEnd){
			BeginEnd beginEnd  = (BeginEnd)arg0;

			if(beginEnd.getBeginIndex() != this.getBeginIndex() || beginEnd.getEndIndex() != this.getEndIndex() || beginEnd.getType() != this.getType()){
				return false;
			}
		}
		return true;
	}



	@Override
	public int hashCode() {
		return (beginIndex+"_"+endIndex+"_"+type).hashCode() ;
	}

	public boolean isContain(int index,int type){
		if(this.beginIndex <= index && this.endIndex>= index && this.type ==type){
			return true;
		}
		return false;
	}

	public boolean isContain(int index){
		if(this.beginIndex <= index && this.endIndex>= index){
			return true;
		}
		return false;
	}

	
}
