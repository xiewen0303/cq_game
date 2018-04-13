package com.junyou.bus.wuxing.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.wuxing.service.WuXingMoShenService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

/**
 * 
 *@Description 五行系统
 *@Author Yang Gao
 *@Since 2016-6-7
 *@Version 1.1.0
 */
@Controller
@EasyWorker(moduleName = GameModType.WU_XING, groupName = EasyGroup.BUS_CACHE)
public class WuXingAction {

    @Autowired
    private WuXingMoShenService wuXingMoShenService;

    @EasyMapping(mapping = ClientCmdType.GET_WUXING_INFO)
    public void getWuXingInfo(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();

        Object[] result = wuXingMoShenService.getJiHuoWuXing(userRoleId);
        if (result != null) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.GET_WUXING_INFO, result);
        }
    }

    @EasyMapping(mapping = ClientCmdType.QINGQIU_WUXING_JIHUO)
    public void jihuoWuXing(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] obj = inMsg.getData();
        Integer type = (Integer) obj[0];
        boolean isAutoGM = (boolean) obj[1];
        Object[] result = wuXingMoShenService.jihuoWuXing(userRoleId, type, isAutoGM);
        BusMsgSender.send2One(userRoleId, ClientCmdType.QINGQIU_WUXING_JIHUO, result);
    }

    @EasyMapping(mapping = ClientCmdType.QINGQIU_WUXING_SHENGJI)
    public void sjWuXing(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] obj = inMsg.getData();
        Integer type = (Integer) obj[0];
        boolean isAutoGM = (boolean) obj[1];
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        Object[] result = wuXingMoShenService.sjWuXing(userRoleId, type, busMsgQueue, isAutoGM);
        BusMsgSender.send2One(userRoleId, ClientCmdType.QINGQIU_WUXING_SHENGJI, result);
        busMsgQueue.flush();

    }

    @EasyMapping(mapping = ClientCmdType.QINGQIU_WUXING_FUTI)
    public void ftWuXing(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Integer type = inMsg.getData();
        Object[] result = wuXingMoShenService.futi(userRoleId, type);
        BusMsgSender.send2One(userRoleId, ClientCmdType.QINGQIU_WUXING_FUTI, result);
    }

    @EasyMapping(mapping = ClientCmdType.QINGQIU_WUXING_ZFZ)
    public void getWxZfz(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Integer type = inMsg.getData();

        Object[] result = wuXingMoShenService.getZfz(userRoleId, type);
        if (result != null) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.QINGQIU_WUXING_ZFZ, result);
        }
    }

    // ------------------------------五行魔神技能------------------------------------//
    @EasyMapping(mapping = ClientCmdType.GET_WX_SKILL_INFO)
    public void getWxSkillInfo(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();

        Object[] result = wuXingMoShenService.getWxSkillInfo(userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.GET_WX_SKILL_INFO, result);
    }

    @EasyMapping(mapping = ClientCmdType.UPLEVEL_WX_SKILL)
    public void uplevelWxSkill(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        int skillId = CovertObjectUtil.object2int(inMsg.getData());
        Object[] result = wuXingMoShenService.upLevelWxSkill(userRoleId, skillId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.UPLEVEL_WX_SKILL, result);
    }

    // ------------------------------五行魔神精魄系统--------------------------------------//
    @EasyMapping(mapping = ClientCmdType.WX_JP_GET_BAG)
    public void getWxJpBagData(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = wuXingMoShenService.getRoleWxJpBagData(userRoleId);
        if (null != result) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.WX_JP_GET_BAG, result);
        }
    }

    @EasyMapping(mapping = ClientCmdType.WX_JP_GET_BODY)
    public void getWxJpBodyData(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        int wxtype = inMsg.getData();
        Object[] result = wuXingMoShenService.getRoleWxJpBodyData(userRoleId, wxtype);
        if (null != result) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.WX_JP_GET_BODY, result);
        }
    }

    @EasyMapping(mapping = ClientCmdType.WX_JP_OPEN_SLOT_BAG)
    public void openWxJpBagSlot(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        int slot = inMsg.getData();
        Object[] result = wuXingMoShenService.openWxJpBagSlot(userRoleId, slot);
        if (null != result) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.WX_JP_OPEN_SLOT_BAG, result);
        }
    }

    @EasyMapping(mapping = ClientCmdType.WX_JP_OPEN_SLOT_BODY)
    public void openWxJpBodySlot(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] data = inMsg.getData();
        int position = (Integer) data[0];
        int slot = (Integer) data[1];
        Object[] result = wuXingMoShenService.openWxJpBodySlot(userRoleId, position, slot);
        if (null != result) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.WX_JP_OPEN_SLOT_BODY, result);
        }
    }

    @EasyMapping(mapping = ClientCmdType.WX_JP_MERGE)
    public void mergeWxJpOnBag(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] data = inMsg.getData();
        long mergeGuid = LongUtils.obj2long(data[0]);
        Object[] beMergeGuids = (Object[]) data[1];
        boolean isAuto = (Boolean) data[2];
        Object[] result = wuXingMoShenService.mergeWxJpOnBag(userRoleId, mergeGuid, beMergeGuids, isAuto);
        if (null != result) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.WX_JP_MERGE, result);
        }
    }

    @EasyMapping(mapping = ClientCmdType.WX_JP_PUT_ON)
    public void putOnWxJp(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] data = inMsg.getData();
        long sourceGuid = LongUtils.obj2long(data[0]);
        int targetPosition = (Integer) data[1];
        int targetSlot = (Integer) data[2];
        Object[] result = wuXingMoShenService.putOnWxJp(userRoleId, sourceGuid, targetPosition, targetSlot);
        if (null != result) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.WX_JP_PUT_ON, result);
        }
    }

    @EasyMapping(mapping = ClientCmdType.WX_JP_TAKE_OFF)
    public void takeOffWxJp(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        long sourceGuid = LongUtils.obj2long(inMsg.getData());
        Object[] result = wuXingMoShenService.takeOffWxJp(userRoleId, sourceGuid);
        if (null != result) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.WX_JP_TAKE_OFF, result);
        }
    }

    @EasyMapping(mapping = ClientCmdType.WX_JP_GET_NORMAL)
    public void normalGetWxJp(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = wuXingMoShenService.normalGetWxJp(userRoleId);
        if (null != result) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.WX_JP_GET_NORMAL, result);
        }
    }

    @EasyMapping(mapping = ClientCmdType.WX_JP_KTTJ)
    public void kttjWxJp(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = wuXingMoShenService.kttjWxJp(userRoleId);
        if (null != result) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.WX_JP_KTTJ, result);
        }
    }

    @EasyMapping(mapping = ClientCmdType.WX_JP_GET_AUTO)
    public void autoGetWxJp(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = wuXingMoShenService.autoGetWxJp(userRoleId);
        if (null != result) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.WX_JP_GET_AUTO, result);
        }
    }

    @EasyMapping(mapping = ClientCmdType.WX_JP_EXCHANGE)
    public void exchangeWxJp(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        int dhId = (Integer) inMsg.getData();
        Object[] result = wuXingMoShenService.exchangeWxJp(userRoleId, dhId);
        if (null != result) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.WX_JP_EXCHANGE, result);
        }
    }

    // ------------------------------糖宝五行魔神系统--------------------------------------//
    @EasyMapping(mapping = ClientCmdType.GET_TB_WUXING_INFO)
    public void getTbWuXingInfo(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = wuXingMoShenService.getTbWuXingInfo(userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.GET_TB_WUXING_INFO, result);
    }

    @EasyMapping(mapping = ClientCmdType.QINGQIU_TB_WUXING_JIHUO)
    public void jihuoTbWuXing(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] obj = inMsg.getData();
        Integer type = (Integer) obj[0];
        boolean isAutoGM = (boolean) obj[1];
        Object[] result = wuXingMoShenService.jihuoTbWuXing(userRoleId, type, isAutoGM);
        BusMsgSender.send2One(userRoleId, ClientCmdType.QINGQIU_TB_WUXING_JIHUO, result);
    }

    @EasyMapping(mapping = ClientCmdType.QINGQIU_TB_WUXING_SHENGJI)
    public void sjTbWuXing(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] obj = inMsg.getData();
        Integer type = (Integer) obj[0];
        boolean isAutoGM = (boolean) obj[1];
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        Object[] result = wuXingMoShenService.sjTbWuXing(userRoleId, type, busMsgQueue, isAutoGM);
        BusMsgSender.send2One(userRoleId, ClientCmdType.QINGQIU_TB_WUXING_SHENGJI, result);
        busMsgQueue.flush();

    }

    @EasyMapping(mapping = ClientCmdType.QINGQIU_TB_WUXING_ZFZ)
    public void getTbWxZfz(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Integer type = inMsg.getData();
        Object[] result = wuXingMoShenService.getTbWxZfz(userRoleId, type);
        if (result != null) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.QINGQIU_TB_WUXING_ZFZ, result);
        }
    }
    
    // ------------------------------糖宝五行魔神技能------------------------------------//
    @EasyMapping(mapping = ClientCmdType.GET_TB_WX_SKILL_INFO)
    public void getTbWxSkillInfo(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = wuXingMoShenService.getTbWxSkillInfo(userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.GET_TB_WX_SKILL_INFO, result);
    }

    @EasyMapping(mapping = ClientCmdType.UPLEVEL_TB_WX_SKILL)
    public void uplevelTbWxSkill(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Integer skillId = inMsg.getData();
        Object[] result = wuXingMoShenService.upLevelTbWxSkill(userRoleId, skillId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.UPLEVEL_TB_WX_SKILL, result);
    }
}
