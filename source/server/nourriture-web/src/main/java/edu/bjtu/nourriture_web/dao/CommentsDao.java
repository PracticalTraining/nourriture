package edu.bjtu.nourriture_web.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.bjtu.nourriture_web.bean.Comments;
import edu.bjtu.nourriture_web.bean.Recipe;
import edu.bjtu.nourriture_web.idao.ICommentsDao;

public class CommentsDao extends HibernateDaoSupport implements ICommentsDao {
	/** search the comments according to foodId **/
	
	public List<Comments> getByRefIdFood(int refId) {
		List<Comments> list = getHibernateTemplate().find("from Comments where refId=? AND commentOn=?", refId, 0);
		return list;
	}
	
	public List<Comments> getByRefIdRecipe(int refId) {
		List<Comments> list = getHibernateTemplate().find("from Comments where refId=? AND commentOn=?", refId, 1);
		return list;
	}
	
	public List<Comments> getByRefId(int refId) {
		List<Comments> list = getHibernateTemplate().find("from Comments where refId=?", refId);
		return list;
	}
	
	public int add(Comments comment) {
		 getHibernateTemplate().save(comment);
		
		 return comment.getId();
	 }
	
	 public Comments getById(int id) {
		 
		return getHibernateTemplate().get(Comments.class, id);
	}
	 
	 public void deletebyid(int id) {
			
		Comments my_comment =	new Comments();
		my_comment.setId(id);
		getHibernateTemplate().delete(my_comment);		
	}
	
}
