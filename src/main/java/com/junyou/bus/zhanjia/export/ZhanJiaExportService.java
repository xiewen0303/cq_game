package com.junyou.bus.zhanjia.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.zhanjia.configure.export.ZhanJiaJiChuConfig;
import com.junyou.bus.zhanjia.configure.export.ZhanJiaJiChuConfigExportService;
import com.junyou.bus.zhanjia.entity.ZhanJiaInfo;
import com.junyou.bus.zhanjia.manage.ZhanJia;
import com.junyou.bus.zhanjia.service.ZhanJiaService;
import com.junyou.bus.zhanjia.vo.ZhanJiaRankVo;
import com.junyou.public_.rank.export.IZhanJiaRankExportService;
import com.junyou.public_.share.export.PublicRoleStateExportService;

@Service
public class ZhanJiaExportService implements IZhanJiaRankExportService<ZhanJiaRankVo>{
	
	@Autowired
	private ZhanJiaService xianJianService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	
	/**
	 * 获得仙剑的属性（除移动速度）
	 */
	public Map<String,Long> getXianJianAttrs(Long userRoleId,ZhanJia xianjian){
		return xianJianService.getXianJianAttrs(userRoleId,xianjian);
	}
	
	/**
	 * 获得仙剑的属性（除移动速度）
	 */
	public Map<String,Long> getXianJianAttr(Long userRoleId){
		return xianJianService.getXianJianAttr(userRoleId);
	}
	
	
	  
	public ZhanJiaInfo getXianJianInfo(long userRoleId){
		return xianJianService.getXianJianInfo(userRoleId);
	}

	/**
	 * 获取翅膀等级
	 * @param userRoleId
	 * @return
	 */
	public Integer getXianJianLevel(long userRoleId){
		ZhanJiaInfo xianJianInfo = xianJianService.getXianJianInfo(userRoleId);
		if(xianJianInfo == null){
			return -1;
		}
		return xianJianInfo.getXianjianLevel();
		 
	}
	
	/**
	 * 获取翅膀等级
	 * @param userRoleId
	 * @return
	 */
	public Integer getXianJianLevelOther(long userRoleId){
		ZhanJiaInfo xianJianInfo = xianJianService.getXianJianInfo(userRoleId);
		if(xianJianInfo == null){
			return -1;
		}
		
		ZhanJiaJiChuConfig config = zhanJiaJiChuConfigExportService.loadById(xianJianInfo.getXianjianLevel());
		return config.getLevel();
	}
	@Autowired
	private ZhanJiaJiChuConfigExportService zhanJiaJiChuConfigExportService;

	public ZhanJiaInfo initXianJian(Long userRoleId) {
		return xianJianService.initXianJian(userRoleId);
	}
 
	public ZhanJiaInfo getXianJianInfoOnOROff(long userRoleId){
		if(publicRoleStateExportService.isPublicOnline(userRoleId)){
			ZhanJiaInfo xianjian = xianJianService.getXianJianInfo(userRoleId);
			
			return xianjian;
		}else{
			ZhanJiaInfo xianjian = xianJianService.getXianJianInfoDB(userRoleId);
			if(xianjian == null){
				return null;
			}
			return xianjian;
		}
	}
	
	
	@Override
	public List<ZhanJiaRankVo> getXianJianRankVo(int limit) {
		return xianJianService.getXianJianRankVo(limit);
	}
	public void onlineHandle(Long userRoleId){
		xianJianService.onlineHandle(userRoleId);
	}
	public Object[] useXianJianQND(Long userRoleId,int count) {
		  return xianJianService.xjUseQND(userRoleId,count); 
	}

	public Object[] addXianJianCZD(Long userRoleId, int consumeCount) {
		 return xianJianService.xjUseCZD(userRoleId,consumeCount);
	}
	
	public Object[] sjByItem(Long userRoleId, Integer minLevel, Integer maxLevel, Integer zfz){
		return  xianJianService.sjByItem(userRoleId, minLevel, maxLevel, zfz);
	}
	
	public int getSkillMaxCount(Long userRoleId){
		return xianJianService.getSkillMaxCount(userRoleId);
	}
	public Object[] zhanjiaUpdateShowByHuanhua(Long userRoleId,Integer showId){
		return xianJianService.xianJianUpdateShow(userRoleId, showId, false);
	}
	public void noticeAttrChange(long userRoleId){
		xianJianService.notifyStageXianJianChange(userRoleId);
	}
	/**
	 * 促销奖励直升
	 */
	public Object[] sjByCuxiao(Long userRoleId, int newLevel){
		return  xianJianService.sjByCuxiao(userRoleId, newLevel);
	}
}
