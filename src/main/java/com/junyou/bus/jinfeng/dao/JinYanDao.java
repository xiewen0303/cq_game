package com.junyou.bus.jinfeng.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.junyou.bus.jinfeng.entity.JinYan;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.AbsDao;
import com.kernel.data.dao.QueryParamMap;


@Component
public class JinYanDao extends AbsDao<JinYan>{

	public Object insert2DB(JinYan jinYan) {
		
		return insert(jinYan, jinYan.getUserRoleId(), AccessType.getDirectDbType());
	}

	public JinYan selectFromDB(Long roleId) {
		return load(roleId, roleId, AccessType.getDirectDbType());
	}

	public Object deleteFromDB(Long roleId) {
		return delete(roleId, roleId, AccessType.getDirectDbType());
	}

	public Integer selectCountFromDBByUserId(String userId,String serverId) {
		QueryParamMap<String,Object> map = new QueryParamMap<String, Object>();
		map.put("userId", userId);
		map.put("serverId", serverId);
		return (Integer)query("selectCountFromDBByUserId", map).get(0);
	}

	public void updateFromDB(JinYan jinYan) {
		update(jinYan, jinYan.getUserRoleId(), AccessType.getDirectDbType());
	}

	@SuppressWarnings("unchecked")
	public JinYan selectFromDBByUserId(String userId,String serverId) {
		QueryParamMap<String,Object> map = new QueryParamMap<String, Object>();
		map.put("userId", userId);
		map.put("serverId", serverId);
		List<JinYan> list = query("selectMultiJinYan", map);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	public void deleteFromDBByUserId(String userId, String serverId) {
		QueryParamMap<String,Object> map = new QueryParamMap<String, Object>();
		map.put("userId", userId);
		map.put("serverId", serverId);
		delete("deleteJinYanByUserId", map);
	}

	
}