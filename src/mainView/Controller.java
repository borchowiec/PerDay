package mainView;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public Button addButton;
    public VBox elementsContainer;
    public BorderPane mainContainer;

    @Override   //19 min, min size of window
    public void initialize(URL location, ResourceBundle resources) {
        addButton.minWidthProperty().bind( mainContainer.widthProperty() );
        elementsContainer.minWidthProperty().bind( mainContainer.widthProperty() );
    }

    public void addNewElement() {
        System.out.println("add");
    }

    public void refresh() {
        System.out.println("refresh");
    }
}
