package com.junyou.gameconfig.export;

/**
 * @description 坐标点大小(用于多格寻路和占位)
 * @author ShiJie Chi
 * @date 2013-3-26 上午10:44:47 
 */
public enum PathNodeSize {
	_1X,_2X,_4X;
	
	public static int getPathNodeAttrackSizeVal(PathNodeSize pathNodeSize){
		if(pathNodeSize.equals(_4X)){
			return 4;
		}else if(pathNodeSize.equals(_2X)){
			return 2;
		}else{
			return 1;
		}
	}
	
	
}
