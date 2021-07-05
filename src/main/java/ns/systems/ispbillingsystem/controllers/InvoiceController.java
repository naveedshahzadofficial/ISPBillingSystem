/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ns.systems.ispbillingsystem.controllers;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.StringConverter;
import ns.systems.ispbillingsystem.helpers.ISPConstants;
import ns.systems.ispbillingsystem.helpers.ISPHelper;
import ns.systems.ispbillingsystem.models.Customer;
import ns.systems.ispbillingsystem.models.CustomerPackage;
import ns.systems.ispbillingsystem.models.Invoice;
import ns.systems.ispbillingsystem.models.InvoicePackage;
import ns.systems.ispbillingsystem.models.Setting;
import ns.systems.ispbillingsystem.repositories.CustomerRepository;
import ns.systems.ispbillingsystem.repositories.InvoiceRepository;
import ns.systems.ispbillingsystem.repositories.SettingRepository;
import org.apache.log4j.Logger;
/**
 *
 * @author naveed
 */
public class InvoiceController implements Initializable {

    private static final Logger logger = Logger.getLogger(InvoiceController.class.getName());
    private static final InvoiceRepository invoiceRepository = new InvoiceRepository();
    private static final CustomerRepository customerRepository = new CustomerRepository();
    private static final SettingRepository settingRepository = new SettingRepository();

    private static String prefix;
    private static Setting setting;

    private ObservableList<Invoice> invoices;
    private FilteredList<Invoice> filteredList;

    private ObservableList<Customer> customers;
    private ObservableList<String> year_months = FXCollections.observableArrayList();

    @FXML
    private JFXComboBox<String> cbMonth;
    @FXML
    private JFXComboBox<Customer> cbCustomer;
    @FXML
    private JFXTextField searchBox;
    @FXML
    private StackPane tvStackPane;
    @FXML
    private TableColumn<Invoice, Integer> idColumn;
    @FXML
    private TableColumn<Invoice, String> codeColumn;
    @FXML
    private TableColumn<Invoice, String> nameColumn;
    @FXML
    private TableColumn<Invoice, String> billMonthColumn;
    @FXML
    private TableColumn<Invoice, Double> amountColumn;
    @FXML
    private TableColumn<Invoice, String> statusColumn;
    @FXML
    private TableColumn<Invoice, String> actionColumn;
    @FXML
    private DatePicker dpInvoiceDate;
    @FXML
    private TableView<Invoice> tableView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            idColumn.setCellValueFactory(new PropertyValueFactory<Invoice, Integer>("id"));
            codeColumn.setCellValueFactory(new PropertyValueFactory<Invoice, String>("code"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<Invoice, String>("name"));
            billMonthColumn.setCellValueFactory(new PropertyValueFactory<Invoice, String>("billingMonth"));
            amountColumn.setCellValueFactory(new PropertyValueFactory<Invoice, Double>("amount"));
            amountColumn.setCellFactory(tc -> {
                TableCell cell
                        = new TableCell() {
                    @Override
                    protected void updateItem(Object billingAmont, boolean empty) {
                        if (empty || billingAmont == null) {
                            setText("");
                        } else {
                            setText(ISPHelper.getPriceFormat(Double.parseDouble(billingAmont.toString())));
                        }
                    }
                };
                return cell;
            });

            statusColumn.setCellValueFactory(new PropertyValueFactory<Invoice, String>("status"));
            statusColumn.setCellFactory(tc -> {
                TableCell cell
                        = new TableCell() {
                    @Override
                    protected void updateItem(Object status, boolean empty) {
                        if (empty || status == null) {
                            setText("");
                        } else {
                            setText(ISPHelper.getHumanInvoiceStatus(Integer.parseInt(status.toString())));
                        }
                    }
                };
                return cell;
            });

            // action column
            Callback<TableColumn<Invoice, String>, TableCell<Invoice, String>> cellFoctory = (TableColumn<Invoice, String> param) -> {
                // make cell containing buttons
                final TableCell<Invoice, String> cell = new TableCell<Invoice, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        //that cell created only on non-empty rows
                        if (empty) {
                            setGraphic(null);
                            setText(null);

                        } else {

                            GlyphIcon invoiceIcon = GlyphsBuilder.create(FontAwesomeIcon.class).icon(FontAwesomeIconName.FILE_PDF_ALT).size("20px").styleClass("edit-icon").build();

                            invoiceIcon.setOnMouseClicked((MouseEvent t) -> {
                                Invoice oldInvoice = tableView.getSelectionModel().getSelectedItem();
                                String FileName = "./Invoices/"+oldInvoice.getBillingMonth()+"/"+oldInvoice.getCode()+"-"+oldInvoice.getName()+".pdf";
                                System.out.println(FileName);
                                File pdfFile = new File(FileName);
                                if (pdfFile.exists()) {

                                   if (Desktop.isDesktopSupported()) {
                                       try {
                                           Desktop.getDesktop().open(pdfFile);
                                       } catch (IOException e) {
                                           e.printStackTrace();
                                       }
                                   } else {
                                        System.out.println("Awt Desktop is not supported!");
                                    }

                                } else {
                                    System.out.println("File is not exists!");
                                }

                            });



                        HBox managebtn = new HBox(invoiceIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(invoiceIcon, new Insets(2, 3, 0, 2));
                        setGraphic(managebtn);
                        setText(null);
                    }
                }

            };

            return cell;
        };
            actionColumn.setCellFactory(cellFoctory);

            invoices = FXCollections.observableArrayList(invoiceRepository.list());
            tableView.setItems(invoices);
            filteredList = new FilteredList<>(invoices, e -> true);

            dpInvoiceDate.setValue(LocalDate.now());
            customers = FXCollections.observableArrayList(customerRepository.list());
            customers.add(0, new Customer("All", "All Customers", 0.0, 0.0, null, null, null, 1));
            cbCustomer.setItems(customers);
            cbCustomer.setCellFactory((ListView<Customer> p) -> {
                return new ListCell<Customer>() {
                    @Override
                    protected void updateItem(Customer customer, boolean bln) {
                        super.updateItem(customer, bln);
                        if (customer != null) {
                            if ("All".equals(customer.getCode())) {
                                setText(customer.getName());
                            } else {
                                setText(customer.getCode() + " : " + customer.getName());
                            }
                        } else {
                            setText(null);
                        }
                    }

                };
            });
            cbCustomer.setConverter(new StringConverter<Customer>() {
                @Override
                public String toString(Customer customer) {
                    return customer == null ? null : ("All".equals(customer.getCode()) ? customer.getName() : customer.getCode() + " : " + customer.getName());
                }

                @Override
                public Customer fromString(String string) {
                    return null;
                }
            });

            cbCustomer.getSelectionModel().selectFirst();

            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            String[] shortMonths = new DateFormatSymbols().getShortMonths();
            for (int i = 0; i <= 1; i++) {
                for (String shortMonth : shortMonths) {
                    if (!shortMonth.isBlank()) {
                        year_months.add(shortMonth + "-" + (year - i));
                    }
                }
            }

            cbMonth.setItems(year_months);
            cbMonth.getSelectionModel().select(shortMonths[month] + "-" + year);

            setting = ISPHelper.getSettingCode(settingRepository, ISPConstants.INVOICE_CODE_FIELD, ISPConstants.INVOICE_PREFIX_FIELD, ISPConstants.INVOICE_PREFIX);
            prefix = ISPHelper.getSettingPrefix(settingRepository, ISPConstants.INVOICE_PREFIX_FIELD);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void filterKeyReleased(KeyEvent event) {
    }

    @FXML
    private void clearFilter(MouseEvent event) {
    }

    @FXML
    private void filterSearchButton(MouseEvent event) {
    }

    @FXML
    private void clearInvoice(MouseEvent event) {

    }

    @FXML
    private void saveInvoice(MouseEvent event) {

        Customer customer = cbCustomer.getValue();

        logger.info(customer.getCode() + cbMonth.getValue() + dpInvoiceDate.getValue());

        if ("All".equals(customer.getCode())) {
            customers.stream().forEach((cust) -> {

                if ("All".equals(cust.getCode())) {
                    return;
                }
                
                Integer is_invoice = invoiceRepository.checkInvoice(cust, cbMonth.getValue());
                if(is_invoice>0)
                     return;
                
                String completedCode = prefix + setting.getOptionValue();
                Double total_bill = ((Collection<? extends CustomerPackage>) cust.getPackages()).stream().collect(Collectors.summingDouble(cp -> cp.getQuantity() * cp.getPackagee().getPrice()));

                Invoice new_invoice = new Invoice(completedCode, cust.getName(), total_bill, cust, 1, dpInvoiceDate.getValue(), cbMonth.getValue());
                Set<InvoicePackage> invoicePackages = cust.getPackages().stream().map(pkg -> new InvoicePackage(new_invoice, pkg.getPackagee(), pkg.getPackagee().getName(), pkg.getQuantity(), pkg.getPackagee().getPrice(), pkg.getPackagee().getActualPrice())).collect(Collectors.toSet());

                new_invoice.setInvoicePackages(invoicePackages);
                invoiceRepository.save(new_invoice);
                invoices.add(0, new_invoice);
                tableView.refresh();
                invoicePDF(new_invoice);
                setting.setOptionValue(String.valueOf(Integer.parseInt(setting.getOptionValue()) + 1));
                settingRepository.update(setting);

            });
        } else {
            Integer is_invoice = invoiceRepository.checkInvoice(customer, cbMonth.getValue());
                System.out.println(is_invoice);
                if(is_invoice>0){
                                Alert alert = new Alert(AlertType.WARNING);
                                alert.setTitle("Warring");
                                alert.setHeaderText("");
                                alert.setContentText("Your Invoice "+cbMonth.getValue()+" for "+customer.getName()+" has been already generated.");
                                alert.show();
                    
                }else{
                String completedCode = prefix + setting.getOptionValue();
                Double total_bill = ((Collection<? extends CustomerPackage>) customer.getPackages()).stream().collect(Collectors.summingDouble(cp -> cp.getQuantity() * cp.getPackagee().getPrice()));
                Invoice new_invoice = new Invoice(completedCode, customer.getName(), total_bill, customer, 1, dpInvoiceDate.getValue(), cbMonth.getValue());
                Set<InvoicePackage> invoicePackages = customer.getPackages().stream().map(pkg -> new InvoicePackage(new_invoice, pkg.getPackagee(), pkg.getPackagee().getName(), pkg.getQuantity(), pkg.getPackagee().getPrice(), pkg.getPackagee().getActualPrice())).collect(Collectors.toSet());
                new_invoice.setInvoicePackages(invoicePackages);
                invoiceRepository.save(new_invoice);
                invoices.add(0, new_invoice);
                tableView.refresh();
                invoicePDF(new_invoice);
                setting.setOptionValue(String.valueOf(Integer.parseInt(setting.getOptionValue()) + 1));
                settingRepository.update(setting);
                }

        }

    }

    private void invoicePDF(Invoice invoice) {

        try {
            String path = "./Invoices/" + invoice.getBillingMonth() + "/" + invoice.getCode() + "-" + invoice.getName() + ".pdf";
            File pdfFile = new File(path);
            pdfFile.getParentFile().mkdirs();
            pdfFile.createNewFile(); // if file already exists will do nothing

            Rectangle pageSize = new Rectangle(265, 302);
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(pdfFile));
            //pdfDocument.setDefaultPageSize(PageSize.LETTER);
            Document document = new Document(pdfDocument, new PageSize(pageSize));
            document.setMargins(0, 0, 0, 0);

            Table header = new Table(3);
            header.setWidth(UnitValue.createPercentValue(100));

            Image logoImage = new Image(ImageDataFactory.create(getClass().getResource("/ns/systems/ispbillingsystem/images/parents.png")));
            logoImage.setAutoScale(true);
            logoImage.setHeight(32f);

            header.addCell(new Cell(2, 1)
                    .add(logoImage)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(Border.NO_BORDER)
            );

            header.addCell(new Cell(0, 1)
                    .add(new Paragraph().add(new Text("Invoice No: ").setFontSize(8f).setBold()).add(new Text(invoice.getCode()).setFontSize(8f)).setTextAlignment(TextAlignment.LEFT))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setPaddingTop(5f)
                    .setBorder(Border.NO_BORDER)
            );

            header.addCell(new Cell(0, 1)
                    .add(new Paragraph().add(new Text("Bill Month: ").setFontSize(8f).setBold()).add(new Text(invoice.getBillingMonth()).setFontSize(8f)).setTextAlignment(TextAlignment.LEFT))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.TOP)
                    .setPaddingTop(5f)
                    .setBorder(Border.NO_BORDER)
            );

            header.addCell(new Cell(0, 1)
                    .add(new Paragraph().add(new Text("Name: ").setFontSize(8f).setBold()).add(new Text(invoice.getName()).setFontSize(8f)).setTextAlignment(TextAlignment.LEFT))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.TOP)
                    .setBorder(Border.NO_BORDER)
            );

            header.addCell(new Cell(0, 1)
                    .add(new Paragraph().add(new Text("Mobile No: ").setFontSize(8f).setBold()).add(new Text(invoice.getCustomer().getPhone()).setFontSize(8f)).setTextAlignment(TextAlignment.LEFT))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.TOP)
                    .setBorder(Border.NO_BORDER)
            );

            document.add(header);

            Table packages = new Table(3);
            packages.setWidth(UnitValue.createPercentValue(100));
            packages.addCell(new Cell()
                    .add(new Paragraph().add(new Text("Package").setFontSize(8f).setBold()).setTextAlignment(TextAlignment.CENTER))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(new DeviceRgb(237, 237, 237))
                    .setBorder(Border.NO_BORDER)
            );

            packages.addCell(new Cell()
                    .add(new Paragraph().add(new Text("Qty.").setFontSize(8f).setBold()).setTextAlignment(TextAlignment.CENTER))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(new DeviceRgb(237, 237, 237))
                    .setBorder(Border.NO_BORDER)
            );

            packages.addCell(new Cell()
                    .add(new Paragraph().add(new Text("Amount").setFontSize(8f).setBold()).setTextAlignment(TextAlignment.CENTER))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBackgroundColor(new DeviceRgb(237, 237, 237))
                    .setBorder(Border.NO_BORDER)
            );
            invoice.getInvoicePackages().stream().forEach((invPkg) -> {
                packages.addCell(new Cell()
                        .add(new Paragraph().add(new Text(invPkg.getPackageName()).setFontSize(8f)).setTextAlignment(TextAlignment.CENTER))
                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setBorder(Border.NO_BORDER)
                );

                packages.addCell(new Cell()
                        .add(new Paragraph().add(new Text(String.valueOf(invPkg.getQuantity())).setFontSize(8f)).setTextAlignment(TextAlignment.CENTER))
                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setBorder(Border.NO_BORDER)
                );

                packages.addCell(new Cell()
                        .add(new Paragraph().add(new Text(ISPHelper.getPriceFormat(invPkg.getPrice())).setFontSize(8f)).setTextAlignment(TextAlignment.CENTER))
                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setBorder(Border.NO_BORDER)
                );

            });

            packages.addCell(new Cell(0, 2)
                    .add(new Paragraph().add(new Text("Total").setFontSize(8f).setBold()).setTextAlignment(TextAlignment.RIGHT))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(Border.NO_BORDER)
            );

            packages.addCell(new Cell()
                    .add(new Paragraph().add(new Text(ISPHelper.getPriceFormat(invoice.getAmount())).setFontSize(8f).setBold()).setTextAlignment(TextAlignment.CENTER))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(Border.NO_BORDER)
            );

            packages.addCell(new Cell(0, 2)
                    .add(new Paragraph().add(new Text("Cash").setFontSize(8f).setBold()).setTextAlignment(TextAlignment.RIGHT))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(Border.NO_BORDER)
            );

            packages.addCell(new Cell()
                    .add(new Paragraph().add(new Text("0.00").setFontSize(8f).setBold()).setTextAlignment(TextAlignment.CENTER))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(Border.NO_BORDER)
            );

            packages.addCell(new Cell(0, 2)
                    .add(new Paragraph().add(new Text("Balance").setFontSize(8f).setBold()).setTextAlignment(TextAlignment.RIGHT))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(Border.NO_BORDER)
            );

            packages.addCell(new Cell()
                    .add(new Paragraph().add(new Text(ISPHelper.getPriceFormat(invoice.getAmount())).setFontSize(8f).setBold()).setTextAlignment(TextAlignment.CENTER))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(Border.NO_BORDER)
            );

            document.add(packages);

            Table footer = new Table(4);
            footer.setWidth(UnitValue.createPercentValue(100));
            footer.setMarginTop(10f);
            footer.addCell(new Cell(2, 1)
                    .setWidth(UnitValue.createPercentValue(25))
                    .add(new Paragraph().add(new Text("Issuer Sign:").setFontSize(8f).setBold()).setTextAlignment(TextAlignment.CENTER))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(Border.NO_BORDER)
            );

            footer.addCell(new Cell(0, 2)
                    .add(new Paragraph().add(new Text("").setFontSize(8f).setBold()).setPaddingTop(5f).setTextAlignment(TextAlignment.CENTER))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(Border.NO_BORDER)
                    .setBorderBottom(new SolidBorder(1f))
            );

            footer.addCell(new Cell()
                    .setWidth(UnitValue.createPercentValue(10))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(Border.NO_BORDER)
            );

            footer.addCell(new Cell(0, 3)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(Border.NO_BORDER)
            );

            footer.addCell(new Cell(2, 1)
                    .setWidth(UnitValue.createPercentValue(25))
                    .add(new Paragraph().add(new Text("Receiver Sign:").setFontSize(8f).setBold()).setTextAlignment(TextAlignment.CENTER))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(Border.NO_BORDER)
            );

            footer.addCell(new Cell(0, 2)
                    .add(new Paragraph().add(new Text("").setFontSize(8f).setBold()).setPaddingTop(5f).setTextAlignment(TextAlignment.CENTER))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(Border.NO_BORDER)
                    .setBorderBottom(new SolidBorder(1f))
            );

            footer.addCell(new Cell()
                    .setWidth(UnitValue.createPercentValue(10))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(Border.NO_BORDER)
            );

            footer.addCell(new Cell(0, 3)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(Border.NO_BORDER)
            );

            footer.addCell(new Cell(0, 4)
                    .add(new Paragraph().add(new Text("").setFontSize(8f).setBold()).setPaddingTop(5f).setTextAlignment(TextAlignment.CENTER))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(Border.NO_BORDER)
                    .setBorderBottom(new SolidBorder(1f))
            );

            footer.addCell(new Cell()
                    .setWidth(UnitValue.createPercentValue(25))
                    .add(new Paragraph().add(new Text("Business Date:").setFontSize(8f).setBold()).setPaddingTop(5f).setTextAlignment(TextAlignment.CENTER))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(Border.NO_BORDER)
            );

            footer.addCell(new Cell(0,3)
                    .add(new Paragraph()
                    .add(new Text(ISPHelper.getLocalDateFormat(invoice.getInvoiceDate())).setFontSize(8f))
                    .add(new Text("\tDue Date:  ").setFontSize(8f).setBold())
                    .add(new Text(ISPHelper.getDateFormat(ISPHelper.getStringDateFormat("05-" + invoice.getBillingMonth()))).setFontSize(8f))
                    )
                    .setPaddingTop(5f).setTextAlignment(TextAlignment.LEFT)
                    .setHorizontalAlignment(HorizontalAlignment.LEFT)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(Border.NO_BORDER)
            );

            footer.addCell(new Cell(0, 4)
                    .setWidth(UnitValue.createPercentValue(25))
                    .add(new Paragraph()
                            .add(new Text("We Know You Have A Choice. Thanks For Choosing Us\n").setFontSize(8f))
                            .add(new Text("Ahmad Net Service\n").setBold().setFontSize(8f))
                            .add(new Text("0301-3000168  -  03216593500").setFontSize(8f))
                            .setTextAlignment(TextAlignment.CENTER))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(Border.NO_BORDER)
            );

            document.add(footer);
            document.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
