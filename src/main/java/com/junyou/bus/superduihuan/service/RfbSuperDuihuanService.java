package com.junyou.bus.superduihuan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.entity.RoleAccount;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.superduihuan.configure.export.SuperDuihuanConfig;
import com.junyou.bus.superduihuan.configure.export.SuperDuihuanConfigExportService;
import com.junyou.bus.superduihuan.configure.export.SuperDuihuanGroupConfig;
import com.junyou.bus.superduihuan.dao.RefabuSuperDuihuanDao;
import com.junyou.bus.superduihuan.entity.RefabuSuperDuihuan;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.err.AppErrorCode;
import com.junyou.event.RfbActivityPartInLogEvent;
import com.junyou.event.RfbSuperDuiHuanLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

@Service
public class RfbSuperDuihuanService {
	@Autowired
	private RefabuSuperDuihuanDao refabuSuperDuihuanDao;
	@Autowired
    private RoleBagExportService roleBagExportService;
    @Autowired
    private GoodsConfigExportService goodsConfigExportService;
    @Autowired
    private AccountExportService accountExportService;
    @Autowired
    private RoleExportService roleExportService;

	public List<RefabuSuperDuihuan> initRefabuSuperDuihuan(Long userRoleId) {
		return refabuSuperDuihuanDao.initRefabuSuperDuihuan(userRoleId);
	}

	private List<RefabuSuperDuihuan> getDuihuanRecord(Long userRoleId,
			final Integer subId) {
		List<RefabuSuperDuihuan> list = refabuSuperDuihuanDao.cacheLoadAll(
				userRoleId, new IQueryFilter<RefabuSuperDuihuan>() {

					@Override
					public boolean check(RefabuSuperDuihuan info) {
						return subId.equals(info.getSubId());
					}

					@Override
					public boolean stopped() {
						return false;
					}
				});
		if (list != null && list.size() > 0) {
			return list;
		}
		return null;

	}

	private RefabuSuperDuihuan get(Long userRoleId, final Integer subId,
			final Integer configId) {
		List<RefabuSuperDuihuan> list = refabuSuperDuihuanDao.cacheLoadAll(
				userRoleId, new IQueryFilter<RefabuSuperDuihuan>() {
					private boolean stop = false;

					@Override
					public boolean check(RefabuSuperDuihuan info) {
						if (subId.equals(info.getSubId())
								&& info.getConfigId().equals(configId)) {
							stop = true;
						}
						return stop;
					}

					@Override
					public boolean stopped() {
						return stop;
					}
				});
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;

	}

	private void create(Long userRoleId, Integer subId, Integer configId) {
		RefabuSuperDuihuan pojo = new RefabuSuperDuihuan();
		pojo.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		pojo.setSubId(subId);
		pojo.setUserRoleId(userRoleId);
		pojo.setConfigId(configId);
		pojo.setTimesCount(0);
		pojo.setUpdateTime(GameSystemTime.getSystemMillTime());
		refabuSuperDuihuanDao.cacheInsert(pojo, userRoleId);
	}

	public Object[] getInfo(Long userRoleId, Integer subId) {
		SuperDuihuanGroupConfig config = SuperDuihuanConfigExportService
				.getInstance().loadByMap(subId);
		if (config == null) {
			return null;
		}
		Object[] ret = new Object[4];
		ret[0] = config.getDesc();
		ret[1] = config.getBg();
		ret[2] = config.getVo();
		ret[3] = getSuperDuihuanStates(userRoleId, subId);
		return ret;
	}

	private Object[] getSuperDuihuanStates(Long userRoleId, Integer subId) {
		List<RefabuSuperDuihuan> list = getDuihuanRecord(userRoleId, subId);
		if (list == null) {
			return null;
		}
		Object[] ret = new Object[list.size()];
		for (int i = 0; i < list.size(); i++) {
			RefabuSuperDuihuan duihuan = list.get(i);
			ret[i] = new Object[] { duihuan.getConfigId(),
					duihuan.getTimesCount() };
		}
		return ret;
	}

	public Object[] getStates(Long userRoleId, Integer subId) {
		Object[] ret = new Object[] { subId,
				getSuperDuihuanStates(userRoleId, subId) };
		return ret;
	}

    public Object[] duihuan(Long userRoleId, Integer subId, Integer version, Integer configId, List<Long> itemIds1, List<Long> itemIds2) {
        ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
        if (configSong == null) {
            return AppErrorCode.NO_SUB_ACTIVITY;
        }
        // 版本不一样
        if (configSong.getClientVersion() != version) {
            // 处理数据变化:
            Object newSubHandleData = getInfo(userRoleId, subId);
            Object[] data = new Object[] { subId, configSong.getClientVersion(), newSubHandleData };
            BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY, data);
            return null;
        }
        SuperDuihuanConfig sConfig = SuperDuihuanConfigExportService.getInstance().loadByKeyId(subId, configId);
        if (sConfig == null) {
            return AppErrorCode.CONFIG_ERROR;
        }
        RefabuSuperDuihuan duihuan = get(userRoleId, subId, configId);
        if (duihuan == null) {
            create(userRoleId, subId, configId);
            duihuan = get(userRoleId, subId, configId);
        }
        // 判断次数
        if (duihuan.getTimesCount() >= sConfig.getCount()) {
            return AppErrorCode.SUPER_DUIHUAN_COUNT_TIMES_LIMIT;
        }
        GoodsConfigureVo item3 = sConfig.getItem3();
        Map<String, Integer> goodsMap = new HashMap<String, Integer>();
        goodsMap.put(item3.getGoodsId(), item3.getGoodsCount());
        Object[] checkResult = roleBagExportService.checkPutGoodsAndNumberAttr(goodsMap, userRoleId);
        if (checkResult != null) {
            return checkResult;
        }
        // 验证item1
        if (itemIds1 == null || itemIds1.size() == 0) {
            return AppErrorCode.SUPER_DUIHUAN_ITEM_NOT_ENOUGH;
        }
        GoodsConfigureVo item1 = sConfig.getItem1();
        boolean isZhuzhi1 = isShuzhi(item1);
        int item1Count = 0;
        if (!isZhuzhi1) {
            for (Long e : itemIds1) {
                RoleItemExport roleItemExport = roleBagExportService.getBagItemByGuid(userRoleId, e);
                if (roleItemExport == null) {
                    return AppErrorCode.SUPER_DUIHUAN_ITEM_NOT_ENOUGH;
                }
                String goodsId = roleItemExport.getGoodsId();
                GoodsConfig goodsConfig = goodsConfigExportService.loadById(goodsId);
                if (goodsConfig == null) {
                    return AppErrorCode.SUPER_DUIHUAN_ITEM_ERROR;
                }
                if (!goodsConfig.getId1().equals(item1.getGoodsId())) {
                    return AppErrorCode.SUPER_DUIHUAN_ITEM_ERROR;
                }
                if (roleItemExport.getQianhuaLevel() < item1.getQhLevel()) {
                    return AppErrorCode.SUPER_DUIHUAN_ITEM_ERROR;
                }
                // 过期物品不能兑换
                if (roleItemExport.isExpireTime()) {
                    return AppErrorCode.IS_EXPIRE_GOODS;
                }

                item1Count = item1Count + roleItemExport.getCount();
            }
            if (item1Count < item1.getGoodsCount()) {
                return AppErrorCode.SUPER_DUIHUAN_ITEM_NOT_ENOUGH;
            }
        } else {
            Object[] result = checkShuzhi(userRoleId, item1);
            if (result != null) {
                return result;
            }
        }
        // 验证item2
        if (itemIds2 == null || itemIds2.size() == 0) {
            return AppErrorCode.SUPER_DUIHUAN_ITEM_NOT_ENOUGH;
        }
        GoodsConfigureVo item2 = sConfig.getItem2();
        boolean isZhuzhi2 = isShuzhi(item2);
        if (!isZhuzhi2) {
            int item2Count = 0;
            for (Long e : itemIds2) {
                RoleItemExport roleItemExport = roleBagExportService.getBagItemByGuid(userRoleId, e);
                if (roleItemExport == null) {
                    return AppErrorCode.SUPER_DUIHUAN_ITEM_NOT_ENOUGH;
                }
                String goodsId = roleItemExport.getGoodsId();
                GoodsConfig goodsConfig = goodsConfigExportService.loadById(goodsId);
                if (goodsConfig == null) {
                    return AppErrorCode.SUPER_DUIHUAN_ITEM_ERROR;
                }
                if (!goodsConfig.getId1().equals(item2.getGoodsId())) {
                    return AppErrorCode.SUPER_DUIHUAN_ITEM_ERROR;
                }
                if (roleItemExport.getQianhuaLevel() < item2.getQhLevel()) {
                    return AppErrorCode.SUPER_DUIHUAN_ITEM_ERROR;
                }
                // 过期物品不能兑换
                if (roleItemExport.isExpireTime()) {
                    return AppErrorCode.IS_EXPIRE_GOODS;
                }
                item2Count = item2Count + roleItemExport.getCount();
            }
            if(item1.getGoodsId().equals(item2.getGoodsId())){
            	 if ((item2Count) < (item1.getGoodsCount()+item2.getGoodsCount())) {
                     return AppErrorCode.SUPER_DUIHUAN_ITEM_NOT_ENOUGH;
                 }
            }else{
            	 if (item2Count < item2.getGoodsCount()) {
                     return AppErrorCode.SUPER_DUIHUAN_ITEM_NOT_ENOUGH;
                 }
            }
        } else {
        	if(item1.getGoodsId().equals(item2.getGoodsId())){
        		GoodsConfigureVo newGoods = new GoodsConfigureVo(item2.getGoodsId(), item1.getGoodsCount()+item2.getGoodsCount());
        		 Object[] result = checkShuzhi(userRoleId, newGoods);
                 if (result != null) {
                     return result;
                 }
           }else{
        	   Object[] result = checkShuzhi(userRoleId, item2);
               if (result != null) {
                   return result;
               }
           }
            
        }
        // 消耗的元宝
        int consumeGold = 0;
        // 扣道具
        if (!isZhuzhi1) {
            int item1Need = item1.getGoodsCount();
            for (Long e : itemIds1) {
                if (item1Need > 0) {
                    RoleItemExport roleItemExport = roleBagExportService.getBagItemByGuid(userRoleId, e);
                    int count = roleItemExport.getCount() > item1Need ? item1Need : roleItemExport.getCount();
                    roleBagExportService.removeBagItemByGuid(e, count, userRoleId, GoodsSource.GOODS_SUPER_DUIHUAN_CONSUME, true, true);
                    item1Need = item1Need - count;
                }
            }
        } else {
            consumeGold += removeShuzhi(userRoleId, item1);
        }
        
        if (!isZhuzhi2) {
            int item2Need = item2.getGoodsCount();
            for (Long e : itemIds2) {
                if (item2Need > 0) {
                    RoleItemExport roleItemExport = roleBagExportService.getBagItemByGuid(userRoleId, e);
                    int count = roleItemExport.getCount() > item2Need ? item2Need : roleItemExport.getCount();
                    roleBagExportService.removeBagItemByGuid(e, count, userRoleId, GoodsSource.GOODS_SUPER_DUIHUAN_CONSUME, true, true);
                    item2Need = item2Need - count;
                }
            }
        } else {
            consumeGold += removeShuzhi(userRoleId, item2);
        }
        
        Map<String, GoodsConfigureVo> item3Map = new HashMap<String, GoodsConfigureVo>();
        item3Map.put(item3.getGoodsId(), item3);
        // 获得新道具
        roleBagExportService.putGoodsVoAndNumberAttr(item3Map, userRoleId, GoodsSource.GOODS_SUPER_DUIHUAN_GET, LogPrintHandle.GET_SUPER_DUIHUAN, LogPrintHandle.GBZ_SUPER_DUIHUAN, true);
        // 更新次数
        duihuan.setTimesCount(duihuan.getTimesCount() + 1);
        duihuan.setUpdateTime(GameSystemTime.getSystemMillTime());
        refabuSuperDuihuanDao.cacheUpdate(duihuan, userRoleId);

        // 打印活动参与日志
        GamePublishEvent.publishEvent(new RfbActivityPartInLogEvent(LogPrintHandle.REFABU_SUPERDUIHUAN, configSong.getActivityId(), configSong.getSubName(), configSong.getSubActivityType(), configSong.getStartTimeByMillSecond(), configSong.getEndTimeByMillSecond(), userRoleId));

        // 日志打印
        if(consumeGold > 0){
            try {
                RoleWrapper role = roleExportService.getLoginRole(userRoleId);
                GamePublishEvent.publishEvent(new RfbSuperDuiHuanLogEvent(userRoleId, role.getName(), consumeGold, item3.getGoodsId(), item3.getGoodsCount()));
            } catch (Exception e) {
                ChuanQiLog.error("打印热发布超值兑换活动日志错误:{}", e);
            }
        }
        return new Object[] { AppErrorCode.SUCCESS, subId, configId };
	}

	private boolean isShuzhi(GoodsConfigureVo vo) {
		if (vo.getGoodsId().equals(ModulePropIdConstant.GOLD_GOODS_ID)) {
			return true;
		}
		if (vo.getGoodsId().equals(ModulePropIdConstant.BIND_GOLD_GOODS_ID)) {
			return true;
		}
		if (vo.getGoodsId().equals(ModulePropIdConstant.MONEY_GOODS_ID)) {
			return true;
		}
		if (vo.getGoodsId().equals(ModulePropIdConstant.EXP_GOODS_ID)) {
			return true;
		}
		if (vo.getGoodsId().equals(ModulePropIdConstant.MONEY_ZHENQI_ID)) {
			return true;
		}
		return false;
	}

	private Object[] checkShuzhi(Long userRoleId, GoodsConfigureVo vo) {
		if (vo.getGoodsId().equals(ModulePropIdConstant.GOLD_GOODS_ID)) {
			RoleAccount account = accountExportService
					.getRoleAccount(userRoleId);
			if (account.getYb() < vo.getGoodsCount()) {
				return AppErrorCode.YUANBAO_NOT_ENOUGH;
			}
			return null;
		}
		if (vo.getGoodsId().equals(ModulePropIdConstant.BIND_GOLD_GOODS_ID)) {
			RoleAccount account = accountExportService
					.getRoleAccount(userRoleId);
			if (account.getBindYb() < vo.getGoodsCount()) {
				return AppErrorCode.BANG_YUANBAO_NOT_ENOUGH;
			}
			return null;
		}
		if (vo.getGoodsId().equals(ModulePropIdConstant.MONEY_GOODS_ID)) {
			RoleAccount account = accountExportService
					.getRoleAccount(userRoleId);
			if (account.getJb() < vo.getGoodsCount()) {
				return AppErrorCode.BANG_YUANBAO_NOT_ENOUGH;
			}
			return null;
		}
		if (vo.getGoodsId().equals(ModulePropIdConstant.EXP_GOODS_ID)) {
			return AppErrorCode.CONFIG_ERROR;
		}
		if (vo.getGoodsId().equals(ModulePropIdConstant.MONEY_ZHENQI_ID)) {
			return AppErrorCode.CONFIG_ERROR;
		}
		return null;
	}

    private int removeShuzhi(Long userRoleId, GoodsConfigureVo vo) {
        // 消耗的元宝
        int consumeGold = 0;
        if (vo.getGoodsId().equals(ModulePropIdConstant.GOLD_GOODS_ID)) {
            accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, vo.getGoodsCount(), userRoleId, LogPrintHandle.CONSUME_SUPER_DUIHUAN, true, LogPrintHandle.CBZ_SUPER_DUIHUAN);
            if (PlatformConstants.isQQ()) {
                BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_YB, vo.getGoodsCount(), LogPrintHandle.CONSUME_SUPER_DUIHUAN, QQXiaoFeiType.CONSUME_SUPER_DUIHUAN, 1 });
            }
            consumeGold = vo.getGoodsCount();
        }
        if (vo.getGoodsId().equals(ModulePropIdConstant.BIND_GOLD_GOODS_ID)) {
            accountExportService.decrCurrencyWithNotify(GoodsCategory.BGOLD, vo.getGoodsCount(), userRoleId, LogPrintHandle.CONSUME_SUPER_DUIHUAN, true, LogPrintHandle.CBZ_SUPER_DUIHUAN);
            if (PlatformConstants.isQQ()) {
                BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_BYB, vo.getGoodsCount(), LogPrintHandle.CONSUME_SUPER_DUIHUAN, QQXiaoFeiType.CONSUME_SUPER_DUIHUAN, 1 });
            }
        }
        if (vo.getGoodsId().equals(ModulePropIdConstant.MONEY_GOODS_ID)) {
            accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY, vo.getGoodsCount(), userRoleId, LogPrintHandle.CONSUME_SUPER_DUIHUAN, true, LogPrintHandle.CBZ_SUPER_DUIHUAN);
        }
        return consumeGold;
    }
}
