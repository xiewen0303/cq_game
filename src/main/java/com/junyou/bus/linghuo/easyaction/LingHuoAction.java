package com.junyou.bus.linghuo.easyaction;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.linghuo.service.LingHuoService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.parameter.RequestParameterUtil;
import com.kernel.pool.executor.Message;
 
/**
 * 灵火
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-1-15 下午5:15:01
 */
@Controller
@EasyWorker(moduleName = GameModType.LING_HUO,groupName=EasyGroup.BUS_CACHE)
public class LingHuoAction {
	
	@Autowired
	private LingHuoService lingHuoService;
	
	@EasyMapping(mapping = ClientCmdType.GET_LINGHUO_INFO)
	public void getLingHuoId(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result=lingHuoService.getLingHuoId(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_LINGHUO_INFO, result);
	}
	
	@EasyMapping(mapping = ClientCmdType.JIHUO_LINGHUO)
	public void jihuoLingHuo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Integer id = inMsg.getData();
		
		
		Object[] result=lingHuoService.jiHuoLingHuo(userRoleId, id);
		BusMsgSender.send2One(userRoleId, ClientCmdType.JIHUO_LINGHUO, result);
		
	}

	@EasyMapping(mapping = ClientCmdType.LINGHUO_BLESS_INFO)
	public void loadLinghuoBlessInfo(Message inMsg){
	    Long userRoleId = inMsg.getRoleId();
	    Integer linghuoId = inMsg.getData();
        Object[] result = lingHuoService.loadLinghuoBlessInfo(userRoleId, linghuoId);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.LINGHUO_BLESS_INFO, result);
        }
	}
	
    @EasyMapping(mapping = ClientCmdType.LINGHUO_BLESS_PUT_ON)
    public void putOnLinghuoBless(Message inMsg) {
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        Long userRoleId = inMsg.getRoleId();
        Object[] data = inMsg.getData();
        Integer linghuoId = RequestParameterUtil.object2Integer(data[0]);
        Integer linghuoSlot = RequestParameterUtil.object2Integer(data[1]);
//        Object guid = data[2];
        lingHuoService.putOnLinghuoBless(userRoleId, linghuoId, linghuoSlot, busMsgQueue);
        busMsgQueue.flushAndRemove();
    }
	
	@EasyMapping(mapping = ClientCmdType.LINGHUO_BLESS_SEND_ATTRS)
	public void sendLinghuoBlessAttr(Message inMsg){
	    Long userRoleId = inMsg.getRoleId();
	    Map<String, Long> attrsMap = lingHuoService.getLinghuoBlessAttrs(userRoleId);
	    BusMsgSender.send2One(userRoleId, ClientCmdType.LINGHUO_BLESS_SEND_ATTRS, attrsMap);
	}
}
