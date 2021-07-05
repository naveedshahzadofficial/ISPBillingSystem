package ns.systems.ispbillingsystem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ns.systems.ispbillingsystem.models.Customer;
import ns.systems.ispbillingsystem.models.Invoice;
import ns.systems.ispbillingsystem.repositories.CustomerRepository;
import ns.systems.ispbillingsystem.repositories.InvoiceRepository;
import ns.systems.ispbillingsystem.repositories.SettingRepository;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;



class InvoiceTests {

    private static final Logger logger = Logger.getLogger(InvoiceTests.class.getName());
    private static final InvoiceRepository invoiceRepository = new InvoiceRepository();
    private static final CustomerRepository customerRepository = new CustomerRepository();
    private static final SettingRepository settingRepository = new SettingRepository();

    @BeforeAll
    static void setUp(){
        PropertyConfigurator.configure(InvoiceTests.class.getResource("/ns/systems/ispbillingsystem/cfgs/log4j.properties"));
    }

    @Test
    void checkInvoice(){
        Customer customer = customerRepository.find(6L);
        Assertions.assertNotNull(customer);
        Integer is_invoice = invoiceRepository.checkInvoice(customer,"Jul-2021");
        Assertions.assertTrue(is_invoice==0);
    }


}
