/**
 * 
 */
package com.junyou.utils.name;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.utils.ChuanQiConfigUtil;


/**
 * @description
 * @author ShiJie Chi
 * @created 2011-11-9上午11:29:23
 */
public class RoleFieldCheck {
	
	/**
	 * 正则匹配包含指定英文符号
	 */
	public static final String CHECK_USER_NAEM_REGX = "[^!`~@#\\$%\\^&\\*\\+\\(\\)\\.,\\?'\":;\\[\\]\\{\\}|<>//\\\\\\s]*";
	public static final String CHECK_USER_NAEM_REGX_YN = "[^!`~@#\\$%\\^&\\*\\+\\(\\)\\.,\\?'\":;\\[\\]\\{\\}|<>//\\\\]*";
	public static final String CHECK_USER_NAEM_REGX_HG = "[^!`~@#\\$%\\^&\\*\\+\\(\\),\\?'\":;\\[\\]\\{\\}|<>//\\\\]*";
	
	/**
	 * 正则字符串
	 * 
	 * 1.标准CJK文字  \u3400-\u4DB5\u4E00-\u9FA5\u9FA6-\u9FBB\uF900-\uFA2D\uFA30-\uFA6A\uFA70-\uFAD9\u20000-\u2A6D6\u2F800-\u2FA1D
	 * 2.全角ASCII、全角中英文标点、半宽片假名、半宽平假名、半宽韩文字母：FF00-FFEF
     * 3.CJK部首补充：\u2E80-\u2EFF
	 * 4.CJK标点符号：\u3001-\u303F  除去了\u3000(全角空格)
     * 5.CJK笔划：\u31C0-\u31EF
     * 6.注音符号：\u3100-\u312F
     * 越南半角空格：\u0020-
	 */
	private static final String REGX_CHAR = "[\u3400-\u4DB5\u4E00-\u9FA5\u9FA6-\u9FBB\uF900-\uFA2D\uFA30-\uFA6A\uFA70-\uFAD9\u20000-\u2A6D6\u2F800-\u2FA1D\uFF00-\uFFEF\u2E80-\u2EFF\u3001-\u303F\u31C0-\u31EF\u3100-\u312F]+";
	private static final String REGX_CHAR_YN = "[\u0020-\u3400-\u4DB5\u4E00-\u9FA5\u9FA6-\u9FBB\uF900-\uFA2D\uFA30-\uFA6A\uFA70-\uFAD9\u20000-\u2A6D6\u2F800-\u2FA1D\uFF00-\uFFEF\u2E80-\u2EFF\u3001-\u303F\u31C0-\u31EF\u3100-\u312F]+";
	private static final String REGX_CHAR_HG = "[\u0020-\u3400-\u4DB5\u4E00-\u9FA5\u9FA6-\u9FBB\uF900-\uFA2D\uFA30-\uFA6A\uFA70-\uFAD9\u20000-\u2A6D6\u2F800-\u2FA1D\uFF00-\uFFEF\u2E80-\u2EFF\u3001-\u303F\u31C0-\u31EF\u3100-\u312F]+";
	
	/**
	 * 验证角色名字是否含有不允许的特定字符
	 * 
	 * @param userName
	 * @return true:合法
	 */
	private static boolean checkDeleteChar(String userName){
		Pattern p = Pattern.compile(CHECK_USER_NAEM_REGX);
		Matcher m = p.matcher(userName);
		return  m.matches();
	}
	/**
	 * 验证角色名字是否含有不允许的特定字符越南
	 * 
	 * @param userName
	 * @return true:合法
	 */
	private static boolean checkDeleteCharYN(String userName){
		Pattern p = Pattern.compile(CHECK_USER_NAEM_REGX_YN);
		Matcher m = p.matcher(userName);
		return  m.matches();
	}
	
	/**
     * 验证角色名字是否含有不允许的特定字符-韩国
     * 
     * @param userName
     * @return true:合法
     */
    private static boolean checkDeleteCharHG(String userName){
        Pattern p = Pattern.compile(CHECK_USER_NAEM_REGX_HG);
        Matcher m = p.matcher(userName);
        return  m.matches();
    }
	
	/**
	 *  验证字符区间
	 * @param userName
	 * @return true:合法
	 */
	private static boolean checkIntervalChar(String userName){
		if( ChuanQiConfigUtil.isCheckCharsetRegion() ){
			
			Pattern p = Pattern.compile(REGX_CHAR);
			Matcher m = p.matcher(userName);
			return m.matches();
		}else{
			return true;
		}
	}
	/**
	 *  验证字符区间越南
	 * @param userName
	 * @return true:合法
	 */
	private static boolean checkIntervalCharYN(String userName){
		if( ChuanQiConfigUtil.isCheckCharsetRegion() ){
			
			Pattern p = Pattern.compile(REGX_CHAR_YN);
			Matcher m = p.matcher(userName);
			return m.matches();
		}else{
			return true;
		}
	}
	
	/**
     *  验证字符区间韩国
     * @param userName
     * @return true:合法
     */
    private static boolean checkIntervalCharHG(String userName){
        if( ChuanQiConfigUtil.isCheckCharsetRegion() ){
            Pattern p = Pattern.compile(REGX_CHAR_HG);
            Matcher m = p.matcher(userName);
            return m.matches();
        }else{
            return true;
        }
    }
    
	/**
	 * 验证字符合法
	 * @return true:合法
	 */
	public static boolean checkInputName(String userName){
		if(null!=userName && (userName.length() > 12 || userName.length() == 0)) return false;
		return checkInput(userName);
	}
	
	/**
	 * 验证字符合法
	 * @return true:合法
	 */
	public static boolean checkInput(String userName){
		if(PlatformConstants.isYueNan()){
			//判定字符区间 (1)
			if(!checkIntervalCharYN(userName)){
				return false;
			}
			//判定不合法的指定字符(2)
			if(!checkDeleteCharYN(userName)){
				return false;
			}
		}else if(PlatformConstants.isHanGuo()){
		    //判定字符区间 (1)
            if(!checkIntervalCharHG(userName)){
                return false;
            }
            //判定不合法的指定字符(2)
            if(!checkDeleteCharHG(userName)){
                return false;
            }
		}else{
			//判定字符区间 (1)
			if(!checkIntervalChar(userName)){
				return false;
			}
			//判定不合法的指定字符(2)
			if(!checkDeleteChar(userName)){
				return false;
			}
		}
		// 敏感字处理(3):
		return !MinganciUtil.check(userName);
	}
}
