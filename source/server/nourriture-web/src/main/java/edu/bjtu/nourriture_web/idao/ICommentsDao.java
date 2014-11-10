package edu.bjtu.nourriture_web.idao;

import java.util.List;

import edu.bjtu.nourriture_web.bean.Comments;

public interface ICommentsDao {
	/** get the score of food by foodId **/
	List<Comments> getByRefId(int refId);
}
