package edu.bjtu.nourriture_web.restfulservice;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.bjtu.nourriture_web.bean.Customer;
import edu.bjtu.nourriture_web.common.RestfulServiceUtil;
import edu.bjtu.nourriture_web.idao.ICustomerDao;

@Path("customer")
@Produces("application/json")
public class CustomerRestfulService {
	private ICustomerDao customerDao;
	private JsonArray customerChildrenLinks;
	
	public ICustomerDao getCustomerDao() {
		return customerDao;
	}

	public void setCustomerDao(ICustomerDao customerDao) {
		this.customerDao = customerDao;
	}
	
	{
		//initialize direct children links
		customerChildrenLinks = new JsonArray();
		RestfulServiceUtil.addChildrenLinks(customerChildrenLinks, "search customer", "/search", "GET");
		RestfulServiceUtil.addChildrenLinks(customerChildrenLinks, "login", "/login", "GET");
		RestfulServiceUtil.addChildrenLinks(customerChildrenLinks, "get customer according to id", "/{id}", "GET");
	}

	/** add a customer **/
	@POST
	public String addCustomer(@FormParam("name") String name,@FormParam("password") String password,
			@FormParam("sex") int sex,@FormParam("age") int age){
		JsonObject ret = new JsonObject();
		//define error code
		final int ERROR_CODE_USER_EXIST = -1;
		final int ERROR_CODE_BAD_PARAM = -2;
		//check request parameters
		if(name == null || name.equals("") || password == null || password.equals("")
				|| (sex != 0 && sex != 1) || age < 0){
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", customerChildrenLinks);
			return ret.toString();
		}
		//check if user name is already exist
		if(customerDao.isNameExist(name)){
			ret.addProperty("errorCode", ERROR_CODE_USER_EXIST);
			ret.add("links", customerChildrenLinks);
			return ret.toString();
		}
		//add one row to database
		Customer customer = new Customer();
		customer.setName(name);
		customer.setPassword(password);
		customer.setSex(sex);
		customer.setAge(age);
		ret.addProperty("id", customerDao.add(customer));
		ret.add("links", customerChildrenLinks);
		return ret.toString();
	}
}
