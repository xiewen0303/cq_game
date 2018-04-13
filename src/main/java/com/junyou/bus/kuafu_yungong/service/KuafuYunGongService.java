/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.kuafu_yungong.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.hczbs.entity.Zhengbasai;
import com.junyou.bus.hczbs.export.HcZhengBaSaiExportService;
import com.junyou.bus.kuafu_yungong.constants.KuafuYunGongConstants;
import com.junyou.bus.kuafu_yungong.entity.KuafuYunGongDayReward;
import com.junyou.bus.kuafu_yungong.entity.KuafuYunGongResult;
import com.junyou.bus.marry.export.MarryExportService;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoExportService;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoWrapper;
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.KuafuYunGongPublicConfig;
import com.junyou.gameconfig.role.configure.export.ZhuJueDzConfig;
import com.junyou.gameconfig.role.configure.export.ZhuJueDzConfigExportService;
import com.junyou.io.export.SessionManagerExportService;
import com.junyou.kuafu.manager.KuafuManager;
import com.junyou.kuafu.manager.KuafuRoleServerManager;
import com.junyou.kuafu.manager.KuafuServerInfoManager;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.kuafu.share.tunnel.KuafuNetTunnel;
import com.junyou.kuafu.share.util.KuafuConfigUtil;
import com.junyou.kuafu.share.util.KuafuServerInfo;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.public_.guild.entity.Guild;
import com.junyou.public_.guild.entity.GuildMember;
import com.junyou.public_.guild.export.GuildExportService;
import com.junyou.public_.guild.manager.GuildManager;
import com.junyou.public_.guild.util.GuildUtil;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.element.role.RoleFactory;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.state.NoBeiAttack;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.KuafuConfigPropUtil;
import com.junyou.utils.active.ActiveUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.cache.redis.Redis;
import com.kernel.cache.redis.RedisKey;
import com.kernel.spring.container.DataContainer;

/**
 * @Description 跨服云宫之巅源服业务处理
 * @Author Yang Gao
 * @Since 2016-9-20
 * @Version 1.1.0
 */
@Service
public class KuafuYunGongService {
    @Autowired
    private DataContainer dataContainer;
    @Autowired
    private RoleExportService roleExportService;
    @Autowired
    private BusScheduleExportService scheduleExportService;
    @Autowired
    private SessionManagerExportService sessionManagerExportService;
    @Autowired
    private ZhuJueDzConfigExportService zhuJueDzConfigExportService;
    @Autowired
    private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
    @Autowired
    private HcZhengBaSaiExportService hcZhengBaSaiExportService;
    @Autowired
    private RoleBagExportService roleBagExportService;
    @Autowired
    private GuildExportService guildExportService;
    @Autowired
    private RoleBusinessInfoExportService roleBusinessInfoExportService;
    @Autowired
    private StageControllExportService stageControllExportService;
    @Autowired
    private DiTuConfigExportService diTuConfigExportService;
    @Autowired
    private MarryExportService marryExportService;
    @Autowired
    private EmailExportService emailExportService;

    /**
     * 获取redis对象
     * 
     * @return
     */
    private Redis getRedis() {
        Redis redis = GameServerContext.getRedis();
        if (null == redis) {
            ChuanQiLog.error("redis is not open!");
            return null;
        }
        return redis;

    }

    /**
     * 获取本服服务器编号
     */
    private String getLocalServerId() {
        return ChuanQiConfigUtil.getServerId();
    }

    /**
     * 本服全服通告(不带参数替换)
     * 
     * @param code
     * @param parameter
     */
    private void yunGongNotice(int code) {
        BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT6, new Object[] { code });
    }

    /**
     * 本服全服通告(带参数替换)
     * 
     * @param code
     * @param parameter
     */
    private void yunGongNotice2(int code, Object parameter) {
        BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT6, new Object[] { code, parameter });
    }

    /**
     * 获取跨服云宫之巅公共配置数据
     * 
     * @return
     */
    private KuafuYunGongPublicConfig getKfYunGongPublicConfig() {
        return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_KUAFU_YUNGONG);
    }
    
    /**
     * 公会操作开启处理
     */
    private void guildOperationOpenHandler(){
        ActiveUtil.setKfYunGong(true);
        ActiveUtil.setCanChangeGuild(false);
    }
    
    /**
     * 公会操作关闭处理
     */
    private void guildOperationCloseHandler(){
        ActiveUtil.setKfYunGong(false);
        ActiveUtil.setCanChangeGuild(true);
    }

    /**
     * 活动触发阶段,不停服的情况下,每天扫描一次;停服后重新扫描
     */
    public void initJob() {
        // 跨服不执行定时器
        if (KuafuConfigPropUtil.isKuafuServer()) {
            return;
        }
        String serverId = getLocalServerId();
        KuafuYunGongPublicConfig config = getKfYunGongPublicConfig();
        if (null == config) {
            ChuanQiLog.error("********[YunGongZhiDian]*********source serverId={}, activity config is null!!!********[YunGongZhiDian]*********", serverId);
            return;
        }
        // 活动开启所在周
        int curWeek = DatetimeUtil.getCurrentWeek() - 1;// 周日-周六=[1-7]-1 =[0-6]
        if (curWeek != config.getWeek()) {
            return;
        }
        Zhengbasai zhengbasai = hcZhengBaSaiExportService.loadZhengbasai();
        if (null == zhengbasai) {
            ChuanQiLog.info("********[YunGongZhiDian]*********source serverId={},not yungongzhizhan winner guild, not join yungongzhidian!!!********[YunGongZhiDian]*********", serverId);
            return;
        }
        long curTime = GameSystemTime.getSystemMillTime();
        long entTime = DatetimeUtil.getTheDayTheTime(config.getEndtime()[0], config.getEndtime()[1], curTime);
        if (curTime >= entTime) {// 活动时间已过,活动结束
            ChuanQiLog.info("********[YunGongZhiDian]*********source serverId={}, avtivity time over ,time={}********[YunGongZhiDian]*********", serverId, new Date(curTime));
            return;
        }
        long readyTime = DatetimeUtil.getTheDayTheTime(config.getReadytime()[0], config.getReadytime()[1], curTime);
        long delay = readyTime - curTime;
        if (delay > 0) {// 未到准备时间,启动进入准备阶段延迟定时器
            scheduleExportService.cancelSchedule(GameConstants.DEFAULT_ROLE_ID.toString(), GameConstants.KUAFU_YUNGONG_READY_PRODUCE);
            scheduleExportService.schedule(GameConstants.DEFAULT_ROLE_ID.toString(), GameConstants.KUAFU_YUNGONG_READY_PRODUCE, new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.KUAFU_YUNGONG_READY, null), (int) delay, TimeUnit.MILLISECONDS);
            return;
        }
        if (!activityGrouping()) {// 系统启动分组
            return;
        }
        long startTime = DatetimeUtil.getTheDayTheTime(config.getStarttime()[0], config.getStarttime()[1], curTime);
        delay = startTime - curTime;
        if (delay > 0) {// 未到开始时间,启动进入开始阶段延迟定时器
            scheduleExportService.cancelSchedule(GameConstants.DEFAULT_ROLE_ID.toString(), GameConstants.KUAFU_YUNGONG_START_PRODUCE);
            scheduleExportService.schedule(GameConstants.DEFAULT_ROLE_ID.toString(), GameConstants.KUAFU_YUNGONG_START_PRODUCE, new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.KUAFU_YUNGONG_START, null), (int) delay, TimeUnit.MILLISECONDS);
            return;
        }
        // 活动已开始,但未结束,直接进入开始阶段
        activityStart();
    }

    /**
     * 活动准备阶段</br> 1:在特定服指定跨服服务器分组工作</br> 2:全服推送活动公告</br> 3:进入活动开始</br>
     * 
     * @param isOpen 是否直接开始活动(true=直接启动;false=定时器延迟启动)
     */
    public void activityReadyJob() {
        // 分组
        if (!activityGrouping()) {
            return;
        }
        // 推送全服活动准备公告
        KuafuYunGongPublicConfig config = getKfYunGongPublicConfig();
        yunGongNotice(config.getGonggao1());
        // 进入活动开始定时器
        long curTime = GameSystemTime.getSystemMillTime();
        long startTime = DatetimeUtil.getTheDayTheTime(config.getStarttime()[0], config.getStarttime()[1], curTime);
        long delay = startTime - curTime;
        if(delay > 0){
            scheduleExportService.cancelSchedule(GameConstants.DEFAULT_ROLE_ID.toString(), GameConstants.KUAFU_YUNGONG_START_PRODUCE);
            scheduleExportService.schedule(GameConstants.DEFAULT_ROLE_ID.toString(), GameConstants.KUAFU_YUNGONG_START_PRODUCE, new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.KUAFU_YUNGONG_START, null), (int) delay, TimeUnit.MILLISECONDS);
            ChuanQiLog.info("********[YunGongZhiDian]*********source serverId={},activity ready step ********[YunGongZhiDian]*********", getLocalServerId());
        }
    }

    /**
     * 活动分组:新一轮活动开始</br> 1:只在特定服务器执行此方法</br> (</br> 特定的服务器: 1.1 多个小平台使用同一服务器,混服
     * )</br> 2:为所有参赛服务器(源服云宫之战出现冠军的服务器有资格参赛)分配跨服服务器
     */
    private boolean activityGrouping() {
        String pServerId = ChuanQiConfigUtil.getPlatformServerId();
        if (PlatformConstants.isQQ()) {
            if (Integer.parseInt(pServerId) != 1 && Integer.parseInt(pServerId) != 118) {
                return true;
            }
        } else {
            if (Integer.parseInt(pServerId) != 1) {
                return true;
            }
        }
        Redis redis = getRedis();
        if (null == redis) {
            return false;
        }
        // 删除整个平台对应的redis库中所有分组信息
        if (redis.exists(RedisKey.KUAFU_YUNGONG_SERVER_MATCH_KEY)) {
            redis.del(RedisKey.KUAFU_YUNGONG_SERVER_MATCH_KEY);
        }
        // 删除整个平台对应的redis库中所有活动结果数据
        Set<String> resultKeys = redis.keys(RedisKey.KUAFU_YUNGONG_RESULT_KEY + "*");
        if (!ObjectUtil.isEmpty(resultKeys)) {
            for (String key : resultKeys) {
                redis.del(key);
            }
        }
        // 删除整个平台对应的redis库中所有活动奖励结果数据
        Set<String> dayRewardServerKeys = redis.keys(RedisKey.KUAFU_YUNGONG_DAY_REWARD_KEY + "*");
        if (!ObjectUtil.isEmpty(dayRewardServerKeys)) {
            for (String key : dayRewardServerKeys) {
                redis.del(key);
            }
        }
        // 实际跨服服务器列表
        Set<String> kfServerIdSets = redis.zrange(RedisKey.KUAFU_SERVER_LIST_KEY, 0, -1);
        if (ObjectUtil.isEmpty(kfServerIdSets)) {
            ChuanQiLog.error("********[YunGongZhiDian]*********pServerId={},source servverId={},begin grouping,kuafu server list is empty!!! grouping faild********[YunGongZhiDian]*********", pServerId, getLocalServerId());
            return false;
        }
        // set转list,方便操作
        List<String> kfServerIdList = new ArrayList<String>(kfServerIdSets);
        // 实际参赛服务器列表
        List<String> serverList = redis.lrange(RedisKey.KUAFU_YUNGONG_SERVER_KEY, 0, -1);
        if (ObjectUtil.isEmpty(serverList)) {
            ChuanQiLog.error("********[YunGongZhiDian]*********pServerId={},source servverId={},begin grouping,reality server list is empty!!! grouping faild********[YunGongZhiDian]*********", pServerId, getLocalServerId());
            return false;
        }
        int kfSize = kfServerIdList.size();// 跨服服务器的数量
        int serverSize = serverList.size();// 参赛服务器的数量
        // 根据实际参赛服务器数和实际跨服服务器数,得到每组跨服服务器的最小参赛服务器数量
        int groupNumber = new Double(Math.ceil(serverSize * 1D / kfSize)).intValue();
        if (groupNumber <= 0) {
            ChuanQiLog.error("********[YunGongZhiDian]*********pServerId={},source servverId={},begin grouping,grouping faild,kuafu server size={},reality server size ={} ********[YunGongZhiDian]*********", pServerId, getLocalServerId(), kfSize, serverSize);
            return false;
        }
        // 容错处理,当配置的每组跨服的参赛服务器数目少于实际最小数时,使用实际最小数;否则使用配置数
        KuafuYunGongPublicConfig config = getKfYunGongPublicConfig();
        if (null != config && config.getGroupNumber() > 0) {
            groupNumber = config.getGroupNumber() < groupNumber ? groupNumber : config.getGroupNumber();
        }
        Map<String, String> matchMap = new HashMap<String, String>();
        for (String kfServerId : kfServerIdList) {
            if (serverSize <= 0) {
                break;
            }
            Collections.shuffle(serverList);// 对数组随机组合,打乱顺序
            List<String> subServerIdListt = serverList.subList(0, serverSize >= groupNumber ? groupNumber : serverSize);
            for (String serverId : subServerIdListt) {
                matchMap.put(serverId, kfServerId);
            }
            subServerIdListt.clear();
            serverSize = serverList.size();
        }
        redis.hmset(RedisKey.KUAFU_YUNGONG_SERVER_MATCH_KEY, matchMap);
        ChuanQiLog.info("********[YunGongZhiDian]*********pServerId={},source servverId={},grouping success!!!********[YunGongZhiDian]*********", pServerId, getLocalServerId());
        return true;
    }

    /**
     * 获取跨服云宫之巅活动中本服匹配的跨服服务器编号
     * 
     * @return
     */
    private String getKfYunGongMatchServerId() {
        Redis redis = getRedis();
        if (null == redis) {
            return null;
        }
        return redis.hget(RedisKey.KUAFU_YUNGONG_SERVER_MATCH_KEY, getLocalServerId());
    }

    /**
     * 活动开始阶段
     */
    public void activityStart() {
        KuafuYunGongPublicConfig config = getKfYunGongPublicConfig();
        if (null == config) {
            return;
        }
        // 本服匹配的跨服服务器是否存在
        if (ObjectUtil.strIsEmpty(getKfYunGongMatchServerId())) {
            ChuanQiLog.error("********[YunGongZhiDian]*********source serverId={}, not found match kuafu server!!!********[YunGongZhiDian]*********", getLocalServerId());
            return;
        }
        // 本服是否存在云宫之战的冠军公会
        Long guildId = null;
        Zhengbasai zhengbasai = hcZhengBaSaiExportService.loadZhengbasai();
        if (null != zhengbasai) {
            guildId = zhengbasai.getGuildId();
        }
        if (null == guildId) {
            return;
        }
        // 开启活动结束定时器
        long curTime = GameSystemTime.getSystemMillTime();
        long entTime = DatetimeUtil.getTheDayTheTime(config.getEndtime()[0], config.getEndtime()[1], curTime);
        long delay = entTime - curTime;
        if (delay > 0) {
            scheduleExportService.cancelSchedule(GameConstants.DEFAULT_ROLE_ID.toString(), GameConstants.KUAFU_YUNGONG_END_PRODUCE);
            scheduleExportService.schedule(GameConstants.DEFAULT_ROLE_ID.toString(), GameConstants.KUAFU_YUNGONG_END_PRODUCE, new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.KUAFU_YUNGONG_END, null), (int) delay, TimeUnit.MILLISECONDS);
            // 标记活动开始
            guildOperationOpenHandler();
            dataContainer.putData(GameConstants.KUAFU_YUNGONG_ACTIVITY, GameConstants.KUAFU_YUNGONG_ACTIVITY, guildId);
            // 推送全服活动开始公告
            yunGongNotice(config.getGonggao2());
            // 给本服参赛公会的所有成员发送活动开启通告
            Guild guild = GuildManager.getManager().getGuild(guildId);
            if (null != guild) {
                for (GuildMember member : guild.getAllMembers()) {
                    BusMsgSender.send2One(member.getUserRoleId(), ClientCmdType.KF_YUNGONG_BEGIN_NOTICE, null);
                }
            }
            ChuanQiLog.info("********[YunGongZhiDian]*********source serverId={},kuafu yungongzhidian activity begin step!!!********[YunGongZhiDian]*********", getLocalServerId());
        }
    }

    /**
     * 活动结束阶段
     */
    public void activityEnd() {
        guildOperationCloseHandler();
        // 活动结束标识
        dataContainer.removeData(GameConstants.KUAFU_YUNGONG_ACTIVITY, GameConstants.KUAFU_YUNGONG_ACTIVITY);
        // 清除活动参赛服务器数据
        Redis redis = getRedis();
        if (null != redis && redis.exists(RedisKey.KUAFU_YUNGONG_SERVER_KEY)) {
            redis.del(RedisKey.KUAFU_YUNGONG_SERVER_KEY);
        }
        // 推送全服活动结束公告
        KuafuYunGongResult result = getKuafuYunGongResultData();
        if (null != result) {
            Guild guild = GuildManager.getManager().getGuild(result.getGuildId());
            if (null != guild) {
                KuafuYunGongPublicConfig config = getKfYunGongPublicConfig();
                if (null != config) {
                    yunGongNotice2(config.getGonggao4(), new Object[] { guild.getName(), guild.getLeader().getName() });
                }
            }
        }
        ChuanQiLog.info("********[YunGongZhiDian]*********source serverId={},kuafu yungongzhidian over step!!!********[YunGongZhiDian]*********", getLocalServerId());
    }

    /**
     * 获取跨服云宫之战胜利帮派对象
     * 
     * @return
     */
    private KuafuYunGongResult getKuafuYunGongResultData() {
        Redis redis = getRedis();
        if (null == redis) {
            return null;
        }
        String matchServerId = getKfYunGongMatchServerId();
        if (ObjectUtil.strIsEmpty(matchServerId)) {
            return null;
        }
        KuafuYunGongResult kfYunGongresult = null;
        String win_guild_json = redis.get(RedisKey.getKfYunGongResultKey(matchServerId));
        if (!ObjectUtil.strIsEmpty(win_guild_json)) {
            try {
                kfYunGongresult = JSON.parseObject(win_guild_json, KuafuYunGongResult.class);
            } catch (Exception e) {
                ChuanQiLog.error("analysis source serverId={},kuafu yungongzhidian vo[KuafuYunGongResult] exception info={}", win_guild_json, e);
            }
        }
        return kfYunGongresult;
    }

    /**
     * 根据用户编号获取跨服云宫之战奖励数据
     * 
     * @param userRoleId
     * @return
     */
    private KuafuYunGongDayReward getKuafuYunGongDayReward(Long userRoleId) {
        Redis redis = getRedis();
        if (null == redis) {
            return null;
        }
        Map<Long, KuafuYunGongDayReward> rewardMap = getKuafuYunGongDayRewardMap();
        if (ObjectUtil.isEmpty(rewardMap)) {
            return null;
        }
        return rewardMap.get(userRoleId);
    }

    /**
     * 获取本服跨服云宫之战所有奖励数据集合
     * 
     * @return
     */
    private Map<Long, KuafuYunGongDayReward> getKuafuYunGongDayRewardMap() {
        Redis redis = getRedis();
        if (null == redis) {
            return null;
        }
        String matchServerId = getKfYunGongMatchServerId();
        if (ObjectUtil.strIsEmpty(matchServerId)) {
            return null;
        }
        Map<Long, KuafuYunGongDayReward> rewardMap = null;
        Map<String, String> rewardMapJson = redis.hgetAll(RedisKey.getKfYunGongDayRewardKey(matchServerId));
        if (!ObjectUtil.isEmpty(rewardMapJson)) {
            rewardMap = new HashMap<Long, KuafuYunGongDayReward>();
            for (String json : rewardMapJson.values()) {
                try {
                    KuafuYunGongDayReward reward = JSON.parseObject(json, KuafuYunGongDayReward.class);
                    if (null != reward) {
                        rewardMap.put(reward.getUserRoleId(), reward);
                    }
                } catch (Exception e) {
                    ChuanQiLog.error("deserialize KuafuYunGongDayReward data error,data={}", json);
                }
            }
        }
        return rewardMap;
    }

    /**
     * 获取角色对象数据
     * 
     * @param userRoleId
     * @return
     */
    private RoleWrapper getRoleWrapper(long userRoleId) {
        RoleWrapper role = null;
        if (sessionManagerExportService.isOnline(userRoleId)) {
            role = roleExportService.getLoginRole(userRoleId);
        } else {
            role = roleExportService.getUserRoleFromDb(userRoleId);
        }
        return role;
    }

    /**
     * 获取跨服云宫活动信息
     * 
     * @return 本服参加活动的公会id
     */
    public Long getKuafuYunGongActivityInfo() {
        return dataContainer.getData(GameConstants.KUAFU_YUNGONG_ACTIVITY, GameConstants.KUAFU_YUNGONG_ACTIVITY);
    }

    /**
     * 检测源服跨服云宫之巅活动是否正在运行
     * 
     * @param userRoleId
     * @param responseCmd
     * @return true:运行;false=尚未开始或已结束
     */
    private boolean kufuYunGongRunning(Long userRoleId, Short responseCmd) {
        if (!isKuafuYunGongRunning()) {
            BusMsgSender.send2One(userRoleId, responseCmd, AppErrorCode.KF_YUNGONG_NOT_START_OR_OVER);
            return false;
        }
        return true;
    }
    
    /**
     * 活动结束
     * 
     * @return true=运行;false=结束或未开始
     */
    private boolean isKuafuYunGongRunning(){
        if(ActiveUtil.isKfYunGong() && getKuafuYunGongResultData() != null){//结束或未开始
            ActiveUtil.setKfYunGong(false);
            ActiveUtil.setCanChangeGuild(true);
            // 活动结束标识
            dataContainer.removeData(GameConstants.KUAFU_YUNGONG_ACTIVITY, GameConstants.KUAFU_YUNGONG_ACTIVITY);
        }
        return ActiveUtil.isKfYunGong();
    }
    

    /**
     * 4531请求跨服云宫之战占领信息
     * 
     * @param userRoleId
     * @return
     */
    public Object[] kfYunGongGetWinInfo(long userRoleId) {
        List<Object> clientInfo = Arrays.asList(null, null, null, null, null, null);
        clientInfo.set(5, !isKuafuYunGongRunning());
        KuafuYunGongResult kuafuYunGongResult = getKuafuYunGongResultData();
        if (kuafuYunGongResult != null) {
            Object[] arr1 = new Object[3];
            arr1[0] = kuafuYunGongResult.getGuildName(); // 门派名称
            boolean isReward = false;
            KuafuYunGongDayReward kuafuYunGongDayReward = getKuafuYunGongDayReward(userRoleId);
            if (null != kuafuYunGongDayReward && DatetimeUtil.dayIsToday(kuafuYunGongDayReward.getUpdateTime(), GameSystemTime.getSystemMillTime())) {
                isReward = kuafuYunGongDayReward.getState().equals(KuafuYunGongConstants.REWARD_STAUTS_1);
            }
            arr1[1] = isReward; 
            clientInfo.set(0, arr1);
            Map<Long, KuafuYunGongDayReward> rewardMap = getKuafuYunGongDayRewardMap();
            if (!ObjectUtil.isEmpty(rewardMap)) {
                Object[] leader = new Object[2];
                Object[] leaderPeiOu = new Object[2];// 门主配偶
                int fuLeaderIndex = 2; // 副掌门索引起始位
                for (KuafuYunGongDayReward reward : rewardMap.values()) {
                    if (reward.getPosition() > 2) {// 排除长老
                        continue;
                    }
                    String userName = reward.getName();
                    Integer userSex = reward.getSex();
                    Object[] arr2 = new Object[2];
                    arr2[0] = userName;
                    arr2[1] = userSex;
                    // 门主
                    if (reward.getPosition() == 1 || reward.getUserRoleId().longValue() == kuafuYunGongResult.getGuildLeaderId().longValue()) {
                        leader[0] = userName;
                        leader[1] = userSex;
                    }
                    if (reward.getPosition() == -1) {// -1表示配偶
                        leaderPeiOu[0] = userName;
                        leaderPeiOu[1] = userSex;
                    }
                    if (reward.getPosition() == 2) {
                        clientInfo.set(fuLeaderIndex, arr2);
                        fuLeaderIndex++;
                    }
                }
                if (leader[0] == null) {
                    leader = null;
                }
                clientInfo.set(1, leader);// 调整门主顺序
                if (leaderPeiOu[0] == null) {
                    leaderPeiOu = null;// 无配偶
                }
                clientInfo.set(4, leaderPeiOu);// 配偶
            }
        }
        return clientInfo.toArray();
    }

    /**
     * 4532源服发起请求,进入跨服云宫之战场景
     * 
     * @param userRoleId
     * @return
     */
    public Object[] kfYunGongEnter(Long userRoleId) {
        if (!ActiveUtil.isKfYunGong()) {
            return AppErrorCode.KF_YUNGONG_NOT_START_OR_OVER;
        }
        // 活动是否正在进行
        Long guildId = getKuafuYunGongActivityInfo();
        if (null == guildId) {
            return AppErrorCode.KF_YUNGONG_NOT_START_OR_OVER;
        }
        // 玩家是否加入公会
        GuildMember guildMember = guildExportService.getGuildMember(userRoleId);
        if (null == guildMember) {
            return AppErrorCode.KF_YUNGONG_NOT_GUILD;
        }
        // 玩家所在公会不属于参赛公会
        if (!guildId.equals(guildMember.getGuildId())) {
            return AppErrorCode.KF_YUNGONG_NOT_GUILD;
        }
        // 活动配置是否存在
        KuafuYunGongPublicConfig config = getKfYunGongPublicConfig();
        if (null == config) {
            return AppErrorCode.CONFIG_ERROR;
        }
        // 参赛等级限制
        RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
        int roleLevel = null == roleWrapper ? 0 : roleWrapper.getLevel().intValue();
        if (roleLevel < config.getNeedLevel()) {
            return AppErrorCode.KF_YUNGONG_NOT_LEVEL;
        }
        // 参赛战力限制
        RoleBusinessInfoWrapper roleInfoWrapper = roleBusinessInfoExportService.getRoleBusinessInfoWrapper(userRoleId);
        long roleCurFighter = null == roleInfoWrapper ? 0L : roleInfoWrapper.getCurFighter().longValue();
        if (roleCurFighter < config.getNeedZhanli()) {
            return AppErrorCode.CONFIG_ERROR;
        }
        // 判断是否在副本中
        if (stageControllExportService.inFuben(userRoleId)) {
            return AppErrorCode.FUBEN_IS_IN_FUBEN;
        }
        return readyEnterKuafuYunGong(userRoleId, getKfYunGongMatchServerId());
    }

    /**
     * 进入指定跨服服务器
     * 
     * @param userRoleId
     * @param serverId
     * @return
     */
    private Object[] readyEnterKuafuYunGong(Long userRoleId, String serverId) {
        Redis redis = getRedis();
        if (null == redis) {
            return AppErrorCode.ERR;
        }
        if (ObjectUtil.strIsEmpty(serverId)) {
            ChuanQiLog.error("********[YunGongZhiDian]*********source serverId={} not font matching kuafu server!!!********[YunGongZhiDian]*********", getLocalServerId());
            return AppErrorCode.ERR;
        }
        // 为玩家绑定跨服
        KuafuServerInfo serverInfo = KuafuServerInfoManager.getInstance().getKuafuServerInfo(serverId);
        if (serverInfo == null) {
            serverInfo = KuafuServerInfoManager.getInstance().getKuafuServerInfo(serverId, redis);
            if (serverInfo == null) {
                return AppErrorCode.CONFIG_ERROR;
            }
        }
        // 校验连接是否可用
        KuafuNetTunnel tunnel = KuafuConfigUtil.getConnection(serverInfo);
        if (tunnel == null || !tunnel.isConnected()) {
            KuafuConfigUtil.returnBrokenConnection(tunnel);
            String errorKey = RedisKey.buildKuafuServerErrorKey(serverId);
            redis.hset(errorKey, getLocalServerId(), String.valueOf(1));
            Map<String, String> timesMap = redis.hgetAll(errorKey);
            if (timesMap != null && timesMap.size() >= KuafuConfigUtil.CAN_NOT_CONNECT_CLEAN_TIMES) {
                redis.zrem(RedisKey.KUAFU_SERVER_LIST_KEY, serverId);
                redis.hset(RedisKey.KUAFU_DELETE_SERVER_LIST, serverId, DatetimeUtil.formatTime(GameSystemTime.getSystemMillTime(), DatetimeUtil.FORMART3));
                ChuanQiLog.error("kuafu server connect fail count={}，remove connect fail server id:{}", KuafuConfigUtil.CAN_NOT_CONNECT_CLEAN_TIMES, serverId);
            }
            return AppErrorCode.KUAFU_NO_CONNECTION;
        } else {
            redis.del(RedisKey.buildKuafuServerErrorKey(serverId));
        }
        KuafuConfigUtil.returnConnection(tunnel);
        KuafuRoleServerManager.getInstance().bindServer(userRoleId, serverInfo);

        String stageId = stageControllExportService.getCurStageId(userRoleId);
        if (ObjectUtil.strIsEmpty(stageId)) {
            return AppErrorCode.ERR;
        }
        IStage stage = StageManager.getStage(stageId);
        if (stage == null || stage.isCopy()) {
            return AppErrorCode.ERR;
        }
        IRole role = stage.getElement(userRoleId, ElementType.ROLE);
        if (role == null) {
            return AppErrorCode.ERR;
        }
        // 向跨服发送绑定roleId -->bind(serverId)
        KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID, userRoleId, InnerCmdType.BIND_ROLE_ID_SERVERID, new Object[] { getLocalServerId(), userRoleId });
        // 转成跨服roleData数据
        Object roleData = RoleFactory.createKuaFuRoleData(role);
        // 向跨服发送进入房间的玩家数据
        KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID, userRoleId, InnerCmdType.KUAFU_YUNGONG_SEND_ROLE_DATA, new Object[] { userRoleId, getLocalServerId(), new Object[] { roleData } });
        return null;
    }

    /**
     * 4533 请求领取官员奖励
     * 
     * @param userRoleId
     * @return
     */
    public Object[] kfYunGongReceiveReward(Long userRoleId) {
        if (ActiveUtil.isKfYunGong()) {
            return AppErrorCode.KF_YUNGONG_RUNNING;
        }
        // 活动获胜帮派官员每日奖励不存在
        KuafuYunGongDayReward reward = getKuafuYunGongDayReward(userRoleId);
        if (null == reward) {
            return AppErrorCode.KF_YUNGONG_NOT_REWARD;
        }
        // 活动获胜帮派官员每日奖励已经领取
        long now_time = GameSystemTime.getSystemMillTime();
        if (DatetimeUtil.dayIsToday(reward.getUpdateTime(), now_time) && reward.getState().equals(KuafuYunGongConstants.REWARD_STAUTS_1)) {
            return AppErrorCode.KF_YUNGONG_HAS_REWARD;
        }
        // 配置不存在
        KuafuYunGongPublicConfig config = getKfYunGongPublicConfig();
        if (null == config) {
            return AppErrorCode.CONFIG_ERROR;
        }
        // 背包空间不足
        Map<String, Integer> item = config.getJiangitem();
        Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(item, userRoleId);
        if (code != null) {
            return code;
        }
        // 发送奖励
        roleBagExportService.putGoodsAndNumberAttr(item, userRoleId, GoodsSource.KF_YUNGONG_DAY_REWARD, LogPrintHandle.GET_NORMAL, LogPrintHandle.GBZ_NORMAL, true);
        // 更新数据
        reward.setState(KuafuYunGongConstants.REWARD_STAUTS_1);
        reward.setUpdateTime(now_time);
        Redis redis = getRedis();
        if (null != redis) {
            redis.hset(RedisKey.getKfYunGongDayRewardKey(getKfYunGongMatchServerId()), String.valueOf(userRoleId), JSON.toJSONString(reward));
        }
        ChuanQiLog.info("********[YunGongZhiDian]*********source serverId={},userRoleId={}, receive guild reward, item={}********[YunGongZhiDian]*********", getLocalServerId(), userRoleId, JSON.toJSONString(item));
        return new Object[] { AppErrorCode.SUCCESS };
    }

    /**
     * 4534 请求采集旗子(转发到跨服服务器执行指令)
     * 
     * @param userRoleId
     * @param qiziGuid 旗子guid
     * @return
     */
    public void kfYunGongCollect(Long userRoleId, Long qiziGuid) {
        short command = ClientCmdType.KF_YUNGONG_COLLECT;
        if (!kufuYunGongRunning(userRoleId, command)) {
            return;
        }
        KuafuYunGongPublicConfig config = getKfYunGongPublicConfig();
        if (null == config) {
            BusMsgSender.send2One(userRoleId, command, AppErrorCode.CONFIG_ERROR);
            return;
        }
        // 校验玩家有没有帮派以及是否有权限拔旗
        Object[] result = guildExportService.flagCheck(userRoleId, config.getNeedgong());
        if (result != null) {
            BusMsgSender.send2One(userRoleId, command, result);
            return;
        }
        KuafuMsgSender.send2KuafuServer(userRoleId, userRoleId, InnerCmdType.I_KUAFU_YUNGONG_COLLECT, qiziGuid);
    }

    /**
     * 4535 请求拔起旗子(转发到跨服服务器执行指令)
     * 
     * @param userRoleId
     * @return
     */
    public void kfYunGongPull(Long userRoleId) {
        short command = ClientCmdType.KF_YUNGONG_PULL;
        if (!kufuYunGongRunning(userRoleId, command)) {
            return;
        }
        KuafuYunGongPublicConfig config = getKfYunGongPublicConfig();
        if (null == config) {
            BusMsgSender.send2One(userRoleId, command, AppErrorCode.CONFIG_ERROR);
            return;
        }
        // 校验玩家有没有帮派
        Object[] guildInfo = guildExportService.getGuildInfo(userRoleId);
        if (guildInfo == null) {
            BusMsgSender.send2One(userRoleId, command, AppErrorCode.TERRITORY_NO_GUILD);
            return;
        }
        // 校验帮派职位
        Integer position = (Integer) guildInfo[8];
        if (!GuildUtil.isLeaderOrViceLeader(position)) {
            BusMsgSender.send2One(userRoleId, command, AppErrorCode.TERRITORY_POSITION_LIMIT);
            return;
        }
        KuafuMsgSender.send2KuafuServer(userRoleId, userRoleId, InnerCmdType.I_KUAFU_YUNGONG_PULL, null);
    }

    /**
     * 4536 请求复活
     * 
     * @param userRoleId
     */
    public void kfYunGongFuhuo(Long userRoleId) {
        if (!kufuYunGongRunning(userRoleId, null)) {
            return;
        }
        KuafuMsgSender.send2KuafuServer(userRoleId, userRoleId, InnerCmdType.I_KUAFU_YUNGONG_AUTO_FUHUO, null);
    }

    /**
     * 4541 请求退出活动场景
     * 
     * @param userRoleId
     */
    public void kfYunGongExit(Long userRoleId) {
        KuafuMsgSender.send2KuafuServer(userRoleId, userRoleId, InnerCmdType.I_KUAFU_YUNGONG_EXIT, null);
    }

    /**
     * 源服玩家进入小黑屋安全场景
     * 
     * @param userRoleId
     */
    public void enterXiaoheiwu(Long userRoleId) {
        String stageId = stageControllExportService.getCurStageId(userRoleId);
        IStage stage = StageManager.getStage(stageId);
        if (stage == null) {
            return;
        }
        IRole role = stage.getElement(userRoleId, ElementType.ROLE);
        if (role == null) {
            return;
        }
        KuafuManager.startKuafu(userRoleId);
        // 标记当前为副本状态
        stageControllExportService.changeFuben(userRoleId, true);
        // 发送到场景控制中心进入小黑屋地图，以便从跨服服务器出来时走完整流程
        DiTuConfig config = diTuConfigExportService.loadSafeDiTu();
        int[] birthXy = config.getRandomBirth();
        Object[] applyEnterData = new Object[] { config.getId(), birthXy[0], birthXy[1], MapType.KUAFU_SAFE_MAP };
        // 传送前加一个无敌状态
        role.getStateManager().add(new NoBeiAttack());
        role.setChanging(true);
        StageMsgSender.send2StageControl(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
    }

    /**
     * 源服离开小黑屋(退出跨服云宫之巅场景)
     * 
     * @param userRoleId
     */
    public void exitXiaoheiwu(Long userRoleId) {
        KuafuManager.removeKuafu(userRoleId);
        BusMsgSender.send2BusInner(userRoleId, InnerCmdType.S_APPLY_LEAVE_STAGE, null);
        stageControllExportService.changeFuben(userRoleId, false);
        KuafuRoleServerManager.getInstance().removeBind(userRoleId);
    }

    /**
     * 源服玩家拔起旗子消耗更新
     * 
     * @param userRoleId
     */
    public Object[] pullQiziConsume(Long userRoleId) {
        // 校验玩家有没有帮派
        Object[] guildInfo = guildExportService.getGuildInfo(userRoleId);
        if (guildInfo == null) {
            return AppErrorCode.TERRITORY_NO_GUILD;
        }
        // 校验帮派职位
        Integer position = (Integer) guildInfo[8];
        if (!GuildUtil.isLeaderOrViceLeader(position)) {
            return AppErrorCode.TERRITORY_POSITION_LIMIT;
        }
        KuafuYunGongPublicConfig config = getKfYunGongPublicConfig();
        if (null == config) {
            return AppErrorCode.CONFIG_ERROR;
        }
        // 校验玩家有没有帮派以及是否有权限拔旗
        Object[] result = guildExportService.flagCost(userRoleId, config.getNeedgong());
        if (result != null) {
            return result;
        }
        return null;
    }

    /**
     * 更新活动结果数据(只在获胜的帮派所在服务器执行)
     * 
     * @param userRoleId
     * @param kfServerId
     * @param winGuildId
     */
    public void updateResultData(Long userRoleId, String kfServerId, Long winGuildId) {
        if (ObjectUtil.strIsEmpty(kfServerId) || winGuildId == null || winGuildId.longValue() <= 0) {
            return;
        }
        Redis redis = getRedis();
        if (null == redis) {
            return;
        }
        Object[] info = guildExportService.getGuildBaseInfo(winGuildId);
        if (null != info) {
            // 门主角色编号
            Long guildLeaderId = (Long) info[1];
            if (guildLeaderId == null) {
                ChuanQiLog.error("********[YunGongZhiDian]*********source serverId={},kuafu yungongzhidian update result data,winner guildId={}, not found leader********[YunGongZhiDian]*********", getLocalServerId(), winGuildId);
            }
            // 门主配偶编号
            Long peiOuId = null;
            try {
                peiOuId = marryExportService.getPeiouUserRoleId(guildLeaderId);
            } catch (Exception e) {
                ChuanQiLog.error("********[YunGongZhiDian]*********source serverId={},kuafu yungongzhidian update result data, get leader peiou data error={}********[YunGongZhiDian]*********", getLocalServerId(), e);
            }
            // 创建活动结果数据
            KuafuYunGongResult result = new KuafuYunGongResult();
            result.setGuildId(winGuildId);
            result.setGuildName((String) info[0]);
            result.setGuildLeaderId(guildLeaderId);
            result.setUpdateTime(GameSystemTime.getSystemMillTime());
            redis.set(RedisKey.getKfYunGongResultKey(kfServerId), JSON.toJSONString(result));

            // 创建活动获胜公会官员的领奖数据
            Map<Long, KuafuYunGongDayReward> dayRewardsMap = null;
            List<GuildMember> allMembers = guildExportService.getAllLeader(winGuildId);
            if (!ObjectUtil.isEmpty(allMembers)) {
                dayRewardsMap = new HashMap<Long, KuafuYunGongDayReward>();
                for (GuildMember guildMember : allMembers) {
                    RoleWrapper role = getRoleWrapper(guildMember.getUserRoleId());
                    if (null == role) {
                        ChuanQiLog.error("********[YunGongZhiDian]*********source serverId={}, kuafu yungongzhidian update result data,,local server not found role, roleId={}", getLocalServerId(), guildMember.getUserRoleId());
                        continue;
                    }
                    ZhuJueDzConfig zhujueConfig = zhuJueDzConfigExportService.loadById(role.getConfigId());
                    KuafuYunGongDayReward dayReward = new KuafuYunGongDayReward();
                    dayReward.setGuildId(winGuildId);
                    dayReward.setUserRoleId(guildMember.getUserRoleId());
                    dayReward.setName(role.getName());
                    dayReward.setSex(zhujueConfig.getSex());
                    dayReward.setPosition(guildMember.getPostion());
                    dayReward.setState(KuafuYunGongConstants.REWARD_STAUTS_0);
                    dayReward.setUpdateTime(GameSystemTime.getSystemMillTime());
                    dayRewardsMap.put(dayReward.getUserRoleId(), dayReward);
                }
                // 配偶奖励处理
                if (peiOuId != null) {
                    KuafuYunGongDayReward peiOuDayReward = dayRewardsMap.get(peiOuId);
                    if (null == peiOuDayReward) {
                        RoleWrapper peiOuRole = getRoleWrapper(peiOuId);
                        if (null != peiOuRole) {
                            ZhuJueDzConfig zhujueConfig = zhuJueDzConfigExportService.loadById(peiOuRole.getConfigId());
                            peiOuDayReward = new KuafuYunGongDayReward();
                            peiOuDayReward.setGuildId(winGuildId);
                            peiOuDayReward.setUserRoleId(peiOuId);
                            peiOuDayReward.setName(peiOuRole.getName());
                            peiOuDayReward.setSex(zhujueConfig.getSex());
                            peiOuDayReward.setPosition(-1);
                            peiOuDayReward.setState(KuafuYunGongConstants.REWARD_STAUTS_0);
                            peiOuDayReward.setUpdateTime(GameSystemTime.getSystemMillTime());
                        }
                    } else {
                        peiOuDayReward.setPosition(-1);
                    }
                    dayRewardsMap.put(peiOuId, peiOuDayReward);
                }
                // 更新数据到redis
                for (Entry<Long, KuafuYunGongDayReward> entry : dayRewardsMap.entrySet()) {
                    redis.hset(RedisKey.getKfYunGongDayRewardKey(kfServerId), String.valueOf(entry.getKey()), JSON.toJSONString(entry.getValue()));
                }
                // 更新门主及其配偶地图外显
                if (null != guildLeaderId) {
                    Guild guild = GuildManager.getManager().getGuild(winGuildId);
                    guild.setKfYunGongWinner(true);
                    if (KuafuManager.kuafuIng(guildLeaderId)) {
                        KuafuMsgSender.send2KuafuServer(guildLeaderId, guildLeaderId, InnerCmdType.I_KUAFU_YUNGONG_CHANGE_CLOTHES, null);
                    } else {
                        BusMsgSender.send2One(guildLeaderId, ClientCmdType.KF_YUNGONG_CLOTHES_SHOW, true);
                    }
                }
                if (null != peiOuId) {
                    if (KuafuManager.kuafuIng(peiOuId)) {
                        KuafuMsgSender.send2KuafuServer(peiOuId, peiOuId, InnerCmdType.I_KUAFU_YUNGONG_CHANGE_CLOTHES, null);
                    } else {
                        BusMsgSender.send2One(peiOuId, ClientCmdType.KF_YUNGONG_CLOTHES_SHOW, true);
                    }
                }
            }
        }
        ChuanQiLog.error("*********[YunGongZhiDian]*********source serverId={}, kuafu yungongzhidian update result data success*********[YunGongZhiDian]*********", getLocalServerId());
    }

    /**
     * 跨服活动结束,同步源服活动进度
     */
    public void kuafuActivityEnd() {
        // 源服活动尚未开始或已经结束
        if (getKuafuYunGongActivityInfo() == null) {
            return;
        }
        ChuanQiLog.info("********[YunGongZhiDian]*********source serverId={}, kuafu server activity over, synchronize local server progress", getLocalServerId());
        // 取消源服活动结束定时器
        scheduleExportService.cancelSchedule(GameConstants.DEFAULT_ROLE_ID.toString(), GameConstants.KUAFU_YUNGONG_END_PRODUCE);
        // 源服活动结束处理
        activityEnd();
    }

    /**
     * 判断玩家是否拥有跨服云宫之巅外显奖励
     * 
     * @param userRoleId
     * @return
     */
    public boolean kuafuYunGongIsShowCloth(Long userRoleId) {
        // 活动获胜帮派官员每日奖励不存在
        KuafuYunGongDayReward reward = getKuafuYunGongDayReward(userRoleId);
        if (null != reward && (reward.getPosition().equals(GameConstants.GUILD_LEADER_POSTION) || reward.getPosition().equals(-1))) {
            return true;
        }
        return false;
    }

    /**
     * 发放邮件奖励
     * 
     * @param userRoleId
     * @param rewardType 奖励类型
     */
    public void kuafuYunGongSendEmailReward(Long userRoleId, Integer rewardType) {
        if (null == rewardType) {
            return;
        }
        KuafuYunGongPublicConfig config = getKfYunGongPublicConfig();
        if (null == config) {
            ChuanQiLog.error("********[YunGongZhiDian]*********source serverId={}, send email reward, KuafuYunGongPublicConfig is null !!!********[YunGongZhiDian]*********");
            return;
        }
        String title = EmailUtil.getCodeEmail(KuafuYunGongConstants.REWARD_EMAIL_TITLE);
        // 停留达到一定时间的奖励
        if (rewardType.equals(KuafuYunGongConstants.REWARD_TYPE_TIME)) {
            Map<String, Integer> attachmentTimeMap = new HashMap<String, Integer>();
            attachmentTimeMap.put(ModulePropIdConstant.EXP_GOODS_ID, config.getJingyan2());
            attachmentTimeMap.put(ModulePropIdConstant.MONEY_ZHENQI_ID, config.getZhenqi2());
            String[] attachmentTimeArray = EmailUtil.getAttachments(attachmentTimeMap);
            for (String attachment : attachmentTimeArray) {
                emailExportService.sendEmailToOne(userRoleId, title,EmailUtil.getCodeEmail(KuafuYunGongConstants.TIME_MINUTE_REWARD), GameConstants.EMAIL_TYPE_SINGLE, attachment);
            }
            ChuanQiLog.info("********[YunGongZhiDian]*********source serverId={}, send time reward email, roleId={}********[YunGongZhiDian]*********" , getLocalServerId(), userRoleId);
        }
        // 胜利方奖励
        else if (rewardType.equals(KuafuYunGongConstants.REWARD_TYPE_WIN)) {
            RoleWrapper role = getRoleWrapper(userRoleId);
            if (null == role) {
                ChuanQiLog.error("********[YunGongZhiDian]*********source serverId={}, send winner reward email, roleId={} is not exists!!!********[YunGongZhiDian]", getLocalServerId(), userRoleId);
                return;
            }
            Map<String, Integer> attachmentWinMap = new HashMap<String, Integer>();
            int winExp = config.getWinExp() * role.getLevel() * role.getLevel();
            int winBg = config.getWinBg();
            attachmentWinMap.put(ModulePropIdConstant.EXP_GOODS_ID, winExp);
            attachmentWinMap.put(ModulePropIdConstant.GONGXIAN_GOODS_ID, winBg);
            String[] attachmentWinArray = EmailUtil.getAttachments(attachmentWinMap);
            for (String attachment : attachmentWinArray) {
                emailExportService.sendEmailToOne(userRoleId,title, EmailUtil.getCodeEmail(KuafuYunGongConstants.WIN_EAMIL_CONTENT, String.valueOf(winBg), String.valueOf(winExp)), GameConstants.EMAIL_TYPE_SINGLE, attachment);
            }
            ChuanQiLog.info("********[YunGongZhiDian]*********source serverId={}, send winner reward email, roleId={}********[YunGongZhiDian]*********", getLocalServerId(), userRoleId);
            KuafuYunGongDayReward reward = getKuafuYunGongDayReward(userRoleId);
            if (reward == null) {
                return;
            }
            // 发放门主称号奖励邮件
            if (reward.getPosition().equals(GameConstants.GUILD_LEADER_POSTION)) {
                emailExportService.sendEmailToOne(userRoleId, title,EmailUtil.getCodeEmail(KuafuYunGongConstants.CHENGHAO_EAMIL_CONTENT), GameConstants.EMAIL_TYPE_SINGLE, new StringBuffer().append(config.getChenhao1()).append(GameConstants.FUJIAN_SPLIT_TWO).append(1).toString());
                ChuanQiLog.info("********[YunGongZhiDian]*********source serverId={}, send winner leader title reward email success!!!********[YunGongZhiDian]*********", getLocalServerId());
            } else if (reward.getPosition().equals(-1)) {
                emailExportService.sendEmailToOne(userRoleId, title,EmailUtil.getCodeEmail(KuafuYunGongConstants.CHENGHAO_EAMIL_CONTENT), GameConstants.EMAIL_TYPE_SINGLE, new StringBuffer().append(config.getChenhao2()).append(GameConstants.FUJIAN_SPLIT_TWO).append(1).toString());
                ChuanQiLog.info("********[YunGongZhiDian]*********source serverId={}, send winner leader peiou title reward email success!!!********[YunGongZhiDian]*********", getLocalServerId());
            }
        }
        // 失败方奖励
        else if (rewardType.equals(KuafuYunGongConstants.REWARD_TYPE_LOSE)) {
            RoleWrapper role = getRoleWrapper(userRoleId);
            if (null == role) {
                ChuanQiLog.error("********[YunGongZhiDian]*********source serverId={}, send lose reward email, roleId={} is not exists!!!********[YunGongZhiDian]", userRoleId);
                return;
            }
            Map<String, Integer> attachmentLoseMap = new HashMap<String, Integer>();
            int loseExp = config.getLoseExp() * role.getLevel() * role.getLevel();
            int loseBg = config.getLoseBg();
            attachmentLoseMap.put(ModulePropIdConstant.EXP_GOODS_ID, loseExp);
            attachmentLoseMap.put(ModulePropIdConstant.GONGXIAN_GOODS_ID, loseBg);
            String[] attachmentLoseArray = EmailUtil.getAttachments(attachmentLoseMap);
            for (String attachment : attachmentLoseArray) {
                emailExportService.sendEmailToOne(userRoleId,title, EmailUtil.getCodeEmail(KuafuYunGongConstants.LOSE_EAMIL_CONTENT, String.valueOf(loseBg), String.valueOf(loseExp)), GameConstants.EMAIL_TYPE_SINGLE, attachment);
            }
            ChuanQiLog.info("********[YunGongZhiDian]*********source serverId={}, send lose reward email, roleId={}********[YunGongZhiDian]*********", getLocalServerId(), userRoleId);
        }

    }

}
