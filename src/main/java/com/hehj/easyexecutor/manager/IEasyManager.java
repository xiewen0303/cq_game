package com.hehj.easyexecutor.manager;

import com.hehj.easyexecutor.resolver.IEasyResolver;

/**
 * @description
 * 命令管理
 * @author hehj
 * @created 2009-11-30 下午02:04:30
 */
public interface IEasyManager {

	public IEasyResolver getResolver(Short command);
	
}
