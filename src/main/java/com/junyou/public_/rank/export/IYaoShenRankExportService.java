package com.junyou.public_.rank.export;

import java.util.List;

import org.springframework.stereotype.Component;

import com.junyou.public_.rank.vo.IYaoShenRankVo;


@Component
public interface IYaoShenRankExportService <T extends IYaoShenRankVo> {
	
	List<T> getYaoShenRankVo(int limit);

}
