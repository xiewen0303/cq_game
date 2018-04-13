package com.junyou.bus.linghuo.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.bus.jingji.service.IFightVal;
import com.junyou.bus.jingji.util.FightPowerType;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.utils.common.CovertObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.linghuo.configure.export.LingHuoShuXingBiaoConfig;
import com.junyou.bus.linghuo.configure.export.LingHuoShuXingBiaoConfigExportService;
import com.junyou.bus.linghuo.dao.RoleLinghuoBlessDao;
import com.junyou.bus.linghuo.dao.RoleLinghuoInfoDao;
import com.junyou.bus.linghuo.entity.RoleLinghuoBless;
import com.junyou.bus.linghuo.entity.RoleLinghuoInfo;
import com.junyou.bus.linghuo.filter.RoleLinghuoBlessFilter;
import com.junyou.bus.mogonglieyan.configure.ZhuFuShangDianBiaoConfig;
import com.junyou.bus.mogonglieyan.configure.ZhuFuShangDianBiaoConfigExportService;
import com.junyou.bus.mogonglieyan.dao.RoleMogonglieyanDao;
import com.junyou.bus.mogonglieyan.entity.RoleMogonglieyan;
import com.junyou.bus.mogonglieyan.service.RoleMoGongLieYanService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.number.LongUtils;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

@Service
public class LingHuoService implements IFightVal {

    @Override
    public long getZplus(long userRoleId, int fightPowerType) {
        if(fightPowerType == FightPowerType.HZ_JH) {
            RoleLinghuoInfo roleLinghuoInfo = getRoleLinghuoInfo(userRoleId);
            if(roleLinghuoInfo == null){
                return 0;
            }
            Map<String,Long> datas = getLingHuoAttrs(userRoleId,roleLinghuoInfo.getLinghuoId());
            if(datas == null){
                return 0;
            }
            return CovertObjectUtil.getZplus(datas);
        }else if(fightPowerType == FightPowerType.HZ_ZF){
            Map<String,Long> datas = getLinghuoBlessAttrs(userRoleId);
            return CovertObjectUtil.getZplus(datas);
        }
        return 0;
    }

    @Autowired
	private RoleLinghuoInfoDao roleLinghuoInfoDao;
	@Autowired
	private LingHuoShuXingBiaoConfigExportService lingHuoShuXingBiaoConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	
    @Autowired
    private RoleLinghuoBlessDao roleLinghuoBlessDao;
    @Autowired
    private RoleBagExportService roleBagExportService;
    @Autowired
    private GoodsConfigExportService goodsConfigExportService;
    @Autowired
    private RoleMoGongLieYanService moGongLieYanService;
    @Autowired
    private ZhuFuShangDianBiaoConfigExportService zhuFuShangDianBiaoConfigExportService;
    @Autowired
    private RoleMogonglieyanDao roleMogonglieyanDao;
    
	public List<RoleLinghuoInfo> initRoleLinghuoInfo(Long userRoleId){
		return roleLinghuoInfoDao.initRoleLinghuoInfo(userRoleId);
	}
	
	
	private RoleLinghuoInfo getRoleLinghuoInfo(Long userRoleId){
		List<RoleLinghuoInfo> list = roleLinghuoInfoDao.cacheLoadAll(userRoleId);
		if(list == null || list.size() <= 0 ){
			RoleLinghuoInfo info = new RoleLinghuoInfo();
			info.setUserRoleId(userRoleId);
			info.setLinghuoId(0);
			info.setCreateTime(new Timestamp(System.currentTimeMillis()));
			info.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			
			roleLinghuoInfoDao.cacheInsert(info, userRoleId);
			
			return info;
		} 
		
		return list.get(0);
	}
	
	public Object[] getLingHuoId(Long userRoleId){
		
		RoleLinghuoInfo info = getRoleLinghuoInfo(userRoleId);
		
		
		return new Object[]{info.getLinghuoId()};
	}
	
	
	public Object[] jiHuoLingHuo(Long userRoleId,int id){
		
		RoleLinghuoInfo info = getRoleLinghuoInfo(userRoleId);
		if(id <= info.getLinghuoId()){
			return AppErrorCode.LH_YI_JIHUO;
		}
		int lingHuoId = info.getLinghuoId()+1;
		
		if(lingHuoId != id){
			return AppErrorCode.LH_NO_KUAJI;
		}
		
		LingHuoShuXingBiaoConfig config = lingHuoShuXingBiaoConfigExportService.loadById(id);
		if(config == null){
			return AppErrorCode.LH_NO_LINGHUO;
		}
		
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		
		if(role.getLevel() < config.getLv()){
			return AppErrorCode.LH_NO_TIAOJIAN;
		}
		
		info.setLinghuoId(id);
		info.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		
		roleLinghuoInfoDao.cacheUpdate(info, userRoleId);
		
		// 推送内部场景坐骑属性变化
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_LINGHUO_CHANGE,info.getLinghuoId());
		
		return new Object[]{1};
	}
	
	public Map<String,Long> getLingHuoAttrs(Long userRoleId,int lingHuoId){
		LingHuoShuXingBiaoConfig sjConfig = lingHuoShuXingBiaoConfigExportService.loadById(lingHuoId);
		if(sjConfig == null){
			return null;
		}
		Map<String, Long> attrs = sjConfig.getAttrs();
		
		return attrs;
	}
	
	public Integer getLingHuoConfigId(Long userRoleId){
		RoleLinghuoInfo info = getRoleLinghuoInfo(userRoleId);
		return info.getLinghuoId();
	}
	

	//-----------------------灵活祝福----------------------------------//
	/**
	 * 初始化数据到内存
	 * 
	 * @param userRoleId
	 * @return
	 */
	public List<RoleLinghuoBless> initRoleLinghuoBless(Long userRoleId){
	    return roleLinghuoBlessDao.initRoleLinghuoBless(userRoleId);
	}

	/**
	 * 获取玩家所有灵火祝福数据
	 * 
	 * @param userRoleId
	 * @return
	 */
	private List<RoleLinghuoBless>  getCacheRoleLinghuoBlessData(Long userRoleId){
	    return roleLinghuoBlessDao.cacheLoadAll(userRoleId);
	}
	
	/**
     * 获取玩家指定灵火的所有灵火祝福数据
     * 
     * @param userRoleId
     * @param linghuoId   灵火配置id
     * @return
     */
    private List<RoleLinghuoBless>  getCacheRoleLinghuoBlessData(Long userRoleId, Integer linghuoId){
        return roleLinghuoBlessDao.cacheLoadAll(userRoleId, new RoleLinghuoBlessFilter(linghuoId, null));
    }
    
    /**
     * 获取玩家指定灵火上,格位的灵火祝福数据
     * 
     * @param userRoleId
     * @param linghuoId   灵火配置id
     * @param linghuoSlot   灵火格位
     * @return
     */
    private RoleLinghuoBless  getCacheRoleLinghuoBlessData(Long userRoleId, Integer linghuoId, Integer linghuoSlot){
        List<RoleLinghuoBless> list = roleLinghuoBlessDao.cacheLoadAll(userRoleId, new RoleLinghuoBlessFilter(linghuoId, linghuoSlot));
        if(ObjectUtil.isEmpty(list)){
           return null; 
        }
        return list.get(0);
    }

    /**
     * 获取玩家灵火祝福所有加成属性
     * @param userRoleId
     * @return
     */
    public Map<String, Long> getLinghuoBlessAttrs(Long userRoleId) {
        List<RoleLinghuoBless>  list = getCacheRoleLinghuoBlessData(userRoleId);
        if(ObjectUtil.isEmpty(list)){
            return null;
        }
        Map<String, Long> attrMap = new HashMap<String, Long>();// 所有加成属性
        Map<String, Long> tempAttrMap = new HashMap<String,Long>();// 保存每一个灵火孔位上的加成属性
        for(RoleLinghuoBless roleLinghuoBless : list){
            LingHuoShuXingBiaoConfig config = lingHuoShuXingBiaoConfigExportService.loadById(roleLinghuoBless.getLinghuoId());
            if(null != config){
                ObjectUtil.longMapAdd(tempAttrMap, config.getBaseAttrs());
                ObjectUtil.longMapTimes(tempAttrMap, roleLinghuoBless.getBlessValue() / 10000F);
                ObjectUtil.longMapAdd(attrMap,tempAttrMap);
                tempAttrMap.clear();
            }
        }
        return attrMap;
    }
    
    /**
     * 2852 灵火祝福:基本信息
     * @param userRoleId
     * @param linghuoId
     * @return
     */
    public Object[] loadLinghuoBlessInfo(Long userRoleId, Integer linghuoId) {
        List<Object[]> data = null;
        LingHuoShuXingBiaoConfig config = lingHuoShuXingBiaoConfigExportService.loadById(linghuoId);
        if (null != config) {
            List<RoleLinghuoBless> list = getCacheRoleLinghuoBlessData(userRoleId, linghuoId);
            if (!ObjectUtil.isEmpty(list)) {
                data = new ArrayList<>();
                for (RoleLinghuoBless roleLinghuoBless : list) {
                    Map<String, Long> attrMap = new HashMap<String, Long>();
                    ObjectUtil.longMapAdd(attrMap, config.getBaseAttrs());
                    ObjectUtil.longMapTimes(attrMap, roleLinghuoBless.getBlessValue() / 10000F);
                    data.add(new Object[] { roleLinghuoBless.getLinghuoSlot(), attrMap });
                }
            }
        }
        return new Object[] { linghuoId, null == data ? null : data.toArray() };
    }
	
    /**
     * 2853 灵火祝福:请求镶嵌
     * 
     * @param userRoleId
     * @param linghuoId         灵火id
     * @param linghuoSlot       灵火格位号
     * @param guid              镶嵌物品guid
     * @param busMsgQueue
     * @return
     */
    public void putOnLinghuoBless(Long userRoleId, Integer linghuoId, Integer linghuoSlot, Object guid, BusMsgQueue busMsgQueue){
        short responseCmd = ClientCmdType.LINGHUO_BLESS_PUT_ON;
        long guidVal;
        int linghuoIdVal,slotVal;
        /*数据有效性校验*/
        if (null == linghuoId || (linghuoIdVal = linghuoId.intValue()) <= 0 || null == linghuoSlot || guid == null || (guidVal = LongUtils.obj2long(guid)) <=0 ) {
            busMsgQueue.addMsg(userRoleId, responseCmd, AppErrorCode.PARAMETER_ERROR);
            return;
        }
        /*灵火格位号校验*/
        slotVal = linghuoSlot.intValue();
        if(slotVal < GameConstants.LINGHUO_BLESS_MIN_SLOT  || GameConstants.LINGHUO_BLESS_MAX_SLOT < slotVal){
            busMsgQueue.addMsg(userRoleId, responseCmd, AppErrorCode.LH_BLESS_SLOT_ERROR);
            return;
        }
        /*灵火是否激活校验*/
        Integer activateMaxId = null;
        RoleLinghuoInfo roleLinghuoInfo = getRoleLinghuoInfo(userRoleId);
        if(null != roleLinghuoInfo){
            activateMaxId = roleLinghuoInfo.getLinghuoId();
        }
        if(activateMaxId == null || linghuoId > activateMaxId){
            busMsgQueue.addMsg(userRoleId, responseCmd, AppErrorCode.LH_BLESS_NOT_ACTIVATE);
            return;
        }
        /*灵火是否存在校验*/
        LingHuoShuXingBiaoConfig config = lingHuoShuXingBiaoConfigExportService.loadById(linghuoIdVal);
        if (null == config) {
            busMsgQueue.addMsg(userRoleId, responseCmd, AppErrorCode.LH_BLESS_NOT_EXIST);
            return;
        }
        /*孔位上是否已镶嵌校验*/
        RoleLinghuoBless roleLinghuoBless = getCacheRoleLinghuoBlessData(userRoleId, linghuoIdVal, slotVal);
        if(null != roleLinghuoBless){
            busMsgQueue.addMsg(userRoleId, responseCmd, AppErrorCode.LH_BLESS_ALREADY_EXIST);
            return;
        }
        /*镶嵌道具校验*/
        RoleItemExport roleItem = roleBagExportService.getBagItemByGuid(userRoleId, guidVal);
        if (roleItem == null) {
            busMsgQueue.addMsg(userRoleId, responseCmd, AppErrorCode.NOT_FOUND_GOOODS);
            return;
        }
        GoodsConfig goodsConfig = goodsConfigExportService.loadById(roleItem.getGoodsId());
        if (null == goodsConfig) {
            busMsgQueue.addMsg(userRoleId, responseCmd, AppErrorCode.CONFIG_ERROR);
            return;
        }
        if (GoodsCategory.LINGHUO_BLESS_ITEM != goodsConfig.getCategory()) {
            busMsgQueue.addMsg(userRoleId, responseCmd, AppErrorCode.ITEM_NOT_ERROR);
            return;
        }
        /*道具使用等级限制校验*/
        int minLvl = goodsConfig.getData1();
        int maxLvl = goodsConfig.getData2().intValue();
        if(config.getLv() < minLvl || maxLvl < config.getLv()){
            busMsgQueue.addMsg(userRoleId, responseCmd, AppErrorCode.LH_BLESS_ITEM_LEVEL);
            return;
        }
        Integer blessValue = Integer.parseInt(goodsConfig.getData3());
        /*道具消耗*/
        BagSlots bagSlots = roleBagExportService.removeBagItemByGuid(guidVal, 1, userRoleId, GoodsSource.LINGHUO_BLESS_PUT_ON, true, true);
        if (!bagSlots.isSuccee()) {
            busMsgQueue.addMsg(userRoleId, responseCmd, bagSlots.getErrorCode());
            return;
        }
        /*数据更新*/
        long now_time = GameSystemTime.getSystemMillTime();
        roleLinghuoBless = new RoleLinghuoBless();
        roleLinghuoBless.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
        roleLinghuoBless.setUserRoleId(userRoleId);
        roleLinghuoBless.setLinghuoId(linghuoId);
        roleLinghuoBless.setLinghuoSlot(linghuoSlot);
        roleLinghuoBless.setBlessValue(blessValue);
        roleLinghuoBless.setCreateTime(now_time);
        roleLinghuoBless.setUpdateTime(now_time);
        roleLinghuoBlessDao.cacheInsert(roleLinghuoBless, userRoleId);
        /*灵火镶嵌后的属性加成*/
        Map<String, Long> baseAttrMap = new HashMap<String,Long>();
        ObjectUtil.longMapAdd(baseAttrMap, config.getBaseAttrs());
        ObjectUtil.longMapTimes(baseAttrMap, blessValue / 10000F);
        busMsgQueue.addMsg(userRoleId, responseCmd, new Object[]{AppErrorCode.SUCCESS, linghuoId, linghuoSlot, baseAttrMap});
        /*刷新场景属性*/
        busMsgQueue.addStageMsg(userRoleId, InnerCmdType.I_LINGHUO_BLESS_CHARGE, getLinghuoBlessAttrs(userRoleId));
        /*刷新灵火祝福对角色总的加成属性*/
        busMsgQueue.addMsg(userRoleId, ClientCmdType.LINGHUO_BLESS_SEND_ATTRS, getLinghuoBlessAttrs(userRoleId));
    }


	public void putOnLinghuoBless(Long userRoleId, Integer linghuoId, Integer linghuoSlot, BusMsgQueue busMsgQueue) {
		
		short responseCmd = ClientCmdType.LINGHUO_BLESS_PUT_ON;
//        long guidVal;
        int linghuoIdVal,slotVal;
        /*数据有效性校验*/
        if (null == linghuoId || (linghuoIdVal = linghuoId.intValue()) <= 0 || null == linghuoSlot) {
            busMsgQueue.addMsg(userRoleId, responseCmd, AppErrorCode.PARAMETER_ERROR);
            return;
        }
        /*灵火格位号校验*/
        slotVal = linghuoSlot.intValue();
        if(slotVal < GameConstants.LINGHUO_BLESS_MIN_SLOT  || GameConstants.LINGHUO_BLESS_MAX_SLOT < slotVal){
            busMsgQueue.addMsg(userRoleId, responseCmd, AppErrorCode.LH_BLESS_SLOT_ERROR);
            return;
        }
        /*灵火是否激活校验*/
        Integer activateMaxId = null;
        RoleLinghuoInfo roleLinghuoInfo = getRoleLinghuoInfo(userRoleId);
        if(null != roleLinghuoInfo){
            activateMaxId = roleLinghuoInfo.getLinghuoId();
        }
        if(activateMaxId == null || linghuoId > activateMaxId){
            busMsgQueue.addMsg(userRoleId, responseCmd, AppErrorCode.LH_BLESS_NOT_ACTIVATE);
            return;
        }
        /*灵火是否存在校验*/
        LingHuoShuXingBiaoConfig config = lingHuoShuXingBiaoConfigExportService.loadById(linghuoIdVal);
        if (null == config) {
            busMsgQueue.addMsg(userRoleId, responseCmd, AppErrorCode.LH_BLESS_NOT_EXIST);
            return;
        }
        /*孔位上是否已镶嵌校验*/
        RoleLinghuoBless roleLinghuoBless = getCacheRoleLinghuoBlessData(userRoleId, linghuoIdVal, slotVal);
        if(null != roleLinghuoBless){
            busMsgQueue.addMsg(userRoleId, responseCmd, AppErrorCode.LH_BLESS_ALREADY_EXIST);
            return;
        }
        
//        /*镶嵌道具校验*/
//        RoleItemExport roleItem = roleBagExportService.getBagItemByGuid(userRoleId, guidVal);
//        if (roleItem == null) {
//            busMsgQueue.addMsg(userRoleId, responseCmd, AppErrorCode.NOT_FOUND_GOOODS);
//            return;
//        }
//        
//        GoodsConfig goodsConfig = goodsConfigExportService.loadById(roleItem.getGoodsId());
//        if (null == goodsConfig) {
//            busMsgQueue.addMsg(userRoleId, responseCmd, AppErrorCode.CONFIG_ERROR);
//            return;
//        }
//        if (GoodsCategory.LINGHUO_BLESS_ITEM != goodsConfig.getCategory()) {
//            busMsgQueue.addMsg(userRoleId, responseCmd, AppErrorCode.ITEM_NOT_ERROR);
//            return;
//        }
//        /*道具使用等级限制校验*/
//        int minLvl = goodsConfig.getData1();
//        int maxLvl = goodsConfig.getData2().intValue();
//        if(config.getLv() < minLvl || maxLvl < config.getLv()){
//            busMsgQueue.addMsg(userRoleId, responseCmd, AppErrorCode.LH_BLESS_ITEM_LEVEL);
//            return;
//        }
        
        ZhuFuShangDianBiaoConfig zhuFuConfig = zhuFuShangDianBiaoConfigExportService.loadByLevel(config.getLv());
        if (null == zhuFuConfig) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.LINGHUO_BLESS_PUT_ON, AppErrorCode.CONFIG_ERROR);
            return;
        }
        
        if(!checkNeedVal(userRoleId, zhuFuConfig, busMsgQueue)){
        	return;
        }
        
        long blessValue = zhuFuConfig.getPercent();
        
        
        
        /*数据更新*/
        long now_time = GameSystemTime.getSystemMillTime();
        roleLinghuoBless = new RoleLinghuoBless();
        roleLinghuoBless.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
        roleLinghuoBless.setUserRoleId(userRoleId);
        roleLinghuoBless.setLinghuoId(linghuoId);
        roleLinghuoBless.setLinghuoSlot(linghuoSlot);
        roleLinghuoBless.setBlessValue((int)blessValue);
        roleLinghuoBless.setCreateTime(now_time);
        roleLinghuoBless.setUpdateTime(now_time);
        roleLinghuoBlessDao.cacheInsert(roleLinghuoBless, userRoleId);
        /*灵火镶嵌后的属性加成*/
        Map<String, Long> baseAttrMap = new HashMap<String,Long>();
        ObjectUtil.longMapAdd(baseAttrMap, config.getBaseAttrs());
        ObjectUtil.longMapTimes(baseAttrMap, blessValue / 10000F);
        busMsgQueue.addMsg(userRoleId, responseCmd, new Object[]{AppErrorCode.SUCCESS, linghuoId, linghuoSlot, baseAttrMap});
        /*刷新场景属性*/
        busMsgQueue.addStageMsg(userRoleId, InnerCmdType.I_LINGHUO_BLESS_CHARGE, getLinghuoBlessAttrs(userRoleId));
        /*刷新灵火祝福对角色总的加成属性*/
        busMsgQueue.addMsg(userRoleId, ClientCmdType.LINGHUO_BLESS_SEND_ATTRS, getLinghuoBlessAttrs(userRoleId));
//        
	}
	
	public boolean checkNeedVal(long userRoleId,ZhuFuShangDianBiaoConfig config,BusMsgQueue busMsgQueue){
		RoleMogonglieyan roleMogonglieyan = moGongLieYanService.getCacheRoleMogonglieyan(userRoleId);
        if (null == roleMogonglieyan) {
            return false;
        }
        long beforeJinghua = roleMogonglieyan.getJinghuaVal();
//        ZhuFuShangDianBiaoConfig config = zhuFuShangDianBiaoConfigExportService.loadByLevel(level);
//        if (null == config) {
//            busMsgQueue.addMsg(userRoleId, ClientCmdType.LINGHUO_BLESS_PUT_ON, AppErrorCode.CONFIG_ERROR);
//            return false;
//        }
        long needJinghua = config.getNeedJinghua();
        if (beforeJinghua < needJinghua) {
            busMsgQueue.addMsg(userRoleId, ClientCmdType.LINGHUO_BLESS_PUT_ON, AppErrorCode.MGLY_JINGHUA_NOT_ENOUGH);
            return false;
        }
//        Map<String, Integer> itemMap = config.getItemMap();
//        if (ObjectUtil.isEmpty(itemMap)) {
//            busMsgQueue.addMsg(userRoleId, ClientCmdType.MGLY_BUY_LHZX, AppErrorCode.CONFIG_ERROR);
//            return;
//        }
//        // 背包空间不足
//        Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(itemMap, userRoleId);
//        if (code != null) {
//            busMsgQueue.addMsg(userRoleId, ClientCmdType.MGLY_BUY_LHZX, code);
//            return;
//        }
        // 更新数据
        roleMogonglieyan.setJinghuaVal(beforeJinghua - needJinghua);
        roleMogonglieyan.setUpdateTime(GameSystemTime.getSystemMillTime());
        roleMogonglieyanDao.cacheUpdate(roleMogonglieyan, userRoleId);
//        // 发奖励
//        roleBagExportService.putGoodsAndNumberAttr(itemMap, userRoleId, GoodsSource.MOGONGLIEYAN_EXCHANGE, LogPrintHandle.GET_MGLY_EXCHANGE_ITEM, LogPrintHandle.GBZ_MGLY_EXCHANGE_ITEM, true);
        // 记录兑换日志
//        JSONArray goods = LogPrintHandle.getLogGoodsParam(itemMap, null);
//        RoleWrapper role = roleExportService.getLoginRole(userRoleId);
//        GamePublishEvent.publishEvent(new MoGongLieYanLogEvent(userRoleId, null == role ? "" : role.getName(), beforeJinghua, roleMogonglieyan.getJinghuaVal(), goods));
//        busMsgQueue.addMsg(userRoleId, ClientCmdType.MGLY_BUY_LHZX, AppErrorCode.OK);
        // 刷新精华值
        busMsgQueue.addMsg(userRoleId, ClientCmdType.MGLY_INIT_INFO, new Object[] { roleMogonglieyan.getYumoVal(), roleMogonglieyan.getJinghuaVal() });
        return true;
	}
    
}
