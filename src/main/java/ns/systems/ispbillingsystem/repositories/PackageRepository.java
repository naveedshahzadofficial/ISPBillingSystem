/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ns.systems.ispbillingsystem.repositories;

import java.util.List;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import ns.systems.ispbillingsystem.helpers.ISPHelper;
import ns.systems.ispbillingsystem.services.CommonService;
import ns.systems.ispbillingsystem.models.Packagee;
import ns.systems.ispbillingsystem.models.Company;
import ns.systems.ispbillingsystem.utilities.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
/**
 *
 * @author naveed
 */
public class PackageRepository implements CommonService<Packagee>{

    private static SessionFactory sessionFactory;
    private Transaction transaction;
    private Session session;
    
    private static final Logger logger = Logger.getLogger(PackageRepository.class.getName());

    
     public PackageRepository() {
        this.sessionFactory =  HibernateUtil.getSessionFactory();
    }
     
    @Override
    public List<Packagee> list() {
          try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Packagee> query = builder.createQuery(Packagee.class);
        Root<Packagee> root = query.from(Packagee.class);
        query.select(root).orderBy(builder.desc(root.get("id")));
        return session.createQuery(query).list();
        }
        finally{
            session.close();
        }
    }
    
    public List<Packagee> multiList() {
          try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Packagee> query = builder.createQuery(Packagee.class);
        Root<Packagee> root = query.from(Packagee.class);
        Join<Packagee, Company> company = root.join("company");
       
        query.multiselect(root,company).orderBy(builder.desc(root.get("id")));
       
        return session.createQuery(query).getResultList();
        }
        finally{
            session.close();
        }
    }

    @Override
    public void save(Packagee object) {
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
    public void update(Packagee object) {
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
    public Packagee find(Long Id) {
         try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Packagee> query = builder.createQuery(Packagee.class);
        Root<Packagee> root = query.from(Packagee.class);
        query.select(root).where(builder.equal(root.get("id"), Id));
        return session.createQuery(query).uniqueResult();
        }
        finally{
            session.close();
        }
    }

    @Override
    public Packagee findByKey(String ColumnName, String ColumnValue) {
         try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Packagee> query = builder.createQuery(Packagee.class);
        Root<Packagee> root = query.from(Packagee.class);
        query.select(root).where(builder.equal(root.get(ColumnName), ColumnValue));
        return session.createQuery(query).uniqueResult();
        }
        finally{
            session.close();
        }
    }

    @Override
    public List<Packagee> getWhere(String ColumnName, String ColumnValue) {
        try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Packagee> query = builder.createQuery(Packagee.class);
        Root<Packagee> root = query.from(Packagee.class);
        query.select(root).where(builder.equal(root.get(ColumnName), ColumnValue)).orderBy(builder.desc(root.get("id")));
        return session.createQuery(query).list();
        }
        finally{
            session.close();
        }
    }
    
    @Override
    public List<Packagee> whereLike(String ColumnName, String ColumnValue, String JoinType) {
         try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Packagee> query = builder.createQuery(Packagee.class);
        Root<Packagee> root = query.from(Packagee.class);
        query.select(root).where(builder.like(root.get(ColumnName), ISPHelper.setLikeType(ColumnValue, JoinType))).orderBy(builder.desc(root.get("id")));
        return session.createQuery(query).list();
        }
        finally{
            session.close();
        }
    }

    @Override
    public void delete(Packagee object) {
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
