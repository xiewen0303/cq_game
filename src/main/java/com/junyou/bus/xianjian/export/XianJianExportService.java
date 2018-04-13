package com.junyou.bus.xianjian.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.xianjian.configure.export.XianJianJiChuConfig;
import com.junyou.bus.xianjian.configure.export.XianJianJiChuConfigExportService;
import com.junyou.bus.xianjian.entity.XianJianInfo;
import com.junyou.bus.xianjian.manage.XianJian;
import com.junyou.bus.xianjian.service.XianjianService;
import com.junyou.bus.xianjian.vo.XianJianRankVo;
import com.junyou.public_.rank.export.IXianJianRankExportService;
import com.junyou.public_.share.export.PublicRoleStateExportService;

@Service
public class XianJianExportService implements IXianJianRankExportService<XianJianRankVo> {

	@Autowired
	private XianjianService xianJianService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;

	/**
	 * 获得仙剑的属性（除移动速度）
	 */
	public Map<String, Long> getXianJianAttrs(Long userRoleId, XianJian xianjian) {
		return xianJianService.getXianJianAttrs(userRoleId, xianjian);
	}

	/**
	 * 获得仙剑的属性（除移动速度）
	 */
	public Map<String, Long> getXianJianAttr(Long userRoleId) {
		return xianJianService.getXianJianAttr(userRoleId);
	}

	public XianJianInfo getXianJianInfo(long userRoleId) {
		return xianJianService.getXianJianInfo(userRoleId);
	}

	/**
	 * 获取仙剑等级
	 * @param userRoleId
	 * @return
	 */
	public Integer getXianJianLevel(long userRoleId) {
		XianJianInfo xianJianInfo = xianJianService.getXianJianInfo(userRoleId);
		if (xianJianInfo == null) {
			return -1;
		}
		return xianJianInfo.getXianjianLevel();
	}
	
	/**
	 * 获取仙剑等级
	 * @param userRoleId
	 * @return
	 */
	public int getXianJianLevelOther(long userRoleId) {
		XianJianInfo xianJianInfo = xianJianService.getXianJianInfo(userRoleId);
		if (xianJianInfo == null) {
			return -1;
		}
		
		XianJianJiChuConfig config = xianJianJiChuConfigExportService.loadById(xianJianInfo.getXianjianLevel());
		return config == null?0:config.getLevel();
	}
	
	@Autowired
	private XianJianJiChuConfigExportService xianJianJiChuConfigExportService;

	public XianJianInfo initXianJian(Long userRoleId) {
		return xianJianService.initXianJian(userRoleId);
	}

	public XianJianInfo getXianJianInfoOnOROff(long userRoleId) {
		if (publicRoleStateExportService.isPublicOnline(userRoleId)) {
			XianJianInfo xianjian = xianJianService.getXianJianInfo(userRoleId);

			return xianjian;
		} else {
			XianJianInfo xianjian = xianJianService.getXianJianInfoDB(userRoleId);
			if (xianjian == null) {
				return null;
			}
			return xianjian;
		}
	}

	@Override
	public List<XianJianRankVo> getXianJianRankVo(int limit) {
		return xianJianService.getXianJianRankVo(limit);
	}

	public void onlineHandle(Long userRoleId) {
		xianJianService.onlineHandle(userRoleId);
	}

	public Object[] useXianJianQND(Long userRoleId, int count) {
		return xianJianService.xjUseQND(userRoleId, count);
	}

	public Object[] addXianJianCZD(Long userRoleId, int consumeCount) {
		return xianJianService.xjUseCZD(userRoleId, consumeCount);
	}
	/**
	 * 消耗道具  直接升级  糖宝武器
	 * @param userRoleId
	 * @param minLevel
	 * @param maxLevel
	 * @param zfz
	 * @return
	 */
	public Object[] sjByItem(Long userRoleId, Integer minLevel, Integer maxLevel, Integer zfz) {

		return xianJianService.sjByItem(userRoleId, minLevel, maxLevel, zfz);

	}
	
	public int getSkillMaxCount(Long userRoleId){
		return xianJianService.getSkillMaxCount(userRoleId);
	}
	public Object[] xianjianUpdateShowByHuanhua(Long userRoleId,Integer showId){
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
