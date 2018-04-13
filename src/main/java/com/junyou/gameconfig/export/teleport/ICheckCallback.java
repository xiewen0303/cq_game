package com.junyou.gameconfig.export.teleport;

import java.util.Map;

import com.junyou.configure.ClientTimeScope;

public interface ICheckCallback {
	
	public boolean isSuccess();
	
	public Object[] getErrorCode();
	
	public void moneyError();
	
	public void liquanError();
	
	public void maxlevelError();
	
	public void minLevelError();
	
	public void noGuildError();
	
	public void gzLevelError();
	
	public void startTimeError();
	
	public void endTimeError();
	
	public void mustItemError();
	
	public void useItemError();
	
	public void vipError();
	
	public void timeError();
	
	public void useItemHandle(String goodsId,int goodsCount);

	public boolean maxLevelCheck(int maxLevel);
	
	public boolean minLevelCheck(int minLevel);
	
	public boolean haveGuild();
	
	public boolean guanZhiLevelCheck(int gzLevel);
	
	public boolean startDayCheck(String startDay);
	
	public boolean endDayCheck(String endDay);
	
	public boolean startTimeCheck(long startTime);
	
	public boolean endTimeCheck(long endTime);
	
	public boolean startDayTimeCheck(int hour,int minute);
	
	public boolean endDayTimeCheck(int hour,int minute);
	
	public boolean mustItemCheck(Map<String, Integer> items);

	public boolean useItemCheck(Map<String, Integer> items);
	
	public boolean vipCheck(Integer vipLevel);
	
	public boolean activeTimeCheck(ClientTimeScope timeScope);
	
	public void success();
}
