package com.junyou.public_.email.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.junyou.utils.datetime.GameSystemTime;

public class EmailManager {
	private EmailManager(){}
	private static EmailManager manager = new EmailManager();
	public static EmailManager getManager(){return manager;}
	
	private ConcurrentMap<Long, Email> emails = new ConcurrentHashMap<>();
	
	public String init(List<Email> list){
		long now = GameSystemTime.getSystemMillTime();
		StringBuilder delIds = new StringBuilder();
		for (Email email : list) {
			if(email.getExpireTime() > now){
				emails.put(email.getId(), email);
			}else{
				delIds.append(",").append(email.getId());
			}
		}
		if(delIds.length() > 1){
			return delIds.substring(1);
		}else{
			return null;
		}
	}
	
	public void addEmail(Email email){
		emails.put(email.getId(), email);
	}
	
	public Email getEmail(Long id){
		return emails.get(id);
	}
	
	public List<Long> getExpireEmailIds(){
		List<Email> list = new ArrayList<>(emails.values());
		long now = GameSystemTime.getSystemMillTime();
		List<Long> ids = new ArrayList<>();
		for (Email email : list) {
			if(email.getExpireTime() <= now){
				emails.remove(email.getId());
				ids.add(email.getId());
			}
		}
		return ids;
	}
	
	
	public boolean delEmail(Long id){
		return emails.remove(id) != null;
	}
}
