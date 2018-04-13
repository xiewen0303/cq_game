package com.junyou.bus.platform.qq.service.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.platform.qq.service.QQShuJiaHuoDongService;
/**
 * @author zhongdian
 * 2015-8-4 下午2:11:32
 */
@Service
public class QQShuJiaHuoDongExportService {

	@Autowired
	private QQShuJiaHuoDongService qqShuJiaHuoDongService;
	/**
	 * 执行腾讯暑假活动调用
	 */
	public void sendPlayzoneTask(Long userRoleId,String task){
		qqShuJiaHuoDongService.sendPlayzoneTask(userRoleId, task);
	}
}
