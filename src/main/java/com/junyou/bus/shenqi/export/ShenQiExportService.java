package com.junyou.bus.shenqi.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.shenqi.entity.ShenQiInfo;
import com.junyou.bus.shenqi.service.ShenQiService;

@Service
public class ShenQiExportService {

	@Autowired
	private ShenQiService shenQiService;

	public List<ShenQiInfo> initShenQiInfo(Long userRoleId) {
		return shenQiService.initShenQiInfo(userRoleId);
	}

	public int getActivatedShenqiNum(Long userRoleId) {
		return shenQiService.getActivatedShenqiNum(userRoleId);
	}
	
	public void activateShenqiByItem(Long userRoleId, Integer shenqiId) {
		 shenQiService.activateShenqiByItem(userRoleId, shenqiId,null);
	}
	public Map<String,Long> getActivatedShenqiAttr(Long userRoleId){
		return shenQiService.getActivatedShenqiAttr(userRoleId);
	}
	public Map<String,Long> getActivatedShenqiZhufuAttr(Long userRoleId,Map<Integer,Integer> typeLevels){//Integer zuoqiLevel,Integer chibangLevel,Integer xianjianLevel,Integer zhanjiaLevel){
		return shenQiService.getActivatedShenqiZhufuAttr(userRoleId, typeLevels);//zuoqiLevel, chibangLevel, xianjianLevel,zhanjiaLevel);
	}
	
	public void onlineHandle(Long userRoleId){
		shenQiService.onlineHandle(userRoleId);
	}
	public Map<String, Long> onlineInitAttr(Long userRoleId){
		return shenQiService.countAllAttr(userRoleId);
	}
	
	public  List<ShenQiInfo> getRoleActivatedShenqi(Long userRoleId){
		return shenQiService.getRoleActivatedShenqi(userRoleId);
	}
	
	public ShenQiInfo getShenQiInfo(Long userRoleId,Integer shenQiId){
		return shenQiService.getRoleShenQiInfo(userRoleId, shenQiId);
	}
}
