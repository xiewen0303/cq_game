package com.junyou.bus.bag.manage;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;

import com.junyou.bus.bag.BagContants;
import com.junyou.bus.bag.ContainerType;
import com.junyou.bus.bag.entity.RoleItem;
import com.junyou.log.ChuanQiLog;

/**
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-11-27下午3:47:54
 *@Description: 背包容器类
 */
public class BagItems extends AbstractContainer{
	
	private ReentrantLock lock = new ReentrantLock();
	private static int TRY_LOCK_TIME = 1;
	
	/**
	 * 初始格位号
	 */
	private int beginSlot;
	
	/**
	 * 最后的格位号
	 */
	private int endSlot;
	
	/**
	 * 空格位置,没放物品的
	 */
	private TreeSet<Integer> emptySlots=new TreeSet<Integer>();
	
	/**
	 * 容器
	 */
	private Map<Long,RoleItem> baseSlots = new ConcurrentHashMap<Long, RoleItem>();

	@Autowired
	public RoleItem removeItem(long guid){
		RoleItem roleItem = null;
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			
			roleItem = baseSlots.remove(guid);
			emptySlots.add(roleItem.getSlot()); 
		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		} finally {
			if(lock.isHeldByCurrentThread()){
				lock.unlock();
			}
		}
		
		return roleItem;
	}
	
	public RoleItem getItemByGuid(long guid){
		
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			
			return baseSlots.get(guid);
		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		} finally {
			if(lock.isHeldByCurrentThread()){
				lock.unlock();
			}
		}	
		
		return null;
	}
	
	public void addItem(RoleItem item){
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			
			this.baseSlots.put(item.getId(), item);
			
			if(item.getSlot() == null){
				item.setSlot(getNextNewSlot());
			}
			
			emptySlots.remove(item.getSlot());
		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		} finally {
			if(lock.isHeldByCurrentThread()){
				lock.unlock();
			}
		}
	}
	
	@Override
	public Integer getType() {
		return ContainerType.BAGITEM.getType();
	}

	@Override
	public List<RoleItem> getItems() {
		return new  ArrayList<RoleItem>(baseSlots.values());
	}
	
	/**
	 * 得到下一个新的格位
	 * @return
	 */
	private Integer getNextNewSlot(){
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			
			return emptySlots.first();
		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		} finally {
			if(lock.isHeldByCurrentThread()){
				lock.unlock();
			}
		}
		
		return null;
	}
	
	/**
	 * 提供给扩容使用
	 * @param slot
	 */
	@Override
	public void addSlot(int slot) {
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
		
			this.emptySlots.add(slot);
		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		} finally {
			if(lock.isHeldByCurrentThread()){
				lock.unlock();
			}
		}
	}
	
	/**
	 * 当前拥有的物品数
	 * @return
	 */
	public int getNowCapacity() {
		return baseSlots.size();
	}
	
	/**
	 * 背包是否已满
	 * @return
	 */
	public boolean isFull(){
		if(emptySlots.size()==0){
			return true;
		}
		return false;
	}
	
	/**
	 * 获得当前可用的空格数
	 * @return
	 */
	public int getEmptyCount(){
		return emptySlots.size();
	}
	

	@Override
	public void resetEmptySolt() {
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
		
			emptySlots.clear();
			for(int i=beginSlot;i<=endSlot;i++){
				emptySlots.add(i);
			}
			
			for (RoleItem item: baseSlots.values()) {
				Integer solt=item.getSlot();
				boolean flag=emptySlots.contains(solt);
				if(flag){
					emptySlots.remove(solt);
				}
			}
		
		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		} finally {
			if(lock.isHeldByCurrentThread()){
				lock.unlock();
			}
		}
	}


	@Override
	public void setContainerDesc(Map<Integer, Integer> data) {
		beginSlot= data.get(BagContants.BAG_B_SLOT);
		endSlot=data.get(BagContants.BAG_E_SLOT);
	}
	
	public boolean hasEmptySlot(Integer slot){
		if(slot == null) return true;
		return emptySlots.contains(slot);
	}
	

	public int getBeginSlot() {
		return beginSlot;
	}

	public void setBeginSlot(int beginSlot) {
		this.beginSlot = beginSlot;
	}

	public int getEndSlot() {
		return endSlot;
	}
	@Override
	public void setEndSlot(int endSlot) {
		this.endSlot = endSlot;
	}
}
