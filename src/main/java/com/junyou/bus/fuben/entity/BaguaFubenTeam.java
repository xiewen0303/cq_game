package com.junyou.bus.fuben.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.bus.bagua.configure.export.BaguaZhenMenConfig;
import com.junyou.bus.bagua.configure.export.BaguaZhenMenExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.datetime.GameSystemTime;
/**
 * 多人副本队伍表
 * @author jy
 *
 */
public class BaguaFubenTeam {
	// 队伍ID
	private int teamId;
	// 领导人
	private BaguaFubenTeamMember leader; 
	// 队伍名称
	private String name;
	// 成员列表map
	private Map<Long,BaguaFubenTeamMember> members = new HashMap<Long, BaguaFubenTeamMember>();
	// 成员列表list
	private List<Long> roleIdList = new ArrayList<>();
	private Object[] teamInfoArr;
	// 所属副本ID
	private int belongFubenId;
	// 队伍密码
	// private int pwd; 策划后续决定是否需要开启
	// 队伍战力
	private Long strength;
	// 是否自动开始
	private boolean isAuto;
	// 是否进入倒计时
	private boolean isDjs;
	//存到redis的数据
	private Map<String,String> saveInfo;
	
	private Map<Integer,Map<Integer,BaguaZhenmenInfo>> zhenmenInfo = new HashMap<Integer,Map<Integer,BaguaZhenmenInfo>>();
	
	private boolean isIng;
	
	private long startTime;
	
	public void setIng(boolean isIng) {
		this.isIng = isIng;
	}

	public BaguaFubenTeam(BaguaFubenTeamMember leader) {
		this.leader = leader;
		this.name = leader.getMemberName();
		members.put(leader.getRoleId(), leader);
		roleIdList.add(leader.getRoleId());
		startTime = GameSystemTime.getSystemMillTime();
	}
		
	public String getName() {
		return name;
	}

	public long getStartTime() {
		return startTime;
	}

	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	
	public BaguaFubenTeamMember getLeader() {
		return leader;
	}
	/**
	 *  设置队长
	 * @param leader
	 */
	private void setLeader(BaguaFubenTeamMember leader){
		this.leader = leader;
		this.name = leader.getMemberName();
	}
		
	/**
	 * 获取队伍角色信息
	 * @return
	 */
	public Object[] getTeamInfoArr(){
		if(teamInfoArr == null){
			teamInfoArr = new Object[members.size()];
			int i = 0;
			for (BaguaFubenTeamMember member : members.values()) {
				teamInfoArr[i++] = member.getMemberVo();
			}
		}
		return teamInfoArr;
	}
			
	public List<BaguaFubenTeamMember> getMembers() {
		return new ArrayList<>(members.values());
	}
	public BaguaFubenTeamMember getMember(Long userRoleId) {
		return members.get(userRoleId);
	}
	
	/**
	 * 新增成员
	 * @param member
	 * @return
	 */
	public synchronized void addMembers(BaguaFubenTeamMember member,int maxTeamLimit) {
		if(members.size() >= maxTeamLimit){
			return;
		}
		member.setTeam(this);
		members.put(member.getRoleId(), member);
		roleIdList.add(member.getRoleId());
		teamInfoArr = null;
	}
	
	/**
	 * 移除成员
	 * @param userRoleId
	 */
	public synchronized BaguaFubenTeamMember removeMember(Long userRoleId){
		if(!members.containsKey(userRoleId)){
			return null;
		}
		BaguaFubenTeamMember member = members.remove(userRoleId);
		BaguaFubenTeam mft = member.getTeam();
		roleIdList.remove(userRoleId);
		member.setTeam(null);
		teamInfoArr = null;
		KuafuMsgSender.send2OneKuafuSource(member.getServerId(), InnerCmdType.INNER_KF_TO_ONE_CLIENT, new Object[]{ClientCmdType.BAGUA_TEAM_LEAVE, AppErrorCode.OK,userRoleId});
		BaguaFubenTeamManage.removeTeam(userRoleId,true);
		if(roleIdList.isEmpty()){
			BaguaFubenTeamManage.getTeamMap().remove(mft.getTeamId());
			List<BaguaFubenTeam> teams = BaguaFubenTeamManage.getTeamFubenMap().get(mft.getBelongFubenId());
			teams.remove(mft);
		}else{			
			// 发送多人
			Object[] obj = new Object[]{teamId,member.getRoleId()};
			for(BaguaFubenTeamMember teamMember : members.values()){				
				Object[] msg = new Object[]{ClientCmdType.BAGUA_FUBEN_LEAVE_SENDER, obj,teamMember.getRoleId()};
				KuafuMsgSender.send2OneKuafuSource(teamMember.getServerId(), InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
			}
		}
		if(userRoleId.equals(mft.getLeader().getRoleId())){
			//队长退队
			if(!roleIdList.isEmpty()){
				//如果队伍没解散，变更新队长
				BaguaFubenTeamMember leader = members.values().iterator().next();
				setLeader(leader);
				// 发送多人
				Object[] obj = new Object[]{teamId,leader.getRoleId()};
				for(BaguaFubenTeamMember teamMember : members.values()){			
					Object[] msg = new Object[]{ClientCmdType.BAGUA_FUBEN_TEAM_CHANGE, obj,teamMember.getRoleId()};
					KuafuMsgSender.send2OneKuafuSource(teamMember.getServerId(), InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
				}
			}
		}
		return member;
	}
	
	/**
	 * 是否是队伍中成员
	 * @param userRoleId
	 * @return
	 */
	public boolean isTeamMember(Long userRoleId){
		return members.containsKey(userRoleId);
	}
	
	/**
	 * 获取成员id数组
	 * @return
	 */
	public Long[] getRoleIdList() {
		return roleIdList.toArray(new Long[roleIdList.size()]);
	}

	public int getBelongFubenId() {
		return belongFubenId;
	}

	public void setBelongFubenId(int belongFubenId) {
		this.belongFubenId = belongFubenId;
	}

//	public int getPwd() {
//		return pwd;
//	}
//
//	public void setPwd(int pwd) {
//		this.pwd = pwd;
//	}

	public Long getStrength() {
		return strength;
	}

	public void setStrength(Long strength) {
		this.strength = strength;
	}

	public boolean isAuto() {
		return isAuto;
	}

	public void setAuto(boolean isAuto) {
		this.isAuto = isAuto;
	}

	public boolean isDjs() {
		return isDjs;
	}

	public void setDjs(boolean isDjs) {
		this.isDjs = isDjs;
	}

	public Map<Integer, Map<Integer, BaguaZhenmenInfo>> getZhenmenInfo() {
		return zhenmenInfo;
	}

	public void setZhenmenInfo(
			Map<Integer, Map<Integer, BaguaZhenmenInfo>> zhenmenInfo) {
		this.zhenmenInfo = zhenmenInfo;
//		printZhenmenInfo();
	}
	
	public boolean isZhenMenOpen(int ceng,int position){
		return zhenmenInfo.get(ceng).get(position).isOpen();
	}
	
	public Integer getZhenmenId(int ceng,int position){
		return zhenmenInfo.get(ceng).get(position).getZhenmenId();
	}
	
	public void openZhenmen(int ceng,int position){
		zhenmenInfo.get(ceng).get(position).setOpen(true);
	}
	
	public Object[] getOpenDoorInfo(int ceng,BaguaZhenMenExportService baguaZhenMenExportService){
		if(ceng==8){
			return new Object[]{};
		}
		Map<Integer,BaguaZhenmenInfo> cengZhenmenInfo = zhenmenInfo.get(ceng);
		List<Object[]> objs = new ArrayList<>();
		for(Integer key:cengZhenmenInfo.keySet()){
			BaguaZhenmenInfo e = cengZhenmenInfo.get(key);
			if(!e.isOpen()){
				continue;
			}
			int zhenmenId = e.getZhenmenId();
			BaguaZhenMenConfig baguaZhenMenConfig = baguaZhenMenExportService.loadByOrder(zhenmenId);
			objs.add(new Object[]{e.getZhenmenId(),key});
		}
		return objs.toArray();
	}

	public Map<String, String> getSaveInfo() {
		if(saveInfo == null){
			saveInfo = new HashMap<>();
			saveInfo.put(GameConstants.REDIS_BAGUA_FUBEN_TEAMID_KEY, teamId+"");
			saveInfo.put(GameConstants.REDIS_BAGUA_FUBEN_TEAMLEADER_KEY, name);
			saveInfo.put(GameConstants.REDIS_BAGUA_FUBEN_FUBENID_KEY, belongFubenId+"");
			saveInfo.put(GameConstants.REDIS_BAGUA_FUBEN_ZPLUS_KEY, strength.toString());
			saveInfo.put(GameConstants.REDIS_BAGUA_FUBEN_COUNT_KEY, roleIdList.size()+"");
			saveInfo.put(GameConstants.REDIS_BAGUA_FUBEN_SERVERID_KEY, ChuanQiConfigUtil.getServerId());
			saveInfo.put(GameConstants.REDIS_BAGUA_FUBEN_ING, "0");
		}else{
			saveInfo.put(GameConstants.REDIS_BAGUA_FUBEN_TEAMLEADER_KEY, name);
			saveInfo.put(GameConstants.REDIS_BAGUA_FUBEN_ZPLUS_KEY, strength.toString());
			saveInfo.put(GameConstants.REDIS_BAGUA_FUBEN_COUNT_KEY, roleIdList.size()+"");
			saveInfo.put(GameConstants.REDIS_BAGUA_FUBEN_ING, (isDjs || isIng)?"1":"0");
		}
		return saveInfo;
	}
	
	
	public void printZhenmenInfo(){
		for(Integer ceng:zhenmenInfo.keySet()){
			Map<Integer,BaguaZhenmenInfo> cengMap = zhenmenInfo.get(ceng);
			for(Integer index:cengMap.keySet()){
				BaguaZhenmenInfo info = cengMap.get(index);
				if(info.getZhenmenId()==1){
					System.out.println("ceng:"+ceng + "index:" +index);
				}
			}
		}
	}
	
}
