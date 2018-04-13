package com.junyou.bus.email.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;

import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.MailPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.helper.PublicConfigureHelper;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.ObjectUtil;

public class EmailUtil {
	/**普通邮件开头*/
	private static final String NOMAL_EMAIL_HEAD = "0";
	/**code码邮件开头*/
	private static final String CODE_EMAIL_HEAD  = "1";
	/**code码参数拼接符*/
	private static final String CODE_EMAIL_SPLIT = "ü";
	/**一级分隔符*/
	private static final String SPLIT_CHAR = ",";
	/**二级分隔符*/
	private static final String SUB_SPLIT_CHAR = ":";
	/**内容循环分隔符*/
	private static final String CODE_CONTENT_LOOP_SPLIT = "ä";
	/**用于区分gm邮件是否为特殊文本还是正常文本*/
	public static final String CODE_GM_FLAG = "卍";
	
	
	/**
	 * 获取普通邮件标题/内容
	 * @param title
	 * @return
	 */
	public static String getNomalEmail(String str){
		return NOMAL_EMAIL_HEAD + str;
	}
	/**
	 * 获取code码邮件标题/内容
	 * @param title
	 * @return
	 */
	public static String getCodeEmail(String str){
		return CODE_EMAIL_HEAD + str;
	}
	
	public static void main(String[] args) {
		List<String[]> result = new ArrayList<>();
		result.add(new String[]{"100","1","2"});
//		result.add(new String[]{"100","3","4"});
//		result.add(new String[]{"100","5","6"});
		System.out.println(getCodeEmail(result));
//		System.out.println(getCodeEmail(null));
	}
	
	/**
	 * 获取code码邮件标题/内容（带参数） 内容可循环的。
	 * @param title
	 * @param params  String[0]表示code码
	 * @return
	 */
	public static String getCodeEmail(List<String[]> params){
		if(params == null || params.size() == 0){
			ChuanQiLog.error("enter the params is null in mail!");
			return null;
		}
		StringBuilder text = new StringBuilder();
		boolean falgHead = true;
		for (String[] args : params) {
			text.append(CODE_CONTENT_LOOP_SPLIT);
			for (int i = 0; i < args.length; i++) {
				if(i==0 && falgHead){
					text.append(CODE_EMAIL_HEAD).append(args[i]);
					falgHead = false;
				}else if(i==0){
					text.append(args[i]);
				}else{
					text.append(CODE_EMAIL_SPLIT).append(args[i]);
				}
			}
		}
		return text.substring(1).toString();
	}
	
	/**
	 * 获取code码邮件标题/内容（带参数）
	 * @param title
	 * @param args
	 * @return
	 */
	public static String getCodeEmail(String str,String... args){
		StringBuilder text = new StringBuilder();
		text.append(CODE_EMAIL_HEAD);
		text.append(str);
		for (String arg : args) {
			text.append(CODE_EMAIL_SPLIT).append(arg);
		}
		return text.toString();
	}
	
	/**
	 * 获取code码邮件标题/内容（带参数）
	 * @param title
	 * @param args
	 * @return
	 */
	public static String getCodeEmailPm(String str,String goodsId,String count,String price){
		StringBuilder text = new StringBuilder();
		text.append(CODE_EMAIL_HEAD);
		text.append(str);
		text.append("ᾮ");
		text.append(goodsId).append(CODE_EMAIL_SPLIT);
		text.append(count).append(CODE_EMAIL_SPLIT);
		text.append(price);
		return text.toString();
	}
	/**
	 * 将道具字符串转为邮件附件字符串集合(每12份道具为一封邮件附件)
	 * @param item
	 * @return
	 */
	public static String[] getAttachments(String item){
		Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap(item);
		return getAttachments(itemMap);
	}
	public static String[] getAttachments(Map<String, Integer> itemMap){
		int size = itemMap.size() / GameConstants.EMAIL_MAX_ITEMS_COUNT;
		if(itemMap.size() % GameConstants.EMAIL_MAX_ITEMS_COUNT > 0){
			size ++;
		}
		String[] items = new String[size];
		StringBuffer sb = new StringBuffer();
		int index = 0;
		int count = 0;
		for (Entry<String, Integer> entry : itemMap.entrySet()) {
			if(count == GameConstants.EMAIL_MAX_ITEMS_COUNT){
				items[index] = sb.substring(1);
				index++;
				count = 0;
				sb = new StringBuffer();
			}
			sb.append(SPLIT_CHAR).append(entry.getKey()).append(SUB_SPLIT_CHAR).append(entry.getValue());
			count++;
		}
		items[index] = sb.substring(1);
		return items;
	}
	public static String[] getAttachmentsForGoodsVo(Map<String, GoodsConfigureVo> itemMap){
		int size = itemMap.size() / GameConstants.EMAIL_MAX_ITEMS_COUNT;
		if(itemMap.size() % GameConstants.EMAIL_MAX_ITEMS_COUNT > 0){
			size ++;
		}
		String[] items = new String[size];
		StringBuffer sb = new StringBuffer();
		int index = 0;
		int count = 0;
		for (GoodsConfigureVo vo : itemMap.values()) {
			if(count == GameConstants.EMAIL_MAX_ITEMS_COUNT){
				items[index] = sb.substring(1);
				index++;
				count = 0;
				sb = new StringBuffer();
			}
			sb.append(SPLIT_CHAR).append(vo.getGoodsId()).append(SUB_SPLIT_CHAR).append(vo.getGoodsCount());
			if(vo.getQhLevel() > 0){
				sb.append(SUB_SPLIT_CHAR).append(vo.getQhLevel());
			}
		}
		items[index] = sb.substring(1);
		return items;
	}
	
	public static List<String[]> atta2Items(String atta){
		if(ObjectUtil.strIsEmpty(atta)){
			return null;
		}
		List<String[]> list = new ArrayList<>();
		String[] strs = atta.split(SPLIT_CHAR);
		for (String str : strs) {
			list.add(str.split(SUB_SPLIT_CHAR));
		}
		return list;
	}
	
	public static int getEmailMaxCount(){
		MailPublicConfig mailPublicconfig = PublicConfigureHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_MAIL);
		return mailPublicconfig == null ? 100 : mailPublicconfig.getMaxnum();
	}
	
	/**
	 * 没附件过期
	 * @return
	 */
	public static long getBaseGuoQi(){
		
		MailPublicConfig mailPublicconfig = PublicConfigureHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_MAIL);
		return mailPublicconfig == null ? 7 * 24 * 60 * 60 * 1000 : mailPublicconfig.getGouqi2();
	}
	
	/**
	 * 有附件过期
	 * @return
	 */
	public static long getGuoQi(){
		MailPublicConfig mailPublicconfig = PublicConfigureHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_MAIL);
		return mailPublicconfig == null ? 15 * 24 * 60 * 60 * 1000 : mailPublicconfig.getGouqi1();
	}
}
