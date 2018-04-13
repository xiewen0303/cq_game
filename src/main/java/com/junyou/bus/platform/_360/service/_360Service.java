package com.junyou.bus.platform._360.service;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.entity.RoleAccount;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.platform._360.util._360Util;
import com.junyou.bus.platform.common.dao.RolePlatformInfoDao;
import com.junyou.bus.platform.common.entity.RolePlatformInfo;
import com.junyou.bus.platform.common.service.PtCommonService;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.configure.export.PtCommonPublicConfig;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.http.HttpClientMocker;
import com.junyou.utils.math.BitOperationUtil;
import com.junyou.utils.md5.Md5Utils;

@Service
public class _360Service {
	@Autowired
	private PtCommonService ptCommonService;

	@Autowired
	private RoleExportService roleExportService;

	@Autowired
	private RolePlatformInfoDao rolePlatformInfoDao;
	@Autowired
	private AccountExportService accountExportService;

	/**
	 * 获取玩家在360平台领取礼包情况
	 * 
	 * @param userRoleId
	 * @return
	 */
	public Integer getStateByRoleId(Long userRoleId) {

		int state = ptCommonService.getStateByRoleId(userRoleId);
		//先把分享礼包的那一位状态设置为0
		state = BitOperationUtil.chanageStateZero(state, 3);
		return state;
	}

	/**
	 * position = １领取360加速球奖励 |　２领取３６０游戏大厅奖励
	 * 
	 * @param userRoleId
	 * @param position
	 * @return
	 */
	public Object[] getReward(Long userRoleId, int position) {
		// 判断背包是否已满
		PtCommonPublicConfig commonConfig = get360PublicConfig(position);
		if (commonConfig == null) {
			return AppErrorCode.PLATFORM_DATA_ERROR;
		}
		String giftType = position == 1 ? PlatformPublicConfigConstants._360_JIASUQIU_LB : PlatformPublicConfigConstants._360_YOUXIDATING_LB;

		return ptCommonService.updateBagAndState(userRoleId, commonConfig.getItems().get("item"), position - 1, giftType, false);
	}

	/**
	 * 360加速球 + 360游戏大厅 + 特权等级礼包 + 分享礼包
	 * 
	 * @return
	 */
	private PtCommonPublicConfig get360PublicConfig(int type) {
		String flag = null;
		if (type == 2) {
			flag = PlatformPublicConfigConstants._360_YOUXIDATING_LB;
		} else if (type == 1) {
			flag = PlatformPublicConfigConstants._360_JIASUQIU_LB;
		} else if (type == 3) {
			flag = PlatformPublicConfigConstants._360_TEQUAN_LB;
		} else if (type == 4) {
			flag = PlatformPublicConfigConstants._360_TEQUAN_SHARE_LB;
		}
		return ptCommonService.getConfigByModName(flag);
	}

	// =======================360特权==========================================
	/**
	 * 获取360平台特权礼包领取情况 360平台未提供获取当前会员等级的接口，暂时就不传给客户端，客户端隐藏会员当前等级
	 * 
	 * @param userRoleId
	 * @return
	 */
	public Object[] getTequanStateByRoleId(Long userRoleId) {

		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if (role == null) {
			return AppErrorCode.ROLE_IS_NULL;
		}
		String qid = role.getUserId();
		String skey = ChuanQiConfigUtil.getPlatformServerId();
		// 请求下活动是否结束或者为上线 ,用等级等于1的会员起请求下接口 主要是看这个活动是否过期
		int flag = get360TequanAccess("1", skey, qid);
		if (flag == -5 || flag == -4 || flag == -6) {
			return new Object[] { 0 };
		}
		RolePlatformInfo rolePlatformInfo = ptCommonService.getRolePlatformInfo(userRoleId);
		int state = 0;
		// 跨天
		updateData(rolePlatformInfo);
		if (rolePlatformInfo != null) {
			state = rolePlatformInfo.getGiftStateStandby();
		}
		//查询分享礼包领取情况  第二位表示分享礼包
		boolean share = false;
		if(rolePlatformInfo!=null && !BitOperationUtil.calState(rolePlatformInfo.getGiftState(), 2)){
			share  = true;
		}
		return new Object[] { 1, state,0,share };
	}

	/**
	 * 领取特权等级礼包
	 */
	public Object[] getTequanReward(Long userRoleId, int tequanNum) {

		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if (role == null) {
			return AppErrorCode.ROLE_IS_NULL;
		}
		String qid = role.getUserId();
		String skey = ChuanQiConfigUtil.getPlatformServerId();
		return getTequanReward(userRoleId, tequanNum, qid, skey);
	}
	/**
	 * 领取分享礼包
	 */
	public Object[] getTequanShareGift(Long userRoleId) {

		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if (role == null) {
			return AppErrorCode.ROLE_IS_NULL;
		}
		String qid = role.getUserId();
		String skey = ChuanQiConfigUtil.getPlatformServerId();
		return getTequanShareReward(userRoleId, qid, skey);
	}

	/**
	 * 跨天了更新每日可领取的数据
	 * 
	 * @param userRoleId
	 * @param tequanNum
	 * @return
	 */
	private void updateData(RolePlatformInfo rolePlatformInfo) {
		if (rolePlatformInfo != null && !DatetimeUtil.dayIsToday(rolePlatformInfo.getUpdateTime())) {
			PtCommonPublicConfig commonConfig = ptCommonService.getConfigByModName(PlatformPublicConfigConstants._360_TEQUAN_MEIRI_SWITCH);
			if (commonConfig == null) {
				return;
			}
			// 把每日可领取的数据刷新过来
			for (Entry<String, Object> entry : commonConfig.getInfo().entrySet()) {
				String name = entry.getKey();
				Integer num = (Integer) entry.getValue();
				if (num == 1) {
					name = name.trim();
					int tequanIndex = Integer.parseInt(name.substring(name.length() - 1, name.length()));
					Integer newState = BitOperationUtil.chanageStateZero(rolePlatformInfo.getGiftStateStandby(), tequanIndex);
					rolePlatformInfo.setGiftStateStandby(newState); // 5级每天都允许领取一次
				}
			}
			rolePlatformInfoDao.cacheUpdate(rolePlatformInfo, rolePlatformInfo.getUserRoleId());
		}
	}
	/**
	 * 分享礼包
	 * @param userRoleId
	 * @param tequanNum
	 * @param qid
	 * @param skey
	 * @return
	 */
	public Object[] getTequanShareReward(Long userRoleId,  String qid, String skey) {
//		qid="1235888215";  //***TODO 测试阶段写死***
		PtCommonPublicConfig commonConfig  = get360PublicConfig(4); 
		if (commonConfig == null) {
			return AppErrorCode.PLATFORM_DATA_ERROR;
		}
		// 跨天
		RolePlatformInfo rolePlatformInfo = ptCommonService.getRolePlatformInfo(userRoleId);

		this.updateData(rolePlatformInfo);

		int flag = get360TequanAccess("share", skey, qid);
		if (flag == 1) {
			// 可领取
			Object[] result = ptCommonService.updateBagAndState(userRoleId, commonConfig.getItems().get("item"), 2, PlatformPublicConfigConstants._360_TEQUAN_SHARE_LB, false);
			if((int)result[0]!=0){
				//背包没满
				result = new Object[] { result[0] };
			} 
			return result;
		} else {
			Object[] result = null;
			if (flag == -2) {
				result = AppErrorCode.QQ_CHENGZHANG_GIFT_LEVEL_LIMIT;
			} else if (flag == -4) {
				result = AppErrorCode.PLATFORM_360_ERROR_2;
			} else if (flag == -5) {
				result = AppErrorCode.PLATFORM_360_ERROR_3;
			} else if (flag == -1) {
				result = AppErrorCode.PLATFORM_360_ERROR_1;
			} else if (flag == -6) {
				result = AppErrorCode.PARAMETER_ERROR;
			} else if (flag == -7) {
				result = AppErrorCode.PLATFORM_360_ERROR_7;
			} else if (flag == -8) {
				result = AppErrorCode.PLATFORM_360_ERROR_8;
			}else if (flag == -3) {
				result = AppErrorCode.PARAMETER_ERROR;
			} else if (flag == 0) {
				result = AppErrorCode.PLATFORM_360_ERROR_4; 
			}
			if(result!=null){
				result[0] = flag; //原值返回
			}
			ChuanQiLog.error("***360特权分享礼包领取错误：returnResult={}***", flag);
			return result;
		}
	}
	
	/**
	 * 特权礼包只有这5个等级可领取：5、10、15、20、25。 5级礼包每天可领取一次 。 特权礼包领取
	 * @param userRoleId
	 * @param position
	 *            tequan1
	 * @return
	 */
	public Object[] getTequanReward(Long userRoleId, int tequanNum, String qid, String skey) {

		PtCommonPublicConfig commonConfig = ptCommonService.getConfigByModName(PlatformPublicConfigConstants._360_TEQUAN_LEVEL);
		if (commonConfig == null) {
			return AppErrorCode.PLATFORM_DATA_ERROR;
		}
		// 跨天
		RolePlatformInfo rolePlatformInfo = ptCommonService.getRolePlatformInfo(userRoleId);

		this.updateData(rolePlatformInfo);

		String name = "tequan" + tequanNum;
		if(commonConfig.getInfo().get(name)==null){
			return AppErrorCode.PARAMETER_ERROR;
		}
		int level = (Integer) commonConfig.getInfo().get(name);
//		 int flag = -4;
		int flag = get360TequanAccess(level+"", skey, qid);
		if (flag == 1) {
			// 可领取
			commonConfig = get360PublicConfig(3);
			Object[] result = ptCommonService.updateBagAndState(userRoleId, commonConfig.getItems().get(name), tequanNum - 1, PlatformPublicConfigConstants._360_TEQUAN_LB, false);
			if((int)result[0]!=0){
				//背包没满
				result = new Object[] { result[0], tequanNum };
			} 
			return result;
		} else {
			Object[] result = null;
			if (flag == -2) {
				result = AppErrorCode.QQ_CHENGZHANG_GIFT_LEVEL_LIMIT;
			} else if (flag == -4) {
				result = AppErrorCode.PLATFORM_360_ERROR_2;
			} else if (flag == -5) {
				result = AppErrorCode.PLATFORM_360_ERROR_3;
			} else if (flag == -1) {
				result = AppErrorCode.PLATFORM_360_ERROR_1;
			} else if (flag == -6) {
				result = AppErrorCode.PARAMETER_ERROR;
			}else if (flag == -3) {
				result = AppErrorCode.PARAMETER_ERROR;
			} else if (flag == 0) {
				result = AppErrorCode.PLATFORM_360_ERROR_4; 
			}
			if(result!=null){
				result[0] = flag; //原值返回
			}
			ChuanQiLog.error("***360tequan!error:returnResult={}***", flag);
			return result;
		}
	}
	/**
	 * http://<your_url>?qid=QID&skey=SKEY&level=LEVEL 360压测接口
	 */
	public Object[] web360TequanTest(Map<String, Object> params) {
		try {
			ChuanQiLog.info("360特权执行逻辑");
			PtCommonPublicConfig commonConfig = ptCommonService.getConfigByModName(PlatformPublicConfigConstants._360_TEQUAN_LEVEL);
			if (commonConfig == null) {
				ChuanQiLog.error("error:commonConfig is null");
				return null;
			}
			if (params.get("qid") == null || params.get("skey") == null || params.get("level") == null) {
				ChuanQiLog.error("error:params is error");
				return null;
			}
			String qid = (String) params.get("qid");
			String skey = (String) params.get("skey");
			Integer level = Integer.parseInt((String) params.get("level"));
			
			ChuanQiLog.info("是否只允许两个qid访问:360TestQidRestrict ={}",_360Util.get360TestQidRestrict());
			
			if (_360Util.get360TestQidRestrict().equals("true")) {
				// 上线后只允许这两个qid访问
				if (!qid.equals(_360Util.get360TestQid1()) && !qid.equals(_360Util.get360TestQid2())) {
					return null;
				}
			}
			
			Map<String, Object> info = commonConfig.getInfo();
			boolean  levelIsExist  = false;//config中是否存在这个等级
			for (Entry<String, Object> entry : info.entrySet()) {
				String tequanName = entry.getKey();
				Integer _level = (Integer) entry.getValue();
				if (level == _level) {
					levelIsExist = true;
					RoleAccount roleAccount = accountExportService.getRoleAccountFromDb(qid, ChuanQiConfigUtil.getServerId());
					if (roleAccount == null) {
						ChuanQiLog.error("360tequan qid={} no user!，RoleAccount找不到用户", qid);
						return null;
					}
					tequanName = tequanName.trim();
					int num = Integer.parseInt(tequanName.substring(tequanName.length() - 1, tequanName.length()));
					// 领取奖励
					
					ChuanQiLog.info("****进入领取礼包程序，qid={}，特权数字={}，skey={},userRoleId={}****", qid,num,skey,roleAccount.getUserRoleId());
					
					Object[] result = this.getTequanReward(roleAccount.getUserRoleId(), num, qid, skey);
					if ((Integer) result[0] == 1) {
						BusMsgSender.send2One(roleAccount.getUserRoleId(), ClientCmdType.GET_PLATFORM_360_TEQUAN_GIFT, result);
					}
					return result;
				} 
			}
			if(!levelIsExist){
				ChuanQiLog.error("****WEB roder,360tequan level is error. level not found in config !,level={}",level);
			}
		} catch (Exception e) {
			ChuanQiLog.error("360tequan test error={},params={}", e.getMessage(), params.toString());
		}
		return null;
	}

	/**
	 * String requestUrl = "http://beta.hd.wan.360.cn/check_privi.html?"; $sign
	 * = md5("$aid|$gkey|$qid|$type|$time|$privkey");
	 * 
	 * @param levelStr等级礼包的时候这里是等级level=3,分享礼包的时候这里是level=share
	 * @param platformId
	 * @param qid
	 * @return
	 */
	private int get360TequanAccess(String levelStr, String platformId, String qid) {
		String skey = platformId;
		String privkey = _360Util.getPrivkey();
		int aid = _360Util.getAid();
		String gkey = _360Util.getGkey();
		String type = _360Util.getType();

		long time = GameSystemTime.getSystemMillTime();
		StringBuffer signBuf = new StringBuffer();
		signBuf.append(aid).append("|").append(gkey).append("|").append(qid).append("|").append(type).append("|").append(time).append("|").append(privkey);
		String sign = Md5Utils.md5To32(signBuf.toString());
		// 参数填充
		StringBuffer params = new StringBuffer();
		params.append("aid=").append(aid);
		params.append("&gkey=").append(gkey);
		params.append("&skey=").append(skey);
		params.append("&qid=").append(qid);
		params.append("&type=").append(type);
		params.append("&level=").append(levelStr);
		params.append("&time=").append(time);
		params.append("&sign=").append(sign);
		// 发起后台请求
		String result = HttpClientMocker.requestMockPost(_360Util.getTequanUrl(), params.toString());

		return Integer.parseInt(result);
	}
}
