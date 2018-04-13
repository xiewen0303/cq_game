package com.junyou.bus.lianyuboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.lianyuboss.entity.GuildBossLianyu;
import com.junyou.bus.lianyuboss.entity.LianyuBossPublicVo;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class GuildBossLianyuDao extends BusAbsCacheDao<GuildBossLianyu> implements IDaoOperation<GuildBossLianyu> {

	public List<GuildBossLianyu> initGuildBossLianyu(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
	
	@SuppressWarnings("unchecked")
	public List<GuildBossLianyu> initGuildBossLianyuById(Long guildId,Long userRoleId,String time) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("guildId", guildId);
		queryParams.put("userRoleId", userRoleId);
		queryParams.put("time", time);
		return query("selectLianyuBossAllDataByUserRoleId",queryParams);
	}
	/**凌晨时间到删除所有过期的数据**/
	@SuppressWarnings("unchecked")
	public void deleteKuatianShujuByLingceng(String time) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("ctime", time);
		delete("deleteKuatianShujuByLingceng",queryParams);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<LianyuBossPublicVo> initLianyuBossPublicData(String startTime) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("startTime", startTime);
		return query("selectLianyuBossPublicData", queryParams);
	}
	
	public void deleteDataByUserRoleId(Long userRoleId) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);//换了帮派的玩家也会同时删除之前在别的帮派的记录
//		queryParams.put("guildId", guildId);
		delete("deleteGuildBossDataByUserRoleId", queryParams);
	}
	
}