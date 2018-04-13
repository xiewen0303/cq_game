package com.junyou.bus.jingji.entity;

import com.junyou.bus.daytask.configure.BeginEnd;

public class RankShowConfig {
	private int id;
//	private int min;
//	private int max;

	private BeginEnd beginEnd;
	private int rank1Begin;
	private int rank1End;

	private int rank2Begin;
	private int rank2End;

	private int rank3Begin;
	private int rank3End;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BeginEnd getBeginEnd() {
		return beginEnd;
	}

	public void setBeginEnd(BeginEnd beginEnd) {
		this.beginEnd = beginEnd;
	}

	public int getRank1Begin() {
		return rank1Begin;
	}

	public void setRank1Begin(int rank1Begin) {
		this.rank1Begin = rank1Begin;
	}

	public int getRank1End() {
		return rank1End;
	}

	public void setRank1End(int rank1End) {
		this.rank1End = rank1End;
	}

	public int getRank2Begin() {
		return rank2Begin;
	}

	public void setRank2Begin(int rank2Begin) {
		this.rank2Begin = rank2Begin;
	}

	public int getRank2End() {
		return rank2End;
	}

	public void setRank2End(int rank2End) {
		this.rank2End = rank2End;
	}

	public int getRank3Begin() {
		return rank3Begin;
	}

	public void setRank3Begin(int rank3Begin) {
		this.rank3Begin = rank3Begin;
	}

	public int getRank3End() {
		return rank3End;
	}

	public void setRank3End(int rank3End) {
		this.rank3End = rank3End;
	}
}
