package com.junyou.stage.collect.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.stage.collect.element.ActivityBox;
import com.junyou.stage.collect.service.CollectBoxService;

@Component
public class BoxExportService {
	@Autowired
	private	CollectBoxService collectBoxService;
	
	public ActivityBox createBox(){
		return collectBoxService.createActivityBox();
	}
}
