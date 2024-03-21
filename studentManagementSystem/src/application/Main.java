package application;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        StudentManagementSystem adminInterface = new StudentManagementSystem();
        adminInterface.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
