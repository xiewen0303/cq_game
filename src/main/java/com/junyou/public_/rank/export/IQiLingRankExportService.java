package com.junyou.public_.rank.export;

import java.util.List;

import org.springframework.stereotype.Component;

import com.junyou.public_.rank.vo.IQiLingRankVo;


@Component
public interface IQiLingRankExportService <T extends IQiLingRankVo> {
	
	List<T> getQiLingRankVo(int limit);

}
