package edu.bjtu.nourriture_web.bean;

public class Flavour {
	/** auto increment field (自增序列) **/
	private int id;
	/** flavour name  (口味名) **/
	private String name;
	/** is the top classification (是否是顶级分类) **/
	private boolean topFlavour;
	/** superior flavour  (上级口味) **/
	private int superiorFlavourId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isTopFlavour() {
		return topFlavour;
	}
	public void setTopFlavour(boolean topFlavour) {
		this.topFlavour = topFlavour;
	}
	public int getSuperiorFlavourId() {
		return superiorFlavourId;
	}
	public void setSuperiorFlavourId(int superiorFlavourId) {
		this.superiorFlavourId = superiorFlavourId;
	}
}
