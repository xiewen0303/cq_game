/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.wuxing.constants;

import java.util.ArrayList;
import java.util.List;

/**
 *@Description
 *@Author Yang Gao
 *@Since 2016-5-10
 *@Version 1.1.0
 */
public enum WuxingMoshenType {
    /** 金属性魔神  **/
    GOLD(1),
    /** 木属性魔神 **/
    WOOD(2),
    /** 土属性魔神 **/
    EARTH(3),
    /** 水属性魔神 **/
    WATER(4),
    /** 火属性魔神 **/
    FIRE(5),
    ;
    private int type ;

    /**
     * @param type
     */
    private WuxingMoshenType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    public static List<Integer> findAllWuxingMoshen(){
        List<Integer> list = new ArrayList<>();
        for(WuxingMoshenType wxtype: WuxingMoshenType.values()){
            list.add(wxtype.getType());
        }
        return list;
    }
    
    public static boolean isWxMoshen(int t){
        return findAllWuxingMoshen().contains(t);
    }
    
}
