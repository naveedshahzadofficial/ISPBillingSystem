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
import ns.systems.ispbillingsystem.models.Company;
import ns.systems.ispbillingsystem.services.CommonService;
import ns.systems.ispbillingsystem.utilities.HibernateUtil;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.Session;


/**
 *
 * @author naveed
 */
public class CompanyRepository implements CommonService<Company>{
   
    private static SessionFactory sessionFactory;
    private Transaction transaction;
    private Session session;
    
    public CompanyRepository() {
        this.sessionFactory =  HibernateUtil.getSessionFactory();
    }
    
    @Override
    public List<Company>list(){
        try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Company> query = builder.createQuery(Company.class);
        Root<Company> root = query.from(Company.class);
        query.select(root).orderBy(builder.desc(root.get("id")));
        return session.createQuery(query).list();
        }
        finally{
            session.close();
        }
    }
    
    @Override
    public void save(Company company){
        session = sessionFactory.openSession();
     try{
         transaction = session.beginTransaction();
         session.save(company);
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
    public void update(Company company) {
        session = sessionFactory.openSession();
     try{
         transaction = session.beginTransaction();
         session.update(company);
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
    public Company find(Long Id) {
        try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Company> query = builder.createQuery(Company.class);
        Root<Company> root = query.from(Company.class);
        query.select(root).where(builder.equal(root.get("id"), Id));
        return session.createQuery(query).uniqueResult();
        }
        finally{
            session.close();
        }
    }

    @Override
    public Company findByKey(String ColumnName, String ColumnValue) {
        try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Company> query = builder.createQuery(Company.class);
        Root<Company> root = query.from(Company.class);
        query.select(root).where(builder.equal(root.get(ColumnName), ColumnValue));
        return session.createQuery(query).uniqueResult();
        }
        finally{
            session.close();
        }
    }
    
     @Override
     public List<Company> getWhere(String ColumnName, String ColumnValue){
       try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Company> query = builder.createQuery(Company.class);
        Root<Company> root = query.from(Company.class);
        query.select(root).where(builder.equal(root.get(ColumnName), ColumnValue)).orderBy(builder.desc(root.get("id")));
        return session.createQuery(query).list();
        }
        finally{
            session.close();
        }
    }
    
    @Override
    public List<Company> whereLike(String ColumnName, String ColumnValue,String JoinType){
       try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Company> query = builder.createQuery(Company.class);
        Root<Company> root = query.from(Company.class);
        query.select(root).where(builder.like(root.get(ColumnName), ISPHelper.setLikeType(ColumnValue, JoinType))).orderBy(builder.desc(root.get("id")));
        return session.createQuery(query).list();
        }
        finally{
            session.close();
        }
    }
    
 

    @Override
    public void delete(Company company) {
         session = sessionFactory.openSession();
     try{
         transaction = session.beginTransaction();
         session.delete(company);
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
