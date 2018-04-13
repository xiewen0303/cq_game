package com.kernel.check.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表名标识
 * @author DaoZheng Yuan
 * 2014年12月22日 下午3:56:44
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
	/**
	 * @return 表名
	 */
	String value();

	/**
	 * @return 备注
	 */
	String comment() default "一般是什么也没写";
}
