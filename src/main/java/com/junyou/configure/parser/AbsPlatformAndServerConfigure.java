package com.junyou.configure.parser;

import java.util.ArrayList;
import java.util.List;

import com.junyou.configure.loader.DirType;
import com.junyou.configure.schedule.RefreshableConfigureManager;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.md5.Md5Utils;
/**
 * 等通知配置解析器（读取平台及服务器全部配置文件）
 * @description 
 * @author Hanchun
 * @email 279444454@qq.com
 * @date 2015-3-27 下午2:57:42
 */
public abstract class AbsPlatformAndServerConfigure extends AbsConfigureParser {
	
	private List<DirType> typeList;
	
	@Override
	public void init() {
		typeList = new ArrayList<DirType>();
		typeList.add(DirType.SERVER);
		typeList.add(DirType.PLATFORM);
		typeList.add(DirType.GLOBAL);
		for (DirType type : typeList) {
			Object[] signInfo = getLoader().loadSign(getConfigureName(),type);
			if(signInfo == null){
				continue ;
			}
			String _sign = (String)signInfo[0];
			String path = (String)signInfo[1];
			byte[] data = getLoader().load(path);
			
			try{
				this.sign = _sign;
				configureDataResolve(data);
			}catch (Exception e) {
				ChuanQiLog.error("",e);
			}
			break;
		}
		
		//增加入管理器
		RefreshableConfigureManager.addConfig(this);
	}
	
	
	/**
	 * 配置版本刷新
	 * @return -2：未找到配置文件，-1：当前无更新，0：更新成功,读取服务器配置，1：平台配置，2：全服配置
	 */
	public int versionRefresh(){
		int t = -2;
		try{
			for (int i = 0; i < typeList.size(); i++) {
				DirType type = typeList.get(i);
				Object[] signInfo = getLoader().loadSign(getConfigureName(),type);
				if(signInfo == null){
					continue ;
				}
				String _sign = (String)signInfo[0];
				String path = (String)signInfo[1];
				
				String sign = this.sign;
				if(null == sign ||	!sign.equals(_sign)){
					byte[] data = getLoader().load(path);
					
					//验证下载后的文件的md5值和取到的版本号是否一致，不一致的话不做任何处理
					if( _sign != null && !_sign.equals(Md5Utils.md5Bytes(data))){
						t = -3;
						break;
					}
					
					this.sign = _sign;
					
					configureDataResolve(data);
					
					t = i;
				}else{
					t = -1;
				}
				break;
			}
		}catch(Exception e){
			ChuanQiLog.error("versionrefresh failed:" + getConfigureName());
		}
		
		return t;
	}
	
	/**
	 * 获取当前配置简称
	 * @return
	 */
	public abstract String getSortName();
}
