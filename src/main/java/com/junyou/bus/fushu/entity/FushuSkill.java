package com.junyou.bus.fushu.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("fushu_skill")
public class FushuSkill extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;
	
	@Column("user_role_id")
	private Long userRoleId;

	@Column("type")
	private Integer type;

	@Column("skill1")
	private String skill1;

	@Column("skill2")
	private String skill2;

	@Column("skill3")
	private String skill3;

	@Column("skill4")
	private String skill4;

	@Column("skill5")
	private String skill5;

	@Column("skill6")
	private String skill6;
	
	@EntityField
	private Object[] clientMsg;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getType(){
		return type;
	}

	public  void setType(Integer type){
		this.type = type;
	}

	public String getSkill1(){
		return skill1;
	}

	public  void setSkill1(String skill1){
		this.skill1 = skill1;
	}

	public String getSkill2(){
		return skill2;
	}

	public  void setSkill2(String skill2){
		this.skill2 = skill2;
	}

	public String getSkill3(){
		return skill3;
	}

	public  void setSkill3(String skill3){
		this.skill3 = skill3;
	}

	public String getSkill4(){
		return skill4;
	}

	public  void setSkill4(String skill4){
		this.skill4 = skill4;
	}

	public String getSkill5(){
		return skill5;
	}

	public  void setSkill5(String skill5){
		this.skill5 = skill5;
	}

	public String getSkill6(){
		return skill6;
	}

	public  void setSkill6(String skill6){
		this.skill6 = skill6;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public FushuSkill copy(){
		FushuSkill result = new FushuSkill();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setType(getType());
		result.setSkill1(getSkill1());
		result.setSkill2(getSkill2());
		result.setSkill3(getSkill3());
		result.setSkill4(getSkill4());
		result.setSkill5(getSkill5());
		result.setSkill6(getSkill6());
		return result;
	}

	public Object[] getClientMsg() {
		if(clientMsg == null){
			clientMsg = new Object[6];
		}
		clientMsg[0] = getSkill1();
		clientMsg[1] = getSkill2();
		clientMsg[2] = getSkill3();
		clientMsg[3] = getSkill4();
		clientMsg[4] = getSkill5();
		clientMsg[5] = getSkill6();
		return clientMsg;
	}
	
	
}
