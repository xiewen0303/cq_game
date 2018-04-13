package com.junyou.bus.qiling.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.qiling.entity.QiLingInfo;
import com.junyou.bus.qiling.manage.QiLing;
import com.junyou.bus.qiling.service.QiLingService;
import com.junyou.bus.qiling.vo.QiLingRankVo;
import com.junyou.public_.rank.export.IQiLingRankExportService;
import com.junyou.public_.share.export.PublicRoleStateExportService;

@Service
public class QiLingExportService implements IQiLingRankExportService<QiLingRankVo>{
	
	@Autowired
	private QiLingService qiLingService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	
	/**
	 * 获得器灵的属性（除移动速度）
	 */
	public Map<String,Long> getQiLingAttrs(Long userRoleId,QiLing qling){
		return qiLingService.getQiLingAttrs(userRoleId,qling);
	}
	/**
	 * 获得器灵的属性（除移动速度）
	 */
	public Map<String,Long> getQiLingAttr(Long userRoleId){
		return qiLingService.getQiLingAttr(userRoleId);
	}
	
//	/**
//	 * 获得坐骑移动速度属性
//	 */
//	public Map<String,Integer> getZuoQiSeedAttr(ZuoQi zuoqi){
//		return zuoQiService.getZuoQiSeedAttr(zuoqi);
//	} 
	
	  
	public QiLingInfo getQiLingInfo(long userRoleId){
		return qiLingService.getQiLingInfo(userRoleId);
	}

	public Object[] getEquips(Long userRoleId) {
		return qiLingService.getEquips(userRoleId);
	}

//	public Object[] useZuoqiQND(Long userRoleId,int count) {
//		  return zuoQiService.zqUseQND(userRoleId,count); 
//	}
//
//	public Object[] addZuoQiCZD(Long userRoleId, int consumeCount) {
//		 return zuoQiService.zqUseCZD(userRoleId,consumeCount);
//	}
//	/**
//	 * 坐骑换装
//	 * @param userRoleId
//	 * @param guid
//	 * @param targetSlot
//	 * @param containerType
//	 * @return
//	 */
//	public Object[] zuoqiChangeEquip(Long userRoleId, long guid,int targetSlot, int containerType) {
//		return zuoQiService.zuoqiChangeEquip(userRoleId, guid,targetSlot, containerType);
//	}
	public Object[] useQiLingQND(Long userRoleId,int count) {
		  return qiLingService.cbUseQND(userRoleId,count); 
	}

	public Object[] addQiLingCZD(Long userRoleId, int consumeCount) {
		 return qiLingService.cbUseCZD(userRoleId,consumeCount);
	}
	/**
	 * 获取器灵等级
	 * @param userRoleId
	 * @return
	 */
	public Integer getQiLingLevel(long userRoleId){
		QiLingInfo qiLingInfo = qiLingService.getQiLingInfo(userRoleId);
		if(qiLingInfo == null){
			return -1;
		}
		return qiLingInfo.getQilingLevel();
		 
	}

	public QiLingInfo initQiLing(Long userRoleId) {
		return qiLingService.initQiLing(userRoleId);
	}
 
	public QiLingInfo getQiLingInfoOnOROff(long userRoleId){
		if(publicRoleStateExportService.isPublicOnline(userRoleId)){
			QiLingInfo qilingInfo = qiLingService.getQiLingInfo(userRoleId);
			
			return qilingInfo;
		}else{
			QiLingInfo qilingInfo = qiLingService.getQiLingInfoDB(userRoleId);
			if(qilingInfo == null){
				return null;
			}
			return qilingInfo;
		}
	}
	
	
	@Override
	public List<QiLingRankVo> getQiLingRankVo(int limit) {
		return qiLingService.getChiBangRankVo(limit);
	}
	
	public void onlineHandle(Long userRoleId){
		qiLingService.onlineHandle(userRoleId);
	}
	
	public Object[] sjByItem(Long userRoleId,Integer minLevel,Integer maxLevel,Integer bgold){
		return qiLingService.sjByItem(userRoleId, minLevel, maxLevel, bgold);
	}
	
	
//	@Override
//	public List<ZuoqiRankVo> getZuoqiRankVo(int limit) {
//		return zuoQiService.getZuoqiRankVo(limit);
//	}

//	public int getZuoQiShowId(long userRoleId) {
//		ZuoQiInfo zuoqiInfo = zuoQiService.getZuoQiInfo(userRoleId);
//		if(zuoqiInfo == null){
//			return   -1;
//		}
//		return zuoqiInfo.getIsGetOn().intValue() == 1 ? zuoqiInfo.getShowId() : -1;
//	}
	
	public int getSkillMaxCount(Long userRoleId){
		return qiLingService.getSkillMaxCount(userRoleId);
	}
}
