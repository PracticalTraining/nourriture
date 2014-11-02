package edu.bjtu.nourriture_web.service;

import edu.bjtu.nourriture_web.idao.IFlavourDao;
import edu.bjtu.nourriture_web.iservice.IFlavourService;

public class FlavourService implements IFlavourService{
	private IFlavourDao flavourDao;

	public IFlavourDao getFlavourDao() {
		return flavourDao;
	}

	public void setFlavourDao(IFlavourDao flavourDao) {
		this.flavourDao = flavourDao;
	} 
}
