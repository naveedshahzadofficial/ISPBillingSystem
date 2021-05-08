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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ns.systems.ispbillingsystem.App;
import ns.systems.ispbillingsystem.models.Setting;
import ns.systems.ispbillingsystem.repositories.SettingRepository;
/**
 *
 * @author naveed
 */
public class ISPHelper {
    
     public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
     public static void setVBoxNode(VBox vBox,Node node){
         vBox.getChildren().clear();
         vBox.getChildren().add((Node) node);
     }
     public static void setHBoxNode(HBox hBox,Node node){
         hBox.getChildren().clear();
         hBox.getChildren().add((Node) node);
     }
     
      public static String setLikeType(String ColumnValue, String JoinType){
           String left_prefix = "",right_prefix="";
        
        if(JoinType!="left")
            if("right".equals(JoinType))
                right_prefix="%";
            else{
                left_prefix="%";
                right_prefix="%";
            }
        else left_prefix="%";
        
        return left_prefix+ColumnValue+right_prefix;
      }
      
      public static Setting getSettingCode(SettingRepository settingRepository,String codeField,String prefixField, String prefixValue){
      Setting setting = settingRepository.findByKey("optionName",codeField);
       if(setting==null){
           setting =  new Setting(codeField,"1");
           settingRepository.save(setting);
           settingRepository.save(new Setting(prefixField,prefixValue));
       }
       return setting;
      }
      
      public static String getSettingPrefix(SettingRepository settingRepository,String prefixField){
       String prefix = settingRepository.findByKey("optionName",prefixField).getOptionValue();
       return prefix;
      }
     
      public static String getHumanStatus(int status){
          return status==1?"Active":"In Active";
      }
     
     
}
