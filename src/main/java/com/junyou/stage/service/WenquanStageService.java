package com.junyou.stage.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.StageUtil;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.wenquan.configure.export.WenQuanPaiMingJiangLiConfig;
import com.junyou.bus.wenquan.configure.export.WenQuanPaiMingJiangLiConfigExportService;
import com.junyou.bus.wenquan.service.export.WenquanExportService;
import com.junyou.bus.wenquan.vo.WenquanRankVo;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfig;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfigExportService;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.gameconfig.map.configure.export.MapConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.WenquanPublicConfig;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.PublicFubenStage;
import com.junyou.stage.model.stage.fuben.PublicFubenStageFactory;
import com.junyou.stage.model.stage.wenquan.WenquanManager;
import com.junyou.stage.model.stage.wenquan.WenquanStage;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.active.ActiveUtil;
import com.kernel.spring.container.DataContainer;

@Service
public class WenquanStageService {
	@Autowired
	private DingShiActiveConfigExportService dingShiActiveConfigExportService;
	@Autowired
	private PublicFubenStageFactory publicFubenStageFactory;
	@Autowired
	private MapConfigExportService mapConfigExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private BusScheduleExportService scheduleExportService;
	@Autowired
	private WenQuanPaiMingJiangLiConfigExportService wenQuanPaiMingJiangLiConfigExportService;
	@Autowired
	private WenquanExportService wenquanExportService;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private RoleBehaviourService roleBehaviourService;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private StageControllExportService stageControllExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;

	public Object[] enterWenquan(Long userRoleId) {
		// 判断是否有活动
		Integer hdId = dataContainer.getData(
				GameConstants.COMPONENT_WENQUAN_ACTIVE_ID,
				GameConstants.COMPONENT_WENQUAN_ACTIVE_ID);
		if (hdId == null) {
			return AppErrorCode.WENQUAN_ACTIVE_NOT_START;
		}

		// 判断是否有配置
		DingShiActiveConfig config = dingShiActiveConfigExportService
				.getConfig(hdId);
		if (config == null || config.getMapId() == 0) {
			return AppErrorCode.CONFIG_ERROR;
		}

		// 判断等级是否满足
		if (config.getMinLevel() > roleExportService.getUserRole(userRoleId)
				.getLevel()) {
			return AppErrorCode.ROLE_LEVEL_ERROR;
		}

		// 判断是否在副本中
		if (stageControllExportService.inFuben(userRoleId)) {
			return AppErrorCode.FUBEN_IS_IN_FUBEN;
		}

		DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(config
				.getMapId());
		if (dituCoinfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		// 发送到场景进入地图
		int birthPoint[] = dituCoinfig.getRandomBirth();
		int x = birthPoint[0];
		int y = birthPoint[1];
		BusMsgSender.send2BusInner(userRoleId,
				InnerCmdType.S_APPLY_CHANGE_STAGE,
				new Object[] { dituCoinfig.getId(), x, y,
						MapType.WENQUAN_MAP_TYPE });
		return null;
	}

	public Object[] exitWenquan(Long userRoleId) {
		// 判断是否有活动
		Integer hdId = dataContainer.getData(
				GameConstants.COMPONENT_WENQUAN_ACTIVE_ID,
				GameConstants.COMPONENT_WENQUAN_ACTIVE_ID);
		if (hdId == null) {
			return AppErrorCode.WENQUAN_ACTIVE_NOT_START;
		}

		// 判断是否有配置
		DingShiActiveConfig config = dingShiActiveConfigExportService
				.getConfig(hdId);
		if (config == null || config.getMapId() == 0) {
			return AppErrorCode.CONFIG_ERROR;
		}

		// 判断是否在副本中
		if (!stageControllExportService.inFuben(userRoleId)) {
			return AppErrorCode.FUBEN_NOT_IN_FUBEN;
		}

		// 通知场景离开战场
		StageMsgSender.send2StageControl(userRoleId,
				InnerCmdType.S_APPLY_LEAVE_STAGE, userRoleId);
		return null;
	}

	public void activeStart(Integer id) {
		DingShiActiveConfig config = dingShiActiveConfigExportService
				.getConfig(id);
		if (config == null) {
			return;
		}

		Integer endLine = Integer.parseInt(config.getData1());
		Integer mapId = config.getMapId();
		dataContainer.putData(GameConstants.COMPONENT_WENQUAN_ACTIVE_ID,
				GameConstants.COMPONENT_WENQUAN_ACTIVE_ID, id);
		wenquanExportService.initRank();
		createWenquanStage(mapId, 1, endLine);

		ActiveUtil.setWenquan(true);
		BusTokenRunable runable = new BusTokenRunable(
				GameConstants.DEFAULT_ROLE_ID, InnerCmdType.WENQUAN_END, null);

		Long delay = config.getCalcEndSecondTime();
		scheduleExportService.schedule(
				GameConstants.DEFAULT_ROLE_ID.toString(),
				GameConstants.COMPONENT_WENQUAN, runable, delay.intValue(),
				TimeUnit.MILLISECONDS);
		ChuanQiLog.info("温泉活动开始了");
	}

	public void createWenquanStage(Integer mapId, Integer startLine,
			Integer endLine) {
		Integer hdId = dataContainer.getData(
				GameConstants.COMPONENT_WENQUAN_ACTIVE_ID,
				GameConstants.COMPONENT_WENQUAN_ACTIVE_ID);
		if (hdId == null) {
			return;
		}
		DingShiActiveConfig config = dingShiActiveConfigExportService
				.getConfig(hdId);
		MapConfig mapConfig = mapConfigExportService.load(mapId);
		for (int i = startLine; i <= endLine; i++) {
			PublicFubenStage stage = publicFubenStageFactory.create(
					StageUtil.getStageId(mapId, i), i, mapConfig);
			if (stage != null) {
				WenquanStage wenquanStage = (WenquanStage) stage;
				wenquanStage.setDingShiActiveConfig(config);
				WenquanManager.getManager().addStage(wenquanStage);
				wenquanStage.start();
			}
		}
	}

	public WenquanPublicConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_WENQUAN);
	}

	public void activeEnd() {
		dataContainer.removeData(GameConstants.COMPONENT_WENQUAN_ACTIVE_ID,
				GameConstants.COMPONENT_WENQUAN_ACTIVE_ID);
		ActiveUtil.setWenquan(false);
		WenquanManager.getManager().stop();
		Map<Integer, WenQuanPaiMingJiangLiConfig> configs = wenQuanPaiMingJiangLiConfigExportService
				.getConfigs();
		List<WenquanRankVo> ranklist = wenquanExportService.getRank();
		if (ranklist != null) {
			for (WenquanRankVo e : ranklist) {
				Long userRoleId = e.getUserRoleId();
				if (wenquanExportService.isTodayActive(userRoleId)) {
					int rank = e.getRank();
					WenQuanPaiMingJiangLiConfig config = configs.get(rank);
					if (config != null) {
						String[] attachmentArray = EmailUtil.getAttachments(config.getJiangitem());
						String title = EmailUtil.getCodeEmail(AppErrorCode.WENQUAN_REWARD_EMAIL_TITLE);
						String content = EmailUtil.getCodeEmail(AppErrorCode.WENQUAN_REWARD_EMAIL);
						emailExportService.sendEmailToOne(userRoleId,title, content,GameConstants.EMAIL_TYPE_SINGLE,attachmentArray[0]);
					}
				}
			}
		}
		List<WenquanStage> stages = WenquanManager.getManager().getStages();
		for (WenquanStage e : stages) {
			Object[] roleIds = e.getAllRoleIds();
			if (roleIds != null) {
				for (Object o : roleIds) {
					Long userRoleId = (Long) o;
					BusMsgSender.send2BusInner(userRoleId,
							InnerCmdType.S_APPLY_LEAVE_STAGE, userRoleId);
				}
			}
		}
		Set<Long> julinSet = WenquanManager.getManager().getJulinSet();
		for(Long e:julinSet){
			wenquanExportService.updateJulinUpdateTime(e);
		}
		WenquanManager.getManager().clear();
		ChuanQiLog.info("温泉活动结束了");
	}

	public void addExpAndZhenqi(String stageId, Long userRoleId) {
		if (!ActiveUtil.isWenquan()) {
			return;
		}
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;// 场景不存在
		}

		if (stage.getStageType() != StageType.WENQUAN) {
			return;// 当前地图不是领地战地图
		}

		IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;// 角色不存在
		}
		roleBehaviourService.dazuoAward(userRoleId, stageId, true);
		role.startWenquanAddExpZhenqiSchedule();
	}

	public void gotoHighArea(Long userRoleId, String stageId) {
		if (!ActiveUtil.isWenquan()) {
			return;
		}
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;
		}

		if (stage.getStageType() != StageType.WENQUAN) {
			return;
		}

		IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;
		}
		WenquanPublicConfig publicConfig = getPublicConfig();
		int[] highAreaBirthPoint = publicConfig.getZuobiao();
		int x = highAreaBirthPoint[0];
		int y = highAreaBirthPoint[1];
		stage.teleportTo(stage.getElement(userRoleId, ElementType.ROLE), x, y);
		StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()),
				ClientCmdType.BEHAVIOR_TELEPORT, new Object[] { userRoleId, x,
						y });
		WenquanManager.getManager().setInHighArea(userRoleId, true);
		StageMsgSender.send2One(userRoleId, ClientCmdType.GOTO_HIGH_AREA,
				AppErrorCode.OK);
	}
}
