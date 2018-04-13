package com.junyou.stage.model.element.monster;

import com.junyou.utils.datetime.GameSystemTime;

/**
 * BOSS vo
 *@author  DaoZheng Yuan
 *@created 2013-7-26下午2:10:06
 */
public class BossVo {

	private String monsterId;
	
	private String mapId;
	
	private int x;
	private int y;
	
	//刷新出来的时间
	private Long refreshTime;
	//死亡的时间
	private Long deadTime;
	//刷新间隔时间
	private int jgTime;
	
	//状态  true:活着状态 ,false:死亡状态
	private boolean state;
	
	/**
	 * 是否有击杀者
	 */
	private boolean haveKiller = false;
	
	private String killerRoleId;
	private String killerName;
	private int killerConfigId;

	public BossVo(String monsterId, String mapId, int x, int y,int produceDelay) {
		super();
		this.monsterId = monsterId;
		this.mapId = mapId;
		this.x = x;
		this.y = y;
				
		refreshTime = GameSystemTime.getSystemMillTime();
		this.jgTime =  produceDelay;
		
		state = true;
	}
	
	/**
	 * 获取数据
	 * @return
	 */
	public Object getMsgData(){
		
		/**
		 * [0:String(怪物配置id),1:number(BOSS出现的剩余时间,0表示已出现)],...
		 */
		Long tmpTime = 0L;
		if(refreshTime == null){
			tmpTime = deadTime + jgTime;
		}
		
		return new Object[]{ getMonsterId(),tmpTime};
	}
	
	/**
	 * 获取数据
	 * @return
	 */
	public Object getMsgData_(){
		/**
		 * (0:String(怪物ID),1:Array(如果没有击杀者或者怪物存活，传null)[0:String(击杀者Guid),1:String(击杀者名字),2:int(击杀者职业)],2:Number(怪物刷新时间，怪物活着传0
		 */
		
		Object[] killData = null;
		Long tmpTime = 0L;
		if(refreshTime == null){
			tmpTime = deadTime + jgTime;
		}
		
		if(isHaveKiller()){
			killData = new Object[]{killerRoleId,killerName,killerConfigId};
		}
		
		return new Object[]{
				getMonsterId(),
				killData,
				tmpTime
		};
	}
	

	/**
	 * 是否有击杀者
	 * @return
	 */
	public boolean isHaveKiller(){
		return haveKiller;
	}
	
	/**
	 * 活着状态
	 */
	public void changeLiveState(){
		state = true;
		refreshTime = GameSystemTime.getSystemMillTime();
		deadTime = null;
		
		haveKiller = false;
	}

	/**
	 * 死亡状态
	 */
	public void changeDeadState(String roleId,String roleName,int configId){
		this.state = false;
		
		refreshTime = null;
		deadTime = GameSystemTime.getSystemMillTime();
				
		if(roleId != null){
			haveKiller = true;
			setKill(roleId,roleName,configId);
		}
	}
	
	/**
	 * 设置击杀者
	 * @param roleId
	 * @param roleName
	 * @param configId
	 */
	private void setKill(String roleId,String roleName,int configId){
		this.killerRoleId = roleId;
		this.killerName = roleName;
		this.killerConfigId = configId;
	}
	
	
	
	
	public String getKillerRoleId() {
		return killerRoleId;
	}


	public String getKillerName() {
		return killerName;
	}


	public int getKillerConfigId() {
		return killerConfigId;
	}


	public String getMonsterId() {
		return monsterId;
	}





	public String getMapId() {
		return mapId;
	}





	public int getX() {
		return x;
	}





	public int getY() {
		return y;
	}


	/**
	 * 获取BOSS状态
	 * @return
	 */
	public boolean getState(){
		return state;
	}
}
