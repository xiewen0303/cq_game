package com.kernel.check.db.annotation;

import java.lang.annotation.*;

/**
 * 主键标识
 * @author DaoZheng Yuan
 * 2014年12月22日 下午3:56:35
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Primary {
}
