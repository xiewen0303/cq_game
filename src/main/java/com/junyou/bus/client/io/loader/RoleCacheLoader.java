package com.junyou.bus.client.io.loader;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.account.entity.RoleAccount;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.entity.RoleItem;
import com.junyou.bus.bag.entity.RoleItemDesc;
import com.junyou.bus.bag.entity.RoleItemUseCsxz;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.service.GoodsUseLimitService;
import com.junyou.bus.bagua.entity.RoleBagua;
import com.junyou.bus.bagua.service.export.BaguaExportService;
import com.junyou.bus.boss_jifen.entity.RoleBossJifen;
import com.junyou.bus.boss_jifen.service.export.RoleBossJifenExportService;
import com.junyou.bus.branchtask.entity.TaskBranch;
import com.junyou.bus.branchtask.service.TaskBranchService;
import com.junyou.bus.caidan.entity.RefbCaidan;
import com.junyou.bus.caidan.service.RoleCaidanExportService;
import com.junyou.bus.chenghao.entity.RoleChenghao;
import com.junyou.bus.chenghao.service.export.ChenghaoExportService;
import com.junyou.bus.chengjiu.entity.RoleChengjiu;
import com.junyou.bus.chengjiu.entity.RoleChengjiuData;
import com.junyou.bus.chengjiu.export.RoleChengJiuExportService;
import com.junyou.bus.chengshen.entity.RoleChengShen;
import com.junyou.bus.chengshen.export.ChengShenExportService;
import com.junyou.bus.chibang.entity.ChiBangInfo;
import com.junyou.bus.chibang.export.ChiBangExportService;
import com.junyou.bus.chongwu.entity.RoleChongwu;
import com.junyou.bus.chongwu.entity.RoleChongwuSkill;
import com.junyou.bus.chongwu.service.export.ChongwuExportService;
import com.junyou.bus.cuilian.entity.RoleCuilian;
import com.junyou.bus.cuilian.service.export.CuilianExportService;
import com.junyou.bus.cuxiao.export.CuxiaoExportService;
import com.junyou.bus.daytask.entity.TaskDay;
import com.junyou.bus.daytask.export.TaskDayExportService;
import com.junyou.bus.email.entity.EmailRelation;
import com.junyou.bus.email.export.EmailRelationExportService;
import com.junyou.bus.equip.entity.RoleXiangwei;
import com.junyou.bus.equip.entity.XuantieDuihuan;
import com.junyou.bus.equip.export.RoleXiangweiExportService;
import com.junyou.bus.equip.export.XuanTieDuiHuanExportService;
import com.junyou.bus.extremeRecharge.entity.RfbExtremeRecharge;
import com.junyou.bus.extremeRecharge.export.RfbExtremeRechargeExportService;
import com.junyou.bus.firstChargeRebate.entity.RefabuFirstChargeRebate;
import com.junyou.bus.firstChargeRebate.export.RefabuFirstChargeRebateExportService;
import com.junyou.bus.flower.entity.RoleSendFlower;
import com.junyou.bus.flower.export.FlowerSendExportService;
import com.junyou.bus.fuben.entity.Fuben;
import com.junyou.bus.fuben.entity.MoreFuben;
import com.junyou.bus.fuben.entity.WuxingFuben;
import com.junyou.bus.fuben.entity.WuxingShilianFuben;
import com.junyou.bus.fuben.entity.WuxingSkillFuben;
import com.junyou.bus.fuben.entity.XinmoDouchangFuben;
import com.junyou.bus.fuben.entity.XinmoFuben;
import com.junyou.bus.fuben.entity.XinmoShenyuanFuben;
import com.junyou.bus.fuben.export.FubenExportService;
import com.junyou.bus.fuben.export.MoreFubenExportService;
import com.junyou.bus.fushu.entity.FushuSkill;
import com.junyou.bus.fushu.export.FushuSkillExportService;
import com.junyou.bus.giftcard.entity.GiftCard;
import com.junyou.bus.giftcard.entity.GiftCardPlatform;
import com.junyou.bus.giftcard.export.GiftCardExportService;
import com.junyou.bus.happycard.entity.RefabuHappyCard;
import com.junyou.bus.happycard.entity.RefabuHappyCardItem;
import com.junyou.bus.happycard.service.export.HappyCardExportService;
import com.junyou.bus.huajuan.entity.RoleHuajuan;
import com.junyou.bus.huajuan.entity.RoleHuajuanExp;
import com.junyou.bus.huajuan.service.export.HuajuanExportService;
import com.junyou.bus.huajuan2.entity.RoleHuajuan2;
import com.junyou.bus.huajuan2.entity.RoleHuajuan2Exp;
import com.junyou.bus.huajuan2.export.Huajuan2ExportService;
import com.junyou.bus.huanhua.entity.RoleHuanhua;
import com.junyou.bus.huanhua.service.export.HuanhuaExportService;
import com.junyou.bus.jewel.entity.RoleJewel;
import com.junyou.bus.jewel.export.JewelExportService;
import com.junyou.bus.jingji.entity.RoleJingjiDuihuan;
import com.junyou.bus.jingji.export.JingjiExportService;
import com.junyou.bus.jueban.entity.RefabuJueban;
import com.junyou.bus.jueban.export.RefabuJuebanExportService;
import com.junyou.bus.kaifuactivity.entity.QiriLevelLibao;
import com.junyou.bus.kaifuactivity.entity.ZhanliBipin;
import com.junyou.bus.kaifuactivity.export.KaiFuHuoDongExportService;
import com.junyou.bus.kfjingji.entity.KuafuJingji;
import com.junyou.bus.kfjingji.export.KuafuJingjiExportService;
import com.junyou.bus.kuafuarena1v1.entity.RoleGongxunDuihuanInfo;
import com.junyou.bus.kuafuarena1v1.entity.RoleKuafuArena1v1;
import com.junyou.bus.kuafuarena1v1.service.export.KuafuArena1v1SourceExportService;
import com.junyou.bus.laowanjia.entity.RefbLaowanjia;
import com.junyou.bus.laowanjia.export.RefbLaowanjiaExportService;
import com.junyou.bus.leichong.entity.Leichong;
import com.junyou.bus.leichong.export.LeiChongExportService;
import com.junyou.bus.leihao.entity.RefabuLeihao;
import com.junyou.bus.leihao.service.export.LeiHaoExportService;
import com.junyou.bus.lianchong.entity.RoleLianchong;
import com.junyou.bus.lianchong.export.LianChongExportService;
import com.junyou.bus.lianyuboss.entity.GuildBossLianyu;
import com.junyou.bus.lianyuboss.export.LianyuBossExortService;
import com.junyou.bus.linghuo.entity.RoleLinghuoBless;
import com.junyou.bus.linghuo.entity.RoleLinghuoInfo;
import com.junyou.bus.linghuo.export.LingHuoExportService;
import com.junyou.bus.lj.entity.RoleLj;
import com.junyou.bus.lj.export.LJExportService;
import com.junyou.bus.login.entity.RefabuSevenLogin;
import com.junyou.bus.login.export.RefabuSevenLoginExportService;
import com.junyou.bus.lunpan.entity.RefabuLunpan;
import com.junyou.bus.lunpan.export.LunPanExportService;
import com.junyou.bus.maigu.entity.RoleMaigu;
import com.junyou.bus.maigu.service.export.MaiguExportService;
import com.junyou.bus.marry.entity.RoleMarryInfo;
import com.junyou.bus.marry.export.MarryExportService;
import com.junyou.bus.miaosha.entity.RefbMiaosha;
import com.junyou.bus.miaosha.export.RefbMiaoshaExportService;
import com.junyou.bus.mogonglieyan.entity.RoleMogonglieyan;
import com.junyou.bus.mogonglieyan.export.RoleMoGongLieYanExportService;
import com.junyou.bus.oncechong.entity.RoleOncechong;
import com.junyou.bus.oncechong.export.OnceChongExportService;
import com.junyou.bus.onlinerewards.entity.RoleOnlineRewards;
import com.junyou.bus.onlinerewards.export.OnlineRewardsExportService;
import com.junyou.bus.personal_boss.entity.RolePersonalBoss;
import com.junyou.bus.personal_boss.service.export.RolePersonalBossExportService;
import com.junyou.bus.platform.qq.entity.Renwujishi;
import com.junyou.bus.platform.qq.entity.RolePlatformQqHz;
import com.junyou.bus.platform.qq.entity.RolePlatformQqLz;
import com.junyou.bus.platform.qq.entity.RoleQdianZhigou;
import com.junyou.bus.platform.qq.entity.RoleQqTgp;
import com.junyou.bus.platform.qq.entity.TencentLanzuan;
import com.junyou.bus.platform.qq.entity.TencentShangdian;
import com.junyou.bus.platform.qq.entity.TencentUserInfo;
import com.junyou.bus.platform.qq.entity.TencentWeiduan;
import com.junyou.bus.platform.qq.service.export.QqExportService;
import com.junyou.bus.platform.qq.service.export.RenWuJiShiExportService;
import com.junyou.bus.platform.qq.service.export.TencentLuoPanExportService;
import com.junyou.bus.platform.qq.service.export.TencentShangDianExportService;
import com.junyou.bus.platform.taiwan.export.TaiWanExportService;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.platform.yuenan.entity.YuenanYaoqing;
import com.junyou.bus.platform.yuenan.export.YuenanExportService;
import com.junyou.bus.qiling.entity.QiLingInfo;
import com.junyou.bus.qiling.export.QiLingExportService;
import com.junyou.bus.qipan.entity.Qipan;
import com.junyou.bus.qipan.export.QiPanExportService;
import com.junyou.bus.rechargefanli.entity.RefabuRefanli;
import com.junyou.bus.rechargefanli.export.RechargeFanliExportService;
import com.junyou.bus.rfbactivity.entity.RoleYuanbaoRecord;
import com.junyou.bus.rfbactivity.export.RefabuActivityExportService;
import com.junyou.bus.rfbflower.entity.RoleRfbFlower;
import com.junyou.bus.rfbflower.export.FlowerCharmRankExportService;
import com.junyou.bus.role.entity.Tangbao;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.TangbaoExportService;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoExportService;
import com.junyou.bus.rolebusiness.entity.RoleBusinessInfo;
import com.junyou.bus.rolestage.entity.RoleStage;
import com.junyou.bus.rolestage.export.RoleStageExportService;
import com.junyou.bus.shenmo.entity.RoleKuafuArena4v4;
import com.junyou.bus.shenmo.service.export.KuafuArena4v4SourceExportService;
import com.junyou.bus.shenqi.entity.ShenQiEquip;
import com.junyou.bus.shenqi.entity.ShenQiInfo;
import com.junyou.bus.shenqi.entity.ShenQiJinjie;
import com.junyou.bus.shenqi.export.ShenQiEquipExportService;
import com.junyou.bus.shenqi.export.ShenQiExportService;
import com.junyou.bus.shenqi.export.ShenQiJinJieExportService;
import com.junyou.bus.shizhuang.entity.RoleShiZhuangJinJie;
import com.junyou.bus.shizhuang.entity.RoleShizhuang;
import com.junyou.bus.shizhuang.export.RoleShiZhuangExportService;
import com.junyou.bus.shoplimit.entity.ShopLimitInfo;
import com.junyou.bus.shoplimit.export.ShopLimitExportService;
import com.junyou.bus.shouchong.entity.RefbRoleShouchong;
import com.junyou.bus.shouchong.export.RefbRoleShouchongExportService;
import com.junyou.bus.skill.entity.RoleSkill;
import com.junyou.bus.skill.entity.RoleSkillGewei;
import com.junyou.bus.skill.export.RoleSkillExportService;
import com.junyou.bus.smsd.entity.ShenmiShangdian;
import com.junyou.bus.smsd.export.ShenMiShangDianExportService;
import com.junyou.bus.suoyaota.entity.RefbSuoyaota;
import com.junyou.bus.suoyaota.export.SuoYaoTaExportService;
import com.junyou.bus.superduihuan.entity.RefabuSuperDuihuan;
import com.junyou.bus.superduihuan.service.export.RfbSuperDuihuanExportService;
import com.junyou.bus.tafang.entity.RoleTafang;
import com.junyou.bus.tafang.export.RoleTaFangExportService;
import com.junyou.bus.tanbao.entity.RefabuTanbao;
import com.junyou.bus.tanbao.export.TanSuoBaoZangExportService;
import com.junyou.bus.task.entity.Task;
import com.junyou.bus.task.export.TaskExportService;
import com.junyou.bus.territory.entity.TerritoryDayReward;
import com.junyou.bus.territory.export.TerritoryExportService;
import com.junyou.bus.tianyu.entity.TianYuInfo;
import com.junyou.bus.tianyu.export.TianYuExportService;
import com.junyou.bus.tongtian.entity.RoleTongtianRoad;
import com.junyou.bus.tongtian.export.TongtianRoadExportService;
import com.junyou.bus.touzi.entity.RoleTouzi;
import com.junyou.bus.touzi.export.RoleTouziExportService;
import com.junyou.bus.tuangou.entity.RefbRoleTuangou;
import com.junyou.bus.tuangou.export.TuanGouExportService;
import com.junyou.bus.wenquan.entity.RoleWenquan;
import com.junyou.bus.wenquan.service.export.WenquanExportService;
import com.junyou.bus.wuqi.entity.WuQiInfo;
import com.junyou.bus.wuqi.export.WuQiExportService;
import com.junyou.bus.wuxing.entity.RoleWuxing;
import com.junyou.bus.wuxing.entity.RoleWuxingFuti;
import com.junyou.bus.wuxing.entity.RoleWuxingJingpo;
import com.junyou.bus.wuxing.entity.RoleWuxingJingpoItem;
import com.junyou.bus.wuxing.entity.RoleWuxingSkill;
import com.junyou.bus.wuxing.entity.TangbaoWuxing;
import com.junyou.bus.wuxing.entity.TangbaoWuxingSkill;
import com.junyou.bus.wuxing.export.WuXingMoShenExportService;
import com.junyou.bus.xianjian.entity.XianJianInfo;
import com.junyou.bus.xianjian.export.XianJianExportService;
import com.junyou.bus.xianqi.entity.RoleXianqi;
import com.junyou.bus.xianqi.entity.RoleXianqiJuexing;
import com.junyou.bus.xianqi.entity.RoleXianyuanFeihua;
import com.junyou.bus.xianqi.entity.XianqiFuben;
import com.junyou.bus.xianqi.export.XianYuanFeiHuaServiceExport;
import com.junyou.bus.xianqi.export.XianqiFubenServiceExport;
import com.junyou.bus.xianqi.export.XianqiServiceExport;
import com.junyou.bus.xiaofei.entity.RefabuXiaofei;
import com.junyou.bus.xiaofei.server.RefabuXiaoFeiService;
import com.junyou.bus.xingkongbaozang.entity.RefabuXkbz;
import com.junyou.bus.xingkongbaozang.export.XkbzExportService;
import com.junyou.bus.xinmo.entity.RoleXinmo;
import com.junyou.bus.xinmo.entity.RoleXinmoLiandan;
import com.junyou.bus.xinmo.entity.RoleXinmoLiandanItem;
import com.junyou.bus.xinmo.entity.RoleXinmoMoshen;
import com.junyou.bus.xinmo.entity.RoleXinmoMoshenShiti;
import com.junyou.bus.xinmo.entity.RoleXinmoSkill;
import com.junyou.bus.xinmo.entity.RoleXinmoXilian;
import com.junyou.bus.xinmo.export.XinmoExportService;
import com.junyou.bus.xinwen.entity.RoleTangbaoXinwen;
import com.junyou.bus.xinwen.export.XinwenExportService;
import com.junyou.bus.xiulianzhilu.entity.RoleXiulianJifen;
import com.junyou.bus.xiulianzhilu.entity.RoleXiulianTask;
import com.junyou.bus.xiulianzhilu.export.XiuLianExportService;
import com.junyou.bus.xiuxian.entity.RefbRoleXiuxian;
import com.junyou.bus.xiuxian.export.RfbXiuXianExportService;
import com.junyou.bus.xunbao.entity.RefbXunbao;
import com.junyou.bus.xunbao.export.RefbXunBaoExportService;
import com.junyou.bus.yabiao.entity.Yabiao;
import com.junyou.bus.yabiao.service.export.YabiaoExportService;
import com.junyou.bus.yaoshen.entity.RoleYaoshen;
import com.junyou.bus.yaoshen.entity.RoleYaoshenFumo;
import com.junyou.bus.yaoshen.entity.RoleYaoshenHunpo;
import com.junyou.bus.yaoshen.entity.RoleYaoshenMowen;
import com.junyou.bus.yaoshen.entity.RoleYaoshenMoyin;
import com.junyou.bus.yaoshen.service.export.YaoshenExportService;
import com.junyou.bus.yaoshen.service.export.YaoshenFumoExportService;
import com.junyou.bus.yaoshen.service.export.YaoshenHunpoExportService;
import com.junyou.bus.yaoshen.service.export.YaoshenMoYinExportService;
import com.junyou.bus.zhanjia.entity.ZhanJiaInfo;
import com.junyou.bus.zhanjia.export.ZhanJiaExportService;
import com.junyou.bus.zhuanpan.entity.RefabuZhuanpan;
import com.junyou.bus.zhuanpan.export.ZhuanPanExportService;
import com.junyou.bus.zhuansheng.entity.Zhuansheng;
import com.junyou.bus.zhuansheng.export.ZhuanshengExportService;
import com.junyou.bus.zuoqi.entity.ZuoQiInfo;
import com.junyou.bus.zuoqi.export.ZuoQiExportService;
import com.kernel.data.cache.EntityCache;
import com.kernel.data.cache.IEntityCache;
import com.kernel.data.cache.IEntityCacheLoader;

@Component("roleCacheLoader")
public class RoleCacheLoader implements IEntityCacheLoader {
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleExportService userRoleExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RoleStageExportService roleStageExportService; 
	@Autowired
	private GoodsUseLimitService goodsUseLimitService;
	@Autowired
	private TaskExportService taskExportService;
	@Autowired
	private EmailRelationExportService emailRelationExportService;
	@Autowired
	private RoleSkillExportService roleSkillExportService;
	@Autowired
	private FubenExportService fubenExportService;
	@Autowired
	private MoreFubenExportService moreFubenExportService;
	@Autowired
	private JingjiExportService jingjiExportService;
	@Autowired
	private RoleBusinessInfoExportService roleBusinessInfoExportService;
	@Autowired
	private ZuoQiExportService zuoQiExportService;
	@Autowired
	private TaskDayExportService taskDayExportService;
	@Autowired
	private ChiBangExportService chiBangExportService;
	@Autowired
	private QiLingExportService qilingExportService;
	@Autowired
	private TianYuExportService tianyuExportService;
	@Autowired
	private XianJianExportService xianJianExportService;
	@Autowired
	private TangbaoExportService tangbaoExportService;
	@Autowired
	private GiftCardExportService giftCardExportService;
	@Autowired
	private RefbRoleShouchongExportService refbRoleShouchongExportService;
	@Autowired
	private XuanTieDuiHuanExportService xuanTieDuiHuanExportService;
	@Autowired
	private KaiFuHuoDongExportService kaiFuHuoDongExportService;
	@Autowired
	private RfbXiuXianExportService rfbXiuXianExportService;
	@Autowired
	private ShenQiExportService shenQiExportService;
	@Autowired
	private HuajuanExportService huajuanExportService;
	@Autowired
	private HuanhuaExportService huanhuaExportService;
	@Autowired
	private KuafuArena1v1SourceExportService kuafuArena1v1ExportService;
	@Autowired
	private KuafuArena4v4SourceExportService kuafuArena4v4ExportService;
	@Autowired
	private BaguaExportService baguaExportService;
	@Autowired
	private MaiguExportService maiguExportService;
	@Autowired
	private ChongwuExportService chongwuExportService;
	@Autowired
	private YabiaoExportService yabiaoExportService;
	@Autowired
	private WenquanExportService wenquanExportService;
	@Autowired
	private YaoshenExportService yaoshenExportService;
	@Autowired
	private XinwenExportService xinwenExportService;
	@Autowired
	private TongtianRoadExportService tongtianLoadExportService;
	@Autowired
	private CuxiaoExportService cuxiaoExportService;
	@Autowired
	private ChengShenExportService chengShenExportService;
	@Autowired
	private YaoshenHunpoExportService yaoshenHunpoExportService;
	@Autowired
	private YaoshenMoYinExportService yaoshenMoYinExportService;
	@Autowired
	private YaoshenFumoExportService yaoshenFumoExportService;
	@Autowired
	private ChenghaoExportService chenghaoExportService;
	@Autowired
	private QqExportService qqExportService;
	@Autowired
	private QiPanExportService qiPanExportService;
	@Autowired
	private LeiChongExportService leiChongExportService;
	@Autowired
	private XkbzExportService xkbzExportService;
	@Autowired
	private LianChongExportService lianChongExportService;
	@Autowired
	private OnlineRewardsExportService onlineRewardsExportService;
	@Autowired
	private JewelExportService jewelExportService;
	@Autowired
	private FlowerSendExportService flowerSendExportService;
	@Autowired
	private FlowerCharmRankExportService flowerCharmRankExportService;
	@Autowired
	private ShenMiShangDianExportService shenMiShangDianExportService;
	@Autowired
	private RefabuXiaoFeiService refabuXiaoFeiService;
	@Autowired
	private RoleTouziExportService roleTouziExportService;
	@Autowired
	private ZhuanPanExportService zhuanPanExportService;
	@Autowired
	private LunPanExportService lunPanExportService;
	@Autowired
	private ZhanJiaExportService zhanJiaExportService;
	@Autowired
	private LingHuoExportService lingHuoExportService;
	@Autowired
	private TerritoryExportService territoryExportService;
	@Autowired
	private RoleChengJiuExportService roleChengJiuExportService;
	@Autowired
	private RenWuJiShiExportService renWuJiShiExportService;
	@Autowired
	private RefabuActivityExportService refabuActivityExportService;
	@Autowired
	private RfbSuperDuihuanExportService superDuihuanExportService;
	@Autowired
	private HappyCardExportService happyCardExportService;
	@Autowired
	private SuoYaoTaExportService suoYaoTaExportService;
	@Autowired
	private RoleShiZhuangExportService roleShiZhuangExportService;
	@Autowired
	private TencentLuoPanExportService tencentLuoPanExportService;
	@Autowired
	private TencentShangDianExportService tencentShangDianExportService;
	@Autowired
	private MarryExportService marryExportService;
	@Autowired
	private RechargeFanliExportService rechargeFanliExportService;
	@Autowired
	private CuilianExportService cuilianExportService;
	@Autowired
	private FushuSkillExportService fushuSkillExportService;
	@Autowired
	private LeiHaoExportService leiHaoExportService;
	@Autowired
	private RoleCaidanExportService roleCaidanExportService;
	@Autowired
	private TaiWanExportService taiWanExportService;
	@Autowired
	private RoleTaFangExportService roleTaFangExportService;
	@Autowired
	private KuafuJingjiExportService kuafuJingjiExportService;
	@Autowired
	private RefbXunBaoExportService refbXunBaoExportService;
	@Autowired
	private TanSuoBaoZangExportService tanSuoBaoZangExportService;
	@Autowired
	private ZhuanshengExportService zhuanshengExportService;
	@Autowired
	private LianyuBossExortService lianyuBossExortService;
	@Autowired
	private YuenanExportService yuenanExportService;
	@Autowired
	private TuanGouExportService tuanGouExportService;
	@Autowired
	private RefabuSevenLoginExportService refabuSevenLoginExportService;
	@Autowired
	private RefbMiaoshaExportService refbMiaoshaExportService;
	@Autowired
	private RefbLaowanjiaExportService refbLaowanjiaExportService;
	@Autowired
	private WuXingMoShenExportService wuXingMoShenExportService;
	@Autowired
	private RefabuFirstChargeRebateExportService refabuFirstChargeRebateExportService;
	@Autowired
	private XinmoExportService xinmoExportService;
    @Autowired
    private RefabuJuebanExportService refabuJuebanExportService;
    @Autowired
    private Huajuan2ExportService huaJuan2ExportService;
    @Autowired
    private RoleMoGongLieYanExportService roleMoGongLieYanExportService;
    @Autowired
    private XianqiServiceExport xianqiServiceExport;
    @Autowired
    private XianqiFubenServiceExport xianqiFubenServiceExport;
    @Autowired
    private XianYuanFeiHuaServiceExport xianYuanFeiHuaServiceExport;
    @Autowired
    private RfbExtremeRechargeExportService rfbExtremeRechargeExportService;
    @Autowired
    private ShenQiJinJieExportService shenQiJinJieExportService;
    @Autowired
    private ShenQiEquipExportService shenQiEquipExportService;
    @Autowired
	private RolePersonalBossExportService rolePersonalBossExportService;
    @Autowired
    private RoleXiangweiExportService roleXiangweiExportService;
    @Autowired
    private RoleBossJifenExportService bossJifenExportService;
    @Autowired
    private WuQiExportService wuQiExportService;
    @Autowired
    private OnceChongExportService onceChongExportService;
    @Autowired
    private TaskBranchService taskBranchService;
    @Autowired
    private XiuLianExportService xiuLianExportService;
    @Autowired
    private ShopLimitExportService shopLimitExportService;
    @Autowired
    private LJExportService lJExportService;
    
	@Override
	public IEntityCache loadEntityCache(Long userRoleId) {
		IEntityCache entityCache = new EntityCache(userRoleId);
		
		//玩家角色
		UserRole userRole = userRoleExportService.initUserRole(userRoleId);
		entityCache.addModelData(userRole, UserRole.class);
		//玩家角色货币信息
		RoleAccount roleAccount = accountExportService.initRoleAccount(userRoleId);
		entityCache.addModelData(roleAccount, RoleAccount.class);
		//角色场景信息
		RoleStage roleStage = roleStageExportService.getRoleStageFromDb(userRoleId);
		entityCache.addModelData(roleStage, RoleStage.class);
		//加载背包数据
		List<RoleItem> roleItems=roleBagExportService.initAll(userRoleId);
		if(roleItems.size() > 0){
			entityCache.addModelData(roleItems, RoleItem.class);
		}
		//加载个人业务数据
		RoleBusinessInfo roleBusinessInfo = roleBusinessInfoExportService.initRoleBusinessInfo(userRoleId);
		entityCache.addModelData(roleBusinessInfo, RoleBusinessInfo.class);
		//加载背包描述信息
 		RoleItemDesc  roleItemDesc = roleBagExportService.initDescAll(userRoleId);
		if(roleItemDesc!=null){
			entityCache.addModelData(roleItemDesc, RoleItemDesc.class);
		}
		//加载每日使用次数限制
		List<RoleItemUseCsxz> roleItemUseCsxzs=goodsUseLimitService.initAll(userRoleId);
		if(roleItemUseCsxzs!=null){
			entityCache.addModelData(roleItemUseCsxzs, RoleItemUseCsxz.class);
		}
		//加载主线任务
		List<Task> task = taskExportService.initTask(userRoleId);
		if(task != null){
			entityCache.addModelData(task, Task.class);
		}
		//加载邮件
		List<EmailRelation> email = emailRelationExportService.initEmailRelation(userRoleId);
		if(email != null){
			entityCache.addModelData(email, EmailRelation.class);
		}
		//技能
		List<RoleSkill> skill = roleSkillExportService.initRoleSkill(userRoleId);
		if(skill != null){
			entityCache.addModelData(skill, RoleSkill.class);
		}
		//糖宝技能格位
		List<RoleSkillGewei> skillGeWei = roleSkillExportService.initRoleSkillGeweis(userRoleId);
		if(skillGeWei != null){
			entityCache.addModelData(skillGeWei, RoleSkillGewei.class);
		}
		//副本
		List<Fuben> fuben = fubenExportService.initFuben(userRoleId);
		if(fuben != null){
			entityCache.addModelData(fuben, Fuben.class);
		}
		
		//五行副本
        List<WuxingFuben> wxFuben = fubenExportService.initWxFuben(userRoleId);
        if(wxFuben != null){
            entityCache.addModelData(wxFuben, WuxingFuben.class);
        }
        
        //五行技能副本
        List<WuxingSkillFuben> wxSkillFuben = fubenExportService.initWxSkillFuben(userRoleId);
        if(wxSkillFuben != null){
            entityCache.addModelData(wxSkillFuben, WuxingSkillFuben.class);
        }
        
        //五行试炼副本
        List<WuxingShilianFuben> wxShilianFuben = fubenExportService.initWxShilianFuben(userRoleId);
        if(wxShilianFuben != null){
            entityCache.addModelData(wxShilianFuben, WuxingShilianFuben.class);
        }
        
		//多人副本
		List<MoreFuben> moreFuben = moreFubenExportService.initFuben(userRoleId);
		if(moreFuben != null){
			entityCache.addModelData(moreFuben, MoreFuben.class);
		}
		//个人竞技
		List<RoleJingjiDuihuan> roleJingjiDuihuan = jingjiExportService.initRoleJingjiDuihuan(userRoleId);
		if(roleJingjiDuihuan != null){
			entityCache.addModelData(roleJingjiDuihuan, RoleJingjiDuihuan.class);
		}
		
		//幻化
		List<RoleHuanhua> huanhuaList = huanhuaExportService.initRoleHuanhua(userRoleId);
		if(huanhuaList != null){
			entityCache.addModelData(huanhuaList, RoleHuanhua.class);
		}
		//坐骑
		ZuoQiInfo zuoqiInfo = zuoQiExportService.initZuoQi(userRoleId);
		if(zuoqiInfo != null){
			entityCache.addModelData(zuoqiInfo, ZuoQiInfo.class);
		}
		
		//翅膀
		ChiBangInfo chibangInfo = chiBangExportService.initChiBang(userRoleId);
		if(chibangInfo != null){
			entityCache.addModelData(chibangInfo, ChiBangInfo.class);
		}
		//器灵
		QiLingInfo qiLingInfo = qilingExportService.initQiLing(userRoleId);
		if(qiLingInfo != null){
			entityCache.addModelData(qiLingInfo, QiLingInfo.class);
		}
		//器灵
		TianYuInfo tianYuInfo = tianyuExportService.initTianYu(userRoleId);
		if(tianYuInfo != null){
			entityCache.addModelData(tianYuInfo, TianYuInfo.class);
		}
		//仙剑
		XianJianInfo xianjianInfo = xianJianExportService.initXianJian(userRoleId);
		if(xianjianInfo != null){
			entityCache.addModelData(xianjianInfo, XianJianInfo.class);
		}
		//日常任务
		List<TaskDay> taskDays = taskDayExportService.initTask(userRoleId);
		if(taskDays != null){
			entityCache.addModelData(taskDays, TaskDay.class);
		}
		//糖宝
		List<Tangbao> tangbao = tangbaoExportService.initTangbao(userRoleId);
		if(tangbao != null){
			entityCache.addModelData(tangbao, Tangbao.class);
		}
		//礼品码
		List<GiftCard> giftCard = giftCardExportService.initGiftCard(userRoleId);
		if(giftCard != null){
			entityCache.addModelData(giftCard, GiftCard.class);
		}
		List<GiftCardPlatform> giftCardPlatform = giftCardExportService.initCardPlatform(userRoleId);
		if(giftCardPlatform != null){
			entityCache.addModelData(giftCardPlatform, GiftCardPlatform.class);
		}
		//首充信息
		List<RefbRoleShouchong> rfbShoucongs = refbRoleShouchongExportService.initRefbRoleShouchong(userRoleId);
		if(rfbShoucongs != null){
			entityCache.addModelData(rfbShoucongs, RefbRoleShouchong.class);
		}
		//超值兑换
		List<RefabuSuperDuihuan> duihuanInfo = superDuihuanExportService.initRefabuSuperDuihuan(userRoleId);
		if(duihuanInfo != null){
			entityCache.addModelData(duihuanInfo, RefabuSuperDuihuan.class);
		}
		//欢乐卡牌
		List<RefabuHappyCard> happyCard = happyCardExportService.initRefabuHappyCard(userRoleId);
		if(happyCard != null){
			entityCache.addModelData(happyCard, RefabuHappyCard.class);
		}
		//欢乐卡牌
		List<RefabuHappyCardItem> happyCardItem = happyCardExportService.initRefabuHappyCardItem(userRoleId);
		if(happyCardItem != null){
			entityCache.addModelData(happyCardItem, RefabuHappyCardItem.class);
		}
		//玄铁兑换
		List<XuantieDuihuan> xuantie = xuanTieDuiHuanExportService.initXuanTieDuiHuan(userRoleId);
		if(xuantie != null){
			entityCache.addModelData(xuantie, XuantieDuihuan.class);
		}
		//热发布修仙礼包
		List<RefbRoleXiuxian> refbXiuXians = rfbXiuXianExportService.initRefbRoleXiuxian(userRoleId);
		if(refbXiuXians != null){
			entityCache.addModelData(refbXiuXians, RefbRoleXiuxian.class);
		}
		
		//等级
		List<QiriLevelLibao> qmxx = kaiFuHuoDongExportService.initLevel(userRoleId);
		if(qmxx != null){
			entityCache.addModelData(qmxx, QiriLevelLibao.class);
		}
		
		//套装象位
		List<RoleXiangwei> roleXiangwei = roleXiangweiExportService.initRoleXiangwei(userRoleId);
		if(roleXiangwei != null){
			entityCache.addModelData(roleXiangwei, RoleXiangwei.class);
		}
		//战力比拼
		List<ZhanliBipin> zlbp = kaiFuHuoDongExportService.initZhanLiBiPin(userRoleId);
		if(zlbp != null){
			entityCache.addModelData(zlbp, ZhanliBipin.class);
		}
		
		//神器
		List<ShenQiInfo> shenqi = shenQiExportService.initShenQiInfo(userRoleId);
		if(shenqi != null){
			entityCache.addModelData(shenqi, ShenQiInfo.class);
		}
		
		//神器
		List<ShenQiEquip> shenqiEquip = shenQiEquipExportService.initShenQiEquip(userRoleId);
		if(shenqiEquip != null){
			entityCache.addModelData(shenqiEquip, ShenQiEquip.class);
		}
		
		//画卷
		List<RoleHuajuan> huajuan = huajuanExportService.initRoleHuajuan(userRoleId);
		if(huajuan != null){
			entityCache.addModelData(huajuan, RoleHuajuan.class);
		}
		List<RoleHuajuanExp> huajuanExp = huajuanExportService.initRoleHuajuanExp(userRoleId);
		if(huajuanExp != null){
			entityCache.addModelData(huajuanExp, RoleHuajuanExp.class);
		}
		
		//淬炼
		List<RoleCuilian> cuilian = cuilianExportService.initRoleCuilian(userRoleId);
		if(cuilian != null){
			entityCache.addModelData(cuilian, RoleCuilian.class);
		}
		//跨服个人竞技
		List<RoleKuafuArena1v1> roleKuafuArena1v1 = kuafuArena1v1ExportService.initRoleKuafuArena1v1(userRoleId);
		if(roleKuafuArena1v1 != null){
			entityCache.addModelData(roleKuafuArena1v1, RoleKuafuArena1v1.class);
		}
		//跨服4v4
		List<RoleKuafuArena4v4> roleKuafuArena4v4 = kuafuArena4v4ExportService.initRoleKuafuArena4v4(userRoleId);
		if(roleKuafuArena4v4 != null){
			entityCache.addModelData(roleKuafuArena4v4, RoleKuafuArena4v4.class);
		}
		//八卦阵
		List<RoleBagua> roleBaguaList = baguaExportService.initRoleBagua(userRoleId);
		if(roleBaguaList != null){
			entityCache.addModelData(roleBaguaList, RoleBagua.class);
		}
		//埋骨之地
		List<RoleMaigu> roleMaiguList = maiguExportService.initRoleMaigu(userRoleId);
		if(roleBaguaList != null){
			entityCache.addModelData(roleMaiguList, RoleMaigu.class);
		}
		
		List<RoleGongxunDuihuanInfo> roleGongxunDuihuanInfoList = kuafuArena1v1ExportService.initRoleGongxunDuihuanInfo(userRoleId);
		if(roleGongxunDuihuanInfoList != null){
			entityCache.addModelData(roleGongxunDuihuanInfoList, RoleGongxunDuihuanInfo.class);
		}
		
		//押镖
		Yabiao yabiao = yabiaoExportService.initYabiao(userRoleId);
		if(yabiao != null){
			entityCache.addModelData(yabiao, Yabiao.class);
		}
		//温泉
		RoleWenquan roleWenquan = wenquanExportService.initRoleWenquan(userRoleId);
		if(roleWenquan != null){
			entityCache.addModelData(roleWenquan, RoleWenquan.class);
		}
		//妖神
		RoleYaoshen roleYaoshen = yaoshenExportService.initRoleYaoshen(userRoleId);
		if(roleYaoshen != null){
			entityCache.addModelData(roleYaoshen, RoleYaoshen.class);
		}
		//成神
		RoleChengShen roleChengShen = chengShenExportService.initRoleChengShen(userRoleId);
		if(roleChengShen != null){
			entityCache.addModelData(roleChengShen, RoleChengShen.class);
		}

		//妖神--魂魄
		RoleYaoshenHunpo roleYaoshenHunpo = yaoshenHunpoExportService.initRoleYaoshenHunpo(userRoleId);
		if(roleYaoshenHunpo != null){
			entityCache.addModelData(roleYaoshenHunpo, RoleYaoshenHunpo.class);
		}
		//妖神--魔印
		RoleYaoshenMoyin roleYaoshenMoyin = yaoshenMoYinExportService.initRoleYaoshenMoYin(userRoleId);
		if(roleYaoshenMoyin != null){
			entityCache.addModelData(roleYaoshenMoyin, RoleYaoshenMoyin.class);
		}
		 
		//糖宝心纹
		RoleTangbaoXinwen roleTangbaoXinwen = xinwenExportService.initRoleYaoshenMowen(userRoleId);
		if(roleTangbaoXinwen != null){
			entityCache.addModelData(roleTangbaoXinwen, RoleTangbaoXinwen.class);
		}
		//妖神附魔
		RoleYaoshenFumo roleYaoshenFumo = yaoshenFumoExportService.initRoleYaoshenMoYin(userRoleId);
		if(roleYaoshenFumo != null){
			entityCache.addModelData(roleYaoshenFumo, RoleYaoshenFumo.class);
		}
		//通天之路
		RoleTongtianRoad roleTongtianLoad = tongtianLoadExportService.initData(userRoleId);
		if(roleTongtianLoad!=null){
			entityCache.addModelData(roleTongtianLoad, RoleTongtianRoad.class);
		}
//		//促销奖励  策划说不要了
//		List<RoleCuxiao> roleCuxiaos = cuxiaoExportService.initRoleCuxiao(userRoleId);
//		if(roleCuxiaos!=null){
//			entityCache.addModelData(roleCuxiaos, RoleCuxiao.class);
//		}
		//至尊充值
		List<RfbExtremeRecharge> roleExtremeRecharge= rfbExtremeRechargeExportService.initRfbExtremeRecharge(userRoleId);
		if(roleExtremeRecharge!=null){
			entityCache.addModelData(roleExtremeRecharge, RfbExtremeRecharge.class);
		}
		//妖神魔纹
		RoleYaoshenMowen roleYaoshenMowen = yaoshenExportService.initRoleYaoshenMowen(userRoleId);
		if(roleYaoshenMowen != null){
			entityCache.addModelData(roleYaoshenMowen, RoleYaoshenMowen.class);
		}
		//称号
		List<RoleChenghao> roleChenghao = chenghaoExportService.initRoleChenghao(userRoleId);
		if(roleChenghao != null){
			entityCache.addModelData(roleChenghao, RoleChenghao.class);
		}
		
		//宠物
		List<RoleChongwu> chongwu = chongwuExportService.initRoleChongwu(userRoleId);
		if(chongwu != null){
			entityCache.addModelData(chongwu, RoleChongwu.class);
		}
		//宠物技能
		List<RoleChongwuSkill> chongwuSkill = chongwuExportService.initRoleChongwuSkill(userRoleId);
		if(chongwuSkill != null){
		    entityCache.addModelData(chongwuSkill, RoleChongwuSkill.class);
		}
		//棋盘
		List<Qipan> qipan = qiPanExportService.initQiPan(userRoleId);
		if(qipan != null){
			entityCache.addModelData(qipan, Qipan.class);
		}
		//累充
		List<Leichong> leiChong = leiChongExportService.initLeiChong(userRoleId);
		if(leiChong != null){
			entityCache.addModelData(leiChong, Leichong.class);
		}
		//星空宝藏
		List<RefabuXkbz> xkbz = xkbzExportService.initRefabuXkbz(userRoleId);
		if(xkbz != null){
			entityCache.addModelData(xkbz, RefabuXkbz.class);
		}
		//老玩家回归
		List<RefbLaowanjia> laowanjia = refbLaowanjiaExportService.initRefbLaowanjias(userRoleId);
		if(laowanjia != null){
			entityCache.addModelData(laowanjia, RefbLaowanjia.class);
		}
		//连充
		List<RoleLianchong> rolelianchongs = lianChongExportService.initLeiChong(userRoleId);
		if(rolelianchongs != null){
			entityCache.addModelData(rolelianchongs, RoleLianchong.class);
		}
		//热发布在线奖励
		List<RoleOnlineRewards> roleOnlineRewards = onlineRewardsExportService.initOnlineRewards(userRoleId);
		if(roleOnlineRewards != null){
			entityCache.addModelData(roleOnlineRewards, RoleOnlineRewards.class);
		}
		//宝石
		List<RoleJewel> roleJewels  =  jewelExportService.initJewel(userRoleId);
		if(roleJewels != null){
			entityCache.addModelData(roleJewels, RoleJewel.class);
		}
		//送花
		List<RoleSendFlower> roleSendFlowers  = flowerSendExportService.initData(userRoleId);
		if(roleSendFlowers != null){
			entityCache.addModelData(roleSendFlowers, RoleSendFlower.class);
		}
		//鲜花魅力榜
		List<RoleRfbFlower> roleRfbFlowers  = flowerCharmRankExportService.initRoleRfbFlower(userRoleId);
		if(roleRfbFlowers != null){
			entityCache.addModelData(roleRfbFlowers, RoleRfbFlower.class);
		}
		//神秘商店
		List<ShenmiShangdian> shangdian = shenMiShangDianExportService.initShenmiShangdian(userRoleId);
		if(shangdian != null){
			entityCache.addModelData(shangdian, ShenmiShangdian.class);
		}
		//消费排名
		List<RefabuXiaofei> xiaofei = refabuXiaoFeiService.initRefabuXiaofei(userRoleId);
		if(xiaofei != null){
			entityCache.addModelData(xiaofei, RefabuXiaofei.class);
		}
		//投资计划
		List<RoleTouzi> roleTouzis = roleTouziExportService.initRoleTouzi(userRoleId);
		if(roleTouzis != null){
			entityCache.addModelData(roleTouzis, RoleTouzi.class);
		}
		List<RoleLinghuoInfo> linghuo = lingHuoExportService.initRoleLinghuoInfo(userRoleId);
		if(linghuo != null){
			entityCache.addModelData(linghuo, RoleLinghuoInfo.class);
		}
		ZhanJiaInfo zhanjia = zhanJiaExportService.initXianJian(userRoleId);
		if(zhanjia != null){
			entityCache.addModelData(zhanjia, ZhanJiaInfo.class);
		}
		List<RefabuZhuanpan> zhuanpan = zhuanPanExportService.initRefabuZhuanpan(userRoleId);
		if(zhuanpan != null){
			entityCache.addModelData(zhuanpan, RefabuZhuanpan.class);
		}
		List<RefabuLunpan> lunpan = lunPanExportService.initRefabuLunpan(userRoleId);
		if(lunpan != null){
		    entityCache.addModelData(lunpan, RefabuLunpan.class);
		}
		List<TerritoryDayReward> territoryDayRewards = territoryExportService.initDayReward(userRoleId);
		if(territoryDayRewards != null){
			entityCache.addModelData(territoryDayRewards, TerritoryDayReward.class);
		}
		//修炼任务
		List<RoleXiulianJifen> roleXiulianJifens = xiuLianExportService.initRoleXiulianJifens(userRoleId);
		if(roleXiulianJifens != null){
			entityCache.addModelData(roleXiulianJifens, RoleXiulianJifen.class);
		}
		List<RoleXiulianTask> roleXiulianTasks = xiuLianExportService.initRoleXiulianTasks(userRoleId);
		if(roleXiulianTasks != null){
			entityCache.addModelData(roleXiulianTasks, RoleXiulianTask.class);
		}
		//成就
		List<RoleChengjiu> chengjius = roleChengJiuExportService.initChengjiu(userRoleId);
		if(chengjius != null){
			entityCache.addModelData(chengjius, RoleChengjiu.class);
		}
		List<RoleChengjiuData> chengjiuDatas = roleChengJiuExportService.initRoleChengjiuData(userRoleId);
		if(chengjiuDatas != null){
			entityCache.addModelData(chengjiuDatas, RoleChengjiuData.class);
		}
		if(PlatformConstants.isQQ()){
			RolePlatformQqHz rolePlatformQqHz = qqExportService.initRolePlatformQqHz(userRoleId);
			if(rolePlatformQqHz != null){
				entityCache.addModelData(rolePlatformQqHz, RolePlatformQqHz.class);
			}
			RolePlatformQqLz rolePlatformQqLz = qqExportService.initRolePlatformQqLz(userRoleId);
			if(rolePlatformQqLz != null){
				entityCache.addModelData(rolePlatformQqLz, RolePlatformQqLz.class);
			}
			List<Renwujishi> jishi = renWuJiShiExportService.initAllRenWuJiShi(userRoleId);
			if(jishi != null){
				entityCache.addModelData(jishi, Renwujishi.class);
			}
			List<TencentWeiduan> weiduan = qqExportService.iniTencentWeiduan(userRoleId);
			if(weiduan != null){
				entityCache.addModelData(weiduan, TencentWeiduan.class);
			}
			List<RoleQqTgp> qqtgp = qqExportService.initRoleQqTgps(userRoleId);
			if(qqtgp != null){
				entityCache.addModelData(qqtgp, RoleQqTgp.class);
			}
			List<RoleQdianZhigou> qDian = qqExportService.initRoleQdianZhigous(userRoleId);
			if(qDian != null){
				entityCache.addModelData(qDian, RoleQdianZhigou.class);
			}
			//腾讯用户绑定数据
			List<TencentUserInfo> tUser = tencentLuoPanExportService.initTencentUserInfos(userRoleId);
			if(tUser != null){
				entityCache.addModelData(tUser, TencentUserInfo.class);
			}
			List<TencentShangdian> tsd = tencentShangDianExportService.initTencentShangdians(userRoleId);
			if(tsd != null){
				entityCache.addModelData(tsd, TencentShangdian.class);
			}
			List<TencentLanzuan> tlz = qqExportService.initTencentLanzuans(userRoleId);
			if(tlz != null){
				entityCache.addModelData(tlz, TencentLanzuan.class);
			}
		}
		if(PlatformConstants.isTaiWan()){
			List<TencentUserInfo> tUser = taiWanExportService.initTencentUserInfos(userRoleId);
			if(tUser != null){
				entityCache.addModelData(tUser, TencentUserInfo.class);
			}
		}
		if(PlatformConstants.isYueNan()){
			List<YuenanYaoqing> yn = yuenanExportService.initYaoqings(userRoleId);
			if(yn != null){
				entityCache.addModelData(yn, YuenanYaoqing.class);
			}
		}
		List<RoleYuanbaoRecord> rYuanbaoRecords = refabuActivityExportService.initRoleYuanbaoRecords(userRoleId);
		if(rYuanbaoRecords != null){
			entityCache.addModelData(rYuanbaoRecords, RoleYuanbaoRecord.class);
		}
		List<RefbSuoyaota> refbSuoyaotas = suoYaoTaExportService.initRefbSuoyaota(userRoleId);
		if(refbSuoyaotas != null){
			entityCache.addModelData(refbSuoyaotas, RefbSuoyaota.class);
		}
		//时装
		List<RoleShizhuang> roleShizhuangs = roleShiZhuangExportService.initRoleShizhuang(userRoleId);
		if(roleShizhuangs != null){
			entityCache.addModelData(roleShizhuangs, RoleShizhuang.class);
		}
		//时装进阶
		List<RoleShiZhuangJinJie> roleShizhuangJinJie = roleShiZhuangExportService.initRoleShizhuangJinJie(userRoleId);
		if(roleShizhuangJinJie != null){
			entityCache.addModelData(roleShizhuangJinJie, RoleShiZhuangJinJie.class);
		}
		//婚姻
		List<RoleMarryInfo> roleMarryInfos = marryExportService.initRoleMarryInfo(userRoleId);
		if(roleMarryInfos != null){
			entityCache.addModelData(roleMarryInfos, RoleMarryInfo.class);
		}
		//充值返利
		List<RefabuRefanli> refabuRefanlis = rechargeFanliExportService.initRefabuRefanli(userRoleId);
		if(refabuRefanlis != null){
			entityCache.addModelData(refabuRefanlis, RefabuRefanli.class);
		}
		//累计消耗
		List<RefabuLeihao> leihao = leiHaoExportService.initRefabuLeihao(userRoleId);
		if(leihao != null){
			entityCache.addModelData(leihao, RefabuLeihao.class);
		}
		//附属技能
		List<FushuSkill> fushuSkills = fushuSkillExportService.initFushuSkill(userRoleId);
		if(fushuSkills != null){
			entityCache.addModelData(fushuSkills, FushuSkill.class);
		}
		//幸运彩蛋
		List<RefbCaidan> refbCaidans = roleCaidanExportService.initRefbCaidan(userRoleId);
		if(refbCaidans != null){
			entityCache.addModelData(refbCaidans, RefbCaidan.class);
		}
		//塔防
		List<RoleTafang> roleTafangs = roleTaFangExportService.initRoleTafang(userRoleId);
		if(roleTafangs != null){
			entityCache.addModelData(roleTafangs, RoleTafang.class);
		}
		//封神之战
		List<KuafuJingji> kuafuJingjis = kuafuJingjiExportService.initKuafuJingji(userRoleId);
		if(kuafuJingjis != null){
			entityCache.addModelData(kuafuJingjis, KuafuJingji.class);
		}
		//热发布寻宝
		List<RefbXunbao> refbXunbao = refbXunBaoExportService.initRefbXunbao(userRoleId);
		if(refbXunbao != null){
			entityCache.addModelData(refbXunbao, RefbXunbao.class);
		}
		//热发布探宝
		List<RefabuTanbao> refbTunbao = tanSuoBaoZangExportService.initRefabuTanbao(userRoleId);
		if(refbTunbao != null){
			entityCache.addModelData(refbTunbao, RefabuTanbao.class);
		}
		//转生
		List<Zhuansheng> zhuanshengs = zhuanshengExportService.initZhuansheng(userRoleId);
		if(zhuanshengs != null){
			entityCache.addModelData(zhuanshengs, Zhuansheng.class);
		}
		//门派boss个人数据
		List<GuildBossLianyu> guildBossLianyus = lianyuBossExortService.initGuildBossLianyu(userRoleId);
		if(guildBossLianyus != null){
			entityCache.addModelData(guildBossLianyus, GuildBossLianyu.class);
		}
		//团购
		List<RefbRoleTuangou> refbRoleTuangous = tuanGouExportService.initRefbRoleTuangou(userRoleId);
		if(refbRoleTuangous != null){
			entityCache.addModelData(refbRoleTuangous, RefbRoleTuangou.class);
		}
		//热发布登陆
		List<RefabuSevenLogin> refabuSevenLogins = refabuSevenLoginExportService.initRefabuSevenLogin(userRoleId);
		if(refabuSevenLogins != null){
			entityCache.addModelData(refabuSevenLogins, RefabuSevenLogin.class);
		}
		//团购秒杀
		List<RefbMiaosha> refbMiaoshas = refbMiaoshaExportService.initRefbMiaosha(userRoleId);
		if(refbMiaoshas != null){
			entityCache.addModelData(refbMiaoshas, RefbMiaosha.class);
		}
		//五行
		List<RoleWuxing> roleWuxing = wuXingMoShenExportService.initRoleWuxing(userRoleId);
		if(roleWuxing != null){
			entityCache.addModelData(roleWuxing, RoleWuxing.class);
		}
		//五行附体
		List<RoleWuxingFuti> roleWuxingFuti = wuXingMoShenExportService.initRoleWuxingFutis(userRoleId);
		if(roleWuxingFuti != null){
			entityCache.addModelData(roleWuxingFuti, RoleWuxingFuti.class);
		}
		//五行技能
		List<RoleWuxingSkill> roleWuxingSkill = wuXingMoShenExportService.initRoleWuxingSkills(userRoleId);
		if(roleWuxingSkill != null){
		    entityCache.addModelData(roleWuxingSkill, RoleWuxingSkill.class);
		}
		//五行精魄
		List<RoleWuxingJingpo> roleWuxingJingpo = wuXingMoShenExportService.initRoleBodyWxJpList(userRoleId);
		if(roleWuxingJingpo != null){
		    entityCache.addModelData(roleWuxingJingpo, RoleWuxingJingpo.class);
		}
		//五行精魄背包
		List<RoleWuxingJingpoItem> roleWuxingJingpoItem = wuXingMoShenExportService.initRoleWxJpItemList(userRoleId);
		if(roleWuxingJingpoItem != null){
		    entityCache.addModelData(roleWuxingJingpoItem, RoleWuxingJingpoItem.class);
		}
	    //热发布活动:首冲返利
        List<RefabuFirstChargeRebate> refabuFitstRebates = refabuFirstChargeRebateExportService.initRefabuFirstChargeRebate(userRoleId);
        if(refabuFitstRebates != null){
            entityCache.addModelData(refabuFitstRebates, RefabuFirstChargeRebate.class);
        }
        //糖宝五行
        List<TangbaoWuxing> tangbaoWuxing = wuXingMoShenExportService.initTangbaoWuxing(userRoleId);
        if(tangbaoWuxing != null){
            entityCache.addModelData(tangbaoWuxing, TangbaoWuxing.class);
        }
        //糖宝五行技能
        List<TangbaoWuxingSkill> tangbaoWuxingSkill = wuXingMoShenExportService.initTbWuxingSkills(userRoleId);
        if(tangbaoWuxingSkill != null){
            entityCache.addModelData(tangbaoWuxingSkill, TangbaoWuxingSkill.class);
        }
        //心魔
        List<RoleXinmo> roleXinmoList = xinmoExportService.initRoleXinmo(userRoleId);
        if(roleXinmoList != null){
            entityCache.addModelData(roleXinmoList, RoleXinmo.class);
        }
        //心魔:天炉炼丹数据
        List<RoleXinmoLiandan> roleXinmoLiandanList = xinmoExportService.initRoleXinmoLiandan(userRoleId);
        if(roleXinmoLiandanList != null){
            entityCache.addModelData(roleXinmoLiandanList, RoleXinmoLiandan.class);
        }
        //心魔:天炉炼丹仓库数据
        List<RoleXinmoLiandanItem> roleXinmoLiandanItemList = xinmoExportService.initRoleXinmoLiandanItem(userRoleId);
        if(roleXinmoLiandanItemList != null){
            entityCache.addModelData(roleXinmoLiandanItemList, RoleXinmoLiandanItem.class);
        }
        //心魔:魔神数据
        List<RoleXinmoMoshen> roleXinmoMoshenList = xinmoExportService.initRoleXinmoMoshen(userRoleId);
        if(roleXinmoMoshenList != null){
            entityCache.addModelData(roleXinmoMoshenList, RoleXinmoMoshen.class);
        }
        //心魔:魔神噬体数据
        List<RoleXinmoMoshenShiti> roleXinmoMoshenShitiList = xinmoExportService.initRoleXinmoMoshenShiti(userRoleId);
        if(roleXinmoMoshenShitiList != null){
            entityCache.addModelData(roleXinmoMoshenShitiList, RoleXinmoMoshenShiti.class);
        }
        //心魔:魔神副本
        List<XinmoFuben> xinmoFuben = fubenExportService.initXinmoFuben(userRoleId);
        if(xinmoFuben != null){
            entityCache.addModelData(xinmoFuben, XinmoFuben.class);
        }
        //心魔:魔神技能数据
        List<RoleXinmoSkill> roleXinmoSkillList = xinmoExportService.initRoleXinmoSkill(userRoleId);
        if(roleXinmoSkillList != null){
            entityCache.addModelData(roleXinmoSkillList, RoleXinmoSkill.class);
        }
        //热发布活动:绝版礼包
        List<RefabuJueban> refabuJueban = refabuJuebanExportService.initRefabuJueban(userRoleId);
        if(refabuJueban != null){
            entityCache.addModelData(refabuJueban, RefabuJueban.class);
        }
        //心魔:深渊副本
        List<XinmoShenyuanFuben> xinmoShenyuanFuben = fubenExportService.initXmShenyuanFuben(userRoleId);
        if(xinmoShenyuanFuben != null){
            entityCache.addModelData(xinmoShenyuanFuben, XinmoShenyuanFuben.class);
        }
        //心魔:洗练数据
        List<RoleXinmoXilian>  roleXinmoXilianList = xinmoExportService.initRoleXinmoXilian(userRoleId);
        if(roleXinmoXilianList != null){
            entityCache.addModelData(roleXinmoXilianList, RoleXinmoXilian.class);
        }
        
        //心魔:斗场副本
        List<XinmoDouchangFuben> xmDouchangFuben = fubenExportService.initXmDouchangFuben(userRoleId);
        if(xmDouchangFuben != null){
            entityCache.addModelData(xmDouchangFuben, XinmoDouchangFuben.class);
        }
        //画卷2
        List<RoleHuajuan2> roleHuaJuan2Data = huaJuan2ExportService.initRoleHuajuan2(userRoleId);
        if(roleHuaJuan2Data != null){
            entityCache.addModelData(roleHuaJuan2Data, RoleHuajuan2.class);
        }
        List<RoleHuajuan2Exp> roleHuaJuan2ExpData = huaJuan2ExportService.initRoleHuajuan2Exp(userRoleId);
        if(roleHuaJuan2ExpData != null){
            entityCache.addModelData(roleHuaJuan2ExpData, RoleHuajuan2Exp.class);
        }
        //灵火祝福
        List<RoleLinghuoBless> linghuoBless = lingHuoExportService.initRoleLinghuoBlessList(userRoleId);
        if(linghuoBless != null){
            entityCache.addModelData(linghuoBless, RoleLinghuoBless.class);
        }
      //个人boss
  		List<RolePersonalBoss> personalBossList = rolePersonalBossExportService.initRolePersonalBoss(userRoleId);
  		if(personalBossList!=null) {
  			entityCache.addModelData(personalBossList, RolePersonalBoss.class);
  		}
  		// boss积分
  		List<RoleBossJifen> roleBossJifenList = bossJifenExportService.initRoleBossJifen(userRoleId);
  		if(roleBossJifenList!=null) {
  			entityCache.addModelData(roleBossJifenList, RoleBossJifen.class);
  		}
        //灵火祝福:魔宫猎焰
        List<RoleMogonglieyan> roleMglyList = roleMoGongLieYanExportService.initRoleMglyList(userRoleId);
        if(roleMglyList != null){
            entityCache.addModelData(roleMglyList, RoleMogonglieyan.class);
        }
        // 仙器
        List<RoleXianqi> roleXianqiList = xianqiServiceExport.initRoleXianqi(userRoleId);
        if(roleXianqiList != null){
            entityCache.addModelData(roleXianqiList, RoleXianqi.class);
        }
        // 仙器觉醒
        List<RoleXianqiJuexing> roleXianqiJuexingList = xianqiServiceExport.initRoleXianqiJuexing(userRoleId);
        if(roleXianqiJuexingList != null){
            entityCache.addModelData(roleXianqiJuexingList, RoleXianqiJuexing.class);
        }
        // 仙器副本(云瑶晶脉)
        List<XianqiFuben> xianqiFubenList = xianqiFubenServiceExport.initXianqiFubenData(userRoleId);
        if(xianqiFubenList != null){
            entityCache.addModelData(xianqiFubenList, XianqiFuben.class);
        }
        //仙缘飞化
        List<RoleXianyuanFeihua>  xuanyuanList=xianYuanFeiHuaServiceExport.initRoleXianyuanFeihua(userRoleId);
        if(xuanyuanList !=null){
        	entityCache.addModelData(xuanyuanList, RoleXianyuanFeihua.class);
        }
        //神器升阶
        List<ShenQiJinjie>  shenQiJinJieList=shenQiJinJieExportService.initShenQiJinJie(userRoleId);
        if(shenQiJinJieList !=null){
        	entityCache.addModelData(shenQiJinJieList, ShenQiJinjie.class);
        }
        
        //新圣剑进阶
		WuQiInfo wuQiInfo = wuQiExportService.initZuoQi(userRoleId);
		if(wuQiInfo != null){
			entityCache.addModelData(wuQiInfo, WuQiInfo.class);
		}
		
		//单笔充值
		List<RoleOncechong> roleOncechongs = onceChongExportService.initOnceChong(userRoleId);
		if(roleOncechongs != null){
			entityCache.addModelData(roleOncechongs, RoleOncechong.class);
		}
		
		//支线任务
		List<TaskBranch> taskBranch = taskBranchService.initAll(userRoleId);
		if(taskBranch != null){
			entityCache.addModelData(taskBranch, TaskBranch.class);
		}
		
		//限时礼包
		ShopLimitInfo shopLimitInfo = shopLimitExportService.initLimitShopInfo(userRoleId);
		if(shopLimitInfo != null){
			entityCache.addModelData(shopLimitInfo, ShopLimitInfo.class);
		}
		
		//炼金
		RoleLj roleLj = lJExportService.initRoleLJ(userRoleId);
		if(roleLj != null){
			entityCache.addModelData(roleLj, RoleLj.class);
		}
		return entityCache;
	}

}
