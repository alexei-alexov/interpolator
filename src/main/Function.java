package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Created by lex on 6/18/2017.
 */
public class Function {

    private static final int POINTS = 50;

    private final Expression expr;

    private String expression;
    private double perscise;

    public Function(String expression) {
        this.expression = expression;
        this.expr = new ExpressionBuilder(expression).variables("x").build();;

    }

    public ObservableList<XYChart.Data> getData(double start, double end) {
        perscise = (end - start) / POINTS; perscise = Math.round(perscise * 1000000.0) / 1000000.0;
        end += perscise;
        ObservableList<XYChart.Data> result = FXCollections.observableArrayList();
        for(; start <= end; start += perscise)
            result.add(new XYChart.Data(start, f(start)));
        return result;
    }

    public double f(double x) {
        expr.setVariable("x", x);
        return expr.evaluate();
    }

}
