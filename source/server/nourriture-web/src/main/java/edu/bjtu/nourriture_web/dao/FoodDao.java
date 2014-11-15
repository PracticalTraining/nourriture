package edu.bjtu.nourriture_web.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.bjtu.nourriture_web.bean.Food;
import edu.bjtu.nourriture_web.bean.Customer;
import edu.bjtu.nourriture_web.bean.Food;
import edu.bjtu.nourriture_web.idao.IFoodDao;

public class FoodDao extends HibernateDaoSupport implements IFoodDao {
	public int add(Food food) {
		getHibernateTemplate().save(food);
		return food.getId();
	}
	public Food getById(int id) {
		return getHibernateTemplate().get(Food.class, id);
	}
	/** search the food according to manufacturerId **/
    public List getByManufacturerId(int manufacturerId) {
		List<Food> list = getHibernateTemplate().find("from Food where manufacturerId = ?", manufacturerId);
		return list;
	}
    /**delete food by id**/
	public void deletebyid(int id){
		Food food = new Food();
		food.setId(id);
		getHibernateTemplate().delete(food);
	}
	/**update the food**/
	public void update(Food food) {
		getHibernateTemplate().saveOrUpdate(food);
	}
	/** search **/
	public List<Food> search(double fromPrice, double toPrice,
			String[] categoryIds, String[] flavourIds, String[] produceRegionIds,
			String[] buyRegionIds) {
		List<Food> list;
		StringBuffer sb = new StringBuffer();
		sb.append("from Food where ");
		
		if(fromPrice != -1 && toPrice != -1){
			sb.append("price > ? and price < ? and ");
		} else if(fromPrice != -1) {
			sb.append("price > ? and ");
		} else if(toPrice != -1){
			sb.append("price < ? and "); 
		}
		
		boolean hasIds = false;
		if(categoryIds != null && categoryIds.length != 0 && !categoryIds[0].equals(""))
		{
			sb.append("(");
			hasIds = true;
			for(String categoryId : categoryIds){
				sb.append("categoryId = ");
				sb.append(categoryId);
				sb.append(" or ");
			}
			sb.delete(sb.length() - 3, sb.length());
			sb.append(") and ");
		}
		
		if(flavourIds != null && flavourIds.length != 0 && !flavourIds[0].equals("")){
			sb.append("(");
			hasIds = true;
			for(String flavourId : flavourIds){
				sb.append("flavourId =");
				sb.append(flavourId);
				sb.append(" or ");
			}
			sb.delete(sb.length() - 3, sb.length());
			sb.append(") and ");
		}
		
//		if(produceRegionIds != null && produceRegionIds.length != 0){
//			sb.append("(");
//			hasIds = true;
//			for(String produceRegionId : produceRegionIds){
//				sb.append("produceLocationId =");
//				sb.append(produceRegionId);
//				sb.append(" or ");
//			}
//			sb.delete(sb.length() - 3, sb.length());
//			sb.append(") and ");
//		}
//		
//		if(buyRegionIds != null && buyRegionIds.length != 0){
//			sb.append("(");
//			hasIds = true;
//			for(String buyRegionId : buyRegionIds){
//				sb.append("produceLocationId =");
//				sb.append(buyRegionId);
//				sb.append(" or ");
//			}
//			sb.delete(sb.length() - 3, sb.length());
//			sb.append(") and ");
//		}
		if(hasIds){
			sb.delete(sb.length() - 5, sb.length());
		}
		System.out.println(sb);
		
		if(fromPrice != -1 && toPrice != -1){
			list = getHibernateTemplate().find(sb.toString(),fromPrice,toPrice);
		} else if(fromPrice != -1) {
			list = getHibernateTemplate().find(sb.toString(),fromPrice);
		} else if(toPrice != -1){
			list = getHibernateTemplate().find(sb.toString(),toPrice);
		} else {
			list = new ArrayList<Food>();
		}
		return list;
	}
	
}
