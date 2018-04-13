package com.junyou.bus.platform._360.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.platform.common.entity.VplanMessage;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.http.HttpClientMocker;
import com.junyou.utils.md5.Md5Utils;

/**
 * 360V计划测试
 * @author lxn
 *
 */
public class _360Vplan_Test {

	public static void main(String[] args) {

		// TODO Auto-generated method stub
		
		String uid  ="1402167009";  //真是会员：1402167009
		String gkey="hqg";
		String lkey="c7802435f1004460a7bf86c2ed6d33a8";
		long time = GameSystemTime.getSystemMillTime();
		String version="3.0";
		String url ="http://rcapi.360-g.net/vplan_wan?";
		StringBuffer signBuf = new StringBuffer();
		signBuf.append(gkey).append(uid).append(time).append(lkey);
		String sign = Md5Utils.md5To32(signBuf.toString());
		
		StringBuffer paramsBuf = new StringBuffer();
		paramsBuf.append("uid=").append(uid)
		         .append("&gkey=").append(gkey)
		         .append("&time=").append(time)
		         .append("&sign=").append(sign)
		         .append("&version=").append(version);
		String result = HttpClientMocker.requestMockPost(url, paramsBuf.toString());
		System.out.println(result.contains("\"errno\":0,"));
		String str  = "{\"errno\":-2,\"errmsg\":3432,\"data\":[]}";
		
		JSONObject  json  = JSONObject.parseObject(str);
		VplanMessage vmessage = JSON.parseObject(result,VplanMessage.class);
		System.out.println("完整链接："+url+ paramsBuf.toString());
		System.out.println("V计划会员信息："+result);
	}
	
 
	 
}
