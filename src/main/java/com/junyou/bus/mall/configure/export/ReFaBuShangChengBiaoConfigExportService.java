package com.junyou.bus.mall.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsRemoteRefreshAbleConfigureParserWait;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.md5.ThreeDesEncryptAndEncrypt;
import com.junyou.utils.zip.CompressConfigUtil;

/**
 * 
 * @description 热发布商城 
 *
 * @author HanChun
 * @date 2015-03-17 14:29:30
 */
@Component
public class ReFaBuShangChengBiaoConfigExportService extends AbsRemoteRefreshAbleConfigureParserWait {
	/**
	 * 压缩后的所有商城数据
	 */
	private static Object COMPRESS_DATA = null; 
	
	private Map<Integer, ReFaBuShangChengBiaoConfig> refabuConfig ;
	/**
	  * configFileName
	 */
	private String FILE_NAME = "ReFaBuShangChengBiao";
	/**
	 * 配置名  http://192.168.0.202/cqupload/
	 */
	private String configureName = "gameconfig/" + FILE_NAME;
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if(data == null){
			return;
		}
		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = GameConfigUtil.getJsonArrayByBytes(data1);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" ReFaBuShangChengBiao data is error! ");
			return;
		}
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();  
		for(int i=0;i<json.size();i++){  
			JSONArray json1 = json.getJSONArray(i);
			Map<String,Object> map = new HashMap<String, Object>();
			for (int j = 0; j < json1.size(); j++) {
				Map<String,Object> aa = (Map<String, Object>)JSONObject.parse(json1.getString(j));
				map.putAll(aa);
			}
			list.add(map);
		}  
		if(list != null && list.size() > 0){
			Map<Integer, ReFaBuShangChengBiaoConfig> tmpRefabuConfig = new HashMap<>();
			for (Map<String,Object> map : list) {
				if (null != map) {

					ReFaBuShangChengBiaoConfig config = createReFaBuShangChengBiaoConfig(map);
					if(config == null){//配置异常
						continue;
					}
					tmpRefabuConfig.put(config.getId(), config);
				}
			}
			this.refabuConfig = tmpRefabuConfig;
			//压缩数据
			COMPRESS_DATA = CompressConfigUtil.compressAddCheckObject(list.toArray())[1];
			
			ConfigMd5SignManange.addConfigSign(FILE_NAME, data);
		}
	}
	
	private ReFaBuShangChengBiaoConfig createReFaBuShangChengBiaoConfig(Map<String, Object> tmp){
		ReFaBuShangChengBiaoConfig config = new ReFaBuShangChengBiaoConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		
		config.setMaxlevel(CovertObjectUtil.object2int(tmp.get("maxlevel")));
											
		config.setPrice(CovertObjectUtil.object2int(tmp.get("price")));
											
		config.setOrder(CovertObjectUtil.object2int(tmp.get("order")));
											
		config.setSellid(CovertObjectUtil.obj2StrOrNull(tmp.get("sellid")));
											
		config.setModelid(CovertObjectUtil.object2int(tmp.get("modelid")));
											
		config.setShowprice(CovertObjectUtil.object2int(tmp.get("showprice")));
											
		config.setDay(CovertObjectUtil.object2int(tmp.get("day")));
											
		config.setMoneytype(CovertObjectUtil.object2int(tmp.get("moneytype")));
											
		config.setMinlevel(CovertObjectUtil.object2int(tmp.get("minlevel")));
		
		config.setSuperScript(CovertObjectUtil.object2String(tmp.get("superscript")));
		

		
		int count = CovertObjectUtil.object2int(tmp.get("count")) == 0 ? 1 : CovertObjectUtil.object2int(tmp.get("count"));//配置的物品数量，如果为null就默认为1
		
		config.setCount(count);
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	/**
	 * 获取所有商城压缩后的数据
	 * @return
	 */
	public Object shopAllCompress(){
		return COMPRESS_DATA;
	}
	
	public ReFaBuShangChengBiaoConfig loadById(Integer id){
		return refabuConfig.get(id);
	}
	/**
	 * 获取版本号
	 * @return
	 */
	public String getReFaBuSign(){
		return ConfigMd5SignManange.getConfigSignByFileName(FILE_NAME);
	}

//	@Override
	public String getSortName() {
		return FILE_NAME;
	}
}