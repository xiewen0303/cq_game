package com.junyou.public_.rank.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.kaifuactivity.export.KaiFuHuoDongExportService;
import com.junyou.bus.platform.qq.json.JSONException;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoExportService;
import com.junyou.constants.GameConstants;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.rank.service.RankService;
import com.junyou.public_.rank.vo.IChiBangRankVo;
import com.junyou.public_.rank.vo.IFightingRankVo;
import com.junyou.public_.rank.vo.ILevelRankVo;
import com.junyou.public_.rank.vo.IQiLingRankVo;
import com.junyou.public_.rank.vo.IRankVo;
import com.junyou.public_.rank.vo.IWuqiRankVo;
import com.junyou.public_.rank.vo.IXianJianRankVo;
import com.junyou.public_.rank.vo.IXinwenRankVo;
import com.junyou.public_.rank.vo.IYaoShenHunpoRankVo;
import com.junyou.public_.rank.vo.IYaoShenMowenRankVo;
import com.junyou.public_.rank.vo.IYaoShenMoyinRankVo;
import com.junyou.public_.rank.vo.IYaoShenRankVo;
import com.junyou.public_.rank.vo.IZhanJiaRankVo;
import com.junyou.public_.rank.vo.IZuoqiRankVo;
import com.junyou.utils.KuafuConfigPropUtil;


@Service
public class RankExportService {
	
	@Autowired
	private ILevelRankExportService<ILevelRankVo> levelRankExportService;
	@Autowired
	private IFightingRankExportService<IFightingRankVo> fightingRankExportService;
	@Autowired
	private IZuoqiRankExportService<IZuoqiRankVo> zuoqiRankExportService;
	@Autowired
	private RankService rankService;
	@Autowired
	private KaiFuHuoDongExportService kaiFuHuoDongExportService;
	@Autowired
	private IChiBangRankExportService<IChiBangRankVo> chiBangRankExportService;
	@Autowired
	private IXianJianRankExportService<IXianJianRankVo> xianJianRankExportService;
	@Autowired
	private IZhanJiaRankExportService<IZhanJiaRankVo> zhanJiaRankExportService;
	@Autowired
	private IYaoShenRankExportService<IYaoShenRankVo> yaoShenRankExportService;
	@Autowired
	private IYaoShenMowenRankExportService<IYaoShenMowenRankVo> yaoShenMowenRankExportService;
	@Autowired
	private IYaoShenMoyinRankExportService<IYaoShenMoyinRankVo> yaoShenMoyinRankExportService;
	@Autowired
	private IXinwenRankExportService<IXinwenRankVo> xinwenRankExportService;
	@Autowired
	private IQiLingRankExportService<IQiLingRankVo> qiLingRankExportService;
	@Autowired
	private IYaoShenHunpoRankExportService<IYaoShenHunpoRankVo> yaoShenHunpoRankExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleBusinessInfoExportService roleBusinessInfoExportService;
	@Autowired
	private IWuqiRankExportService<IWuqiRankVo> wuqiRankExportService;
	
	/**排行统计上限*/
	public static final int STATIS_LIMIT=100;
	
	private boolean isStarted=false;
	
	public  void init(){
		if(isStarted){
			return;
		}
		if(!KuafuConfigPropUtil.isKuafuServer()){
			refreshRank();
		}
		
		isStarted=true;
	}
	
	//synchronized 防止JobManage和 GameBusStartStopHandle所属线程并发
	public synchronized void refreshRank(){

		//战力
		quartzZhanLiRank();
		//等级
		quartzLevelRank();
		
		//坐骑
		quartzZuoqiRank();
		//翅膀
		quartzChiBangRank();
		//仙剑
		quartzXianJianRank();
		//战甲
		quartzZhanJiaRank();
		//妖神
		quartzYaoShenRank();
		//妖神--魔纹
		quartzYaoShenMowenRank();
		//器灵
		quartzQiLingRank();
		//妖神魂魄
		quartzYaoShenHunpoRank();
		//妖神魔印
		quartzYaoShenMoyinRank();
		//糖宝心纹
		quartzTangbaoXinwenRank();
		//新圣剑
		quartzWuQiRank();
	}
	
	/**
	 * 刷新战力榜数据
	 */
	public void quartzZhanLiRank(){
		try {
			
			List<IFightingRankVo> fightingRankVos=fightingRankExportService.getFightingRankVo(STATIS_LIMIT);
			
			
			if(fightingRankVos != null && fightingRankVos.size() > 0){
				
				//保存第一名数据（魁首）
				IFightingRankVo oneRank = (IFightingRankVo) fightingRankVos.get(0);
				rankService.refreshKuiShou(GameConstants.ZL_TYPE, oneRank);
				
				rankService.refreshNativeRank(GameConstants.ZL_TYPE, fightingRankVos);
				
			}
			//排行榜调用外部业务（开服排行活动）
			kaiFuHuoDongExportService.tuisongZhanLi(fightingRankVos);
			//没有竞技榜，在这顺便推送一下竞技榜活动的结算
			kaiFuHuoDongExportService.tuisongJingJi();
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
	
	/**
	 * 刷新等级榜数据
	 */
	public void quartzLevelRank(){
		try {
			List<ILevelRankVo>  levelRankVos=levelRankExportService.getLevelRankVo(STATIS_LIMIT);
			
			if(levelRankVos != null && levelRankVos.size() > 0){
				
				//保存第一名数据（魁首）
				ILevelRankVo oneRank = (ILevelRankVo) levelRankVos.get(0);
				rankService.refreshKuiShou(GameConstants.LEVEL_TYPE, oneRank);
				//刷新排行榜数据
				rankService.refreshNativeRank(GameConstants.LEVEL_TYPE, levelRankVos);
			}
			//排行榜调用外部业务（开服排行活动）
			//rankExportMethd(roleIds, RankType.FIGHTING);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
	/**
	 * 刷新坐骑榜数据
	 */
	public void quartzZuoqiRank(){
		try {
			List<IZuoqiRankVo> zuoqiRankVos=zuoqiRankExportService.getZuoqiRankVo(STATIS_LIMIT);
			
			if(zuoqiRankVos != null && zuoqiRankVos.size() > 0){
				
				//保存第一名数据（魁首）
				IZuoqiRankVo oneRank = (IZuoqiRankVo) zuoqiRankVos.get(0);
				rankService.refreshKuiShou(GameConstants.ZQ_TYPE, oneRank);
				//刷新排行榜数据
				rankService.refreshNativeRank(GameConstants.ZQ_TYPE, zuoqiRankVos);
			}
			//排行榜调用外部业务（开服排行活动）
			kaiFuHuoDongExportService.tuisongZuoQi(zuoqiRankVos);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
	/**
	 * 刷新翅膀榜数据
	 */
	public void quartzChiBangRank(){
		try {
			List<IChiBangRankVo> chibangVos=chiBangRankExportService.getChiBangRankVo(STATIS_LIMIT);
			
			if(chibangVos != null && chibangVos.size() > 0){
				
				//保存第一名数据（魁首）
				IChiBangRankVo oneRank = (IChiBangRankVo) chibangVos.get(0);
				rankService.refreshKuiShou(GameConstants.CB_TYPE, oneRank);
				//刷新排行榜数据
				rankService.refreshNativeRank(GameConstants.CB_TYPE, chibangVos);
			}
			//排行榜调用外部业务（开服排行活动）
			kaiFuHuoDongExportService.tuisongChiBang(chibangVos);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
	/**
	 * 刷新糖宝仙剑榜数据
	 */
	public void quartzXianJianRank(){
		try {
			List<IXianJianRankVo> xianjianVos=xianJianRankExportService.getXianJianRankVo(STATIS_LIMIT);
			
			if(xianjianVos != null && xianjianVos.size() > 0){
				
				//保存第一名数据（魁首）
				IXianJianRankVo oneRank = (IXianJianRankVo) xianjianVos.get(0);
				rankService.refreshKuiShou(GameConstants.TB_XJ_TYPE, oneRank);
				//刷新排行榜数据
				rankService.refreshNativeRank(GameConstants.TB_XJ_TYPE, xianjianVos);
			}
			//排行榜调用外部业务（开服排行活动）
			kaiFuHuoDongExportService.tuisongTangBao(xianjianVos);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
	
	/**
	 * 刷新糖宝战甲榜数据
	 */
	public void quartzZhanJiaRank(){
		try {
			List<IZhanJiaRankVo> xianjianVos=zhanJiaRankExportService.getXianJianRankVo(STATIS_LIMIT);
			
			if(xianjianVos != null && xianjianVos.size() > 0){
				
				//保存第一名数据（魁首）
				IZhanJiaRankVo oneRank = (IZhanJiaRankVo) xianjianVos.get(0);
				rankService.refreshKuiShou(GameConstants.TB_ZJ_TYPE, oneRank);
				//刷新排行榜数据
				rankService.refreshNativeRank(GameConstants.TB_ZJ_TYPE, xianjianVos);
			}
			//排行榜调用外部业务（开服排行活动）
			kaiFuHuoDongExportService.tuisongZhanJia(xianjianVos);
			
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
	
	/**
	 * 刷新妖神榜数据
	 */
	public void quartzYaoShenRank(){
		try {
			List<IYaoShenRankVo> yaoshenVos=yaoShenRankExportService.getYaoShenRankVo(STATIS_LIMIT);
			
			if(yaoshenVos != null && yaoshenVos.size() > 0){
				
				//保存第一名数据（魁首）
				IYaoShenRankVo oneRank = (IYaoShenRankVo) yaoshenVos.get(0);
				rankService.refreshKuiShou(GameConstants.YS_TYPE, oneRank);
				//刷新排行榜数据
				rankService.refreshNativeRank(GameConstants.YS_TYPE, yaoshenVos);
			}
			//排行榜调用外部业务（开服排行活动）
			kaiFuHuoDongExportService.tuisongYaoShen(yaoshenVos);
			
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
	
	/**
	 * 刷新妖神魔纹榜数据
	 */
	public void quartzYaoShenMowenRank(){
		try {
			List<IYaoShenMowenRankVo> mowenVos=yaoShenMowenRankExportService.getYaoShenMowenRankVo(STATIS_LIMIT);
			
			if(mowenVos != null && mowenVos.size() > 0){
				//保存第一名数据（魁首）
				IYaoShenMowenRankVo oneRank = (IYaoShenMowenRankVo) mowenVos.get(0);
				rankService.refreshKuiShou(GameConstants.YS_MOWEN_TYPE, oneRank);
				//刷新排行榜数据
				rankService.refreshNativeRank(GameConstants.YS_MOWEN_TYPE, mowenVos);
			}
			//排行榜调用外部业务（开服排行活动）
			kaiFuHuoDongExportService.tuisongYaoShenMowen(mowenVos);
			
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
	/**
	 * 刷新妖神魔印榜数据
	 */
	public void quartzYaoShenMoyinRank(){
		try {
			List<IYaoShenMoyinRankVo> moyinVos=yaoShenMoyinRankExportService.getYaoShenMoyinRankVo(STATIS_LIMIT);
			
			if(moyinVos != null && moyinVos.size() > 0){
				//保存第一名数据（魁首）
				IYaoShenMoyinRankVo oneRank = (IYaoShenMoyinRankVo) moyinVos.get(0);
				rankService.refreshKuiShou(GameConstants.YS_MOYIN_TYPE, oneRank);
				//刷新排行榜数据
				rankService.refreshNativeRank(GameConstants.YS_MOYIN_TYPE, moyinVos);
			}
			//排行榜调用外部业务（开服排行活动）
			kaiFuHuoDongExportService.tuisongYaoShenMoYin(moyinVos);
			
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}

	/**
	 * 刷新糖宝心纹榜数据
	 */
	public void quartzTangbaoXinwenRank(){
		try { 
			List<IXinwenRankVo> xinwenVos=xinwenRankExportService.getXinwenRankVo(STATIS_LIMIT);
			
			if(xinwenVos != null && xinwenVos.size() > 0){
				//保存第一名数据（魁首）
				IXinwenRankVo oneRank = (IXinwenRankVo) xinwenVos.get(0);
				rankService.refreshKuiShou(GameConstants.TANGBAO_XINWEN_TYPE, oneRank);
				//刷新排行榜数据
				rankService.refreshNativeRank(GameConstants.TANGBAO_XINWEN_TYPE, xinwenVos);
			}
			//排行榜调用外部业务（开服排行活动）
			kaiFuHuoDongExportService.tuisongTangBaoXinWen(xinwenVos);
			
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
	
	/**
	 * 刷新妖神魂魄榜数据
	 */
	public void quartzYaoShenHunpoRank(){
		try {
			List<IYaoShenHunpoRankVo> mowenVos=yaoShenHunpoRankExportService.getYaoShenHunpoRankVo(STATIS_LIMIT);
			
			if(mowenVos != null && mowenVos.size() > 0){
				//保存第一名数据（魁首）
				IYaoShenHunpoRankVo oneRank = (IYaoShenHunpoRankVo) mowenVos.get(0);
				rankService.refreshKuiShou(GameConstants.YS_HUNPO_TYPE, oneRank);
				//刷新排行榜数据
				rankService.refreshNativeRank(GameConstants.YS_HUNPO_TYPE, mowenVos);
			}
			//排行榜调用外部业务（开服排行活动）
			kaiFuHuoDongExportService.tuisongYaoShenHunpo(mowenVos);
			
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
	/**
	 * 刷新器灵榜数据
	 */
	public void quartzQiLingRank(){
		try {
			
			List<IQiLingRankVo> qlVos=qiLingRankExportService.getQiLingRankVo(STATIS_LIMIT);
			 
			if(qlVos != null && qlVos.size() > 0){
				//保存第一名数据（魁首）
				IQiLingRankVo oneRank = (IQiLingRankVo) qlVos.get(0);
				rankService.refreshKuiShou(GameConstants.QILING_TYPE, oneRank);
				//刷新排行榜数据
				rankService.refreshNativeRank(GameConstants.QILING_TYPE, qlVos);
			}
			//排行榜调用外部业务（开服排行活动）
			kaiFuHuoDongExportService.tuisongQiLing(qlVos);
			
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
	
	
	public void refreshRank(Integer rankType , List<? extends IRankVo> iRankVos){
		rankService.refreshNativeRank(rankType,iRankVos );
	}

	/**
	 * 根据排行榜类型获取第1名数据
	 * @param type
	 * @return
	 */
	public Object[] getKuiShouInfoByType(int type){
		return rankService.getKuiShouInfoByType(type);
	}
	/**
	 * 获取我的名次
	 * @param userRoleId
	 * @param type
	 * @return
	 */
	public int getMyOrderByType(Long userRoleId,int type){
		return rankService.getMyOrderByType(userRoleId, type);
	}
	public long getUserServerRank(String userId,String serverId,int type){
		UserRole role = roleExportService.getRoleFromDb(userId, serverId);
		if(role == null){
			return 0;
		}
		if(GameConstants.LEVEL_TYPE == type){//等级榜
			int rank = getMyOrderByType(role.getId(), type);
			if(rank == -1){
				return roleExportService.getUserServerRank(userId, serverId);
			}else{
				return rank;
			}
		}else if(GameConstants.ZL_TYPE == type){
			int rank = getMyOrderByType(role.getId(), type);
			if(rank == -1){
				return roleBusinessInfoExportService.getUserServerRank(userId, serverId);
			}else{
				return rank;
			}
		}
		return 0;
	}
	public JSONObject hangouGetRank(String serverId,int rankType){
		try {
			return rankService.hangouGetRank( serverId, rankType);
		} catch (JSONException e) {
			ChuanQiLog.error("",e);
		}
		return null;
	}
	
	/**
	 * 刷新新圣剑榜数据
	 */
	public void quartzWuQiRank(){
		try {
			List<IWuqiRankVo> wuqiRankVos = wuqiRankExportService.getWuqiRankVo(STATIS_LIMIT);
			
			if(wuqiRankVos != null && wuqiRankVos.size() > 0){
				
				//保存第一名数据（魁首）
				IWuqiRankVo oneRank = (IWuqiRankVo) wuqiRankVos.get(0);
				rankService.refreshKuiShou(GameConstants.WQ_TYPE, oneRank);
				
				//刷新排行榜数据
				rankService.refreshNativeRank(GameConstants.WQ_TYPE, wuqiRankVos);
			}
			
			//排行榜调用外部业务（开服排行活动）  TODO  关联相应的热发布活动
			kaiFuHuoDongExportService.tuisongWuQi(wuqiRankVos);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
}
