package mainView;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import mainView.tools.JsonHandler;
import mainView.tools.Layout;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public Button addButton;
    public VBox elementsContainer;
    public BorderPane mainContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addButton.minWidthProperty().bind(mainContainer.widthProperty());
        elementsContainer.minWidthProperty().bind(mainContainer.widthProperty());
        try {
            JsonHandler.load(elementsContainer.getChildren());
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        }
    }

    public void addNewElement() {
        VBox el = Layout.getNewElement(elementsContainer.getChildren());
        elementsContainer.getChildren().add(el);
    }

    public void refresh() {
        System.out.println("refresh");
    }

    public void save() {
        try {
            JsonHandler.save(elementsContainer.getChildren());
            System.out.println("zapisano");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
