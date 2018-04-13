package com.junyou.gameconfig.publicconfig.configure.export;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.junyou.bus.dati.configure.DaTiPublicConfig;
import com.junyou.bus.fuben.entity.JianzhongFubenConfig;
import com.junyou.bus.kuafu_yungong.constants.KuafuYunGongConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.entity.Tangbao;
import com.junyou.bus.tafang.configure.TaFangPublicConfig;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 * 公共数据创建工厂
 * 
 * @author LiuYu
 * @date 2014-12-9 下午6:32:54
 */
public class GongGongShuJuConfigCreateFactory {

	public static AdapterPublicConfig createPublicConfig(String mId,
			Map<String, Object> tmp) {
		AdapterPublicConfig config = null;

		// 人物基础属性配置
		if (mId.equals(PublicConfigConstants.MOD_ROLE_BASE)) {
			config = createRoleBasePublicConfig(tmp);
		}
		// PK相关配置
		else if (mId.equals(PublicConfigConstants.MOD_PK)) {
			config = createPKPublicConfig(tmp);
		}
		// 技能相关配置
		else if (mId.equals(PublicConfigConstants.MOD_SKILL)) {
			config = createSkillPublicConfig(tmp);
		}
		// 经验玉
		else if (mId.equals(PublicConfigConstants.MOD_JINGYANYU)) {
			config = createJingYanYuPublicConfig(tmp);
		}
		// 组队相关配置
		else if (mId.equals(PublicConfigConstants.MOD_TEAM)) {
			config = createTeamPublicConfig(tmp);
		}
		// 土城高经验区
		else if (mId.equals(PublicConfigConstants.MOD_TUCHEN_EXP)) {
			config = createTuChenExpPublicConfig(tmp);
		}
		// 自动回血回蓝配置
		else if (mId.equals(PublicConfigConstants.MOD_HP_MP)) {
			config = createHpMpPublicConfig(tmp);
		}
		//妖神附魔
		else if (mId.equals(PublicConfigConstants.MOD_YAOSHEN_FUMO)) {
			config = createYaoshenFumoConfig(tmp); 
		}
		// 门派
		else if (mId.equals(PublicConfigConstants.MOD_GUILD)) {
			config = createGuildPublicConfig(tmp);
		}
		// 副本
		else if (mId.equals(PublicConfigConstants.MOD_FUBEN)) {
			config = createFubenPublicConfig(tmp);
		}

		// 好友
		else if (mId.equals(PublicConfigConstants.MOD_FRIEND)) {
			config = createFriendPublicConfig(tmp);
		}
		// 随机属性
		else if (mId.equals(PublicConfigConstants.MOD_RANDOM_ATTRS)) {
			config = createRandomAttrsConfig(tmp);
		}
		// 领地战
		else if (mId.equals(PublicConfigConstants.MOD_TERRITORY_WAR)) {
			config = createTerritoryPublicConfig(tmp);
		}
		// 云宫之战 皇城争霸赛
		else if (mId.equals(PublicConfigConstants.MOD_HCZBS_WAR)) {
			config = createHcZBSPublicConfig(tmp);
		}
		// 野外boss活动
		else if (mId.equals(PublicConfigConstants.MOD_YW_BOSS)) {
			config = createYwBossPublicConfig(tmp);
		}
		// 守护副本
		else if (mId.equals(PublicConfigConstants.MOD_SHOUHU)) {
			config = createShouhuPublicConfig(tmp);
		}
		// 押镖
		else if (mId.equals(PublicConfigConstants.MOD_YABIAO)) {
			config = createYabiaoPublicConfig(tmp);
		} else if (mId.equals(PublicConfigConstants.MOD_DATI)) {
			config = createDatiPublicConfig(tmp);
		}
		// 御剑返利
		else if (mId.equals(PublicConfigConstants.MOD_ZUOQIFANLI)) {
			config = createYuJianFanLiPublicConfig(tmp);
		}
		// 武器返利
		else if (mId.equals(PublicConfigConstants.MOD_WUQIFANLI)) {
			config = createWuQiFanLiPublicConfig(tmp);
		}
		// 糖宝
		else if (mId.equals(PublicConfigConstants.MOD_TANGBAO)) {
			config = createTangbaoPublicConfig(tmp);
		}
		// 寻宝
		else if (mId.equals(PublicConfigConstants.MOD_XUNBAO)) {
			config = createXunBaoPublicConfig(tmp);
		}
		// 怒气
		else if (mId.equals(PublicConfigConstants.MOD_NUQI)) {
			config = createNuqiPublicConfig(tmp);
		}
		// 爬塔
		else if (mId.equals(PublicConfigConstants.MOD_PATA)) {
			config = createPataPublicConfig(tmp);
		}
		// 微端
		else if (mId.equals(PublicConfigConstants.MOD_WEIDUAN)) {
			config = createWeiDuanPublicConfig(tmp);
		}

		// 微端2
		else if (mId.equals(PublicConfigConstants.MOD_DESKSAVA)) {
			config = createDESKSAVAPublicConfig(tmp);
		}
		// 阵营战
		else if (mId.equals(PublicConfigConstants.MOD_CAMP)) {
			config = createCampPublicConfig(tmp);
		}
		// 御剑
		else if (mId.equals(PublicConfigConstants.MOD_YUJIAN)) {
			config = createYuJianPublicConfig(tmp);
		}
		// 翅膀
		else if (mId.equals(PublicConfigConstants.MOD_CHIBANG)) {
			config = createChiBangPublicConfig(tmp);
		}
		// 器灵
		else if (mId.equals(PublicConfigConstants.MOD_QILING)) {
			config = createQiLingPublicConfig(tmp);
		}
		// 器灵
		else if (mId.equals(PublicConfigConstants.MOD_TIANYU)) {
			config = createTianYuPublicConfig(tmp);
		}
		// 通天之路
		else if (mId.equals(PublicConfigConstants.MOD_TONGTIAN)) {
			config = createTongtianPublicConfig(tmp);
		}
		// 仙剑
		else if (mId.equals(PublicConfigConstants.MOD_XIANJIAN)) {
			config = createXianJianPublicConfig(tmp);
		}
		// 战甲
		else if (mId.equals(PublicConfigConstants.MOD_ZHANJIA)) {
			config = createZhanJiaPublicConfig(tmp);
		}
		// 探宝
		else if (mId.equals(PublicConfigConstants.MOD_TANBAO)) {
			config = createTanBaoPublicConfig(tmp);
		}
		// 聊天
		else if (mId.equals(PublicConfigConstants.MOD_CHAT)) {
			config = createChatPublicConfig(tmp);
		}
		// 温泉
		else if (mId.equals(PublicConfigConstants.MOD_WENQUAN)) {
			config = createWenquanPublicConfig(tmp);
		}
		// 妖神
		else if (mId.equals(PublicConfigConstants.MOD_YAOSHEN)) {
			config = createYaoshenPublicConfig(tmp);
		}
		// 妖神魔纹
		else if (mId.equals(PublicConfigConstants.MOD_YAOSHEN_MOWEN)) {
			config = createYaoshenMowenPublicConfig(tmp);
		}
		// 妖神魂魄
		else if (mId.equals(PublicConfigConstants.MOD_YAOSHEN_HUNPO)) {
			config = createYaoshenHunpoPublicConfig(tmp);
		}
		//妖神魔印
		else if(mId.equals(PublicConfigConstants.MOD_YAOSHEN_MOYIN)){
			config = createYaoshenMoYinConfig(tmp);
		}
		//心纹
		else if(mId.equals(PublicConfigConstants.MOD_XINWEN)){
			config = createXinWenConfig(tmp);
		}
		//成神
		else if(mId.equals(PublicConfigConstants.MOD_CHENG_SHEN)){
			config = createChengShenConfig(tmp);
		}
		//宠物
		else if(mId.equals(PublicConfigConstants.MOD_CHONGWU)){
			config = createChongwuPublicConfig(tmp);
		}
		// 称号
		else if (mId.equals(PublicConfigConstants.MOD_CHENGHAO)) {
			config = createChenghaoPublicConfig(tmp);
		}
		// 神器
		else if (mId.equals(PublicConfigConstants.MOD_SHENQI)) {
			config = createShenQiPublicConfig(tmp);
		}
		// 画卷
		else if (mId.equals(PublicConfigConstants.MOD_HUAJUAN)) {
			config = createHuajuanPublicConfig(tmp);
		}
		// 神器洗练
		else if (mId.equals(PublicConfigConstants.MOD_SHENQI_XILIAN)) {
			config = createShenQiXiLianPublicConfig(tmp);
		}
		// 资源
		else if (mId.equals(PublicConfigConstants.MOD_ZIYUAN)) {
			config = createZiYuanPublicConfig(tmp);
		}
		// 结婚
		else if (mId.equals(PublicConfigConstants.MOD_MARRY)) {
			config = createMarryPublicConfig(tmp);
		}
		//套装象位
		else if (mId.equals(PublicConfigConstants.MOD_TAOZHUANG_XIANGWEI)){
			config = createSuitXiangweiConfig(tmp);
		}
		// 改名
		else if (mId.equals(PublicConfigConstants.MOD_MODIFY_NAME)) {
			config = createModifyNameConfig(tmp);
		}
		// 淬炼
		else if (mId.equals(PublicConfigConstants.MOD_CUILIAN)) {
			config = createCuilianPublicConfig(tmp);
		}
		// 混沌战场
		else if (mId.equals(PublicConfigConstants.MOD_HUNDUN)) {
			config = createHundunPublicConfig(tmp);
		}
		// 跨服个人竞技
		else if (mId.equals(PublicConfigConstants.MOD_KUAFU_ARENA_1v1)) {
			config = createKuafuArenaSinglePublicConfig(tmp);
		}
		// 八卦阵
		else if (mId.equals(PublicConfigConstants.MOD_BAGUA)) {
			config = createBaguaPublicConfig(tmp);
		}
		// 埋骨之地
		else if (mId.equals(PublicConfigConstants.MOD_MAIGU)) {
			config = createMaiguPublicConfig(tmp);
		}
		// 神魔战场
		else if (mId.equals(PublicConfigConstants.MOD_SHENMO)) {
			config = createShenMoPublicConfig(tmp);
		}
		// 送花
		else if (mId.equals(PublicConfigConstants.MOD_FLOWER)) {
			config = createFlowerPublicConfig(tmp);
		}
		// 塔防副本
		else if (mId.equals(PublicConfigConstants.MOD_TAFANG)) {
			config = createTaFangPublicConfig(tmp);
		}
		// 转生
		else if (mId.equals(PublicConfigConstants.MOD_ZHUANSHENG)) {
			config = createZhuanshengPublicConfig(tmp);
		}
		// 遗迹boss
		else if (mId.equals(PublicConfigConstants.MOD_YIJIBOSS)) {
			config = createYijibossPublicConfig(tmp);
		}
		// 跨服boss
		else if (mId.equals(PublicConfigConstants.KUAFU_BOSS)) {
			config = createYueNanKuafuBossPublicConfig(tmp);
		}
		// 跨服大乱斗
		else if (mId.equals(PublicConfigConstants.KUAFU_DALUANDOU)) {
			config = createKuafuLuanDouPublicConfig(tmp);
		}
		//群仙宴
		else if(mId.equals(PublicConfigConstants.KUAFU_QUNXIANYAN)){
			config = createKuafuQunXianYanPublicConfig(tmp);
		}
		//五行
		else if(mId.equals(PublicConfigConstants.WUXING)){
			config = createWuXingPublicConfig(tmp);
		}
        // 五行副本
        else if (mId.equals(PublicConfigConstants.MOD_WUXING_FUBEN)) {
            config = createWuXingFubenPublicConfig(tmp);
        }
        // 五行技能副本
        else if (mId.equals(PublicConfigConstants.MOD_WUXING_SKILL_FUBEN)) {
            config = createWuXingSkillFubenPublicConfig(tmp);
        }
	    // 五行魔神精魄
        else if (mId.equals(PublicConfigConstants.MOD_WUXING_JINGPO)) {
            config = createWuXingJingpoPublicConfig(tmp);
        }
		// 五行试炼副本
        else if (mId.equals(PublicConfigConstants.MOD_WUXING_SHILIAN)) {
            config = createWuXingShilianPublicConfig(tmp);
        }
		// 心魔:天炉炼丹
        else if (mId.equals(PublicConfigConstants.MOD_XINMO_LIANDAN)) {
            config = createXinmoLiandanPublicConfig(tmp);
        }
		// 心魔:幻世魔神
        else if (mId.equals(PublicConfigConstants.MOD_XINMO_MOSHEN)) {
            config = createXinmoMoshenPublicConfig(tmp);
        }
		// 心魔:挑战心魔
        else if (mId.equals(PublicConfigConstants.MOD_XINMO_FUBEN)) {
            config = createXinmoFubenPublicConfig(tmp);
        }
		// 心魔:挑战心魔深渊
        else if (mId.equals(PublicConfigConstants.MOD_XINMO_SHENYUAN_FUBEN)) {
            config = createXinmoShenyuanFubenPublicConfig(tmp);
        }
		// 心魔:洗练
        else if (mId.equals(PublicConfigConstants.MOD_XINMO_XILIAN)) {
            config = createXinmoXilianPublicConfig(tmp);
        }
		// 心魔:斗场
        else if (mId.equals(PublicConfigConstants.MOD_XINMO_DOUCHANG_FUBEN)) {
            config = createXinmoDouchangPublicConfig(tmp);
        }
		// 跨服云宫之巅
        else if (mId.equals(PublicConfigConstants.MOD_KUAFU_YUNGONG)) {
            config = createKuafuYunGongPublicConfig(tmp);
        }
		// 魔宫猎焰公共地图场景
        else if (mId.equals(PublicConfigConstants.MOD_MOGONGLIEYAN)) {
            config = createMoGongLieYanPublicConfig(tmp);
        }
		// 云瑶晶脉公共地图场景
        else if (mId.equals(PublicConfigConstants.MOD_YUNYAOJINGMAI)) {
            config = createYunYaoJingMaiPublicConfig(tmp);
        }
		// 剧情坐标传送配置信息
        else if (mId.equals(PublicConfigConstants.JUQINGPOINT)) {
            config = createPlotPointsPublicConfig(tmp);
        }
		// 新圣剑
        else if (mId.equals(PublicConfigConstants.XIN_SHENG_JIAN)) {
            config = createXinShengJianPublicConfig(tmp);
        }
		// 限时礼包
        else if (mId.equals(PublicConfigConstants.XIAN_SHI_LI_BAO)) {
            config = createXianShiLibaoPublicConfig(tmp);
        }
		
		// 邮件
        else if (mId.equals(PublicConfigConstants.MOD_MAIL)) {
            config = createMailPublicConfig(tmp);
        }

		// 活动大厅
		else if (mId.equals(PublicConfigConstants.MOD_ACTIVITIESHALL)) {
			config = createactivitieShallPublicConfig(tmp);
		}


		if(config != null){
			config.setId(mId);
		}

		return config;
	}


    /**
     * 加载五行试炼配置
     * @param tmp
     * @return
     */
    private static AdapterPublicConfig createWuXingShilianPublicConfig(Map<String, Object> tmp) {
        WuXingShilianFubenPublicConfig config = new WuXingShilianFubenPublicConfig();
        config.setMapId(CovertObjectUtil.object2int(tmp.get("mapid")));
        config.setMaxFightNum(CovertObjectUtil.object2int(tmp.get("counts")));
        config.setTimeSeconds(CovertObjectUtil.object2int(tmp.get("time")));
        return config;
    }

    /**
	 * 送花
	 */
	private static FlowerSendPublicConfig createFlowerPublicConfig(Map<String, Object> tmp){
		FlowerSendPublicConfig config  = new FlowerSendPublicConfig();
		config.setOpen(CovertObjectUtil.object2int(tmp.get("open")));
		return config;
	}
	/**
	 * 转生
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createZhuanshengPublicConfig(Map<String, Object> tmp){
		ZhuanshengPublicConfig config = new ZhuanshengPublicConfig();
		config.setOpen(CovertObjectUtil.object2int(tmp.get("open")));
		config.setMinDuihuan(CovertObjectUtil.object2int(tmp.get("zuidi")));
		config.setPerXiuwei(CovertObjectUtil.object2int(tmp.get("duihuan")));
		return config;
	}
	/**
	 * 糖宝心纹
	 */
	private static XinwenPublicConfig createXinWenConfig(Map<String, Object> tmp){
		XinwenPublicConfig config  = new XinwenPublicConfig();
		config.setOpen(CovertObjectUtil.object2int(tmp.get("open")));
		return config;
	}

	/**
	 * 塔防副本
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createTaFangPublicConfig(Map<String, Object> tmp){
		TaFangPublicConfig config = new TaFangPublicConfig();
		config.setMap(CovertObjectUtil.object2int(tmp.get("mapid")));
		config.setCount(CovertObjectUtil.object2int(tmp.get("count")));
		config.setMoneyLevel(CovertObjectUtil.object2int(tmp.get("needlevel")));
		config.setMoneyVip(CovertObjectUtil.object2int(tmp.get("needvip")));
		config.setPerMonsterTime(CovertObjectUtil.object2int(tmp.get("time1")));
		config.setMonstersTime(CovertObjectUtil.object2int(tmp.get("time2")));
		String items = CovertObjectUtil.obj2StrOrNull(tmp.get("giveitem"));
		if(items != null){
			String[] itemInfo = items.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
			if(itemInfo.length > 1){
				Map<String,Integer> map = new HashMap<>();
				map.put(itemInfo[0], CovertObjectUtil.object2int(itemInfo[1]));
				config.setSendGift(map);
			}
		}
		return config;
	}
	
	/**
	 * 神魔战场
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createShenMoPublicConfig(
			Map<String, Object> tmp) {
		ShenMoPublicConfig config = new ShenMoPublicConfig();
		config.setLevel(CovertObjectUtil.object2int(tmp.get("needlevel")));
		config.setPiptime1(CovertObjectUtil.object2int(tmp.get("piptime1")));
		config.setPiptime2(CovertObjectUtil.object2int(tmp.get("piptime2")));
		config.setPiptime3(CovertObjectUtil.object2int(tmp.get("piptime3")));
		config.setPiptime4(CovertObjectUtil.object2int(tmp.get("piptime4")));
		config.setPiptime5(CovertObjectUtil.object2int(tmp.get("piptime5")));
		config.setDaojishi1(CovertObjectUtil.object2int(tmp.get("daojishi1")));
		config.setDaojishi2(CovertObjectUtil.object2int(tmp.get("daojishi2")));
		config.setMianTime(CovertObjectUtil.object2int(tmp.get("miantime")));
		config.setHdTime(CovertObjectUtil.object2int(tmp.get("hdtime")));
		config.setEndTimel(CovertObjectUtil.object2int(tmp.get("endtime")));
		config.setZdfh(CovertObjectUtil.object2int(tmp.get("zdfh")));
		config.setOutCf(CovertObjectUtil.object2int(tmp.get("outcf")));
		config.setWinScore(CovertObjectUtil.object2int(tmp.get("wintj")));
		config.setKillScore(CovertObjectUtil.object2int(tmp.get("killvalue")));
		config.setAssScore(CovertObjectUtil.object2int(tmp.get("zgvalue")));
		config.setMap(CovertObjectUtil.object2int(tmp.get("map")));
		config.setPaiming(CovertObjectUtil.object2int(tmp.get("paiming")));
		config.setPaijifen(CovertObjectUtil.object2int(tmp.get("paijifen")));
		config.setOpenBeginTime(CovertObjectUtil.object2String(tmp.get("time1")));
		config.setOpenEndTime(CovertObjectUtil.object2String(tmp.get("time2")));
		config.setGongji(CovertObjectUtil.object2int(tmp.get("gongji")));
		config.setDayGongxunTimes(CovertObjectUtil.object2int(tmp.get("cishu")));

		String qingjifen = CovertObjectUtil.object2String(tmp.get("qingjifen"));
		String[] qingjifenStrArray = qingjifen.split("_");
		int year = Integer.parseInt(qingjifenStrArray[0]);
		int month = Integer.parseInt(qingjifenStrArray[1]);
		int day = Integer.parseInt(qingjifenStrArray[2]);
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, day, 23, 59, 50);
		config.setQingjifenTime(c.getTimeInMillis());

		List<int[]> jyzb = new ArrayList<>();
		String zbStr = CovertObjectUtil.object2String(tmp.get("jyzb"));
		String[] zbs = zbStr.split(GameConstants.CONFIG_SPLIT_CHAR);
		for (String zbInfo : zbs) {
			String[] zb = zbInfo.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
			if (zb != null && zb.length > 1) {
				jyzb.add(new int[] { CovertObjectUtil.object2int(zb[0]),
						CovertObjectUtil.object2int(zb[1]) });
			}
		}
		config.setJyzb(jyzb);

		return config;
	}

	/**
	 * 混沌战场
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createHundunPublicConfig(
			Map<String, Object> tmp) {
		HundunPublicConfig config = new HundunPublicConfig();
		config.setEnterTime(CovertObjectUtil.object2int(tmp.get("itime")) * 1000);
		config.setMonsterJifen(CovertObjectUtil.object2int(tmp
				.get("monsterjifen")));
		config.setRoleJifen(CovertObjectUtil.object2int(tmp.get("peoplejifen")));
		config.setResetRate(CovertObjectUtil.obj2float(tmp.get("hp1")));
		config.setShengyu(CovertObjectUtil.obj2float(tmp.get("sunshi")));
		return config;
	}

	/**
	 * 结婚
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createMarryPublicConfig(
			Map<String, Object> tmp) {
		MarryPublicConfig config = new MarryPublicConfig();
		config.setAddLoveTime(CovertObjectUtil.object2int(tmp.get("needtime")));
		config.setAddLove(CovertObjectUtil.object2int(tmp.get("qmd")));
		config.setDivorceCd(CovertObjectUtil.object2int(tmp.get("lhcd")));
		config.setReadyDivorceCd(CovertObjectUtil.object2int(tmp.get("xylhcd")));
		config.setCode(CovertObjectUtil.obj2StrOrNull(tmp.get("jhcode")));
		config.setDivorceCost(CovertObjectUtil.object2int(tmp.get("needmoney")));
		return config;
	}
	
	/**
	 * 套装象位
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createSuitXiangweiConfig(
			Map<String, Object> tmp) {
		SuitXiangweiPublicConfig config = new SuitXiangweiPublicConfig();
		config.setNeedLevel(CovertObjectUtil.object2int(tmp.get("level")));
		return config;
	}

	/**
	 * 资源
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createZiYuanPublicConfig(
			Map<String, Object> tmp) {
		ZiYuanPublicConfig config = new ZiYuanPublicConfig();
		config.setMoneyRate(CovertObjectUtil.obj2float(tmp.get("needmoney")));
		config.setGoldRate(CovertObjectUtil.obj2float(tmp.get("needgold")));
		return config;
	}

	/**
	 * 聊天
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createChatPublicConfig(
			Map<String, Object> tmp) {
		ChatPublicConfig config = new ChatPublicConfig();
		config.setId(PublicConfigConstants.MOD_CHAT);
		if(PlatformConstants.isQQ()){
			config.setLevel(CovertObjectUtil.object2int(tmp.get("qqminlevel")));
		}else{
			config.setLevel(CovertObjectUtil.object2int(tmp.get("minlevel")));
			
		}
		return config;
	}

	/**
	 * 探宝
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createTanBaoPublicConfig(
			Map<String, Object> tmp) {
		TanBaoPublicConfig config = new TanBaoPublicConfig();

		config.setAddExpTime(CovertObjectUtil.object2int(tmp.get("time")));
		config.setFuhuoTime(CovertObjectUtil.object2int(tmp.get("time1")));
		config.setMinJf(CovertObjectUtil.object2int(tmp.get("dieminjf")));
		config.setMaxJf(CovertObjectUtil.object2int(tmp.get("diemaxjf")));
		config.setDrop(CovertObjectUtil.obj2float(tmp.get("diejf")));
		config.setEmailCode(CovertObjectUtil.object2String(tmp.get("jlcode")));

		return config;
	}

	/**
	 * 微端奖励
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createWeiDuanPublicConfig(
			Map<String, Object> tmp) {
		WeiDuanPublicConfig config = new WeiDuanPublicConfig();

		String jiangItem = CovertObjectUtil.object2String(tmp.get("jiangitem"));
		config.setJiangitem(ConfigAnalysisUtils.getConfigMap(jiangItem));

		return config;
	}


	/**
	 * 微端奖励2
	 *
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createDESKSAVAPublicConfig(Map<String, Object> tmp) {
		WeiDuanPublicConfig config = new WeiDuanPublicConfig();

		String jiangItem = CovertObjectUtil.object2String(tmp.get("jiangitem"));
		config.setJiangitem(ConfigAnalysisUtils.getConfigMap(jiangItem));

		return config;
	}





	/**
	 * 爬塔
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createPataPublicConfig(
			Map<String, Object> tmp) {
		PataPublicConfig config = new PataPublicConfig();
		config.setFreeTime(CovertObjectUtil.object2int(tmp.get("cishu")));
		config.setMapId(CovertObjectUtil.object2int(tmp.get("mapid")));
		config.setBuyGold(CovertObjectUtil.object2int(tmp.get("gold")));
		String position = CovertObjectUtil.object2String(tmp
				.get("guaiwuzuobiao"));
		String[] xys = position.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		config.setPosition(new Integer[] { Integer.parseInt(xys[0]),
				Integer.parseInt(xys[1]) });
		return config;
	}

	/**
	 * 怒气
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createNuqiPublicConfig(
			Map<String, Object> tmp) {
		NuqiPublicConfig config = new NuqiPublicConfig();
		config.setMaxNq(CovertObjectUtil.object2int(tmp.get("maxsp")));
		config.setTimeAdd(CovertObjectUtil.object2int(tmp.get("time")));
		config.setKillAdd(CovertObjectUtil.object2int(tmp.get("monster")));
		config.setJiange(CovertObjectUtil.object2int(tmp.get("jiange")));
		config.setBoshu(CovertObjectUtil.object2int(tmp.get("boshu")));
		return config;
	}

	/**
	 * 寻宝
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createXunBaoPublicConfig(
			Map<String, Object> tmp) {
		// 寻宝
		XunBaoPublicConfig config = new XunBaoPublicConfig();
		int maxCapacity = CovertObjectUtil.object2int(tmp.get("maxxunbao"));

		config.setMaxCapacity(maxCapacity);
		config.setNeedGold(CovertObjectUtil.object2int(tmp.get("gold")));
		config.setNeedGoodsId(CovertObjectUtil.object2String(tmp
				.get("needitem")));
		config.setXbjifen(CovertObjectUtil.object2int(tmp.get("xbjifen")));
		config.setPageSize(CovertObjectUtil.object2int(tmp.get("maxxunbao")));

		String times1Str = CovertObjectUtil.object2String(tmp.get("count1"));
		String[] times1 = times1Str.split("\\|");
		config.addTypeCount(CovertObjectUtil.object2int(times1[0]),
				CovertObjectUtil.object2int(times1[1]));

		String times2Str = CovertObjectUtil.object2String(tmp.get("count2"));
		String[] times2 = times2Str.split("\\|");
		config.addTypeCount(CovertObjectUtil.object2int(times2[0]),
				CovertObjectUtil.object2int(times2[1]));

		String times3Str = CovertObjectUtil.object2String(tmp.get("count3"));
		String[] times3 = times3Str.split("\\|");
		config.addTypeCount(CovertObjectUtil.object2int(times3[0]),
				CovertObjectUtil.object2int(times3[1]));

		return config;
	}

	// 糖宝杂项
	private static AdapterPublicConfig createTangbaoPublicConfig(
			Map<String, Object> tmp) {

		TangbaoPublicConfig config = new TangbaoPublicConfig();
		config.setName(CovertObjectUtil.object2String(tmp.get("name")));
		config.setZzNeedLevel(CovertObjectUtil.object2int(tmp.get("needlevel")));
		config.setZzMax(CovertObjectUtil.object2int(tmp.get("maxzizhi")));
		config.setCzNeedLevel(CovertObjectUtil.object2int(tmp.get("needlevel1")));
		config.setCzMax(CovertObjectUtil.object2int(tmp.get("maxchengzh")));
		config.setFtNeedZz(CovertObjectUtil.object2int(tmp.get("needzizhi")));
		config.setSkill(CovertObjectUtil.obj2StrOrNull(tmp.get("skill")));
		config.setRes(CovertObjectUtil.obj2StrOrNull(tmp.get("res")));
		config.setHeartTime(CovertObjectUtil.object2int(tmp.get("heart")));
		config.setNeedLevel(CovertObjectUtil.object2int(tmp.get("open")));
		List<Integer> list = new ArrayList<>();
		for (int i = 1; i < 7; i++) {
			list.add(CovertObjectUtil.object2int(tmp.get("level" + i)));
		}
		config.setSkillNeedLevel(list);

		Map<String, Long> attributeMap = ConfigAnalysisUtils
				.setAttributeVal(tmp);
		config.setEatAttMax(attributeMap);

		int maxcount = CovertObjectUtil.object2int(tmp.get("maxcount"));
		if (maxcount < 1) {
			maxcount = Integer.MAX_VALUE;
		}
		Tangbao.setMaxCount(maxcount);

		return config;
	}

	// 随机属性
	private static AdapterPublicConfig createRandomAttrsConfig(
			Map<String, Object> tmp) {

		RandomAttrPublicConfig config = new RandomAttrPublicConfig();

		Map<Integer, Integer> randomCounts = new HashMap<>();
		for (Entry<String, Object> entry : tmp.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			randomCounts.put(CovertObjectUtil.object2int(key),
					CovertObjectUtil.object2int(value));
		}
		config.setData(new ReadOnlyMap<>(randomCounts));

		return config;
	}

	private static float max(float val, float compareVal) {
		return val < compareVal ? compareVal : val;
	}

	private static AdapterPublicConfig createFriendPublicConfig(
			Map<String, Object> tmp) {
		FriendPublicConfig config = new FriendPublicConfig();

		config.setMaxBlack(CovertObjectUtil.object2int(tmp.get("hei")));
		config.setMaxEnemy(CovertObjectUtil.object2int(tmp.get("zui")));
		config.setMaxFirend(CovertObjectUtil.object2int(tmp.get("friendmax")));

		return config;
	}

	private static DaTiPublicConfig createDatiPublicConfig(
			Map<String, Object> tmp) {
		DaTiPublicConfig config = new DaTiPublicConfig();
		config.setTiSum(CovertObjectUtil.object2int(tmp.get("data1")));
		config.setEachSumTime(CovertObjectUtil.object2int(tmp.get("data2")));
		config.setEachAnswerTime(CovertObjectUtil.object2int(tmp.get("data3")));
		config.setInitDoublePointCardNum(CovertObjectUtil.object2int(tmp
				.get("shuangbei")));
		config.setInitExcludeCard(CovertObjectUtil.object2int(tmp.get("paichu")));
		config.setShowRankLimit(CovertObjectUtil.object2int(tmp.get("mingci")));
		config.setSettlementLimit(CovertObjectUtil.object2int(tmp.get("paim")));
		return config;
	}

	private static YuJianFanLiPublicConfig createYuJianFanLiPublicConfig(
			Map<String, Object> tmp) {
		YuJianFanLiPublicConfig config = new YuJianFanLiPublicConfig();

		config.setKfTime(CovertObjectUtil.object2int(tmp.get("fltime")));
		config.setFlid(CovertObjectUtil.object2int(tmp.get("flid")));
		config.setFlitem(CovertObjectUtil.object2String(tmp.get("flitem")));

		return config;
	}
	
	private static WuQiFanLiPublicConfig createWuQiFanLiPublicConfig(Map<String, Object> tmp) {
		WuQiFanLiPublicConfig config = new WuQiFanLiPublicConfig();
		
		config.setKfTime(CovertObjectUtil.object2int(tmp.get("fltime")));
		config.setFlid(CovertObjectUtil.object2int(tmp.get("flid")));
		config.setFlitem(CovertObjectUtil.object2String(tmp.get("flitem")));

		return config;
	}

	/**
	 * 神器相关
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createShenQiPublicConfig(
			Map<String, Object> tmp) {
		ShenQiPublicConfig config = new ShenQiPublicConfig();

		config.setSqId(CovertObjectUtil.object2int(tmp.get("item")));
		config.setGold(CovertObjectUtil.object2int(tmp.get("gold")));
		config.setDay(CovertObjectUtil.object2int(tmp.get("day")));
		config.setDelayDay(CovertObjectUtil.object2int(tmp.get("serveropenday")));
		return config;
	}
	/**
	 * 画卷
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createHuajuanPublicConfig(
			Map<String, Object> tmp) {
		HuajuanPublicConfig config = new HuajuanPublicConfig();

		config.setNum(CovertObjectUtil.object2int(tmp.get("num")));
		return config;
	}

	/**
	 * 神器洗练
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createShenQiXiLianPublicConfig(
			Map<String, Object> tmp) {
		ShenQiXiLianPublicConfig config = new ShenQiXiLianPublicConfig();
		config.setNeedvip1(CovertObjectUtil.object2String(tmp.get("needvip1")));
		config.setNeedgold(CovertObjectUtil.object2int(tmp.get("needgold")));
		String needfu = CovertObjectUtil.object2String(tmp.get("needfu"));
		String needshi = CovertObjectUtil.object2String(tmp.get("needshi"));
		config.setNeedfu(needfu.split(":"));
		config.setNeedshi(needshi.split(":"));
		config.setJiage(CovertObjectUtil.object2int(tmp.get("jiage")));
		config.setNeedmoney(CovertObjectUtil.object2int(tmp.get("needmoney")));

		return config;
	}

	/**
	 * cuilian
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createCuilianPublicConfig(
			Map<String, Object> tmp) {
		CuilianPublicConfig config = new CuilianPublicConfig();

		config.setMoneyExp(CovertObjectUtil.object2int(tmp.get("needmoney")));
		config.setBgoldZhenqi(CovertObjectUtil.object2int(tmp.get("needbgold")));
		config.setGoldExp(CovertObjectUtil.object2int(tmp.get("goldexp")));
		config.setGoldZhenqi(CovertObjectUtil.object2int(tmp.get("goldzhenqi")));
		config.setGoldMoney(CovertObjectUtil.object2int(tmp.get("goldmoney")));
		return config;
	}

	/**
	 * 改名相关
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createModifyNameConfig(
			Map<String, Object> tmp) {
		ModifyNamePublicConfig config = new ModifyNamePublicConfig();
		config.setGoodsId(CovertObjectUtil.object2String(tmp.get("id")));
		config.setCdTime(CovertObjectUtil.object2int(tmp.get("cdtime")) * 60 * 60 * 1000L);
		return config;
	}

	/**
	 * 守护副本
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createShouhuPublicConfig(
			Map<String, Object> tmp) {
		ShouhuPublicConfig config = new ShouhuPublicConfig();

		config.setShouhuId(CovertObjectUtil.object2String(tmp.get("shouhunpc")));
		String npcZb = CovertObjectUtil.obj2StrOrNull(tmp.get("shzuobiao"));
		if (!ObjectUtil.strIsEmpty(npcZb)) {
			String[] zb = npcZb.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
			config.setShouhuPoint(new Integer[] { Integer.parseInt(zb[0]),
					Integer.parseInt(zb[1]) });
		}
		String gwZb = CovertObjectUtil.obj2StrOrNull(tmp.get("gwzuobiao"));
		if (!ObjectUtil.strIsEmpty(gwZb)) {
			String[] zb = gwZb.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
			config.setMonsterPoint(new Integer[] { Integer.parseInt(zb[0]),
					Integer.parseInt(zb[1]) });
		}
		config.setMapId(CovertObjectUtil.object2int(tmp.get("mapid")));
		config.setNeedGold(CovertObjectUtil.object2int(tmp.get("gold")));

		return config;
	}

	/**
	 * 创建boss
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createYwBossPublicConfig(
			Map<String, Object> tmp) {
		YWBossPublicConfig config = new YWBossPublicConfig();
		List<String> lists = new ArrayList<>();
		lists.add(CovertObjectUtil.object2String(tmp.get("jiangitem1")));
		lists.add(CovertObjectUtil.object2String(tmp.get("jiangitem2")));
		lists.add(CovertObjectUtil.object2String(tmp.get("jiangitem3")));
		lists.add(CovertObjectUtil.object2String(tmp.get("jiangitem4")));

		config.setRankAwards(lists);

		return config;
	}

	private static AdapterPublicConfig createTerritoryPublicConfig(
			Map<String, Object> tmp) {
		TerritoryPublicConfig config = new TerritoryPublicConfig();

		config.setNeedGold(CovertObjectUtil.object2int(tmp.get("needgong")));
		config.setWinBg(CovertObjectUtil.object2int(tmp.get("banggong")));
		config.setWinExp(CovertObjectUtil.object2int(tmp.get("exp")));
		config.setLoseBg(CovertObjectUtil.object2int(tmp.get("banggong1")));
		config.setLoseExp(CovertObjectUtil.object2int(tmp.get("exp1")));
		config.setBuff(CovertObjectUtil.object2String(tmp.get("buff")));
		config.setNeedTime(CovertObjectUtil.object2int(tmp.get("needtime")));
		config.setDelay(CovertObjectUtil.object2int(tmp.get("delay")));
		config.setTime2(CovertObjectUtil.object2int(tmp.get("time2")));
		config.setJingyan2(CovertObjectUtil.object2int(tmp.get("jingyan2")));
		config.setZhenqi2(CovertObjectUtil.object2int(tmp.get("zhenqi2")));
		config.setKqbuff(CovertObjectUtil.object2String(tmp.get("kqbuff")));
		config.setZiyuanid(CovertObjectUtil.object2int(tmp.get("ziyuanid")));
		config.setLongt(CovertObjectUtil.object2String(tmp.get("longt")));
		config.setJigong(CovertObjectUtil.object2int(tmp.get("jigong")));
		config.setJigongshu(CovertObjectUtil.object2int(tmp.get("jigongshu")));
		config.setShanghai(CovertObjectUtil.object2int(tmp.get("shanghai")));
		return config;
	}

	private static AdapterPublicConfig createHcZBSPublicConfig(
			Map<String, Object> tmp) {
		HcZBSPublicConfig config = new HcZBSPublicConfig();

		config.setNeedGold(CovertObjectUtil.object2int(tmp.get("needgong")));
		config.setWinBg(CovertObjectUtil.object2int(tmp.get("banggong")));
		config.setWinExp(CovertObjectUtil.object2int(tmp.get("exp")));
		config.setLoseBg(CovertObjectUtil.object2int(tmp.get("banggong1")));
		config.setLoseExp(CovertObjectUtil.object2int(tmp.get("exp1")));
		config.setBuff(CovertObjectUtil.object2String(tmp.get("buff")));
		config.setNeedTime(CovertObjectUtil.object2int(tmp.get("needtime")));
		config.setDelay(CovertObjectUtil.object2int(tmp.get("delay")));
		config.setTime2(CovertObjectUtil.object2int(tmp.get("time2")));
		config.setJingyan2(CovertObjectUtil.object2int(tmp.get("jingyan2")));
		config.setZhenqi2(CovertObjectUtil.object2int(tmp.get("zhenqi2")));
		config.setKqbuff(CovertObjectUtil.object2String(tmp.get("kqbuff")));
		config.setZiyuanid(CovertObjectUtil.object2int(tmp.get("ziyuanid")));
		config.setLongt(CovertObjectUtil.object2String(tmp.get("longt")));
		config.setShanghai(CovertObjectUtil.object2int(tmp.get("shanghai")));
		return config;
	}

	/**
	 * 人物基础属性相关配置
	 * 
	 * @param tmp
	 * @return
	 */
	private static RoleBasePublicConfig createRoleBasePublicConfig(
			Map<String, Object> tmp) {
		RoleBasePublicConfig config = new RoleBasePublicConfig();

		config.setSpeed(CovertObjectUtil.object2int(tmp.get("speed")));// 人物基本速度
		// config.setQuanshenqianghuaNum(CovertObjectUtil.object2int(tmp.get("value2")));//激活全身强化所需装备数量
		// config.setWeiDuanBuff(CovertObjectUtil.object2String(tmp.get("value3")));//微端BUFF
		config.setMabiBuff(CovertObjectUtil.object2String(tmp.get("mabibuff")));//麻痹BUFF
		config.setProtectLevel(CovertObjectUtil.object2int(tmp.get("baohulv")));// 新手保护的等级
		config.setZiqibuff(CovertObjectUtil.object2String(tmp.get("ziqibuff")));// 防御buff

		Float bl = CovertObjectUtil.object2Float(tmp.get("fuhuohp"));
		config.setPtHpBv(max(bl, 0.05f));// 普通复活后保留血量百分比
		// bl = CovertObjectUtil.object2Float(tmp.get("value10"));
		// config.setTsHpBv(max(bl, 0.05f));//特殊复活后保留血量百分比

		return config;
	}

	/**
	 * PK相关配置
	 * 
	 * @param tmp
	 * @return
	 */
	private static PKPublicConfig createPKPublicConfig(Map<String, Object> tmp) {
		PKPublicConfig config = new PKPublicConfig();

		int needRed = CovertObjectUtil.object2int(tmp.get("needred"));
		if (needRed < 1) {
			needRed = 1;
		}
		config.setNeedRed(needRed);

		config.setNeedYellow(CovertObjectUtil.object2int(tmp.get("needyellow")));

		config.setPkChiXu(CovertObjectUtil.object2int(tmp.get("pkchixu")));

		config.setPkCleanTime(CovertObjectUtil.object2int(tmp.get("pkxiaochu")));

		config.setPkVal(CovertObjectUtil.object2int(tmp.get("pkzhi")));
		int maxPkValue = CovertObjectUtil.object2int(tmp.get("pkmax"));
		if (needRed > maxPkValue) {
			// 异常处理PK最在值,必须大于红名值
			maxPkValue = config.getNeedRed() + 1;
		}
		config.setPkMaxVal(maxPkValue);

		return config;
	}

	/**
	 * 技能相关配置
	 * 
	 * @param tmp
	 * @return
	 */
	private static SkillPublicConfig createSkillPublicConfig(
			Map<String, Object> tmp) {
		SkillPublicConfig config = new SkillPublicConfig();

		config.setLevel(CovertObjectUtil.object2int(tmp.get("value1")));// 人物基本速度

		return config;
	}

	/**
	 * 经验玉
	 * 
	 * @param map
	 * @return
	 */
	private static JingYanYuPublicConfig createJingYanYuPublicConfig(
			Map<String, Object> map) {
		JingYanYuPublicConfig config = new JingYanYuPublicConfig();

		config.setBeishu(CovertObjectUtil.obj2float(map.get("value2")));
		config.setNeedlevel(CovertObjectUtil.object2int(map.get("value1")));

		return config;
	}

	/**
	 * Team相关配置
	 * 
	 * @param tmp
	 * @return
	 */
	private static TeamPublicConfig createTeamPublicConfig(
			Map<String, Object> tmp) {
		TeamPublicConfig config = new TeamPublicConfig();

		config.setMaxTeamCount(CovertObjectUtil.object2int(tmp.get("value1")));

		// 队伍2人时的经验加成
		Float value = CovertObjectUtil.object2Float(tmp.get("value2"));
		config.setTeamXs(2, value);
		// 队伍3人时的经验加成
		value = CovertObjectUtil.object2Float(tmp.get("value3"));
		config.setTeamXs(3, value);
		// 队伍4人时的经验加成
		value = CovertObjectUtil.object2Float(tmp.get("value4"));
		config.setTeamXs(4, value);
		// 队伍5人时的经验加成
		value = CovertObjectUtil.object2Float(tmp.get("value5"));
		config.setTeamXs(5, value);
		// 队伍6人时的经验加成
		value = CovertObjectUtil.object2Float(tmp.get("value6"));
		config.setTeamXs(6, value);
		// 队伍7人时的经验加成
		value = CovertObjectUtil.object2Float(tmp.get("value7"));
		config.setTeamXs(7, value);
		// 队伍8人时的经验加成
		value = CovertObjectUtil.object2Float(tmp.get("value8"));
		config.setTeamXs(8, value);

		return config;
	}

	/**
	 * 土城高经验
	 * 
	 * @param map
	 * @return
	 */
	public static TuChenExpPublicConfig createTuChenExpPublicConfig(
			Map<String, Object> map) {
		TuChenExpPublicConfig config = new TuChenExpPublicConfig();

		config.setOpen(CovertObjectUtil.object2boolean(map.get("value1")));
		String pointStr = CovertObjectUtil.object2String(map.get("value2"));
		if (!CovertObjectUtil.isEmpty(pointStr)) {
			String[] pArr = pointStr.split("\\|");
			Map<String, String> points = new HashMap<String, String>();
			for (String p : pArr) {
				String[] tmp = p.split(":");
				points.put(tmp[0] + "-" + tmp[1], null);
			}

			config.setPoints(points);
		}

		config.setMapId(CovertObjectUtil.object2String(map.get("value3")));
		config.setOpenDay(CovertObjectUtil.object2int(map.get("value4")));
		int addExpTime = CovertObjectUtil.object2int(map.get("value5"));
		config.setAddExpTime(addExpTime <= 0 ? 1000 : addExpTime);

		String startStr = CovertObjectUtil.object2String(map.get("value6"));
		if (!CovertObjectUtil.isEmpty(startStr)) {
			String[] sArr = startStr.split(":");
			config.setStartHour(Integer.parseInt(sArr[0]));
			config.setStartMinute(Integer.parseInt(sArr[1]));
		}

		String endStr = CovertObjectUtil.object2String(map.get("value7"));
		if (!CovertObjectUtil.isEmpty(endStr)) {
			String[] eArr = endStr.split(":");
			config.setEndHour(Integer.parseInt(eArr[0]));
			config.setEndMinute(Integer.parseInt(eArr[1]));
		}

		int sumXs = CovertObjectUtil.object2int(map.get("value8"));
		config.setSumExpXs(sumXs);
		float hXs = CovertObjectUtil.object2Float(map.get("value9"));
		config.sethXs(hXs);
		int wXs = CovertObjectUtil.object2int(map.get("value10"));
		config.setwXs(wXs);
		int minLevel = CovertObjectUtil.object2int(map.get("value11"));
		config.setMinLevel(minLevel < 1 ? 1 : minLevel);

		return config;
	}

	/**
	 * 回蓝回血
	 * 
	 * @param tmp
	 * @return
	 */
	private static HpMpPublicConfig createHpMpPublicConfig(
			Map<String, Object> tmp) {
		HpMpPublicConfig hpMpPublicConfig = new HpMpPublicConfig();

		hpMpPublicConfig.setHpValue(CovertObjectUtil.object2int(tmp
				.get("value1")));
		hpMpPublicConfig.setHpTimeValue(CovertObjectUtil.object2int(tmp
				.get("value2")));
		hpMpPublicConfig.setMpValue(CovertObjectUtil.object2int(tmp
				.get("value3")));
		hpMpPublicConfig.setMpTimeValue(CovertObjectUtil.object2int(tmp
				.get("value4")));
		hpMpPublicConfig.setZyTimeValue(CovertObjectUtil.object2int(tmp
				.get("value5")));

		return hpMpPublicConfig;
	}

	/**
	 * 门派
	 * 
	 * @param map
	 * @return
	 */
	private static GuildPublicConfig createGuildPublicConfig(
			Map<String, Object> map) {
		GuildPublicConfig config = new GuildPublicConfig();

		config.setVipLevel(CovertObjectUtil.object2int(map.get("vip")));
		config.setLevel(CovertObjectUtil.object2int(map.get("level")));
		config.setDay(CovertObjectUtil.object2int(map.get("day")));
		config.setMoneyRate(CovertObjectUtil.object2int(map.get("money")));
		config.setMoneyGong(CovertObjectUtil.object2int(map.get("mengong")));
		config.setMoneyGuildGet(CovertObjectUtil.object2int(map.get("money1")));
		config.setItemGong(CovertObjectUtil.object2int(map.get("mengong1")));
		config.setbGoldGong(CovertObjectUtil.object2int(map.get("bgoldmen")));
		config.setbGoldGuildGet(CovertObjectUtil.object2int(map
				.get("juanbgold")));
		config.setGoldGong(CovertObjectUtil.object2int(map.get("goldmen")));
		config.setGoldGuildGet(CovertObjectUtil.object2int(map.get("juangold")));
		config.setImpeachItemId(CovertObjectUtil.object2String(map
				.get("thitem")));
		config.setLeaderNoLoginDay(CovertObjectUtil.object2int(map
				.get("thtime")));
		
		
		config.setLianyuBossAttrReduce(CovertObjectUtil.object2int(map.get("bossjsxone")));
		String lianyuBirthStr = CovertObjectUtil.object2String(map.get("guaiwuzuobiao"));
		String[] lianyuBirth = lianyuBirthStr.split(GameConstants.CONFIG_SPLIT_CHAR);
		config.setLianyuBossBirth(new Integer[]{Integer.parseInt(lianyuBirth[0]),Integer.parseInt(lianyuBirth[1])});
		config.setLianyuBossOpen(CovertObjectUtil.object2int(map.get("bossopen")));
		config.setLianyuBossMapId(CovertObjectUtil.object2int(map.get("bossmapid")));
		
		return config;
	}

	/**
	 * 副本
	 * 
	 * @param map
	 * @return
	 */
	private static FubenPublicConfig createFubenPublicConfig(
			Map<String, Object> map) {
		FubenPublicConfig config = new FubenPublicConfig();

		config.setNeedVip(CovertObjectUtil.object2int(map.get("needvip")));
		config.setMiaoVip(CovertObjectUtil.object2int(map.get("quickvip")));

		return config;
	}

	/**
	 * 押镖
	 * 
	 * @param tmp
	 * @return
	 */
	private static YabiaoPublicConfig createYabiaoPublicConfig(
			Map<String, Object> tmp) {
		YabiaoPublicConfig config = new YabiaoPublicConfig();

		config.setMaxYbTime(CovertObjectUtil.object2int(tmp.get("yabiaocount")));
		config.setMaxJbTime(CovertObjectUtil.object2int(tmp.get("jiebiaocount")));
		config.setMaxTime(CovertObjectUtil.object2int(tmp.get("maxtime")));
		config.setMaxCell(CovertObjectUtil.object2int(tmp.get("maxjuli")));
		config.setNeedLevel(CovertObjectUtil.object2int(tmp.get("level")));
		config.setJbExp(CovertObjectUtil.obj2float(tmp.get("jbexp")));
		config.setNeedMoney(CovertObjectUtil.object2int(tmp.get("needmoney")));
		config.setNeedId(CovertObjectUtil.object2String(tmp.get("needid")));
		config.setDefaultBiaoCheId(CovertObjectUtil.object2int(tmp
				.get("biaoche")));
		config.setNeedgold(CovertObjectUtil.object2int(tmp.get("needgold")));
		config.setNeedgold1(CovertObjectUtil.object2int(tmp.get("needgold1")));
		config.setNeedbgold(CovertObjectUtil.object2int(tmp.get("needbgold")));

		config.setShoeId(CovertObjectUtil.object2int(tmp.get("feixie")));
		config.setFhnpc(CovertObjectUtil.object2String(tmp.get("fhnpc")));

		config.setMaxcishu(CovertObjectUtil.object2int(tmp.get("maxcishu")));
		config.setCharnewspeed(CovertObjectUtil.object2int(tmp.get("charnewspeed")));
		return config;
	}

	/**
	 * 御剑
	 * 
	 * @param tmp
	 * @return
	 */
	private static YuJianPublicConfig createYuJianPublicConfig(
			Map<String, Object> tmp) {
		YuJianPublicConfig config = new YuJianPublicConfig();
		config.setOpen(CovertObjectUtil.object2int(tmp.get("open")));
		return config;
	}

	/**
	 * 翅膀
	 * 
	 * @param tmp
	 * @return
	 */
	private static ChiBangPublicConfig createChiBangPublicConfig(
			Map<String, Object> tmp) {
		ChiBangPublicConfig config = new ChiBangPublicConfig();
		config.setOpen(CovertObjectUtil.object2int(tmp.get("open")));
		return config;
	}

	/**
	 * 器灵
	 * 
	 * @param tmp
	 * @return
	 */
	private static QiLingPublicConfig createQiLingPublicConfig(
			Map<String, Object> tmp) {
		QiLingPublicConfig config = new QiLingPublicConfig();
		config.setOpen(CovertObjectUtil.object2int(tmp.get("open")));
		return config;
	}
	/**
	 * 天羽
	 * 
	 * @param tmp
	 * @return
	 */
	private static TianYuPublicConfig createTianYuPublicConfig(
			Map<String, Object> tmp) {
		TianYuPublicConfig config = new TianYuPublicConfig();
		config.setOpen(CovertObjectUtil.object2int(tmp.get("open")));
		return config;
	}
	/**
	 * 通天
	 * @param tmp
	 * @return
	 */
	private static TongtianPublicConfig createTongtianPublicConfig(
			Map<String, Object> tmp) {
		TongtianPublicConfig config = new TongtianPublicConfig();
		config.setOpen(CovertObjectUtil.object2int(tmp.get("open")));
		return config;
	}

	
	
	/**
	 * 仙剑
	 * 
	 * @param tmp
	 * @return
	 */
	private static XianJianPublicConfig createXianJianPublicConfig(
			Map<String, Object> tmp) {
		XianJianPublicConfig config = new XianJianPublicConfig();
		config.setOpen(CovertObjectUtil.object2int(tmp.get("open")));
		return config;
	}

	/**
	 * 战甲
	 * 
	 * @param tmp
	 * @return
	 */
	private static ZhanJiaPublicConfig createZhanJiaPublicConfig(
			Map<String, Object> tmp) {
		ZhanJiaPublicConfig config = new ZhanJiaPublicConfig();
		config.setOpen(CovertObjectUtil.object2int(tmp.get("open")));
		return config;
	}

	// /**
	// * 容器格位
	// * @param tmp
	// * @return
	// */
	// private static ContainerSlotConfig
	// createContainerSlotPublicConfig(Map<String, Object> tmp){
	// ContainerSlotConfig containerSlotConfig = new ContainerSlotConfig();
	// int bagStartSlot=CovertObjectUtil.object2int(tmp.get("startbeibao"));
	// int bagEndSlot=CovertObjectUtil.object2int(tmp.get("endbeibao"));
	// int bagCapacity=bagEndSlot-bagStartSlot+1;
	// containerSlotConfig.setBagStartSlot(bagStartSlot);
	// containerSlotConfig.setBagEndSlot(bagEndSlot);
	// containerSlotConfig.setBagCapacity(bagCapacity);
	//
	// int storageStartSlot=CovertObjectUtil.object2int(tmp.get("startcangku"));
	// int storageEndSlot=CovertObjectUtil.object2int(tmp.get("endcangku"));
	// int storageCapacity=bagEndSlot-bagStartSlot+1;
	// containerSlotConfig.setStorageCapacity(storageCapacity);
	// containerSlotConfig.setStorageEndSlot(storageEndSlot);
	// containerSlotConfig.setStorageStartSlot(storageStartSlot);
	//
	// return containerSlotConfig;
	// }
	/**
	 * 阵营战
	 * 
	 * @param tmp
	 * @return
	 */
	public static CampPublicConfig createCampPublicConfig(
			Map<String, Object> tmp) {
		CampPublicConfig config = new CampPublicConfig();

		config.setId(PublicConfigConstants.MOD_CAMP);
		config.setTime(CovertObjectUtil.object2int(tmp.get("time")));

		int[] campIndex = new int[2];
		Map<Integer, int[]> map = new HashMap<>();
		for (int i = 1; i < 3; i++) {
			Integer camp = CovertObjectUtil.object2int(tmp.get("id" + i));
			campIndex[i - 1] = camp;

			// 坐标
			String pointStr = CovertObjectUtil.object2String(tmp.get("zhenying"
					+ i));
			if (!CovertObjectUtil.isEmpty(pointStr)) {
				String[] point = pointStr
						.split(GameConstants.GOODS_CONFIG_SUB_SPLIT_CHAR);
				map.put(camp,
						new int[] { CovertObjectUtil.object2int(point[0]),
								CovertObjectUtil.object2int(point[1]) });
			}

		}
		if (map != null && map.size() >= 0) {
			config.setPoints(new ReadOnlyMap<>(map));
		}
		config.setCamp(campIndex);
		config.setJifen1(CovertObjectUtil.object2int(tmp.get("jifen1")));
		config.setJifen2(CovertObjectUtil.object2int(tmp.get("jifen2")));
		config.setJifen3(CovertObjectUtil.object2int(tmp.get("jifen3")));
		config.setHarm(CovertObjectUtil.object2int(tmp.get("shanghai")));
		config.setPaiming(CovertObjectUtil.object2int(tmp.get("paiming")));
		return config;
	}

	public static WenquanPublicConfig createWenquanPublicConfig(
			Map<String, Object> tmp) {
		WenquanPublicConfig config = new WenquanPublicConfig();
		config.setNeedgold(CovertObjectUtil.object2int(tmp.get("needgold")));
		config.setZhenqi(CovertObjectUtil.object2int(tmp.get("zhenqi")));
		config.setJingyan(CovertObjectUtil.object2int(tmp.get("jingyan")));
		config.setCishu(CovertObjectUtil.object2int(tmp.get("cishu")));
		config.setZhenqi1(CovertObjectUtil.object2int(tmp.get("zhenqi1")));
		config.setJingyan1(CovertObjectUtil.object2int(tmp.get("jingyan1")));
		config.setNeedgold1(CovertObjectUtil.object2int(tmp.get("needgold1")));
		config.setBeishu(CovertObjectUtil.object2int(tmp.get("beishu")));
		config.setBeishu1(CovertObjectUtil.object2int(tmp.get("beishu1")));
		config.setPaim(CovertObjectUtil.object2int(tmp.get("paim")));
		String flag = CovertObjectUtil.object2String(tmp.get("zuobiao"));
		String[] zuobiao = flag.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		config.setZuobiao(new int[] { Integer.parseInt(zuobiao[0]),
				Integer.parseInt(zuobiao[1]) });
		return config;
	}

	public static YaoshenPublicConfig createYaoshenPublicConfig(
			Map<String, Object> tmp) {
		YaoshenPublicConfig config = new YaoshenPublicConfig();
		config.setOpen(CovertObjectUtil.object2int(tmp.get("open")));
		config.setItemgold(CovertObjectUtil.object2int(tmp.get("itemgold")));
		config.setItembgold(CovertObjectUtil.object2int(tmp.get("itembgold")));
		int yaoshencd = CovertObjectUtil.object2int(tmp.get("yaoshencd"));
		config.setYaoshencd(yaoshencd * 1000L);
		return config;
	}

	public static YaoshenPublicConfig createYaoshenMowenPublicConfig(
			Map<String, Object> tmp) {
		YaoshenPublicConfig config = new YaoshenPublicConfig();
		config.setOpen(CovertObjectUtil.object2int(tmp.get("open")));
		return config;
	}
	public static YaoshenMoYinConfig createYaoshenMoYinConfig(Map<String, Object> tmp){
		YaoshenMoYinConfig yaoshenMoYinConfig  = new YaoshenMoYinConfig();
		yaoshenMoYinConfig.setOpen(CovertObjectUtil.object2int(tmp.get("open")));
		return yaoshenMoYinConfig;
	}
	public static YaoshenFumoConfig createYaoshenFumoConfig(Map<String, Object> tmp){
		YaoshenFumoConfig config  = new YaoshenFumoConfig();
		config.setOpen(CovertObjectUtil.object2int(tmp.get("open")));
		return config;
	}
	public static ChengShenConfig createChengShenConfig(Map<String, Object> tmp){
		ChengShenConfig config  = new ChengShenConfig();
		config.setOpen(CovertObjectUtil.object2int(tmp.get("open")));
		return config;
	}
	public static YaoshenHunpoPublicConfig createYaoshenHunpoPublicConfig(
			Map<String, Object> tmp) {
		YaoshenHunpoPublicConfig config = new YaoshenHunpoPublicConfig();
		config.setOpen(CovertObjectUtil.object2int(tmp.get("open")));
		config.setJingQi(CovertObjectUtil.object2int(tmp.get("jingqi")));
		config.setMapId(CovertObjectUtil.object2int(tmp.get("mapid")));
		config.setTime(CovertObjectUtil.object2int(tmp.get("time")));
		String str = CovertObjectUtil.object2String(tmp.get("poshen"));
		String jiangitem = CovertObjectUtil.object2String(tmp.get("jiangitem"));
		String[] allItem = jiangitem
				.split(GameConstants.CONFIG_WEEK_SPLIT_CHAR);
		Map<String, Integer> jiangMap = new HashMap<>();
		for (int i = 0; i < allItem.length; i++) {
			String[] oneItem = allItem[i]
					.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
			jiangMap.put(oneItem[0], Integer.parseInt(oneItem[1]));
		}
		config.setJiangItem(new ReadOnlyMap<>(jiangMap));
		Map<String, Integer> item = new HashMap<>();
		String[] arr = str.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		item.put(arr[0], Integer.parseInt(arr[1]));
		config.setItem(new ReadOnlyMap<>(item));

		JianzhongFubenConfig jianzhongFubenConfig = new JianzhongFubenConfig();
		jianzhongFubenConfig.setMapId(config.getMapId());
		jianzhongFubenConfig.setTime(config.getTime());
		jianzhongFubenConfig.setJiangItem(config.getJiangItem());
		jianzhongFubenConfig.setOpenLevel(config.getOpen());

		config.setJianzhongFubenConfig(jianzhongFubenConfig);

		return config;
	}

	public static ChongwuPublicConfig createChongwuPublicConfig(
			Map<String, Object> tmp) {
		ChongwuPublicConfig config = new ChongwuPublicConfig();
		config.setOpen(CovertObjectUtil.object2int(tmp.get("open")));
		config.setSpeed(CovertObjectUtil.object2int(tmp.get("x19")));
		config.setItemgold(CovertObjectUtil.object2int(tmp.get("itemgold")));
		return config;
	}

	public static ChenghaoPublicConfig createChenghaoPublicConfig(
			Map<String, Object> tmp) {
		ChenghaoPublicConfig config = new ChenghaoPublicConfig();
		config.setTouxianvip(CovertObjectUtil.object2int(tmp.get("touxianvip")));
		config.setToudingnum(CovertObjectUtil.object2int(tmp.get("toudingnum")));
		return config;
	}

	/**
	 * 跨服个人竞技
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createKuafuArenaSinglePublicConfig(
			Map<String, Object> tmp) {
		KuafuArena1v1PublicConfig config = new KuafuArena1v1PublicConfig();

		config.setSmallWin(CovertObjectUtil.object2int(tmp.get("win1")));
		config.setBigWin(CovertObjectUtil.object2int(tmp.get("win2")));
		config.setSmallLose(CovertObjectUtil.object2int(tmp.get("lose1")));
		config.setBigLose(CovertObjectUtil.object2int(tmp.get("lose2")));
		config.setDayGongxunTimes(CovertObjectUtil.object2int(tmp.get("cishu")));
		config.setOpenBeginTime(CovertObjectUtil.object2String(tmp.get("time1")));
		config.setOpenEndTime(CovertObjectUtil.object2String(tmp.get("time2")));
		config.setOpenLevel(CovertObjectUtil.object2int(tmp.get("needlevel")));

		config.setMatchJie1(CovertObjectUtil.object2int(tmp.get("needtime1")));
		config.setMatchJie2(CovertObjectUtil.object2int(tmp.get("needtime2")));
		config.setMatchJie3(CovertObjectUtil.object2int(tmp.get("needtime3")));

		config.setDaojishi1(CovertObjectUtil.object2int(tmp.get("daojishi1")));
		config.setDaojishi2(CovertObjectUtil.object2int(tmp.get("daojishi2")));

		config.setExitDaojishi(CovertObjectUtil.object2int(tmp.get("miantime")));

		config.setJifenP(CovertObjectUtil.object2int(tmp.get("jifen")));
		config.setMapId(CovertObjectUtil.object2int(tmp.get("map")));

		String zuobiao1Str = CovertObjectUtil
				.object2String(tmp.get("zuobiao1"));
		String[] zuobiao1StrArray = zuobiao1Str.split("\\|");
		int[] zuobiao1 = new int[zuobiao1StrArray.length];
		for (int i = 0; i < zuobiao1StrArray.length; i++) {
			zuobiao1[0] = Integer.parseInt(zuobiao1StrArray[0]);
			zuobiao1[1] = Integer.parseInt(zuobiao1StrArray[1]);
		}
		config.setZuobiao1(zuobiao1);

		String zuobiao2Str = CovertObjectUtil
				.object2String(tmp.get("zuobiao2"));
		String[] zuobiao2StrArray = zuobiao2Str.split("\\|");
		int[] zuobiao2 = new int[zuobiao2StrArray.length];
		for (int i = 0; i < zuobiao2StrArray.length; i++) {
			zuobiao2[0] = Integer.parseInt(zuobiao2StrArray[0]);
			zuobiao2[1] = Integer.parseInt(zuobiao2StrArray[1]);
		}
		config.setZuobiao2(zuobiao2);

		config.setInitJifen(CovertObjectUtil.object2int(tmp.get("startfen")));
		config.setEndtime(CovertObjectUtil.object2int(tmp.get("endtime")));
		config.setZongtime(CovertObjectUtil.object2int(tmp.get("zongtime")));

		config.setPaiming(CovertObjectUtil.object2int(tmp.get("paiming")));

		config.setPaijifen(CovertObjectUtil.object2int(tmp.get("paijifen")));

		String qingjifen = CovertObjectUtil.object2String(tmp.get("qingjifen"));
		String[] qingjifenStrArray = qingjifen.split("_");
		int year = Integer.parseInt(qingjifenStrArray[0]);
		int month = Integer.parseInt(qingjifenStrArray[1]);
		int day = Integer.parseInt(qingjifenStrArray[2]);
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, day, 23, 59, 50);
		config.setQingjifenTime(c.getTimeInMillis());
		return config;
	}

	/**
	 * 八卦阵
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createBaguaPublicConfig(
			Map<String, Object> tmp) {
		BaguaPublicConfig config = new BaguaPublicConfig();

		config.setCeng(CovertObjectUtil.object2int(tmp.get("ceng")));

		String zuobiaoStr = CovertObjectUtil.object2String(tmp.get("zuobiao"));
		config.setZuobiaoStr(zuobiaoStr);
		String[] zuobiaoStrArray = zuobiaoStr.split(":");
		int[] zuobiao = new int[zuobiaoStrArray.length];
		for (int i = 0; i < zuobiaoStrArray.length; i++) {
			zuobiao[i] = Integer.parseInt(zuobiaoStrArray[i]);
		}
		config.setZuobiao(zuobiao);

		String bosszbStr = CovertObjectUtil.object2String(tmp.get("bosszb"));
		config.setBosszbStr(bosszbStr);
		String[] bosszbStrArray = bosszbStr.split(":");
		int[] bosszb = new int[bosszbStrArray.length];
		for (int i = 0; i < bosszbStrArray.length; i++) {
			bosszb[i] = Integer.parseInt(bosszbStrArray[i]);
		}
		config.setBosszb(bosszb);

		String fuhuoStr = CovertObjectUtil.object2String(tmp.get("fuhuo"));
		config.setFuhuoStr(fuhuoStr);
		String[] fuhuoStrArray = fuhuoStr.split(":");
		int[] fuhuo = new int[fuhuoStrArray.length];
		for (int i = 0; i < fuhuoStrArray.length; i++) {
			fuhuo[i] = Integer.parseInt(fuhuoStrArray[i]);
		}
		config.setFuhuo(fuhuo);

		config.setShanghai(CovertObjectUtil.object2int(tmp.get("shanghai")));

		config.setNocs(CovertObjectUtil.object2int(tmp.get("nocs")));

		String cszbStr = CovertObjectUtil.object2String(tmp.get("cszb"));
		config.setCszbStr(cszbStr);
		String[] cszbStrArray = cszbStr.split(";");
		List<int[]> cszb = new ArrayList<int[]>();
		for (int i = 0; i < cszbStrArray.length; i++) {
			String infoStr = cszbStrArray[i];
			String[] tmpArray = infoStr.split(":");
			int[] array = new int[tmpArray.length];
			for (int j = 0; j < tmpArray.length; j++) {
				array[j] = Integer.parseInt(tmpArray[j]);
			}
			cszb.add(array);
		}
		config.setCszbList(cszb);

		return config;
	}

	private static AdapterPublicConfig createMaiguPublicConfig(
			Map<String, Object> tmp) {
		MaiguPublicConfig config = new MaiguPublicConfig();
		config.setMgnpc(CovertObjectUtil.object2String(tmp.get("mgnpc")));

		String shzuobiaoStr = CovertObjectUtil.object2String(tmp
				.get("shzuobiao"));
		String[] shzuobiaoStrArray = shzuobiaoStr.split(":");
		int[] shzuobiao = new int[2];
		for (int i = 0; i < shzuobiaoStrArray.length; i++) {
			shzuobiao[i] = Integer.parseInt(shzuobiaoStrArray[i]);
		}
		config.setShzuobiao(shzuobiao);

		String gwzuobiaoStr = CovertObjectUtil.object2String(tmp
				.get("gwzuobiao"));
		String[] gwzuobiaoStrArray = gwzuobiaoStr.split("\\|");
		int[][] gwzuobiao = new int[4][2];
		for (int i = 0; i < gwzuobiaoStrArray.length; i++) {
			String[] info = gwzuobiaoStrArray[i].split(":");
			for (int j = 0; j < info.length; j++) {
				gwzuobiao[i][j] = Integer.parseInt(info[j]);
			}
		}
		config.setGwzuobiao(gwzuobiao);

		config.setMapid(CovertObjectUtil.object2int(tmp.get("mapid")));

		String fszbStr = CovertObjectUtil.object2String(tmp.get("fszb"));
		String[] fszbStrArray = fszbStr.split("\\|");
		int[][] fszb = new int[4][2];
		for (int i = 0; i < fszbStrArray.length; i++) {
			String[] info = fszbStrArray[i].split(":");
			for (int j = 0; j < info.length; j++) {
				fszb[i][j] = Integer.parseInt(info[j]);
			}
		}
		config.setFszb(fszb);
		return config;
	}
	
	/**
	 * 创建boss
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createYijibossPublicConfig(
			Map<String, Object> tmp) {
		YijibossPublicConfig config = new YijibossPublicConfig();
		config.setOpen(CovertObjectUtil.object2int(tmp.get("open")));
		config.setLastattack(CovertObjectUtil.object2String(tmp.get("lastattack")));
		config.setMonsterId(CovertObjectUtil.object2String(tmp.get("bossid")));

		return config;
	}
	/**
	 * 跨服boss
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createYueNanKuafuBossPublicConfig(
			Map<String, Object> tmp) {
		KuafuBossPublicConfig config = new KuafuBossPublicConfig();
		config.setOpen(CovertObjectUtil.object2int(tmp.get("open")));
		config.setBossid(CovertObjectUtil.object2String(tmp.get("bossid")));
		config.setMapid(CovertObjectUtil.object2int(tmp.get("mapid")));		
		String zuobiaoStr = CovertObjectUtil.object2String(tmp
				.get("zuobiao")); 
		String[] zuobiaoStrArray = zuobiaoStr.split(":");
		int[] zuobiaoArray = new int[zuobiaoStrArray.length];
		for(int i=0;i<zuobiaoStrArray.length;i++){
			zuobiaoArray[i] = Integer.parseInt(zuobiaoStrArray[i]);
		}
		config.setZuobiao(zuobiaoArray);
		String jiangItemStr = CovertObjectUtil.object2String(tmp
				.get("lastattack"));
		Map<String, Integer> jiangItem = ConfigAnalysisUtils
				.getConfigMap(jiangItemStr);
		config.setLastattack(jiangItem);
		config.setBossdeathgg(CovertObjectUtil.object2int(tmp.get("bossdeathgg")));
		config.setExptime(CovertObjectUtil.object2int(tmp.get("exptime")));
		config.setJiangexp(CovertObjectUtil.obj2long(tmp.get("jiangexp")));
		config.setMaxpople(CovertObjectUtil.object2int(tmp.get("maxpople")));
		
		String zuobiao1Str = CovertObjectUtil.object2String(tmp
				.get("zuobiao1")); 
		String[] zuobiao1StrArray =zuobiao1Str.split("\\|");
		for(String e:zuobiao1StrArray){
			String[] zuobiaoStrArray1 = e.split(":");
			int[] zuobiaoArray1 = new int[zuobiaoStrArray1.length];
			for(int i=0;i<zuobiaoStrArray1.length;i++){
				zuobiaoArray1[i] = Integer.parseInt(zuobiaoStrArray1[i]);
			}
			config.addZuobiao1(zuobiaoArray1);
		}
		config.setFuhuotime(CovertObjectUtil.object2int(tmp.get("fuhuotime")));
		config.setAttacktime(CovertObjectUtil.object2int(tmp.get("attacktime")));
		return config;
	}
	/**
	 * 跨服群仙宴
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createKuafuQunXianYanPublicConfig(Map<String, Object> tmp) {
		KuafuQunXianYanPublicConfig config = new KuafuQunXianYanPublicConfig();
		config.setOpen(CovertObjectUtil.object2int(tmp.get("needlevel")));
		config.setZplus(CovertObjectUtil.object2int(tmp.get("needzplus")));
		config.setMapid(CovertObjectUtil.object2int(tmp.get("map")));	
		config.setStartTime(CovertObjectUtil.object2String(tmp.get("time1")));
		config.setEndTime(CovertObjectUtil.object2String(tmp.get("time2")));
		config.setMaxpople(CovertObjectUtil.object2int(tmp.get("huodongrs")));
		config.setKillValue(CovertObjectUtil.object2int(tmp.get("killvalue")));
		config.setKillValeMax(CovertObjectUtil.object2int(tmp.get("killvalemax")));
		config.setExptime(CovertObjectUtil.object2int(tmp.get("exptime")));
		config.setJlcode(CovertObjectUtil.object2String(tmp.get("jlcode")));
		config.setRankRefresh(CovertObjectUtil.object2int(tmp.get("cd")));
		config.setJiangexp(CovertObjectUtil.obj2long(tmp.get("jiangexp")));
		config.setSxCode(CovertObjectUtil.object2String(tmp.get("gonggao")));
		config.setCjCode(CovertObjectUtil.object2String(tmp.get("gonggao1")));
		
		return config;
	}
	/**
	 * 五行
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createWuXingPublicConfig(Map<String, Object> tmp) {
		WuXingPublicConfig config = new WuXingPublicConfig();
		config.setMoshencd(CovertObjectUtil.obj2long(tmp.get("moshencd")));
		
		return config;
	}

    /**
     * 五行副本
     * @param tmp
     * @return
     */
    private static AdapterPublicConfig createWuXingFubenPublicConfig(Map<String, Object> tmp) {
        WuXingFubenPublicConfig config = new WuXingFubenPublicConfig();
        config.setCount(CovertObjectUtil.object2int(tmp.get("cishu")));
        config.setBuyNeedGold(CovertObjectUtil.object2int(tmp.get("needgold")));

        return config;
    }

    /**
     * 五行技能副本
     * @param tmp
     * @return
     */
    private static AdapterPublicConfig createWuXingSkillFubenPublicConfig(Map<String, Object> tmp) {
        WuXingSkillFubenPublicConfig config = new WuXingSkillFubenPublicConfig();
        config.setMapId(CovertObjectUtil.object2int(tmp.get("map")));
        config.setTime1(CovertObjectUtil.object2int(tmp.get("time1")));
        config.setTime2(CovertObjectUtil.object2int(tmp.get("time2")));
        String pointstr = CovertObjectUtil.object2String(tmp.get("zuobiao2"));
        if (!ObjectUtil.strIsEmpty(pointstr)) {
            String[] points = pointstr.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
            config.setBossXyPoint(new Integer[] { Integer.parseInt(points[0]), Integer.parseInt(points[1]) });
        }
        return config;
    }
    
    /**
     * 五行魔神精魄
     * @param tmp
     * @return
     */
    private static AdapterPublicConfig createWuXingJingpoPublicConfig(Map<String, Object> tmp) {
        WuXingJingpoPublicConfig config = new WuXingJingpoPublicConfig();
        config.setNeedGold(CovertObjectUtil.object2int(tmp.get("needgold1")));
        config.setAutoCount(CovertObjectUtil.object2int(tmp.get("counts")));
        config.setKttjLevel(CovertObjectUtil.object2int(tmp.get("kuitan")));
        return config;
    }

	/**
	 * 跨服大乱斗
	 * 
	 * @param tmp
	 * @return
	 */
	private static AdapterPublicConfig createKuafuLuanDouPublicConfig(Map<String, Object> tmp) {
		KuafuLuanDouPublicConfig config = new KuafuLuanDouPublicConfig();
		config.setMaxpople(CovertObjectUtil.object2int(tmp.get("maxpople")));
		
		String zuobiao1Str = CovertObjectUtil.object2String(tmp.get("zuobiao")); 
		String[] zuobiao1StrArray =zuobiao1Str.split("\\|");
		for(String e:zuobiao1StrArray){
			String[] zuobiaoStrArray1 = e.split(":");
			int[] zuobiaoArray1 = new int[zuobiaoStrArray1.length];
			for(int i=0;i<zuobiaoStrArray1.length;i++){
				zuobiaoArray1[i] = Integer.parseInt(zuobiaoStrArray1[i]);
			}
			config.addZuobiao1(zuobiaoArray1);
		}
		config.setFuhuotime(CovertObjectUtil.object2int(tmp.get("zdfh")));
		config.setCftime(CovertObjectUtil.object2int(tmp.get("cftime")));
		config.setKilljf(CovertObjectUtil.object2int(tmp.get("killjf")));
		config.setFhbuff(CovertObjectUtil.object2String(tmp.get("fhbuff")));
		config.setStoptime(CovertObjectUtil.object2int(tmp.get("stoptime")));
		
		return config;
	}

	/**
     * 心魔-天炉炼丹
     * @param tmp
     * @return
     */
    private static AdapterPublicConfig createXinmoLiandanPublicConfig(Map<String, Object> tmp) {
        XinmoLiandanPublicConfig config = new XinmoLiandanPublicConfig();
        config.setOpenLevel(CovertObjectUtil.object2int(tmp.get("needlevel")));
        return config;
    }
    
    /**
     * 心魔-魔神
     * @param tmp
     * @return
     */
    private static AdapterPublicConfig createXinmoMoshenPublicConfig(Map<String, Object> tmp) {
        XinmoMoshenPublicConfig config = new XinmoMoshenPublicConfig();
        config.setXinMoCd(CovertObjectUtil.object2int(tmp.get("xinmocd")));
        config.setXinMoChiXuCd(CovertObjectUtil.object2int(tmp.get("xinmochixu")));
        return config;
    }
    
    /**
     * 心魔-副本
     * @param tmp
     * @return
     */
    private static AdapterPublicConfig createXinmoFubenPublicConfig(Map<String, Object> tmp) {
        XinmoFubenPublicConfig config = new XinmoFubenPublicConfig();
        config.setMaxFuhuaVal(CovertObjectUtil.object2int(tmp.get("fuhuamax")));
        return config;
    }
    
    /**
     * 心魔-深渊副本
     * 
     * @param tmp
     * @return
     */
    private static AdapterPublicConfig createXinmoShenyuanFubenPublicConfig(Map<String, Object> tmp) {
        return new XinmoShenyuanFubenPublicConfig(
                CovertObjectUtil.object2int(tmp.get("cd")), 
                CovertObjectUtil.object2int(tmp.get("cd_time")), 
                CovertObjectUtil.object2int(tmp.get("cd_gold")));
    }
    
    /**
     * 心魔-洗练
     * 
     * @param tmp
     * @return
     */
    private static AdapterPublicConfig createXinmoXilianPublicConfig(Map<String, Object> tmp) {
        return new XinmoXilianPublicConfig(
                CovertObjectUtil.object2String(tmp.get("needitem1")), 
                CovertObjectUtil.object2String(tmp.get("needitem2")),
                CovertObjectUtil.object2String(tmp.get("needitem3")),
                CovertObjectUtil.object2int(tmp.get("needmoney")));
    }
    
    /**
     * 心魔-斗场
     * 
     * @param tmp
     * @return
     */
    private static AdapterPublicConfig createXinmoDouchangPublicConfig(Map<String, Object> tmp) {
        return new XinmoDouchangFubenPublicConfig(
                CovertObjectUtil.object2int(tmp.get("map")), 
                CovertObjectUtil.object2int(tmp.get("time")),
                CovertObjectUtil.object2int(tmp.get("times")),
                CovertObjectUtil.object2int(tmp.get("needgold")));
    }
    
    /**
     * 跨服云宫之巅
     * 
     * @param tmp
     * @return
     */
    private static AdapterPublicConfig createKuafuYunGongPublicConfig(Map<String, Object> tmp) {
        KuafuYunGongPublicConfig publicConfig = null;
        try {
            publicConfig = new KuafuYunGongPublicConfig();
            publicConfig.setWeek(CovertObjectUtil.object2int(tmp.get("week")));
            
            String readytimeStr = CovertObjectUtil.object2String(tmp.get("readytime"));
            String[] readytime = readytimeStr.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
            publicConfig.setReadytime(new int[] { Integer.parseInt(readytime[0]), Integer.parseInt(readytime[1]) });
            
            String starttimeStr = CovertObjectUtil.object2String(tmp.get("starttime"));
            String[] starttime = starttimeStr.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
            publicConfig.setStarttime(new int[] { Integer.parseInt(starttime[0]), Integer.parseInt(starttime[1]) });
            
            String endtimeStr = CovertObjectUtil.object2String(tmp.get("endtime"));
            String[] endtime = endtimeStr.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
            publicConfig.setEndtime(new int[] { Integer.parseInt(endtime[0]), Integer.parseInt(endtime[1]) });
            
            publicConfig.setWinBg(CovertObjectUtil.object2int(tmp.get("winBg")));
            publicConfig.setWinExp(CovertObjectUtil.object2int(tmp.get("winExp")));
            publicConfig.setAddexp(CovertObjectUtil.obj2float(tmp.get("addexp")));
            publicConfig.setAddzhenqi(CovertObjectUtil.obj2float(tmp.get("addzhenqi")));
            publicConfig.setJiangitem(ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(tmp.get("jiangitem"))));
            publicConfig.setLoseBg(CovertObjectUtil.object2int(tmp.get("loseBg")));
            publicConfig.setLoseExp(CovertObjectUtil.object2int(tmp.get("loseExp")));
            
            publicConfig.setTime2(CovertObjectUtil.object2int(tmp.get("time2")));
            publicConfig.setZhenqi2(CovertObjectUtil.object2int(tmp.get("zhenqi2")));
            publicConfig.setJingyan2(CovertObjectUtil.object2int(tmp.get("jingyan2")));
            
            publicConfig.setMap(CovertObjectUtil.object2int(tmp.get("map")));
            publicConfig.setTime(CovertObjectUtil.object2int(tmp.get("time")));
            publicConfig.setNeedTime(CovertObjectUtil.object2int(tmp.get("needTime")));
            publicConfig.setNeedZhanli(CovertObjectUtil.obj2long(tmp.get("needZhanli")));
            publicConfig.setNeedLevel(CovertObjectUtil.object2int(tmp.get("needLevel")));
            int groupNumber = CovertObjectUtil.object2int(tmp.get("groupNumber"));
            publicConfig.setGroupNumber(groupNumber > KuafuYunGongConstants.DEFAULT_GROUP_NUMBER ? KuafuYunGongConstants.DEFAULT_GROUP_NUMBER : groupNumber);
            
            publicConfig.setDelay(CovertObjectUtil.object2int(tmp.get("delay")));
            publicConfig.setExp(CovertObjectUtil.obj2long(tmp.get("exp")));
            publicConfig.setZhenqi(CovertObjectUtil.obj2long(tmp.get("zhenqi")));
            
            publicConfig.setZiyuanid(CovertObjectUtil.object2int(tmp.get("ziyuanid")));
            String zuobiaoStr = CovertObjectUtil.object2String(tmp.get("flagzuobiao"));
            String[] zuobiao = zuobiaoStr.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
            publicConfig.setZuobiao(new int[] { Integer.parseInt(zuobiao[0]), Integer.parseInt(zuobiao[1]) });
            publicConfig.setNeedgong(CovertObjectUtil.object2int(tmp.get("needgong")));
            publicConfig.setKqbuff(CovertObjectUtil.obj2StrOrNull(tmp.get("kqbuff")));
            publicConfig.setLongt(CovertObjectUtil.obj2StrOrNull(tmp.get("longt")));
            publicConfig.setShanghai(CovertObjectUtil.object2int(tmp.get("shanghai")));
            
            List<Integer[]> fuhuoZuobiao = new ArrayList<Integer[]>();
            String gongFuhuoStr1 = CovertObjectUtil.object2String(tmp.get("gongfuhuo"));
            for (String fuhuoStr : gongFuhuoStr1.split(GameConstants.CONFIG_SPLIT_CHAR)) {
                String[] fuhuo = fuhuoStr.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
                fuhuoZuobiao.add(new Integer[] { Integer.parseInt(fuhuo[0]), Integer.parseInt(fuhuo[1]) });
            }
            publicConfig.setGongfuhuo(fuhuoZuobiao);
            
            List<Integer[]> fuhuoZuobiao2 = new ArrayList<Integer[]>();
            String gongFuhuoStr2 = CovertObjectUtil.object2String(tmp.get("shoufuhuo"));
            for (String fuhuoStr : gongFuhuoStr2.split(GameConstants.CONFIG_SPLIT_CHAR)) {
                String[] fuhuo = fuhuoStr.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
                fuhuoZuobiao2.add(new Integer[] { Integer.parseInt(fuhuo[0]), Integer.parseInt(fuhuo[1]) });
            }
            publicConfig.setShoufuhuo(fuhuoZuobiao2);
            
            publicConfig.setGonggao1(CovertObjectUtil.object2int(tmp.get("gonggao1")));
            publicConfig.setGonggao2(CovertObjectUtil.object2int(tmp.get("gonggao2")));
            publicConfig.setGonggao3(CovertObjectUtil.object2int(tmp.get("gonggao3")));
            publicConfig.setGonggao4(CovertObjectUtil.object2int(tmp.get("gonggao4")));
            publicConfig.setGonggao5(CovertObjectUtil.object2int(tmp.get("gonggao5")));
            publicConfig.setChenhao1(CovertObjectUtil.obj2StrOrNull(tmp.get("chenhao1")));
            publicConfig.setChenhao2(CovertObjectUtil.obj2StrOrNull(tmp.get("chenhao2")));
        } catch (Exception e) {
            ChuanQiLog.error("解析跨服云宫之巅公共配置数据异常,异常信息:{}", e.getMessage());
        }
        return publicConfig;
    }

    
    /**
     * 构造魔宫猎焰公共数据
     * 
     * @param tmp
     * @return
     */
    private static AdapterPublicConfig createMoGongLieYanPublicConfig(Map<String, Object> tmp) {
        MoGongLieYanPublicConfig publicConfig = null;
        try {
            publicConfig = new MoGongLieYanPublicConfig();
            publicConfig.setYumo(CovertObjectUtil.object2int(tmp.get("yumo")));
            publicConfig.setMap(CovertObjectUtil.object2int(tmp.get("map")));
            publicConfig.setTime(CovertObjectUtil.object2int(tmp.get("time")));
            publicConfig.setExp(CovertObjectUtil.object2int(tmp.get("exp")));
            publicConfig.setZhenqi(CovertObjectUtil.object2int(tmp.get("zhenqi")));
            publicConfig.setJianyumo1(CovertObjectUtil.object2int(tmp.get("jianyumo1")));
            publicConfig.setJianyumo2(CovertObjectUtil.object2int(tmp.get("jianyumo2")));
            publicConfig.setJianyumo3(CovertObjectUtil.object2int(tmp.get("jianyumo3")));
            publicConfig.setJianyumo4(CovertObjectUtil.object2int(tmp.get("jianyumo4")));
            publicConfig.setDelaytime(CovertObjectUtil.object2int(tmp.get("delaytime")));
        } catch (Exception e) {
            ChuanQiLog.error("解析魔宫猎焰公共配置数据异常,异常信息:{}", e.getMessage());
        }
        
        return publicConfig;
    }
    
    /**
     * 构造云瑶晶脉公共数据
     * 
     * @param tmp
     * @return
     */
    private static AdapterPublicConfig createYunYaoJingMaiPublicConfig(Map<String, Object> tmp) {
        YunYaoJingMaiPublicConfig publicConfig = null;
        try {
            publicConfig = new YunYaoJingMaiPublicConfig();
            publicConfig.setMap(CovertObjectUtil.object2int(tmp.get("map")));
            publicConfig.setMaxtimes(CovertObjectUtil.object2int(tmp.get("maxtimes")));
            publicConfig.setNeedLevel(CovertObjectUtil.object2int(tmp.get("needLevel")));
            String starttimeStr = CovertObjectUtil.obj2StrOrNull(tmp.get("starttime"));
            if (!ObjectUtil.strIsEmpty(starttimeStr)) {
                String[] starttimeArr = starttimeStr.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
                if (null != starttimeArr) {
                    publicConfig.setStarttime(new int[] { Integer.parseInt(starttimeArr[0]), Integer.parseInt(starttimeArr[1]) });
                }
            }
            String endtimeStr = CovertObjectUtil.obj2StrOrNull(tmp.get("endtime"));
            if (!ObjectUtil.strIsEmpty(endtimeStr)) {
                String[] endtimeArr = endtimeStr.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
                if (null != endtimeArr) {
                    publicConfig.setEndtime(new int[] { Integer.parseInt(endtimeArr[0]), Integer.parseInt(endtimeArr[1]) });
                }
            }
        } catch (Exception e) {
            ChuanQiLog.error("解析云瑶晶脉公共配置数据异常,异常信息:{}", e.getMessage());
        }
        return publicConfig;
    }
    
    /**
     * 剧情坐标传送信息表
     * @param tmp
     * @return
     */
	private static AdapterPublicConfig createPlotPointsPublicConfig(Map<String, Object> tmp) {
        PlotPointsPublicConfig publicConfig = null;
        try {
            publicConfig = new PlotPointsPublicConfig();
            Object tmep =  tmp.get("points");
            if(tmep == null){
            	ChuanQiLog.error("公共配置表【juqingpoint】数据为空");
            	return publicConfig;
            }
            for (String msg : tmep.toString().split(",")) {
            	  publicConfig.addData(msg);
			}
        } catch (Exception e) {
            ChuanQiLog.error("解析云瑶晶脉公共配置数据异常,异常信息:{}", e.getMessage());
        }
        return publicConfig;
    }
	
	
	/**
	 * 新圣剑 
	 * @param tmp
	 * @return
	 */
	private static XinShengJianPublicConfig createXinShengJianPublicConfig(Map<String, Object> tmp) {
		XinShengJianPublicConfig config = new XinShengJianPublicConfig();
		config.setOpen(CovertObjectUtil.object2int(tmp.get("open")));
		return config;
	}
	
	/**
	 * 限时礼包
	 * @param tmp
	 * @return
	 */
	private static OpenPublicConfig createXianShiLibaoPublicConfig(Map<String, Object> tmp) {
		OpenPublicConfig config = new OpenPublicConfig();
		config.setOpen(CovertObjectUtil.object2int(tmp.get("open")));
		return config;
	}
	
	/**
	 * 邮件
	 * @param tmp
	 * @return
	 */
	private static MailPublicConfig createMailPublicConfig(Map<String, Object> tmp) {
		MailPublicConfig config = new MailPublicConfig();
		config.setMaxnum(CovertObjectUtil.object2int(tmp.get("maxnum")));
		config.setGouqi1(CovertObjectUtil.object2int(tmp.get("time")) * 24 * 60 * 60 * 1000L);
		config.setGouqi2(CovertObjectUtil.object2int(tmp.get("time1")) * 24 * 60 * 60 * 1000L);
		return config;
	}

	/**
	 * 活动大厅
	 * @param tmp
	 * @return
	 */
	private static ActivitieShall createactivitieShallPublicConfig(Map<String, Object> tmp) {
		ActivitieShall config = new ActivitieShall();
		config.setPvprefreshcd(CovertObjectUtil.object2int(tmp.get("pvprefreshcd")) * 1000);
		return config;
	}
}
