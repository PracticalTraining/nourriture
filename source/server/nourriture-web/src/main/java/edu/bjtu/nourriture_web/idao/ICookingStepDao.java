package edu.bjtu.nourriture_web.idao;

import java.util.List;

import edu.bjtu.nourriture_web.bean.CookingStep;

public interface ICookingStepDao {
	 int 			add(CookingStep my_cookingstep);
	 
	 CookingStep 	getById(int id);
	
	 void 			update(CookingStep my_cookingstep);
	 
	 void 			deletebyid(int id);
	 List<CookingStep> getByRecipeId(int rId);

}
