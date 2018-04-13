package com.junyou.public_.rank.export;

import java.util.List;

import org.springframework.stereotype.Component;

import com.junyou.public_.rank.vo.IZuoqiRankVo;
@Component
public interface IZuoqiRankExportService <T	 extends IZuoqiRankVo> {
	
	List<T> getZuoqiRankVo(int limit);

}
