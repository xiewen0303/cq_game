package com.junyou.public_.rank.export;

import java.util.List;

import org.springframework.stereotype.Component;

import com.junyou.public_.rank.vo.IXinwenRankVo;


@Component
public interface IXinwenRankExportService <T extends IXinwenRankVo> {
	
	List<T> getXinwenRankVo(int limit);

}
