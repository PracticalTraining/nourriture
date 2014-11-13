package edu.bjtu.nourriture_web.dao;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.opensymphony.xwork2.util.location.LocationAttributes;

import edu.bjtu.nourriture_web.bean.Customer;
import edu.bjtu.nourriture_web.bean.Location;
import edu.bjtu.nourriture_web.idao.ILocationDao;

public class LocationDao extends HibernateDaoSupport implements ILocationDao {
	public int add(Location location) {
		getHibernateTemplate().save(location);
		return location.getId();
	}
	
	public boolean isRegionNotExist(int regionId){
		List<Location> list = getHibernateTemplate().find("from Location where regionId = ?", regionId);
		return !list.isEmpty();
	}
	
	public List<Location> searchById(int regionId) {
		List<Location> list = getHibernateTemplate().find("from Location where regionId = ?", regionId);
		return list;
	}
	public Location getById(int id) {
		return getHibernateTemplate().get(Location.class, id);
	}
	public void update(Location location) {
		getHibernateTemplate().saveOrUpdate(location);
	}
	/**delete location by id**/
	public void deletebyid(int id){
		Location location=new Location();
		location.setId(id);
		getHibernateTemplate().delete(location);
	}
}
