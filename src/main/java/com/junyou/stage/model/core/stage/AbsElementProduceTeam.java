/**
 * 
 */
package com.junyou.stage.model.core.stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.element.monster.Monster;
import com.junyou.utils.lottery.Lottery;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-16上午10:35:22
 */
public abstract class AbsElementProduceTeam implements IElementProduceTeam {
	
	
	protected IStage stage;
	
	protected String teamId;
	protected ElementType elementType;
	private String elementId;
	
	private int limit;
	private List<Integer[]> availableXyPoints;
	
	private Map<Long,Integer[]> products = new HashMap<Long, Integer[]>();
	private int count;
	
	private boolean isSchedule = false;
	
	private Integer delay;
	
	
	public AbsElementProduceTeam(){
		
	}
	
	
	protected Map<Long,Integer[]> getProducts() {
		return products;
	}
	
	/**
	 * 
	 */
	public AbsElementProduceTeam(String teamId,ElementType elementType,int limit,String elementId,List<Integer[]> xyPoints,Integer produceDelay) {
		
		this.teamId = teamId;
		this.elementType = elementType;
		this.elementId = elementId;
		
		this.limit = limit;
		
		if(xyPoints != null && xyPoints.size() > 0){
			//每次重新new一个坐标集合对象
			this.availableXyPoints = new ArrayList<>(xyPoints);
		}else{
			//容错处理，保证地图能正常创建，只是创建的场景对象在一个不正常的坐标点
			this.availableXyPoints = new ArrayList<>();
			this.availableXyPoints.add(new Integer[]{50,50});
		}
		
		if(limit <= 0 || xyPoints.size() < limit){
			this.limit = xyPoints.size();
		}
		
		checkParams(this.limit,xyPoints);
		
		this.delay = produceDelay;
	}


	private void checkParams(int limit, List<Integer[]> xyPoints) {
		if(limit == 0 || limit > xyPoints.size()){
			throw new RuntimeException("teamId:" + teamId + " [limit:]" + limit + " [ xyPoints:]" + xyPoints.size());
		}
		
	}

	@Override
	public void produce(int needCount) {
		
		int rest = limit - this.count;
		needCount = needCount > rest ? rest : needCount;
		
		while(needCount-- > 0){
			
			IStageElement element = create(teamId,elementId);
			if(element != null){
				
				Integer[] xyPoint = rollBornPosition();
				setElementFaceTo(element,xyPoint);
				element.setBornPosition(xyPoint[0], xyPoint[1]);
				
				this.count++;
				products.put(element.getId(),xyPoint);
				stage.enter(element,element.getBornPosition().getX(), element.getBornPosition().getY());
				
				//开启AI (@ydz-2015-1-10 暂时关闭怪物创建立即开启AI)
//				elementAiStart(element);
				
//				bossAddManage(element);
			}
		}
		
	}
	
	
//	private void elementAiStart(IStageElement element){
//		
//		if(ElementType.isMonster(element.getElementType())){
//			Monster monster = (Monster) element;
//			
//			if(!monster.isNoMove() || monster.isAttractRed()){
//				//开启AI
//				((IMonster)element).getAi().schedule(1000, TimeUnit.MILLISECONDS);
//			}
//		}
//	}
	
	/**
	 * 地区首领加入管理 
	 * @param element
	 */
	private void bossAddManage(IStageElement element){
		try {
			
			if(element instanceof Monster){
				Monster monster = (Monster) element;
//				if(StageConfigureHelper.getBossTiaoZhanConfigExportService().isShowBoss(monster.getMonsterId())){
////					boolean flag = BossManage.isFirstRefresh(monster.getMonsterId());
//					
//					String mapId = monster.getStage().getMapId();
//					int x = monster.getBornPosition().getX();
//					int y = monster.getBornPosition().getY();
//					
//					BossManage.addBoss(monster.getMonsterId(),mapId,x , y,delay);
//				}
			}
			
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
	
	/**
	 * 设置创建怪物面向
	 * @param element
	 */
	private void setElementFaceTo(IStageElement element,Integer[] xyPoint){
		if(ElementType.isMonster(element.getElementType())){
			if(xyPoint.length > 2){
				int faceTo = xyPoint[2];
				
				Monster m = (Monster) element;
				m.setFaceTo(faceTo);
			}
		}
	}

	/**
	 * @param
	 */
	private Integer[] rollBornPosition() {
		int rollVal = Lottery.roll(availableXyPoints.size());
		return availableXyPoints.remove(rollVal);
	}

	public Integer getDelay() {
		return delay;
	}


	/**
	 * 创建元素
	 * @param teamId 队伍编号
	 * @param elementId 元素编号
	 */
	protected abstract IStageElement create(String teamId,String elementId);

	@Override
	public void reset() {
		clear();
		produce(getLimit());
	}

	@Override
	public void clear() {
		for(Long stageElementId : products.keySet()){
			IStageElement element = stage.getElement(stageElementId, elementType);
			if(null != element){
				stage.leave(element);
			}
		}
	}

	@Override
	public int getLimit() {
		return limit;
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public String getId() {
		return teamId;
	}
	
	@Override
	public void retrieve(IStageElement element) {
		Integer[] xyPoint = products.remove(element.getId());
		availableXyPoints.add(xyPoint);
		
		this.count--;
		stage.leave(element);
		
		if(!isSchedule){
			schedule();
			isSchedule = true;
		}
	}
	
	/**
	 * 回收但是不启动刷新定时
	 * @param element
	 */
	public void retrieveNoSchedule(IStageElement element) {
		Integer[] xyPoint = products.remove(element.getId());
		availableXyPoints.add(xyPoint);
		this.count--;
		stage.leave(element);
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}

	@Override
	public void setStage(IStage stage) {
		this.stage = stage;
	}

	@Override
	public void produceAll() {
		produce(getLimit());
		
		randomXunlouSchedule();
		
		isSchedule = false;
		
	}
	
	
}
