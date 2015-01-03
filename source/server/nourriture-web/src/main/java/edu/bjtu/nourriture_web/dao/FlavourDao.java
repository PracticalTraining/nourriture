package edu.bjtu.nourriture_web.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.bjtu.nourriture_web.bean.Flavour;
import edu.bjtu.nourriture_web.idao.IFlavourDao;

public class FlavourDao extends HibernateDaoSupport implements IFlavourDao {

	public Flavour getById(int id) {
		Flavour flavour = getHibernateTemplate().get(Flavour.class, id);
		return flavour;
	}

	public int add(Flavour flavour) {
		// TODO Auto-generated method stub
		getHibernateTemplate().save(flavour);
		return (flavour.getId());
	}

	public boolean isFlavourExist(int id) {
		// TODO Auto-generated method stub
		List<Flavour> list = getHibernateTemplate().find(
				"from Flavour where id= ?", id);
		if (list.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/** delete the Flavour **/
	public void delete(Flavour deleteFlavour) {
		// TODO Auto-generated method stub
		getHibernateTemplate().delete(deleteFlavour);
	}

	public boolean isSuperiorCategoryIdExist(int FlavourId) {
		// TODO Auto-generated method stub
		List<Flavour> list = getHibernateTemplate().find(
				"from Flavour  where id=?", FlavourId);
		if (list.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public void update(Flavour updateFlavour) {
		// TODO Auto-generated method stub
		getHibernateTemplate().update(updateFlavour);
	}

	public Flavour searchFlavourDetailById(int id) {
		// TODO Auto-generated method stub
		List<Flavour> flavours = getHibernateTemplate().find(
				"from Flavour where id=?", id);

		return flavours.get(0);
	}

	// get FoodCategory's superiorCategoryId
	public int getSuperiorCategoryId(int id) {
		// TODO Auto-generated method stub
		List<Integer> FlavourId = getHibernateTemplate().find(
				"FlavourId from Flavour where id=?", id);
		return FlavourId.get(0);
	}

	public List<Flavour> getChildrenFlavour(int id) {
		// TODO Auto-generated method stub
		return getHibernateTemplate()
				.find("from Flavour where FlavourId=?", id);

	}

	public List<Flavour> getAllFlavour() {
		// TODO Auto-generated method stub
		return getHibernateTemplate().find("from Flavour");
	}

	public List<Flavour> getChildren(int pId) {
		return getHibernateTemplate().find("from Flavour where superiorFlavourId = ?",pId);
	}

}
