package edu.bjtu.nourriture_web.restfulservice;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.bjtu.nourriture_web.bean.Comments;
import edu.bjtu.nourriture_web.common.RestfulServiceUtil;
import edu.bjtu.nourriture_web.dao.CommentsDao;
import edu.bjtu.nourriture_web.idao.ICommentsDao;

@Path("comments")
@Produces("application/json;charset=UTF-8")
public class CommentsRestfulService {
	// dao
	private CommentsDao commentsDao;
	// direct children links
	private JsonArray commentsChildrenLinks;

	// get set method for spring IOC
	public ICommentsDao getCommentsDao() {
		return commentsDao;
	}

	public void setCommentsDao(CommentsDao commentsDao) {
		this.commentsDao = commentsDao;
	}

	{
		// initialize direct children links
		commentsChildrenLinks = new JsonArray();
		RestfulServiceUtil.addChildrenLinks(commentsChildrenLinks,
				"add a comments ", "/{id}", "POST");
		commentsChildrenLinks = new JsonArray();
	}

	/** add a comment **/
	@POST
	public String addComments(@FormParam("score") int score,
			@FormParam("description") String description,
			@FormParam("customerId") int customerId,
			@FormParam("commentOn") int commentOn, @FormParam("refId") int refId) {
		JsonObject ret = new JsonObject();
		// define error code
		final int ERROR_CODE_BAD_PARAM = -1;
		// check request parameters
		if (score == 0 || description == null || description.equals("")
				|| customerId == 0 || commentOn == 0 || refId == 0) {
			ret.addProperty("errorCode", ERROR_CODE_BAD_PARAM);
			ret.add("links", commentsChildrenLinks);
			return ret.toString();
		}
		// add one row to database
		Comments comments = new Comments();
		comments.setScore(score);
		comments.setDescription(description);
		comments.setCommentOn(commentOn);
		comments.setCustomerId(customerId);
		comments.setRefId(refId);
		ret.addProperty("id", commentsDao.add(comments));
		ret.add("links", commentsChildrenLinks);
		return ret.toString();
	}

	// @GET
	// public List<Comments> getAllComments() {
	// JsonObject ret = new JsonObject();
	// List<Comments> list = commentsDao.getAllComments();
	// JsonArray jRecipes = new JsonArray();
	// for (Comments comments : list) {
	// JsonObject jComments = JsonUtil.beanToJson(comments);
	// jRecipes.add(jComments);
	// }
	// ret.add("recipes", jRecipes);
	// ret.add("links", new JsonArray());
	// return;
	// }

}
