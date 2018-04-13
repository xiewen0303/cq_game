package com.junyou.bus.email.entity;

import java.util.Comparator;

public class EmailInfoComparator implements Comparator<EmailInfo>{

	@Override
	public int compare(EmailInfo o1, EmailInfo o2) {
		if(o1.getEmail().getCreateTime().getTime() > o2.getEmail().getCreateTime().getTime()){
			return 1;
		}else{
			return -1;
		}
	}

}
