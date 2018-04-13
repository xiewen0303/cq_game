package com.junyou.bus.rfbactivity.service;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.rfbactivity.dao.RoleYuanbaoRecordDao;
import com.junyou.bus.rfbactivity.entity.RoleYuanbaoRecord;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * @author zhongdian
 * 2015-8-3 下午1:57:03
 */
@Service
public class RoleYuanbaoRecordService {

	@Autowired
	private RoleYuanbaoRecordDao roleYuanbaoRecordDao;
	
	public List<RoleYuanbaoRecord> initRoleYuanbaoRecords(Long userRoleId){
		return roleYuanbaoRecordDao.initRoleYuanbaoRecord(userRoleId);
	}
	
	
	public RoleYuanbaoRecord getRoleYuanBaoRecord(Long userRoleId){
		List<RoleYuanbaoRecord> list = roleYuanbaoRecordDao.cacheAsynLoadAll(userRoleId);
		if(list == null || list.size() <= 0){
			RoleYuanbaoRecord record = new RoleYuanbaoRecord();
			
			record.setUserRoleId(userRoleId);
			record.setCzValue(0);
			record.setXfValue(0);
			record.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			record.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			
			roleYuanbaoRecordDao.cacheInsert(record, userRoleId);
			return record;
		}
		RoleYuanbaoRecord record = list.get(0);
		//不是同一天，清理数据
		if(!DateUtils.isSameDay(record.getUpdateTime(), new Timestamp(GameSystemTime.getSystemMillTime()))){
			record.setCzValue(0);
			record.setXfValue(0);
			record.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			
			roleYuanbaoRecordDao.cacheUpdate(record, userRoleId);
			
		}
		return record;
	}
	
	
	/**
	 * 消费元宝（每日清空）
	 * @param userRoleId
	 * @param xfValue
	 */
	public void xiaofeiYuanBao(Long userRoleId,long xfValue){
		RoleYuanbaoRecord record = getRoleYuanBaoRecord(userRoleId);
		record.setXfValue((int) (record.getXfValue() + xfValue));
		record.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		roleYuanbaoRecordDao.cacheUpdate(record, userRoleId);
		
	}
	
	
	/**
	 * 充值元宝（每日清空）
	 * @param userRoleId
	 * @param czValue
	 */
	public void rechargeYuanBao(Long userRoleId,long czValue){
		RoleYuanbaoRecord record = getRoleYuanBaoRecord(userRoleId);
		record.setCzValue((int) (record.getCzValue() + czValue));
		record.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		roleYuanbaoRecordDao.cacheUpdate(record, userRoleId);
		
	}
	
	
	
}
