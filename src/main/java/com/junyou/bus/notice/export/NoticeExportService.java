package com.junyou.bus.notice.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.notice.service.NoticeService;


@Component
public class NoticeExportService {
	
	@Autowired
	private NoticeService noticeService;
	
	public Object getNotice(){
		return noticeService.getNotice();
	}
}
