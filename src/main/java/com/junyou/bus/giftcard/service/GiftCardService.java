package com.junyou.bus.giftcard.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.giftcard.configure.LiBaoConfig;
import com.junyou.bus.giftcard.configure.LiBaoConfigExportService;
import com.junyou.bus.giftcard.dao.GiftCardDao;
import com.junyou.bus.giftcard.dao.GiftCardPlatformDao;
import com.junyou.bus.giftcard.entity.GiftCard;
import com.junyou.bus.giftcard.entity.GiftCardPlatform;
import com.junyou.bus.giftcard.filter.GiftCardFilterCardno;
import com.junyou.bus.giftcard.filter.GiftCardPlatformFilter;
import com.junyou.bus.giftcard.filter.GiftCardPlatformFilterCardno;
import com.junyou.bus.platform.qq.json.JSONException;
import com.junyou.bus.platform.qq.json.JSONObject;
import com.junyou.bus.platform.qq.utils.OpensnsException;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.GiftCardLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.common.DownloadPathUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.md5.Md5Utils;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;


@Service
public class GiftCardService{
	
	@Autowired
	private GiftCardDao giftCardDao;
	
	@Autowired
	private RoleBagExportService roleBagExportService;
	
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private GiftCardPlatformDao giftCardPlatformDao;
	@Autowired
	private LiBaoConfigExportService liBaoConfigExportService;
	
	/**
	 * 初始化
	 * @param userRoleId
	 * @return
	 */
	public List<GiftCard> initGiftCard(Long userRoleId) {
		return giftCardDao.initGiftCard(userRoleId);
	}
	
	public List<GiftCardPlatform> initGiftCardPlatform(Long userRoleId){
		return giftCardPlatformDao.initGiftCardPlatform(userRoleId);
	}
	
	/**
	 * 正常礼包卡
	 * @param userRoleId
	 * @param cardNo
	 * @return
	 */
	public int getGiftCardRewardNomal(Long userRoleId,String cardNo){
		//礼包卡验证
		if(StringUtils.isBlank(cardNo) || !cardNo.startsWith(GameConstants.GIFT_START_STR) || cardNo.length()<18){
			return AppErrorCode.GIFT_CARD_IS_NULL;
		}
		
		//是否重复
		String repeat = cardNo.substring(3,4);
		//礼品类型
		String gType = cardNo.substring(5,9);
		LiBaoConfig config = liBaoConfigExportService.loadById(gType);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR_NUMBER;
		}
		
		if("0".equals(repeat)){// 0不可重复
			List<GiftCard> cards = giftCardDao.cacheLoadAll(userRoleId);
			if(cards != null && cards.size() > 0){
				for(GiftCard card : cards){
					String tmpType = card.getCardno().substring(5, 9);
					if(tmpType.equals(gType) && card.getCardno().startsWith(GameConstants.GIFT_START_STR)){
						return AppErrorCode.GIFT_CARD_REWARDED;
					}
				}
			}
		}else if("2".equals(repeat)){
			//判断玩家是否有领过这1个礼包码
			List<GiftCard> gidtList = giftCardDao.cacheLoadAll(userRoleId, new GiftCardFilterCardno(cardNo));
			if(gidtList != null && gidtList.size() > 0){
				return AppErrorCode.GIFT_CARD_REWARDED;
			}
		}
		
		//礼包卡类型
		String cardType = cardNo.substring(4, 5);
		if("c".equals(cardType)){//加密算法验证
			if(cardNo.length() != 25){
				return AppErrorCode.GIFT_CARD_NO_PASS;
			}
			if(!"0".equals(repeat)){
				return AppErrorCode.GIFT_CARD_NO_PASS;
			}
			if(!gType.equals("g001")){
				return AppErrorCode.GIFT_CARD_NO_PASS;
			}
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			String passText = gType+role.getUserId();
			String md5Result = Md5Utils.md5To16(passText);
			String currentText = cardNo.substring(9, 25);
			if(!md5Result.equals(currentText)){
				return AppErrorCode.GIFT_CARD_NO_PASS;
			}
		}else{
			StringBuilder urlSb = new StringBuilder();
			urlSb.append(ChuanQiConfigUtil.getManageToolUrl()).append("libao_check.do?").append("code=").append(cardNo).append("&serverid=");
			if("a".equals(cardType)){//单服务器
				urlSb.append(ChuanQiConfigUtil.getServerId());
			}else if("b".equals(cardType)){//平台
				urlSb.append(ChuanQiConfigUtil.getPlatfromId());
			}else{//其他
				return AppErrorCode.GIFT_CARD_TYPE_ERROR;
			}
			String sign = getCallbackSign(urlSb.toString());
			if(!"100".equals(sign)){
				if("0".equals(sign)){
					return AppErrorCode.GIFT_CARD_IS_NULL;
				}else if("1".equals(sign)){
					return AppErrorCode.GIFT_CARD_REWARDED;
				}else{
					return AppErrorCode.GIFT_CARD_OTHER_ERROR;
				}
			}else if(sign == null){
				return AppErrorCode.GIFT_CARD_OTHER_ERROR;
			}
		}
		
		if("a".equals(cardType) || "b".equals(cardType)){
			StringBuilder tmpSb = new StringBuilder();
			tmpSb.append(ChuanQiConfigUtil.getManageToolUrl()).append("libao_state.do?code=").append(cardNo);
			String stageSign = getCallbackSign(tmpSb.toString());
			if(stageSign == null || !"1".equals(stageSign)){
				return AppErrorCode.GIFT_CARD_OTHER_ERROR;
			}
		}
		
		//创建礼包pojo
		GiftCard card = new GiftCard();
		card.setId(IdFactory.getInstance().generateId(ServerIdType.GIFTCARD));
		card.setUserRoleId(userRoleId);
		card.setCardno(cardNo);
		card.setUseTime(GameSystemTime.getSystemMillTime());
		giftCardDao.cacheInsert(card, userRoleId);
		
		if(config.getMoney() > 0){
			roleBagExportService.incrNumberWithNotify(GoodsCategory.MONEY, config.getMoney(), userRoleId, LogPrintHandle.GET_GIFT_CARD, LogPrintHandle.GBZ_GIFT_CARD);
		} 
		if(config.getBindgold() > 0){
			roleBagExportService.incrNumberWithNotify(GoodsCategory.BGOLD, config.getBindgold(), userRoleId, LogPrintHandle.GET_GIFT_CARD, LogPrintHandle.GBZ_GIFT_CARD);
		}
		String content = EmailUtil.getCodeEmail("", config.getName());
		roleBagExportService.putInBagOrEmail(config.getItems(), userRoleId, GoodsSource.GIFT_CARD_REWARD, true,content);
		
		//打印日志
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		JSONArray goods = LogPrintHandle.getLogGoodsParam(config.getItems(), null);
		GamePublishEvent.publishEvent(new GiftCardLogEvent(userRoleId,role.getName(),goods,cardNo,config.getMoney(),config.getBindgold()));
		return AppErrorCode.GIFT_CARD_SUCCESS;
	}
	
	/**
	 * 腾讯礼包码
	 * @param userRoleId
	 * @param cardNo
	 * @return
	 */
	public int getGiftCardRewardQzone(Long userRoleId,String cardNo){
		if(cardNo.startsWith("hqg")){
			return getGiftCardRewardNomal(userRoleId, cardNo);
		}else{
			//礼包卡验证
			if(StringUtils.isBlank(cardNo) || cardNo.length() != 16){
				return AppErrorCode.GIFT_CARD_IS_NULL;
			}
			//请求后面验证码是否有效
			StringBuilder urlSb = new StringBuilder();
			urlSb.append(ChuanQiConfigUtil.getManageToolUrl()).append("other_libao_check.do?").append("code=").append(cardNo).append("&serverid=");
			//平台
			urlSb.append(ChuanQiConfigUtil.getPlatfromId());
			
			//后台返回验证 结果
			String resp = getCallbackSign(urlSb.toString());
			String rs = "";
			String gType = "";
			String giftType = "";
			String boci = "";
			try {
				 rs = getJsonValue(resp, "result");
				 gType = getJsonValue(resp, "libaoNo");
				 giftType = getJsonValue(resp, "libaoType");
				 boci = getJsonValue(resp, "pici");
			} catch (OpensnsException e) {
				e.printStackTrace();
				BusMsgSender.send2One(userRoleId, ClientCmdType.GIFT_CARD,AppErrorCode.GIFT_CARD_OTHER_ERROR );
			}
			if(!"100".equals(rs)){
				if("0".equals(rs)){
					return AppErrorCode.GIFT_CARD_IS_NULL;
				}else if("1".equals(rs)){
					return AppErrorCode.GIFT_CARD_REWARDED;
				}else{
					return AppErrorCode.GIFT_CARD_OTHER_ERROR;
				}
			}else if(rs == null){
				return AppErrorCode.GIFT_CARD_OTHER_ERROR;
			}
			
			//礼品类型
			LiBaoConfig config = liBaoConfigExportService.loadById(gType);
			if(config == null){
				return AppErrorCode.CONFIG_ERROR_NUMBER;
			}
			//不可重领取
			List<GiftCardPlatform> cards = giftCardPlatformDao.cacheLoadAll(userRoleId, new GiftCardPlatformFilter(boci));
			if(cards != null && cards.size() > 0){
				if("0".equals(giftType)){
					return AppErrorCode.GIFT_CARD_REWARDED;
				}
				/*for(GiftCardPlatform card : cards){
					String tmpType = card.getCardno().substring(0, giftType.length());
					if(tmpType.equals(giftType)){
						return AppErrorCode.GIFT_CARD_REWARDED;
					}
				}*/
			}
			
			
			//更新后台码的状态
			StringBuilder tmpSb = new StringBuilder();
			tmpSb.append(ChuanQiConfigUtil.getManageToolUrl()).append("other_libao_state.do?code=").append(cardNo);
			String stageSign = getCallbackSign(tmpSb.toString());
			if(stageSign == null || !"1".equals(stageSign)){
				return AppErrorCode.GIFT_CARD_OTHER_ERROR;
			}
			//创建礼包pojo
			GiftCardPlatform card = new GiftCardPlatform();
			card.setId(IdFactory.getInstance().generateId(ServerIdType.GIFTCARD));
			card.setUserRoleId(userRoleId);
			card.setCardno(cardNo);
			card.setUseTime(GameSystemTime.getSystemMillTime());
			card.setCardBoci(boci);
			giftCardPlatformDao.cacheInsert(card, userRoleId);
			
			if(config.getMoney() > 0){
				roleBagExportService.incrNumberWithNotify(GoodsCategory.MONEY, config.getMoney(), userRoleId, LogPrintHandle.GET_GIFT_CARD, LogPrintHandle.GBZ_GIFT_CARD);
			} 
			if(config.getBindgold() > 0){
				roleBagExportService.incrNumberWithNotify(GoodsCategory.BGOLD, config.getBindgold(), userRoleId, LogPrintHandle.GET_GIFT_CARD, LogPrintHandle.GBZ_GIFT_CARD);
			}
			String content = EmailUtil.getCodeEmail("", config.getName());
			roleBagExportService.putInBagOrEmail(config.getItems(), userRoleId, GoodsSource.GIFT_CARD_REWARD, true,content);
			
			//打印日志
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			JSONArray goods = LogPrintHandle.getLogGoodsParam(config.getItems(), null);
			GamePublishEvent.publishEvent(new GiftCardLogEvent(userRoleId,role.getName(),goods,cardNo,config.getMoney(),config.getBindgold()));
			return AppErrorCode.GIFT_CARD_SUCCESS;
		}
	}
	
	/**
	 * 越南礼包码
	 * @param userRoleId
	 * @param cardNo
	 * @return
	 */
	public int getGiftCardRewardVN(Long userRoleId,String cardNo){
		if(cardNo.startsWith("hqg")){
			return getGiftCardRewardNomal(userRoleId, cardNo);
		}else{
			//礼包卡验证
			if(StringUtils.isBlank(cardNo) || cardNo.length() != 8){
				return AppErrorCode.GIFT_CARD_IS_NULL;
			}
			//判断玩家是否有领过这1个礼包码
			List<GiftCardPlatform> gidtList = giftCardPlatformDao.cacheLoadAll(userRoleId, new GiftCardPlatformFilterCardno(cardNo));
			if(gidtList != null && gidtList.size() > 0){
				return AppErrorCode.GIFT_CARD_REWARDED;
			}
			//请求后面验证码是否有效
			StringBuilder urlSb = new StringBuilder();
			urlSb.append(ChuanQiConfigUtil.getManageToolUrl()).append("other_libao_check.do?").append("code=").append(cardNo).append("&serverid=");
			//平台
			urlSb.append(ChuanQiConfigUtil.getPlatfromId());
			
			//后台返回验证 结果
			String resp = getCallbackSign(urlSb.toString());
			String rs = "";
			String gType = "";
			String giftType = "";
			String boci = "";
			try {
				rs = getJsonValue(resp, "result");
				gType = getJsonValue(resp, "libaoNo");
				giftType = getJsonValue(resp, "libaoType");
				boci = getJsonValue(resp, "pici");
			} catch (OpensnsException e) {
				e.printStackTrace();
				BusMsgSender.send2One(userRoleId, ClientCmdType.GIFT_CARD,AppErrorCode.GIFT_CARD_OTHER_ERROR );
			}
			if(!"100".equals(rs)){
				if("0".equals(rs)){
					return AppErrorCode.GIFT_CARD_IS_NULL;
				}else if("1".equals(rs)){
					return AppErrorCode.GIFT_CARD_REWARDED;
				}else{
					return AppErrorCode.GIFT_CARD_OTHER_ERROR;
				}
			}else if(rs == null){
				return AppErrorCode.GIFT_CARD_OTHER_ERROR;
			}
			
			//礼品类型
			LiBaoConfig config = liBaoConfigExportService.loadById(gType);
			if(config == null){
				return AppErrorCode.CONFIG_ERROR_NUMBER;
			}
			//不可重领取
			List<GiftCardPlatform> cards = giftCardPlatformDao.cacheLoadAll(userRoleId, new GiftCardPlatformFilter(boci));
			if(cards != null && cards.size() > 0){
				if("0".equals(giftType)){
					return AppErrorCode.GIFT_CARD_REWARDED;
				}
				/*for(GiftCardPlatform card : cards){
					String tmpType = card.getCardno().substring(0, giftType.length());
					if(tmpType.equals(giftType)){
						return AppErrorCode.GIFT_CARD_REWARDED;
					}
				}*/
			}
			
			
			//更新后台码的状态
			StringBuilder tmpSb = new StringBuilder();
			tmpSb.append(ChuanQiConfigUtil.getManageToolUrl()).append("other_libao_state.do?code=").append(cardNo);
			String stageSign = getCallbackSign(tmpSb.toString());
			if(stageSign == null || !"1".equals(stageSign)){
				return AppErrorCode.GIFT_CARD_OTHER_ERROR;
			}
			//创建礼包pojo
			GiftCardPlatform card = new GiftCardPlatform();
			card.setId(IdFactory.getInstance().generateId(ServerIdType.GIFTCARD));
			card.setUserRoleId(userRoleId);
			card.setCardno(cardNo);
			card.setUseTime(GameSystemTime.getSystemMillTime());
			card.setCardBoci(boci);
			giftCardPlatformDao.cacheInsert(card, userRoleId);
			
			if(config.getMoney() > 0){
				roleBagExportService.incrNumberWithNotify(GoodsCategory.MONEY, config.getMoney(), userRoleId, LogPrintHandle.GET_GIFT_CARD, LogPrintHandle.GBZ_GIFT_CARD);
			} 
			if(config.getBindgold() > 0){
				roleBagExportService.incrNumberWithNotify(GoodsCategory.BGOLD, config.getBindgold(), userRoleId, LogPrintHandle.GET_GIFT_CARD, LogPrintHandle.GBZ_GIFT_CARD);
			}
			String content = EmailUtil.getCodeEmail("", config.getName());
			roleBagExportService.putInBagOrEmail(config.getItems(), userRoleId, GoodsSource.GIFT_CARD_REWARD, true,content);
			
			//打印日志
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			JSONArray goods = LogPrintHandle.getLogGoodsParam(config.getItems(), null);
			GamePublishEvent.publishEvent(new GiftCardLogEvent(userRoleId,role.getName(),goods,cardNo,config.getMoney(),config.getBindgold()));
			return AppErrorCode.GIFT_CARD_SUCCESS;
		}
	}
	
	/**
	 * 韩国礼包码
	 * 
	 * @param userRoleId
	 * @param cardNo
	 * @return
	 */
	public int getGiftCardRewardHG(Long userRoleId,String cardNo){
        if(cardNo.startsWith("hqg")){
            return getGiftCardRewardNomal(userRoleId, cardNo);
        }else{
            //礼包卡验证
            if(StringUtils.isBlank(cardNo) || cardNo.length() != 8){
                return AppErrorCode.GIFT_CARD_IS_NULL;
            }
            //判断玩家是否有领过这1个礼包码
            List<GiftCardPlatform> gidtList = giftCardPlatformDao.cacheLoadAll(userRoleId, new GiftCardPlatformFilterCardno(cardNo));
            if(gidtList != null && gidtList.size() > 0){
                return AppErrorCode.GIFT_CARD_REWARDED;
            }
            //请求后面验证码是否有效
            StringBuilder urlSb = new StringBuilder();
            urlSb.append(ChuanQiConfigUtil.getManageToolUrl()).append("other_libao_check.do?").append("code=").append(cardNo).append("&serverid=");
            //平台
            urlSb.append(ChuanQiConfigUtil.getPlatfromId());
            
            //后台返回验证 结果
            String resp = getCallbackSign(urlSb.toString());
            String rs = "";
            String gType = "";
            String giftType = "";
            String boci = "";
            try {
                rs = getJsonValue(resp, "result");
                gType = getJsonValue(resp, "libaoNo");
                giftType = getJsonValue(resp, "libaoType");
                boci = getJsonValue(resp, "pici");
            } catch (OpensnsException e) {
                e.printStackTrace();
                BusMsgSender.send2One(userRoleId, ClientCmdType.GIFT_CARD,AppErrorCode.GIFT_CARD_OTHER_ERROR );
            }
            if(!"100".equals(rs)){
                if("0".equals(rs)){
                    return AppErrorCode.GIFT_CARD_IS_NULL;
                }else if("1".equals(rs)){
                    return AppErrorCode.GIFT_CARD_REWARDED;
                }else{
                    return AppErrorCode.GIFT_CARD_OTHER_ERROR;
                }
            }else if(rs == null){
                return AppErrorCode.GIFT_CARD_OTHER_ERROR;
            }
            
            //礼品类型
            LiBaoConfig config = liBaoConfigExportService.loadById(gType);
            if(config == null){
                return AppErrorCode.CONFIG_ERROR_NUMBER;
            }
            //不可重领取
            List<GiftCardPlatform> cards = giftCardPlatformDao.cacheLoadAll(userRoleId, new GiftCardPlatformFilter(boci));
            if(cards != null && cards.size() > 0){
                if("0".equals(giftType)){
                    return AppErrorCode.GIFT_CARD_REWARDED;
                }
                /*for(GiftCardPlatform card : cards){
                    String tmpType = card.getCardno().substring(0, giftType.length());
                    if(tmpType.equals(giftType)){
                        return AppErrorCode.GIFT_CARD_REWARDED;
                    }
                }*/
            }
            
            
            //更新后台码的状态
            StringBuilder tmpSb = new StringBuilder();
            tmpSb.append(ChuanQiConfigUtil.getManageToolUrl()).append("other_libao_state.do?code=").append(cardNo);
            String stageSign = getCallbackSign(tmpSb.toString());
            if(stageSign == null || !"1".equals(stageSign)){
                return AppErrorCode.GIFT_CARD_OTHER_ERROR;
            }
            //创建礼包pojo
            GiftCardPlatform card = new GiftCardPlatform();
            card.setId(IdFactory.getInstance().generateId(ServerIdType.GIFTCARD));
            card.setUserRoleId(userRoleId);
            card.setCardno(cardNo);
            card.setUseTime(GameSystemTime.getSystemMillTime());
            card.setCardBoci(boci);
            giftCardPlatformDao.cacheInsert(card, userRoleId);
            
            if(config.getMoney() > 0){
                roleBagExportService.incrNumberWithNotify(GoodsCategory.MONEY, config.getMoney(), userRoleId, LogPrintHandle.GET_GIFT_CARD, LogPrintHandle.GBZ_GIFT_CARD);
            } 
            if(config.getBindgold() > 0){
                roleBagExportService.incrNumberWithNotify(GoodsCategory.BGOLD, config.getBindgold(), userRoleId, LogPrintHandle.GET_GIFT_CARD, LogPrintHandle.GBZ_GIFT_CARD);
            }
            String content = EmailUtil.getCodeEmail("", config.getName());
            roleBagExportService.putInBagOrEmail(config.getItems(), userRoleId, GoodsSource.GIFT_CARD_REWARD, true,content);
            
            //打印日志
            RoleWrapper role = roleExportService.getLoginRole(userRoleId);
            JSONArray goods = LogPrintHandle.getLogGoodsParam(config.getItems(), null);
            GamePublishEvent.publishEvent(new GiftCardLogEvent(userRoleId,role.getName(),goods,cardNo,config.getMoney(),config.getBindgold()));
            return AppErrorCode.GIFT_CARD_SUCCESS;
        }
    }
    
	
	
	/**
     * 获取值
     * @param resp
     * @return
     * @throws OpensnsException
     */
    public static String getJsonValue(String resp,String type) throws OpensnsException{
    	// 解码JSON
    	JSONObject jo = null;
    	try 
    	{
    		jo = new JSONObject(resp);
    	} 
    	catch (JSONException e) 
    	{
    		ChuanQiLog.error("api return json error",e);
    	} 
    	
    	// 检测ret值
        String rc = jo.optString(type);
    	
    	return rc;
    }
	
	/**
	 * 礼包卡兑换
	 * @param userRoleId
	 * @param cardNo
	 * @return
	 */
	public int getGiftCardReward(Long userRoleId,String cardNo){
//		if(PlatformConstants.isQQ()){
//			return getGiftCardRewardQzone(userRoleId, cardNo);
//		}else if(PlatformConstants.isYueNan()){
//			return getGiftCardRewardVN(userRoleId, cardNo);
//		}else if(PlatformConstants.isHanGuo()){
//		    return getGiftCardRewardHG(userRoleId, cardNo);
//		}else{
		//TODO wind 可以添加其他平台的领取验证信息
		return getGiftCardRewardNomal(userRoleId, cardNo);
//		}
	}
	
	/**
	 * 获取后台返回sign
	 * @param urlSb
	 * @return
	 */
	private String getCallbackSign(String urlSb){
		try{
			URL url = new URL(urlSb);
			byte[] data = DownloadPathUtil.download(url);
			if(data == null){
				return null;
			}
			InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(data));
			BufferedReader reader_ = new BufferedReader(reader);
			String sign = reader_.readLine();
			return sign;
		}catch (Exception e) {
			return null;
		}
	}
	
}
