package com.junyou.bus.huanhua.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.chibang.export.ChiBangExportService;
import com.junyou.bus.huanhua.configure.YuJianHuanHuaBiaoConfig;
import com.junyou.bus.huanhua.configure.YuJianHuanHuaBiaoConfigExportService;
import com.junyou.bus.huanhua.constants.HuanhuaConstants;
import com.junyou.bus.huanhua.dao.RoleHuanhuaDao;
import com.junyou.bus.huanhua.entity.RoleHuanhua;
import com.junyou.bus.wuqi.export.WuQiExportService;
import com.junyou.bus.xianjian.export.XianJianExportService;
import com.junyou.bus.zhanjia.export.ZhanJiaExportService;
import com.junyou.bus.zuoqi.export.ZuoQiExportService;
import com.junyou.err.AppErrorCode;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

@Service
public class HuanHuaService {
	@Autowired
	private RoleHuanhuaDao roleHuanhuaDao;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private YuJianHuanHuaBiaoConfigExportService yuJianHuanHuaBiaoConfigExportService;
	@Autowired
	private ZuoQiExportService zuoQiExportService;
	@Autowired
	private ChiBangExportService chiBangExportService;
	@Autowired
	private XianJianExportService xianJianExportService;
	@Autowired
	private ZhanJiaExportService zhanJiaExportService;
	@Autowired
	private WuQiExportService wuQiExportService;
	
	public List<RoleHuanhua> initRoleHuanhua(Long userRoleId) {
		return roleHuanhuaDao.initRoleHuanhua(userRoleId);
	}

	public RoleHuanhua createRoleHuanhua(Long userRoleId, Integer type,
			Integer configId) {
		RoleHuanhua roleHuanhua = new RoleHuanhua();
		roleHuanhua.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		roleHuanhua.setType(type);
		roleHuanhua.setConfigId(configId);
		roleHuanhua.setUserRoleId(userRoleId);
		roleHuanhua.setCreateTime(GameSystemTime.getSystemMillTime());
		roleHuanhuaDao.cacheInsert(roleHuanhua, userRoleId);
		return roleHuanhua;
	}

	public List<RoleHuanhua> getRoleHuanhua(Long userRoleId, final Integer type) {
		List<RoleHuanhua> list = roleHuanhuaDao.cacheLoadAll(userRoleId,
				new IQueryFilter<RoleHuanhua>() {

					@Override
					public boolean check(RoleHuanhua info) {
						return type.equals(info.getType());
					}

					@Override
					public boolean stopped() {
						return false;
					}
				});
		return list;
	}

	public List<Integer> getRoleHuanhuaConfigList(Long userRoleId,
			final Integer type) {
		List<RoleHuanhua> list = roleHuanhuaDao.cacheLoadAll(userRoleId,
				new IQueryFilter<RoleHuanhua>() {

					@Override
					public boolean check(RoleHuanhua info) {
						return type.equals(info.getType());
					}

					@Override
					public boolean stopped() {
						return false;
					}
				});
		if (list != null) {
			List<Integer> ret = new ArrayList<Integer>();
			for (RoleHuanhua e : list) {
				ret.add(e.getConfigId());
			}
			return ret;
		}
		return null;
	}

	public RoleHuanhua getRoleHuanhua(Long userRoleId, final Integer type,
			final Integer configId) {
		List<RoleHuanhua> list = roleHuanhuaDao.cacheLoadAll(userRoleId,
				new IQueryFilter<RoleHuanhua>() {
					private boolean stop = false;

					@Override
					public boolean check(RoleHuanhua roleHuanhua) {
						if (roleHuanhua.getType().equals(type)
								&& roleHuanhua.getConfigId().equals(configId)) {
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

	public Object[] getHuanhuaInfo(Long userRoleId, int type) {
		Object[] ret = new Object[] { AppErrorCode.SUCCESS, type, null };
		List<RoleHuanhua> list = getRoleHuanhua(userRoleId, type);
		if (list == null) {
			return ret;
		}
		Object[] array = new Object[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i).getConfigId();
		}
		ret[2] = array;
		return ret;
	}

	public Object[] huanhua(Long userRoleId, int type, int configId) {
		RoleHuanhua roleHuanhua = getRoleHuanhua(userRoleId, type, configId);
		if (roleHuanhua == null) {
			return AppErrorCode.HUANHUA_NOT_ACTIVATED;
		}
		Object[] ret=null;
		if(type == HuanhuaConstants.HUANHUA_TYPE_1){
			ret = zuoQiExportService.zuoqiUpdateShowByHuanhua(userRoleId, configId);
		}else if(type == HuanhuaConstants.HUANHUA_TYPE_2){
			ret = chiBangExportService.chiBangUpdateShowByHuanhua(userRoleId, configId);
		}else if(type == HuanhuaConstants.HUANHUA_TYPE_3){
			ret = xianJianExportService.xianjianUpdateShowByHuanhua(userRoleId, configId);
		}else if(type == HuanhuaConstants.HUANHUA_TYPE_4){
			ret = zhanJiaExportService.zhanjiaUpdateShowByHuanhua(userRoleId, configId);
		}if(type == HuanhuaConstants.HUANHUA_TYPE_7){
			ret = wuQiExportService.wuqiUpdateShowByHuanhua(userRoleId, configId);
		}
		if(ret!=null && ((Integer)ret[0])==1){
			return new Object[]{AppErrorCode.SUCCESS,type,configId};
		}
		return ret;
	}

	public Object[] huanhuaActivate(Long userRoleId, int type, int configId) {
		RoleHuanhua roleHuanhua = getRoleHuanhua(userRoleId, type, configId);
		if (roleHuanhua != null) {
			return AppErrorCode.HUANHUA_ACTIVATED;
		}
		YuJianHuanHuaBiaoConfig config = yuJianHuanHuaBiaoConfigExportService
				.loadById(configId);
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		String needitem = config.getNeeditem();
		BagSlots bagSlots = roleBagExportService.removeBagItemByGoodsId(
				needitem, 1, userRoleId, GoodsSource.HUANHUA_ACTIVATED, true,
				true);
		if (bagSlots.getErrorCode() != null) {
			return bagSlots.getErrorCode();
		}
		createRoleHuanhua(userRoleId, type, configId);
		ChuanQiLog.info("userRoleId={} huanhuaActivate={}", userRoleId,
				configId);
		if (type == HuanhuaConstants.HUANHUA_TYPE_1) {
			zuoQiExportService.noticeAttrChange(userRoleId);
		} else if (type == HuanhuaConstants.HUANHUA_TYPE_2) {
			chiBangExportService.noticeAttrChange(userRoleId);
		} else if (type == HuanhuaConstants.HUANHUA_TYPE_3) {
			xianJianExportService.noticeAttrChange(userRoleId);
		} else if (type == HuanhuaConstants.HUANHUA_TYPE_4) {
			zhanJiaExportService.noticeAttrChange(userRoleId);
		} else if (type == HuanhuaConstants.HUANHUA_TYPE_7) {
			wuQiExportService.noticeAttrChange(userRoleId);
		}
		return new Object[] { AppErrorCode.SUCCESS,type, configId };
	}
}
