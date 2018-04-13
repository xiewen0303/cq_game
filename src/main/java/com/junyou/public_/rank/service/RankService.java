package com.junyou.public_.rank.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.platform.qq.json.JSONException;
import com.junyou.bus.platform.qq.service.export.QqExportService;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoExportService;
import com.junyou.constants.GameConstants;
import com.junyou.public_.rank.dao.RankDao;
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
import com.junyou.utils.datetime.DatetimeUtil;


@Service
public class RankService {

	@Autowired
	private RankDao  rankDao;
	@Autowired
	private QqExportService qqExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleBusinessInfoExportService roleBusinessInfoExportService;
	
	public IRankVo[] showRank(Long userRoleId,int rankType, int page){
		return rankDao.cutRanks(rankType, page);
	}
	
	public Object[] showRank(int rankType, int startIndex, int needCount){
		List<? extends IRankVo>	iRankVos= rankDao.cutRanks( rankType,  startIndex,  needCount);
		Object[] result = null;
		int index=0;
		if(iRankVos!=null&& iRankVos.size() > 0){
			result=new Object[iRankVos.size()];
			if(PlatformConstants.isQQ()){
				for (IRankVo iRankVo : iRankVos) {
					Object[] obj = iRankVo.getOutData();
					obj =  (Object[]) resizeArray(obj, obj.length+1);
					if(rankType == 1 ||rankType == 2 ){
						obj[9] = qqExportService.getRoleQQPlatformInfoNeicun(iRankVo.getUserRoleId());	
					}else{
						obj[7] = qqExportService.getRoleQQPlatformInfoNeicun(iRankVo.getUserRoleId());
					}
					
					result[index++]=obj;
				}
			}else{
				for (IRankVo iRankVo : iRankVos) {
					result[index++] = iRankVo.getOutData();
				}
			}
		}
		return new Object[]{new Object[]{startIndex,needCount},result,rankDao.getCountByRankType(rankType)};
	}
	
	public JSONObject hangouGetRank(String serverId,int rankType) throws JSONException{
		int startIndex = 0;
		int needCount = 49;
		List<? extends IRankVo>	iRankVos= rankDao.cutRanks( rankType,  startIndex,  needCount);
		//Object[] result = null;
		JSONArray jsonArray = new JSONArray();
		JSONObject obj = new JSONObject();
		if(iRankVos!=null&& iRankVos.size() > 0){
			//result=new Object[iRankVos.size()];
			for (IRankVo iRankVo : iRankVos) {
				if(rankType == GameConstants.ZL_TYPE){
					JSONObject json = new JSONObject();
					json.put("rank", iRankVo.getOutData()[3]);
					json.put("name", iRankVo.getOutData()[2]);
					json.put("number", iRankVo.getOutData()[6]);
					jsonArray.add(json);
				}else if(rankType == GameConstants.LEVEL_TYPE){
					JSONObject json = new JSONObject();
					json.put("rank", iRankVo.getOutData()[3]);
					json.put("name", iRankVo.getOutData()[2]);
					json.put("number", iRankVo.getOutData()[5]);
					jsonArray.add(json);
				}
			}
		}
		obj.put("rank", jsonArray);
		/*//查询自己的排名
		long myRank = getUserServerRank(userId, serverId, rankType);
		array.add(myRank);*/
		return obj;
	}
	
	public long getUserServerRank(String userId,String serverId,int type){
		if(GameConstants.LEVEL_TYPE == type){//等级榜
			return roleExportService.getUserServerRank(userId, serverId);
		}else if(GameConstants.ZL_TYPE == type){
			return roleBusinessInfoExportService.getUserServerRank(userId, serverId);
		}
		return 0;
	}
	
	private static Object resizeArray (Object oldArray, int newSize) {
		   int oldSize = java.lang.reflect.Array.getLength(oldArray);
		   Class elementType = oldArray.getClass().getComponentType();
		   Object newArray = java.lang.reflect.Array.newInstance(elementType,newSize);
		   int preserveLength = Math.min(oldSize,newSize);
		   if (preserveLength > 0)
		      System.arraycopy (oldArray,0,newArray,0,preserveLength);
		   return newArray; 
		}

	/**
	 * 属性排行
	 * @param rankType
	 * @param rankVos
	 */
	public void refreshNativeRank(Integer rankType ,List<? extends IRankVo> rankVos){
		
		rankDao.refreshNativeRank(rankType, rankVos);
		
	}
	/**
	 * 刷新魁首
	 * @param rankType
	 * @param rankVo
	 */
	public void refreshKuiShou(Integer rankType,IRankVo rankVo){
		
		rankDao.refreshKuiShou(rankType, rankVo);
	}
	
	
	public Object[] getRankInfo(Long userRoleId,int type){

		Object selfRankData = getRankDataByRoleId(userRoleId,type);
		return new Object[]{type,getNextHourTime(),selfRankData};
	}
	
	private long getNextHourTime(){
		return DatetimeUtil.getRankCurNextHourTime();
	}
	
	/**
	 * 根据类型获取排行榜第1名
	 * @param type
	 * @return
	 */
	public Object[] getKuiShouInfoByType(int type){
		IRankVo vo = rankDao.getKuiShou(type);
		if(vo != null){
			return new Object[]{vo.getConfigId(),vo.getUserRoleId(),vo.getName()};
		}
		return null;
	}
	/**
	 * 获取我的名次 根据排行类型
	 * @param userRoleId
	 * @param type
	 * @return
	 */
	public int getMyOrderByType(Long userRoleId,int type){
		List<IRankVo> levelVos = (List<IRankVo>) rankDao.getRankData(type);
		
		if(levelVos != null && levelVos.size() > 0){
			
			for (IRankVo tmpRank : levelVos) {
				
				if(tmpRank.getUserRoleId().equals(userRoleId)){
					return tmpRank.getRank();
				}
			}
		}
		return -1;
		
	}
	
	public Object[] getKuiShouRankInfo(Object[] ranks){
		
		if(ranks == null || ranks.length <= 0){
			return null;
		}
		List<Object[]> list = new ArrayList<>();
		for (int i = 0; i < ranks.length; i++) {
			IRankVo vo = rankDao.getKuiShou((int)ranks[i]);
			if(vo != null){
				list.add(new Object[]{ranks[i],vo.getConfigId(),vo.getUserRoleId(),vo.getName()});
			}
		}
		
		return list.toArray();
	}
	
	/**
	 * 根据索引获取等级榜数据
	 * @param startIndex
	 * @param needCount
	 * @return
	 */
	public Object[] getLevelRankInfo(int startIndex, int needCount){
		return showRank(GameConstants.LEVEL_TYPE, startIndex, needCount);
	}
	/**
	 * 根据索引获取战力榜数据
	 * @param startIndex
	 * @param needCount
	 * @return
	 */
	public Object[] getZhanLiRankInfo(int startIndex, int needCount){
		return showRank(GameConstants.ZL_TYPE, startIndex, needCount);
	}
	/**
	 * 根据索引获取坐骑榜数据
	 * @param startIndex
	 * @param needCount
	 * @return
	 */
	public Object[] getZuoQiRankInfo(int startIndex, int needCount){
		return showRank(GameConstants.ZQ_TYPE, startIndex, needCount);
	}
	
	/**
	 * 根据索引获取翅膀榜数据
	 * @param startIndex
	 * @param needCount
	 * @return
	 */
	public Object[] getChiBangRankInfo(int startIndex, int needCount){
		return showRank(GameConstants.CB_TYPE, startIndex, needCount);
	}
	/**
	 * 根据索引获取仙剑榜数据
	 * @param startIndex
	 * @param needCount
	 * @return
	 */
	public Object[] getXianJiaRankInfo(int startIndex, int needCount){
		return showRank(GameConstants.TB_XJ_TYPE, startIndex, needCount);
	}
	/**
	 * 根据索引获取翅膀榜数据
	 * @param startIndex
	 * @param needCount
	 * @return
	 */
	public Object[] getZhanJiaRankInfo(int startIndex, int needCount){
		return showRank(GameConstants.TB_ZJ_TYPE, startIndex, needCount);
	}
	/**
	 * 根据索引获取妖神榜数据
	 * @param startIndex
	 * @param needCount
	 * @return
	 */
	public Object[] getYaoShenRankInfo(int startIndex, int needCount){
		return showRank(GameConstants.YS_TYPE, startIndex, needCount);
	}
	
	/**
	 * 根据索引获取妖神魔纹榜数据
	 * @param startIndex
	 * @param needCount
	 * @return
	 */
	public Object[] getYaoShenMowenRankInfo(int startIndex, int needCount){
		return showRank(GameConstants.YS_MOWEN_TYPE, startIndex, needCount);
	}
	/**
	 * 根据索引获取妖神魔印榜数据
	 * @param startIndex
	 * @param needCount
	 * @return
	 */
	public Object[] getYaoShenMoyinRankInfo(int startIndex, int needCount){
		return showRank(GameConstants.YS_MOYIN_TYPE, startIndex, needCount);
	}
	/**
	 * 根据索引获取妖神魂魄榜数据
	 * @param startIndex
	 * @param needCount
	 * @return
	 */
	public Object[] getYaoShenHunpoRankInfo(int startIndex, int needCount){
		return showRank(GameConstants.YS_HUNPO_TYPE, startIndex, needCount);
	}
	/**
	 * 根据索引获取糖宝心纹榜数据
	 * @param startIndex
	 * @param needCount
	 * @return
	 */
	public Object[] getTangbaoXinwenRankInfo(int startIndex, int needCount){
		return showRank(GameConstants.TANGBAO_XINWEN_TYPE, startIndex, needCount);
	}
	/**
	 * 根据索引获取圣剑榜数据
	 * @param startIndex
	 * @param needCount
	 * @return
	 */
	public Object[] getShengjianRankInfo(int startIndex, int needCount){
		return showRank(GameConstants.WQ_TYPE, startIndex, needCount);
	}
	/**
	 * 根据索引获取器灵榜数据
	 * @param startIndex
	 * @param needCount
	 * @return
	 */
	public Object[] getQilingRankInfo(int startIndex, int needCount){
		return showRank(GameConstants.QILING_TYPE, startIndex, needCount);
	}
	
	
	@SuppressWarnings("unchecked")
	public Object getRankDataByRoleId(Long userRoleId,int rankType){
		Object result = null;
		
		switch (rankType) {
		case GameConstants.ZL_TYPE:
			
			List<IFightingRankVo> fightingVos = (List<IFightingRankVo>) rankDao.getRankData(rankType);

			if(fightingVos != null && fightingVos.size() > 0){
				
				for (IFightingRankVo tmpRank : fightingVos) {
					
					if(tmpRank.getUserRoleId().equals(userRoleId)){
						result = tmpRank.getOutData();
						break;
					}
				}
			}
			
			return result;
			
		case GameConstants.LEVEL_TYPE:
			
			List<ILevelRankVo> levelVos = (List<ILevelRankVo>) rankDao.getRankData(rankType);
			
			if(levelVos != null && levelVos.size() > 0){
				
				for (ILevelRankVo tmpRank : levelVos) {
					
					if(tmpRank.getUserRoleId().equals(userRoleId)){
						result = tmpRank.getOutData();
						break;
					}
				}
			}
			
			return result;
		case GameConstants.ZQ_TYPE:
			
			List<IZuoqiRankVo> zuoqiVos = (List<IZuoqiRankVo>) rankDao.getRankData(rankType);
			
			if(zuoqiVos != null && zuoqiVos.size() > 0){
				
				for (IZuoqiRankVo tmpRank : zuoqiVos) {
					
					if(tmpRank.getUserRoleId().equals(userRoleId)){
						result = tmpRank.getOutData();
						break;
					}
				}
			}
			
			return result;
		case GameConstants.CB_TYPE:
			
			List<IChiBangRankVo> chibangVos = (List<IChiBangRankVo>) rankDao.getRankData(rankType);
			
			if(chibangVos != null && chibangVos.size() > 0){
				
				for (IChiBangRankVo tmpRank : chibangVos) {
					
					if(tmpRank.getUserRoleId().equals(userRoleId)){
						result = tmpRank.getOutData();
						break;
					}
				}
			}
			return result;
			
		case GameConstants.TB_XJ_TYPE:
			
			List<IXianJianRankVo> xianjianVos = (List<IXianJianRankVo>) rankDao.getRankData(rankType);
			
			if(xianjianVos != null && xianjianVos.size() > 0){
				
				for (IXianJianRankVo tmpRank : xianjianVos) {
					
					if(tmpRank.getUserRoleId().equals(userRoleId)){
						result = tmpRank.getOutData();
						break;
					}
				}
			}
			
			return result;
		case GameConstants.TB_ZJ_TYPE:
			
			List<IZhanJiaRankVo> zhanjiaVos = (List<IZhanJiaRankVo>) rankDao.getRankData(rankType);
			
			if(zhanjiaVos != null && zhanjiaVos.size() > 0){
				
				for (IZhanJiaRankVo tmpRank : zhanjiaVos) {
					
					if(tmpRank.getUserRoleId().equals(userRoleId)){
						result = tmpRank.getOutData();
						break;
					}
				}
			}
			
			return result;
		case GameConstants.YS_TYPE:
			
			List<IYaoShenRankVo> yaoshenVos = (List<IYaoShenRankVo>) rankDao.getRankData(rankType);
			
			if(yaoshenVos != null && yaoshenVos.size() > 0){
				
				for (IYaoShenRankVo tmpRank : yaoshenVos) {
					
					if(tmpRank.getUserRoleId().equals(userRoleId)){
						result = tmpRank.getOutData();
						break;
					}
				}
			}
			
			return result;

		case GameConstants.YS_MOWEN_TYPE:
			
			List<IYaoShenMowenRankVo> yaoshenMowenVos = (List<IYaoShenMowenRankVo>) rankDao.getRankData(rankType);
			
			if(yaoshenMowenVos != null && yaoshenMowenVos.size() > 0){
				
				for (IYaoShenMowenRankVo tmpRank : yaoshenMowenVos) {
					
					if(tmpRank.getUserRoleId().equals(userRoleId)){
						result = tmpRank.getOutData();
						break;
					}
				}
			}
			return result;
			 
		  case GameConstants.TANGBAO_XINWEN_TYPE:
			
			List<IXinwenRankVo> xinwenVos = (List<IXinwenRankVo>) rankDao.getRankData(rankType);
			
			if(xinwenVos != null && xinwenVos.size() > 0){
				
				for (IXinwenRankVo tmpRank : xinwenVos) {
					
					if(tmpRank.getUserRoleId().equals(userRoleId)){
						result = tmpRank.getOutData();
						break;
					}
				}
			}
			return result;
			
			
		case GameConstants.YS_HUNPO_TYPE:
			
			List<IYaoShenHunpoRankVo> yaoshenHunpoVos = (List<IYaoShenHunpoRankVo>) rankDao.getRankData(rankType);
			
			if(yaoshenHunpoVos != null && yaoshenHunpoVos.size() > 0){
				
				for (IYaoShenHunpoRankVo tmpRank : yaoshenHunpoVos) {
					
					if(tmpRank.getUserRoleId().equals(userRoleId)){
						result = tmpRank.getOutData();
						break;
					}
				}
			}
			
			return result;
			
			case GameConstants.YS_MOYIN_TYPE:
			
			List<IYaoShenMoyinRankVo> yaoshenMoyinVos = (List<IYaoShenMoyinRankVo>) rankDao.getRankData(rankType);
			
			if(yaoshenMoyinVos != null && yaoshenMoyinVos.size() > 0){
				
				for (IYaoShenMoyinRankVo tmpRank : yaoshenMoyinVos) {
					if(tmpRank.getUserRoleId().equals(userRoleId)){
						result = tmpRank.getOutData();
						break;
					}
				}
			}
			
			return result;

		case GameConstants.QILING_TYPE:
			
			List<IQiLingRankVo> qlVos = (List<IQiLingRankVo>) rankDao.getRankData(rankType);
			
			if(qlVos != null && qlVos.size() > 0){
				
				for (IQiLingRankVo tmpRank : qlVos) {
					
					if(tmpRank.getUserRoleId().equals(userRoleId)){
						result = tmpRank.getOutData();
						break;
					}
				}
			}
			
			return result;
			
		case GameConstants.WQ_TYPE:
			
			List<IWuqiRankVo> sjVos = (List<IWuqiRankVo>) rankDao.getRankData(rankType);
			
			if(sjVos != null && sjVos.size() > 0){
				
				for (IWuqiRankVo tmpRank : sjVos) {
					
					if(tmpRank.getUserRoleId().equals(userRoleId)){
						result = tmpRank.getOutData();
						break;
					}
				}
			}
			
			return result;
			
		default:
			break;
		}
		return result;
	}
	
	
}
