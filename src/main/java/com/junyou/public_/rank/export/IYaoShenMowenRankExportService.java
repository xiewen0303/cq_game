package com.junyou.public_.rank.export;

import java.util.List;

import org.springframework.stereotype.Component;

import com.junyou.public_.rank.vo.IYaoShenMowenRankVo;


@Component
public interface IYaoShenMowenRankExportService <T extends IYaoShenMowenRankVo> {
	
	List<T> getYaoShenMowenRankVo(int limit);

}
