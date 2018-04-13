package com.junyou.bus.huajuan2.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.huajuan2.service.Huajuan2Service;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.HUAJUAN2, groupName = EasyGroup.BUS_CACHE)
public class Huajuan2Action {

	@Autowired
	private Huajuan2Service huajuan2Service;
    
    @EasyMapping(mapping = ClientCmdType.HUAJUAN2_ACTIVE)
    public void activeHuaJuan2(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        Integer configId = inMsg.getData();
        Object[] result = huajuan2Service.huajuan2Active(userRoleId, configId);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.HUAJUAN2_ACTIVE, result);
        }
    }
    
    @EasyMapping(mapping = ClientCmdType.HUAJUAN2_INFO)
    public void getInfoHuaJuan2(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        Object[] result = huajuan2Service.huajuan2GetInfo(userRoleId);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.HUAJUAN2_INFO, result);
        }
    }
    
    @EasyMapping(mapping = ClientCmdType.HUAJUAN2_UP)
    public void upHuaJuan2(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        Integer configId = inMsg.getData();
        Object[] result = huajuan2Service.huajuan2Up(userRoleId,configId);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.HUAJUAN2_UP, result);
        }
    }
    
    @EasyMapping(mapping = ClientCmdType.HUAJUAN2_DOWN)
    public void downHuaJuan2(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        Integer configId = inMsg.getData();
        Object[] result = huajuan2Service.huajuan2down(userRoleId,configId);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.HUAJUAN2_DOWN, result);
        }
    }
    
    @EasyMapping(mapping = ClientCmdType.HUAJUAN2_FENJIE)
    public void fenjieHuaJuan2(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        Object[] ids = inMsg.getData();
        Object[] result = huajuan2Service.huajuan2Fenjie(userRoleId, ids);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.HUAJUAN2_FENJIE, result);
        }
    }
    
    @EasyMapping(mapping = ClientCmdType.HUAJUAN2_UPGRADE)
    public void upgradeHuaJuan2(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        Integer configId = inMsg.getData();
        Object[] result = huajuan2Service.huajuan2Upgrade(userRoleId, configId);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.HUAJUAN2_UPGRADE, result);
        }
    }
}
