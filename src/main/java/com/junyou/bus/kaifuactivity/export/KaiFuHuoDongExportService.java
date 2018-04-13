package com.junyou.bus.kaifuactivity.export;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.kaifuactivity.entity.QiriLevelLibao;
import com.junyou.bus.kaifuactivity.entity.ZhanliBipin;
import com.junyou.bus.kaifuactivity.service.RfbChiBangPaiMingService;
import com.junyou.bus.kaifuactivity.service.RfbJingJiPaiMingService;
import com.junyou.bus.kaifuactivity.service.RfbLevelHuoDongService;
import com.junyou.bus.kaifuactivity.service.RfbQiLingPaiMingService;
import com.junyou.bus.kaifuactivity.service.RfbTangBaoService;
import com.junyou.bus.kaifuactivity.service.RfbTangBaoXinWenPaiMingService;
import com.junyou.bus.kaifuactivity.service.RfbWuQiPaiMingService;
import com.junyou.bus.kaifuactivity.service.RfbXianZhuangQiangHuaService;
import com.junyou.bus.kaifuactivity.service.RfbYaoMoPaiMingService;
import com.junyou.bus.kaifuactivity.service.RfbYaoShenHunpoPaiMingService;
import com.junyou.bus.kaifuactivity.service.RfbYaoShenMoYinPaiMingService;
import com.junyou.bus.kaifuactivity.service.RfbYaoShenPaiMingService;
import com.junyou.bus.kaifuactivity.service.RfbYuJianFeiXingService;
import com.junyou.bus.kaifuactivity.service.RfbZhanJiaPaiMingService;
import com.junyou.bus.kaifuactivity.service.RfbZhanLiBiPinService;
import com.junyou.public_.rank.vo.IChiBangRankVo;
import com.junyou.public_.rank.vo.IFightingRankVo;
import com.junyou.public_.rank.vo.IQiLingRankVo;
import com.junyou.public_.rank.vo.IWuqiRankVo;
import com.junyou.public_.rank.vo.IXianJianRankVo;
import com.junyou.public_.rank.vo.IXinwenRankVo;
import com.junyou.public_.rank.vo.IYaoShenHunpoRankVo;
import com.junyou.public_.rank.vo.IYaoShenMowenRankVo;
import com.junyou.public_.rank.vo.IYaoShenMoyinRankVo;
import com.junyou.public_.rank.vo.IYaoShenRankVo;
import com.junyou.public_.rank.vo.IZhanJiaRankVo;
import com.junyou.public_.rank.vo.IZuoqiRankVo;



@Service
public class KaiFuHuoDongExportService {

	@Autowired
	private RfbLevelHuoDongService rfbLevelHuoDongService;
	@Autowired
	private RfbZhanLiBiPinService rfbZhanLiBiPinService;
	@Autowired
	private RfbYuJianFeiXingService rfbYuJianFeiXingService;
	@Autowired
	private RfbChiBangPaiMingService rfbChiBangPaiMingService;
	@Autowired
	private RfbJingJiPaiMingService rfbJingJiPaiMingService;
	@Autowired
	private RfbXianZhuangQiangHuaService rfbXianZhuangQiangHuaService;
	@Autowired
	private RfbTangBaoService rfbTangBaoService;
	@Autowired
	private RfbZhanJiaPaiMingService rfbZhanJiaPaiMingService;
	@Autowired
	private RfbYaoShenPaiMingService rfbYaoShenPaiMingService;
	@Autowired
	private RfbYaoMoPaiMingService rfbYaoMoPaiMingService;
	@Autowired
	private RfbQiLingPaiMingService rfbQiLingPaiMingService;
	@Autowired
	private RfbYaoShenHunpoPaiMingService rfbYaoShenHunpoPaiMingService;
	@Autowired
	private RfbYaoShenMoYinPaiMingService rfbYaoShenMoYinPaiMingService;
	@Autowired
	private RfbTangBaoXinWenPaiMingService rfbTangBaoXinWenPaiMingService;
	@Autowired
	private RfbWuQiPaiMingService rfbWuQiPaiMingService;
	
	
	public List<QiriLevelLibao> initLevel(long userRoleId) {
		return rfbLevelHuoDongService.initAll(userRoleId);
	}
	
	public List<ZhanliBipin> initZhanLiBiPin(long userRoleId){
		return rfbZhanLiBiPinService.initAll(userRoleId);
	}
	
	
	public Object[] getRefbLevelInfo(Long userRoleId, Integer subId){
		return rfbLevelHuoDongService.getRefbInfo(userRoleId, subId);
	} 
	
	public Object[] getRefbLevelLingQuStatus(Integer subId){
		return rfbLevelHuoDongService.getRefbLingQuStatus(subId);
	}
	
	public Object[] getRefbZhanliInfo(Long userRoleId, Integer subId){
		return rfbZhanLiBiPinService.getRefbInfo(userRoleId, subId);
	} 
	
	public Object[] getRefbZhanLiLingQuStatus(Long userRoleId,Integer subId){
		return rfbZhanLiBiPinService.getRefbLingQuStatus(userRoleId, subId);
	}
	
	public Object[] getRefbYuJianFeiXingInfo(Long userRoleId, Integer subId){
		return rfbYuJianFeiXingService.getRefbInfo(userRoleId, subId);
	} 
	
	public Object[] getRefbYuJianLingQuStatus(Long userRoleId,Integer subId){
		return rfbYuJianFeiXingService.getRefbLingQuStatus(userRoleId, subId);
	}
	
	public Object[] getRefbChiBangInfo(Long userRoleId, Integer subId){
		return rfbChiBangPaiMingService.getRefbInfo(userRoleId, subId);
	} 
	
	public Object[] getRefbChiBangLingQuStatus(Long userRoleId,Integer subId){
		return rfbChiBangPaiMingService.getRefbLingQuStatus(userRoleId, subId);
	}
	
	public Object[] getRefbJingJiInfo(Long userRoleId, Integer subId){
		return rfbJingJiPaiMingService.getRefbInfo(userRoleId, subId);
	} 
	
	public Object[] getRefbJingJiLingQuStatus(Long userRoleId,Integer subId){
		return rfbJingJiPaiMingService.getRefbLingQuStatus(userRoleId, subId);
	}
	public Object[] getRefbQiangHuaInfo(Long userRoleId, Integer subId){
		return rfbXianZhuangQiangHuaService.getRefbInfo(userRoleId, subId);
	} 
	
	public Object[] getRefbQiangHuaLingQuStatus(Long userRoleId,Integer subId){
		return rfbXianZhuangQiangHuaService.getRefbLingQuStatus(userRoleId, subId);
	}
	public Object[] getRefbTangBaoInfo(Long userRoleId, Integer subId){
		return rfbTangBaoService.getRefbInfo(userRoleId, subId);
	} 
	
	public Object[] getRefbTangBaoLingQuStatus(Long userRoleId,Integer subId){
		return rfbTangBaoService.getRefbLingQuStatus(userRoleId, subId);
	}
	public Object[] getRefbYaoShenInfo(Long userRoleId, Integer subId){
		return rfbYaoShenPaiMingService.getRefbInfo(userRoleId, subId);
	} 
	
	public Object[] getRefbYaoShenLingQuStatus(Long userRoleId,Integer subId){
		return rfbYaoShenPaiMingService.getRefbLingQuStatus(userRoleId, subId);
	}
	public Object[] getRefbYaoMoInfo(Long userRoleId, Integer subId){
		return rfbYaoMoPaiMingService.getRefbInfo(userRoleId, subId);
	} 
	
	public Object[] getRefbYaoMoLingQuStatus(Long userRoleId,Integer subId){
		return rfbYaoMoPaiMingService.getRefbLingQuStatus(userRoleId, subId);
	}
	public Object[] getRefbZhanJiaInfo(Long userRoleId, Integer subId){
		return rfbZhanJiaPaiMingService.getRefbInfo(userRoleId, subId);
	} 
	
	public Object[] getRefbZhanJiaLingQuStatus(Long userRoleId,Integer subId){
		return rfbZhanJiaPaiMingService.getRefbLingQuStatus(userRoleId, subId);
	}
	
	public Object[] getRefbQiLingInfo(Long userRoleId, Integer subId){
		return rfbQiLingPaiMingService.getRefbInfo(userRoleId, subId);
	} 
	
	public Object[] getRefbQiLingLingQuStatus(Long userRoleId,Integer subId){
		return rfbQiLingPaiMingService.getRefbLingQuStatus(userRoleId, subId);
	}
	public Object[] getRefbTangBaoXinWenInfo(Long userRoleId, Integer subId){
		return rfbTangBaoXinWenPaiMingService.getRefbInfo(userRoleId, subId);
	} 
	
	public Object[] getRefbTangBaoXinWenLingQuStatus(Long userRoleId,Integer subId){
		return rfbTangBaoXinWenPaiMingService.getRefbLingQuStatus(userRoleId, subId);
	}
	public Object[] getRefbYaoShenHunPoInfo(Long userRoleId, Integer subId){
		return rfbYaoShenHunpoPaiMingService.getRefbInfo(userRoleId, subId);
	} 
	
	public Object[] getRefbYaoShenHunPoLingQuStatus(Long userRoleId,Integer subId){
		return rfbYaoShenHunpoPaiMingService.getRefbLingQuStatus(userRoleId, subId);
	}
	public Object[] getRefbYaoShenMoYinInfo(Long userRoleId, Integer subId){
		return rfbYaoShenMoYinPaiMingService.getRefbInfo(userRoleId, subId);
	} 
	
	public Object[] getRefbYaoShenMoYinLingQuStatus(Long userRoleId,Integer subId){
		return rfbYaoShenMoYinPaiMingService.getRefbLingQuStatus(userRoleId, subId);
	}
	
	public void tuisongZhanLi(List<IFightingRankVo> vos){
		rfbZhanLiBiPinService.startZhanLi(vos);
	}
	public void tuisongZhanJia(List<IZhanJiaRankVo> vos){
		rfbZhanJiaPaiMingService.start(vos);
	}
	public void tuisongZuoQi(List<IZuoqiRankVo> vos){
		rfbYuJianFeiXingService.start(vos);
	}
	public void tuisongChiBang(List<IChiBangRankVo> vos){
		rfbChiBangPaiMingService.start(vos);
	}
	public void tuisongTangBao(List<IXianJianRankVo> vos){
		rfbTangBaoService.start(vos);
	}
	public void tuisongYaoShen(List<IYaoShenRankVo> vos){
		rfbYaoShenPaiMingService.start(vos);
	}
	public void tuisongYaoShenMowen(List<IYaoShenMowenRankVo> vos){
		rfbYaoMoPaiMingService.start(vos);
	}
	public void tuisongYaoShenHunpo(List<IYaoShenHunpoRankVo> vos){
		rfbYaoShenHunpoPaiMingService.start(vos);
	}
	public void tuisongYaoShenMoYin(List<IYaoShenMoyinRankVo> vos){
		rfbYaoShenMoYinPaiMingService.start(vos);
	}

	public void tuisongQiLing(List<IQiLingRankVo> vos){
		rfbQiLingPaiMingService.start(vos);
	}
	public void tuisongTangBaoXinWen(List<IXinwenRankVo> vos){
		rfbTangBaoXinWenPaiMingService.start(vos);
	}
	
	public void tuisongJingJi(){
		rfbJingJiPaiMingService.start();
	}
	
	public Object[] getRefbWuQiInfo(Long userRoleId, Integer subId){
		return rfbWuQiPaiMingService.getRefbInfo(userRoleId, subId);
	} 
	public Object[] getRefbWuQiStatus(Long userRoleId,Integer subId){
		return rfbWuQiPaiMingService.getRefbLingQuStatus(userRoleId, subId);
	}
	public void tuisongWuQi(List<IWuqiRankVo> vos){
		rfbWuQiPaiMingService.start(vos);
	}
}
