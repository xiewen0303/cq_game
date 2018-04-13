package com.junyou.bus.tuangou.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.tuangou.entity.RefbRoleTuangou;
import com.junyou.bus.tuangou.service.TuanGouService;

/**
 * @author zhongdian
 * 2016-1-13 下午2:50:08
 */
@Service
public class TuanGouExportService {

	@Autowired
	private TuanGouService tuanGouService;
	
	
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		return tuanGouService.getRefbInfo(userRoleId, subId);
	}
	
	public void quartTuanGou() {
		tuanGouService.quartTuanGou();
	}
	
	public List<RefbRoleTuangou> initRefbRoleTuangou(Long userRoleId){
		return tuanGouService.initRefbRoleTuangou(userRoleId);
	} 
	/**
	 * 结算邮件(跟运营确认了20:00:00结束)
	 */
	public void jieSuanEmail() {
		tuanGouService.jieSuanEmail();
	}
	
	public void qingkongData(){
		tuanGouService.qingkongData();
	}
	
}
