package com.kernel.data.write.async;

import java.io.Serializable;

import com.kernel.data.dao.IEntity;

public class AsyncEntityOperate implements Serializable{

	private static final long serialVersionUID = 1L;

	private String id;

	private IEntity entity;
	
	private boolean insert;
	private boolean update;
	private boolean delete;
	
	public AsyncEntityOperate(String entityId) {
		this.id = entityId;
	}
	
	public void insert(IEntity insertEntity){

		this.insert = true;
		this.entity = insertEntity.copy();
	}

	public void update(IEntity updateEntity){
		
		this.update = true;
		this.entity = updateEntity.copy();
	}
	
	public boolean delete(IEntity deleteEntity){

		if(insert) return true;
		
		this.delete = true;
		this.entity = deleteEntity.copy();
		return false;
		
	}
	
	public String getId() {
		return id;
	}

	public IEntity getInsert() {
		if(insert) return entity;
		return null;
	}

	public IEntity getUpdate() {
		if(insert) return null;
		if(delete) return null;
		if(update) return entity;
		return null;
	}

	public IEntity getDelete() {
		if(delete) return entity;
		return null;
	}
	
	public IEntity getEntity(){
		return entity;
	}
	
}
