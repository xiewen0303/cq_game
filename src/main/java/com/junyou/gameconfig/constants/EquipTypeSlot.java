package com.junyou.gameconfig.constants;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 身上装备格位号
 * @description 
 * @author DaoZheng Yuan
 * @created 2011-6-14上午10:36:34
 */
public class EquipTypeSlot {
	
	/**
	 * 装备之武器
	 */
	public static final int ARMS_SLOT = -1;
	/**
	 * 装备之项链 [颈部]
	 */
	public static final int NECKLACE_SLOT = -2;
	
	/**
	 * 裤子
	 */
	public static final int PANTS_SLOT = -3; 
	
	/**
	 * 护腕
	 */
	public static final int HUWAN_SLOT = -4; 
	
	/**
	 * 装备之鞋子
	 */
	public static final int FOOT_SLOT = -5;
	
	/**
	 * 勋章
	 */
	public static final int MEDAL_SLOT = -6;
	
	/**
	 * 装备之头盔
	 */
	public static final int HEAD_SLOT = -7;
	
	/**
	 * 装备之上衣
	 */
	public static final int CHEST_SLOT = -8;
	 
	/**
	 * 腰带
	 */
	public static final int YAODAI_SLOT = -9;  
	/**
	 * 玉佩
	 */
	public static final int YUPEI_SLOT = -10;   
	/**
	 * 装备之戒指
	 */
	public static final int RING_SLOT = -11; 
	/**
	 * 徽章
	 */
	public static final int HUIZHANG_SLOT = -12;
	/***
	 * 神武类装备
	 */
	public static final int SHENGWU_BING_SLOT = -20;
	public static final int SHENGWU_KUI_SLOT = -21;
	public static final int SHENGWU_KAI_SLOT = -22;
	public static final int SHENGWU_YU_SLOT = -23;
	public static final int SHENGWU_HU_SLOT = -24;
	public static final int SHENGWU_SU_SLOT = -25;
	public static final int SHENGWU_HUAN_SLOT = -26;
	public static final int SHENGWU_JIE_SLOT = -27;
	//----------------------御剑------------------------
	/**剑鞘*/
	public static final int YJJQ_SLOT = -101;
	/**剑穗*/
	public static final int YJJS_SLOT = -102;
	/**剑匣*/
	public static final int YJJX_SLOT = -103;
	/**剑珥*/
	public static final int YJJE_SLOT = -104;
	
	//----------------------翅膀------------------------
	/**翼铛*/
	public static final int CBYD_SLOT = -111;
	/**翼珠*/
	public static final int CBYZ_SLOT = -112;
	/**翼辉*/
	public static final int CBYHui_SLOT = -113;
	/**翼环*/
	public static final int CBYHuan_SLOT = -114;
	
	//----------------------糖宝------------------------
	/**仙灯*/
	public static final int TBXD_SLOT = -121;
	/**仙枝*/
	public static final int TBXZ_SLOT = -122;
	/**仙镜*/
	public static final int TBXJ_SLOT = -123;
	/**仙铃*/
	public static final int TBXL_SLOT = -124;
	
	//----------------------天工------------------------
	/**宝器*/
	public static final int TGBQ_SLOT = -131;
	/**宝缨*/
	public static final int TGBY_SLOT = -132;
	/**宝绶*/
	public static final int TGBS_SLOT = -133;
	/**宝鳞*/
	public static final int TGBL_SLOT = -134;
	
	//----------------------天裳------------------------
	/**锦珞*/
	public static final int TSJL_SLOT = -141;
	/**锦缎*/
	public static final int TSJD_SLOT = -142;
	/**锦花*/
	public static final int TSJH_SLOT = -143;
	/**锦玉*/
	public static final int TSJY_SLOT = -144;
	
	//----------------------器灵------------------------
	/**锦珞*/
	public static final int QLJL_SLOT = -151;
	/**锦缎*/
	public static final int QLJD_SLOT = -152;
	/**锦花*/
	public static final int QLJH_SLOT = -153;
	/**锦玉*/
	public static final int QLJY_SLOT = -154;
	//----------------------天羽------------------------
	/**锦珞*/
	public static final int TYJL_SLOT = -161;
	/**锦缎*/
	public static final int TYJD_SLOT = -162;
	/**锦花*/
	public static final int TYJH_SLOT = -163;
	/**锦玉*/
	public static final int TYJY_SLOT = -164;
	//--------------------宠物-----------------------
    /**宠物装备左上格位灵爪*/
    public static final int CWLZ_SLOT = -171;
    /**宠物装备左下格位灵缰*/
    public static final int CWLJ_SLOT = -172;
    /**宠物装备右上格位灵佩*/
    public static final int CWLP_SLOT = -173;
    /**宠物装备有右下格位灵护*/
    public static final int CWLH_SLOT = -174;
    
    //--------------------神器-----------------------
    /**神器装备左*/
    public static final int SQLZ_SLOT = -181;
    /**礼品装备右*/
    public static final int SQLP_SLOT = -182;
    
    //--------------------新圣剑-----------------------
    public static final int WQ_1_SLOT = -183;
    public static final int WQ_2_SLOT = -184;
    public static final int WQ_3_SLOT = -185;
    public static final int WQ_4_SLOT = -186;
	 
	

	//装备槽位
	private static Map<Integer,Integer> slotTypeMap = new HashMap<Integer,Integer>();
	
	//时装槽位
	private static Map<Integer,Integer> szSlotTypeMap = new HashMap<Integer,Integer>();
	
	//坐骑装备槽位
	private static Map<Integer,Integer> zqSlotTypeMap = new HashMap<Integer,Integer>();
	//翅膀装备槽位
	private static Map<Integer,Integer> cbSlotTypeMap = new HashMap<Integer,Integer>();
	//糖宝装备槽位
	private static Map<Integer,Integer> tbSlotTypeMap = new HashMap<Integer,Integer>();
	//天工装备槽位
	private static Map<Integer,Integer> tgSlotTypeMap = new HashMap<Integer,Integer>();
	//天裳装备槽位
	private static Map<Integer,Integer> tsSlotTypeMap = new HashMap<Integer,Integer>();
	//器灵装备槽位
	private static Map<Integer,Integer> qlSlotTypeMap = new HashMap<Integer,Integer>();
	//天羽装备槽位
	private static Map<Integer,Integer> tySlotTypeMap = new HashMap<Integer,Integer>();
	//宠物装备槽位
	private static Map<Integer,Integer> cwSlotTypeMap = new HashMap<Integer,Integer>();
	
	//全身强化统计格位
	private static Map<Integer,Integer> qsqhSlotTypeMap = new HashMap<Integer,Integer>();
	
    // 神武装备槽位
    private static Map<Integer,Integer> swSlotTypeMap = new HashMap<Integer, Integer>();
    
    //宠物装备槽位
  	private static Map<Integer,Integer> sqSlotTypeMap = new HashMap<Integer,Integer>();
  	//新圣剑装备槽位
  	private static Map<Integer,Integer> wqSlotTypeMap = new HashMap<Integer,Integer>();
  	
	static {
		/**
		 * -------装备------
		 * 武器
		 * 头
		 * 衣服
		 * 鞋子
		 * 戒指
		 * 项链
		 * -------装备------
		 */
		//武器
		slotTypeMap.put(ARMS_SLOT, EquipType.DETAIL_TYPE_ARMS);
		//头
		slotTypeMap.put(HEAD_SLOT, EquipType.DETAIL_TYPE_HEAD);
		//上衣
		slotTypeMap.put(CHEST_SLOT, EquipType.DETAIL_TYPE_CHEST);
		//鞋子
		slotTypeMap.put(FOOT_SLOT, EquipType.DETAIL_TYPE_FOOT);
		//戒指
		slotTypeMap.put(RING_SLOT, EquipType.DETAIL_TYPE_RING);
		//项链
		slotTypeMap.put(NECKLACE_SLOT, EquipType.DETAIL_TYPE_NECKLACE); 
		//勋章
		slotTypeMap.put(MEDAL_SLOT, EquipType.DETAIL_TYPE_MEDAL); 
		//裤子
		slotTypeMap.put(PANTS_SLOT, EquipType.DETAIL_TYPE_PANTS); 
		//徽章
		slotTypeMap.put(HUIZHANG_SLOT, EquipType.DETAIL_TYPE_HUIZHANG);
		//护腕
		slotTypeMap.put(HUWAN_SLOT, EquipType.DETAIL_TYPE_HUWAN); 
		//腰带
		slotTypeMap.put(YAODAI_SLOT, EquipType.DETAIL_TYPE_YAODAI);
		//玉佩
		slotTypeMap.put(YUPEI_SLOT, EquipType.DETAIL_TYPE_YUPEI);
		//神武类
		// ----------------------------神武装备格位--------------------------
        //神武*兵=-20（神武）
        swSlotTypeMap.put(SHENGWU_BING_SLOT, EquipType.DETAIL_TYPE_SHENGWU_BING);
        //神武*盔=-21（神武）
        swSlotTypeMap.put(SHENGWU_KUI_SLOT, EquipType.DETAIL_TYPE_SHENGWU_KUI);
        //神武*铠=-22（神武）
        swSlotTypeMap.put(SHENGWU_KAI_SLOT, EquipType.DETAIL_TYPE_SHENGWU_KAI);
        //神武*御=-23（神武）
        swSlotTypeMap.put(SHENGWU_YU_SLOT, EquipType.DETAIL_TYPE_SHENGWU_YU);
        //神武*护=-24（神武）
        swSlotTypeMap.put(SHENGWU_HU_SLOT, EquipType.DETAIL_TYPE_SHENGWU_HU);
        //神武*速=-25（神武）
        swSlotTypeMap.put(SHENGWU_SU_SLOT, EquipType.DETAIL_TYPE_SHENGWU_SU);
        //神武*环=-26（神武）
        swSlotTypeMap.put(SHENGWU_HUAN_SLOT, EquipType.DETAIL_TYPE_SHENGWU_HUAN);
        // 神武*戒=-27（神武）
        swSlotTypeMap.put(SHENGWU_JIE_SLOT, EquipType.DETAIL_TYPE_SHENGWU_JIE);
        slotTypeMap.putAll(swSlotTypeMap);
		
		// ----------------------------全身强化统计格位----------------------------------
		qsqhSlotTypeMap.put(ARMS_SLOT, EquipType.DETAIL_TYPE_ARMS);
		//头
		qsqhSlotTypeMap.put(HEAD_SLOT, EquipType.DETAIL_TYPE_HEAD);
		//上衣
		qsqhSlotTypeMap.put(CHEST_SLOT, EquipType.DETAIL_TYPE_CHEST);
		//鞋子
		qsqhSlotTypeMap.put(FOOT_SLOT, EquipType.DETAIL_TYPE_FOOT);
		//戒指
		qsqhSlotTypeMap.put(RING_SLOT, EquipType.DETAIL_TYPE_RING);
		//项链
		qsqhSlotTypeMap.put(NECKLACE_SLOT, EquipType.DETAIL_TYPE_NECKLACE);  
		//裤子
		qsqhSlotTypeMap.put(PANTS_SLOT, EquipType.DETAIL_TYPE_PANTS);  
		//护腕
		qsqhSlotTypeMap.put(HUWAN_SLOT, EquipType.DETAIL_TYPE_HUWAN); 
		//腰带
		qsqhSlotTypeMap.put(YAODAI_SLOT, EquipType.DETAIL_TYPE_YAODAI);
		//腰带
		qsqhSlotTypeMap.put(YUPEI_SLOT, EquipType.DETAIL_TYPE_YUPEI);
		
		
		
		// ----------------------------坐骑装备----------------------------------
		zqSlotTypeMap.put(YJJQ_SLOT, EquipType.DETAIL_TYPE_YJJQ);
		zqSlotTypeMap.put(YJJS_SLOT, EquipType.DETAIL_TYPE_YJJS);
		zqSlotTypeMap.put(YJJX_SLOT, EquipType.DETAIL_TYPE_YJJX);
		zqSlotTypeMap.put(YJJE_SLOT, EquipType.DETAIL_TYPE_YJJE);
		// ----------------------------翅膀装备----------------------------------
		cbSlotTypeMap.put(CBYD_SLOT, EquipType.DETAIL_TYPE_CBYD);
		cbSlotTypeMap.put(CBYZ_SLOT, EquipType.DETAIL_TYPE_CBYZ);
		cbSlotTypeMap.put(CBYHui_SLOT, EquipType.DETAIL_TYPE_CBYHui);
		cbSlotTypeMap.put(CBYHuan_SLOT, EquipType.DETAIL_TYPE_CBYHuan);
		// ----------------------------糖宝装备----------------------------------
		tbSlotTypeMap.put(TBXD_SLOT, EquipType.DETAIL_TYPE_TBXD);
		tbSlotTypeMap.put(TBXZ_SLOT, EquipType.DETAIL_TYPE_TBXZ);
		tbSlotTypeMap.put(TBXJ_SLOT, EquipType.DETAIL_TYPE_TBXJ);
		tbSlotTypeMap.put(TBXL_SLOT, EquipType.DETAIL_TYPE_TBXL);
		// ----------------------------天工装备----------------------------------
		tgSlotTypeMap.put(TGBQ_SLOT, EquipType.DETAIL_TYPE_TGBQ);
		tgSlotTypeMap.put(TGBY_SLOT, EquipType.DETAIL_TYPE_TGBY);
		tgSlotTypeMap.put(TGBS_SLOT, EquipType.DETAIL_TYPE_TGBS);
		tgSlotTypeMap.put(TGBL_SLOT, EquipType.DETAIL_TYPE_TGBL);
		// ----------------------------天裳装备----------------------------------
		tsSlotTypeMap.put(TSJL_SLOT, EquipType.DETAIL_TYPE_TSJL);
		tsSlotTypeMap.put(TSJD_SLOT, EquipType.DETAIL_TYPE_TSJD);
		tsSlotTypeMap.put(TSJH_SLOT, EquipType.DETAIL_TYPE_TSJH);
		tsSlotTypeMap.put(TSJY_SLOT, EquipType.DETAIL_TYPE_TSJY);
		// ----------------------------器灵装备----------------------------------
		qlSlotTypeMap.put(QLJL_SLOT, EquipType.DETAIL_TYPE_QLJL);
		qlSlotTypeMap.put(QLJD_SLOT, EquipType.DETAIL_TYPE_QLJD);
		qlSlotTypeMap.put(QLJH_SLOT, EquipType.DETAIL_TYPE_QLJH);
		qlSlotTypeMap.put(QLJY_SLOT, EquipType.DETAIL_TYPE_QLJY);
		// ----------------------------天羽装备----------------------------------
		tySlotTypeMap.put(TYJL_SLOT, EquipType.DETAIL_TYPE_TYJL);
		tySlotTypeMap.put(TYJD_SLOT, EquipType.DETAIL_TYPE_TYJD);
		tySlotTypeMap.put(TYJH_SLOT, EquipType.DETAIL_TYPE_TYJH);
		tySlotTypeMap.put(TYJY_SLOT, EquipType.DETAIL_TYPE_TYJY);
		// ----------------------------宠物装备----------------------------------
		cwSlotTypeMap.put(CWLZ_SLOT, EquipType.DETAIL_TYPE_CWLZ);
		cwSlotTypeMap.put(CWLJ_SLOT, EquipType.DETAIL_TYPE_CWLJ);
		cwSlotTypeMap.put(CWLP_SLOT, EquipType.DETAIL_TYPE_CWLP);
		cwSlotTypeMap.put(CWLH_SLOT, EquipType.DETAIL_TYPE_CWLH);
		// ----------------------------宠物装备----------------------------------
		sqSlotTypeMap.put(SQLZ_SLOT, EquipType.DETAIL_TYPE_SQLZ);
		sqSlotTypeMap.put(SQLP_SLOT, EquipType.DETAIL_TYPE_SQLJ);
		
		// ----------------------------圣剑装备----------------------------------
		wqSlotTypeMap.put(WQ_1_SLOT, EquipType.DETAIL_TYPE_WQ1);
		wqSlotTypeMap.put(WQ_2_SLOT, EquipType.DETAIL_TYPE_WQ2);
		wqSlotTypeMap.put(WQ_3_SLOT, EquipType.DETAIL_TYPE_WQ3);
		wqSlotTypeMap.put(WQ_4_SLOT, EquipType.DETAIL_TYPE_WQ4);
		/**
		 * -------时装------
		 *  翅膀[时装-披风]=43 
			护肩[时装-衣服]=41 
			发型[时装-头饰]=56 
			下装[时装-武器]=71 
		 */
		/*//披风[时装-披风]
		SZSlotTypeMap.put(DORSUM_SLOT, EquipType.DETAIL_TYPE_DORSUM);
		//时装-衣服
		SZSlotTypeMap.put(SHOULDER_SLOT, EquipType.DETAIL_TYPE_SHOULDER);
		//时装-头饰
		SZSlotTypeMap.put(HAIR_TYPE_SLOT, EquipType.DETAIL_TYPE_HAIR);
		//时装-武器
		SZSlotTypeMap.put(LEG_SLOT, EquipType.DETAIL_TYPE_LEG);*/
       
    }
	 
	
	/**
	 * 根据格位获取物品明细类型
	 * @param slotNum
	 * @return
	 */
	public static Integer getEquipPartBySlotNum(int slotNum){
		//基本装备
		Integer type = slotTypeMap.get(slotNum);
		if(type != null) return type;
		 
		//坐骑身上的装备
		type = zqSlotTypeMap.get(slotNum);
		if(type != null)return type;
		//翅膀身上的装备
		type = cbSlotTypeMap.get(slotNum);
		if(type != null)return type;
		//糖宝身上的装备
		type = tbSlotTypeMap.get(slotNum);
		if(type != null)return type;
		//天工身上的装备
		type = tgSlotTypeMap.get(slotNum);
		if(type != null)return type;
		//天裳身上的装备
		type = tsSlotTypeMap.get(slotNum);
		if(type != null)return type;
		//器灵身上的装备
		type = qlSlotTypeMap.get(slotNum);
		if(type != null)return type;
		//器灵身上的装备
		type = tySlotTypeMap.get(slotNum);
		if(type != null)return type;
		//宠物身上的装备
		type = cwSlotTypeMap.get(slotNum);
		if(type != null)return type;
		
		//神器身上的装备
		type = sqSlotTypeMap.get(slotNum);
		if(type != null)return type;
		
		//时装
		type = szSlotTypeMap.get(slotNum);
		if(type != null) return type;
		
		//新圣剑
		type = wqSlotTypeMap.get(slotNum);
		if(type != null) return type;
		
		return type;
	}
	
	public static boolean isEquip(int slotNum){
		if(slotTypeMap.containsKey(slotNum)){
			return true;
		}
		if(zqSlotTypeMap.containsKey(slotNum)){
			return true;
		}
		if(cbSlotTypeMap.containsKey(slotNum)){
			return true;
		}
		if(tbSlotTypeMap.containsKey(slotNum)){
			return true;
		}
		if(tgSlotTypeMap.containsKey(slotNum)){
			return true;
		}
		if(tsSlotTypeMap.containsKey(slotNum)){
			return true;
		}
		if(qlSlotTypeMap.containsKey(slotNum)){
			return true;
		}
		if(tySlotTypeMap.containsKey(slotNum)){
			return true;
		}
		if(cwSlotTypeMap.containsKey(slotNum)){
		    return true;
		}
		if(sqSlotTypeMap.containsKey(slotNum)){
		    return true;
		}
		if(wqSlotTypeMap.containsKey(slotNum)){
		    return true;
		}
		return false;
	}
	
	/**
	 * 是否是坐骑装备
	 * @param slotNum
	 * @return
	 */
	public static boolean isZuoQiEquip(int slotNum){
		return zqSlotTypeMap.containsKey(slotNum);
	}	
	 
	
	/**
	 * 是否是翅膀装备
	 * @param slotNum
	 * @return
	 */
	public static boolean isChiBangEquip(int slotNum){
		return cbSlotTypeMap.containsKey(slotNum);
	}
	
	/**
	 * 是否是糖宝装备
	 * @param slotNum
	 * @return
	 */
	public static boolean isTangBaoEquip(int slotNum){
		return tbSlotTypeMap.containsKey(slotNum);
	}
	
	/**
	 * 是否是天工装备
	 * @param slotNum
	 * @return
	 */
	public static boolean isTianGongEquip(int slotNum){
		return tgSlotTypeMap.containsKey(slotNum);
	}
	
	/**
	 * 是否是天裳装备
	 * @param slotNum
	 * @return
	 */
	public static boolean isTianShangEquip(int slotNum){
		return tsSlotTypeMap.containsKey(slotNum);
	}
	
	/**
	 * 是否是器灵装备
	 * @param slotNum
	 * @return
	 */
	public static boolean isQiLingEquip(int slotNum){
		return qlSlotTypeMap.containsKey(slotNum);
	}
	
	/**
	 * 是否是天羽装备
	 * @param slotNum
	 * @return
	 */
	public static boolean isTianYuEquip(int slotNum){
		return tySlotTypeMap.containsKey(slotNum);
	}
	
	/**
     * 是否是宠物装备
     * @param targetSlot
     * @return
     */
    public static boolean isChongWuEquip(Integer slotNum) {
        return cwSlotTypeMap.containsKey(slotNum);
    }

	/**
	 * 	是否是人物身上装备
	 * @param targetSlot
	 * @return
	 */
	public static boolean isBodyEquip(Integer targetSlot) {
		return slotTypeMap.containsKey(targetSlot);
	}
	
	/**
	 * 全身强化格位
	 * @return
	 */
	public static Collection<Integer> getQSQHSlots(){
		return qsqhSlotTypeMap.keySet();
	}
	
    /**
     * 全身神武强化格位
     * 
     * @return
     */
    public static Collection<Integer> getSwSlots() {
        return swSlotTypeMap.keySet();
    }
    
    /**
     *  是否是神武装备
     * @param targetSlot
     * @return
     */
    public static boolean isSwEquip(Integer targetSlot) {
        return swSlotTypeMap.containsKey(targetSlot);
    }

    /**
     * 是否是神器装备
     * @param targetSlot
     * @return
     */
    public static boolean isShenQiEquip(Integer slotNum) {
        return sqSlotTypeMap.containsKey(slotNum);
    }
    /**
	 * 是否是圣剑装备
	 * @param slotNum
	 * @return
	 */
	public static boolean isWuQiEquip(int slotNum){
		return wqSlotTypeMap.containsKey(slotNum);
	}
 
}
