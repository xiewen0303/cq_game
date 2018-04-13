/**
 * 
 */
package com.junyou.bus.xingkongbaozang.server;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.service.AbstractActivityService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xingkongbaozang.configure.export.XkbzConfig;
import com.junyou.bus.xingkongbaozang.configure.export.XkbzConfigExportService;
import com.junyou.bus.xingkongbaozang.configure.export.XkbzConfigGroup;
import com.junyou.bus.xingkongbaozang.dao.RefabuXkbzDao;
import com.junyou.bus.xingkongbaozang.entity.RefabuXkbz;
import com.junyou.bus.xingkongbaozang.filter.XkbzFilter;
import com.junyou.cmd.ClientCmdType;
import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;


/**
 *星空宝藏
 */
@Service
public class XkbzService extends AbstractActivityService {

	@Override
	public boolean getChildFlag(long userRoleId,int subId) {
		return checkA1(userRoleId, subId);
	}
	
	
	private boolean checkA1(long userRoleId,int subId){
		//判断配置
		XkbzConfigGroup configs = XkbzConfigExportService.getInstance().loadByMap(subId);
		if(configs == null){
			return false;
		}
		RefabuXkbz xkbz = getRefabuXkbz(userRoleId, subId);
		//玩家已领取次数
		if(xkbz == null){
			return false;
		}
		for (XkbzConfig config : configs.getConfigMap().values()) {
			int configId = config.getId();
			
			if(config.getJifen().intValue() > xkbz.getJifen()){
				continue;
			}
			//判断是否已经领取过
			if(!isContains(xkbz.getLingquStatus(), configId)){
				continue;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 状态数据中 是否包含 id数据
	 * @param status
	 * @param id
	 * @return true 不存在，false 存在
	 */
	private boolean isContains(String status,int id){
		if(status == null || status.equals("")){
			return true;
		}
		String[] str = status.split(",");
		for (int i = 0; i < str.length; i++) {
			if(id == Integer.parseInt(str[i])){
				return false;
			}
		}
		return true;
	}
	
	@Autowired
	private RefabuXkbzDao refabuXkbzDao;
	@Autowired
	private RoleBagExportService roleBagExportService;
	
	public List<RefabuXkbz> initRefabuXkbz(Long userRoleId){
		return refabuXkbzDao.initRefabuXkbz(userRoleId);
	}
	
	public RefabuXkbz getRefabuXkbz(Long userRoleId,int subId){
		List<RefabuXkbz> list = refabuXkbzDao.cacheLoadAll(userRoleId, new XkbzFilter(subId));
		if(list == null || list.size() <= 0){
			RefabuXkbz xkbz = new RefabuXkbz();
			xkbz.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
			xkbz.setUserRoleId(userRoleId);
			xkbz.setSubId(subId);
			xkbz.setJifen(0);
			xkbz.setXfBgold(0);
			xkbz.setXfGold(0);
			xkbz.setLingquStatus("");
			xkbz.setCreateTime(System.currentTimeMillis());
			xkbz.setUpdateTime(System.currentTimeMillis());
			refabuXkbzDao.cacheInsert(xkbz, userRoleId);
			return xkbz;
		}
		RefabuXkbz result = list.get(0); 
		updateJianCe(subId, result);
		
		return result;
	}
	
	private void updateJianCe(int subId,RefabuXkbz xkbz){
		//判断活动是否结束
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		long startTime = configSong.getStartTimeByMillSecond();//活动开始时间
		long upTime = xkbz.getUpdateTime();
		long dTime = GameSystemTime.getSystemMillTime();
		if(startTime  > upTime && startTime < dTime){//如果活动开始时间大于业务上次更新时间，清理业务数据
			xkbz.setJifen(0);
			xkbz.setXfBgold(0);
			xkbz.setXfGold(0);
			xkbz.setLingquStatus("");
			xkbz.setUpdateTime(System.currentTimeMillis());
			
			refabuXkbzDao.cacheUpdate(xkbz, xkbz.getUserRoleId());
			return;
		}
		XkbzConfigGroup config = XkbzConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return;
		}
		//不是同一天，清理数据
		if(config.getClean().intValue() > 0 &&!DatetimeUtil.dayIsToday(xkbz.getUpdateTime())){
			xkbz.setJifen(0);
			xkbz.setXfBgold(0);
			xkbz.setXfGold(0);
			xkbz.setLingquStatus("");
			xkbz.setUpdateTime(System.currentTimeMillis());
			
			refabuXkbzDao.cacheUpdate(xkbz, xkbz.getUserRoleId());
			return;
			
		}
	}
	
	
	public Object[] lingqu(Long userRoleId,Integer version,int subId,int configId){
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		
		//版本不一样
		if(configSong.getClientVersion() != version){
			//处理数据变化:
			Object newSubHandleData = getRefbInfo(userRoleId, subId);
			Object[] data = new Object[]{subId,configSong.getClientVersion(),newSubHandleData};
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY,data );
			return null;
		}
		
		//判断配置
		XkbzConfig config = XkbzConfigExportService.getInstance().loadByKeyId(subId,configId);
		if(config== null){
			return AppErrorCode.CONFIG_ERROR;
		}
		RefabuXkbz xkbz = getRefabuXkbz(userRoleId, subId);
		
		if(config.getJifen().intValue() > xkbz.getJifen()){
			return AppErrorCode.XKBZ_NO_YAOQIU;
		}
		if(!isContains(xkbz.getLingquStatus(), configId)){
			return AppErrorCode.XKBZ_YI_LINGQU;
		}
		Map<String,GoodsConfigureVo> jiangli = config.getItemMap();
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBagVo(jiangli, userRoleId);
		if(bagCheck != null){
			return bagCheck;
		}
		
		//更新玩家领取状态
		if("".equals(xkbz.getLingquStatus())){
			xkbz.setLingquStatus(configId +"");
		}else{
			xkbz.setLingquStatus(xkbz.getLingquStatus() + GameConstants.SQL_WHERE_INT_JOIN+configId +"");
		}
		xkbz.setUpdateTime(System.currentTimeMillis());
		
		refabuXkbzDao.cacheUpdate(xkbz, userRoleId);
		
		//发放奖励
		roleBagExportService.putGoodsVoAndNumberAttr(jiangli, userRoleId, GoodsSource.GOODS_GET_XKBZ, LogPrintHandle.GET_XKBZ, LogPrintHandle.GBZ_XKBZ, true);
		
		super.checkIconFlag(userRoleId, subId);
		
		return new Object[]{1,subId,configId};
	}
	
	/**
	 * 获取某个子活动的热发布某个活动信息
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		XkbzConfigGroup config = XkbzConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		//处理数据
		RefabuXkbz xkbz = getRefabuXkbz(userRoleId, subId);		

		List<String> list = new ArrayList<>();
		if(xkbz.getLingquStatus() != null && !"".equals(xkbz.getLingquStatus())){
			String[] ids =xkbz.getLingquStatus().split(GameConstants.SQL_WHERE_INT_JOIN);
			for (int i = 0; i < ids.length; i++) {
				list.add(ids[i]);
			}
		}
		return new Object[]{
				config.getPic(),
				config.getDes(),
				config.getConfigVo(),
				xkbz.getJifen(),
				list.toArray(),
				config.getDes2()
		};
	}
	
	public Object[] getRefbLingQuStatus(Long userRoleId, Integer subId){
		RefabuXkbz xkbz = getRefabuXkbz(userRoleId, subId);		
		
		List<String> list = new ArrayList<>();
		if(xkbz.getLingquStatus() != null && !"".equals(xkbz.getLingquStatus())){
			String[] ids =xkbz.getLingquStatus().split(GameConstants.SQL_WHERE_INT_JOIN);
			for (int i = 0; i < ids.length; i++) {
				list.add(ids[i]);
			}
		}
		return new Object[]{subId,xkbz.getJifen(),list.toArray()};
	}
	
	
	
	public void xiaofeiYb(Long userRoleId,Long addVal,Integer goldType){
		if(addVal < 0){
			return;
		}
		Map<Integer, XkbzConfigGroup> groups = XkbzConfigExportService.getInstance().getAllConfig();
		if(groups.size() == 0){
			return;
		}
		//循环充值礼包配置数据
		for(Map.Entry<Integer, XkbzConfigGroup> entry: groups.entrySet()){
			//是否在有这个活动或者是否在时间内
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(entry.getKey());
			if(configSong == null || !configSong.isRunActivity()){
				continue;
			}
			RefabuXkbz xkbz = getRefabuXkbz(userRoleId,  entry.getKey());		
			
			if(goldType == 1){//元宝
				addVal += xkbz.getXfGold();
				//转成积分
				float bl = entry.getValue().getGoldJifen();
				int sg =  (int) (addVal % (1/bl));
				xkbz.setXfGold(sg);
				addVal = addVal - sg;
				int jifen = (int) (addVal / (1/bl));
				xkbz.setJifen(xkbz.getJifen() + jifen);
			}else{//绑元
				addVal += xkbz.getXfBgold();
				//转成积分
				float bl = entry.getValue().getbGoldJifen();
				int sg =  (int) (addVal % (1/bl));
				xkbz.setXfBgold(sg);
				addVal = addVal - sg;
				int jifen = (int) (addVal / (1/bl));
				xkbz.setJifen(xkbz.getJifen() + jifen);
			}
			xkbz.setUpdateTime(GameSystemTime.getSystemMillTime());
			refabuXkbzDao.cacheUpdate(xkbz, userRoleId);
			
			List<String> list = new ArrayList<>();
			if(xkbz.getLingquStatus() != null && !"".equals(xkbz.getLingquStatus())){
				String[] ids =xkbz.getLingquStatus().split(GameConstants.SQL_WHERE_INT_JOIN);
				for (int i = 0; i < ids.length; i++) {
					list.add(ids[i]);
				}
			}
			
			BusMsgSender.send2One(userRoleId, ClientCmdType.UPDATE_XINGKONGBAOZHAN, new Object[]{entry.getKey(),xkbz.getJifen(),list.toArray()});

			//检查通知客服端关闭掉Icon提示
			checkIconFlag(userRoleId, configSong.getId());
		}
	}
}