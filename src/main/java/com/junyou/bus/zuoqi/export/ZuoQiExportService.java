package com.junyou.bus.zuoqi.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.zuoqi.configure.export.YuJianJiChuConfig;
import com.junyou.bus.zuoqi.configure.export.YuJianJiChuConfigExportService;
import com.junyou.bus.zuoqi.entity.ZuoQiInfo;
import com.junyou.bus.zuoqi.manage.ZuoQi;
import com.junyou.bus.zuoqi.service.ZuoQiService;
import com.junyou.bus.zuoqi.vo.ZuoqiRankVo;
import com.junyou.public_.rank.export.IZuoqiRankExportService;
import com.junyou.public_.share.export.PublicRoleStateExportService;

@Service
public class ZuoQiExportService implements IZuoqiRankExportService<ZuoqiRankVo>{
	
	@Autowired
	private ZuoQiService zuoQiService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private YuJianJiChuConfigExportService yuJianJiChuConfigExportService;
	
	/**
	 * 获得坐骑的属性（除移动速度）
	 */
	public Map<String,Long> getZuoQiAttrs(Long userRoleId,ZuoQi zuoqi,int dj){
		return zuoQiService.getZuoQiAttrs(userRoleId,zuoqi,dj);
	} 
	
	/**
	 * 获得坐骑移动速度属性
	 */
	public Map<String,Long> getZuoQiSeedAttr(ZuoQi zuoqi){
		return zuoQiService.getZuoQiSeedAttr(zuoqi);
	} 
	
	
	public ZuoQiService getZuoQiService() {
		return zuoQiService;
	}

	public void setZuoQiService(ZuoQiService zuoQiService) {
		this.zuoQiService = zuoQiService;
	}
	
	public ZuoQiInfo getZuoQiInfo(long userRoleId){
		return zuoQiService.getZuoQiInfo(userRoleId);
	}

	public Object[] getZuoQiEquips(Long userRoleId) {
		return zuoQiService.getZuoQiEquips(userRoleId);
	}

	public Object[] useZuoqiQND(Long userRoleId,int count) {
		  return zuoQiService.zqUseQND(userRoleId,count); 
	}

	public Object[] addZuoQiCZD(Long userRoleId, int consumeCount) {
		 return zuoQiService.zqUseCZD(userRoleId,consumeCount);
	}
	/**
	 * 坐骑消耗道具 直升
	 * @param userRoleId
	 * @param minLevel
	 * @param maxLevel
	 * @param zfz
	 * @return
	 */
	public Object[] sjByItem(Long userRoleId, Integer minLevel, Integer maxLevel, Integer zfz){
		return  zuoQiService.sjByItem(userRoleId, minLevel, maxLevel, zfz);
	}
	
	/**
	 * 促销奖励直升
	 * @param zfz
	 * @return
	 */
	public Object[] sjByCuxiao(Long userRoleId, int newLevel){
		return  zuoQiService.sjByCuxiao(userRoleId, newLevel);
	}
	
	
	/**
	 * 坐骑换装
	 * @param userRoleId
	 * @param guid
	 * @param targetSlot
	 * @param containerType
	 * @return
	 */
	public Object[] zuoqiChangeEquip(Long userRoleId, long guid,int targetSlot, int containerType) {
		return zuoQiService.zuoqiChangeEquip(userRoleId, guid,targetSlot, containerType);
	}
	
	/**
	 * 如果坐骑没有开启，发送-1出去
	 * @param userRoleId
	 * @return
	 */
	public Integer getZuoQiInfoLevel(long userRoleId){
		ZuoQiInfo zuoQiInfo = zuoQiService.getZuoQiInfo(userRoleId);
		if(zuoQiInfo == null){
			return -1;
		}
		return zuoQiInfo.getZuoqiLevel();
	}
	
	/**
	 * 如果坐骑没有开启，发送-1出去
	 * @param userRoleId
	 * @return
	 */
	public Integer getZuoQiInfoLevelOther(long userRoleId){
		ZuoQiInfo zuoQiInfo = zuoQiService.getZuoQiInfo(userRoleId);
		if(zuoQiInfo == null){
			return -1;
		}
		
		YuJianJiChuConfig config = yuJianJiChuConfigExportService.loadById(zuoQiInfo.getZuoqiLevel());
		return config.getLevel();
	}

	public ZuoQiInfo initZuoQi(Long userRoleId) {
		return zuoQiService.initZuoQi(userRoleId);
	}

	@Override
	public List<ZuoqiRankVo> getZuoqiRankVo(int limit) {
		return zuoQiService.getZuoqiRankVo(limit);
	}

	public ZuoQiInfo getZuoQiInfoOnOROff(long userRoleId){
		if(publicRoleStateExportService.isPublicOnline(userRoleId)){
			ZuoQiInfo zuoqi = zuoQiService.getZuoQiInfo(userRoleId);
			
			return zuoqi;
		}else{
			ZuoQiInfo zuoqi = zuoQiService.getZuoQiDB(userRoleId);
			if(zuoqi == null){
				return null;
			}
			return zuoqi;
		}
	}
	
	public void onlineHandle(Long userRoleId){
		zuoQiService.onlineHandle(userRoleId);
	}
	
    public void offlineHandle(Long userRoleId){
        zuoQiService.offlineHandle(userRoleId);
    }
	
//	public int getZuoQiShowId(long userRoleId) {
//		ZuoQiInfo zuoqiInfo = zuoQiService.getZuoQiInfo(userRoleId);
//		if(zuoqiInfo == null){
//			return   -1;
//		}
//		return zuoqiInfo.getIsGetOn().intValue() == 1 ? zuoqiInfo.getShowId() : -1;
//	}
	
	public int getSkillMaxCount(Long userRoleId){
		return zuoQiService.getSkillMaxCount(userRoleId);
	}
	
	public void noticeAttrChange(Long userRoleId){
		zuoQiService.notifyStageZuoqiChange(userRoleId);
	}
	
	public Object[] zuoqiUpdateShowByHuanhua(Long userRoleId,Integer showId){
		return zuoQiService.zuoqiUpdateShow(userRoleId, showId, false);
	}
}
