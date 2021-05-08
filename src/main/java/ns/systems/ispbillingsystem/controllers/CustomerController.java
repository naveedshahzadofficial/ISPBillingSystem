package ns.systems.ispbillingsystem.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.util.StringConverter;
import ns.systems.ispbillingsystem.helpers.ISPConstants;
import ns.systems.ispbillingsystem.helpers.ISPHelper;
import ns.systems.ispbillingsystem.helpers.ISPValidation;
import ns.systems.ispbillingsystem.repositories.CustomerRepository;
import ns.systems.ispbillingsystem.repositories.SettingRepository;
import ns.systems.ispbillingsystem.models.Customer;
import ns.systems.ispbillingsystem.models.Setting;
import ns.systems.ispbillingsystem.models.Package;
import ns.systems.ispbillingsystem.repositories.PackageRepository;
import org.apache.log4j.Logger;

public class CustomerController implements Initializable {

    private static final Logger logger = Logger.getLogger(CustomerController.class.getName());

    private static final CustomerRepository customerRepository = new CustomerRepository();
    private static final PackageRepository packageRepository = new PackageRepository();
    private static final SettingRepository settingRepository = new SettingRepository();

    private static String prefix;
    private static Setting setting;
    private boolean isUpdated = false;
    private Customer oldCustomer = null;
    private int selectedIndex = 0;

    private ObservableList<Customer> customers;
    private FilteredList<Customer> filteredList;
    RequiredFieldValidator validator = new RequiredFieldValidator();

    @FXML
    private JFXTextField tfCode;
    @FXML
    private JFXTextField tfName;
    @FXML
    private JFXTextField tfPhone;
    @FXML
    private JFXRadioButton rbActive;
    @FXML
    private ToggleGroup tgPackageStatus;
    @FXML
    private JFXRadioButton rbInActive;
    @FXML
    private JFXTextField searchBox;
    @FXML
    private StackPane tvStackPane;
    @FXML
    private TableView<Customer> tableView;
    @FXML
    private TableColumn<Customer, Integer> idColumn;
    @FXML
    private TableColumn<Customer, String> codeColumn;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    @FXML
    private TableColumn<Customer, String> statusColumn;
    @FXML
    private TableColumn<Customer, String> actionColumn;
    @FXML
    private JFXTextField tfCnic;
    @FXML
    private JFXTextField tfDiscount;
    @FXML
    private JFXTextArea taAddress;
    @FXML
    private TableColumn<Customer, String> discountColumn;
    @FXML
    private JFXListView<Package> lvPackages;

    ObservableList<Package> packages;
    @FXML
    private TableColumn<?, ?> cnicColumn;
    @FXML
    private TableColumn<?, ?> pkgColumn;
    @FXML
    private TableColumn<?, ?> priceColumn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {

            idColumn.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("id"));
            codeColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("code"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
            discountColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("discount"));

            statusColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("status"));
            statusColumn.setCellFactory(tc -> {
                TableCell cell
                        = new TableCell() {
                    @Override
                    protected void updateItem(Object status, boolean empty) {
                        if (empty || status == null) {
                            setText("");
                        } else {
                            setText(ISPHelper.getHumanStatus(Integer.parseInt(status.toString())));
                        }
                    }
                };
                return cell;
            });

            // action column
            Callback<TableColumn<Customer, String>, TableCell<Customer, String>> cellFoctory = (TableColumn<Customer, String> param) -> {
                // make cell containing buttons
                final TableCell<Customer, String> cell = new TableCell<Customer, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        //that cell created only on non-empty rows
                        if (empty) {
                            setGraphic(null);
                            setText(null);

                        } else {

                            GlyphIcon deleteIcon = GlyphsBuilder.create(FontAwesomeIcon.class).icon(FontAwesomeIconName.TRASH).size("20px").styleClass("delete-icon").build();
                            GlyphIcon editIcon = GlyphsBuilder.create(FontAwesomeIcon.class).icon(FontAwesomeIconName.PENCIL_SQUARE).size("20px").styleClass("edit-icon").build();

                            editIcon.setOnMouseClicked((MouseEvent t) -> {
                                selectedIndex = tableView.getSelectionModel().getSelectedIndex();
                                oldCustomer = tableView.getSelectionModel().getSelectedItem();
                                isUpdated = true;
                                tfCode.setText(oldCustomer.getCode());
                                tfName.setText(oldCustomer.getName());
                                tfPhone.setText(oldCustomer.getPhone());
                                tfCnic.setText(oldCustomer.getCnic());
                                tfDiscount.setText(oldCustomer.getDiscount().toString());
                                taAddress.setText(oldCustomer.getAddress());

                                if (oldCustomer.getStatus() == 1) {
                                    rbActive.setSelected(true);
                                } else {
                                    rbInActive.setSelected(true);
                                }

                                packages.forEach((pkg) -> {
                                    BooleanProperty selected = new SimpleBooleanProperty(false);
                                    pkg.setSelected(selected);
                                });
                                lvPackages.refresh();

                                oldCustomer.getPackages().forEach((oldPkg) -> {
                                    packages.forEach((pkg) -> {
                                        if (pkg.toString().equals(oldPkg.toString())) {
                                            BooleanProperty selected = new SimpleBooleanProperty(true);
                                            pkg.setSelected(selected);
                                        }
                                    });
                                });

                                lvPackages.refresh();

                            });

                            deleteIcon.setOnMouseClicked((MouseEvent t) -> {
                                selectedIndex = tableView.getSelectionModel().getSelectedIndex();
                                oldCustomer = tableView.getSelectionModel().getSelectedItem();

                                JFXDialogLayout content = new JFXDialogLayout();
                                content.setHeading(new Text("Confirmation Dailog"));
                                content.setBody(new Text("Are you sure to delete " + oldCustomer.getCode() + "?"));
                                JFXDialog dialog = new JFXDialog(tvStackPane, content, JFXDialog.DialogTransition.CENTER);
                                JFXButton yes = new JFXButton("Yes");
                                yes.setButtonType(JFXButton.ButtonType.RAISED);
                                yes.getStyleClass().add("btn-danger");
                                JFXButton cancel = new JFXButton("Cancel");
                                cancel.setButtonType(JFXButton.ButtonType.RAISED);
                                cancel.getStyleClass().add("btn-default");
                                content.setActions(cancel, yes);
                                dialog.show();

                                yes.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent t) {
                                        customerRepository.delete(oldCustomer);
                                        customers.remove(oldCustomer);
                                        tableView.refresh();
                                        //packageTable.getItems().remove(selectedIndex);
                                        dialog.close();
                                    }
                                });

                                cancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent t) {
                                        dialog.close();
                                    }
                                });

//                                Alert alert = new Alert(AlertType.CONFIRMATION);
//                                alert.setTitle("Confirmation Dailog");
//                                alert.setHeaderText("");
//                                alert.setContentText("Are you sure to delete "+oldPackage.getCode()+"?");
//                                Optional<ButtonType> action = alert.showAndWait();
//                                if (action.get() == ButtonType.OK) {
//                                 packageRepository.delete(oldPackage);
//                                 packageTable.getItems().remove(selectedIndex);
//                                }
                            });

                            HBox managebtn = new HBox(editIcon, deleteIcon);
                            managebtn.setStyle("-fx-alignment:center");
                            HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                            HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

                            setGraphic(managebtn);

                            setText(null);

                        }
                    }

                };

                return cell;
            };

            actionColumn.setCellFactory(cellFoctory);

            customers = FXCollections.observableArrayList(customerRepository.list());
            tableView.setItems(customers);
            filteredList = new FilteredList<>(customers, e -> true);

            setting = ISPHelper.getSettingCode(settingRepository, ISPConstants.CUSTOMER_CODE_FIELD, ISPConstants.CUSTOMER_PREFIX_FIELD, ISPConstants.CUSTOMER_PREFIX);
            prefix = ISPHelper.getSettingPrefix(settingRepository, ISPConstants.CUSTOMER_PREFIX_FIELD);
            tfCode.setText(prefix + setting.getOptionValue());

            lvPackages.setCellFactory(CheckBoxListCell.forListView(Package::getSelected, new StringConverter<Package>() {
                @Override
                public String toString(Package pkg) {
                    return pkg.getName() + " - Rs. " + pkg.getPrice();
                }

                @Override
                public Package fromString(String string) {
                    return null;
                }

            }));

            packages = FXCollections.observableArrayList(packageRepository.getWhere("status", "1"));
            lvPackages.setItems(packages);

            validator.setIcon(GlyphsBuilder.create(FontAwesomeIcon.class).icon(FontAwesomeIconName.WARNING).size("1em").build());

            ISPValidation.textFieldRequired(validator, tfName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clearForm(MouseEvent event) {
        selectedIndex = 0;
        isUpdated = false;
        oldCustomer = null;
        tfCode.setText(prefix + setting.getOptionValue());
        tfName.setText("");
        tfPhone.setText("");
        tfCnic.setText("");
        tfDiscount.setText("");
        taAddress.setText("");
        rbActive.setSelected(true);
        packages.forEach((pkg) -> {
            BooleanProperty selected = new SimpleBooleanProperty(false);
            pkg.setSelected(selected);
        });
        lvPackages.refresh();

    }

    @FXML
    private void saveForm(MouseEvent event) {
        if (!tfName.validate()) {
            return;
        }

        RadioButton rbSelected
                = (RadioButton) tgPackageStatus.getSelectedToggle();
        Integer selectedStatus = (rbSelected != null && "Active".equals(rbSelected.getText())) ? 1 : 0;
        Double discount = (tfDiscount.getText().equals("")) ? 0 : Double.parseDouble(tfDiscount.getText());
        Customer new_customer = new Customer(tfCode.getText(), tfName.getText(), discount, tfPhone.getText(), tfCnic.getText(), taAddress.getText(), selectedStatus);

        new_customer.setPackages(packages.stream().filter(e -> e.isSelected() == true).collect(Collectors.toSet()));

        if (isUpdated) {
            new_customer.setId(oldCustomer.getId());
            customerRepository.update(new_customer);
            customers.set(selectedIndex, new_customer);
            tableView.refresh();
            //packageTable.getItems().set(selectedIndex, new_package);

        } else {
            customerRepository.save(new_customer);
            customers.add(0, new_customer);
            tableView.refresh();
            //packageTable.getItems().add(0, new_package);
            setting.setOptionValue(String.valueOf(Integer.parseInt(setting.getOptionValue()) + 1));
            settingRepository.update(setting);
        }
        clearForm(event);

    }

    @FXML
    private void filterKeyReleased(KeyEvent event) {
        filteredResult();
    }

    @FXML
    private void clearFilter(MouseEvent event) {
        searchBox.setText("");
    }

    @FXML
    private void filterSearchButton(MouseEvent event) {
        filteredResult();
    }

    private void filteredResult() {

        searchBox.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredList.setPredicate((Predicate<? super Customer>) pkg -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (pkg.getCode().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (pkg.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (pkg.getPhone().toString().contains(lowerCaseFilter)) {
                    return true;
                } else if (ISPHelper.getHumanStatus(pkg.getStatus()).toLowerCase().startsWith(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
        });
        SortedList<Customer> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);
    }
}
