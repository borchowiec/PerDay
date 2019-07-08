package mainView.tools;

import com.google.gson.JsonObject;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Layout {
    private static VBox getMainBox() {
        VBox vBox = new VBox(10);
        vBox.getStyleClass().add("element");
        return vBox;
    }

    private static HBox getTopBar(String title, VBox mainBox, ObservableList<Node> elements) {
        TextField titleInput = new TextField(title);
        titleInput.getStyleClass().add("titleInput");

        Button removeButton = new Button("x");
        removeButton.getStyleClass().add("removeButton");
        removeButton.setOnAction(event -> {
            elements.remove(mainBox);
        });

        HBox topBox = new HBox();
        topBox.getStyleClass().add("topElementsBar");
        topBox.getChildren().addAll(titleInput, removeButton);

        return topBox;
    }

    private static HBox getCounterBox(String label, int value, String classOfSpinner) {
        TextField input = new TextField(label);
        Spinner<Integer> spinner = new Spinner<>();
        setUpSpinner(spinner);
        spinner.getValueFactory().setValue(value);
        spinner.getStyleClass().add(classOfSpinner);

        HBox box = new HBox();
        box.getStyleClass().add("counterBox");
        box.getChildren().addAll(input, spinner);

        return box;
    }

    private static HBox getTargetDateBox(String label, LocalDate date) {
        TextField targetDateInput = new TextField(label);
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
        if (date.isBefore(LocalDate.now()))
            targetDatePicker.setValue(LocalDate.now());
        else
            targetDatePicker.setValue(date);
        targetDatePicker.setEditable(false);

        HBox targetDateBox = new HBox();
        targetDateBox.getStyleClass().add("counterBox");
        targetDateBox.getChildren().addAll(targetDateInput, targetDatePicker);

        return targetDateBox;
    }

    public static HBox getResultBox(String thingName) {
        Label perDayLabel = new Label("0 ");
        perDayLabel.setMinWidth(100);
        perDayLabel.setAlignment(Pos.CENTER_RIGHT);
        perDayLabel.getStyleClass().add("perDayLabel");

        TextField thingNameInput = new TextField(thingName);
        thingNameInput.setAlignment(Pos.CENTER_RIGHT);

        Label daysLabel = new Label("/ Day");
        daysLabel.setMinWidth(100);

        HBox resultBox = new HBox();
        resultBox.getStyleClass().add("resultBox");
        resultBox.setAlignment(Pos.CENTER);
        resultBox.getChildren().addAll(perDayLabel, thingNameInput, daysLabel);

        return resultBox;
    }

    public static VBox getNewElement(ObservableList<Node> elements) {
        VBox vBox = getMainBox();

        vBox.getChildren().addAll(
                getTopBar("Untitled", vBox, elements),
                getCounterBox("Current value", 0, "currentSpinner"),
                getCounterBox("Target value", 0, "targetSpinner"),
                getTargetDateBox("Target date", LocalDate.now()),
                getResultBox("Things")
        );
        setUpCounting(vBox);
        return vBox;
    }

    public static VBox getNewElement(JsonObject jsonData, ObservableList<Node> elements) {
        VBox vBox = getMainBox();

        JsonObject jsonDate = jsonData.get("targetDate").getAsJsonObject();
        LocalDate date = LocalDate.of(jsonDate.get("year").getAsInt(),
                jsonDate.get("month").getAsInt(),
                jsonDate.get("day").getAsInt());

        vBox.getChildren().addAll(
                getTopBar(jsonData.get("title").getAsString(), vBox, elements),
                getCounterBox(jsonData.get("currentValueLabel").getAsString(), jsonData.get("currentValue").getAsInt(), "currentSpinner"),
                getCounterBox(jsonData.get("targetValueLabel").getAsString(), jsonData.get("targetValue").getAsInt(), "targetSpinner"),
                getTargetDateBox(jsonData.get("targetDateLabel").getAsString(), date),
                getResultBox(jsonData.get("thingName").getAsString())
        );
        setUpCounting(vBox);
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

    private static void setUpCounting(VBox elementsBox) {
        Spinner<Integer> currentSpinner = (Spinner<Integer>) elementsBox.lookup(".currentSpinner");
        Spinner<Integer> targetSpinner = (Spinner<Integer>) elementsBox.lookup(".targetSpinner");
        DatePicker targetDatePicker = (DatePicker) elementsBox.lookup(".date-picker");
        Label perDayLabel = (Label) elementsBox.lookup(".perDayLabel");

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

        perDayLabel.setText(countPerDay(currentSpinner.getValue(), targetSpinner.getValue(), LocalDate.now(), targetDatePicker.getValue())+"");
    }

    private static double countPerDay(int currentValue, int targetValue, LocalDate currentDate, LocalDate targetDate) {
        double result = targetValue - currentValue;
        long days = ChronoUnit.DAYS.between(currentDate, targetDate) + 1;
        return Math.round(result / days * 100.0) / 100.0;
    }
}
