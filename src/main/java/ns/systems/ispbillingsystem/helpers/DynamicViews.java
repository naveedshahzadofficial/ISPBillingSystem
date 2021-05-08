/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ns.systems.ispbillingsystem.helpers;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.apache.log4j.Logger;

/**
 *
 * @author naveed
 */
public class DynamicViews {
    public DynamicViews(){
        
    }

    /**
     *
     * @param borderPane
     * @param resource
     */
    public static void loadBorderCenter(BorderPane borderPane,String resource){
    
        try {
        Parent dashboard = FXMLLoader.load(new DynamicViews().getClass().getResource("/ns/systems/ispbillingsystem/fxmls/"+resource+".fxml"));
            
        borderPane.setCenter(dashboard );
        } catch (IOException ex) {
             Logger.getLogger(DynamicViews.class.getName()).error("Some thing is wrong", ex);
        }
    }
    
    public static void loadHBox(HBox hbox,String resource){
        try {
          //  Parent node  = ISPHelper.loadFXML("fxmls/dashboard");
         Parent node  = FXMLLoader.load(new DynamicViews().getClass().getResource("/ns/systems/ispbillingsystem/fxmls/"+resource+".fxml"));
         hbox.getChildren().clear();
         hbox.getChildren().add((Node) node);     
        } catch (IOException ex) {
             Logger.getLogger(DynamicViews.class.getName()).error("Some thing is wrong", ex);
        }
    }
}
