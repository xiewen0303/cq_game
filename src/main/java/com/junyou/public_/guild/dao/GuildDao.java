package com.junyou.public_.guild.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.public_.guild.entity.Guild;
import com.kernel.data.dao.AbsDao;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class GuildDao extends AbsDao<Guild> implements IDaoOperation<Guild> {

	@SuppressWarnings("unchecked")
	public List<Guild> queryGuildByGuildName(String guildName){
		QueryParamMap<String,Object> queryParam = new QueryParamMap<String, Object>();
		queryParam.put("name", guildName);
		
		return (List<Guild>)query("selectMultiGuild",queryParam );
	}
}