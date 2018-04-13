package com.junyou.bus.equip.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.equip.service.EquipService;
import com.junyou.bus.equip.service.XuanTieDuiHuanService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.number.LongUtils;
import com.junyou.utils.parameter.RequestParameterUtil;
import com.kernel.pool.executor.Message;
 
/**
 * 装备Action
 * @author wind
 * @email  18221610336@163.com
 * @date  2014-12-31 上午10:20:11
 */
@Controller
@EasyWorker(moduleName = GameModType.EQUIP_MODULE)
public class EquipAction{
	
	@Autowired
	private EquipService equipService;
	@Autowired
	private XuanTieDuiHuanService xuanTieDuiHuanService;
	
	@EasyMapping(mapping=ClientCmdType.CHANGE_EQUIP)
	public void changeEquip(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		long guid = CovertObjectUtil.obj2long(data[0]); 
		int targetSlot = (Integer)data[1];
		
		Object[] result = equipService.changeEquip(userRoleId,guid,targetSlot);
		BusMsgSender.send2One(userRoleId, ClientCmdType.CHANGE_EQUIP, result);
	}

	//打开面板获取熔炼值
	@EasyMapping(mapping=ClientCmdType.GET_RONGLIAN_VAL)
	public void getRoneLianVal(Message inMsg){
		Long userRoleId = inMsg.getRoleId(); 
		
		Object[] result=equipService.getRonglianVal(userRoleId);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.GET_RONGLIAN_VAL, result);
	}
	//装备回收
	@EasyMapping(mapping=ClientCmdType.EQUIP_RECYCLE)
	public void equipRecycle(Message inMsg){
		Long userRoleId = inMsg.getRoleId(); 
		
		Object[] guids = inMsg.getData();
		Object[] result=equipService.equipRecycle(userRoleId,guids);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.EQUIP_RECYCLE, result);
	}
	
	
	@EasyMapping(mapping = ClientCmdType.EQUIP_QH)
	public void equipQH(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] datas = (Object[])inMsg.getData();
		long guid=LongUtils.obj2long(datas[0]);
		Boolean isAutoGM =(Boolean)datas[1];
		
		BusMsgQueue busMsgQueue=new BusMsgQueue(); 
		Object[] result=equipService.equipQH(userRoleId,guid,busMsgQueue,isAutoGM);
		BusMsgSender.send2One(userRoleId, ClientCmdType.EQUIP_QH, result);
		busMsgQueue.flush();
	}

	@EasyMapping(mapping = ClientCmdType.EQUIP_AUTO_QH)
	public void autoEquipQH(Message inMsg) {
		/*Long userRoleId = inMsg.getRoleId();
		Object[] datas = (Object[])inMsg.getData();
		long guid=LongUtils.obj2long(datas[0]);
		int targetLevel = (Integer)datas[1];
		boolean isAutoGM =(boolean)datas[2];
		
		BusMsgQueue busMsgQueue=new BusMsgQueue(); 
		Object[] result=equipService.autoEquipQH(userRoleId,guid,busMsgQueue,isAutoGM,targetLevel);
		BusMsgSender.send2One(userRoleId, ClientCmdType.EQUIP_AUTO_QH, result);
		busMsgQueue.flush();*/
	}
	
	//打开玄铁兑换面板
	@EasyMapping(mapping=ClientCmdType.XUANTIE_STORE)
	public void getXuanTieDuiHuanInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId(); 
		
		Object[] result=xuanTieDuiHuanService.getDuiHuanInfo(userRoleId);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.XUANTIE_STORE, result);
	}
	//兑换
	@EasyMapping(mapping=ClientCmdType.XUANTIE_DUIHUAN)
	public void xuanTieDuiHuan(Message inMsg){
		Long userRoleId = inMsg.getRoleId(); 
		
		Integer id = inMsg.getData();
		Object[] result=xuanTieDuiHuanService.duihuan(userRoleId, id);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.XUANTIE_DUIHUAN, result);
	}
	//装备升级
	@EasyMapping(mapping=ClientCmdType.ZHUANGBEI_SHENGJI)
	public void zhuangbeiShengJi(Message inMsg){
		Long userRoleId = inMsg.getRoleId(); 
		Object[] datas = (Object[])inMsg.getData();
		long guid=LongUtils.obj2long(datas[0]);
		Boolean isAutoGM =(Boolean)datas[1];
		
		BusMsgQueue busMsgQueue=new BusMsgQueue(); 
		
		Object[] result=equipService.equipLevelUp(userRoleId, guid,busMsgQueue,isAutoGM);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.ZHUANGBEI_SHENGJI, result);
		busMsgQueue.flush();
	}
	
	//神武装备升级
	@EasyMapping(mapping=ClientCmdType.SW_ZHUANBEI_SJ)
	public void swZhuangbeiShengJi(Message inMsg){
		Long userRoleId = inMsg.getRoleId(); 
		Object[] datas = (Object[])inMsg.getData();
		long guid=LongUtils.obj2long(datas[0]);
		Boolean isAutoGM =(Boolean)datas[1];
		
		BusMsgQueue busMsgQueue=new BusMsgQueue(); 
		
		Object[] result=equipService.swEquipLevelUp(userRoleId, guid,busMsgQueue,isAutoGM);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.SW_ZHUANBEI_SJ, result);
		busMsgQueue.flush();
	}
	//装备提品
	@EasyMapping(mapping=ClientCmdType.ZHUANGBEI_TIPIN)
	public void zhuangbeiTiping(Message inMsg){
		Long userRoleId = inMsg.getRoleId(); 
		long guid=LongUtils.obj2long(inMsg.getData());
		
		BusMsgQueue busMsgQueue = new BusMsgQueue(); 
		
		Object[] result=equipService.tipin(userRoleId, guid,busMsgQueue);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.ZHUANGBEI_TIPIN, result);
		busMsgQueue.flush();
	}
	//附属装备提品
	@EasyMapping(mapping=ClientCmdType.FUSHU_ZHUANGBEI_TIPIN)
	public void fuShuZhuangbeiTiping(Message inMsg){
		Long userRoleId = inMsg.getRoleId(); 
		long guid=LongUtils.obj2long(inMsg.getData());
		
		BusMsgQueue busMsgQueue = new BusMsgQueue(); 
		
		Object[] result=equipService.fuShuTiPin(userRoleId, guid,busMsgQueue);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.FUSHU_ZHUANGBEI_TIPIN, result);
		busMsgQueue.flush();
	}
	//强化锤强化
	@EasyMapping(mapping=ClientCmdType.ZHUANGBEI_QIANGHUACHUI)
	public void userQianghuachui(Message inMsg){
		Long userRoleId = inMsg.getRoleId(); 
		Object[] datas =  inMsg.getData();
		Long guid=LongUtils.obj2long(datas[0]);
		String hammerId = String.valueOf(datas[1]);
		Object[] result=equipService.useHammer(userRoleId, guid,hammerId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ZHUANGBEI_QIANGHUACHUI, result);
	}
	//装备升级
	@EasyMapping(mapping=ClientCmdType.FUSHU_EQUIP_SHENGJIE)
	public void fushuZhuangbeiShengJi(Message inMsg){
		Long userRoleId = inMsg.getRoleId(); 
		long guid=LongUtils.obj2long(inMsg.getData());
		BusMsgQueue busMsgQueue=new BusMsgQueue(); 
		Object[] result=equipService.fushuEquipLevelUp(userRoleId, guid,busMsgQueue);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.FUSHU_EQUIP_SHENGJIE, result);
		busMsgQueue.flush();
	}
	//套装铸神系统
	@EasyMapping(mapping=ClientCmdType.TAO_ZHUANG_ZHU_SHEN)
	public void taoZhuangZhushen(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
//		Object[] datas = (Object[])inMsg.getData();
//		long guid=LongUtils.obj2long(datas[0]);
//		Boolean isAutoGM =(Boolean)datas[1];
		Long guid = CovertObjectUtil.object2Long(inMsg.getData());
		BusMsgQueue busMsgQueue=new BusMsgQueue(); 
		Object[] result=equipService.taozhuangzhushen(userRoleId,guid,busMsgQueue,false);
		BusMsgSender.send2One(userRoleId, ClientCmdType.TAO_ZHUANG_ZHU_SHEN, result);
		busMsgQueue.flush();
	}
	//神武装备升星（星铸功能:提升神武装备强化等级）
	@EasyMapping(mapping=ClientCmdType.SW_ZHUANBEI_SX)
	public void swZhuangbeiShengxing(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] datas = inMsg.getData();
		Long guid = CovertObjectUtil.object2Long(datas[0]);
		Boolean isAutoGM = CovertObjectUtil.object2Boolean(datas[1]);
		
		Object[] result = equipService.swEquipStarUp(userRoleId, guid, isAutoGM);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.SW_ZHUANBEI_SX, result);
	}
	
    // -----------------宠物附属装备------------------------//
	//宠物换装(穿装与卸装)
    @EasyMapping(mapping = ClientCmdType.CHONGWU_EQUIP_CHANGE)
    public void changeChongwuEquip(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] data = inMsg.getData();
        Integer chongwuConfigId = RequestParameterUtil.object2Integer(data[0]);
        Long guid = LongUtils.obj2long(data[1]);
        Integer targetSlot = RequestParameterUtil.object2Integer(data[2]);
        Object[] result = equipService.chongwuEquipChange(userRoleId, chongwuConfigId, guid, targetSlot);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.CHONGWU_EQUIP_CHANGE, result);
        }
    }
    
    //宠物附属装备强化
    @EasyMapping(mapping = ClientCmdType.CHONGWU_EQUIP_STRONG)
    public void strongChongwuEquip(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] data = inMsg.getData();
        Integer chongwuConfigId = RequestParameterUtil.object2Integer(data[0]);
        Long guid = LongUtils.obj2long(data[1]);
        Object[] result=equipService.chongwuEquipStrong(userRoleId,chongwuConfigId, guid);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.CHONGWU_EQUIP_STRONG, result);
        }
    }

    //宠物附属装备升阶
    @EasyMapping(mapping=ClientCmdType.CHONGWU_EQUIP_UPLEVEL)
    public void uplevelChongwuEquip(Message inMsg){
        Long userRoleId = inMsg.getRoleId(); 
        Object[] data = inMsg.getData();
        Integer chongwuConfigId = RequestParameterUtil.object2Integer(data[0]);
        Long guid = LongUtils.obj2long(data[1]);
        Object[] result = equipService.chongwuEquipUplevel(userRoleId, chongwuConfigId, guid);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.CHONGWU_EQUIP_UPLEVEL, result);
        }
    }

    //宠物附属装备提品
    @EasyMapping(mapping=ClientCmdType.CHONGWU_EQUIP_TIPIN)
    public void tipinChongwuEquip(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        Object[] data = inMsg.getData();
        Integer chongwuConfigId = RequestParameterUtil.object2Integer(data[0]);
        Long guid = LongUtils.obj2long(data[1]);
        Object[] result = equipService.chongwuEquipTiPin(userRoleId, chongwuConfigId, guid);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.CHONGWU_EQUIP_TIPIN, result);
        }
    }
    
    // -----------------神器附属装备------------------------//
   	//神器穿装
   @EasyMapping(mapping = ClientCmdType.SHENQI_UP_EQUIP)
   public void shenQiEquipUp(Message inMsg) {
       Long userRoleId = inMsg.getRoleId();
       Object[] data = inMsg.getData();
       Long guid = LongUtils.obj2long(data[0]);
       Integer ConfigId = RequestParameterUtil.object2Integer(data[1]);
       Integer targetSlot = RequestParameterUtil.object2Integer(data[2]);
       Object[] result = equipService.shenQiEquipChange(userRoleId, ConfigId, guid, targetSlot);
       if(null != result){
           BusMsgSender.send2One(userRoleId, ClientCmdType.SHENQI_UP_EQUIP, result);
       }
   }
    
}
