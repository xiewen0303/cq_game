package com.junyou.utils.name;

import java.util.HashMap;
import java.util.Map;

import com.junyou.gameconfig.goods.configure.export.helper.BusConfigureHelper;

public class MinganciUtil {

	
	private static class SpAnalyzer {
		
		private String[] buffer;
		private int max=0;
		private int pos=0;
		private int words = 0;
		
		public SpAnalyzer(String[] buffer,int words) {
			this.buffer = buffer;
			this.words = words -1;
			this.max = buffer.length ;
		}
		
		public String analyze(){
			if(null!=buffer[words]){
				StringBuffer tmp = new StringBuffer("");
				for(int i=0;i<=words;i++){
					tmp.append(buffer[(pos+i) % max]);
				}
				pos = ++pos % max;
				return tmp.toString();
			}
			return null;
		}
		
	}
	
	public static void init(){};
	
	/**
	 * 判定是否包含敏感词
	 * @param word
	 * @return true 包含
	 */
	public static boolean check(String word){

		String[] strs = word.split("");
		int start = 0;
		int length = word.length();
		if("".equals(strs[0]) && length > 1) start = 1;
		
		int max = length;
		int buffer_pos = 0;
		String[] buffer = new String[max];
		Map<Integer, SpAnalyzer> analyzers = new HashMap<Integer, SpAnalyzer>();
		for(int i=1;i<=max;i++){
			analyzers.put(i, new SpAnalyzer(buffer, i));
		}
		boolean mg = false;
		for(int i = start;i<=word.length();i++){
			
			buffer[buffer_pos] = strs[i];
			if("".equals(strs[i].trim())){
				buffer[buffer_pos] = "";
			}else{
				buffer[buffer_pos] = strs[i];
			}
			for(int j=1;j<=max;j++){
				String tmp = analyzers.get(j).analyze(); 
				if(mg(tmp)){
					mg= true;
					break;
				}
			}
			buffer_pos = ++buffer_pos % max;
		}

		
		return mg;
	}
	
	private static boolean mg(String word){
		if(null == word || "".equals(word)) return false;
		return BusConfigureHelper.getPingbiziConfigExportService().checkPingbizi(word);
	}
	
	public static void main(String[] args) {
//		System.out.println(minganciMap.size());
	}
}
