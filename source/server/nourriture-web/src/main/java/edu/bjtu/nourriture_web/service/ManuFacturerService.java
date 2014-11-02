package edu.bjtu.nourriture_web.service;

import edu.bjtu.nourriture_web.idao.IManuFacturerDao;
import edu.bjtu.nourriture_web.iservice.IManuFacturerService;

public class ManuFacturerService implements IManuFacturerService{
	private IManuFacturerDao manuFacturerDao;

	public IManuFacturerDao getManuFacturerDao() {
		return manuFacturerDao;
	}

	public void setManuFacturerDao(IManuFacturerDao manuFacturerDao) {
		this.manuFacturerDao = manuFacturerDao;
	}
}
