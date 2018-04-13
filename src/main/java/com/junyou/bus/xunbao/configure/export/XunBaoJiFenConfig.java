package com.junyou.bus.xunbao.configure.export;

import com.kernel.data.dao.AbsVersion;
 
/**
 * 寻宝积分配置表 
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-2-5 下午2:36:14
 */
public class XunBaoJiFenConfig extends AbsVersion {

	private Integer id;

	private String name;

	private String needitem;

	private Integer needxbjifen;

	private String itemid;

	private Integer fenzu;
	
	/**
	 * 获取配置数据
	 * @return
	 */
	public Object[] getConfigData(){
		return new Object[]{
			getId()
			,getItemid()
			,getNeeditem()
			,getNeedxbjifen()
		};
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getNeeditem() {
		return needitem;
	}

	public void setNeeditem(String needitem) {
		this.needitem = needitem;
	}
	public Integer getNeedxbjifen() {
		return needxbjifen;
	}

	public void setNeedxbjifen(Integer needxbjifen) {
		this.needxbjifen = needxbjifen;
	}
	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	public Integer getFenzu() {
		return fenzu;
	}

	public void setFenzu(Integer fenzu) {
		this.fenzu = fenzu;
	}
}
