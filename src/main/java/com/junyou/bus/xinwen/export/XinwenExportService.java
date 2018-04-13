package com.junyou.bus.xinwen.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.xinwen.entity.RoleTangbaoXinwen;
import com.junyou.bus.xinwen.service.XinwenService;
import com.junyou.bus.xinwen.vo.XinwenRankVo;
import com.junyou.public_.rank.export.IXinwenRankExportService;
@Service
public class XinwenExportService implements IXinwenRankExportService<XinwenRankVo>{

	@Autowired
	private XinwenService xinwenService;
	
	public RoleTangbaoXinwen initRoleYaoshenMowen(Long userRoleId) {
		return xinwenService.initRoleTangbaoXinwen(userRoleId);
	}
	/**
	 * 获取糖宝心纹的阶数
	 */
	public int getTangbaoXinwenJie(Long userRoleId){
		return xinwenService.getXinwenJie(userRoleId);
	}
	/**
	 * 玩家登陆获取糖宝心纹属性
	 * @param userRoleId
	 * @return
	 */
	public Map<String, Long> getXinwenAttributeAfterLogin(Long userRoleId) {
		return xinwenService.getXinwenAttributeAfterLogin(userRoleId);
	}
	/**
	 * 获取心纹属性
	 * @param jie
	 * @param ceng
	 * @param qndNum
	 * @param czdNum
	 * @return
	 */
	public Map<String, Long> getXinwenAttribute(Integer jie, Integer ceng, Integer qndNum, Integer czdNum) {
		return xinwenService.getXinwenAttribute(jie, ceng, qndNum, czdNum);
	}
	/**
	 *玩家升级到一定等级自动激活心纹
	 */
	public void roleUpLevel(Long userRoleId,int level){
		
		xinwenService.checkLevelAndActiveXinwen(userRoleId,level);
	}
	@Override
	public List<XinwenRankVo> getXinwenRankVo(int limit) {
		
		return xinwenService.getXinwenRankVo(limit);
	}
	
	public Object[] sjByItem(Long userRoleId,Integer minLevel,Integer maxLevel,Integer addCeng){
		return xinwenService.sjByItem(userRoleId, minLevel, maxLevel, addCeng);
	}
}
