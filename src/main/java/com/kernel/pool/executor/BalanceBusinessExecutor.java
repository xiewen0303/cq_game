package com.kernel.pool.executor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @description 带负载均衡的执行池 
 * @author hehj
 * @date 2012-3-28 下午4:39:19 
 */
public class BalanceBusinessExecutor implements IBusinessExexutor {

	private static final Log LOG = LogFactory.getLog("server_status_logger");
	
	private int loadSize = 11;
	private int loadCurror = 0;
	private int loadGap = 60 * 1000;
	private int clean_gap = 10 * 60 * 1000;
	
	private HashMap<String, ExecutorPoolGroup> groups = new HashMap<String, ExecutorPoolGroup>();
	
	private IRuleInfoCheck ruleInfoCheck;
	
	/**
	 * @param size 负载时间段数
	 * @param gap	负载段时间间隔(单位：分钟)
	 * @param cleanGap	路由信息清理时间间隔(单位：分钟)
	 * @param name	执行器名称
	 * @param groupConfigMap 分组配置 
	 */
	public BalanceBusinessExecutor(int size,int gap,int cleanGap,String name,Map<String, Integer> groupConfigMap,IRuleInfoCheck ruleInfoCheck){
		
		if(size > 0){
			this.loadSize = size;
		}
		
		if(gap > 0){
			this.loadGap = gap * 60 * 1000;
		}
		
		if(cleanGap > 0){
			this.clean_gap = cleanGap * 60 * 1000;
		}

		if(null == ruleInfoCheck) throw new RuntimeException("IRuleInfoCheck[ruleInfoCheck] can't be null");
		this.ruleInfoCheck = ruleInfoCheck;
		
		if(null != groupConfigMap){
			
			for(Map.Entry<String, Integer> entry : groupConfigMap.entrySet()){
				addExecutorGroup(entry.getKey(), entry.getValue());
			}
			
//			for(String group : groupConfigMap.keySet()){
//				int groupSize = groupConfigMap.get(group);
//				addExecutorGroup(group, groupSize);
//			}
			
		}
		
		Thread cleanThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				int cleanCount = clean_gap / loadGap;
				int sleepCount = 0;
				for(;;){
					
					try {
						
						Thread.sleep(loadGap);
						loadCurror = ++loadCurror % loadSize;
						for(ExecutorPoolGroup group : groups.values()){
						
							group.setLoadCurror(loadCurror);
							
						}
						
						// rule info clean
						++sleepCount;
						if( sleepCount == cleanCount){
							
							sleepCount = 0;
							StringBuilder infoBuilder = new StringBuilder();
							for(ExecutorPoolGroup group : groups.values()){
								
								group.cleanRuleExecutorRelation();
								infoBuilder.append(group.groupInfo());
							}
							
							LOG.error(infoBuilder.toString());
						}
						
					} catch (Exception e) {
					}
					
				}
			}
			
		},"BalanceBusinessExecutor-Cleaner-"+name);
		
		cleanThread.setDaemon(true);
		cleanThread.start();
		
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
	
	public Map<String, Map<String, Integer>> getExecutorInfos(){
		
		Map<String, Map<String, Integer>> infos = new HashMap<String, Map<String,Integer>>();
		for(ExecutorPoolGroup group : groups.values()){
			infos.put(group.getName(), group.getGroupInfo());
		}
		return infos;
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
		
		private BalanceExecutor[] executors;
		private Map<Object, RuleExecutorRelation> ruleMap = new HashMap<Object, RuleExecutorRelation>();
		
		public ExecutorPoolGroup(int size,String name) {
			this.name = name;
			this.size = size;
			init();
		}
	
		private void init(){
			this.executors = new BalanceExecutor[size];
			for(int i=0;i<size;i++){
				this.executors[i] = new BalanceExecutor(name+"-"+i);
			}
		}
		
		public String getName(){
			return this.name;
		}
		
		public void setLoadCurror(int loadCurror){
			for(BalanceExecutor executor : executors){
				executor.setCurror(loadCurror);
			}
		}
		
		public Map<String, Integer> getGroupInfo(){
			Map<String, Integer> info = new HashMap<String, Integer>();
			for(BalanceExecutor executor : this.executors){
				info.put(executor.getName(), executor.getLoad());
			}
			return info;
		}
		
		public String groupInfo(){
			StringBuilder infoBuilder = new StringBuilder("\n"+this.name);
			for(BalanceExecutor executor : this.executors){
				infoBuilder.append("\n\t").append(executor.getName()).append("  ").append(executor.getLoad());
			}
			return infoBuilder.toString();
		}
		
		public synchronized void cleanRuleExecutorRelation(){
			
			Iterator<RuleExecutorRelation> iterator = ruleMap.values().iterator();
			while(iterator.hasNext()){
				
				RuleExecutorRelation relation = iterator.next();
				
				if(relation.canClean()){
					iterator.remove();
				}
			}
			
		}
		
		public ExecutorService getExecutor(String ruleInfo){
			RuleExecutorRelation ruleExecutorRelation = ruleMap.get(ruleInfo);
			if(null == ruleExecutorRelation){
				synchronized (this) {
					ruleExecutorRelation = ruleMap.get(ruleInfo);
					if(null == ruleExecutorRelation){
						ruleExecutorRelation = new RuleExecutorRelation(ruleInfo, getLowestExecutor(),ruleInfoCheck);
						ruleMap.put(ruleInfo, ruleExecutorRelation);
					}
				}
			}
			return ruleExecutorRelation.getBalanceExecutor().getExecutorService();
		}
		
		private BalanceExecutor getLowestExecutor(){
			
			BalanceExecutor lowestExecutor = null;
			int minLoad = 0;
			for(BalanceExecutor executor : executors){
				
				int load = executor.getLoad();
				if(null == lowestExecutor){
					lowestExecutor = executor;
					minLoad = load;
				}else{
					if(load < minLoad){
						lowestExecutor = executor;
						minLoad = load;
					}
				}
				
			}
			
			return lowestExecutor;
		}
	}
	
	private class BalanceExecutor{

		private String name;
		
		private int[] loads = new int[loadSize];
		
		private int curror = 0;
		
		private ExecutorService executorService;

		public BalanceExecutor(String name) {
			this.name = name;
			
			executorService = Executors.newSingleThreadExecutor(new ThreadNameFactory(name));
			
		}
		
		public synchronized void setCurror(int value){
			this.curror = value;
			loads[this.curror] = 0;
		}
		
		public synchronized ExecutorService getExecutorService() {
			++loads[curror];
			return executorService;
		}
		
		public synchronized int getLoad(){
			int load = 0;
			for(int i=0;i<loadSize;i++){
				load += loads[i];
			}
			return load;
		}

		public String getName() {
			return name;
		}
		
	}
	
	
	private class RuleExecutorRelation{
		
		private String ruleInfo;
		
		private BalanceExecutor balanceExecutor;
		
		private IRuleInfoCheck ruleInfoCheck;
		
		public RuleExecutorRelation(String ruleInfo,BalanceExecutor balanceExecutor,IRuleInfoCheck ruleInfoCheck) {
			this.ruleInfo = ruleInfo;
			this.balanceExecutor = balanceExecutor;
			this.ruleInfoCheck = ruleInfoCheck;
		}
		
		public boolean canClean(){
			return ruleInfoCheck.valid(this.ruleInfo);
		}
		
		public BalanceExecutor getBalanceExecutor(){
			return this.balanceExecutor;
		}
	}
	
	
	public static void main(String[] args) {
		Map<String, Integer> groups = new HashMap<String, Integer>();
		groups.put("test", 3);
		final BalanceBusinessExecutor balanceBusinessExecutor = new BalanceBusinessExecutor(6,1,2,"test",groups,null);
		
		for(int j=0;j<5;j++){
			
			new Thread(){
				
				@Override
				public void run() {
					
					for(int i=0;i<1000000;i++){
						
						try {
							
							
							RouteInfo routeInfo = new RouteInfo("test");
							routeInfo.setInfo(String.valueOf(i));
							balanceBusinessExecutor.getExecutorService(routeInfo);
							
							Thread.sleep(5);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
				
			}.start();
			
		}
		
	}
}
