/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.event;

import com.junyou.log.LogPrintHandle;


/**
 *@Description  热发布子活动参与人数日志打印
 *@Author Yang Gao
 *@Since 2016-4-28
 *@Version 1.1.0
 */
public class RfbActivityPartInLogEvent extends AbsGameLogEvent {

    private static final long serialVersionUID = 1L;
    
    /*子活动ID*/
    private int     activityId;
    /*子活动名称*/
    private String  activityName;
    /*子活动类型*/
    private int     activityType ;
    /*子活动开始时间*/
    private long    activityStartTime;
    /*子活动结束时间*/
    private long    activityEndTime;
    /*参与角色游戏编号*/
    private long    userRoleId;
    /*日志类型*/
    private int     logType;

    /**
     * @param type
     */
    public RfbActivityPartInLogEvent(int logType, int activityId, String activityName, int activityType, long activityStartTime, long activityEndTime, long userRoleId) {
        super(LogPrintHandle.REFABU_PART_IN);
        
        this.logType = logType;
        this.activityId = activityId;
        this.activityName = activityName;
        this.activityType = activityType;
        this.activityStartTime = activityStartTime;
        this.activityEndTime = activityEndTime;
        this.userRoleId = userRoleId;
    }
    
    public int getLogType() {
        return logType;
    }


    public void setLogType(int logType) {
        this.logType = logType;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public long getActivityStartTime() {
        return activityStartTime;
    }

    public void setActivityStartTime(long activityStartTime) {
        this.activityStartTime = activityStartTime;
    }

    public long getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(long activityEndTime) {
        this.activityEndTime = activityEndTime;
    }

    public long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(long userRoleId) {
        this.userRoleId = userRoleId;
    }

}
