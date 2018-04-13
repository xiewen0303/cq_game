package com.junyou.bus.fuben.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.fuben.dao.WuxingSkillFubenDao;
import com.junyou.bus.fuben.entity.WuxingSkillFuben;
import com.junyou.bus.fuben.entity.WuxingSkillFubenConfig;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.WuxingSkillFubenLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.WuXingSkillFubenPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IElementProduceTeam;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.pet.Pet;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.WuxingFubenMonsterProduceTeam;
import com.junyou.stage.model.stage.fuben.WuxingSkillFubenStage;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.gen.id.IdFactory;

/**
 * @Description 五行技能副本业务
 * @Author Yang Gao
 * @Since 2016-5-3
 * @Version 1.1.0
 */

@Service
public class WuxingSkillFubenService {
    @Autowired
    private WuxingSkillFubenDao wuxingSkillFubenDao;
    @Autowired
    private WuxingSkillFubenConfigService wuxingSkillFubenConfigService;
    @Autowired
    private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
    @Autowired
    private StageControllExportService stageControllExportService;
    @Autowired
    private DiTuConfigExportService diTuConfigExportService;
    @Autowired
    private RoleExportService roleExportService;
    @Autowired
    private RoleBagExportService roleBagExportService;

    /**
     * @Description 获取不为空的玩家五行技能副本记录
     * @param userRoleId
     * @return
     */
    public WuxingSkillFuben getWxSkillFuben(Long userRoleId) {
        WuxingSkillFuben wxSkillFuben = wuxingSkillFubenDao.cacheLoad(userRoleId, userRoleId);
        if (null != wxSkillFuben && !DatetimeUtil.dayIsToday(wxSkillFuben.getUpdateTime())) {
            /* 此处可以处理跨天业务 */
            resertWxSkillFuben(wxSkillFuben);
        }
        return wxSkillFuben;
    }

    /**
     * @Description 创建五行技能副本数据
     * @param userRoleId
     * @return
     */
    private WuxingSkillFuben createWxSkillFuben(Long userRoleId) {
        long nowTime = GameSystemTime.getSystemMillTime();

        WuxingSkillFuben wxSkillFuben = new WuxingSkillFuben();
        wxSkillFuben.setUserRoleId(userRoleId);
        wxSkillFuben.setPassLayer(0);
        wxSkillFuben.setClearLayer(0);
        wxSkillFuben.setCanClearLayer(0);
        wxSkillFuben.setAddBuff(0);
        wxSkillFuben.setSubBuff(0);
        wxSkillFuben.setCreateTime(nowTime);
        wxSkillFuben.setUpdateTime(nowTime);
        wuxingSkillFubenDao.cacheInsert(wxSkillFuben, userRoleId);

        return wxSkillFuben;
    }

    /**
     * @Description 隔天重置五行技能副本数据
     * @param wxSkillFuben
     */
    private void resertWxSkillFuben(WuxingSkillFuben wxSkillFuben) {
        if (null == wxSkillFuben) {
            return;
        }
        wxSkillFuben.setPassLayer(0);
        wxSkillFuben.setAddBuff(0);
        wxSkillFuben.setSubBuff(0);
        wxSkillFuben.setCanClearLayer(wxSkillFuben.getClearLayer());
        wxSkillFuben.setClearLayer(0);
        wxSkillFuben.setUpdateTime(GameSystemTime.getSystemMillTime());
        wuxingSkillFubenDao.cacheUpdate(wxSkillFuben, wxSkillFuben.getUserRoleId());
    }

    /**
     * @Description 获取玩家五行技能副本信息
     * @param userRoleId
     * @return
     */
    public Object[] getRoleWxSkillFubenInfo(Long userRoleId) {
        WuxingSkillFuben wxSkillFuben = wuxingSkillFubenDao.cacheLoad(userRoleId, userRoleId);
        if (null == wxSkillFuben) {
            wxSkillFuben = createWxSkillFuben(userRoleId);
        } else if (!DatetimeUtil.dayIsToday(wxSkillFuben.getUpdateTime())) {
            /* 在当前副本中战斗,请求不响应.(防止跨天数据混乱) */
            String stageId = stageControllExportService.getCurStageId(userRoleId);
            IStage istage = StageManager.getStage(stageId);
            if (null != istage && WuxingSkillFubenStage.class.isInstance(istage)) {
                return null;
            } else {
                resertWxSkillFuben(wxSkillFuben);
            }
        }
        return new Object[] { wxSkillFuben.getCanClearLayer(), wxSkillFuben.getPassLayer(), new Object[] { wxSkillFuben.getAddBuff(), wxSkillFuben.getSubBuff() } };
    }

    /**
     * @Description 玩家进入五行技能副本
     * @param userRoleId 玩家编号
     * @param layer 副本层次
     * @param busMsgQueue 业务消息分发载体
     */
    public void enterWxSkillFuben(Long userRoleId, int layer, BusMsgQueue busMsgQueue) {
        /* 请求数据错误 */
        if (0 >= layer || layer > wuxingSkillFubenConfigService.findMaxLayer()) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.WUXING_SKILL_FUBEN_ENTER, AppErrorCode.WX_FUBEN_SKILL_LAYER);
            return;
        }

        /* 配置异常 */
        WuxingSkillFubenConfig config = wuxingSkillFubenConfigService.loadByLayer(layer);
        if (config == null) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.WUXING_SKILL_FUBEN_ENTER, AppErrorCode.CONFIG_ERROR);
            return;
        }

        WuxingSkillFuben wxSkillFuben = getWxSkillFuben(userRoleId);
        if(null == wxSkillFuben){
            return ;
        }
        /* 副本难度不正确 */
        if (layer != wxSkillFuben.getPassLayer() + 1) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.WUXING_SKILL_FUBEN_ENTER, AppErrorCode.WX_FUBEN_SKILL_LEVEL);
            return;
        }

        /* 当前状态不可挑战副本 */
        if (GameConstants.FUBEN_STATE_READY != wxSkillFuben.getState()) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.WUXING_SKILL_FUBEN_ENTER, AppErrorCode.FUBEN_STATE_ERROR_TZ);
            return;
        }
        
        /* 在副本中 */
        if (stageControllExportService.inFuben(userRoleId)) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.WUXING_SKILL_FUBEN_ENTER, AppErrorCode.FUBEN_IS_IN_FUBEN);
            return;
        }

        WuXingSkillFubenPublicConfig wxSkillFubenPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_WUXING_SKILL_FUBEN);
        if (null == wxSkillFubenPublicConfig) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.WUXING_SKILL_FUBEN_ENTER, AppErrorCode.CONFIG_ERROR);
            return;
        }

        // 玩家每天第一次挑战前,存在可扫荡关卡,则后端执行清除
        if (wxSkillFuben.getPassLayer() <= 0 && 0 < wxSkillFuben.getCanClearLayer()) {
            wxSkillFuben.setClearLayer(0);
            wxSkillFuben.setCanClearLayer(0);
            wxSkillFuben.setUpdateTime(GameSystemTime.getSystemMillTime());
        }

        /* 更新数据 */
        wxSkillFuben.setState(GameConstants.FUBEN_STATE_FIGHT);
        wuxingSkillFubenDao.cacheUpdate(wxSkillFuben, userRoleId);

        /* 发送到场景进入地图 */
        DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(wxSkillFubenPublicConfig.getMapId());
        int[] birthXy = dituCoinfig.getRandomBirth();
        Object[] applyEnterData = new Object[] { dituCoinfig.getId(), birthXy[0], birthXy[1], MapType.WUXING_SKILL_FUBEN_MAP, layer };
        busMsgQueue.addBusMsg(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
    }

    /**
     * @Description 玩家请求离开五行技能副本
     * @param userRoleId
     * @param busMsgQueue
     */
    public void exitWxSkillFuben(Long userRoleId, BusMsgQueue busMsgQueue) {
        if (!stageControllExportService.inFuben(userRoleId)) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.WUXING_SKILL_FUBEN_EXIT, AppErrorCode.FUBEN_NOT_IN_FUBEN);
            return;
        }
        /* 发送到场景内部处理:退出副本命令 */
        busMsgQueue.addStageMsg(userRoleId, InnerCmdType.S_EXIT_FUBEN, null);
    }

    /**
     * @Description 内部处理客户端离开五行技能副本场景指令
     * @param userRoleId
     */
    public void innerExitWxSkillFuben(Long userRoleId) {
        WuxingSkillFuben wxSkillFuben = getWxSkillFuben(userRoleId);
        if(null == wxSkillFuben){
            return ;
        }
        wxSkillFuben.setState(GameConstants.FUBEN_STATE_READY);
        wuxingSkillFubenDao.cacheUpdate(wxSkillFuben, userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.WUXING_SKILL_FUBEN_EXIT, AppErrorCode.OK);
    }

    /**
     * @Description 领取奖励
     * @param userRoleId
     * @param rewardType 选择的奖励类型
     * @return
     */
    public Object[] receiveReward(Long userRoleId, int rewardType) {
        /* 副本尚未完成 */
        String stageId = stageControllExportService.getCurStageId(userRoleId);
        if (null == stageId) {
            return AppErrorCode.STAGE_IS_NOT_EXIST;
        }

        IStage istage = StageManager.getStage(stageId);
        if (!WuxingSkillFubenStage.class.isInstance(istage)) {
            return AppErrorCode.FUBEN_NOT_IN_FUBEN;
        }

        WuxingSkillFuben wxSkillFuben = wuxingSkillFubenDao.cacheLoad(userRoleId, userRoleId);
        if(null == wxSkillFuben){
            return null;
        }
        if (GameConstants.FUBEN_STATE_FINISH != wxSkillFuben.getState()) {
            return AppErrorCode.FUBEN_NOT_FINISH;
        }
        int newPassLayer = wxSkillFuben.getPassLayer() + 1;
        WuxingSkillFubenConfig config = wuxingSkillFubenConfigService.loadByLayer(newPassLayer);
        if (null == config) {
            return AppErrorCode.WX_FUBEN_SKILL_RECEIVE_ERROR;
        }

        int addBuffVal = 0;// 奖励增加buff值
        int subBuffVal = 0;// 奖励减伤buff值
        Map<String, Integer> rewardMap = null;// 奖励物品
        switch (rewardType) {
        case GameConstants.WXSKILL_NORMAL_REWARD:
            if (newPassLayer % 3 == 0) {
                return AppErrorCode.WX_FUBEN_SKILL_REWARD_TYPE_ERROR;
            }
            break;
        case GameConstants.WXSKILL_ITEM_REWARD:
            if (newPassLayer % 3 != 0) {
                return AppErrorCode.WX_FUBEN_SKILL_REWARD_TYPE_ERROR;
            }
            rewardMap = new HashMap<>();
            ObjectUtil.mapAdd(rewardMap, config.getProp());
            break;
        case GameConstants.WXSKILL_BUFFADD_REWARD:
            if (newPassLayer % 3 != 0) {
                return AppErrorCode.WX_FUBEN_SKILL_REWARD_TYPE_ERROR;
            }
            addBuffVal = wxSkillFuben.getAddBuff() + config.getAddBuffVal();
            wxSkillFuben.setAddBuff(addBuffVal);
            break;
        case GameConstants.WXSKILL_BUFFSUB_REWARD:
            if (newPassLayer % 3 != 0) {
                return AppErrorCode.WX_FUBEN_SKILL_REWARD_TYPE_ERROR;
            }
            subBuffVal = wxSkillFuben.getSubBuff() + config.getSubBuffVal();
            wxSkillFuben.setSubBuff(subBuffVal);
            break;
        default:
            return AppErrorCode.WX_FUBEN_SKILL_REWARD_TYPE_ERROR;
        }

        /* 完美胜利时间:单位毫秒 */
        WuXingSkillFubenPublicConfig wxSkillFubenPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_WUXING_SKILL_FUBEN);
        if (null == wxSkillFubenPublicConfig) {
            return AppErrorCode.CONFIG_ERROR;
        }
        int perfectTime = wxSkillFubenPublicConfig.getTime2() * 1000;
        WuxingSkillFubenStage stage = (WuxingSkillFubenStage) istage;
        if (stage.getFinishTime() <= perfectTime && newPassLayer == wxSkillFuben.getClearLayer() + 1) {
            wxSkillFuben.setClearLayer(newPassLayer);
        }

        /* 更新数据 */
        if(!DatetimeUtil.dayIsToday(wxSkillFuben.getUpdateTime())) {
            resertWxSkillFuben(wxSkillFuben);// 单独处理跨天
        }else{
            wxSkillFuben.setState(GameConstants.FUBEN_STATE_READY);
            wxSkillFuben.setPassLayer(newPassLayer);
            wxSkillFuben.setUpdateTime(GameSystemTime.getSystemMillTime());
            wuxingSkillFubenDao.cacheUpdate(wxSkillFuben, userRoleId);
        }

        /* 发送奖励 */
        long money = config.getMoney();// 奖励银两
        long exp = config.getExp();// 奖励经验
        long zq = config.getZq();// 奖励真气
        if (zq > 0) {
            roleBagExportService.incrNumberWithNotify(GoodsCategory.ZHENQI, zq, userRoleId, LogPrintHandle.GET_WUXING_SKILL_FUBEN_GIFT, LogPrintHandle.GBZ_WUXING_SKILL_FUBEN_GIFT);
        }
        if (money > 0) {
            money = roleBagExportService.incrNumberWithNotify(GoodsCategory.MONEY, money, userRoleId, LogPrintHandle.GET_WUXING_SKILL_FUBEN_GIFT, LogPrintHandle.GBZ_WUXING_SKILL_FUBEN_GIFT);
        }
        if (exp > 0) {
            roleBagExportService.incrNumberWithNotify(GoodsCategory.EXP, exp, userRoleId, 0, 0);
        }
        if (!ObjectUtil.isEmpty(rewardMap)) {
            roleBagExportService.putInBagOrEmailWithNumber(rewardMap, userRoleId, GoodsSource.WUXING_SKILL_FUBEN, LogPrintHandle.GET_WUXING_SKILL_FUBEN_GIFT, LogPrintHandle.GBZ_WUXING_SKILL_FUBEN_GIFT, true, EmailUtil.getCodeEmail(GameConstants.FUBEN_EMAIL_CONTENT_CODE));
        }

        /* 日志打印 */
        printLog(userRoleId, LogPrintHandle.WX_SKILL_FUBEN_TIAOZHAN, newPassLayer, money, exp, zq, addBuffVal, subBuffVal, rewardMap);

        return new Object[] { AppErrorCode.SUCCESS, new Object[] { wxSkillFuben.getCanClearLayer(), wxSkillFuben.getPassLayer(), new Object[] { wxSkillFuben.getAddBuff(), wxSkillFuben.getSubBuff() } } };
    }

    /**
     * @Description 扫荡副本
     * @param userRoleId
     */
    public Object[] clearWxSkillFuben(Long userRoleId) {
        WuxingSkillFuben wxSkillFuben = getWxSkillFuben(userRoleId);
        if(null == wxSkillFuben){
            return null;
        }
        int clearMaxLayer = wxSkillFuben.getCanClearLayer();
        if (wxSkillFuben.getPassLayer() > 0 || 0 >= clearMaxLayer || clearMaxLayer > wuxingSkillFubenConfigService.findMaxLayer()) {
            return AppErrorCode.WX_FUBEN_SKILL_CLEAR_FAIL;
        }

        /* 更新数据 */
        wxSkillFuben.setPassLayer(clearMaxLayer);
        wxSkillFuben.setClearLayer(clearMaxLayer);
        wxSkillFuben.setCanClearLayer(0);
        wxSkillFuben.setUpdateTime(GameSystemTime.getSystemMillTime());
        wuxingSkillFubenDao.cacheUpdate(wxSkillFuben, userRoleId);

        /* 发送奖励 */
        long zq = 0;
        long exp = 0;
        long money = 0;
        int addBuffVal = 0;
        int subBuffVal = 0;
        Map<String, Integer> rewardMap = new HashMap<String, Integer>();
        while (clearMaxLayer > 0) {
            WuxingSkillFubenConfig config = wuxingSkillFubenConfigService.loadByLayer(clearMaxLayer);
            zq += config.getZq();
            exp += config.getExp();
            money += config.getMoney();
            ObjectUtil.mapAdd(rewardMap, config.getProp());
            addBuffVal += config.getAddBuffVal();
            subBuffVal += config.getSubBuffVal();
            clearMaxLayer--;
        }
        if (zq > 0) {
            roleBagExportService.incrNumberWithNotify(GoodsCategory.ZHENQI, zq, userRoleId, 0, 0);
        }
        if (money > 0) {
            money = roleBagExportService.incrNumberWithNotify(GoodsCategory.MONEY, money, userRoleId, LogPrintHandle.GET_WUXING_SKILL_FUBEN_GIFT, LogPrintHandle.GBZ_WUXING_SKILL_FUBEN_GIFT);
        }
        if (exp > 0) {
            roleBagExportService.incrNumberWithNotify(GoodsCategory.EXP, exp, userRoleId, 0, 0);
        }
        if (!ObjectUtil.isEmpty(rewardMap)) {
            roleBagExportService.putInBagOrEmailWithNumber(rewardMap, userRoleId, GoodsSource.WUXING_SKILL_FUBEN, 0, 0, true, GameConstants.FUBEN_SAODANG_EMAIL_CONTENT_CODE);
        }

        /* buff奖励 */
        if (addBuffVal > 0 || subBuffVal > 0) {
            addBuffVal += wxSkillFuben.getAddBuff();
            wxSkillFuben.setAddBuff(addBuffVal);

            subBuffVal += wxSkillFuben.getSubBuff();
            wxSkillFuben.setSubBuff(subBuffVal);

            wxSkillFuben.setUpdateTime(GameSystemTime.getSystemMillTime());
            wuxingSkillFubenDao.cacheUpdate(wxSkillFuben, userRoleId);
        }
        /* 日志打印 */
        printLog(userRoleId, LogPrintHandle.WX_SKILL_FUBEN_SAODANG, clearMaxLayer, money, exp, zq, addBuffVal, subBuffVal, rewardMap);
        return new Object[] { exp, zq, money, ObjectUtil.map2ObjArray(rewardMap), new Object[] { addBuffVal, subBuffVal } };
    }

    /**
     * @Description 五行副本完成更新状态
     * @param userRoleId
     */
    public void finishWxSkillFuben(Long userRoleId) {
        WuxingSkillFuben wxSkillFuben = wuxingSkillFubenDao.cacheLoad(userRoleId, userRoleId);
        if (null == wxSkillFuben) {
            return;
        }
        wxSkillFuben.setState(GameConstants.FUBEN_STATE_FINISH);
        wuxingSkillFubenDao.cacheUpdate(wxSkillFuben, userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.WUXING_SKILL_FUBEN_FINISH, AppErrorCode.OK);
    }

    /**
     * @Description 日志打印
     * @param userRoleId 玩家编号
     * @param logType 日志小类型
     * @param layer 副本层次
     * @param money 奖励银两
     * @param exp 奖励经验
     * @param zq 奖励真气
     * @param addBuff 奖励增伤buff值
     * @param subBuff 奖励减伤buff值
     * @param rewardMap 奖励物品
     */
    private void printLog(Long userRoleId, int logType, int layer, long money, long exp, long zq, int addBuff, int subBuff, Map<String, Integer> rewardMap) {
        RoleWrapper role = roleExportService.getLoginRole(userRoleId);
        GamePublishEvent.publishEvent(new WuxingSkillFubenLogEvent(userRoleId, role.getName(), layer, money, exp, zq, addBuff, subBuff, LogPrintHandle.getLogGoodsParam(rewardMap, null), logType));
    }

    /**
     * @Description 玩家进入下层副本
     * @param userRoleId
     * @param layer
     * @param busMsgQueue
     */
    public void nextWxSkillFuben(Long userRoleId, int layer, BusMsgQueue busMsgQueue) {
        /* 请求数据错误 */
        if (0 >= layer || layer > wuxingSkillFubenConfigService.findMaxLayer()) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.WUXING_SKILL_FUBEN_NEXT, AppErrorCode.WX_FUBEN_SKILL_LAYER);
            return;
        }

        /* 配置异常 */
        WuxingSkillFubenConfig config = wuxingSkillFubenConfigService.loadByLayer(layer);
        if (config == null) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.WUXING_SKILL_FUBEN_NEXT, AppErrorCode.CONFIG_ERROR);
            return;
        }

        WuxingSkillFuben wxSkillFuben = getWxSkillFuben(userRoleId);
        if(null == wxSkillFuben){
            return ;
        }
        
        /* 当前状态不可挑战副本 */
        if (GameConstants.FUBEN_STATE_READY != wxSkillFuben.getState()) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.WUXING_SKILL_FUBEN_NEXT, AppErrorCode.FUBEN_STATE_ERROR_TZ);
            return;
        }

        /* 副本难度不正确 */
        if (layer != wxSkillFuben.getPassLayer() + 1) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.WUXING_SKILL_FUBEN_NEXT, AppErrorCode.WX_FUBEN_SKILL_LEVEL);
            return;
        }

        String stageId = stageControllExportService.getCurStageId(userRoleId);
        if (null == stageId) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.WUXING_SKILL_FUBEN_NEXT, AppErrorCode.STAGE_IS_NOT_EXIST);
            return;
        }

        IStage istage = StageManager.getStage(stageId);
        if (!WuxingSkillFubenStage.class.isInstance(istage)) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.WUXING_SKILL_FUBEN_NEXT, AppErrorCode.FUBEN_NOT_IN_FUBEN);
            return;
        }

        WuxingSkillFubenStage fubenStage = (WuxingSkillFubenStage) istage;
        if (!fubenStage.isWantedListComplete()) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.WUXING_SKILL_FUBEN_NEXT, AppErrorCode.FUBEN_NOT_FINISH);
            return;
        }

        WuXingSkillFubenPublicConfig wxSkillFubenPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_WUXING_SKILL_FUBEN);
        if (null == wxSkillFubenPublicConfig) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.WUXING_SKILL_FUBEN_NEXT, AppErrorCode.CONFIG_ERROR);
            return;
        }

        /* 更新数据 */
        wxSkillFuben.setState(GameConstants.FUBEN_STATE_FIGHT);
        wuxingSkillFubenDao.cacheUpdate(wxSkillFuben, userRoleId);

        /* 重新刷怪物 */
        List<Integer[]> monsterList = new ArrayList<>();
        monsterList.add(wxSkillFubenPublicConfig.getBossXyPoint());
        IElementProduceTeam team = new WuxingFubenMonsterProduceTeam(String.valueOf(IdFactory.getInstance().generateNonPersistentId()), ElementType.MONSTER, monsterList.size(), config.getMonsterId(), monsterList, config.getWxAttrsMap(), 0, 0);

        fubenStage.getStageProduceManager().addElementProduceTeam(team);
        team.produceAll();
        fubenStage.resertWantedMap();

        IRole role = fubenStage.getChallenger();
        /* 重新计算buff值 */
        fubenStage.clearWxSkillFubenBuff();
        fubenStage.addWxSkillFubenBuff(role);
        /* 重新设置场景战斗开始时间 */
        fubenStage.setStatTime();

        /* 设置新的出生坐标 */
        DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(wxSkillFubenPublicConfig.getMapId());
        int[] xy = dituCoinfig.getRandomBirth();
        role.setPosition(xy[0], xy[1]);
        busMsgQueue.addMsg(userRoleId, ClientCmdType.BEHAVIOR_TELEPORT, role.getMoveData());
        Pet pet = role.getPet();
        if (pet != null && fubenStage.isCanHasTangbao()) {
            pet.setPosition(xy[0], xy[1]);
            busMsgQueue.addMsg(userRoleId, ClientCmdType.BEHAVIOR_TELEPORT, pet.getMoveData());
        }

        /* 推送给客户端进入成功 */
        busMsgQueue.addMsg(userRoleId, ClientCmdType.WUXING_SKILL_FUBEN_NEXT, new Object[] { AppErrorCode.SUCCESS, layer, (GameSystemTime.getSystemMillTime() + wxSkillFubenPublicConfig.getTime1() * 1000) });
    }

    /**
     * 上线初始化数据到内存
     * @param userRoleId
     * @return
     */
    public List<WuxingSkillFuben> initWxSkillFubenData(Long userRoleId) {
        return wuxingSkillFubenDao.initWuxingSkillFuben(userRoleId);
    }

}
