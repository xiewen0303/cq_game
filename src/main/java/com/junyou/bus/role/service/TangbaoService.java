package com.junyou.bus.role.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.junyou.bus.jingji.service.IFightVal;
import com.junyou.bus.jingji.util.FightPowerType;
import com.junyou.bus.shenqi.entity.ShenQiInfo;
import com.junyou.utils.common.CovertObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.equip.configure.export.ZhanLiXiShuConfigExportService;
import com.junyou.bus.role.dao.TangbaoDao;
import com.junyou.bus.role.entity.Tangbao;
import com.junyou.bus.role.entity.TangbaoConfig;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.role.export.TangbaoConfigExportService;
import com.junyou.bus.role.utils.TangbaoUtil;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xianjian.configure.export.XianJianJiChuConfig;
import com.junyou.bus.xianjian.configure.export.XianJianJiChuConfigExportService;
import com.junyou.bus.xianjian.export.XianJianExportService;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.bus.zhanjia.configure.export.ZhanJiaJiChuConfig;
import com.junyou.bus.zhanjia.configure.export.ZhanJiaJiChuConfigExportService;
import com.junyou.bus.zhanjia.export.ZhanJiaExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.TangbaoPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.model.element.pet.Pet;
import com.junyou.stage.model.element.pet.PetVo;
import com.junyou.utils.common.ObjectUtil;
import com.kernel.spring.container.DataContainer;

/**
 * @author LiuYu
 * 2015-5-1 下午2:48:45
 */
@Service
public class TangbaoService implements IFightVal {
	@Override
	public long getZplus(long userRoleId, int fightPowerType) {
		if(fightPowerType == FightPowerType.HXRZ){
			Tangbao tangbao = getTangbao(userRoleId);
			if(tangbao == null || tangbao.getEatAttribute() == null){
				return 0;
			}
			float zplus = 0f;
			for (Entry<String, Long> entry : tangbao.getEatAttribute().entrySet()) {
				Float xs = zhanLiXiShuConfigExportService.getZLXS(entry.getKey());
				if(xs != null){
					zplus += xs * entry.getValue();
				}
			}
			return CovertObjectUtil.obj2long(zplus);
		}
		return 0;
	}

	@Autowired
	private TangbaoDao tangbaoDao;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private TangbaoConfigExportService tangbaoConfigExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private ZhanLiXiShuConfigExportService zhanLiXiShuConfigExportService;
	
	@Autowired
	private XianJianExportService xianJianExportService;
	@Autowired
	private ZhanJiaExportService zhanJiaExportService;
	@Autowired
	private XianJianJiChuConfigExportService xianJianJiChuConfigExportService;
	@Autowired
	private ZhanJiaJiChuConfigExportService zhanJiaJiChuConfigExportService;
	
	public Object[] useCzdNew(Long userRoleId,Long guid){
		RoleItemExport roleItemExport = roleBagExportService
				.getBagItemByGuid(userRoleId, guid);
		if (roleItemExport == null) {
			return AppErrorCode.NO_YB_GOODS;
		}
		String goodsId = roleItemExport.getGoodsId();
		GoodsConfig goodsConfig = goodsConfigExportService
				.loadById(goodsId);
		if (goodsConfig.getCategory() != GoodsCategory.TANGBAO_MXZX
				&& goodsConfig.getCategory() != GoodsCategory.WANNENG_CZD) {
			return AppErrorCode.NO_YB_GOODS;
		}
		Object[] ret = eatZizhiDan(userRoleId, new Integer[]{1});
		if(ret == null){
			ret = AppErrorCode.OK;
			roleBagExportService.removeBagItemByGuid(guid,1, userRoleId, GoodsSource.TANGBAO_CZ, true, true);
		}
		return ret;
	}
	public Object[] useQndNew(Long userRoleId,Long guid){
		RoleItemExport roleItemExport = roleBagExportService
				.getBagItemByGuid(userRoleId, guid);
		if (roleItemExport == null) {
			return AppErrorCode.NO_YB_GOODS;
		}
		String goodsId = roleItemExport.getGoodsId();
		GoodsConfig goodsConfig = goodsConfigExportService
				.loadById(goodsId);
		if (goodsConfig.getCategory() != GoodsCategory.TANGBAO_ZIZHI
				&& goodsConfig.getCategory() != GoodsCategory.WANNENG_QND) {
			return AppErrorCode.NO_YB_GOODS;
		}
		Object[] ret =eatMeiXinZhiXue(userRoleId, new Integer[]{1});
		if(ret == null){
			ret = AppErrorCode.OK;
			roleBagExportService.removeBagItemByGuid(guid,1, userRoleId, GoodsSource.TANGBAO_QN, true, true);
		}
		return ret;
	}
	
	private Tangbao createTangbao(Long userRoleId){
		Tangbao tangbao = new Tangbao();
		tangbao.setUserRoleId(userRoleId);
		tangbao.setCzdan(0);
		tangbao.setZzdan(0);
		tangbao.setProgress(0);
		tangbao.setEatInfo("");
		tangbao.setEatAttribute(new HashMap<String,Long>());
		return tangbao;
	}
	
	/**
	 * 获取糖宝信息，可能为null
	 * @param userRoleId
	 * @return
	 */
	private Tangbao getTangbao(Long userRoleId){
		Tangbao tangbao = tangbaoDao.cacheLoad(userRoleId, userRoleId);
		if(tangbao != null && tangbao.getEatAttribute() == null && tangbao.isActive()){//初始化道具加成属性
			TangbaoPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_TANGBAO);
			Map<String,Long> attribute = new HashMap<>();
			for (Entry<String, Integer> entry : tangbao.getEatInfoMap().entrySet()) {
				GoodsConfig config = getGoodsConfig(entry.getKey());
				if(config == null){
					continue;
				}
				String key = config.getData3();
				long val = config.getData1() * entry.getValue();
				if(attribute.containsKey(key)){
					val += attribute.get(key);
				}
				//判断上限
				Long max = publicConfig.getEatAttMax().get(key);
				if(max != null && val > max){
					val = max;
				}
				
				attribute.put(key, val);
			}
			tangbao.setEatAttribute(attribute);
		}
		return tangbao;
	}
	
	private GoodsConfig getGoodsConfig(String goodsId){
		return goodsConfigExportService.loadById(goodsId);
	}
	
	/**
	 * 通知场景糖宝基本属性变化
	 */
	private void noticeStageTangbaoBaseAttributeChange(Long userRoleId,Integer level){
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.PET_BASE_ATTRIBUTE_CHANGE, getTangbaoBaseAttribute(userRoleId, level));
	}
	
	/**
	 * 通知场景糖宝主人属性变化
	 */
	private void noticeStageTangbaoOwnerAttributeChange(Long userRoleId,Map<String, Long> attMap){
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.PET_OWNER_ATTRIBUTE_CHANGE, attMap);
		BusMsgSender.send2One(userRoleId, ClientCmdType.TANGBAO_OWNER_ATTRIBUTE_CHANGE, TangbaoUtil.changeAttributeOutput(attMap));
	}
	
	/**
	 * 初始化糖宝数据
	 * @param userRoleId
	 * @return
	 */
	public List<Tangbao> initTangbao(Long userRoleId){
		return tangbaoDao.initTangbao(userRoleId);
	}

	/**
	 * 上线业务
	 * @param userRoleId
	 */
	public void onlineHandle(Long userRoleId){
		Tangbao tangbao = getTangbao(userRoleId);
		if(tangbao == null){
			return;
		}
		if(tangbao.reCheck()){
			tangbaoDao.cacheUpdate(tangbao, userRoleId);
		}
		if(tangbao.isActive()){
			BusMsgSender.send2One(userRoleId, ClientCmdType.TANGBAO_ACTIVE_PROGRESS, AppErrorCode.OK);
			BusMsgSender.send2One(userRoleId, ClientCmdType.TANGBAO_ZZD_COUNT, tangbao.getZzdan());
			BusMsgSender.send2One(userRoleId, ClientCmdType.TANGBAO_MXZX_COUNT, tangbao.getCzdan());
		}else{
			BusMsgSender.send2One(userRoleId, ClientCmdType.TANGBAO_ACTIVE_PROGRESS, tangbao.getMsgData());
		}
	}
	/**
	 * 获取糖宝的属性
	 * @param userRoleId
	 * @param level
	 * @return
	 */
	public Map<String,Long> getTangbaoBaseAttribute(Long userRoleId,Integer level){
		Tangbao tangbao = getTangbao(userRoleId);
		if (tangbao == null || !tangbao.isActive()) {
			return null;
		}
		TangbaoConfig config = tangbaoConfigExportService.getTangbaoConfig(level);
		if(config == null){
			return null;
		}
		Map<String,Long> attribute = config.getAttribute();
		Long speed = attribute.remove(EffectType.x19.name());
		
		float rate = 0.02f * tangbao.getZzdan() + 1;
		rate += 0.04f * tangbao.getCzdan();
		ObjectUtil.longMapTimes(attribute, rate);
		
		if(tangbao.getZzdan()!=null && tangbao.getZzdan()>0){
			Map<String,Long> xianjianAndZhanJiaAttr = getXianJianAndZhanJiaBaseAttibute(userRoleId);
			if(xianjianAndZhanJiaAttr!=null && xianjianAndZhanJiaAttr.size()>0){
					float rate1 = 0.02f * tangbao.getZzdan();
					ObjectUtil.longMapTimes(xianjianAndZhanJiaAttr, rate1);
					ObjectUtil.longMapAdd(attribute, xianjianAndZhanJiaAttr);
			}
		}
		
		//添加菩提果效果
		Map<String,Long> eatMap = new HashMap<>();
		ObjectUtil.longMapAdd(eatMap, tangbao.getEatAttribute());
		ObjectUtil.longMapTimes(eatMap, 0.5f);
		ObjectUtil.longMapAdd(attribute, eatMap);
		
		attribute.put(EffectType.x19.name(), speed);
		
		return attribute;
	}
	/**
	 * 获取糖宝的属性
	 * @param userRoleId
	 * @param level
	 * @return
	 */
	public Map<String,Long> getXianJianAndZhanJiaBaseAttibute(Long userRoleId){
		Map<String,Long> map = new HashMap<String,Long>();
		//仙剑 
		Integer xianjianLevel = xianJianExportService.getXianJianLevel(userRoleId);
		if(xianjianLevel!=null && xianjianLevel > -1){
			XianJianJiChuConfig config =  xianJianJiChuConfigExportService.loadById(xianjianLevel);
			if(config!=null){
				ObjectUtil.longMapAdd(map, config.getAttrs());
			}
		}
		//战甲
		Integer zhanjiaLevel= zhanJiaExportService.getXianJianLevel(userRoleId);
		if(zhanjiaLevel !=null && zhanjiaLevel > -1){
			ZhanJiaJiChuConfig config =  zhanJiaJiChuConfigExportService.loadById(zhanjiaLevel);
			if(config!=null){
				ObjectUtil.longMapAdd(map, config.getAttrs());
			}
		}
		return map;
	}
	
	/**
	 * 获取糖宝给主人增加的属性
	 * @param userRoleId
	 * @return
	 */
	public Map<String,Long> getTangbaoRoleAttribute(Long userRoleId){
		Tangbao tangbao = getTangbao(userRoleId);
		if (tangbao == null || !tangbao.isActive()) {
			return null;
		}
		Map<String, Long> att = tangbao.getEatAttribute();
		calZplus(att);
		return att;
	}
	
	/**
	 * 使用资质丹
	 * @param userRoleId
	 * @param count
	 * @return
	 */
	public Object[] eatZizhiDan(Long userRoleId,Integer[] consume){
		int count = consume[0];
		if(count < 1){
			return AppErrorCode.TB_COUNT_IS_ERROR;//数量异常
		}
		
		Tangbao tangbao = getTangbao(userRoleId);
		if (tangbao == null || !tangbao.isActive()) {
			return AppErrorCode.TB_NO_TANGBAO;//没有糖宝
		}
		
		TangbaoPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_TANGBAO);
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(role.getLevel() < config.getZzNeedLevel()){
			return AppErrorCode.TB_NO_ENOUGH_LEVEL;//等级不足
		}
		
		int max = config.getZzMax() - tangbao.getZzdan();
		if(max < 1){
			return AppErrorCode.TB_IS_MAX_COUNT;//数量已达到上限
		}else if(max < count){
			count = max;
		}
		consume[0] = count;
		
		tangbao.setZzdan(tangbao.getZzdan() + count);
		tangbaoDao.cacheUpdate(tangbao, userRoleId);
		//通知场景糖宝属性变化
		noticeStageTangbaoBaseAttributeChange(userRoleId,role.getLevel());
		BusMsgSender.send2One(userRoleId, ClientCmdType.TANGBAO_ZZD_COUNT, tangbao.getZzdan());
		return null;
	}
	
	/**
	 * 使用眉心之血
	 * @param userRoleId
	 * @param count
	 * @return
	 */
	public Object[] eatMeiXinZhiXue(Long userRoleId,Integer[] consume){
		int count = consume[0];
		if(count < 1){
			return AppErrorCode.TB_COUNT_IS_ERROR;//数量异常
		}
		
		Tangbao tangbao = getTangbao(userRoleId);
		if (tangbao == null || !tangbao.isActive()) {
			return AppErrorCode.TB_NO_TANGBAO;//没有糖宝
		}
		
		TangbaoPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_TANGBAO);
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(role.getLevel() < config.getCzNeedLevel()){
			return AppErrorCode.TB_NO_ENOUGH_LEVEL;//等级不足
		}
		
		int max = config.getCzMax() - tangbao.getCzdan();
		if(max < 1){
			return AppErrorCode.TB_IS_MAX_COUNT;//数量已达到上限
		}else if(max < count){
			count = max;
		}
		consume[0] = count;
		
		tangbao.setCzdan(tangbao.getCzdan() + count);
		tangbaoDao.cacheUpdate(tangbao, userRoleId);
		//通知场景糖宝属性变化
		noticeStageTangbaoBaseAttributeChange(userRoleId,role.getLevel());
		BusMsgSender.send2One(userRoleId, ClientCmdType.TANGBAO_MXZX_COUNT, tangbao.getCzdan());
		
		return null;
	}
	
	/**
	 * 使用菩提
	 * @param userRoleId
	 * @param count
	 * @return
	 */
	public Object[] eatPuTi(Long userRoleId,Integer[] consume,GoodsConfig goodsConfig){
		Tangbao tangbao = getTangbao(userRoleId);
		if (tangbao == null || !tangbao.isActive()) {
			return AppErrorCode.TB_NO_TANGBAO;//没有糖宝
		}
		String goodsId = goodsConfig.getId();
		
		TangbaoPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_TANGBAO);
		Long att = tangbao.getEatAttribute().get(goodsConfig.getData3());
		if(att == null){
			att = 0l;
		}
		Long max = config.getEatAttMax().get(goodsConfig.getData3());
		if(max == null){
			return AppErrorCode.TB_IS_MAX_COUNT;//已达到上限
		}
		max = max - att;
		if(max < 1){
			return AppErrorCode.TB_IS_MAX_COUNT;//已达到上限
		}
		
		long maxCount = max / goodsConfig.getData1();
		if(max % goodsConfig.getData1() > 0){
			maxCount++;
		}
		int count = consume[0];
		//计算实际消耗数量
		if(count > maxCount){
			count = (int)maxCount;
		}
		
		Integer eatCount = tangbao.getEatInfoMap().get(goodsId);
		if(eatCount == null){
			eatCount = count;
		}else{
			eatCount += count;
		}
		consume[0] = count;
		tangbao.getEatInfoMap().put(goodsId, eatCount);
		tangbao.setEatInfo(JSON.toJSONString(tangbao.getEatInfoMap()));
		
		long add = count * goodsConfig.getData1();
		if(add > max){
			add = max;
		}
		tangbao.getEatAttribute().put(goodsConfig.getData3(),att + add);
		calZplus(tangbao.getEatAttribute());
		tangbaoDao.cacheUpdate(tangbao, userRoleId);
		
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		
		//通知场景糖宝属性变化
		noticeStageTangbaoBaseAttributeChange(userRoleId,roleWrapper.getLevel());
		//通知场景人物属性变化
		noticeStageTangbaoOwnerAttributeChange(userRoleId,tangbao.getEatAttribute());
		
		return null;
	}
	
	/**
	 * 激活糖宝
	 * @param userRoleId
	 */
	public void activeTangbao(Long userRoleId){
		Tangbao tangbao = getTangbao(userRoleId);
		if(tangbao == null){
			tangbao = createTangbao(userRoleId);
			tangbaoDao.cacheInsert(tangbao, userRoleId);
		}else if(tangbao.isActive()){
			return;//已有糖宝
		}
		tangbao.setProgress(-1);
		tangbaoDao.cacheUpdate(tangbao, userRoleId);
		
		//通知场景角色新增宠物 
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		TangbaoConfig config = tangbaoConfigExportService.getTangbaoConfig(role.getLevel());
		if(config == null){
			return;
		}
		BusMsgSender.send2One(userRoleId, ClientCmdType.TANGBAO_ACTIVE_PROGRESS, AppErrorCode.OK);
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.PET_ADD_PET, config.getAttribute());
		//修炼任务
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.JIHUO_XINGKE, null});
	}
	
	/**
	 * 更新进度
	 * @param userRoleId
	 */
	public void addTangbaoProgress(Long userRoleId){
		if(!publicRoleStateExportService.isPublicOnline(userRoleId)){
			return;
		}
		Tangbao tangbao = getTangbao(userRoleId);
		if(tangbao == null || tangbao.isActive()){
			return;
		}
		tangbao.addProgress();
		tangbaoDao.cacheUpdate(tangbao, userRoleId);
		
		if(tangbao.isActive()){//糖宝成功激活
			BusMsgSender.send2One(userRoleId, ClientCmdType.TANGBAO_ACTIVE_PROGRESS, AppErrorCode.OK);
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			TangbaoConfig config = tangbaoConfigExportService.getTangbaoConfig(role.getLevel());
			if(config == null){
				return;
			}
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.PET_ADD_PET, config.getAttribute());
		}else{
			BusMsgSender.send2One(userRoleId, ClientCmdType.TANGBAO_ACTIVE_PROGRESS, tangbao.getMsgData());
		}
	}
	
	/**
	 * 获取糖宝属性
	 * @param userRoleId
	 * @return
	 */
	public Map<Integer,Long> getTangbaoAttribute(Long userRoleId){
		Tangbao tangbao = getTangbao(userRoleId);
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(role == null){
			return null;
		}
		if(tangbao == null){
			TangbaoPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_TANGBAO);
			if(publicConfig == null){
				return null;
			}
			if(role.getLevel() < publicConfig.getNeedLevel()){
				return null;
			}
			tangbao = createTangbao(userRoleId);
			tangbaoDao.cacheInsert(tangbao, userRoleId);
		}
		if(!tangbao.isActive()){
			BusMsgSender.send2One(userRoleId, ClientCmdType.TANGBAO_ACTIVE_PROGRESS, tangbao.getMsgData());
			return null;
		}
		
		PetVo petVo = dataContainer.getData(GameConstants.COMPONENT_NAME_PET, role.getId().toString());
		if(petVo == null){
			return null;
		}
		Pet pet = petVo.getPet();
		if(pet == null){
			return null;
		}
		
		return TangbaoUtil.tangbaoAttributeOutput(pet.getFightAttribute(), tangbao.getEatAttribute());
	}
	
	/**
	 * 一键食用
	 * @param userRoleId
	 */
	public void oneKeyEat(Long userRoleId){
		Tangbao tangbao = getTangbao(userRoleId);
		if(tangbao == null || !tangbao.isActive()){
			return;
		}
		TangbaoPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_TANGBAO);
		if(config == null){
			return;
		}
		boolean update = false;
		for (String putiId : ModulePropIdConstant.getPutiIds()) {
			int count = roleBagExportService.getBagItemCountByGoodsId(putiId, userRoleId);
			if(count > 0){
				GoodsConfig goodsConfig = goodsConfigExportService.loadById(putiId);
				if(goodsConfig == null){
					continue;
				}
				Long att = tangbao.getEatAttribute().get(goodsConfig.getData3());
				if(att == null){
					att = 0l;
				}
				Long max = config.getEatAttMax().get(goodsConfig.getData3());
				max = max - att;
				if(max < 1){
					continue;//已达到上限
				}
				
				long maxCount = max / goodsConfig.getData1();
				if(max % goodsConfig.getData1() > 0){
					maxCount++;
				}
				if(count > maxCount){
					count = (int)maxCount;
				}
				Integer eatCount = tangbao.getEatInfoMap().get(putiId);
				if(eatCount == null){
					eatCount = count;
				}else{
					eatCount += count;
				}
				tangbao.getEatInfoMap().put(putiId, eatCount);
				
				long add = count * goodsConfig.getData1();
				if(add > max){
					add = max;
				}
				tangbao.getEatAttribute().put(goodsConfig.getData3(),att + add);
				update = true;
				roleBagExportService.removeBagItemByGoodsId(putiId, count, userRoleId, GoodsSource.TB_EAT, true, true);
			}
		}
		if(update){
			tangbao.setEatInfo(JSON.toJSONString(tangbao.getEatInfoMap()));
			calZplus(tangbao.getEatAttribute());
			tangbaoDao.cacheUpdate(tangbao, userRoleId);
			
			RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
			//通知场景糖宝属性变化
			noticeStageTangbaoBaseAttributeChange(userRoleId,roleWrapper.getLevel());
			//通知场景人物属性变化
			noticeStageTangbaoOwnerAttributeChange(userRoleId,tangbao.getEatAttribute());
		}
	}
	
	public void refreshAttribute(Long userRoleId){
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		//通知场景糖宝属性变化
		noticeStageTangbaoBaseAttributeChange(userRoleId,roleWrapper.getLevel());
	}
	
	public int getTangbaoState(Long userRoleId){
		Tangbao tangbao = getTangbao(userRoleId);
		if(tangbao == null || tangbao.isActive()){
			return 0;
		}else{
			return 1;
		}
	}
	private void calZplus(Map<String,Long> att){
		Float zplus = 0f;
		for (Entry<String, Long> entry : att.entrySet()) {
			Float xs = zhanLiXiShuConfigExportService.getZLXS(entry.getKey());
			if(xs != null){
				zplus += xs * entry.getValue();
			}
		}
		att.put(EffectType.zplus.name(), zplus.longValue());
	}
	
	public boolean hasTangbao(Long userRoleId){
		Tangbao tangbao = getTangbao(userRoleId);
		if(tangbao != null && tangbao.isActive()){
			return true;
		}
		return false;
	}
}
