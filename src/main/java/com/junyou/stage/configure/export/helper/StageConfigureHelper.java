package com.junyou.stage.configure.export.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.equip.configure.export.TiPinBiaoConfig;
import com.junyou.bus.equip.configure.export.TiPinBiaoConfigExportService;
import com.junyou.bus.hczbs.export.HcZhengBaSaiExportService;
import com.junyou.bus.lianyuboss.export.LianyuBossExortService;
import com.junyou.bus.shenmo.configure.export.ShenMoScoreConfigExportService;
import com.junyou.bus.territory.export.TerritoryExportService;
import com.junyou.bus.wuqi.configure.export.XinShengJianJiChuConfigExportService;
import com.junyou.bus.zuoqi.configure.export.YuJianJiChuConfigExportService;
import com.junyou.gameconfig.dazuo.configure.export.DaZuoConfigExportService;
import com.junyou.gameconfig.export.PublicCdExportService;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapConfigExportService;
import com.junyou.gameconfig.monster.configure.export.MonsterExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.public_.guild.export.GuildExportService;
import com.junyou.stage.campwar.confiure.export.ZhenYinZhanJiNengBiaoConfigExportService;
import com.junyou.stage.collect.export.BoxExportService;
import com.junyou.stage.configure.export.impl.BuffConfigExportService;
import com.junyou.stage.configure.export.impl.CampRelationExportService;
import com.junyou.stage.configure.export.impl.SkillConfigExportService;
import com.junyou.stage.configure.export.impl.ZhuanDouLiCanShuConfigExportService;
import com.junyou.stage.configure.export.impl.ZiYuanConfigExportService;
import com.kernel.spring.container.DataContainer;

@Component
public class StageConfigureHelper {

	private static CampRelationExportService campRelationExportService;
	private static MonsterExportService monsterExportService;	
	private static SkillConfigExportService skillConfigExportService;
	private static PublicCdExportService publicCdExportService;
	private static GoodsConfigExportService goodsConfigExportService;
	private static ZiYuanConfigExportService ziYuanConfigExportService;
	private static ZhuanDouLiCanShuConfigExportService zhuanDouLiCanShuConfigExportService;
	private static BuffConfigExportService buffConfigExportService;
	private static GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	private static DiTuConfigExportService diTuConfigExportService;
	private static MapConfigExportService mapExportService;
	private static DaZuoConfigExportService daZuoConfigExportService; 
	private static YuJianJiChuConfigExportService yuJianJiChuConfigExportService;
	private static BoxExportService boxExportService;
	private static DataContainer dataContainer;
	private static ZhenYinZhanJiNengBiaoConfigExportService zhenYinZhanJiNengBiaoConfigExportService;
	private static TerritoryExportService territoryExportService;	 
	private static HcZhengBaSaiExportService hcZhengBaSaiExportService;	 
	private static GuildExportService guildExportService;
	private static ShenMoScoreConfigExportService shenMoScoreConfigExportService;
	private static LianyuBossExortService LianyuBossExortService;
	private static XinShengJianJiChuConfigExportService xinShengJianJiChuConfigExportService;
	private static TiPinBiaoConfigExportService tiPinBiaoConfigExportService;
	
	
	public static XinShengJianJiChuConfigExportService getXinShengJianJiChuConfigExportService() {
		return xinShengJianJiChuConfigExportService;
	}
	
	@Autowired
	public void setXinShengJianJiChuConfigExportService(
			XinShengJianJiChuConfigExportService xinShengJianJiChuConfigExportService) {
		StageConfigureHelper.xinShengJianJiChuConfigExportService = xinShengJianJiChuConfigExportService;
	}
	
	public static LianyuBossExortService getLianyuBossExortService() {
		return LianyuBossExortService;
	}
	@Autowired
	public  void setLianyuBossExortService(LianyuBossExortService lianyuBossExortService) {
		LianyuBossExortService = lianyuBossExortService;
	}
	
	public static ShenMoScoreConfigExportService getShenMoScoreConfigExportService() {
		return shenMoScoreConfigExportService;
	}
	@Autowired
	public void setShenMoScoreConfigExportService(ShenMoScoreConfigExportService shenMoScoreConfigExportService) {
		StageConfigureHelper.shenMoScoreConfigExportService = shenMoScoreConfigExportService;
	}
	 
	public static YuJianJiChuConfigExportService getYuJianJiChuConfigExportService() {
		return yuJianJiChuConfigExportService;
	}
	@Autowired
	public void setYuJianJiChuConfigExportService(YuJianJiChuConfigExportService yuJianJiChuConfigExportService) {
		StageConfigureHelper.yuJianJiChuConfigExportService = yuJianJiChuConfigExportService;
	}
	public static DaZuoConfigExportService getDaZuoConfigExportService() {
		return daZuoConfigExportService;
	}
	@Autowired
	public void setDaZuoConfigExportService(
			DaZuoConfigExportService daZuoConfigExportService) {
		StageConfigureHelper.daZuoConfigExportService = daZuoConfigExportService;
	}
	@Autowired
	public void setMapExportService(MapConfigExportService mapExportService) {
		StageConfigureHelper.mapExportService = mapExportService;
	}
	@Autowired
	public void setDiTuConfigExportService(DiTuConfigExportService diTuConfigExportService) {
		StageConfigureHelper.diTuConfigExportService = diTuConfigExportService;
	}

	@Autowired
	public void setGongGongShuJuBiaoConfigExportService(GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService) {
		StageConfigureHelper.gongGongShuJuBiaoConfigExportService = gongGongShuJuBiaoConfigExportService;
	}

	@Autowired
	public void setBuffConfigExportService(BuffConfigExportService buffConfigExportService) {
		StageConfigureHelper.buffConfigExportService = buffConfigExportService;
	}

	@Autowired
	public void setZhuanDouLiCanShuConfigExportService(ZhuanDouLiCanShuConfigExportService zhuanDouLiCanShuConfigExportService) {
		StageConfigureHelper.zhuanDouLiCanShuConfigExportService = zhuanDouLiCanShuConfigExportService;
	}

	@Autowired
	public void setZiYuanConfigExportService(ZiYuanConfigExportService ziYuanConfigExportService) {
		StageConfigureHelper.ziYuanConfigExportService = ziYuanConfigExportService;
	}
	
	@Autowired
	public void setGoodsConfigExportService(
			GoodsConfigExportService goodsConfigExportService) {
		StageConfigureHelper.goodsConfigExportService = goodsConfigExportService;
	}

	@Autowired
	public void setPublicCdExportService(
			PublicCdExportService publicCdExportService) {
		StageConfigureHelper.publicCdExportService = publicCdExportService;
	}

	@Autowired
	public void setSkillConfigExportService(
			SkillConfigExportService skillConfigExportService) {
		StageConfigureHelper.skillConfigExportService = skillConfigExportService;
	}

	@Autowired
	public void setCampRelationExportService(
			CampRelationExportService campRelationExportService) {
		StageConfigureHelper.campRelationExportService = campRelationExportService;
	}
	
	@Autowired
	public void setMonsterExportService(
			MonsterExportService monsterExportService) {
		StageConfigureHelper.monsterExportService = monsterExportService;
	}
	
	@Autowired
	public void setBoxExportService(BoxExportService boxExportService) {
		StageConfigureHelper.boxExportService = boxExportService;
	}
	@Autowired
	public void setDataContainer(DataContainer dataContainer) {
		StageConfigureHelper.dataContainer = dataContainer;
	}
	@Autowired
	public void setZhenYinZhanJiNengBiaoConfigExportService(
			ZhenYinZhanJiNengBiaoConfigExportService zhenYinZhanJiNengBiaoConfigExportService) {
		StageConfigureHelper.zhenYinZhanJiNengBiaoConfigExportService = zhenYinZhanJiNengBiaoConfigExportService;
	}
	
	@Autowired
	public void setTerritoryExportService(
			TerritoryExportService territoryExportService) {
		StageConfigureHelper.territoryExportService = territoryExportService;
	}
	@Autowired
	public  void setHcZhengBaSaiExportService(HcZhengBaSaiExportService hcZhengBaSaiExportService) {
		StageConfigureHelper.hcZhengBaSaiExportService = hcZhengBaSaiExportService;
	}
	 
	@Autowired
	public void setGuildExportService(
			GuildExportService guildExportService) {
		StageConfigureHelper.guildExportService = guildExportService;
	}
	
	
	public static ZiYuanConfigExportService getZiYuanConfigExportService() {
		return ziYuanConfigExportService;
	}

	public static MonsterExportService getMonsterExportService() {
		return monsterExportService;
	}

	public static CampRelationExportService getCampRelationExportService() {
		return campRelationExportService;
	}

	public static SkillConfigExportService getSkillConfigExportService() {
		return skillConfigExportService;
	}
	
	public static PublicCdExportService getPublicCdExportService() {
		return publicCdExportService;
	}
	
	public static GoodsConfigExportService getGoodsConfigExportService() {
		return goodsConfigExportService;
	}
	
	public static ZhuanDouLiCanShuConfigExportService getZhuanDouLiCanShuConfigExportService() {
		return zhuanDouLiCanShuConfigExportService;
	}
	
	public static BuffConfigExportService getBuffConfigExportService() {
		return buffConfigExportService;
	}
	
	public static GongGongShuJuBiaoConfigExportService getGongGongShuJuBiaoConfigExportService() {
		return gongGongShuJuBiaoConfigExportService;
	}
	
	
	public static DiTuConfigExportService getDiTuConfigExportService() {
		return diTuConfigExportService;
	}
	public static MapConfigExportService getMapExportService() {
		return mapExportService;
	}
	
	public static BoxExportService getBoxExportService(){
		return boxExportService; 
	}
	public static DataContainer getDataContainer() {
		return dataContainer;
	}
	public static ZhenYinZhanJiNengBiaoConfigExportService getZhenYinZhanJiNengBiaoConfigExportService() {
		return zhenYinZhanJiNengBiaoConfigExportService;
	}
	public static TerritoryExportService getTerritoryExportService() {
		return territoryExportService;
	}
	public static HcZhengBaSaiExportService getHcZhengBaSaiExportService() {
		return hcZhengBaSaiExportService;
	}
	public static GuildExportService getGuildExportService() {
		return guildExportService;
	}
	
	public static TiPinBiaoConfigExportService getTiPinBiaoConfigExportService(){
		return tiPinBiaoConfigExportService;
	}
	
	@Autowired
	public void setTiPinBiaoConfigExportService(
			TiPinBiaoConfigExportService tiPinBiaoConfigExportService) {
		StageConfigureHelper.tiPinBiaoConfigExportService = tiPinBiaoConfigExportService;
	}
	
}
