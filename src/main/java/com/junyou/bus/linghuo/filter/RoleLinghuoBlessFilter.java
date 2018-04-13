/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.linghuo.filter;

import com.junyou.bus.linghuo.entity.RoleLinghuoBless;
import com.kernel.data.dao.IQueryFilter;

/**
 *@Description 灵火祝福数据过滤器
 *@Author Yang Gao
 *@Since 2016-9-19
 *@Version 1.1.0
 */
public class RoleLinghuoBlessFilter implements IQueryFilter<RoleLinghuoBless> {

    /*灵火id*/
    private Integer linghuoId;
    /*灵火格位号*/
    private Integer linghuoSlot;
    
    public RoleLinghuoBlessFilter(Integer linghuoId, Integer linghuoSlot) {
        this.linghuoId = linghuoId;
        this.linghuoSlot = linghuoSlot;
    }

    @Override
    public boolean check(RoleLinghuoBless entity) {
        if (this.linghuoId != null && this.linghuoSlot != null) {
            return this.linghuoId.equals(entity.getLinghuoId()) && this.linghuoSlot.equals(entity.getLinghuoSlot());
        } 
        else if (this.linghuoId != null) {
            return this.linghuoId.equals(entity.getLinghuoId());
        } 
        else if (this.linghuoSlot != null) {
            return this.linghuoSlot.equals(entity.getLinghuoSlot());
        }
        return false;
    }

    @Override
    public boolean stopped() {
        return false;
    }


}
