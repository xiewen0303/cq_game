package com.junyou.public_.guild.manager;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.junyou.public_.guild.entity.GuildJuanXianRank;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;

public class GuildJuanXianManager {
	private GuildJuanXianManager(){};
	private static GuildJuanXianManager manager = new GuildJuanXianManager();
	public static GuildJuanXianManager getManager(){return manager;}
	private List<GuildJuanXianRank> ranks;
	private ConcurrentMap<Long,GuildJuanXianRank> allJuanXian = new ConcurrentHashMap<Long, GuildJuanXianRank>();
	private long updateTime;
	/**
	 * 玩家捐献
	 * @param userRoleId
	 * @param name
	 * @param value
	 */
	public void juanxian(Long userRoleId,String name,int value){
		checkTime();
		GuildJuanXianRank rank = allJuanXian.get(userRoleId);
		if(rank == null){
			rank = new GuildJuanXianRank();
			rank.setName(name);
			rank.setValue(value);
			allJuanXian.put(userRoleId, rank);
		}else{
			rank.setValue(rank.getValue() + value);
		}
	}
	/**
	 * 获取今日捐献排行
	 * @param start
	 * @param size
	 * @return
	 */
	public Object[] getJuanXianList(int start,int size){
		//TODO
		return null;
	}
	
	
	/**
	 * 检测是否跨天
	 */
	private void checkTime(){
		if(!DatetimeUtil.dayIsToday(updateTime)){
			//处理跨天
			updateTime = GameSystemTime.getSystemMillTime();
			allJuanXian.clear();
			if(ranks != null){
				ranks.clear();
			}
		}
	}
}
