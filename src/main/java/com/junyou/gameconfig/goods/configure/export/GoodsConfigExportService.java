package com.junyou.gameconfig.goods.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsGroupFileAbleConfigureInit;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.export.GoodsIdConfig;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 * 
 * @description
 *
 * @author LiuJuan
 * @created 2011-12-15 下午05:45:43
 */
@Component
public class GoodsConfigExportService extends AbsGroupFileAbleConfigureInit{

	/**
	 * 配置名
	 */
	@Override
	protected String[] getGroupConfigureNames() {
		
		String[] configureNames = new String[]{
				"DaoJuBiao.jat"
				,"ZhuangBeiBiao.jat"
		};

		return configureNames;
	}
	
	private Map<String, GoodsConfig> goodsConfigs = new HashMap<>();
	private Map<String, GoodsIdConfig> idConfigs = new HashMap<>();
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data,String configName) {
		if(data == null){
			return;
		}
		//配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configName, data);
		
		Object[] goodslistData =  GameConfigUtil.getResource(data);
		for(Object obj : goodslistData){
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				GoodsConfig config = create(tmp);
				goodsConfigs.put(config.getId(), config);
				
				GoodsIdConfig idConfig = idConfigs.get(config.getId1());
				if(idConfig == null){
					idConfig = new GoodsIdConfig();
					idConfig.setGoodsId(config.getId1());
					idConfigs.put(config.getId1(), idConfig);
				}
				idConfig.addGoodsConfigs(config);
				
				initModulePropIdConstant(config);
				
				initNumberType(config, config.getCategory());
			}
		}
		if("ZhuangBeiBiao.jat".equals(configName)){
			sortIdConfigs();
		}
	}
	
	private void initNumberType(GoodsConfig config,int numberType){
		if(numberType < 0){
			GoodsCategory.addNumberTypeGoods(numberType, config.getId());
		}else if(numberType == GoodsCategory.REVIVE_PROP && ModulePropIdConstant.SPOT_REVIVE_ITEM_ID == null){
			//复活石大类ID
			ModulePropIdConstant.SPOT_REVIVE_ITEM_ID = config.getId1();
		}else if(numberType == GoodsCategory.GUILD_ITEM){
			if(config.getData1() == 1){
				//门派令牌大类ID
				ModulePropIdConstant.GUILD_ITEM1_ID = config.getId1();
			}else if(config.getData1() == 2){
				//门派令牌大类ID
				ModulePropIdConstant.GUILD_ITEM2_ID = config.getId1();
			}else if(config.getData1() == 3){
				//门派令牌大类ID
				ModulePropIdConstant.GUILD_ITEM3_ID = config.getId1();
			}else if(config.getData1() == 4){
				//门派令牌大类ID
				ModulePropIdConstant.GUILD_ITEM4_ID = config.getId1();
			}
		}else if(GoodsCategory.isFeiXie(numberType)){
			//飞鞋小类ID
			ModulePropIdConstant.FEI_XIE_ITEM_ID = config.getId();
		}else if(GoodsCategory.isTangbaoPuti(numberType)){
			//菩提小类ID
			ModulePropIdConstant.addPutiId(config.getId());
		}
	}
	
	/**
	 * 初始化服务器模块道具ID常量
	 * @param idConfig
	 */
	private void initModulePropIdConstant(GoodsConfig config){
		int category = config.getCategory();
		String itemId = config.getId();
		
		switch (category) {
		case GoodsCategory.GOLD:
			ModulePropIdConstant.GOLD_GOODS_ID = itemId;
			break;
		case GoodsCategory.BGOLD:
			ModulePropIdConstant.BIND_GOLD_GOODS_ID = itemId;
			break;
		case GoodsCategory.MONEY:
			ModulePropIdConstant.MONEY_GOODS_ID = itemId;
			break;
		case GoodsCategory.ZHENQI:
			ModulePropIdConstant.MONEY_ZHENQI_ID = itemId;
			break;
		case GoodsCategory.EXP:
			ModulePropIdConstant.EXP_GOODS_ID = itemId;
			break;
		case GoodsCategory.RONGYU:
			ModulePropIdConstant.RONGYU_GOODS_ID = itemId;
			break;
		case GoodsCategory.GONGXIAN:
			ModulePropIdConstant.GONGXIAN_GOODS_ID = itemId;
			break;
		case GoodsCategory.TANBAO:
			ModulePropIdConstant.TANBAO_GOODS_ID = config.getId();
			break;
		case GoodsCategory.GONGXUN:
			ModulePropIdConstant.GONGXUN_GOODS_ID = config.getId();
			break;
		default:
			break;
		}
		
	}	
	
	/**
	 * 物品信息
	 * @param tmp
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private GoodsConfig create(Map<String, Object> tmp) {		
		Map<String, Object> itemProperty = (Map<String, Object>) tmp.get("property");
		if (null != itemProperty) {
			tmp.putAll(itemProperty);
		}
		
		GoodsConfig config = new GoodsConfig();
		
		//公用
		config.setId(CovertObjectUtil.object2String(tmp.get("id")).trim());
		config.setName(CovertObjectUtil.object2String(tmp.get("name")));
		
		int type = CovertObjectUtil.object2int(tmp.get("type"));
		config.setCategory(type != 0 ?type:GoodsCategory.EQUIP_TYPE); //默认是装备类型
		
		
		config.setFirstType(CovertObjectUtil.object2int(tmp.get("type1")));
		config.setRareLevel(CovertObjectUtil.object2int(tmp.get("rarelevel")));
		Integer maxStack =CovertObjectUtil.object2int(tmp.get("maxstack"));
		config.setMaxStack(maxStack<1?1:maxStack);
		config.setRecycle(CovertObjectUtil.object2int(tmp.get("recycle")));
		config.setNotify(CovertObjectUtil.object2boolean(tmp.get("guangbo")));
		config.setJob(CovertObjectUtil.object2int(tmp.get("needjob")));
		config.setLevelReq(CovertObjectUtil.object2int(tmp.get("minlevel")));
		config.setBangding(CovertObjectUtil.object2int(tmp.get("bangding")));
		config.setBind(CovertObjectUtil.object2boolean(tmp.get("bangding")));
		config.setCuihui(CovertObjectUtil.object2int(tmp.get("cuihui")));
		config.setRecord(CovertObjectUtil.object2boolean(tmp.get("record")));
		config.setDes(CovertObjectUtil.object2String(tmp.get("des")));
		config.setNeedzhuansheng(CovertObjectUtil.object2int(tmp.get("needzhuansheng")));
		
		config.setDaily(CovertObjectUtil.object2int(tmp.get("daily")));
		
		//道具
		config.setId1(CovertObjectUtil.object2String(tmp.get("id1")));
		config.setDurationDay(CovertObjectUtil.object2int(tmp.get("day")));
		
		config.setData1(CovertObjectUtil.object2int(tmp.get("data1")));
		config.setData2(CovertObjectUtil.object2Float(tmp.get("data2")));
		
		String data3 = CovertObjectUtil.object2String(tmp.get("data3"));
		if(!CovertObjectUtil.isEmpty(data3)){
			config.setData3(data3);
		}
		String data4 = CovertObjectUtil.object2String(tmp.get("data4"));
		if(!CovertObjectUtil.isEmpty(data4)){
			config.setData4(data4);
		}
		config.setOrder(CovertObjectUtil.object2int(tmp.get("order")));
		//每日使用次数限制：  0或不填：无限制  >0的整数：限制次数
		int xianZhiId = CovertObjectUtil.object2int(tmp.get("xianzhi"));
		config.setXianZhiId(xianZhiId);
		// 0或不填：不可淬炼  >0的整数：可淬炼
		int cuilian = CovertObjectUtil.object2int(tmp.get("cuilian"));
		config.setCuilian(cuilian);
		//装备
		config.setEqpart(CovertObjectUtil.object2int(tmp.get("eqpart")));
		config.setSexReq(CovertObjectUtil.object2int(tmp.get("needsex")));
		config.setGuanzhiReq(CovertObjectUtil.object2int(tmp.get("needguanzhi")));
		config.setIsJianding(CovertObjectUtil.object2Boolean(tmp.get("jianding")));
		config.setRlz(CovertObjectUtil.object2int(tmp.get("ronglian")));//熔炉值
		config.setJobshow(CovertObjectUtil.object2int(tmp.get("jobshow")));
		
		config.setMaxDurability(CovertObjectUtil.object2int(tmp.get("maxnaijiu")));
		
		config.setFenzu(CovertObjectUtil.object2int(tmp.get("fenzu")));
		
		config.setMaxIntensify(CovertObjectUtil.object2int(tmp.get("maxqianghua")));
		
		config.setNextid(CovertObjectUtil.object2String(tmp.get("nextid")));
		
		String needitem = CovertObjectUtil.object2String(tmp.get("needitem"));
		if(!CovertObjectUtil.isEmpty(needitem)){
			
			Map<String,Integer> itemMap = new HashMap<String, Integer>();
			String[] tmpArr = needitem.split("\\|");
			for (String itemValue : tmpArr) {
				String[] innArr = itemValue.split(":");
				
				itemMap.put(innArr[0], Integer.parseInt(innArr[1]));
			}
			
			config.setNeedItem(itemMap);
		}
		
		//config.setNeedItem(CovertObjectUtil.object2String(tmp.get("needitem")));
		config.setOdds(CovertObjectUtil.obj2float(tmp.get("odds")));
		/**    强化属性        **/
		Map<String, String> qianghuaMap = new HashMap<String, String>();
		String activate = CovertObjectUtil.object2String(tmp.get("qianghua"));
		if(!CovertObjectUtil.isEmpty(activate)){
			//List<Integer> itemMap = new ArrayList<Integer>();
//			String[] tmpArr = activate.split("\\|");
//			for (String itemValue : tmpArr) {
//				QiangHuaJiHuoShuXingConfig qhsxConfig = qiangHuaJiHuoShuXingConfigExportServiceImpl.loadById(itemValue);
//				if(qhsxConfig != null){
//					if(qianghuaMap.containsKey(qhsxConfig.getLevel()+"")){
//						String sx = qianghuaMap.get(qhsxConfig.getLevel()+"");
//						qianghuaMap.put(qhsxConfig.getLevel()+"", sx+","+itemValue);
//					}else{
//						qianghuaMap.put(qhsxConfig.getLevel()+"",itemValue);
//					}
//				}
//				//itemMap.add(Integer.valueOf(itemValue));
//			}
			//config.setActivates(itemMap);
			config.setQianghuaSX(qianghuaMap);
		}
		/**    强化属性     end   **/
		
		
		
		//道具
		String gongneng = CovertObjectUtil.object2String(tmp.get("gongneng"));
		config.setSkill(CovertObjectUtil.object2String(gongneng));
		config.setBiao(CovertObjectUtil.object2int(tmp.get("biao")));
		config.setServerUse(CovertObjectUtil.object2int(tmp.get("serveruse")));
		config.setUseUser(CovertObjectUtil.object2int(tmp.get("user")));
		
		config.setUseAble(CovertObjectUtil.object2Boolean(tmp.get("useable")));
		config.setCollectType(CovertObjectUtil.object2int(tmp.get("collecttype")));
		config.setSummonId(CovertObjectUtil.object2String(tmp.get("summonid")));
		config.setShuliandu(CovertObjectUtil.object2int(tmp.get("shuliandu")));
		config.setShenji(CovertObjectUtil.object2int(tmp.get("shenji")));
		config.setOpenGold(CovertObjectUtil.object2int(tmp.get("opengold")));
		config.setGuildLevelUp(CovertObjectUtil.object2int(tmp.get("guildlevelup")));
		config.setCd(CovertObjectUtil.obj2StrOrNull(tmp.get("cd")));
		
		
		//装备
		config.setEquipLocked(CovertObjectUtil.object2Boolean(tmp.get("equiplocked")));
		config.setWhetherRepair(CovertObjectUtil.object2Boolean(tmp.get("whetherrepair")));
		config.setRepair(CovertObjectUtil.object2Float(tmp.get("repair")));
		config.setMaxSockets(CovertObjectUtil.object2int(tmp.get("maxsockets")));
		config.setSuit(CovertObjectUtil.object2int(tmp.get("tid")));
		config.setEquipId(CovertObjectUtil.obj2StrOrNull(tmp.get("zhuanhuan")));
		config.setTipinId(CovertObjectUtil.obj2StrOrNull(tmp.get("nextrare")));
		
		
		Map<String,Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		/**            基础属性                                   **/
		config.setEquipBaseAttr(attrs);
		
		/**
		 * 宝石相关
		 */
		String gemtype = CovertObjectUtil.object2String(tmp.get("gemtype"));
		config.setGemtype(gemtype);
		String nextid = CovertObjectUtil.object2String(tmp.get("nextid"));
		config.setNextid(nextid);
		int equneed = CovertObjectUtil.object2int(tmp.get("equneed"));
		config.setEquneed(equneed);
		String proadd = CovertObjectUtil.object2String(tmp.get("proadd"));
		config.setProadd(proadd);
		float provalue = CovertObjectUtil.object2Float(tmp.get("provalue"));
		config.setProvalue(provalue);
		String effectid = CovertObjectUtil.object2String(tmp.get("effectid"));
		config.setEffectid(effectid);
		float effectodds = CovertObjectUtil.object2Float(tmp.get("effectodds"));
		config.setEffectodds(effectodds);
		
		return config;
	}
	/**对大类id配置管理器内的配置文件进行排序*/
	private void sortIdConfigs(){
		for (GoodsIdConfig config : idConfigs.values()) {
			config.sort();
		}
	}
	
	public GoodsConfig loadById(String goodsId) {
		GoodsConfig goodsConfig = goodsConfigs.get(goodsId);
		if (null == goodsConfig) {
			ChuanQiLog.error("GoodsConfig is null id:"+goodsId);
			return null;
		}
		
		return goodsConfig;
	}


	/**根据大类id获取物品列表(已排序)*/
	public List<GoodsConfig> loadById1(String id1) {
		if(ObjectUtil.strIsEmpty(id1)){
			return new ArrayList<>();
		}
		GoodsIdConfig config = idConfigs.get(id1);
		if(config == null){
			return new ArrayList<>();
		}
		return config.getGoodsConfigs();
	}
	
	/**根据大类id获取物品列表(已排序)*/
	public List<String> loadIdsById1(String id1) {
		if(ObjectUtil.strIsEmpty(id1)){
			return new ArrayList<>();
		}
		GoodsIdConfig config = idConfigs.get(id1);
		if(config == null){
			return new ArrayList<>();
		}
		return config.getGoodsIds();
	}
	
	/**获取所有配置*/
	public List<GoodsConfig> loadAllConfigs(){
		return new ArrayList<>(goodsConfigs.values());
	}
}
