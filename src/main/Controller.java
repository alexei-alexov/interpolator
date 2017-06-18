package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
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
    private TableView<DataRow> table;
    @FXML
    private TableColumn<DataRow, String> xColumn;
    @FXML
    private TableColumn<DataRow, String> yColumn;

    private final ObservableList<DataRow> data = FXCollections.observableArrayList(
            new DataRow(),
            new DataRow(1, 1),
            new DataRow(3, 6),
            new DataRow(15, 5)
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
            stage.setTitle("Lex Interpolator");
            stage.setScene(new Scene(root));
            controller.setData(type, data,
                    functionField.getText().trim().length() == 0 ? null : new Function(functionField.getText().trim()));
            stage.showAndWait();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
