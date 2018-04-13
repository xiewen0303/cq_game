package com.junyou.bus.platform.taiwan.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.common.service.PtCommonService;
import com.junyou.bus.platform.qq.dao.TencentUserInfoDao;
import com.junyou.bus.platform.qq.entity.TencentUserInfo;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * @author zhongdian
 * 2015-10-23 上午11:10:40
 */
@Service
public class TaiWanService {

	@Autowired
	private TencentUserInfoDao tencentUserInfoDao;
	@Autowired
	private PtCommonService ptCommonService;
	
	public void tencentViaUser(Long userRoleId){
		Map<String, String> keyMap = getRoleMap(userRoleId);	
		TencentUserInfo info = new TencentUserInfo();
		if(keyMap != null){
			info.setUserRoleId(userRoleId);
			info.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			info.setPf(keyMap.get("pf"));
		}else{
			info = new TencentUserInfo();
			info.setUserRoleId(userRoleId);
			info.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		}
		tencentUserInfoDao.dbInsert(info);
    }
	//获取web传来的参数
	private  Map<String, String> getRoleMap(Long userRoleId){
		return ptCommonService.getRoleMapTW(userRoleId,PlatformPublicConfigConstants.PLATFORM_ARGS_MODEL);
	}
    public List<TencentUserInfo> initTencentUserInfos(Long userRoleId){
    	return tencentUserInfoDao.initTencentUserInfo(userRoleId);
    }
    
    public String getUserZhuCePf(Long userRoleId){
    	List<TencentUserInfo> lists = tencentUserInfoDao.cacheAsynLoadAll(userRoleId);
    	if(lists == null || lists.size() <= 0){
    		return null;
    	}
    	TencentUserInfo info = lists.get(0);
    	return info.getPf();
    }
	
}
