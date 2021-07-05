package ns.systems.ispbillingsystem;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;
import ns.systems.ispbillingsystem.helpers.ISPHelper;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 * JavaFX App
 */
public class App extends Application {

    private static final Logger logger = Logger.getLogger(App.class.getName());
    
    private static Scene scene;
    private double gapX = 0, gapY = 0;

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = ISPHelper.loadFXML("fxmls/login");
        PropertyConfigurator.configure(App.class.getResource("cfgs/log4j.properties"));
        logger.info("Start Application");
        scene = new Scene(root, 700, 500);
        stage.setResizable(false);
        stage.initStyle(StageStyle.DECORATED.UNDECORATED);
        root.setOnMouseDragged(e -> this.dragStage(e, stage));
        root.setOnMouseMoved(e -> this.calculateGap(e, stage));
        stage.setScene(scene);
        stage.show();
    }
    
  

    private void calculateGap(MouseEvent event, Stage stage) {
        gapX = event.getScreenX() - stage.getX();
        gapY = event.getScreenY() - stage.getY();
    }

    private void dragStage(MouseEvent event, Stage stage) {
        stage.setX(event.getScreenX() - gapX);
        stage.setY(event.getScreenY() - gapY);
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(ISPHelper.loadFXML(fxml));
    }

   

    public static void main(String[] args) {
        launch();
    }

}