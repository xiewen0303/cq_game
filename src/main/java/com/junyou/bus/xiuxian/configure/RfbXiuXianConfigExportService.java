package com.junyou.bus.xiuxian.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.rfbactivity.configure.export.AbstractRfbConfigService;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.configure.export.IRfbConfigTemplateService;
import com.junyou.constants.GameConstants;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.json.JsonUtils;
import com.junyou.utils.md5.Md5Utils;
import com.junyou.utils.md5.ThreeDesEncryptAndEncrypt;

/**
 * 修仙礼包
 * @author DaoZheng Yuan
 * 2015年6月7日 下午2:29:11
 */
public class RfbXiuXianConfigExportService extends AbstractRfbConfigService {

	private static final RfbXiuXianConfigExportService INSTANCE = new RfbXiuXianConfigExportService();
	
	/**
	 * key:子活动id
	 * value:子活动数据
	 */
	private Map<Integer,XiuXianGroupConfig> XIUXIAN = new HashMap<>();
	
	//背景图类型id
	private int BG_TYPE_ID = -1;
	
	
	/**
	 * 私有构造方法(不允许外面创建实例)
	 */
	private RfbXiuXianConfigExportService(){
	}
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static RfbXiuXianConfigExportService getInstance() {
		return INSTANCE;
	}
	
	public Map<Integer,XiuXianGroupConfig> loadAll(){
		return XIUXIAN;
	}
	
	public XiuXianConfig loadById(String subId,Integer index){
		XiuXianGroupConfig config = XIUXIAN.get(subId);
		if(config != null){
			return config.getXiuXianConfigById(index);
		}
		
		return null;
	}
	
	public XiuXianGroupConfig loadById(int subId){
		return XIUXIAN.get(subId);
	}
	
	@Override
	public void analysisConfigureDataResolve(int subId, byte[] data) {
		if(data == null){
			ChuanQiLog.error(" xiuxian 1 data is error! ");
		}
		
		byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
		JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
		if(json == null || json.isEmpty()){
			ChuanQiLog.error(" xiuxian 2 data is error! ");
			return;
		}
		
		//版本号比对
		String md5Value = Md5Utils.md5Bytes(data);
		XiuXianGroupConfig group = XIUXIAN.get(subId);
		if(group != null){
			
			if(md5Value.equals(group.getMd5Version())){
				//版本号一致，不处理下面的业务直接跳出
				ChuanQiLog.error(" ShouChong subid={} version is same md5Value={}",subId,md5Value);
				return;
			}
		}
		
		XiuXianGroupConfig newGroup = new XiuXianGroupConfig();
		newGroup.setMd5Version(md5Value);
		
		//处理子活动的版本号
		ActivityConfigSon configSon = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		if(configSon != null){
			configSon.setClientVersion(configSon.getClientVersion() + 1);
		}
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();  
		for(int i=0;i<json.size();i++){  
			JSONArray json1 = json.getJSONArray(i);
			Map<String,Object> map = new HashMap<String, Object>();
			for (int j = 0; j < json1.size(); j++) {
				@SuppressWarnings("unchecked")
				Map<String,Object> tmpObjMap = (Map<String, Object>)JSONObject.parse(json1.getString(j));
				map.putAll(tmpObjMap);
			}
			list.add(map);
		}  
		
		for (Map<String,Object> map : list) {
			if (null != map) {
				XiuXianConfig config = createXiuXianConfig(map);
				if(BG_TYPE_ID == config.getId()){
					//背景图处理-解析配置里，-1类型的id,goodsId里存的是背景图名称 
					newGroup.setBgImageName(config.getGoodsId());
				}else{
					newGroup.addXiuXianConfig(config);
					newGroup.addLbIds(config.getId());
				}
			}
		}
		
		//加入管理
		XIUXIAN.put(subId, newGroup);
	}
	
	private XiuXianConfig createXiuXianConfig(Map<String, Object> tmp) {
		XiuXianConfig config = new XiuXianConfig();
		int id = CovertObjectUtil.object2int(tmp.get("id"));
		config.setId(id);
		config.setMoneyType(CovertObjectUtil.object2int(tmp.get("moneytype")));
        String goods_str = CovertObjectUtil.object2String(tmp.get("sellid"));
        if (!ObjectUtil.strIsEmpty(goods_str)) {
            if(BG_TYPE_ID == id){
                config.setGoodsId(goods_str);
            }else{
                String[] goods = goods_str.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
                int len = null == goods ? 0 : goods.length;
                int initNum = 1;
                if(len > 1){
                    initNum = Integer.parseInt(goods[1]);
                }
                config.setGoodsId(goods[0]);
                config.setGoodsNum(initNum);
            }
        }
		int price = CovertObjectUtil.object2int(tmp.get("price"));
		if(price <= 0){
			//容错处理,防止运营配置错了，价格放大就是不让买
			price = 99999999;
		}
		config.setNeedValue(price);
		config.setShowNeedValue(CovertObjectUtil.object2int(tmp.get("showprice")));
		config.setRoleMaxCount(CovertObjectUtil.object2int(tmp.get("danren")));
		config.setServerMaxCount(CovertObjectUtil.object2int(tmp.get("quanfu")));
		
		return config;
	}

	@Override
	public Object getChildData(int subId) {
		XiuXianGroupConfig  subCfg = XIUXIAN.get(subId);
		return subCfg != null ?  subCfg.getBgImageName() : "";
	}
}
