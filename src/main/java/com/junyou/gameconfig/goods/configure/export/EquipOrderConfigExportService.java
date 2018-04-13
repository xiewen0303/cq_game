package com.junyou.gameconfig.goods.configure.export;
 
import java.util.HashMap; 
import java.util.Map;
import org.springframework.stereotype.Component; 
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser; 
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;
 

/**
 * 道具类型排序表
  *@author: wind
  *@email: 18221610336@163.com
  *@version: 2014-12-18下午2:43:38
  *@Description:
 */
@Component
public class EquipOrderConfigExportService extends AbsClasspathConfigureParser{
	
	/**
	  * configFileName  TODO
	 */
	private String configureName = "ZhuangBeiPaiXuBiao.jat";
	
	private Map<Integer,Integer> eqpartOrders;
	
	protected void configureDataResolve(byte[] data) {
		if(data == null){
			return;
		}
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer,Integer> eqpartOrders = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				eqpartOrders.put(CovertObjectUtil.object2int(tmp.get("eqpart")),CovertObjectUtil.object2int(tmp.get("order")));
			}
		}
		this.eqpartOrders = eqpartOrders;
	}
	
	protected String getConfigureName() {
		return configureName;
	} 
 
	
	public Integer getEqpartOrders(Integer eqpart){
		return eqpartOrders.get(eqpart);
	}
}