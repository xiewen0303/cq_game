package com.hehj.easyexecutor.manager;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import com.hehj.easyexecutor.Interceptor.IInterceptor;
import com.hehj.easyexecutor.annotation.ClassInterceptors;
import com.hehj.easyexecutor.annotation.EasyInterceptor;
import com.hehj.easyexecutor.annotation.EasyInterceptors;
import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.cmd.CmdGroupInfo;
import com.hehj.easyexecutor.config.IEasyexecutorMeta;
import com.hehj.easyexecutor.config.InterceptorConfig;
import com.hehj.easyexecutor.resolver.DefaultResolver;
import com.hehj.easyexecutor.resolver.IEasyResolver;

public class DefaultManager implements IEasyManager {
	
	private Map<Short, IEasyResolver> resolvers = new HashMap<Short, IEasyResolver>();
	
	private SortedMap<Integer,WildcardEntity> wildCardEntities = new ConcurrentSkipListMap<Integer, WildcardEntity>(); 
	
	private IEasyWorkerContainer workerContainer;
	
	private IEasyexecutorMeta easyexecutorMeta;
	
	private List<IInterceptor> globalInterceptors;
	
	public DefaultManager(IEasyexecutorMeta easyexecutorMeta,IEasyWorkerContainer workerContainer){
		if( null == workerContainer ) throw new NullPointerException("workerContainer can't be null.");
		if( null == easyexecutorMeta ) throw new NullPointerException("easyexecutorMeta can't be null.");
		this.workerContainer = workerContainer;
		this.easyexecutorMeta = easyexecutorMeta;
		init();
	}
	
	public void init(){
		
		if(easyexecutorMeta.globalInterceptors().size()>0){
			this.globalInterceptors = new ArrayList<IInterceptor>();
			for(InterceptorConfig interceptorConfig : easyexecutorMeta.globalInterceptors()){
				
				try {
					@SuppressWarnings("unchecked")
					Class<IInterceptor> cls = (Class<IInterceptor>) Thread.currentThread().getContextClassLoader().loadClass(interceptorConfig.getClassName());
					if(interceptorConfig.isSpringBean()){
						globalInterceptors.add((IInterceptor) workerContainer.getWorker(cls));
					}else{
						globalInterceptors.add(cls.newInstance());
					}
				} catch (Exception e) {
					throw new RuntimeException("",e);
				}
				
			}
		}
		
		File root = new File(DefaultManager.class.getClass().getResource("/").getFile());
		String prefix = root.getAbsolutePath();
		List<Class<?>> classes = new ArrayList<Class<?>>(); 
		listCommandClass(root,prefix,classes);
		for(Class<?> cls : classes){
			analyzeClass(cls);
		}
		
	}

	private void analyzeClass(Class<?> cls){
		
		EasyWorker easyWorker = cls.getAnnotation(EasyWorker.class);
		if (null != easyWorker) {
			
			try {
				// analyze class interceptors
				List<IInterceptor> classInterceptorList = null;
				ClassInterceptors classInterceptors = cls.getAnnotation(ClassInterceptors.class);
				if(null!=classInterceptors){
					EasyInterceptor[] interceptors = classInterceptors.value();
					if(null != interceptors && interceptors.length > 0){
						classInterceptorList = new ArrayList<IInterceptor>();
						for(EasyInterceptor interceptor : interceptors){
							IInterceptor tmp = null;
							if(interceptor.isSpringBean()){
								tmp = (IInterceptor) workerContainer.getWorker(interceptor.value());
							}else{
								tmp = interceptor.value().newInstance();
							}
							classInterceptorList.add(tmp);
						}
					}
				}
				
				Method[] methods = cls.getDeclaredMethods();

				for (Method m : methods) {
				
					EasyMapping commandMapping = m.getAnnotation(EasyMapping.class);
					if (null != commandMapping) {

						List<IInterceptor> methodInterceptors = null;
						EasyInterceptors easyInterceptors = m.getAnnotation(EasyInterceptors.class);
						if(null != easyInterceptors){
							EasyInterceptor[] interceptors = easyInterceptors.value();
							if(null != interceptors && interceptors.length > 0){
								methodInterceptors = new ArrayList<IInterceptor>();
								for(EasyInterceptor interceptor : interceptors){
									IInterceptor tmp = null;
									if(interceptor.isSpringBean()){
										tmp = (IInterceptor) workerContainer.getWorker(interceptor.value());
									}else{
										tmp = interceptor.value().newInstance();
									}
									methodInterceptors.add(tmp);
								}
							}
						}
						
						Class<?>[] paramTypes = m.getParameterTypes();
						if("".equals(easyWorker.workerName())){
							resolvers.put(commandMapping.mapping(), new DefaultResolver(m, paramTypes,cls,workerContainer.getWorker(cls),globalInterceptors,classInterceptorList,methodInterceptors));
							CmdGroupInfo.registerCmds(commandMapping.mapping(), easyWorker.moduleName(), easyWorker.groupName().getGroupName(),commandMapping.kuafuType());
						}else{
							resolvers.put(commandMapping.mapping(), new DefaultResolver(m, paramTypes,cls,workerContainer.getWorker(easyWorker.workerName()),classInterceptorList,globalInterceptors,methodInterceptors));
							CmdGroupInfo.registerCmds(commandMapping.mapping(), easyWorker.moduleName(), easyWorker.groupName().getGroupName(),commandMapping.kuafuType());
						}
						
					}
	
				}
				
			} catch (Exception e) {
				
				throw new RuntimeException("error in analyzeClass",e);
				
			} 
		}
		
	}
	
	private void listCommandClass(File file,String prefix,List<Class<?>> list){

		File[] files = file.listFiles();
		if(null!=files){
			try{
				for(File f : files){
					if(f.isDirectory()){
						listCommandClass(f,prefix,list);
					}else{
						if(f.getName().endsWith(".class")){
						
							String parent = f.getParent();
							String name = f.getName();
							String classpath = (parent.substring(prefix.length()+1)+File.separator+name.substring(0, name.length()-6)).replace("\\", ".");
							if(easyexecutorMeta.isScanPackage(classpath)){
								list.add(Class.forName(classpath.replace(File.separator, ".")));
							}
						}else if(f.getName().endsWith(".jar")){
							JarFile jarFile = new JarFile(f);
							Enumeration<JarEntry> jarEntries = jarFile.entries();
							while(jarEntries.hasMoreElements()){
								JarEntry jarEntry = jarEntries.nextElement();
								if(jarEntry.getName().endsWith(".class")){

									String jarEntryName = jarEntry.getName();
									String classpath = jarEntryName.substring(0, jarEntryName.length()-6).replace("/", ".");
									if(easyexecutorMeta.isScanPackage(classpath)){
										list.add(Thread.currentThread().getContextClassLoader().loadClass(classpath));
									}
								}
							}
								
						}
					}
				}
			}catch (Exception e) {
				throw new RuntimeException("error in listCommandClass",e);
			}
		}
		
	}
	
	@Override
	public IEasyResolver getResolver(Short command) {
		
		IEasyResolver resolver = resolvers.get(command);
		if(null == resolver){
		
			for(int weight : wildCardEntities.keySet()){
			
				WildcardEntity entity = wildCardEntities.get(weight);
				
				if(entity.isMatch(command)){
				 
					return entity.getResolver(); 
				
				}
				
			}
			
		}
		
		return resolver;
	}

	public class WildcardEntity{

		private Pattern p;
		
		private IEasyResolver resolver;
		
		public WildcardEntity(String wildString,IEasyResolver resolver) {
			
			this.p = Pattern.compile(wildString);
			this.resolver = resolver;
			
		}
		
		public boolean isMatch(Short mappingString){
			return p.matcher(mappingString.toString()).matches();
		}
		
		public IEasyResolver getResolver(){
			return resolver;
		}
	}
	
}
