package com.junyou.stage.model.skill.firepath;

import java.util.HashMap;
import java.util.Map;

import com.junyou.stage.configure.SkillFirePathType;
import com.junyou.stage.model.core.skill.ISkillFirePath;

public class SkillFirePathFactory {

	private static Map<SkillFirePathType,ISkillFirePath> maps = new HashMap<SkillFirePathType, ISkillFirePath>();
	
	static{
		maps.put(SkillFirePathType.DEFAULT, new DefaultSkillFirePath());
		maps.put(SkillFirePathType.RECTANGLE, new RectangleSkillFirePath());
		maps.put(SkillFirePathType.SECTOR, new SelfSectorSkillFirePath());
		maps.put(SkillFirePathType.SELF_CIRCLE, new SelfCircleSkillFirePath());
		maps.put(SkillFirePathType.TARGET_CIRCLE, new TargetCircleSkillFirePath());
		maps.put(SkillFirePathType.SELF, new SelfSkillFirePath());
		maps.put(SkillFirePathType.PASSTHROUGH, new PassThroughSkillFirePath());
		
		maps.put(SkillFirePathType.FOREVER_TRUE, new ForeverTrueSkillFirePath());
		maps.put(SkillFirePathType.TAFANG, new TaFangSkillFirePath());
	}
	
	/**
	 * 获取技能施法路径
	 * @param type {@link SkillFirePathType}
	 */
	public static ISkillFirePath getSkillFirePath(SkillFirePathType type){
		
		ISkillFirePath path = maps.get(type);
		if(null == path){
			path = maps.get(SkillFirePathType.DEFAULT);
		}
		
		return path;
		
	}
}
