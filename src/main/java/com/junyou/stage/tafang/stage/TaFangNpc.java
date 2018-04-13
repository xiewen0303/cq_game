package com.junyou.stage.tafang.stage;

import java.util.List;

import com.junyou.cmd.ClientCmdType;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.stage.model.element.monster.Monster;
import com.junyou.utils.collection.ReadOnlyList;

/**
 * @author LiuYu
 * 2015-10-10 下午2:18:12
 */
public class TaFangNpc extends Monster{

	public TaFangNpc(Long id, MonsterConfig monsterConfig) {
		super(id, null, monsterConfig);
	}
	/**可攻击列*/
	private List<Integer> attLine;
	/**所在位置id*/
	private int positionId;

	
	
	public List<Integer> getAttLine() {
		return attLine;
	}

	public void setAttLine(List<Integer> attLine) {
		this.attLine = new ReadOnlyList<>(attLine);
	}
	
	public int getPositionId() {
		return positionId;
	}

	public void setPositionId(int positionId) {
		this.positionId = positionId;
	}

	@Override
	public boolean isTaFangNpc(){
		return true;
	}
	
	@Override
	public short getEnterCommand() {
		return ClientCmdType.AOI_TAFANG_NPC_ENTER;
	}
	
	@Override
	public Object getMsgData() {
		if(msgData == null){
			msgData = new Object[]{
					getId(),//0 Number 唯一标识Guid 
					getMonsterId(),//1 String 怪物配置标识 
					getPosition().getX(),//2 int x坐标 
					getPosition().getY(),//3 int y坐标 
					getPositionId()
			};
		}else{
			msgData[2] = getPosition().getX();
			msgData[3] = getPosition().getY();
		}
		return msgData;
	}
}
