package com.kernel.sync.annotation;

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
public @interface Sync {
	/**
	 * 设置同步的组件标识
	 */
	String component();
	
	/**
	 * 指定同步对象的参数位置<br>
	 * <b>注：</b>参数在indexes中的不同位置会影响同步对象的取值
	 */
	int[]	   indexes();
}
