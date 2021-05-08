/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ns.systems.ispbillingsystem.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ns.systems.ispbillingsystem.helpers.ISPHelper;

/**
 *
 * @author naveed
 */
public class LoginController  implements Initializable {

    @FXML
    private Button btn_login;
    @FXML
    private TextField et_username;
    @FXML
    private PasswordField et_password;
    @FXML
    private Hyperlink hl_forgot_password;
    @FXML
    private Button btn_close;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btn_close.setOnAction(e->{ System.exit(0); });
        btn_login.setOnAction((ActionEvent e)->{
            
            try {
                Scene scene = new Scene(ISPHelper.loadFXML("fxmls/_layout"));
                Stage loginStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                Stage stage = new Stage();
                stage.setResizable(true);
                stage.initStyle(StageStyle.DECORATED);
                stage.setTitle("Dashboard - Welcome, Admin");
                stage.setScene(scene);
                stage.show();
                loginStage.hide();
                        
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    
   
    
    
}
