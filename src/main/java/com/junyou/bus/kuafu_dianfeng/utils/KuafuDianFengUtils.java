package com.junyou.bus.kuafu_dianfeng.utils;

import java.util.Set;

import com.junyou.bus.kuafu_dianfeng.constants.KuaFuDianFengConstants;
import com.junyou.context.GameServerContext;
import com.junyou.kuafu.manager.KuafuRoleServerManager;
import com.junyou.kuafu.manager.KuafuServerInfoManager;
import com.junyou.kuafu.share.util.KuafuConfigUtil;
import com.junyou.kuafu.share.util.KuafuServerInfo;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.ObjectUtil;
import com.kernel.cache.redis.Redis;
import com.kernel.cache.redis.RedisKey;


/**
 *@Description  巅峰之战工具类
 *@Author Yang Gao
 *@Since 2016-5-18
 *@Version 1.1.0
 */
public class KuafuDianFengUtils {

    
    /** 根据轮次获取最大房间 **/
    public static int getMaxRoomByLoop(int loop){
        int maxRoom = 0;
        switch (loop) {
        case KuaFuDianFengConstants.LOOP_ONE:
            maxRoom = KuaFuDianFengConstants.MAX_ROOM_LOOP_ONE;
            break;
        case KuaFuDianFengConstants.LOOP_TWO:
            maxRoom = KuaFuDianFengConstants.MAX_ROOM_LOOP_TWO;
            break;
        case KuaFuDianFengConstants.LOOP_THREE:
            maxRoom = KuaFuDianFengConstants.MAX_ROOM_LOOP_THREE;
            break;
        case KuaFuDianFengConstants.LOOP_FOUR:
            maxRoom = KuaFuDianFengConstants.MAX_ROOM_LOOP_FOUR;
            break;
        default:
            break;
        }
        return maxRoom;
    }
    
    //获取巅峰之战最大轮数
    public static int getMaxLoop(){
        return KuaFuDianFengConstants.LOOP_FOUR;
    }
    
    public static void bindDianFengKuaFuServerId(long userRoleId, String serverId, Redis redis){
        KuafuServerInfo kuafuServerInfo = KuafuServerInfoManager.getInstance().getKuafuServerInfo(serverId, redis);
        if (kuafuServerInfo == null) {
            return;
        }
        KuafuRoleServerManager.getInstance().bindServer(userRoleId, kuafuServerInfo);
    }
    
    //获取巅峰之战的跨服服务器信息
    public static String getDianFengKuaFuServerId() {
        Redis redis = GameServerContext.getRedis();
        if (null == redis) {
            ChuanQiLog.error("redis 没有开启");
            return null;
        }
        if (!KuafuConfigUtil.isKuafuAvailable()) {
            return null;
        }
        String serverId = redis.get(RedisKey.KUAFU_DIANFENG_SERVER_ID);
        if (ObjectUtil.strIsEmpty(serverId)) {
            Set<String> serverIdSets = redis.zrange(RedisKey.KUAFU_SERVER_LIST_KEY, 0, 0);
            if (null == serverIdSets || serverIdSets.size() == 0) {
                ChuanQiLog.error("dianfeng kuafu server not find");
                return null;
            } else {
                for (String e : serverIdSets) {
                    serverId = e;
                }
                redis.set(RedisKey.KUAFU_DIANFENG_SERVER_ID, serverId);
            }
        }
        return serverId;
    }
}
