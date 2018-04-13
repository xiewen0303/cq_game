package com.junyou.bus.tanbao.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;


/**
 * @author LiuYu
 * 2015-6-17 下午10:15:14
 */
@Service
public class TanBaoPaiMingJiangLiConfigExportService extends AbsClasspathConfigureParser{
	/**
	 * 配置名
	 */
	private String configureName = "DuoBaoPaiMingJiangLi.jat";
	
	private Map<Integer, String> configs;
	
	private int maxRank;
	private int puGift = -1;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] listData = GameConfigUtil.getResource(data);
		Map<Integer, String> configs = new HashMap<>();
		int rank = 1;
		for (Object obj:listData) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			int id = CovertObjectUtil.object2int(tmp.get("id"));
			String giftStr = CovertObjectUtil.obj2StrOrNull(tmp.get("jiangitem"));
			if(id < 0){
				configs.put(puGift, giftStr);
			}else{
				int max = CovertObjectUtil.object2int(tmp.get("max"));
				for (; rank <= max; rank++) {
					configs.put(rank, giftStr);
				}
			}
		}
		this.configs = configs;
		this.maxRank = rank;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public String getGift(int rank){
		if(rank >= maxRank){
			rank = puGift;
		}
		return configs.get(rank);
	}
	
	
}
