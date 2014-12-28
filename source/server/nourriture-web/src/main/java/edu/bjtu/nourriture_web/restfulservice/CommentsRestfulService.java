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

import edu.bjtu.nourriture_web.bean.Comments;
import edu.bjtu.nourriture_web.bean.Customer;
import edu.bjtu.nourriture_web.bean.Food;
import edu.bjtu.nourriture_web.bean.Recipe;
import edu.bjtu.nourriture_web.common.JsonUtil;
import edu.bjtu.nourriture_web.common.RestfulServiceUtil;
import edu.bjtu.nourriture_web.idao.ICommentsDao;
import edu.bjtu.nourriture_web.idao.ICustomerDao;
import edu.bjtu.nourriture_web.idao.IFoodDao;
import edu.bjtu.nourriture_web.idao.IRecipeDao;

@Path("comments")
public class CommentsRestfulService {
	ICommentsDao				commentsDao;
	IRecipeDao					recipeDao;
	IFoodDao					foodDao;
	ICustomerDao				customerDao;
	private JsonArray 			commentsChildrenLinks = new JsonArray();
	
	
	public ICommentsDao getCommentsDao() {
		return commentsDao;
	}
	
	public void setCommentsDao(ICommentsDao commentsDao) {
		this.commentsDao = commentsDao;
	}
	
	public ICustomerDao getCustomerDao() {
		return customerDao;
	}
	
	public void setCustomerDao(ICustomerDao customerDao) {
		this.customerDao = customerDao;
	}
	
	public IRecipeDao getRecipeDao() {
		return recipeDao;
	}

	public void setRecipeDao(IRecipeDao recipeDao) {
		this.recipeDao = recipeDao;
	}

	public IFoodDao getFoodDao() {
		return foodDao;
	}

	public void setFoodDao(IFoodDao foodDao) {
		this.foodDao = foodDao;
	}
	
	{
		RestfulServiceUtil.addChildrenLinks(commentsChildrenLinks, "see food comments by pages", "/getFoodCmt", "GET");
		RestfulServiceUtil.addChildrenLinks(commentsChildrenLinks, "see recipe comments by pages", "/getRecipeCmt", "GET");
		RestfulServiceUtil.addChildrenLinks(commentsChildrenLinks, "delete comments according to id", "/{id}", "DELETE");
	}

	@POST
	public String addComments(@FormParam("score") int score, @FormParam("description") String description,
							@FormParam("customerId") int customerId, @FormParam("commentON") int commentOn, 
							@FormParam("refId") int refId) {
		
		JsonObject		ret 										= new JsonObject();
		final int 		ERROR_CODE_BAD_PARAM						= -1;
		final int		ERROR_CODE_CUSTOMER_DOES_NOT_EXISTS			= -2;
		final int		ERROR_CODE_FOOD_OR_RECIPE_DOES_NOT_EXISTS	= -3;
		Comments		my_comments									= new Comments();
		Recipe			my_recipe									= this.recipeDao.getById(refId);
		Food			my_food										= this.foodDao.getById(refId);
		Customer		my_customer									= this.customerDao.getById(customerId);
		
		if (score > 5 || score < 0  || description.equals("") || description == null 
				|| customerId < 0 || commentOn < 0 || commentOn > 1 || refId < 0)
		{		
			 ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
             ret.add("links", this.commentsChildrenLinks);
	         return ret.toString();		
		}
		
		if (my_customer == null)
		{
			ret.addProperty("errorCode", ERROR_CODE_CUSTOMER_DOES_NOT_EXISTS);
			ret.add("links", this.commentsChildrenLinks);
			ret.toString();
		}
		
		if (my_food == null && my_recipe == null)
		{
			ret.addProperty("errorCode", ERROR_CODE_FOOD_OR_RECIPE_DOES_NOT_EXISTS);
			ret.add("links", this.commentsChildrenLinks);
			ret.toString();
		}
		my_comments.setCommentOn(commentOn);
		my_comments.setCustomerId(customerId);
		my_comments.setDescription(description);
		my_comments.setRefId(refId);
		my_comments.setScore(score);
		
		this.commentsDao.add(my_comments);
		ret.addProperty("result", 0);
		ret.add("links", this.commentsChildrenLinks);
	return ret.toString();
	
	}

	@SuppressWarnings("unused")
	@DELETE
	@Path("{id}")
	public	String	deleteComment(@PathParam("id") int id) {
		
	final int ERROR_CODE_COMMENT_DOES_NOT_EXITS		= -1;
	Comments my_comment								= new Comments();
	JsonObject	ret 								= new JsonObject();	

	if (my_comment == null ) {
		ret.addProperty("errorCode", ERROR_CODE_COMMENT_DOES_NOT_EXITS);
		ret.add("links", this.commentsChildrenLinks);
		return ret.toString();
	}
	this.commentsDao.deletebyid(id);
	ret.addProperty("result", 0);
	ret.add("links", this.commentsChildrenLinks);
	
	return ret.toString();
	}
	
	@GET
	@Path("getFoodCmt")
	public	String getFoodCommentsByPages(@QueryParam("foodId")int foodId, @QueryParam("pageSize")int pageSize, 
			@QueryParam("page")int page) {
		
		JsonObject	ret 									= new JsonObject();
		final int	ERROR_CODE_BAD_PARAM					= -1;
		final int 	ERROR_CODE_FOOD_DOES_NOT_EXIST			= -2;
		List<Comments>	comments							= this.commentsDao.getByRefId(foodId);;
		Food 			my_food								= this.foodDao.getById(foodId);
		JsonArray	commentsArray							= new JsonArray();
		int 		i										= pageSize * page - pageSize;
		int			iterator								= 0;
		
		if (foodId < 0 || pageSize < 0 || page < 0) {
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
            ret.add("links", this.commentsChildrenLinks);
	        return ret.toString();
		}
		
		if (my_food == null) {
			ret.addProperty("errorCode", ERROR_CODE_FOOD_DOES_NOT_EXIST);
			ret.add("links", this.commentsChildrenLinks);
			return ret.toString();		
		}
		for (Comments comment : comments) 
		{
			if (iterator >= i && iterator != (pageSize * page))
			{
				JsonObject jComments			= JsonUtil.beanToJson(comment);
				commentsArray.add(jComments);
			}
			iterator ++;
		}	
		
		ret.add("comments", commentsArray);
		ret.add("links", this.commentsChildrenLinks);
        return ret.toString();

	}
	
	@GET
	@Path("getRecipeCmt")
	public String getRecipeCommentsByPages(@QueryParam("recipeId") int recipeId, @QueryParam("pageSize") int pageSize, 
			@QueryParam("page") int page) {
		
		JsonObject	ret 										= new JsonObject();
		Recipe			my_recipe								= this.recipeDao.getById(recipeId);
		final int	ERROR_CODE_BAD_PARAM						= -1;
		final int 	ERROR_CODE_RECIPE_DOES_NOT_EXIST			= -2;
		List<Comments>	comments		= this.commentsDao.getByRefId(recipeId);	
		JsonArray	commentsArray		= new JsonArray();
		int 		i					= pageSize * page - pageSize;
		int			iterator			= 0;
		
		if (recipeId < 0 || pageSize < 0 || page < 0) {
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
            ret.add("links", this.commentsChildrenLinks);
	        return ret.toString();
		}
		
		if (my_recipe == null) {
			ret.addProperty("errorCode", ERROR_CODE_RECIPE_DOES_NOT_EXIST);
			ret.add("links", this.commentsChildrenLinks);
			return ret.toString();		
		}
		
		for (Comments comment : comments) 
			{
				if (iterator >= i && iterator != (pageSize * page))
				{
					JsonObject jComments			= JsonUtil.beanToJson(comment);
					commentsArray.add(jComments);
				}
				iterator ++;
			}		
		
		ret.add("comments", commentsArray);
		ret.add("links", this.commentsChildrenLinks);
        return ret.toString();

	}
}
