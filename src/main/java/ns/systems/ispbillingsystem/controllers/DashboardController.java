/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ns.systems.ispbillingsystem.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
/**
 *
 * @author naveed
 */
public class DashboardController implements Initializable {

   
    @FXML
    private BarChart<?, ?> barChartSales;
    @FXML
    private PieChart pieChartSales;
    @FXML
    private PieChart pieChartExpenses;
    @FXML
    private NumberAxis numberAxisSales;
    @FXML
    private CategoryAxis categoryAxisSales;
    @FXML
    private BarChart<?, ?> barChartExpense;
    @FXML
    private NumberAxis numberAxisExpense;
    @FXML
    private CategoryAxis categoryAxisExpense;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
           pieChartSales.setTitle("Minimum Sales");
           ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("1 MB", 13),
                new PieChart.Data("2 MB", 25),
                new PieChart.Data("3 MB", 10),
                new PieChart.Data("4 MB", 22),
                new PieChart.Data("6 MB", 30),
                new PieChart.Data("8 MB", 30));
                pieChartSales.setData(pieChartData);
                
                barChartSales.setTitle("Maximum Sales");
                
                categoryAxisSales.setLabel("Packages");

                numberAxisSales.setLabel("Sales");

                XYChart.Series dataSeries1 = new XYChart.Series();
                dataSeries1.setName("Jan-2021");

        dataSeries1.getData().add(new XYChart.Data("1 MB", 100));
        dataSeries1.getData().add(new XYChart.Data("2 MB"  , 65));
        dataSeries1.getData().add(new XYChart.Data("4 MB"  , 23));
        
        XYChart.Series dataSeries2 = new XYChart.Series();
                dataSeries2.setName("Feb-2021");

        dataSeries2.getData().add(new XYChart.Data("1 MB", 25));
        dataSeries2.getData().add(new XYChart.Data("2 MB"  , 150));
        dataSeries2.getData().add(new XYChart.Data("4 MB"  , 10));
        

        barChartSales.getData().addAll(dataSeries1,dataSeries2);




                
                /* pieChartData.forEach(data
                -> data.nameProperty().bind(Bindings.concat(data.getName(), " ", data.pieValueProperty(), " Tons"
                )
                )
                );*/
                
                pieChartExpenses.setTitle("Total Amount Head Wise");
                ObservableList<PieChart.Data> pieChartHeadWiseData = FXCollections.observableArrayList(
                new PieChart.Data("Receiveable", 30000),
                new PieChart.Data("Sales", 40000));
                pieChartExpenses.setData(pieChartHeadWiseData);
                
                barChartExpense.setTitle("Expense Category Wise");
                categoryAxisExpense.setLabel("Items");
                numberAxisExpense.setLabel("Amount");
                 XYChart.Series dataSeries3 = new XYChart.Series();
                dataSeries1.setName("Jan-2021");

                dataSeries3.getData().add(new XYChart.Data("TEA", 100));
                dataSeries3.getData().add(new XYChart.Data("Neam Salary"  , 200));
                dataSeries3.getData().add(new XYChart.Data("Wire Jecket"  , 140));
                barChartExpense.getData().addAll(dataSeries3);

    }
    
    
    
}
