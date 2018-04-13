package com.junyou.bus.bagua.configure.export;

public class BaguaZhenMenConfig {
	private int order;
	private String name;
	private int chuanshang;
	private int chuanxia;

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getChuanshang() {
		return chuanshang;
	}

	public void setChuanshang(int chuanshang) {
		this.chuanshang = chuanshang;
	}

	public int getChuanxia() {
		return chuanxia;
	}

	public void setChuanxia(int chuanxia) {
		this.chuanxia = chuanxia;
	}

	public int getChuan(){
		if(chuanshang>0){
			return chuanshang;
		}else{
			return -chuanxia;
		}
	}
}
