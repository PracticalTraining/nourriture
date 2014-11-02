package edu.bjtu.nourriture_web.service;

import edu.bjtu.nourriture_web.idao.ILocationDao;
import edu.bjtu.nourriture_web.iservice.ILocationService;

public class LocationService implements ILocationService{
	private ILocationDao locationDao;

	public ILocationDao getLocationDao() {
		return locationDao;
	}

	public void setLocationDao(ILocationDao locationDao) {
		this.locationDao = locationDao;
	}
}
