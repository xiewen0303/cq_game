package com.kernel.check.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识该字段为实体中特有属性，不对应数据库中字段
 * @author zl
 * @date 2014-10-16 下午12:42:42
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityField {
	String value() default "";
}
