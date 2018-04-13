package com.junyou.bus.kuafu_yungong.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.kuafu_yungong.service.KuafuYunGongService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.module.GameModType;
import com.junyou.utils.number.LongUtils;
import com.junyou.utils.parameter.RequestParameterUtil;
import com.kernel.pool.executor.Message;

/**
 * 
 *@Description 跨服-云宫之巅
 *@Author Yang Gao
 *@Since 2016-9-18
 *@Version 1.1.0
 */
@Controller
@EasyWorker(moduleName = GameModType.KUAFU_YUNGONG, groupName = EasyGroup.BUS_CACHE)
public class KuafuYunGongAction {

    @Autowired
    private KuafuYunGongService kuafuYunGongService;
    
    // --------------------------------------源服到源服处理的指令---------------------------------------
    @EasyMapping(mapping = InnerCmdType.KUAFU_YUNGONG_READY)
    public void activityReady(Message inMsg) {
        kuafuYunGongService.activityReadyJob();
    }
    
    @EasyMapping(mapping = InnerCmdType.KUAFU_YUNGONG_START)
    public void activityStart(Message inMsg) {
        kuafuYunGongService.activityStart();
    }
    
    @EasyMapping(mapping = InnerCmdType.KUAFU_YUNGONG_END)
    public void activityEnd(Message inMsg) {
        kuafuYunGongService.activityEnd();
    }
    
    /**
     * 
     * 4531 请求跨服云宫之巅活动结果数据
     * @param inMsg
     */
    @EasyMapping(mapping = ClientCmdType.KF_YUNGONG_RESULT_INFO)
    public void kuafuYunGongGetResultInfo(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        Object[] result = kuafuYunGongService.kfYunGongGetWinInfo(userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.KF_YUNGONG_RESULT_INFO, result);
    }
    
    /**
     * 
     * 4533 请求领取活动奖励
     * @param inMsg
     */
    @EasyMapping(mapping = ClientCmdType.KF_YUNGONG_RECEIVE)
    public void kuafuYunGongReceiveReward(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        Object[] result = kuafuYunGongService.kfYunGongReceiveReward(userRoleId);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.KF_YUNGONG_RECEIVE, result);
        }
    }
    
    // --------------------------------------------源服转到跨服处理的指令--------------------------------
    
    /**
     * 
     * 4532 请求进入活动场景
     * @param inMsg
     */
    @EasyMapping(mapping = ClientCmdType.KF_YUNGONG_ENTER)
    public void kuafuYunGongEnter(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        Object[] result = kuafuYunGongService.kfYunGongEnter(userRoleId);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.KF_YUNGONG_ENTER, result);
        }
    }
    
    /**
     * 
     * 4534 请求采集旗子
     * @param inMsg
     */
    @EasyMapping(mapping = ClientCmdType.KF_YUNGONG_COLLECT)
    public void kuafuYunGongCollect(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        Long qiziGuid = LongUtils.obj2long(inMsg.getData());
        kuafuYunGongService.kfYunGongCollect(userRoleId,qiziGuid);
    }
    
    /**
     * 
     * 4534 请求拔起旗子
     * @param inMsg
     */
    @EasyMapping(mapping = ClientCmdType.KF_YUNGONG_PULL)
    public void kuafuYunGongPull(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        kuafuYunGongService.kfYunGongPull(userRoleId);
    }
    
    /**
     * 
     * 4536 请求复活
     * @param inMsg
     */
    @EasyMapping(mapping = ClientCmdType.KF_YUNGONG_FUOHUO)
    public void kuafuYunGongFuhuo(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        kuafuYunGongService.kfYunGongFuhuo(userRoleId);
    }
    
    /**
     * 
     * 4541 请求退出活动场景
     * @param inMsg
     */
    @EasyMapping(mapping = ClientCmdType.KF_YUNGONG_EXIT)
    public void kuafuYunGongExit(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        kuafuYunGongService.kfYunGongExit(userRoleId);
    }
    
    
    // --------------------------------------------跨服转到源服处理的指令--------------------------------
    @EasyMapping(mapping = InnerCmdType.KUAFU_YUNGONG_ENTER_FAIL, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
    public void kuafuYunGongEnterFail(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        BusMsgSender.send2One(userRoleId, ClientCmdType.KF_YUNGONG_ENTER, null == inMsg.getData() ? AppErrorCode.KUAFU_ENTER_FAIL : (Object[]) inMsg.getData());
    }
    
    @EasyMapping(mapping = InnerCmdType.KUAFU_YUNGONG_ENTER_XIAOHEIWU, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
    public void enterXiaoheiwu(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        kuafuYunGongService.enterXiaoheiwu(userRoleId);
    }
    
    @EasyMapping(mapping = InnerCmdType.KUAFU_YUNGONG_EXIT_XIAOHEIWU, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
    public void exitXiaoheiwu(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        kuafuYunGongService.exitXiaoheiwu(userRoleId);
    }
    
    @EasyMapping(mapping = InnerCmdType.KUAFU_YUNGONG_QIZI_CONSUME, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
    public void kuafuYunGongQiziConsume(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = kuafuYunGongService.pullQiziConsume(userRoleId);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.KF_YUNGONG_PULL, result);
        }
    }
    
    @EasyMapping(mapping = InnerCmdType.KUAFU_YUNGONG_UPDATE_RESULT, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
    public void kuafuYunGongUpdateResult(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] data = inMsg.getData();
        String kfServerId = RequestParameterUtil.object2String(data[0]);
        Long winGuildId = RequestParameterUtil.object2Long(data[1]);
        kuafuYunGongService.updateResultData(userRoleId,kfServerId, winGuildId);
    }
    
    @EasyMapping(mapping = InnerCmdType.KUAFU_YUNGONG_SEND_EMAIL_REWARD, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
    public void kuafuYunGongSendEmailReward(Message inMsg) {
        Object[] data = (Object[]) inMsg.getData();
        Long userRoleId = LongUtils.obj2long(data[0]);
        Integer rewardType = (Integer) data[1];
        kuafuYunGongService.kuafuYunGongSendEmailReward(userRoleId, rewardType);
    }
    
    @EasyMapping(mapping = InnerCmdType.KUAFU_YUNGONG_ACTIVITY_END, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
    public void kuafuYunGongActivityEnd(Message inMsg) {
        kuafuYunGongService.kuafuActivityEnd();
    }
    
    
    
}
