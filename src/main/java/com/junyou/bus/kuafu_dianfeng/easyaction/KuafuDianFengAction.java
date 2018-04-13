package com.junyou.bus.kuafu_dianfeng.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.kuafu_dianfeng.service.KuafuDianFengService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.module.GameModType;
import com.junyou.utils.common.CovertObjectUtil;
import com.kernel.pool.executor.Message;

/**
 *@Description  巅峰之战action
 *@Author Yang Gao
 *@Since 2016-5-18
 *@Version 1.1.0
 */

@Controller
@EasyWorker(moduleName = GameModType.KUAFU_DIANFENG, groupName = EasyGroup.BUS_CACHE)
public class KuafuDianFengAction {
    
    
	@Autowired
	private KuafuDianFengService kuafuDianFengService;

	// ------------------------------------------源服处理,不需要跨服处理的指令begin-----------------------------------
	@EasyMapping(mapping = ClientCmdType.KUAFU_DIANFENG_GET_INFO)
	public void getKuafuDianFengInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] res = kuafuDianFengService.getKuafuDianFengInfo();
	    BusMsgSender.send2One(userRoleId, ClientCmdType.KUAFU_DIANFENG_GET_INFO,res);
	}
	
    @EasyMapping(mapping = ClientCmdType.ENTER_KUAFU_DIANFENG)
    public void enterKuafuDianFeng(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] res = kuafuDianFengService.enterKuafuDianFeng(userRoleId);
        if (res != null) {
            BusMsgSender.send2One(userRoleId, ClientCmdType.ENTER_KUAFU_DIANFENG, res);
        }
    }
    
    // ------------------------------------------源服处理,不需要跨服处理的指令end-----------------------------------

    // ------------------------------------------源服发送到的跨服指令begin------------------------------------------
    @EasyMapping(mapping = InnerCmdType.KUAFU_DIANFENG_SAVE_DATA, kuafuType = EasyKuafuType.DIRECT_SWAP_TYPE)
    public void saveDianFengRedisData(Message inMsg){
        Object[] data = inMsg.getData();
        long userRoleId = CovertObjectUtil.obj2long(data[0]);
        String sourceServerId =  CovertObjectUtil.object2String(data[1]);
        int loop = CovertObjectUtil.object2int(data[2]);
        int room = CovertObjectUtil.object2int(data[3]);
        boolean isOnline = CovertObjectUtil.object2boolean(data[4]);
        kuafuDianFengService.saveDianFengData(userRoleId, sourceServerId, loop, room, isOnline);
    }
    
    @EasyMapping(mapping = InnerCmdType.KUAFU_DIANFENG_INSERT_DATA, kuafuType = EasyKuafuType.DIRECT_SWAP_TYPE)
    public void insertDianFengRedisData(Message inMsg){
        Object[] data = inMsg.getData();
        long userRoleId = CovertObjectUtil.obj2long(data[0]);
        int loop = CovertObjectUtil.object2int(data[1]);
        int room = CovertObjectUtil.object2int(data[2]);
        String jsonData = CovertObjectUtil.object2String(data[3]);
        boolean isToNext = CovertObjectUtil.object2boolean(data[4]);
        kuafuDianFengService.insertDianFengData(userRoleId, loop, room, jsonData, isToNext);
    }
    
    @EasyMapping(mapping = InnerCmdType.KUAFU_DIANFENG_UPDATE_DATA, kuafuType = EasyKuafuType.DIRECT_SWAP_TYPE)
    public void updateDianFengRedisData(Message inMsg){
        Object[] data = inMsg.getData();
        int loop = CovertObjectUtil.object2int(data[0]);
        int room = CovertObjectUtil.object2int(data[1]);
        long winRoleId = CovertObjectUtil.obj2long(data[2]);
        kuafuDianFengService.updateDianFengRedisData(loop, room, winRoleId);
    }
    
    // ------------------------------------------源服发送到的跨服指令end------------------------------------------
    
    // ------------------------------------------跨服发送到源服的指令begin------------------------------------------
    @EasyMapping(mapping = InnerCmdType.KUAFU_DIANFENG_ENTER_FAIL, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
    public void enterFail(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        BusMsgSender.send2One(userRoleId, ClientCmdType.ENTER_KUAFU_DIANFENG, AppErrorCode.KUAFU_ENTER_FAIL);
    }

    @EasyMapping(mapping = InnerCmdType.KUAFU_DIANFENG_ENTER_XIAOHEIWU, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
    public void enterXiaoheiwu(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        kuafuDianFengService.enterXiaoheiwu(userRoleId);
    }
    
    @EasyMapping(mapping = InnerCmdType.KUAFU_DIANFENG_EXIT_STAGE, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
    public void exitDianFengStage(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        kuafuDianFengService.exitDianFengStage(userRoleId);
    }
    
    @EasyMapping(mapping = InnerCmdType.KUAFU_DIANFENG_NOTICE, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
    public void noticeDianFeng(Message inMsg) {
        Object[] data = inMsg.getData();
        long userRoleId = CovertObjectUtil.obj2long(data[0]);
        int loop = CovertObjectUtil.object2int(data[1]);
        BusMsgSender.send2One(userRoleId, ClientCmdType.KUAFU_DIANFENG_LOOP_NOTICE, loop);
    }
    // ------------------------------------------跨服发送到源服的指令end------------------------------------------
    
    
}
