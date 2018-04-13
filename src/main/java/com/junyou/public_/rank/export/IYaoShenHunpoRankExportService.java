package com.junyou.public_.rank.export;

import java.util.List;

import org.springframework.stereotype.Component;

import com.junyou.public_.rank.vo.IYaoShenHunpoRankVo;


@Component
public interface IYaoShenHunpoRankExportService <T extends IYaoShenHunpoRankVo> {
	
	List<T> getYaoShenHunpoRankVo(int limit);

}
