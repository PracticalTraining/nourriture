package edu.bjtu.nourriture_web.restfulservice;

import javax.ws.rs.Path;

import edu.bjtu.nourriture_web.idao.IAdminDao;

@Path("admin")
public class AdminRestfulService {
	private IAdminDao adminDao;

	public IAdminDao getAdminDao() {
		return adminDao;
	}

	public void setAdminDao(IAdminDao adminDao) {
		this.adminDao = adminDao;
	}
}
