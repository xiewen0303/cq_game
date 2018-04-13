package com.junyou.public_.rank.export;

import java.util.List;

import org.springframework.stereotype.Component;

import com.junyou.public_.rank.vo.IZhanJiaRankVo;


@Component
public interface IZhanJiaRankExportService <T extends IZhanJiaRankVo> {
	
	List<T> getXianJianRankVo(int limit);

}
