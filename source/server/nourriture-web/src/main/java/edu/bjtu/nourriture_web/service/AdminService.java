package edu.bjtu.nourriture_web.service;

import edu.bjtu.nourriture_web.idao.IAdminDao;
import edu.bjtu.nourriture_web.iservice.IAdminService;

public class AdminService implements IAdminService {
	private IAdminDao adminDao;

	public IAdminDao getAdminDao() {
		return adminDao;
	}

	public void setAdminDao(IAdminDao adminDao) {
		this.adminDao = adminDao;
	}

}
