package com.junyou.gameconfig.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.export.ZuBaoConfig.ZuBaoOdds;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.lottery.Lottery;

/**
 * 
 * @description 
 *
 * @author LiuJuan
 * @date 2012-3-16 下午3:40:46
 */
@Component
public class ZuBaoConfigExportService extends AbsClasspathConfigureParser{

	/**
	 * 配置名
	 */
	private String configureName = "WuPinZuBao.jat";
	
	private Map<String, ZuBaoConfig> configs = new HashMap<>();
	
	private static String split_str = ":";

	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj:dataList) {
			Map<Object, Object> tmp = (Map<Object, Object>)obj;
			if (null != tmp) {
				ZuBaoConfig zuBaoConfig = new ZuBaoConfig();
				zuBaoConfig.setDropId(CovertObjectUtil.object2String(tmp.get("dropNumber")));
				List<ZuBaoOdds> list = new ArrayList<ZuBaoOdds>();
				for (int i = 0; i < 76; i++) {
					String drop = CovertObjectUtil.object2String(tmp.get("drop"+i));
					if (null != drop && !"".equals(drop)) {
						String[] str = drop.split(split_str);
						String goodsId = CovertObjectUtil.object2String(str[0]);
						int goodsCount = 1;
						if (str.length > 1) {
							goodsCount = CovertObjectUtil.object2int(str[1]);
						}
						float rate = CovertObjectUtil.object2Float(tmp.get("drop"+i+"Odds"));
						ZuBaoOdds zubaoOdds = zuBaoConfig.newZuBaoConfig(goodsId, goodsCount, rate);
						list.add(zubaoOdds);
					}
				}
				if (list.size() > 0) {					
					zuBaoConfig.setZubaoOdds(list);
				}
				configs.put(zuBaoConfig.getDropId(),zuBaoConfig);
			}
		}
		
	}

	protected String getConfigureName() {
		
		return configureName;
	}

	public ZuBaoConfig loadById(String id) {
		
		return configs.get(id);
	}
	
	
	public Object[] componentRoll(String componentDropId) {
		ZuBaoConfig zuBaoConfig = loadById(componentDropId);
		if(null == zuBaoConfig){
			return null;
		}
		
		Map<ZuBaoOdds, Float> goodsMap = new HashMap<ZuBaoOdds, Float>();
		List<ZuBaoOdds> zubaoOddsList = zuBaoConfig.getZubaoOdds();
		for (int i = 0; i < zubaoOddsList.size(); i++) {
			
			ZuBaoOdds zuBaoOdds = zubaoOddsList.get(i);
			
			goodsMap.put(zuBaoOdds, zuBaoOdds.getRate());
		}
		ZuBaoOdds zuBaoOdds = Lottery.getRandomKey2(goodsMap);
		
		if(zuBaoOdds.getGoodsId().startsWith("@")){
			return componentRoll(zuBaoOdds.getGoodsId());
		}
		
		return new Object[]{zuBaoOdds.getGoodsId(), zuBaoOdds.getGoodsCount()};
	}
	
	
//	public Map<String, Integer> componentMapRoll(String componentDropId) {
//		ZuBaoConfig zuBaoConfig = loadById(componentDropId);
//		if(null == zuBaoConfig){
//			return null;
//		}
//		
//		Map<ZuBaoOdds, Float> goodsMap = new HashMap<ZuBaoOdds, Float>();
//		List<ZuBaoOdds> zubaoOddsList = zuBaoConfig.getZubaoOdds();
//		for (int i = 0; i < zubaoOddsList.size(); i++) {
//			
//			ZuBaoOdds zuBaoOdds = zubaoOddsList.get(i);
//			
//			goodsMap.put(zuBaoOdds, zuBaoOdds.getRate());
//		}
//		ZuBaoOdds zuBaoOdds = Lottery.getRandomKey2(goodsMap);
//		
//		if(zuBaoOdds.getGoodsId().startsWith("@")){
//			return componentMapRoll(zuBaoOdds.getGoodsId());
//		}
//		
//		return new Object[]{zuBaoOdds.getGoodsId(), zuBaoOdds.getGoodsCount()};
//	}
	
	
	
}
