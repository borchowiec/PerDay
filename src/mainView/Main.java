package mainView;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainView.fxml"));
        root.getStylesheets().add("mainView/style.css");
        primaryStage.setTitle("Per Day");
        primaryStage.setScene(new Scene(root, 300, 600));
        primaryStage.setMinWidth(320);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
