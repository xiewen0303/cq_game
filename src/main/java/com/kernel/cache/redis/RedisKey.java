package com.kernel.cache.redis;

import com.junyou.utils.ChuanQiConfigUtil;

public class RedisKey {
	/**
	 * 单平台充值时间队列
	 */
	public static final String CHARGE_TIME_RANK_KEY_PREFIX = "charge_time_rank_";

	public static final String buildChargeTimeRankKey(String platformId) {
		return CHARGE_TIME_RANK_KEY_PREFIX + platformId;
	}
	
	/**
	 * 单平台消费时间队列
	 */
	public static final String XIAOFEI_TIME_RANK_KEY_PREFIX = "xiaofei_time_rank_";
	
	public static final String buildXiaoFeiTimeRankKey(String platformId) {
		return XIAOFEI_TIME_RANK_KEY_PREFIX + platformId;
	}
	
	public static final String XIAOFEI_DETAIL_KEY_PREFIX = "xf_cd_";
	
	public static final String buildXiaoFeiDetailKey(String xiaoFeiId) {
		return XIAOFEI_DETAIL_KEY_PREFIX + xiaoFeiId;
	}

	public static final String XIAOFEI_USER_KEY_PREFIX = "xf_cu_";
	
	public static final String buildXiaoFeiUserKey(long userRoleId) {
		return XIAOFEI_USER_KEY_PREFIX + userRoleId;
	}
	
//	public static final String XIAOFEI_USER_CONFIG_KEY_PREFIX = "xf_cc_";
//	
//	public static final String buildXiaoFeiUserConfigIdKey(long userRoleId) {
//		return XIAOFEI_USER_CONFIG_KEY_PREFIX + userRoleId;
//	}
	
	public static final String CHARGE_DETAIL_KEY_PREFIX = "cd_";

	public static final String buildChargeDetailKey(long chargeId) {
		return CHARGE_DETAIL_KEY_PREFIX + chargeId;
	}

	public static final String buildChargeDetailKey(String chargeId) {
		return CHARGE_DETAIL_KEY_PREFIX + chargeId;
	}

	public static final String CHARGE_USER_KEY_PREFIX = "cu_";

	public static final String buildChargeUserKey(long userRoleId) {
		return CHARGE_USER_KEY_PREFIX + userRoleId;
	}

	// --------------------跨服相关--------------------
	public static final String KUAFU_SERVER_KEY_PREFIX = "kuafu_server_";

	public static final String buildKuafuServerKey(String serverId) {
		return KUAFU_SERVER_KEY_PREFIX + serverId;
	}

	public static final String KUAFU_SERVER_SCORE_KEY_PREFIX = "kuafu_server_score_";

	public static final String buildKuafuServerScoreKey(String serverId) {
		return KUAFU_SERVER_SCORE_KEY_PREFIX + serverId;
	}

	public static final String KUAFU_SERVER_LIST_KEY = "kuafu_server_list";

	public static final String KUAFU_SERVER_ERROR_PREFIX = "kuafu_server_error_";

	public static final String buildKuafuServerErrorKey(String serverId) {
		return KUAFU_SERVER_ERROR_PREFIX + serverId;
	}

	public static final String KUAFU_DELETE_SERVER_LIST = "kuafu_delete_server_list";
	// --------------------跨服匹配相关--------------------
	public static final String KUAFU_MATCH_SERVER = "kuafu_match_server";
	public static final String KUAFU_MATCH_ID = "kmatch_id";
	public static final String KUAFU_MATCH_ID_PREFIX = "kmatch_id_";

	public static final String buildKMatchId(long matchId) {
		return KUAFU_MATCH_ID_PREFIX + matchId;
	}

	public static final String KUAFU_ARENA_1V1_RANK = "kuafu_arena_1v1_rank";
	public static final String KUAFU_ARENA_1V1_RANK_ROLE = "k1v1_";

	public static final String buildkuafuArena1v1RankRoleKey(long userRoleId) {
		return KUAFU_ARENA_1V1_RANK_ROLE + userRoleId;
	}
	
	/**
	 * 跨服1v1竞技排名奖励数据对象</br>
	 * HashMap</br>
	 * {</br>
	 *     key:用户编号(String)userRoleId</br>
	 *     value:用户本轮活动名次(String)rank</br>
	 * }
	 */
	public static final String KUAFU_AREA_1V1_RANK_REWARD = "kuafu_area_1v1_rank_reward";
	/**
	 * 跨服4v4竞技排名奖励数据对象</br>
	 * HashMap</br>
	 * {</br>
	 *     key:用户编号(String)userRoleId</br>
	 *     value:用户本轮活动名次(String)rank</br>
	 * }
	 */
	public static final String KUAFU_AREA_4V4_RANK_REWARD = "kuafu_area_4v4_rank_reward";
	
	public static final String KUAFU_ARENA_1V1_CLEAN_FLAG = "kuafu_arena_1v1_clean_flag";

	// --------------------八卦阵相关--------------------
	public static String getBaguaTeamListKey(int fubenId) {
		return "bg_teams_" + fubenId;
	}

	public static int getRedisTeamId(long teamId) {
		return (int) (teamId & Integer.MAX_VALUE);
	}

	public static String getBaguaTeamIdKey(String teamId) {
		return "bg_teamid_" + teamId;
	}

	public static String getBaguaServerKey() {
		return "bg_teams_server_" + ChuanQiConfigUtil.getServerId();
	}

	// -------------------神魔相关--------------------
	public static final String KUAFU_ARENA_4V4_RANK = "kuafu_arena_4v4_rank";
	public static final String KUAFU_ARENA_4V4_RANK_ROLE = "k4v4_";

	public static final String buildkuafuArena4v4RankRoleKey(long userRoleId) {
		return KUAFU_ARENA_4V4_RANK_ROLE + userRoleId;
	}

	public static final String KUAFU_ARENA_4V4_CLEAN_FLAG = "kuafu_arena_4v4_clean_flag";

	// ----------------------封神之战---------------------------
	public static String getRedisKuaFuJingJiRoleKey(Long userRoleId) {
		return "kfjj_role_" + userRoleId.toString();
	}

	public static String getRedisKuaFuJingJiRankKey(Integer rank) {
		return "kfjj_rank_" + rank.toString();
	}

	public static String getRedisKuaFuJingJiLastRankKey(Long userRoleId) {
		return "kfjj_lr_" + userRoleId.toString();
	}

	public static byte[] getRedisKuaFuJingJiAttributeKey(Long userRoleId) {
		return ("kfjj_" + userRoleId).getBytes();
	}

	public static String getRedisKuaFuFightKey(Long userRoleId) {
		return "kfjj_fight_" + userRoleId.toString();
	}

	public static String getRedisKuaFuFightZhanBaoKey(Long userRoleId) {
		return "kfjj_ftzb_" + userRoleId.toString() + "_";
	}

	// ----------------------平台热发布鲜花魅力排行榜---------------------------
	/** 鲜花榜当前活动数据key,暂定30天过期 **/
	public static String getRedisKuafuRfbFlowerCharmKey(int subId) {
		return "kf_flower_data_" + subId;
	}

	/** 鲜花榜个人信息key **/
	public static String getRedisKuafuRfbFlowerCharmRoleInfoKey(Long userRoleId) {
		return "kf_flower_role_info_" + userRoleId;
	}

	// ----------------------跨服世界boss---------------------------
	public static String KUAFU_BOSS_STAGE_NUM_KEY_PREFIX ="kuafu_boss_stage_num_";
	public static String getKuafuBossStageRoleNumKey(String serverId) {
		return KUAFU_BOSS_STAGE_NUM_KEY_PREFIX+serverId;
	}
	
	public static String getKuafuBossDeadFlag(String serverId) {
		return "kuafu_boss_dead_flag_"+serverId;
	}

	public static String getKuafuBossBindServerIdKey(long userRoleId) {
		return "kuafu_boss_bind_server_id_"+userRoleId;
	}

	public static String getKuafuBossRewardKey(long userRoleId){
		return "kuafu_boss_reward_"+userRoleId;
	}
	public static String KUAFU_BOSS_START_TIME ="kuafu_boss_start_time";
	public static String KUAFU_BOSS_NEXT_ID ="kuafu_boss_next_id";
	public static String KUAFU_BOSS_DEAD_FLAG ="kuafu_boss_dead_flag";
	// ----------------------跨服群仙宴---------------------------
	public static String KUAFU_QUNXIANYAN_STAGE_NUM_KEY_PREFIX ="kuafu_qunxianyan_stage_num_";
	public static String getKuafuQunXianYanStageRoleNumKey(String serverId) {
		return KUAFU_QUNXIANYAN_STAGE_NUM_KEY_PREFIX+serverId;
	}
	
	public static String getKuafuQunXianYanBindServerIdKey(long userRoleId) {
		return "kuafu_qunxianyan_bind_server_id_"+userRoleId;
	}
	
	/** 群仙宴积分key **/
	public static String KUAFU_QUNXIANYAN_RANK_JIFEN = "kuafu_qunxianyan_rank_jifen";
	//------------------------跨服大乱斗--------------------------
	public static String KUAFU_LUANDOU_BAOMING_DATA_KEY ="kuafu_luandou_baoming_data_key";//跨服乱斗报名数据key
	/** 大乱斗房间key **/
	public static String getRedisKuafuLuanDouFangJianKey(int id) {
		return "kf_luandou_fangjian_" + id;
	}
	/** 大乱斗房间积分key **/
	public static String getRedisKuafuLuanDouFangJianJiFenKey(int id) {
		return "kf_luandou_fangjian_jifen_" + id;
	}
	
	/** 晋级榜:大乱斗每个房间前2名key **/
	public static String getRedisKuafuLuanDouRoomRankKey(int fjNumber){
	    return "kf_luandou_fangjian_rank_" + fjNumber;
	}	

	// ----------------------跨服世界boss---------------------------
	public static final String COMPETITION_KUAFU_MATCH_SERVER = "competition_kuafu_match_server";
	public static final String KUAFU_COMPETITION_TIME_1V1 = "1v1_competition_time";
	public static final String KUAFU_COMPETITION_ROLES_1V1 = "1v1_competition_roles";
	public static final String KUAFU_COMPETITION_TIME_4V4 = "4v4_competition_time";
	public static final String KUAFU_COMPETITION_ROLES_4V4 = "4v4_competition_roles";
	
	//--------------------------跨服秒杀------------------------------------
	public static String getKFMSTop10KeyPrifix(Integer subId,Integer id){
		return "kfms_top10_"+subId.toString()+"_"+id.toString()+"_";
	}
	public static String getKFMSFirstKeyPrifix(Integer subId,Integer id){
		return "kfms_first_"+subId.toString()+"_"+id.toString();
	}
	
    // ------------------------跨服巅峰之战--------------------------
	public static String KUAFU_DIANFENG_SERVER_ID = "kuafu_dianfeng_server_id";
    /** 巅峰之战房间key **/
    public static String getRedisKfDianFengRoomKey(int loop) {
        return "kf_dianfeng_loopRooms_" + loop;
    }
    
    // ------------------------跨服云宫之战--------------------------------
    
    public static String KUAFU_YUNGONG_RESULT_KEY = "kuafu_yungong_result";
    /**
     * 跨服云宫之战胜利公会数据(新一轮活动开始前删除redis中上次活动的结果数据)</br>
     * Redis Key:kuafu_yungong_result_<code>matchServerId</code></br>
     * Redis Value:String(战胜公会vo数据KuafuYunGongResult)
     */
    public static String getKfYunGongResultKey(String matchServerId){
        return KUAFU_YUNGONG_RESULT_KEY + "_" + matchServerId;
    }
    
    public static String KUAFU_YUNGONG_DAY_REWARD_KEY = "kuafu_yungong_day_reward";
    /**
     * 跨服云宫之战胜利公会奖励数据(新一轮活动开始前删除redis中上次活动的结果数据)</br>
     * Redis Key:kuafu_yungong_day_reward_<code>matchServerId</code></br>
     * Redis Value:map{key=userRoleId,value=公会奖励数据vo数据KuafuYunGongDayReward}
     */
    public static String getKfYunGongDayRewardKey(String matchServerId){
        return KUAFU_YUNGONG_DAY_REWARD_KEY + "_" + matchServerId;
    }
    /**
     * 跨服云宫之战所有参赛服务器匹配的跨服服务器信息(新一轮活动开始前删除redis中上次活动的结果数据)</br>
     * Redis Key:kuafu_yungong_server_match</br>
     * Redis Value:map{key=参赛服务器id,value=匹配的跨服服务器id}
     */
    public static String KUAFU_YUNGONG_SERVER_MATCH_KEY = "kuafu_yungong_server_match";
    /**
     * 跨服云宫之战所有参赛服务器信息(一轮活动结束后立即删除redis中的数据)</br>
     * Redis Key:kuafu_yungong_server</br>
     * Redis Value:参赛服务器id
     */
    public static String KUAFU_YUNGONG_SERVER_KEY = "kuafu_yungong_server";

}