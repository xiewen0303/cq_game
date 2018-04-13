package com.hehj.easyexecutor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.hehj.easyexecutor.enumeration.EasyGroup;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EasyWorker {

	String workerName() default "";
	
	/**
	 * moduleName 模块名称
	 * @return
	 */
	String moduleName() default "";
	/**
	 * groupName 指令执行组类型	{@link EasyGroup}
	 * @return
	 */
	EasyGroup groupName() default EasyGroup.BUS_CACHE;
}
