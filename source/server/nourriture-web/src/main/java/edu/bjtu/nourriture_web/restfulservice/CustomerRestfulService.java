package edu.bjtu.nourriture_web.restfulservice;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.bjtu.nourriture_web.bean.Customer;
import edu.bjtu.nourriture_web.common.JsonUtil;
import edu.bjtu.nourriture_web.common.RestfulServiceUtil;
import edu.bjtu.nourriture_web.idao.ICustomerDao;
import edu.bjtu.nourriture_web.idao.IFlavourDao;
import edu.bjtu.nourriture_web.idao.IFoodCategoryDao;
import edu.bjtu.nourriture_web.idao.IRecipeCategoryDao;

@Path("customer")
@Produces("application/json;charset=UTF-8")
public class CustomerRestfulService {
	//dao
	private ICustomerDao customerDao;
	private IFlavourDao flavourDao;
	private IFoodCategoryDao foodCategoryDao;
	private IRecipeCategoryDao recipeCategoryDao;
	//direct children links
	private JsonArray customerChildrenLinks;
	private JsonArray searchChildrenLinks;
	private JsonArray loginChildrenLinks;
	private JsonArray idChildrenLinks;
	private JsonArray interestChildrenLinks;
	
	//get set method for spring IOC
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
		RestfulServiceUtil.addChildrenLinks(customerChildrenLinks, "update customer according to id", "/{id}", "PUT");
		searchChildrenLinks = new JsonArray();
		loginChildrenLinks = new JsonArray();
		idChildrenLinks = new JsonArray();
		RestfulServiceUtil.addChildrenLinks(idChildrenLinks, "get customer interests", "/{interest}", "GET");
		RestfulServiceUtil.addChildrenLinks(idChildrenLinks, "add a customer interest", "/{interest}", "POST");
		RestfulServiceUtil.addChildrenLinks(idChildrenLinks, "delete a customer interest", "/{interest}", "DELETE");
		interestChildrenLinks = new JsonArray();
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
			JsonObject jCustomer = transformCustomer(customer);
			customers.add(jCustomer);
		}
		ret.add("customers", customers);
		ret.add("links", searchChildrenLinks);
		return ret.toString();
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
		if(!customerDao.isNameExist(name)){
			ret.addProperty("errorCode", ERROR_CODE_USER_NOT_EXIST);
			ret.add("links", loginChildrenLinks);
			return ret.toString();
		}
		//check password
		int loginResult = customerDao.login(name, password);
		if(loginResult == -1){
			ret.addProperty("errorCode", ERROR_CODE_PASSWORD_NOT_VALIDATED);
			ret.add("links", loginChildrenLinks);
			return ret.toString();
		}
		ret.addProperty("id", loginResult);
		ret.add("links", loginChildrenLinks);
		return ret.toString();
	}
	
	/** get detail information about a customer by id **/
	@GET
	@Path("{id}")
	public String getById(@PathParam("id") int id){
		JsonObject ret = new JsonObject();
		//select from database
		Customer customer = customerDao.getById(id);
		if(customer != null)
		{
			JsonObject jCustomer = transformCustomer(customer);
			ret.add("customer", jCustomer);
		}
		ret.add("links", idChildrenLinks);
		return ret.toString();
	}
	
	/** update customer basic information **/
	@PUT
	@Path("{id}")
	public String updateCustomer(@PathParam("id") int id,
			@FormParam("age") @DefaultValue("-1") int age,
			@FormParam("sex") @DefaultValue("-1") int sex){
		JsonObject ret = new JsonObject();
		//define error code
		final int ERROR_CODE_USER_NOT_EXIST = -1;
		final int ERROR_CODE_BAD_PARAM = -2;
		//check request parameters
		if((sex != 0 && sex != 1) || age < 0){
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", idChildrenLinks);
			return ret.toString();
		}
		//check if user exsit
		Customer customer = customerDao.getById(id);
		if(customer == null){
			ret.addProperty("errorCode", ERROR_CODE_USER_NOT_EXIST);
			ret.add("links", idChildrenLinks);
			return ret.toString();
		}
		customer.setAge(age);
		customer.setSex(sex);
		customerDao.update(customer);
		ret.addProperty("result", 0);
		ret.add("links", idChildrenLinks);
		return ret.toString();
	}

	/** get insterests **/
	@GET
	@Path("{id}/{interest}")
	public String getInterests(@PathParam("id") int id,@PathParam("interest") String interest){
		JsonObject ret = new JsonObject();
		//define error code
	    final int ERROR_CODE_USER_NOT_EXIST = -1;
	    final int ERROR_CODE_INTEREST_NOT_SET = -2;
	    final int ERROR_CODE_BAD_PARAM = -3;
	    //check request parameters
	    if(id <= 0 || 
				interest == null || interest.equals("") || 
				(!interest.equals("flavour") && !interest.equals("foodCategory") && !interest.equals("recipeCategory"))){
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", interestChildrenLinks);
			return ret.toString();
		}
	    //check if user exsit
  		Customer customer = customerDao.getById(id);
  		if(customer == null){
  			ret.addProperty("errorCode", ERROR_CODE_USER_NOT_EXIST);
  			ret.add("links", interestChildrenLinks);
  			return ret.toString();
  		}
  	    //check if interest is not setted
		if(interest.equals("flavour")){
			String sIds = customer.getInterestFlavourIds();
			if(sIds == null || sIds.equals("")){
				ret.addProperty("errorCode", ERROR_CODE_INTEREST_NOT_SET);
				ret.add("links", interestChildrenLinks);
				return ret.toString();
			} else {
				JsonArray flavours = new JsonArray();
				String[] ids = sIds.split(",");
				for(String sid : ids){
					int fId = Integer.parseInt(sid);
					JsonObject flavour = JsonUtil.beanToJson(flavourDao.getById(fId));
					flavour.remove("topFlavour");
					flavour.remove("superiorFlavourId");
					flavours.add(flavour);
				}
				ret.add("flavours", flavours);
				ret.add("links", interestChildrenLinks);
				return ret.toString();
			}
		} else if(interest.equals("foodCategory")){
			String sIds = customer.getInterestFoodCategoryIds();
			if(sIds == null || sIds.equals("")){
				ret.addProperty("errorCode", ERROR_CODE_INTEREST_NOT_SET);
				ret.add("links", interestChildrenLinks);
				return ret.toString();
			} else {
				JsonArray foodCategorys = new JsonArray();
				String[] ids = sIds.split(",");
				for(String sid : ids){
					int fId = Integer.parseInt(sid);
					JsonObject foodCategory = JsonUtil.beanToJson(foodCategoryDao.getById(fId));
					foodCategory.remove("topCategory");
					foodCategory.remove("superiorCategoryId");
					foodCategorys.add(foodCategory);
				}
				ret.add("foodCategorys", foodCategorys);
				ret.add("links", interestChildrenLinks);
				return ret.toString();
			}
		} else {
			String sIds = customer.getInterestRecipeCategoryIds();
			if(sIds == null || sIds.equals("")){
				ret.addProperty("errorCode", ERROR_CODE_INTEREST_NOT_SET);
				ret.add("links", interestChildrenLinks);
				return ret.toString();
			} else {
				JsonArray recipeCategorys = new JsonArray();
				String[] ids = sIds.split(",");
				for(String sid : ids){
					int fId = Integer.parseInt(sid);
					JsonObject recipeCategory = JsonUtil.beanToJson(recipeCategoryDao.getById(fId));
					recipeCategory.remove("topCategory");
					recipeCategory.remove("superiorCategoryId");
					recipeCategorys.add(recipeCategory);
				}
				ret.add("foodCategorys", recipeCategorys);
				ret.add("links", interestChildrenLinks);
				return ret.toString();
			}
		}
	}
	
	/** add a interest **/
	@POST
	@Path("{id}/{interest}")
	public String addInterest(@PathParam("id") int id,@PathParam("interest") String interest,
			@FormParam("id") int InId){
		JsonObject ret = new JsonObject();
		//define error code
		final int ERROR_CODE_USER_NOT_EXIST = -1;
		final int ERROR_CODE_INTEREST_NOT_EXIST = -2;
		final int ERROR_CODE_INTEREST_ALREADY_SET = -3;
		final int ERROR_CODE_BAD_PARAM = -4;
		//check request parameters
		if(id <= 0 || 
				interest == null || interest.equals("") || 
				(!interest.equals("flavour") && !interest.equals("foodCategory") && !interest.equals("recipeCategory")) ||
				InId < 0){
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", interestChildrenLinks);
			return ret.toString();
		}
		//check if user exsit
		Customer customer = customerDao.getById(id);
		if(customer == null){
			ret.addProperty("errorCode", ERROR_CODE_USER_NOT_EXIST);
			ret.add("links", interestChildrenLinks);
			return ret.toString();
		}
		//check if interest exsit
		if((interest.equals("flavour") && flavourDao.getById(InId) == null) ||
				(interest.equals("foodCategory") && foodCategoryDao.getById(InId) == null) ||
				(interest.equals("recipeCategory") && recipeCategoryDao.getById(InId) == null)){
			ret.addProperty("errorCode", ERROR_CODE_INTEREST_NOT_EXIST);
			ret.add("links", interestChildrenLinks);
			return ret.toString();
		}
		//check if interest is already setted
		if(interest.equals("flavour")){
			String sIds = customer.getInterestFlavourIds();
			if(sIds != null){
				if(sIds.contains(String.valueOf(InId))){
					ret.addProperty("errorCode", ERROR_CODE_INTEREST_ALREADY_SET);
					ret.add("links", interestChildrenLinks);
					return ret.toString();
				}
			}
		} else if(interest.equals("foodCategory")){
			String sIds = customer.getInterestFoodCategoryIds();
			if(sIds != null){
				if(sIds.contains(String.valueOf(InId))){
					ret.addProperty("errorCode", ERROR_CODE_INTEREST_ALREADY_SET);
					ret.add("links", interestChildrenLinks);
					return ret.toString();
				}
			}
		} else {
			String sIds = customer.getInterestRecipeCategoryIds();
			if(sIds != null){
				if(sIds.contains(String.valueOf(InId))){
					ret.addProperty("errorCode", ERROR_CODE_INTEREST_ALREADY_SET);
					ret.add("links", interestChildrenLinks);
					return ret.toString();
				}
			}
		}
		//add interest to database
		if(interest.equals("flavour")){
			String sIds = customer.getInterestFlavourIds();
			if(sIds == null || sIds.equals(""))
				sIds = String.valueOf(InId);
			else
				sIds += ("," + InId);
			customer.setInterestFlavourIds(sIds);
		} else if(interest.equals("foodCategory")){
			String sIds = customer.getInterestFoodCategoryIds();
			if(sIds == null || sIds.equals(""))
				sIds = String.valueOf(InId);
			else
				sIds += ("," + InId);
			customer.setInterestFoodCategoryIds(sIds);
		} else {
			String sIds = customer.getInterestRecipeCategoryIds();
			if(sIds == null || sIds.equals(""))
				sIds = String.valueOf(InId);
			else
				sIds += ("," + InId);
			customer.setInterestRecipeCategoryIds(sIds);
		}
		customerDao.update(customer);
		ret.addProperty("result", 0);
		ret.add("links", interestChildrenLinks);
		return ret.toString();
	}
	
	/** delete a interest **/
	@DELETE
	@Path("{id}/{interest}")
	public String deleteInterest(@PathParam("id") int id,@PathParam("interest") String interest,
			@FormParam("id") int InId){
		JsonObject ret = new JsonObject();
		//define error code
		final int ERROR_CODE_USER_NOT_EXIST = -1;
		final int ERROR_CODE_INTEREST_NOT_EXIST = -2;
		final int ERROR_CODE_INTEREST_NOT_SET = -3;
		final int ERROR_CODE_BAD_PARAM = -4;
		//check request parameters
		if(id <= 0 || 
				interest == null || interest.equals("") || 
				(!interest.equals("flavour") && !interest.equals("foodCategory") && !interest.equals("recipeCategory")) ||
				InId < 0){
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", interestChildrenLinks);
			return ret.toString();
		}
		//check if user exsit
		Customer customer = customerDao.getById(id);
		if(customer == null){
			ret.addProperty("errorCode", ERROR_CODE_USER_NOT_EXIST);
			ret.add("links", interestChildrenLinks);
			return ret.toString();
		}
		//check if interest exsit
		if((interest.equals("flavour") && flavourDao.getById(InId) == null) ||
				(interest.equals("foodCategory") && foodCategoryDao.getById(InId) == null) ||
				(interest.equals("recipeCategory") && recipeCategoryDao.getById(InId) == null)){
			ret.addProperty("errorCode", ERROR_CODE_INTEREST_NOT_EXIST);
			ret.add("links", interestChildrenLinks);
			return ret.toString();
		}
		//check if interest is already setted
		if(interest.equals("flavour")){
			String sIds = customer.getInterestFlavourIds();
			if(sIds == null || sIds.equals("") || !sIds.contains(String.valueOf(InId))){
				ret.addProperty("errorCode", ERROR_CODE_INTEREST_NOT_SET);
				ret.add("links", interestChildrenLinks);
				return ret.toString();
			}
		} else if(interest.equals("foodCategory")){
			String sIds = customer.getInterestFoodCategoryIds();
			if(sIds == null || sIds.equals("") || !sIds.contains(String.valueOf(InId))){
				ret.addProperty("errorCode", ERROR_CODE_INTEREST_NOT_SET);
				ret.add("links", interestChildrenLinks);
				return ret.toString();
			}
		} else {
			String sIds = customer.getInterestRecipeCategoryIds();
			if(sIds == null || sIds.equals("") || !sIds.contains(String.valueOf(InId))){
				ret.addProperty("errorCode", ERROR_CODE_INTEREST_NOT_SET);
				ret.add("links", interestChildrenLinks);
				return ret.toString();
			}
		}
		//delete interest to database
		if(interest.equals("flavour")){
			String sIds = customer.getInterestFlavourIds();
			customer.setInterestFlavourIds(RestfulServiceUtil.deleteIdFromIdList(InId, sIds));
		} else if(interest.equals("foodCategory")){
			String sIds = customer.getInterestFoodCategoryIds();
			customer.setInterestFoodCategoryIds(RestfulServiceUtil.deleteIdFromIdList(InId, sIds));
		} else {
			String sIds = customer.getInterestRecipeCategoryIds();
			customer.setInterestRecipeCategoryIds(RestfulServiceUtil.deleteIdFromIdList(InId, sIds));
		}
		customerDao.update(customer);
		ret.addProperty("result", 0);
		ret.add("links", interestChildrenLinks);
		return ret.toString();
	}
	
	/**
	 * transform customer from bean to json,customer interest ids will be transformed to detail information
	 * @param bean form of customer
	 * @return json form of customer
	 */
	private JsonObject transformCustomer(Customer customer){
		JsonObject jCustomer = JsonUtil.beanToJson(customer);
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
		jCustomer.remove("interestFlavourIds");
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
		jCustomer.remove("interestFoodCategoryIds");
		jCustomer.add("interestFoodCategorys", foodCategorys);
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
		jCustomer.remove("interestRecipeCategoryIds");
		jCustomer.add("interestRecipeCategorys", recipeCategorys);
		return jCustomer;
	}
}
