package com.kernel.token.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description 业务方法同步配置元数据
 * @author hehj
 * 2011-11-29 下午3:26:41
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenCheck {
	
	/**
	 * 设置要校验的token的组件标识
	 */
	String component();
	
}
