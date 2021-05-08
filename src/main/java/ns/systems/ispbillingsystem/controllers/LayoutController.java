/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ns.systems.ispbillingsystem.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ns.systems.ispbillingsystem.helpers.DynamicViews;
import ns.systems.ispbillingsystem.helpers.ISPHelper;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
/**
 *
 * @author naveed
 */
public class LayoutController implements Initializable {

    @FXML
    private HBox content_layout;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DynamicViews.loadHBox(content_layout, "dashboard");
    }
    
    
     public void setNode(Node node) {
        content_layout.getChildren().clear();
        content_layout.getChildren().add((Node) node);
    }

    @FXML
    private void showPackages(MouseEvent event) {
        DynamicViews.loadHBox(content_layout, "packages");
    }

    @FXML
    private void showDashboard(MouseEvent event) {
        DynamicViews.loadHBox(content_layout, "dashboard");
    }

    @FXML
    private void showCompanies(MouseEvent event) {
        DynamicViews.loadHBox(content_layout, "companies");
    }

    @FXML
    private void showCustomers(MouseEvent event) {
        DynamicViews.loadHBox(content_layout, "customers");
    }
   
}
