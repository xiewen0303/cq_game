package com.junyou.event;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.junyou.bus.platform.utils.PlatformConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.md5.Md5Utils;

/**
 * 37聊天监控事件
 * @author LiuYu
 * 2015-5-9 下午1:38:50
 */
public class ChatListenerFor37Event extends AbsHttpRequestEvent{

	private static final long serialVersionUID = 1L;
	private String serverId;
	private long time;
	private String userId;
	private String name;
	private long userRoleId;
	private String toUserId;
	private String toName;
	private String content;
	private String ip;
	
	private static String keyString = "chat_key";
	private static String urlString = "chat_url";

	public ChatListenerFor37Event(String userId,String name, long userRoleId, String toUserId, String toName,String content, String ip,String serverId) {
		super();
		this.time = GameSystemTime.getSystemMillTime()/1000;
		this.userId = userId;
		this.name = name;
		this.userRoleId = userRoleId;
		this.toUserId = toUserId;
		this.toName = toName;
		this.content = content;
		this.ip = ip;
		this.serverId = serverId;
	}
	
	private String getSign(){
		StringBuffer sb = new StringBuffer();
		sb.append(PlatformConfigUtil.getNowPlatformConfigInfo(keyString)).append(serverId);
		sb.append(userId).append(name);
		sb.append(userRoleId).append(content);
		sb.append(time);
		return Md5Utils.md5To32(sb.toString());
	}
	
	@Override
	public String getHttpUrl() {
		StringBuilder sb = new StringBuilder();
		sb.append(PlatformConfigUtil.getNowPlatformConfigInfo(urlString));
		sb.append("&server_id=").append(serverId);
		sb.append("&time=").append(time);
		sb.append("&login_account=").append(userId);
		sb.append("&actor=").append(name);
		sb.append("&actor_id=").append(userRoleId);
		sb.append("&to_login_account=").append(toUserId);
		sb.append("&to_actor=").append(toName);
		try {
			String cont = URLEncoder.encode(content,"utf-8");
			sb.append("&content=").append(cont);
		} catch (UnsupportedEncodingException e) {
			ChuanQiLog.error("",e);
		}
		sb.append("&ip=").append(ip);
		sb.append("&sign=").append(getSign());
		
		return sb.toString();
	}

	@Override
	public void callBack(String data) {
		if(data != null && !"1".equals(data)){
			ChuanQiLog.error("37聊天监控返回信息:{},请求连接:{}",data,getHttpUrl());
		}
	}
}
