package com.junyou.kuafumatch.util;

import com.junyou.kuafu.share.tunnel.KuafuNetTunnel;
import com.junyou.kuafumatch.manager.KuafuCompetitionMatchServerManager;
import com.junyou.log.ChuanQiLog;

public class KuafuCompetitionMatchServerUtil {
	public static boolean isMatchServerAvailable() {
		KuafuNetTunnel tunnel = null;
		boolean returned = false;
		try {
			tunnel = KuafuCompetitionMatchServerManager.getKuafuMatchConnection();
			if (tunnel != null && tunnel.isConnected()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			KuafuCompetitionMatchServerManager.returnKuafuMatchBrokenConnection(tunnel);
			returned = true;
			ChuanQiLog.error("", e);
			return false;
		} finally {
			if (!returned) {
				KuafuCompetitionMatchServerManager.returnKuafuMatchConnection(tunnel);
			}
		}
	}
}
