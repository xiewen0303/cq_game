package com.junyou.bus.fuben.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.fuben.dao.XinmoDouchangFubenDao;
import com.junyou.bus.fuben.entity.XinmoDouchangFuben;
import com.junyou.bus.fuben.entity.XinmoDouchangFubenConfig;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.XinmoDouchangLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.XinmoDouchangFubenPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.skill.buff.BuffFactory;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.XinmoDouchangFubenStage;
import com.junyou.stage.tunnel.DirectMsgWriter;
import com.junyou.stage.utils.StageHelper;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.parameter.RequestParameterUtil;

/**
 * 
 * @Description 心魔斗场副本业务
 * @Author Yang Gao
 * @Since 2016-8-23
 * @Version 1.1.0
 */
@Service
public class XinmoDouchangFubenService {

    @Autowired
    private XinmoDouchangFubenDao xmDouchangFubenDao;
    @Autowired
    private XinmoDouchangFubenConfigService xinmoDouchangFubenConfigService;

    @Autowired
    private StageControllExportService stageControllExportService;
    @Autowired
    private DiTuConfigExportService diTuConfigExportService;
    @Autowired
    private EmailExportService emailExportService;
    @Autowired
    private RoleExportService roleExportService;
    @Autowired
    private AccountExportService accountExportService;
    @Autowired
    private RoleVipInfoExportService roleVipInfoExportService;
    @Autowired
    private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;

    /**
     * 获取缓存中副本信息
     * 
     * @param userRoleId
     * @return
     */
    private XinmoDouchangFuben getCacheXmDouchangFuben(Long userRoleId) {
        XinmoDouchangFuben xmDouchangFuben = xmDouchangFubenDao.cacheLoad(userRoleId, userRoleId);
        if (null != xmDouchangFuben && !DatetimeUtil.dayIsToday(xmDouchangFuben.getUpdateTime())) {// 处理跨天
            xmDouchangFuben.setFightCount(0);
            xmDouchangFuben.setBuyCount(0);
            xmDouchangFuben.setUpdateTime(GameSystemTime.getSystemMillTime());
            xmDouchangFubenDao.cacheUpdate(xmDouchangFuben, userRoleId);
        }
        return xmDouchangFuben;
    }

    /**
     * 初始化构建玩家心魔斗场副本数据
     * 
     * @param userRoleId
     * @return
     */
    private XinmoDouchangFuben createXmDouchangFuben(Long userRoleId) {
        long now_timne = GameSystemTime.getSystemMillTime();
        XinmoDouchangFuben xmDouchangFuben = new XinmoDouchangFuben();
        xmDouchangFuben.setUserRoleId(userRoleId);
        xmDouchangFuben.setFightCount(0);
        xmDouchangFuben.setBuyCount(0);
        xmDouchangFuben.setCreateTime(now_timne);
        xmDouchangFuben.setUpdateTime(now_timne);
        xmDouchangFubenDao.cacheInsert(xmDouchangFuben, userRoleId);
        return xmDouchangFuben;
    }

    /**
     * 获取心魔斗场副本配置信息
     * 
     * @return
     */
    private XinmoDouchangFubenConfig getXmDouchangFubenConfig() {
        return xinmoDouchangFubenConfigService.loadByConfig();
    }

    /**
     * 获取心魔斗场副本公共配置数据
     * 
     * @return
     */
    private XinmoDouchangFubenPublicConfig getXmDouchangPublicConfig() {
        return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_XINMO_DOUCHANG_FUBEN);
    }

    /**
     * 发送邮件奖励
     * 
     * @param userRoleId
     * @param killNum 击杀怪物数量
     */
    @SuppressWarnings("unchecked")
    private void sendXmDouchangFubenEmailReward(Long userRoleId, Integer killNum) {
        XinmoDouchangFubenConfig xmDouchangFubenConfig = getXmDouchangFubenConfig();
        if (null == xmDouchangFubenConfig) {
            return;
        }
        Map<Integer, Object[]> rewardMap = xmDouchangFubenConfig.getRewardMap();
        if (ObjectUtil.isEmpty(rewardMap)) {
            return;
        }
        Object[] rewardData = rewardMap.get(killNum);
        if (null == rewardData) {
            return;
        }
        Map<String, Integer> rewardItem = new HashMap<>();
        int len = rewardData.length;
        long exp = len > 0 ? (long) rewardData[0] : 0;
        long money = len > 1 ? (long) rewardData[1] : 0;
        long zq = len > 2 ? (long) rewardData[2] : 0;
        Map<String, Integer> reward = (len > 3  && rewardData[3] instanceof Map) ? (Map<String, Integer>) rewardData[3] : null;
        if (zq > 0) {
            rewardItem.put(ModulePropIdConstant.MONEY_ZHENQI_ID, (int) zq);
        }
        if (money > 0) {
            rewardItem.put(ModulePropIdConstant.MONEY_GOODS_ID, (int) money);
        }
        if (exp > 0) {
            rewardItem.put(ModulePropIdConstant.EXP_GOODS_ID, (int) exp);
        }
        if (!ObjectUtil.isEmpty(reward)) {
            ObjectUtil.mapAdd(rewardItem, reward);
        }
        if (!ObjectUtil.isEmpty(rewardItem)) {
        	String title = EmailUtil.getCodeEmail(GameConstants.FUBEN_EMAIL_CONTENT_CODE_TITLE);
            String content = EmailUtil.getCodeEmail(GameConstants.FUBEN_EMAIL_CONTENT_CODE);
            String[] attachments = EmailUtil.getAttachments(rewardItem);
            for (String attachment : attachments) {
                emailExportService.sendEmailToOne(userRoleId,title, content, GameConstants.EMAIL_TYPE_SINGLE, attachment);
            }
        }
        /* 打印日志 */
        RoleWrapper role = roleExportService.getLoginRole(userRoleId);
        XinmoDouchangFuben xmDouchangFuben = getCacheXmDouchangFuben(userRoleId);
        GamePublishEvent.publishEvent(new XinmoDouchangLogEvent(userRoleId, role.getName(), xmDouchangFuben.getFightCount(), xmDouchangFuben.getBuyCount(), killNum, exp, money, zq, LogPrintHandle.getLogGoodsParam(reward, null)));
    }

    /**
     * 初始化心魔斗场副本数据库到缓存数据
     * 
     * @param userRoleId
     * @return
     */
    public List<XinmoDouchangFuben> initXmDouchangFubenCacheData(Long userRoleId) {
        return xmDouchangFubenDao.initXinmoDouchangFuben(userRoleId);
    }

    /**
     * 下线在副本场景中发奖处理
     * 
     * @param userRoleId
     */
    public void offlineHandle(Long userRoleId) {
        String stageId = stageControllExportService.getCurStageId(userRoleId);
        IStage stage = StageManager.getStage(stageId);
        if (null == stage || !XinmoDouchangFubenStage.class.isInstance(stage)) {
            return;
        }
        // 发奖
        sendXmDouchangFubenEmailReward(userRoleId, ((XinmoDouchangFubenStage)stage).getKillNum());
    }

    /**
     * 请求心魔斗场面板信息
     * 
     * @param userRoleId
     * @return
     */
    public Object getXmDouchangFubenInfo(Long userRoleId) {
        XinmoDouchangFuben xmDouchangFuben = getCacheXmDouchangFuben(userRoleId);
        if (null == xmDouchangFuben) {
            xmDouchangFuben = createXmDouchangFuben(userRoleId);
        }
        return new Object[] { xmDouchangFuben.getFightCount(), xmDouchangFuben.getBuyCount() };
    }

    /**
     * 请求进入心魔斗场副本
     * 
     * @param userRoleId
     * @param busMsgQueue
     */
    public void enterXmDouchangFuben(Long userRoleId, BusMsgQueue busMsgQueue) {
        XinmoDouchangFuben xmDouchangFuben = getCacheXmDouchangFuben(userRoleId);
        if (null == xmDouchangFuben) {
            return;
        }
        short requestCmd = ClientCmdType.XM_DOUCHANG_ENTER;
        /* 当前状态不可挑战副本 */
        if (GameConstants.FUBEN_STATE_READY != xmDouchangFuben.getState()) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.FUBEN_STATE_ERROR_TZ);
            return;
        }
        /* 在副本中 */
        if (stageControllExportService.inFuben(userRoleId)) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.FUBEN_IS_IN_FUBEN);
            return;
        }
        XinmoDouchangFubenPublicConfig xmDouchangPublicConfig = getXmDouchangPublicConfig();
        if (null == xmDouchangPublicConfig) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.CONFIG_ERROR);
            return;
        }
        /* 校验副本次数 */
        if (xmDouchangFuben.getFightCount() >= xmDouchangFuben.getBuyCount() + xmDouchangPublicConfig.getCount()) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.FUBEN_NO_COUNT);
            return;
        }
        XinmoDouchangFubenConfig xmDouchangFubenConfig = getXmDouchangFubenConfig();
        if (null == xmDouchangFubenConfig) {
            busMsgQueue.addMsg(userRoleId, requestCmd, AppErrorCode.CONFIG_ERROR);
            return;
        }
        /* 更新副本数据 */
        xmDouchangFuben.setFightCount(xmDouchangFuben.getFightCount() + 1);
        xmDouchangFuben.setState(GameConstants.FUBEN_STATE_FIGHT);
        xmDouchangFuben.setUpdateTime(GameSystemTime.getSystemMillTime());
        xmDouchangFubenDao.cacheUpdate(xmDouchangFuben, userRoleId);
        /* 发送到场景进入地图 */
        DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(xmDouchangPublicConfig.getMapId());
        int[] birthXy = dituCoinfig.getRandomBirth();
        Object[] applyEnterData = new Object[] { dituCoinfig.getId(), birthXy[0], birthXy[1], MapType.XINMO_DOUCHANG_FUBEN_MAP, null, xmDouchangPublicConfig.getTime() };
        busMsgQueue.addBusMsg(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
    }

    /**
     * 请求退出心魔斗场副本
     * 
     * @param userRoleId
     * @param busMsgQueue
     */
    public void exitXmDouchangFuben(Long userRoleId, BusMsgQueue busMsgQueue) {
        if (!stageControllExportService.inFuben(userRoleId)) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.XM_DOUCHANG_EXIT, AppErrorCode.FUBEN_NOT_IN_FUBEN);
            return;
        }
        /* 发送到场景内部处理:退出副本命令 */
        busMsgQueue.addStageMsg(userRoleId, InnerCmdType.S_EXIT_FUBEN, null);
    }

    /**
     * 请求购买心魔斗场副本挑战次数
     * 
     * @param userRoleId
     * @return
     */
    public Object[] buyXmDouchangFubenCount(Long userRoleId) {
        XinmoDouchangFuben xmDouchangFuben = getCacheXmDouchangFuben(userRoleId);
        if (null == xmDouchangFuben) {
            return null;
        }
        int buyCount = xmDouchangFuben.getBuyCount();
        int maxCount = roleVipInfoExportService.getVipTequan(userRoleId, GameConstants.VIP_XM_DOUCHANG_FUBEN_BUY_COUNT);
        if (buyCount >= maxCount) {
            return AppErrorCode.FUBEN_TODAY_NO_BUY_COUNT;
        }

        XinmoDouchangFubenPublicConfig xmDouchangPublicConfig = getXmDouchangPublicConfig();
        if (null == xmDouchangPublicConfig) {
            return AppErrorCode.CONFIG_ERROR;
        }
        int needGold = xmDouchangPublicConfig.getNeedGold();
        Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, needGold, userRoleId, LogPrintHandle.CONSUME_XINMO_DOUCHANG_FUBEN, true, LogPrintHandle.CBZ_BUY_XINMO_DOUCHANG_COUNT);
        if (null != result) {
            return result;
        } else {
            if (PlatformConstants.isQQ()) {
                BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_YB, needGold, LogPrintHandle.CONSUME_XINMO_DOUCHANG_FUBEN, QQXiaoFeiType.CONSUME_XM_DOUCHANG_FUBEN, 1 });
            }
        }
        ++buyCount;
        xmDouchangFuben.setBuyCount(buyCount);
        xmDouchangFubenDao.cacheUpdate(xmDouchangFuben, userRoleId);
        return new Object[] { AppErrorCode.SUCCESS, buyCount };
    }

    // ---------------------------------内部指令处理----------------------------------------//
    /**
     * 心魔斗场规定时间内正常通关处理
     * 
     * @param userRoleId
     */
    public void innerXmDouchangFubenFinish(Long userRoleId) {
        String stageId = stageControllExportService.getCurStageId(userRoleId);
        IStage stage = StageManager.getStage(stageId);
        if (null == stage || !XinmoDouchangFubenStage.class.isInstance(stage)) {
            return;
        }
        XinmoDouchangFuben xmDouchangFuben = getCacheXmDouchangFuben(userRoleId);
        if (null == xmDouchangFuben) {
            return;
        }
        // 更新数据
        xmDouchangFuben.setState(GameConstants.FUBEN_STATE_FINISH);
        xmDouchangFuben.setUpdateTime(GameSystemTime.getSystemMillTime());
        xmDouchangFubenDao.cacheUpdate(xmDouchangFuben, userRoleId);

        // 推送通关成功击杀怪物数量
        XinmoDouchangFubenStage xmDouchangStage = (XinmoDouchangFubenStage) stage;
        BusMsgSender.send2One(userRoleId, ClientCmdType.XM_DOUCHANG_FINISH, new Object[] { xmDouchangStage.getKillNum() });
    }

    /**
     * 心魔斗场成功退出副本
     * 
     * @param userRoleId
     * @param killNum 击杀怪物数量
     */
    public void innerXmDouchangFubenExit(Long userRoleId, Integer killNum) {
        XinmoDouchangFuben xmDouchangFuben = getCacheXmDouchangFuben(userRoleId);
        if (null == xmDouchangFuben) {
            return;
        }
        // 更新数据
        xmDouchangFuben.setState(GameConstants.FUBEN_STATE_READY);
        xmDouchangFuben.setUpdateTime(GameSystemTime.getSystemMillTime());
        xmDouchangFubenDao.cacheUpdate(xmDouchangFuben, userRoleId);
        // 通知退出副本成功
        BusMsgSender.send2One(userRoleId, ClientCmdType.XM_DOUCHANG_EXIT, AppErrorCode.OK);
        // 发奖
        sendXmDouchangFubenEmailReward(userRoleId, killNum);
    }

    /**
     * 击杀副本怪物,添加buff效果和记录副本杀怪数量
     * 
     * @param userRoleId
     * @param monsterId
     */
    public void killMonsterAddBuff(Long userRoleId, String monsterId) {
        if (ObjectUtil.strIsEmpty(monsterId)) {
            return;
        }
        String stageId = stageControllExportService.getCurStageId(userRoleId);
        IStage stage = StageManager.getStage(stageId);
        if (null == stage || !XinmoDouchangFubenStage.class.isInstance(stage)) {
            return;
        }
        XinmoDouchangFubenConfig xmDouchangFubenConfig = getXmDouchangFubenConfig();
        if (null == xmDouchangFubenConfig) {
            return;
        }
        List<Object[]> monsterList = xmDouchangFubenConfig.getMonsterList();
        if (ObjectUtil.isEmpty(monsterList)) {
            return;
        }
        Integer xinmo_type = null;// 击杀怪物获取的心魔类型
        String buff_id = null;// 击杀怪物获取的buff编号
        for (Object[] monsterData : monsterList) {
            if (monsterId.equals(String.valueOf(monsterData[0]))) {
                xinmo_type = RequestParameterUtil.object2Integer(monsterData[2]);
                buff_id = RequestParameterUtil.object2String(monsterData[3]);
                break;
            }
        }
        if (xinmo_type == null || buff_id == null) {
            ChuanQiLog.error("Kill Monster Not Exist On XinmoFuben. [monsterId = {}]", monsterId);
//            ChuanQiLog.error("心魔斗场副本,击杀怪物不存在副本中,怪物编号:{}", monsterId);
            return;
        }
        XinmoDouchangFubenStage xmDouchangStage = (XinmoDouchangFubenStage) stage;
        // 添加杀怪记录
        xmDouchangStage.addKillNum();
        // 添加buff效果
        Map<String, Long> buffValMap = StageHelper.getXinmoExportService().getXinmoOrSkillAttrByXmType(userRoleId, xinmo_type);
        IRole attacker = xmDouchangStage.getChallenger();
        IBuff buff = BuffFactory.create(attacker, buff_id, buffValMap);
        if (null == buff) {
            ChuanQiLog.error("Kill Monster Add Buff Not Attrs On XinmoFuben. [xinmo_type={}, buff_id={}]", xinmo_type, buff_id);
//            ChuanQiLog.error("心魔斗场副本,击杀怪物获得心魔buff[xinmo_type={},buff_id={}],没有获取到任何属性,不会产生buff效果", xinmo_type, buff_id);
            return;
        }
        xmDouchangStage.addKillBuff(buff);
        // 刷新人物buff
        attacker.getBuffManager().addBuff(buff);
        attacker.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());

    }

}
