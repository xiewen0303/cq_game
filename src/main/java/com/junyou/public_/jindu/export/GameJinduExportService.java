package com.junyou.public_.jindu.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.public_.jindu.entity.GameJindu;
import com.junyou.public_.jindu.service.GameJinduService;

@Service
public class GameJinduExportService {
	@Autowired
	private GameJinduService gameJinduService;
	
	public List<GameJindu> initGameJindu() {
		return gameJinduService.initGameJindu();
	}
}
