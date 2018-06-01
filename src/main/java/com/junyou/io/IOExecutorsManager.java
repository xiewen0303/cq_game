package com.junyou.io;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.junyou.log.ChuanQiLog;
import com.kernel.pool.executor.ThreadNameFactory;

public class IOExecutorsManager {

	private static ExecutorService[] executors = new ExecutorService[8];

	private static Map<ExecutorService, AtomicInteger> map = new HashMap<ExecutorService, AtomicInteger>();

	static {
		for (int i = 0; i < 8; i++) {
			executors[i] = Executors
					.newSingleThreadExecutor(new ThreadNameFactory("io-send-"
							+ i + "-"));
			map.put(executors[i], new AtomicInteger(0));
		}
	}

	public static void bindExecutorService(GameSession gameSession) {
		ExecutorService executorService = null;
		int min = Integer.MAX_VALUE;
		AtomicInteger counter = null;
		for (ExecutorService e : map.keySet()) {
			int value = map.get(e).get();
			if (value < min) {
				min = value;
				executorService = e;
				counter = map.get(e);
			}
		}

		if (counter == null) {
			ChuanQiLog.error("unkown error");
			return;
		}
		synchronized (counter) {
			counter.incrementAndGet();
		}
		gameSession.setExecutorService(executorService);

	}

	public static void removebindExecutorService(GameSession gameSession) {
		ExecutorService executorService = gameSession.getExecutorService();
		if (executorService == null) {
			return;
		}
		AtomicInteger counter = map.get(executorService);
		if (counter == null) {
			return;
		}
		synchronized (counter) {
			int value = counter.decrementAndGet();
			if (value < 0) {
				counter.set(0);
			}
		}
	}
}
