package edu.bjtu.nourriture_web.idao;

import edu.bjtu.nourriture_web.bean.CookingStep;
import edu.bjtu.nourriture_web.bean.Location;

public interface ICookingStepDao {
	 int 			add(CookingStep my_cookingstep);
	 
	 CookingStep 	getById(int id);
	
	 void 			update(CookingStep my_cookingstep);
	 
	 void 			deletebyid(int id);

}
