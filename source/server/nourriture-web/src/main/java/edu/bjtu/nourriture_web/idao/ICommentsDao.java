package edu.bjtu.nourriture_web.idao;

import java.util.List;

import edu.bjtu.nourriture_web.bean.Comments;

public interface ICommentsDao {
	/** get the score of food by foodId **/
	
	List<Comments> getByRefIdFood(int refId);
	
	List<Comments> getByRefIdRecipe(int refId);

	List<Comments> getByRefId(int refId);
	
	public int add(Comments comment);
	
	public Comments getById(int id);
	
	void deletebyid(int id);
		
}