package com.junyou.bus.xiaofei.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.bus.xiaofei.entity.RefabuXiaofei;


/**
 * 消费管理管理
 *@author  ZHONGDIAN
 *@created 2013-4-10下午9:11:22
 */
public class XiaoFeiManager{
	
	private static  Map<Integer, List<RefabuXiaofei>> xiaofeis =  new HashMap<Integer, List<RefabuXiaofei>>();
	
	public static void clearXiaoFeiManager(){
		xiaofeis =  new HashMap<Integer, List<RefabuXiaofei>>(); 
	}
	
	public static void clearRank(int subId){
		xiaofeis.remove(subId);
	}
	
	/**
	 * 是否包含数据
	 * @return
	 */
	public  boolean isConstantData(int subId){
		List<RefabuXiaofei> list = xiaofeis.get(subId);
		if(list.size() == 0){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * 设置管理器数据
	 * @param xiaofei
	 * @param isClear
	 */
	public static  void setRanks(int suibId,List<RefabuXiaofei> xiaofei,boolean isClear){
		if(isClear){
			clearRank(suibId);
		}
		if(xiaofei != null){
			xiaofeis.put(suibId, xiaofei);
		}else{
			xiaofeis.put(suibId, new ArrayList<RefabuXiaofei>());
		}
	}
	
	/**
	 * 获取管理器数据
	 * @param xiaofei
	 * @param isClear
	 */
	public static  List<RefabuXiaofei> getRanks(int subId){
		if(xiaofeis.get(subId) == null){
			return null;
		}
		return xiaofeis.get(subId);
	}
	/**
	 * 
	 * @param begin 开始位置
	 * @param num  个数
	 * @return
	 */
	public static List<RefabuXiaofei> getRanksByNum(int subId,int begin,int num){
		
		List<RefabuXiaofei> xfList = xiaofeis.get(subId);
		
		if(xfList != null){
			int kaishi = begin ;//数据开始位
			int geshu = num;
			int size = 0;//循环遍历条件
			if(geshu >= xfList.size()){
				size = xfList.size()-1;
			}else{
				size = geshu;
			}
			List<RefabuXiaofei> list = new ArrayList<RefabuXiaofei>();
			for (int i = kaishi; i <= size; i++) {
				list.add(xfList.get(i));
			}
			return list;
			
		}
		return null;
	}
	
	/**
	 * 获取管理器玩家消费信息
	 * @param userRoleId
	 * @return
	 */
	public static RefabuXiaofei getActivityXiaoFeiByRoleId(int subId,Long userRoleId){
		List<RefabuXiaofei> xfList = xiaofeis.get(subId);
		if(xfList == null){
			return null;
		}
		for (int i = 0; i < xfList.size(); i++) {
			RefabuXiaofei xiaofei = xfList.get(i);
			if(userRoleId.equals(xiaofei.getUserRoleId())){
				return xiaofei;
			}
		}
		return null;
	}
	/**
	 * 删除管理器玩家消费信息
	 * @param userRoleId
	 * @return
	 */
	public static void delActivityXiaoFeiByRoleId(int subId,Long userRoleId){
		List<RefabuXiaofei> xfList = xiaofeis.get(subId);
		if(xfList == null){
			return;
		}
		for (int i = 0; i < xfList.size(); i++) {
			RefabuXiaofei xiaofei = xfList.get(i);
			if(userRoleId.equals(xiaofei.getUserRoleId())){
				xfList.remove(i);
			}
		}
		return;
	}
	
	/**
	 * 根据指定名次获取消费信息
	 * @param num
	 * @return
	 */
	public static RefabuXiaofei getActivityXiaoFeiByNum(int subId,int num){
		List<RefabuXiaofei> xfList = xiaofeis.get(subId);
		if(xfList != null && xfList.size() >= num){
			return xfList.get(num-1);
		}
		return null;
	}
	
	/**
	 * 判断是否在榜中 是否上榜 0：未上榜，1：上榜
	 * @param userRoleId
	 * @return
	 */
	public static int getXiaoFeiByRoleId(int subId,Long userRoleId){
		List<RefabuXiaofei> xfList = xiaofeis.get(subId);
		if(xfList == null){
			return 0;
		}
		for (int i = 0; i < xfList.size(); i++) {
			RefabuXiaofei xiaofei = xfList.get(i);
			if(userRoleId.equals(xiaofei.getUserRoleId())){
				return 1;
			}
		}
		return 0;
	}

	/**
	 * 获取玩家名次
	 * @param userRoleId
	 * @return
	 */
	public static int getUserPaiMing(int subId,Long userRoleId){
		List<RefabuXiaofei> xfList = xiaofeis.get(subId);
		if(xfList == null){
			return 0;
		}
		for (int i = 0; i < xfList.size(); i++) {
			RefabuXiaofei xiaofei = xfList.get(i);
			if(userRoleId.equals(xiaofei.getUserRoleId())){
				return i+1;
			}
		}
		return 0;
	}
	
	/**
	 * 获取玩家已充值元宝数
	 * @param userRoleId
	 * @return
	 */
	public static int getUserXfGold(int subId,Long userRoleId){
		List<RefabuXiaofei> xfList = xiaofeis.get(subId);
		if(xfList == null){
			return 0;
		}
		for (int i = 0; i < xfList.size(); i++) {
			RefabuXiaofei xiaofei = xfList.get(i);
			if(userRoleId.equals(xiaofei.getUserRoleId())){
				return xiaofei.getXfGold();
			}
		}
		return 0;
	}
	
	/**
	 * 获取排行长度
	 * @return
	 */
	public static int getRankSize(int subId){
		List<RefabuXiaofei> xfList = xiaofeis.get(subId);
		if(xfList == null){
			return 0;
		}
		return xfList.size();
	}


}
