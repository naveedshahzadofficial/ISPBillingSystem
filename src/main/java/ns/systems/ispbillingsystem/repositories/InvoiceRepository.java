/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ns.systems.ispbillingsystem.repositories;

import java.util.List;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ns.systems.ispbillingsystem.helpers.ISPHelper;
import ns.systems.ispbillingsystem.models.Customer;
import ns.systems.ispbillingsystem.services.CommonService;
import ns.systems.ispbillingsystem.models.Invoice;
import ns.systems.ispbillingsystem.utilities.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
/**
 *
 * @author naveed
 */
public class InvoiceRepository implements CommonService<Invoice>{

    private static SessionFactory sessionFactory;
    private Transaction transaction;
    private Session session;
    
    private static final Logger logger = Logger.getLogger(InvoiceRepository.class.getName());

    
     public InvoiceRepository() {
        this.sessionFactory =  HibernateUtil.getSessionFactory();
    }
     
    @Override
    public List<Invoice> list() {
          try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Invoice> query = builder.createQuery(Invoice.class);
        Root<Invoice> root = query.from(Invoice.class);
        query.select(root).orderBy(builder.desc(root.get("id")));
        return session.createQuery(query).list();
        }
        finally{
            session.close();
        }
    }
    

    @Override
    public void save(Invoice object) {
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
    public void update(Invoice object) {
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
    public Invoice find(Long Id) {
         try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Invoice> query = builder.createQuery(Invoice.class);
        Root<Invoice> root = query.from(Invoice.class);
        query.select(root).where(builder.equal(root.get("id"), Id));
        return session.createQuery(query).uniqueResult();
        }
        finally{
            session.close();
        }
    }

    @Override
    public Invoice findByKey(String ColumnName, String ColumnValue) {
         try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Invoice> query = builder.createQuery(Invoice.class);
        Root<Invoice> root = query.from(Invoice.class);
        query.select(root).where(builder.equal(root.get(ColumnName), ColumnValue));
        return session.createQuery(query).uniqueResult();
        }
        finally{
            session.close();
        }
    }

    @Override
    public List<Invoice> getWhere(String ColumnName, String ColumnValue) {
        try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Invoice> query = builder.createQuery(Invoice.class);
        Root<Invoice> root = query.from(Invoice.class);
        query.select(root).where(builder.equal(root.get(ColumnName), ColumnValue)).orderBy(builder.desc(root.get("id")));
        return session.createQuery(query).list();
        }
        finally{
            session.close();
        }
    }
    
    @Override
    public List<Invoice> whereLike(String ColumnName, String ColumnValue, String JoinType) {
         try{
        session = sessionFactory.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Invoice> query = builder.createQuery(Invoice.class);
        Root<Invoice> root = query.from(Invoice.class);
        query.select(root).where(builder.like(root.get(ColumnName), ISPHelper.setLikeType(ColumnValue, JoinType))).orderBy(builder.desc(root.get("id")));
        return session.createQuery(query).list();
        }
        finally{
            session.close();
        }
    }

    @Override
    public void delete(Invoice object) {
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
    
    public Integer checkInvoice(Customer customer,String billingMonth){
     try{
        session = sessionFactory.openSession();
        Query query = session.createQuery("SELECT count(id) FROM Invoice inv WHERE billingMonth = :billingMonth and customer = :customer");
        query.setParameter("billingMonth", billingMonth);
        query.setParameter("customer", customer);
        return ((Number) query.getSingleResult()).intValue();
        }
        finally{
            session.close();
        }
    }
    
}
