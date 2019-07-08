package mainView.tools;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.*;
import java.time.LocalDate;
import java.util.Iterator;

public class JsonHandler {
    public static void save(ObservableList<Node> elements) throws IOException {
        JsonObject json = new JsonObject();
        JsonArray array = new JsonArray();

        elements.forEach(node -> {
            JsonObject jsonElement = new JsonObject();
            VBox element = (VBox) node;

            Iterator<Node> iterator = element.getChildren().iterator();
            Node line = iterator.next();
            jsonElement.addProperty("title", ((TextField)line.lookup(".text-field")).getText());

            line = iterator.next();
            jsonElement.addProperty("currentValueLabel", ((TextField)line.lookup(".text-field")).getText());
            jsonElement.addProperty("currentValue", (int) ((Spinner)line.lookup(".spinner")).getValue());

            line = iterator.next();
            jsonElement.addProperty("targetValueLabel", ((TextField)line.lookup(".text-field")).getText());
            jsonElement.addProperty("targetValue", (int) ((Spinner)line.lookup(".spinner")).getValue());

            line = iterator.next();
            jsonElement.addProperty("targetDateLabel", ((TextField)line.lookup(".text-field")).getText());
            LocalDate date = ((DatePicker)line.lookup(".date-picker")).getValue();
            JsonObject jsonDate = new JsonObject();
            jsonDate.addProperty("year", date.getYear());
            jsonDate.addProperty("month", date.getMonthValue());
            jsonDate.addProperty("day", date.getDayOfMonth());
            jsonElement.add("targetDate", jsonDate);

            line = iterator.next();
            jsonElement.addProperty("thingName", ((TextField)line.lookup(".text-field")).getText());

            array.add(jsonElement);
        });
        json.add("elements", array);

        BufferedWriter writer = new BufferedWriter(new FileWriter("data.json"));
        writer.write(json.toString());
        writer.close();
    }

    public static void load(ObservableList<Node> elements) throws FileNotFoundException {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(new FileReader("data.json"));
        JsonObject json = jsonElement.getAsJsonObject();
        JsonArray array = json.get("elements").getAsJsonArray();

        array.forEach(el -> {
            elements.add(Layout.getNewElement(el.getAsJsonObject()));
        });
    }
}
