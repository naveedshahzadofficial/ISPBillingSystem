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
import ns.systems.ispbillingsystem.models.Package;
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
public class PackageRepository implements CommonService<Package>{

    private static SessionFactory sessionFactory;
    private Transaction transaction;
    private Session session;
    
    private static final Logger logger = Logger.getLogger(PackageRepository.class.getName());

    
     public PackageRepository() {
        this.sessionFactory =  HibernateUtil.getSessionFactory();
    }
     
    @Override
    public List<Package> list() {
          try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Package> query = builder.createQuery(Package.class);
        Root<Package> root = query.from(Package.class);
        query.select(root).orderBy(builder.desc(root.get("id")));
        return session.createQuery(query).list();
        }
        finally{
            session.close();
        }
    }
    
    public List<Package> multiList() {
          try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Package> query = builder.createQuery(Package.class);
        Root<Package> root = query.from(Package.class);
        Join<Package, Company> company = root.join("company");
       
        query.multiselect(root,company).orderBy(builder.desc(root.get("id")));
       
        return session.createQuery(query).getResultList();
        }
        finally{
            session.close();
        }
    }

    @Override
    public void save(Package object) {
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
    public void update(Package object) {
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
    public Package find(Long Id) {
         try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Package> query = builder.createQuery(Package.class);
        Root<Package> root = query.from(Package.class);
        query.select(root).where(builder.equal(root.get("id"), Id));
        return session.createQuery(query).uniqueResult();
        }
        finally{
            session.close();
        }
    }

    @Override
    public Package findByKey(String ColumnName, String ColumnValue) {
         try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Package> query = builder.createQuery(Package.class);
        Root<Package> root = query.from(Package.class);
        query.select(root).where(builder.equal(root.get(ColumnName), ColumnValue));
        return session.createQuery(query).uniqueResult();
        }
        finally{
            session.close();
        }
    }

    @Override
    public List<Package> getWhere(String ColumnName, String ColumnValue) {
        try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Package> query = builder.createQuery(Package.class);
        Root<Package> root = query.from(Package.class);
        query.select(root).where(builder.equal(root.get(ColumnName), ColumnValue)).orderBy(builder.desc(root.get("id")));
        return session.createQuery(query).list();
        }
        finally{
            session.close();
        }
    }
    
    @Override
    public List<Package> whereLike(String ColumnName, String ColumnValue, String JoinType) {
         try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Package> query = builder.createQuery(Package.class);
        Root<Package> root = query.from(Package.class);
        query.select(root).where(builder.like(root.get(ColumnName), ISPHelper.setLikeType(ColumnValue, JoinType))).orderBy(builder.desc(root.get("id")));
        return session.createQuery(query).list();
        }
        finally{
            session.close();
        }
    }

    @Override
    public void delete(Package object) {
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
