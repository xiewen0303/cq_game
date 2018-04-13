package com.junyou.stage.model.stage.wenquan;

import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfig;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.element.role.Role;
import com.junyou.stage.model.stage.fuben.PublicFubenStage;
import com.junyou.stage.tunnel.StageMsgSender;

public class WenquanStage extends PublicFubenStage{
	private DingShiActiveConfig dingShiActiveConfig;
	
	
	public DingShiActiveConfig getDingShiActiveConfig() {
		return dingShiActiveConfig;
	}


	public void setDingShiActiveConfig(DingShiActiveConfig dingShiActiveConfig) {
		this.dingShiActiveConfig = dingShiActiveConfig;
	}


	public WenquanStage(String id, Integer mapId,Integer lineNo, AOIManager aoiManager, PathInfoConfig pathInfoConfig) {
		super(id, mapId, lineNo, aoiManager, pathInfoConfig,StageType.WENQUAN);
	}
	

	@Override
	public void enter(IStageElement element, int x, int y) {
		super.enter(element, x, y);
		if(ElementType.isRole(element.getElementType())){
			Role role = (Role)element;
			role.startWenquanAddExpZhenqiSchedule();
		}
	}
	
	public void leave(IStageElement element) {
		super.leave(element);
	}
	

	@Override
	public boolean isAddPk() {
		return false;
	}

	@Override
	public boolean isCanRemove(){
		return !isOpen() && (getAllRoleIds() == null || getAllRoleIds().length == 0);
	}

	public void enterNotice(Long userRoleId) {
		StageMsgSender.send2One(userRoleId, ClientCmdType.ENTER_WENQUAN, new Object[] { AppErrorCode.SUCCESS,
				dingShiActiveConfig.getCalcEndSecondTime() });
	}
	
	public void exitNotice(Long userRoleId) {
		StageMsgSender.send2One(userRoleId, ClientCmdType.EXIT_WENQUAN, AppErrorCode.OK);
	}
	
	@Override
	public boolean isCanHasTangbao() {
		return false;
	}
	/**
	 * 是否可以携带宠物
	 * @return
	 */
	public boolean isCanHasChongwu(){
		return false;
	}
	public boolean isFubenMonster() {
		return false;
	}
	public boolean isCanUseShenQi() {
		return false;
	}
	public boolean isCanPk() {
		return false;
	}
	public boolean isCanFeijian(){
		return false;
	}
	public boolean isCanDazuo(){
		return false;
	}
	public boolean isCanJump(){
		return false;
	}
	@Override
	public Short getSameMoCmd() {
		return ClientCmdType.AOI_WENQUAN_SAME_CMD;
	}
	/**
	 * 能够变换妖神
	 * @return
	 */
	public boolean isCanChangeYaoShen(){
		return false;
	}
	
	public boolean isCanChangeShow(){
		return false;
	}
}