package com.junyou.stage.collect.manage;

import java.util.List;
import com.junyou.stage.collect.element.BoxProduceTeam;
import com.junyou.constants.GameConstants;
import com.kernel.schedule.ScheduleExecutor;


/**活动管理器*/
public class CollectBoxActivityManager {
	
	/** 活动进行标识 */
	private volatile boolean activityUnderway = false;
	
	private int activityId;
	
	private String stageId;

	/**可投放的坐标集*/
	private List<Integer[]> canPutPoints;
	
	/**一个场景最大投放量*/
	private int boxLimit;
	/**刷新间隔*/
	private long flushInterval;
	/**活动结束时间*/
	private long endTime;
	/**统一回收时间*/
	private long vanishTime;
	
	private ScheduleExecutor scheduleExecutor;
	
	private BoxProduceTeam boxTeam ;
	
/*	*//**
	 * 已投放的箱子容器 
	 * pointId: x|y
	 * (目前只有单场景)
	 * *//*
	private Set<String> existingBoxContainer;*/

	
	private static CollectBoxActivityManager collectBoxManager;
	
	private CollectBoxActivityManager() {
		/*this.existingBoxContainer = Collections
				.synchronizedSet(new HashSet<String>());*/
		this.stringBuilder = new StringBuilder();
		this.scheduleExecutor=new ScheduleExecutor();

	}

	public static CollectBoxActivityManager getManager() {
		if (collectBoxManager == null) {
			collectBoxManager = new CollectBoxActivityManager();
		}
		return collectBoxManager;
	}

	private StringBuilder stringBuilder;

/*	*//** 加入到坐标容器 *//*
	public void addBox(ActivityBox box) {
		String pointId = builderStrPointId(box.getBornPosition().getX(),box.getBornPosition().getY());
		existingBoxContainer.add(pointId);
	}*/
/*	*//** 从容器删除 *//*
	public void removeBox(ActivityBox box){
		String pointId = builderStrPointId(box.getBornPosition().getX(),box.getBornPosition().getY());
		existingBoxContainer.remove(pointId);
	}*/

	/** 构造字符类型的坐标id */
	public String builderStrPointId(int x, int y) {
		String pointId = stringBuilder.append(x)
				.append(GameConstants.CONFIG_SPLIT_CHAR)
				.append(y).toString();
		stringBuilder.delete(0, stringBuilder.length());
		return pointId;
	}

	/** 构造hash过的坐标id */
	public int builderIntPointId(int x, int y) {
		String pointId = stringBuilder.append(x)
				.append(GameConstants.CONFIG_SPLIT_CHAR)
				.append(y).toString();
		stringBuilder.delete(0, stringBuilder.length());
		return pointId.hashCode();
	}

/*	*//** 此坐标是否有箱子 *//*
	public boolean hasBoxforPoint(int x, int y) {
		String pointId = builderStrPointId(x, y);
		return existingBoxContainer.contains(pointId);

	}*/

	public List<Integer[]> getCanPutPoints() {
		return canPutPoints;
	}

	public void setCanPutPoints(List<Integer[]> canPutPoints) {
		this.canPutPoints = canPutPoints;
	}

	public int getBoxLimit() {
		return boxLimit;
	}

	public void setBoxLimit(int boxLimit) {
		this.boxLimit = boxLimit;
	}
	
	public long getVanishTime() {
		return vanishTime;
	}

	public void setVanishTime(long vanishTime) {
		this.vanishTime = vanishTime;
	}
	
	public String getStageId() {
		return stageId;
	}

	public void setStageId(String stageId) {
		this.stageId = stageId;
	}
/*	*//**随机一个可投放的坐标*//*
	public  Integer[] randomPoint(){
		int rollVal = Lottery.roll(canPutPoints.size());
		 Integer[] point = canPutPoints.get(rollVal);
		if(hasBoxforPoint(point[0], point[1])){
			randomPoint();
		}
		return point;
		
	}*/
	
/*	*//**当前存在箱子数量*//*
	public int currExistAmount(){
		return this.existingBoxContainer.size();
	}*/
	
	public void activityStart(){
		this.activityUnderway=true;
	}
	
	public void activityEnd(){
		this.activityUnderway=false;
	}
	
	public boolean activityIsUnderway(){
		 return activityUnderway;
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public long getFlushInterval() {
		return flushInterval;
	}

	public void setFlushInterval(long flushInterval) {
		this.flushInterval = flushInterval;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public ScheduleExecutor getScheduleExecutor() {
		return scheduleExecutor;
	}

	public BoxProduceTeam getBoxTeam() {
		return boxTeam;
	}

	public void setBoxTeam(BoxProduceTeam boxTeam) {
		this.boxTeam = boxTeam;
	}
	
	
}
