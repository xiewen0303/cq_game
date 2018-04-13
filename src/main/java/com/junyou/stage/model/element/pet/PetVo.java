package com.junyou.stage.model.element.pet;

/**
 * 宠物VO数据
 * @author DaoZheng Yuan
 * 2013-11-6 上午11:08:51
 */
public class PetVo {

	private Long tbId;
	
	private Long userRoleId;
	
	private Long curHp;
	
	private Integer state;
	
	private Pet pet;

	public PetVo(Long userRoleId, Pet pet,Long tbId) {
		this.userRoleId = userRoleId;
		this.tbId = tbId;
		this.pet = pet;
		this.curHp = pet.getFightAttribute().getCurHp();
	}

	public Long getUserRoleId() {
		return userRoleId;
	}

	public Long getCurHp() {
		return curHp;
	}

	public void setCurHp(Long curHp) {
		this.curHp = curHp;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

	public Long getTbId() {
		return tbId;
	}

	public void setTbId(Long tbId) {
		this.tbId = tbId;
	}
	
	
}
