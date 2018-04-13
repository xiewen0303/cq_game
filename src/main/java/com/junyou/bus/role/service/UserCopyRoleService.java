package com.junyou.bus.role.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.account.dao.RoleAccountDao;
import com.junyou.bus.account.entity.RoleAccount;
import com.junyou.bus.bag.dao.RoleBagDao;
import com.junyou.bus.bag.entity.RoleItem;
import com.junyou.bus.chibang.dao.ChiBangInfoDao;
import com.junyou.bus.chibang.entity.ChiBangInfo;
import com.junyou.bus.chongwu.dao.RoleChongwuDao;
import com.junyou.bus.chongwu.entity.RoleChongwu;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.qiling.dao.QiLingInfoDao;
import com.junyou.bus.qiling.entity.QiLingInfo;
import com.junyou.bus.role.dao.TangbaoDao;
import com.junyou.bus.role.dao.UserRoleDao;
import com.junyou.bus.role.entity.Tangbao;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.rolebusiness.dao.RoleBusinessInfoDao;
import com.junyou.bus.rolebusiness.entity.RoleBusinessInfo;
import com.junyou.bus.shenqi.dao.ShenQiInfoDao;
import com.junyou.bus.shenqi.entity.ShenQiInfo;
import com.junyou.bus.shizhuang.dao.RoleShizhuangDao;
import com.junyou.bus.shizhuang.entity.RoleShizhuang;
import com.junyou.bus.skill.dao.RoleSkillDao;
import com.junyou.bus.skill.entity.RoleSkill;
import com.junyou.bus.vip.dao.RoleVipInfoDao;
import com.junyou.bus.vip.entity.RoleVipInfo;
import com.junyou.bus.xianjian.dao.XianJianInfoDao;
import com.junyou.bus.xianjian.entity.XianJianInfo;
import com.junyou.bus.yaoshen.dao.RoleYaoshenDao;
import com.junyou.bus.yaoshen.entity.RoleYaoshen;
import com.junyou.bus.yaoshen.service.export.YaoshenExportService;
import com.junyou.bus.zhanjia.dao.ZhanJiaInfoDao;
import com.junyou.bus.zhanjia.entity.ZhanJiaInfo;
import com.junyou.bus.zuoqi.dao.ZuoQiInfoDao;
import com.junyou.bus.zuoqi.entity.ZuoQiInfo;
import com.junyou.io.GameSession;
import com.junyou.io.global.GameSessionManager;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.kernel.data.accessor.AccessType;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

@Service
public class UserCopyRoleService {

	@Autowired
	private UserRoleService userRoleService;
	@Autowired
	private YaoshenExportService yaoshenExportService;
	@Autowired
	private RoleYaoshenDao roleYaoshenDao;
	@Autowired
	private UserRoleDao userRoleDao;
	@Autowired
	private RoleAccountDao roleAccountDao;
	@Autowired
	private RoleVipInfoDao roleVipInfoDao;
	@Autowired
	private ZuoQiInfoDao zuoQiInfoDao;
	@Autowired
	private ChiBangInfoDao chiBangInfoDao;
	@Autowired
	private TangbaoDao tangbaoDao;
	@Autowired
	private ZhanJiaInfoDao zhanJiaInfoDao;
	@Autowired
	private XianJianInfoDao xianJianInfoDao;
	@Autowired
	private QiLingInfoDao qiLingInfoDao;
	@Autowired
	private RoleChongwuDao roleChongwuDao;
	@Autowired
	private RoleShizhuangDao roleShizhuangDao;
	@Autowired
	private ShenQiInfoDao shenqiInfoDao;

	@Autowired
	private RoleBagDao roleBagDao;

	@Autowired
	private RoleSkillDao roleSkillDao;
	@Autowired
	private RoleBusinessInfoDao roleBusinessInfoDao;


	/**
	 * 越南版 根据一个号负责多个号 只需要以下功能： 妖神霸体 + 装备（包括强化等级）+神器+人物等级+御剑+翅膀+糖宝+天工+天裳+器灵+宠物+时装 +技能
	 * //[ [ [0:String(账号),1:String(角色名字),2:int(角色配置ID)],... ] ]
	 */
	public Map<String, String> copyRoleByRole(Map<String, Object> param) {
		
		ChuanQiLog.error("copyRoleByRole，data={}", param.toString()); //查看信息用
		
		Map<String, String> map = new HashMap<>();
		map.put("1", null);
		
		if(!PlatformConstants.isYueNan()){
			map.put("1", "platform is error , not vtcgame!");
		}
		
		String serverId = CovertObjectUtil.obj2StrOrNull(param.get("serverId"));
		String webData = CovertObjectUtil.obj2StrOrNull(param.get("userInfo"));
		JSONObject allJsonObjectData = (JSONObject) JSON.parse(webData);

		JSONObject copyRoleData = allJsonObjectData.getJSONObject("user_0");
		String copyUserRoleId = copyRoleData.getString("user_id");// 主账号

		// String serverId ="146";
		// String copyUserRoleId ="lxn";
		UserRole copyUserRole = userRoleService.getRoleFromDb(copyUserRoleId, serverId);
		if (copyUserRole == null) {
			ChuanQiLog.error("host userRole is not exist,copyUserRoleId={}",copyUserRoleId);
			map.put("1", "host userRole is not exist,copyUserRoleId="+copyUserRoleId);
			return map;
		}
		GameSession session = GameSessionManager.getInstance().getSession4UserId(copyUserRoleId, serverId);
		if (session == null) {
			ChuanQiLog.error("host userRole is not login,copyUserRoleId={}",copyUserRoleId);
			map.put("1", "host userRole is not login,copyUserRoleId="+copyUserRoleId);
			return map;
		}
		boolean isChenmi = session.isChenmi();
		String ip = session.getIp();

		StringBuffer sBuffer = new StringBuffer();
		int size = allJsonObjectData.size();
		// int size = 2;
		for (int i = 1; i < size; i++) {
			JSONObject strJsonObject = allJsonObjectData.getJSONObject("user_" + i);
			String userId = strJsonObject.getString("user_id");
			String name = strJsonObject.getString("role_name");
			// String userId = "hopbaohtc1";
			// String name = "HopBa1";
			if (userRoleService.getRoleFromDb(userId, serverId) == null) {
				try {
					Object[] ret = userRoleService.createRoleFromDb(userId, name, copyUserRole.getConfigId(), isChenmi, serverId, ip, false,null);
					if (ret != null && (int) ret[0] == 0) {
						// copy失败的号
						sBuffer.append(userId + ",");
						ChuanQiLog.error("method=copyRoleByRole,copyRoleByRole is error ,userId={},code={}", userId, ret[1]);
						continue;
					}
					Object[] data = (Object[]) ret[1];
					long newUserRoleId = (long) data[1]; // 新账号userRoleId

					// 角色
					UserRole userRole = copyUserRole.copy();
					userRole.setId(newUserRoleId);
					userRole.setUserId(userId);
					userRole.setName(name);
					userRoleDao.update(userRole, userRole.getId(), AccessType.getDirectDbType());

					// 账户
					RoleAccount roleAccount = roleAccountDao.initRoleAccountFromDb(copyUserRole.getId());
					if (roleAccount != null) {
						roleAccount = roleAccount.copy();
						roleAccount.setUserRoleId(newUserRoleId);
						roleAccount.setUserId(userId);
						roleAccountDao.update(roleAccount, newUserRoleId, AccessType.getDirectDbType());
					}

					// vip
					List<RoleVipInfo> roleVipInfos = roleVipInfoDao.initRoleVipInfo(copyUserRole.getId());
					if (roleVipInfos != null && !roleVipInfos.isEmpty()) {
						RoleVipInfo roleVipInfo = roleVipInfos.get(0);
						RoleVipInfo newRoleVipInfo = roleVipInfo.copy();
						newRoleVipInfo.setUserRoleId(newUserRoleId);
						newRoleVipInfo.setVipExp(roleVipInfo.getVipExp());
						newRoleVipInfo.setVipLevel(roleVipInfo.getVipLevel());
						roleVipInfoDao.update(newRoleVipInfo, newUserRoleId, AccessType.getDirectDbType());
					}

					//role_business_info
					RoleBusinessInfo roleBusinessInfo = roleBusinessInfoDao.getRoleBusinessInfoForDB(copyUserRole.getId());
					if(roleBusinessInfo!=null){
						roleBusinessInfo = roleBusinessInfo.copy();
						roleBusinessInfo.setUserRoleId(newUserRoleId);
						roleBusinessInfoDao.update(roleBusinessInfo, newUserRoleId, AccessType.getDirectDbType());
					}
					// 妖神霸体
					RoleYaoshen roleYaoshen = yaoshenExportService.initRoleYaoshen(copyUserRole.getId());
					if (roleYaoshen != null) {
						roleYaoshen = roleYaoshen.copy();
						roleYaoshen.setUserRoleId(newUserRoleId);
						roleYaoshenDao.insert(roleYaoshen, newUserRoleId, AccessType.getDirectDbType());
					}

					// 御剑
					ZuoQiInfo zuoQiInfo = zuoQiInfoDao.initZuoQiInfo(copyUserRole.getId());
					if (zuoQiInfo != null) {
						zuoQiInfo = zuoQiInfo.copy();
						zuoQiInfo.setUserRoleId(newUserRoleId);
						zuoQiInfo.setIsGetOn(0);//默认不上御剑
						zuoQiInfoDao.insert(zuoQiInfo, newUserRoleId, AccessType.getDirectDbType());
					}

					// 翅膀
					ChiBangInfo chiBangInfo = chiBangInfoDao.initChiBangInfo(copyUserRole.getId());
					if (chiBangInfo != null) {
						chiBangInfo = chiBangInfo.copy();
						chiBangInfo.setUserRoleId(newUserRoleId);
						chiBangInfoDao.insert(chiBangInfo, newUserRoleId, AccessType.getDirectDbType());
					}

					// 糖宝
					List<Tangbao> tangbaos = tangbaoDao.initTangbao(copyUserRole.getId());
					if (tangbaos != null && !tangbaos.isEmpty()) {
						Tangbao tangbao = tangbaos.get(0);
						tangbao = tangbao.copy();
						tangbao.setUserRoleId(newUserRoleId);
						tangbaoDao.insert(tangbao, newUserRoleId, AccessType.getDirectDbType());
					}

					// 天裳
					ZhanJiaInfo zhanJiaInfo = zhanJiaInfoDao.initXianJianInfo(copyUserRole.getId());
					if (zhanJiaInfo != null) {
						zhanJiaInfo = zhanJiaInfo.copy();
						zhanJiaInfo.setUserRoleId(newUserRoleId);
						zhanJiaInfoDao.insert(zhanJiaInfo, newUserRoleId, AccessType.getDirectDbType());

					}
					// 天工
					XianJianInfo xianJianInfo = xianJianInfoDao.initXianJianInfo(copyUserRole.getId());
					if (xianJianInfo != null) {
						xianJianInfo = xianJianInfo.copy();
						xianJianInfo.setUserRoleId(newUserRoleId);
						xianJianInfoDao.insert(xianJianInfo, newUserRoleId, AccessType.getDirectDbType());
					}

					// 器灵
					QiLingInfo qiLingInfo = qiLingInfoDao.initQiLingInfo(copyUserRole.getId());
					if (qiLingInfo != null) {
						qiLingInfo = qiLingInfo.copy();
						qiLingInfo.setUserRoleId(newUserRoleId);
						qiLingInfoDao.insert(qiLingInfo, newUserRoleId, AccessType.getDirectDbType());
					}

					// 宠物
					List<RoleChongwu> roleChongwus = roleChongwuDao.initRoleChongwu(copyUserRole.getId());
					if (roleChongwus != null && !roleChongwus.isEmpty()) {
						for (RoleChongwu roleChongwu : roleChongwus) {
							roleChongwu = roleChongwu.copy();
							roleChongwu.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
							roleChongwu.setUserRoleId(newUserRoleId);
							roleChongwuDao.insert(roleChongwu, newUserRoleId, AccessType.getDirectDbType());
						}
					}

					// 时装
					List<RoleShizhuang> roleShizhuangs = roleShizhuangDao.initRoleShizhuang(copyUserRole.getId());
					if (roleShizhuangs != null && !roleShizhuangs.isEmpty()) {
						for (RoleShizhuang roleShizhuang : roleShizhuangs) {
							roleShizhuang = roleShizhuang.copy();
							roleShizhuang.setUserRoleId(newUserRoleId);
							roleShizhuang.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
							roleShizhuangDao.insert(roleShizhuang, newUserRoleId, AccessType.getDirectDbType());
						}
					}

					// 神器
					List<ShenQiInfo> shenQiInfos = shenqiInfoDao.initShenQiInfo(copyUserRole.getId());
					if (shenQiInfos != null && !shenQiInfos.isEmpty()) {
						for (ShenQiInfo shenQiInfo : shenQiInfos) {
							shenQiInfo = shenQiInfo.copy();
							shenQiInfo.setUserRoleId(newUserRoleId);
							shenQiInfo.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
							shenqiInfoDao.insert(shenQiInfo, newUserRoleId, AccessType.getDirectDbType());
						}
					}

					// 装备
					List<RoleItem> roeItems = roleBagDao.initAll(copyUserRole.getId());
					if (roeItems != null && !roeItems.isEmpty()) {
						for (RoleItem roleItem : roeItems) {
							roleItem = roleItem.copy();
							roleItem.setUserRoleId(newUserRoleId);
							roleItem.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
							roleBagDao.insert(roleItem, newUserRoleId, AccessType.getDirectDbType());
						}
					}
					// 技能
					List<RoleSkill> roleSkills = roleSkillDao.initRoleSkill(copyUserRole.getId());
					if (roleSkills != null && !roleSkills.isEmpty()) {
						for (RoleSkill roleSkill : roleSkills) {
							roleSkill = roleSkill.copy();
							roleSkill.setUserRoleId(newUserRoleId);
							roleSkill.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
							roleSkillDao.insert(roleSkill, newUserRoleId, AccessType.getDirectDbType());
						}
					}

				} catch (Exception e) {
					ChuanQiLog.error("method=copyRoleByRole,copyRoleByRole is error ,userId={},error={}", userId, e);
				}
			} else {
				ChuanQiLog.error("method=copyRoleByRole,userId={},this new role  is exist already", userId);
				// copy失败的号
				sBuffer.append(userId + ",");

			}
		}
		if (sBuffer.length() > 0) {
			map.put("1", sBuffer.toString());
		}
		ChuanQiLog.error("return web map data={}", map.toString());
		return map;
	}

}
