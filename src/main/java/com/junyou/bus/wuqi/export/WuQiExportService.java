package com.junyou.bus.wuqi.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.wuqi.configure.export.XinShengJianJiChuConfig;
import com.junyou.bus.wuqi.configure.export.XinShengJianJiChuConfigExportService;
import com.junyou.bus.wuqi.entity.WuQiInfo;
import com.junyou.bus.wuqi.manage.WuQi;
import com.junyou.bus.wuqi.service.WuQiService;
import com.junyou.bus.wuqi.vo.WuQiRankVo;
import com.junyou.public_.rank.export.IWuqiRankExportService;
import com.junyou.public_.share.export.PublicRoleStateExportService;

@Service
public class WuQiExportService implements IWuqiRankExportService<WuQiRankVo>{
	
	@Autowired
	private WuQiService wuQiService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	
	/**
	 * 获得新圣剑的属性
	 */
	public Map<String,Long> getWuQiAttrs(Long userRoleId,WuQi zuoqi,int dj){
		return wuQiService.getWuQiAttrs(userRoleId,zuoqi,dj);
	}
	
//	/**
//	 * 获得坐骑移动速度属性
//	 */
//	public Map<String,Long> getZuoQiSeedAttr(ZuoQi zuoqi){
//		return zuoQiService.getWuQiSeedAttr(zuoqi);
//	} 
	
	public WuQiInfo getWuQiInfo(long userRoleId){
		return wuQiService.getWuQiInfo(userRoleId);
	}

	public WuQiService getWuQiService() {
		return wuQiService;
	}

	public void setWuQiService(WuQiService wuQiService) {
		this.wuQiService = wuQiService;
	}

	public Object[] getWuQiEquips(Long userRoleId) {
		return wuQiService.getWuQiEquips(userRoleId);
	}

	public Object[] useWuQiQND(Long userRoleId,int count) {
		  return wuQiService.wqUseQND(userRoleId,count);
	}

	public Object[] addWuQiCZD(Long userRoleId, int consumeCount) {
		 return wuQiService.zqUseCZD(userRoleId,consumeCount);
	}
	/**
	 * 新圣剑消耗道具 直升
	 * @param userRoleId
	 * @param minLevel
	 * @param maxLevel
	 * @param zfz
	 * @return
	 */
	public Object[] sjByItem(Long userRoleId, Integer minLevel, Integer maxLevel, Integer zfz){
		return  wuQiService.sjByItem(userRoleId, minLevel, maxLevel, zfz);
	}
	
	/**
	 * 促销奖励直升
	 * @param zfz
	 * @return
	 */
	public Object[] sjByCuxiao(Long userRoleId, int newLevel){
		return  wuQiService.sjByCuxiao(userRoleId, newLevel);
	}
	
	
//	/**
//	 * 新圣剑换装
//	 * @param userRoleId
//	 * @param guid
//	 * @param targetSlot
//	 * @param containerType
//	 * @return
//	 */
//	public Object[] zuoqiChangeEquip(Long userRoleId, long guid,int targetSlot, int containerType) {
//		return wuQiService.zuoqiChangeEquip(userRoleId, guid,targetSlot, containerType);
//	}
	
	/**
	 * 如果坐骑没有开启，发送-1出去
	 * @param userRoleId
	 * @return
	 */
	public Integer getWuQiInfoLevel(long userRoleId){
		WuQiInfo zuoQiInfo = wuQiService.getWuQiInfo(userRoleId);
		if(zuoQiInfo == null){
			return -1;
		}
		return zuoQiInfo.getZuoqiLevel();
	}
	
	public Integer getWuQiInfoLevelOther(long userRoleId){
		WuQiInfo zuoQiInfo = wuQiService.getWuQiInfo(userRoleId);
		if(zuoQiInfo == null){
			return -1;
		}
		XinShengJianJiChuConfig config = xinShengJianConfigExportService.loadById(zuoQiInfo.getZuoqiLevel());
		return config.getLevel();
	}
	
	
	@Autowired
	private XinShengJianJiChuConfigExportService xinShengJianConfigExportService; 

	public WuQiInfo initZuoQi(Long userRoleId) {
		return wuQiService.initWuQi(userRoleId);
	}

	@Override
	public List<WuQiRankVo> getWuqiRankVo(int limit) {
		return wuQiService.getWuqiRankVo(limit);
	}

	public WuQiInfo getWuQiInfoOnOROff(long userRoleId){
		if(publicRoleStateExportService.isPublicOnline(userRoleId)){
			WuQiInfo zuoqi = wuQiService.getWuQiInfo(userRoleId);
			
			return zuoqi;
		}else{
			WuQiInfo zuoqi = wuQiService.getWuQiDB(userRoleId);
			if(zuoqi == null){
				return null;
			}
			return zuoqi;
		}
	}
	
	public void onlineHandle(Long userRoleId){
		wuQiService.onlineHandle(userRoleId);
	}
	
    public void offlineHandle(Long userRoleId){
        wuQiService.offlineHandle(userRoleId);
    }
	
//	public int getZuoQiShowId(long userRoleId) {
//		ZuoQiInfo zuoqiInfo = zuoQiService.getZuoQiInfo(userRoleId);
//		if(zuoqiInfo == null){
//			return   -1;
//		}
//		return zuoqiInfo.getIsGetOn().intValue() == 1 ? zuoqiInfo.getShowId() : -1;
//	}
	
	public int getSkillMaxCount(Long userRoleId){
		return wuQiService.getSkillMaxCount(userRoleId);
	}
	
	public void noticeAttrChange(Long userRoleId){
		wuQiService.notifyStageWuqiChange(userRoleId);
	}
	
	public Object[] wuqiUpdateShowByHuanhua(Long userRoleId,Integer showId){
		return wuQiService.wuqiUpdateShow(userRoleId, showId, false);
	}
}
