package edu.bjtu.nourriture_web.restfulservice;

import java.util.List;

import javax.ws.rs.DELETE;
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
import edu.bjtu.nourriture_web.bean.Recipe;
import edu.bjtu.nourriture_web.bean.RecipeCategory;
import edu.bjtu.nourriture_web.common.JsonUtil;
import edu.bjtu.nourriture_web.common.RestfulServiceUtil;
import edu.bjtu.nourriture_web.idao.ICustomerDao;
import edu.bjtu.nourriture_web.idao.IRecipeCategoryDao;
import edu.bjtu.nourriture_web.idao.IRecipeDao;

@Path("recipe")
public class RecipeRestfulService {
	IRecipeDao					recipeDao;
	ICustomerDao				customerDao;
	IRecipeCategoryDao			recipeCategoryDao;
	private JsonArray 			recipeChildrenLinks = new JsonArray();
	private JsonArray           searchByNameChildrenLinks = new JsonArray();
	
	public IRecipeDao getRecipeDao() {
		return recipeDao;
	}

	public void setRecipeDao(IRecipeDao recipeDao) {
		this.recipeDao = recipeDao;
	}
	
	public ICustomerDao getCustomerDao() {
		return customerDao;
	}

	public void setCustomerDao(ICustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	public IRecipeCategoryDao getRecipeCategoryDao() {
		return recipeCategoryDao;
	}

	public void setRecipeCategoryDao(IRecipeCategoryDao recipeCategoryDao) {
		this.recipeCategoryDao = recipeCategoryDao;
	}


	
	{
		RestfulServiceUtil.addChildrenLinks(recipeChildrenLinks, "get recipe according to id", "/{id}", "GET");
		RestfulServiceUtil.addChildrenLinks(recipeChildrenLinks, "update recipe according to id", "/{id}", "PUT");
		RestfulServiceUtil.addChildrenLinks(recipeChildrenLinks, "delete recipe according to id", "/{id}", "DELETE");
	}
	
	/** update Recipe information **/
	@SuppressWarnings("unused")
	@PUT
	@Path("{id}")
	public String updateRecipe(@PathParam("id") int id,
			@FormParam("name") String name,@FormParam("description") String description,
			@FormParam("ingredient") String ingredient,@FormParam("picture") String picture,
			@FormParam("customerId") int customerId, @FormParam("catogeryId") int catogeryId){
		
		final int 	ERROR_CODE_BAD_PARAM					= -1;
		final int 	ERROR_CODE_RECIPE_DOES_NOT_EXISTS 		= -2;
		final int 	ERROR_CODE_CUSTOMER_DOES_NOT_EXISTS 	= -3;

		Recipe 		my_recipe 								= this.recipeDao.getById(id);
		Customer	my_customer								= this.customerDao.getById(my_recipe.getCustomerId());
		JsonObject	ret 										= new JsonObject();	
		
		if(id <= 0 || name.equals("") || name == null || description.equals("") || description == null
			|| ingredient.equals("") || ingredient == null || picture.equals("") || picture == null ||
			customerId <= 0 || catogeryId <= 0) //check request parameters
		    {
		           ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
	               ret.add("links", this.recipeChildrenLinks);
		           return ret.toString();
		    }
		if(my_recipe == null) { 			//check if Recipe exsits
			ret.addProperty("errorCode", ERROR_CODE_RECIPE_DOES_NOT_EXISTS);
			ret.add("links", this.recipeChildrenLinks);
			return ret.toString();
		}
		if(my_customer == null) { 		//check if Customer exsits
			ret.addProperty("errorCode", ERROR_CODE_CUSTOMER_DOES_NOT_EXISTS);
			ret.add("links", this.recipeChildrenLinks);
			return ret.toString();
		}
		
		//update the database
		my_recipe.setName(name);
		my_recipe.setDescription(description);
		my_recipe.setPicture(picture);
		my_recipe.setIngredient(ingredient);
		my_recipe.setCustomerId(customerId);
		my_recipe.setCatogeryId(catogeryId);

		this.recipeDao.update(my_recipe);
		ret.addProperty("result", 0);
		ret.add("links", this.recipeChildrenLinks);
		
		return ret.toString();
	}
	
	@POST
	public String addRecipe(@FormParam("name") String name,@FormParam("description") String description,
			@FormParam("ingredient") String ingredient,@FormParam("picture") String picture,
			@FormParam("customerId") int customerId, @FormParam("catogeryId") int catogeryId){
		
		final int 		ERROR_CODE_BAD_PARAM							= -1;
		final int 		ERROR_CODE_RECIPE_CATEGORY_DOES_NOT_EXISTS 		= -2;
		final int		ERROR_CODE_CUSTOMER_DOES_NOT_EXISTS				= -3;
		JsonObject		ret 											= new JsonObject();
		Recipe 			my_recipe 										= new Recipe();
		Customer		my_customer										= this.customerDao.getById(customerId);
		RecipeCategory	my_recipeCategory								= this.recipeCategoryDao.getById(catogeryId);
		
		if(name.equals("") || name == null || description.equals("") || description == null
				|| ingredient.equals("") || ingredient == null || picture.equals("") || picture == null ||
				customerId <= 0 || catogeryId <= 0) //check request parameters
			    {
			           ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
		               ret.add("links", this.recipeChildrenLinks);
			           return ret.toString();
			    }
		if(my_recipeCategory == null) { 			//check if Recipe exsits

			ret.addProperty("errorCode", ERROR_CODE_RECIPE_CATEGORY_DOES_NOT_EXISTS);
			ret.add("links", this.recipeChildrenLinks);
			return ret.toString();
		}
		
		if(my_customer == null) { 		//check if Customer exsits
				ret.addProperty("errorCode", ERROR_CODE_CUSTOMER_DOES_NOT_EXISTS);
				ret.add("links", this.recipeChildrenLinks);
				return ret.toString();
			}
		my_recipe.setName(name);
		my_recipe.setDescription(description);
		my_recipe.setPicture(picture);
		my_recipe.setIngredient(ingredient);
		my_recipe.setCustomerId(customerId);
		my_recipe.setCatogeryId(catogeryId);
		
		ret.addProperty("id", this.recipeDao.add(my_recipe));
		ret.add("links", this.recipeChildrenLinks);

		return ret.toString();

	}
	
	
	@GET
	@Path("{id}")
	public String searchRecipe(@PathParam("id") int id){
		
		final int 	ERROR_CODE_RECIPE_DOES_NOT_EXISTS 		= -1;
		JsonObject	ret 									= new JsonObject();
		Recipe 		my_recipe 								= this.recipeDao.getById(id);
		
		if(my_recipe == null) { 			//check if Recipe exsits
			ret.addProperty("errorCode", ERROR_CODE_RECIPE_DOES_NOT_EXISTS);
			ret.add("links", this.recipeChildrenLinks);
			return ret.toString();
		}
		
		JsonObject 	jRecipe 								= JsonUtil.beanToJson(my_recipe);

		ret.add("Recipe", jRecipe);
		ret.add("links", this.recipeChildrenLinks);
		
		return ret.toString();

	}
	
	@DELETE
	@Path("{id}")
	public String deleteCookingStep(@PathParam("id") int id){
		
		final int 	ERROR_CODE_RECIPE_DOES_NOT_EXISTS 		= -1;
		Recipe		my_recipe								= this.recipeDao.getById(id);
		JsonObject	ret 									= new JsonObject();	
		
		if(my_recipe == null) {
			ret.addProperty("errorCode", ERROR_CODE_RECIPE_DOES_NOT_EXISTS);
			ret.add("links", this.recipeChildrenLinks);
			return ret.toString();
		}
		
		this.recipeDao.deletebyid(id);
		ret.addProperty("result", 0);
		ret.add("links", this.recipeChildrenLinks);
		
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
		List<Recipe> listResult = recipeDao.search(name);
		JsonArray recipes = new JsonArray();
		for(Recipe recipe:listResult){
			JsonObject jRecipe = new JsonObject();
			jRecipe.addProperty("id",recipe.getId());
			jRecipe.addProperty("name", recipe.getName());
			jRecipe.addProperty("description", recipe.getDescription());
			jRecipe.addProperty("picture", recipe.getPicture());
			jRecipe.addProperty("ingredient", recipe.getIngredient());
			jRecipe.addProperty("catogeryId", recipe.getCatogeryId());
			jRecipe.addProperty("customerId", recipe.getCustomerId());
			
			recipes.add(jRecipe);
		}
		ret.add("recipes", recipes);
		ret.add("links", searchByNameChildrenLinks);
		return ret.toString();
	}
	
	@GET
	@Path("getPage")
	public String getPage(@QueryParam("categoryId") int categoryId,@QueryParam("page") int page){
		JsonObject ret = new JsonObject();
		List<Recipe> list = recipeDao.getPageRecipes(categoryId, page);
		JsonArray jRecipes = new JsonArray();
		for(Recipe recipe : list){
			JsonObject jRecipe = JsonUtil.beanToJson(recipe);
			jRecipes.add(jRecipe);
		}
		ret.add("recipes", jRecipes);
		ret.add("links", new JsonArray());
		return ret.toString();
	}
	
	/** recommend the recipe **/
	@GET
	@Path("recommend")
	public String recommendByInterest(@QueryParam("customerId") int customerId,@QueryParam("page") int page){
		JsonObject ret = new JsonObject();
		//define error code
		final int ERROR_CODE_CUSTOMER_NOT_EXIST=-1;
		//select from database
		Customer customer = customerDao.getById(customerId);
		if(customer == null){
			ret.addProperty("errorCode", ERROR_CODE_CUSTOMER_NOT_EXIST);
			ret.add("links", new JsonArray());
			return ret.toString();
		}
		String interestRecipeCategoryIds = customer.getInterestRecipeCategoryIds();
		int[] categoryIds = null;
		if(interestRecipeCategoryIds != null && !interestRecipeCategoryIds.equals("")){
			String[] categoryStrIds = interestRecipeCategoryIds.split(",");
			categoryIds = new int[categoryStrIds.length];
			for(int i = 0;i < categoryIds.length;i++){
				categoryIds[i] = Integer.parseInt(categoryStrIds[i]);
			}
		}
		List<Recipe> listResult = recipeDao.getPageRecipes(categoryIds, page);
		JsonArray jRecipes = new JsonArray();
		for(Recipe recipe : listResult){
			JsonObject jRecipe = JsonUtil.beanToJson(recipe);
			jRecipes.add(jRecipe);
		}
		ret.add("recipes", jRecipes);
		ret.add("links", new JsonArray());
		return ret.toString();
	}
}
