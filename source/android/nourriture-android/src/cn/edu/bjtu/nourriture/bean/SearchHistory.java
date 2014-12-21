package cn.edu.bjtu.nourriture.bean;

import com.lidroid.xutils.db.annotation.Table;

@Table(name = "search_history") 
public class SearchHistory {
	private int id;
	private String keyword;
	private long time;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
}
