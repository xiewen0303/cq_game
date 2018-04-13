package com.junyou.bus.jingji.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.chenghao.service.ChenghaoService;
import com.junyou.bus.chengjiu.server.RoleChengJiuService;
import com.junyou.bus.chibang.service.ChiBangService;
import com.junyou.bus.equip.service.EquipService;
import com.junyou.bus.huajuan2.service.Huajuan2Service;
import com.junyou.bus.jewel.service.JewelService;
import com.junyou.bus.jingji.export.ZhanLiDuiBiConfigExportService;
import com.junyou.bus.jingji.util.FightPowerType;
import com.junyou.bus.linghuo.service.LingHuoService;
import com.junyou.bus.lingjing.service.LingJingService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.service.TangbaoService;
import com.junyou.bus.shenqi.service.ShenQiService;
import com.junyou.bus.skill.service.RoleSkillService;
import com.junyou.bus.tianyu.service.TianYuService;
import com.junyou.bus.wuqi.service.WuQiService;
import com.junyou.bus.xianjian.service.XianjianService;
import com.junyou.bus.yaoshen.service.YaoshenFumoService;
import com.junyou.bus.yaoshen.service.YaoshenHunPoService;
import com.junyou.bus.yaoshen.service.YaoshenMoYinService;
import com.junyou.bus.yaoshen.service.YaoshenService;
import com.junyou.bus.zhanjia.service.ZhanJiaService;
import com.junyou.bus.zuoqi.service.ZuoQiService;
import com.junyou.err.AppErrorCode;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.guild.export.GuildExportService;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.dao.RoleInfoDao;
import com.junyou.utils.common.CovertObjectUtil;
import com.kernel.spring.SpringApplicationContext;

@Service
public class FightPowerService {

	@Autowired
	private ZhanLiDuiBiConfigExportService zhanLiDuiBiConfigExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private RoleInfoDao roleInfoDao;

	private static Map<Integer,Class<? extends IFightVal>> fightValServices = new TreeMap<>();

	static {
		fightValServices.put(FightPowerType.ROLE_LEVEL, RoleExportService.class);
		fightValServices.put(FightPowerType.ROLE_VIP, RoleExportService.class);

		fightValServices.put(FightPowerType.EQUIP_JIE, EquipService.class);
		fightValServices.put(FightPowerType.EQUIP_QH, EquipService.class);
		fightValServices.put(FightPowerType.STONE_XQ, JewelService.class);


		fightValServices.put(FightPowerType.SHENQI_JH, ShenQiService.class);
		fightValServices.put(FightPowerType.SHENQI_XL, ShenQiService.class);
		fightValServices.put(FightPowerType.SHENQI_ZF, ShenQiService.class);

		fightValServices.put(FightPowerType.SKILL_BASE, RoleSkillService.class);
		fightValServices.put(FightPowerType.SKILL_BEIDONG, RoleSkillService.class);
		fightValServices.put(FightPowerType.GUILD_SKILL, RoleSkillService.class);
		fightValServices.put(FightPowerType.EM_SKILL, RoleSkillService.class);

		fightValServices.put(FightPowerType.HXRZ, TangbaoService.class);

		fightValServices.put(FightPowerType.WUQI_JIE, WuQiService.class);
		fightValServices.put(FightPowerType.WUQI_ROLE_LEVE, WuQiService.class);

		fightValServices.put(FightPowerType.ZUOQI_JIE, ZuoQiService.class);
		fightValServices.put(FightPowerType.SWING_JIE, ChiBangService.class);
		fightValServices.put(FightPowerType.MOJIAN_JIE,XianjianService.class);
		fightValServices.put(FightPowerType.MOJIA_JIE,ZhanJiaService.class);
		fightValServices.put(FightPowerType.MOYI_JIE,TianYuService.class);

		fightValServices.put(FightPowerType.HH_WUQI, ZuoQiService.class);
		fightValServices.put(FightPowerType.HH_ZUOQI, ChiBangService.class);
		fightValServices.put(FightPowerType.HH_SWING,XianjianService.class);
		fightValServices.put(FightPowerType.HH_MOJIAN,ZhanJiaService.class);
		fightValServices.put(FightPowerType.HH_MOJIA,TianYuService.class);

		fightValServices.put(FightPowerType.EM_XMSX, YaoshenService.class);
		fightValServices.put(FightPowerType.EM_WZSX, YaoshenService.class);
		fightValServices.put(FightPowerType.EM_HLSX, YaoshenHunPoService.class);
		fightValServices.put(FightPowerType.EM_YJSX, YaoshenMoYinService.class);
		fightValServices.put(FightPowerType.EM_LHSX, YaoshenHunPoService.class);
		fightValServices.put(FightPowerType.EM_YSSX, YaoshenFumoService.class);

		fightValServices.put(FightPowerType.TJ_JH, Huajuan2Service.class);
		fightValServices.put(FightPowerType.TJ_ZB, Huajuan2Service.class);
		fightValServices.put(FightPowerType.TJ_JD, Huajuan2Service.class);

		fightValServices.put(FightPowerType.CJ_JH, RoleChengJiuService.class);
		fightValServices.put(FightPowerType.CH_JH, ChenghaoService.class);
		fightValServices.put(FightPowerType.RYZL_ATTRS_JH, LingJingService.class);


		fightValServices.put(FightPowerType.HZ_JH, LingHuoService.class);
		fightValServices.put(FightPowerType.HZ_ZF, LingHuoService.class);

		fightValServices.put(FightPowerType.BAG_OPEN, GuildExportService.class);
		fightValServices.put(FightPowerType.CK_OPEN, GuildExportService.class);
		fightValServices.put(FightPowerType.GUILD_QZ, GuildExportService.class);
		
		fightValServices.put(FightPowerType.TOTAL_FIGHTVALUE, JingjiService.class);
	}

	public Object fightValCompareParent(long selfRoleId,long targetRoleId){
		List<Object> result = new ArrayList<>();
		HashMap<Integer,Long> selfFightMap = getFightPower(0,selfRoleId);
		HashMap<Integer,Long> targetFightMap = getFightPower(0,targetRoleId);
		
		selfFightMap.remove(FightPowerType.TOTAL_FIGHTVALUE);
		long totalZplus = CovertObjectUtil.obj2long(targetFightMap.remove(FightPowerType.TOTAL_FIGHTVALUE));
		if(totalZplus == 0){
			return AppErrorCode.ROLE_INFO_NOT_FOUND;
		}
		Map<Integer, List<Integer>> configs = zhanLiDuiBiConfigExportService.getAllConfigs();
		for (Map.Entry<Integer, List<Integer>> entry : configs.entrySet()) {
			Integer key = entry.getKey();
			List<Integer> values = entry.getValue();
			long selfV = 0l;
			long targetV = 0l;
			for (Integer id :values) {
				selfV += CovertObjectUtil.obj2long(selfFightMap.get(id));
				targetV += CovertObjectUtil.obj2long(targetFightMap.get(id));
			}
			result.add(new Object[]{key,selfV,targetV});
		}

		return new Object[]{1,result.toArray(),totalZplus};
	}

	public Object fightValCompareChild(int type,long selfRoleId,long targetRoleId){
		List<Object> result = new ArrayList<>();
		HashMap<Integer,Long> selfFightMap = getFightPower(type,selfRoleId);
		HashMap<Integer,Long> targetFightMap = getFightPower(type,targetRoleId);

		List<Integer> configs = zhanLiDuiBiConfigExportService.getChilds(type);
		for (Integer  id : configs) {
			long selfV = CovertObjectUtil.obj2long(selfFightMap.get(id));
			long targetV = CovertObjectUtil.obj2long(targetFightMap.get(id));
			result.add(new Object[]{id,selfV,targetV});
		}
		return new Object[]{1,type,result.toArray()};
	}

	/**
	 *
	 * @param type 0:表示所有的
	 * @param userRoleId
	 * @return
	 */
	public HashMap<Integer,Long> getFightPower(int type,long userRoleId) {
		HashMap<Integer,Long> result = new HashMap<>();

		List<Integer> targetChilds = null;
		if(type == 0){ //表示所有的
			targetChilds = new ArrayList<>(zhanLiDuiBiConfigExportService.getChilds());
		}else{
			targetChilds =  new ArrayList<>(zhanLiDuiBiConfigExportService.getChilds(type));
		}
		targetChilds.add(FightPowerType.TOTAL_FIGHTVALUE);
		//将数据存入文件
		if(publicRoleStateExportService.isPublicOnline(userRoleId)) {
			for (Integer fightPowerType : targetChilds) {
				Class<? extends  IFightVal> fightValClass = fightValServices.get(fightPowerType);
				if(fightValClass == null){
					ChuanQiLog.error("fightPowerType is not implements ,type={}",fightPowerType);
					continue;
				}
				IFightVal fightVal = SpringApplicationContext.getApplicationContext().getBean(fightValClass);
				result.put(fightPowerType,fightVal.getZplus(userRoleId,fightPowerType));
			}
		}else{
			HashMap<Integer,Long> info = (HashMap<Integer,Long>)roleInfoDao.loadFightTypeFromFileDb(userRoleId + "");
			if(null == info){
				ChuanQiLog.error("COMPONENET_ROLE_ATTRIBUTE is not exist,userRoleId={}",userRoleId);
				return result;
			}
			for (Integer fightPowerType : targetChilds) {
				result.put(fightPowerType,CovertObjectUtil.obj2long(info.get(fightPowerType)));
			}
		}

		return result;
	}

	public void offOnline(long userRoleId){
		HashMap<Integer,Long> result = new HashMap<>();

		for (Map.Entry<Integer,Class<? extends IFightVal>> entry : fightValServices.entrySet()) {
			Integer fightPowerType = entry.getKey();
			Class<? extends IFightVal> clazz = entry.getValue();
			IFightVal fightVal = SpringApplicationContext.getApplicationContext().getBean(clazz);
			result.put(fightPowerType,fightVal.getZplus(userRoleId,fightPowerType));
		}
		roleInfoDao.writeFightTypeFromFileDb(result,userRoleId+"");
	}

	public Object fightValCompareParent(Long selfRoleId) {
		List<Object> result = new ArrayList<>();
		HashMap<Integer,Long> selfFightMap = getFightPower(0,selfRoleId);
		
		selfFightMap.remove(FightPowerType.TOTAL_FIGHTVALUE);
		
		Map<Integer, List<Integer>> configs = zhanLiDuiBiConfigExportService.getAllConfigs();
		for (Map.Entry<Integer, List<Integer>> entry : configs.entrySet()) {
			Integer key = entry.getKey();
			List<Integer> values = entry.getValue();
			long selfV = 0l;
			for (Integer id :values) {
				selfV += CovertObjectUtil.obj2long(selfFightMap.get(id));
			}
			result.add(new Object[]{key,selfV});
		}

		return new Object[]{1,result.toArray()};
	}

	public Object fightValCompareChild(int type, Long selfRoleId) {
		List<Object> result = new ArrayList<>();
		HashMap<Integer,Long> selfFightMap = getFightPower(type,selfRoleId);

		List<Integer> configs = zhanLiDuiBiConfigExportService.getChilds(type);
		for (Integer  id : configs) {
			long selfV = CovertObjectUtil.obj2long(selfFightMap.get(id));
			result.add(new Object[]{id,selfV});
		}
		return new Object[]{1,type,result.toArray()};
	}
}