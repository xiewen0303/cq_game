package com.junyou.bus.platform.baidu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.platform.baidu.confiure.export.BaiduRenWuJiShiConfig;
import com.junyou.bus.platform.baidu.confiure.export.BaiduRenWuJiShiConfigExportService;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.public_.share.export.PublicRoleStateExportService;


@Service
public class BaiDuRenWuJiShiService {

	
	@Autowired
	private BaiduRenWuJiShiConfigExportService baiduRenWuJiShiConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	
	
	
	/*private RenwujishiBaidu getRenWuJiShi(Long userRoleId){
		List<RenwujishiBaidu> list = renwujishiBaiduDao.cacheLoadAll(userRoleId);
		if(list == null || list.size() <= 0 ){
			RenwujishiBaidu renwu = new RenwujishiBaidu();
			renwu.setUserRoleId(userRoleId);
			renwu.setLingquStatus("");
			renwu.setCreateTime(new Timestamp(System.currentTimeMillis()));
			renwu.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			
			renwujishiBaiduDao.cacheInsert(renwu, userRoleId);
			return renwu;
		}
		
		return list.get(0);
	}*/
	
	public boolean getRenWuStatus(Long userRoleId,String renWuId,int bu) {
		BaiduRenWuJiShiConfig config = baiduRenWuJiShiConfigExportService.getRenWuByIdAndBu(renWuId, bu);
		if(config == null){
			return false;
		}
		if(!publicRoleStateExportService.isPublicOnline(userRoleId)){
			return false;
		}
		
		//switch (config.getType()) {
		                   
			//case GameConstants.RWJS_ROLE_LEVEL:
		RoleWrapper userRole = roleExportService.getLoginRole(userRoleId);
		if(userRole.getLevel() >= config.getCishu()){
			return true;
		}
				//break;
				
	/*		default:
				break;
		}*/
		
		
		return false;
	}

	/*public List<RenwujishiBaidu> initAllRenWuJiShi(Long userRoleId) {
		return renwujishiBaiduDao.initRenwujishiBaidu(userRoleId);
	}
*/
	/*public Integer lingQu(Long userRoleId, String renWuId, int bu,String goodsId) {
		RenwujishiBaidu renwu = getRenWuJiShi(userRoleId);
		BaiduRenWuJiShiConfig config = baiduRenWuJiShiConfigExportService.getRenWuByIdAndBu(renWuId, bu);
		//已领取过奖励
		if(getLingQuStatus(renwu.getLingquStatus(), config.getConfigid()+"")){
			return 3;
		}
		GoodsConfig good = goodsConfigExportService.loadById(goodsId);
		if(good == null){
			return 102;//奖励发放失败
		}
		//发放奖励
		rechargeEmail(userRoleId, goodsId);
		
		if(renwu.getLingquStatus() == null || "".equals(renwu.getLingquStatus())){
			renwu.setLingquStatus(config.getConfigid()+"");
		}else{
			renwu.setLingquStatus(renwu.getLingquStatus()+","+config.getConfigid());
		}
		renwujishiBaiduDao.cacheUpdate(renwu, userRoleId);
		
		return 0;
	}*/

	
	/**
	 * 判断ID是否已领取过
	 * @param lingqu
	 * @param id
	 * @return
	 */
	private boolean getLingQuStatus(String lingqu,String id){
		
		if(lingqu == null || "".equals(lingqu)){
			return false;
		}
		String[] s = lingqu.split(",");
		for (int i = 0; i < s.length; i++) {
			if(s[i].equals(id)){
				return true;
			}
		}
		return false;
	}
	
	 /**
	  * 邮件发送奖励
	  * @param roleNames
	  */
	 private void rechargeEmail(Long userRoleId,String goodsId){
			
			String attachment = goodsId+":"+1;
			String content = EmailUtil.getCodeEmail(AppErrorCode.RWJS_YOUJIAN);
			String title = EmailUtil.getCodeEmail(AppErrorCode.RWJS_YOUJIAN_EMAIL_TITLE);
			emailExportService.sendEmailToOne(userRoleId,title, content,GameConstants.EMAIL_TYPE_SINGLE, attachment);
	}
	 
	 public Integer renWuJiShiLingQu(String openId,String serverId,String renWuId,Integer step){
		 UserRole role = getRoleWrapper(openId, serverId);
		if(role == null){
			return GameConstants.RWJS_NOT_ROLE;
		}
			//if(GameConstants.RWJS_CHECK.equals(cmd)){//查询是否已完成
		boolean obj = getRenWuStatus(role.getId(), renWuId, step);
		if(obj){
			return GameConstants.RWJS_SUCCESS;
		}else{
			return GameConstants.RWJS_UNFINISHED;
		}
			/*}else if(GameConstants.RWJS_CHECKAWARD.equals(cmd)){//查询并发货
				Object[] obj = getRenWuStatus(role.getId(), renWuId, step);
				if((Boolean)obj[0]){
					return lingQu(role.getId(), renWuId, step, payItem);
				}else{
					return GameConstants.RWJS_UNFINISHED;
				}
			}else if(GameConstants.RWJS_AWARD.equals(cmd)){//直接发货
				
				return lingQu(role.getId(), renWuId, step, payItem);
			}
			return null;
			*/
	 }
	
	 private UserRole getRoleWrapper(String userId,String serverId){
		 UserRole userRole = roleExportService.getRoleFromDb(userId, serverId);
		 return userRole;
	 }
		
}
