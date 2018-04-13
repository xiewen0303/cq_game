package com.junyou.bus.notice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.notice.configure.export.NoticeConfigExportService;

@Component
public class NoticeService {
	
	@Autowired
	private NoticeConfigExportService noticeConfigExportService;
	
	public Object getNotice(){
		return noticeConfigExportService.getNoticeCategoryConfig().getMsg();
	}
}
