package edu.bjtu.nourriture_web.restfulservice;

import java.util.ArrayList;
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
import edu.bjtu.nourriture_web.bean.FoodCategory;
import edu.bjtu.nourriture_web.bean.RecipeCategory;
import edu.bjtu.nourriture_web.common.JsonUtil;
import edu.bjtu.nourriture_web.common.RestfulServiceUtil;
import edu.bjtu.nourriture_web.dao.FoodCategoryDao;
import edu.bjtu.nourriture_web.dao.FoodDao;
import edu.bjtu.nourriture_web.dao.RecipeCategoryDao;
import edu.bjtu.nourriture_web.idao.IFoodCategoryDao;

@Path("foodCategory")
public class FoodCategoryRestfulService {
	FoodCategoryDao foodCategoryDao;
	// direct children links
	private JsonArray FoodCategoryChildrenLinks;

	// get set method for spring IOC

	public FoodCategoryDao getFoodCategoryDao() {
		return foodCategoryDao;
	}

	public void setFoodCategoryDao(FoodCategoryDao foodCategoryDao) {
		this.foodCategoryDao = foodCategoryDao;
	}

	{
		// initialize direct children links
		FoodCategoryChildrenLinks = new JsonArray();
		RestfulServiceUtil.addChildrenLinks(FoodCategoryChildrenLinks,
				"get superior food", "/getTop", "GET");
		RestfulServiceUtil.addChildrenLinks(FoodCategoryChildrenLinks,
				"get rfood's detail information", "/{id}", "GET");
		RestfulServiceUtil.addChildrenLinks(FoodCategoryChildrenLinks,
				"update classification of food", "/{id}", "PUT");
		RestfulServiceUtil.addChildrenLinks(FoodCategoryChildrenLinks,
				"delete classification of food", "/{id}", "DELETE");
	}

	/** add FoodCategory **/
	@POST
	public String addFoodCategory(@FormParam("name") String name,
			@FormParam("topCategory") boolean topCategory,
			@FormParam("superiorCategoryId") int superiorCategoryId) {
		JsonObject ret = new JsonObject();
		// define errorCode
		final int ERROR_CODE_BAD_PARAM = -1;
		final int ERROR_CODE_NOTTOPCATEGORY_SUPERIORCATEGORY_NOTEXIST = -2;
		final int ERROR_CODE_TOPCATEGORY__SUPERIORCATEGORYEXIST = -3;

		// check bad request parameter
		System.out.println(name);
		if (name.equals("") || superiorCategoryId < 0) {
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", FoodCategoryChildrenLinks);
			return ret.toString();
		}
		// check parameters
		if (!topCategory&&
				 foodCategoryDao
						.isSuperiorCategoryIdExist(superiorCategoryId) == false) {
			ret.addProperty("errorCode",
					ERROR_CODE_NOTTOPCATEGORY_SUPERIORCATEGORY_NOTEXIST);
			ret.add("links", FoodCategoryChildrenLinks);
			return ret.toString();
		}
		// check parameters
		if (topCategory
				&& superiorCategoryId!=0) {
			ret.addProperty("errorCode",
					ERROR_CODE_TOPCATEGORY__SUPERIORCATEGORYEXIST);
			ret.add("links", FoodCategoryChildrenLinks);
			return ret.toString();
		}
		
			// add one row to database
			FoodCategory foodCategory = new FoodCategory();
			foodCategory.setName(name);
			foodCategory.setTopCategory(topCategory);
			foodCategory.setSuperiorCategoryId(superiorCategoryId);
			ret.addProperty("id", foodCategoryDao.add(foodCategory));
			ret.add("links", FoodCategoryChildrenLinks);
		return ret.toString();

	}

	/** get the superior classfication **/
	@GET
	@Path("getTop")
	public String getSuperiorById(@QueryParam("id") int id) {
		JsonObject ret = new JsonObject();
		// define errorCode
		final int ERROR_CODE_BAD_PARAM = -1;
		final int ERROR_CODE_RECIPECATEGORY_NOT_EXIST = -2;
		final int ERROR_CODE_HASNOR_TOPCATEGROY = -3;
		// check request parameters
		if (id <= 0) {
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", FoodCategoryChildrenLinks);
			return ret.toString();
		}
		if (foodCategoryDao.isCategoryExist(id) == false) {
			ret.addProperty("errorCode", ERROR_CODE_RECIPECATEGORY_NOT_EXIST);
			ret.add("links", FoodCategoryChildrenLinks);
			return ret.toString();
		}
		// search superiorCategoryId from the database
		// int superiorCategoryId = foodCategoryDao.getSuperiorCategoryId(id);
		FoodCategory foodCategory = foodCategoryDao
				.searchFoodCategoryDetailById(id);
		int superiorCategoryId = foodCategory.getSuperiorCategoryId();
		if (superiorCategoryId == 0) {
			ret.addProperty("errorCode", ERROR_CODE_HASNOR_TOPCATEGROY);
			ret.add("links", FoodCategoryChildrenLinks);
			return ret.toString();
		}
		FoodCategory foodCategoryDetailInfo = foodCategoryDao
				.searchFoodCategoryDetailById(superiorCategoryId);
		JsonObject jSuperiorFood = transformFoodToJson(foodCategoryDetailInfo);
		ret.add("superiorFood", jSuperiorFood);
		ret.add("links", FoodCategoryChildrenLinks);
		return ret.toString();

	}

	/** get the detail info of foodCategory by id **/
	@GET
	@Path("{id}")
	public String getDetailInfoById(@PathParam("id") int id) {
		JsonObject ret = new JsonObject();
		// define errorCode
		final int ERROR_CODE_BAD_PARAM = -1;
		final int ERROR_CODE_NO_RESULT = -2;
		// check request parameters
		if (id < 0) {
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", FoodCategoryChildrenLinks);
			return ret.toString();
		}
		if (!foodCategoryDao.isCategoryExist(id)) {
			ret.addProperty("errorCode", ERROR_CODE_NO_RESULT);
			ret.add("links", FoodCategoryChildrenLinks);
			return ret.toString();
		}
		// search the database
		FoodCategory foodCategoryDetailInfo = foodCategoryDao
				.searchFoodCategoryDetailById(id);

		JsonObject jSuperiorFood = transformFoodToJson(foodCategoryDetailInfo);
		ret.add("superiorFood", jSuperiorFood);
		ret.add("links", FoodCategoryChildrenLinks);
		return ret.toString();
	}

	/** update the foodCategory **/
	@PUT
	@Path("{id}")
	public String updateFoodCategory(@PathParam("id") int id,
			@FormParam("name") String name,
			@FormParam("topCategory") boolean topCategory,
			@FormParam("superiorCategoryId") int superiorCategoryId) {
		JsonObject ret = new JsonObject();
		// define errorCode
		final int ERROR_CODE_BAD_PARAM = -1;
		final int ERROR_CODE_NORPROVICE_SUPERIORREGIONNOTEXIST = -2;
		final int ERROR_CODE_PROVICE_SUPERIOREXIST = -3;

		final int ERROR_CODE_NO_RESULT = -4;
		// check bad request parameter
		if (name == null || "".equals(name) || superiorCategoryId < 0) {
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", FoodCategoryChildrenLinks);
			return ret.toString();
		}
		// check parameters
		if (!topCategory
				&& foodCategoryDao
						.isSuperiorCategoryIdExist(superiorCategoryId) == false) {
			ret.addProperty("errorCode",
					ERROR_CODE_NORPROVICE_SUPERIORREGIONNOTEXIST);
			ret.add("links", FoodCategoryChildrenLinks);
			return ret.toString();
		}
		// check parameters
		if (topCategory
				&& superiorCategoryId != 0) {
			ret.addProperty("errorCode", ERROR_CODE_PROVICE_SUPERIOREXIST);
			ret.add("links", FoodCategoryChildrenLinks);
			return ret.toString();
		}

		// search the database
		FoodCategory updateFoodCategory = foodCategoryDao.getById(id);
		if (updateFoodCategory == null) {
			ret.addProperty("errorCode", ERROR_CODE_NO_RESULT);
			ret.add("links", FoodCategoryChildrenLinks);
			return ret.toString();
		}

		updateFoodCategory.setName(name);
		updateFoodCategory.setTopCategory(topCategory);
		updateFoodCategory.setSuperiorCategoryId(superiorCategoryId);
		foodCategoryDao.update(updateFoodCategory);
		ret.addProperty("id", id);
		ret.add("links", FoodCategoryChildrenLinks);
		return ret.toString();
	}

	/** delete the foodcategory by id **/
	@DELETE
	@Path("{id}")
	public String deleteFoodCategoryById(@PathParam("id") int id) {
		JsonObject ret = new JsonObject();
		// define errorCode
		final int ERROR_CODE_BAD_PARAM = -1;
		final int ERROR_CODE_RECIPECATEGORY_NOT_EXIST = -2;
		final int ERROR_CODE_NO_RESULT = -3;
		// check request parameters
		if (id < 0) {
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", FoodCategoryChildrenLinks);
			return ret.toString();
		}
		if (foodCategoryDao.isCategoryExist(id) == false) {
			ret.addProperty("errorCode", ERROR_CODE_RECIPECATEGORY_NOT_EXIST);
			ret.add("links", FoodCategoryChildrenLinks);
			return ret.toString();
		}
		FoodCategory deletefoodCategory = foodCategoryDao
				.searchFoodCategoryDetailById(id);
		int superiorCategoryId = deletefoodCategory.getSuperiorCategoryId();
		// this recipeCategoey is topRecipeCategory
		if (superiorCategoryId == 0) {
			List<FoodCategory> deleteFoodCategories = new ArrayList<FoodCategory>();
			List<FoodCategory> foodCategories = foodCategoryDao
					.getAllFoodCategory();
			for (FoodCategory foodCategory : foodCategories) {
				if (foodCategory.getSuperiorCategoryId() == id) {
					deleteFoodCategories.add(foodCategory);
				}
			}
			for (FoodCategory foodCategory : deleteFoodCategories) {
				foodCategoryDao.delete(foodCategory);
				ret.addProperty("id", id);
				ret.add("links", FoodCategoryChildrenLinks);
			}
			foodCategoryDao.delete(deletefoodCategory);
			ret.addProperty("id", id);
			ret.add("links", FoodCategoryChildrenLinks);
			return ret.toString();
		}
		foodCategoryDao.delete(deletefoodCategory);
		ret.addProperty("id", id);
		ret.add("links", FoodCategoryChildrenLinks);
		return ret.toString();

	}

	/**
	 * transform foodCategory from bean to json
	 * 
	 * @param category
	 * @return
	 */
	private JsonObject transformFoodToJson(FoodCategory category) {
		JsonObject jFoodCategory = JsonUtil.beanToJson(category);
		return jFoodCategory;
	}
	
}
