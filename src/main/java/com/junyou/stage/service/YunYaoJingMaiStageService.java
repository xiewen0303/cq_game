package com.junyou.stage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.xianqi.configure.YunYaoJingMaiConfig;
import com.junyou.bus.xianqi.configure.YunYaoJingMaiConfigExportService;
import com.junyou.bus.xianqi.constants.XianqiConstants;
import com.junyou.bus.xianqi.export.XianqiFubenServiceExport;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.gameconfig.map.configure.export.MapConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.YunYaoJingMaiPublicConfig;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IElementProduceTeam;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.element.goods.Collect;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.PublicFubenStageFactory;
import com.junyou.stage.model.stage.yunyaojingmai.YunYaoJingMaiStage;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.lottery.Lottery;

/**
 * 
 *@Description 云瑶晶脉-仙器副本场景业务处理
 *@Author Yang Gao
 *@Since 2016-11-2
 *@Version 1.1.0
 */
@Service
public class YunYaoJingMaiStageService {
    @Autowired
    private PublicFubenStageFactory publicFubenStageFactory;
    @Autowired
    private MapConfigExportService mapConfigExportService;
    @Autowired
    private RoleBagExportService roleBagExportService;
    @Autowired
    private XianqiFubenServiceExport xianqiFubenServiceExport;
    @Autowired
    private YunYaoJingMaiConfigExportService yunYaoJingMaiConfigExportService;
    @Autowired
    private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;

    /**
     * 获取云瑶晶脉公共数据配置
     * 
     * @return
     */
    private YunYaoJingMaiPublicConfig getPublicConfig() {
        YunYaoJingMaiPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_YUNYAOJINGMAI);
        if (null == publicConfig) {
            ChuanQiLog.error("yunyaojingmai public config is null");
        }
        return publicConfig;
    }

    /**
     * 创建副本场景
     * @param stageId
     * @param mapId
     * @param lineNo
     * @return
     */
    public IStage createYunYaoJingMaiStage(String stageId, Integer mapId, int lineNo) {
        MapConfig  mapConfig = mapConfigExportService.load(mapId);
        if(null == mapConfig){
            ChuanQiLog.error("yunyaojingmai map is not exist!!! mapId = {}", mapId);
            return null;
        }
        YunYaoJingMaiPublicConfig publicConfig = getPublicConfig();
        if(null == publicConfig){
            return null;
        }
        List<YunYaoJingMaiConfig> yunYaoJingMaiConfigs = yunYaoJingMaiConfigExportService.loadAllConfig();
        if(ObjectUtil.isEmpty(yunYaoJingMaiConfigs)){
            ChuanQiLog.error("yunyaojingmai config is null");
            return null;
        }
        YunYaoJingMaiStage stage = publicFubenStageFactory.createYunYaoJingMapStage(stageId, lineNo, mapConfig, publicConfig, yunYaoJingMaiConfigs);
        if(null != stage){
            StageManager.addStageCopy(stage);
            stage.start();
        }
        return stage;
    }
    
    /**
     * 退出副本场景
     * @param userRoleId
     * @param stageId
     */
    public void innerExitHandler(Long userRoleId, String stageId) {
        IStage iStage = StageManager.getStage(stageId);
        if(null == iStage || !StageType.YYJM_STAGE.equals(iStage.getStageType())){
            ChuanQiLog.error("yunyaojingstage exit , stage info error!!! userRoleId={}, stageId={}", userRoleId, stageId);
            return;
        }
        YunYaoJingMaiStage stage = (YunYaoJingMaiStage) iStage;
        IRole role = stage.getElement(userRoleId, ElementType.ROLE);
        if(null == role){
            ChuanQiLog.error("yunyaojingstage exit , stage not found role!!! userRoleId={}, stageId={}", userRoleId, stageId);
            return;
        }
        StageMsgSender.send2StageControl(userRoleId, InnerCmdType.S_APPLY_LEAVE_STAGE, null);
    }

    /**
     * 副本场景结束,清人处理
     * @param stageId
     */
    public void innerForceKickHandler(String stageId) {
        IStage iStage = StageManager.getStage(stageId);
        if(null == iStage || !StageType.YYJM_STAGE.equals(iStage.getStageType())){
            ChuanQiLog.error("yunyaojingmaistage over , force kick stage info error!!! stageId={}", stageId);
            return;
        }
        YunYaoJingMaiStage stage = (YunYaoJingMaiStage) iStage;
        Object[] roleIds = stage.getAllRoleIds();
        if(null != roleIds){
            for(Object roleId : roleIds){
                Long userRoleId = (Long) roleId;
                IRole role = stage.getElement(userRoleId, ElementType.ROLE);
                if(null != role){
                    StageMsgSender.send2StageControl(userRoleId, InnerCmdType.S_APPLY_LEAVE_STAGE, null);
                }
            }
        }
        stage.stop();
        if (stage.isCanRemove()) {
            StageManager.removeCopy(stage);
        }
    }

    /**
     * 副本中请求开采矿石
     * @param userRoleId
     * @param stageId
     * @param collectId
     * @return
     */
    public Object[] xqfubenCollect(Long userRoleId, String stageId, Long collectId) {
        IStage iStage = StageManager.getStage(stageId);
        if(null == iStage || !StageType.YYJM_STAGE.equals(iStage.getStageType())){
            ChuanQiLog.error("role request collect , stage info error!!! userRoleId={}, stageId={}", userRoleId, stageId);
            return AppErrorCode.COLLECT_FAIL;
        }
        int remainCnt = xianqiFubenServiceExport.getRoleXianqiFubenRemainCount(userRoleId);
        if(remainCnt <= 0){
            return AppErrorCode.XQFUBEN_COUNT_LIMIT;
        }
        if(roleBagExportService.getBagEmptySize(userRoleId) < XianqiConstants.XQFUBEN_COLLECT_REWARD_NUM){
            return AppErrorCode.BAG_NOEMPTY;
        }
        YunYaoJingMaiStage stage = (YunYaoJingMaiStage) iStage;
        Collect collect = stage.getElement(collectId, ElementType.COLLECT);
        if(null == collect || XianqiConstants.XQFUBEN_COLLECT_TYPE != collect.getCollertType()){
            return AppErrorCode.COLLECT_FAIL;
        }
        YunYaoJingMaiConfig config = yunYaoJingMaiConfigExportService.loadById(collect.getConfigId());
        if(null == config){
            return AppErrorCode.CONFIG_ERROR;
        }
        stage.saveRoleCollectLog(userRoleId, collectId);
        return new Object[]{AppErrorCode.SUCCESS, config.getTime() * 1000};
    }

    /**
     * 副本中矿石开采完成
     * @param userRoleId
     * @param stageId
     * @return
     */
    public Object[] xqfubenPull(Long userRoleId, String stageId) {
        IStage iStage = StageManager.getStage(stageId);
        if(null == iStage || !StageType.YYJM_STAGE.equals(iStage.getStageType())){
            ChuanQiLog.error("role request complete collect , stage info error!!! userRoleId={}, stageId={}", userRoleId, stageId);
            return null;
        }
        YunYaoJingMaiStage stage = (YunYaoJingMaiStage) iStage;
        stage.lock();
        try {
            Long collectId = stage.getRoleCollectLog(userRoleId);
            Collect collect = stage.getElement(collectId, ElementType.COLLECT);
            if(null == collect || XianqiConstants.XQFUBEN_COLLECT_TYPE != collect.getCollertType()){
                return AppErrorCode.COLLECT_FAIL;
            }
            // 清除采集记录
            stage.clearRoleCollectLog(collectId);
            // 开启重新生产采集物
            IElementProduceTeam collectProduceTeam = stage.getStageProduceManager().getElementProduceTeam(ElementType.COLLECT, collect.getTeamId());
            if(null != collectProduceTeam){
                collectProduceTeam.retrieve(collect);
            }
            YunYaoJingMaiConfig config = yunYaoJingMaiConfigExportService.loadById(collect.getConfigId());
            if(null == config){
                ChuanQiLog.error("role request complete collect, yunyaojingmaiconfig is null, config id={}", collect.getConfigId());
                return AppErrorCode.CONFIG_ERROR;
            }
            String randomGoodsId = Lottery.getRandomKeyByInteger(config.getItemMap());
            if(ObjectUtil.strIsEmpty(randomGoodsId)){
                ChuanQiLog.error("role request complete collect, collect reward is null, id={}", collect.getConfigId());
                return AppErrorCode.CONFIG_ERROR;
            }
            // 更新次数
            xianqiFubenServiceExport.cutXianqiFubenCount(userRoleId);
            // 发放奖励
            Map<String,Integer> reward = new HashMap<String,Integer>();
            reward.put(randomGoodsId, XianqiConstants.XQFUBEN_COLLECT_REWARD_NUM);
            BagSlots errorCode = roleBagExportService.putInBag(reward, userRoleId, 1, true);
            if(!errorCode.isSuccee()){
                ChuanQiLog.error("role request complete collect, collect reward into bag error!!!  id={}, reward map={}", collect.getConfigId(), reward);
                return errorCode.getErrorCode();
            }
            stage.saveCollectRewardLog(userRoleId, reward);
            return stage.getClientShowData(userRoleId);
        } catch (Exception e) {
            ChuanQiLog.error("role request complete collect error, info={}" , e);
        } finally{
            stage.unlock();
        }
        return null;
    }

}
