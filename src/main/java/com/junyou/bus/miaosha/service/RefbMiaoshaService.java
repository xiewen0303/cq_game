package com.junyou.bus.miaosha.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.miaosha.configure.MiaoShaConfig;
import com.junyou.bus.miaosha.configure.MiaoShaConfigExportService;
import com.junyou.bus.miaosha.configure.MiaoShaConfigGroup;
import com.junyou.bus.miaosha.configure.MiaoShaGoodsVo;
import com.junyou.bus.miaosha.dao.RefbMiaoshaDao;
import com.junyou.bus.miaosha.entity.RefbMiaosha;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.err.AppErrorCode;
import com.junyou.event.MiaoShaLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.kernel.cache.redis.Redis;
import com.kernel.cache.redis.RedisKey;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 * @author LiuYu
 * 2016-3-4 下午2:45:16
 */
@Service
public class RefbMiaoshaService {

	@Autowired
	private RefbMiaoshaDao refbMiaoshaDao;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private RoleExportService roleExportService;
	
	public List<RefbMiaosha> initRefbMiaosha(Long userRoleId) {
		return refbMiaoshaDao.initRefbMiaosha(userRoleId);
	}
	
	private RefbMiaosha getRefbMiaosha(long userRoleId,final int subId){
		List<RefbMiaosha> list = refbMiaoshaDao.cacheLoadAll(userRoleId, new IQueryFilter<RefbMiaosha>() {
			private boolean stop;
			@Override
			public boolean check(RefbMiaosha entity) {
				stop = entity.getSubId().equals(subId);
				return stop;
			}

			@Override
			public boolean stopped() {
				return stop;
			}
		});
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	public Object[] buy(long userRoleId,int subId, int version,int boxId){
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if (configSong == null) {
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		if (configSong.getClientVersion() != version) {
			// 处理数据变化:
			Object newSubHandleData = getRefbInfo(userRoleId, subId);
			Object[] data = new Object[] { subId, configSong.getClientVersion(), newSubHandleData };
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY, data);
			return null;
		}
		long date00Time = DatetimeUtil.getDate00Time();
		long cur = GameSystemTime.getSystemMillTime() - date00Time;
		MiaoShaConfig config = MiaoShaConfigExportService.getInstance().loadByKeyId(subId,cur);
		if(config == null){
			return AppErrorCode.MIAOSHA_NO_ACTIVE;//当前无活动
		}
		
		if(config.getTime().getStartTime() > cur){
			return AppErrorCode.MIAOSHA_ACTIVE_NOT_START;//活动尚未开启
		}
		long endTime = config.getTime().getEndTime() + date00Time;
		RefbMiaosha refbMiaosha = getRefbMiaosha(userRoleId, subId);
		if(refbMiaosha != null && !refbMiaosha.isCanBuy(endTime)){
			return AppErrorCode.MIAOSHA_IS_JOIN;//已购买过
		}
		Redis redis = GameServerContext.getRedis();
		if(redis == null){
			return AppErrorCode.KUAFU_NO_CONNECTION;
		}
		Object[] ret = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, config.getGold(), userRoleId, LogPrintHandle.CONSUME_MIAOSHA, true,LogPrintHandle.CBZ_CONSUME_MIAOSHA);
		if(ret != null){
			return ret;
		}
		
		MiaoShaGoodsVo vo = Lottery.getRandomKeyByInteger(config.getDrop(), config.getTotal());
		if(vo == null){
			ChuanQiLog.error("miaosha error.no box gift.userRoleId:{},subId:{},id:{}",userRoleId,subId,config.getId());
			return AppErrorCode.CONFIG_ERROR;//配置异常,日志记录
		}
		boolean create = false;
		if(refbMiaosha == null){
			refbMiaosha = new RefbMiaosha();
			refbMiaosha.setId(IdFactory.getInstance().generateId(ServerIdType.REFABU));
			refbMiaosha.setUserRoleId(userRoleId);
			refbMiaosha.setSubId(subId);
			create = true;
		}
		refbMiaosha.setBoxId(boxId);
		refbMiaosha.setEndTime(endTime);
		if(create){
			refbMiaoshaDao.cacheInsert(refbMiaosha, userRoleId);
		}else{
			refbMiaoshaDao.cacheUpdate(refbMiaosha, userRoleId);
		}
		Map<String,Integer> goods = new HashMap<>();
		goods.put(vo.getGoodsId(), vo.getCount());
		boolean first = false;
		if(vo.isJipin()){
			//检测是否是第一个打开极品的
			String firstKey = RedisKey.getKFMSFirstKeyPrifix(subId,config.getId());
			if(redis.setNx(firstKey, userRoleId+"")){
				first = true;
				redis.expire(firstKey, GameConstants.MIAOSHA_REDIS_EXPIRE_TIME);
			}
		}
		String keyPrifix = RedisKey.getKFMSTop10KeyPrifix(subId,config.getId());
		int count = redis.keys(keyPrifix).size();
		boolean top10 = false;
		if(count < 10){
			while(count < 10){
				if(redis.setNx(keyPrifix+count, userRoleId+"")){
					top10 = true;
					//设置时限
					redis.expire(keyPrifix+count, GameConstants.MIAOSHA_REDIS_EXPIRE_TIME);
					break;
				}
				count++;
			}
		}
		if(first){
			ObjectUtil.mapAdd(goods, config.getFirst());
		}
		if(top10){
			ObjectUtil.mapAdd(goods, config.getTop10());
		}
		ObjectUtil.mapAdd(goods, config.getJoin());
		String title = EmailUtil.getCodeEmail(GameConstants.MIAOSHA_GIFT_EMAIL_TITLE);
		String content = EmailUtil.getCodeEmail(GameConstants.MIAOSHA_GIFT_EMAIL_CODE);
		for (String attachment : EmailUtil.getAttachments(goods)) {
			emailExportService.sendEmailToOne(userRoleId, title,content, GameConstants.EMAIL_TYPE_SINGLE, attachment);
		}
		// 日志
		GamePublishEvent.publishEvent(new MiaoShaLogEvent(userRoleId, getRoleName(userRoleId), subId, config.getId(), config.getGold(), first, top10, LogPrintHandle.getLogGoodsParam(goods, null)));
		
		return new Object[]{1,new Object[]{subId,boxId,first,top10,vo.getClient()}};
	}
	
	//获取玩家名字
	private String getRoleName(Long userRoleId){
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		return role.getName();
	}
	
	public Object[] getRefbInfo(long userRoleId,int subId){
		MiaoShaConfigGroup group = MiaoShaConfigExportService.getInstance().loadByMap(subId);
		if(group == null){
			return null;
		}
		long date00Time = DatetimeUtil.getDate00Time();
		long cur = GameSystemTime.getSystemMillTime() - date00Time;
		MiaoShaConfig config = group.getConfig(cur);
		if(config == null){
			return new Object[]{-1};
		}
		Object[] ret = new Object[]{
				group.getPic(),
				group.getDes(),
				group.getOpenTime(),
				config.getJipinGoods(),
				config.getGold(),
				config.getClientVo(),
				null,
				null
		};
		RefbMiaosha refbMiaosha = getRefbMiaosha(userRoleId, subId);
		if(refbMiaosha == null){
			ret[6] = 0;
		}else{
			ret[6] = refbMiaosha.getBoxId();
		}
		if(cur < config.getTime().getStartTime()){
			ret[7] = new Object[]{1,config.getTime().getStartTime() + date00Time};
		}else{
			ret[7] = new Object[]{2,config.getTime().getEndTime() + date00Time};
		}
		return ret;
	}
	
	public Object[] getRefbStates(long userRoleId,int subId){
		long date00Time = DatetimeUtil.getDate00Time();
		long cur = GameSystemTime.getSystemMillTime() - date00Time;
		MiaoShaConfig config = MiaoShaConfigExportService.getInstance().loadByKeyId(subId, cur);
		if(config == null){
			return null;
		}
		Object[] ret = new Object[]{
				subId,
				config.getGold(),
				null,
				null,
				config.getJipinGoods(),
				config.getClientVo()
		};
		RefbMiaosha refbMiaosha = getRefbMiaosha(userRoleId, subId);
		if(refbMiaosha == null){
			ret[2] = 0;
		}else{
			ret[2] = refbMiaosha.getBoxId();
		}
		if(cur < config.getTime().getStartTime()){
			ret[3] = new Object[]{1,config.getTime().getStartTime() + date00Time};
		}else{
			ret[3] = new Object[]{2,config.getTime().getEndTime() + date00Time};
		}
		return ret;
	}
	
	public Object[] getInfo(long userRoleId,int subId,int version){
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if (configSong == null) {
			return null;
		}
		if (configSong.getClientVersion() != version) {
			// 处理数据变化:
			Object newSubHandleData = getRefbInfo(userRoleId, subId);
			Object[] data = new Object[] { subId, configSong.getClientVersion(), newSubHandleData };
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY, data);
			return null;
		}
		return getRefbStates(userRoleId, subId);
	}
}
