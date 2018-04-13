package com.junyou.bus.branchtask;
/** 
 *注意：这里不要用格式化代码！  thx;
 *各种活动（任务）对应的类型
 * @author lxn
 *活跃度id + 活动描述
 */
public enum BranchEnum {
    /**  强化装备x次 */
    B1(1), 
    /** 圣剑升阶x次  */
    B2(2),
    /**坐骑升阶x次*/
    B3(3),
	/**翅膀进阶x次*/
    B4(4),
    /**装备升级x次*/
    B5(5),
    /**领取x次活跃奖励*/
    B6(6),
    /** 核心熔铸x次*/
    B7(7),
    /**荣耀之路通关x次*/
    B8(8),
    /**通关异族入侵x关*/
    B9(9),
    /**领取x次恶灵来袭奖励*/
    B10(10),
    /**寻宝x次数*/
    B11(11),
    /**战力突破x层*/
    B12(12),
    /**加入公会*/
    B13(13);
    
    private  int branchTaskType;

	private BranchEnum(int branchTaskType) {
		this.branchTaskType = branchTaskType;
	}

	public int getBranchTaskType() {
		return branchTaskType;
	}

	public static boolean isNumberAdd(BranchEnum acEnum) {
		int type = acEnum.getBranchTaskType();
		return isNumberAdd(type);
	}
	
	public static boolean isNumberCompare(BranchEnum acEnum) {
		int type = acEnum.getBranchTaskType();
		return isNumberCompare(type);
	}
	
	/**
	 * 累计数据
	 * @param type
	 * @return
	 */
	public static boolean isNumberAdd(int type) {
		if( B1.getBranchTaskType() == type ||
			B3.getBranchTaskType() == type ||
			B4.getBranchTaskType() == type ||
			B5.getBranchTaskType() == type ||
			B6.getBranchTaskType() == type ||
			B7.getBranchTaskType() == type ||
			B8.getBranchTaskType() == type ||
			B10.getBranchTaskType() == type ||
			B11.getBranchTaskType() == type ||
			B13.getBranchTaskType() == type ||
			B2.getBranchTaskType() == type
			){
			return true;
		}
		return false;
	}
	
	/**
	 * 超过该数值
	 * @param type
	 * @return
	 */
	public static boolean isNumberCompare(int type) {
		if(B9.getBranchTaskType() == type ||
		  B12.getBranchTaskType() == type
		  ){
			return true;
		}
		return false;
	}
}
