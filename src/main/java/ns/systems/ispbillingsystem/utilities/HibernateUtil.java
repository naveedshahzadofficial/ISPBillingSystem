/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ns.systems.ispbillingsystem.utilities;

import ns.systems.ispbillingsystem.models.Company;
import ns.systems.ispbillingsystem.models.Package;
import ns.systems.ispbillingsystem.models.Setting;

import org.hibernate.SessionFactory;
import java.util.Properties;
import ns.systems.ispbillingsystem.models.Customer;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

/**
 *
 * @author naveed
 */
public class HibernateUtil {
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;
    
    public static SessionFactory getSessionFactory(){
        if(sessionFactory == null){
            try{
                Configuration configuration = new Configuration();
                
                // Hibernate Settings equivalent to hibernate.cfg.xml's properties
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                settings.put(Environment.URL, "jdbc:mysql://localhost:3306/isp_billing_system?useSSL=false");
                settings.put(Environment.USER, "root");
                settings.put(Environment.PASS, "root");
                settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL57Dialect");
                
                settings.put(Environment.SHOW_SQL, true);
                settings.put(Environment.FORMAT_SQL, true);
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                settings.put(Environment.HBM2DDL_AUTO, "update");
                settings.put(Environment.ENABLE_LAZY_LOAD_NO_TRANS, true);
                settings.put(Environment.JDBC_TIME_ZONE, "Asia/Karachi");
                settings.put(Environment.LOG_JDBC_WARNINGS, false);
                
                configuration.setProperties(settings);
                configuration.addAnnotatedClass(Company.class);
                configuration.addAnnotatedClass(Package.class);
                configuration.addAnnotatedClass(Setting.class);
                configuration.addAnnotatedClass(Customer.class);
                
              
                 //ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
                
                
                 registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
                 
                 sessionFactory =  configuration.buildSessionFactory(registry);
            } catch(Exception e){
                e.printStackTrace();
                if (registry != null) {
		   StandardServiceRegistryBuilder.destroy(registry);
		}
            }
        }
        
        return sessionFactory;
    }
    
    public static void shutdown() {
		if (registry != null) {
			StandardServiceRegistryBuilder.destroy(registry);
		}
	}
}

/*
HBM2DDL_AUTO
none - No action is performed. The schema will not be generated.
create-only - The database schema will be generated.
drop - The database schema will be dropped and created afterwards.
create - The database schema will be dropped and created afterwards.
create-drop - The database schema will be dropped and created afterwards. Upon closing the the SessionFactory, the database schema will be dropped.
validate - The database schema will be validated using the entity mappings.
update - The database schema will be updated by comparing the existing database schema with the entity mappings.

*/