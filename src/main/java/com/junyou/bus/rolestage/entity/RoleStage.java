package com.junyou.bus.rolestage.entity;

import java.io.Serializable;

import com.junyou.stage.model.skill.PublicCdManager;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Primary;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_stage")
public class RoleStage extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;
		
	@Primary
	@Column("user_role_id")
	private Long userRoleId;

	@Column("map_id")
	private Integer mapId;

	@Column("map_x")
	private Integer mapX;

	@Column("map_y")
	private Integer mapY;

	@Column("hp")
	private Long hp;

	@Column("buff")
	private String buff;

	@Column("shenqi")
	private Integer shenqi;

	@Column("map_node")
	private String mapNode;

	@Column("ti_li")
	private Integer tiLi;

	@Column("line_no")
	private Integer lineNo;

	@Column("prop")
	private String prop;

	@Column("last_main_map")
	private Integer lastMainMap;
	
	@Column("nuqi")
	private Integer nuqi;
	
	@EntityField
	private boolean isLogin = true;
	
	@EntityField
	private PublicCdManager cdManager = new PublicCdManager();
	
	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}
	public Integer getMapId() {
		return mapId;
	}

	public void setMapId(Integer mapId) {
		this.mapId = mapId;
	}
	public Integer getMapX() {
		return mapX;
	}

	public void setMapX(Integer mapX) {
		this.mapX = mapX;
	}
	public Integer getMapY() {
		return mapY;
	}

	public void setMapY(Integer mapY) {
		this.mapY = mapY;
	}
	public Long getHp() {
		return hp;
	}

	public void setHp(Long hp) {
		this.hp = hp;
	}
	public String getBuff() {
		return buff;
	}

	public void setBuff(String buff) {
		this.buff = buff;
	}
	public Integer getShenqi() {
		return shenqi;
	}

	public void setShenqi(Integer shenqi) {
		this.shenqi = shenqi;
	}
	public String getMapNode() {
		return mapNode;
	}

	public void setMapNode(String mapNode) {
		this.mapNode = mapNode;
	}
	public Integer getTiLi() {
		return tiLi;
	}

	public void setTiLi(Integer tiLi) {
		this.tiLi = tiLi;
	}
	public Integer getLineNo() {
		return lineNo;
	}

	public void setLineNo(Integer lineNo) {
		this.lineNo = lineNo;
	}
	public String getProp() {
		return prop;
	}

	public void setProp(String prop) {
		this.prop = prop;
	}
	public Integer getLastMainMap() {
		return lastMainMap;
	}

	public void setLastMainMap(Integer lastMainMap) {
		this.lastMainMap = lastMainMap;
	}
	public Integer getNuqi() {
		return nuqi;
	}

	public void setNuqi(Integer nuqi) {
		this.nuqi = nuqi;
	}

	@Override	
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return getUserRoleId();
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public PublicCdManager getCdManager() {
		return cdManager;
	}

	public void setCdManager(PublicCdManager cdManager) {
		this.cdManager = cdManager;
	}

	public RoleStage copy(){
		RoleStage result = new RoleStage();
		result.setUserRoleId(getUserRoleId());
		result.setMapId(getMapId());
		result.setMapX(getMapX());
		result.setMapY(getMapY());
		result.setHp(getHp());
		result.setBuff(getBuff());
		result.setShenqi(getShenqi());
		result.setMapNode(getMapNode());
		result.setTiLi(getTiLi());
		result.setLineNo(getLineNo());
		result.setProp(getProp());
		result.setLastMainMap(getLastMainMap());
		result.setNuqi(getNuqi());
		result.setLogin(isLogin());
		return result;
	}
}