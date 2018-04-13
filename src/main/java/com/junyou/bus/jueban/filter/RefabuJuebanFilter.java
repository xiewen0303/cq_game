package com.junyou.bus.jueban.filter;

import com.junyou.bus.jueban.entity.RefabuJueban;
import com.kernel.data.dao.IQueryFilter;

/**
 * @Description 绝版礼包数据过滤器
 * @Author Yang Gao
 * @Since 2016-6-6
 * @Version 1.1.0
 */
public class RefabuJuebanFilter implements IQueryFilter<RefabuJueban> {

    private Integer subId;

    public RefabuJuebanFilter(Integer subId) {
        this.subId = subId;
    }

    @Override
    public boolean check(RefabuJueban entity) {
        if (null != entity && entity.getSubId().intValue() == subId) {
            return true;
        }
        return false;
    }

    @Override
    public boolean stopped() {
        return false;
    }

}
