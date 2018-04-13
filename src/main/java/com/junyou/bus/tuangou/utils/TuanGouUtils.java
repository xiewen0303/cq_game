package com.junyou.bus.tuangou.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhongdian
 * 2016-1-12 下午3:00:10
 */
public class TuanGouUtils {

	private  static Map<Integer, List<Integer>> numMap = new HashMap<>(); //数字管理
	private  static Map<Integer, Integer> jMap = new HashMap<>(); //中间数字管理
	
	private static  Map<Integer, Integer> isQuert =  new HashMap<>();
	
	public static void clearnData(){
		numMap = new HashMap<>(); //数字管理
		jMap = new HashMap<>(); //中间数字管理
		isQuert =  new HashMap<>();
	}
	
	public static Integer getIsQuert(Integer subId){
		return isQuert.get(subId);
	}
	
	/**
	 * 启动的时候初始化1-100,已经roll出来的需要移除掉，保证1-100每个数字只能出现一次
	 * @param subId
	 * @param nlist
	 */
	public static void initNumberList(Integer subId,List<Integer> nlist,Integer number,Integer jId){
		Integer q= isQuert.get(subId);
		if(q == null){
			List<Integer> list = numMap.get(subId);
			if(list == null){
				list= new ArrayList<>();
				for (int i = 1; i <= number; i++) {
					list.add(i);
				}
			}
			if(nlist != null && nlist.size() > 0){
				for (int i = 0; i < nlist.size(); i++) {
					/*int cc = list.indexOf(nlist.get(i));
					list.remove(cc);*/
					list.remove(nlist.get(i));
				}
			}
			numMap.put(subId, list);
			jMap.put(subId, jId);
			isQuert.put(subId, 1);
		}
	}
	
	/**
	 * 设置中奖数字
	 */
	public static void setJmapByJid(Integer subId,Integer jId){
		jMap.put(subId, jId);
	}
	/**
	 * 获取中奖数字
	 * @param subId
	 * @return
	 */
	public static Integer getJmapJid(Integer subId){
		Integer id = jMap.get(subId);
		if(id == null){
			return 0;
		}
		return id;
	}
	/**
	 * 获取剩余可购买次数
	 * @param subId
	 * @return
	 */
	public static int getLeftCount(int subId){
		List<Integer> list = numMap.get(subId);
		if(list == null || list.size() <= 0){
			return 0;
		}
		return list.size();
	}
	/**
	 * 获取list中剩余的数字中的一个
	 * @return
	 */
	public static int getRollNumber(int subId){
		List<Integer> list = numMap.get(subId);
		if(list == null || list.size() <= 0){
			return 0;
		}
		int s = list.size();
		int j = (int) (Math.random() * s);
		int aa = list.get(j);
		list.remove(j);
		return aa;
	}
	
	
	
	
	public static void main(String[] args) {
		List<Integer> l = new ArrayList<>();
		for (int i = 1; i <= 10; i++) {
			l.add(i);
		}
		Map<Integer, List<Integer>> numMap = new HashMap<>();
		numMap.put(1, l);
		/*int s = l.size();
		int j = (int) (Math.random() * s);
		int cd = l.get(j);
		l.remove(j);
		System.out.println(cd);
		System.out.println("=----------------=");*/
		List<Integer> ll = numMap.get(1);
		List<Integer> c = new ArrayList<>();
		c.add(5);
		c.add(2);
		c.add(3);
		
		
		for (int i = 0; i < c.size(); i++) {
			/*int cc = l.indexOf(c.get(i));
			System.out.println(cc);*/
			ll.remove(new Integer(c.get(i)));
			
		}
		List<Integer> lll = numMap.get(1);
		System.out.println("=----------------=");
		for (int i = 0; i < lll.size(); i++) {
			System.out.println(lll.get(i));
		}
	}
	
	
	
	
	
}
