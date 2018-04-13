package com.junyou.bus.bag.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.bus.bag.ContainerType;
import com.junyou.bus.bag.entity.RoleItem;

/**
 * 
 * @Description 宠物身上的容器
 * @Author Yang Gao
 * @Since 2016-9-5
 * @Version 1.1.0
 */
public class ChongwuItems extends AbstractContainer {

    /**
     * 宠物穿戴在身上的部位信息
     * 
     * @key 宠物编号+部位Id
     * @value 物品
     */
    private Map<String, RoleItem> positions = new HashMap<String, RoleItem>();

    /**
     * 容器
     * 
     * @key guid 物品guid(全局唯一)
     * @value item 物品
     */
    private Map<Long, RoleItem> equipSlots = new HashMap<Long, RoleItem>();

    private String getChongwuEquipKey(Integer chongwuId, Integer slot) {
        return new StringBuilder().append(chongwuId).append("_").append(slot).toString();
    }

    public RoleItem removeItem(long guid) {
        RoleItem roleItem = equipSlots.remove(guid);
        return positions.remove(getChongwuEquipKey(roleItem.getChongwuId(), roleItem.getSlot()));
    }

    public RoleItem getItemByGuid(long guid) {
        return equipSlots.get(guid);
    }

    public RoleItem getItemByCwIdAndSlot(Integer chongwuId, Integer slot){
    	return positions.get(getChongwuEquipKey(chongwuId, slot));
    }
    
    public void addItem(RoleItem item) {
        equipSlots.put(item.getId(), item);
        positions.put(getChongwuEquipKey(item.getChongwuId(), item.getSlot()), item);
    }

    @Override
    public Integer getType() {
        return ContainerType.CHONGWUITEM.getType();
    }

    @Override
    public List<RoleItem> getItems() {
        return new ArrayList<RoleItem>(equipSlots.values());
    }

    @Override
    public void resetEmptySolt() {
        positions.clear();
        for (RoleItem roleItem : equipSlots.values()) {
            positions.put(getChongwuEquipKey(roleItem.getChongwuId(), roleItem.getSlot()), roleItem);
        }
    }
}