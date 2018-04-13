package com.junyou.bus.yaoshen.service.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.yaoshen.constants.YaoshenConstants;
import com.junyou.bus.yaoshen.entity.RoleYaoshen;
import com.junyou.bus.yaoshen.entity.RoleYaoshenMowen;
import com.junyou.bus.yaoshen.service.YaoshenService;
import com.junyou.bus.yaoshen.vo.YaoShenRankVo;
import com.junyou.public_.rank.export.IYaoShenRankExportService;

@Service
public class YaoshenExportService implements IYaoShenRankExportService<YaoShenRankVo>{
	@Autowired
	private YaoshenService yaoshenService;

	public RoleYaoshen initRoleYaoshen(Long userRoleId) {
		return yaoshenService.initRoleYaoshen(userRoleId);
	}
	public RoleYaoshenMowen initRoleYaoshenMowen(Long userRoleId) {
		return yaoshenService.initRoleYaoshenMowen(userRoleId);
	}

	public Boolean isRoleInYaoshen(Long userRoleId) {
		RoleYaoshen ret = yaoshenService.getRoleYaoshen(userRoleId);
		if (ret == null) {
			return false;
		} else {
			return ret.getIsYaoshenShow() == YaoshenConstants.YAOSHEN_SHOW_YES;
		}
	}

	public Object[] useYaoshenQND(Long userRoleId, int count) {
		return yaoshenService.useQND(userRoleId, count);
	}

	public Object[] addYaoshenCZD(Long userRoleId, int consumeCount) {
		return yaoshenService.useCZD(userRoleId, consumeCount);
	}

	public void onlineHandle(Long userRoleId) {
		yaoshenService.onlineHandle(userRoleId);
	}
	
	public Map<String, Long> getYaoshenAttribute(Long userRoleId,boolean show) {
		return yaoshenService.getYaoshenAttribute(userRoleId,show);
	}
	public Map<String, Long> getYaoshenAttribute(Integer jie,Integer ceng,Integer qndNum,Integer czdNum) {
		return yaoshenService.getYaoshenAttribute(jie,ceng,qndNum,czdNum);
	}
	public Object[] addYaoshenMowenQND(Long userRoleId, int count) {
		return yaoshenService.useMowenQND(userRoleId, count);
	}

	public Object[] addYaoshenMowenCZD(Long userRoleId, int consumeCount) {
		return yaoshenService.useMowenCZD(userRoleId, consumeCount);
	}
	/**
	 * 未激活的状态传-1，
	 * @param userRoleId
	 * @return
	 */
	public int getYaoshenId(Long userRoleId){
		return yaoshenService.getYaoshenId(userRoleId);
	}
	public RoleYaoshen getRoleYaoshen(Long userRoleId) {
		return yaoshenService.getRoleYaoshen(userRoleId);
	}

	public RoleYaoshenMowen getRoleYaoshenMowen(Long userRoleId) {
		return yaoshenService.getRoleYaoshenMowen(userRoleId);
	}

	
	/**
	 * 玩家登陆获取妖神魔纹属性
	 * @param userRoleId
	 * @return
	 */
	public Map<String, Long> getYaoshenMowenAttributeAfterLogin(Long userRoleId) {
		return yaoshenService.getYaoshenMowenAttributeAfterLogin(userRoleId);
	}
	//妖神魔纹
	public Map<String, Long> getYaoshenMowenAttribute(Integer jie,Integer ceng,Integer qndNum,Integer czdNum) {
		return yaoshenService.getYaoshenMowenAttribute(jie,ceng,qndNum,czdNum);
	}
	/**
	 * 获取魔纹的阶数
	 */
	public int getMowenJie(Long userRoleId){
		
		return yaoshenService.getMowenJie(userRoleId);
	}
	/**
	 *玩家升级到一定等级自动激活魔纹
	 */
	public void roleUpLevel(Long userRoleId,int level){
		
		yaoshenService.checkLevelAndActiveMowen(userRoleId,level);
	}
	@Override
	public List<YaoShenRankVo> getYaoShenRankVo(int limit) {
		return yaoshenService.getYaoShenRankVo(limit);
	}
	
	public Object[] sjByItem(Long userRoleId,Integer minLevel,Integer maxLevel,Integer addCeng){
		return yaoshenService.sjByItem(userRoleId, minLevel, maxLevel, addCeng);
	} 
	public Object[] sjByItemMoWen(Long userRoleId,Integer minLevel,Integer maxLevel,Integer addCeng){
		return yaoshenService.sjByItemMoWen(userRoleId, minLevel, maxLevel, addCeng);
	} 
}
