package com.junyou.stage.model.core.attributeformula;

import java.util.ArrayList;
import java.util.List;




public abstract class AbsAttributeFormula implements IAttributeFormula {
	
	private List<IAttributeFormula> attributeFormulaList = null; 
	
	@Override
	public void addRelateFormula(IAttributeFormula relateFormula) {
		
		if(null == attributeFormulaList)
			attributeFormulaList = new ArrayList<IAttributeFormula>();
		
		attributeFormulaList.add(relateFormula);
	}
	
	protected List<IAttributeFormula> getRelateFormula() {
		
		return attributeFormulaList;
	}

}
