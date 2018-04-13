package com.kernel.pool.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description 缺省执行器池
 * @author hehj
 * 2010-9-6 下午03:16:36
 */
public class DefaultBusinessExecutor implements IBusinessExexutor {

	private HashMap<String, ExecutorPoolGroup> groups = new HashMap<String, ExecutorPoolGroup>();
	
	public DefaultBusinessExecutor(){}
	
	public DefaultBusinessExecutor(Map<String, Integer> groupConfigMap){
		
		if(null != groupConfigMap){
			
			for(Map.Entry<String,Integer> entry : groupConfigMap.entrySet()){
				
				addExecutorGroup(entry.getKey(), entry.getValue());
				
			}
			
		}
		
	}
	
	public void addExecutorGroup(String name,int size){
		
		if(!groups.containsKey(name)){
		
			groups.put(name, new ExecutorPoolGroup(size,name));
		
		}
		
	}
	
	@Override
	public void execute(Runnable runnable,RouteInfo routeInfo) {
		
		getExecutorService(routeInfo).execute(runnable);
		
		
	}
	
	@Override
	public Map<String, Map<String, Integer>> getExecutorInfos() {
		return null;
	}
	
	private ExecutorService getExecutorService(RouteInfo routeInfo) {
		
		return groups.get(routeInfo.getGroup()).getExecutor(routeInfo.getInfo());
		
	}

	/**
	 * @description 执行器分组
	 * @author hehj
	 * 2010-9-6 下午03:20:37
	 */
	private class ExecutorPoolGroup{
		
		private String name;
		private int size;
		
		private ExecutorService[] executors;
		private int assignCount;
		private ConcurrentMap<Object, ExecutorService> ruleMap = new ConcurrentHashMap<Object, ExecutorService>();
		
		public ExecutorPoolGroup(int size,String name) {
			this.name = name;
			this.size = size;
			init();
		}
		
		private void init(){
			this.executors = new ExecutorService[size];
			for(int i=0;i<size;i++){
				this.executors[i] = Executors.newSingleThreadExecutor(new JunYouThreadFactory(name+"-"+i));
			}
		}
		
		public ExecutorService getExecutor(Object ruleInfo){
			ExecutorService executor = ruleMap.get(ruleInfo);
			if(null == executor){
				synchronized (this) {
					executor = ruleMap.get(ruleInfo);
					if(null == executor){
						executor = executors[assignCount++%size];
						ruleMap.put(ruleInfo, executor);
					}
				}
			}
			
			return executor;
		}
	}
	
	
	public static void main(String[] args) {
		
//		IExecutorPool pool = DefaultExecutorPool.getExecutorPool();
//		ExecutorService executor = pool.getExecutor(new DefaultExecutorRule("default", 5, "default"));
//	
//		executor.execute(new Runnable() {
//			
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				System.out.println("fdffdsfdsfdsfdf");
//			}
//		});
	}

}
