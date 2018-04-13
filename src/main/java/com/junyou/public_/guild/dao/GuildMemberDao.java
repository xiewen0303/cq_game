package com.junyou.public_.guild.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.public_.guild.entity.GuildMember;
import com.kernel.data.dao.AbsDao;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class GuildMemberDao extends AbsDao<GuildMember> implements IDaoOperation<GuildMember> {

	@SuppressWarnings("unchecked")
	public List<GuildMember> initGuildMember(QueryParamMap<String,Object> queryParams) {
		return query("selectAllGuildMemberVo", queryParams);
	}
}