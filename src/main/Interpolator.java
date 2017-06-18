package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunctionLagrangeForm;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunctionNewtonForm;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lex on 6/18/2017.
 */
public class Interpolator {

    public static final int POINTS = 50;

    public static final int I_LINEAR = 0;
    public static final int I_NEWTON = 1;
    public static final int I_LAGRANGE = 2;
    public static final int I_SPLINE = 3;

    public static ObservableList<XYChart.Data> interpolate(int type, ObservableList<DataRow> data) {

        switch(type) {
            case I_LINEAR:
                return linearInterpolation(data);
            case I_NEWTON:
                return newtonInterpolation(data);
            case I_LAGRANGE:
                return lagrangeInterpolation(data);
            case I_SPLINE:
                return splineInterpolation(data);
            default: return null;
        }


    }

    private static ObservableList<XYChart.Data> linearInterpolation(ObservableList<DataRow> data) {
        ObservableList<XYChart.Data> result = FXCollections.observableArrayList();
        for(DataRow item : data)
            result.add(new XYChart.Data(item.getX(), item.getY()));

        return result;
    }

    private static ObservableList<XYChart.Data> splineInterpolation(ObservableList<DataRow> data) {
        ObservableList<XYChart.Data> result = FXCollections.observableArrayList();
        List<Double> xs = new ArrayList<Double>(), ys = new ArrayList<Double>();
        for(DataRow item : data) {
            xs.add(item.getX());
            ys.add(item.getY());
        }
        // calculate diffs
        SplineInterpolator interpolator = SplineInterpolator.createMonotoneCubicSpline(xs, ys);
        double start = data.get(0).getX(), end = data.get(data.size() - 1).getX();
        double perscise = (end - start) / POINTS; perscise = Math.round(perscise * 1000000.0) / 1000000.0;
        end += perscise;
        for(; start <= end; start += perscise)
            result.add(new XYChart.Data(start, interpolator.interpolate(start)));
        return result;
    }

    private static String lastFormula = null;

    public static String getLastFormula() {
        return lastFormula;
    }

    private static ObservableList<XYChart.Data> newtonInterpolation(ObservableList<DataRow> data) {
        ObservableList<XYChart.Data> result = FXCollections.observableArrayList();

        double[] x = new double[data.size()], y = new double[data.size()];
        DataRow row;
        for(int i = 0; i < data.size(); i++) {
            row = data.get(i);
            x[i] = row.getX();
            y[i] = row.getY();
        }
        PolynomialFunctionLagrangeForm.verifyInterpolationArray(x, y, false);
        final double[] c = new double[x.length-1];
        System.arraycopy(x, 0, c, 0, c.length);

        final double[] a = computeDividedDifference(x, y);
        PolynomialFunctionNewtonForm form = new PolynomialFunctionNewtonForm(a, c);

        double start = data.get(0).getX(), end = data.get(data.size() - 1).getX();
        double perscise = (end - start) / POINTS; perscise = Math.round(perscise * 1000000.0) / 1000000.0;
        end += perscise;
        for(; start <= end; start += perscise)
            result.add(new XYChart.Data(start, form.value(start)));
        return result;
    }

    private static double[] computeDividedDifference(final double x[], final double y[]) {

        PolynomialFunctionLagrangeForm.verifyInterpolationArray(x, y, true);

        final double[] divdiff = y.clone(); // initialization

        final int n = x.length;
        final double[] a = new double [n];
        a[0] = divdiff[0];
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < n-i; j++) {
                double denominator = x[j+i] - x[j];
                if (denominator == 0.0) {
                    denominator = 1.0;
                }
                divdiff[j] = (divdiff[j+1] - divdiff[j]) / denominator;
            }
            a[i] = divdiff[0];
        }
        System.out.println(a);
        return a;
    }

    private static ObservableList<XYChart.Data> lagrangeInterpolation(ObservableList<DataRow> data) {
        ObservableList<XYChart.Data> result = FXCollections.observableArrayList();

        double[] x = new double[data.size()], y = new double[data.size()];
        DataRow row;
        for(int i = 0; i < data.size(); i++) {
            row = data.get(i);
            x[i] = row.getX();
            y[i] = row.getY();
        }
        PolynomialFunctionLagrangeForm.verifyInterpolationArray(x, y, false);

        PolynomialFunctionLagrangeForm form = new PolynomialFunctionLagrangeForm(x, y);

        double start = data.get(0).getX(), end = data.get(data.size() - 1).getX();
        double perscise = (end - start) / POINTS; perscise = Math.round(perscise * 1000000.0) / 1000000.0;
        end += perscise;
        for(; start <= end; start += perscise)
            result.add(new XYChart.Data(start, form.value(start)));
        return result;
    }
}
