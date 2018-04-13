package com.junyou.public_.jindu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.constants.GameConstants;
import com.junyou.public_.jindu.dao.GameJinduDao;
import com.junyou.public_.jindu.entity.GameJindu;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;

@Service
public class GameJinduService {
	@Autowired
	private GameJinduDao gameJinduDao;
	
	private GameJindu gameJindu;
	
	private GameJindu getTodayGameJindu(){
		if(gameJindu == null){
			gameJindu = createGameJindu();
		}else if(!DatetimeUtil.dayIsToday(gameJindu.getTime())){
			gameJindu = createGameJindu();
		}
		return gameJindu;
	}
	
	private GameJindu createGameJindu(){
		GameJindu gameJindu = new GameJindu();
		gameJindu.setTime(GameSystemTime.getSystemMillTime());
		gameJindu.setGameRole(0);
		gameJindu.setCreateRole(0);
		gameJindu.setFirstTask(0);
		gameJindu.setGameRoleOther(0);
		gameJindu.setCreateRoleOther(0);
		gameJindu.setFirstTaskOther(0);
		gameJinduDao.cacheInsert(gameJindu, GameConstants.DEFAULT_ROLE_ID);
		return gameJindu;
	}
	
	public List<GameJindu> initGameJindu() {
		List<GameJindu> list = gameJinduDao.initGameJindu();
		if(list.size() > 0){
			gameJindu = list.get(0);
		}
		return list;
	}
	
	public void changeJindu(int type){
		GameJindu gameJindu = getTodayGameJindu();
		if(type == GameConstants.GAME_JINDU_TYPE_ROLE){
			gameJindu.setGameRole(gameJindu.getGameRole() + 1);
		}else if(type == GameConstants.GAME_JINDU_TYPE_CREATE){
			gameJindu.setCreateRole(gameJindu.getCreateRole() + 1);
		}else if(type == GameConstants.GAME_JINDU_TYPE_FIRST){
			gameJindu.setFirstTask(gameJindu.getFirstTask() + 1);
		}else if(type == GameConstants.GAME_JINDU_TYPE_ROLE_OTHER){
			gameJindu.setGameRoleOther(gameJindu.getGameRoleOther() + 1);
		}else if(type == GameConstants.GAME_JINDU_TYPE_CREATE_OTHER){
			gameJindu.setCreateRoleOther(gameJindu.getCreateRoleOther() + 1);
		}else if(type == GameConstants.GAME_JINDU_TYPE_FIRST_OTHER){
			gameJindu.setFirstTaskOther(gameJindu.getFirstTaskOther() + 1);
		}
		gameJinduDao.cacheUpdate(gameJindu, GameConstants.DEFAULT_ROLE_ID);
	}
	
	
	
}
