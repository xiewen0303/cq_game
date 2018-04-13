package com.junyou.bus.platform.qq.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.platform.qq.service.QqGameService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 * @Description QQ游戏大厅特权礼包
 * @Author Yang Gao
 * @Since 2016-7-7
 * @Version 1.1.0
 */
@Controller
@EasyWorker(moduleName = GameModType.QQ_PLATFORM_MODULE)
public class QqGameAction {
    @Autowired
    private QqGameService qqGameService;

    @EasyMapping(mapping = ClientCmdType.QQGAME_XINSHOU_INFO)
    public void getQqGameXinShouInfo(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = qqGameService.getXinShouInfo(userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.QQGAME_XINSHOU_INFO, result);
    }

    @EasyMapping(mapping = ClientCmdType.QQGAME_XINSHOU_GIFT)
    public void receiveQqGameXinShouGift(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = qqGameService.receiveXinShouGift(userRoleId);
        if (null != result) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.QQGAME_XINSHOU_GIFT, result);
        }
    }

    @EasyMapping(mapping = ClientCmdType.QQGAME_LEVEL_INFO)
    public void getQqGameLevelInfo(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = qqGameService.getLevelInfo(userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.QQGAME_LEVEL_INFO, result);
    }

    @EasyMapping(mapping = ClientCmdType.QQGAME_LEVEL_GIFT)
    public void receiveQqGameLevelGift(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Integer level = (Integer) inMsg.getData();
        Object[] result = qqGameService.receiveLevelGift(userRoleId, level);
        if (null != result) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.QQGAME_LEVEL_GIFT, result);
        }
    }

    @EasyMapping(mapping = ClientCmdType.QQGAME_EVERY_INFO)
    public void getQqGameEveryInfo(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = qqGameService.getEveryInfo(userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.QQGAME_EVERY_INFO, result);
    }

    @EasyMapping(mapping = ClientCmdType.QQGAME_EVERY_GIFT)
    public void receiveQqGameEveryGift(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = qqGameService.receiveEveryGift(userRoleId);
        if (null != result) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.QQGAME_EVERY_GIFT, result);
        }
    }

}
