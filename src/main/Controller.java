package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class Controller {

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
            new DataRow()
    );

    @FXML
    public void initialize() {
        table.setEditable(true);
        table.setItems(data);

        xColumn.setCellValueFactory(new PropertyValueFactory<>("x"));
        yColumn.setCellValueFactory(new PropertyValueFactory<>("y"));
    }

    @FXML
    private void interpolateLinear() {

    }

    @FXML
    private void interpolateNewton() {

    }

    @FXML
    private void interpolateLagrange() {

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



}
