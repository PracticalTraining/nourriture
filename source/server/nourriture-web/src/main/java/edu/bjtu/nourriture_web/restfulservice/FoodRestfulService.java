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
import javax.ws.rs.QueryParam;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.bjtu.nourriture_web.bean.Customer;
import edu.bjtu.nourriture_web.bean.Food;
import edu.bjtu.nourriture_web.common.RestfulServiceUtil;
import edu.bjtu.nourriture_web.idao.ICustomerDao;
import edu.bjtu.nourriture_web.idao.IFlavourDao;
import edu.bjtu.nourriture_web.idao.IFoodCategoryDao;
import edu.bjtu.nourriture_web.idao.IFoodDao;
import edu.bjtu.nourriture_web.idao.ILocationDao;
import edu.bjtu.nourriture_web.idao.IManuFacturerDao;

@Path("food")
public class FoodRestfulService {
	//dao
	private IFoodDao foodDao;
	private IFlavourDao flavourDao;
	private ICustomerDao customerDao;
	private IFoodCategoryDao foodCategoryDao;
	private IManuFacturerDao manuFacturerDao;
	private ILocationDao locationDao;
	//direct children links
	private JsonArray foodChildrenLinks;
	private JsonArray idChildrenLinks;
	private JsonArray searchChildrenLinks;
	private JsonArray recommendChildrenLinks;
	private JsonArray searchByNameChildrenLinks;
	//get set method for spring IOC
	public IFoodDao getFoodDao() {
		return foodDao;
	}
		
	public void setFoodDao(IFoodDao foodDao) {
		this.foodDao = foodDao;
	}
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
	public IManuFacturerDao getManuFacturerDao() {
		return manuFacturerDao;
	}

	public void setManuFacturerDao(IManuFacturerDao manuFacturerDao) {
		this.manuFacturerDao = manuFacturerDao;
	}
	public ILocationDao getLocationDao() {
		return locationDao;
    }
    public void setLocationDao(ILocationDao locationDao) {
		this.locationDao = locationDao;
    }
	//initialize direct children links
	{
		foodChildrenLinks = new JsonArray();
		RestfulServiceUtil.addChildrenLinks(foodChildrenLinks, "search the food by id", "/{id}", "GET");
		RestfulServiceUtil.addChildrenLinks(foodChildrenLinks, "update the food by id", "/{id}", "PUT");
		RestfulServiceUtil.addChildrenLinks(foodChildrenLinks, "delete the food by id", "/{id}", "DELETE");
		idChildrenLinks = new JsonArray();
		searchChildrenLinks = new JsonArray();
		recommendChildrenLinks = new JsonArray();
		searchByNameChildrenLinks = new JsonArray();
	}
	/** add a food **/
	@POST
	public String addFood(@FormParam("name") String name,@FormParam("price") double price,
			@FormParam("picture") String picture,@FormParam("categoryId") int categoryId,@FormParam("flavourId") int flavourId
			,@FormParam("manufacturerId") int manufacturerId,@FormParam("produceLocationId") int produceLocationId,@FormParam("buyLocationId") 
	int buyLocationId){
		JsonObject ret = new JsonObject();
		//define error code
		final int ERROR_CODE_BAD_PARAM = -1;
		final int ERROR_CODE_CATEGORY_NOT_EXIST = -2;
		final int ERROR_CODE_FLAVOUR_NOT_EXIST = -3;
		final int ERROR_CODE_MANUFACTURER_NOT_EXIST = -4;
		final int ERROR_CODE_PRODUCELOCATION_NOT_EXIST = -5;
		final int ERROR_CODE_BUYLOCATION_NOT_EXIST = -6;
		
		//check request parameters
		if(name == null || name.equals("") || picture == null || picture.equals("") || price < 0|| categoryId < 0 || flavourId < 0
				|| manufacturerId < 0 || produceLocationId < 0 || buyLocationId < 0){
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", foodChildrenLinks);
			return ret.toString();
		}
		//check if  category is not exist
		if(!foodCategoryDao.isCategoryExist(categoryId)){
			ret.addProperty("errorCode", ERROR_CODE_CATEGORY_NOT_EXIST);
			ret.add("links", foodChildrenLinks);
			return ret.toString();
		}
		//check if flavour is not exist
		if(!flavourDao.isFlavourExist(flavourId)){
			ret.addProperty("errorCode", ERROR_CODE_FLAVOUR_NOT_EXIST);
			ret.add("links", foodChildrenLinks);
			return ret.toString();
		}
		//check if manufacture is not exist
		
		if(!manuFacturerDao.isManuFacturerExist(manufacturerId)){
			ret.addProperty("errorCode", ERROR_CODE_MANUFACTURER_NOT_EXIST);
			ret.add("links", foodChildrenLinks);
			return ret.toString();
		}
		//check if producelocation is not exist
		if(!locationDao.isLocationExist(produceLocationId)){
			ret.addProperty("errorCode", ERROR_CODE_PRODUCELOCATION_NOT_EXIST);
			ret.add("links", foodChildrenLinks);
			return ret.toString();
		}
		//check if buylocation is not exist
		if(!locationDao.isLocationExist(buyLocationId)){
			ret.addProperty("errorCode", ERROR_CODE_BUYLOCATION_NOT_EXIST);
			ret.add("links", foodChildrenLinks);
			return ret.toString();
		}
		//add one row to database
		Food food = new Food();
		food.setName(name);
		food.setPrice(price);
		food.setCategoryId(categoryId);
		food.setFlavourId(flavourId);
		food.setManufacturerId(manufacturerId);
		food.setProduceLocationId(produceLocationId);
		food.setBuyLocationId(buyLocationId);
		food.setPicture(picture);
		ret.addProperty("id", foodDao.add(food));
		ret.add("links", foodChildrenLinks);
		return ret.toString();
	}
	/** get detail information about a food by id **/
	@GET
	@Path("{id}")
	public String getById(@PathParam("id") int id) {
		JsonObject ret = new JsonObject();
		final int ERROR_CODE_FOOD_NOT_EXIST = -1;
		// select from database
		Food food = foodDao.getById(id);
		if (food == null) {
			ret.addProperty("errorCode", ERROR_CODE_FOOD_NOT_EXIST);
			ret.add("links", idChildrenLinks);
			return ret.toString();
		}
		ret.addProperty("id", id);
		ret.addProperty("name", food.getName());
		ret.addProperty("price", food.getPrice());
		ret.addProperty("categoryId", food.getCategoryId());
		ret.addProperty("flavourId", food.getFlavourId());
		ret.addProperty("manufacturerId", food.getManufacturerId());
		ret.addProperty("produceLocationId", food.getProduceLocationId());
		ret.addProperty("buyLocationId", food.getBuyLocationId());
		ret.addProperty("picture", food.getPicture());
		ret.add("links", idChildrenLinks);
		return ret.toString();

	}

	/** update food by id **/
	@PUT
	@Path("{id}")
	public String updateById(@PathParam("id") int id,
			@FormParam("name") String name, @FormParam("price") double price,
			@FormParam("picture") String picture,
			@FormParam("categoryId") int categoryId,
			@FormParam("flavourId") int flavourId,
			@FormParam("manufacturerId") int manufacturerId,
			@FormParam("produceLocationId") int produceLocationId,
			@FormParam("buyLocationId") int buyLocationId) {
		JsonObject ret = new JsonObject();
		//define error code
		final int ERROR_CODE_FOOD_NOT_EXIST=-1; 
		final int ERROR_CODE_BAD_PARAM = -2;
		final int ERROR_CODE_CATEGORY_NOT_EXIST = -3;
		final int ERROR_CODE_FLAVOUR_NOT_EXIST = -4;
		final int ERROR_CODE_MANUFACTURER_NOT_EXIST = -5;
		final int ERROR_CODE_PRODUCELOCATION_NOT_EXIST = -6;
		final int ERROR_CODE_BUYLOCATION_NOT_EXIST = -7;
		
		//check request parameters
		if(name == null || name.equals("") || picture == null || picture.equals("")  || price < 0|| categoryId < 0 || flavourId < 0
				|| manufacturerId < 0 || produceLocationId < 0 || buyLocationId < 0){
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", idChildrenLinks);
			return ret.toString();
		}
		//check if  food is not exist
		Food food = foodDao.getById(id);
		if (food == null) {
			ret.addProperty("errorCode", ERROR_CODE_FOOD_NOT_EXIST);
			ret.add("links", idChildrenLinks);
			return ret.toString();
		}
		//check if  category is not exist
		if(!foodCategoryDao.isCategoryExist(categoryId)){
			ret.addProperty("errorCode", ERROR_CODE_CATEGORY_NOT_EXIST);
			ret.add("links", idChildrenLinks);
			return ret.toString();
		}
		//check if flavour is not exist
		if(!flavourDao.isFlavourExist(flavourId)){
			ret.addProperty("errorCode", ERROR_CODE_FLAVOUR_NOT_EXIST);
			ret.add("links", idChildrenLinks);
			return ret.toString();
		}
		//check if manufacture is not exist
		
		if(!manuFacturerDao.isManuFacturerExist(manufacturerId)){
			ret.addProperty("errorCode", ERROR_CODE_MANUFACTURER_NOT_EXIST);
			ret.add("links", idChildrenLinks);
			return ret.toString();
		}
		//check if producelocation is not exist
		if(!locationDao.isLocationExist(produceLocationId)){
			ret.addProperty("errorCode", ERROR_CODE_PRODUCELOCATION_NOT_EXIST);
			ret.add("links", idChildrenLinks);
			return ret.toString();
		}
		//check if buylocation is not exist
		if(!locationDao.isLocationExist(buyLocationId)){
			ret.addProperty("errorCode", ERROR_CODE_BUYLOCATION_NOT_EXIST);
			ret.add("links", idChildrenLinks);
			return ret.toString();
		}

		food.setName(name);
		food.setPrice(price);
		food.setCategoryId(categoryId);
		food.setFlavourId(flavourId);
		food.setManufacturerId(manufacturerId);
		food.setProduceLocationId(produceLocationId);
		food.setBuyLocationId(buyLocationId);
		food.setPicture(picture);
		foodDao.update(food);
		ret.addProperty("result", 0);
		ret.add("links", idChildrenLinks);
		return ret.toString();

	}

	/** delete food by id **/
	@DELETE
	@Path("{id}")
	public String deleteFood(@PathParam("id") int id) {
		JsonObject ret = new JsonObject();
		//define error code
		final int ERROR_CODE_FOOD_NOT_EXIST = -1;
        Food food = foodDao.getById(id);
		//check if food is exist
		if (food == null) {
			ret.addProperty("errorCode", ERROR_CODE_FOOD_NOT_EXIST);
			ret.add("links", idChildrenLinks);
			return ret.toString();
		}
		foodDao.deletebyid(id);
		ret.addProperty("result", 0);
		ret.add("links", idChildrenLinks);
        return ret.toString();
    }
	
	/** search the food**/
	@GET
	@Path("search")
	public String searchBySift(@QueryParam("fromPrice") @DefaultValue("-1") double fromPrice,@QueryParam("toPrice") @DefaultValue("-1") double toPrice
		,@QueryParam("categoryIds") @DefaultValue("") String categoryIds,@QueryParam("flavourIds") @DefaultValue("") String flavourIds,
		@QueryParam("produceRegionIds") @DefaultValue("") String produceRegionIds,@QueryParam("buyRegionIds") @DefaultValue("") String buyRegionIds){
		JsonObject ret = new JsonObject();
		String[] categoryids = categoryIds.split(",");	
		String[] flavourids = flavourIds.split(",");
		String[] produceregionids = produceRegionIds.split(",");
		String[] buyregionids = buyRegionIds.split(",");
		List<Food> listResult = foodDao.search(fromPrice, toPrice, categoryids, flavourids, produceregionids, buyregionids);
		
		JsonArray foods = new JsonArray();
		for(Food food:listResult){
			JsonObject jFood = new JsonObject();
			jFood.addProperty("id",food.getId());
			jFood.addProperty("name", food.getName());
			jFood.addProperty("price", food.getPrice());
			jFood.addProperty("picture", food.getPicture());
			jFood.addProperty("categoryId", food.getCategoryId());
			jFood.addProperty("flavourId", food.getFlavourId());
			jFood.addProperty("manufacturerId", food.getManufacturerId());
			jFood.addProperty("produceLocationId", food.getProduceLocationId());
			jFood.addProperty("buyLocationId", food.getBuyLocationId());
			
			foods.add(jFood);
		}
		ret.add("foods", foods);
		ret.add("links", searchChildrenLinks);
		return ret.toString();
	   
	}
	/** recommend the food **/
	@GET
	@Path("recommend")
	public String recommendByInterest(@QueryParam("customerId") int customerId){
		JsonObject ret = new JsonObject();
		//define error code
		final int ERROR_CODE_CUSTOMER_NOT_EXIST=-1;
		//select from database
		Customer customer = customerDao.getById(customerId);
		if(customer == null){
			ret.addProperty("errorCode", ERROR_CODE_CUSTOMER_NOT_EXIST);
			ret.add("links", recommendChildrenLinks);
			return ret.toString();
		}
		String interestFlavourIds = customer.getInterestFlavourIds();
		String interestFoodCategoryIds = customer.getInterestFoodCategoryIds();
		String[] interestflavourids = interestFlavourIds.split(",");
		String[] interestfoodcategoryids = interestFoodCategoryIds.split(",");
		List<Food> listResult = foodDao.search(-1, -1, interestfoodcategoryids, interestflavourids, null, null);
		JsonArray foods = new JsonArray();
		for(Food food:listResult){
			JsonObject jFood = new JsonObject();
			jFood.addProperty("id",food.getId());
			jFood.addProperty("name", food.getName());
			jFood.addProperty("price", food.getPrice());
			jFood.addProperty("picture", food.getPicture());
			jFood.addProperty("categoryId", food.getCategoryId());
			jFood.addProperty("flavourId", food.getFlavourId());
			jFood.addProperty("manufacturerId", food.getManufacturerId());
			jFood.addProperty("produceLocationId", food.getProduceLocationId());
			jFood.addProperty("buyLocationId", food.getBuyLocationId());
			
			foods.add(jFood);
		}
		ret.add("foods", foods);
		ret.add("links", searchChildrenLinks);
		return ret.toString();
	}
	
	@GET
	@Path("searchByName")
	public String searchByName(@QueryParam("name") String name){
		JsonObject ret = new JsonObject();
		//define error code
		final int ERROR_CODE_BAD_PARAM = -1;
		
		if(name == null || name.equals("")){
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", searchByNameChildrenLinks);
			return ret.toString();
		}
		List<Food> listResult = foodDao.search(name);
		JsonArray foods = new JsonArray();
		for(Food food:listResult){
			JsonObject jFood = new JsonObject();
			jFood.addProperty("id",food.getId());
			jFood.addProperty("name", food.getName());
			jFood.addProperty("price", food.getPrice());
			jFood.addProperty("picture", food.getPicture());
			jFood.addProperty("categoryId", food.getCategoryId());
			jFood.addProperty("flavourId", food.getFlavourId());
			jFood.addProperty("manufacturerId", food.getManufacturerId());
			jFood.addProperty("produceLocationId", food.getProduceLocationId());
			jFood.addProperty("buyLocationId", food.getBuyLocationId());
			
			foods.add(jFood);
		}
		ret.add("foods", foods);
		ret.add("links", searchByNameChildrenLinks);
		return ret.toString();
	}
}
