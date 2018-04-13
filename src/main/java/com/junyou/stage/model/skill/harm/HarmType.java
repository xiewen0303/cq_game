package com.junyou.stage.model.skill.harm;


/**
 * 
 * @description 伤害类型
 *
 * @author LiuJuan
 * @created 2011-12-14 上午11:22:24
 */
public enum HarmType {
    /**
     * 普通掩码
     */
    PUTONG(0),
    /**
     * 闪避掩码
     */
    SHANBI(1),
    /**
     * 治疗掩码
     */
    ZHILIAO(4),
    /**
     * 暴击掩码
     */
    BAOJI(256),
    /**
     * 抵挡掩码
     */
    DIDANG(512),
    /**
     * 反击掩码
     */
    FANJI(1024);

    private final Integer type;

    private HarmType(Integer type) {
        this.type = type;
    }

    public Integer getVal() {
        return type;
    }

    /**
     * 是否为治疗伤害类型
     */
    public static boolean isZhiLiaoHarmType(HarmType harmType) {

        return harmType.equals(ZHILIAO);

    }

    public static boolean isSucessHarmType(HarmType harmType) {

        return harmType.equals(PUTONG) || harmType.equals(BAOJI);
    }

}
