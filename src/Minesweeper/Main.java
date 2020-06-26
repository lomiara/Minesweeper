package Minesweeper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Scene scene;
        Parent root = loader.load();
        Controller controller = loader.getController();
        primaryStage.setTitle("Minesweeper");
        primaryStage.setOnCloseRequest(windowEvent -> {
            System.out.println(windowEvent.getEventType());
            System.exit(0);
        });
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        controller.setStage(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
