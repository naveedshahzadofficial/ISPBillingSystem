/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ns.systems.ispbillingsystem;

import junit.framework.TestCase;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 *
 * @author naveed
 */
public class TestLogging extends TestCase {
    
    Logger logger = Logger.getLogger(TestLogging.class.getName());
        

     
    public TestLogging(String testName) {
        super(testName);
        BasicConfigurator.configure();
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testPersist(){

     logger.info("... testPersist ...");
     
     
    
    }

    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}
}
