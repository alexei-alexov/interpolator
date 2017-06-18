package main;

import com.sun.istack.internal.Nullable;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.util.ArrayList;

/**
 * Created by lex on 6/18/2017.
 */
public class Graphic {

    @FXML
    private Label titleLabel;
    @FXML
    private LineChart<Number, Number> chart;

    private XYChart.Series interpolated;
    private XYChart.Series control;

    @FXML
    public void initialize() {



    }


    public void setData(int type, ObservableList<DataRow> data, @Nullable Function f) {
        ArrayList<DataRow> interpolatedData = Interpolator.interpolate(type, data);
        ArrayList<DataRow> controlData = f.getData(double start, double end);

    }




}
