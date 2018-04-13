package com.junyou.bus.fuben.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.fuben.entity.Fuben;
import com.junyou.bus.fuben.entity.WuxingFuben;
import com.junyou.bus.fuben.entity.WuxingShilianFuben;
import com.junyou.bus.fuben.entity.WuxingSkillFuben;
import com.junyou.bus.fuben.entity.XinmoDouchangFuben;
import com.junyou.bus.fuben.entity.XinmoFuben;
import com.junyou.bus.fuben.entity.XinmoShenyuanFuben;
import com.junyou.bus.fuben.service.FubenService;
import com.junyou.bus.fuben.service.PataService;
import com.junyou.bus.fuben.service.WuxingFubenService;
import com.junyou.bus.fuben.service.WuxingShilianFubenService;
import com.junyou.bus.fuben.service.WuxingSkillFubenService;
import com.junyou.bus.fuben.service.XinmoDouchangFubenService;
import com.junyou.bus.fuben.service.XinmoFubenService;
import com.junyou.bus.fuben.service.XinmoShenyuanFubenService;

@Service
public class FubenExportService {
	@Autowired
	private FubenService fubenService;
	@Autowired
	private PataService pataService;
	@Autowired
	private WuxingFubenService wxFubenService;
	@Autowired
	private WuxingSkillFubenService wxSkillFubenService;
	@Autowired
	private WuxingShilianFubenService wxShilianFubenService;
    @Autowired
    private XinmoFubenService xinmoFubenService;
    @Autowired
    private XinmoShenyuanFubenService xinmoShenyuanFubenService;
    @Autowired
    private XinmoDouchangFubenService xinmoDouchangubenService;
    
	/**
	 * 初始化
	 * @param userRoleId
	 * @return
	 */
	public List<Fuben> initFuben(Long userRoleId){
		return fubenService.initFuben(userRoleId);
	}
	/**
	 * 上线业务
	 * @param userRoleId
	 */
	public void onlineHandle(Long userRoleId){
		fubenService.onlineHandle(userRoleId);
		xinmoFubenService.onlineHandle(userRoleId);
	    xinmoShenyuanFubenService.onlineHandle(userRoleId);
	}
	/**
	 * 下线业务
	 * @param userRoleId
	 */
	public void offlineHandle(Long userRoleId){
		fubenService.offlineHandle(userRoleId);
		wxFubenService.offlineHandle(userRoleId);
		xinmoFubenService.cancelCutXmFuHuaSchedule(userRoleId);
		xinmoShenyuanFubenService.cancelCutXmShenyuanFubenColingSchedule(userRoleId);
		xinmoDouchangubenService.offlineHandle(userRoleId);
	}
	/**
	 * 计算日常副本资源情况
	 */
	public void calDayFubenResource(Map<String,Map<String,Integer>> map,Long userRoleId){
		fubenService.calDayFubenResource(map, userRoleId);
	}
	/**
	 * 计算守护副本资源情况
	 */
	public void calShouHuFubenResource(Map<String,Map<String,Integer>> map,Long userRoleId){
		fubenService.calShouHuFubenResource(map, userRoleId);
	}
	
	/**
	 * 获取守护副本最高关卡输
	 * @param userRoleId
	 * @return
	 */
	public int getShouhuFubenMax(Long userRoleId){
		return fubenService.getShouhuFubenMax(userRoleId);
	}
	public int getPataMaxCeng(Long userRoleId){
		return pataService.getPataMaxCeng(userRoleId);
	}
	
	/**
	 * 初始化五行副本 
	 * @param userRoleId
	 */
	public List<WuxingFuben> initWxFuben(Long userRoleId){
	    return wxFubenService.initWxFubenData(userRoleId);
	}
	
    /**
     * @Description 获取玩家五行技能副本buff加成和减伤值
     * @param userRoleId
     * @return
     */
    public int[] getRoleWxSkillFubenBuff(Long userRoleId) {
        WuxingSkillFuben wxSkillFuben = wxSkillFubenService.getWxSkillFuben(userRoleId);
        return wxSkillFuben == null ? null : new int[] { wxSkillFuben.getAddBuff(), wxSkillFuben.getSubBuff() };
    }
 
    /**
     * 初始化五行技能副本
     * @param userRoleId
     * @return
     */
    public List<WuxingSkillFuben> initWxSkillFuben(Long userRoleId) {
        return wxSkillFubenService.initWxSkillFubenData(userRoleId);
    }
    
    /**
     * 初始化五行试炼副本 
     * @param userRoleId
     */
    public List<WuxingShilianFuben> initWxShilianFuben(Long userRoleId){
        return wxShilianFubenService.initWxShilianFubenData(userRoleId);
    }

    /**
     * 初始化心魔副本数据到缓存
     * @param userRoleId
     * @return
     */
    public List<XinmoFuben> initXinmoFuben(Long userRoleId) {
        return xinmoFubenService.initXinmoFubenData(userRoleId);
    }
	
    /**
     * 初始化心魔深渊副本数据到缓存 
     * @param userRoleId
     * @return
     */
    public List<XinmoShenyuanFuben> initXmShenyuanFuben(Long userRoleId){
        return xinmoShenyuanFubenService.initXmShenyuanFubenCacheData(userRoleId);
    }
    
    /**
     * 初始化心魔斗场副本数据到缓存 
     * @param userRoleId
     * @return
     */
    public List<XinmoDouchangFuben> initXmDouchangFuben(Long userRoleId){
        return xinmoDouchangubenService.initXmDouchangFubenCacheData(userRoleId);
    }
    
}
