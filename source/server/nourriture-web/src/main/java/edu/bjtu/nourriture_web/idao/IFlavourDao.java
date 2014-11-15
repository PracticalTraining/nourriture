package edu.bjtu.nourriture_web.idao;

import edu.bjtu.nourriture_web.bean.Flavour;
import edu.bjtu.nourriture_web.bean.FoodCategory;
import edu.bjtu.nourriture_web.bean.RecipeCategory;
import edu.bjtu.nourriture_web.dao.FlavourDao;

public interface IFlavourDao {
	/** get detail information of the Flavour by id **/
	Flavour getById(int id);

	/** add one row **/
	int add(Flavour flavour);

	/** check if Flavour is already exist **/
	boolean isFlavourExist(int id);

	/** delete the Flavour **/
	void delete(Flavour deleteFlavour);

	/** search the detail info of the Flavour **/
	Flavour searchFlavourDetailById(int id);

	/** update the Flavour **/
	void update(Flavour updateFlavour);
}
