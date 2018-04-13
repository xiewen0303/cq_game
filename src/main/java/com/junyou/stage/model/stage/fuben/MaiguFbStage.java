package com.junyou.stage.model.stage.fuben;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.junyou.bus.maigu.configure.constants.MaiguConstant;
import com.junyou.bus.maigu.configure.export.ZuDuiShouHhuGuaiWuConfigExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.gameconfig.monster.configure.export.MonsterExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.MaiguPublicConfig;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.stage.model.element.monster.MonsterFactory;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.aoi.AoiStage;
import com.junyou.stage.schedule.StageScheduleExecutor;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.gen.id.IdFactory;

/**
 * 多人副本类
 * 
 * @author chenjianye 2015年04月28日
 */
public class MaiguFbStage extends AoiStage {

	private StageScheduleExecutor scheduleExecutor;
	private List<IRole> challengers;
	private List<IMonster> monsters;
	private Map<String, Integer> wantedMap;
	private long startTime;

	private int expireDelay;
	private short exitCmd;
	private int fubenId;
	private int boshu;
	private MaiguPublicConfig publicConfig;
	
	private IMonster npc;

	private ZuDuiShouHhuGuaiWuConfigExportService zuDuiShouHhuGuaiWuConfigExportService;
	private MonsterExportService monsterExportService;

	/**
	 * 多人副本基础抽象类
	 * 
	 * @param id
	 * @param mapId
	 * @param aoiManager
	 * @param pathInfoConfig
	 * @param stageType
	 *            场景类型
	 * @param isKillAllMonster
	 *            是否要全部击杀完场景怪物
	 * @param needKillMap
	 *            需要击杀的怪物Map列表 key:怪物配置id,value:怪物数量
	 */
	public MaiguFbStage(
			String id,
			Integer mapId,
			AOIManager aoiManager,
			PathInfoConfig pathInfoConfig,
			StageType stageType,
			int expireDelay,
			short exitCmd,
			int fubenId,
			GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService,
			ZuDuiShouHhuGuaiWuConfigExportService zuDuiShouHhuGuaiWuConfigExportService,
			MonsterExportService monsterExportService) {
		super(id, mapId, 1, aoiManager, pathInfoConfig, stageType);

		this.scheduleExecutor = new StageScheduleExecutor(getId());
		// 副本创建时间
		this.startTime = GameSystemTime.getSystemMillTime();

		this.challengers = new ArrayList<>();

		this.wantedMap = new HashMap<>(
				zuDuiShouHhuGuaiWuConfigExportService.getAllMonsters());

		this.expireDelay = expireDelay * 1000;
		this.exitCmd = exitCmd;

		this.fubenId = fubenId;

		this.zuDuiShouHhuGuaiWuConfigExportService = zuDuiShouHhuGuaiWuConfigExportService;
		this.monsterExportService = monsterExportService;
		publicConfig = gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_MAIGU);
	}

	/**
	 * 获取多人副本的场景定时器
	 * 
	 * @return
	 */
	protected StageScheduleExecutor getScheduleExecutor() {
		return scheduleExecutor;
	}

	/**
	 * 开启副本的过期定时
	 */
	public void startScheduleExpireCheck(Long userRoleId) {
		BusTokenRunable runable = new BusTokenRunable(userRoleId, getExitCmd(),
				getId());
		getScheduleExecutor().schedule(userRoleId.toString(),
				GameConstants.COMPONENT_MAIGU_FUBEN_FORCED_LEAVE, runable,
				getExpireDelay(), TimeUnit.MILLISECONDS);
	}

	public int getExpireDelay() {
		return expireDelay;
	}

	public short getExitCmd() {
		return exitCmd;
	}

	/**
	 * 取消副本的过期定时
	 */
	public void cancleScheduleExpire(Long userRoleId) {
		getScheduleExecutor().cancelSchedule(userRoleId.toString(),
				GameConstants.COMPONENT_MAIGU_FUBEN_FORCED_LEAVE);
	}

	/**
	 * 获取副本开始时间
	 * 
	 * @return
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * 挑战者进入场景
	 */
	public void enter(IStageElement element, int x, int y) {
		super.enter(element, x, y);
		if (element == null) {
			return;
		}

		if (ElementType.isRole(element.getElementType())) {
			IRole role = (IRole) element;
			this.challengers.add(role);
			role.getFightAttribute().resetHp();
			startScheduleExpireCheck(element.getId());
			KuafuMsgSender.send2KuafuSource(element.getId(),
					ClientCmdType.MAIGU_BO_SHU, boshu);
		}
	}

	public void addMonster(IMonster monster) {
		if (null == monsters) {
			this.monsters = new ArrayList<IMonster>();
		}
		this.monsters.add(monster);
	}

	/**
	 * 挑战者离开场景
	 */
	public void leave(IStageElement element) {

		super.leave(element);
		if (element == null) {
			return;
		}

		if (ElementType.ROLE.equals(element.getElementType())) {
			this.challengers.remove((IRole) element);
			cancleScheduleExpire(element.getId());
			// noticeClientExit(element.getId());
		} else if (ElementType.isMonster(element.getElementType())) {
			this.monsters.remove(element);
			if (monsters == null || monsters.size() == 0) {
				if (boshu < MaiguConstant.MAX_BO) {
					// 刷新下一波怪
					boshu = boshu + 1;
					List<Map<String, Integer>> monsters = zuDuiShouHhuGuaiWuConfigExportService
							.getConfig(boshu).getMonsters();
					int[][] guiwuZuobiao = publicConfig.getGwzuobiao();
					for (int i = 0; i < MaiguConstant.POINT_NUM; i++) {
						int[] zuobiao = guiwuZuobiao[i];
						for (Map<String, Integer> e : monsters) {
							for (String monsterId : e.keySet()) {
								MonsterConfig monsterConfig = monsterExportService
										.load(monsterId);
								for (int m = 0; m < e.get(monsterId); m++) {
									long id = IdFactory.getInstance()
											.generateNonPersistentId();
									IMonster monster = MonsterFactory
											.createFubenMonster(id,
													monsterConfig);
									this.enter(monster, zuobiao[0], zuobiao[1]);
									monster.getHatredManager().addActiveHatred(npc,1);
									this.addMonster(monster);
								}
							}
						}
					}
					Object[] roleIds = super.getAllRoleIds();
					for (Object e : roleIds) {
						KuafuMsgSender.send2KuafuSource((Long) e,
								ClientCmdType.MAIGU_BO_SHU, boshu);
					}
				}
			}
		}
	}

	/**
	 * 清场副本中的怪物
	 */
	public void clearFubenMonster() {
		if (monsters != null && monsters.size() > 0) {
			List<IMonster> clearMonster = new ArrayList<IMonster>();

			clearMonster.addAll(monsters);
			for (IMonster monster : clearMonster) {
				if (monster.getStateManager().isDead()) {
					continue;
				}
				// 停止定时
				monster.getScheduler().clear();
				// 从副本地图中移除
				this.leave(monster);
			}
		}
	}

	/**
	 * 击杀名单检测并同步副本通关还需要的条件
	 * 
	 * @return true:是需要检测的怪物ID
	 */
	public boolean wantedListCheck(String killedMonsterId) {
		if (null != wantedMap) {
			boolean flag = wantedMap.containsKey(killedMonsterId);
			if (flag) {

				Integer count = wantedMap.get(killedMonsterId);
				if (count > 1) {
					count--;
					wantedMap.put(killedMonsterId, count);
				} else {
					wantedMap.remove(killedMonsterId);
				}
			}

			return flag;
		}

		return false;
	}

	/**
	 * 获取击杀怪物的总数
	 * 
	 * @return
	 */
	public int getWantedCounts() {
		int count = 0;
		if (null != wantedMap) {
			for (Integer value : wantedMap.values()) {
				count += value;
			}
		}
		return count;
	}

	/**
	 * 是否已经击杀完毕
	 */
	public boolean isWantedListComplete() {
		return null == wantedMap || wantedMap.size() == 0;
	}

	/**
	 * 通关业务,先标记副本结束,再清除场景内的其它怪物,最后再开启强踢倒计时
	 */
	public void tongGuanHandle() {

		// 清怪
		// clearFubenMonster();
		if (challengers != null) {
			for (IRole role : challengers) {
				// 停止之前的副本过期强踢定时
				cancleScheduleExpire(role.getId());
				// 3.开启通关后的倒计时强踢
				BusTokenRunable runable = new BusTokenRunable(role.getId(),
						getExitCmd(), getId());
				getScheduleExecutor().schedule(role.getId().toString(),
						GameConstants.COMPONENT_MAIGU_FUBEN_FORCED_LEAVE,
						runable, getExpireCheckInterval(),
						TimeUnit.MILLISECONDS);
			}
		}

	}

	public int getExpireCheckInterval() {
		return GameConstants.MAIGU_FUBEN_EXPIRE_CHECK_INTERVAL;
	}

	public List<IRole> getChallengers() {
		return challengers;
	}

	public int getFubenId() {
		return fubenId;
	}

	public void setFubenId(int fubenId) {
		this.fubenId = fubenId;
	}

	@Override
	public boolean isCanRemove() {
		return challengers == null || challengers.size() < 1;
	}

	// public Map<Long, Integer> getHurts() {
	// return hurts;
	// }
	//
	// public void setHurts(Map<Long, Integer> hurts) {
	// this.hurts = hurts;
	// }
	/**
	 * 通知前端退出副本
	 */
	public void noticeClientExit(Long userRoleId) {
		KuafuMsgSender.send2KuafuSource(userRoleId,
				ClientCmdType.MAIGU_LEAVE_FUBEN, AppErrorCode.OK);
	}

	public int getBoshu() {
		return boshu;
	}

	public void setBoshu(int boshu) {
		this.boshu = boshu;
	}

	public boolean isNpc(String monsterId) {
		return publicConfig.getMgnpc().equals(monsterId);
	}

	public void npcDeadHandle() {
		clearFubenMonster();
		StageMsgSender.send2StageInner(GameConstants.DEFAULT_ROLE_ID, getId(), InnerCmdType.MAIGU_FUBEN_FINISH, MaiguConstant.RESULT_FAIL);
	}

	@Override
	public boolean isAddPk() {
		return false;
	}

	@Override
	public boolean isCanPk() {
		return false;
	}

	@Override
	public boolean isCanHasTangbao() {
		return true;
	}

	@Override
	public boolean isCanHasChongwu() {
		return true;
	}

	@Override
	public boolean isCanUseShenQi() {
		return true;
	}

	public IMonster getNpc() {
		return npc;
	}

	public void setNpc(IMonster npc) {
		this.npc = npc;
	}

}
