package mainView;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import mainView.tools.Layout;

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
    }

    public void addNewElement() {
        VBox el = Layout.getNewElement();
        elementsContainer.getChildren().add(el);
    }

    public void refresh() {
        System.out.println("refresh");
    }
}
