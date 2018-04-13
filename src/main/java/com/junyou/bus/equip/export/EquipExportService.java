package com.junyou.bus.equip.export;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.ContainerType;
import com.junyou.bus.equip.configure.export.ChongwuFushuEquipConfigExportService;
import com.junyou.bus.equip.configure.export.FuShuQiangHuaBiaoConfigExportService;
import com.junyou.bus.equip.configure.export.QiangHuaBiaoConfigExportService;
import com.junyou.bus.equip.configure.export.ShenWuShuXingJiaChengBiaoConfig;
import com.junyou.bus.equip.configure.export.ShenWuShuXingJiaChengBiaoConfigExportService;
import com.junyou.bus.equip.configure.export.ShenWuXingZhuBiaoConfigExportService;
import com.junyou.bus.equip.configure.export.SuiJiShuXingConfigExportService;
import com.junyou.bus.equip.configure.export.TaoZhuangBiaoConfigExportService;
import com.junyou.bus.equip.configure.export.TaoZhuangZhuShenConfig;
import com.junyou.bus.equip.configure.export.TaoZhuangZhuShenConfigExportService;
import com.junyou.bus.equip.configure.export.ZhanLiXiShuConfigExportService;
import com.junyou.bus.equip.configure.export.ZhuangBeiQiangHuaJiaChengExportService;
import com.junyou.bus.equip.service.EquipService;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.constants.EquipTypeSlot;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.goods.configure.export.helper.BusConfigureHelper;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.element.role.business.Equip;
import com.junyou.utils.common.ObjectUtil;
 

@Service
public class EquipExportService {
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private ZhuangBeiQiangHuaJiaChengExportService zhuangBeiQiangHuaJiaChengExportService;
	@Autowired
	private EquipService equipService;
	@Autowired 
	private QiangHuaBiaoConfigExportService qiangHuaBiaoConfigExportService;
	@Autowired 
	private FuShuQiangHuaBiaoConfigExportService fuShuQiangHuaBiaoConfigExportService;
	@Autowired
	private SuiJiShuXingConfigExportService suiJiShuXingConfigExportService;
	@Autowired
	private TaoZhuangBiaoConfigExportService taoZhuangBiaoConfigExportService; 
	@Autowired
	private TaoZhuangZhuShenConfigExportService taoZhuangZhuShenConfigExportService;
	@Autowired 
	private ZhanLiXiShuConfigExportService zhanLiXiShuConfigExportService;
    @Autowired
    private ShenWuXingZhuBiaoConfigExportService shenWuXingZhuBiaoConfigExportService;
    @Autowired
    private ShenWuShuXingJiaChengBiaoConfigExportService shenWuShuXingJiaChengBiaoConfigExportService;
    @Autowired
    private ChongwuFushuEquipConfigExportService chongwuFushuEquipConfigExportService;
	
	/**
	 * 获得装备所有属性
	 * @param goodsId
	 * @param qianghuaLevel
	 * @param zhushenLevel
	 * @param gems
	 * @param jianDing
	 * @return
	 */
	public Map<String, Long> getEquipAllAttribute(int tpVal,String goodsId,int qianghuaLevel,Integer zhushenLevel,ContainerType type) {
		Map<String,Long> resultMap = new HashMap<String, Long>();
		
		/*
		 * 基础属性
		 */
		float rate = 1;
		if(zhushenLevel!=null && zhushenLevel.intValue()>0){
			TaoZhuangZhuShenConfig taoZhuangZhuShenConfig = taoZhuangZhuShenConfigExportService.loadByLevel(zhushenLevel);
			if(taoZhuangZhuShenConfig==null){
				ChuanQiLog.error("error zhushen level={}",zhushenLevel);
			}else{
				rate =rate + taoZhuangZhuShenConfig.getZsxs();
			}
		}
		GoodsConfig config=goodsConfigExportService.loadById(goodsId);
		Map<String, Long> baseMap = config.getEquipRealAttr(tpVal);
//		Map<String, Long> baseMap = config.getEquipBaseAttr();
		if(baseMap != null && baseMap.size() > 0){
			for (Entry<String,Long> entry : baseMap.entrySet()) {
				resultMap.put(entry.getKey(),(long)( entry.getValue()*rate));
			}
		}
		
		/*
		 * 强化属性
		 */
		if(qianghuaLevel > 0){ 
            Map<String, Long> qhattrs = equipService.getQHAttrs(tpVal,config, qianghuaLevel, type);
            if (qhattrs != null && qhattrs.size() != 0) {
                for (Entry<String, Long> entry : qhattrs.entrySet()) {
                    String key = entry.getKey();
                    long value = entry.getValue();
                    // 只叠加基本属性有的数据
                    if (resultMap.containsKey(key)) {
                        resultMap.put(key, resultMap.get(key) + value);
                    }
                }
                // }else{
                // ChuanQiLog.error("强化属性获取失败,goodsId:"+goodsId+"\t qianghuaLevel:"+qianghuaLevel);
                // }
            }
        }
		if(resultMap.size() == 0){
			return null;
		}
		return resultMap;  
	}
	
//    public Map<String, Long> getQHAttrs(GoodsConfig goodsConfig, int level, ContainerType type) {
//        Float qhxs = 0F;
//        if (ContainerType.BODYTITEM.equals(type)) {
//            if (goodsConfig.swEquip()) {
//                qhxs = shenWuXingZhuBiaoConfigExportService.getQhxs(level);
//            } else {
//                qhxs = qiangHuaBiaoConfigExportService.getQhxs(level);
//            }
//        } else if (ContainerType.CHONGWUITEM.equals(type)) {
//            qhxs = chongwuFushuEquipConfigExportService.getQhxs(level);
//        } else{
//            qhxs = fuShuQiangHuaBiaoConfigExportService.getQhxs(level);
//        }
//        if (qhxs == null) {
//            return null;
//        }
//        return calcQhAttributeByQhxs(goodsConfig, qhxs);
//    }
//    
//    private Map<String, Long> calcQhAttributeByQhxs(GoodsConfig goodsConfig, float qhxs){
//        Map<String, Long> qhAttrs = new HashMap<>();
//
//        Map<String, Long> baseAttrs = goodsConfig.getEquipBaseAttr();
//        if (!ObjectUtil.isEmpty(baseAttrs)) {
//            List<String> swzbAttr = EffectType.findSwZbQHAttrTypes();// 神武装备增加的效果属性集合
//            for (Entry<String, Long> entry : baseAttrs.entrySet()) {
//                String attr = entry.getKey();
//                if (!EffectType.zplus.name().equals(attr) && !swzbAttr.contains(attr)) {// 排除战力值和神武装备增加的效果属性
//                    qhAttrs.put(attr, Math.round(entry.getValue() * (double) qhxs));
//                }
//            }
//        }
//        // 设置新增的战力值
//        long zhanli = getAddZhanLi(qhAttrs);
//        if (zhanli > 0) {
//            qhAttrs.put(EffectType.zplus.name(), zhanli);
//        }
//
//        return qhAttrs;
//    }

//    public long getAddZhanLi(Map<String, Long> attrs) {
//        if (attrs == null) {
//            return 0;
//        }
//        double zl = 0;
//        for (Entry<String, Long> entry : attrs.entrySet()) {
//            Float xs = zhanLiXiShuConfigExportService.getZLXS(entry.getKey());
//            if (xs == null) {
//                ChuanQiLog.error("强化计算战斗力增量错误:" + entry.getKey() + "没有找到对应的战力系数!");
//                continue;
//            }
//            zl += (double) xs * entry.getValue();
//        }
//        return Math.round(zl);
//    }
	
	
	
    // 获取身上所有的装备信息,用于传入场景
    public Object[] getEquips(long userRoleId, ContainerType type) {
        return equipService.getRoleEquipAttribute(userRoleId, type);
    }
	
	//获取身上所有的装备信息,用于传入场景
	public Object[] getRoleEquipAttribute(long userRoleId){
		return equipService.getRoleEquipAttribute(userRoleId,ContainerType.BODYTITEM); 
	} 
	
	/**
	 * 获得全身颜色加强
	 * @param equips
	 * @param job
	 * @return
	 */
	public Map<String,Long> getQSColorAddAttrs(List<Equip> equips){
		Map<String,Long> result = new HashMap<>();
		if(equips == null ) return result;
		
		Collection<Integer> slots = EquipTypeSlot.getQSQHSlots();
		
		boolean flag = true;//判断装备是否已满 
		
		Integer qhLevel = null;
		Integer color = null;
		
		for (Integer slot : slots) {
			boolean flagTemp = false;
			 for (Equip equip : equips) {
				if(equip.getSlot() == slot.intValue()){
					 
					GoodsConfig goodsConfig = goodsConfigExportService.loadById(equip.getGoodsId());
					if(color == null || goodsConfig.getRareLevel() < color){
						color = goodsConfig.getRareLevel();
					}
					
					if(qhLevel == null || equip.getQianghuaLevel() < qhLevel){
						qhLevel = equip.getQianghuaLevel();
					}
					
					flagTemp = true;
				}
			}
			if(!flagTemp){
				flag = false;
				break;
			}
		}
		
		if(!flag) return null;
		
		for (int i = color; i>=0; i--) {
			Map<String, Long>  attrs = zhuangBeiQiangHuaJiaChengExportService.getAttrsByColor(i);
			if(attrs != null){
				 ObjectUtil.longMapAdd(result, attrs);
				 break;
			}
		}
		
		for (int i = qhLevel; i>=0; i--) {
			Map<String, Long>  attrs = zhuangBeiQiangHuaJiaChengExportService.getqhJCAttrs(i);
			if(attrs != null){
				 ObjectUtil.longMapAdd(result, attrs);
				 break;
			}
		}
		
		return result;
	}
	
//	/**
//	 * 获得全身强化加成属性
//	 * @param equips
//	 * @param job
//	 * @return
//	 */
//	public Map<String,Integer> getQSQHAddAttrs(List<Equip> equips){
//		Map<String,Integer> result = null;
//		if(equips == null ) return result;
//		
//		Collection<Integer> slots = EquipTypeSlot.getQSQHSlots();
//		//判断是否已经到全身装备已满
//	
//		boolean flag = true;
//		int qhLevel = 0;
//		
//		for (Integer slot : slots) {
//			boolean flagTemp = false;
//			 for (Equip equip : equips) {
//				if(equip.getSlot() == slot.intValue()){
//					flagTemp =true;
//					if(qhLevel == 0 || equip.getQianghuaLevel() < qhLevel){
//						qhLevel = equip.getQianghuaLevel();
//					}
//					break;
//				}
//			}
//			if(!flagTemp){
//				flag = false;
//				break;
//			}
//		}
//		
//		if(!flag) return null;
//		
//		for (int i = qhLevel; i>=0; i--) {
//			Map<String, Integer>  attrs = zhuangBeiQiangHuaJiaChengExportService.getqhJCAttrs(i);
//			if(attrs != null){
//				return attrs;
//			}
//		}
//		return null;
//	}
	 
 
	/**
	 * 随机属性
	 * @param equips
	 * @return
	 */
	public Map<String, Long> getEquipRandomAttrs(List<Equip> equips) {
		Map<String,Long> datas=new HashMap<>();
		if(equips == null)return datas; 
		
			for (Equip equip : equips) {
				Integer randomId = equip.getRandomAttrId();
				if(randomId == null)continue;
				
				Integer zhushenLevel = equip.getZhuShenLevel();
				float zhushenRate = 0f;
				if(zhushenLevel!=null&&zhushenLevel.intValue()>0){
					TaoZhuangZhuShenConfig taoZhuangZhuShenConfig = taoZhuangZhuShenConfigExportService.loadByLevel(zhushenLevel);
					if(taoZhuangZhuShenConfig==null){
						ChuanQiLog.error("error zhushen level={}",zhushenLevel);
					}else{
						zhushenRate = taoZhuangZhuShenConfig.getSjxs();
					}
				}

				GoodsConfig config = BusConfigureHelper.getGoodsConfigExportService().loadById(equip.getGoodsId());
				int times = config.getRareLevel();
				
				Map<String,Long> attrs = suiJiShuXingConfigExportService.getAttrsById(randomId);
				if(attrs!=null){
					for (Entry<String,Long> entry : attrs.entrySet()){
						Long oldAttr = datas.get(entry.getKey());
						oldAttr = oldAttr == null ? 0 : oldAttr;
						if(zhushenRate>0){
							datas.put(entry.getKey(),oldAttr +(long) (entry.getValue() * times*(1f+zhushenRate)));
						}else{
							datas.put(entry.getKey(),oldAttr + entry.getValue() * times);
						}
					}
				}
			 
			}
	 
		return datas;
	}
	
//	/**
//	 * 通知场景里面属性变化和外显示
//	 * @param userRoleId
//	 * @param obj
//	 */
//	public void notifyOtherRoleSync(long userRoleId,Object[] obj){
//		equipService.notifyOtherRoleSync(userRoleId, obj);
//	}
	
//	/** 
//	 * 攻击耐久度标识
//	 * @param userRoleId
//	 * @return
//	 */
//	public boolean getAttackFlag(Long userRoleId) {
//		return  equipService.getAttackFlag(userRoleId);
//	}
	
//	/**
//	 * 防御耐久度标识
//	 * @param userRoleId
//	 * @return
//	 */
//	public boolean getDefenseFlag(Long userRoleId) {
//		return equipService.getDefenseFlag(userRoleId);
//		 
//	}
	
	/**
	 * 获得套装属性
	 * @param equips
	 * @return
	 */
	public Map<String, Long> getEquipSuitAttrs(List<Equip> equips) {
		Map<String,Long> datas=new HashMap<>();
		if(equips == null)return datas; 
		
		//suitId = count
		Map<Integer,Integer> suitIdCount = new HashMap<>();
		
		//suitId=<level,count>
		Map<Integer,Map<Integer,Integer>> zhushenCount = new HashMap<>();
		
			for (Equip equip : equips) {
				
				GoodsConfig  goodsConfig = goodsConfigExportService.loadById(equip.getGoodsId());
				if(goodsConfig == null){
					continue;
				}
				
				int suitId = goodsConfig.getSuit();
				if(suitId == 0) {
					continue;
				}
				
				Integer zhushenLevel = equip.getZhuShenLevel();
				if(zhushenLevel!=null&&zhushenLevel>0){
					Map<Integer,Integer> tmp = zhushenCount.get(suitId);
					if(tmp==null){
						tmp = new HashMap<Integer,Integer>();
						zhushenCount.put(suitId, tmp);
					}
					for(int i=1;i<=zhushenLevel;i++){
						Integer count = tmp.get(i);
						if(count==null || count.intValue()==0){
							tmp.put(i, 1);
						}else{
							tmp.put(i,count + 1);
						}
					}
				}
				
				Integer count = suitIdCount.get(suitId);
				count = count!=null?++count:1;
				suitIdCount.put(suitId, count);
			}
			
			for (Entry<Integer,Integer> entry : suitIdCount.entrySet()) {
				int suitId = entry.getKey();
				int count = entry.getValue();
				
				Map<String,Long> attrs=taoZhuangBiaoConfigExportService.getAttrs(suitId, count);
				if(attrs == null) continue; 
				ObjectUtil.longMapAdd(datas, attrs);
				
			}
			for (Integer suitIdKey : zhushenCount.keySet()) {
				Map<Integer,Integer> map = zhushenCount.get(suitIdKey);
				for(Integer levelKey:map.keySet()){
					Integer count = map.get(levelKey);
					Map<String,Long> attrs=taoZhuangBiaoConfigExportService.getAttrs(suitIdKey, count);
					if(attrs == null) {
						continue; 
					}
					float rate = 0;
					TaoZhuangZhuShenConfig taoZhuangZhuShenConfig = taoZhuangZhuShenConfigExportService.loadByLevel(levelKey);
					if(taoZhuangZhuShenConfig!=null){
						rate = taoZhuangZhuShenConfig.getTzxs();
					}
					Map<String,Long> tmp = new HashMap<String,Long>();
					for(String key:attrs.keySet()){
						long value = attrs.get(key);
						tmp.put(key, (long)(value*rate));
					}
					ObjectUtil.longMapAdd(datas, tmp);
				}
			}
 
		return datas;
	}
	
    /**
     * 获得神武装备全身强化属性加成
     * 
     * @param equips
     * @param job
     * @return
     */
    public Map<String, Long> getSWQSAddAttrs(List<Equip> equips) {
        Map<String, Long> result = new HashMap<>();
        if (equips == null) {
            return result;
        }
        /*神武全身强化等级key=装备部位,value=强化等级*/
        Map<Integer, Integer> swQhLevelMap = new HashMap<>();
        Collection<Integer> slots = EquipTypeSlot.getSwSlots();
        for (Equip e : equips) {
            if (slots.contains(e.getSlot())) {
                swQhLevelMap.put(e.getSlot(), e.getQianghuaLevel());
            }
        }

        if (ObjectUtil.isEmpty(swQhLevelMap)) {
            return result;
        }
        List<ShenWuShuXingJiaChengBiaoConfig>  configList = shenWuShuXingJiaChengBiaoConfigExportService.getAllQhLevelConfig();
        for(ShenWuShuXingJiaChengBiaoConfig config : configList){
            int needNum = config.getNeedNum();
            if(swQhLevelMap.size() < needNum){
                continue;
            }
            int num = 0;
            for(int qhLevel : swQhLevelMap.values()){
                if(qhLevel >= config.getLv()){
                    num++;
                }
            }
            if(num >= needNum){
                ObjectUtil.longMapAdd(result, config.getAttrs());
            }
        }
        return result;
    }
	
	public Object[] getAllEquips(Long userRoleId){
		return equipService.getAllEquips(userRoleId);
	}

    
	/**
     * 统计暗金装备
     */
    public void startCalcEquip() {
        equipService.calcEquipRecordLog();
    }
	
}
