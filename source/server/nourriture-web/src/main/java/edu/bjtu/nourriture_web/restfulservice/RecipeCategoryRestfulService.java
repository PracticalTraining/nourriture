package edu.bjtu.nourriture_web.restfulservice;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.bjtu.nourriture_web.bean.RecipeCategory;
import edu.bjtu.nourriture_web.common.JsonUtil;
import edu.bjtu.nourriture_web.common.RestfulServiceUtil;
import edu.bjtu.nourriture_web.dao.RecipeCategoryDao;

@Path("recipeCategory")
@Produces("application/json;charset=UTF-8")
public class RecipeCategoryRestfulService {
	// dao
	RecipeCategoryDao recipeCategoryDao;
	// direct children links
	private JsonArray recipecategoryChildrenLinks;

	// get set method for spring IOC

	public RecipeCategoryDao getRecipeCategoryDao() {
		return recipeCategoryDao;
	}

	public void setRecipeCategoryDao(RecipeCategoryDao recipeCategoryDao) {
		this.recipeCategoryDao = recipeCategoryDao;
	}

	{
		// initialize direct children links
		recipecategoryChildrenLinks = new JsonArray();
		// RestfulServiceUtil.addChildrenLinks(recipecategoryChildrenLinks,
		// "get uperior recipe", "/superior", "GET");
		RestfulServiceUtil.addChildrenLinks(recipecategoryChildrenLinks,
				"get recipe's detail information", "/{id}", "GET");
		RestfulServiceUtil.addChildrenLinks(recipecategoryChildrenLinks,
				"update classification of recipe", "/{id}", "PUT");
		RestfulServiceUtil.addChildrenLinks(recipecategoryChildrenLinks,
				"delete classification of recipe", "/{id}", "DELETE");
	}

	/** add recipeCategory **/
	@POST
	public String addRecipeCategory(@FormParam("name") String name,
			@FormParam("topCategory") boolean topCategory,
			@FormParam("superiorCategoryId") int superiorCategoryId) {
		JsonObject ret = new JsonObject();
		// define errorCode
		final int ERROR_CODE_BAD_PARAM = -1;
		final int ERROR_CODE_NOTTOPCATEGORY_SUPERIORCATEGORY_NOTEXIST = -2;
		final int ERROR_CODE_TOPCATEGORY__SUPERIORCATEGORYEXIST = -3;

		// check bad request parameter
		if (name == null || "".equals(name) || superiorCategoryId < 0) {
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", recipecategoryChildrenLinks);
			return ret.toString();
		}
		// check parameters
		if (!topCategory
				&& !recipeCategoryDao
						.isSuperiorCategoryIdExist(superiorCategoryId)) {
			ret.addProperty("errorCode",
					ERROR_CODE_NOTTOPCATEGORY_SUPERIORCATEGORY_NOTEXIST);
			ret.add("links", recipecategoryChildrenLinks);
			return ret.toString();
		}
		// check parameters
		if (topCategory
				&& recipeCategoryDao
						.isSuperiorCategoryIdExist(superiorCategoryId)) {
			ret.addProperty("errorCode",
					ERROR_CODE_TOPCATEGORY__SUPERIORCATEGORYEXIST);
			ret.add("links", recipecategoryChildrenLinks);
			return ret.toString();
		}

		// add one row to database
		RecipeCategory recipeCategory = new RecipeCategory();
		recipeCategory.setName(name);
		recipeCategory.setTopCategory(topCategory);
		recipeCategory.setSuperiorCategoryId(superiorCategoryId);
		ret.addProperty("id", recipeCategoryDao.add(recipeCategory));
		ret.add("links", recipecategoryChildrenLinks);
		return ret.toString();
	}

	// /** get the superior classfication **/
	// @GET
	// @Path("superior")
	// public String getSuperior(@QueryParam("name") String name) {
	// JsonObject ret = new JsonObject();
	// // define errorCode
	// final int ERROR_CODE_NO_RESULT = -1;
	// final int ERROR_CODE_BAD_PARAM = -2;
	// // check request parameters
	// if (name == null || "".equals(name)) {
	// ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
	// ret.add("links", recipecategoryChildrenLinks);
	// return ret.toString();
	// }
	// // search the database
	// List<RecipeCategory> superiorRecipe = recipeCategoryDao
	// .searchRecipeByName(name);
	// if (superiorRecipe == null) {
	// ret.addProperty("errorCode", ERROR_CODE_NO_RESULT);
	// ret.add("links", recipecategoryChildrenLinks);
	// return ret.toString();
	// }
	//
	// JsonObject jSuperiorRecipe = transformRecipeToJson(superiorRecipe);
	// ret.add("superiorRecipe", jSuperiorRecipe);
	// ret.add("links", recipecategoryChildrenLinks);
	// return ret.toString();
	//
	// }

	/** get the detail info of recipeCategory by id **/
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
			ret.add("links", recipecategoryChildrenLinks);
			return ret.toString();
		}
		// search the database
		List<RecipeCategory> recipeCategoryDetailInfo = recipeCategoryDao
				.searchRecipeCategoryDetailById(id);
		if (recipeCategoryDetailInfo.isEmpty()) {
			ret.addProperty("errorCode", ERROR_CODE_NO_RESULT);
			ret.add("links", recipecategoryChildrenLinks);
			return ret.toString();
		}

		JsonObject jSuperiorRecipe = transformRecipeToJson(recipeCategoryDetailInfo);
		ret.add("superiorRecipe", jSuperiorRecipe);
		ret.add("links", recipecategoryChildrenLinks);
		return ret.toString();
	}

	/** update the recipeCategory **/
	@PUT
	@Path("{id}")
	public String updateRecipeCategory(@PathParam("id") int id,
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
			ret.add("links", recipecategoryChildrenLinks);
			return ret.toString();
		}
		// check parameters
		if (!topCategory
				&& recipeCategoryDao
						.isSuperiorCategoryIdExist(superiorCategoryId) == false) {
			ret.addProperty("errorCode",
					ERROR_CODE_NORPROVICE_SUPERIORREGIONNOTEXIST);
			ret.add("links", recipecategoryChildrenLinks);
			return ret.toString();
		}
		// check parameters
		if (topCategory
				&& recipeCategoryDao
						.isSuperiorCategoryIdExist(superiorCategoryId) == true) {
			ret.addProperty("errorCode", ERROR_CODE_PROVICE_SUPERIOREXIST);
			ret.add("links", recipecategoryChildrenLinks);
			return ret.toString();
		}

		// search the database
		RecipeCategory updateRecipeCategory = recipeCategoryDao.getById(id);
		if (updateRecipeCategory == null) {
			ret.addProperty("errorCode", ERROR_CODE_NO_RESULT);
			ret.add("links", recipecategoryChildrenLinks);
			return ret.toString();
		}
		updateRecipeCategory.setName(name);
		updateRecipeCategory.setTopCategory(topCategory);
		updateRecipeCategory.setSuperiorCategoryId(superiorCategoryId);
		recipeCategoryDao.update(updateRecipeCategory);
		ret.addProperty("id", id);
		ret.add("links", recipecategoryChildrenLinks);
		return ret.toString();
	}

	/** delete the recipecategory by id **/
	@DELETE
	@Path("{id}")
	public String deleteRecipeCategoryById(@PathParam("id") int id) {
		JsonObject ret = new JsonObject();
		// define errorCode
		final int ERROR_CODE_BAD_PARAM = -1;
		final int ERROR_CODE_NO_RESULT = -2;
		// check request parameters
		if (id < 0) {
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", recipecategoryChildrenLinks);
			return ret.toString();
		}
		// search the database
		RecipeCategory deleteRecipeCategory = recipeCategoryDao.getById(id);
		if (deleteRecipeCategory == null) {
			ret.addProperty("errorCode", ERROR_CODE_NO_RESULT);
			ret.add("links", recipecategoryChildrenLinks);
			return ret.toString();
		}
		recipeCategoryDao.delete(deleteRecipeCategory);
		ret.addProperty("id", id);
		ret.add("links", recipecategoryChildrenLinks);
		return ret.toString();

	}

	/**
	 * transform recipeCategory from bean to json
	 * 
	 * @param category
	 * @return
	 */
	private JsonObject transformRecipeToJson(List<RecipeCategory> category) {
		JsonObject jRecipeCategory = JsonUtil.beanToJson(category);
		return jRecipeCategory;
	}
}
