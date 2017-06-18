package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Controller {

    public static Controller instance;

    @FXML
    private TextField functionField;
    @FXML
    private TextField xField;
    @FXML
    private TextField yField;
    @FXML
    private TextArea textFlow;
    @FXML
    private TextField xCalculateField;

    @FXML
    private TableView<DataRow> table;
    @FXML
    private TableColumn<DataRow, String> xColumn;
    @FXML
    private TableColumn<DataRow, String> yColumn;

    private final ObservableList<DataRow> data = FXCollections.observableArrayList(
            new DataRow(0.68, 0.80866),
            new DataRow(0.73, 0.89492),
            new DataRow(0.8, 1.02964),
            new DataRow(0.88, 1.20966),
            new DataRow(0.93, 1.34087),
            new DataRow(0.99, 1.52368)
    );

    @FXML
    public void initialize() {
        table.setEditable(true);
        table.setItems(data);

        xColumn.setCellValueFactory(new PropertyValueFactory<>("x"));
        yColumn.setCellValueFactory(new PropertyValueFactory<>("y"));

        instance = this;
    }

    @FXML
    private void interpolateLinear() {
        if (data.size() > 1)
            showGraph(Interpolator.I_LINEAR);
    }

    @FXML
    private void interpolateNewton() {
        if (data.size() > 1)
            showGraph(Interpolator.I_NEWTON);
    }

    @FXML
    private void interpolateLagrange() {
        if (data.size() > 1)
            showGraph(Interpolator.I_LAGRANGE);
    }

    @FXML
    private void interpolateSpline() {
        if (data.size() > 1)
            showGraph(Interpolator.I_SPLINE);
    }

    @FXML
    private void addData(){
        try {
            String xText = xField.getText();
            String yText = yField.getText();

            double x = Double.parseDouble(xText);
            double y = Double.parseDouble(yText);

            DataRow newRow = new DataRow(x, y);

            data.add(newRow);
        }
        catch(Exception e) {
            System.out.println("Error input.");
        }
    }

    @FXML
    private void deleteSelected() {
        int indexToDelete = table.getFocusModel().getFocusedIndex();
        if (indexToDelete != -1) {
            data.remove(indexToDelete);
        }
    }

    @FXML
    private void deleteAll() {
        data.clear();
    }

    public int getAmountOfPoints() {
        return data.size();
    }


    private void showGraph(int type) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("graphic.fxml"));
            Parent root = loader.load();
            Graphic controller = loader.getController();
            Stage stage = new Stage(StageStyle.UTILITY);
            stage.setTitle("Lex Interpolator :: " + Interpolator.getNameByType(type));
            stage.setScene(new Scene(root));
            controller.setData(type, data,
                    functionField.getText().trim().length() == 0 ? null : new Function(functionField.getText().trim()));
            stage.showAndWait();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void calculateX() {
        textFlow.setText(Interpolator.getAddByX(Double.parseDouble(xCalculateField.getText())));
    }

    public ObservableList<DataRow> getData() {
        return data;
    }
}
