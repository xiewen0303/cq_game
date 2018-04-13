package com.junyou.bus.yaoshen.service.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.yaoshen.service.YaoshenService;
import com.junyou.bus.yaoshen.vo.YaoShenMowenRankVo;
import com.junyou.public_.rank.export.IYaoShenMowenRankExportService;

@Service
public class YaoshenMowenExportService implements IYaoShenMowenRankExportService<YaoShenMowenRankVo>{
	@Autowired
	private YaoshenService yaoshenService;
	
	
	
	
	@Override
	public List<YaoShenMowenRankVo> getYaoShenMowenRankVo(int limit) {
		return yaoshenService.getMowenRankVo(limit);
	}
}
