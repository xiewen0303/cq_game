package com.junyou.bus.flower.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.flower.service.FlowerService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

/**
 * @author lxn 2015-11-3 上午11:44:34
 */
@Controller
@EasyWorker(moduleName = GameModType.FLOWER_SEND, groupName = EasyGroup.BUS_CACHE)
public class FlowerAction {

	@Autowired
	private FlowerService flowerService;

	@EasyMapping(mapping = ClientCmdType.FLOWER_SEND)
	public void getInfo(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		long receiverRoleId = LongUtils.obj2long(data[0]);
		long guid = LongUtils.obj2long(data[1]);
		Object[] ret = flowerService.sendFlower(userRoleId, guid, receiverRoleId);
		if (ret != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.FLOWER_SEND, ret);
		}
	}

	/**
	 * 测试redis用法
	 * 
	 * @param inMsg
	@EasyMapping(mapping = ClientCmdType.FLOWER_TEST)
	public void test(Message inMsg) {
		Long userRoleId = inMsg.getRoleId();
		Redis redis = GameServerContext.getRedis();
//		 String testKey = "lxn_test_1";
//		 redis.zadd(testKey, 1, "11111");
//		 redis.zadd(testKey, 1, "33333");
//		 redis.zadd(testKey, 1, "55555");
//		 redis.zadd(testKey, 2, "22222"); 
//		 redis.zadd(testKey, 1, "66666"); 
		 
//		 Set<Tuple> set = redis.zrevrangeWithScore(testKey, 0, -1);
//			for (Tuple tuple : set) {
//				System.out.println(tuple.getElement() + ":" + tuple.getScore());
//			}
//		  System.out.println(redis.zrevrank(testKey, "66666"));
		System.out.println(redis.hget("yrt", RfbFlowerConstants.FIELD_SHANGBANG_TIME));
	}
	 */
	
	 
}
