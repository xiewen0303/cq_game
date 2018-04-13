/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xinmo.easyaction;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xinmo.service.XinmoService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.number.LongUtils;
import com.junyou.utils.parameter.RequestParameterUtil;
import com.kernel.pool.executor.Message;

/**
 *@Description 心魔系统指令交互处理
 *@Author Yang Gao
 *@Since 2016-6-28
 *@Version 1.1.0
 */
@Controller
@EasyWorker(moduleName = GameModType.XINMO)
public class XinmoAction {

    @Autowired
    private XinmoService xinmoService;
    
    //*********************************心魔*************************************//
    @EasyMapping(mapping = ClientCmdType.XINMO_GET_INFO)
    public void xinmoGetInfo(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        Object[] result = xinmoService.getXinmoInfo(userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.XINMO_GET_INFO, result);
    }
    
    @EasyMapping(mapping = ClientCmdType.XINMO_SHENGJI)
    public void xinmoShengji(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        Object[] data = inMsg.getData();
        Object[] items = (Object[]) data[0];
        Boolean isAuto = (Boolean) data[1];
        
        List<Long> itemIds = new ArrayList<Long>();
        if(null != items && items.length > 0){
            for (int i = 0; i < items.length; i++) {
                Long id = LongUtils.obj2long(items[i]);
                if(!itemIds.contains(id)){
                    itemIds.add(id);
                }
            }
        }
        Object[] result = xinmoService.xinmoShengji(userRoleId, itemIds, isAuto);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.XINMO_SHENGJI, result);
        }
    }
    
    @EasyMapping(mapping = ClientCmdType.XINMO_TUPO)
    public void xinmoTupo(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        Object[] data = inMsg.getData();
        Object[] items = (Object[]) data[0];
        Boolean isAuto = (Boolean) data[1];
        
        List<Long> itemIds = new ArrayList<Long>();
        if(null != items && items.length > 0){
            for (int i = 0; i < items.length; i++) {
                Long id = LongUtils.obj2long(items[i]);
                if(!itemIds.contains(id)){
                    itemIds.add(id);
                }
            }
        }
        Object[] result = xinmoService.xinmoTupo(userRoleId, itemIds, isAuto);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.XINMO_TUPO, result);
        }
    }
    
    @EasyMapping(mapping = ClientCmdType.XINMO_NINGSHEN)
    public void xinmoNingshen(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        Object[] items = inMsg.getData();
        List<Long> itemIds = new ArrayList<Long>();
        if(null != items && items.length > 0){
            for (int i = 0; i < items.length; i++) {
                Long id = LongUtils.obj2long(items[i]);
                if(!itemIds.contains(id)){
                    itemIds.add(id);
                }
            }
        }
        Object[] result = xinmoService.xinmoNingshen(userRoleId, itemIds);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.XINMO_NINGSHEN, result);
        }
    }

    @EasyMapping(mapping = InnerCmdType.INNER_ADD_XINMO_EXP,kuafuType = EasyKuafuType.KF2S_HANDLE_TYPE)
    public void xinmoNingshenAddExp(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        xinmoService.killMonsterAddXinmoExp(userRoleId);
    }

    //*******************************心魔:天炉炼丹***********************************//
    @EasyMapping(mapping = InnerCmdType.XM_LIANDAN_DINGSHI)
    public void xinmoDingshiStart(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        xinmoService.produceLianDan(userRoleId);
    }
    
    @EasyMapping(mapping = ClientCmdType.XM_LIANDAN_GET_INFO)
    public void xinmoLiandanGetInfo(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        Object[] result = xinmoService.xinmoLiandanGetInfo(userRoleId);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.XM_LIANDAN_GET_INFO, result);
        }
    }
    
    @EasyMapping(mapping = ClientCmdType.XM_LIANDAN_SHENGJI)
    public void xinmoLiandanShengji(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        Object[] result = xinmoService.xinmoLiandanShengji(userRoleId);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.XM_LIANDAN_SHENGJI, result);
        }
    }
    
    @EasyMapping(mapping = ClientCmdType.XM_LIANDAN_OPEN)
    public void xinmoLiandanOpen(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        long guid = LongUtils.obj2long(inMsg.getData());
        Object[] result = xinmoService.xinmoLiandanOpen(userRoleId,guid);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.XM_LIANDAN_OPEN, result);
        }
    }
    
    @EasyMapping(mapping = ClientCmdType.XM_LIANDAN_TOOK_OUT)
    public void xinmoLiandanTookOut(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        long guid = LongUtils.obj2long(inMsg.getData());
        Object[] result = xinmoService.xinmoLiandanTookOut(userRoleId,guid);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.XM_LIANDAN_TOOK_OUT, result);
        }
    }
    
    @EasyMapping(mapping = ClientCmdType.XM_LIANDAN_SORT_DANLI)
    public void xinmoLiandanSort(Message inMsg){
        Long userRoleId = inMsg.getRoleId();
        Object[] result = xinmoService.xinmoLiandanSort(userRoleId);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.XM_LIANDAN_SORT_DANLI, result);
        }
    }
    
    //*******************************心魔-魔神***************************************//
    @EasyMapping(mapping = ClientCmdType.XM_MOSHEN_GET_INFO)
    public void getXinmoMoshenInfo(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = xinmoService.getXinmoMoshenInfo(userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.XM_MOSHEN_GET_INFO, result);
    }

    @EasyMapping(mapping = ClientCmdType.XM_MOSHEN_ACTIVATE)
    public void xinmoMoshenActivate(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] obj = inMsg.getData();
        Integer type = RequestParameterUtil.object2Integer(obj[0]);
        Boolean isAutoGM = RequestParameterUtil.object2Boolean(obj[1]);
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        xinmoService.xinmoMoshenActivate(userRoleId, type, isAutoGM, busMsgQueue);
        busMsgQueue.flush();
    }

    @EasyMapping(mapping = ClientCmdType.XM_MOSHEN_UP_LEVEL)
    public void xinmoMoshenUpLvl(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] obj = inMsg.getData();
        Integer type = RequestParameterUtil.object2Integer(obj[0]);
        Boolean isAutoGM = RequestParameterUtil.object2Boolean(obj[1]);
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        xinmoService.xinmoMoshenUpLvl(userRoleId, type, isAutoGM, busMsgQueue);
        busMsgQueue.flush();
    }
    
    @EasyMapping(mapping = ClientCmdType.XM_MOSHEN_SHITI)
    public void xinmoMoshenShiti(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Integer type =  RequestParameterUtil.object2Integer(inMsg.getData());
        BusMsgQueue busMsgQueue = new BusMsgQueue();
        xinmoService.xinmoMoshenShiti(userRoleId, type, busMsgQueue);
        busMsgQueue.flush();
    }
    
    @EasyMapping(mapping = ClientCmdType.XM_MOSHEN_BLESS_INFO)
    public void getXinmoMoshenBlessInfo(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Integer type =  RequestParameterUtil.object2Integer(inMsg.getData());
        Object[] result = xinmoService.getXinmoMoshenBlessInfo(userRoleId, type);
        if(null != result){
            BusMsgSender.send2One(userRoleId, ClientCmdType.XM_MOSHEN_BLESS_INFO, result);
        }
    }
    
    @EasyMapping(mapping = InnerCmdType.XM_MOSHEN_SHITI_CD)
    public void xinmoMoshenShitiCdEnd(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        xinmoService.xinmoMoshenShitiCdEnd(userRoleId);
    }

    //*******************************心魔-魔神技能***************************************//
    @EasyMapping(mapping = ClientCmdType.XM_SKILL_GET_INFO)
    public void getXinmoSkillInfo(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = xinmoService.getXmSkillInfo(userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.XM_SKILL_GET_INFO, result);
    }
    
    @EasyMapping(mapping = ClientCmdType.XM_SKILL_UP_LEVEL)
    public void getXinmoSkillUpLevel(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Integer skill_id = RequestParameterUtil.object2Integer(inMsg.getData());
        Object[] result = xinmoService.upLevelXmSkill(userRoleId, skill_id);
        BusMsgSender.send2One(userRoleId, ClientCmdType.XM_SKILL_UP_LEVEL, result);
    }
    
    //*******************************心魔-洗练***************************************//
    @EasyMapping(mapping = ClientCmdType.XM_XILIAN_GET_INFO)
    public void getXinmoXilianInfo(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] result = xinmoService.getXinmoXilianInfo(userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.XM_XILIAN_GET_INFO, result);
    }
    
    @EasyMapping(mapping = ClientCmdType.XM_XILIAN_BEGIN)
    public void xinmoXilianBegin(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Object[] data = inMsg.getData();
        Integer xm_type = RequestParameterUtil.object2Integer(data[0]);
        Object[] result = xinmoService.xinmoXilianBegin(userRoleId, xm_type, data[1]);
        BusMsgSender.send2One(userRoleId, ClientCmdType.XM_XILIAN_BEGIN, result);
    }
    
    @EasyMapping(mapping = ClientCmdType.XM_XILIAN_REPLACE)
    public void xinmoXilianReplace(Message inMsg) {
        Long userRoleId = inMsg.getRoleId();
        Integer xm_type = RequestParameterUtil.object2Integer(inMsg.getData());
        Object[] result = xinmoService.xinmoXilianReplace(userRoleId, xm_type);
        if(result != null){
            BusMsgSender.send2One(userRoleId, ClientCmdType.XM_XILIAN_REPLACE, result);
        }
    }
    
    
}
