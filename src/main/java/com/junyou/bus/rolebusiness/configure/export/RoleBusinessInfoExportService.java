package com.junyou.bus.rolebusiness.configure.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.rolebusiness.entity.RoleBusinessInfo;
import com.junyou.bus.rolebusiness.service.RoleBusinessInfoService;
import com.junyou.bus.rolebusiness.vo.FightingRankVo;
import com.junyou.public_.rank.export.IFightingRankExportService;

/**
 * 角色业务数据 Export
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-1-12 下午4:56:23 
 */
@Component
public class RoleBusinessInfoExportService implements IFightingRankExportService<FightingRankVo>{
	@Autowired
	private RoleBusinessInfoService roleBusinessInfoService;
	
	/**
	 * 初始化玩家业务数据访问库
	 * @param userRoleId
	 * @return
	 */
	public RoleBusinessInfo initRoleBusinessInfo(Long userRoleId){
		return roleBusinessInfoService.getRoleBusinessInfoForDB(userRoleId);
	}
	/**
	 * 创建玩家业务数据
	 * @param userRoleId
	 */
	public void createRoleBusinessInfo(Long userRoleId){
		roleBusinessInfoService.createRoleBusinessInfo(userRoleId);
	}
	
	/**
	 * 获取玩家业务数据
	 * @param userRoleId	
	 * @return
	 */
	public RoleBusinessInfoWrapper getRoleBusinessInfoWrapper(Long userRoleId){
		return new RoleBusinessInfoWrapper(roleBusinessInfoService.getRoleBusinessInfo(userRoleId));
	}
	
	/**
	 * 获取玩家业务数据访问库
	 * @param userRoleId
	 * @return
	 */
	public RoleBusinessInfoWrapper getRoleBusinessInfoForDB(Long userRoleId){
		return new RoleBusinessInfoWrapper(roleBusinessInfoService.getRoleBusinessInfoForDB(userRoleId));
	}
	
	/**
	 * 上线业务
	 * @param userRoleId
	 */
	public void onlineHandle(Long userRoleId){
		roleBusinessInfoService.onlineHandle(userRoleId);
	}
	
	/**
	 * 下线业务
	 * @param userRoleId
	 */
	public void offlineHandle(Long userRoleId){
		roleBusinessInfoService.offlineHandle(userRoleId);
	}
	
	/**
	 * 获取战斗力排行数据
	 * @return
	 */
	public List<Object> getZhanLiRanks(){
		return roleBusinessInfoService.getZhanLiRanks();
	}
	
	/**
	 * 保存角色战斗力
	 * @param userRoleId
	 * @param fighterValue
	 */
	public void roleSaveFighter(Long userRoleId,Long fighterValue){
		roleBusinessInfoService.roleSaveFighter(userRoleId, fighterValue);
	}
	
	/**
	 * 真气是否足够
	 * @param userRoleId
	 * @param value
	 */
	public boolean isEnoughZhenqi(long userRoleId,long value){
		return roleBusinessInfoService.isEnoughZhenqi(userRoleId, value);
	}
	/**
	 * 消耗真气
	 * @param userRoleId
	 * @param value
	 * @return
	 */
	public boolean costZhenqi(long userRoleId, long value){
		return roleBusinessInfoService.costZhenqi(userRoleId, value);
	}
	/**
	 * 增加真气
	 * @param userRoleId
	 * @param value
	 * @return
	 */
	public void addZhenqi(long userRoleId, long value){
		roleBusinessInfoService.addZhenqi(userRoleId, value);
	}
	/**
	 * 熔炼值是否足够
	 * @param userRoleId
	 * @param value
	 */
	public boolean isEnoughRongLianVal(long userRoleId,int value){
		return roleBusinessInfoService.isEnoughRongLianVal(userRoleId, value);
	}
	/**
	 * 消耗熔炼值
	 * @param userRoleId
	 * @param value
	 * @return
	 */
	public boolean costRongLianVal(long userRoleId, int value){
		return roleBusinessInfoService.costRongLianVal(userRoleId, value);
	}
	/**
	 * 增加熔炼值
	 * @param userRoleId
	 * @param value
	 * @return
	 */
	public int addRongLianVal(long userRoleId, int value){
		return roleBusinessInfoService.addRongLianVal(userRoleId, value);
	}
	/**
	 * 玄铁值是否足够
	 * @param userRoleId
	 * @param value
	 */
	public boolean isEnoughXuanTieVal(long userRoleId,int value){
		return roleBusinessInfoService.isEnoughXuanTieVal(userRoleId, value);
	}
	/**
	 * 消耗玄铁值
	 * @param userRoleId
	 * @param value
	 * @return
	 */
	public boolean costXuanTieVal(long userRoleId, int value){
		return roleBusinessInfoService.costXuanTieVal(userRoleId, value);
	}
	/**
	 * 增加玄铁值
	 * @param userRoleId
	 * @param value
	 * @return
	 */
	public void addXuanTieVal(long userRoleId, int value){
		roleBusinessInfoService.addXuanTieVal(userRoleId, value);
	}
	
	/**
	 * 荣誉是否足够
	 * @param userRoleId
	 * @param value
	 */
	public boolean isEnoughRongyu(long userRoleId,int value){
		return roleBusinessInfoService.isEnoughRongyu(userRoleId, value);
	}
	/**
	 * 消耗荣誉
	 * @param userRoleId
	 * @param value
	 * @return
	 */
	public boolean costRongyu(long userRoleId, int value){
		return roleBusinessInfoService.costRongyu(userRoleId, value);
	}
	/**
	 * 增加荣誉
	 * @param userRoleId
	 * @param value
	 * @return
	 */
	public void addRongyu(long userRoleId, int value){
		roleBusinessInfoService.addRongyu(userRoleId, value);
	}
	/**
	 * 增加跳闪值
	 * @param userRoleId
	 * @param value
	 */
	public void addTiaoShan(long userRoleId,int value){
		roleBusinessInfoService.addTiaoShan(userRoleId,value);
	}
	
	/**
	 * 消耗跳闪值
	 * @param userRoleId
	 * @param value
	 */
	public void costTiaoShan(long userRoleId,int value){
		roleBusinessInfoService.costTiaoShan(userRoleId,value);
	}
	
	/**
	 * 更新吆喝时间
	 * @param userRoleId
	 * @param systemMillTime
	 */
	public void updateYHLastTime(long userRoleId,long systemMillTime) {
		roleBusinessInfoService.updateYHLastTime(userRoleId,systemMillTime);
	}
	
	
	@Override
	public List<FightingRankVo> getFightingRankVo(int limit) {
	
		return 	roleBusinessInfoService.getFightingRankVo(limit);
	}
	/**
	 * 获取角色战斗力[在线拉缓存，不在线的拉库]
	 * @param userRoleId
	 * @return
	 */
	public long getRoleBusOutFromDb4Catche(long userRoleId){
		
		return roleBusinessInfoService.getRoleBusOutFromDb4Catche(userRoleId);
	}
	
	public boolean clearPkValue(Long userRoleId,int value,boolean notice){
		return roleBusinessInfoService.clearPkValue(userRoleId, value, notice);
	}
	/**
	 * 修改gm类型持续时间
	 * @param userRoleId
	 * @param userTypeTime
	 * @param isOnline
	 */
	public void changeKeepTime(Long userRoleId,Long userTypeTime,boolean isOnline){
		roleBusinessInfoService.changeKeepTime(userRoleId, userTypeTime, isOnline);
	}
	/**
	 * 检测gm类型时间是否已到期
	 * @param userRoleId
	 * @return
	 */
	public boolean checkKeepTime(Long userRoleId){
		return roleBusinessInfoService.checkKeepTime(userRoleId);
	}
	/**
	 * 增加修为
	 * @param userRoleId
	 * @param value
	 */
	public void addXiuwei(long userRoleId,long value){
		roleBusinessInfoService.addXiuwei(userRoleId, value);
	}
	
	/**
	 * 修为是否足够
	 * @param userRoleId
	 * @param value
	 */
	public boolean isEnoughXiuwei(long userRoleId,int value){
		return roleBusinessInfoService.isEnoughXiuwei(userRoleId, value);
	}
	/**
	 * 消耗修为
	 * @param userRoleId
	 * @param value
	 * @return
	 */
	public boolean costXiuwei(long userRoleId, long value){
		return roleBusinessInfoService.costXiuwei(userRoleId, value);
	}
	
	/**
	 * 查询角色在全服的战力排名
	 * @param userId
	 * @param serverId
	 * @return
	 */
	public long getUserServerRank(String userId,String serverId){
		return roleBusinessInfoService.getUserServerRank(userId, serverId);
	}
}
