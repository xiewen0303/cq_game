package com.junyou.stage.model.stage.tanbao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.gameconfig.publicconfig.configure.export.TanBaoPublicConfig;
import com.junyou.stage.configure.export.impl.SkillConfigExportService;
import com.junyou.stage.model.core.help.GongGongServiceHelper;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOI;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.skill.SkillManager;
import com.junyou.stage.model.stage.fuben.PublicFubenStage;
import com.junyou.stage.tunnel.StageMsgSender;

/**
 * 探宝场景
 * @author LiuYu
 * @date 2015-6-16 下午4:14:04
 */
public class TanBaoStage  extends PublicFubenStage{
	
	public TanBaoStage(String id, Integer mapId, Integer lineNo,AOIManager aoiManager, PathInfoConfig pathInfoConfig) {
		super(id, mapId, lineNo, aoiManager, pathInfoConfig, StageType.XIANGONG);
	}
	
	private void initSkill(IRole role){
		role.clearSkills();
		List<String> tanbaoSkillIds = SkillConfigExportService.getTanbaoSkillIds();
		if(tanbaoSkillIds != null && tanbaoSkillIds.size() > 0){
			for (String skillId : tanbaoSkillIds) {
				ISkill skill = SkillManager.getManager().getSkill(skillId, 1);
				role.addSkill(skill);
			}
		}
	}
	private void initExpSchedule(IRole role){
		TanBaoPublicConfig tanBaoPublicConfig = GongGongServiceHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_TANBAO);
		if(tanBaoPublicConfig != null){
			role.tanBaoExpSchedule(tanBaoPublicConfig.getAddExpTime());
		}
	}

	@Override
	public boolean isCanFeijian() {
		return false;
	}

	@Override
	public boolean isCanDazuo() {
		return false;
	}
	
	public boolean isCanJump(){
		return false;
	}

	@Override
	public Short getSameMoCmd() {
		return ClientCmdType.AOI_XIANGONG_SAME_CMD;
	}

	@Override
	public boolean isCanHasTangbao() {
		return false;
	}

	@Override
	public void enter(IStageElement element, int x, int y) {
		super.enter(element, x, y);
		if(ElementType.isRole(element.getElementType())){
			IRole role = (IRole)element;
			initSkill(role);
			initExpSchedule(role);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IStageElement> List<T> getElementsByPoint(Point from, ElementType elementType){
		AOI curCenterAoi = aoiManager.getAOI( aoiManager.getBabyCenterAOIPoint( getPointInAoi(from) ) );
		if(curCenterAoi == null){
			return null;
		}
		Collection<IStageElement> elements = curCenterAoi.getElements(elementType);
		if(elements == null){
			return null;
		}
		List<T> list = new ArrayList<>();
		for (IStageElement element : elements) {
			int x = element.getPosition().getX() - from.getX();
			int y = element.getPosition().getY() - from.getY();
			if(x < 2 && x > -2 && y < 2 && y > -2){
				list.add((T)element);
			}
		}
		return list;
	}

	@Override
	public boolean isCanRemove() {
		return !isOpen() && getAllElements(ElementType.ROLE).size() < 1;
	}

	@Override
	public Short getBackFuHuoCmd() {
		return ClientCmdType.FUHUO_POINT_FUHUO;
	}
	
	public void enterNotice(Long userRoleId) {
		StageMsgSender.send2One(userRoleId, ClientCmdType.ENTER_TANBAO, TanBaoManager.getManager().getEnterSucessMsg());
	}
	
	public void exitNotice(Long userRoleId) {
		StageMsgSender.send2One(userRoleId, ClientCmdType.EXIT_TANBAO, AppErrorCode.OK);
	}

	@Override
	public boolean isFubenMonster() {
		return true;
	}
	
	@Override
	public boolean isCanChangeSkill() {
		return false;
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
	/**
	 * 是否可以携带宠物
	 * @return
	 */
	public boolean isCanHasChongwu(){
		return false;
	}

	@Override
	public boolean canFuhuo() {
		return false;
	}

	
	
}
