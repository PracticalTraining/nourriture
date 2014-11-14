package edu.bjtu.nourriture_web.idao;

import edu.bjtu.nourriture_web.bean.Flavour;
import edu.bjtu.nourriture_web.bean.FoodCategory;

public interface IFlavourDao {
	/** get detail information of the flavour by id **/
	Flavour getById(int id);
	/** add one row **/
	int add(Flavour flavour);
	/** update flavour **/
	void update(Flavour flavour);
	/** check if the flavour is exsit **/
	boolean isFlavourExist(int flavourId);
}
