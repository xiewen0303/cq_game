package com.junyou.stage.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.boss_jifen.service.export.RoleBossJifenExportService;
import com.junyou.bus.chengjiu.export.RoleChengJiuExportService;
import com.junyou.bus.chibang.export.ChiBangExportService;
import com.junyou.bus.equip.export.RoleXiangweiExportService;
import com.junyou.bus.fuben.export.FubenExportService;
import com.junyou.bus.huajuan.service.export.HuajuanExportService;
import com.junyou.bus.huajuan2.export.Huajuan2ExportService;
import com.junyou.bus.huanhua.service.export.HuanhuaExportService;
import com.junyou.bus.linghuo.export.LingHuoExportService;
import com.junyou.bus.qiling.export.QiLingExportService;
import com.junyou.bus.shenqi.export.ShenQiJinJieExportService;
import com.junyou.bus.tianyu.export.TianYuExportService;
import com.junyou.bus.wuqi.export.WuQiExportService;
import com.junyou.bus.wuxing.export.WuXingMoShenExportService;
import com.junyou.bus.xianjian.export.XianJianExportService;
import com.junyou.bus.xianqi.export.XianYuanFeiHuaServiceExport;
import com.junyou.bus.xianqi.export.XianqiFubenServiceExport;
import com.junyou.bus.xianqi.export.XianqiServiceExport;
import com.junyou.bus.xinmo.export.XinmoExportService;
import com.junyou.bus.xinwen.export.XinwenExportService;
import com.junyou.bus.zhanjia.export.ZhanJiaExportService;
import com.junyou.bus.zuoqi.export.ZuoQiExportService;
import com.junyou.stage.configure.export.impl.RoleBehaviourExportService;
import com.junyou.stage.export.TeamExportService;

@Component
public class StageHelper {
    private static TeamExportService teamExportService;
    private static RoleBehaviourExportService roleBExportService;
    private static ZuoQiExportService zuoQiExportService;
    private static RoleBagExportService roleBagExportService;
    private static ChiBangExportService chiBangExportService;
    private static QiLingExportService qiLingExportService;
    private static TianYuExportService tianYuExportService;
    private static XianJianExportService xianJianExportService;
    private static ZhanJiaExportService zhanJiaExportService;
    private static LingHuoExportService lingHuoExportService;
    private static RoleChengJiuExportService roleChengJiuExportService;
    private static XinwenExportService xinwenExportService;
    private static HuajuanExportService huajuanExportService;
    private static HuanhuaExportService huanhuaExportService;
    private static WuXingMoShenExportService wuXingMoShenExportService;
    private static FubenExportService fubenExportService;
    private static XinmoExportService xinmoExportService;
    private static Huajuan2ExportService huajuan2ExportService;
    private static XianqiServiceExport xianqiServiceExport;
    private static XianqiFubenServiceExport xianqiFubenServiceExport;
    private static XianYuanFeiHuaServiceExport xianYuanFeiHuaServiceExport;
    private static ShenQiJinJieExportService shenQiJinJieExportService;
    private static RoleXiangweiExportService roleXiangweiExportService;
    private static RoleBossJifenExportService roleBossJifenExportService;
    private static WuQiExportService wuQiExportService;
    
    
    @Autowired
	public  void setWuQiExportService(WuQiExportService wuQiExportService) {
		StageHelper.wuQiExportService = wuQiExportService;
	}
    @Autowired
	public  void setRoleBossJifenExportService(RoleBossJifenExportService roleBossJifenExportService) {
		StageHelper.roleBossJifenExportService = roleBossJifenExportService;
	}

	@Autowired
	public  void setXianYuanFeiHuaServiceExport(XianYuanFeiHuaServiceExport xianYuanFeiHuaServiceExport) {
		StageHelper.xianYuanFeiHuaServiceExport = xianYuanFeiHuaServiceExport;
	}

	@Autowired
    public void setHuanhuaExportService(HuanhuaExportService huanhuaExportService) {
        StageHelper.huanhuaExportService = huanhuaExportService;
    }

    @Autowired
    public void setWuXingMoShenExportService(WuXingMoShenExportService wuXingMoShenExportService) {
        StageHelper.wuXingMoShenExportService = wuXingMoShenExportService;
    }

    @Autowired
    public void setHuajuanExportService(HuajuanExportService huajuanExportService) {
        StageHelper.huajuanExportService = huajuanExportService;
    }

    @Autowired
    public void setHuajuan2ExportService(Huajuan2ExportService huajuan2ExportService) {
        StageHelper.huajuan2ExportService = huajuan2ExportService;
    }

    @Autowired
    public void setXinwenExportService(XinwenExportService xinwenExportService) {
        StageHelper.xinwenExportService = xinwenExportService;
    }

    @Autowired
    public void setRoleBagExportService(RoleBagExportService roleBagExportService) {
        StageHelper.roleBagExportService = roleBagExportService;
    }

    @Autowired
    public void setZuoQiExportService(ZuoQiExportService zuoQiExportService) {
        StageHelper.zuoQiExportService = zuoQiExportService;
    }

    @Autowired
    public void setRoleBExportService(RoleBehaviourExportService roleBExportService) {
        StageHelper.roleBExportService = roleBExportService;
    }

    @Autowired
    public void setTeamExportService(TeamExportService teamExportService) {
        StageHelper.teamExportService = teamExportService;
    }

    @Autowired
    public void setChiBangExportService(ChiBangExportService chiBangExportService) {
        StageHelper.chiBangExportService = chiBangExportService;
    }

    @Autowired
    public void setQiLingExportService(QiLingExportService qiLingExportService) {
        StageHelper.qiLingExportService = qiLingExportService;
    }

    @Autowired
    public void setTianYuExportService(TianYuExportService tianYuExportService) {
        StageHelper.tianYuExportService = tianYuExportService;
    }

    @Autowired
    public void setXianJianExportService(XianJianExportService xianJianExportService) {
        StageHelper.xianJianExportService = xianJianExportService;
    }

    @Autowired
    public void setZhanJiaExportService(ZhanJiaExportService zhanJiaExportService) {
        StageHelper.zhanJiaExportService = zhanJiaExportService;
    }

    @Autowired
    public void setLingHuoExportService(LingHuoExportService lingHuoExportService) {
        StageHelper.lingHuoExportService = lingHuoExportService;
    }

    @Autowired
    public void setRoleChengJiuExportService(RoleChengJiuExportService roleChengJiuExportService) {
        StageHelper.roleChengJiuExportService = roleChengJiuExportService;
    }

    @Autowired
    public void setFubenExportService(FubenExportService fubenExportService) {
        StageHelper.fubenExportService = fubenExportService;
    }

    @Autowired
    public void setXinmoExportService(XinmoExportService xinmoExportService) {
        StageHelper.xinmoExportService = xinmoExportService;
    }
    
    @Autowired
    public void setXianqiServiceExport(XianqiServiceExport xianqiServiceExport) {
        StageHelper.xianqiServiceExport = xianqiServiceExport;
    }
    
    @Autowired
    public void setXianqiFubenServiceExport(XianqiFubenServiceExport xianqiFubenServiceExport) {
        StageHelper.xianqiFubenServiceExport = xianqiFubenServiceExport;
    }
    @Autowired
    public void setshenQiJinJieExportService(ShenQiJinJieExportService shenQiJinJieExportService) {
    	StageHelper.shenQiJinJieExportService = shenQiJinJieExportService;
    }
    
    @Autowired
	public void setRoleXiangweiExportService(RoleXiangweiExportService roleXiangweiExportService){
    	StageHelper. roleXiangweiExportService = roleXiangweiExportService;
	}

    //**************************************对外提供静态访问方法**************************************************//
    public static HuanhuaExportService getHuanhuaExportService() {
        return huanhuaExportService;
    }

    public static WuXingMoShenExportService getWuXingMoShenExportService() {
        return wuXingMoShenExportService;
    }

    public static HuajuanExportService getHuajuanExportService() {
        return huajuanExportService;
    }
    public static Huajuan2ExportService getHuajuan2ExportService() {
        return huajuan2ExportService;
    }

    public static RoleBagExportService getRoleBagExportService() {
        return roleBagExportService;
    }

    public static XinwenExportService getXinwenExportService() {
        return xinwenExportService;
    }

    public static ZuoQiExportService getZuoQiExportService() {
        return zuoQiExportService;
    }
    
    public static WuQiExportService getWuQiExportService() {
        return wuQiExportService;
    }

    public static RoleBehaviourExportService getRoleBExportService() {
        return roleBExportService;
    }

    public static TeamExportService getTeamExportService() {
        return teamExportService;
    }

    public static ChiBangExportService getChiBangExportService() {
        return chiBangExportService;
    }

    public static QiLingExportService getQiLingExportService() {
        return qiLingExportService;
    }

    public static TianYuExportService getTianYuExportService() {
        return tianYuExportService;
    }

    public static XianJianExportService getXianJianExportService() {
        return xianJianExportService;
    }

    public static ZhanJiaExportService getZhanJiaExportService() {
        return zhanJiaExportService;
    }

    public static LingHuoExportService getLingHuoExportService() {
        return lingHuoExportService;
    }

    public static RoleChengJiuExportService getRoleChengJiuExportService() {
        return roleChengJiuExportService;
    }

    public static FubenExportService getFubenExportService() {
        return fubenExportService;
    }

    public static XinmoExportService getXinmoExportService() {
        return xinmoExportService;
    }

    public static XianqiServiceExport getXianqiServiceExport() {
        return xianqiServiceExport;
    }

    public static XianqiFubenServiceExport getXianqiFubenServiceExport() {
        return xianqiFubenServiceExport;
    }
    public static XianYuanFeiHuaServiceExport getXianYuanFeiHuaServiceExport() {
		return xianYuanFeiHuaServiceExport;
	}
    
	public static ShenQiJinJieExportService getShenQiJinJieExportService() {
		return shenQiJinJieExportService;
	}
	
	public static RoleXiangweiExportService getRoleXiangweiExportService(){
		return roleXiangweiExportService;
	}
	
    public static RoleBossJifenExportService getRoleBossJifenExportService() {
		return roleBossJifenExportService;
	}
    
}
