package com.junyou.bus.daytask.filter;

import com.junyou.bus.daytask.entity.TaskDay;
import com.kernel.data.dao.IQueryFilter;

public class TaskDayTypeFilter implements IQueryFilter<TaskDay> {

	private int type;
	
	private boolean found = false;
	
	
	public TaskDayTypeFilter(int type) {
		this.type = type;
	}

	@Override
	public boolean check(TaskDay entity) {
		boolean flag = entity.getTaskDayType()== type;
		if(flag){
			found = true;
		}
		
		return flag;
	}

	@Override
	public boolean stopped() {
		return found;
	}

}
