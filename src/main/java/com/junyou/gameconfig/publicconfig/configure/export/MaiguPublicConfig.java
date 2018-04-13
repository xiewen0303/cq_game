package com.junyou.gameconfig.publicconfig.configure.export;


/**
 * 埋骨
 * 
 * @author LiuYu
 * @date 2015-7-8 上午9:37:35
 */
public class MaiguPublicConfig extends AdapterPublicConfig {
	/**
	 * 守护NPC的ID
	 */
	private String mgnpc;
	/**
	 * 守护NPC的坐标
	 */
	private int[] shzuobiao;
	/**
	 * 怪物的坐标，在固定坐标周围
	 */
	private int[][] gwzuobiao;
	/**
	 * 地图ID
	 */
	private int mapid;
	/**
	 * 4位玩家进入场景后的移动到指定坐标
	 */
	private int[][] fszb;

	public String getMgnpc() {
		return mgnpc;
	}

	public void setMgnpc(String mgnpc) {
		this.mgnpc = mgnpc;
	}

	public int[] getShzuobiao() {
		return shzuobiao;
	}

	public void setShzuobiao(int[] shzuobiao) {
		this.shzuobiao = shzuobiao;
	}

	public int[][] getGwzuobiao() {
		return gwzuobiao;
	}

	public void setGwzuobiao(int[][] gwzuobiao) {
		this.gwzuobiao = gwzuobiao;
	}

	public int getMapid() {
		return mapid;
	}

	public void setMapid(int mapid) {
		this.mapid = mapid;
	}

	public int[][] getFszb() {
		return fszb;
	}

	public void setFszb(int[][] fszb) {
		this.fszb = fszb;
	}

}
