package edu.bjtu.nourriture_web.restfulservice;


import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.bjtu.nourriture_web.bean.Customer;
import edu.bjtu.nourriture_web.bean.Food;
import edu.bjtu.nourriture_web.idao.ICustomerDao;
import edu.bjtu.nourriture_web.idao.IFoodDao;

import javax.imageio.*;

@Path("food")
public class FoodRestfulService {
	//dao
	private IFoodDao foodDao;
	//direct children links
	private JsonArray foodChildrenLinks;
	private JsonArray idChildrenLinks;
	private JsonArray searchChildrenLinks;
	//get set method for spring IOC
	public IFoodDao getFoodDao() {
		return foodDao;
	}
		
	public void setFoodDao(IFoodDao foodDao) {
		this.foodDao = foodDao;
	}
	{
	//initialize direct children links
		foodChildrenLinks = new JsonArray();
	}
	/** add a customer **/
	@POST
	public String addCustomer(@FormParam("name") String name,@FormParam("price") double price,
			@FormParam("picture") String picture,@FormParam("categoryId") int categoryId,@FormParam("flavourId") int flavourId
			,@FormParam("manufacturerId") int manufacturerId,@FormParam("produceLocationId") int produceLocationId,@FormParam("buyLocationId") 
	int buyLocationId){
		JsonObject ret = new JsonObject();
		//define error code
		final int ERROR_CODE_BAD_PARAM = -1;
		final int ERROR_CODE_CATEGORY_NOT_EXIST = -2;
		final int ERROR_CODE_FLAVOUR_NOT_EXIST = -3;
		final int ERROR_CODE_MANUFACTURER_NOT_EXIST = -4;
		
		//check request parameters
		if(name == null || name.equals("") || price < 0|| categoryId < 0 || flavourId < 0
				|| manufacturerId < 0 || produceLocationId < 0 || buyLocationId < 0){
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", foodChildrenLinks);
			return ret.toString();
		}
		//check if  category is not exist
		if(!foodDao.isCategoryExist(categoryId)){
			ret.addProperty("errorCode", ERROR_CODE_CATEGORY_NOT_EXIST);
			ret.add("links", foodChildrenLinks);
			return ret.toString();
		}
		//check if flavour is not exist
		if(!foodDao.isFlavourExist(flavourId)){
			ret.addProperty("errorCode", ERROR_CODE_FLAVOUR_NOT_EXIST);
			ret.add("links", foodChildrenLinks);
			return ret.toString();
		}
		//check if manufacture is not exist
		if(!foodDao.isManufacturerExist(manufacturerId)){
			ret.addProperty("errorCode", ERROR_CODE_MANUFACTURER_NOT_EXIST);
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
	/** get detail information about a customer by id **/
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
		final int ERROR_CODE_CATEGORY_NOT_EXIST = -3;
		final int ERROR_CODE_FOOD_NOT_EXIST = -1;
		final int ERROR_CODE_BAD_PARAM = -2;
		JsonObject ret = new JsonObject();
		Food food = foodDao.getById(id);
		//check if food is exist
		if (food == null) {
			ret.addProperty("errorCode", ERROR_CODE_FOOD_NOT_EXIST);
			ret.add("links", idChildrenLinks);
			return ret.toString();
		}
		//check if  category is not exist
		if(!foodDao.isCategoryExist(categoryId)){
			ret.addProperty("errorCode", ERROR_CODE_CATEGORY_NOT_EXIST);
			ret.add("links", idChildrenLinks);
			return ret.toString();
		}
		//check request parameters
		if (name == null || name.equals("") || price < 0 || categoryId < 0
				|| flavourId < 0 || manufacturerId < 0 || produceLocationId < 0
				|| buyLocationId < 0) {
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
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
		ret.addProperty("id", foodDao.add(food));
		ret.add("links", idChildrenLinks);
		return ret.toString();

	}

	/** delete food by id **/
	@DELETE
	@Path("{id}")
	public String deleteInterest(@PathParam("id") int id) {
		JsonObject ret = new JsonObject();
		final int ERROR_CODE_FOOD_NOT_EXIST = -1;

		Food food = foodDao.getById(id);
		//check if food is exist
		if (food == null) {
			ret.addProperty("errorCode", ERROR_CODE_FOOD_NOT_EXIST);
			ret.add("links", idChildrenLinks);
			return ret.toString();
		}

		return ret.toString();

	}

}
