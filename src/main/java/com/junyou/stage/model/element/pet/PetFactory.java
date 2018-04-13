package com.junyou.stage.model.element.pet;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.publicconfig.configure.export.TangbaoPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.helper.PublicConfigureHelper;
import com.junyou.stage.model.core.attribute.BaseAttributeType;
import com.junyou.stage.model.core.attribute.BaseFightAttribute;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.state.StateManager;
import com.junyou.stage.model.element.componentlistener.FightListener;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.fight.statistic.PetFightStatistic;
import com.junyou.stage.model.hatred.PetHatredManager;
import com.junyou.stage.model.skill.PublicCdManager;
import com.junyou.stage.model.skill.SkillManager;
import com.junyou.stage.model.skill.buff.BuffManager;
import com.junyou.stage.tunnel.StageMsgSender;
import com.kernel.gen.id.IdFactory;

@Component
public class PetFactory {
    
    @Deprecated
	public static Pet create(IRole role,Map<String,Long> levelAttribute,Map<String,Long> xianjianAttribute,Map<String,Long> zhanjiaAttribute,Map<String,Long> tbXinwenAttribute,Map<String,Long> tbTianYuAttribute,Map<String, Long> tbWuxingBaseAttrs,boolean isCreate,Long tbId){
		TangbaoPublicConfig publicConfig = PublicConfigureHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_TANGBAO);
		Long id;
		if(tbId != null && tbId != 0){
			id = tbId;
		}else{
			id = IdFactory.getInstance().generateNonPersistentId()*1L;
		}
		PublicCdManager cdManager = new PublicCdManager();
		Pet pet = new Pet(id, publicConfig.getName(), role,cdManager,1,publicConfig.getHeartTime());
		Long harm = levelAttribute.remove(GameConstants.PET_HARM_KEY);
		if(harm == null || harm < 1){
			harm = 1l;
		}
		pet.setHarm(harm);
		/**
		 * 装配 
		 * */
		BaseFightAttribute fightAttribute = new PetFightAttribute(pet);
		//状态管理器相关
		pet.setStateManager(new StateManager());
		//buff管理器
		pet.setBuffManager(new BuffManager());
		//战斗变化统计器
		pet.setFightStatistic(new PetFightStatistic(pet));
		//属性管理器
		pet.setPetFightAttribute(fightAttribute);
		//仇恨管理器
		pet.setHatredManager(new PetHatredManager(pet));
		//AI设置
		pet.setAi(new PetAi(pet));
		
		
		//填充属性
		fightAttribute.setBaseAttribute(BaseAttributeType.LEVEL,levelAttribute);
		if(xianjianAttribute!=null){
			fightAttribute.setBaseAttribute(BaseAttributeType.XIANJIAN, xianjianAttribute);
		}
		if(zhanjiaAttribute!=null){
			fightAttribute.setBaseAttribute(BaseAttributeType.ZHANJIA, zhanjiaAttribute);
		}
		
		if(tbXinwenAttribute!=null){
			fightAttribute.setBaseAttribute(BaseAttributeType.TANGBAO_XINWEN, tbXinwenAttribute);
		}
		
		if(tbTianYuAttribute!=null){
			fightAttribute.setBaseAttribute(BaseAttributeType.TIANYU, tbTianYuAttribute);
		}
		
		if(tbWuxingBaseAttrs!=null){
		    fightAttribute.setBaseAttribute(BaseAttributeType.TB_WUXING, tbWuxingBaseAttrs);
		}
		
		//初始化技能
		ISkill skill = SkillManager.getManager().getSkill(publicConfig.getSkill());
		if(skill != null){
			pet.addSkill(0, skill);//默认技能
		}
		
		if(isCreate){
			PetVo petVo = new PetVo(role.getId(), pet,id);
			petVo.setState(GameConstants.PET_STATE_NOMAL);
			pet.setPetVo(petVo);
			pet.setState(GameConstants.PET_STATE_NOMAL);
			StageMsgSender.send2One(role.getId(), ClientCmdType.TANGBAO_MAX_HP, pet.getFightAttribute().getMaxHp());
		}
		
		//组建绑定监听器
		FightListener fightListener = new FightListener(pet.getFightStatistic());
		pet.getFightAttribute().addListener(fightListener);
		pet.getStateManager().addListener(fightListener);
		pet.getBuffManager().addListener(fightListener);
		
		//填充血量
		pet.getFightAttribute().resetHpMp();
		
		return pet;
	}

    /**
     * 创建糖宝
     * @param role
     * @param levelAttribute
     * @param isCreate
     * @param tbId
     * @return
     */
    public static Pet create(IRole role, Map<String, Long> levelAttribute, boolean isCreate, Long tbId) {
        TangbaoPublicConfig publicConfig = PublicConfigureHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_TANGBAO);
        Long id;
        if (tbId != null && tbId != 0) {
            id = tbId;
        } else {
            id = IdFactory.getInstance().generateNonPersistentId() * 1L;
        }
        PublicCdManager cdManager = new PublicCdManager();
        Pet pet = new Pet(id, publicConfig.getName(), role, cdManager, 1, publicConfig.getHeartTime());
        Long harm = levelAttribute.remove(GameConstants.PET_HARM_KEY);
        if (harm == null || harm < 1) {
            harm = 1l;
        }
        pet.setHarm(harm);
        /**
         * 装配
         * */
        BaseFightAttribute fightAttribute = new PetFightAttribute(pet);
        // 状态管理器相关
        pet.setStateManager(new StateManager());
        // buff管理器
        pet.setBuffManager(new BuffManager());
        // 战斗变化统计器
        pet.setFightStatistic(new PetFightStatistic(pet));
        // 属性管理器
        pet.setPetFightAttribute(fightAttribute);
        // 仇恨管理器
        pet.setHatredManager(new PetHatredManager(pet));
        // AI设置
        pet.setAi(new PetAi(pet));

        // 填充战斗属性
        fightAttribute.setBaseAttribute(BaseAttributeType.LEVEL, levelAttribute);
        // 初始化技能
        ISkill skill = SkillManager.getManager().getSkill(publicConfig.getSkill());
        if (skill != null) {
            pet.addSkill(0, skill);// 默认技能
        }

        if (isCreate) {
            PetVo petVo = new PetVo(role.getId(), pet, id);
            petVo.setState(GameConstants.PET_STATE_NOMAL);
            pet.setPetVo(petVo);
            pet.setState(GameConstants.PET_STATE_NOMAL);
            StageMsgSender.send2One(role.getId(), ClientCmdType.TANGBAO_MAX_HP, pet.getFightAttribute().getMaxHp());
        }

        // 组建绑定监听器
        FightListener fightListener = new FightListener(pet.getFightStatistic());
        pet.getFightAttribute().addListener(fightListener);
        pet.getStateManager().addListener(fightListener);
        pet.getBuffManager().addListener(fightListener);

        // 填充血量
        pet.getFightAttribute().resetHpMp();

        return pet;
    }
    
}
