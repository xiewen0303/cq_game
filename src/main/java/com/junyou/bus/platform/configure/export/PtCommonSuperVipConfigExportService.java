package com.junyou.bus.platform.configure.export;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.junyou.configure.parser.impl.AbsRemoteRefreshAbleConfigureParserByPlatformAndServer;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
/** 超级会员显示qq 累充等相同业务
 * @description 
 * @author Lxn
 */
@Component
public class PtCommonSuperVipConfigExportService{//TODO wind extends AbsRemoteRefreshAbleConfigureParserByPlatformAndServer  {
	
	private SuperVipConfig superVipConfig =null; //缓存配置
	
	private static final String FILE_NAME = "VipQQ";
	 
	private String configureName = "vipqq/"+FILE_NAME;

	

//	@Override
	protected void configureDataResolve(byte[] data) {
		if(data == null){
			ChuanQiLog.error(" platform data is null! ");
			return;
		}
		JSONObject json = GameConfigUtil.getJsonByBytes(data);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" platform data is error! ");
			return;
		}
		superVipConfig = getBaseInfo(json);
		 
	}
	
	private SuperVipConfig getBaseInfo(JSONObject json){
		if(json!=null && !json.isEmpty()){
			SuperVipConfig config = new SuperVipConfig();
			Integer allRecharge  =CovertObjectUtil.object2Integer(json.get("allRecharge"));
			allRecharge=allRecharge==null?GameConstants.ALLRECHARGE:allRecharge;
			Integer onceRecharge  =CovertObjectUtil.object2Integer(json.get("onceRecharge"));
			onceRecharge=onceRecharge==null?GameConstants.ONCERECHARGE:onceRecharge;
			String pic  =CovertObjectUtil.object2String(json.get("pic"));
			pic=pic==null?"":pic;
			Long qq  =CovertObjectUtil.object2Long(json.get("qq"));
			qq=qq==null?0L:qq;
			config.setAllRecharge(allRecharge);
			config.setOnceRecharge(onceRecharge);
			config.setPic(pic);
			config.setQq(qq);
			if(config.getOnceRecharge()==0){
				//有些没有单次充值的限定默认 ：单次充值=累充！ （防止运营忘记把没有单次充值的需求填写为0，运营默认填写值应该跟累充一样）
				config.setOnceRecharge(config.getAllRecharge());
			}
			return config;
		}
		
		return null;
	}
	
	//@Override
	protected String getConfigureName() {
		return configureName;
	}
	public SuperVipConfig getVipBaseConfig() {
	   return superVipConfig;
	}
	/**
	 * 更新config
	 */
	public  void updateConfig(Map<String, Object> params){
		int state = CovertObjectUtil.object2Integer(params.get("state")); 
		if(state==0 && superVipConfig==null){
			superVipConfig = new SuperVipConfig();
		}
		superVipConfig.setAllRecharge(CovertObjectUtil.object2Integer(params.get("allRecharge")));
		superVipConfig.setOnceRecharge(CovertObjectUtil.object2Integer(params.get("onceRecharge")));
		superVipConfig.setPic(CovertObjectUtil.object2String(params.get("pic")));
		superVipConfig.setQq(CovertObjectUtil.object2Long(params.get("qq")));
		if(superVipConfig.getOnceRecharge()==0){
			//有些没有单次充值的限定默认 ：单次充值=累充！ （防止运营忘记把没有单次充值的需求填写为0，运营默认填写值应该跟累充一样）
			superVipConfig.setOnceRecharge(superVipConfig.getAllRecharge());
		}
	}
	/**
	 * 活动取消
	 */
	public  void closeActivity(){
		this.superVipConfig = null;
	}
	//@Override
	protected void clearData() {
		
	}
	//@Override
	public String getSortName() {
		return FILE_NAME;
	}
}
