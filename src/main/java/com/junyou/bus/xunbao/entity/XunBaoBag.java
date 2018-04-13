package com.junyou.bus.xunbao.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("xun_bao_bag")
public class XunBaoBag extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("goods_id")
	private String goodsId;

	@Column("goods_count")
	private Integer goodsCount;

	@Column("create_time")
	private Long createTime;


	public Long getId(){
		return id;
	}

	public  void setId(Long id){
		this.id = id;
	}

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public String getGoodsId(){
		return goodsId;
	}

	public  void setGoodsId(String goodsId){
		this.goodsId = goodsId;
	}

	public Integer getGoodsCount(){
		return goodsCount;
	}

	public  void setGoodsCount(Integer goodsCount){
		this.goodsCount = goodsCount;
	}

	public Long getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Long createTime){
		this.createTime = createTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public XunBaoBag copy(){
		XunBaoBag result = new XunBaoBag();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setGoodsId(getGoodsId());
		result.setGoodsCount(getGoodsCount());
		result.setCreateTime(getCreateTime());
		return result;
	}
}
