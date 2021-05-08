/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ns.systems.ispbillingsystem.repositories;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ns.systems.ispbillingsystem.helpers.ISPHelper;
import ns.systems.ispbillingsystem.services.CommonService;
import ns.systems.ispbillingsystem.models.Setting;
import ns.systems.ispbillingsystem.utilities.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
/**
 *
 * @author naveed
 */
public class SettingRepository implements CommonService<Setting>{

    private static SessionFactory sessionFactory;
    private Transaction transaction;
    private Session session;
    
     public SettingRepository() {
        this.sessionFactory =  HibernateUtil.getSessionFactory();
    }

    @Override
    public List<Setting> list() {
         try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Setting> query = builder.createQuery(Setting.class);
        Root<Setting> root = query.from(Setting.class);
        query.select(root).orderBy(builder.desc(root.get("id")));
        return session.createQuery(query).list();
        }
        finally{
            session.close();
        }
    }

    @Override
    public void save(Setting object) {
      session = sessionFactory.openSession();
     try{
         transaction = session.beginTransaction();
         session.save(object);
         transaction.commit();
     }catch(Exception ex){
         ex.printStackTrace();
     if(transaction != null){
               transaction.rollback();
           }
     }finally{
            session.close();
        }
    }

    @Override
    public void update(Setting object) {
         session = sessionFactory.openSession();
     try{
         transaction = session.beginTransaction();
         session.update(object);
         transaction.commit();
     }catch(Exception ex){
         ex.printStackTrace();
     if(transaction != null){
               transaction.rollback();
           }
     }finally{
            session.close();
        }
    }

    @Override
    public Setting find(Long Id) {
         try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Setting> query = builder.createQuery(Setting.class);
        Root<Setting> root = query.from(Setting.class);
        query.select(root).where(builder.equal(root.get("id"), Id));
        return session.createQuery(query).uniqueResult();
        }
        finally{
            session.close();
        }
    }
    
    @Override
    public Setting findByKey(String ColumnName, String ColumnValue) {
        try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Setting> query = builder.createQuery(Setting.class);
        Root<Setting> root = query.from(Setting.class);
        query.select(root).where(builder.equal(root.get(ColumnName), ColumnValue));
        return session.createQuery(query).uniqueResult();
        }
        finally{
            session.close();
        }
    }
    
    @Override
    public List<Setting> getWhere(String ColumnName, String ColumnValue) {
         try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Setting> query = builder.createQuery(Setting.class);
        Root<Setting> root = query.from(Setting.class);
        query.select(root).where(builder.equal(root.get(ColumnName), ColumnValue)).orderBy(builder.desc(root.get("id")));
        return session.createQuery(query).list();
        }
        finally{
            session.close();
        }
    }

    @Override
    public List<Setting> whereLike(String ColumnName, String ColumnValue, String JoinType) {
         try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Setting> query = builder.createQuery(Setting.class);
        Root<Setting> root = query.from(Setting.class);
        query.select(root).where(builder.like(root.get(ColumnName), ISPHelper.setLikeType(ColumnValue, JoinType))).orderBy(builder.desc(root.get("id")));
        return session.createQuery(query).list();
        }
        finally{
            session.close();
        }
    }

    @Override
    public void delete(Setting object) {
        session = sessionFactory.openSession();
     try{
         transaction = session.beginTransaction();
         session.delete(object);
         transaction.commit();
     }catch(Exception ex){
           ex.printStackTrace();
     if(transaction != null){
               transaction.rollback();
           }
     }finally{
            session.close();
        }
    }
     
    
}
