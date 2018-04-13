package com.junyou.bus.chongwu.configure.export;

public class ChongWuJiHuoBiaoConfig {

	private Integer id;

	private String needitem;

	private Integer count;

	private String name;

	private String skill1;
	
	private String skill2;
	
	private String skill3;
	
    /* 是否开放宠物装备false=不开启;true=开启 */
	private boolean equipOpen;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNeeditem() {
		return needitem;
	}

	public void setNeeditem(String needitem) {
		this.needitem = needitem;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public String getSkill1() {
        return skill1;
    }

    public void setSkill1(String skill1) {
        this.skill1 = skill1;
    }

    public String getSkill2() {
        return skill2;
    }

    public void setSkill2(String skill2) {
        this.skill2 = skill2;
    }

    public String getSkill3() {
        return skill3;
    }

    public void setSkill3(String skill3) {
        this.skill3 = skill3;
    }

    public boolean isEquipOpen() {
        return equipOpen;
    }

    public void setEquipOpen(boolean equipOpen) {
        this.equipOpen = equipOpen;
    }

}
