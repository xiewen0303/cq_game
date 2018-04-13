package com.junyou.bus.chengjiu.server;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.bus.jingji.service.IFightVal;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.utils.common.CovertObjectUtil;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.assign.export.AssignExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.chenghao.service.export.ChenghaoExportService;
import com.junyou.bus.chengjiu.configure.export.ChengJiuPeiZhiConfig;
import com.junyou.bus.chengjiu.configure.export.ChengJiuPeiZhiConfigExportService;
import com.junyou.bus.chengjiu.dao.RoleChengjiuDao;
import com.junyou.bus.chengjiu.dao.RoleChengjiuDataDao;
import com.junyou.bus.chengjiu.entity.RoleChengjiu;
import com.junyou.bus.chengjiu.entity.RoleChengjiuData;
import com.junyou.bus.chibang.configure.export.ChiBangJiChuConfig;
import com.junyou.bus.chibang.configure.export.ChiBangJiChuConfigExportService;
import com.junyou.bus.chibang.entity.ChiBangInfo;
import com.junyou.bus.chibang.export.ChiBangExportService;
import com.junyou.bus.fuben.export.FubenExportService;
import com.junyou.bus.qiling.configure.export.QiLingJiChuConfig;
import com.junyou.bus.qiling.configure.export.QiLingJiChuConfigExportService;
import com.junyou.bus.qiling.entity.QiLingInfo;
import com.junyou.bus.qiling.export.QiLingExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoExportService;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoWrapper;
import com.junyou.bus.shenqi.export.ShenQiExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.bus.vip.util.RoleVipWrapper;
import com.junyou.bus.wuqi.configure.export.XinShengJianJiChuConfig;
import com.junyou.bus.wuqi.configure.export.XinShengJianJiChuConfigExportService;
import com.junyou.bus.wuqi.entity.WuQiInfo;
import com.junyou.bus.wuqi.export.WuQiExportService;
import com.junyou.bus.xianjian.configure.export.XianJianJiChuConfig;
import com.junyou.bus.xianjian.configure.export.XianJianJiChuConfigExportService;
import com.junyou.bus.xianjian.entity.XianJianInfo;
import com.junyou.bus.xianjian.export.XianJianExportService;
import com.junyou.bus.zhanjia.configure.export.ZhanJiaJiChuConfig;
import com.junyou.bus.zhanjia.configure.export.ZhanJiaJiChuConfigExportService;
import com.junyou.bus.zhanjia.entity.ZhanJiaInfo;
import com.junyou.bus.zhanjia.export.ZhanJiaExportService;
import com.junyou.bus.zuoqi.configure.export.YuJianJiChuConfig;
import com.junyou.bus.zuoqi.configure.export.YuJianJiChuConfigExportService;
import com.junyou.bus.zuoqi.entity.ZuoQiInfo;
import com.junyou.bus.zuoqi.export.ZuoQiExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * 成就
 */
@Service
public class RoleChengJiuService implements IFightVal{

	public long getZplus(long userRoleId, int fightPowerType) {
		Map<String, Long>  data =getChengJiuAttrs(userRoleId);
		if(data == null){
			return 0;
		}
		return CovertObjectUtil.getZplus(data);
	}

	@Autowired
	private RoleChengjiuDao roleChengjiuDao;
	@Autowired
	private ChengJiuPeiZhiConfigExportService chengJiuPeiZhiConfigExportServiceImpl;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleBusinessInfoExportService roleBusinessInfoExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private ZuoQiExportService zuoQiExportService;
	@Autowired
	private YuJianJiChuConfigExportService yuJianJiChuConfigExportService;
	@Autowired
	private ChiBangExportService chiBangExportService;
	@Autowired
	private QiLingExportService qilingExportService;
	@Autowired
	private ChiBangJiChuConfigExportService chiBangJiChuConfigExportService;
	@Autowired
	private QiLingJiChuConfigExportService qiLingJiChuConfigExportService;
	@Autowired
	private XianJianExportService xianJianExportService;
	@Autowired
	private XianJianJiChuConfigExportService xianJianJiChuConfigExportService;
	@Autowired
	private ZhanJiaExportService zhanJiaExportService;
	@Autowired
	private ZhanJiaJiChuConfigExportService zhanJiaJiChuConfigExportService;
	@Autowired
	private RoleChengjiuDataDao roleChengjiuDataDao;
	@Autowired
	private AssignExportService assignExportService;
	@Autowired
	private ShenQiExportService shenQiExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private ChenghaoExportService chenghaoExportService;
	@Autowired
	private FubenExportService fubenExportService;
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	@Autowired
	private WuQiExportService wuQiExportService;
	@Autowired
	private XinShengJianJiChuConfigExportService xinShengJianJiChuConfigExportService;
	
	
	public List<RoleChengjiuData> initRoleChengjiuData(Long userRoleId){
		return roleChengjiuDataDao.initRoleChengjiuData(userRoleId);
	}
	
	public List<RoleChengjiu> initRoleChengJiu(Long userRoleId){
		return roleChengjiuDao.initRoleChengjiu(userRoleId);
	}

	public void onlineHandle(Long userRoleId){
		
		int cjValue = getChengJiuValue(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.CHENGJIU_TUISONG,new Object[]{cjValue});
	}
	
	
	private RoleChengjiuData getRoleChengJiuData(Long userRoleId){
		List<RoleChengjiuData> list = roleChengjiuDataDao.cacheAsynLoadAll(userRoleId);
		if(list!= null && list.size() > 0){
			RoleChengjiuData chengjiu = list.get(0);
			//不是同一天，清理数据
			if(!DateUtils.isSameDay(chengjiu.getUpdateTime(), new Timestamp(GameSystemTime.getSystemMillTime()))){
				chengjiu.setDanriXiaofeiNumber(0);//单日消费元宝  跨天清空
				
				roleChengjiuDataDao.cacheUpdate(chengjiu, userRoleId);
			}
			return chengjiu;
		}
		RoleChengjiuData data = new RoleChengjiuData();
		data.setUserRoleId(userRoleId);
		data.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		data.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		data.setDayTaskCount(0);
		data.setGuildTaskCount(0);
		data.setGouyuFubenCount(0);
		data.setDihuoFubenCount(0);
		data.setSnashengFubenCount(0);
		data.setXianmobangCount(0);
		data.setChengBiaoCount(0);
		data.setKillBossCount(0);
		data.setXizaoFeizaoCount(0);
		data.setShichangChushouCount(0);
		data.setDanriXiaofeiNumber(0);
		data.setDanciRechargeNumber(0);
		data.setLeijiRechargeNumber(0);
		data.setLeijiXunbaoCount(0);
		data.setLeijiQiandaoCount(0);
		data.setKillMonterCount(0);
		
		roleChengjiuDataDao.cacheInsert(data, userRoleId);
		
		return data;
	}
	
	private RoleChengjiu getRoleChengJiu(Long userRoleId){
		List<RoleChengjiu> list = roleChengjiuDao.cacheAsynLoadAll(userRoleId);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		RoleChengjiu chengjiu = new RoleChengjiu();
		chengjiu.setUserRoleId(userRoleId);
		chengjiu.setChengjiuId("");
		chengjiu.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		chengjiu.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		chengjiu.setReceiveId("");
		roleChengjiuDao.cacheInsert(chengjiu, userRoleId);
		
		return chengjiu;
		
	}
	
	
	public Object[] getChengJiuInfo(Long userRoleId){
		
		RoleChengjiu chengjiu = getRoleChengJiu(userRoleId);
		
		Map<Integer, Integer> dianMap = new HashMap<>();//成就点数MAP<成就标签页type,成就点数>
		Map<String,Long> attrsMap = new HashMap<>(); 	//已获得的属性map
		
		setChengJiuById(userRoleId,chengjiu.getChengjiuId(), dianMap, attrsMap);
		
		List<Object[]> dianList = new ArrayList<>();
		if(dianMap.size() > 0){
			for (Integer id : dianMap.keySet()) {
				dianList.add(new Object[]{id,dianMap.get(id)});
			}
		}
		
		List<Object[]> proList = new ArrayList<>();
		if(attrsMap.size() > 0){
			for (String pro : attrsMap.keySet()) {
				proList.add(new Object[]{pro,attrsMap.get(pro)});
			}
		}
		
		return new Object[]{1,dianList.toArray(),proList.toArray()};
	}
	
	
	/**
	 * 根据成就ID 获取成就点数和成就属性
	 * @return
	 */
	private void setChengJiuById(Long userRoleId,String cjId,Map<Integer, Integer> dianMap,Map<String,Long> attrsMap){
		if(cjId == null || "".equals(cjId)){
			return;
		}
		String[] chengjiu = cjId.split(",");
		for (int i = 0; i < chengjiu.length; i++) {
			ChengJiuPeiZhiConfig config = chengJiuPeiZhiConfigExportServiceImpl.loadById(Integer.parseInt(chengjiu[i]));
			if(config == null){
				continue;
			}
			//如果有称号  则激活称号
			if(config.getChenghao() != null && !"".equals(config.getChenghao())){
				chenghaoExportService.activateChenghao(userRoleId, Integer.parseInt(config.getChenghao()));
			}
			//增加成就点数
			if(dianMap.containsKey(config.getPageType())){
				dianMap.put(config.getPageType(), dianMap.get(config.getPageType()) + config.getCjvalue());
			}else{
				dianMap.put(config.getPageType(), config.getCjvalue());
			}
			//增加属性
			for (String pro:config.getAttrs().keySet()) {
				if(attrsMap.containsKey(pro)){
					attrsMap.put(pro, attrsMap.get(pro) + config.getAttrs().get(pro));
				}else{
					attrsMap.put(pro, config.getAttrs().get(pro));
				}
			}
		}
		
		return;
	}
	public Map<String, Long> getChengJiuAttrs(Long userRoleId){
		RoleChengjiu roleChengJiu = getRoleChengJiu(userRoleId);
		String cjId = roleChengJiu.getChengjiuId();
		Map<String, Long> attrsMap = new HashMap<>();
		if(cjId == null || "".equals(cjId)){
			return attrsMap;
		}
		String[] chengjiu = cjId.split(",");
		for (int i = 0; i < chengjiu.length; i++) {
			ChengJiuPeiZhiConfig config = chengJiuPeiZhiConfigExportServiceImpl.loadById(Integer.parseInt(chengjiu[i]));
			if(config == null){
				continue;
			}
			//增加属性
			for (String pro:config.getAttrs().keySet()) {
				if(attrsMap.containsKey(pro)){
					attrsMap.put(pro, attrsMap.get(pro) + config.getAttrs().get(pro));
				}else{
					attrsMap.put(pro, config.getAttrs().get(pro));
				}
			}
		}
		return attrsMap;
	}
	public Map<String, Long> getChengJiuAttrsKf(Long userRoleId,String cjId){
		Map<String, Long> attrsMap = new HashMap<>();
		if(cjId == null || "".equals(cjId)){
			return attrsMap;
		}
		String[] chengjiu = cjId.split(",");
		for (int i = 0; i < chengjiu.length; i++) {
			ChengJiuPeiZhiConfig config = chengJiuPeiZhiConfigExportServiceImpl.loadById(Integer.parseInt(chengjiu[i]));
			if(config == null){
				continue;
			}
			//增加属性
			for (String pro:config.getAttrs().keySet()) {
				if(attrsMap.containsKey(pro)){
					attrsMap.put(pro, attrsMap.get(pro) + config.getAttrs().get(pro));
				}else{
					attrsMap.put(pro, config.getAttrs().get(pro));
				}
			}
		}
		return attrsMap;
	}
	/**
	 * 根据成就ID 获取成就点数
	 * @return
	 */
	public int getChengJiuValue(Long userRoleId){
		RoleChengjiu roleChengJiu = getRoleChengJiu(userRoleId);
		String cjId = roleChengJiu.getChengjiuId();
		int cjValue = 0;
		if(cjId == null || "".equals(cjId)){
			return cjValue;
		}
		String[] chengjiu = cjId.split(",");
		for (int i = 0; i < chengjiu.length; i++) {
			ChengJiuPeiZhiConfig config = chengJiuPeiZhiConfigExportServiceImpl.loadById(Integer.parseInt(chengjiu[i]));
			if(config == null){
				continue;
			}
			//增加成就点数
			cjValue += config.getCjvalue();
		}
		
		return cjValue;
	}
	
	/**
	 * 激活成就
	 * @param chengjiu
	 * @param id
	 */
	private void jiHuo(RoleChengjiu chengjiu,ChengJiuPeiZhiConfig config){
		String cjId = chengjiu.getChengjiuId();
		//保险起见  先判断是否已经激活过这个成就
		if(isJiHuo(cjId, config.getId())){
			return;
		}
		
		if(cjId == null || "".equals(cjId)){
			chengjiu.setChengjiuId(config.getId()+"");
		}else{
			chengjiu.setChengjiuId(chengjiu.getChengjiuId() + "," + config.getId());
		}
		
		chengjiu.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		roleChengjiuDao.cacheUpdate(chengjiu, chengjiu.getUserRoleId());
		
		// 推送内部场景成就属性变化
		BusMsgSender.send2Stage(chengjiu.getUserRoleId(), InnerCmdType.INNET_CHENGJIU_CHANGE,chengjiu.getChengjiuId());
		//如果有称号  则激活称号
		if(config.getChenghao() != null && !"".equals(config.getChenghao())){
			chenghaoExportService.activateChenghao(chengjiu.getUserRoleId(), Integer.parseInt(config.getChenghao()));
		}
	}
	
	/**
	 * 判断成就ID是否已经激活
	 * @param chengJiuId
	 * @param id
	 * @return
	 */
	private boolean isJiHuo(String cjId,Integer id){
		if(cjId == null || "".equals(cjId)){
			return false;
		}
		String[] chengjiu = cjId.split(",");
		for (int i = 0; i < chengjiu.length; i++) {
			if(Integer.parseInt(chengjiu[i]) == id.intValue()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断成就ID是否要领奖
	 * @return
	 */
	private boolean isLinJiang(String receiveIds,Integer id){
		if(receiveIds == null || "".equals(receiveIds)){
			return false;
		}
		String[] chengjiu = receiveIds.split(",");
		for (int i = 0; i < chengjiu.length; i++) {
			if(Integer.parseInt(chengjiu[i]) == id.intValue()){
				return true;
			}
		}
		return false;
	}
	
	
	public Object[] getChengJiuByType(Long userRoleId,Object[] types){
		RoleChengjiu chengjiu = getRoleChengJiu(userRoleId);
		RoleChengjiuData data = getRoleChengJiuData(userRoleId);
		
		if(types == null || types.length <= 0){
			return AppErrorCode.NOT_REQUEST_CHENGJIU_TYPES;
		}
		List<Object[]> cjList = new ArrayList<>();
		List<Integer> idList;
		for (int i = 0; i < types.length; i++) {
			List<ChengJiuPeiZhiConfig> configs = chengJiuPeiZhiConfigExportServiceImpl.loadByType(Integer.parseInt(types[i].toString()));
			if(configs == null || configs.size() <= 0){
				return AppErrorCode.NOT_CHENGJIU_TYPES;
			}
			switch (Integer.parseInt(types[i].toString())) {
			
			case  GameConstants.CJ_LEVEL:
				RoleWrapper role = roleExportService.getLoginRole(userRoleId);
				idList = getIdListByType(configs, chengjiu, role.getLevel());
				cjList.add(new Object[]{types[i],role.getLevel(),idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			case  GameConstants.CJ_ZPLUS:
				RoleBusinessInfoWrapper roleBus = roleBusinessInfoExportService.getRoleBusinessInfoWrapper(userRoleId);
				
				idList = getIdListByType(configs, chengjiu, roleBus.getCurFighter().intValue());
				cjList.add(new Object[]{types[i],roleBus.getCurFighter(),idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			case  GameConstants.CJ_QIANGHUA:
				int qhLevel = roleBagExportService.getAllEquipsQHLevel(userRoleId);
				idList = getIdListByType(configs, chengjiu, qhLevel);
				cjList.add(new Object[]{types[i],qhLevel,idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			case  GameConstants.CJ_ZIZHUANG:
				int zizhuang = roleBagExportService.getZiZhuangCount(userRoleId);
				idList = getIdListByType(configs, chengjiu, zizhuang);
				cjList.add(new Object[]{types[i],zizhuang,idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			case  GameConstants.CJ_CHENGZHUANG:
				int chengzhuang = roleBagExportService.getChengZhuangCount(userRoleId);
				idList = getIdListByType(configs, chengjiu, chengzhuang);
				cjList.add(new Object[]{types[i],chengzhuang,idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			case  GameConstants.CJ_TAOZHUANG:
				int taozhuang = roleBagExportService.getTaoZhuangCount(userRoleId);
				idList = getIdListByType(configs, chengjiu, taozhuang);
				cjList.add(new Object[]{types[i],taozhuang,idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			case  GameConstants.CJ_ZUOQI:
				//玩家坐骑
				int zuoQiLevel = 0;
				ZuoQiInfo zuoqiInfo = zuoQiExportService.getZuoQiInfo(userRoleId);
				if(zuoqiInfo != null ){
					YuJianJiChuConfig yujianConfig = yuJianJiChuConfigExportService.loadById(zuoqiInfo.getZuoqiLevel());
					if(yujianConfig != null){
						zuoQiLevel = yujianConfig.getLevel();
					}
				}
				idList = getIdListByType(configs, chengjiu, zuoQiLevel);
				cjList.add(new Object[]{types[i],zuoQiLevel,idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
				
			case  GameConstants.CJ_WUQI:
				//玩家新圣剑
				int wuQiLevel = 0;
				WuQiInfo wuqiInfo = wuQiExportService.getWuQiInfo(userRoleId);
				if(wuqiInfo != null ){
					XinShengJianJiChuConfig xinShengJianConfig = xinShengJianJiChuConfigExportService.loadById(wuqiInfo.getZuoqiLevel());
					if(xinShengJianConfig != null){
						wuQiLevel = xinShengJianConfig.getLevel();
					}
				}
				idList = getIdListByType(configs, chengjiu, wuQiLevel);
				cjList.add(new Object[]{types[i],wuQiLevel,idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
				
			case  GameConstants.CJ_CHIBANG:
				//玩家翅膀
				int chibangLevel = 0;
				ChiBangInfo chibangInfo = chiBangExportService.getChiBangInfo(userRoleId);
				if(chibangInfo != null ){
					ChiBangJiChuConfig chibangConfig = chiBangJiChuConfigExportService.loadById(chibangInfo.getChibangLevel());
					if(chibangConfig != null){
						chibangLevel = chibangConfig.getLevel();
					}
				}
				idList = getIdListByType(configs, chengjiu, chibangLevel);
				cjList.add(new Object[]{types[i],chibangLevel,idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			case  GameConstants.CJ_QILING:
				//玩家器灵
				int qlLevel = 0;
				QiLingInfo qlInfo = qilingExportService.getQiLingInfo(userRoleId);
				if(qlInfo != null ){
					QiLingJiChuConfig qlConfig = qiLingJiChuConfigExportService.loadById(qlInfo.getQilingLevel());
					if(qlConfig != null){
						qlLevel = qlConfig.getLevel();
					}
				}
				idList = getIdListByType(configs, chengjiu, qlLevel);
				cjList.add(new Object[]{types[i],qlLevel,idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			case  GameConstants.CJ_XIANJIAN:
				//玩家仙剑
				int xianjianLevel = 0;
				XianJianInfo xianjianInfo = xianJianExportService.getXianJianInfo(userRoleId);
				if(xianjianInfo != null ){
					XianJianJiChuConfig xianjianConfig = xianJianJiChuConfigExportService.loadById(xianjianInfo.getXianjianLevel());
					if(xianjianConfig != null){
						xianjianLevel = xianjianConfig.getLevel();
					}
				}
				idList = getIdListByType(configs, chengjiu, xianjianLevel);
				cjList.add(new Object[]{types[i],xianjianLevel,idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			case  GameConstants.CJ_ZHANJIA:
				//玩家战甲
				int zhanjiaLevel = 0;
				ZhanJiaInfo zhanJiaInfo = zhanJiaExportService.getXianJianInfo(userRoleId);
				if(zhanJiaInfo != null ){
					ZhanJiaJiChuConfig zhanjiaConfi = zhanJiaJiChuConfigExportService.loadById(zhanJiaInfo.getXianjianLevel());
					if(zhanjiaConfi != null){
						zhanjiaLevel = zhanjiaConfi.getLevel();
					}
				}
				idList = getIdListByType(configs, chengjiu, zhanjiaLevel);
				cjList.add(new Object[]{types[i],zhanjiaLevel,idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			case  GameConstants.CJ_SHENBING:
				int sb = shenQiExportService.getActivatedShenqiNum(userRoleId);
				idList = getIdListByType(configs, chengjiu,sb);
				cjList.add(new Object[]{types[i],sb,idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			case  GameConstants.CJ_JICHUJINENG:
				
				break;
			case  GameConstants.CJ_BEIDONGJINENG:
				
				break;
			case  GameConstants.CJ_DAYTASKCOUNT:
				idList = getIdListByType(configs, chengjiu, data.getDayTaskCount());
				
				cjList.add(new Object[]{types[i],data.getDayTaskCount(),idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});	
				break;
			case  GameConstants.CJ_GUILDTASKCOUNT:
				idList = getIdListByType(configs, chengjiu, data.getGuildTaskCount());
				
				cjList.add(new Object[]{types[i],data.getGuildTaskCount(),idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});	
				break;
			case  GameConstants.CJ_GOUYUFUBENCOUNT:
				idList = getIdListByType(configs, chengjiu, data.getGouyuFubenCount());
				
				cjList.add(new Object[]{types[i],data.getGouyuFubenCount(),idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});	
				break;
			case  GameConstants.CJ_DIHUOFUBENCOUNT:
				idList = getIdListByType(configs, chengjiu, data.getDihuoFubenCount());
				
				cjList.add(new Object[]{types[i],data.getDihuoFubenCount(),idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});	
				break;
			case  GameConstants.CJ_NANWUYUEFUBENCOUNT:
				int nanwuyue = fubenExportService.getShouhuFubenMax(userRoleId);
				idList = getIdListByType(configs, chengjiu, nanwuyue);
				cjList.add(new Object[]{types[i],nanwuyue,idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			case  GameConstants.CJ_SANSHENGFUBENCOUNT:
				idList = getIdListByType(configs, chengjiu, data.getSnashengFubenCount());
				
				cjList.add(new Object[]{types[i],data.getSnashengFubenCount(),idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});	
				break;
			case  GameConstants.CJ_XIANMOBANGCENG:
				int pataCeng = fubenExportService.getPataMaxCeng(userRoleId);
				idList = getIdListByType(configs, chengjiu, pataCeng);
				cjList.add(new Object[]{types[i],pataCeng,idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			case  GameConstants.CJ_XIANMOBANG://仙魔榜通关次数
				idList = getIdListByType(configs, chengjiu, data.getXianmobangCount());
				
				cjList.add(new Object[]{types[i],data.getXianmobangCount(),idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});				
				break;
			case  GameConstants.CJ_CHENGBIAO://护送橙镖
				idList = getIdListByType(configs, chengjiu, data.getChengBiaoCount());
				
				cjList.add(new Object[]{types[i],data.getChengBiaoCount(),idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});	
				break;
			case  GameConstants.CJ_YEWAIBOSS://累计击杀野外BOSS
				idList = getIdListByType(configs, chengjiu, data.getKillBossCount());
				
				cjList.add(new Object[]{types[i],data.getKillBossCount(),idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			case  GameConstants.CJ_DATI:
				idList = getIdListByTypeNoJindu(configs, chengjiu);
				
				cjList.add(new Object[]{types[i],data.getKillBossCount(),idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			case  GameConstants.CJ_XIZAOFEIZAO://洗澡丢肥皂次数
				idList = getIdListByType(configs, chengjiu, data.getXizaoFeizaoCount());
				
				cjList.add(new Object[]{types[i],data.getXizaoFeizaoCount(),idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			case  GameConstants.CJ_SHICHANGMAI://市场成功出售次数
				idList = getIdListByType(configs, chengjiu, data.getShichangChushouCount());
				
				cjList.add(new Object[]{types[i],data.getShichangChushouCount(),idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			case  GameConstants.CJ_YINLIANG://拥有银两
				long yinliang = accountExportService.getCurrency(GoodsCategory.MONEY, userRoleId);
				idList = getIdListByType(configs, chengjiu,(int) yinliang);
				cjList.add(new Object[]{types[i],yinliang,idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			case  GameConstants.CJ_XIAOFEIYUANBAO://单日消费元宝数量
				idList = getIdListByType(configs, chengjiu, data.getDanriXiaofeiNumber());
				
				cjList.add(new Object[]{types[i],data.getDanriXiaofeiNumber(),idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			case  GameConstants.CJ_DANCHONGZHIYUANBAO://单次充值元宝数量
				idList = getIdListByType(configs, chengjiu, data.getDanciRechargeNumber());
				
				cjList.add(new Object[]{types[i],data.getDanciRechargeNumber(),idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			case  GameConstants.CJ_LEICHONGZHIYUANBAO://累计充值元宝数量
				idList = getIdListByType(configs, chengjiu, data.getLeijiRechargeNumber());
				
				cjList.add(new Object[]{types[i],data.getLeijiRechargeNumber(),idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			case  GameConstants.CJ_TIJUNBAOHE://累计开启帝君宝盒
				idList = getIdListByType(configs, chengjiu, data.getLeijiXunbaoCount());
				
				cjList.add(new Object[]{types[i],data.getLeijiXunbaoCount(),idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			case  GameConstants.CJ_QIANDAOCOUNT://签到
				int assignAll = assignExportService.getAssignAll(userRoleId);
				idList = getIdListByType(configs, chengjiu, assignAll);
				cjList.add(new Object[]{types[i],assignAll,idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			case  GameConstants.CJ_PKVALUE:
				RoleBusinessInfoWrapper roleBusPk = roleBusinessInfoExportService.getRoleBusinessInfoWrapper(userRoleId);
				
				idList = getIdListByType(configs, chengjiu, roleBusPk.getPkVal());
				cjList.add(new Object[]{types[i], roleBusPk.getPkVal(),idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			case  GameConstants.CJ_KILLMONTER://累计击杀怪物
				
				idList = getIdListByType(configs, chengjiu, data.getKillMonterCount());
				
				cjList.add(new Object[]{types[i],data.getKillMonterCount(),idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
			
			case  GameConstants.CJ_VIP://vip等级
				RoleVipWrapper vipInfo = roleVipInfoExportService.getRoleVipInfo(userRoleId);
				idList = getIdListByType(configs, chengjiu, vipInfo.getVipLevel());
				cjList.add(new Object[]{types[i],vipInfo.getVipLevel(),idList.toArray(),getRIdListByType(configs, chengjiu).toArray()});
				break;
				
			default:
				break;
			}
			
		}
		return new Object[]{1,cjList.toArray()};
	}
	
	/**
	 * 获取一领奖id
	 * @param configs
	 * @param chengjiu
	 * @param number
	 * @return
	 */
	private List<Integer> getRIdListByType(List<ChengJiuPeiZhiConfig> configs,RoleChengjiu chengjiu) {
		List<Integer> list = new ArrayList<>();
		for (int j = 0; j < configs.size(); j++) {
			ChengJiuPeiZhiConfig config = configs.get(j);
			if(isLinJiang(chengjiu.getReceiveId(), config.getId())){
				list.add(config.getId());
			}
		}
		return list;
	}

	private List<Integer> getIdListByType(List<ChengJiuPeiZhiConfig> configs,RoleChengjiu chengjiu,int number){
		List<Integer> list = new ArrayList<>();
		for (int j = 0; j < configs.size(); j++) {
			ChengJiuPeiZhiConfig config = configs.get(j);
			if(isJiHuo(chengjiu.getChengjiuId(), config.getId())){
				list.add(config.getId());
			}else{
				//判断是否已经达到条件
				if(number >= config.getNumber()){
					jiHuo(chengjiu, config);
					list.add(config.getId());
				}
			}
		}
		
		return list;
	}
	private List<Integer> getIdListByTypeNoJindu(List<ChengJiuPeiZhiConfig> configs,RoleChengjiu chengjiu){
		List<Integer> list = new ArrayList<>();
		for (int j = 0; j < configs.size(); j++) {
			ChengJiuPeiZhiConfig config = configs.get(j);
			if(isJiHuo(chengjiu.getChengjiuId(), config.getId())){
				list.add(config.getId());
			}
		}
		
		return list;
	}
	
	
	/**
	 * 检测并激活成就
	 * @param userRoleId
	 * @param type  成就类型
	 * @param number 到达的成就值或者是增长的成就值（视成就类型而定）
	 */
	public void jianCeAndJiHuoCheng(Long userRoleId,int type,int cjValue){
		RoleChengjiu chengjiu = getRoleChengJiu(userRoleId);
		RoleChengjiuData data = getRoleChengJiuData(userRoleId);
		List<ChengJiuPeiZhiConfig> configs = null;
		ChengJiuPeiZhiConfig config = null;
		
		switch (type) {
		
		case  GameConstants.CJ_LEVEL://升级触发
			configs = chengJiuPeiZhiConfigExportServiceImpl.loadByTypeAndNumber(type,cjValue);
			if(configs == null || configs.size() <= 0){
				return;
			}
			//有数据则表示类型这个数字的成就存在
			config = configs.get(0);
			//激活成就
			jiHuo(chengjiu, config);
			//推送客户端激活成就
			tuiSong(userRoleId, config.getId());
			break;
		case  GameConstants.CJ_ZPLUS:
			/*configs = chengJiuPeiZhiConfigExportServiceImpl.loadByType(type);
			if(configs == null || configs.size() <= 0){
				return;
			}
			for (int j = 0; j < configs.size(); j++) {
				config = configs.get(j);
				if(isJiHuo(chengjiu.getChengjiuId(), config.getId())){
					continue;
				}else{
					//判断是否已经达到条件
					if(cjValue >= config.getNumber()){
						jiHuo(chengjiu, config.getId());
						//推送客户端激活成就
						tuiSong(userRoleId, config.getId());
					}else{
						break;
					}
				}
			}*/
			chageType(chengjiu, type, cjValue);
			break;
		case  GameConstants.CJ_QIANGHUA:
			int qhLevel = roleBagExportService.getAllEquipsQHLevel(userRoleId);
			chageType(chengjiu, type, qhLevel);
			break;
		case  GameConstants.CJ_ZIZHUANG:
			int zi = roleBagExportService.getZiZhuangCount(userRoleId);
			chageType(chengjiu, type, zi);
			break;
		case  GameConstants.CJ_CHENGZHUANG:
			int cheng = roleBagExportService.getChengZhuangCount(userRoleId);
			chageType(chengjiu, type, cheng);
			break;
		case  GameConstants.CJ_TAOZHUANG:
			int tao = roleBagExportService.getTaoZhuangCount(userRoleId);
			chageType(chengjiu, type, tao);
			break;
		case  GameConstants.CJ_ZUOQI:
			configs = chengJiuPeiZhiConfigExportServiceImpl.loadByTypeAndNumber(type,cjValue);
			if(configs == null || configs.size() <= 0){
				return;
			}
			config = configs.get(0);
			jiHuo(chengjiu, config);
			tuiSong(userRoleId, config.getId());
			break;
			
		case  GameConstants.CJ_CHIBANG:
			configs = chengJiuPeiZhiConfigExportServiceImpl.loadByTypeAndNumber(type,cjValue);
			if(configs == null || configs.size() <= 0){
				return;
			}
			config = configs.get(0);
			jiHuo(chengjiu, config);
			tuiSong(userRoleId, config.getId());
			break;
		case  GameConstants.CJ_XIANJIAN:
			configs = chengJiuPeiZhiConfigExportServiceImpl.loadByTypeAndNumber(type,cjValue);
			if(configs == null || configs.size() <= 0){
				return;
			}
			config = configs.get(0);
			jiHuo(chengjiu, config);
			tuiSong(userRoleId, config.getId());
			break;
		case  GameConstants.CJ_ZHANJIA:
			configs = chengJiuPeiZhiConfigExportServiceImpl.loadByTypeAndNumber(type,cjValue);
			if(configs == null || configs.size() <= 0){
				return;
			}
			config = configs.get(0);
			jiHuo(chengjiu, config);
			tuiSong(userRoleId, config.getId());
			break;
		case  GameConstants.CJ_SHENBING:
			int sb = shenQiExportService.getActivatedShenqiNum(userRoleId);
			configs = chengJiuPeiZhiConfigExportServiceImpl.loadByTypeAndNumber(type,sb);
			if(configs == null || configs.size() <= 0){
				return;
			}
			config = configs.get(0);
			jiHuo(chengjiu, config);
			tuiSong(userRoleId, config.getId());
			break;
		case  GameConstants.CJ_JICHUJINENG:
			
			break;
		case  GameConstants.CJ_BEIDONGJINENG:
			
			break;
		case  GameConstants.CJ_DAYTASKCOUNT:
			/*configs = chengJiuPeiZhiConfigExportServiceImpl.loadByTypeAndNumber(type,data.getDayTaskCount()+cjValue);
			if(configs == null || configs.size() <= 0){
				data.setDayTaskCount(data.getDayTaskCount() + cjValue);
				return;
			}
			config = configs.get(0);
			jiHuo(chengjiu, config);
			tuiSong(userRoleId, config.getId());*/
			data.setDayTaskCount(data.getDayTaskCount()+ cjValue);
			chageType(chengjiu, type, data.getDayTaskCount());
			break;
		case  GameConstants.CJ_GUILDTASKCOUNT:
			/*configs = chengJiuPeiZhiConfigExportServiceImpl.loadByTypeAndNumber(type,data.getGuildTaskCount()+cjValue);
			if(configs == null || configs.size() <= 0){
				data.setGuildTaskCount(data.getGuildTaskCount() + cjValue);
				return;
			}
			config = configs.get(0);
			jiHuo(chengjiu, config);
			tuiSong(userRoleId, config.getId());*/
			data.setGuildTaskCount(data.getGuildTaskCount()+ cjValue);
			chageType(chengjiu, type, data.getGuildTaskCount());
			break;
		case  GameConstants.CJ_GOUYUFUBENCOUNT:
			configs = chengJiuPeiZhiConfigExportServiceImpl.loadByTypeAndNumber(type,data.getGouyuFubenCount()+cjValue);
			if(configs == null || configs.size() <= 0){
				data.setGouyuFubenCount(data.getGouyuFubenCount() + cjValue);
				break;
			}
			data.setGouyuFubenCount(data.getGouyuFubenCount() + cjValue);
			config = configs.get(0);
			jiHuo(chengjiu, config);
			tuiSong(userRoleId, config.getId());
			break;
		case  GameConstants.CJ_DIHUOFUBENCOUNT:
			configs = chengJiuPeiZhiConfigExportServiceImpl.loadByTypeAndNumber(type,data.getDihuoFubenCount()+cjValue);
			if(configs == null || configs.size() <= 0){
				data.setDihuoFubenCount(data.getDihuoFubenCount() + cjValue);
				break;
			}
			data.setDihuoFubenCount(data.getDihuoFubenCount() + cjValue);
			config = configs.get(0);
			jiHuo(chengjiu, config);
			tuiSong(userRoleId, config.getId());
			break;
		case  GameConstants.CJ_NANWUYUEFUBENCOUNT:
			int nanwuyue = fubenExportService.getShouhuFubenMax(userRoleId);
			chageType(chengjiu, type, nanwuyue);
			break;
		case  GameConstants.CJ_SANSHENGFUBENCOUNT:
			configs = chengJiuPeiZhiConfigExportServiceImpl.loadByTypeAndNumber(type,data.getSnashengFubenCount()+cjValue);
			if(configs == null || configs.size() <= 0){
				data.setSnashengFubenCount(data.getSnashengFubenCount() + cjValue);
				break;
			}
			data.setSnashengFubenCount(data.getSnashengFubenCount() + cjValue);
			config = configs.get(0);
			jiHuo(chengjiu, config);
			tuiSong(userRoleId, config.getId());
			break;
		case  GameConstants.CJ_XIANMOBANGCENG:
			int pataCeng = fubenExportService.getPataMaxCeng(userRoleId);
			chageType(chengjiu, type, pataCeng);
			break;
		case  GameConstants.CJ_XIANMOBANG:
			configs = chengJiuPeiZhiConfigExportServiceImpl.loadByTypeAndNumber(type,data.getXianmobangCount()+cjValue);
			if(configs == null || configs.size() <= 0){
				data.setXianmobangCount(data.getXianmobangCount() + cjValue);
				break;
			}
			data.setXianmobangCount(data.getXianmobangCount() + cjValue);
			config = configs.get(0);
			jiHuo(chengjiu, config);
			tuiSong(userRoleId, config.getId());
			break;
		case  GameConstants.CJ_CHENGBIAO:
			configs = chengJiuPeiZhiConfigExportServiceImpl.loadByTypeAndNumber(type,data.getChengBiaoCount()+cjValue);
			if(configs == null || configs.size() <= 0){
				data.setChengBiaoCount(data.getChengBiaoCount() + cjValue);
				break;
			}
			data.setChengBiaoCount(data.getChengBiaoCount() + cjValue);
			config = configs.get(0);
			jiHuo(chengjiu, config);
			tuiSong(userRoleId, config.getId());
			break;
		case  GameConstants.CJ_YEWAIBOSS:
			configs = chengJiuPeiZhiConfigExportServiceImpl.loadByTypeAndNumber(type,data.getKillBossCount()+cjValue);
			if(configs == null || configs.size() <= 0){
				data.setKillBossCount(data.getKillBossCount() + cjValue);
				break;
			}
			data.setKillBossCount(data.getKillBossCount() + cjValue);
			config = configs.get(0);
			jiHuo(chengjiu, config);
			tuiSong(userRoleId, config.getId());
			break;
		case  GameConstants.CJ_DATI:
			chageTypeNoJindu(chengjiu, type, cjValue);
			break;
		case  GameConstants.CJ_XIZAOFEIZAO:
			configs = chengJiuPeiZhiConfigExportServiceImpl.loadByTypeAndNumber(type,data.getXizaoFeizaoCount()+cjValue);
			if(configs == null || configs.size() <= 0){
				data.setXizaoFeizaoCount(data.getXizaoFeizaoCount() + cjValue);
				break;
			}
			data.setXizaoFeizaoCount(data.getXizaoFeizaoCount() + cjValue);
			config = configs.get(0);
			jiHuo(chengjiu, config);
			tuiSong(userRoleId, config.getId());
			break;
		case  GameConstants.CJ_SHICHANGMAI:
			configs = chengJiuPeiZhiConfigExportServiceImpl.loadByTypeAndNumber(type,data.getShichangChushouCount()+cjValue);
			if(configs == null || configs.size() <= 0){
				data.setShichangChushouCount(data.getShichangChushouCount() + cjValue);
				break;
			}
			data.setShichangChushouCount(data.getShichangChushouCount() + cjValue);
			config = configs.get(0);
			jiHuo(chengjiu, config);
			tuiSong(userRoleId, config.getId());
			break;
		case  GameConstants.CJ_YINLIANG:
			chageType(chengjiu, type, cjValue);
			break;
		case  GameConstants.CJ_XIAOFEIYUANBAO:
			data.setDanriXiaofeiNumber(data.getDanriXiaofeiNumber() + cjValue);
			chageType(chengjiu, type, data.getDanriXiaofeiNumber());
			/*configs = chengJiuPeiZhiConfigExportServiceImpl.loadByTypeAndNumber(type,data.getDanriXiaofeiNumber()+cjValue);
			if(configs == null || configs.size() <= 0){
				
				return;
			}
			config = configs.get(0);
			jiHuo(chengjiu, config);
			tuiSong(userRoleId, config.getId());*/
			break;
			
		case  GameConstants.CJ_DANCHONGZHIYUANBAO:
			chageType(chengjiu, type, cjValue);
			break;
		case  GameConstants.CJ_LEICHONGZHIYUANBAO:
			/*configs = chengJiuPeiZhiConfigExportServiceImpl.loadByTypeAndNumber(type,data.getLeijiRechargeNumber()+cjValue);
			if(configs == null || configs.size() <= 0){
				data.setLeijiRechargeNumber(data.getLeijiRechargeNumber() + cjValue);
				return;
			}
			config = configs.get(0);
			jiHuo(chengjiu, config);
			tuiSong(userRoleId, config.getId());*/
			data.setLeijiRechargeNumber(data.getLeijiRechargeNumber()+ cjValue);
			chageType(chengjiu, type, data.getLeijiRechargeNumber());
			break;
			
		case  GameConstants.CJ_TIJUNBAOHE:
			/*configs = chengJiuPeiZhiConfigExportServiceImpl.loadByTypeAndNumber(type,data.getLeijiXunbaoCount()+cjValue);
			if(configs == null || configs.size() <= 0){
				data.setLeijiXunbaoCount(data.getLeijiXunbaoCount() + cjValue);
				return;
			}
			config = configs.get(0);
			jiHuo(chengjiu, config);
			tuiSong(userRoleId, config.getId());*/
			data.setLeijiXunbaoCount(data.getLeijiXunbaoCount()+ cjValue);
			chageType(chengjiu, type, data.getLeijiXunbaoCount());
			break;
		case  GameConstants.CJ_QIANDAOCOUNT:
			int assignAll = assignExportService.getAssignAll(userRoleId);
			
			chageType(chengjiu, type, assignAll);
			break;
		case  GameConstants.CJ_PKVALUE:
			chageType(chengjiu, type, cjValue);
			break;
		case  GameConstants.CJ_KILLMONTER:
			
			configs = chengJiuPeiZhiConfigExportServiceImpl.loadByTypeAndNumber(type,data.getKillMonterCount()+cjValue);
			if(configs == null || configs.size() <= 0){
				data.setKillMonterCount(data.getKillMonterCount() + cjValue);
				break;
			}
			data.setKillMonterCount(data.getKillMonterCount() + cjValue);
			config = configs.get(0);
			jiHuo(chengjiu, config);
			tuiSong(userRoleId, config.getId());
			break;
		case  GameConstants.CJ_VIP://vip
			configs = chengJiuPeiZhiConfigExportServiceImpl.loadByTypeAndNumber(type,cjValue);
			if(configs == null || configs.size() <= 0){
				return;
			}
			//有数据则表示类型这个数字的成就存在
			config = configs.get(0);
			//激活成就
			jiHuo(chengjiu, config);
			//推送客户端激活成就
			tuiSong(userRoleId, config.getId());
			break;
			
			
		case  GameConstants.CJ_WUQI:
			configs = chengJiuPeiZhiConfigExportServiceImpl.loadByTypeAndNumber(type,cjValue);
			if(configs == null || configs.size() <= 0){
				return;
			}
			config = configs.get(0);
			jiHuo(chengjiu, config);
			tuiSong(userRoleId, config.getId());
			break;
			 
		default:
			break;
		}
		data.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		roleChengjiuDataDao.cacheUpdate(data, userRoleId);
		
	}
	
	/**
	 * 会上下变化的成就类型检测激活
	 */
	private void chageType(RoleChengjiu chengjiu,int type,int cjValue){
		List<ChengJiuPeiZhiConfig> configs = chengJiuPeiZhiConfigExportServiceImpl.loadByType(type);
		if(configs == null || configs.size() <= 0){
			return;
		}
		for (int j = 0; j < configs.size(); j++) {
			ChengJiuPeiZhiConfig config = configs.get(j);
			if(isJiHuo(chengjiu.getChengjiuId(), config.getId())){
				continue;
			}else{
				//判断是否已经达到条件
				if(cjValue >= config.getNumber()){
					jiHuo(chengjiu, config);
					//推送客户端激活成就
					tuiSong(chengjiu.getUserRoleId(), config.getId());
				}
			}
		}
	}
	/**
	 * 会上下变化但指定的的成就类型检测激活
	 */
	private void chageTypeNoJindu(RoleChengjiu chengjiu,int type,int cjValue){
		List<ChengJiuPeiZhiConfig> configs = chengJiuPeiZhiConfigExportServiceImpl.loadByType(type);
		if(configs == null || configs.size() <= 0){
			return;
		}
		for (int j = 0; j < configs.size(); j++) {
			ChengJiuPeiZhiConfig config = configs.get(j);
			if(isJiHuo(chengjiu.getChengjiuId(), config.getId())){
				continue;
			}else{
				//判断是否已经达到条件
				if(cjValue == config.getNumber()){
					jiHuo(chengjiu, config);
					//推送客户端激活成就
					tuiSong(chengjiu.getUserRoleId(), config.getId());
				}
			}
		}
	}
	
	/**
	 * 推送成就变化信息
	 */
	private void tuiSong(Long userRoleId,int id){
		BusMsgSender.send2One(userRoleId,ClientCmdType.CHENGJIU_TUISONG,  new Object[]{getChengJiuValue(userRoleId),id});
	}
	
	/**
	 * 领奖
	 * @param userRoleId
	 * @param configId
	 */
	public Object[] lingjiang(Long userRoleId, int configId) {
		ChengJiuPeiZhiConfig config = chengJiuPeiZhiConfigExportServiceImpl.loadById(configId);
		if(config == null || config.getItems() == null){
			ChuanQiLog.error("config is not exits");
			return AppErrorCode.NO_FIND_CONFIG;
		}
		
		RoleChengjiu chengjiu = getRoleChengJiu(userRoleId);
		String cjId = chengjiu.getChengjiuId();
		//保险起见  先判断是否已经激活过这个成就
		if(!isJiHuo(cjId, configId)){
			ChuanQiLog.error("chenghao is not jihuo");
			return AppErrorCode.NO_FIND_CONFIG;
		}
		String receiveId = chengjiu.getReceiveId() == null ? "":chengjiu.getReceiveId();
		if(isLinJiang(receiveId, configId)){
			ChuanQiLog.error("chenghao is not jihuo");
			return AppErrorCode.GET_ALREADY_ERROR;
		}
		
		Map<String,Integer> items = config.getItems();
		Object[] code =  roleBagExportService.checkPutGoodsAndNumberAttr(items, userRoleId);
		// 背包空间不足 请先清理背包
		if (code != null) {
			return code;
		}
		
		chengjiu.setReceiveId(receiveId+configId+",");
		roleChengjiuDao.cacheUpdate(chengjiu, userRoleId);
		
		roleBagExportService.putGoodsAndNumberAttr(config.getItems(), userRoleId, GoodsSource.CHENGJIU_AWARD,
				LogPrintHandle.GET_CJ_GIFT,LogPrintHandle.GBZ_CJ_GIFT, true);
		
		return  new Object[]{AppErrorCode.SUCCESS,configId};
	}
}