package edu.bjtu.nourriture_web.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.bjtu.nourriture_web.bean.Region;
import edu.bjtu.nourriture_web.idao.IRegionDao;

public class RegionDao extends HibernateDaoSupport implements IRegionDao {

	public boolean isSuperiorRegionIdExist(int superiorRegionId) {
		// TODO Auto-generated method stub
		List<Region> regions = getHibernateTemplate().find(
				"from Region where id=?", superiorRegionId);
		if (regions == null) {
			return false;
		} else {
			return true;
		}
	}

	public int add(Region region) {
		// TODO Auto-generated method stub
		getHibernateTemplate().save(region);
		return region.getId();

	}

	public List<Region> searchRegionById(int id) {
		// TODO Auto-generated method stub
		List<Region> regions = new ArrayList<Region>();
		List<Integer> superiorRegionIds = getHibernateTemplate().find(
				" superiorRegionId from Region where id=?", id);
		for (Integer superiorRegionId : superiorRegionIds) {
			Region region = (Region) getHibernateTemplate().find(
					"from Region where id=? ", superiorRegionId);
			regions.add(region);
		}
		return regions;
	}

	public List<Region> searchRegionDetailById(int id) {
		// TODO Auto-generated method stub
		return getHibernateTemplate().find("from Region where id=?", id);
	}

	public Region getById(int id) {
		// TODO Auto-generated method stub
		return getHibernateTemplate().get(Region.class, id);
	}

	public boolean isRegionExist(int regionId) {
		List<Region> list = getHibernateTemplate().find(
				"from Region where id = ?", regionId);
		return !list.isEmpty();
	}

	public void deleteByName(Region deleteRegion) {
		// TODO Auto-generated method stub
		getHibernateTemplate().delete(deleteRegion);
	}

	public void update(Region updateRegion) {
		// TODO Auto-generated method stub
		getHibernateTemplate().update(updateRegion);
	}
}
