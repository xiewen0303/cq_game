package com.junyou.bus.platform.qq.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.junyou.utils.common.ObjectUtil;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_qq_qzone")
public class RoleQqQzone extends AbsVersion implements Serializable, IEntity {

    @EntityField
    private static final long serialVersionUID = 1L;

    @Column("user_role_id")
    private Long userRoleId;

    @Column("once_award_status")
    private Integer onceAwardStatus;

    @Column("every_award_timestamp")
    private Long everyAwardTimestamp;

    @Column("level_award_log")
    private String levelAwardLog;

    @Column("create_timestamp")
    private Long createTimestamp;

    @Column("update_timestamp")
    private Long updateTimestamp;

    /**
     * 获取所有已领取的等级礼包的等级集合 
     * @return
     */
    public List<Integer> getReceiveLevelArray(){
        List<Integer> levelIntArray = null;
        if (!ObjectUtil.strIsEmpty(this.levelAwardLog)) {
            levelIntArray = new ArrayList<Integer>();
            String[] levelStrArray = this.levelAwardLog.split(",");
            for (int i = 0; i < levelStrArray.length; i++) {
                levelIntArray.add(Integer.valueOf(levelStrArray[i]));
            }
        }
        return levelIntArray;
    }
    
    /**
     * 更新领取的等级礼包日志集合 
     * @return
     */
    public void updateLevelLog(int level){
        StringBuilder sb = new StringBuilder();
        if (null == this.levelAwardLog){
            sb.append(level);
        }else{
            sb.append(this.levelAwardLog).append(",").append(level);
        }
        this.setLevelAwardLog(sb.toString());
    }
    
    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public Integer getOnceAwardStatus() {
        return onceAwardStatus;
    }

    public void setOnceAwardStatus(Integer onceAwardStatus) {
        this.onceAwardStatus = onceAwardStatus;
    }

    public Long getEveryAwardTimestamp() {
        return everyAwardTimestamp;
    }

    public void setEveryAwardTimestamp(Long everyAwardTimestamp) {
        this.everyAwardTimestamp = everyAwardTimestamp;
    }

    public String getLevelAwardLog() {
        return levelAwardLog;
    }

    public void setLevelAwardLog(String levelAwardLog) {
        this.levelAwardLog = levelAwardLog;
    }

    public Long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Long createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public Long getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Long updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    @Override
    public String getPirmaryKeyName() {
        return "userRoleId";
    }

    @Override
    public Long getPrimaryKeyValue() {
        return userRoleId;
    }

    public RoleQqQzone copy() {
        RoleQqQzone result = new RoleQqQzone();
        result.setUserRoleId(getUserRoleId());
        result.setOnceAwardStatus(getOnceAwardStatus());
        result.setEveryAwardTimestamp(getEveryAwardTimestamp());
        result.setLevelAwardLog(getLevelAwardLog());
        result.setCreateTimestamp(getCreateTimestamp());
        result.setUpdateTimestamp(getUpdateTimestamp());
        return result;
    }
}
