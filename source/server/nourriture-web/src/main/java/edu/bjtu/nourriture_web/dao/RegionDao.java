package edu.bjtu.nourriture_web.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.bjtu.nourriture_web.bean.Region;
import edu.bjtu.nourriture_web.idao.IRegionDao;

public class RegionDao extends HibernateDaoSupport implements IRegionDao {

	public boolean isSuperiorRegionIdExist(int superiorRegionId) {
		// TODO Auto-generated method stub
		List<Region> list = getHibernateTemplate().find(
				"from Region where superiorRegionId=?", superiorRegionId);
		if (list == null) {
			return false;
		} else {
			return true;
		}
	}

	public int add(Region region) {
		// TODO Auto-generated method stub
		return (Integer) getHibernateTemplate().save(region);

	}

	public List<Region> searchRegionByName(String name) {
		// TODO Auto-generated method stub
		List<Region> regions = new ArrayList<Region>();
		List<Integer> superiorRegionIds = getHibernateTemplate().find(
				" superiorRegionId from Region where name=?", name);
		for (Integer integer : superiorRegionIds) {
			Region region = (Region) getHibernateTemplate().find(
					"from Region where id=? ", integer);
			regions.add(region);
		}
		return regions;
	}

	public List<Region> searchRegionDetailByName(String name) {
		// TODO Auto-generated method stub
		return getHibernateTemplate().find("from Region where name=?", name);
	}

	public Region getById(int id) {
		// TODO Auto-generated method stub
		return getHibernateTemplate().get(Region.class, id);
	}

	public void delete(Region deleteRegion) {
		// TODO Auto-generated method stub
		getHibernateTemplate().delete(deleteRegion);
	}

}
