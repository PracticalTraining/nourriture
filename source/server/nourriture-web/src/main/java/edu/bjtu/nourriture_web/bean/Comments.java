package edu.bjtu.nourriture_web.bean;

public class Comments {
	/** 自增序列 **/
	private int id;
	/** 评分 取值范围：1-5星 **/
	private int score;
	/** 评论 **/
	private String description;
	/** 发表人 **/
	private int customerId;
	/** 0:评论食物 1:评论菜谱 **/
	private int commentOn;
	/** 外键 **/
	private int refId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public int getCommentOn() {
		return commentOn;
	}
	public void setCommentOn(int commentOn) {
		this.commentOn = commentOn;
	}
	public int getRefId() {
		return refId;
	}
	public void setRefId(int refId) {
		this.refId = refId;
	}
}
