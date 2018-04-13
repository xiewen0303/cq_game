package com.junyou.public_.rank.export;

import java.util.List;

import org.springframework.stereotype.Component;

import com.junyou.public_.rank.vo.IYaoShenMoyinRankVo;


@Component
public interface IYaoShenMoyinRankExportService <T extends IYaoShenMoyinRankVo> {
	
	List<T> getYaoShenMoyinRankVo(int limit);

}
