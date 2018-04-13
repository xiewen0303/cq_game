package com.junyou.bus.role.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.role.service.Stage2BusService;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;


/**
 * 
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-4-9 下午6:42:08
 */
@Controller
@EasyWorker(moduleName = GameModType.STAGE_2_BUS)
public class Stage2BusAction {
	@Autowired
	private Stage2BusService stage2BusService;
	
	@EasyMapping(mapping = InnerCmdType.INNER_DAZUO_AWARD,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void innerDazuoAward(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] datas =  inMsg.getData() ;
		int exp = (int)datas[0];
		int zhenqi = (int)datas[1];
		short cmd = (short)datas[2];
		stage2BusService.innerDazuoAward(exp,zhenqi,userRoleId,cmd);
	}
	@EasyMapping(mapping = InnerCmdType.INNER_ADD_EXP,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void addExp(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Long addExp =  LongUtils.obj2long(data[0]);
		Short cmd = null;
		if(data.length > 1){
			cmd = (Short)data[1];
		}
		stage2BusService.addExp(userRoleId, addExp,cmd);
	}
	@EasyMapping(mapping = InnerCmdType.CHONGWU_ADD_EXP,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void addChongwuExp(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Long addExp =  LongUtils.obj2long(data[0]);
		stage2BusService.addChongwuExp(userRoleId, addExp);
	}
	@EasyMapping(mapping = InnerCmdType.INNER_TERRITORY_ADD_EXP_ZHENQI,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void territoryAddExp_Zhenqi(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Long addExp =  LongUtils.obj2long(data[0]);
		Long addZhenqi =  LongUtils.obj2long(data[1]);
		stage2BusService.territoryAddExp_Zhenqi(userRoleId,addExp, addZhenqi);
	}
	@EasyMapping(mapping = InnerCmdType.INNER_HCZBS_ADD_EXP_ZHENQI,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void hczbsAddExp_Zhenqi(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Long addExp =  LongUtils.obj2long(data[0]);
		Long addZhenqi =  LongUtils.obj2long(data[1]);
		stage2BusService.hczbsAddExp_Zhenqi(userRoleId,addExp, addZhenqi);
	}

    @EasyMapping(mapping = InnerCmdType.KUAFU_YUNGONG_ADD_EXP_ZQ, kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
    public void kuafuYunGongAddExpZq(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] data = inMsg.getData();
        Long addExp = LongUtils.obj2long(data[0]);
        Long addZhenqi = LongUtils.obj2long(data[1]);
        stage2BusService.kuafuYunGongAddExpZq(userRoleId, addExp, addZhenqi);
    }
	
	@EasyMapping(mapping = InnerCmdType.INNER_CLEAR_NUQI,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
	public void clearNuqi(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		stage2BusService.clearNuqi(userRoleId);
	}
}
