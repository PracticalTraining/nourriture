package edu.bjtu.nourriture_web.idao;

import edu.bjtu.nourriture_web.bean.Flavour;

public interface IFlavourDao {
	/** get detail information of the flavour by id **/
	Flavour getById(int id);
}
