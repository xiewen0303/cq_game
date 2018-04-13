package com.junyou.public_.rank.dao;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.junyou.public_.rank.vo.IRankVo;

@Repository
public class RankDao /*extends BusAbsCacheDao<Rank> implements
		IDaoOperation<Rank> 需要入库再加上*/ {

	
	Integer LEVEL = 1;
	
	/*
	*//**
	 * 排行榜缓存数据结构 Map<排行类型, Map<排名, Rank>>
	 * */
	/*
	 * private final static Map<Integer, ConcurrentSkipListMap<Integer,
	 * IRankVo>> NATIVE_RANKS=new ConcurrentHashMap<Integer,
	 * ConcurrentSkipListMap<Integer,IRankVo>>();
	 */

	/**
	 * 排行榜缓存数据结构 Map<排行类型, 排行表List>
	 * */
	private final static Map<Integer, List<? extends IRankVo>> NATIVE_RANKS_LIST = new ConcurrentHashMap<Integer, List<? extends IRankVo>>();
	/**
	 * 排行榜魁首缓存数据结构 Map<排行类型, 排行第1名数据IRankVo>
	 * */
	private final static Map<Integer, IRankVo> KUISHOU_MAP = new ConcurrentHashMap<Integer, IRankVo>();

	
	
	private static final int PAGINAL_NUM = 10;

	/*	*//**
	 * 获取分页后的排行表
	 * 
	 * @param rankType
	 *            :排行类型
	 * @param fromRank
	 *            起始排名
	 * @param toRank
	 *            结束排名
	 * */
	/*
	 * public Rank[] cutRanksByType(int rankType, int fromRank,int toRank){
	 * //获取某一类型排行 ConcurrentSkipListMap<Integer, Rank>
	 * typeRank=getNativeAllRank().get(rankType); //分页 Map<Integer,Rank>
	 * pagingRankMap=typeRank.subMap(fromRank,true, toRank,true);
	 * Collection<Rank> ranksColl=pagingRankMap.values();
	 * 
	 * return ranksColl.toArray(new Rank[ranksColl.size()]); }
	 */

	/**
	 * 刷新魁首数据
	 */
	public void refreshKuiShou(Integer rankType,IRankVo rankVo){
		
		KUISHOU_MAP.put(rankType, rankVo);
	}
	/**
	 * 根据榜类型获取魁首数据
	 * @param rankType
	 * @return
	 */
	public IRankVo getKuiShou(Integer rankType){
		
		return KUISHOU_MAP.get(rankType);
	}
	
	/** 刷新本服排行 */
	public void refreshNativeRank(Integer rankType,List<? extends IRankVo> rankVos) {
		
		NATIVE_RANKS_LIST.put(rankType, rankVos);
	}

	
	public List<? extends IRankVo> getRankData(Integer rankType){
		
		
		return NATIVE_RANKS_LIST.get(rankType);
		/*Object result = null;
		
		switch (rankType.VALUE) {
		case RankType.FIGHTING.VALUE:
		
			List<IFightingRankVo> fightingVos = (List<IFightingRankVo>) NATIVE_RANKS_LIST.get(rankType.VALUE);
			
			
			if(fightingVos != null && fightingVos.size() > 0){
				
				for (IFightingRankVo tmpRank : fightingVos) {
					
					if(tmpRank.getUserRoleId().equals(userRoleId)){
						result = tmpRank.getOutData();
						break;
					}
				}
			}
			
			return result;
		case LEVEL:
			
			List<ILevelRankVo> levelVos = (List<ILevelRankVo>) NATIVE_RANKS_LIST.get(rankType.VALUE);
			
			if(levelVos != null && levelVos.size() > 0){
				
				for (ILevelRankVo tmpRank : levelVos) {
					
					if(tmpRank.getUserRoleId().equals(userRoleId)){
						result = tmpRank.getOutData();
						break;
					}
				}
			}
			
			return result;
		default:
			break;
		}
		*/
	}
	
	
	/**
	 * 根据排行类型按页获取排行
	 * 
	 * @param rankType
	 *            :排行类型
	 * @param page
	 *            : 页
	 * */
	public IRankVo[] cutRanks(int rankType, int page) {
		// 没开放或不存在的排行或非法页
		if (NATIVE_RANKS_LIST.get(rankType) == null
				|| page > getSumPageByRankType(rankType) || page < 1) {
			return null;
		}

		List<?> rankList = NATIVE_RANKS_LIST.get(rankType).subList(
				(page - 1) * PAGINAL_NUM, page * PAGINAL_NUM);

		return rankList.toArray(new IRankVo[rankList.size()]);
	}

	/**
	 * 根据排行类型按页获取排行
	 * 
	 * @param rankType
	 *            :排行类型
	 * @param startIndex
	 *            : 起始索引
	 * @param needCount
	 *            : 记录数
	 * @return 
	 * */
	public List<? extends IRankVo> cutRanks(int rankType, int startIndex, int needCount) {
		// 没开放或不存在的排行
		if (NATIVE_RANKS_LIST.get(rankType) == null) {
			return null;
		}
		int count = getCountByRankType(rankType);
		// 不在数据范围
		if (startIndex < 0 || startIndex + 1 > count) {
			return null;
		}
		return NATIVE_RANKS_LIST.get(rankType).subList(startIndex,Math.min(count, needCount+1));
	}

	/** 获取某一类型的排行总数 */
	public int getCountByRankType(int rankType) {
		if (NATIVE_RANKS_LIST.get(rankType) == null) {
			return 0;
		}
		return NATIVE_RANKS_LIST.get(rankType).size();
	}

	/** 获取某一类型的排行总页数 */
	public int getSumPageByRankType(int rankType) {
		// 没开放或不存在的排行
		if (NATIVE_RANKS_LIST.get(rankType) == null) {
			return 0;
		}
		int size=NATIVE_RANKS_LIST.get(rankType).size() ;
		return size%PAGINAL_NUM==0? size/ PAGINAL_NUM: size/ PAGINAL_NUM+ 1;
	}
}
