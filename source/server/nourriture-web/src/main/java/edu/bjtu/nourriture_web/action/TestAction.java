package edu.bjtu.nourriture_web.action;

import com.opensymphony.xwork2.ActionSupport;

import edu.bjtu.nourriture_web.iservice.ITestService;

public class TestAction extends ActionSupport{
	private ITestService testService;
	private String str;

	@Override
	public String execute() throws Exception {
		testService.test();
		return SUCCESS;
	}

	public ITestService getTestService() {
		return testService;
	}

	public void setTestService(ITestService testService) {
		this.testService = testService;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}
}
