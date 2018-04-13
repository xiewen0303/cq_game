package com.kernel.data.write.async;

/**
 * @description 回写业务执行单元
 * @author hehj
 * 2011-11-8 下午3:26:55
 */
public class AsyncWriteUnit {

	private String id;
	private AsyncWriteWorker worker;
	
	public AsyncWriteUnit(String unitid,AsyncWriteWorker writeWorker) {
		this.id = unitid;
		this.worker = writeWorker;
	}
	
	/**
	 * 处理回写业务
	 */
	public void flush(){
		worker.sync(id);
	}
	
}
