package main;

import com.sun.istack.internal.Nullable;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Created by lex on 6/18/2017.
 */
public class Graphic {

    @FXML
    private Label titleLabel;
    @FXML
    private LineChart<Number, Number> chart;
    @FXML
    private CheckBox showControl;
    @FXML
    private Button closeButton;


    private XYChart.Series interpolated;
    private XYChart.Series control;

    @FXML
    public void initialize() {

        final double SCALE_DELTA = 1.1;
        chart.setOnScroll(new EventHandler<ScrollEvent>() {
            public void handle(ScrollEvent event) {
                event.consume();

                if (event.getDeltaY() == 0) {
                    return;
                }

                double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;
                chart.resizeRelocate(1, 2, 300, 300);
                chart.setScaleX(chart.getScaleX() * scaleFactor);
                chart.setScaleY(chart.getScaleY() * scaleFactor);
            }
        });

        chart.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    chart.setScaleX(1.0);
                    chart.setScaleY(1.0);
                }
            }
        });

    }


    public void setData(int type, ObservableList<DataRow> data, @Nullable Function f) {
        titleLabel.setText(Interpolator.getNameByType(type));
        ObservableList<XYChart.Data> interpolatedData = Interpolator.interpolate(type);
        interpolated = new XYChart.Series();

        interpolated.getData().addAll(interpolatedData);
        chart.getData().add(interpolated);

        if (f != null) {
            ObservableList<XYChart.Data> controlData = f.getData(data.get(0).getX(), data.get(data.size()-1).getX());
            control = new XYChart.Series();
            control.getData().addAll(controlData);

            showControl.setDisable(false);
            showControl.setSelected(true);
            changeSelected();
        }
        else{
            showControl.setDisable(true);
        }
    }

    @FXML
    private void changeSelected() {
        if (showControl.isSelected()) {
            chart.getData().add(control);
        }
        else {
            chart.getData().remove(1);
        }
    }

    @FXML
    private void close() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }


}
