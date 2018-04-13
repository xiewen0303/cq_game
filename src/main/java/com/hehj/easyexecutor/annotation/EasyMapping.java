package com.hehj.easyexecutor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.hehj.easyexecutor.enumeration.EasyKuafuType;

/**
 * 指令映射注解
 * @author DaoZheng Yuan</br>
 * 2014-11-12 下午4:17:04
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EasyMapping {
	/**
	 * mapping 指令(必填值)
	 * @return
	 */
	short mapping();

	/**
	 * kuafuType 跨服指令类型	{@link EasyKuafuType}
	 * @return
	 */
	EasyKuafuType kuafuType() default EasyKuafuType.NOT_KUAFU_TYPE;
}
