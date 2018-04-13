package com.junyou.bus.platform._360.service;

import com.junyou.log.ChuanQiLog;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.http.HttpClientMocker;
import com.junyou.utils.md5.Md5Utils;

public class _360Tequan_Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		 String qid = "170610636"; 
		 String skey = "1";
		 int levelStr = 1;
//		 String levelStr = "share";
		
		 String privkey = "360hqg_KIO6GKLA3TWH";
		 int aid = 2517;
		 String gkey = "hqg";
		 String type  = "privi";
		 long time = GameSystemTime.getSystemMillTime();
		 StringBuffer signBuf = new StringBuffer();
		 signBuf.append(aid).append("|") .append(gkey) .append("|").append(qid).append("|")
		 .append(type) .append("|").append(time).append("|").append(privkey);
		 
		 String sign = Md5Utils.md5To32(signBuf.toString());
		 
		 String requestUrl = "http://hd.wan.360.cn/check_privi.html?";
		//参数填充
		StringBuffer params = new StringBuffer();
		params.append("aid=").append(aid);
		params.append("&gkey=").append(gkey);
		params.append("&skey=").append(skey);
		params.append("&qid=").append(qid);
		params.append("&type=").append(type);
		params.append("&level=").append(levelStr);
		params.append("&time=").append(time);
		params.append("&sign=").append(sign);
		ChuanQiLog.debug("完整访问连接:"+requestUrl+params.toString());
		//发起后台请求
		String result = HttpClientMocker.requestMockPost(requestUrl, params.toString());
		ChuanQiLog.debug("特权接口返回："+result);

	}
}
