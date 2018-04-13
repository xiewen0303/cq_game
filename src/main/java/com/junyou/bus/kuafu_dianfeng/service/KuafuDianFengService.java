package com.junyou.bus.kuafu_dianfeng.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.kuafu_dianfeng.configure.DianFengZhiZhanConfig;
import com.junyou.bus.kuafu_dianfeng.configure.DianFengZhiZhanConfigExportService;
import com.junyou.bus.kuafu_dianfeng.constants.KuaFuDianFengConstants;
import com.junyou.bus.kuafu_dianfeng.utils.KuafuDianFengUtils;
import com.junyou.bus.kuafu_dianfeng.vo.DianFengPlayerVO;
import com.junyou.bus.kuafuluandou.constants.KuaFuDaLuanDouConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.StageUtil;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.io.export.SessionManagerExportService;
import com.junyou.kuafu.manager.KuafuManager;
import com.junyou.kuafu.manager.KuafuRoleServerManager;
import com.junyou.kuafu.manager.KuafuServerInfoManager;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.kuafu.share.tunnel.KuafuNetTunnel;
import com.junyou.kuafu.share.util.KuafuConfigUtil;
import com.junyou.kuafu.share.util.KuafuServerInfo;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.element.role.RoleFactory;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.state.NoBeiAttack;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.kernel.cache.redis.Redis;
import com.kernel.cache.redis.RedisKey;

/**
 * @Description 巅峰之战业务处理类
 * @Author Yang Gao
 * @Since 2016-5-18
 * @Version 1.1.0
 */
@Service
public class KuafuDianFengService {

    private static Map<Integer, List<String>> ROOM_NAME_SOURCE_LOOP_ONE = new HashMap<Integer, List<String>>();
    private static Map<Integer, List<String>> ROOM_NAME_SOURCE_LOOP_TWO = new HashMap<Integer, List<String>>();
    private static Map<Integer, List<String>> ROOM_NAME_SOURCE_LOOP_THREE = new HashMap<Integer, List<String>>();
    private static Map<Integer, List<String>> ROOM_NAME_SOURCE_LOOP_FOUR = new HashMap<Integer, List<String>>();

    @Autowired
    private EmailExportService emailExportService;
    @Autowired
    private DiTuConfigExportService diTuConfigExportService;
    @Autowired
    private SessionManagerExportService sessionManagerExportService;
    @Autowired
    private StageControllExportService stageControllExportService;
    @Autowired
    private RoleExportService roleExportService;
    @Autowired
    private DianFengZhiZhanConfigExportService dianFengZhiZhanConfigExportService;

    static {
        initLoopRoomGroupName();
    }

    // 初始化每一轮比赛的房间名集合
    private static void initLoopRoomGroupName() {
        ROOM_NAME_SOURCE_LOOP_ONE.put(1, Arrays.asList("A1", "H2"));
        ROOM_NAME_SOURCE_LOOP_ONE.put(2, Arrays.asList("F1", "C2"));
        ROOM_NAME_SOURCE_LOOP_ONE.put(3, Arrays.asList("D1", "E2"));
        ROOM_NAME_SOURCE_LOOP_ONE.put(4, Arrays.asList("G1", "B2"));
        ROOM_NAME_SOURCE_LOOP_ONE.put(5, Arrays.asList("B1", "G2"));
        ROOM_NAME_SOURCE_LOOP_ONE.put(6, Arrays.asList("E1", "D2"));
        ROOM_NAME_SOURCE_LOOP_ONE.put(7, Arrays.asList("C1", "F2"));
        ROOM_NAME_SOURCE_LOOP_ONE.put(8, Arrays.asList("H1", "A2"));

        ROOM_NAME_SOURCE_LOOP_TWO.put(1, Arrays.asList("I1", "J1"));
        ROOM_NAME_SOURCE_LOOP_TWO.put(2, Arrays.asList("K1", "L1"));
        ROOM_NAME_SOURCE_LOOP_TWO.put(3, Arrays.asList("M1", "N1"));
        ROOM_NAME_SOURCE_LOOP_TWO.put(4, Arrays.asList("O1", "P1"));

        ROOM_NAME_SOURCE_LOOP_THREE.put(1, Arrays.asList("Q1", "R1"));
        ROOM_NAME_SOURCE_LOOP_THREE.put(2, Arrays.asList("S1", "T1"));

        ROOM_NAME_SOURCE_LOOP_FOUR.put(1, Arrays.asList("U1", "V1"));
    }

    // 根据轮次获取房间名称集合
    private static Map<Integer, List<String>> getRoomNameByLoop(int loop) {
        switch (loop) {
        case KuaFuDianFengConstants.LOOP_ONE:
            return ROOM_NAME_SOURCE_LOOP_ONE;
        case KuaFuDianFengConstants.LOOP_TWO:
            return ROOM_NAME_SOURCE_LOOP_TWO;
        case KuaFuDianFengConstants.LOOP_THREE:
            return ROOM_NAME_SOURCE_LOOP_THREE;
        case KuaFuDianFengConstants.LOOP_FOUR:
            return ROOM_NAME_SOURCE_LOOP_FOUR;
        default:
            break;
        }
        return null;
    }

    // 获取巅峰之战当前时间的进行轮次 (0=没有开始或者已经结束;不等于0则表示有效返回)
    private int getDianFengCurLoop(long cur_time, long error_time) {
        Map<Integer, DianFengZhiZhanConfig> configs = dianFengZhiZhanConfigExportService.getAllDianFengConfigMap();
        if (!ObjectUtil.isEmpty(configs)) {
            int toDayWeek = DatetimeUtil.getCurWeek();
            long now_time = cur_time + error_time;
            for (Entry<Integer, DianFengZhiZhanConfig> entry : configs.entrySet()) {
                DianFengZhiZhanConfig dfConfig = entry.getValue();
                if (toDayWeek != dfConfig.getWeek()) {
                    continue;
                }
                long begin_time = DatetimeUtil.getCalcTimeCurTime(now_time, dfConfig.getBegintime()[0], dfConfig.getBegintime()[1], dfConfig.getBegintime()[2]);
                long end_time = DatetimeUtil.getCalcTimeCurTime(now_time, dfConfig.getEndtime()[0], dfConfig.getEndtime()[1], dfConfig.getEndtime()[2]);
                if (begin_time <= now_time && now_time <= end_time) {
                    return dfConfig.getLoop();
                }
            }
        }
        return 0;
    }

    // -------------------------------------------------------------------//

    /**
     * 巅峰之战开始轮循
     */
    public void loopDianFeng() {
        sendDianFengStartNotice();
    }

    /**
     * 清除巅峰之战结果数据
     */
    public void clearAllDianFengData() {
        Redis redis = GameServerContext.getRedis();
        if (null == redis) {
            ChuanQiLog.error("redis not open");
            return;
        }
        // 清除巅峰之战数据
        for (int loop = 1; loop <= KuafuDianFengUtils.getMaxLoop(); loop++) {
            redis.del(RedisKey.getRedisKfDianFengRoomKey(loop));
        }
    }

    /**
     * 巅峰之战发送奖励
     */
    public void sendDianFengReward() {
        sendReward();
    }

    /**
     * 获取玩家巅峰之战的场景ID
     * 
     * @param userRoleId
     * @return
     */
    public String getDianFengRoleStageId(Long userRoleId) {
        Redis redis = GameServerContext.getRedis();
        if (redis == null) {
            ChuanQiLog.error("redis not open");
            return null;
        }
        int loop = getDianFengCurLoop(GameSystemTime.getSystemMillTime(), GameConstants.SPRING_DINGSHI_ERRER_TIME);
        DianFengZhiZhanConfig dfConfig = dianFengZhiZhanConfigExportService.loadByLoop(loop);
        if (null == dfConfig) {
            return null;
        }
        int room = getDianFengRoomOnLoop(userRoleId, redis, loop);
        if (0 >= room) {
            return null;
        }
        return StageUtil.getDianFengStageId(dfConfig.getMapId(), loop, room);
    }

    // -------------------------------------------------------------------//

    // 巅峰之战发送奖励
    private void sendReward() {
        Redis redis = GameServerContext.getRedis();
        if (null == redis) {
            ChuanQiLog.error("redis not open");
            return;
        }
        int maxLoop = KuafuDianFengUtils.getMaxLoop();
        for (int loop = 1; loop <= maxLoop; loop++) {
            Map<String, String> map = redis.hgetAll(RedisKey.getRedisKfDianFengRoomKey(loop));
            if (!ObjectUtil.isEmpty(map)) {
                for (String voJSONString : map.values()) {
                    try {
                        List<DianFengPlayerVO> voList = JSON.parseArray(voJSONString, DianFengPlayerVO.class);
                        if (!ObjectUtil.isEmpty(voList)) {
                            for (DianFengPlayerVO vo : voList) {
                                ChuanQiLog.info("******KuaFuDianFeng******source server send loop kuafudianfeng reward email, serverId={}, userRoleId={}, isBenfu={}, loop={}, reward data={}******KuaFuDianFeng******", ChuanQiConfigUtil.getServerId(), vo.getUserRoleId(), roleExportService.isLocalServerRole(vo.getUserRoleId()), loop, voJSONString);
                                if(!roleExportService.isLocalServerRole(vo.getUserRoleId())){
                                    continue;
                                }
                                if (vo.getArenaResult() != null && KuaFuDianFengConstants.STATE_WIN == vo.getArenaResult().intValue()) {
                                    if (loop == maxLoop) {// 冠军
                                        sendEmailRewardByLoop(vo.getUserRoleId(), loop + 1);
                                        BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT7, new Object[] { AppErrorCode.KUAFU_DIANFENG_OVER_NOTICE, new Object[] { new Object[] { 2, vo.getUserRoleId(), vo.getNickName() } } });
                                    }
                                } else {
                                    sendEmailRewardByLoop(vo.getUserRoleId(), loop);
                                }
                            }
                        }
                    } catch (Exception e) {
                        ChuanQiLog.error("deserialize dianfeng vo data error ,data:{}", voJSONString);
                    }
                }
            }
        }
        // 删除巅峰之战跨服服务器房间key
        if(redis.exists(RedisKey.KUAFU_DIANFENG_SERVER_ID)){
            redis.del(RedisKey.KUAFU_DIANFENG_SERVER_ID);
        }
    }

    // 发送不同轮次的邮件奖励
    private void sendEmailRewardByLoop(Long userRoleId, int loop) {
        int rewardId = dianFengZhiZhanConfigExportService.getRewardIdByLoop(loop);
        if (0 >= rewardId) {
            return;
        }

        String fuJianItem = new StringBuffer().append(rewardId).append(GameConstants.FUJIAN_SPLIT_TWO).append(1).toString();
        String content = EmailUtil.getCodeEmail(AppErrorCode.KUAFU_DIANFENG_REWARD_EMAIL);
        String title = EmailUtil.getCodeEmail(AppErrorCode.KUAFU_DIANFENG_REWARD_EMAIL_TITLE);
        emailExportService.sendEmailToOne(userRoleId, title,content, GameConstants.EMAIL_TYPE_SINGLE, fuJianItem);
        ChuanQiLog.info("*******KuafuDianFeng********send loop email reward, userRoleId={}, loop={}, rewardItem={}*******KuafuDianFeng********", userRoleId, loop, fuJianItem);
    }

    // 发送新的一轮活动开始通知
    private void sendDianFengStartNotice() {
        Redis redis = GameServerContext.getRedis();
        if (redis == null) {
            ChuanQiLog.error("redis not open");
            return;
        }
        int loop = getDianFengCurLoop(GameSystemTime.getSystemMillTime(), GameConstants.SPRING_DINGSHI_ERRER_TIME);
        switch (loop) {
        case KuaFuDianFengConstants.LOOP_ONE:
            oneLoopStartNotice(redis);
            break;
        case KuaFuDianFengConstants.LOOP_TWO:
        case KuaFuDianFengConstants.LOOP_THREE:
        case KuaFuDianFengConstants.LOOP_FOUR:
            otherLoopStartNotice(redis, loop);
            break;
        default:
            break;
        }
    }

    // 其他轮次比赛开始通知(loop=其他轮次)
    private void otherLoopStartNotice(Redis redis, int loop) {
        Map<String, String> loopDataMap = redis.hgetAll(RedisKey.getRedisKfDianFengRoomKey(loop));
        if (!ObjectUtil.isEmpty(loopDataMap)) {
            ChuanQiLog.info("******KuafuDengFeng******source server start loop dianfengzhi, serverId={}, loop={}, loopDataMap={}******KuafuDengFeng******", ChuanQiConfigUtil.getServerId(), loop, JSON.toJSONString(loopDataMap));
            for (Entry<String, String> entry : loopDataMap.entrySet()) {
                try {
                    List<DianFengPlayerVO> voList = JSON.parseArray(entry.getValue(), DianFengPlayerVO.class);
                    dianFengNotice(loop, Integer.parseInt(entry.getKey()), voList, redis);
                } catch (Exception e) {
                    ChuanQiLog.error("deserialize dianfeng vo data error ,data:{}", entry.getValue());
                }
            }
        }
    }

    // 第一轮次比赛开始通知
    private void oneLoopStartNotice(Redis redis) {
        Map<Integer, List<DianFengPlayerVO>> roomMap = new HashMap<>();
        List<DianFengPlayerVO> voList = getDaLuanDouAllDataVO(redis);
        int loop = KuaFuDianFengConstants.LOOP_ONE;
        if (!ObjectUtil.isEmpty(voList)) {
            for (DianFengPlayerVO vo : voList) {
                int roomNo = getOneLoopRoomByGroup(loop, vo.getGroupName());
                List<DianFengPlayerVO> userRoleList = roomMap.get(roomNo);
                if (null == userRoleList) {
                    userRoleList = new ArrayList<>();
                }
                userRoleList.add(vo);
                roomMap.put(roomNo, userRoleList);
            }
        }
        ChuanQiLog.info("******KuafuDengFeng******source server start loop dianfengzhi, serverId={}, loop={}, roomMap={}******KuafuDengFeng******", ChuanQiConfigUtil.getServerId(), loop, JSON.toJSONString(roomMap));
        if (ObjectUtil.isEmpty(roomMap)) {
            return;
        }
        for (Entry<Integer, List<DianFengPlayerVO>> entry : roomMap.entrySet()) {
            dianFengNotice(KuaFuDianFengConstants.LOOP_ONE, entry.getKey(), entry.getValue(), redis);
        }
    }

    // 获取第一轮次分配的房间
    private int getOneLoopRoomByGroup(int loop, String groupName) {
        Map<Integer, List<String>> loopGroupName = getRoomNameByLoop(loop);
        if (null != loopGroupName) {
            for (Entry<Integer, List<String>> entry : loopGroupName.entrySet()) {
                if (entry.getValue().contains(groupName)) {
                    return entry.getKey();
                }
            }
        }
        return 0;
    }

    // 根据比赛信息推送通知
    private void dianFengNotice(int loop, int room, List<DianFengPlayerVO> voList, Redis redis) {
        int length = ObjectUtil.isEmpty(voList) ? 0 : voList.size();
        if (0 >= length) {
            return;
        }
        String kfDianFengServerId = KuafuDianFengUtils.getDianFengKuaFuServerId();
        if (1 == length) {
            DianFengPlayerVO vo = voList.get(0);
            long userRoleId = vo.getUserRoleId();
            if(!roleExportService.isLocalServerRole(userRoleId)){
                return;
            }
            vo.setArenaResult(KuaFuDianFengConstants.STATE_WIN);
            BusMsgSender.send2One(userRoleId, ClientCmdType.KUAFU_DIANFENG_WINNER, loop);

            KuafuDianFengUtils.bindDianFengKuaFuServerId(userRoleId, kfDianFengServerId, redis);
            if (loop == KuaFuDianFengConstants.LOOP_ONE) {
                Object[] result = new Object[] { userRoleId, loop, room, JSON.toJSONString(vo), true };
                KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID, userRoleId, InnerCmdType.KUAFU_DIANFENG_INSERT_DATA, result);
            } else {
                Object[] result = new Object[] { loop, room, userRoleId };
                KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID, userRoleId, InnerCmdType.KUAFU_DIANFENG_UPDATE_DATA, result);
            }
        } else if (2 == length) {
            for (int i = 0; i < length; i++) {
                DianFengPlayerVO vo = voList.get(i);
                long userRoleId = vo.getUserRoleId();
                if(!roleExportService.isLocalServerRole(userRoleId)){
                    continue;
                }
                KuafuDianFengUtils.bindDianFengKuaFuServerId(userRoleId, kfDianFengServerId, redis);
                if(loop == KuaFuDianFengConstants.LOOP_ONE){
                    Object[] result = new Object[] { userRoleId, loop, room, JSON.toJSONString(vo), false};
                    KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID, userRoleId, InnerCmdType.KUAFU_DIANFENG_INSERT_DATA, result);
                }
                Object[] result = new Object[] { userRoleId, ChuanQiConfigUtil.getServerId(), loop, room, sessionManagerExportService.isOnline(userRoleId) };
                KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID, userRoleId, InnerCmdType.KUAFU_DIANFENG_SAVE_DATA, result);
            }
        }
    }
    
    // 获取其它轮次分配玩家站位昵称(loop=其他轮次;beforeRoom上轮分配的房间;curRoom=当前轮次分配的房间)
    private static String getOtherLoopGroupName(int loop, int beforeRoom, int curRoom) {
        Map<Integer, List<String>> loopGroupNameMap = getRoomNameByLoop(loop);
        if (null != loopGroupNameMap) {
            List<String> list = loopGroupNameMap.get(curRoom);
            int size = null == list ? 0 : list.size();
            if (size > 0) {
                int index = beforeRoom % size;
                index = (index == 0 ? size : index) - 1;
                return list.get(index);
            }
        }
        return null;
    }

    // 获取其它轮次分配的房间
    private static int getOtherLoopRoomByBeforeRoom(int room, int loop) {
        if (0 >= room) {
            return 0;
        }
        int groupCnt = 0;
        if (KuaFuDianFengConstants.LOOP_TWO <= loop && loop <= KuafuDianFengUtils.getMaxLoop()) {
            groupCnt = KuafuDianFengUtils.getMaxRoomByLoop(loop - 1) / KuafuDianFengUtils.getMaxRoomByLoop(loop);
        }
        if (0 >= groupCnt) {
            return 0;
        }
        if (room % groupCnt != 0) {
            room += (groupCnt - 1);
        }
        return room / groupCnt;
    }

    // -------------------------------------------------------------------//

    /**
     * 获取大乱斗所有数据VO集合
     * 
     * @param redis
     * @return
     */
    private List<DianFengPlayerVO> getDaLuanDouAllDataVO(Redis redis) {
        List<DianFengPlayerVO> rsList = new ArrayList<>();
        for (int room = 1; room <= KuaFuDaLuanDouConstants.KUAFU_LUANDOU_FANGJIAN_COUNT; room++) {
            Map<String, String> map = redis.hgetAll(RedisKey.getRedisKuafuLuanDouRoomRankKey(room));
            if (ObjectUtil.isEmpty(map)) {
                continue;
            }
            for (String voJSONString : map.values()) {
                try {
                    DianFengPlayerVO vo = JSON.parseObject(voJSONString, DianFengPlayerVO.class);
                    rsList.add(vo);
                } catch (Exception e) {
                    ChuanQiLog.error("deserialize dianfeng vo data error ,data:{}", voJSONString);
                }
            }
        }
        return rsList;
    }

    /**
     * 获取大乱斗所有数据作为巅峰之战第一轮比赛的参赛者展示VO
     * 
     * @param redis
     * @param rsList
     * @return
     */
    private Object[] getDaLuanDouAllDataVO(Redis redis, List<Object[]> rsList) {
        List<Object[]> loopList = new ArrayList<>();
        List<DianFengPlayerVO> voList = getDaLuanDouAllDataVO(redis);
        if (!ObjectUtil.isEmpty(voList)) {
            for (DianFengPlayerVO vo : voList) {
                loopList.add(vo.getShowObject());
            }
        }
        if (ObjectUtil.isEmpty(loopList)) {
            return null;
        } else {
            rsList.add(loopList.toArray());
            return rsList.toArray();
        }
    }

    /**
     * 获取所有巅峰之战的数据展示VO
     * 
     * @param redis
     * @return
     */
    private List<Object[]> getDianFengAllDataVO(Redis redis) {
        List<Object[]> rsList = new ArrayList<>();
        for (int loop = 1; loop <= KuafuDianFengUtils.getMaxLoop(); loop++) {
            Map<String, String> map = redis.hgetAll(RedisKey.getRedisKfDianFengRoomKey(loop));
            if (ObjectUtil.isEmpty(map)) {
                continue;
            }
            List<Object[]> loopList = new ArrayList<>();
            for (String room : map.keySet()) {
                String voJSONString = map.get(String.valueOf(room));
                if (ObjectUtil.strIsEmpty(voJSONString)) {
                    continue;
                }
                try {
                    List<DianFengPlayerVO> voList = JSON.parseArray(voJSONString, DianFengPlayerVO.class);
                    if (ObjectUtil.isEmpty(voList)) {
                        continue;
                    }
                    for (DianFengPlayerVO vo : voList) {
                        loopList.add(vo.getShowObject());
                    }
                } catch (Exception e) {
                    ChuanQiLog.error("deserialize dianfeng vo data error,data:{}", voJSONString);
                }
            }
            rsList.add(loopList.toArray());
        }
        return rsList;
    }

    /**
     * 获取当前轮次玩家所在房间号
     * 
     * @param userRoleId
     * @param loop
     * @return 0=无效房间号;非0有效房间号
     */
    private int getDianFengRoomOnLoop(Long userRoleId, Redis redis, int loop) {
        Map<String, String> loopDataMap = redis.hgetAll(RedisKey.getRedisKfDianFengRoomKey(loop));
        if (!ObjectUtil.isEmpty(loopDataMap)) {
            for (Entry<String, String> entry : loopDataMap.entrySet()) {
                try {
                    String json = entry.getValue();
                    if (ObjectUtil.strIsEmpty(json)) {
                        continue;
                    }
                    List<DianFengPlayerVO> dfVoList = JSON.parseArray(entry.getValue(), DianFengPlayerVO.class);
                    for (DianFengPlayerVO vo : dfVoList) {
                        // 取上一轮房间中胜利的一方作为进入下一轮的参赛人员
                        if (userRoleId.equals(vo.getUserRoleId())) {
                            return Integer.parseInt(entry.getKey());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return 0;
    }

    // -------------------------------------------------------------------//

    /**
     * 获取巅峰之战信息
     * 
     * @param userRoleId
     * @return
     */
    public Object[] getKuafuDianFengInfo() {
        Redis redis = GameServerContext.getRedis();
        if (redis == null) {
            ChuanQiLog.error("redis not open");
            return null;
        }
        List<Object[]> rsList = getDianFengAllDataVO(redis);
        return ObjectUtil.isEmpty(rsList) ? getDaLuanDouAllDataVO(redis, rsList) : rsList.toArray();
    }

    /**
     * 玩家请求进入巅峰之战
     * 
     * @param userRoleId
     * @param busMsgQueue
     */
    public Object[] enterKuafuDianFeng(Long userRoleId) {
        int curLoop = getDianFengCurLoop(GameSystemTime.getSystemMillTime(), 0);
        if (0 >= curLoop || curLoop > KuafuDianFengUtils.getMaxLoop()) {
            return AppErrorCode.KF_DIANFENG_NOT_ENTER;
        }
        // 判断是否在副本中
        if (stageControllExportService.inFuben(userRoleId)) {
            return AppErrorCode.FUBEN_IS_IN_FUBEN;
        }
        Redis redis = GameServerContext.getRedis();
        if (redis == null) {
            ChuanQiLog.error("redis not open");
            return AppErrorCode.CONFIG_ERROR;
        }
        // 判断跨服是否有效
        String serverId = KuafuDianFengUtils.getDianFengKuaFuServerId();
        if (ObjectUtil.strIsEmpty(serverId)) {
            return AppErrorCode.KUAFU_NO_CONNECTION;
        }

        int room = getDianFengRoomOnLoop(userRoleId, redis, curLoop);
        if (0 >= room) {
            return AppErrorCode.KF_DIANFENG_NOT_ROOM;
        }
        // 比赛已经结果已经出来,无法中途进去
        if (!checkEnterDianFeng(userRoleId, redis, curLoop, room)) {
            return AppErrorCode.KF_DIANFENG_ENTER_ROOM_FAIL;
        }
        return readyEnterDianFeng(userRoleId, redis, serverId, curLoop, room);
    }

    /**
     * 检查玩家是否可以进入场景 房间数据中产生了胜利的玩家=房间分出胜负,比赛已经结束了
     * 
     * @param userRoleId
     * @param redis
     * @param loop
     * @param room
     * @return true=可以;false=不可以
     */
    private boolean checkEnterDianFeng(Long userRoleId, Redis redis, int loop, int room) {
        Map<String, String> map = redis.hgetAll(RedisKey.getRedisKfDianFengRoomKey(loop));
        if (ObjectUtil.isEmpty(map)) {
            return false;
        }
        String voJSONString = map.get(String.valueOf(room));
        if (ObjectUtil.strIsEmpty(voJSONString)) {
            return false;
        }
        try {
            List<DianFengPlayerVO> voList = JSON.parseArray(voJSONString, DianFengPlayerVO.class);
            if (ObjectUtil.isEmpty(voList)) {
                return false;
            }
            for (DianFengPlayerVO vo : voList) {
                if (null == vo.getArenaResult() || KuaFuDianFengConstants.STATE_NOT != vo.getArenaResult().intValue()) {
                    return false;
                }
            }
        } catch (Exception e) {
            ChuanQiLog.error("deserialize dianfeng vo data error,data:{}", voJSONString);
        }

        return true;
    }

    /**
     * 进入巅峰之战
     * 
     * @param userRoleId
     * @param redis
     * @param serverId 跨服服务器编号
     * @param loop 轮次
     * @param room 房间
     * @return
     */
    private Object[] readyEnterDianFeng(Long userRoleId, Redis redis, String serverId, int loop, int room) {
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
            redis.hset(errorKey, ChuanQiConfigUtil.getServerId(), String.valueOf(1));
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
        KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID, userRoleId, InnerCmdType.BIND_ROLE_ID_SERVERID, new Object[] { ChuanQiConfigUtil.getServerId(), userRoleId });
        // 转成跨服roleData数据
        Object roleData = RoleFactory.createKuaFuRoleData(role);
        // 向跨服发送进入房间的玩家数据
        KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID, userRoleId, InnerCmdType.KUAFU_DIANFENG_SEND_ROLE_DATA, new Object[] { loop, room, userRoleId, new Object[] { roleData, userRoleId } });
        return null;
    }

    /**
     * 巅峰之战玩家进入小黑屋
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
     * 跨服巅峰之战请求退出场景
     * 
     * @param userRoleId
     */
    public void exitDianFengStage(Long userRoleId) {
        // 更新redis中战斗数据
        KuafuManager.removeKuafu(userRoleId);
        BusMsgSender.send2BusInner(userRoleId, InnerCmdType.S_APPLY_LEAVE_STAGE, null);
        stageControllExportService.changeFuben(userRoleId, false);
        KuafuRoleServerManager.getInstance().removeBind(userRoleId);
    }

    // -------------------------------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------------------------------

    /**
     * key=loop+room</br>
     * value=</br>
     * [</br>
     *  userRoleId:角色id
     *  sourceId:角色所在源服serverId
     *  isOnline:角色是否在线true=在线
     * ]
     */
    private static Map<String, List<Object[]>> dianFengDataMap = new ConcurrentHashMap<String, List<Object[]>>();
    private static int roomMaxRoleCount = KuaFuDaLuanDouConstants.KUAFU_LUANDOU_FANGJIAN_RANK;

    private String dianFengMapKey(int loop, int room) {
        return "loop:" + loop + ".room:" + room;
    }

    /**
     * 根据轮次和房间获取玩家的巅峰之战vo数据
     * 
     * @param userRoleId
     * @param loop
     * @param room
     * @return
     */
    private DianFengPlayerVO getDianFengDataByLoopAndRoom(long userRoleId, int loop, int room, Redis redis) {
        Map<String, String> map = redis.hgetAll(RedisKey.getRedisKfDianFengRoomKey(loop));
        if (!ObjectUtil.isEmpty(map)) {
            String voJSONString = map.get(String.valueOf(room));
            if (!ObjectUtil.strIsEmpty(voJSONString)) {
                try {
                    List<DianFengPlayerVO> voList = JSON.parseArray(voJSONString, DianFengPlayerVO.class);
                    if (!ObjectUtil.isEmpty(voList)) {
                        for (DianFengPlayerVO vo : voList) {
                            if (vo.getUserRoleId().equals(userRoleId)) {
                                return vo;
                            }
                        }
                    }
                } catch (Exception e) {
                    ChuanQiLog.error("deserialize dianfeng vo data error,data:{}", voJSONString);
                }
            }
        }
        return null;
    }

    /**
     * 解析生成巅峰之战vo数据
     * @param jsonData
     * @param redis
     * @return
     */
    private DianFengPlayerVO createDianFengVo(String jsonData, Redis redis){
        DianFengPlayerVO vo = null;
        if(!ObjectUtil.strIsEmpty(jsonData)){
            try {
                vo = JSON.parseObject(jsonData, DianFengPlayerVO.class);
            } catch (Exception e) {
                ChuanQiLog.error("deserialize dianfeng vo data error,data={}", jsonData);
            }
        }
        return vo;
    }
    
    /**
     * 插入redis巅峰之战数据
     * 
     * @param loop
     * @param room
     * @param vo
     * @param redis
     */
    private void insertRedisData(int loop, int room, DianFengPlayerVO vo, Redis redis) {
        if(null == vo) return;
        List<DianFengPlayerVO> voList = null;
        String voJSON = redis.hget(RedisKey.getRedisKfDianFengRoomKey(loop), String.valueOf(room));
        if (ObjectUtil.strIsEmpty(voJSON)) {
            voList = new ArrayList<>();
        } else {
            try {
                voList = JSON.parseArray(voJSON, DianFengPlayerVO.class);
            } catch (Exception e) {
                ChuanQiLog.error("deserialize dianfeng vo data error,data:{}", voJSON);
            }
        }
        voList.add(vo);
        redis.hset(RedisKey.getRedisKfDianFengRoomKey(loop), String.valueOf(room), JSON.toJSONString(voList));
        ChuanQiLog.info("******KuafuDianFeng******kuafu server save redis data, loopRooms={}, room={}, data={}, resultListData={}******KuafuDianFeng******",  RedisKey.getRedisKfDianFengRoomKey(loop) , String.valueOf(room) , JSON.toJSONString(vo), JSON.toJSONString(voList));
        noticeDataRefresh();
    }

    /**
     *  插入玩家进入下一轮比赛房间数据
     * @param loop  当前轮次
     * @param room  当前房间
     * @param winVo 获胜玩家vo数据
     * @param redis
     */
    private  void insertNextRedisData(int loop, int room, DianFengPlayerVO winVo, Redis redis) {
        if(null == winVo) return;
        int nextLoop = loop + 1;
        if (0 >= nextLoop || nextLoop > KuafuDianFengUtils.getMaxLoop()) {
            return;
        }
        int nextRoom = getOtherLoopRoomByBeforeRoom(room, nextLoop);
        if (0 >= nextRoom) {
            return;
        }
        List<DianFengPlayerVO> voList = null;
        String voJSON = redis.hget(RedisKey.getRedisKfDianFengRoomKey(nextLoop), String.valueOf(nextRoom));
        if (ObjectUtil.strIsEmpty(voJSON)) {
            voList = new ArrayList<>();
        } else {
            try {
                voList = JSON.parseArray(voJSON, DianFengPlayerVO.class);
            } catch (Exception e) {
                ChuanQiLog.error("deserialize dianfeng vo data error,data:{}", voJSON);
            }
        }
        DianFengPlayerVO vo = new DianFengPlayerVO(getOtherLoopGroupName(nextLoop, room, nextRoom), winVo.getUserRoleId(), winVo.getRoleConfigId(), winVo.getNickName(), winVo.getLevel(), winVo.getServerId(), winVo.getZhanli(), KuaFuDianFengConstants.STATE_NOT);
        voList.add(vo);
        redis.hset(RedisKey.getRedisKfDianFengRoomKey(nextLoop), String.valueOf(nextRoom), JSON.toJSONString(voList));
        ChuanQiLog.info("******KuafuDianFeng******kuafu server save redis data, loopRooms={}, room={}, data={}, resultListData={}******KuafuDianFeng******",  RedisKey.getRedisKfDianFengRoomKey(loop) , String.valueOf(room) , JSON.toJSONString(vo), JSON.toJSONString(voList));
        noticeDataRefresh();
    }
    
    /**
     * 修改redis数据 
     * @param loop
     * @param room
     * @param voJsonString
     * @param redis
     */
    private void modifyRedisData(int loop, int room, String voJsonString, Redis redis){
        if(ObjectUtil.strIsEmpty(voJsonString)){
            return;
        }
        redis.hset(RedisKey.getRedisKfDianFengRoomKey(loop), String.valueOf(room), voJsonString);
    }
    
    // 通知数据刷新
    private void noticeDataRefresh() {
        KuafuMsgSender.send2AllKuafuSource(ClientCmdType.KUAFU_DIANFENG_GET_INFO, getKuafuDianFengInfo());
    }

    /**
     * 更新巅峰之战比赛结果数据
     * @param loop 轮次
     * @param room 房间
     * @param winRoleId 获胜玩家
     */
    public void updateDianFengRedisData(int loop, int room, long winRoleId) {
        // 巅峰之战指定的跨服才执行
        String kfDianFengServerId = KuafuDianFengUtils.getDianFengKuaFuServerId();
        if (!ChuanQiConfigUtil.getServerId().equals(kfDianFengServerId)) {
            return;
        }
        Redis redis = GameServerContext.getRedis();
        if (redis == null) {
            ChuanQiLog.error("redis 没有开启");
            return;
        }
        Map<String, String> map = redis.hgetAll(RedisKey.getRedisKfDianFengRoomKey(loop));
        if (!ObjectUtil.isEmpty(map)) {
            String voJSONString = map.get(String.valueOf(room));
            if (!ObjectUtil.strIsEmpty(voJSONString)) {
                try {
                    List<DianFengPlayerVO> voList = JSON.parseArray(voJSONString, DianFengPlayerVO.class);
                    if (!ObjectUtil.isEmpty(voList)) {
                        DianFengPlayerVO winVo = null;
                        for (DianFengPlayerVO vo : voList) {
                            if (vo.getUserRoleId().equals(winRoleId)) {
                                winVo = vo;
                                vo.setArenaResult(KuaFuDianFengConstants.STATE_WIN);
                            }else{
                                vo.setArenaResult(KuaFuDianFengConstants.STATE_LOSE);
                            }
                        }
                        modifyRedisData( loop, room, JSON.toJSONString(voList), redis);
                        ChuanQiLog.info("******KuafuDianFeng******kuafu server update loop kuafudianfeng result data, serverId={}, loop={}, room={}, data={}******KuafuDianFeng******", kfDianFengServerId, loop, room, JSON.toJSONString(voList));
                        insertNextRedisData(loop, room, winVo, redis);
                    }
                } catch (Exception e) {
                    ChuanQiLog.error("deserialize dianfeng vo data error,data:{}", voJSONString);
                }
            }
        }
    }

   /**
    * 插入巅峰之战数据 
    * @param userRoleId
    * @param loop
    * @param room
    * @param jsonData
    * @param isToNext
    */
    public void insertDianFengData(long userRoleId, int loop, int room, String jsonData, boolean isToNext) {
        // 巅峰之战指定的跨服才执行
        String kfDianFengServerId = KuafuDianFengUtils.getDianFengKuaFuServerId();
        if (!ChuanQiConfigUtil.getServerId().equals(kfDianFengServerId)) {
            return;
        }
        Redis redis = GameServerContext.getRedis();
        if (redis == null) {
            ChuanQiLog.error("redis 没有开启");
            return;
        }
        DianFengPlayerVO oldVo = getDianFengDataByLoopAndRoom(userRoleId, loop, room, redis);
        if(null != oldVo){
            return;
        }
        ChuanQiLog.info("******KuafuDianFeng******kuafu server insert result data to redis, serverId={}, userRoleId={}, loop={}, room={}, result={}, isToNext={}******KuafuDianFeng******", kfDianFengServerId, userRoleId, loop, room, jsonData, isToNext);
        DianFengPlayerVO vo = createDianFengVo(jsonData, redis);
        insertRedisData(loop, room, vo, redis);
        if(isToNext){
            insertNextRedisData(loop, room, vo, redis);
        }
    }

    /**
     * 保存巅峰之战数据 
     * @param userRoleId
     * @param sourceServiceId
     * @param loop
     * @param room
     * @param isOnline 源服在线状态
     */
    public void saveDianFengData(long userRoleId, String sourceServiceId, int loop, int room, boolean isOnline) {
        // 巅峰之战指定的跨服才执行
        String kfDianFengServerId = KuafuDianFengUtils.getDianFengKuaFuServerId();
        if (!ChuanQiConfigUtil.getServerId().equals(kfDianFengServerId)) {
            return;
        }
        Redis redis = GameServerContext.getRedis();
        if (redis == null) {
            ChuanQiLog.error("redis not open");
            return;
        }
        DianFengPlayerVO vo = getDianFengDataByLoopAndRoom(userRoleId, loop, room, redis);
        if (null == vo) {
            return;
        }
        String mapKey = dianFengMapKey(loop, room);
        List<Object[]> mapData = dianFengDataMap.get(mapKey);
        if (null == mapData) {
            mapData = new ArrayList<Object[]>();
        }
        mapData.add(new Object[] { userRoleId, sourceServiceId, isOnline });
        dianFengDataMap.put(mapKey, mapData);
        ChuanQiLog.info("******KuafuDianFeng******save source server kuafudianfeng data to kuafu server, kuafu serverId={}, source serverId={}, userRoleId={}, loop={}, room={}, isOnline={}******KuafuDianFeng******", kfDianFengServerId, sourceServiceId, userRoleId, loop, room, isOnline);
        if (mapData.size() >= roomMaxRoleCount) {
            Map<Long, String> onLineRolesMap = new HashMap<>();
            Long[] roleIds = new Long[roomMaxRoleCount];
            for (int idx = 0; idx < roomMaxRoleCount; idx++) {
                Object[] data = mapData.get(idx);
                long roleId = CovertObjectUtil.obj2long(data[0]);
                String sourceSid = CovertObjectUtil.object2String(data[1]);
                boolean online = CovertObjectUtil.object2boolean(data[2]);
                roleIds[idx] = roleId;
                if (online)
                    onLineRolesMap.put(roleId, sourceSid);
            }
            // 双方都不在线,系统直接出结果
            if (ObjectUtil.isEmpty(onLineRolesMap)) {
                boolean redWin = false;// 红方获胜
                DianFengPlayerVO winVo = null;// 获胜对象
                // 红方
                DianFengPlayerVO redVo = getDianFengDataByLoopAndRoom(roleIds[0], loop, room, redis);
                long redZhanli = redVo.getZhanli();
                // 蓝方
                DianFengPlayerVO blueVo = getDianFengDataByLoopAndRoom(roleIds[1], loop, room, redis);
                long blueZhanli = blueVo.getZhanli();

                // 比拼结果
                if (redZhanli > blueZhanli) {
                    redWin = true;
                } else if (redZhanli == blueZhanli && Lottery.roll(KuaFuDianFengConstants.ZHANLI_RATE, Lottery.HUNDRED)) {
                    redWin = true;
                }
                List<DianFengPlayerVO> voList = new ArrayList<>();
                // 更新数据
                if (redWin) {
                    winVo = redVo;
                    redVo.setArenaResult(KuaFuDianFengConstants.STATE_WIN);
                    blueVo.setArenaResult(KuaFuDianFengConstants.STATE_LOSE);
                } else {
                    winVo = blueVo;
                    blueVo.setArenaResult(KuaFuDianFengConstants.STATE_WIN);
                    redVo.setArenaResult(KuaFuDianFengConstants.STATE_LOSE);
                }
                voList.add(redVo);
                voList.add(blueVo);
                modifyRedisData( loop, room, JSON.toJSONString(voList), redis);
                insertNextRedisData(loop, room, winVo, redis);
            } else {
                for (long roleId : roleIds) {
                    if (onLineRolesMap.containsKey(roleId)) {
                        KuafuMsgSender.send2KuafuSource(onLineRolesMap.get(roleId), InnerCmdType.KUAFU_DIANFENG_NOTICE, new Object[] { roleId, loop });
                        ChuanQiLog.info("******KuafuDianFeng*******notice source server role join stage,source serverId={}, userRoleId={}, loop={}, room={}******KuafuDianFeng*******", onLineRolesMap.get(roleId), roleId, loop, room);
                    }
                }
            }
            dianFengDataMap.remove(mapKey);
        }

    }
    

}
