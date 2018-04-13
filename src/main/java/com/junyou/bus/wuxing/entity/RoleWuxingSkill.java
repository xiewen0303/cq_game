package com.junyou.bus.wuxing.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_wuxing_skill")
public class RoleWuxingSkill extends AbsVersion implements Serializable, IEntity {

    @EntityField
    private static final long serialVersionUID = 1L;

    @Column("id")
    private Long id;

    @Column("user_role_id")
    private Long userRoleId;

    @Column("wuxing_type")
    private Integer wuxingType;

    @Column("skill_id1")
    private Integer skillId1;

    @Column("skill_id2")
    private Integer skillId2;

    @Column("skill_id3")
    private Integer skillId3;

    @Column("skill_id4")
    private Integer skillId4;

    @Column("create_time")
    private Long createTime;

    @Column("update_time")
    private Long updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public Integer getWuxingType() {
        return wuxingType;
    }

    public void setWuxingType(Integer wuxingType) {
        this.wuxingType = wuxingType;
    }

    public Integer getSkillId1() {
        return skillId1;
    }

    public void setSkillId1(Integer skillId1) {
        this.skillId1 = skillId1;
    }

    public Integer getSkillId2() {
        return skillId2;
    }

    public void setSkillId2(Integer skillId2) {
        this.skillId2 = skillId2;
    }

    public Integer getSkillId3() {
        return skillId3;
    }

    public void setSkillId3(Integer skillId3) {
        this.skillId3 = skillId3;
    }

    public Integer getSkillId4() {
        return skillId4;
    }

    public void setSkillId4(Integer skillId4) {
        this.skillId4 = skillId4;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String getPirmaryKeyName() {
        return "id";
    }

    @Override
    public Long getPrimaryKeyValue() {
        return id;
    }

    public RoleWuxingSkill copy() {
        RoleWuxingSkill result = new RoleWuxingSkill();
        result.setId(getId());
        result.setUserRoleId(getUserRoleId());
        result.setWuxingType(getWuxingType());
        result.setSkillId1(getSkillId1());
        result.setSkillId2(getSkillId2());
        result.setSkillId3(getSkillId3());
        result.setSkillId4(getSkillId4());
        result.setCreateTime(getCreateTime());
        result.setUpdateTime(getUpdateTime());
        return result;
    }

    /**
     * @Description 获取指定位置的技能编号
     * @param seq 位置
     * @return
     */
    public Integer getWxSkillIdBySeq(int seq) {
        Integer rs = null;
        switch (seq) {
        case 1:
            rs = getSkillId1();
            break;

        case 2:
            rs = getSkillId2();
            break;

        case 3:
            rs = getSkillId3();
            break;

        case 4:
            rs = getSkillId4();
            break;

        default:
            break;
        }
        return rs;
    }

    /**
     * @Description 获取玩家所学习的技能编号集合
     * @return
     */
    public List<Integer> findRoleWxSkillIds() {
        List<Integer> rsIdList = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            Integer skillId = getWxSkillIdBySeq(i);
            if (skillId != null) {
                rsIdList.add(skillId);
            }
        }
        return rsIdList;
    }

    /**
     * @Description 更新指定位置的技能编号
     * @param skill_seq 技能位置
     * @param skill_id 技能等级
     */
    public void updateSkillId(int skill_seq, int skill_id) {
        if (0 == skill_seq || 0 == skill_id) {
            return;
        }
        switch (skill_seq) {
        case 1:
            this.setSkillId1(skill_id);
            break;
        case 2:
            this.setSkillId2(skill_id);
            break;
        case 3:
            this.setSkillId3(skill_id);
            break;
        case 4:
            this.setSkillId4(skill_id);
            break;
        default:
            break;
        }
    }
}
