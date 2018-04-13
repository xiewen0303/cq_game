/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xinmo.filter;

import com.junyou.bus.xinmo.entity.RoleXinmoMoshen;
import com.kernel.data.dao.IQueryFilter;

/**
 *@Description
 *@Author Yang Gao
 *@Since 2016-8-2
 *@Version 1.1.0
 */
public class XinmoMoshenFilter implements IQueryFilter<RoleXinmoMoshen> {
    private int xmMoshenType;
    
    public XinmoMoshenFilter(int xmMoshenType) {
        this.xmMoshenType = xmMoshenType;
    }
    
    @Override
    public boolean check(RoleXinmoMoshen entity) {
        Integer xmMoshen_type = entity.getXmMoshenType();
        if (null != xmMoshen_type && xmMoshen_type.equals(xmMoshenType)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean stopped() {
        return false;
    }

}
