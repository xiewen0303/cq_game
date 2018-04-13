package com.junyou.bus.fuben.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.fuben.dao.WuxingFubenDao;
import com.junyou.bus.fuben.entity.WuxingFuben;
import com.junyou.bus.fuben.entity.WuxingFubenConfig;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.IncrRoleResp;
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
import com.junyou.event.WuxingFubenLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.WuXingFubenPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * @Description 五行副本业务
 * @Author Yang Gao
 * @Since 2016-4-20 下午1:29:56
 * @Version 1.1.0
 */
@Service
public class WuxingFubenService {
    @Autowired
    private WuxingFubenDao wuxingFubenDao;
    @Autowired
    private WuxingFubenConfigService wuxingFubenConfigService;
    @Autowired
    private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
    @Autowired
    private StageControllExportService stageControllExportService;
    @Autowired
    private DiTuConfigExportService diTuConfigExportService;
    @Autowired
    private RoleVipInfoExportService roleVipInfoExportService;
    @Autowired
    private RoleExportService roleExportService;
    @Autowired
    private AccountExportService accountExportService;
    @Autowired
    private EmailExportService emailExportService;
    @Autowired
    private RoleBagExportService roleBagExportService;

    /**
     * @Description 获取不为空的玩家五行副本记录
     * @param userRoleId
     * @return
     */
    private WuxingFuben getWxFuben(Long userRoleId) {
        WuxingFuben wxFuben = wuxingFubenDao.cacheLoad(userRoleId, userRoleId);
        if (null == wxFuben) {
            wxFuben = createWxFuben(userRoleId);
        } else if (!DatetimeUtil.dayIsToday(wxFuben.getUpdateTime())) {
            /* 此处可以处理跨天业务 */
            wxFuben.setFightCount(0);
            wxFuben.setBuyCount(0);
            wxFuben.setUpdateTime(GameSystemTime.getSystemMillTime());
            wuxingFubenDao.cacheUpdate(wxFuben, userRoleId);
        }
        return wxFuben;
    }

    /**
     * @Description 创建新五行副本数据
     * @param userRoleId
     * @return
     */
    private WuxingFuben createWxFuben(Long userRoleId) {
        WuxingFuben wxFuben = new WuxingFuben();
        wxFuben.setUserRoleId(userRoleId);
        wxFuben.setFubenInfo("");
        wxFuben.setFightCount(0);
        wxFuben.setBuyCount(0);
        long nowTime = GameSystemTime.getSystemMillTime();
        wxFuben.setCreateTime(nowTime);
        wxFuben.setUpdateTime(nowTime);
        wuxingFubenDao.cacheInsert(wxFuben, userRoleId);
        return wxFuben;
    }

    /**
     * @Description 解析五行副本记录
     * @param fubenInfo
     * @return
     */
    private Map<String, List<Integer>> analyzeWxFubenInfo(Long userRoleId) {
        Map<String, List<Integer>> rsMap = new HashMap<String, List<Integer>>();

        Map<Integer, WuxingFubenConfig> allWxFubenMap = wuxingFubenConfigService.loadAll();
        WuxingFuben wuxingFuben = getWxFuben(userRoleId);
        String fubenInfo = wuxingFuben.getFubenInfo();
        if (!CovertObjectUtil.isEmpty(fubenInfo)) {
            String[] strArray = fubenInfo.split(GameConstants.WUXING_FUBEN_SPLIT_CHAR);
            for (String s : strArray) {
                WuxingFubenConfig wxFuben = allWxFubenMap.get(Integer.parseInt(s));
                if (null != wxFuben) {
                    String key = wxFuben.getType();
                    List<Integer> val = rsMap.get(key);
                    if (null == val)
                        val = new ArrayList<Integer>();
                    val.add(wxFuben.getLevel());
                    rsMap.put(key, val);
                }
            }
        }

        return rsMap;
    }

    /**
     * @Description 创建五行副本挑战记录数据传输对象VO
     * @param userRoleId
     * @return Array[Object1[副本类型(String1),副本难度最大等级,没有为0(Integer1)],...,ObjectN[StringN,IntegerN]]
     */
    private Object[] createClientWxFubenVo(Long userRoleId) {
        List<Object[]> rs = new ArrayList<Object[]>();
        Map<String, List<Integer>> map = analyzeWxFubenInfo(userRoleId);

        Set<String> typeSet = wuxingFubenConfigService.loadAllType();
        for (String wxtype : typeSet) {
            List<Integer> listVal = map.get(wxtype);
            if (null == listVal) {
                rs.add(new Object[] { wxtype, 0 });
            } else {
                Collections.sort(listVal, new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o2.compareTo(o1);
                    }
                });
                rs.add(new Object[] { wxtype, listVal.get(0) });
            }
        }
        return rs.toArray();
    }

    /**
     * @Description 获取指定类型下玩家的最大难度等级
     * @param userRoleId
     * @param type
     * @return 0=没有挑战过此类型任何难度的副本;大于0=此类型挑战的最大难度等级
     */
    private int getWxCurMaxLvlByType(Long userRoleId, String type) {
        int rsInt = 0;
        Object[] wxFubenObj = createClientWxFubenVo(userRoleId);
        if (null != wxFubenObj) {
            for (Object obj : wxFubenObj) {
                Object[] array = (Object[]) obj;
                String wxFubenType = (String) array[0];
                if (type.equals(wxFubenType)) {
                    Integer rsInteger = (Integer) array[1];
                    rsInt = rsInteger == null ? 0 : rsInteger.intValue();
                    break;
                }
            }
        }
        return rsInt;
    }

    /**
     * @Description 发生邮件
     * @param userRoleId
     * @param contentCode
     * @param items
     */
    private void sendEmail(Long userRoleId, String titleCode,String contentCode, Map<String, Integer> items) {
    	String title = EmailUtil.getCodeEmail(titleCode);
        String content = EmailUtil.getCodeEmail(contentCode);
        String[] attachments = EmailUtil.getAttachments(items);
        for (String attachment : attachments) {
            emailExportService.sendEmailToOne(userRoleId,title, content, GameConstants.EMAIL_TYPE_SINGLE, attachment);
        }
    }

    /**
     * @Description 获取玩家五行副本信息
     * @param userRoleId
     * @return
     */
    public Object[] getRoleWxFubenInfo(Long userRoleId) {
        WuxingFuben wxFuben = getWxFuben(userRoleId);
        int buyCount = 0;
        int fightCount = 0;
        if (null != wxFuben) {
            buyCount = wxFuben.getBuyCount();
            fightCount = wxFuben.getFightCount();
        }
        return new Object[] { fightCount, buyCount, createClientWxFubenVo(userRoleId) };
    }

    /**
     * @Description 玩家进入五行副本
     * @param userRoleId 玩家编号
     * @param fubenId 副本编号
     * @param busMsgQueue 业务消息分发载体
     */
    public void enterWxFuben(Long userRoleId, Integer fubenId, BusMsgQueue busMsgQueue) {
        WuxingFubenConfig config = wuxingFubenConfigService.loadById(fubenId);
        /* 配置异常 */
        if (config == null) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_WUXING_FUBEN, AppErrorCode.CONFIG_ERROR);
            return;
        }

        WuxingFuben wxFuben = getWxFuben(userRoleId);
        /* 当前状态不可挑战副本 */
        if (GameConstants.FUBEN_STATE_READY != wxFuben.getState()) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_WUXING_FUBEN, AppErrorCode.FUBEN_STATE_ERROR_TZ);
            return;
        }

        /* 在副本中 */
        if (stageControllExportService.inFuben(userRoleId)) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_WUXING_FUBEN, AppErrorCode.FUBEN_IS_IN_FUBEN);
            return;
        }

        /* 校验玩家选择的五行副本难度有效性 :不能超过玩家最大难度+1 */
        int maxLevel = getWxCurMaxLvlByType(userRoleId, config.getType()) + 1;
        if (config.getLevel() > maxLevel) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_WUXING_FUBEN, AppErrorCode.WUXING_FUBEN_LEVEL);
            return;
        }

        WuXingFubenPublicConfig wxFubenPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_WUXING_FUBEN);
        if (null == wxFubenPublicConfig) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_WUXING_FUBEN, AppErrorCode.CONFIG_ERROR);
            return;
        }
        /* 挑战次数不够 */
        int remainCnt = wxFubenPublicConfig.getCount() + wxFuben.getBuyCount() - wxFuben.getFightCount();
        if (remainCnt <= 0) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.ENTER_WUXING_FUBEN, AppErrorCode.FUBEN_NO_COUNT);
            return;
        }

        /* 更新数据 */
        wxFuben.setCurFubenId(fubenId);
        wxFuben.setState(GameConstants.FUBEN_STATE_FIGHT);
        wuxingFubenDao.cacheUpdate(wxFuben, userRoleId);

        /* 发送到场景进入地图 */
        DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(config.getMapId());
        int[] birthXy = dituCoinfig.getRandomBirth();
        Object[] applyEnterData = new Object[] { dituCoinfig.getId(), birthXy[0], birthXy[1], MapType.WUXING_FUBEN_MAP, fubenId };
        busMsgQueue.addBusMsg(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
    }

    /**
     * @Description 玩家请求离开五行副本
     * @param userRoleId
     * @param busMsgQueue
     */
    public void exitWxFuben(Long userRoleId, BusMsgQueue busMsgQueue) {
        if (!stageControllExportService.inFuben(userRoleId)) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.EXIT_WUXING_FUBEN, AppErrorCode.FUBEN_NOT_IN_FUBEN);
            return;
        }
        /* 发送到场景内部处理:退出副本命令 */
        busMsgQueue.addStageMsg(userRoleId, InnerCmdType.S_EXIT_FUBEN, null);
    }

    /**
     * @Description 内部五行副本退出处理：更新状态
     * @param userRoleId
     */
    public void innerExitWxFuben(Long userRoleId) {
        WuxingFuben wxFuben = getWxFuben(userRoleId);
        wxFuben.setState(GameConstants.FUBEN_STATE_READY);
        wuxingFubenDao.cacheUpdate(wxFuben, userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.EXIT_WUXING_FUBEN, AppErrorCode.OK);
    }

    /**
     * @Description 玩家购买五行副本挑战次数
     * @param userRoleId
     * @return
     */
    public Object[] buyCount(Long userRoleId) {
        WuxingFuben wxFuben = getWxFuben(userRoleId);
        int buyCount = wxFuben.getBuyCount();
        int maxCount = roleVipInfoExportService.getVipTequan(userRoleId, GameConstants.VIP_WX_FUBEN_BUY_COUNT);
        if (buyCount >= maxCount) {
            return AppErrorCode.WUXING_FUBEN_TODAY_NO_BUY_COUNT;
        }
        WuXingFubenPublicConfig wxFubenPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_WUXING_FUBEN);
        if (null == wxFubenPublicConfig) {
            return AppErrorCode.CONFIG_ERROR;
        }
        int needGold = wxFubenPublicConfig.getBuyNeedGold();
        Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, needGold, userRoleId, LogPrintHandle.CONSUME_WUXING_FUBEN, true, LogPrintHandle.CBZ_BUY_WUXING_COUNT);
        if (null != result) {
            return result;
        } else {
            if (PlatformConstants.isQQ()) {
                BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_YB, needGold, LogPrintHandle.CONSUME_WUXING_FUBEN, QQXiaoFeiType.CONSUME_WUXING_FUBEN, 1 });
            }
        }
        ++buyCount;
        wxFuben.setBuyCount(buyCount);
        wuxingFubenDao.cacheUpdate(wxFuben, userRoleId);
        
        return new Object[] { AppErrorCode.SUCCESS, buyCount };

    }

    /**
     * @Description 领取奖励
     * @param userRoleId
     * @param isOffline true=离线处理;false=正常处理
     * @return
     */
    public Object[] receiveReward(Long userRoleId, boolean offline) {
        WuxingFuben wxFuben = wuxingFubenDao.cacheLoad(userRoleId, userRoleId);
        if(wxFuben == null){
            return null;
        }
        if (GameConstants.FUBEN_STATE_FINISH != wxFuben.getState()) {
            return AppErrorCode.WUXING_FUBEN_NOT_FINISH;// 副本尚未完成
        }

        /* 玩家正在挑战的副本编号 */
        int fubenId = wxFuben.getCurFubenId();
        WuxingFubenConfig config = wuxingFubenConfigService.loadById(fubenId);
        if (null == config) {
            return AppErrorCode.WUXING_FUBEN_ID_ERROR;
        }

        String fubenInfo = wxFuben.getFubenInfo();
        int addCount = wxFuben.getFightCount();
        
        /* 当前副本首次挑战标识: 首次挑战消耗当日挑战次数 */
        boolean firstFlag = false;
        if (CovertObjectUtil.isEmpty(fubenInfo)) {
            firstFlag = true;
        } else {
            String[] strArray = fubenInfo.split(GameConstants.WUXING_FUBEN_SPLIT_CHAR);
            List<String> strList = Arrays.asList(strArray);
            if (!strList.contains(String.valueOf(fubenId))) {
                firstFlag = true;
            }
        }
        /* 更新业务数据 */
        if (firstFlag) {
            if (CovertObjectUtil.isEmpty(fubenInfo)) {
                fubenInfo += fubenId;
            } else {
                fubenInfo += GameConstants.WUXING_FUBEN_SPLIT_CHAR + fubenId;
            }
        } else {
            addCount++;
        }
        wxFuben.setCurFubenId(0);
        wxFuben.setState(GameConstants.FUBEN_STATE_READY);
        wxFuben.setFightCount(addCount);
        wxFuben.setFubenInfo(fubenInfo);
        wxFuben.setUpdateTime(GameSystemTime.getSystemMillTime());
        wuxingFubenDao.cacheUpdate(wxFuben, userRoleId);

        /* 发送奖励 */
        long money = config.getMoney();
        long exp = config.getExp();
        long zq = config.getZq();
        ReadOnlyMap<String, Integer> items = config.getProp();

        Map<String, Integer> rewardMap = new HashMap<>();
        ObjectUtil.mapAdd(rewardMap, items);
        if (money > 0) {
            if (offline) {
                rewardMap.put(ModulePropIdConstant.MONEY_GOODS_ID, (int) money);
            } else {
                money = accountExportService.incrCurrencyWithNotify(GoodsCategory.MONEY, money, userRoleId, LogPrintHandle.GET_WUXING_FUBEN_GIFT, LogPrintHandle.GBZ_WUXING_FUBEN_GIFT);
            }
        }
        if (exp > 0) {
            if (offline) {
                rewardMap.put(ModulePropIdConstant.EXP_GOODS_ID, (int) exp);
            } else {
                IncrRoleResp incrRoleResp = roleExportService.incrExp(userRoleId, exp);
                if (incrRoleResp == null) {
                    exp = 0;
                } else {
                    exp = incrRoleResp.getIncrExp();
                }
            }

        }
        if (zq > 0) {
            if (offline) {
                rewardMap.put(ModulePropIdConstant.MONEY_ZHENQI_ID, (int) zq);
            } else {
                roleExportService.addZhenqi(userRoleId, zq);
            }
        }
        /* 离线和玩家背包已满,物品奖励发送到玩家邮箱 */
        if (offline || roleBagExportService.checkPutInBag(rewardMap, userRoleId) != null) {
        	sendEmail(userRoleId,GameConstants.WUXING_FUBEN_EMAIL_TITLE_CODE, GameConstants.WUXING_FUBEN_EMAIL_CONTENT_CODE, rewardMap);
        } else {
            roleBagExportService.putInBag(rewardMap, userRoleId, GoodsSource.WUXING_FUBEN, true);
        }
        /* 打印日志 */
        RoleWrapper role = roleExportService.getLoginRole(userRoleId);
        GamePublishEvent.publishEvent(new WuxingFubenLogEvent(userRoleId, role.getName(), fubenId, money, exp, zq, LogPrintHandle.getLogGoodsParam(rewardMap, null)));
        return AppErrorCode.OK;
    }

    /**
     * @Description 五行副本完成更新状态
     * @param userRoleId
     */
    public void finishFuben(Long userRoleId) {
        WuxingFuben wxFuben = getWxFuben(userRoleId);
        wxFuben.setState(GameConstants.FUBEN_STATE_FINISH);
        wuxingFubenDao.cacheUpdate(wxFuben, userRoleId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.FINISH_WUXING_FUBEN, AppErrorCode.OK);
    }

    /**
     * @Description 下线处理
     * @param userRoleId
     */
    public void offlineHandle(Long userRoleId) {
        receiveReward(userRoleId, true);
    }

    /**
     * 上线初始化处理
     * @return
     */
    public List<WuxingFuben> initWxFubenData(Long userRoleId) {
        return wuxingFubenDao.initWuxingFuben(userRoleId);
    }

}
