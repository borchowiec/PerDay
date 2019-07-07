package mainView.tools;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Layout {
    public static VBox getNewElement() {
        VBox vBox =  new VBox(10);
        vBox.getStyleClass().add("element");

        //topBox
        TextField titleInput = new TextField("Untitled");
        titleInput.getStyleClass().add("titleInput");

        Button removeButton = new Button("x");
        removeButton.getStyleClass().add("removeButton");

        HBox topBox = new HBox();
        topBox.getStyleClass().add("topElementsBar");
        topBox.getChildren().addAll(titleInput, removeButton);

        //currentBox
        TextField currentInput = new TextField("Current value");
        Spinner<Integer> currentSpinner = new Spinner<>();
        setUpSpinner(currentSpinner);

        HBox currentBox = new HBox();
        currentBox.getStyleClass().add("counterBox");
        currentBox.getChildren().addAll(currentInput, currentSpinner);

        //targetBox
        TextField targetInput = new TextField("Target value");
        Spinner<Integer> targetSpinner = new Spinner<>();
        setUpSpinner(targetSpinner);

        HBox targetBox = new HBox();
        targetBox.getStyleClass().add("counterBox");
        targetBox.getChildren().addAll(targetInput, targetSpinner);

        //targetDateBox
        TextField targetDateInput = new TextField("Target date");
        DatePicker targetDatePicker = new DatePicker();
        Callback<DatePicker, DateCell> dayCellFactory = (final DatePicker datePicker) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        };
        targetDatePicker.setDayCellFactory(dayCellFactory);
        targetDatePicker.setValue(LocalDate.now());
        targetDatePicker.setEditable(false);

        HBox targetDateBox = new HBox();
        targetDateBox.getStyleClass().add("counterBox");
        targetDateBox.getChildren().addAll(targetDateInput, targetDatePicker);

        //resultBox
        Label perDayLabel = new Label("0 ");
        perDayLabel.setMinWidth(100);
        perDayLabel.setAlignment(Pos.CENTER_RIGHT);

        TextField thingNameInput = new TextField("Things");
        thingNameInput.setAlignment(Pos.CENTER_RIGHT);

        Label daysLabel = new Label("/ Day");
        daysLabel.setMinWidth(100);

        HBox resultBox = new HBox();
        resultBox.getStyleClass().add("resultBox");
        resultBox.setAlignment(Pos.CENTER);
        resultBox.getChildren().addAll(perDayLabel, thingNameInput, daysLabel);

        currentSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue > targetSpinner.getValue())
                targetSpinner.getValueFactory().setValue(newValue);
            perDayLabel.setText(countPerDay(currentSpinner.getValue(), targetSpinner.getValue(), LocalDate.now(), targetDatePicker.getValue())+"");
        });

        targetSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue < currentSpinner.getValue())
                currentSpinner.getValueFactory().setValue(newValue);
            perDayLabel.setText(countPerDay(currentSpinner.getValue(), targetSpinner.getValue(), LocalDate.now(), targetDatePicker.getValue())+"");
        });

        targetDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            perDayLabel.setText(countPerDay(currentSpinner.getValue(), targetSpinner.getValue(), LocalDate.now(), targetDatePicker.getValue())+"");
        });

        vBox.getChildren().addAll(topBox, currentBox, targetBox, targetDateBox, resultBox);
        return vBox;
    }

    private static void setUpSpinner(Spinner spinner) {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0));
        spinner.setEditable(true);
        spinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                spinner.getEditor().setText(newValue.replaceAll("[^\\d]", ""));
            if (newValue.length() == 0)
                spinner.getEditor().setText("1");
        });
        spinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                spinner.increment(0);
            }
        });
    }

    private static double countPerDay(int currentValue, int targetValue, LocalDate currentDate, LocalDate targetDate) {
        double result = targetValue - currentValue;
        long days = ChronoUnit.DAYS.between(currentDate, targetDate) + 1;
        return Math.round(result / days * 100.0) / 100.0;
    }
}
