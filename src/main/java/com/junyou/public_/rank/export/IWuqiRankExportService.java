package com.junyou.public_.rank.export;

import java.util.List;
import org.springframework.stereotype.Component;
import com.junyou.public_.rank.vo.IWuqiRankVo;

@Component
public interface IWuqiRankExportService <T extends IWuqiRankVo> {
	
	List<T> getWuqiRankVo(int limit);

}
