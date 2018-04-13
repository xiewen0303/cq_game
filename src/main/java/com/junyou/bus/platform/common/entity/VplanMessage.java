package com.junyou.bus.platform.common.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * 360 V计划用户信息对象
 * @author lxn
 *3、当errno为-1，-2时请注意检查参数传入是否有问题，当errno为-3或者2001时，建议给出默认值（按非会员处理），记录相关日志并反馈给我们。
 *目前服务运行正常，没有出现系统错误，极少情况（网络抖动等原因）会出现超时，各业务接入的时候注意容错~
 */
public class VplanMessage implements Serializable {
	private int errno;
	private String errmsg;
	private Data data;//玩家主数据
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public int getErrno() {
		return errno;
	}
	public void setErrno(int errno) {
		this.errno = errno;
	}
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	
	//主体数据
	public class Data{
		private  String version;
		private Long uid;
		private String type;
		private int is_send;
		private int level;
		private int value;
		private int growth_speed;
		private int left_day;
		
		private Map<String, Long> end_time;
		private Map<String, Long> value_range;
		private Map<String, Long> level_range;
		private Map<String, Long> expire_time;
		
		/**
		 * 获取会员过期时间
		 * @return
		 */
		public Long getEndTime(){
			if(type.equals("Y")){
				return  this.is_send==0?this.getEnd_time().get("year_true"):this.getEnd_time().get("year");
			}else if((type.equals("M"))) {
				return  this.is_send==0?this.getEnd_time().get("month_true"):this.getEnd_time().get("month");
			}
			return  0L;
		}
		/**
		 * Y：年费会员，M：月费会员， E: 过期会员， N：非会员
		 * @return
		 */
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public int getIs_send() {
			return is_send;
		}
		public void setIs_send(int is_send) {
			this.is_send = is_send;
		}
		public int getLevel() {
			return level;
		}
		public void setLevel(int level) {
			this.level = level;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		public int getGrowth_speed() {
			return growth_speed;
		}
		public void setGrowth_speed(int growth_speed) {
			this.growth_speed = growth_speed;
		}
		public int getLeft_day() {
			return left_day;
		}
		public void setLeft_day(int left_day) {
			this.left_day = left_day;
		}
		/**
		 *  "end_time": {
            "year_true": 1445875200,
            "year": 1445875200,
            "month_true": 1445875200,
            "month": 1445875200
        	}
		 */
		public Map<String, Long> getEnd_time() {
			return end_time;
		}
		public void setEnd_time(Map<String, Long> end_time) {
			this.end_time = end_time;
		}
		/**
		 * "value_range": {
            "min": 400,
            "max": 1199
        }*/
		public Map<String, Long> getValue_range() {
			return value_range;
		}
		public void setValue_range(Map<String, Long> value_range) {
			this.value_range = value_range;
		}
		/** "level_range": {
            "min": 1,
            "max": 5
        }*/
		public Map<String, Long> getLevel_range() {
			return level_range;
		}
		public void setLevel_range(Map<String, Long> level_range) {
			this.level_range = level_range;
		}
		/**"expire_time": {
            "month": 1445788800,
            "year": 1445788800
        }*/
		public Map<String, Long> getExpire_time() {
			return expire_time;
		}
		public void setExpire_time(Map<String, Long> expire_time) {
			this.expire_time = expire_time;
		}
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
		public Long getUid() {
			return uid;
		}
		public void setUid(Long uid) {
			this.uid = uid;
		}
		 
	}
}
//********************各字段说明*****************
//version             当前请求接口的版本
//uid					用户uid 
//type(重要)			当前类型（Y、M、E、N）
//is_send(重要)		当前是否处于赠送体验阶段（0、1）
//level(重要)			当前等级
//value(重要)			当前成长值
//end_time	
//  year_true		真实年费过期时间戳
//	year    		年费过期时间戳
//	month_true		真实月费过期时间戳
//	month    		月费过期时间戳
//value_range			
//	min				当前等级最小经验值
//	max				当前等级最大经验值
//level_range			（后续可能会增加）
//	min				目前已经开通的最小等级
//	max				目前已经开通的最大等级
//growth_speed		经验值成长速度（15、12、-12、0） 
//left_day			还有多少天到期
//expire_time（待废弃）
//	month 			月费到期时间
//	year 			年费到期时间


//{
//    "errno": 0,
//    "errmsg": "",
//    "data": {
//        "version": "3.0",
//        "uid": 1402167009,
//        "type": "Y",
//        "is_send": 0,
//        "level": 2,
//        "value": 700,
//        "end_time": {
//            "year_true": 1445875200,
//            "year": 1445875200,
//            "month_true": 1445875200,
//            "month": 1445875200
//        },
//        "value_range": {
//            "min": 400,
//            "max": 1199
//        },
//        "level_range": {
//            "min": 1,
//            "max": 5
//        },
//        "growth_speed": 15,
//        "left_day": 69,
//        "expire_time": {
//            "month": 1445788800,
//            "year": 1445788800
//        }
//    }
//}
