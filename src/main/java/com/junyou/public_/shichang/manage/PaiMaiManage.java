package com.junyou.public_.shichang.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.bus.bag.entity.RoleItem;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.shichang.entity.PaiMaiInfo;
import com.junyou.public_.shichang.util.PaiMaiUtil;

/**
 * 拍卖缓存管理
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-4-9 上午11:28:27
 */
public class PaiMaiManage {
	
	private static PaiMaiManage paiMaiManage = new PaiMaiManage();
	
	//所有的拍卖
	private List<PaiMai> paimais = new ArrayList<>();
	
	//guid = paimai
	private Map<Long,PaiMai> guidPaiMais = new HashMap<Long, PaiMai>();
	
	//userRoleId = paimai
	private Map<Long,List<PaiMai>> userRoleIdPaiMais = new HashMap<Long, List<PaiMai>>();
	
	
	public static PaiMaiManage getPaiMaiManage(){
		return paiMaiManage;
	}
	
	public List<PaiMai> getPaimais() {
		return paimais;
	}

	public void setPaimais(List<PaiMai> paimais) {
		this.paimais = paimais;
	}

	public Map<Long, PaiMai> getGuidPaiMais() {
		return guidPaiMais;
	}

	public void setGuidPaiMais(Map<Long, PaiMai> guidPaiMais) {
		this.guidPaiMais = guidPaiMais;
	}

	public Map<Long, List<PaiMai>> getUserRoleIdPaiMais() {
		return userRoleIdPaiMais;
	}

	public void setUserRoleIdPaiMais(Map<Long, List<PaiMai>> userRoleIdPaiMais) {
		this.userRoleIdPaiMais = userRoleIdPaiMais;
	} 
	
	public void addPaiMai(PaiMai paimai){
		if(paimai == null) return;
		long userRoleId = paimai.getUserRoleId();
		
		paimais.add(paimai);
		guidPaiMais.put(paimai.getRoleItem().getId(), paimai);
		List<PaiMai> paimais = userRoleIdPaiMais.get(userRoleId);
		if(paimais == null){
			paimais = new ArrayList<>();
			userRoleIdPaiMais.put(userRoleId, paimais);
		}
		paimais.add(paimai);
	}
	
	public PaiMai removePaiMai(long guid){
		PaiMai paimai =	guidPaiMais.remove(guid);
		if(paimai == null) return null;
		
		paimais.remove(paimai);
		long userRoleId = paimai.getUserRoleId();
		List<PaiMai> paimais = userRoleIdPaiMais.get(userRoleId);
		paimais.remove(paimai);
		
		return paimai;
	}
	
	/**
	 * 获得拍卖信息
	 * @param guid
	 * @return
	 */
	public PaiMai getPaiMaiByGuid(long guid) {
		return guidPaiMais.get(guid);
	}

	public List<PaiMai> getPaiMaiByRoleId(Long userRoleId) {
		return userRoleIdPaiMais.get(userRoleId);
	}

	/**
	 * 初始化在线数据
	 * @param roleItems
	 * @param paiMaiInfos
	 */
	public void init(List<RoleItem> roleItems, List<PaiMaiInfo> paiMaiInfos) {
		for (PaiMaiInfo paiMaiInfo : paiMaiInfos) {
			RoleItem ri =null;
			for (RoleItem roleItem : roleItems) {
				if(roleItem.getId().equals(paiMaiInfo.getGuid())){
					ri = roleItem; 
					break;
				}
			}
			if(ri != null){
				PaiMai  paiMai = PaiMaiUtil.coverToPaiMai(paiMaiInfo, ri); 
				addPaiMai(paiMai);
			}else{
				ChuanQiLog.error("启动加载市场数据错误");
			}
		}
	}
}
