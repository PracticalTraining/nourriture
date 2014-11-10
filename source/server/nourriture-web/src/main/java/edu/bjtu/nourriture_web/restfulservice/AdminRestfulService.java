package edu.bjtu.nourriture_web.restfulservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.bjtu.nourriture_web.bean.Admin;
import edu.bjtu.nourriture_web.bean.Customer;
import edu.bjtu.nourriture_web.common.RestfulServiceUtil;
import edu.bjtu.nourriture_web.idao.IAdminDao;
import edu.bjtu.nourriture_web.idao.IFlavourDao;
import edu.bjtu.nourriture_web.idao.IFoodCategoryDao;
import edu.bjtu.nourriture_web.idao.IRecipeCategoryDao;

@Path("admin")
public class AdminRestfulService {
	private IAdminDao adminDao;

	
	private JsonArray adminChildrenLinks;
	private JsonArray loginChildrenLinks;
	private JsonArray idChildrenLinks;
	

	public IAdminDao getAdminDao() {
		return adminDao;
	}

	public void setAdminDao(IAdminDao adminDao) {
		this.adminDao = adminDao;
	}
	
	{
	adminChildrenLinks=new JsonArray();
	RestfulServiceUtil.addChildrenLinks(adminChildrenLinks, "login", "/login", "GET");
	RestfulServiceUtil.addChildrenLinks(adminChildrenLinks, "get customer according to id", "/{id}", "GET");
	
	loginChildrenLinks = new JsonArray();
	idChildrenLinks = new JsonArray();
	}
	
	
	/** login by name and password **/
	@GET
	@Path("login")
	public String login(@QueryParam("name")String name,@QueryParam("password") String password){
		JsonObject ret = new JsonObject();
		//define error code
		final int ERROR_CODE_USER_NOT_EXIST = -1;
		final int ERROR_CODE_PASSWORD_NOT_VALIDATED = -2;
		final int ERROR_CODE_BAD_PARAM = -3;
		//check request parameters
		if(name == null || name.equals("") || password == null || password.equals("")){
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", loginChildrenLinks);
			return ret.toString();
		}
		//check if user name is exsit
		if(!adminDao.isNameExist(name)){
			ret.addProperty("errorCode", ERROR_CODE_USER_NOT_EXIST);
			ret.add("links", loginChildrenLinks);
			return ret.toString();
		}
		//check password
		int loginResult = adminDao.login(name, password);
		if(loginResult == -1){
			ret.addProperty("errorCode", ERROR_CODE_PASSWORD_NOT_VALIDATED);
			ret.add("links", loginChildrenLinks);
			return ret.toString();
		}
		ret.addProperty("id", loginResult);
		ret.add("links", loginChildrenLinks);
		return ret.toString();
	}
	
	public String getById(@PathParam("id") int id){
		JsonObject ret = new JsonObject();
		//select from database
		Admin admin= adminDao.getById(id);
		if(admin != null)
		{
			JsonObject jAdmin = transformAdmin(admin);
			ret.add("admin", jAdmin);
		}
		ret.add("links", idChildrenLinks);
		return ret.toString();
	}

	private JsonObject transformAdmin(Admin admin) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
