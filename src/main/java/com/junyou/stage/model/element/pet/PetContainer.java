package com.junyou.stage.model.element.pet;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PetContainer {

	/**
	 * KEY:技能ID
	 */
	private Map<String,Pet> pets = new HashMap<String, Pet>();
	
	public void add(String skillId,Pet employee) {
		pets.put(skillId, employee);
	}
	
	
	public Pet getPet(String skillId){
		return pets.get(skillId);
	}
	
	public Collection<Pet> getPets(){
		return null != pets ? pets.values() : null;
	}
	
	public void remove(String skillId) {
		
		pets.remove(skillId);
		
	}
}
