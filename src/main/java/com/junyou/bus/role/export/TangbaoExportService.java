package com.junyou.bus.role.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.role.entity.Tangbao;
import com.junyou.bus.role.service.TangbaoService;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;

/**
 * @author LiuYu
 * 2015-5-1 下午5:09:34
 */
@Service
public class TangbaoExportService {
	@Autowired
	private TangbaoService tangbaoService;
	
	/**
	 * 初始化糖宝数据
	 * @param userRoleId
	 * @return
	 */
	public List<Tangbao> initTangbao(Long userRoleId){
		return tangbaoService.initTangbao(userRoleId);
	}
	
	/**
	 * 上线业务
	 * @param userRoleId
	 */
	public void onlineHandle(Long userRoleId){
		tangbaoService.onlineHandle(userRoleId);
	}

	/**
	 * 获取糖宝的属性
	 * @param userRoleId
	 * @param level
	 * @return
	 */
	public Map<String,Long> getTangbaoBaseAttibute(Long userRoleId,Integer level){
		return tangbaoService.getTangbaoBaseAttribute(userRoleId, level);
	}
	/**
	 * 获取糖宝状态（0：未开启或已激活，1：激活中）
	 * @param userRoleId
	 * @return
	 */
	public int getTangbaoState(Long userRoleId){
		return tangbaoService.getTangbaoState(userRoleId);
	}

	/**
	 * 获取糖宝给主人增加的属性
	 * @param userRoleId
	 * @return
	 */
	public Map<String,Long> getTangbaoRoleAttribute(Long userRoleId){
		return tangbaoService.getTangbaoRoleAttribute(userRoleId);
	}
	
	/**
	 * 使用资质丹
	 * @param userRoleId
	 * @param count
	 * @return
	 */
	public Object[] eatZizhiDan(Long userRoleId,Integer[] consume){
		return tangbaoService.eatZizhiDan(userRoleId, consume);
	}
	
	/**
	 * 使用眉心之血
	 * @param userRoleId
	 * @param count
	 * @return
	 */
	public Object[] eatMeiXinZhiXue(Long userRoleId,Integer[] consume){
		return tangbaoService.eatMeiXinZhiXue(userRoleId, consume);
	}
	/**
	 * 使用菩提
	 * @param userRoleId
	 * @param count
	 * @param goodsId
	 * @return
	 */
	public Object[] eatPuTi(Long userRoleId,Integer[] consume,GoodsConfig goodsConfig){
		return tangbaoService.eatPuTi(userRoleId,consume,goodsConfig);
	}
	
	/**
	 * 激活糖宝
	 * @param userRoleId
	 */
	public void activeTangbao(Long userRoleId){
		tangbaoService.activeTangbao(userRoleId);
	}
	
	/**
	 * 是否有糖宝
	 * @param userRoleId
	 * @return
	 */
	public boolean hasTangbao(Long userRoleId){
		return tangbaoService.hasTangbao(userRoleId);
	}
}
