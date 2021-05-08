/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ns.systems.ispbillingsystem.helpers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

/**
 *
 * @author naveed
 */
public class ISPValidation {
    
      public static void textFieldRequired(RequiredFieldValidator validator, JFXTextField tfField){
           tfField.getValidators().add(validator);
       validator.setMessage("This field is required.");
       tfField.focusedProperty().addListener((var ov, var oldValue, var newValue) -> {
            if(!newValue){
                tfField.validate();
            }
       });
      }
   
      public static void comboBoxRequired(RequiredFieldValidator validator, JFXTextField tfField){
           tfField.getValidators().add(validator);
       validator.setMessage("This field is required.");
       tfField.focusedProperty().addListener((var ov, var oldValue, var newValue) -> {
            if(!newValue){
                tfField.validate();
            }
       });
      }
    
}
