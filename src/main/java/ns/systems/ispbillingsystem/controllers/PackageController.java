/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ns.systems.ispbillingsystem.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
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
import javafx.util.StringConverter;
import ns.systems.ispbillingsystem.helpers.ISPConstants;
import ns.systems.ispbillingsystem.helpers.ISPHelper;
import ns.systems.ispbillingsystem.helpers.ISPValidation;
import ns.systems.ispbillingsystem.models.Packagee;
import ns.systems.ispbillingsystem.models.Setting;
import ns.systems.ispbillingsystem.models.Company;
import ns.systems.ispbillingsystem.repositories.PackageRepository;
import ns.systems.ispbillingsystem.repositories.SettingRepository;
import ns.systems.ispbillingsystem.repositories.CompanyRepository;
import org.apache.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author naveed
 */
public class PackageController implements Initializable {

    private static final Logger logger = Logger.getLogger(PackageController.class.getName());

    private static final PackageRepository packageRepository = new PackageRepository();
    private static final SettingRepository settingRepository = new SettingRepository();
    private static final CompanyRepository companyRepository = new CompanyRepository();

    private static String prefix;
    private static Setting setting;
    private boolean isUpdated = false;
    private Packagee oldPackage = null;
    private int selectedIndex = 0;

    @FXML
    private TableView<Packagee> packageTable;

    @FXML
    private TableColumn<Packagee, Integer> idColumn;

    @FXML
    private TableColumn<Packagee, String> nameColumn;

    @FXML
    private TableColumn<Packagee, Double> priceColumn;

    @FXML
    private TableColumn<Packagee, String> statusColumn;

    @FXML
    private TableColumn<Packagee, String> actionColumn;
    @FXML
    private TableColumn<Packagee, Double> actualPriceColumn;
    @FXML
    private ToggleGroup tgPackageStatus;
    @FXML
    private JFXTextField tfCode;
    @FXML
    private JFXTextField tfName;
    @FXML
    private JFXComboBox<Company> cbCompany;
    @FXML
    private JFXTextField tfPrice;
    @FXML
    private JFXTextField tfAcutalPrice;
    @FXML
    private JFXRadioButton rbActive;
    @FXML
    private JFXRadioButton rbInActive;
    @FXML
    private JFXTextField searchBox;
    @FXML
    private TableColumn<Packagee, String> codeColumn;

    RequiredFieldValidator validator = new RequiredFieldValidator();
    @FXML
    private StackPane tvStackPane;
    @FXML
    private TableColumn<Packagee, Company> companyColumn;

    private ObservableList<Packagee> packages;
    private FilteredList<Packagee> filteredList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            idColumn.setCellValueFactory(new PropertyValueFactory<Packagee, Integer>("id"));
            codeColumn.setCellValueFactory(new PropertyValueFactory<Packagee, String>("code"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<Packagee, String>("name"));
            companyColumn.setCellValueFactory(new PropertyValueFactory<Packagee, Company>("company"));
            companyColumn.setCellFactory(tc -> {
                return new TableCell<Packagee, Company>() {
                    @Override
                    public void updateItem(Company company, boolean empty) {
                        if (empty || company == null) {
                            setText("");
                        } else {
                            setText(company.getName());
                        }
                    }
                };
            });
            priceColumn.setCellValueFactory(new PropertyValueFactory<Packagee, Double>("price"));
            priceColumn.setCellFactory(tc -> new TableCell<Packagee, Double>() {

                @Override
                protected void updateItem(Double price, boolean empty) {
                    super.updateItem(price, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(ISPHelper.getPriceFormat(price));
                    }
                }
            });
            actualPriceColumn.setCellValueFactory(new PropertyValueFactory<Packagee, Double>("actualPrice"));
            actualPriceColumn.setCellFactory(tc -> new TableCell<Packagee, Double>() {

                @Override
                protected void updateItem(Double price, boolean empty) {
                    super.updateItem(price, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(ISPHelper.getPriceFormat(price));
                    }
                }
            });
            statusColumn.setCellValueFactory(new PropertyValueFactory<Packagee, String>("status"));
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
            Callback<TableColumn<Packagee, String>, TableCell<Packagee, String>> cellFoctory = (TableColumn<Packagee, String> param) -> {
                // make cell containing buttons
                final TableCell<Packagee, String> cell = new TableCell<Packagee, String>() {
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
                                selectedIndex = packageTable.getSelectionModel().getSelectedIndex();
                                oldPackage = packageTable.getSelectionModel().getSelectedItem();
                                isUpdated = true;
                                tfCode.setText(oldPackage.getCode());
                                tfName.setText(oldPackage.getName());
                                cbCompany.getSelectionModel().select(oldPackage.getCompany());
                                tfAcutalPrice.setText(oldPackage.getActualPrice().toString());
                                tfPrice.setText(oldPackage.getPrice().toString());
                                if (oldPackage.getStatus() == 1) {
                                    rbActive.setSelected(true);
                                } else {
                                    rbInActive.setSelected(true);
                                }

                            });

                            deleteIcon.setOnMouseClicked((MouseEvent t) -> {
                                selectedIndex = packageTable.getSelectionModel().getSelectedIndex();
                                oldPackage = packageTable.getSelectionModel().getSelectedItem();

                                JFXDialogLayout content = new JFXDialogLayout();
                                content.setHeading(new Text("Confirmation Dailog"));
                                content.setBody(new Text("Are you sure to delete " + oldPackage.getCode() + "?"));
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
                                        packageRepository.delete(oldPackage);
                                        packages.remove(oldPackage);
                                        packageTable.refresh();
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

            packages = FXCollections.observableArrayList(packageRepository.list());
            packageTable.setItems(packages);
            filteredList = new FilteredList<>(packages, e -> true);

            setting = ISPHelper.getSettingCode(settingRepository, ISPConstants.PACKAGE_CODE_FIELD, ISPConstants.PACKAGE_PREFIX_FIELD, ISPConstants.PACKAGE_PREFIX);
            prefix = ISPHelper.getSettingPrefix(settingRepository, ISPConstants.PACKAGE_PREFIX_FIELD);

            tfCode.setText(prefix + setting.getOptionValue());

            ObservableList<Company> companies = FXCollections.observableArrayList(companyRepository.getWhere("status", "1"));
            cbCompany.setItems(companies);
            cbCompany.setCellFactory((ListView<Company> p) -> {
                return new ListCell<Company>() {
                    @Override
                    protected void updateItem(Company company, boolean bln) {
                        super.updateItem(company, bln);
                        if (company != null) {
                            setText(company.getCode() + " : " + company.getName());
                        } else {
                            setText(null);
                        }
                    }

                };
            });
            cbCompany.setConverter(new StringConverter<Company>() {
                @Override
                public String toString(Company company) {
                    return company == null ? null : company.getCode() + " : " + company.getName();
                }

                @Override
                public Company fromString(String string) {
                    return null;
                }
            });
            cbCompany.getSelectionModel().selectFirst();

            validator.setIcon(GlyphsBuilder.create(FontAwesomeIcon.class).icon(FontAwesomeIconName.WARNING).size("1em").build());

            ISPValidation.textFieldRequired(validator, tfName);
            ISPValidation.textFieldRequired(validator, tfPrice);
            ISPValidation.textFieldRequired(validator, tfAcutalPrice);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    private void clearPackage(MouseEvent event) {
        selectedIndex = 0;
        isUpdated = false;
        oldPackage = null;
        tfCode.setText(prefix + setting.getOptionValue());
        tfName.setText("");
        tfPrice.setText("");
        tfAcutalPrice.setText("");
        cbCompany.getSelectionModel().selectFirst();
        rbActive.setSelected(true);
    }

    @FXML
    private void savePackage(MouseEvent event) {
        if (!tfName.validate() || !tfPrice.validate() || !tfAcutalPrice.validate()) {
            return;
        }

        RadioButton rbSelected
                = (RadioButton) tgPackageStatus.getSelectedToggle();
        Integer selectedStatus = (rbSelected != null && "Active".equals(rbSelected.getText())) ? 1 : 0;
        Packagee new_package = new Packagee(tfCode.getText(), tfName.getText(), Double.parseDouble(tfPrice.getText()), Double.parseDouble(tfAcutalPrice.getText()), selectedStatus, cbCompany.getValue());
        if (isUpdated) {
            new_package.setId(oldPackage.getId());
            packageRepository.update(new_package);
            packages.set(selectedIndex, new_package);
            packageTable.refresh();
            //packageTable.getItems().set(selectedIndex, new_package);

        } else {
            packageRepository.save(new_package);
            packages.add(0, new_package);
            packageTable.refresh();
            //packageTable.getItems().add(0, new_package);
            setting.setOptionValue(String.valueOf(Integer.parseInt(setting.getOptionValue()) + 1));
            settingRepository.update(setting);
        }
        clearPackage(event);
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
            filteredList.setPredicate((Predicate<? super Packagee>) pkg -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (pkg.getCode().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (pkg.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (pkg.getCompany().getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (pkg.getPrice().toString().contains(lowerCaseFilter)) {
                    return true;
                } else if (pkg.getActualPrice().toString().contains(lowerCaseFilter)) {
                    return true;
                } else if (ISPHelper.getHumanStatus(pkg.getStatus()).toLowerCase().startsWith(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
        });
        SortedList<Packagee> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(packageTable.comparatorProperty());
        packageTable.setItems(sortedList);
    }

    @FXML
    private void filterKeyReleased(KeyEvent event) {
        filteredResult();
    }
}
