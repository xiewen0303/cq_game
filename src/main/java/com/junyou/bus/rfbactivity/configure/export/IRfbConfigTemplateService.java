package com.junyou.bus.rfbactivity.configure.export;

/**
 * 热发布业务解析模板接口
 */
public interface IRfbConfigTemplateService {

	/**
	 * 解析业务数据
	 * @param subId
	 * @param data
	 */
	public void analysisConfigureDataResolve(int subId,byte[] data);
	
	/**
	 * 活动的子活动的背景图,可以扩展
	 * @return
	 */
	public Object getChildData(int subId);
	
//	/**
//	 * 表示
//	 * @return
//	 */
//	public int getGuid();
}
