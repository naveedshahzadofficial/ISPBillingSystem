module ispbillingsystem {
    
    // JAVA
    requires java.base;
    requires java.naming; 
    requires java.persistence;
    requires java.desktop;
    
    // JAVA Fx
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.graphicsEmpty;
    
    // Liberies
    requires fontawesomefx;
    requires com.jfoenix;
    requires log4j;
    requires pdfa;
    requires layout;
    requires kernel;
    requires io;
    requires java.sql;
    requires org.hibernate.commons.annotations;
    requires org.hibernate.orm.core;
    requires org.jboss.logging;
    requires org.controlsfx.controls;
    
    // Opens and Exports
   
    opens ns.systems.ispbillingsystem to javafx.fxml;
    exports ns.systems.ispbillingsystem;
    
    opens ns.systems.ispbillingsystem.controllers to javafx.fxml;
    exports ns.systems.ispbillingsystem.controllers;
    
    opens ns.systems.ispbillingsystem.models  to org.hibernate.orm.core; 
    exports ns.systems.ispbillingsystem.models;
    
    exports ns.systems.ispbillingsystem.repositories;
}