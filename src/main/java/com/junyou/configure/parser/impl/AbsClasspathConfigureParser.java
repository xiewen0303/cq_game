package com.junyou.configure.parser.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.junyou.configure.loader.ClasspathConfigureLoader;
import com.junyou.configure.loader.IConfigureLoader;
import com.junyou.configure.parser.AbsConfigureParser;

/**
 * @description 类路径配置解析器
 * @author ShiJie Chi
 * @date 2013-2-25 下午2:23:51 
 */
public abstract class AbsClasspathConfigureParser extends AbsConfigureParser {

	@Autowired
	private ClasspathConfigureLoader configureLoader;
	
	@Override
	protected IConfigureLoader getLoader() {
		return configureLoader;
	}

}
