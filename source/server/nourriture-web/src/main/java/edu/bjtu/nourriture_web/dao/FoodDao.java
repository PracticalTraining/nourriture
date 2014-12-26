package edu.bjtu.nourriture_web.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.bjtu.nourriture_web.bean.Food;
import edu.bjtu.nourriture_web.idao.IFoodDao;

@SuppressWarnings("unchecked")
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
	
	public List<Food> search(String name) {
		List<Food> list = null;
		list = getHibernateTemplate().find("from Food where name like ?","%" + name + "%");
		return list;
	}
	
	public List<Food> getPageFoods(final int categoryId, final int page) {
		return getHibernateTemplate().executeFind(new HibernateCallback<List<Food>>() {

			public List<Food> doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query = session.createQuery("from Food where categoryId = ?"); 
				query.setParameter(0, categoryId);
				query.setFirstResult(page * 10); 
				query.setMaxResults(10); 
				List<Food> list = query.list();
				return list; 
			}
		});
	}
	
	public List<Food> getPageFoods(final int[] categoryIds, final int[] flavourIds, final int page) {
		return getHibernateTemplate().executeFind(new HibernateCallback<List<Food>>() {

			public List<Food> doInHibernate(Session session) throws HibernateException,
					SQLException {
				List<Integer> categoryList = new ArrayList<Integer>();
				if(categoryIds != null)
					for(int categoryId :categoryIds){
						categoryList.add(categoryId);
					}
				List<Integer> flavourList = new ArrayList<Integer>();
				if(flavourIds != null)
					for(int flavourId :flavourIds){
						flavourList.add(flavourId);
					}
				Query query = null;
				if(categoryIds != null && flavourIds != null)
				{
					query = session.createQuery("from Food where categoryId in (:categoryIds) or flavourId in (:flavourIds)");
					query.setParameterList("categoryIds", categoryList);
					query.setParameterList("flavourIds", flavourList);
				}
				else if(categoryIds != null && flavourIds == null)
				{
					query = session.createQuery("from Food where categoryId in (:categoryIds)");
					query.setParameterList("categoryIds", categoryList);
				}
				else if(categoryIds == null && flavourIds != null)
				{
					query = session.createQuery("from Food where flavourId in (:flavourIds)");
					query.setParameterList("flavourIds", flavourList);
				}
				else
				{
					return new ArrayList<Food>();
				}
				query.setFirstResult(page * 10); 
				query.setMaxResults(10); 
				List<Food> list = query.list();
				return list; 
			}
		});
	}
	
	
	
}
