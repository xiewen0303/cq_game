package com.junyou.bus.tanbao.entity;

import java.util.ArrayList;
import java.util.List;

import com.junyou.gameconfig.export.DropRule;

/**
 * @author LiuYu
 * 2015-6-17 下午10:04:07
 */
public class TanBaoBaoXiangConfig {
	private Integer id;
	private List<Integer[]> zuobiao;
	//掉落物品的消失时间（秒）
	private Integer dropGoodsDuration;
	// 掉落保护时间（秒）
	private Integer dropProtectDuration;
	//刷新公告
	private String gonggao;
	//采集公告
	private String gonggao1;
	private List<DropRule> dropRule = new ArrayList<DropRule>();
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public List<Integer[]> getZuobiao() {
		return new ArrayList<>(zuobiao);
	}
	public void setZuobiao(List<Integer[]> zuobiao) {
		this.zuobiao = zuobiao;
	}
	public Integer getDropGoodsDuration() {
		return dropGoodsDuration;
	}
	public void setDropGoodsDuration(Integer dropGoodsDuration) {
		this.dropGoodsDuration = dropGoodsDuration;
	}
	public Integer getDropProtectDuration() {
		return dropProtectDuration;
	}
	public void setDropProtectDuration(Integer dropProtectDuration) {
		this.dropProtectDuration = dropProtectDuration;
	}
	public List<DropRule> getDropRule() {
		return dropRule;
	}
	public void setDropRule(List<DropRule> dropRule) {
		this.dropRule = dropRule;
	}
	public String getGonggao() {
		return gonggao;
	}
	public void setGonggao(String gonggao) {
		this.gonggao = gonggao;
	}
	public String getGonggao1() {
		return gonggao1;
	}
	public void setGonggao1(String gonggao1) {
		this.gonggao1 = gonggao1;
	}
	
	
}
