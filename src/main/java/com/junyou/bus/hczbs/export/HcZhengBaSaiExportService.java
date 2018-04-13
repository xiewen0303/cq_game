package com.junyou.bus.hczbs.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.hczbs.entity.Zhengbasai;
import com.junyou.bus.hczbs.entity.ZhengbasaiDayReward;
import com.junyou.bus.hczbs.service.HcZhengBaSaiService;

@Service
public class HcZhengBaSaiExportService {
	@Autowired
	private HcZhengBaSaiService hcZhengBaSaiService;
	
	/**
	 * 初始化
	 * @return
	 */
	public List<Zhengbasai> initZhengbasai() {
		return hcZhengBaSaiService.initZhengbasai();
	}
	/**
	 * 初始化ZhengbasaiDayReward
	 * @return
	 */
	public List<ZhengbasaiDayReward> initZhengbasaiDayReward() {
		return hcZhengBaSaiService.initZhengbasaiDayReward();
	}
 
	/**
	 *玩家一登陆查询  检查皇城门主buff
	 * @param userRoleId
	 * @return
	 */
	public Map<String, Long> getGuildXunZhangAttr(Long userRoleId){
		return hcZhengBaSaiService.getGuildXunZhangAttr(userRoleId);
	}
	/**
	 * 玩家登陆查询是否显示套装
	 * @param userRoleId
	 * @return
	 */
	public boolean showCloth(Long userRoleId){
		return hcZhengBaSaiService.showCloth(userRoleId);
	}
	/**
	 * 门主勋章
	 * @param userRoleId
	 */
	public void onlineHandle(Long userRoleId){
		hcZhengBaSaiService.onlineHandle(userRoleId);
	}
	/**
	 *  检查玩家是不是皇城争霸赛的胜利门主
	 * @param userRoleId
	 */
	public Long getHcZbsWinerGuildId(){
		return hcZhengBaSaiService.getHcZbsWinerGuildId();
	}
	
	/**
	 * 帮派解散门主属性去掉  废弃
	 */
	@Deprecated
	public void guildDissolve(long guildId,Long userRoleId){
		hcZhengBaSaiService.guildDissolve(guildId,userRoleId);
	}
	/**
	 * 获取Zhengbasai
	 */
	public Zhengbasai loadZhengbasai() {

		return hcZhengBaSaiService.loadZhengbasai();
	}
	
	public void createZhengbasai(long guildId){
		hcZhengBaSaiService.createZhengbasai(guildId);
	}
	public void updateZhengbasai(long guildId){
		hcZhengBaSaiService.updateZhengbasai(guildId);
	}
	
	public int getHcZbsRewardState(Long userRoleId){
		return hcZhengBaSaiService.hcZbsHasReward(userRoleId)?1:0; 
	}

	public void removeGuildXunZhang(List<Long> leaderIds){
		  hcZhengBaSaiService.removeGuildXunZhang(leaderIds); 
	}
	public void addGuildXunZhang(){
		  hcZhengBaSaiService.addGuildXunZhang(); 
	}
	
	
	
	 
}
