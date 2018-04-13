package com.junyou.stage.model.element.goods;

import com.junyou.stage.configure.export.impl.ZiYuanConfig;


/**
 * 可采集物
 *@author  DaoZheng Yuan
 *@created 2013-10-26下午3:14:25
 */
public class CollectFacory {

	
	/**
	 * 创建可采集物
	 * @param id
	 * @param configId
	 * @return
	 */
	public static Collect create(Long id ,String teamId,ZiYuanConfig ziYuanConfig){
		Collect collect = new Collect(id,teamId,ziYuanConfig);
		
		return collect;
	}
	/**
	 * 创建探宝宝箱
	 * @param id
	 * @param configId
	 * @return
	 */
	public static TanBaoBox create(Long id ,ZiYuanConfig ziYuanConfig){
		TanBaoBox box = new TanBaoBox(id,"TanBaoBox",ziYuanConfig);
		
		return box;
	}
	
	/**
	 * 创建争霸赛旗帜
	 * @param id
	 * @param configId
	 * @return
	 */
	public static HcZBSQizi createHcZBSQizi(Long id ,ZiYuanConfig ziYuanConfig,String stageId){
		HcZBSQizi qizi = new HcZBSQizi(id,"Hczbs_qizi",ziYuanConfig,stageId);
		
		return qizi;
	}
	/**
	 * 创建领地战旗帜
	 * @param id
	 * @param configId
	 * @return
	 */
	public static TerritoryQizi createTerritoryQizi(Long id ,ZiYuanConfig ziYuanConfig,String stageId){
		TerritoryQizi qizi = new TerritoryQizi(id,"Territory_qizi",ziYuanConfig,stageId);
		
		return qizi;
	}
	
	/**
     * 创建跨服云宫之巅旗帜
     * @param id
     * @param configId
     * @return
     */
    public static KuafuYunGongQizi createKuafuYunGongQizi(Long id ,ZiYuanConfig ziYuanConfig){
        return new KuafuYunGongQizi(id,"KuafuYunGong_qizi",ziYuanConfig);
    }
}
