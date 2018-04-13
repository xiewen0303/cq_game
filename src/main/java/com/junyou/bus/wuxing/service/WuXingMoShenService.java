package com.junyou.bus.wuxing.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.wuxing.configure.export.HuanShiMoShenJiChuBiaoConfig;
import com.junyou.bus.wuxing.configure.export.HuanShiMoShenJiChuBiaoConfigExportService;
import com.junyou.bus.wuxing.configure.export.MoShenJiNengBiaoConfig;
import com.junyou.bus.wuxing.configure.export.MoShenJiNengBiaoConfigExportService;
import com.junyou.bus.wuxing.configure.export.TangbaoMoShenBaseConfig;
import com.junyou.bus.wuxing.configure.export.TangbaoMoShenBaseConfigExportService;
import com.junyou.bus.wuxing.configure.export.TangbaoMoShenJiNengBiaoConfig;
import com.junyou.bus.wuxing.configure.export.TangbaoMoShenJiNengBiaoConfigExportService;
import com.junyou.bus.wuxing.configure.export.jingpo.MoshenJingpoConfig;
import com.junyou.bus.wuxing.configure.export.jingpo.MoshenJingpoConfigExportService;
import com.junyou.bus.wuxing.configure.export.jingpo.MoshenJingpoDiaoLuoConfig;
import com.junyou.bus.wuxing.configure.export.jingpo.MoshenJingpoDuiHuanConfig;
import com.junyou.bus.wuxing.configure.export.jingpo.MoshenJingpoKongWeiConfig;
import com.junyou.bus.wuxing.configure.export.jingpo.MoshenJingpoShuaXinConfig;
import com.junyou.bus.wuxing.constants.WuxingMoshenType;
import com.junyou.bus.wuxing.dao.RoleWuxingDao;
import com.junyou.bus.wuxing.dao.RoleWuxingFutiDao;
import com.junyou.bus.wuxing.dao.RoleWuxingJingpoDao;
import com.junyou.bus.wuxing.dao.RoleWuxingJingpoItemDao;
import com.junyou.bus.wuxing.dao.RoleWuxingSkillDao;
import com.junyou.bus.wuxing.dao.TangbaoWuxingDao;
import com.junyou.bus.wuxing.dao.TangbaoWuxingSkillDao;
import com.junyou.bus.wuxing.entity.RoleWuxing;
import com.junyou.bus.wuxing.entity.RoleWuxingFuti;
import com.junyou.bus.wuxing.entity.RoleWuxingJingpo;
import com.junyou.bus.wuxing.entity.RoleWuxingJingpoItem;
import com.junyou.bus.wuxing.entity.RoleWuxingSkill;
import com.junyou.bus.wuxing.entity.TangbaoWuxing;
import com.junyou.bus.wuxing.entity.TangbaoWuxingSkill;
import com.junyou.bus.wuxing.filter.TangbaoWuXingFilter;
import com.junyou.bus.wuxing.filter.TangbaoWuxingSkillFilter;
import com.junyou.bus.wuxing.filter.WuXingFilter;
import com.junyou.bus.wuxing.filter.WuxingJingpoFilter;
import com.junyou.bus.wuxing.filter.WuxingSkillFilter;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.WuxingMoshenLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.event.util.LogFormatUtils;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.WuXingJingpoPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.WuXingPublicConfig;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.junyou.utils.lottery.RandomUtil;
import com.junyou.utils.number.LongUtils;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;
import com.kernel.spring.container.DataContainer;

/**
 * 五行魔神
 * 
 * @author: wind
 * @email: 18221610336@163.com
 * @version: 2014-11-27下午3:38:12
 */

@Service
public class WuXingMoShenService {

    @Autowired
    private DataContainer dataContainer;
    @Autowired
    private RoleWuxingDao roleWuxingDao;
    @Autowired
    private RoleWuxingFutiDao roleWuxingFutiDao;
    @Autowired
    private RoleWuxingSkillDao roleWuxingSkillDao;
    @Autowired
    private RoleWuxingJingpoDao roleWuxingJingpoDao;
    @Autowired
    private RoleWuxingJingpoItemDao roleWuxingJingpoItemDao;
    @Autowired
    private TangbaoWuxingDao tangbaoWuxingDao;
    @Autowired
    private TangbaoWuxingSkillDao tangbaoWuxingSkillDao;

    @Autowired
    private RoleExportService roleExportService;
    @Autowired
    private AccountExportService accountExportService;
    @Autowired
    private RoleBagExportService roleBagExportService;
    @Autowired
    private HuanShiMoShenJiChuBiaoConfigExportService huanShiMoShenJiChuBiaoConfigExportService;
    @Autowired
    private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
    @Autowired
    private MoShenJiNengBiaoConfigExportService moShenJiNengBiaoConfigExportService;
    @Autowired
    private MoshenJingpoConfigExportService moshenJingpoConfigExportService;
    @Autowired
    private TangbaoMoShenBaseConfigExportService tangbaoMoShenBaseConfigExportService;
    @Autowired
    private TangbaoMoShenJiNengBiaoConfigExportService tangbaoMoShenJiNengBiaoConfigExportService;
    
    public List<RoleWuxing> initRoleWuxing(Long userRoleId) {
        return roleWuxingDao.initRoleWuxing(userRoleId);
    }

    public List<RoleWuxingFuti> initRoleWuxingFutis(Long userRoleId) {
        return roleWuxingFutiDao.initRoleWuxingFuti(userRoleId);
    }

    public void onlineTuiSong(Long userRoleId) {
        RoleWuxingFuti futi = getRoleWuxingFuti(userRoleId);
        if (futi.getWuxingId() == 0) {
            return;
        }
        BusMsgSender.send2One(userRoleId, ClientCmdType.TUISONG_FUTI, futi.getWuxingId());
    }

    public Integer getFuTiType(Long userRoleId) {
        RoleWuxingFuti futi = getRoleWuxingFuti(userRoleId);
        if (futi.getWuxingId() == 0) {
            return 0;
        }
        HuanShiMoShenJiChuBiaoConfig config = huanShiMoShenJiChuBiaoConfigExportService.loadById(futi.getWuxingId());
        if (config == null) {
            return 0;
        }
        return config.getType();
    }

    public Integer getFuTiConfigId(Long userRoleId) {
        RoleWuxingFuti futi = getRoleWuxingFuti(userRoleId);
        /*
         * if(futi.getWuxingId() == 0){ return 0; }
         */
        /*
         * HuanShiMoShenJiChuBiaoConfig config =
         * huanShiMoShenJiChuBiaoConfigExportService
         * .loadById(futi.getWuxingId()); if(config == null){ return 0; }
         */
        return futi.getWuxingId();
    }

    private RoleWuxingFuti getRoleWuxingFuti(Long userRoleId) {
        List<RoleWuxingFuti> list = roleWuxingFutiDao.cacheAsynLoadAll(userRoleId);
        if (list == null || list.size() <= 0) {
            RoleWuxingFuti futi = new RoleWuxingFuti();
            futi.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
            futi.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
            futi.setUserRoleId(userRoleId);
            futi.setWuxingId(0);

            roleWuxingFutiDao.cacheInsert(futi, userRoleId);

            return futi;
        }
        return list.get(0);
    }

    private RoleWuxing getRoleWuXingByType(Long userRoleId, int type) {
        List<RoleWuxing> list = roleWuxingDao.cacheLoadAll(userRoleId, new WuXingFilter(type));
        if (list == null || list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }

    private RoleWuxing createRoleWuXing(Long userRoleId, int type) {
        RoleWuxing wuxing = new RoleWuxing();
        wuxing.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
        wuxing.setUserRoleId(userRoleId);
        wuxing.setWuxingLevel(1);
        wuxing.setWuxingType(type);
        wuxing.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
        wuxing.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
        wuxing.setLastSjTime(0l);
        wuxing.setZfzVal(0);

        roleWuxingDao.cacheInsert(wuxing, userRoleId);

        return wuxing;
    }
    
    public Long getRoleWuXingTotallZplus(Long userRoleId){
    	List<RoleWuxing> wuxingList = roleWuxingDao.cacheLoadAll(userRoleId);
    	Long totalZplus=0l;
    	
    	if(wuxingList ==null || wuxingList.size() ==0){
    		return 0l;
    	}
    	
    	for (RoleWuxing roleWuxing : wuxingList) {
    		HuanShiMoShenJiChuBiaoConfig  config = huanShiMoShenJiChuBiaoConfigExportService.getWuXingConfigByTypeAndLevel(roleWuxing.getWuxingType(), roleWuxing.getWuxingLevel());
    		Long zplus = config.getAttrs().get(EffectType.zplus.name());
    		if(zplus != null){
    			totalZplus +=zplus;
    		}
		}
    	
    	return totalZplus;
    }

    /**
     * 获得已激活的五行
     * 
     * @param userRoleId
     * @return
     */
    public Object[] getJiHuoWuXing(Long userRoleId) {
        List<RoleWuxing> list = roleWuxingDao.cacheLoadAll(userRoleId);
        if (list == null || list.size() <= 0) {
            return null;
        }
        List<Object[]> reList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            RoleWuxing wuxing = list.get(i);
            HuanShiMoShenJiChuBiaoConfig config = huanShiMoShenJiChuBiaoConfigExportService.getWuXingConfigByTypeAndLevel(wuxing.getWuxingType(), wuxing.getWuxingLevel());
            if (config == null) {
                continue;
            }
            reList.add(new Object[] { wuxing.getWuxingType(), config.getId(), getZfzValue(wuxing, config), wuxing.getLastSjTime() });
        }
        return reList.toArray();
    }

    /**
     * 激活五行
     */
    public Object[] jihuoWuXing(Long userRoleId, int type, boolean isAutoGM) {
        // 五行是否已经被激活过
        RoleWuxing wuxing = getRoleWuXingByType(userRoleId, type);
        if (wuxing != null) {
            return AppErrorCode.WUXING_YI_JIHUO;
        }
        // 五行配置
        HuanShiMoShenJiChuBiaoConfig config = huanShiMoShenJiChuBiaoConfigExportService.getWuXingConfigByTypeAndLevel(type, GameConstants.WUXING_CHUSHI_LEVEL);
        if (config == null) {
            return AppErrorCode.CONFIG_ERROR;
        }
        // 激活需要道具
        List<String> needGoodsIds = huanShiMoShenJiChuBiaoConfigExportService.getConsumeIds(config.getItem());
        int needCount = config.getCount();
        Map<String, Integer> tempResources = new HashMap<>();
        Object[] goldObj = new Object[2];
        for (String goodsId : needGoodsIds) {
            int owerCount = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
            if (owerCount >= needCount) {
                tempResources.put(goodsId, needCount);
                needCount = 0;
                break;
            }
            needCount = needCount - owerCount;
            tempResources.put(goodsId, owerCount);
        }
        if (isAutoGM && needCount > 0) {
            int bPrice = config.getBgold();// 绑定元宝的价格
            if (bPrice < 1) {
                return AppErrorCode.WUXING_NO_ITEM;
            }
            int bCount = 0;
            int nowNeedBgold = 0;
            for (int i = 0; i < needCount; i++) {
                nowNeedBgold = (bCount + 1) * bPrice;
                Object[] bgoldError = roleBagExportService.isEnought(GoodsCategory.BGOLD, nowNeedBgold, userRoleId);
                if (null != bgoldError) {
                    break;
                }
                bCount++;
            }
            nowNeedBgold = bCount * bPrice;
            tempResources.put(GoodsCategory.BGOLD + "", nowNeedBgold);
            goldObj[0] = nowNeedBgold;
            needCount = needCount - bCount;

            int price = config.getGold();// 需要通过商城配置表获得对应物品的价格
            if (price < 1) {
                return AppErrorCode.CONFIG_ERROR;
            }

            int nowNeedGold = needCount * price;

            Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD, nowNeedGold, userRoleId);
            if (null != goldError) {
                return AppErrorCode.YB_ERROR;
            }

            tempResources.put(GoodsCategory.GOLD + "", nowNeedGold);
            goldObj[1] = nowNeedGold;
            needCount = 0;
        } else {
            if (needCount > 0) {
                return AppErrorCode.WUXING_NO_ITEM;
            }
        }
        Integer newNeedGold = tempResources.remove(GoodsCategory.GOLD + "");
        Integer newNeedBgold = tempResources.remove(GoodsCategory.BGOLD + "");

        // 扣除元宝
        if (newNeedGold != null && newNeedGold > 0) {
            roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, newNeedGold, userRoleId, LogPrintHandle.CONSUME_WUXING_JIHUO, true, LogPrintHandle.CBZ_WUXING_JIHUO);
            // 腾讯OSS消费上报
            if (PlatformConstants.isQQ()) {
                BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_YB, newNeedGold, LogPrintHandle.CONSUME_WUXING_JIHUO + "", QQXiaoFeiType.CONSUME_WUXING_JIHUO, 1 });
            }
        } else {
            newNeedGold = 0;
        }
        // 扣除绑定元宝
        if (newNeedBgold != null && newNeedBgold > 0) {
            roleBagExportService.decrNumberWithNotify(GoodsCategory.BGOLD, newNeedBgold, userRoleId, LogPrintHandle.CONSUME_WUXING_JIHUO, true, LogPrintHandle.CBZ_WUXING_JIHUO);
            // 腾讯OSS消费上报
            if (PlatformConstants.isQQ()) {
                BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_BYB, newNeedGold, LogPrintHandle.CONSUME_WUXING_JIHUO + "", QQXiaoFeiType.CONSUME_WUXING_JIHUO, 1 });
            }
        } else {
            newNeedBgold = 0;
        }

        roleBagExportService.removeBagItemByGoods(tempResources, userRoleId, GoodsSource.WUXING_JH, true, true);

        // 激活
        RoleWuxing wx = createRoleWuXing(userRoleId, type);
        HuanShiMoShenJiChuBiaoConfig wxConfig = huanShiMoShenJiChuBiaoConfigExportService.getWuXingConfigByTypeAndLevel(wx.getWuxingType(), wx.getWuxingLevel());
        int configId = 0;
        if (wxConfig != null) {
            configId = wxConfig.getId();
        }
        // 通知属性变化
        notifyStageWuXingChange(userRoleId);
        
        refreshWxJpSlot(userRoleId, wx.getWuxingType(), wx.getWuxingLevel());
        
        return new Object[] { 1, type, configId, goldObj };
    }

    /** 五行升阶 */
    public Object[] sjWuXing(Long userRoleId, int type, BusMsgQueue busMsgQueue, boolean isAutoGM) {
        RoleWuxing wuxing = getRoleWuXingByType(userRoleId, type);
        if (wuxing == null) {
            return AppErrorCode.WUXING_WEI_JIHUO;
        }
        int wuxingLevel = wuxing.getWuxingLevel();
        // 五行配置
        HuanShiMoShenJiChuBiaoConfig config = huanShiMoShenJiChuBiaoConfigExportService.getWuXingConfigByTypeAndLevel(type, wuxingLevel);
        if (config == null) {
            return AppErrorCode.CONFIG_ERROR;
        }
        if (config.getItem() == null || "".equals(config.getItem())) {
            return AppErrorCode.WUXING_MAX_LEVEL;
        }
        Map<String, Integer> needResource = new HashMap<>();

        int zfzVal = getZfzValue(wuxing, config);

        Object[] result = zuoQiSj(config, needResource, userRoleId, true, isAutoGM, zfzVal, config.getGold(), config.getBgold());

        Object[] erroCode = (Object[]) result[0];
        if (erroCode != null) {
            return erroCode;
        }

        int newlevel = (Integer) result[1];
        int newZfz = (Integer) result[2];
        Integer newNeedMoney = needResource.remove(GoodsCategory.MONEY + "");
        Integer newNeedGold = needResource.remove(GoodsCategory.GOLD + "");
        Integer newNeedBgold = needResource.remove(GoodsCategory.BGOLD + "");

        // 扣除金币
        if (newNeedMoney != null && newNeedMoney > 0) {
            roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, newNeedMoney, userRoleId, LogPrintHandle.CONSUME_WUXING_SHENGJI, true, LogPrintHandle.CBZ_WUXING_SHENGJI);
        }
        // 扣除元宝
        if (newNeedGold != null && newNeedGold > 0) {
            roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, newNeedGold, userRoleId, LogPrintHandle.CONSUME_WUXING_SHENGJI, true, LogPrintHandle.CBZ_WUXING_SHENGJI);
            // 腾讯OSS消费上报
            if (PlatformConstants.isQQ()) {
                BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_YB, newNeedGold, LogPrintHandle.CONSUME_WUXING_SHENGJI, QQXiaoFeiType.CONSUME_WUXING_SHENGJI, 1 });
            }
        }
        // 扣除绑定元宝
        if (newNeedBgold != null && newNeedBgold > 0) {
            roleBagExportService.decrNumberWithNotify(GoodsCategory.BGOLD, newNeedBgold, userRoleId, LogPrintHandle.CONSUME_WUXING_SHENGJI, true, LogPrintHandle.CBZ_WUXING_SHENGJI);
            // 腾讯OSS消费上报
            if (PlatformConstants.isQQ()) {
                BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_BYB, newNeedBgold, LogPrintHandle.CONSUME_WUXING_SHENGJI, QQXiaoFeiType.CONSUME_WUXING_SHENGJI, 1 });
            }
        }

        BagSlots bagSlots = roleBagExportService.removeBagItemByGoods(needResource, userRoleId, GoodsSource.WUXING_SJ, true, true);
        if(!bagSlots.isSuccee()){
            return bagSlots.getErrorCode();
        }
        wuxing.setZfzVal(newZfz);
        wuxing.setWuxingLevel(newlevel);
        HuanShiMoShenJiChuBiaoConfig newConfig = huanShiMoShenJiChuBiaoConfigExportService.getWuXingConfigByTypeAndLevel(type, newlevel);
        if (newZfz > 0 && (newlevel != wuxingLevel || zfzVal == 0)) {
            long zfzCdTime = 0l;

            float clearTime = newConfig.getCztime();
            if (clearTime == 0) {
                zfzCdTime = 0l;
            } else {
                zfzCdTime = GameSystemTime.getSystemMillTime() + (int) (clearTime * 60 * 60 * 1000);
            }
            wuxing.setLastSjTime(zfzCdTime);
        } else if (newZfz == 0) {
            wuxing.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            wuxing.setLastSjTime(0l);
        }

        roleWuxingDao.cacheUpdate(wuxing, userRoleId);

        // 如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象
        if (newlevel > wuxingLevel) {
            notifyStageWuXingChange(userRoleId);

            if (newConfig.isGgopen()) {
                UserRole userRole = roleExportService.getUserRole(userRoleId);
                BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[] { getGongGao(type), new Object[] { userRole.getName(), newlevel } });
            }
            
            refreshWxJpSlot(userRoleId, wuxing.getWuxingType(), wuxing.getWuxingLevel());
        }
        // 日志记录
        JSONArray consumeItemArray = new JSONArray(); 
        LogFormatUtils.parseJSONArray(bagSlots,consumeItemArray);
        GamePublishEvent.publishEvent(new WuxingMoshenLogEvent(LogPrintHandle.WUXING_MOSHEN_LOG, LogPrintHandle.WUXING_MOSHEN_TYPE_ROLE, userRoleId, newNeedMoney, newNeedGold, consumeItemArray, wuxingLevel, newlevel, zfzVal, newZfz));
        return new Object[] { 1, type, newConfig.getId(), wuxing.getZfzVal(), wuxing.getLastSjTime() };
    }
    
    /**
     * 五行道具升阶
     * @param userRoleId
     * @param type
     * @param level
     * @param rate
     * @param items
     * @return
     */
    public Object[] sjByItem(Long userRoleId,Integer type,Integer level,Integer rate,String items){
    	int wuXingtype = getType(type);
    	RoleWuxing wuxing = getRoleWuXingByType(userRoleId, wuXingtype);
    	if (wuxing == null) {
            return AppErrorCode.WUXING_WEI_JIHUO;
        }
    	//当前五行等级
    	int wuxingLevel = wuxing.getWuxingLevel();
    	
    	 // 五行配置
        HuanShiMoShenJiChuBiaoConfig config = huanShiMoShenJiChuBiaoConfigExportService.getWuXingConfigByTypeAndLevel(wuXingtype, wuxingLevel);
        if (config == null) {
            return AppErrorCode.CONFIG_ERROR;
        }
        int maxLevel = huanShiMoShenJiChuBiaoConfigExportService.getMaxJjLevel();
        if(wuxingLevel >= maxLevel){
        	return AppErrorCode.WUXING_MAX_LEVEL;
        }
    	if(level.intValue() >= wuxingLevel){
    		int zfz = getZfzValue(wuxing, config);
    		int newZfz =zfz + (int) Math.round(config.getZfzmax() * (rate/100d));
    		// 如果祝福值大于了最大值，算强化成功
            int maxzf = config.getZfzmax();
            if (newZfz >= maxzf) {
            	int newLevel = wuxing.getWuxingLevel()+1;
            	wuxing.setZfzVal(0);
            	wuxing.setWuxingLevel(newLevel);
            	wuxing.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            	HuanShiMoShenJiChuBiaoConfig newConfig = huanShiMoShenJiChuBiaoConfigExportService.getWuXingConfigByTypeAndLevel(wuXingtype, newLevel);
            	wuxing.setLastSjTime(0l);
            	roleWuxingDao.cacheUpdate(wuxing, userRoleId);
            	notifyStageWuXingChange(userRoleId);
            	if (newConfig != null && newConfig.isGgopen()) {
                    UserRole userRole = roleExportService.getUserRole(userRoleId);
                    BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[] { getGongGao(type), new Object[] { userRole.getName(), newLevel } });
                }
            	BusMsgSender.send2One(userRoleId, ClientCmdType.QINGQIU_WUXING_SHENGJI, new Object[] { 1, wuXingtype, newConfig.getId(), wuxing.getZfzVal(), wuxing.getLastSjTime() });
            }else{
            	float clearTime = config.getCztime();
            	long zfzCdTime = 0l;
                if (clearTime == 0) {
                    zfzCdTime = 0l;
                }else{
                	zfzCdTime = GameSystemTime.getSystemMillTime() + (int) (clearTime * 60 * 60 * 1000);
                }
            	wuxing.setLastSjTime(zfzCdTime);
            	wuxing.setZfzVal(newZfz);
            	roleWuxingDao.cacheUpdate(wuxing, userRoleId);
            	BusMsgSender.send2One(userRoleId, ClientCmdType.QINGQIU_WUXING_SHENGJI, new Object[] { 1, wuXingtype, config.getId(), wuxing.getZfzVal(), wuxing.getLastSjTime() });
            }
    	}else{
    		// ****如果不能使用则发送相应的道具进背包****
    		if(!CovertObjectUtil.isEmpty(items)){
    			Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap(items);
    			roleBagExportService.putGoodsAndNumberAttr(itemMap, userRoleId, GoodsSource.GOODS_WUXING_GIFT, LogPrintHandle.GET_WUXING_GIFT, LogPrintHandle.GET_WUXING_GIFT, true);
    		}
    	}
    	return null;
    }

    private int getGongGao(int type) {
        if (type == 1) {
            return AppErrorCode.WUXING_SJ_NOTICE_1;
        } else if (type == 2) {
            return AppErrorCode.WUXING_SJ_NOTICE_2;
        } else if (type == 3) {
            return AppErrorCode.WUXING_SJ_NOTICE_3;
        } else if (type == 4) {
            return AppErrorCode.WUXING_SJ_NOTICE_4;
        } else {
            return AppErrorCode.WUXING_SJ_NOTICE_5;
        }
    }
    
    private int getType(int type) {
        if (type == GameConstants.WUXING_JIN_SJTYPE) {
            return GameConstants.WUXING_JIN_TYPE;
        } else if (type == GameConstants.WUXING_MU_SJTYPE) {
            return GameConstants.WUXING_MU_TYPE;
        } else if (type == GameConstants.WUXING_TU_SJTYPE) {
            return GameConstants.WUXING_TU_TYPE;
        } else if (type == GameConstants.WUXING_SHUI_SJTYPE) {
            return GameConstants.WUXING_SHUI_TYPE;
        } else {
            return GameConstants.WUXING_HUO_TYPE;
        }
    }

    /**
     * 坐骑升阶
     * 
     * @param zqLevel
     * @param needResource
     * @param userRoleId
     * @param isSendErrorCode
     * @param maxLevel
     * @param isAutoGM
     * @param targetLevel
     * @param zfzVal
     * @param isAuto
     * @return
     */
    private Object[] zuoQiSj(HuanShiMoShenJiChuBiaoConfig config, Map<String, Integer> needResource, long userRoleId, boolean isSendErrorCode, boolean isAutoGM, int zfzVal, int yb, int byb) {

        Map<String, Integer> tempResources = new HashMap<>();
        int money = config.getMoney();
        int oldMoney = needResource.get(GoodsCategory.MONEY) == null ? 0 : needResource.get(GoodsCategory.MONEY);
        Object[] isOb = roleBagExportService.isEnought(GoodsCategory.MONEY, money + oldMoney, userRoleId);
        if (null != isOb) {
            Object[] errorCode = isSendErrorCode ? isOb : null;
            return new Object[] { errorCode };
        }
        tempResources.put(GoodsCategory.MONEY + "", money);

        List<String> needGoodsIds = huanShiMoShenJiChuBiaoConfigExportService.getConsumeIds(config.getItem());
        int needCount = config.getCount();

        for (String goodsId : needGoodsIds) {
            int oldNeedCount = needResource.get(goodsId) == null ? 0 : needResource.get(goodsId);

            int owerCount = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
            if (owerCount >= oldNeedCount + needCount) {
                tempResources.put(goodsId, needCount);
                needCount = 0;
                break;
            }
            needCount = oldNeedCount + needCount - owerCount;
            tempResources.put(goodsId, owerCount - oldNeedCount);
        }

        if (isAutoGM && needCount > 0) {
            int bPrice = byb;// 绑定元宝的价格
            int bCount = 0;
            int nowNeedBgold = 0;
            for (int i = 0; i < needCount; i++) {
                nowNeedBgold = (bCount + 1) * bPrice;
                Object[] bgoldError = roleBagExportService.isEnought(GoodsCategory.BGOLD, nowNeedBgold, userRoleId);
                if (null != bgoldError) {
                    break;
                }
                bCount++;
            }
            nowNeedBgold = bCount * bPrice;
            tempResources.put(GoodsCategory.BGOLD + "", nowNeedBgold);

            needCount = needCount - bCount;

            int price = yb;

            int nowNeedGold = needCount * price;

            Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD, nowNeedGold, userRoleId);
            if (null != goldError) {
                Object[] errorCode = isSendErrorCode ? goldError : null;
                return new Object[] { errorCode };
            }

            tempResources.put(GoodsCategory.GOLD + "", nowNeedGold);
            needCount = 0;
        }

        if (needCount > 0) {
            Object[] errorCode = isSendErrorCode ? AppErrorCode.WUXING_NO_ITEM : null;
            return new Object[] { errorCode };
        }
        ObjectUtil.mapAdd(needResource, tempResources);

        boolean flag = isSJSuccess(zfzVal, config);
        if (!flag) {
            zfzVal += RandomUtil.getIntRandomValue(config.getZfzmin2(), config.getZfzmin3() + 1);
        }

        // 如果祝福值大于了最大值，算强化成功
        int maxzf = config.getZfzmax();
        int level = config.getLevel();
        if (flag || zfzVal >= maxzf) {
            zfzVal = 0;
            level = level + 1;
        }

        return new Object[] { null, level, zfzVal };
    }

    private int getZfzValue(RoleWuxing wuxing, HuanShiMoShenJiChuBiaoConfig config) {
        int zfzValue = wuxing.getZfzVal();
        boolean isClear = config.isZfztime();
        long qiLastTime = wuxing.getLastSjTime();
        if (qiLastTime <= GameSystemTime.getSystemMillTime() && isClear) {
            wuxing.setLastSjTime(0l);
            wuxing.setZfzVal(0);
            roleWuxingDao.cacheUpdate(wuxing, wuxing.getUserRoleId());
            return 0;
        }
        return zfzValue;
    }

    /**
     * 五行升阶
     * 
     * @param zfzValue
     * @param qiLastTime
     * @param qhConfig
     * @return
     */
    public boolean isSJSuccess(int zfzValue, HuanShiMoShenJiChuBiaoConfig config) {

        int minzf = config.getZfzmin();

        if (zfzValue < minzf) {
            return false;
        }

        int pro = config.getPro();

        if (RandomUtil.getIntRandomValue(1, 101) > pro) {
            return false;
        }
        return true;
    }

    /**
     * 通知场景里面属性变化
     */
    public void notifyStageWuXingChange(long userRoleId) {
        List<RoleWuxing> list = roleWuxingDao.cacheLoadAll(userRoleId);
        if(list == null || list.size() <= 0){
        	return;
        }
        List<Integer> configList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
        	RoleWuxing wuxing = list.get(i);
        	HuanShiMoShenJiChuBiaoConfig config = huanShiMoShenJiChuBiaoConfigExportService.getWuXingConfigByTypeAndLevel(wuxing.getWuxingType(), wuxing.getWuxingLevel());
        	if(config == null){
        		continue;
        	}
        	configList.add(config.getId());
        }
        // 推送内部场景坐骑属性变化
        BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_WUXING_CHANGE, configList);
    }

    public Object[] futi(Long userRoleId, int type) {
        WuXingPublicConfig publicConfig = getPublicConfig();
        if (publicConfig == null) {
            return AppErrorCode.CONFIG_ERROR;
        }

        RoleWuxingFuti futi = getRoleWuxingFuti(userRoleId);
        if (type == 0) {// 解体
            futi.setWuxingId(0);
            futi.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
            roleWuxingFutiDao.cacheUpdate(futi, userRoleId);

            BusMsgSender.send2Stage(userRoleId, InnerCmdType.WUXING_FUTI_CHARGE, new Object[] { 0, type });
        } else {
            Long cd = publicConfig.getMoshencd() * 1000;
            // 校验cd
            Long lastChangeTime = dataContainer.getData(GameConstants.WUXING_FUTI_TIME, userRoleId.toString());
            if (lastChangeTime != null) {
                Long currentTime = GameSystemTime.getSystemMillTime();
                if (currentTime < lastChangeTime + cd) {
                    return AppErrorCode.YAOSHEN_SHOW_CHANGE_CD;
                }
            }
            RoleWuxing wuxing = getRoleWuXingByType(userRoleId, type);
            if (wuxing == null) {
                return AppErrorCode.WUXING_WEI_JIHUO_FUTI;
            }

            // 五行配置
            HuanShiMoShenJiChuBiaoConfig config = huanShiMoShenJiChuBiaoConfigExportService.getWuXingConfigByTypeAndLevel(type, wuxing.getWuxingLevel());
            if (config == null) {
                return AppErrorCode.CONFIG_ERROR;
            }
            futi.setWuxingId(config.getId());
            futi.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
            roleWuxingFutiDao.cacheUpdate(futi, userRoleId);

            BusMsgSender.send2Stage(userRoleId, InnerCmdType.WUXING_FUTI_CHARGE, new Object[] { config.getId(), type });
        }

        dataContainer.putData(GameConstants.WUXING_FUTI_TIME, userRoleId.toString(), GameSystemTime.getSystemMillTime());
        return new Object[] { 1, futi.getWuxingId() };
    }

    public Object[] getZfz(Long userRoleId, int type) {
        RoleWuxing wuxing = getRoleWuXingByType(userRoleId, type);
        if (wuxing == null) {
            return null;
        }

        // 五行配置
        HuanShiMoShenJiChuBiaoConfig config = huanShiMoShenJiChuBiaoConfigExportService.getWuXingConfigByTypeAndLevel(type, wuxing.getWuxingLevel());
        if (config == null) {
            return null;
        }

        return new Object[] { type, config.getId(), getZfzValue(wuxing, config), wuxing.getLastSjTime() };
    }

    public List<Integer> getRoleWuXings(Long userRoleId) {
        List<RoleWuxing> list = roleWuxingDao.cacheLoadAll(userRoleId);
        if (list == null || list.size() <= 0) {
            return null;
        }
        List<Integer> configList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
        	RoleWuxing wuxing = list.get(i);
            HuanShiMoShenJiChuBiaoConfig config = huanShiMoShenJiChuBiaoConfigExportService.getWuXingConfigByTypeAndLevel(wuxing.getWuxingType(), wuxing.getWuxingLevel());
        	if(config == null){
        		continue;
        	}
        	configList.add(config.getId());
		}
        return configList;
    }

    public WuXingPublicConfig getPublicConfig() {
        return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.WUXING);
    }

    /**
     * 获得五行的属性
     */
    public Map<String, Long> getWuXingAttrs(Long userRoleId, List<Integer> list) {

        if (list == null || list.size() <= 0) {
            return null;
        }
        Map<String, Long> attrs = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            // 五行配置
            HuanShiMoShenJiChuBiaoConfig config = huanShiMoShenJiChuBiaoConfigExportService.loadById(list.get(i));
            if (config == null) {
                continue;
            }
            ObjectUtil.longMapAdd(attrs, config.getAttrs());
        }
        return attrs;
    }

    /************************************ 五行魔神技能 ************************************/
    /**
     * 获取玩家五行魔神技能集合
     * 
     * @param userRoleId
     * @return
     */
    private List<RoleWuxingSkill> getRoleWxSkills(Long userRoleId) {
        return roleWuxingSkillDao.cacheLoadAll(userRoleId);
    }

    /**
     * 根据五行类型获取玩家技能对象
     * 
     * @param userRoleId
     * @param type
     * @return
     * 
     */
    private RoleWuxingSkill getRoleWxSkillByType(Long userRoleId, int type) {
        List<RoleWuxingSkill> skill_list = roleWuxingSkillDao.cacheLoadAll(userRoleId, new WuxingSkillFilter(type));
        if (ObjectUtil.isEmpty(skill_list)) {
            return null;
        }
        return skill_list.get(0);
    }

    /**
     * 获取玩家五行魔神技能信息
     * 
     * @param userRoleId
     * @return
     */
    public Object[] getWxSkillInfo(Long userRoleId) {
        List<RoleWuxingSkill> roleWxSkills = getRoleWxSkills(userRoleId);
        if (!ObjectUtil.isEmpty(roleWxSkills)) {
            List<Integer> rsList = new ArrayList<>();
            for (RoleWuxingSkill roleWxSkill : roleWxSkills) {
                rsList.addAll(roleWxSkill.findRoleWxSkillIds());
            }
            return rsList.toArray();
        } else {
            return null;
        }
    }

    /**
     * 玩家升级五行魔神技能
     * 
     * @param userRoleId
     * @param skillId 要升级的魔神技能配置id
     */
    public Object[] upLevelWxSkill(Long userRoleId, int skillId) {
        MoShenJiNengBiaoConfig config = moShenJiNengBiaoConfigExportService.loadById(skillId);
        if (null == config) {
            return AppErrorCode.CONFIG_ERROR;
        }
        int s_type = config.getType();
        RoleWuxingSkill roleWxSkill = getRoleWxSkillByType(userRoleId, s_type);

        /* 技能有效性检验 */
        Integer oldSkillId = null;
        int s_seq = config.getSeq();
        if (null != roleWxSkill) {
            oldSkillId = roleWxSkill.getWxSkillIdBySeq(s_seq);
        }
        int oldLevel = 0;
        MoShenJiNengBiaoConfig config2 = moShenJiNengBiaoConfigExportService.loadById(oldSkillId);
        if (null != config2) {
            oldLevel = config2.getLevel();
        }
        /* 最大等级限制校验 */
        int skillMaxLevel = moShenJiNengBiaoConfigExportService.getWxSkillMaxLevel(s_type, s_seq);
        if (oldLevel >= skillMaxLevel) {
            return AppErrorCode.WX_SKILL_MAX_LEVEL;
        }
        ++oldLevel;
        int s_level = config.getLevel();
        if (s_level != oldLevel) {
            return AppErrorCode.WX_SKILL_NO_LEARN;
        }

        /* 技能五行阶级限制校验 */
        int roleWxLevel = 0;
        RoleWuxing roleWuxing = getRoleWuXingByType(userRoleId, s_type);
        if (null != roleWuxing) {
            roleWxLevel = roleWuxing.getWuxingLevel();
        }
        int s_limit_wxLevel = config.getLimit();
        if (s_limit_wxLevel > roleWxLevel) {
            return AppErrorCode.WX_SKILL_LIMIT_LEVEL;
        }

        /* 道具消耗校验 */
        List<String> goodsIdList = moShenJiNengBiaoConfigExportService.getConsumeIds(config.getNeedItem());
        if (ObjectUtil.isEmpty(goodsIdList)) {
            return AppErrorCode.CONFIG_ERROR;
        }
        Map<String, Integer> itemMap = new HashMap<>();
        int needCount = config.getItemCount();
        for (String goodsId : goodsIdList) {
            int itemCnt = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
            if (itemCnt >= needCount) {
                itemMap.put(goodsId, needCount);
                needCount = 0;
                break;
            } else if (itemCnt > 0) {
                itemMap.put(goodsId, itemCnt);
                needCount -= itemCnt;
            }
        }
        if (needCount > 0) {
            return AppErrorCode.WX_SKILL_NOT_ENOUGH;
        }

        /* 金币消耗校验 */
        int money = config.getNeedMoney();
        Object[] isOb = accountExportService.isEnought(GoodsCategory.MONEY, money, userRoleId);
        if (null != isOb) {
            return AppErrorCode.JB_ERROR;
        }

        /* 扣除金币 */
        if (money > 0) {
            Object[] result = roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, money, userRoleId, LogPrintHandle.CONSUME_WUXING_SKILL_UPLEVEL, true, LogPrintHandle.CBZ_WUXING_SKILL_UPLEVEL);
            if (result != null) {
                return result;
            }
        }
        /* 扣除道具 */
        roleBagExportService.removeBagItemByGoods(itemMap, userRoleId, GoodsSource.CONSUME_WX_SKILL_UPLEVEL, true, true);

        /* 更新玩家五行技能数据 */
        long nowTime = GameSystemTime.getSystemMillTime();
        if (null == roleWxSkill) {
            roleWxSkill = new RoleWuxingSkill();
            roleWxSkill.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
            roleWxSkill.setUserRoleId(userRoleId);
            roleWxSkill.setWuxingType(s_type);
            roleWxSkill.setCreateTime(nowTime);
            roleWxSkill.updateSkillId(s_seq, skillId);
            roleWxSkill.setUpdateTime(nowTime);
            roleWuxingSkillDao.cacheInsert(roleWxSkill, userRoleId);
        } else {
            roleWxSkill.updateSkillId(s_seq, skillId);
            roleWxSkill.setUpdateTime(nowTime);
            roleWuxingSkillDao.cacheUpdate(roleWxSkill, userRoleId);
        }
        /* 刷新玩家五行技能附带的属性 */
        BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_WX_SKILL_CHARGE, getWuXingSkillAttrs(userRoleId));
        return new Object[] { AppErrorCode.SUCCESS, skillId };
    }

    /**
     * 初始化玩家五行魔神技能数据
     * 
     * @param userRoleId
     * @return
     */
    public List<RoleWuxingSkill> initRoleWuxingSkill(Long userRoleId) {
        return roleWuxingSkillDao.initRoleWuxingSkill(userRoleId);
    }

    /**
     * 获取五行魔神技能增加的属性集合
     * 
     * @param userRoleId
     * @return
     * 
     */
    public Map<String, Long> getWuXingSkillAttrs(Long userRoleId) {
        List<RoleWuxingSkill> roleWxSkills = getRoleWxSkills(userRoleId);
        if (ObjectUtil.isEmpty(roleWxSkills)) {
            return null;
        }
        Map<String, Long> attrsMap = new HashMap<>();
        for (RoleWuxingSkill roleWxSkill : roleWxSkills) {
            List<Integer> roleWxSkillIds = roleWxSkill.findRoleWxSkillIds();
            if (ObjectUtil.isEmpty(roleWxSkillIds)) {
                continue;
            }
            for (Integer wxSkillId : roleWxSkillIds) {
                MoShenJiNengBiaoConfig skillConfig = moShenJiNengBiaoConfigExportService.loadById(wxSkillId);
                if (null == skillConfig) {
                    continue;
                }
                ObjectUtil.longMapAdd(attrsMap, skillConfig.getAttrMap());
            }
        }
        return attrsMap;
    }

    /************************************ 五行魔神精魄系统 ************************************/
    /**
     * 初始化玩家魔神背包的所有精魄数据
     * 
     * @param userRoleId
     * @return
     */
    public List<RoleWuxingJingpoItem> initRoleWxJpItemList(Long userRoleId) {
        return roleWuxingJingpoItemDao.initRoleWuxingJingpoItem(userRoleId);
    }

    /**
     * 初始化玩家魔神身上的精魄数据
     * 
     * @param userRoleId
     * @return
     */
    public List<RoleWuxingJingpo> initRoleBodyWxJpList(Long userRoleId) {
        return roleWuxingJingpoDao.initRoleWuxingJingpo(userRoleId);
    }

    /**
     * 获取魔神身上精魄的属性集合
     * 
     * @param userRoleId
     * @return
     */
    public Map<String, Long> getWuXingJpAttrs(Long userRoleId) {
        Map<String, Long> attrsMap = new HashMap<>();
        for (int wxtype : WuxingMoshenType.findAllWuxingMoshen()) {
            ObjectUtil.longMapAdd(attrsMap, calcWxJpBodyAttrMap(userRoleId, wxtype));
        }
        return attrsMap;
    }

    /**
     * 获取玩家五行魔神背包的精魄数据
     * 
     * @param userRoleId
     * @return
     */
    public Object[] getRoleWxJpBagData(Long userRoleId) {
        RoleWuxingJingpo roleWxJp = getRoleWxJp(userRoleId);
        List<Object[]> voList = null;
        List<RoleWuxingJingpoItem> wxJpBagList = getRoleWxJpItemByPosition(userRoleId, GameConstants.WX_JP_BAG_TYPE, GameConstants.WX_JP_BAG_SLOT);
        if (!ObjectUtil.isEmpty(wxJpBagList)) {
            voList = new ArrayList<>();
            for (RoleWuxingJingpoItem item : wxJpBagList) {
                voList.add(createWxJpVo(item));
            }
        }
        return new Object[] { roleWxJp.getOpenSlot(), roleWxJp.getLiemingLevel(), roleWxJp.getMoshenJinghua(), voList == null ? null : voList.toArray() };
    }

    /**
     * 获取玩家五行魔神身上的精魄数据
     * 
     * @param userRoleId
     * @param wxtype 五行魔神类型
     * @return
     */
    public Object[] getRoleWxJpBodyData(Long userRoleId, int wxtype) {
        if (!WuxingMoshenType.isWxMoshen(wxtype)) {
            return new Object[] { 0, "请求参数错误" };
        }
        int wxMoshenOpenSlot = 0;
        RoleWuxingJingpo roleWxJp = getRoleWxJp(userRoleId);
        Object[] jpSlot = roleWxJp.getBodyJingpoMap().get(wxtype);
        for (Object slot : jpSlot) {
            if (GameConstants.WX_JP_SLOT_OPEN_YES == (Integer) slot) {
                ++wxMoshenOpenSlot;
            }
        }
        List<Object[]> voList = null;
        List<RoleWuxingJingpoItem> wxJpBodyList = getRoleWxJpItemByPosition(userRoleId, wxtype, null);
        if (!ObjectUtil.isEmpty(wxJpBodyList)) {
            voList = new ArrayList<>();
            for (RoleWuxingJingpoItem item : wxJpBodyList) {
                voList.add(createWxJpVo(item));
            }
        }
        return new Object[] { wxtype, wxMoshenOpenSlot, null == voList ? null : voList.toArray(), calcWxJpBodyAttrMap(userRoleId, wxtype) };
    }

    /**
     * 开启魔神精魄背包格位
     * 
     * @param userRoleId
     * @param slot 开启格位
     * @return
     */
    public Object[] openWxJpBagSlot(Long userRoleId, int slot) {
        RoleWuxingJingpo roleWxJp = getRoleWxJp(userRoleId);
        int nextSlotOpen = roleWxJp.getOpenSlot() + 1;
        if (nextSlotOpen != slot) {
            return AppErrorCode.WX_JP_OPEN_SLOT_ERROR;
        }
        Map<Integer, MoshenJingpoKongWeiConfig> jpBagKwMap = moshenJingpoConfigExportService.getWxJpBagKwMap();
        if(ObjectUtil.isEmpty(jpBagKwMap)){
            return AppErrorCode.CONFIG_ERROR;
        }
        if (slot > jpBagKwMap.size()) {
            return AppErrorCode.WX_JP_BAG_SLOT_OVER;
        }
        MoshenJingpoKongWeiConfig jpKwConfig = jpBagKwMap.get(slot);
        int kwType = jpKwConfig.getNeedType();
        if (GameConstants.WX_JP_SLOT_OPEN_GOLD == kwType) {
            int gold = jpKwConfig.getNeedCount();
            /* 元宝消耗校验 */
            Object[] isOb = accountExportService.isEnought(GoodsCategory.GOLD, gold, userRoleId);
            if (null != isOb) {
                return AppErrorCode.YB_ERROR;
            }
            /* 元宝金币 */
            if (gold > 0) {
                Object[] result = roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, gold, userRoleId, LogPrintHandle.CONSUME_WUXING_OPEN_SLOT_BAG, true, LogPrintHandle.CBZ_WUXING_OPEN_SLOT_BAG);
                if (result != null) {
                    return result;
                } else {
                    if (PlatformConstants.isQQ()) {
                        BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_YB, gold, LogPrintHandle.CBZ_WUXING_OPEN_SLOT_BAG, QQXiaoFeiType.CONSUME_WUXING_JP_BAG_OPEN_SLOT, 1 });
                    }
                }
            }
            /* 数据更新 */
            roleWxJp.setOpenSlot(nextSlotOpen);
            roleWxJp.setUpdateTime(GameSystemTime.getSystemMillTime());
            roleWuxingJingpoDao.cacheUpdate(roleWxJp, userRoleId);
        }

        return new Object[] { AppErrorCode.SUCCESS, slot };
    }

    /**
     * 开启魔神身上的格位
     * 
     * @param userRoleId
     * @param position 开启的魔神位置
     * @param slot 开启的格位号
     * @return
     */
    public Object[] openWxJpBodySlot(Long userRoleId, int position, int slot) {
        RoleWuxingJingpo roleWxJp = getRoleWxJp(userRoleId);
        Map<Integer, Object[]> bodyJpMap = roleWxJp.getBodyJingpoMap();
        Object[] jpData = bodyJpMap.get(position);
        int len = jpData == null ? 0 : jpData.length;
        if (0 >= len || slot > len) {
            return null;
        }
        long jpStatus = LongUtils.obj2long(jpData[slot - 1]);
        if (GameConstants.WX_JP_SLOT_OPEN_YES == jpStatus) {
            return AppErrorCode.WX_JP_SLOT_HAS_OPEN;
        }

        MoshenJingpoKongWeiConfig jpKwConfig = moshenJingpoConfigExportService.loadMsJpBodyKwBySlot(slot);
        if(null == jpKwConfig){
            return AppErrorCode.CONFIG_ERROR;
        }

        int kwType = jpKwConfig.getNeedType();
        if (GameConstants.WX_JP_SLOT_OPEN_GOLD == kwType) {
            int gold = jpKwConfig.getNeedCount();
            /* 元宝消耗校验 */
            Object[] isOb = accountExportService.isEnought(GoodsCategory.GOLD, gold, userRoleId);
            if (null != isOb) {
                return AppErrorCode.YB_ERROR;
            }
            /* 元宝金币 */
            if (gold > 0) {
                Object[] result = roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, gold, userRoleId, LogPrintHandle.CONSUME_WUXING_OPEN_SLOT_BODY, true, LogPrintHandle.CBZ_WUXING_OPEN_SLOT_BODY);
                if (result != null) {
                    return result;
                } else {
                    if (PlatformConstants.isQQ()) {
                        BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_YB, gold, LogPrintHandle.CBZ_WUXING_OPEN_SLOT_BODY, QQXiaoFeiType.CONSUME_WUXING_JP_BODY_OPEN_SLOT, 1 });
                    }
                }
            }
            /* 数据更新 */
            jpData[slot - 1] = GameConstants.WX_JP_SLOT_OPEN_YES;
            bodyJpMap.put(position, jpData);
            roleWxJp.setBodyData(JSON.toJSONString(bodyJpMap));
            roleWxJp.setUpdateTime(GameSystemTime.getSystemMillTime());
            roleWuxingJingpoDao.cacheUpdate(roleWxJp, userRoleId);
        }

        return new Object[] { AppErrorCode.SUCCESS, position, slot };
    }

    /**
     * 吞噬魔神精魄
     * 
     * @param userRoleId
     * @param mergeGuid 源精魄(吞噬的精魄)
     * @param beMergeGuids 目标精魄数组(被合成吞噬掉的精魄数组)
     * @param isAuto 是否强制吃掉
     * @return
     */
    public Object[] mergeWxJpOnBag(Long userRoleId, long mergeGuid, Object[] beMergeGuids, boolean isAuto) {
        int beMergeLen = beMergeGuids == null ? 0 : beMergeGuids.length;
        if (0 >= beMergeLen) {
            return AppErrorCode.WX_JP_MERGE_GUID_ERROR;
        }

        RoleWuxingJingpoItem roleWxJpItem = getRoleWxJpItem(userRoleId, mergeGuid);
        if (null == roleWxJpItem) {
            return AppErrorCode.WX_JP_IS_NULL;
        }

        MoshenJingpoConfig jpConfig = moshenJingpoConfigExportService.loadMsJpById(roleWxJpItem.getGoodsId());
        if(null == jpConfig){
            return AppErrorCode.CONFIG_ERROR;
        }

        /* 经验精魄不能吞噬其他精魄 */
        if (GameConstants.WX_JP_JINGPO_TYPE_EXP == jpConfig.getType()) {
            return AppErrorCode.WX_JP_MERGE_GUID_ERROR;
        }

        /* 该品质的精魄最大等级 */
        int jpLevel = jpConfig.getLevel();
        int jpQuality = jpConfig.getQuality();
        int maxLevel = getWxJpMaxLevelByQuality(jpQuality);
        if (jpLevel >= maxLevel) {
            return AppErrorCode.WX_JP_MERGE_LEVEL_MAX;
        }

        /* 被吞噬的只能是背包的精魄,身上的精魄不允许吞噬被吞噬 */
        if (checkWxJpOnBody(userRoleId, beMergeGuids)) {
            return AppErrorCode.WX_JP_MERGE_GUID_ERROR;
        }

        int eatLevel = jpLevel;
        int goosId = roleWxJpItem.getGoodsId();
        int attrType = jpConfig.getAttrType();
        int eatExp = roleWxJpItem.getEatExp();
        int upLevelExp = jpConfig.getNeedExp();

        /* 等到品阶的最大等级,经验溢出标识 */
        int overUseCount = 0;
        for (Object guid : beMergeGuids) {
            RoleWuxingJingpoItem tmpItem = getRoleWxJpItem(userRoleId, LongUtils.obj2long(guid));
            if(null == tmpItem){
                return AppErrorCode.CONFIG_ERROR;
            }

            MoshenJingpoConfig tmpJpConfig = moshenJingpoConfigExportService.loadMsJpById(tmpItem.getGoodsId());
            if(null == tmpJpConfig){
                return AppErrorCode.CONFIG_ERROR;
            }

            int tmpExp = tmpJpConfig.getExp() + tmpItem.getEatExp();
            while (tmpExp >= (upLevelExp - eatExp)) {
                tmpExp -= (upLevelExp - eatExp);
                eatExp = 0;
                ++eatLevel;
                MoshenJingpoConfig nextWxJpConfig = moshenJingpoConfigExportService.getWxJpByQualityAndLevel(jpQuality, eatLevel, attrType);
                if(null == nextWxJpConfig){
                    return AppErrorCode.CONFIG_ERROR;
                }
                goosId = nextWxJpConfig.getId();
                upLevelExp = nextWxJpConfig.getNeedExp();
                if (eatLevel >= maxLevel) {
                    eatExp = 0;
                    break;
                }
            }
            ++overUseCount;
            eatExp += tmpExp;
            if (eatLevel >= maxLevel) {
                eatExp = 0;
                break;
            }
        }

        /* 精魄升级经验溢出 */
        if (overUseCount != beMergeLen && !isAuto) {
            return AppErrorCode.WX_JP_MERGE_EXP_OVER;
        }

        /* 更新数据 */
        roleWxJpItem.setGoodsId(goosId);
        roleWxJpItem.setEatExp(eatExp);
        roleWxJpItem.setUpdateTime(GameSystemTime.getSystemMillTime());
        roleWuxingJingpoItemDao.cacheUpdate(roleWxJpItem, userRoleId);

        /* 删除被吞噬的精魄 */
        for (Object removeGuid : beMergeGuids) {
            removeWxBagJpItem(userRoleId, LongUtils.obj2long(removeGuid));
        }

        return new Object[] { AppErrorCode.SUCCESS, createWxJpVo(roleWxJpItem), beMergeGuids };
    }

    /**
     * 镶嵌魔神精魄到身上
     * 
     * @param userRoleId
     * @param sourceGuid 源精魄:背包精魄guid
     * @param targetPosition 镶嵌目标位置
     * @param targetSlot 镶嵌目标格位
     * @return
     */
    public Object[] putOnWxJp(Long userRoleId, long sourceGuid, int targetPosition, int targetSlot) {

        /* 目标位置必须在魔神身上&&目标孔位必须是魔神身上的孔位 */
        RoleWuxingJingpo targetWxJp = getRoleWxJp(userRoleId);
        Object[] targetJpPosition = targetWxJp.getBodyJingpoMap().get(targetPosition);

        /* 目标位置错误 */
        if (null == targetJpPosition) {
            return AppErrorCode.WX_JP_POSITION_ERROR;
        }

        if (0 > targetSlot || targetJpPosition.length < targetSlot) {
            return AppErrorCode.WX_JP_POSITION_ERROR;
        }

        Object _targetJpSlot = targetJpPosition[targetSlot - 1];
        if (null == _targetJpSlot) {
            return AppErrorCode.WX_JP_POSITION_ERROR;
        }

        /* 目标孔位是否开启 */
        int targetJpSlot = (Integer) _targetJpSlot;
        if (GameConstants.WX_JP_SLOT_OPEN_YES != targetJpSlot) {
            return AppErrorCode.WX_JP_SLOT_NOT_OPEN;
        }

        /* 目标孔位上已镶嵌精魄 */
        List<RoleWuxingJingpoItem> targetWxJpItem = getRoleWxJpItemByPosition(userRoleId, targetPosition, targetSlot);
        if (!ObjectUtil.isEmpty(targetWxJpItem)) {
            return AppErrorCode.WX_JP_HAS_PUT_ON;
        }

        RoleWuxingJingpoItem sourceWxJpItem = getRoleWxJpItem(userRoleId, sourceGuid);
        if (null == sourceWxJpItem) {
            return AppErrorCode.WX_JP_IS_NULL;
        }

        int sourceId = sourceWxJpItem.getGoodsId();
        MoshenJingpoConfig sourceConfig = moshenJingpoConfigExportService.loadMsJpById(sourceId);
        if (null == sourceConfig) {
            return AppErrorCode.WX_JP_IS_NULL;
        }

        if (GameConstants.WX_JP_JINGPO_TYPE_EXP == sourceConfig.getType()) {
            return AppErrorCode.WX_JP_EXP_NOT_PUT;
        }

        /* 源精魄位置必须是背包&&源精魄孔位也必须是背包格位 */
        int sourcePosition = sourceWxJpItem.getPosition();
        int sourceSlot = sourceWxJpItem.getSlot();
        if (GameConstants.WX_JP_BAG_TYPE != sourcePosition || sourceSlot != GameConstants.WX_JP_BAG_SLOT) {
            return AppErrorCode.WX_JP_POSITION_ERROR;
        }

        int sourceAttyType = getWxJpAttrTypeById(sourceWxJpItem.getGoodsId());
        if (!checkBodyJpAttrType(userRoleId, targetPosition, sourceAttyType)) {
            return AppErrorCode.WX_JP_HAS_ATTYTYPE;
        }

        /* 更新数据 */
        sourceWxJpItem.setPosition(targetPosition);
        sourceWxJpItem.setSlot(targetSlot);
        sourceWxJpItem.setUpdateTime(GameSystemTime.getSystemMillTime());
        roleWuxingJingpoItemDao.cacheUpdate(sourceWxJpItem, userRoleId);

        BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_WX_JP_CHARGE, getWuXingJpAttrs(userRoleId));

        return new Object[] { AppErrorCode.SUCCESS, new Object[] { sourceGuid, targetPosition, targetSlot }, calcWxJpBodyAttrMap(userRoleId, targetPosition) };
    }

    /**
     * 卸下魔神身上的精魄
     * 
     * @param userRoleId
     * @param sourceGuid 精魄的guid
     * @return
     */
    public Object[] takeOffWxJp(Long userRoleId, long sourceGuid) {
        RoleWuxingJingpoItem sourceWxJpItem = getRoleWxJpItem(userRoleId, sourceGuid);
        int sourceJpPositon = sourceWxJpItem.getPosition();
        int sourceJpSlot = sourceWxJpItem.getSlot();
        if (sourceJpPositon == GameConstants.WX_JP_BAG_TYPE || sourceJpSlot == GameConstants.WX_JP_BAG_SLOT) {
            return AppErrorCode.WX_JP_BAG_NO_TAKE_OFF;
        }

        /* 背包格位是否充足 */
        if (!checkWxJpBagSpaceEnough(userRoleId)) {
            return AppErrorCode.WX_JP_BAG_SLOT_OVER;
        }

        sourceWxJpItem.setPosition(GameConstants.WX_JP_BAG_TYPE);
        sourceWxJpItem.setSlot(GameConstants.WX_JP_BAG_SLOT);
        sourceWxJpItem.setUpdateTime(GameSystemTime.getSystemMillTime());
        roleWuxingJingpoItemDao.cacheUpdate(sourceWxJpItem, userRoleId);

        BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_WX_JP_CHARGE, getWuXingJpAttrs(userRoleId));

        return new Object[] { AppErrorCode.SUCCESS, new Object[] { sourceGuid, GameConstants.WX_JP_BAG_TYPE, GameConstants.WX_JP_BAG_SLOT }, calcWxJpBodyAttrMap(userRoleId, sourceJpPositon) };
    }

    /**
     * 普通获取精魄
     * 
     * @param userRoleId
     * @return
     */
    public Object[] normalGetWxJp(Long userRoleId) {
        if (!checkWxJpBagSpaceEnough(userRoleId)) {
            return AppErrorCode.WX_JP_BAG_SLOT_OVER;
        }
        RoleWuxingJingpo roleWxJp = getRoleWxJp(userRoleId);
        int lmLevel = roleWxJp.getLiemingLevel();
        MoshenJingpoDiaoLuoConfig jpDlConfig = moshenJingpoConfigExportService.loadMsJpDialLuoByLevel(lmLevel);
        if (null == jpDlConfig) {
            return AppErrorCode.CONFIG_ERROR;
        }
        /* 金币消耗校验 */
        long money = jpDlConfig.getNeedMoney();
        Object[] isOb = accountExportService.isEnought(GoodsCategory.MONEY, money, userRoleId);
        if (null != isOb) {
            return AppErrorCode.JB_ERROR;
        }
        /* 扣除金币 */
        if (money > 0) {
            Object[] result = roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, (int) money, userRoleId, LogPrintHandle.CONSUME_WUXING_GET_JP, true, LogPrintHandle.CBZ_WUXING_GET_JP);
            if (result != null) {
                return result;
            }
        }
        /* 获取的精魄,存入魔神背包 */
        int eatExp = 0;
        long guid = IdFactory.getInstance().generateId(ServerIdType.COMMON);
        Integer goodsId = Lottery.getRandomKeyByInteger(jpDlConfig.getProMap());
        RoleWuxingJingpoItem roleWxJpItem = insertWxBagJpItem(userRoleId, guid, goodsId, eatExp);
        if (null == roleWxJpItem) {
            return AppErrorCode.WX_JP_INSERT_BAG_ERROR;
        }
        /* 刷新法宝等级 */
        MoshenJingpoShuaXinConfig jpSxConfig = moshenJingpoConfigExportService.loadMsJpShuaXinByLevel(lmLevel);
        if(null == jpSxConfig){
            return AppErrorCode.CONFIG_ERROR;
        }
        int newLmLevel = Lottery.getRandomKeyByInteger(jpSxConfig.getProMap());
        int jinghua = jpDlConfig.getJinghua();
        roleWxJp.setMoshenJinghua(roleWxJp.getMoshenJinghua() + jinghua);
        roleWxJp.setLiemingLevel(newLmLevel);
        roleWxJp.setUpdateTime(GameSystemTime.getSystemMillTime());
        roleWuxingJingpoDao.cacheUpdate(roleWxJp, userRoleId);
        return new Object[] { AppErrorCode.SUCCESS, newLmLevel, jinghua, createWxJpVo(roleWxJpItem) };
    }

    /**
     * 一键获取精魄
     * 
     * @param userRoleId
     * @return
     */
    public Object[] autoGetWxJp(Long userRoleId) {
        WuXingJingpoPublicConfig jpPubliecConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_WUXING_JINGPO);
        int autoCount = jpPubliecConfig.getAutoCount();

        RoleWuxingJingpo roleWxJp = getRoleWxJp(userRoleId);
        int lmLevel = roleWxJp.getLiemingLevel();

        List<Object[]> voList = new ArrayList<>();
        int jinghua = 0;
        for (int c = 1; c <= autoCount; c++) {
            /* 检查魔神背包剩余空间 */
            if (!checkWxJpBagSpaceEnough(userRoleId)) {
                if (1 == c) {
                    return AppErrorCode.WX_JP_BAG_SLOT_OVER;
                } else {
                    break;
                }
            }
            MoshenJingpoDiaoLuoConfig jpDlConfig = moshenJingpoConfigExportService.loadMsJpDialLuoByLevel(lmLevel);
            if(null == jpDlConfig){
                return AppErrorCode.CONFIG_ERROR;
            }
            /* 金币消耗校验 */
            long money = jpDlConfig.getNeedMoney();
            Object[] isOb = accountExportService.isEnought(GoodsCategory.MONEY, money, userRoleId);
            if (null != isOb) {
                if (1 == c) {
                    return AppErrorCode.JB_ERROR;
                } else {
                    break;
                }
            }

            /* 扣除金币 */
            if (money > 0) {
                Object[] result = roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, (int) money, userRoleId, LogPrintHandle.CONSUME_WUXING_GET_JP, true, LogPrintHandle.CBZ_WUXING_GET_JP);
                if (result != null) {
                    if (1 == c) {
                        return result;
                    } else {
                        break;
                    }
                }
            }

            /* 获取的精魄,存入魔神背包 */
            int eatExp = 0;
            long guid = IdFactory.getInstance().generateId(ServerIdType.COMMON);
            Integer goodsId = Lottery.getRandomKeyByInteger(jpDlConfig.getProMap());
            RoleWuxingJingpoItem roleWxJpItem = insertWxBagJpItem(userRoleId, guid, goodsId, eatExp);
            if (null == roleWxJpItem) {
                if (1 == c) {
                    return AppErrorCode.WX_JP_INSERT_BAG_ERROR;
                } else {
                    break;
                }
            }

            /* 刷新法宝等级 */
            MoshenJingpoShuaXinConfig jpSxConfig = moshenJingpoConfigExportService.loadMsJpShuaXinByLevel(lmLevel);
            if(null == jpSxConfig){
                return AppErrorCode.CONFIG_ERROR;
            }
            lmLevel = Lottery.getRandomKeyByInteger(jpSxConfig.getProMap());
            jinghua += jpDlConfig.getJinghua();
            voList.add(createWxJpVo(roleWxJpItem));
        }
        /* 更新数据 */
        roleWxJp.setMoshenJinghua(roleWxJp.getMoshenJinghua() + jinghua);
        roleWxJp.setLiemingLevel(lmLevel);
        roleWxJp.setUpdateTime(GameSystemTime.getSystemMillTime());
        roleWuxingJingpoDao.cacheUpdate(roleWxJp, userRoleId);

        return new Object[] { AppErrorCode.SUCCESS, lmLevel, jinghua, ObjectUtil.isEmpty(voList) ? null : voList.toArray() };
    }

    /**
     * 窥探天机
     * 
     * @param userRoleId
     * @return
     */
    public Object[] kttjWxJp(Long userRoleId) {
        RoleWuxingJingpo roleWxJp = getRoleWxJp(userRoleId);
        WuXingJingpoPublicConfig jpPubliecConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_WUXING_JINGPO);
        int kttjLevel = jpPubliecConfig.getKttjLevel();
        if (roleWxJp.getLiemingLevel() >= kttjLevel) {
            return null;
        }
        int gold = jpPubliecConfig.getNeedGold();
        /* 元宝消耗校验 */
        Object[] isOb = accountExportService.isEnought(GoodsCategory.GOLD, gold, userRoleId);
        if (null != isOb) {
            return AppErrorCode.YB_ERROR;
        }
        /* 元宝金币 */
        if (gold > 0) {
            Object[] result = roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, gold, userRoleId, LogPrintHandle.CONSUME_WUXING_KTTJ, true, LogPrintHandle.CBZ_WUXING_KTTJ);
            if (result != null) {
                return result;
            } else {
                if (PlatformConstants.isQQ()) {
                    BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_YB, gold, LogPrintHandle.CBZ_WUXING_KTTJ, QQXiaoFeiType.CONSUME_WUXING_JP_KTTJ, 1 });
                }
            }
        }
        /* 更新数据 */
        roleWxJp.setLiemingLevel(kttjLevel);
        roleWxJp.setUpdateTime(GameSystemTime.getSystemMillTime());
        roleWuxingJingpoDao.cacheUpdate(roleWxJp, userRoleId);
        return new Object[] { AppErrorCode.SUCCESS, kttjLevel };
    }

    /**
     * 使用精华兑换精魄
     * 
     * @param userRoleId
     * @param dhId 兑换精魄配置编号
     * @return
     */
    public Object[] exchangeWxJp(Long userRoleId, int dhId) {
        MoshenJingpoDuiHuanConfig msJpDhConfig = moshenJingpoConfigExportService.loadMsJpDuiHuanById(dhId);
        if (null == msJpDhConfig) {
            return AppErrorCode.WX_JP_IS_NULL;
        }
        
        if (!checkWxJpBagSpaceEnough(userRoleId)) {
            return AppErrorCode.WX_JP_BAG_SLOT_OVER;
        }
        int jinghua = msJpDhConfig.getNeedjinghua();
        RoleWuxingJingpo roleWxJp = getRoleWxJp(userRoleId);
        long roleWxJinghua = roleWxJp.getMoshenJinghua();
        if (jinghua > roleWxJinghua) {
            return AppErrorCode.WX_JP_JING_HUA_NOT_ENOUGH;
        }
        /* 更新数据 */
        roleWxJinghua -= jinghua;
        roleWxJp.setMoshenJinghua(roleWxJinghua);
        roleWxJp.setUpdateTime(GameSystemTime.getSystemMillTime());
        roleWuxingJingpoDao.cacheUpdate(roleWxJp, userRoleId);
        /* 精魄存入背包 */
        RoleWuxingJingpoItem roleWxJpItem = insertWxBagJpItem(userRoleId, IdFactory.getInstance().generateId(ServerIdType.COMMON), dhId, 0);
        if (null == roleWxJpItem) {
            return AppErrorCode.WX_JP_INSERT_BAG_ERROR;
        }
        return new Object[] { AppErrorCode.SUCCESS, dhId, createWxJpVo(roleWxJpItem) };
    }

    /**
     * 获取品阶的最大等级
     * 
     * @param quality
     * @return
     */
    private int getWxJpMaxLevelByQuality(int quality) {
        int maxLevel = 0;
        Map<Integer, MoshenJingpoConfig> jpConfigMap = moshenJingpoConfigExportService.getWxJpConfigMap();
        if (!ObjectUtil.isEmpty(jpConfigMap)) {
            for (MoshenJingpoConfig jpConfig : jpConfigMap.values()) {
                if (quality != jpConfig.getQuality() || GameConstants.WX_JP_JINGPO_TYPE_EXP == jpConfig.getType()) {
                    continue;
                }
                if (jpConfig.getLevel() > maxLevel) {
                    maxLevel = jpConfig.getLevel();
                }
            }
        }
        return maxLevel;
    }

    /**
     * 获取精魄的属性类型
     * 
     * @param id
     * @return
     */
    private int getWxJpAttrTypeById(int id) {
        MoshenJingpoConfig jpConfig = moshenJingpoConfigExportService.loadMsJpById(id);
        return jpConfig == null ? 0 : jpConfig.getAttrType();
    }

    /**
     * 检查同一属性类型的精魄不能镶嵌在同一个魔神身上
     * 
     * @param userRoleId 玩家编号
     * @param wxType 魔神类型
     * @param sourceAttrType 准备镶嵌精魄的属性类型
     * @return true可以镶嵌;false=已存在同属性类型的精魄,不能镶嵌
     */
    private boolean checkBodyJpAttrType(Long userRoleId, int wxType, int sourceAttrType) {
        List<RoleWuxingJingpoItem> roleBodyJpItems = getRoleWxJpItemByPosition(userRoleId, wxType, null);
        if (!ObjectUtil.isEmpty(roleBodyJpItems)) {
            for (RoleWuxingJingpoItem roleJpItem : roleBodyJpItems) {
                int jpAttrType = getWxJpAttrTypeById(roleJpItem.getGoodsId());
                if (0 < jpAttrType && sourceAttrType == jpAttrType) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 初始化魔神身上精魄数据
     * 
     * @return
     */
    private String initWxBodyJpData(Long userRoleId) {
        Map<Integer, Object[]> bodyJingpoMap = new HashMap<>();

        Map<Integer, MoshenJingpoKongWeiConfig> wxJpBodyKwMap = moshenJingpoConfigExportService.getWxJpBodyKwMap();
        if (!ObjectUtil.isEmpty(wxJpBodyKwMap)) {
            for (int wxMoShenType : WuxingMoshenType.findAllWuxingMoshen()) {
                RoleWuxing roleWx = getRoleWuXingByType(userRoleId, wxMoShenType);
                int roleWxMoshenLevel = null == roleWx ? 0 : roleWx.getWuxingLevel();

                int tmp = 0;
                Object[] kongData = new Object[wxJpBodyKwMap.size()];
                for (MoshenJingpoKongWeiConfig bodyKw : wxJpBodyKwMap.values()) {
                    boolean openFlag = false;
                    if (1 == bodyKw.getNeedType() && bodyKw.getNeedCount() <= roleWxMoshenLevel) {
                        openFlag = true;
                    }
                    kongData[tmp] = openFlag ? GameConstants.WX_JP_SLOT_OPEN_YES : GameConstants.WX_JP_SLOT_OPEN_NO;
                    ++tmp;
                }
                bodyJingpoMap.put(wxMoShenType, kongData);
            }
        }
        return JSON.toJSONString(bodyJingpoMap);
    }

    /**
     * 获取魔神背包初始开启的最大格位数
     * 
     * @return
     */
    private int initOpenMaxSlot() {
        int openMaxSlot = 0;
        Map<Integer, MoshenJingpoKongWeiConfig> jpBagKWConfigMap = moshenJingpoConfigExportService.getWxJpBagKwMap();
        if (!ObjectUtil.isEmpty(jpBagKWConfigMap)) {
            for (MoshenJingpoKongWeiConfig jpKw : jpBagKWConfigMap.values()) {
                if (0 >= jpKw.getNeedCount()) {
                    ++openMaxSlot;
                }
            }
        }
        return openMaxSlot;
    }

    /**
     * 获取玩家五行魔神的精魄数据
     * 
     * @param userRoleId
     * @return
     */
    private RoleWuxingJingpo getRoleWxJp(Long userRoleId) {
        RoleWuxingJingpo roleWxJp = roleWuxingJingpoDao.cacheLoad(userRoleId, userRoleId);
        if (null == roleWxJp) {
            roleWxJp = new RoleWuxingJingpo();
            long timestamp = GameSystemTime.getSystemMillTime();
            roleWxJp.setUserRoleId(userRoleId);
            roleWxJp.setLiemingLevel(1);
            roleWxJp.setMoshenJinghua(0L);
            roleWxJp.setOpenSlot(initOpenMaxSlot());
            roleWxJp.setCreateTime(timestamp);
            roleWxJp.setUpdateTime(timestamp);
            roleWxJp.setBodyData(initWxBodyJpData(userRoleId));
            roleWuxingJingpoDao.cacheInsert(roleWxJp, userRoleId);
        }
        return roleWxJp;
    }

    /**
     * 根据不同的条件获取魔神背包的精魄列表
     * 
     * @param userRoleId
     * @return
     */
    private List<RoleWuxingJingpoItem> getRoleWxJpItemList(Long userRoleId, Long guid, Integer position, Integer slot) {
        List<RoleWuxingJingpoItem> roleWxJpItemList = roleWuxingJingpoItemDao.cacheLoadAll(userRoleId, new WuxingJingpoFilter(guid, position, slot));
        if (ObjectUtil.isEmpty(roleWxJpItemList)) {
            return null;
        }
        return roleWxJpItemList;
    }

    /**
     * 根据guid获取玩家魔神背包的精魄数据
     * 
     * @param userRoleId
     * @param guid
     * @return
     */
    private RoleWuxingJingpoItem getRoleWxJpItem(Long userRoleId, Long guid) {
        List<RoleWuxingJingpoItem> roleWxJpItemList = getRoleWxJpItemList(userRoleId, guid, null, null);
        if (ObjectUtil.isEmpty(roleWxJpItemList)) {
            return null;
        }
        return roleWxJpItemList.get(0);
    }

    /**
     * 根据精魄所在位置和格位获取玩家魔神背包的精魄数据
     * 
     * @param userRoleId
     * @param position(精魄所在位置-1:背包,1:金,2:木,3:土,4:水,5:火)
     * @param slot(精魄所在格位-1:背包,1,2,3,4,5,6,7)
     * @return
     */
    private List<RoleWuxingJingpoItem> getRoleWxJpItemByPosition(Long userRoleId, Integer position, Integer slot) {
        List<RoleWuxingJingpoItem> roleWxJpItemList = getRoleWxJpItemList(userRoleId, null, position, slot);
        if (ObjectUtil.isEmpty(roleWxJpItemList)) {
            return null;
        }
        return roleWxJpItemList;
    }

    /**
     * 创建魔神精魄数据vo
     * 
     * @param roleWxJpItem
     * @return
     */
    private Object[] createWxJpVo(RoleWuxingJingpoItem roleWxJpItem) {
        if (null == roleWxJpItem) {
            return null;
        }
        long guid = roleWxJpItem.getGuid();
        int goodsId = roleWxJpItem.getGoodsId();
        int eatExp = roleWxJpItem.getEatExp();
        int type = roleWxJpItem.getPosition();
        int kongwei = roleWxJpItem.getSlot();
        return new Object[] { guid, goodsId, eatExp, type, kongwei };
    }

    /**
     * 计算魔神身上所有精魄的属性集合(包含战斗力属性)
     * 
     * @param userRoleId
     * @param wxtype 魔神类型
     * @return
     */
    private Map<String, Long> calcWxJpBodyAttrMap(Long userRoleId, int wxtype) {
        Map<String, Long> attrMap = null;
        List<RoleWuxingJingpoItem> bodyJpList = getRoleWxJpItemByPosition(userRoleId, wxtype, null);
        if (!ObjectUtil.isEmpty(bodyJpList)) {
            attrMap = new HashMap<>();
            for (RoleWuxingJingpoItem roleWxJpItem : bodyJpList) {
                MoshenJingpoConfig jpConfig = moshenJingpoConfigExportService.loadMsJpById(roleWxJpItem.getGoodsId());
                if (null != jpConfig) {
                    ObjectUtil.longMapAdd(attrMap, jpConfig.getAttr());
                }
            }
        }
        return attrMap;
    }

    /**
     * 检查精魄是否在魔神身上
     * 
     * @param guid
     * @return
     */
    private boolean checkWxJpOnBody(Long userRoleId, Object[] guidData) {
        int len = guidData == null ? 0 : guidData.length;
        for (int i = 0; i < len; i++) {
            RoleWuxingJingpoItem roleWxJpItem = getRoleWxJpItem(userRoleId, LongUtils.obj2long(guidData[i]));
            if (null == roleWxJpItem) {
                return false;
            }
            if (roleWxJpItem.getPosition() == GameConstants.WX_JP_BAG_TYPE) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查魔神背包空间是否足够
     * 
     * @param userRoleId
     * @return true=足够;false=不足
     */
    private boolean checkWxJpBagSpaceEnough(Long userRoleId) {
        RoleWuxingJingpo roleWxJp = getRoleWxJp(userRoleId);
        List<RoleWuxingJingpoItem> jpBagList = getRoleWxJpItemByPosition(userRoleId, GameConstants.WX_JP_BAG_TYPE, GameConstants.WX_JP_BAG_SLOT);
        int jpBagCount = ObjectUtil.isEmpty(jpBagList) ? 0 : jpBagList.size();
        return roleWxJp.getOpenSlot() > jpBagCount;
    }

    /**
     * 创建魔神背包精魄数据
     * 
     * @param userRoleId
     * @param goodsId
     * @param eatExp
     * @return
     */
    private RoleWuxingJingpoItem insertWxBagJpItem(Long userRoleId, Long guid, int goodsId, int eatExp) {
        RoleWuxingJingpoItem oldRoleWxJpItem = getRoleWxJpItem(userRoleId, guid);
        if (null != oldRoleWxJpItem) {
            return null;
        }
        if (!checkWxJpBagSpaceEnough(userRoleId)) {
            return null;
        }
        RoleWuxingJingpoItem roleWxJpItem = new RoleWuxingJingpoItem();
        long nowTime = GameSystemTime.getSystemMillTime();
        roleWxJpItem.setGuid(guid);
        roleWxJpItem.setUserRoleId(userRoleId);
        roleWxJpItem.setGoodsId(goodsId);
        roleWxJpItem.setEatExp(eatExp);
        roleWxJpItem.setPosition(GameConstants.WX_JP_BAG_TYPE);
        roleWxJpItem.setSlot(GameConstants.WX_JP_BAG_SLOT);
        roleWxJpItem.setCreateTime(nowTime);
        roleWxJpItem.setUpdateTime(nowTime);
        roleWuxingJingpoItemDao.cacheInsert(roleWxJpItem, userRoleId);
        return roleWxJpItem;
    }

    /**
     * 删除魔神背包精魄数据
     * 
     * @param guid
     * @return
     */
    private boolean removeWxBagJpItem(Long userRoleId, Long guid) {
        RoleWuxingJingpoItem roleWxJpItem = getRoleWxJpItem(userRoleId, guid);
        if (null == roleWxJpItem) {
            return false;
        }
        return roleWuxingJingpoItemDao.cacheDelete(guid, userRoleId) > 0;
    }

    /**
     * 刷新魔神身上的孔位开启状态
     * 
     * @param guid
     * @return
     */
    private void refreshWxJpSlot(Long userRoleId, int wxType, int wxLevel) {
        RoleWuxingJingpo roleWxJp = getRoleWxJp(userRoleId);
        Map<Integer, Object[]> bodyJpMap = roleWxJp.getBodyJingpoMap();
        Object[] jpData = bodyJpMap.get(wxType);

        boolean refreshFlag = false;
        Map<Integer, MoshenJingpoKongWeiConfig> wxJpBodyKwMap = moshenJingpoConfigExportService.getWxJpBodyKwMap();
        if(ObjectUtil.isEmpty(wxJpBodyKwMap)){
            return ;
        }
        
        for (int slot : wxJpBodyKwMap.keySet()) {
            MoshenJingpoKongWeiConfig bodyKw = wxJpBodyKwMap.get(slot);
            if (1 == bodyKw.getNeedType() && bodyKw.getNeedCount() <= wxLevel) {
                refreshFlag = true;
                jpData[slot - 1] = GameConstants.WX_JP_SLOT_OPEN_YES;
            }
        }
        
        if (refreshFlag) {
            bodyJpMap.put(wxType, jpData);
            roleWxJp.setBodyData(JSON.toJSONString(bodyJpMap));
            roleWxJp.setUpdateTime(GameSystemTime.getSystemMillTime());
            roleWuxingJingpoDao.cacheUpdate(roleWxJp, userRoleId);
            /* 内部请求刷新魔神身上精魄数据 */
            BusMsgSender.send2One(userRoleId, ClientCmdType.WX_JP_GET_BODY, getRoleWxJpBodyData(userRoleId, wxType));
        }

    }

    /**
     * GM指令设置魔神精华数目
     * 
     * @param userRoleId
     * @param count
     */
    public void setMoshenJinghua(Long userRoleId, Long count) {
        if (count <= 0) {
            return;
        }
        count = count > Long.MAX_VALUE ? Long.MAX_VALUE : count;
        RoleWuxingJingpo roleWxJp = getRoleWxJp(userRoleId);
        roleWxJp.setMoshenJinghua(count);
        roleWuxingJingpoDao.cacheUpdate(roleWxJp, userRoleId);
    }

    /************************************ 糖宝五行魔神 ************************************/

    // 获取糖宝魔神的祝福值
    private int getTbZfzValue(TangbaoWuxing tbWuxing, TangbaoMoShenBaseConfig config) {
        int zfzValue = tbWuxing.getZfzVal();
        boolean isClear = config.isZfztime();
        long qiLastTime = tbWuxing.getLastSjTime();
        if (qiLastTime <= GameSystemTime.getSystemMillTime() && isClear) {
            tbWuxing.setLastSjTime(0l);
            tbWuxing.setZfzVal(0);
            tangbaoWuxingDao.cacheUpdate(tbWuxing, tbWuxing.getUserRoleId());
            return 0;
        }
        return zfzValue;
    }

    // 根据魔神类型,获取糖宝五行魔神
    private TangbaoWuxing getTangbaoWuXingByType(Long userRoleId, int type) {
        List<TangbaoWuxing> list = tangbaoWuxingDao.cacheLoadAll(userRoleId, new TangbaoWuXingFilter(type));
        if (list == null || list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }

    // 创建糖宝五行魔神
    private TangbaoWuxing createTangbaoWuXing(Long userRoleId, int type) {
        TangbaoWuxing tbWuxing = new TangbaoWuxing();
        tbWuxing.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
        tbWuxing.setUserRoleId(userRoleId);
        tbWuxing.setWuxingLevel(1);
        tbWuxing.setWuxingType(type);
        tbWuxing.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
        tbWuxing.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
        tbWuxing.setLastSjTime(0l);
        tbWuxing.setZfzVal(0);
        tangbaoWuxingDao.cacheInsert(tbWuxing, userRoleId);
        return tbWuxing;
    }

    // 糖宝五行升阶
    private boolean isTbSJSuccess(int zfzValue, TangbaoMoShenBaseConfig config) {
        int minzf = config.getZfzmin();
        if (zfzValue < minzf) {
            return false;
        }

        int pro = config.getPro();
        if (RandomUtil.getIntRandomValue(1, 101) > pro) {
            return false;
        }
        return true;
    }

    // 升阶糖宝五行魔神
    private Object[] sjTangbaoWuxing(TangbaoMoShenBaseConfig config, Map<String, Integer> needResource, long userRoleId, boolean isSendErrorCode, boolean isAutoGM, int zfzVal, int yb, int byb) {
        Map<String, Integer> tempResources = new HashMap<>();
        int money = config.getMoney();
        int oldMoney = needResource.get(GoodsCategory.MONEY) == null ? 0 : needResource.get(GoodsCategory.MONEY);
        Object[] isOb = roleBagExportService.isEnought(GoodsCategory.MONEY, money + oldMoney, userRoleId);
        if (null != isOb) {
            Object[] errorCode = isSendErrorCode ? isOb : null;
            return new Object[] { errorCode };
        }
        tempResources.put(GoodsCategory.MONEY + "", money);
        List<String> needGoodsIds = huanShiMoShenJiChuBiaoConfigExportService.getConsumeIds(config.getItem());
        int needCount = config.getCount();
        for (String goodsId : needGoodsIds) {
            int oldNeedCount = needResource.get(goodsId) == null ? 0 : needResource.get(goodsId);
            int owerCount = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
            if (owerCount >= oldNeedCount + needCount) {
                tempResources.put(goodsId, needCount);
                needCount = 0;
                break;
            }
            needCount = oldNeedCount + needCount - owerCount;
            tempResources.put(goodsId, owerCount - oldNeedCount);
        }
        if (isAutoGM && needCount > 0) {
            int bPrice = byb;// 绑定元宝的价格
            int bCount = 0;
            int nowNeedBgold = 0;
            for (int i = 0; i < needCount; i++) {
                nowNeedBgold = (bCount + 1) * bPrice;
                Object[] bgoldError = roleBagExportService.isEnought(GoodsCategory.BGOLD, nowNeedBgold, userRoleId);
                if (null != bgoldError) {
                    break;
                }
                bCount++;
            }
            nowNeedBgold = bCount * bPrice;
            tempResources.put(GoodsCategory.BGOLD + "", nowNeedBgold);
            needCount = needCount - bCount;
            int price = yb;
            int nowNeedGold = needCount * price;
            Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD, nowNeedGold, userRoleId);
            if (null != goldError) {
                Object[] errorCode = isSendErrorCode ? goldError : null;
                return new Object[] { errorCode };
            }
            tempResources.put(GoodsCategory.GOLD + "", nowNeedGold);
            needCount = 0;
        }
        if (needCount > 0) {
            Object[] errorCode = isSendErrorCode ? AppErrorCode.WUXING_NO_ITEM : null;
            return new Object[] { errorCode };
        }
        ObjectUtil.mapAdd(needResource, tempResources);
        boolean flag = isTbSJSuccess(zfzVal, config);
        if (!flag) {
            zfzVal += RandomUtil.getIntRandomValue(config.getZfzmin2(), config.getZfzmin3() + 1);
        }
        // 如果祝福值大于了最大值，算强化成功
        int maxzf = config.getZfzmax();
        int level = config.getLevel();
        if (flag || zfzVal >= maxzf) {
            zfzVal = 0;
            level = level + 1;
        }
        return new Object[] { null, level, zfzVal };
    }

    // 升级糖宝五行的公告
    private int getTbGongGao(int type) {
        switch (type) {
        case GameConstants.WUXING_GOLD:
            return AppErrorCode.TB_WUXING_SJ_NOTICE_1;
        case GameConstants.WUXING_WOOD:
            return AppErrorCode.TB_WUXING_SJ_NOTICE_2;
        case GameConstants.WUXING_EARTH:
            return AppErrorCode.TB_WUXING_SJ_NOTICE_3;
        case GameConstants.WUXING_WATER:
            return AppErrorCode.TB_WUXING_SJ_NOTICE_4;
        case GameConstants.WUXING_FIRE:
            return AppErrorCode.TB_WUXING_SJ_NOTICE_5;
        default:
            return 0;
        }
    }

    /**
     * 初始化糖宝五行魔神数据集合
     * @param userRoleId
     * @return
     */
    public List<TangbaoWuxing> initTangbaoWuxing(Long userRoleId) {
        return tangbaoWuxingDao.initTangbaoWuxing(userRoleId);
    }

    /**
     * 获取糖宝五行魔神的永久加成属性集合
     * @param userRoleId
     * @return
     */
    public Map<String, Long> getTbWuXingAttrs(Long userRoleId) {
        List<TangbaoWuxing> list = getTbWuXings(userRoleId);
        if (ObjectUtil.isEmpty(list)) {
            return null;
        }
        Map<String, Long> attrs = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            TangbaoWuxing tbwx = list.get(i);
            TangbaoMoShenBaseConfig tbWxConfig = tangbaoMoShenBaseConfigExportService.getTbWuXingConfigByTypeAndLevel(tbwx.getWuxingType(), tbwx.getWuxingLevel());
            if (tbWxConfig == null) {
                continue;
            }
            ObjectUtil.longMapAdd(attrs, tbWxConfig.getAttrs());
        }
        return attrs;
    }
    
    /**
     * 获取所有糖宝五行魔神数据集合
     * @param userRoleId
     * @return
     */
    public List<TangbaoWuxing> getTbWuXings(Long userRoleId) {
        List<TangbaoWuxing> list = tangbaoWuxingDao.cacheLoadAll(userRoleId);
        if (list == null || list.size() <= 0) {
            return null;
        }
        return list;
    }

    /**
     * 获取糖宝五行信息
     * 
     * @param userRoleId
     * @return
     */
    public Object[] getTbWuXingInfo(Long userRoleId) {
        List<TangbaoWuxing> list = tangbaoWuxingDao.cacheLoadAll(userRoleId);
        if (list == null || list.size() <= 0) {
            return null;
        }
        List<Object[]> reList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            TangbaoWuxing tbWuxing = list.get(i);
            TangbaoMoShenBaseConfig config = tangbaoMoShenBaseConfigExportService.getTbWuXingConfigByTypeAndLevel(tbWuxing.getWuxingType(), tbWuxing.getWuxingLevel());
            if (config == null) {
                continue;
            }
            reList.add(new Object[] { tbWuxing.getWuxingType(), config.getId(), getTbZfzValue(tbWuxing, config), tbWuxing.getLastSjTime() });
        }
        return reList.toArray();
    }

    /**
     * 激活糖宝五行魔神
     * 
     * @param userRoleId
     * @param type
     * @param isAutoGM
     * @return
     */
    public Object[] jihuoTbWuXing(Long userRoleId, Integer type, boolean isAutoGM) {
        // 五行魔神是否已经被激活过
        TangbaoWuxing tbWuxing = getTangbaoWuXingByType(userRoleId, type);
        if (tbWuxing != null) {
            return AppErrorCode.TB_WUXING_YI_JIHUO;
        }
        // 五行配置
        TangbaoMoShenBaseConfig config = tangbaoMoShenBaseConfigExportService.getTbWuXingConfigByTypeAndLevel(type, GameConstants.TB_WUXING_CHUSHI_LEVEL);
        if (config == null) {
            return AppErrorCode.CONFIG_ERROR;
        }
        // 激活需要道具
        List<String> needGoodsIds = tangbaoMoShenBaseConfigExportService.getConsumeIds(config.getItem());
        int needCount = config.getCount();
        Map<String, Integer> tempResources = new HashMap<>();
        Object[] goldObj = new Object[2];
        for (String goodsId : needGoodsIds) {
            int owerCount = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
            if (owerCount >= needCount) {
                tempResources.put(goodsId, needCount);
                needCount = 0;
                break;
            }
            needCount = needCount - owerCount;
            tempResources.put(goodsId, owerCount);
        }
        if (isAutoGM && needCount > 0) {
            int bPrice = config.getBgold();// 绑定元宝的价格
            if (bPrice < 1) {
                return AppErrorCode.TB_WUXING_NO_ITEM;
            }
            int bCount = 0;
            int nowNeedBgold = 0;
            for (int i = 0; i < needCount; i++) {
                nowNeedBgold = (bCount + 1) * bPrice;
                Object[] bgoldError = roleBagExportService.isEnought(GoodsCategory.BGOLD, nowNeedBgold, userRoleId);
                if (null != bgoldError) {
                    break;
                }
                bCount++;
            }
            nowNeedBgold = bCount * bPrice;
            tempResources.put(GoodsCategory.BGOLD + "", nowNeedBgold);
            goldObj[0] = nowNeedBgold;
            needCount = needCount - bCount;
            int price = config.getGold();// 需要通过商城配置表获得对应物品的价格
            if (price < 1) {
                return AppErrorCode.CONFIG_ERROR;
            }
            int nowNeedGold = needCount * price;
            Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD, nowNeedGold, userRoleId);
            if (null != goldError) {
                return AppErrorCode.YB_ERROR;
            }
            tempResources.put(GoodsCategory.GOLD + "", nowNeedGold);
            goldObj[1] = nowNeedGold;
            needCount = 0;
        } else {
            if (needCount > 0) {
                return AppErrorCode.TB_WUXING_NO_ITEM;
            }
        }
        Integer newNeedGold = tempResources.remove(GoodsCategory.GOLD + "");
        Integer newNeedBgold = tempResources.remove(GoodsCategory.BGOLD + "");
        // 扣除元宝
        if (newNeedGold != null && newNeedGold > 0) {
            roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, newNeedGold, userRoleId, LogPrintHandle.CONSUME_TB_WUXING_JIHUO, true, LogPrintHandle.CBZ_TB_WUXING_JIHUO);
            // 腾讯OSS消费上报
            if (PlatformConstants.isQQ()) {
                BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_YB, newNeedGold, LogPrintHandle.CONSUME_TB_WUXING_JIHUO + "", QQXiaoFeiType.CONSUME_TB_WUXING_JIHUO, 1 });
            }
        } else {
            newNeedGold = 0;
        }
        // 扣除绑定元宝
        if (newNeedBgold != null && newNeedBgold > 0) {
            roleBagExportService.decrNumberWithNotify(GoodsCategory.BGOLD, newNeedBgold, userRoleId, LogPrintHandle.CONSUME_TB_WUXING_JIHUO, true, LogPrintHandle.CBZ_TB_WUXING_JIHUO);
            // 腾讯OSS消费上报
            if (PlatformConstants.isQQ()) {
                BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_BYB, newNeedGold, LogPrintHandle.CONSUME_TB_WUXING_JIHUO + "", QQXiaoFeiType.CONSUME_TB_WUXING_JIHUO, 1 });
            }
        } else {
            newNeedBgold = 0;
        }
        roleBagExportService.removeBagItemByGoods(tempResources, userRoleId, GoodsSource.TB_WUXING_JH, true, true);

        // 激活
        TangbaoWuxing tbwx = createTangbaoWuXing(userRoleId, type);
        TangbaoMoShenBaseConfig tbWxConfig = tangbaoMoShenBaseConfigExportService.getTbWuXingConfigByTypeAndLevel(tbwx.getWuxingType(), tbwx.getWuxingLevel());
        int configId = 0;
        if (tbWxConfig != null) {
            configId = tbWxConfig.getId();
        }
        // 通知属性变化
        BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_TB_WUXING_CHANGE, getTbWuXingAttrs(userRoleId));
        return new Object[] { AppErrorCode.SUCCESS, type, configId, goldObj };
    }

    /**
     * 升级糖宝五行
     * 
     * @param userRoleId
     * @param type
     * @param busMsgQueue
     * @param isAutoGM
     * @return
     */
    public Object[] sjTbWuXing(Long userRoleId, Integer type, BusMsgQueue busMsgQueue, boolean isAutoGM) {
        TangbaoWuxing tbWuxing = getTangbaoWuXingByType(userRoleId, type);
        if (tbWuxing == null) {
            return AppErrorCode.TB_WUXING_WEI_JIHUO;
        }
        int tbWuxingLevel = tbWuxing.getWuxingLevel();
        // 五行配置
        TangbaoMoShenBaseConfig config = tangbaoMoShenBaseConfigExportService.getTbWuXingConfigByTypeAndLevel(type, tbWuxingLevel);
        if (config == null) {
            return AppErrorCode.CONFIG_ERROR;
        }
        if (config.getItem() == null || "".equals(config.getItem())) {
            return AppErrorCode.TB_WUXING_MAX_LEVEL;
        }
        Map<String, Integer> needResource = new HashMap<>();
        int zfzVal = getTbZfzValue(tbWuxing, config);
        Object[] result = sjTangbaoWuxing(config, needResource, userRoleId, true, isAutoGM, zfzVal, config.getGold(), config.getBgold());
        Object[] erroCode = (Object[]) result[0];
        if (erroCode != null) {
            return erroCode;
        }
        int newlevel = (Integer) result[1];
        int newZfz = (Integer) result[2];
        Integer newNeedMoney = needResource.remove(GoodsCategory.MONEY + "");
        Integer newNeedGold = needResource.remove(GoodsCategory.GOLD + "");
        Integer newNeedBgold = needResource.remove(GoodsCategory.BGOLD + "");
        // 扣除金币
        if (newNeedMoney != null && newNeedMoney > 0) {
            roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, newNeedMoney, userRoleId, LogPrintHandle.CONSUME_TB_WUXING_SHENGJI, true, LogPrintHandle.CBZ_TB_WUXING_SHENGJI);
        }
        // 扣除元宝
        if (newNeedGold != null && newNeedGold > 0) {
            roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, newNeedGold, userRoleId, LogPrintHandle.CONSUME_TB_WUXING_SHENGJI, true, LogPrintHandle.CBZ_TB_WUXING_SHENGJI);
            // 腾讯OSS消费上报
            if (PlatformConstants.isQQ()) {
                BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_YB, newNeedGold, LogPrintHandle.CONSUME_TB_WUXING_SHENGJI, QQXiaoFeiType.CONSUME_TB_WUXING_SHENGJI, 1 });
            }
        }
        // 扣除绑定元宝
        if (newNeedBgold != null && newNeedBgold > 0) {
            roleBagExportService.decrNumberWithNotify(GoodsCategory.BGOLD, newNeedBgold, userRoleId, LogPrintHandle.CONSUME_TB_WUXING_SHENGJI, true, LogPrintHandle.CBZ_TB_WUXING_SHENGJI);
            // 腾讯OSS消费上报
            if (PlatformConstants.isQQ()) {
                BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_BYB, newNeedBgold, LogPrintHandle.CONSUME_TB_WUXING_SHENGJI, QQXiaoFeiType.CONSUME_TB_WUXING_SHENGJI, 1 });
            }
        }
        BagSlots bagSlots = roleBagExportService.removeBagItemByGoods(needResource, userRoleId, GoodsSource.TB_WUXING_SJ, true, true);
        if(!bagSlots.isSuccee()){
            return bagSlots.getErrorCode();
        }
        // 更新数据
        tbWuxing.setZfzVal(newZfz);
        tbWuxing.setWuxingLevel(newlevel);
        TangbaoMoShenBaseConfig newConfig = tangbaoMoShenBaseConfigExportService.getTbWuXingConfigByTypeAndLevel(type, newlevel);
        if (newZfz > 0 && (newlevel != tbWuxingLevel || zfzVal == 0)) {
            long zfzCdTime = 0l;

            float clearTime = newConfig.getCztime();
            if (clearTime == 0) {
                zfzCdTime = 0l;
            } else {
                zfzCdTime = GameSystemTime.getSystemMillTime() + (int) (clearTime * 60 * 60 * 1000);
            }
            tbWuxing.setLastSjTime(zfzCdTime);
        } else if (newZfz == 0) {
            tbWuxing.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            tbWuxing.setLastSjTime(0l);
        }
        tangbaoWuxingDao.cacheUpdate(tbWuxing, userRoleId);
        // 如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象
        if (newlevel > tbWuxingLevel) {
            // 通知属性变化
            BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_TB_WUXING_CHANGE, getTbWuXingAttrs(userRoleId));
            
            if (newConfig.isGgopen()) {
                UserRole userRole = roleExportService.getUserRole(userRoleId);
                BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[] { getTbGongGao(type), new Object[] { userRole.getName(), newlevel } });
            }
        }
        // 日志记录
        JSONArray consumeItemArray = new JSONArray(); 
        LogFormatUtils.parseJSONArray(bagSlots,consumeItemArray);
        GamePublishEvent.publishEvent(new WuxingMoshenLogEvent(LogPrintHandle.WUXING_MOSHEN_LOG, LogPrintHandle.WUXING_MOSHEN_TYPE_TANGBAO, userRoleId, newNeedMoney, newNeedGold, consumeItemArray, tbWuxingLevel, newlevel, zfzVal, newZfz));
        return new Object[] { AppErrorCode.SUCCESS, type, newConfig.getId(), tbWuxing.getZfzVal(), tbWuxing.getLastSjTime() };
    }
    
    /**
     * 获取糖宝五行祝福值和清零时间
     * 
     * @param userRoleId
     * @param type
     * @return
     */
    public Object[] getTbWxZfz(Long userRoleId, Integer type) {
        TangbaoWuxing tbWuxing = getTangbaoWuXingByType(userRoleId, type);
        if (tbWuxing == null) {
            return null;
        }
        // 五行配置
        TangbaoMoShenBaseConfig config = tangbaoMoShenBaseConfigExportService.getTbWuXingConfigByTypeAndLevel(type, tbWuxing.getWuxingLevel());
        if (config == null) {
            return null;
        }
        return new Object[] { type, config.getId(), getTbZfzValue(tbWuxing, config), tbWuxing.getLastSjTime() };
    }

    /************************************ 糖宝五行魔神技能 ************************************/
    /**
     * 获取糖宝五行魔神技能集合
     * 
     * @param userRoleId
     * @return
     */
    private List<TangbaoWuxingSkill> getTbWxSkills(Long userRoleId) {
        return tangbaoWuxingSkillDao.cacheLoadAll(userRoleId);
    }

    /**
     * 根据类型获取糖宝五行魔神技能对象
     * 
     * @param userRoleId
     * @param type
     * @return
     * 
     */
    private TangbaoWuxingSkill getTbWxSkillByType(Long userRoleId, int type) {
        List<TangbaoWuxingSkill> skill_list = tangbaoWuxingSkillDao.cacheLoadAll(userRoleId, new TangbaoWuxingSkillFilter(type));
        if (ObjectUtil.isEmpty(skill_list)) {
            return null;
        }
        return skill_list.get(0);
    }

    /**
     * 初始化糖宝五行魔神技能数据
     * 
     * @param userRoleId
     * @return
     */
    public List<TangbaoWuxingSkill> initTbWuxingSkill(Long userRoleId) {
        return tangbaoWuxingSkillDao.initTangbaoWuxingSkill(userRoleId);
    }

    /**
     * 获取五行魔神技能增加的属性集合
     * 
     * @param userRoleId
     * @return
     * 
     */
    public Map<String, Long> getTbWuXingSkillAttrs(Long userRoleId) {
        List<TangbaoWuxingSkill> tbWxSkills = getTbWxSkills(userRoleId);
        if (ObjectUtil.isEmpty(tbWxSkills)) {
            return null;
        }
        Map<String, Long> attrsMap = new HashMap<>();
        for (TangbaoWuxingSkill tbWxSkill : tbWxSkills) {
            List<Integer> tbWxSkillIds = tbWxSkill.findTbWxSkillIds();
            if (ObjectUtil.isEmpty(tbWxSkillIds)) {
                continue;
            }
            for (Integer wxSkillId : tbWxSkillIds) {
                TangbaoMoShenJiNengBiaoConfig skillConfig = tangbaoMoShenJiNengBiaoConfigExportService.loadById(wxSkillId);
                if (null == skillConfig) {
                    continue;
                }
                ObjectUtil.longMapAdd(attrsMap, skillConfig.getAttrMap());
            }
        }
        return attrsMap;
    }

    /**
     * 获取糖宝五行魔神技能信息
     * 
     * @param userRoleId
     * @return
     */
    public Object[] getTbWxSkillInfo(Long userRoleId) {
        List<TangbaoWuxingSkill> tbWxSkills = getTbWxSkills(userRoleId);
        if (!ObjectUtil.isEmpty(tbWxSkills)) {
            List<Integer> rsList = new ArrayList<>();
            for (TangbaoWuxingSkill tbWxSkill : tbWxSkills) {
                rsList.addAll(tbWxSkill.findTbWxSkillIds());
            }
            return rsList.toArray();
        } else {
            return null;
        }
    }

    /**
     * 升级糖宝五行魔神技能
     * 
     * @param userRoleId
     * @param skillId 要升级的魔神技能配置id
     */
    public Object[] upLevelTbWxSkill(Long userRoleId, Integer skillId) {
        TangbaoMoShenJiNengBiaoConfig config = tangbaoMoShenJiNengBiaoConfigExportService.loadById(skillId);
        if (null == config) {
            return AppErrorCode.CONFIG_ERROR;
        }
        int s_type = config.getType();
        TangbaoWuxingSkill tbWxSkill = getTbWxSkillByType(userRoleId, s_type);

        /* 技能有效性检验 */
        Integer oldSkillId = null;
        int s_seq = config.getSeq();
        if (null != tbWxSkill) {
            oldSkillId = tbWxSkill.getWxSkillIdBySeq(s_seq);
        }
        int oldLevel = 0;
        TangbaoMoShenJiNengBiaoConfig config2 = tangbaoMoShenJiNengBiaoConfigExportService.loadById(oldSkillId);
        if (null != config2) {
            oldLevel = config2.getLevel();
        }
        /* 最大等级限制校验 */
        int skillMaxLevel = tangbaoMoShenJiNengBiaoConfigExportService.getTbWxSkillMaxLevel(s_type, s_seq);
        if (oldLevel >= skillMaxLevel) {
            return AppErrorCode.WX_SKILL_MAX_LEVEL;
        }
        ++oldLevel;
        int s_level = config.getLevel();
        if (s_level != oldLevel) {
            return AppErrorCode.WX_SKILL_NO_LEARN;
        }

        /* 技能五行阶级限制校验 */
        int tbWxLevel = 0;
        TangbaoWuxing tbWuxing = getTangbaoWuXingByType(userRoleId, s_type);
        if (null != tbWuxing) {
            tbWxLevel = tbWuxing.getWuxingLevel();
        }
        int s_limit_wxLevel = config.getLimit();
        if (s_limit_wxLevel > tbWxLevel) {
            return AppErrorCode.WX_SKILL_LIMIT_LEVEL;
        }

        /* 道具消耗校验 */
        List<String> goodsIdList = tangbaoMoShenJiNengBiaoConfigExportService.getConsumeIds(config.getNeedItem());
        if (ObjectUtil.isEmpty(goodsIdList)) {
            return AppErrorCode.CONFIG_ERROR;
        }
        Map<String, Integer> itemMap = new HashMap<>();
        int needCount = config.getItemCount();
        for (String goodsId : goodsIdList) {
            int itemCnt = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
            if (itemCnt >= needCount) {
                itemMap.put(goodsId, needCount);
                needCount = 0;
                break;
            } else if (itemCnt > 0) {
                itemMap.put(goodsId, itemCnt);
                needCount -= itemCnt;
            }
        }
        if (needCount > 0) {
            return AppErrorCode.WX_SKILL_NOT_ENOUGH;
        }

        /* 金币消耗校验 */
        int money = config.getNeedMoney();
        Object[] isOb = accountExportService.isEnought(GoodsCategory.MONEY, money, userRoleId);
        if (null != isOb) {
            return AppErrorCode.JB_ERROR;
        }

        /* 扣除金币 */
        if (money > 0) {
            Object[] result = roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, money, userRoleId, LogPrintHandle.CONSUME_TB_WUXING_SKILL_UPLEVEL, true, LogPrintHandle.CBZ_TB_WUXING_SKILL_UPLEVEL);
            if (result != null) {
                return result;
            }
        }
        /* 扣除道具 */
        roleBagExportService.removeBagItemByGoods(itemMap, userRoleId, GoodsSource.CONSUME_TB_WX_SKILL_UPLEVEL, true, true);

        /* 更新玩家五行技能数据 */
        long nowTime = GameSystemTime.getSystemMillTime();
        if (null == tbWxSkill) {
            tbWxSkill = new TangbaoWuxingSkill();
            tbWxSkill.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
            tbWxSkill.setUserRoleId(userRoleId);
            tbWxSkill.setWuxingType(s_type);
            tbWxSkill.setCreateTime(nowTime);
            tbWxSkill.updateSkillId(s_seq, skillId);
            tbWxSkill.setUpdateTime(nowTime);
            tangbaoWuxingSkillDao.cacheInsert(tbWxSkill, userRoleId);
        } else {
            tbWxSkill.updateSkillId(s_seq, skillId);
            tbWxSkill.setUpdateTime(nowTime);
            tangbaoWuxingSkillDao.cacheUpdate(tbWxSkill, userRoleId);
        }
        /* 刷新糖宝五行技能附带的属性 */
        BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_TB_WX_SKILL_CHARGE, getTbWuXingSkillAttrs(userRoleId));
        return new Object[] { AppErrorCode.SUCCESS, skillId };
    }

}