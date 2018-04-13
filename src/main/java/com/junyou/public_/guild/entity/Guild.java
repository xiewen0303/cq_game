package com.junyou.public_.guild.entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.junyou.public_.guild.manager.GuildLogManager;
import com.junyou.public_.guild.manager.GuildMemberManager;
import com.junyou.public_.guild.util.GuildOutPutUtil;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("guild")
public class Guild extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("name")
	private String name;

	@Column("level")
	private Integer level;
	
	@Column("school")
	private Integer school;

	@Column("gold")
	private Long gold;
	
	@Column("item1")
	private Integer item1;
	
	@Column("item2")
	private Integer item2;
	
	@Column("item3")
	private Integer item3;
	
	@Column("item4")
	private Integer item4;
	

	@Column("notice")
	private String notice;

	@Column("create_time")
	private Long createTime;

	@Column("apply_type")
	private Integer applyType;
	
	@EntityField
	private GuildMemberManager guildMemberManager;
	
	@EntityField
	private GuildLogManager guildLogManager;
	
	@EntityField
	private int maxCount;
	
	@EntityField
	private byte[] updateLock = new byte[0];
	
	@EntityField
	private Object[] myGuildVo;
	
	@EntityField
	private Object[] guildListVo;
	
	@EntityField
	/**公会即将销毁*/
	private boolean dissoling;
	@EntityField
	/**是否是皇城争霸赛胜利者*/
	private boolean hcZbsWinner;
	
    @EntityField
    /**是否是跨服云宫之巅胜利者*/
    private boolean kfYunGongWinner;

    public boolean isKfYunGongWinner() {
        return kfYunGongWinner;
    }

    public void setKfYunGongWinner(boolean kfYunGongWinner) {
        this.kfYunGongWinner = kfYunGongWinner;
    }
    
    public boolean isHcZbsWinner() {
		return hcZbsWinner;
	}
	public void setHcZbsWinner(boolean hcZbsWinner) {
		this.hcZbsWinner = hcZbsWinner;
	}
	
	public Long getId(){
		return id;
	}

	public  void setId(Long id){
		this.id = id;
	}

	public String getName(){
		return name;
	}

	public  void setName(String name){
		this.name = name;
		if(myGuildVo != null){
			myGuildVo[1] = name;
		}
		if(guildListVo != null){
			guildListVo[1] = name;
		}
	}

	public Integer getLevel(){
		return level;
	}

	public  void setLevel(Integer level){
		this.level = level;
		if(myGuildVo != null){
			myGuildVo[6] = level;
		}
		if(guildListVo != null){
			guildListVo[3] = level;
		}
	}

	public Long getGold(){
		return gold;
	}

	/**
	 * 设置公会资金
	 * @param gold
	 */
	public void setGold(Long gold){
		this.gold = gold;
	}
	
	public String getNotice(){
		return notice;
	}

	public  void setNotice(String notice){
		this.notice = notice;
		if(myGuildVo != null){
			myGuildVo[2] = notice;
		}
	}

	public Long getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Long createTime){
		this.createTime = createTime;
	}

	public Integer getApplyType(){
		return applyType;
	}

	public  void setApplyType(Integer applyType){
		this.applyType = applyType;
		if(myGuildVo != null){
			myGuildVo[7] = applyType;
		}
		if(guildListVo != null){
			guildListVo[9] = applyType;
		}
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public void setGuildMemberManager(GuildMemberManager guildMemberManager) {
		this.guildMemberManager = guildMemberManager;
	}

	public GuildLogManager getGuildLogManager() {
		return guildLogManager;
	}

	public void setGuildLogManager(GuildLogManager guildLogManager) {
		this.guildLogManager = guildLogManager;
	}

	public int getMaxCount() {
		return maxCount;
	}
	
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public Integer getSchool() {
		return school;
	}

	public void setSchool(Integer school) {
		this.school = school;
	}

	public Integer getItem1() {
		return item1;
	}

	public void setItem1(Integer item1) {
		this.item1 = item1;
	}

	public Integer getItem2() {
		return item2;
	}

	public void setItem2(Integer item2) {
		this.item2 = item2;
	}

	public Integer getItem3() {
		return item3;
	}

	public void setItem3(Integer item3) {
		this.item3 = item3;
	}

	public Integer getItem4() {
		return item4;
	}

	public void setItem4(Integer item4) {
		this.item4 = item4;
	}

	public Guild copy(){
		Guild result = new Guild();
		result.setId(getId());
		result.setName(getName());
		result.setLevel(getLevel());
		result.setGold(getGold());
		result.setNotice(getNotice());
		result.setCreateTime(getCreateTime());
		result.setApplyType(getApplyType());
		result.setGuildLogManager(getGuildLogManager());
		result.setMaxCount(getMaxCount());
		result.setSchool(getSchool());
		result.setItem1(getItem1());
		result.setItem2(getItem2());
		result.setItem3(getItem3());
		result.setItem4(getItem4());
		return result;
	}
	
	public void initLog(JSONArray array){
		if(guildLogManager == null){
			guildLogManager = new GuildLogManager();
			if(array != null){
				guildLogManager.initLog(array);
			}
		}
	}
	
	/**
	 * 公会人数是否已满
	 * @return true公会已满
	 */
	public boolean isFull(){
		synchronized (updateLock) {
			return guildMemberManager.getSize() >= maxCount;
		}
	}
	
	/**
	 * 新增成员
	 * @param guildMember
	 * @return	是否成功 flase:公会已满，添加失败
	 */
	public boolean addMember(GuildMember guildMember){
		if(isFull() || dissoling){
			return false;
		}
		synchronized (updateLock) {
			if(guildMemberManager.getSize() >= maxCount || dissoling){
				return false;
			}
			guildMemberManager.addMember(guildMember);
		}
		if(myGuildVo != null){
			myGuildVo[5] = getSize();
		}
		if(guildListVo != null){
			guildListVo[4] = getSize();
		}
		return true;
	}
	/**
	 * 成员离开公会
	 * @param userRoleId
	 * @return
	 */
	public void removeMember(Long userRoleId){
		guildMemberManager.removeMember(userRoleId);
		if(myGuildVo != null){
			myGuildVo[5] = getSize();
		}
		if(guildListVo != null){
			guildListVo[4] = getSize();
		}
	}
	
	/**
	 * 重新计算总战力
	 */
	public void reCalZplus(){
		guildMemberManager.reCalZplus();
	}
	
	/**
	 * 获取会长
	 * @return
	 */
	public GuildMember getLeader() {
		return guildMemberManager.getLeader();
	}
	
	/**
	 * 获取当前公会总人数
	 * @return
	 */
	public int getSize(){
		return guildMemberManager.getSize();
	}
	/**
	 * 获取公会总战力
	 * @return
	 */
	public long getAllZplus() {
		return guildMemberManager.getAllZplus();
	}
	
	/**
	 * 获取公会管理层列表
	 * @return
	 */
	public List<Object[]> getManagerList(){
		return guildMemberManager.getManagerList();
	}

	/**
	 * 获取前端显示本公会信息
	 * @return
	 */
	public Object[] getMyGuildVo() {
		if(myGuildVo == null){
			myGuildVo = GuildOutPutUtil.getMyGuildVo(this);
		}
		return myGuildVo;
	}

	public void setMyGuildVo(Object[] myGuildVo) {
		this.myGuildVo = myGuildVo;
	}

	/**
	 * 获取前端显示公会列表信息
	 * @return
	 */
	public Object[] getGuildListVo() {
		if(guildListVo == null){
			guildListVo = GuildOutPutUtil.getGuildListVo(this);
		}else{
			guildListVo[5] = getAllZplus();
			guildListVo[8] = getManagerList().toArray(); 
			guildListVo[10] =isHcZbsWinner();
			guildListVo[11] =isKfYunGongWinner();
		}
		return guildListVo;
	}

	public void setGuildListVo(Object[] guildListVo) {
		this.guildListVo = guildListVo;
	}
	
	/**
	 * 获取所有在线成员id数组
	 * @return
	 */
	public Long[] getRoleIds(){
		return guildMemberManager.getRoleIds();
	}
	
	/**
	 * 获取所有在线成员id
	 * @return
	 */
	public List<Long> getOnlineRoleIds(){
		return guildMemberManager.getOnlineRoleIds();
	}
	
	/**
	 * 获取全部成员列表
	 * @return
	 */
	public List<GuildMember> getAllMembers(){
		return new ArrayList<>(guildMemberManager.getAllMembers());
	}
	/**
	 * 获取该职位有多少人
	 * @param postion
	 * @return
	 */
	public int getPostionCount(int postion){
		return guildMemberManager.getPostionCount(postion);
	}
	/**
	 * 管理层变动
	 */
	public void changeManager(){
		guildMemberManager.changeManager();
	}
	/**
	 * 成员上线业务
	 * @param userRoleId
	 */
	public void onlineHandle(Long userRoleId){
		guildMemberManager.onlineHandle(userRoleId);
	}
	/**转职变职业*/
	public void zhuanzhi(Long userRoleId,int configId){
		guildMemberManager.zhuanzhi(userRoleId, configId);
	}
	/**
	 * 成员下线业务
	 * @param userRoleId
	 */
	public void offlineHandle(Long userRoleId){
		guildMemberManager.offlineHandle(userRoleId);
	}
	/**
	 * 会长变更
	 * @param leader
	 */
	public void changeLeader(GuildMember leader){
		guildMemberManager.changeLeader(leader);
		if(myGuildVo != null){
			myGuildVo[3] = leader.getName();
		}
	}
	/**
	 * 开始解散公会
	 */
	public boolean beginDissol(){
		synchronized (updateLock) {
			if(guildMemberManager.getSize() > 1){
				return false;
			}
			dissoling = true;
		}
		return true;
	}
	/**
	 * 最终写入
	 */
	public void finalWrite(){
		guildLogManager.finalWrite();
	}
	/**
	 * 是否初始化过
	 * @return
	 */
	public boolean isInit(){
		return guildMemberManager != null;
	}

}
