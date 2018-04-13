package com.junyou.bus.lunpan.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.lunpan.server.LunPanService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * 
 *@Description 充值轮盘热发布活动
 *@Author Yang Gao
 *@Since 2016-6-4
 *@Version 1.1.0
 */
@Controller
@EasyWorker(moduleName = GameModType.ACTIVITY)
public class LunPanAction {

    @Autowired
    private LunPanService lunPanService;

    /**
     * 请求转
     * 
     * @param inMsg
     */
    @EasyMapping(mapping = ClientCmdType.LUNPAN_ZHUAN)
    public void buy(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] data = inMsg.getData();
        Integer subId = (Integer) data[0];
        Integer version = (Integer) data[1];
        Boolean is = (Boolean) data[2];

        BusMsgQueue busMsgQueue = new BusMsgQueue();
        Object[] result = lunPanService.zhuan(userRoleId, version, subId, busMsgQueue, is);
        if (result != null) {// 返回null,则版本号不一致，处理配置数据
            BusMsgSender.send2One(userRoleId, ClientCmdType.LUNPAN_ZHUAN, result);
        }
        busMsgQueue.flush();
    }

    /**
     * 请求兑换
     * 
     * @param inMsg
     */
    @EasyMapping(mapping = ClientCmdType.LUNPAN_DUIHUAN)
    public void shuaxin(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] data = inMsg.getData();
        Integer subId = (Integer) data[0];
        Integer configId = (Integer) data[1];
        Integer version = (Integer) data[2];
        Object[] result = lunPanService.duihuan(userRoleId, version, subId, configId);
        if (result != null) {// 返回null,则版本号不一致，处理配置数据
            BusMsgSender.send2One(userRoleId, ClientCmdType.LUNPAN_DUIHUAN, result);
        }
    }

    /**
     * 请求轮盘抽奖数据
     * 
     * @param inMsg
     */
    @EasyMapping(mapping = ClientCmdType.LUNPAN_GET_INFO)
    public void getLunpanInfo(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Integer subId = (Integer) inMsg.getData();
        Object[] result = lunPanService.getLunpanInfo(userRoleId, subId);
        if (null != result) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.LUNPAN_GET_INFO, result);
        }
    }

}
