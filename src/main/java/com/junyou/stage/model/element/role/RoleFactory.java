/**
 * 
 */
package com.junyou.stage.model.element.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.junyou.gameconfig.publicconfig.configure.export.YabiaoPublicConfig;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.PointTakeupType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.bag.ContainerType;
import com.junyou.bus.chenghao.service.export.ChenghaoExportService;
import com.junyou.bus.chengshen.export.ChengShenExportService;
import com.junyou.bus.chibang.entity.ChiBangInfo;
import com.junyou.bus.chibang.manage.ChiBang;
import com.junyou.bus.chibang.util.ChiBangUtil;
import com.junyou.bus.chongwu.service.export.ChongwuExportService;
import com.junyou.bus.equip.export.EquipExportService;
import com.junyou.bus.fushu.export.FushuSkillExportService;
import com.junyou.bus.hczbs.export.HcZhengBaSaiExportService;
import com.junyou.bus.huanhua.constants.HuanhuaConstants;
import com.junyou.bus.jewel.export.JewelExportService;
import com.junyou.bus.kuafu_yungong.export.KuafuYunGongExportService;
import com.junyou.bus.lingjing.export.LingJingExportService;
import com.junyou.bus.marry.export.MarryExportService;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.qq.service.export.QqExportService;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.qiling.entity.QiLingInfo;
import com.junyou.bus.qiling.manage.QiLing;
import com.junyou.bus.qiling.util.QiLingUtil;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.role.export.TangbaoExportService;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoExportService;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoWrapper;
import com.junyou.bus.rolestage.entity.RoleStage;
import com.junyou.bus.rolestage.export.RoleStageExportService;
import com.junyou.bus.shenqi.ShenQiConstants;
import com.junyou.bus.shenqi.configure.export.ShenQiShuXingConfig;
import com.junyou.bus.shenqi.configure.export.ShenQiShuXingConfigExportService;
import com.junyou.bus.shenqi.export.ShenQiExportService;
import com.junyou.bus.shizhuang.export.RoleShiZhuangExportService;
import com.junyou.bus.skill.entity.RoleSkill;
import com.junyou.bus.skill.export.RoleSkillExportService;
import com.junyou.bus.territory.export.TerritoryExportService;
import com.junyou.bus.tianyu.entity.TianYuInfo;
import com.junyou.bus.tianyu.manage.TianYu;
import com.junyou.bus.tianyu.util.TianYuUtil;
import com.junyou.bus.tongtian.export.TongtianRoadExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.entity.VipConfig;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.bus.vip.export.VipConfigExportService;
import com.junyou.bus.vip.util.RoleVipWrapper;
import com.junyou.bus.wuqi.WuQiUtil;
import com.junyou.bus.wuqi.entity.WuQiInfo;
import com.junyou.bus.wuqi.manage.WuQi;
import com.junyou.bus.xianjian.entity.XianJianInfo;
import com.junyou.bus.xianjian.manage.XianJian;
import com.junyou.bus.xianjian.util.XianjianUtil;
import com.junyou.bus.xinwen.export.XinwenExportService;
import com.junyou.bus.yabiao.configure.export.YaBiaoConfig;
import com.junyou.bus.yabiao.configure.export.YaBiaoConfigExportService;
import com.junyou.bus.yaoshen.service.export.YaoshenExportService;
import com.junyou.bus.yaoshen.service.export.YaoshenFumoExportService;
import com.junyou.bus.yaoshen.service.export.YaoshenHunpoExportService;
import com.junyou.bus.yaoshen.service.export.YaoshenMoYinExportService;
import com.junyou.bus.zhanjia.entity.ZhanJiaInfo;
import com.junyou.bus.zhanjia.manage.ZhanJia;
import com.junyou.bus.zhanjia.util.ZhanJiaUtil;
import com.junyou.bus.zhuansheng.export.ZhuanshengExportService;
import com.junyou.bus.zuoqi.ZuoQiUtil;
import com.junyou.bus.zuoqi.entity.ZuoQiInfo;
import com.junyou.bus.zuoqi.manage.ZuoQi;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.export.ExpConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.PKPublicConfig;
import com.junyou.kuafu.manager.KuafuSessionManager;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.guild.configure.GuildConfigService;
import com.junyou.public_.guild.entity.Guild;
import com.junyou.public_.guild.entity.GuildConfig;
import com.junyou.public_.guild.entity.GuildMember;
import com.junyou.public_.guild.manager.GuildManager;
import com.junyou.stage.configure.SkillFireType;
import com.junyou.stage.configure.export.impl.BeiDongSkillConfig;
import com.junyou.stage.configure.export.impl.BeiDongSkillConfigExportService;
import com.junyou.stage.configure.export.impl.BeiDongSkillXueXiConfig;
import com.junyou.stage.configure.export.impl.GuildBeiDongSkillConfig;
import com.junyou.stage.configure.export.impl.GuildBeiDongSkillConfigExportService;
import com.junyou.stage.configure.export.impl.GuildBeiDongSkillXueXiConfig;
import com.junyou.stage.configure.export.impl.PkDebuffConfig;
import com.junyou.stage.configure.export.impl.PkDebuffConfigExportService;
import com.junyou.stage.configure.export.impl.TangBaoSkillConfig;
import com.junyou.stage.configure.export.impl.TangBaoSkillConfigExportService;
import com.junyou.stage.configure.export.impl.TangBaoSkillXueXiConfig;
import com.junyou.stage.model.attribute.role.RoleFightAttribute;
import com.junyou.stage.model.core.attribute.BaseAttributeType;
import com.junyou.stage.model.core.attribute.IBaseAttribute;
import com.junyou.stage.model.core.hatred.StandardHatredRule;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.state.StateManager;
import com.junyou.stage.model.element.biaoche.BiaoCheFactory;
import com.junyou.stage.model.element.biaoche.Biaoche;
import com.junyou.stage.model.element.chongwu.Chongwu;
import com.junyou.stage.model.element.componentlistener.DaZuoListener;
import com.junyou.stage.model.element.componentlistener.FightListener;
import com.junyou.stage.model.element.componentlistener.RoleStateExceptionListener;
import com.junyou.stage.model.element.componentlistener.ZuoQiListener;
import com.junyou.stage.model.element.pet.Pet;
import com.junyou.stage.model.element.pet.PetFactory;
import com.junyou.stage.model.element.pet.PetVo;
import com.junyou.stage.model.element.role.business.Equip;
import com.junyou.stage.model.element.role.business.RoleEquipData;
import com.junyou.stage.model.fight.BattleMode;
import com.junyou.stage.model.fight.statistic.RoleFightStatistic;
import com.junyou.stage.model.hatred.RoleHatredManager3;
import com.junyou.stage.model.prop.PropFormatHelper;
import com.junyou.stage.model.prop.PropModel;
import com.junyou.stage.model.skill.PublicCdManager;
import com.junyou.stage.model.skill.SkillFactory;
import com.junyou.stage.model.skill.SkillManager;
import com.junyou.stage.model.skill.buff.BuffFactory;
import com.junyou.stage.model.skill.buff.BuffManager;
import com.junyou.stage.model.skill.buff.BuffRecoverIgnoreUil;
import com.junyou.stage.model.state.ZuoQiState;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.stage.utils.StageHelper;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.kernel.spring.container.DataContainer;

/**
 * 角色工厂
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2014-12-26 下午4:52:07
 */
@Component
public class RoleFactory {
	
	private static RoleStageExportService roleStageExportService;
	private static DataContainer dataContainer;
	private static RoleExportService roleExportService;
	private static RoleSkillExportService roleSkillExportService;
	private static ExpConfigExportService expConfigExportService;
	private static BeiDongSkillConfigExportService beiDongSkillConfigExportService;
	private static TangBaoSkillConfigExportService tangBaoSkillConfigExportService;
	private static GuildBeiDongSkillConfigExportService guildBeiDongSkillConfigExportService;
	private static EquipExportService equipExportService;
	private static RoleBusinessInfoExportService roleBusinessInfoExportService;
	private static GuildConfigService guildConfigService;
	private static TangbaoExportService tangbaoExportService;
	private static YaBiaoConfigExportService yaBiaoConfigExportService;
	private static RoleVipInfoExportService roleVipInfoExportService;
	private static VipConfigExportService vipConfigExportService;
	private static ShenQiExportService shenQiExportService;
	private static GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	private static PkDebuffConfigExportService pkDebuffConfigExportService;
	private static ShenQiShuXingConfigExportService shenQiShuXingConfigExportService;
	private static LingJingExportService lingJingExportService;
	private static ChenghaoExportService chenghaoExportService;
	private static QqExportService qqExportService;
	private static YaoshenExportService yaoshenExportService;
	private static YaoshenHunpoExportService yaoshenHunpoExportService;
	private static ChengShenExportService chengShenExportService;
	private static YaoshenMoYinExportService yaoshenMoYinExportService;
	private static YaoshenFumoExportService yaoshenFumoExportService;
	private static TerritoryExportService territoryExportService;
	private static JewelExportService jewelExportService;
	private static HcZhengBaSaiExportService hcZhengBaSaiExportService;
    private static KuafuYunGongExportService kuafuYunGongExportService;
	private static ChongwuExportService chongwuExportService;
	private static RoleShiZhuangExportService roleShiZhuangExportService;
	private static MarryExportService marryExportService;
	private static FushuSkillExportService fushuSkillExportService;
	private static ZhuanshengExportService zhuanshengExportService;
	private static XinwenExportService xinwenExportService;
	private static TongtianRoadExportService tongtianLoadExportService;
	@Autowired
	public void setYaoshenFumoExportService(YaoshenFumoExportService yaoshenFumoExportService) {
		RoleFactory.yaoshenFumoExportService = yaoshenFumoExportService;
	}
	
	@Autowired
	public  void setTongtianLoadExportService(TongtianRoadExportService tongtianLoadExportService) {
		RoleFactory.tongtianLoadExportService = tongtianLoadExportService;
	}
	@Autowired
	public  void setzhuanshengExportService(ZhuanshengExportService zhuanshengExportService) {
		RoleFactory.zhuanshengExportService = zhuanshengExportService;
	}
	@Autowired
	public  void setXinwenExportService(XinwenExportService xinwenExportService) {
		RoleFactory.xinwenExportService = xinwenExportService;
	}
	@Autowired
	public  void setChengShenExportService(ChengShenExportService chengShenExportService) {
		RoleFactory.chengShenExportService = chengShenExportService;
	}
	@Autowired
	public   void setYaoshenMoYinExportService(YaoshenMoYinExportService yaoshenMoYinExportService) {
		RoleFactory.yaoshenMoYinExportService = yaoshenMoYinExportService;
	}
	@Autowired
	public  void setYaoshenHunpoExportService(YaoshenHunpoExportService yaoshenHunpoExportService) {
		RoleFactory.yaoshenHunpoExportService = yaoshenHunpoExportService;
	}
	@Autowired
	public void setFushuSkillExportService(FushuSkillExportService fushuSkillExportService) {
		RoleFactory.fushuSkillExportService = fushuSkillExportService;
	}
	@Autowired
	public void setMarryExportService(MarryExportService marryExportService) {
		RoleFactory.marryExportService = marryExportService;
	}
	@Autowired
	public void setHcZhengBaSaiExportService(HcZhengBaSaiExportService hcZhengBaSaiExportService) {
		RoleFactory.hcZhengBaSaiExportService = hcZhengBaSaiExportService;
	}
    @Autowired
	public void setKuafuYunGongExportService(KuafuYunGongExportService kuafuYunGongExportService) {
        RoleFactory.kuafuYunGongExportService = kuafuYunGongExportService;
    }
	@Autowired
	public void setChongwuExportService(ChongwuExportService chongwuExportService) {
		RoleFactory.chongwuExportService = chongwuExportService;
	}
	@Autowired
	public void setRoleShiZhuangExportService(RoleShiZhuangExportService roleShiZhuangExportService) {
		RoleFactory.roleShiZhuangExportService = roleShiZhuangExportService;
	}
	@Autowired
	public void setTerritoryExportService(TerritoryExportService territoryExportService) {
		RoleFactory.territoryExportService = territoryExportService;
	}
	@Autowired
	public void setJewelExportService(JewelExportService jewelExportService) {
		RoleFactory.jewelExportService = jewelExportService;
	}
	@Autowired
	public void setYaoshenExportService(YaoshenExportService yaoshenExportService) {
		RoleFactory.yaoshenExportService = yaoshenExportService;
	}
	@Autowired
	public void setQqExportService(QqExportService qqExportService) {
		RoleFactory.qqExportService = qqExportService;
	}
	@Autowired
	public void setChenghaoExportService(ChenghaoExportService chenghaoExportService) {
		RoleFactory.chenghaoExportService = chenghaoExportService;
	}
	@Autowired
	public void setLingJingExportService(LingJingExportService lingJingExportService) {
		RoleFactory.lingJingExportService = lingJingExportService;
	}
	@Autowired
	public void setShenQiShuXingConfigExportService(ShenQiShuXingConfigExportService shenQiShuXingConfigExportService) {
		RoleFactory.shenQiShuXingConfigExportService = shenQiShuXingConfigExportService;
	}
	@Autowired
	public void setPkDebuffConfigExportService(PkDebuffConfigExportService pkDebuffConfigExportService) {
		RoleFactory.pkDebuffConfigExportService = pkDebuffConfigExportService;
	}
	@Autowired
	public void setGongGongShuJuBiaoConfigExportService(GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService) {
		RoleFactory.gongGongShuJuBiaoConfigExportService = gongGongShuJuBiaoConfigExportService;
	}
	@Autowired
	public void setVipConfigExportService(VipConfigExportService vipConfigExportService) {
		RoleFactory.vipConfigExportService = vipConfigExportService;
	}
	@Autowired
	public void setRoleVipInfoExportService(RoleVipInfoExportService roleVipInfoExportService) {
		RoleFactory.roleVipInfoExportService = roleVipInfoExportService;
	}
	@Autowired
	public void setGuildConfigService(GuildConfigService guildConfigService) {
		RoleFactory.guildConfigService = guildConfigService;
	}
	@Autowired
	public void setTangbaoExportService(TangbaoExportService tangbaoExportService) {
		RoleFactory.tangbaoExportService = tangbaoExportService;
	}
	@Autowired
	public void setYaBiaoConfigExportService(YaBiaoConfigExportService yaBiaoConfigExportService) {
		RoleFactory.yaBiaoConfigExportService = yaBiaoConfigExportService;
	}
	@Autowired
	public void setRoleBusinessInfoExportService(
			RoleBusinessInfoExportService roleBusinessInfoExportService) {
		RoleFactory.roleBusinessInfoExportService = roleBusinessInfoExportService;
	}
	@Autowired
	public void setEquipExportService(EquipExportService equipExportService) {
		RoleFactory.equipExportService = equipExportService;
	}
	@Autowired
	public void setRoleExportService(RoleExportService roleExportService) {
		RoleFactory.roleExportService = roleExportService;
	}
	@Autowired
	public void setDataContainer(DataContainer dataContainer) {
		RoleFactory.dataContainer = dataContainer;
	}
	@Autowired
	public void setRoleStageExportService(RoleStageExportService _roleStageExportService) {
		roleStageExportService = _roleStageExportService;
	}
	@Autowired
	public void setRoleSkillExportService(RoleSkillExportService roleSkillExportService) {
		RoleFactory.roleSkillExportService = roleSkillExportService;
	}
	@Autowired
	public void setExpConfigExportService(ExpConfigExportService expConfigExportService) {
		RoleFactory.expConfigExportService = expConfigExportService;
	}
	@Autowired
	public void setBeiDongSkillConfigExportService(BeiDongSkillConfigExportService beiDongSkillConfigExportService) {
		RoleFactory.beiDongSkillConfigExportService = beiDongSkillConfigExportService;
	}
	@Autowired
	public void setTangBaoSkillConfigExportService(TangBaoSkillConfigExportService tangBaoSkillConfigExportService) {
		RoleFactory.tangBaoSkillConfigExportService = tangBaoSkillConfigExportService;
	}
	@Autowired
	public void setGuildBeiDongSkillConfigExportService(GuildBeiDongSkillConfigExportService guildBeiDongSkillConfigExportService) {
		RoleFactory.guildBeiDongSkillConfigExportService = guildBeiDongSkillConfigExportService;
	}
	@Autowired
	public void setShenQiExportService(ShenQiExportService shenQiExportService) {
		RoleFactory.shenQiExportService = shenQiExportService;
	}
	/**
	 * 将role对象转成跨服数据
	 * @param role
	 * @return
	 */
	public static Object createKuaFuRoleData(IRole role){
		Map<String,Object> otherData = new HashMap<>();
		long roleId = role.getId();
		RoleStage roleStage = roleStageExportService.getRoleStage(roleId);
		roleStage.setNuqi(role.getNuqi());
		RoleWrapper loginRole = roleExportService.getLoginRole(roleId);
		
		KFOtherStageVo otherVo = new KFOtherStageVo();
		otherVo.setGuildId(role.getBusinessData().getGuildId());
		otherVo.setGuildName(role.getBusinessData().getGuildName());
		otherVo.setVipLevel(role.getBusinessData().getVipLevel());
		otherVo.setEquipData(role.getBusinessData().getEquipData());
		int dazuo = roleVipInfoExportService.getVipTequan(roleId, GameConstants.VIP_DAZUO_EXP);
		otherVo.setDazuo(dazuo);
		for ( ISkill skill : role.getSkills()) {
			otherVo.addSkill(skill.getId());
		}
		otherVo.setHcZbsWinnerGuildLeader(role.getBusinessData().isHcZbsWinnerGuildLeader());
		otherVo.setKfYunGongWinnerGuildLeader(role.getBusinessData().isKfYunGongWinnerGuildLeader());
		Pet pet = role.getPet();
		Map<String,Long> petAtt = null;
		if(pet != null){
			IBaseAttribute baseAttribute1 = pet.getFightAttribute().getBaseAttributeMap(BaseAttributeType.LEVEL);
			if(baseAttribute1 != null){
				petAtt = baseAttribute1.toMap();
			}
			IBaseAttribute baseAttribute2 = pet.getFightAttribute().getBaseAttributeMap(BaseAttributeType.XIANJIAN);
			if(baseAttribute2 != null){
				otherData.put(GameConstants.KUAFU_PET_XIANJIAN_KEY, baseAttribute2.toMap());
			}
			
			IBaseAttribute baseAttribute3 = pet.getFightAttribute().getBaseAttributeMap(BaseAttributeType.ZHANJIA);
			if(baseAttribute3 != null){
				otherData.put(GameConstants.KUAFU_PET_ZHANJIA_KEY, baseAttribute3.toMap());
			}
			IBaseAttribute baseAttribute4 = pet.getFightAttribute().getBaseAttributeMap(BaseAttributeType.TIANYU);
			if(baseAttribute4 != null){
				otherData.put(GameConstants.KUAFU_PET_TIANYU_KEY, baseAttribute4.toMap());
			}
			IBaseAttribute baseAttribute5 = pet.getFightAttribute().getBaseAttributeMap(BaseAttributeType.TANGBAO_XINWEN);
			if(baseAttribute5 != null){
				otherData.put(GameConstants.KUAFU_PET_XINWEN_KEY, baseAttribute5.toMap());
			}
			IBaseAttribute baseAttribute6 = pet.getFightAttribute().getBaseAttributeMap(BaseAttributeType.TB_WUXING);
			if(baseAttribute6 != null){
			    otherData.put(GameConstants.KUAFU_PET_WUXING_KEY, baseAttribute6.toMap());
			}
			IBaseAttribute baseAttribute7 = pet.getFightAttribute().getBaseAttributeMap(BaseAttributeType.TB_WUXING_SKILL);
			if(baseAttribute7 != null){
			    otherData.put(GameConstants.KUAFU_PET_WUXING_SKILL_KEY, baseAttribute7.toMap());
			}
		}
		otherData.put(GameConstants.KUAFU_YAOSHEN_SHOW,role.isYaoshen());
		if(role.getBusinessData().getShenqiId() != null && role.getBusinessData().getShenqiId()!=0 ){
			otherData.put(GameConstants.KUAFU_SHENQI_KEY, new Object[]{role.getBusinessData().getShenqiId(),role.getBusinessData().getShenqiSkillId(),role.getBusinessData().getShenqiSkillId2()});
		}
		if(role.getBusinessData().getChongwu() != null && role.getBusinessData().getChongwuId()!=0 ){
			Chongwu cw = role.getBusinessData().getChongwu();
			Map<String,Object> tmap=  new HashMap<String,Object>();
			tmap.put(GameConstants.KUAFU_CHONGWU_KEY_ID, cw.getId());
			tmap.put(GameConstants.KUAFU_CHONGWU_KEY_NAME, cw.getName());
			tmap.put(GameConstants.KUAFU_CHONGWU_KEY_SPEED, cw.getSpeed());
			tmap.put(GameConstants.KUAFU_CHONGWU_KEY_CONFIG_ID, cw.getConfigId());
			otherData.put(GameConstants.KUAFU_CHONGWU_KEY, tmap);
		}
		if(role.getBusinessData().getXianjian()!=null){
			otherData.put(GameConstants.KUAFU_XIANJIAN_KEY, role.getBusinessData().getXianjian());
		}
		if(role.getBusinessData().getZhanjia()!=null){
			otherData.put(GameConstants.KUAFU_ZHANJIA_KEY, role.getBusinessData().getZhanjia());
		}
		if(role.getBusinessData().getChenghaoMap()!=null){
			otherData.put(GameConstants.KUAFU_CHENGHAO_KEY, role.getBusinessData().getChenghaoMap());
		}
		if(role.getBusinessData().getPlatformInfo()!=null){
			otherData.put(GameConstants.KUAFU_PLATFORM_INFO_KEY, role.getBusinessData().getPlatformInfo());
		}
		if(role.getShiZhuang()!=null && role.getShiZhuang() > 0){
			otherData.put(GameConstants.KUAFU_SHIZHUANG_INFO_KEY, role.getShiZhuang());
		}
		if(role.getPeiou() != null){
			otherData.put(GameConstants.KUAFU_MARRY_INFO_KEY, new Object[]{role.getPeiou(),role.getXinwu()});
		}
		otherData.put(GameConstants.KUAFU_NOTICESKILLS_INFO_KEY, role.getNoticeSkills());
		otherData.put(GameConstants.KUAFU_SOURCE_SERVER_ID, ChuanQiConfigUtil.getServerId());
		otherData.put(GameConstants.KUAFU_QILING_INFO_KEY, role.getBusinessData().getQiling());
		otherData.put(GameConstants.KUAFU_TIANYU_INFO_KEY, role.getBusinessData().getTianyu());
		otherData.put(GameConstants.KUAFU_ZHUANSHENG_INFO_KEY, role.getBusinessData().getZhuanShengLevel());
		otherData.put(GameConstants.KUAFU_WUXING_INFO_KEY, role.getBusinessData().getWuxingType());
		otherData.put(GameConstants.KUAFU_WUXING_ID_KEY, role.getBusinessData().getWuxingId());
		otherData.put(GameConstants.KUAFU_WUXING_LIST_KEY, role.getBusinessData().getWuxingList());
		otherData.put(GameConstants.KUAFU_WUQI_KEY, role.getWuQi());
		return new Object[]{
				roleStage
				,loginRole
				,KuafuRoleUtil.getRoleAllAttribute(role)
				,otherVo
				,role.getZuoQi()
				,role.getBusinessData().getChibang()
				,petAtt
				,otherData
				};
	}
	/**
	 * 跨服战场景角色对象
	 * @param roleId
	 * @param enterStageId
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static IRole createKuaFu(Long roleId,String enterStageId,Object data){
		//获取存起来跨服的role,不用反复序列化和反序列化
		IRole kuaFuRole = dataContainer.getData(GameConstants.DATA_CONTAINER_ROLE, roleId.toString());
		if(kuaFuRole != null){
			return copyRole(kuaFuRole);
		}
		//获取存起来跨服的role,不用反复序列化和反序列化
		Object[] datas = (Object[]) data;
		Object[] roleData = (Object[]) datas[0];
		int index = 0;
		RoleStage roleStage = (RoleStage)roleData[index++]; 
		roleStage.setCdManager(new PublicCdManager());
		dataContainer.putData(GameConstants.DATA_CONTAINER_ROLESTAGE, roleId.toString(), roleStage);
		RoleWrapper loginRole = (RoleWrapper) roleData[index++]; 
		Map<Integer,Map<String,Long>> roleAllMap = (Map<Integer, Map<String, Long>>) roleData[index++]; 
		KFOtherStageVo otherVo = (KFOtherStageVo)roleData[index++]; 
		ZuoQi zuoQi = (ZuoQi)roleData[index++]; 
		ChiBang chibang = (ChiBang)roleData[index++];
		Map<String,Long> petAtt = (Map<String,Long>)roleData[index++];
		Map<String,Object> otherData = (Map<String,Object>)roleData[index++];
		Map<String,Long> petXianJianBaseAttribute = null;
		Map<String,Long> petZhanJiaBaseAttribute = null;
		Map<String,Long> xinwenBaseAttribute = null;
		Map<String,Long> tianYuBaseAttribute = null;
		Map<String,Long> tbWuxingBaseAttrs = null;
		Map<String,Long> tbWuxingSkillBaseAttrs = null;
		WuQi wuQi = (WuQi)otherData.get(GameConstants.KUAFU_WUQI_KEY); 
		
		
		XianJian xianjian = null;
		ZhanJia zhanjia = null;
		QiLing  qiLing  = null;
		TianYu  tianyu  = null;
		Integer  wuxingType  = null;
		Integer wuxingId = null;
		List<Integer> wuxing = null;
		Map<Integer,String> chenghaoMap=null;
		Map<Integer,Object> platformInfo = null; 
		if(otherData.containsKey(GameConstants.KUAFU_PET_XIANJIAN_KEY)){
			petXianJianBaseAttribute = (Map<String,Long>)otherData.get(GameConstants.KUAFU_PET_XIANJIAN_KEY);
		}
		if(otherData.containsKey(GameConstants.KUAFU_PET_ZHANJIA_KEY)){
			petZhanJiaBaseAttribute = (Map<String,Long>)otherData.get(GameConstants.KUAFU_PET_ZHANJIA_KEY);
		}

		if(otherData.containsKey(GameConstants.KUAFU_PET_XINWEN_KEY)){
			xinwenBaseAttribute = (Map<String,Long>)otherData.get(GameConstants.KUAFU_PET_XINWEN_KEY);
		}
        if (otherData.containsKey(GameConstants.KUAFU_PET_TIANYU_KEY)) {
            tianYuBaseAttribute = (Map<String, Long>) otherData.get(GameConstants.KUAFU_PET_TIANYU_KEY);
        }
        if (otherData.containsKey(GameConstants.KUAFU_PET_WUXING_KEY)) {
            tbWuxingBaseAttrs = (Map<String, Long>) otherData.get(GameConstants.KUAFU_PET_WUXING_KEY);
        }
        if (otherData.containsKey(GameConstants.KUAFU_PET_WUXING_SKILL_KEY)) {
            tbWuxingSkillBaseAttrs = (Map<String, Long>) otherData.get(GameConstants.KUAFU_PET_WUXING_SKILL_KEY);
        }
        if (otherData.containsKey(GameConstants.KUAFU_XIANJIAN_KEY)){
			xianjian = (XianJian)otherData.get(GameConstants.KUAFU_XIANJIAN_KEY);
		}
		if(otherData.containsKey(GameConstants.KUAFU_ZHANJIA_KEY)){
			zhanjia = (ZhanJia)otherData.get(GameConstants.KUAFU_ZHANJIA_KEY);
		}
		if(otherData.containsKey(GameConstants.KUAFU_QILING_INFO_KEY)){
			qiLing = (QiLing)otherData.get(GameConstants.KUAFU_QILING_INFO_KEY);
		}
		if(otherData.containsKey(GameConstants.KUAFU_TIANYU_INFO_KEY)){
			tianyu = (TianYu)otherData.get(GameConstants.KUAFU_TIANYU_INFO_KEY);
		}
		if(otherData.containsKey(GameConstants.KUAFU_CHENGHAO_KEY)){
			chenghaoMap = (Map<Integer,String>)otherData.get(GameConstants.KUAFU_CHENGHAO_KEY);
		}
		if(otherData.containsKey(GameConstants.KUAFU_WUXING_INFO_KEY)){
			wuxingType = (Integer) otherData.get(GameConstants.KUAFU_WUXING_INFO_KEY);
		}
		if(otherData.containsKey(GameConstants.KUAFU_WUXING_ID_KEY)){
			wuxingId = (Integer) otherData.get(GameConstants.KUAFU_WUXING_ID_KEY);
		}
		if(otherData.containsKey(GameConstants.KUAFU_WUXING_LIST_KEY)){
			wuxing = (List<Integer>) otherData.get(GameConstants.KUAFU_WUXING_LIST_KEY);
		}
		if(otherData.containsKey(GameConstants.KUAFU_PLATFORM_INFO_KEY)){
			platformInfo = (Map<Integer,Object>)otherData.get(GameConstants.KUAFU_PLATFORM_INFO_KEY);
		}
		String serverId = (String)otherData.get(GameConstants.KUAFU_SOURCE_SERVER_ID);
		if(serverId != null){
			KuafuSessionManager.bindServerId(roleId, serverId);
		}else{
			ChuanQiLog.error("玩家{}未获取到所属原服id",roleId);
		}
		
		BusinessData businessData = new BusinessData(loginRole);
		businessData.setXianjian(xianjian);
		businessData.setZhanjia(zhanjia);
		businessData.setQiling(qiLing);
		businessData.setTianyu(tianyu);
		businessData.setChenghaoMap(chenghaoMap);
		businessData.setPlatformInfo(platformInfo);
		businessData.setVipLevel(otherVo.getVipLevel());
		businessData.setWuxingType(wuxingType);
		businessData.setWuxingId(wuxingId);
		businessData.setWuxingList(wuxing);
		if(otherData.containsKey(GameConstants.KUAFU_SHENQI_KEY)){
			Object[] shenqiData = (Object[])otherData.get(GameConstants.KUAFU_SHENQI_KEY);
			businessData.setShenqiId((Integer) shenqiData[0]);
			businessData.setShenqiSkillId((String) shenqiData[1]);
			if(shenqiData.length > 2){
				businessData.setShenqiSkillId2((String) shenqiData[2]);
			}
		}
		businessData.setGuildId(otherVo.getGuildId());
		businessData.setGuildName(otherVo.getGuildName());
		businessData.setEquipData(otherVo.getEquipData());
		businessData.setDazuoExp(otherVo.getDazuo());
		businessData.setHcZbsWinnerGuildLeader(otherVo.isHcZbsWinnerGuildLeader());
		businessData.setKfYunGongWinnerGuildLeader(otherVo.isKfYunGongWinnerGuildLeader());
		if(otherData.containsKey(GameConstants.KUAFU_ZHUANSHENG_INFO_KEY)){
			businessData.setZhuanShengLevel(CovertObjectUtil.object2int(otherData.get(GameConstants.KUAFU_ZHUANSHENG_INFO_KEY)));
		}
		
		Role role = new Role(loginRole.getId(),loginRole.getName());
		if(otherData.containsKey(GameConstants.KUAFU_YAOSHEN_SHOW)){
			Boolean yaoshen = (Boolean) otherData.get(GameConstants.KUAFU_YAOSHEN_SHOW);
			role.setYaoshen(yaoshen);
		}
		if(otherData.containsKey(GameConstants.KUAFU_CHONGWU_KEY)){
			Map<String,Object> tMap = (Map<String,Object>)otherData.get(GameConstants.KUAFU_CHONGWU_KEY);
			Long id = (Long) tMap.get(GameConstants.KUAFU_CHONGWU_KEY_ID);
			String name = (String) tMap.get(GameConstants.KUAFU_CHONGWU_KEY_NAME);
			Integer speed = (Integer) tMap.get(GameConstants.KUAFU_CHONGWU_KEY_SPEED);
			Integer configId = (Integer) tMap.get(GameConstants.KUAFU_CHONGWU_KEY_CONFIG_ID);
			Chongwu cw = new Chongwu(id, name);
			cw.setConfigId(configId);
			cw.setSpeed(speed);
			cw.setRole(role);
			businessData.setChongwu(cw);
		}
		role.setBusinessData(businessData);
		
		//状态管理器相关
		role.setStateManager(new StateManager());
		//战斗变化统计器
		RoleFightStatistic roleFightStatistic = new RoleFightStatistic(role);
		role.setFightStatistic(roleFightStatistic);
		//属性管理器
		RoleFightAttribute roleFightAttribute = new RoleFightAttribute(role);
		
		role.setRoleFightAttribute(roleFightAttribute);
		//buff管理器
		role.setBuffManager(new BuffManager());
		//仇恨管理器
		role.setHatredManager(new RoleHatredManager3(role,new StandardHatredRule()));
		
		//设置技能
		for (String skillId : otherVo.getSkills()) {
			ISkill skill = SkillManager.getManager().getSkill(skillId);
			if(skill != null){
				role.addSkill(skill);
			}
		}
		
		/**
		 * 填充  和平模式
		 * **/
		role.setBattleMode(BattleMode.PEACE);
		
		//设置道具信息
		PropModel propModel = PropFormatHelper.decodeAll(roleStage.getProp(), role);
		role.setPropModel( propModel );
		roleStage.setProp( propModel.format() );
		
		//设置属性
		KuafuRoleUtil.saveKuafuAttribute2Role(roleAllMap, role);
		//计算角色最终属性
		role.getFightAttribute().refresh();
		//填充血量
		roleFightAttribute.setCurHp(roleStage.getHp());
		
		//组件绑定监听器
		FightListener fightListener = new FightListener(role.getFightStatistic());
		role.getFightAttribute().addListener(fightListener);
		role.getStateManager().addListener(fightListener);
		role.getBuffManager().addListener(fightListener);
		role.getStateManager().addListener(new RoleStateExceptionListener(role));
		role.getStateManager().addListener(new ZuoQiListener(role));
		role.getStateManager().addListener(new DaZuoListener(role));
		
		if(petAtt != null){//有宝宝
            Pet pet = PetFactory.create(role, petAtt, true, null);
            // 添加糖宝仙剑属性
            if(null != petXianJianBaseAttribute){
                pet.getFightAttribute().initBaseAttribute(BaseAttributeType.XIANJIAN, petXianJianBaseAttribute);
            }
            // 添加糖宝战甲属性
            if(null != petZhanJiaBaseAttribute){
                pet.getFightAttribute().initBaseAttribute(BaseAttributeType.ZHANJIA, petZhanJiaBaseAttribute);
            }
            // 添加糖宝心纹属性
            if(null != xinwenBaseAttribute){
                pet.getFightAttribute().initBaseAttribute(BaseAttributeType.TANGBAO_XINWEN, xinwenBaseAttribute);
            }
            // 添加糖宝天羽属性
            if(null != tianYuBaseAttribute){
                pet.getFightAttribute().initBaseAttribute(BaseAttributeType.TIANYU, tianYuBaseAttribute);
            }
            // 添加糖宝五行属性
            if(null != tbWuxingBaseAttrs){
                pet.getFightAttribute().initBaseAttribute(BaseAttributeType.TB_WUXING, tbWuxingBaseAttrs);
            }
            // 添加糖宝五行属性
            if(null != tbWuxingSkillBaseAttrs){
                pet.getFightAttribute().initBaseAttribute(BaseAttributeType.TB_WUXING_SKILL, tbWuxingSkillBaseAttrs);
            }
            pet.getFightAttribute().refresh();
            role.setPet(pet);
		}
		
		if(zuoQi != null){
			role.setZuoQi(zuoQi);
		}
		
		if(wuQi != null){
			role.setWuQi(wuQi);
		}
		
		if(chibang != null){
			businessData.setChibang(chibang);
		}
		role.setCdManager(roleStage.getCdManager());
		role.setNuqi(roleStage.getNuqi());
		//时装
		if(otherData.containsKey(GameConstants.KUAFU_SHIZHUANG_INFO_KEY)){
			try {
				role.setShiZhuang((Integer)otherData.get(GameConstants.KUAFU_SHIZHUANG_INFO_KEY));
			} catch (Exception e) {
				ChuanQiLog.error("跨服设置时装时错误。");
			}
		}
		if(otherData.containsKey(GameConstants.KUAFU_MARRY_INFO_KEY)){
			Object[] marryInfo = (Object[])otherData.get(GameConstants.KUAFU_MARRY_INFO_KEY);
			 role.setPeiou((String)marryInfo[0]);
			 role.setXinwu((Integer)marryInfo[1]);
		}
		if(otherData.containsKey(GameConstants.KUAFU_NOTICESKILLS_INFO_KEY)){
			role.setNoticeSkills((Set<String>)otherData.get(GameConstants.KUAFU_NOTICESKILLS_INFO_KEY));
		}
		//跨服的role存起来,不用反复序列化和反序列化
		dataContainer.putData(GameConstants.DATA_CONTAINER_ROLE, roleId.toString(), role);
		return role;
	}
	
	private static IRole copyRole(IRole oldRole){
		BusinessData oldBusData = oldRole.getBusinessData();
		Long roleId = oldRole.getId();
		
		//业务数据
		BusinessData businessData = new BusinessData(oldBusData);
		businessData.setPlatformInfo(oldBusData.getPlatformInfo());
		businessData.setVipLevel(oldBusData.getVipLevel());
		businessData.setDazuoExp(oldBusData.getDazuoExp());
		businessData.setGuildId(oldBusData.getGuildId());
		businessData.setGuildName(oldBusData.getGuildName());
		businessData.setEquipData(oldBusData.getEquipData());
		businessData.initDazuoExp(oldBusData.getDazuoExp());
		businessData.setHcZbsWinnerGuildLeader(oldBusData.isHcZbsWinnerGuildLeader());
		businessData.setKfYunGongWinnerGuildLeader(oldBusData.isKfYunGongWinnerGuildLeader());
		
		businessData.setChenghaoMap(oldBusData.getChenghaoMap());
		businessData.setShenqiId(oldBusData.getShenqiId());
		businessData.setShenqiSkillId(oldBusData.getShenqiSkillId());
		businessData.setShenqiSkillId2(oldBusData.getShenqiSkillId2());
		businessData.setChongwu(oldBusData.getChongwu());
		businessData.setZhuanShengLevel(oldBusData.getZhuanShengLevel());
		businessData.setWuxingType(oldBusData.getWuxingType()); 
		businessData.setWuxingId(oldBusData.getWuxingId()); 
				
		//角色场景数据
		RoleStage roleStage = dataContainer.getData(GameConstants.DATA_CONTAINER_ROLESTAGE, roleId.toString());

		Role role = new Role(roleId,oldRole.getName());
		role.setBusinessData(businessData);
		
		//状态管理器相关
		role.setStateManager(new StateManager());
		//战斗变化统计器
		RoleFightStatistic roleFightStatistic = new RoleFightStatistic(role);
		role.setFightStatistic(roleFightStatistic);
		//属性管理器
		RoleFightAttribute roleFightAttribute = new RoleFightAttribute(role);
		
		role.setRoleFightAttribute(roleFightAttribute);
		//buff管理器
		role.setBuffManager(new BuffManager());
		//仇恨管理器
		role.setHatredManager(new RoleHatredManager3(role,new StandardHatredRule()));
		
		//设置技能
		for (ISkill skill : oldRole.getSkills()) {
			if(skill != null){
				role.addSkill(skill);
			}
		}
		
		role.setBattleMode(oldRole.getBattleMode());
		
		//设置道具信息
		PropModel propModel = PropFormatHelper.decodeAll(roleStage.getProp(), role);
		role.setPropModel( propModel );
		roleStage.setProp( propModel.format() );
		
		//设置属性
		KuafuRoleUtil.copyRoleAttribute(oldRole, role);
		//计算角色最终属性
		role.getFightAttribute().refresh();
		//填充血量
		roleFightAttribute.setCurHp(roleStage.getHp());
		
		//组件绑定监听器
		FightListener fightListener = new FightListener(role.getFightStatistic());
		role.getFightAttribute().addListener(fightListener);
		role.getStateManager().addListener(fightListener);
		role.getBuffManager().addListener(fightListener);
		role.getStateManager().addListener(new RoleStateExceptionListener(role));
		role.getStateManager().addListener(new ZuoQiListener(role));
		role.getStateManager().addListener(new DaZuoListener(role));
		
		role.setZuoQi(oldRole.getZuoQi());
		role.getBusinessData().setChibang(oldBusData.getChibang());
		role.getBusinessData().setXianjian(oldBusData.getXianjian());
		role.getBusinessData().setZhanjia(oldBusData.getZhanjia());
		role.getBusinessData().setQiling(oldBusData.getQiling());
		role.getBusinessData().setTianyu(oldBusData.getTianyu());
		role.getBusinessData().setWuxingList(oldBusData.getWuxingList());
		role.setWuQi(oldRole.getWuQi());
		
		
		role.setCdManager(roleStage.getCdManager());
		
		Pet pet = oldRole.getPet();
		if(pet != null){
			pet.setOwner(role);
			role.setPet(pet);
		}
		role.setNuqi(oldRole.getNuqi());
		role.setShiZhuang(oldRole.getShiZhuang());
		role.setPeiou(oldRole.getPeiou());
		role.setXinwu(oldRole.getXinwu());
		role.setNoticeSkills(oldRole.getNoticeSkills());
		role.setYaoshen(oldRole.isYaoshen());
		//跨服的role存起来,不用反复序列化和反序列化
		dataContainer.putData(GameConstants.DATA_CONTAINER_ROLE, roleId.toString(), role);
		return role;
	}
	
	public static IRole create(Long roleId,String enterStageId) {
		
		RoleWrapper loginRole = roleExportService.getLoginRole(roleId);
		BusinessData businessData = new BusinessData(loginRole);
		
		Role role = new Role(loginRole.getId(),loginRole.getName());
		role.setBusinessData(businessData);
		
		//设置打坐信息 
		businessData.setDazuoTargetRoleId(0);
		
		//设置跳闪值
		RoleBusinessInfoWrapper rbInfo = roleBusinessInfoExportService.getRoleBusinessInfoWrapper(loginRole.getId());
		businessData.setTiaoShan(rbInfo.getJumpVal());
		
		//PK值
		int pkValue = rbInfo.getPkVal();
		role.getBusinessData().setPkValue(pkValue);
		PKPublicConfig pKPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_PK);
//		红名PK点
		role.getBusinessData().setRedPkValue(pKPublicConfig.getNeedRed());
//		灰名时间
		role.getBusinessData().setHuiminTime(rbInfo.getHuiTime());
		RoleStage roleStage = roleStageExportService.getRoleStage(roleId);
		role.getBusinessData().setShenqiId(roleStage.getShenqi());
		if(roleStage.getShenqi()!=null && roleStage.getShenqi().intValue() != 0 ){
			ShenQiShuXingConfig shenqiConfig = shenQiShuXingConfigExportService.loadById(roleStage.getShenqi());
			if(shenqiConfig != null){
				role.getBusinessData().setShenqiSkillId(shenqiConfig.getSkill());
				ISkill skill = SkillManager.getManager().getSkill(shenqiConfig.getSkill());
				if(skill != null){
					role.addSkill(skill);
				}
				role.getBusinessData().setShenqiSkillId2(shenqiConfig.getSkill2());
				ISkill skill2 = SkillManager.getManager().getSkill(shenqiConfig.getSkill2());
				if(skill2 != null){
					role.addSkill(skill2);
				}
			}
		}
		role.setNuqi(roleStage.getNuqi());
		role.setYaoshen(yaoshenExportService.isRoleInYaoshen(roleId));
		
		Chongwu cw = chongwuExportService.getFightCw(roleId);
		if(cw!=null){
			cw.setRole(role);
			role.getBusinessData().setChongwu(cw);
		}
		//平台数据
		Map<Integer,Object> platformInfo = null;
		if(PlatformConstants.isQQ()){
			platformInfo = qqExportService.getRoleQQPlatformInfo(roleId,false);
		}
		if(platformInfo!=null){
			Map<Integer,Object> map = new HashMap<Integer,Object>();
			map.put(QqConstants.QQ_PLATFORM_INFO_HUANG_ZUAN_LEVEL_KEY, platformInfo.get(QqConstants.QQ_PLATFORM_INFO_HUANG_ZUAN_LEVEL_KEY));
			map.put(QqConstants.QQ_PLATFORM_INFO_HUANG_ZUAN_NIANFEI_KEY, platformInfo.get(QqConstants.QQ_PLATFORM_INFO_HUANG_ZUAN_NIANFEI_KEY));
			role.getBusinessData().setPlatformInfo(map);
		}
		//五行附体
		Integer wuxingType = StageHelper.getWuXingMoShenExportService().getFuTiType(roleId);
		Integer wuxingId = StageHelper.getWuXingMoShenExportService().getFuTiConfigId(roleId);
		role.getBusinessData().setWuxingType(wuxingType);
		role.getBusinessData().setWuxingId(wuxingId);
		//称号
		Map<Integer,String> chenghaos = chenghaoExportService.getWearingChenghao(roleId);
		if(chenghaos != null){
			for(Integer e:chenghaos.keySet()){
				role.getBusinessData().addChenghao(e,chenghaos.get(e));
			}
		}
		//状态管理器相关
		role.setStateManager(new StateManager());
		role.addShenqiListener();
		
		//战斗变化统计器
		RoleFightStatistic roleFightStatistic = new RoleFightStatistic(role);
		role.setFightStatistic(roleFightStatistic);
		//属性管理器
		RoleFightAttribute roleFightAttribute = new RoleFightAttribute(role);
		
		role.setRoleFightAttribute(roleFightAttribute);
		
		//角色技能工厂,cd管理器
		SkillFactory skillFactory = new SkillFactory();
		role.setSkillFactory(skillFactory);
		//buff管理器
		role.setBuffManager(new BuffManager());
		//仇恨管理器
		role.setHatredManager(new RoleHatredManager3(role,new StandardHatredRule()));
		//装备
		businessData.setEquipData( new RoleEquipData() );
		
		
		if(pkValue > 0){
			PkDebuffConfig pkconfig = pkDebuffConfigExportService.loadByPk(pkValue);
			if(pkconfig != null && pkconfig.getAttribute() != null){
				role.getFightAttribute().initBaseAttribute(BaseAttributeType.HONGMING, pkconfig.getAttribute());
			}
			if(roleStage.isLogin()){
				BusMsgSender.send2One(roleId, ClientCmdType.PK_CHANGE, new Object[]{roleId,pkValue});
			}
		}
		
		//设置公会信息\属性
		GuildMember member = GuildManager.getManager().getGuildMember(roleId);
		if(member != null){
			Guild guild= GuildManager.getManager().getGuild(member.getGuildId());
			if(guild != null){
				businessData.setGuildId(guild.getId());
				businessData.setGuildName(guild.getName());
				GuildConfig config = guildConfigService.getGuildConfig(guild.getLevel());
				if(config != null){
					role.getFightAttribute().initBaseAttribute(BaseAttributeType.GUILD,config.getAttribute());
				}
			}
		}
		
		
		//坐骑
		List<Integer> zuoqihuanhuanConfigList = StageHelper.getHuanhuaExportService().getRoleHuanhuaConfigList(role.getId(), HuanhuaConstants.HUANHUA_TYPE_1);
		ZuoQiInfo zuoQiInfo = StageHelper.getZuoQiExportService().getZuoQiInfo(role.getId());
		if(zuoQiInfo != null){
			Object[] zuoqiEquips = StageHelper.getZuoQiExportService().getZuoQiEquips(role.getId());
			ZuoQi zuoqi = ZuoQiUtil.coverToZuoQi(zuoQiInfo,zuoqihuanhuanConfigList, zuoqiEquips);
			//设置bus里存储的战斗力
			zuoqi.setZplus(zuoQiInfo.getZplus());
			role.setZuoQi(zuoqi); 
		}
		
		//武器
		List<Integer> wuqihuanhuanConfigList = StageHelper.getHuanhuaExportService().getRoleHuanhuaConfigList(role.getId(), HuanhuaConstants.HUANHUA_TYPE_7);
		WuQiInfo wuQiInfo = StageHelper.getWuQiExportService().getWuQiInfo(role.getId());
		if(wuQiInfo != null){
			Object[] wuqiEquips = StageHelper.getWuQiExportService().getWuQiEquips(role.getId());
			WuQi wuqi = WuQiUtil.coverToWuQi(wuQiInfo,wuqihuanhuanConfigList, wuqiEquips);
			//设置bus里存储的战斗力
			wuqi.setZplus(wuQiInfo.getZplus());
			role.setWuQi(wuqi); 
		}
		
		//翅膀
		List<Integer> chibanghuanhuanConfigList = StageHelper.getHuanhuaExportService().getRoleHuanhuaConfigList(role.getId(), HuanhuaConstants.HUANHUA_TYPE_2);
		ChiBangInfo chibangInfo = StageHelper.getChiBangExportService().getChiBangInfo(role.getId());
		if(chibangInfo != null){
//			TODO 翅膀装备
//			Object[] zuoqiEquips = StageHelper.getChiBangExportService().getChiBangEquips(role.getId());
			
			ChiBang chibang = ChiBangUtil.coverToChiBang(chibangInfo,chibanghuanhuanConfigList,null);// zuoqiEquips
			role.getBusinessData().setChibang(chibang);
		}

		List<Integer> qilinghuanhuanConfigList = StageHelper.getHuanhuaExportService().getRoleHuanhuaConfigList(role.getId(), HuanhuaConstants.HUANHUA_TYPE_6);
		//器灵
		QiLingInfo qilingInfo = StageHelper.getQiLingExportService().getQiLingInfo(role.getId());
		if(qilingInfo != null){
//			Object[] zuoqiEquips = StageHelper.getChiBangExportService().getChiBangEquips(role.getId());
			
			QiLing qiLing = QiLingUtil.coverToQiLing(qilingInfo,qilinghuanhuanConfigList,null);// zuoqiEquips
			role.getBusinessData().setQiling(qiLing);
		}
		//五行
		List<Integer> roleWuxing = StageHelper.getWuXingMoShenExportService().getRoleWuXings(role.getId());
		if(roleWuxing != null){
			role.getBusinessData().setWuxingList(roleWuxing);
		}

		List<Integer> tianyughuanhuanConfigList = StageHelper.getHuanhuaExportService().getRoleHuanhuaConfigList(role.getId(), HuanhuaConstants.HUANHUA_TYPE_5);
		//天羽
		TianYuInfo tianyuInfo = StageHelper.getTianYuExportService().getTianYuInfo(role.getId());
		if(tianyuInfo != null){
			
			TianYu tianyu = TianYuUtil.coverToTianYu(tianyuInfo,tianyughuanhuanConfigList,null);// zuoqiEquips
			role.getBusinessData().setTianyu(tianyu);
		}

		List<Integer> xianjianhuanhuanConfigList = StageHelper.getHuanhuaExportService().getRoleHuanhuaConfigList(role.getId(), HuanhuaConstants.HUANHUA_TYPE_3);
		//仙剑
		XianJianInfo xianjianInfo = StageHelper.getXianJianExportService().getXianJianInfo(role.getId());
		if(xianjianInfo != null){
//			TODO 翅膀装备
//			Object[] zuoqiEquips = StageHelper.getChiBangExportService().getChiBangEquips(role.getId());
			
			XianJian xianjian = XianjianUtil.coverToXianjian(xianjianInfo,xianjianhuanhuanConfigList,null);// zuoqiEquips
			role.getBusinessData().setXianjian(xianjian);
		}
		List<Integer> zhanjiahuanhuanConfigList = StageHelper.getHuanhuaExportService().getRoleHuanhuaConfigList(role.getId(), HuanhuaConstants.HUANHUA_TYPE_4);
		//战甲
		ZhanJiaInfo zhanjiaInfo = StageHelper.getZhanJiaExportService().getXianJianInfo(role.getId());
		if(zhanjiaInfo != null){
			ZhanJia zhanjia = ZhanJiaUtil.coverToXianjian(zhanjiaInfo,zhanjiahuanhuanConfigList,null);// zuoqiEquips
			role.getBusinessData().setZhanjia(zhanjia);
		}
		//灵火
		Integer linghuoId =  StageHelper.getLingHuoExportService().getLingHuoConfigId(role.getId());
		if(linghuoId != null) {
			role.getBusinessData().setLingHuo(linghuoId);
		}
		//等级属性
		Map<String,Long> attribute = expConfigExportService.getLevelAttribute(loginRole.getLevel());
		if(attribute != null){
			role.getFightAttribute().initBaseAttribute(BaseAttributeType.LEVEL,attribute);
		}
		//成就点数
		Integer chengjiuValue = StageHelper.getRoleChengJiuExportService().getChengJiuValue(role.getId());
		if(chengjiuValue == null){
			role.getBusinessData().setCjValue(0);
		}else{
			role.getBusinessData().setCjValue(chengjiuValue);
		}
		//初始化buff
		String buffStr = roleStage.getBuff();
		List<Object[]> buffs = SyncRoleStageDataUtils.decodeBuffs(buffStr);
		if(null != buffs){
			
			for(Object[] tmp : buffs){
				
				String buffCategory = (String)tmp[0];
				if(BuffRecoverIgnoreUil.contains(buffCategory)){
					continue;
				}
				
				IBuff buff = BuffFactory.recover(tmp,role);
				if(null != buff){
//					role.getBuffManager().recoverBuff(buff);
					role.getBuffManager().readyOrRecoverBuff(buff);
				}
			}
		}
		
		
        // 填充装备属性
        Object[] equips = equipExportService.getRoleEquipAttribute(roleId);
        if (null != equips) {
            // 组织格式
            RoleEquipData equipData = businessData.getEquipData();
            equipData.setEquips(equips);

            Map<String, Long> equipAttrs = new HashMap<>();
            for (Equip equip : equipData.getEquips()) {
                Map<String, Long> attributeMap = equipExportService.getEquipAllAttribute(equip.getTpVal(),equip.getGoodsId(), equip.getQianghuaLevel(), equip.getZhuShenLevel(), ContainerType.BODYTITEM);
                if (attributeMap != null) {
                    ObjectUtil.longMapAdd(equipAttrs, attributeMap);
                }
            }
            // 装备基本属性
            if (equipAttrs.size() > 0) {
                role.getFightAttribute().initBaseAttribute(BaseAttributeType.EQUIP_BASE, equipAttrs);
            }
            // 添加全身强化属性
            Map<String, Long> qsqhAttrs = equipExportService.getQSColorAddAttrs(equipData.getEquips());
            if (qsqhAttrs != null && qsqhAttrs.size() > 0) {
                role.getFightAttribute().initBaseAttribute(BaseAttributeType.EQUIP_QSQH, qsqhAttrs);
            }

            // 添加随机属性信息
            Map<String, Long> equipRandomAttrs = equipExportService.getEquipRandomAttrs(equipData.getEquips());
            if (equipRandomAttrs != null && equipRandomAttrs.size() > 0) {
                role.getFightAttribute().initBaseAttribute(BaseAttributeType.EQUIP_RANDOM_ATTR, equipRandomAttrs);
            }

            // 套装
            Map<String, Long> equipSuitAttrs = equipExportService.getEquipSuitAttrs(equipData.getEquips());
            if (equipSuitAttrs != null && equipSuitAttrs.size() > 0) {
                role.getFightAttribute().initBaseAttribute(BaseAttributeType.EQUIP_SUIT_ATTR, equipSuitAttrs);
            }

            // 添加全身神武装备强化属性
            Map<String, Long> qswsqhAttrs = equipExportService.getSWQSAddAttrs(equipData.getEquips());
            if (qswsqhAttrs != null && qswsqhAttrs.size() > 0) {
                role.getFightAttribute().initBaseAttribute(BaseAttributeType.SW_EQUIP_QSQH, qswsqhAttrs);
            }

            // TODO 其他属性,今后做到了在加
        }
        
        //填充圣剑属性
  		Map<String, Long> wuqiBaseAttrs =  StageHelper.getWuQiExportService().getWuQiAttrs(role.getId(),role.getWuQi(),role.getLevel());
  		if(wuqiBaseAttrs != null){
  			role.getFightAttribute().initBaseAttribute(BaseAttributeType.WUQI,wuqiBaseAttrs);
  			
  			Long configZplus = wuqiBaseAttrs.get(EffectType.zplus.name());
  			Long busZplus = wuQiInfo.getZplus();
  			if(!busZplus.equals(configZplus)){
  				//抛出一个异步指令让bus存储圣剑战斗力
  				StageMsgSender.send2Bus(roleId, InnerCmdType.INNER_WUQI_ZPLUS_CHANGE, configZplus);
  			}
  		}
  		
  		//圣剑 装备属性
		try{
			equipAttrs(roleId, role,ContainerType.WUQI,BaseAttributeType.WUQI_EQUIP_BASE,BaseAttributeType.WUQI_EQUIP_RANDOM_ATTR);
		}catch (Exception e) {
			ChuanQiLog.error("圣剑错误",e);
		}
  		

		//填充坐骑属性
		Map<String, Long> zuoqiBaseAttrs =  StageHelper.getZuoQiExportService().getZuoQiAttrs(role.getId(),role.getZuoQi(),role.getLevel());
		if(zuoqiBaseAttrs != null){
			role.getFightAttribute().initBaseAttribute(BaseAttributeType.ZUOQI,zuoqiBaseAttrs);
			
			Long configZplus = zuoqiBaseAttrs.get(EffectType.zplus.name());
			Long busZplus = zuoQiInfo.getZplus();
			if(!busZplus.equals(configZplus)){
				//抛出一个异步指令让bus存储坐骑战斗力
				StageMsgSender.send2Bus(roleId, InnerCmdType.INNER_ZUOQI_ZPLUS_CHANGE, configZplus);
			}
		}
		//填充坐骑装备属性
		try{
			equipAttrs(roleId, role,ContainerType.ZUOQIITEM,BaseAttributeType.ZUOQI_EQUIP_BASE,BaseAttributeType.ZUOQI_EQUIP_RANDOM_ATTR);
		}catch (Exception e) {
			ChuanQiLog.error("坐骑错误",e);
		}
		//填充翅膀属性
		Map<String, Long> chibangBaseAttrs =  StageHelper.getChiBangExportService().getChiBangAttrs(role.getId(),role.getBusinessData().getChibang());
		if(chibangBaseAttrs != null) {
			role.getFightAttribute().initBaseAttribute(BaseAttributeType.CHIBANG,chibangBaseAttrs);
			Long configZplus = chibangBaseAttrs.get(EffectType.zplus.name());
			Long busZplus = chibangInfo.getZplus();
			if(!busZplus.equals(configZplus)){
				//抛出一个异步指令让bus存储坐骑战斗力
				StageMsgSender.send2Bus(roleId, InnerCmdType.INNER_ZPLUS_CHIBANG, configZplus);
			}
		}
		//填充翅膀装备属性
		try{
			equipAttrs(roleId, role,ContainerType.CHIBANGITEM,BaseAttributeType.CHIBANG_EQUIP_BASE,BaseAttributeType.CHIBANG_EQUIP_RANDOM_ATTR);
			
//			Object[] zqEquips = equipExportService.getEquips(roleId, ContainerType.CHIBANGITEM);
//			if(zqEquips != null && zqEquips.length >0){
//				Map<String, Long> equipAttrs = new HashMap<>();
//				List<Equip> equipsList = new ArrayList<>();
//				for(Object tmp : zqEquips){
//					Equip equip = Equip.convert2Equip( (Object[])tmp );
//					equipsList.add(equip);
//					Map<String,Long> tempAttrs = equipExportService.getEquipAllAttribute(equip.getGoodsId(), equip.getQianghuaLevel(),equip.getZhuShenLevel(),ContainerType.CHIBANGITEM);
//					if(tempAttrs != null){
//						ObjectUtil.longMapAdd(equipAttrs, tempAttrs);
//					}
//				}
//				if(equipAttrs.size() >0){
//					role.getFightAttribute().initBaseAttribute(BaseAttributeType.CHIBANG_EQUIP_BASE, equipAttrs);
//				}
//				//添加随机属性信息
//				Map<String, Long> equipRandomAttrs=equipExportService.getEquipRandomAttrs(equipsList);
//				if(equipRandomAttrs !=null && equipRandomAttrs.size() >0){
//					role.getFightAttribute().initBaseAttribute(BaseAttributeType.CHIBANG_EQUIP_RANDOM_ATTR,equipRandomAttrs);
//				}
//			}
		}catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		
		//填充器灵属性
		Map<String, Long> qilingBaseAttrs =  StageHelper.getQiLingExportService().getQiLingAttrs(role.getId(),role.getBusinessData().getQiling());
		if(qilingBaseAttrs != null) {
			role.getFightAttribute().initBaseAttribute(BaseAttributeType.QILING,qilingBaseAttrs);
			
			Long configZplus = qilingBaseAttrs.get(EffectType.zplus.name());
			Long busZplus = qilingInfo.getZplus();
			if(!busZplus.equals(configZplus)){
				//抛出一个异步指令让bus存储器灵战斗力
				StageMsgSender.send2Bus(roleId, InnerCmdType.INNER_ZPLUS_QILING, configZplus);
			}
		}
		//填充天羽属性
		Map<String, Long> tianyuBaseAttrs =  StageHelper.getTianYuExportService().getTianYuAttrs(role.getId(),role.getBusinessData().getTianyu());
		if(tianyuBaseAttrs != null) {
			role.getFightAttribute().initBaseAttribute(BaseAttributeType.TIANYU,tianyuBaseAttrs);
			
			Long configZplus = tianyuBaseAttrs.get(EffectType.zplus.name());
			Long busZplus = tianyuInfo.getZplus();
			if(!busZplus.equals(configZplus)){
				//抛出一个异步指令让bus存储器灵战斗力
				StageMsgSender.send2Bus(roleId, InnerCmdType.INNER_ZPLUS_TIANYU, configZplus);
			}
		}
		//填充仙剑属性
		Map<String, Long> xianjianBaseAttrs =  StageHelper.getXianJianExportService().getXianJianAttrs(role.getId(),role.getBusinessData().getXianjian());
		if(xianjianBaseAttrs != null) {
			role.getFightAttribute().initBaseAttribute(BaseAttributeType.XIANJIAN,xianjianBaseAttrs);
			
			Long configZplus = xianjianBaseAttrs.get(EffectType.zplus.name());
			Long busZplus = xianjianInfo.getZplus();
			if(!busZplus.equals(configZplus)){
				//抛出一个异步指令让bus存储仙剑战斗力
				StageMsgSender.send2Bus(roleId, InnerCmdType.INNER_ZPLUS_XIANJIAN, configZplus);
			}
		}
		//填充战甲属性
		Map<String, Long> zhanjiaBaseAttrs =  StageHelper.getZhanJiaExportService().getXianJianAttrs(role.getId(),role.getBusinessData().getZhanjia());
		if(zhanjiaBaseAttrs != null) {
			role.getFightAttribute().initBaseAttribute(BaseAttributeType.ZHANJIA,zhanjiaBaseAttrs);
			
			Long configZplus = zhanjiaBaseAttrs.get(EffectType.zplus.name());
			Long busZplus = zhanjiaInfo.getZplus();
			if(!busZplus.equals(configZplus)){
				//抛出一个异步指令让bus存储仙剑战斗力
				StageMsgSender.send2Bus(roleId, InnerCmdType.INNER_ZPLUS_ZHANJIA, configZplus);
			}
		}
		//填充五行属性
		Map<String, Long> wuxingBaseAttrs =  StageHelper.getWuXingMoShenExportService().getWuXingAttrs(role.getId(),role.getBusinessData().getWuxingList());
		if(!ObjectUtil.isEmpty(wuxingBaseAttrs)) {
			role.getFightAttribute().initBaseAttribute(BaseAttributeType.WUXING,wuxingBaseAttrs);
		}
		//填充五行技能属性
		Map<String, Long> wuxingSkillBaseAttrs =  StageHelper.getWuXingMoShenExportService().getWuXingSkillAttrs(role.getId());
		if(!ObjectUtil.isEmpty(wuxingSkillBaseAttrs)) {
		    role.getFightAttribute().initBaseAttribute(BaseAttributeType.WUXING_SKILL,wuxingSkillBaseAttrs);
		}
		//填充五行魔神精魄属性
		Map<String, Long> wuxingJpBaseAttrs =  StageHelper.getWuXingMoShenExportService().getWuXingJpAttrs(role.getId());
		if(!ObjectUtil.isEmpty(wuxingJpBaseAttrs)) {
		    role.getFightAttribute().initBaseAttribute(BaseAttributeType.WUXING_JP,wuxingJpBaseAttrs);
		}
		//填充糖宝五行属性
        Map<String, Long> tbWuxingBaseAttrs =  StageHelper.getWuXingMoShenExportService().getTbWuXingAttrs(role.getId());
        if(!ObjectUtil.isEmpty(tbWuxingBaseAttrs)) {
            role.getFightAttribute().initBaseAttribute(BaseAttributeType.TB_WUXING,tbWuxingBaseAttrs);
        }
        //填充糖宝五行技能属性
        Map<String, Long> tbWuxingSkillBaseAttrs =  StageHelper.getWuXingMoShenExportService().getTbWuXingSkillAttrs(role.getId());
        if(!ObjectUtil.isEmpty(tbWuxingSkillBaseAttrs)) {
            role.getFightAttribute().initBaseAttribute(BaseAttributeType.TB_WUXING_SKILL,tbWuxingSkillBaseAttrs);
        }
        //填充心魔属性
        Map<String, Long> xinmoAttrs =  StageHelper.getXinmoExportService().getXinmoAttrs(role.getId());
        if(!ObjectUtil.isEmpty(xinmoAttrs)) {
            role.getFightAttribute().initBaseAttribute(BaseAttributeType.XINMO,xinmoAttrs);
        }
        //填充心魔-魔神属性
        Map<String, Long> xinmoMoshenAttrs =  StageHelper.getXinmoExportService().getXinmoMoshenAttrs(role.getId());
        if(!ObjectUtil.isEmpty(xinmoMoshenAttrs)) {
            role.getFightAttribute().initBaseAttribute(BaseAttributeType.XINMO_MOSHEN,xinmoMoshenAttrs);
        }
        //填充心魔-魔神噬体属性
        Map<String, Long> xinmoMoshenShitiAttrs = StageHelper.getXinmoExportService().getXinmoMoshenShitiAttrs(role.getId());
        if (!ObjectUtil.isEmpty(xinmoMoshenShitiAttrs)) {
            //增加心魔-魔神被动噬体技能属性
            ObjectUtil.longMapAdd(xinmoMoshenShitiAttrs, StageHelper.getXinmoExportService().getXinmoShitiSkillAttrs(role.getId()));
            role.getFightAttribute().initBaseAttribute(BaseAttributeType.XINMO_MOSHEN_SHITI, xinmoMoshenShitiAttrs);
        }
        //填充心魔-魔神普通被动技能属性
        Map<String, Long> xinmoSkillAttrs =  StageHelper.getXinmoExportService().getXinmoNormalSkillAttrs(role.getId());
        if(!ObjectUtil.isEmpty(xinmoSkillAttrs)) {
            role.getFightAttribute().initBaseAttribute(BaseAttributeType.XINMO_SKILL,xinmoSkillAttrs);
        }
        //填充心魔-洗练附加的所有永久属性
        Map<String, Long> xinmoXilianAttrs =  StageHelper.getXinmoExportService().getXinmoXilianAttrs(role.getId());
        if(!ObjectUtil.isEmpty(xinmoXilianAttrs)) {
            role.getFightAttribute().initBaseAttribute(BaseAttributeType.XINMO_XILIAN,xinmoXilianAttrs);
        }
        //填充仙洞属性
        Map<String, Long> xiandongAttrs =  StageHelper.getXianqiServiceExport().getXiandongAttrMap(role.getId());
        if(!ObjectUtil.isEmpty(xiandongAttrs)) {
            role.getFightAttribute().initBaseAttribute(BaseAttributeType.XIANDONG,xiandongAttrs);
        }
        //填充仙器觉醒属性
        Map<String, Long> xianqiJuexingAttrs =  StageHelper.getXianqiServiceExport().getXianqiJuexingAttrMap(role.getId());
        if(!ObjectUtil.isEmpty(xianqiJuexingAttrs)) {
            role.getFightAttribute().initBaseAttribute(BaseAttributeType.XIANQIJUEXING,xianqiJuexingAttrs);
        }
        
        //仙缘飞化属性
        Map<String, Long> xianYuanFeiHuaAttrs =  StageHelper.getXianYuanFeiHuaServiceExport().getXianYuanFeiHuaAttr(role.getId());
        if(!ObjectUtil.isEmpty(xianYuanFeiHuaAttrs)) {
            role.getFightAttribute().initBaseAttribute(BaseAttributeType.XIAYUANFEIHUA,xianYuanFeiHuaAttrs);
        }
        
        //神器进阶属性
        Map<String, Long> shenQiJinJieAttrs =  StageHelper.getShenQiJinJieExportService().getShenQiJinJieAttr(role.getId());
        if(!ObjectUtil.isEmpty(shenQiJinJieAttrs)) {
            role.getFightAttribute().initBaseAttribute(BaseAttributeType.SHENQI_JINJIE,shenQiJinJieAttrs);
        }
        
        //套装象位属性
        Map<String, Long> suitXiangweiAttrs =  StageHelper.getRoleXiangweiExportService().getSuitXiangweiAttr(role.getId());
        if(!ObjectUtil.isEmpty(suitXiangweiAttrs)) {
        	role.getFightAttribute().initBaseAttribute(BaseAttributeType.SUIT_XIANGWEI_ATTR,suitXiangweiAttrs);
        }
        
        Map<String, Long> bossJifenAttr =  StageHelper.getRoleBossJifenExportService().getBossJifenAttr(role.getId());
        if(!ObjectUtil.isEmpty(bossJifenAttr)) {
        	role.getFightAttribute().initBaseAttribute(BaseAttributeType.BOSS_JIFEN_ATTR,bossJifenAttr);
        }
        
		//糖宝心纹
		Map<String,Long> xinwenAttribute  = null;
		 try {
			 xinwenAttribute = xinwenExportService.getXinwenAttributeAfterLogin(roleId);
			if(xinwenAttribute != null){
				role.getFightAttribute().initBaseAttribute(BaseAttributeType.TANGBAO_XINWEN,xinwenAttribute);
				role.getBusinessData().setTangbaoXinwenJie(xinwenExportService.getTangbaoXinwenJie(roleId));
			 }
		 } catch (Exception e) {
			ChuanQiLog.error("***error: tangbaoXinwen add attribute***",e);
		}
		
		//宠物创建
		int petState = tangbaoExportService.getTangbaoState(roleId);
		businessData.setPetState(petState);
		Map<String, Long> petBaseAttr = tangbaoExportService.getTangbaoBaseAttibute(roleId, loginRole.getLevel());
		Pet pet = null;
		if(petBaseAttr != null){//有宝宝
			PetVo petVo = dataContainer.getData(GameConstants.COMPONENT_NAME_PET, role.getId().toString());
			boolean create = petVo == null;
			Long tbId = null;
			if(petVo != null){
				tbId = petVo.getTbId();
			}
			pet = PetFactory.create(role, petBaseAttr,create,tbId);
            // 添加糖宝战斗属性
            if (null != xianjianBaseAttrs) {
                pet.getFightAttribute().initBaseAttribute(BaseAttributeType.XIANJIAN, xianjianBaseAttrs);
            }
            if (null != zhanjiaBaseAttrs) {
                pet.getFightAttribute().initBaseAttribute(BaseAttributeType.ZHANJIA, zhanjiaBaseAttrs);
            }
            if (null != xinwenAttribute) {
                pet.getFightAttribute().initBaseAttribute(BaseAttributeType.TANGBAO_XINWEN, xinwenAttribute);
            }
            if(null != tianyuBaseAttrs){
                pet.getFightAttribute().initBaseAttribute(BaseAttributeType.TIANYU, tianyuBaseAttrs);
            }
            if(null != tbWuxingBaseAttrs){
                pet.getFightAttribute().initBaseAttribute(BaseAttributeType.TB_WUXING, tbWuxingBaseAttrs);
            }
            if(null != tbWuxingSkillBaseAttrs){
                pet.getFightAttribute().initBaseAttribute(BaseAttributeType.TB_WUXING_SKILL, tbWuxingSkillBaseAttrs);
            }
			pet.getFightAttribute().refresh();
			if(!create){
				pet.getFightAttribute().setCurHp(petVo.getCurHp());
				pet.setState(petVo.getState());
				pet.setPetVo(petVo);
				petVo.setPet(pet);
			}else{
				dataContainer.putData(GameConstants.COMPONENT_NAME_PET, role.getId().toString(),pet.getPetVo());
			}
			role.setPet(pet); 
			
			//宝宝给主人增加属性
			Map<String, Long> petAddAtt = tangbaoExportService.getTangbaoRoleAttribute(roleId);
			role.getFightAttribute().initBaseAttribute(BaseAttributeType.PET_EAT,petAddAtt);
		}
		//填充糖宝装备属性
		try{
			Object[] zqEquips = equipExportService.getEquips(roleId, ContainerType.TANGBAOITEM);
			if(zqEquips != null && zqEquips.length >0){
				Map<String, Long> equipAttrs = new HashMap<>();
				List<Equip> equipsList = new ArrayList<>();
				for(Object tmp : zqEquips){
					Equip equip = Equip.convert2Equip( (Object[])tmp );
					equipsList.add(equip);
					Map<String,Long> tempAttrs = equipExportService.getEquipAllAttribute(equip.getTpVal(),equip.getGoodsId(), equip.getQianghuaLevel(),equip.getZhuShenLevel(),ContainerType.TANGBAOITEM);
					if(tempAttrs != null){
						ObjectUtil.longMapAdd(equipAttrs, tempAttrs);
					}
				}
				if(equipAttrs.size() >0){
					role.getFightAttribute().initBaseAttribute(BaseAttributeType.TANGBAO_EQUIP_BASE, equipAttrs);
					if(pet != null){
						pet.getFightAttribute().initBaseAttribute(BaseAttributeType.TANGBAO_EQUIP_BASE, equipAttrs);
					}
				}
				//添加随机属性信息
				Map<String, Long> equipRandomAttrs=equipExportService.getEquipRandomAttrs(equipsList);
				if(equipRandomAttrs !=null && equipRandomAttrs.size() >0){
					role.getFightAttribute().initBaseAttribute(BaseAttributeType.TANGBAO_EQUIP_RANDOM_ATTR,equipRandomAttrs);
					if(pet != null){
						pet.getFightAttribute().initBaseAttribute(BaseAttributeType.TANGBAO_EQUIP_RANDOM_ATTR, equipRandomAttrs);
					}
				}
			}
		}catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		//填充天工装备属性
		try{
			Object[] zqEquips = equipExportService.getEquips(roleId, ContainerType.TIANGONGITEM);
			if(zqEquips != null && zqEquips.length >0){
				Map<String, Long> equipAttrs = new HashMap<>();
				List<Equip> equipsList = new ArrayList<>();
				for(Object tmp : zqEquips){
					Equip equip = Equip.convert2Equip( (Object[])tmp );
					equipsList.add(equip);
					Map<String,Long> tempAttrs =equipExportService.getEquipAllAttribute(equip.getTpVal(),equip.getGoodsId(), equip.getQianghuaLevel(),equip.getZhuShenLevel(),ContainerType.TIANGONGITEM);
					if(tempAttrs != null){
						ObjectUtil.longMapAdd(equipAttrs, tempAttrs);
					}
				}
				if(equipAttrs.size() >0){
					role.getFightAttribute().initBaseAttribute(BaseAttributeType.TIANGONG_EQUIP_BASE, equipAttrs);
					if(pet != null){
						pet.getFightAttribute().initBaseAttribute(BaseAttributeType.TIANGONG_EQUIP_BASE, equipAttrs);
					}
				}
				//添加随机属性信息
				Map<String, Long> equipRandomAttrs=equipExportService.getEquipRandomAttrs(equipsList);
				if(equipRandomAttrs !=null && equipRandomAttrs.size() >0){
					role.getFightAttribute().initBaseAttribute(BaseAttributeType.TIANGONG_EQUIP_RANDOM_ATTR,equipRandomAttrs);
					if(pet != null){
						pet.getFightAttribute().initBaseAttribute(BaseAttributeType.TIANGONG_EQUIP_RANDOM_ATTR, equipRandomAttrs);
					}
				}
			}
		}catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		//填充天裳装备属性
		try{
			Object[] zqEquips = equipExportService.getEquips(roleId, ContainerType.TIANSHANGITEM);
			if(zqEquips != null && zqEquips.length >0){
				Map<String, Long> equipAttrs = new HashMap<>();
				List<Equip> equipsList = new ArrayList<>();
				for(Object tmp : zqEquips){
					Equip equip = Equip.convert2Equip( (Object[])tmp );
					equipsList.add(equip);
					Map<String,Long> tempAttrs = equipExportService.getEquipAllAttribute(equip.getTpVal(),equip.getGoodsId(), equip.getQianghuaLevel(),equip.getZhuShenLevel(),ContainerType.TIANSHANGITEM);
					if(tempAttrs != null){
						ObjectUtil.longMapAdd(equipAttrs, tempAttrs);
					}
				}
				if(equipAttrs.size() >0){
					role.getFightAttribute().initBaseAttribute(BaseAttributeType.TIANSHANG_EQUIP_BASE, equipAttrs);
					if(pet != null){
						pet.getFightAttribute().initBaseAttribute(BaseAttributeType.TIANSHANG_EQUIP_BASE, equipAttrs);
					}
				}
				//添加随机属性信息
				Map<String, Long> equipRandomAttrs=equipExportService.getEquipRandomAttrs(equipsList);
				if(equipRandomAttrs !=null && equipRandomAttrs.size() >0){
					role.getFightAttribute().initBaseAttribute(BaseAttributeType.TIANSHANG_EQUIP_RANDOM_ATTR,equipRandomAttrs);
					if(pet != null){
						pet.getFightAttribute().initBaseAttribute(BaseAttributeType.TIANSHANG_EQUIP_RANDOM_ATTR, equipRandomAttrs);
					}
				}
			}
		}catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		//填充器灵装备属性
		try{
			Object[] zqEquips = equipExportService.getEquips(roleId, ContainerType.QILINGITEM);
			if(zqEquips != null && zqEquips.length >0){
				Map<String, Long> equipAttrs = new HashMap<>();
				List<Equip> equipsList = new ArrayList<>();
				for(Object tmp : zqEquips){
					Equip equip = Equip.convert2Equip( (Object[])tmp );
					equipsList.add(equip);
					Map<String,Long> tempAttrs = equipExportService.getEquipAllAttribute(equip.getTpVal(),equip.getGoodsId(), equip.getQianghuaLevel(),equip.getZhuShenLevel(),ContainerType.QILINGITEM);
					if(tempAttrs != null){
						ObjectUtil.longMapAdd(equipAttrs, tempAttrs);
					}
				}
				if(equipAttrs.size() >0){
					role.getFightAttribute().initBaseAttribute(BaseAttributeType.QILING_EQUIP_BASE, equipAttrs);
				}
				//添加随机属性信息
				Map<String, Long> equipRandomAttrs=equipExportService.getEquipRandomAttrs(equipsList);
				if(equipRandomAttrs !=null && equipRandomAttrs.size() >0){
					role.getFightAttribute().initBaseAttribute(BaseAttributeType.QILING_EQUIP_RANDOM_ATTR,equipRandomAttrs);
				}
			}
		}catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		//填充天羽装备属性
		try{
			Object[] zqEquips = equipExportService.getEquips(roleId, ContainerType.TIANYUITEM);
			if(zqEquips != null && zqEquips.length >0){
				Map<String, Long> equipAttrs = new HashMap<>();
				List<Equip> equipsList = new ArrayList<>();
				for(Object tmp : zqEquips){
					Equip equip = Equip.convert2Equip( (Object[])tmp );
					equipsList.add(equip);
					Map<String,Long> tempAttrs = equipExportService.getEquipAllAttribute(equip.getTpVal(),equip.getGoodsId(), equip.getQianghuaLevel(),equip.getZhuShenLevel(),ContainerType.TIANYUITEM);
					if(tempAttrs != null){
						ObjectUtil.longMapAdd(equipAttrs, tempAttrs);
					}
				}
				if(equipAttrs.size() >0){
					role.getFightAttribute().initBaseAttribute(BaseAttributeType.TIANYU_EQUIP_BASE, equipAttrs);
				}
				//添加随机属性信息
				Map<String, Long> equipRandomAttrs=equipExportService.getEquipRandomAttrs(equipsList);
				if(equipRandomAttrs !=null && equipRandomAttrs.size() >0){
					role.getFightAttribute().initBaseAttribute(BaseAttributeType.TIANYU_EQUIP_RANDOM_ATTR,equipRandomAttrs);
				}
			}
		}catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		
		if(pet != null){
			pet.getFightAttribute().refresh();
		}
		
		//填充灵火属性
		Map<String, Long> linghuoBaseAttrs =  StageHelper.getLingHuoExportService().getLingHuoAttrs(role.getId(),role.getBusinessData().getLingHuo());
		if(linghuoBaseAttrs != null) {
			role.getFightAttribute().initBaseAttribute(BaseAttributeType.LINGHUO,linghuoBaseAttrs);
		}
		//填充灵火祝福属性
		Map<String, Long> linghuoBlessBaseAttrs =  StageHelper.getLingHuoExportService().getLinghuoBlessAllAttrsMap(role.getId());
		if(linghuoBlessBaseAttrs != null) {
		    role.getFightAttribute().initBaseAttribute(BaseAttributeType.LINGHUO_BLESS,linghuoBlessBaseAttrs);
		}
		//填充成就属性
		Map<String, Long> chengjiuAttrs =  StageHelper.getRoleChengJiuExportService().getChengJiuAttrs(role.getId());
		if(chengjiuAttrs != null) {
			role.getFightAttribute().initBaseAttribute(BaseAttributeType.CHENGJIU,chengjiuAttrs);
		}
		//门派勋章-领地战
		Map<String,Long> guildXuanZhangTerritoryAttr = territoryExportService.getGuildXunZhangAttr(role.getId());
		if(guildXuanZhangTerritoryAttr != null) {
			role.getFightAttribute().initBaseAttribute(BaseAttributeType.GUILD_XUANZHANG_TERRITORY,guildXuanZhangTerritoryAttr);
		}
		//宠物属性
		try{
			Map<String,Long> chongwuAttr = chongwuExportService.getAllChongwuAttribute(role.getId());
			if(chongwuAttr != null) {
				role.getFightAttribute().initBaseAttribute(BaseAttributeType.CHONGWU,chongwuAttr);
			}
		}catch (Exception e) {
			ChuanQiLog.error("***ERROR: chongwu add attribute***",e);
		}
		//宠物技能属性
		Map<String,Long> chongwuSkillAttr = chongwuExportService.getAllChongwuSkillAttribute(role.getId());
		if(!ObjectUtil.isEmpty(chongwuSkillAttr)){
		    role.getFightAttribute().initBaseAttribute(BaseAttributeType.CHONGWU_SKILL,chongwuSkillAttr);
		}
		//填充宠物装备属性
        try{
            Object[] chongwuEquips = equipExportService.getEquips(roleId, ContainerType.CHONGWUITEM);
            if(chongwuEquips != null && chongwuEquips.length >0){
                Map<String, Long> equipAttrs = new HashMap<>();
                List<Equip> equipsList = new ArrayList<>();
                for(Object tmp : chongwuEquips){
                    Equip equip = Equip.convert2Equip( (Object[])tmp );
                    equipsList.add(equip);
                    Map<String,Long> tempAttrs = equipExportService.getEquipAllAttribute(equip.getTpVal(),equip.getGoodsId(), equip.getQianghuaLevel(),equip.getZhuShenLevel(),ContainerType.CHONGWUITEM);
                    if(tempAttrs != null){
                        ObjectUtil.longMapAdd(equipAttrs, tempAttrs);
                    }
                }
                if(equipAttrs.size() >0){
                    role.getFightAttribute().initBaseAttribute(BaseAttributeType.CHONGWU_EQUIP_BASE, equipAttrs);
                }
                //添加随机属性信息
                Map<String, Long> equipRandomAttrs=equipExportService.getEquipRandomAttrs(equipsList);
                if(equipRandomAttrs !=null && equipRandomAttrs.size() >0){
                    role.getFightAttribute().initBaseAttribute(BaseAttributeType.CHONGWU_EQUIP_RANDOM_ATTR,equipRandomAttrs);
                }
            }
        }catch (Exception e) {
            ChuanQiLog.error("填充宠物装备属性异常:{}",e);
        }
        
		//背包属性增加
		Map<String, Long>  bsAttrs = StageHelper.getRoleBagExportService().getBagStorageAttrs(role.getId());
		if(bsAttrs != null){
			role.getFightAttribute().initBaseAttribute(BaseAttributeType.BAG_SLOG,bsAttrs);
		}
		
		//画卷属性增加
		Map<String, Long>  huajuanAttrs = StageHelper.getHuajuanExportService().getHuajuanAttr(role.getId());
		if(huajuanAttrs != null){
			role.getFightAttribute().initBaseAttribute(BaseAttributeType.HUAJUAN,huajuanAttrs);
		}
        //画卷2属性增加
        Map<String, Long> huajuanAttrs2 = StageHelper.getHuajuan2ExportService().getHuajuan2Attr(role.getId());
        if(huajuanAttrs2 != null){
            role.getFightAttribute().initBaseAttribute(BaseAttributeType.HUAJUAN2, huajuanAttrs2);
        }
		
		//VIP属性增加
		RoleVipWrapper vip = roleVipInfoExportService.getRoleVipInfo(roleId);
		businessData.setVipLevel(vip.getVipLevel());
		VipConfig vipConfig = vipConfigExportService.getVipConfigById(vip.getVipId());
		if(vipConfig != null){
			role.getFightAttribute().initBaseAttribute(BaseAttributeType.VIP, vipConfig.getAttribute());
		}
		
		//设置道具信息
		PropModel propModel = PropFormatHelper.decodeAll(roleStage.getProp(), role);
		role.setPropModel( propModel );
		roleStage.setProp( propModel.format() );
		
		if(roleStage.isLogin()){//登陆操作
			propModel.noticeClient();
		}
		
		//镖车
		Biaoche biaoche = dataContainer.getData(GameConstants.COMPONENT_BIAOCHE_NMAE, role.getId()+"");
		if(biaoche != null){
			if(biaoche.isChangeMap()){
				
				YaBiaoConfig ybConfig = yaBiaoConfigExportService.getYabiaoConfigById(biaoche.getBcConfigId());
				if(ybConfig != null){
					
					long curHp = biaoche.getFightAttribute().getCurHp();
					
					long expireTime = biaoche.getExpireTimes();			
					if(expireTime <= 0){
						expireTime = 10000;//毫秒
					}
					biaoche = BiaoCheFactory.createBiaoche(role, ybConfig, expireTime);
					
					biaoche.getFightAttribute().setCurHp(curHp);
					biaoche.setChangeMap(true);
					
					dataContainer.putData(GameConstants.COMPONENT_BIAOCHE_NMAE, role.getId()+"", biaoche);	
				}
			}
			
			biaoche.setOwner(role);
			role.setBiaoche(biaoche);

			//押镖状态减速
			try{
				YabiaoPublicConfig ybPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_YABIAO);
				long charnewspeed =  (long)ybPublicConfig.getCharnewspeed();//押镖修改移动属性
				Map<String,Long> valMap = new HashMap<>();
				valMap.put(EffectType.x80.name(),charnewspeed);
				role.getFightAttribute().setBaseAttribute(BaseAttributeType.JB_BIAOCE_ATTR,valMap);
			} catch (Exception e){
				ChuanQiLog.error("***ERROR: yabiao speed update attribute***",e);
			}
		}
		
		/**
		 * 填充  和平模式
		 * **/
		role.setBattleMode(BattleMode.PEACE);
		
		//微端登录BUFF处理
//		boolean isWeiDuanBuff = nodeControlExportService.isWeiDuanLogin(role.getBusinessData().getUserId());
//		if(isWeiDuanBuff){
//			RoleBasePublicConfig roleBasePublicConfig = gongGongShuJuBiaoConfigExportService.loadByRoleBaseId();
//			
//			String wdBuffId = roleBasePublicConfig.getWeiDuanBuff();
//			IBuff buff = BuffFactory.create(role, role, wdBuffId);
//			if(buff != null){
//				role.getBuffManager().addBuff(buff);
//			}
//		}
		
		//组件绑定监听器
		FightListener fightListener = new FightListener(role.getFightStatistic());
		role.getFightAttribute().addListener(fightListener);
		role.getStateManager().addListener(fightListener);
		role.getBuffManager().addListener(fightListener);
		role.getStateManager().addListener(new RoleStateExceptionListener(role));
		role.getStateManager().addListener(new ZuoQiListener(role));
		role.getStateManager().addListener(new DaZuoListener(role));
//		//GM状态处理
//		if(loginRole.isGm()){
//			if(dataContainer.getData(StageConstants.COMPONENT_GM_HANLE_YS, roleId) == null){
//				role.setYinshen(1);
//			}
//			role.setIsGm(1);
//			role.getStateManager().add(new NoBeiAttack());//无法被攻击
//			role.getStateManager().add(new NoAttack());//无法攻击
//		}
		
		//填充技能
		List<RoleSkill> list = roleSkillExportService.loadAllRoleSkill(roleId);
		long skillZplus = 0;//主动技能战力
		Map<String,Long> beidongAttribute = new HashMap<>();
		Map<String,Long> tbBeidongAttribute = new HashMap<>();
		Map<String,Long> guildSkillAttribute = new HashMap<>();
		Set<String> noticeSkills = new HashSet<>();
		for (RoleSkill roleSkill : list) {
			if(roleSkill.getType().intValue() == GameConstants.SKILL_TYPE_ZHUDONG){//主动技能
				ISkill skill = SkillManager.getManager().getSkill(roleSkill.getSkillId(), roleSkill.getLevel());
				if(skill == null){
					continue;
				}
				if(!role.isYaoshen() || SkillFireType.isNoChangeClearSkill(skill.getSkillConfig().getSkillFireType())){
					role.addSkill(skill);
				}
				role.addSkillShulian(roleSkill);
				skillZplus += skill.getSkillXueXiConfig().getZplus();
			}else if(roleSkill.getType().intValue() == GameConstants.SKILL_TYPE_BEIDONG){//被动技能
				BeiDongSkillConfig config = beiDongSkillConfigExportService.loadById(roleSkill.getSkillId());
				if(config == null){
					continue;
				}
				BeiDongSkillXueXiConfig xuexiConfig = config.getConfig(roleSkill.getLevel());
				if(xuexiConfig == null){
					continue;
				}
				ObjectUtil.longMapAdd(beidongAttribute, xuexiConfig.getAttributes());
			}else if(roleSkill.getType().intValue() == GameConstants.SKILL_TYPE_TANGBAO){//糖宝被动技能
			    if(!roleSkillExportService.isTbSkillZhuangbei(role.getId(), roleSkill.getSkillId())){
                    continue;
                }
				TangBaoSkillConfig config = tangBaoSkillConfigExportService.loadById(roleSkill.getSkillId());
				if(config == null){
					continue;
				}
				TangBaoSkillXueXiConfig xuexiConfig = config.getConfig(roleSkill.getLevel());
				if(xuexiConfig == null){
					continue;
				}
				ObjectUtil.longMapAdd(tbBeidongAttribute, xuexiConfig.getAttributes());
			}else if(roleSkill.getType().intValue() == GameConstants.SKILL_TYPE_XINFA){//心法技能
				//TODO
			}else if(roleSkill.getType().intValue() == GameConstants.SKILL_TYPE_GUILD){//帮派技能
				GuildBeiDongSkillConfig config = guildBeiDongSkillConfigExportService.loadById(roleSkill.getSkillId());
				if(config == null){
					continue;
				}
				GuildBeiDongSkillXueXiConfig xuexiConfig = config.getConfig(roleSkill.getLevel());
				if(xuexiConfig == null){
					continue;
				}
				ObjectUtil.longMapAdd(guildSkillAttribute, xuexiConfig.getAttributes());
			}else if(roleSkill.getType().intValue() == GameConstants.SKILL_TYPE_YAOSHEN){//妖神技能
				ISkill skill = SkillManager.getManager().getSkill(roleSkill.getSkillId(), roleSkill.getLevel());
				if(skill == null){
					continue;
				}
				if(role.isYaoshen()){
					role.addSkill(skill);
				}
				role.addSkillShulian(roleSkill);
				skillZplus += skill.getSkillXueXiConfig().getZplus();
			}
		}
		role.setNoticeSkills(noticeSkills);
		if(skillZplus > 0){
			Map<String,Long> skillZplusMap = new HashMap<>();
			skillZplusMap.put(EffectType.zplus.name(), skillZplus);
			role.getFightAttribute().initBaseAttribute(BaseAttributeType.SKILL, skillZplusMap);
		}
		if(beidongAttribute.size() > 0){
			role.getFightAttribute().initBaseAttribute(BaseAttributeType.BEIDONG_SKILL, beidongAttribute);
		}
		if(tbBeidongAttribute.size() > 0){
		    role.getFightAttribute().initBaseAttribute(BaseAttributeType.TB_BEIDONG_SKILL, tbBeidongAttribute);
		    if(pet != null){
		        pet.getFightAttribute().incrBaseAttribute(BaseAttributeType.TB_BEIDONG_SKILL, tbBeidongAttribute);
		    }
		}
		if(guildSkillAttribute.size() > 0){
			role.getFightAttribute().initBaseAttribute(BaseAttributeType.GUILD_SKILL, guildSkillAttribute);
		}

		
		//神器属性
//		Map<String,Long> shenqiAttribute = shenQiExportService.getActivatedShenqiAttr(role.getId());
//		if(shenqiAttribute != null){
//			role.getFightAttribute().initBaseAttribute(BaseAttributeType.SHENQI,shenqiAttribute);
//		}
		//神器属性
		try {
			Map<String,Long> attr=shenQiExportService.onlineInitAttr(roleId);
			if(attr!=null){
				role.getFightAttribute().initBaseAttribute(BaseAttributeType.SHENQI,attr);
			}
		} catch (Exception e) {
			ChuanQiLog.error("***ERROR: shenQi wash  add attribute***",e);
		}
		
		//神器祝福属性
		Integer zuoqiLevel = role.getZuoQi()==null?null:role.getZuoQi().getZuoQiLevel();
		Integer chibangLevel = role.getBusinessData().getChibang()==null?null:role.getBusinessData().getChibang().getZuoQiLevel();
		Integer xianjianLevel = role.getBusinessData().getXianjian()==null?null:role.getBusinessData().getXianjian().getXianjianLevel();
		Integer zhanjiaLevel = role.getBusinessData().getZhanjia()==null?null:role.getBusinessData().getZhanjia().getXianjianLevel();
//		Integer wuQiLevel = role.getBusinessData().getZhanjia()==null?null:role.getBusinessData().getZhanjia().getXianjianLevel();
		
		Map<Integer,Integer> typeLevels = new HashMap<>();
		typeLevels.put(ShenQiConstants.ZHUFU_TYPE_ZUOQI, zuoqiLevel);
		typeLevels.put(ShenQiConstants.ZHUFU_TYPE_CHIBANG, chibangLevel);
		typeLevels.put(ShenQiConstants.ZHUFU_TYPE_XIANJIAN, xianjianLevel);
		typeLevels.put(ShenQiConstants.ZHUFU_TYPE_ZHANJIA, zhanjiaLevel);
		
		//TODO  等待加模块  typeLevels.put(ShenQiConstants.ZHUFU_TYPE_WUQI, wuQiLevel);
		
		Map<String,Long> shenqiZhufuAttribute = shenQiExportService.getActivatedShenqiZhufuAttr(role.getId(),typeLevels);
		if(shenqiZhufuAttribute != null){
			role.getFightAttribute().initBaseAttribute(BaseAttributeType.SHENQI_ZHUFU,shenqiZhufuAttribute);
		}
		
		//灵境属性
		Map<String,Long> lingjingAttribute = lingJingExportService.getAttribute(roleId);
		if(lingjingAttribute != null && lingjingAttribute.size() > 0){
			role.getFightAttribute().initBaseAttribute(BaseAttributeType.LINGJING,lingjingAttribute);
		}
		
		//称号属性
		Map<String,Long> chenghaoAttribute = chenghaoExportService.getChenghaoAttribute(roleId);
		if(chenghaoAttribute != null){
			role.getFightAttribute().initBaseAttribute(BaseAttributeType.CHENGHAO,chenghaoAttribute);
		}
		//妖神属性
		Map<String,Long> yaoshenAttribute = yaoshenExportService.getYaoshenAttribute(roleId,false);
		if(yaoshenAttribute != null){
			role.getFightAttribute().initBaseAttribute(BaseAttributeType.YAOSHEN,yaoshenAttribute);
		}
		try {
			//妖神魔纹属性
			Map<String,Long> yaoshenMowenAttribute = yaoshenExportService.getYaoshenMowenAttributeAfterLogin(roleId);
			if(yaoshenMowenAttribute != null){
				role.getFightAttribute().initBaseAttribute(BaseAttributeType.YAOSHEN_MOWEN,yaoshenMowenAttribute);
			}
			role.setYaoshenMowenJie(yaoshenExportService.getMowenJie(roleId));//设置角色魔纹阶数	
		} catch (Exception e) {
			ChuanQiLog.error("***error: yaoshenMowen add attribute***",e);
		}
		try {
			//妖神魂魄属性
			Map<String,Long> hunpoAttribute = yaoshenHunpoExportService.getYaoshenHunpoAttribute(roleId);
			if(hunpoAttribute != null){
				role.getFightAttribute().initBaseAttribute(BaseAttributeType.YAOSHEN_HUNPO,hunpoAttribute);
			}
		} catch (Exception e) {
			ChuanQiLog.error("***error: yaoshenHunpo add attribute***",e);
		}
		
		try {
			//妖神魔印属性
			Map<String,Long> moYinAttribute = yaoshenMoYinExportService.getYaoshenMoYinAttribute(roleId);
			if(moYinAttribute != null){ 
				role.getFightAttribute().initBaseAttribute(BaseAttributeType.YAOSHEN_MOYIN,moYinAttribute);
			}
		} catch (Exception e) {
			ChuanQiLog.error("***error: yaoshenMoYin add attribute***",e);
		}
		try {
			//妖神附魔属性
			Map<String,Long> fumoAttribute = yaoshenFumoExportService.getYaoshenFumoAttributeAfterLogin(roleId);
			if(fumoAttribute != null && !fumoAttribute.isEmpty()){ 
				role.getFightAttribute().initBaseAttribute(BaseAttributeType.YAOSHEN_FUMO,fumoAttribute);
			}
		} catch (Exception e) {
			ChuanQiLog.error("***error: fumoAttribute add attribute***",e);
		}
		  
		try {
			//妖神魂魄--魄神属性
			Map<String,Long> poshenAttribute = yaoshenHunpoExportService.getHunpoAllPoshenAttribute(roleId);
			if(poshenAttribute != null){
				role.getFightAttribute().initBaseAttribute(BaseAttributeType.YAOSHEN_HUNPO_POSHEN,poshenAttribute);
			}
		} catch (Exception e) {
			ChuanQiLog.error("***error: yaoshenHunpo--poshen add attribute***",e);
		}
		
		 
		 try {
			//通天之路
			Map<String,Long> tongtianAttribute = tongtianLoadExportService.initAttrMap(roleId);
			if(tongtianAttribute != null){
				role.getFightAttribute().initBaseAttribute(BaseAttributeType.TONGTIAN_ROAD,tongtianAttribute);
			 }
		} catch (Exception e) {
			ChuanQiLog.error("***error: tongtianLoad add attribute***",e);
		}
		 
		try {
			//成神属性
			Map<String,Long> chengShenAttribute = chengShenExportService.initAttrMap(roleId);
			if(chengShenAttribute != null){
				role.getFightAttribute().initBaseAttribute(BaseAttributeType.CHENG_SHEN,chengShenAttribute);
			}
		} catch (Exception e) {
			ChuanQiLog.error("***error: chengShen add attribute***",e);
		}
		
		//宝石属性
		try {
			Map<String,Long> jewelAttribute =jewelExportService.countJewelAttr(roleId);
			if(jewelAttribute!=null){
				role.getFightAttribute().initBaseAttribute(BaseAttributeType.JEWEL,jewelAttribute);
			}
		} catch (Exception e) {
			ChuanQiLog.error("***ERROR: jewel add attribute***",e);
		}
		//皇城争霸赛/云宫之战
		try {
			Map<String,Long> zbsMapAttr =hcZhengBaSaiExportService.getGuildXunZhangAttr(roleId);
			if(zbsMapAttr!=null){
				//buff
				role.getFightAttribute().initBaseAttribute(BaseAttributeType.GUILD_XUANZHANG_HCZBS,zbsMapAttr);
//				role.setHcZbsWinnerGuildLeader(true);
			}
			if(hcZhengBaSaiExportService.showCloth(roleId)){
				role.getBusinessData().setHcZbsWinnerGuildLeader(true);//外显
			}
		} catch (Exception e) {
			ChuanQiLog.error("***ERROR: hcZhengBaSai add attribute***",e);
		}
		//跨服云宫之巅
        if(kuafuYunGongExportService.kuafuYunGongIsShowCloth(roleId)){
            role.getBusinessData().setKfYunGongWinnerGuildLeader(true);
            BusMsgSender.send2One(roleId, ClientCmdType.KF_YUNGONG_CLOTHES_SHOW, true);
        }
		//时装属性
		try {
			Map<String,Long> shizhuangAtt = new HashMap<>();
			Map<String,Long> shizhuangLevelAtt = new HashMap<>();
			Map<String,Long> shizhuangJinjieAtt = new HashMap<>();
			Integer showId = roleShiZhuangExportService.getAttribute(roleId, shizhuangAtt, shizhuangLevelAtt,shizhuangJinjieAtt);
			role.setShiZhuang(showId);
			if(shizhuangAtt.size() > 0){
				role.getFightAttribute().initBaseAttribute(BaseAttributeType.SHIZHUANG_ACTIVE,shizhuangAtt);
			}
			if(shizhuangLevelAtt.size() > 0){
				role.getFightAttribute().initBaseAttribute(BaseAttributeType.SHIZHUANG_SHENGJI,shizhuangLevelAtt);
			}
			if(shizhuangJinjieAtt.size() > 0){
				role.getFightAttribute().initBaseAttribute(BaseAttributeType.SHIZHUANG_JINJIE,shizhuangJinjieAtt);
			}
		} catch (Exception e) {
			ChuanQiLog.error("***ERROR: jewel add attribute***");
		}
		//婚姻
		try{
			Map<String,Long> xinwuAtt = new HashMap<>();
			Map<String,Long> lfAtt = new HashMap<>();
			marryExportService.putJieHunAtt(roleId, xinwuAtt, lfAtt);
			if(xinwuAtt.size() > 0){
				role.getFightAttribute().initBaseAttribute(BaseAttributeType.XINWU,xinwuAtt);
			}
			if(lfAtt.size() > 0){
				role.getFightAttribute().initBaseAttribute(BaseAttributeType.LONGFENG,lfAtt);
			}
			Object[] marryInfo = marryExportService.getMarryInfo(roleId);
			if(marryInfo != null){
				String peiou = (String)marryInfo[2];
				Integer xinwu = (Integer)marryInfo[7];
				role.setPeiou(peiou);
				role.setXinwu(xinwu);
			}
		}catch (Exception e) {
			ChuanQiLog.error("***ERROR: marry add attribute***",e);
		}
		//附属技能
		try{
			Map<BaseAttributeType,Map<String,Long>> attMap = fushuSkillExportService.getAllSkillAttribute(roleId);
			if(attMap != null && attMap.size() > 0){
				for (Entry<BaseAttributeType, Map<String, Long>> entry : attMap.entrySet()) {
					role.getFightAttribute().initBaseAttribute(entry.getKey(),entry.getValue());
				}
			}
		}catch (Exception e) {
			ChuanQiLog.error("***ERROR: fushuSkill add attribute***",e);
		}
		//转生
		try{
			Map<String,Long> zsAttribute = zhuanshengExportService.getZhuangShengAttribute(roleId);
			if(zsAttribute != null && zsAttribute.size() > 0){
				role.getFightAttribute().initBaseAttribute(BaseAttributeType.ZHUAN_SHENG,zsAttribute);
			}
			Integer zsLevel = zhuanshengExportService.getZhuanshengLevel(roleId);
			if(zsLevel != null){
				role.getBusinessData().setZhuanShengLevel(zsLevel);
			}
		}catch (Exception e) {
			ChuanQiLog.error("***ERROR: zhuansheng add attribute***",e);
		}


		//计算角色最终属性
		role.getFightAttribute().refresh();
		//填充当前血量
		roleFightAttribute.setCurHp(roleStage.getHp());
		
		//同步总战力
		Long totalFighteValue = role.getFightAttribute().getZhanLi();
		roleBusinessInfoExportService.roleSaveFighter(roleId, totalFighteValue);
		
		if(role.getZuoQi() != null && role.getZuoQi().isGetOn()){//同步坐骑状态
			role.getStateManager().add(new ZuoQiState(role));
			if(roleStage.isLogin()){
				BusMsgSender.send2One(roleId, ClientCmdType.ZUOQI_UP, new Object[]{roleId,role.getZuoQi().getShowId()});
			}
		}
		role.setCdManager(roleStage.getCdManager());
		
		roleStage.setLogin(false);
		return role;
	}

	private static void equipAttrs(Long roleId, Role role,ContainerType containerType,BaseAttributeType  baseAttributeType,BaseAttributeType  randomAttributeType) {
		Object[] zqEquips = equipExportService.getEquips(roleId, containerType);
		if(zqEquips != null && zqEquips.length >0){
			Map<String, Long> equipAttrs = new HashMap<>();
			List<Equip> equipsList = new ArrayList<>();
			for(Object tmp : zqEquips){
				Equip equip = Equip.convert2Equip( (Object[])tmp );
				equipsList.add(equip);
				Map<String,Long> tempAttrs = equipExportService.getEquipAllAttribute(equip.getTpVal(),equip.getGoodsId(), equip.getQianghuaLevel(),equip.getZhuShenLevel(),containerType);
				if(tempAttrs != null){
					ObjectUtil.longMapAdd(equipAttrs, tempAttrs);
				}
			}
			if(equipAttrs.size() >0){
				role.getFightAttribute().initBaseAttribute(baseAttributeType,equipAttrs);//(BaseAttributeType.ZUOQI_EQUIP_BASE, equipAttrs);
			}
			//添加随机属性信息
			Map<String, Long> equipRandomAttrs=equipExportService.getEquipRandomAttrs(equipsList);
			if(equipRandomAttrs !=null && equipRandomAttrs.size() >0){
				role.getFightAttribute().initBaseAttribute(randomAttributeType,equipRandomAttrs);//(BaseAttributeType.ZUOQI_EQUIP_RANDOM_ATTR,equipRandomAttrs);
			}
		}
	}
}
