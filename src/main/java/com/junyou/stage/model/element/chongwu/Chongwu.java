package com.junyou.stage.model.element.chongwu;

import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.gameconfig.export.PathNodeSize;
import com.junyou.stage.model.core.element.AbsElement;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.PointTakeupType;
import com.junyou.stage.model.core.state.IStateManager;
import com.junyou.stage.model.element.role.IRole;

public class Chongwu extends AbsElement{
	public Chongwu(Long id, String name) {
		super(id, name);
	}

	private int configId;
	private int speed;
	private IRole role;
	private Object[] msgData;

	public int getConfigId() {
		return configId;
	}

	public void setConfigId(int configId) {
		this.configId = configId;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	@Override
	public IStateManager getStateManager() {
		return null;
	}

	@Override
	public Object getStageData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getCamp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PathNodeSize getPathNodeSize() {
		return PathNodeSize._1X;
	}

	@Override
	public ElementType getElementType() {
		return ElementType.CHONGWU;
	}

	@Override
	public short getEnterCommand() {
		return ClientCmdType.AOI_CHONGWU;
	}

	@Override
	public PointTakeupType getTakeupType() {
		return PointTakeupType.BEING;
	}

	@Override
	public Object getMsgData() {
		if(msgData == null){
			/**
			 * 	
			0	Guid
			1	int	x坐标
			2	int	y坐标
			3	移动速度
			4	主人名字
			5	主人GUID
			6	宠物id
			 */
			msgData = new Object[]{
					getId(),
					getPosition().getX(),
					getPosition().getY(),
					getSpeed(),
					getRole().getName(),
					getRole().getId(),
					getConfigId()
			};
		}else{
			int index = 0;
			msgData[index++] = getId();
			msgData[index++] = getPosition().getX();
			msgData[index++] = getPosition().getY();
			msgData[index++] = getSpeed();
			msgData[index++] = getRole().getName();
			msgData[index++] = getRole().getId();
			msgData[index++] = getConfigId();
		}
		return msgData;
	
	}
	@Override
	public void deadHandle(IHarm harm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leaveStageHandle(IStage stage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterStageHandle(IStage stage) {
		// TODO Auto-generated method stub
		BusMsgSender.send2One(getRole().getId(), ClientCmdType.CHONGWU_MOVE_SYN_4_ROLE, new Object[]{getPosition().getX(),getPosition().getY()});
	}

	public IRole getRole() {
		return role;
	}

	public void setRole(IRole role) {
		this.role = role;
	}

}
