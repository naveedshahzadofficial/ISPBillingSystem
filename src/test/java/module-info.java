module ns.systems.ispbillingsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.graphicsEmpty;
    requires java.base;
    requires java.naming; 
    requires java.persistence;
    requires fontawesomefx;
    requires com.jfoenix;
    requires log4j;
    requires java.sql;
    requires org.hibernate.commons.annotations;
    requires org.hibernate.orm.core;
    requires org.jboss.logging;
    requires org.controlsfx.controls;
    requires junit;
   
    opens ns.systems.ispbillingsystem to javafx.fxml;
    exports ns.systems.ispbillingsystem;
    
    
    
}