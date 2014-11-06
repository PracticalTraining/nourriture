package edu.bjtu.nourriture_web.restfulservice;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.bjtu.nourriture_web.bean.Customer;
import edu.bjtu.nourriture_web.bean.Flavour;
import edu.bjtu.nourriture_web.common.JsonUtil;
import edu.bjtu.nourriture_web.common.RestfulServiceUtil;
import edu.bjtu.nourriture_web.idao.ICustomerDao;
import edu.bjtu.nourriture_web.idao.IFlavourDao;
import edu.bjtu.nourriture_web.idao.IFoodCategoryDao;
import edu.bjtu.nourriture_web.idao.IRecipeCategoryDao;

@Path("customer")
@Produces("application/json")
public class CustomerRestfulService {
	private ICustomerDao customerDao;
	private IFlavourDao flavourDao;
	private IFoodCategoryDao foodCategoryDao;
	private IRecipeCategoryDao recipeCategoryDao;
	private JsonArray customerChildrenLinks;
	private JsonArray searchChildrenLinks;
	
	public ICustomerDao getCustomerDao() {
		return customerDao;
	}
	
	public void setCustomerDao(ICustomerDao customerDao) {
		this.customerDao = customerDao;
	}
	
	public IFlavourDao getFlavourDao() {
		return flavourDao;
	}

	public void setFlavourDao(IFlavourDao flavourDao) {
		this.flavourDao = flavourDao;
	}

	public IFoodCategoryDao getFoodCategoryDao() {
		return foodCategoryDao;
	}

	public void setFoodCategoryDao(IFoodCategoryDao foodCategoryDao) {
		this.foodCategoryDao = foodCategoryDao;
	}

	public IRecipeCategoryDao getRecipeCategoryDao() {
		return recipeCategoryDao;
	}

	public void setRecipeCategoryDao(IRecipeCategoryDao recipeCategoryDao) {
		this.recipeCategoryDao = recipeCategoryDao;
	}


	{
		//initialize direct children links
		customerChildrenLinks = new JsonArray();
		RestfulServiceUtil.addChildrenLinks(customerChildrenLinks, "search customer", "/search", "GET");
		RestfulServiceUtil.addChildrenLinks(customerChildrenLinks, "login", "/login", "GET");
		RestfulServiceUtil.addChildrenLinks(customerChildrenLinks, "get customer according to id", "/{id}", "GET");
		searchChildrenLinks = new JsonArray();
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
	
	/** search customer by name **/
	@GET
	@Path("search")
	public String searchByName(@QueryParam("name") String name){
		JsonObject ret = new JsonObject();
		//define error code
		final int ERROR_CODE_NO_RESULT = -1;
		final int ERROR_CODE_BAD_PARAM = -2;
		//check request parameters
		if(name == null || name.equals("")){
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", searchChildrenLinks);
			return ret.toString();
		}
		//search in the database
		List<Customer> list = customerDao.searchByName(name);
		if(list.isEmpty()){
			ret.addProperty("errorCode", ERROR_CODE_NO_RESULT);
			ret.add("links", searchChildrenLinks);
			return ret.toString();
		}
		JsonArray customers = new JsonArray();
		for(Customer customer : list){
			JsonObject jCustomer = JsonUtil.beanToJson(customer);
			customers.add(jCustomer);
			//get flavour infomations
			String str = customer.getInterestFlavourIds();
			JsonArray flavours = new JsonArray();
			if(str != null && !str.equals(""))
			{
				String[] ids = str.split(",");
				for(String id : ids){
					int fId = Integer.parseInt(id);
					JsonObject flavour = JsonUtil.beanToJson(flavourDao.getById(fId));
					flavour.remove("topFlavour");
					flavour.remove("superiorFlavourId");
					flavours.add(flavour);
				}
			}
			jCustomer.remove("interestFlavours");
			jCustomer.add("interestFlavours", flavours);
			//get food category informations
			str = customer.getInterestFoodCategoryIds();
			JsonArray foodCategorys = new JsonArray();
			if(str != null && !str.equals(""))
			{
				String[] ids = str.split(",");
				for(String id : ids){
					int fId = Integer.parseInt(id);
					JsonObject foodCategory = JsonUtil.beanToJson(foodCategoryDao.getById(fId));
					foodCategory.remove("topCategory");
					foodCategory.remove("superiorCategoryId");
					flavours.add(foodCategory);
				}
			}
			jCustomer.remove("interestfoodCategorys");
			jCustomer.add("interestfoodCategorys", foodCategorys);
			//get food recipe informations
			str = customer.getInterestRecipeCategoryIds();
			JsonArray recipeCategorys = new JsonArray();
			if(str != null && !str.equals(""))
			{
				String[] ids = str.split(",");
				for(String id : ids){
					int fId = Integer.parseInt(id);
					JsonObject recipeCategory = JsonUtil.beanToJson(recipeCategoryDao.getById(fId));
					recipeCategory.remove("topCategory");
					recipeCategory.remove("superiorCategoryId");
					flavours.add(recipeCategory);
				}
			}
			jCustomer.remove("interestrecipeCategorys");
			jCustomer.add("interestrecipeCategorys", recipeCategorys);
		}
		ret.add("customers", customers);
		ret.add("links", searchChildrenLinks);
		return ret.toString();
	}
}
