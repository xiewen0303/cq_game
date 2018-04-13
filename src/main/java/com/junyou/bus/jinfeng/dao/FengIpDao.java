package com.junyou.bus.jinfeng.dao;

import org.springframework.stereotype.Component;

import com.junyou.bus.jinfeng.entity.FengIp;
import com.kernel.data.dao.AbsDao;


@Component
public class FengIpDao extends AbsDao<FengIp>{

	public FengIp getFengIpByIp(String ip){
		return load(ip, null, getAccessType());
	}
	
	public void insert(FengIp fengIp){
		insert(fengIp, null, getAccessType());
	}
	
	public void delete(String ip){
		delete(ip, null, getAccessType());
	}
	
}