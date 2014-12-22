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

import edu.bjtu.nourriture_web.bean.CookingStep;
import edu.bjtu.nourriture_web.bean.Recipe;
import edu.bjtu.nourriture_web.common.JsonUtil;
import edu.bjtu.nourriture_web.common.RestfulServiceUtil;
import edu.bjtu.nourriture_web.idao.ICookingStepDao;
import edu.bjtu.nourriture_web.idao.IRecipeDao;

@Path("cookingStep")
public class CookingStepRestfulService {
	
	ICookingStepDao				cookingStepDao;
	IRecipeDao					recipeDao;
	private JsonArray 			cookingStepChildrenLinks;	
	
	public ICookingStepDao getCookingStepDao() {
	
		return cookingStepDao;
	
	}
	public void setCookingStepDao(ICookingStepDao cookingStepDao) {
		
		this.cookingStepDao = cookingStepDao;
	
	}
	
	public IRecipeDao getRecipeDao() {
	
		return recipeDao;
	
	}
	
	public void setRecipeDao(IRecipeDao recipeDao) {
	
		this.recipeDao = recipeDao;
	
	}

	
	
	{
		this.cookingStepChildrenLinks = new JsonArray();
		RestfulServiceUtil.addChildrenLinks(cookingStepChildrenLinks, "get cooking step according to id", "/{id}", "GET");
		RestfulServiceUtil.addChildrenLinks(cookingStepChildrenLinks, "update cooking step according to id", "/{id}", "PUT");
		RestfulServiceUtil.addChildrenLinks(cookingStepChildrenLinks, "delete cooking step according to id", "/{id}", "DELETE");
	}
	
	/** update CookingStep information **/
	@PUT
	@Path("{id}")
	public String updateCookingStep(@PathParam("id") int id,
			@FormParam("stepCount") int stepCount,@FormParam("description") String description,
			@FormParam("picture") String picture,@FormParam("recipeId") int recipeId){
		
		final int 	ERROR_CODE_BAD_PARAM					= -1;
		final int 	ERROR_CODE_RECIPE_DOES_NOT_EXISTS 		= -2;
		CookingStep my_cookingStep 							= this.cookingStepDao.getById(id);
		Recipe		my_recipe								= this.recipeDao.getById(my_cookingStep.getRecipeId());
		JsonObject ret 										= new JsonObject();	
		
		if(stepCount <= 0 || description.equals("") || description == null 
		    	|| picture.equals("") || picture == null || recipeId <= 0) //check request parameters
		    {
		           ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
	               ret.add("links", this.cookingStepChildrenLinks);
		           return ret.toString();
		    }
		if(my_recipe == null) { 		//check if Recipe exsits
			ret.addProperty("errorCode", ERROR_CODE_RECIPE_DOES_NOT_EXISTS);
			ret.add("links", this.cookingStepChildrenLinks);
			return ret.toString();
		}
		
		//update the database
		my_cookingStep.setStepCount(stepCount);
		my_cookingStep.setDescription(description);
		my_cookingStep.setPicture(picture);
		my_cookingStep.setRecipeId(recipeId);
		this.cookingStepDao.update(my_cookingStep);
		ret.addProperty("result", 0);
		ret.add("links", this.cookingStepChildrenLinks);
		
		return ret.toString();
	}
	
	/**add a Cooking Step**/
	@POST
	public String addCookingStep(@FormParam("stepCount") int stepCount,@FormParam("description") String description,
			@FormParam("picture") String picture,@FormParam("recipeId") int recipeId){
		
		JsonObject ret 					= new JsonObject();
		CookingStep my_cookingstep 		= new CookingStep();
		final int ERROR_CODE_BAD_PARAM	= -1;
		
	    if(stepCount <= 0 || description.equals("") || description == null 
	    	|| picture.equals("") || picture == null || recipeId <= 0) 
	    {
	           ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
               ret.add("links", this.cookingStepChildrenLinks);
	           return ret.toString();
	    }
		//add a row to database
	    my_cookingstep.setStepCount(stepCount);
	    my_cookingstep.setDescription(description);
	    my_cookingstep.setPicture(picture);
	    my_cookingstep.setRecipeId(recipeId);
		ret.addProperty("id", this.cookingStepDao.add(my_cookingstep));
		ret.add("links", this.cookingStepChildrenLinks);
		
		return ret.toString();
	}
	
	@SuppressWarnings("unused")
	@GET
	@Path("{id}")
	public String searchCookingStepById(@PathParam("id") int id){
		
		final int ERROR_CODE_COOKING_STEP_DOES_NOT_EXIST 	= -1;	//define error code
		final int ERROR_CODE_RECIPE_DOES_NOT_EXISTS 		= -2;
		CookingStep my_cookingStep 							= this.cookingStepDao.getById(id);
		Recipe		my_recipe								= this.recipeDao.getById(my_cookingStep.getRecipeId());
		JsonObject ret 										= new JsonObject();	

		if(my_cookingStep == null) {
			ret.addProperty("errorCode", ERROR_CODE_COOKING_STEP_DOES_NOT_EXIST);
			ret.add("links", this.cookingStepChildrenLinks);
			return ret.toString();
		}
		if(my_recipe == null) {
			ret.addProperty("errorCode", ERROR_CODE_RECIPE_DOES_NOT_EXISTS);
			ret.add("links", this.cookingStepChildrenLinks);
			return ret.toString();
		}
		
		JsonObject jCookingStep 							= JsonUtil.beanToJson(my_cookingStep);
		
		ret.add("Recipe", jCookingStep);
		return ret.toString();
	}
	
	@DELETE
	@Path("{id}")
	public String deleteCookingStep(@PathParam("id") int id){
		
		final int 	ERROR_CODE_RECIPE_DOES_NOT_EXISTS 		= -1;
		CookingStep my_cookingStep 							= this.cookingStepDao.getById(id);
		Recipe		my_recipe								= this.recipeDao.getById(my_cookingStep.getRecipeId());
		JsonObject	ret 									= new JsonObject();	
		
		if(my_recipe == null) {
			ret.addProperty("errorCode", ERROR_CODE_RECIPE_DOES_NOT_EXISTS);
			ret.add("links", this.cookingStepChildrenLinks);
			return ret.toString();
		}
		
		this.cookingStepDao.deletebyid(id);
		ret.addProperty("result", 0);
		ret.add("links", this.cookingStepChildrenLinks);
		
		return ret.toString();
	}
	
	@GET
	@Path("getRecipeSteps")
	public String getRecipeCookingStep(@QueryParam("rId") int rId){
		JsonObject ret = new JsonObject();
		
		final int ERROR_CODE_BAD_PARAM = -1;
		
		if(rId <= 0){
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", new JsonArray());
			return ret.toString();
		}
		
		List<CookingStep> list = cookingStepDao.getByRecipeId(rId);
		JsonArray jCookingSteps = new JsonArray();
		for(CookingStep cookingStep : list){
			JsonObject jCookingStep = JsonUtil.beanToJson(cookingStep);
			jCookingSteps.add(jCookingStep);
		}
		
		ret.add("cookingSteps", jCookingSteps);
		ret.add("links", new JsonArray());
		return ret.toString();
	}
}
