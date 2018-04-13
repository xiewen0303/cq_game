package com.junyou.bus.platform.qq.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.platform.qq.service.QqQzoneService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

/**
 *@Description QQ空间特权礼包
 *@Author Yang Gao
 *@Since 2016-7-19
 *@Version 1.1.0
 */
@Controller
@EasyWorker(moduleName = GameModType.QQ_PLATFORM_MODULE)
public class QqQzoneAction {
    @Autowired
    private QqQzoneService qqQzoneService;

    @EasyMapping(mapping = ClientCmdType.QqQzone_XINSHOU_INFO)
    public void getQqQzoneXinShouInfo(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = qqQzoneService.getXinShouInfo(userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.QqQzone_XINSHOU_INFO, result);
    }

    @EasyMapping(mapping = ClientCmdType.QqQzone_XINSHOU_GIFT)
    public void receiveQqQzoneXinShouGift(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = qqQzoneService.receiveXinShouGift(userRoleId);
        if (null != result) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.QqQzone_XINSHOU_GIFT, result);
        }
    }

    @EasyMapping(mapping = ClientCmdType.QqQzone_LEVEL_INFO)
    public void getQqQzoneLevelInfo(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = qqQzoneService.getLevelInfo(userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.QqQzone_LEVEL_INFO, result);
    }

    @EasyMapping(mapping = ClientCmdType.QqQzone_LEVEL_GIFT)
    public void receiveQqQzoneLevelGift(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Integer level = (Integer) inMsg.getData();
        Object[] result = qqQzoneService.receiveLevelGift(userRoleId, level);
        if (null != result) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.QqQzone_LEVEL_GIFT, result);
        }
    }

    @EasyMapping(mapping = ClientCmdType.QqQzone_EVERY_INFO)
    public void getQqQzoneEveryInfo(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = qqQzoneService.getEveryInfo(userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.QqQzone_EVERY_INFO, result);
    }

    @EasyMapping(mapping = ClientCmdType.QqQzone_EVERY_GIFT)
    public void receiveQqQzoneEveryGift(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = qqQzoneService.receiveEveryGift(userRoleId);
        if (null != result) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.QqQzone_EVERY_GIFT, result);
        }
    }

}
