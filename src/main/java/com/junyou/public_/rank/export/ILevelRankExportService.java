package com.junyou.public_.rank.export;

import java.util.List;

import org.springframework.stereotype.Component;

import com.junyou.public_.rank.vo.ILevelRankVo;


/**给供应模块声明的等级排行需求接口
 * 
 * @author Jon
 * */
@Component
public interface ILevelRankExportService <T extends ILevelRankVo>{
	
	List<T> getLevelRankVo(int limit);
	
}
