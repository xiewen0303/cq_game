package com.junyou.bus.email.entity;

import com.junyou.public_.email.entity.Email;

public class EmailInfo {
	public EmailInfo(Email email,EmailRelation emailRelation){
		this.email = email;
		this.emailRelation = emailRelation;
	}
	private Email email;
	private EmailRelation emailRelation;
	public Email getEmail() {
		return email;
	}
	public EmailRelation getEmailRelation() {
		return emailRelation;
	}
	
}
