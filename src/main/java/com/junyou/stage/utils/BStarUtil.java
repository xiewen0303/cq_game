package com.junyou.stage.utils;

import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.aoi.AoiPoint;

/**
 * B星寻路算法
 * @author DaoZheng Yuan
 * 2013-12-27 下午4:08:04
 */
public class BStarUtil {
	
	private static Point POS_0 = new Point(0, 1);

	private static Point POS_1 = new Point(1, 1);

	private static Point POS_2 = new Point(1, 0);

	private static Point POS_3 = new Point(1, -1);
	
	private static Point POS_4 = new Point(0, -1);

	private static Point POS_5 = new Point(-1, -1);

	private static Point POS_6 = new Point(-1, 0);

	private static Point POS_7 = new Point(-1, 1);
	
	
	private static Point[][][] POINTS = new Point[][][]{
		/*================from 0=================**/
		{/*to0*/{POS_7,  POS_1,  POS_6,  POS_2,  POS_5,  POS_3,  POS_4,  POS_0},  
		/*to1*/{POS_1,  POS_2,  POS_3,  POS_4,  POS_7,  POS_6,  POS_5,  POS_0},  
		/*to2*/{POS_2,  POS_3,  POS_4,  POS_5,  POS_1,  POS_7,  POS_6,  POS_0},  
		/*to3*/{POS_3,  POS_4,  POS_5,  POS_6,  POS_2,  POS_1,  POS_7,  POS_0},  
		/*to4*/{POS_4,  POS_5,  POS_3,  POS_6,  POS_2,  POS_7,  POS_1,  POS_0},  
		/*to5*/{POS_5,  POS_4,  POS_3,  POS_2,  POS_6,  POS_7,  POS_1,  POS_0},  
		/*to6*/{POS_6,  POS_5,  POS_4,  POS_3,  POS_7,  POS_1,  POS_2,  POS_0},  
		/*to7*/{POS_7,  POS_6,  POS_5,  POS_4,  POS_1,  POS_2,  POS_3,  POS_0}
		},/*================from 1=================**/
		{/*to0*/{POS_0,  POS_7,  POS_6,  POS_5,  POS_2,  POS_3,  POS_4,  POS_1},  
		/*to1*/{POS_0,  POS_2,  POS_7,  POS_3,  POS_6,  POS_4,  POS_5,  POS_1},  
		/*to2*/{POS_2,  POS_3,  POS_4,  POS_5,  POS_0,  POS_7,  POS_6,  POS_1},  
		/*to3*/{POS_3,  POS_4,  POS_5,  POS_6,  POS_2,  POS_0,  POS_7,  POS_1},  
		/*to4*/{POS_4,  POS_5,  POS_6,  POS_7,  POS_3,  POS_2,  POS_0,  POS_1},  
		/*to5*/{POS_5,  POS_6,  POS_4,  POS_7,  POS_3,  POS_0,  POS_2,  POS_1},  
		/*to6*/{POS_6,  POS_5,  POS_4,  POS_3,  POS_7,  POS_0,  POS_2,  POS_1},  
		/*to7*/{POS_7,  POS_6,  POS_5,  POS_4,  POS_0,  POS_2,  POS_3,  POS_1}
		},/*================from 2=================**/
		{/*to0*/{POS_0,  POS_7,  POS_6,  POS_5,  POS_1,  POS_3,  POS_4,  POS_2},  
		/*to1*/{POS_1,  POS_0,  POS_7,  POS_6,  POS_3,  POS_4,  POS_5,  POS_2},  
		/*to2*/{POS_1,  POS_3,  POS_0,  POS_4,  POS_7,  POS_5,  POS_6,  POS_2},  
		/*to3*/{POS_3,  POS_4,  POS_5,  POS_6,  POS_1,  POS_0,  POS_7,  POS_2},  
		/*to4*/{POS_4,  POS_5,  POS_6,  POS_7,  POS_3,  POS_1,  POS_0,  POS_2},  
		/*to5*/{POS_5,  POS_6,  POS_7,  POS_0,  POS_4,  POS_3,  POS_1,  POS_2},  
		/*to6*/{POS_6,  POS_7,  POS_5,  POS_0,  POS_4,  POS_1,  POS_3,  POS_2},  
		/*to7*/{POS_7,  POS_6,  POS_5,  POS_4,  POS_0,  POS_1,  POS_3,  POS_2}
		},/*================from 3=================**/
		{/*to0*/{POS_0,  POS_7,  POS_6,  POS_5,  POS_1,  POS_2,  POS_4,  POS_3},  
		/*to1*/{POS_1,  POS_0,  POS_7,  POS_6,  POS_2,  POS_4,  POS_5,  POS_3},  
		/*to2*/{POS_2,  POS_1,  POS_0,  POS_7,  POS_4,  POS_5,  POS_6,  POS_3},  
		/*to3*/{POS_2,  POS_4,  POS_1,  POS_5,  POS_0,  POS_6,  POS_7,  POS_3},  
		/*to4*/{POS_4,  POS_5,  POS_6,  POS_7,  POS_2,  POS_1,  POS_0,  POS_3},  
		/*to5*/{POS_5,  POS_6,  POS_7,  POS_0,  POS_4,  POS_2,  POS_1,  POS_3},  
		/*to6*/{POS_6,  POS_7,  POS_0,  POS_1,  POS_5,  POS_4,  POS_2,  POS_3},  
		/*to7*/{POS_7,  POS_0,  POS_6,  POS_1,  POS_5,  POS_2,  POS_4,  POS_3}
		},/*================from 4=================**/
		{/*to0*/{POS_0,  POS_1,  POS_7,  POS_2,  POS_6,  POS_3,  POS_5,  POS_4},  
		/*to1*/{POS_1,  POS_0,  POS_7,  POS_6,  POS_2,  POS_3,  POS_5,  POS_4},  
		/*to2*/{POS_2,  POS_1,  POS_0,  POS_7,  POS_3,  POS_5,  POS_6,  POS_4},  
		/*to3*/{POS_3,  POS_2,  POS_1,  POS_0,  POS_5,  POS_6,  POS_7,  POS_4},  
		/*to4*/{POS_3,  POS_5,  POS_2,  POS_6,  POS_1,  POS_7,  POS_0,  POS_4},  
		/*to5*/{POS_5,  POS_6,  POS_7,  POS_0,  POS_3,  POS_2,  POS_1,  POS_4},  
		/*to6*/{POS_6,  POS_7,  POS_0,  POS_1,  POS_5,  POS_3,  POS_2,  POS_4},  
		/*to7*/{POS_7,  POS_0,  POS_1,  POS_2,  POS_6,  POS_5,  POS_3,  POS_4}
		},/*================from 5=================**/
		{/*to0*/{POS_0,  POS_1,  POS_2,  POS_3,  POS_7,  POS_6,  POS_4,  POS_5},  
		/*to1*/{POS_1,  POS_2,  POS_0,  POS_3,  POS_7,  POS_4,  POS_6,  POS_5},  
		/*to2*/{POS_2,  POS_1,  POS_0,  POS_7,  POS_3,  POS_4,  POS_6,  POS_5},  
		/*to3*/{POS_3,  POS_2,  POS_1,  POS_0,  POS_4,  POS_6,  POS_7,  POS_5},  
		/*to4*/{POS_4,  POS_3,  POS_2,  POS_1,  POS_6,  POS_7,  POS_0,  POS_5},  
		/*to5*/{POS_4,  POS_6,  POS_3,  POS_7,  POS_2,  POS_0,  POS_1,  POS_5},  
		/*to6*/{POS_6,  POS_7,  POS_0,  POS_1,  POS_4,  POS_3,  POS_2,  POS_5},  
		/*to7*/{POS_7,  POS_0,  POS_1,  POS_2,  POS_6,  POS_4,  POS_3,  POS_5}
		},/*================from 6=================**/
		{/*to0*/{POS_0,  POS_1,  POS_2,  POS_3,  POS_7,  POS_5,  POS_4,  POS_6},  
		/*to1*/{POS_1,  POS_2,  POS_3,  POS_4,  POS_0,  POS_7,  POS_5,  POS_6},  
		/*to2*/{POS_2,  POS_3,  POS_1,  POS_4,  POS_0,  POS_5,  POS_7,  POS_6},  
		/*to3*/{POS_3,  POS_2,  POS_1,  POS_0,  POS_4,  POS_5,  POS_7,  POS_6},  
		/*to4*/{POS_4,  POS_3,  POS_2,  POS_1,  POS_5,  POS_7,  POS_0,  POS_6},  
		/*to5*/{POS_5,  POS_4,  POS_3,  POS_2,  POS_7,  POS_0,  POS_1,  POS_6},  
		/*to6*/{POS_5,  POS_7,  POS_4,  POS_0,  POS_3,  POS_1,  POS_2,  POS_6},  
		/*to7*/{POS_7,  POS_0,  POS_1,  POS_2,  POS_5,  POS_4,  POS_3,  POS_6}
		},/*================from 7=================**/
		{/*to0*/{POS_0,  POS_1,  POS_2,  POS_3,  POS_6,  POS_5,  POS_4,  POS_7},  
		/*to1*/{POS_1,  POS_2,  POS_3,  POS_4,  POS_0,  POS_6,  POS_5,  POS_7},  
		/*to2*/{POS_2,  POS_3,  POS_4,  POS_5,  POS_1,  POS_0,  POS_6,  POS_7},  
		/*to3*/{POS_3,  POS_4,  POS_2,  POS_5,  POS_1,  POS_6,  POS_0,  POS_7},  
		/*to4*/{POS_4,  POS_3,  POS_2,  POS_1,  POS_5,  POS_6,  POS_0,  POS_7},  
		/*to5*/{POS_5,  POS_4,  POS_3,  POS_2,  POS_6,  POS_0,  POS_1,  POS_7},  
		/*to6*/{POS_6,  POS_5,  POS_4,  POS_3,  POS_0,  POS_1,  POS_2,  POS_7},  
		/*to7*/{POS_6,  POS_0,  POS_5,  POS_1,  POS_4,  POS_2,  POS_3,  POS_7}
		},/*================ stop =================**/
		{/*to0*/{POS_0,  POS_1,  POS_7,  POS_2,  POS_6,  POS_3,  POS_5,  POS_4},  
		/*to1*/{POS_1,  POS_2,  POS_0,  POS_3,  POS_7,  POS_4,  POS_6,  POS_5},  
		/*to2*/{POS_2,  POS_3,  POS_1,  POS_4,  POS_0,  POS_5,  POS_7,  POS_6},  
		/*to3*/{POS_3,  POS_4,  POS_2,  POS_5,  POS_1,  POS_6,  POS_0,  POS_7},  
		/*to4*/{POS_4,  POS_5,  POS_3,  POS_6,  POS_2,  POS_7,  POS_1,  POS_0},  
		/*to5*/{POS_5,  POS_6,  POS_4,  POS_7,  POS_3,  POS_0,  POS_2,  POS_1},  
		/*to6*/{POS_6,  POS_7,  POS_5,  POS_0,  POS_4,  POS_1,  POS_3,  POS_2},  
		/*to7*/{POS_7,  POS_0,  POS_6,  POS_1,  POS_5,  POS_2,  POS_4,  POS_3}
		}};
	
	
	/**
	 * 计算方向
	 * </br>
	   [5],[4],[3]</br>
	   [6],[8],[2]</br>
	   [7],[0],[1]</br>
	 * 
	 * @param fx
	 * @param fy
	 * @param tx
	 * @param ty
	 * @return 返回方向值
	 */
	private static int getMouseDirection8(int fx,int fy, int tx, int ty){
		float d =(ty - fy) * 1f / (tx - fx) * 1f;
		if (fx <= tx){
			if (d > 2.414213562373095)
			{
				return 0;
			}else if (d > 0.41421356237309503){
				return 1;
			}else if (d > -0.41421356237309503){
				return 2;
			}else if (d > -2.414213562373095){
				return 3;
			}else{
				return 4;
			}
			
		}else{
			if (d <= -2.414213562373095){
				return 0;
			}else if (d <= -0.41421356237309503){
				return 7;
			}else if (d <= 0.41421356237309503){
				return 6;
			}else if (d <= 2.414213562373095){
				return 5;
			}else{
				return 4;
			}
		}
	}
	
	/**
	 * b星寻路
	 * @param from
	 * @param target
	 * @return Point[]
	 */
	public static Point[] findPints(Point from,Point target){
		int dir1 = 8;
		if(from.getHisX() > 0){
			dir1 = getMouseDirection8(from.getX(), from.getY(),from.getHisX(), from.getHisY());
		}
		
		int dir2 = getMouseDirection8(from.getX(), from.getY(), target.getX(), target.getY());
		
		return POINTS[dir1][dir2];
	}
	
	/**
	 * b星寻路
	 * @param from
	 * @param target
	 * @return Point[]
	 */
	public static Point[] findPints(Point from,AoiPoint target){
		int dir1 = 8;
		if(from.getHisX() > 0){
			dir1 = getMouseDirection8(from.getX(), from.getY(),from.getHisX(), from.getHisY());
		}
		
		int dir2 = getMouseDirection8(from.getX(), from.getY(), target.getX(), target.getY());
		
		return POINTS[dir1][dir2];
	}
}
