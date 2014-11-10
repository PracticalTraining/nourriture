package edu.bjtu.nourriture_web.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.bjtu.nourriture_web.bean.Comments;
import edu.bjtu.nourriture_web.idao.ICommentsDao;

public class CommentsDao extends HibernateDaoSupport implements ICommentsDao {
	/** search the comments according to foodId **/
	public List<Comments> getByRefId(int refId) {
		List<Comments> list = getHibernateTemplate().find(
				"from comments where refId=?", refId);
		return list;
	}
}
