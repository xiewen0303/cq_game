package com.kernel.check.db.annotation;

import java.lang.annotation.*;

/**
 * 外键标识
 * @author DaoZheng Yuan
 * 2014年12月22日 下午3:56:13
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	/**
	 * @return 字段名
	 */
	String value();

	/**
	 * @return 备注
	 */
	String comment() default "这家伙什么都没写";
}
