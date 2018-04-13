package com.junyou.kuafu.share.tunnel;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

import com.junyou.kuafu.share.util.KuafuServerInfo;

public class KuafuNetTunnelPool extends AbsKuafuNetTunnelPool<KuafuNetTunnel> {

	public KuafuNetTunnelPool(final GenericObjectPool.Config poolConfig,
			final KuafuServerInfo kuafuServerInfo) {
		super(poolConfig, new KuafuNetTunnelFactory(kuafuServerInfo));
	}

	private static class KuafuNetTunnelFactory extends BasePoolableObjectFactory {
		private final KuafuServerInfo kuafuServerInfo;

		public KuafuNetTunnelFactory(final KuafuServerInfo kuafuServerInfo) {
			super();
			this.kuafuServerInfo = kuafuServerInfo;
		}

		@Override
		public Object makeObject() throws Exception {
			final KuafuNetTunnel tunnel = new KuafuNetTunnel();
			tunnel.initTunnel(kuafuServerInfo);
			return tunnel;
		}

		public void destroyObject(final Object obj) throws Exception {
			if (obj instanceof KuafuNetTunnel) {
				final KuafuNetTunnel tunnel = (KuafuNetTunnel) obj;
				tunnel.destroy();
			}
		}

		public boolean validateObject(final Object obj) {
			if (obj instanceof KuafuNetTunnel) {
				final KuafuNetTunnel tunnel = (KuafuNetTunnel) obj;
				return tunnel.isConnected();
			}
			return false;
		}

	}

}
