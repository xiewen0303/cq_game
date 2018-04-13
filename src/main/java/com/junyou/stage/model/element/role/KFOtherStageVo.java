package com.junyou.stage.model.element.role;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.junyou.stage.model.element.role.business.RoleEquipData;

/**
 * 场景其它数据VO
 * @author DaoZheng Yuan
 * 2014-10-16 下午5:57:55
 */
public class KFOtherStageVo implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer vipLevel = 0;//VIP等级
	
	private Long guildId;//公会id
	
	private String guildName;//公会名
	
	private List<String> skills = new ArrayList<>();//技能
	
	private RoleEquipData equipData;//装备
	
	private Integer dazuo;//打坐加成
	
    private boolean isHcZbsWinnerGuildLeader = false;// 是否是皇城战胜利帮派门主

    public boolean isHcZbsWinnerGuildLeader() {
        return isHcZbsWinnerGuildLeader;
    }

    public void setHcZbsWinnerGuildLeader(boolean isHcZbsWinnerGuildLeader) {
        this.isHcZbsWinnerGuildLeader = isHcZbsWinnerGuildLeader;
    }

    private boolean isKfYunGongWinnerGuildLeader = false;// 是否是跨服云宫之巅胜利帮派门主

    public boolean isKfYunGongWinnerGuildLeader() {
        return isKfYunGongWinnerGuildLeader;
    }

    public void setKfYunGongWinnerGuildLeader(boolean isKfYunGongWinnerGuildLeader) {
        this.isKfYunGongWinnerGuildLeader = isKfYunGongWinnerGuildLeader;
    }
	
	public Integer getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(Integer vipLevel) {
		this.vipLevel = vipLevel;
	}

	public Long getGuildId() {
		return guildId;
	}

	public void setGuildId(Long guildId) {
		this.guildId = guildId;
	}

	public String getGuildName() {
		return guildName;
	}

	public void setGuildName(String guildName) {
		this.guildName = guildName;
	}

	public List<String> getSkills() {
		return skills;
	}

	public void addSkill(String skillId) {
		this.skills.add(skillId);
	}

	public RoleEquipData getEquipData() {
		return equipData;
	}

	public void setEquipData(RoleEquipData equipData) {
		this.equipData = equipData;
	}

	public Integer getDazuo() {
		return dazuo;
	}

	public void setDazuo(Integer dazuo) {
		this.dazuo = dazuo;
	}
	
	
}
