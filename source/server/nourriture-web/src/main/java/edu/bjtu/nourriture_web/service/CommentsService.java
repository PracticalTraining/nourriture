package edu.bjtu.nourriture_web.service;

import edu.bjtu.nourriture_web.idao.ICommentsDao;
import edu.bjtu.nourriture_web.iservice.ICommentsService;

public class CommentsService implements ICommentsService {
	private ICommentsDao commentsDao;

	public ICommentsDao getCommentsDao() {
		return commentsDao;
	}

	public void setCommentsDao(ICommentsDao commentsDao) {
		this.commentsDao = commentsDao;
	}
}
