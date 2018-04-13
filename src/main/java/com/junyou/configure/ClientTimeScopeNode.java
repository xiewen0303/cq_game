package com.junyou.configure;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClientTimeScopeNode {

	public static final Integer VALUE_WILDCARD = -1;
	
	private List<Integer> years;
	
	private List<Integer> months;
	
	private List<Integer> days;
	
	private List<Integer> weeks;
	
	private Integer beginHour;
	
	private Integer beginMin;
	
	private Integer endHour;
	
	private Integer endMin;
	

	public void setYears(List<Integer> years) {
		this.years = years;
	}



	public void setMonths(List<Integer> months) {
		this.months = months;
	}



	public void setDays(List<Integer> days) {
		this.days = days;
	}



	public void setWeeks(List<Integer> weeks) {
		this.weeks = weeks;
	}



	public void setBeginHour(Integer beginHour) {
		this.beginHour = beginHour;
	}



	public void setBeginMin(Integer beginMin) {
		this.beginMin = beginMin;
	}



	public void setEndHour(Integer endHour) {
		this.endHour = endHour;
	}



	public void setEndMin(Integer endMin) {
		this.endMin = endMin;
	}



	public boolean inScope(Calendar calendar) {
		// TODO Auto-generated method stub
		
		//年
		int year = calendar.get(Calendar.YEAR);
		if(null != years && !years.contains(VALUE_WILDCARD) &&  !years.contains(year)){
			return false;
		}
		
		//月
		int month = calendar.get(Calendar.MONTH) + 1;
		if(null != months && !months.contains(VALUE_WILDCARD) &&  !months.contains(month)){
			return false;
		}
		
		//日
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		if(null != days && !days.contains(VALUE_WILDCARD) &&  !days.contains(day)){
			return false;
		}
		
		//星期
		int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if(null != weeks && !weeks.contains(VALUE_WILDCARD) &&  !weeks.contains(week)){
			return false;
		}
		
		//时间
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		//开始时间
		if((null != beginHour && hour < beginHour) || (hour == beginHour &&  (null == beginMin || minute < beginMin))){
			return false;
		}
		//结束时间
		if((null != endHour && hour > endHour) || ( hour == endHour &&  (null == endMin || minute > endMin ))  ){
			return false;
		}
		
		
		return true;
	}
	
	
	public static void main(String[] args) {
		ClientTimeScopeNode node = new ClientTimeScopeNode();
		
		List<Integer> years = new ArrayList<Integer>();
		years.add(2012);
		node.setYears(years);
		
		List<Integer> months = new ArrayList<Integer>();
		months.add(5);
		node.setMonths(months);
		
		List<Integer> days = new ArrayList<Integer>();
		days.add(21);
		node.setDays(days);
	
		List<Integer> weeks = new ArrayList<Integer>();
		weeks.add(1);
		node.setWeeks(weeks);
		
		node.setBeginHour(13);
		node.setBeginMin(45);
		node.setEndHour(13);
		node.setEndMin(45);
		
		boolean inScope = node.inScope(Calendar.getInstance());
		System.out.println(inScope);
		
	}

}
