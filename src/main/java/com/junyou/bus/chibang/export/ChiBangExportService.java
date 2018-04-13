package com.junyou.bus.chibang.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.chibang.configure.export.ChiBangJiChuConfig;
import com.junyou.bus.chibang.configure.export.ChiBangJiChuConfigExportService;
import com.junyou.bus.chibang.entity.ChiBangInfo;
import com.junyou.bus.chibang.manage.ChiBang;
import com.junyou.bus.chibang.service.ChiBangService;
import com.junyou.bus.chibang.vo.ChiBangRankVo;
import com.junyou.public_.rank.export.IChiBangRankExportService;
import com.junyou.public_.share.export.PublicRoleStateExportService;

@Service
public class ChiBangExportService implements IChiBangRankExportService<ChiBangRankVo>{
	
	@Autowired
	private ChiBangService chiBangService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	
	/**
	 * 获得翅膀的属性（除移动速度）
	 */
	public Map<String,Long> getChiBangAttrs(Long userRoleId,ChiBang zuoqi){
		return chiBangService.getChiBangAttrs(userRoleId,zuoqi);
	}
	/**
	 * 获得翅膀的属性（除移动速度）
	 */
	public Map<String,Long> getChiBangAttr(Long userRoleId){
		return chiBangService.getChiBangAttr(userRoleId);
	}
	/**
	 * 促销  直升 翅膀
	 */
	public Object[] sjByCuxiao(Long userRoleId,int newlevel){
		return chiBangService.sjByCuxiao(userRoleId,newlevel);
	}
	
	
	
//	/**
//	 * 获得坐骑移动速度属性
//	 */
//	public Map<String,Integer> getZuoQiSeedAttr(ZuoQi zuoqi){
//		return zuoQiService.getZuoQiSeedAttr(zuoqi);
//	} 
	
	  
	public ChiBangInfo getChiBangInfo(long userRoleId){
		return chiBangService.getChiBangInfo(userRoleId);
	}

	public Object[] getEquips(Long userRoleId) {
		return chiBangService.getEquips(userRoleId);
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
	public Object[] useChiBangQND(Long userRoleId,int count) {
		  return chiBangService.cbUseQND(userRoleId,count); 
	}

	public Object[] addChiBangCZD(Long userRoleId, int consumeCount) {
		 return chiBangService.cbUseCZD(userRoleId,consumeCount);
	}
	/**
	 * 获取翅膀等级
	 * @param userRoleId
	 * @return
	 */
	public Integer getChibangLevel(Long userRoleId){
		ChiBangInfo chiBangInfo = chiBangService.getChiBangInfo(userRoleId);
		if(chiBangInfo == null){
			return -1;
		}
		return chiBangInfo.getChibangLevel();
	}
	
	/**
	 * 获取翅膀等级
	 * @param userRoleId
	 * @return
	 */
	public int getChibangLevelOther(long userRoleId){
		ChiBangInfo chiBangInfo = chiBangService.getChiBangInfo(userRoleId);
		if(chiBangInfo == null){
			return -1;
		}
		
		ChiBangJiChuConfig config = chiBangJiChuConfigExportService.loadById(chiBangInfo.getChibangLevel());
		return config.getLevel();
	}
	@Autowired
	private ChiBangJiChuConfigExportService chiBangJiChuConfigExportService;

	public ChiBangInfo initChiBang(Long userRoleId) {
		return chiBangService.initChiBang(userRoleId);
	}
 
	public ChiBangInfo getChiBangInfoOnOROff(long userRoleId){
		if(publicRoleStateExportService.isPublicOnline(userRoleId)){
			ChiBangInfo chibang = chiBangService.getChiBangInfo(userRoleId);
			
			return chibang;
		}else{
			ChiBangInfo chibang = chiBangService.getChiBangInfoDB(userRoleId);
			if(chibang == null){
				return null;
			}
			return chibang;
		}
	}
	
	
	@Override
	public List<ChiBangRankVo> getChiBangRankVo(int limit) {
		return chiBangService.getChiBangRankVo(limit);
	}
	
	public void onlineHandle(Long userRoleId){
		chiBangService.onlineHandle(userRoleId);
	}
	
	public Object[] sjByItem(Long userRoleId,Integer minLevel,Integer maxLevel,Integer bgold){
		return chiBangService.sjByItem(userRoleId, minLevel, maxLevel, bgold);
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
		return chiBangService.getSkillMaxCount(userRoleId);
	}
	
	public Object[] chiBangUpdateShowByHuanhua(Long userRoleId, int showId) {
		return chiBangService.chiBangUpdateShow(userRoleId, showId,false);
	}
	public void noticeAttrChange(long userRoleId){
		chiBangService.notifyStageChiBangChange(userRoleId);
	}
}
