package edu.bjtu.nourriture_web.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;



import edu.bjtu.nourriture_web.bean.Region;
import edu.bjtu.nourriture_web.idao.IRegionDao;

public class RegionDao extends HibernateDaoSupport implements IRegionDao  {
	public boolean isRegionExist(int regionId){
		List<Region> list = getHibernateTemplate().find("from Region where id = ?", regionId);
		return !list.isEmpty();
	}
}
