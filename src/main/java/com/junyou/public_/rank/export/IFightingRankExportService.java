package com.junyou.public_.rank.export;

import java.util.List;

import org.springframework.stereotype.Component;

import com.junyou.public_.rank.vo.IFightingRankVo;


@Component
public interface IFightingRankExportService <T extends IFightingRankVo> {
	
	List<T> getFightingRankVo(int limit) ;
	
}
