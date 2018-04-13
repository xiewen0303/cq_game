package com.junyou.public_.rank.export;

import java.util.List;

import org.springframework.stereotype.Component;

import com.junyou.public_.rank.vo.IXianJianRankVo;


@Component
public interface IXianJianRankExportService <T extends IXianJianRankVo> {
	
	List<T> getXianJianRankVo(int limit);

}
