package com.junyou.public_.rank.export;

import java.util.List;

import org.springframework.stereotype.Component;

import com.junyou.public_.rank.vo.IChiBangRankVo;


@Component
public interface IChiBangRankExportService <T extends IChiBangRankVo> {
	
	List<T> getChiBangRankVo(int limit);

}
