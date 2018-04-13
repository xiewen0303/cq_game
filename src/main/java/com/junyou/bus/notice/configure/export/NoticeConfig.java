package com.junyou.bus.notice.configure.export;

/**
 * 公告内容
 * @author zhubo 
 * @email  zhubo@junyougame.com
 * @date 2015-4-21 上午10:51:01
 */
public class NoticeConfig  {
	private Integer id;
	private String text;
	private Long startTime;
	private Long endTime;
	private Integer step;
	private Integer location;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public Integer getStep() {
		return step;
	}
	public void setStep(Integer step) {
		this.step = step;
	}
	public Integer getLocation() {
		return location;
	}
	public void setLocation(Integer location) {
		this.location = location;
	}
}
