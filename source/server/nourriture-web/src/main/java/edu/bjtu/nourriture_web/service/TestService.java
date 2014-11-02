package edu.bjtu.nourriture_web.service;

import edu.bjtu.nourriture_web.idao.ITestDao;
import edu.bjtu.nourriture_web.iservice.ITestService;

public class TestService implements ITestService {
	private ITestDao testDao;

	public String test() {
		testDao.test();
		return "this is test function for ssh,run successfully";
	}

	public ITestDao getTestDao() {
		return testDao;
	}

	public void setTestDao(ITestDao testDao) {
		this.testDao = testDao;
	}

}
