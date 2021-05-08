package ns.systems.ispbillingsystem.controllers;

import com.jfoenix.controls.JFXButton;
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
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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
import ns.systems.ispbillingsystem.helpers.ISPConstants;
import ns.systems.ispbillingsystem.helpers.ISPHelper;
import ns.systems.ispbillingsystem.helpers.ISPValidation;
import ns.systems.ispbillingsystem.repositories.CompanyRepository;
import ns.systems.ispbillingsystem.repositories.SettingRepository;
import ns.systems.ispbillingsystem.models.Company;
import ns.systems.ispbillingsystem.models.Setting;
import org.apache.log4j.Logger;

public class CompanyController implements Initializable {

    private static final Logger logger = Logger.getLogger(CompanyController.class.getName());
    
    private static final CompanyRepository companyRepository = new CompanyRepository();
    private static final SettingRepository settingRepository = new SettingRepository();
    
    private static String prefix;
    private static Setting setting;
    private boolean isUpdated = false;
    private Company oldCompany = null;
    private int selectedIndex = 0;
    
    private ObservableList<Company> companies;
    private FilteredList<Company> filteredList;
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
    private TableView<Company> tableView;
    @FXML
    private TableColumn<Company, Integer> idColumn;
    @FXML
    private TableColumn<Company, String> codeColumn;
    @FXML
    private TableColumn<Company, String> nameColumn;
    @FXML
    private TableColumn<Company, String> phoneColumn;
    @FXML
    private TableColumn<Company, String> statusColumn;
    @FXML
    private TableColumn<Company, String> actionColumn;
    
   
    
        /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            
            idColumn.setCellValueFactory(new PropertyValueFactory<Company, Integer>("id"));
            codeColumn.setCellValueFactory(new PropertyValueFactory<Company, String>("code"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<Company, String>("name"));
            phoneColumn.setCellValueFactory(new PropertyValueFactory<Company, String>("phone"));
            
            statusColumn.setCellValueFactory(new PropertyValueFactory<Company, String>("status"));
            statusColumn.setCellFactory(tc -> {
               TableCell cell =
                       new TableCell() {
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
            Callback<TableColumn<Company, String>, TableCell<Company, String>> cellFoctory = (TableColumn<Company, String> param) -> {
                // make cell containing buttons
                final TableCell<Company, String> cell = new TableCell<Company, String>() {
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
                                oldCompany = tableView.getSelectionModel().getSelectedItem();
                                isUpdated = true;
                                tfCode.setText(oldCompany.getCode());
                                tfName.setText(oldCompany.getName());
                                tfPhone.setText(oldCompany.getPhone());
                                if (oldCompany.getStatus() == 1) {
                                    rbActive.setSelected(true);
                                } else {
                                    rbInActive.setSelected(true);
                                }

                            });

                            deleteIcon.setOnMouseClicked((MouseEvent t) -> {
                                selectedIndex = tableView.getSelectionModel().getSelectedIndex();
                                oldCompany = tableView.getSelectionModel().getSelectedItem();

                                JFXDialogLayout content = new JFXDialogLayout();
                                content.setHeading(new Text("Confirmation Dailog"));
                                content.setBody(new Text("Are you sure to delete " + oldCompany.getCode() + "?"));
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
                                        companyRepository.delete(oldCompany);
                                        companies.remove(oldCompany);
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
             
             companies = FXCollections.observableArrayList(companyRepository.list());
             tableView.setItems(companies);
             filteredList = new FilteredList<>(companies, e -> true);
             
             setting = ISPHelper.getSettingCode(settingRepository, ISPConstants.COMPANY_CODE_FIELD, ISPConstants.COMPANY_PREFIX_FIELD, ISPConstants.COMPANY_PREFIX);
             prefix = ISPHelper.getSettingPrefix(settingRepository, ISPConstants.COMPANY_PREFIX_FIELD);
             tfCode.setText(prefix + setting.getOptionValue());
             
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
        oldCompany = null;
        tfCode.setText(prefix + setting.getOptionValue());
        tfName.setText("");
        tfPhone.setText("");
        rbActive.setSelected(true);
    }

    @FXML
    private void saveForm(MouseEvent event) {
        if (!tfName.validate()) {
            return;
        }
        
         RadioButton rbSelected
                = (RadioButton) tgPackageStatus.getSelectedToggle();
        Integer selectedStatus = (rbSelected != null && "Active".equals(rbSelected.getText())) ? 1 : 0;
        Company new_company = new Company(tfCode.getText(), tfName.getText(), selectedStatus, tfPhone.getText());
        if (isUpdated) {
            new_company.setId(oldCompany.getId());
            companyRepository.update(new_company);
            companies.set(selectedIndex, new_company);
            tableView.refresh();
            //packageTable.getItems().set(selectedIndex, new_package);

        } else {
            companyRepository.save(new_company);
            companies.add(0, new_company);
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
    
    
      private void filteredResult(){
        
    searchBox.textProperty().addListener((observableValue, oldValue, newValue) -> {
                    filteredList.setPredicate((Predicate<? super Company>) pkg -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lowerCaseFilter = newValue.toLowerCase();

                        if (pkg.getCode().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (pkg.getName().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }else if (pkg.getPhone().toString().contains(lowerCaseFilter)) {
                            return true;
                        } else if (ISPHelper.getHumanStatus(pkg.getStatus()).toLowerCase().startsWith(lowerCaseFilter)) {
                            return true;
                        }

                        return false;
                    });
                });
                SortedList<Company> sortedList = new SortedList<>(filteredList);
                sortedList.comparatorProperty().bind(tableView.comparatorProperty());
                tableView.setItems(sortedList);
    }
}