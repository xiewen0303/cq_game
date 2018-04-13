/**
 * 
 */
package com.junyou.bus.kaifuactivity.service;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.jingji.entity.JingJiManager;
import com.junyou.bus.jingji.entity.RoleJingji;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuXianJieJingJiConfig;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuXianJieJingJiConfigExportService;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuXianJieJingJiGroupConfig;
import com.junyou.bus.kaifuactivity.dao.KaifuActityDao;
import com.junyou.bus.kaifuactivity.entity.KaifuActity;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.KaiFuPaiMingHDLogEvent;
import com.junyou.event.PaiMingLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;



/**
 * @description
 * @author ZHONGDIAN
 * @created 2011-11-16上午10:29:07
 */
@Service
public class RfbJingJiPaiMingService { 
	
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private KaifuActityDao kaifuActityDao;
	/**竞技排行相关 **/
	public void start() {
		Map<Integer,KaiFuXianJieJingJiGroupConfig> vo = KaiFuXianJieJingJiConfigExportService.getInstance().getAllConfig();
		if(vo == null || vo.size() <= 0){
			return;
		}
		for (Integer subId : vo.keySet()) {
			//判断活动是否结束
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
			long time = 10*60*1000;//结算间隔时间
			if(configSong != null && (System.currentTimeMillis() >configSong.getEndTimeByMillSecond() && System.currentTimeMillis() - configSong.getEndTimeByMillSecond() <= time)){
				zhanLiPaiXing(subId);
			}
		}
		
	}
	
	public void zhanLiPaiXing(int subId) {
		//判断是否已经发过邮件
		List<KaifuActity> list = kaifuActityDao.dbLoadAll(subId);
		if(list != null && list.size() > 0 ){
			return;
		}
		
		KaiFuXianJieJingJiGroupConfig config = KaiFuXianJieJingJiConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return;
		}
		//处理数据
		Map<Integer, KaiFuXianJieJingJiConfig> configMap = config.getConfigMap();
		
		//记录排名日志
		JSONArray consumeItemArray = new JSONArray(); 
		
		for (int i = 1; i <= 40; i++) {
			KaiFuXianJieJingJiConfig zhanliConfig =  configMap.get(i);
			if(zhanliConfig == null){
				break;
			}
			int start = zhanliConfig.getMin();
			int end = zhanliConfig.getMax();
			for (int j = start; j <= end; j++) {
				RoleJingji roleJingji = JingJiManager.getManager().getRoleJingjiByRank(j);
				try {
					Map<String,Object> entity = new HashMap<>();
					entity.put("userRoleId", roleJingji.getUserRoleId());
					entity.put("name",roleJingji.getName());
					entity.put("rank",j);
					entity.put("numer",0);
					consumeItemArray.add(entity);
				} catch (Exception e) {
					ChuanQiLog.error(""+e);
				}
				if(roleJingji == null){
					break;
				}
				long userRoleId = roleJingji.getUserRoleId();
				String title = EmailUtil.getCodeEmail(AppErrorCode.KAIFU_PAIMING_EMAIL_TITLE);
				String content = EmailUtil.getCodeEmail(AppErrorCode.KAIFU_JINGJI_PAIMING,j+"");
				emailExportService.sendEmailToOne(userRoleId,title, content,GameConstants.EMAIL_TYPE_SINGLE, zhanliConfig.getEmailItem());
			
				//日志
				GamePublishEvent.publishEvent(new KaiFuPaiMingHDLogEvent(userRoleId, roleJingji.getName(), j, zhanliConfig.getEmailItem(), LogPrintHandle.KAIFU_ACTITY_JINGJI));
			}
		
		}
		//排名总日志
		try {
			GamePublishEvent.publishEvent(new PaiMingLogEvent(LogPrintHandle.PAIMING_ACTITY_JINGJI,consumeItemArray));
		} catch (Exception e) {
			ChuanQiLog.error(""+e);
		}
		
		//记录数据
		inertKaiFuActity(subId);
	}
	
	private void inertKaiFuActity(int subId){
		
		KaifuActity actity = new KaifuActity();
		actity.setSubId(subId);
		actity.setCreateTime(new Timestamp(System.currentTimeMillis()));
		
		kaifuActityDao.dbInsert(actity);
	}
	
	/**
	 * 获取某个子活动的热发布某个活动信息
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		KaiFuXianJieJingJiGroupConfig config = KaiFuXianJieJingJiConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		
		List<Object[]> data = new ArrayList<>();
		
		RoleJingji roleJingji = JingJiManager.getManager().getRoleJingjiByRank(1);//竞技第1名
		Object[] one = null;
		if(roleJingji != null){
			one = new Object[]{roleJingji.getConfigId(),roleJingji.getUserRoleId(),roleJingji.getName()};
		}
		
		RoleJingji jj = JingJiManager.getManager().getRoleJingjiByRoleId(userRoleId);
		int myMingci = 0;
		if(jj != null){
			myMingci = jj.getRank();
		}
		
		return new Object[]{
				config.getPic(),	
				config.getPxData().toArray(),
				//config.getTzData().toArray(),
				new Object[]{one,myMingci,data.toArray()}
		};
	}
	/**
	 * 获取礼包信息
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getRefbLingQuStatus(Long userRoleId, Integer subId){
		
		RoleJingji roleJingji = JingJiManager.getManager().getRoleJingjiByRank(1);//竞技第1名
		Object[] one = null;
		if(roleJingji != null){
			one = new Object[]{roleJingji.getConfigId(),roleJingji.getUserRoleId(),roleJingji.getName()};
		}
		
		RoleJingji jj = JingJiManager.getManager().getRoleJingjiByRoleId(userRoleId);
		int myMingci = 0;
		if(jj != null){
			myMingci = jj.getRank();
		}
		
		return  new Object[]{subId,new Object[]{one,myMingci}};
	}
	
}