package com.junyou.gameconfig.shuaguai.configure.export;
 
import java.util.ArrayList;
import java.util.List;

import com.junyou.utils.datetime.DatetimeUtil;
import com.kernel.data.dao.AbsVersion;

/**
 * 定时刷怪表 
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-4-24 下午4:45:02
 */

public class DingShiShuaGuaiConfig extends AbsVersion  {

	private String name;

	private int id;

	private int line; //0:全线刷; 1：只刷一线

	private Integer huodong;//活动ID  通过活动控制野外boss是否刷出
	
	private int[] time1; //怪物刷出时间

	private int[] time2; //怪物回收时间

	private Integer gonggao; //公告code码
	
	private int count;	//怪物数量
	
	private String monsterId1; //怪物ID
	
	private String monsterId2; //怪物ID
	
	private int type; //1：有伤害排行的怪
	
	private String monsterFakeId;//假怪物
	
	private String bianqiang;//是否关联boss变强
	
	private int type1; //是否有排行
	
	private boolean qingguai;//结束时是否清怪
	
	private List<Integer> mapId;//怪物出现的地图ID
	
	private int[] xlzb;//寻路坐标
	
	private List<int[]> randomCoord;//随机产生怪物的坐标
	
	private int[] beginTime; //开始时间
	private int[] endTime;	 //结束时间
	
	public boolean inBossTime(){
		if(type != 4){
			return true;
		}
		if(time1 == null || time2 == null){
			return true;
		}
		
		long nowTime = System.currentTimeMillis();
		long begin = DatetimeUtil.getCalcTimeKuang(0, time1[0], time1[1]);
		long end = DatetimeUtil.getCalcTimeKuang(0, time2[0], time2[1]);
		if(nowTime >= begin &&  nowTime <= end){
			return true;
		}
		return false;
	}
	
	public boolean isNoBoss(){
		if(beginTime == null || endTime == null){
			return false;
		}
		
		if(type != 4){
			return false;
		}
		long nowTime = System.currentTimeMillis();
		
		long begin = DatetimeUtil.getCalcTimeKuang(0, beginTime[0], beginTime[1]);
		long end = DatetimeUtil.getCalcTimeKuang(0, endTime[0], endTime[1]);
		
		if(begin > end){
			begin -= 24*60*60*1000;
		}
		
		if(nowTime > begin && nowTime < end){
			return true;
		}
		return false;
	}
	
	public void setBeginTime(int[] beginTime) {
		this.beginTime = beginTime;
	}

	public void setEndTime(int[] endTime) {
		this.endTime = endTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public Integer getHuodong() {
		return huodong;
	}

	public void setHuodong(Integer huodong) {
		this.huodong = huodong;
	}

	public int[] getTime1() {
		return time1;
	}

	public void setTime1(int[] time1) {
		this.time1 = time1;
	}

	public int[] getTime2() {
		return time2;
	}

	public void setTime2(int[] time2) {
		this.time2 = time2;
	}

	public Integer getGonggao() {
		return gonggao;
	}

	public void setGonggao(Integer gonggao) {
		this.gonggao = gonggao;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getMonsterId1() {
		return monsterId1;
	}

	public void setMonsterId1(String monsterId1) {
		this.monsterId1 = monsterId1;
	}

	public String getMonsterId2() {
		return monsterId2;
	}

	public void setMonsterId2(String monsterId2) {
		this.monsterId2 = monsterId2;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMonsterFakeId() {
		return monsterFakeId;
	}

	public void setMonsterFakeId(String monsterFakeId) {
		this.monsterFakeId = monsterFakeId;
	}

	public String getBianqiang() {
		return bianqiang;
	}

	public void setBianqiang(String bianqiang) {
		this.bianqiang = bianqiang;
	} 

	public int getType1() {
		return type1;
	}

	public void setType1(int type1) {
		this.type1 = type1;
	}

	public List<Integer> getMapId() {
		return mapId;
	}

	public void setMapId(List<Integer> mapId) {
		this.mapId = mapId;
	}

	public int[] getXlzb() {
		return xlzb;
	}

	public void setXlzb(int[] xlzb) {
		this.xlzb = xlzb;
	}

	public List<int[]> getRandomCoord() {
		return  new ArrayList<>(randomCoord);
	}

	public void setRandomCoord(List<int[]> randomCoord) {
		this.randomCoord = randomCoord;
	}

	public boolean isQingguai() {
		return qingguai;
	}

	public void setQingguai(boolean qingguai) {
		this.qingguai = qingguai;
	}
	
	
}
