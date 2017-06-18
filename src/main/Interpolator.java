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

    private static List<Double> dataX = new ArrayList<>();
    private static List<Double> dataY = new ArrayList<>();

    private static double[] dataArrayX;
    private static double[] dataArrayY;
    private static double[] dataArrayA;
    private static double[] dataArrayC;
    private static double perscise;

    private static final String[] NAMES = {
            "Лінійна інтерполяція",
            "Інтерполяція Ньютона",
            "Інтерполяція Лагранжа",
            "Сплайнова інтерполяція"
    };

    private static boolean dataChanged = true;

    public static void notifyChange(){
        dataChanged = true;
    }

    private static void recalculateData() {
        ObservableList<DataRow> data = Controller.instance.getData();
        dataArrayX = new double[data.size()];
        dataArrayY = new double[data.size()];
        DataRow item;
        for(int i = 0; i < data.size(); i++) {
            item = data.get(i);
            dataX.add(item.getX());
            dataY.add(item.getY());
            dataArrayX[i] = item.getX();
            dataArrayY[i] = item.getY();

        }

        dataArrayC = new double[dataArrayX.length-1];
        System.arraycopy(dataArrayX, 0, dataArrayC, 0, dataArrayC.length);

        dataArrayA = computeDividedDifference(dataArrayX, dataArrayY);

        double start = dataArrayX[0], end = dataArrayX[dataArrayX.length - 1];
        perscise = (end - start) / POINTS; perscise = Math.round(perscise * 1000000.0) / 1000000.0;

    }

    private static void checkData() {
        if (dataChanged){
            recalculateData();
            dataChanged = false;
        }
    }

    public static ObservableList<XYChart.Data> interpolate(int type) {
        checkData();
        switch(type) {
            case I_LINEAR:
                return linearInterpolation();
            case I_NEWTON:
                return newtonInterpolation();
            case I_LAGRANGE:
                return lagrangeInterpolation();
            case I_SPLINE:
                return splineInterpolation();
            default: return null;
        }
    }

    private static ObservableList<XYChart.Data> linearInterpolation() {

        ObservableList<XYChart.Data> result = FXCollections.observableArrayList();
        for(DataRow item : Controller.instance.getData())
            result.add(new XYChart.Data(item.getX(), item.getY()));

        return result;
    }

    private static ObservableList<XYChart.Data> splineInterpolation() {
        ObservableList<XYChart.Data> result = FXCollections.observableArrayList();

        // calculate diffs
        SplineInterpolator interpolator = SplineInterpolator.createMonotoneCubicSpline(dataX, dataY);
        double start = dataX.get(0), end = dataX.get(dataX.size() - 1);
        end += perscise;
        for(; start <= end; start += perscise)
            result.add(new XYChart.Data(start, interpolator.interpolate(start)));
        return result;
    }

    private static ObservableList<XYChart.Data> newtonInterpolation() {
        ObservableList<XYChart.Data> result = FXCollections.observableArrayList();

        PolynomialFunctionLagrangeForm.verifyInterpolationArray(dataArrayX, dataArrayY, false);

        PolynomialFunctionNewtonForm form = new PolynomialFunctionNewtonForm(dataArrayA, dataArrayC);

        double start = dataArrayX[0], end = dataArrayX[dataArrayX.length - 1];
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

    private static ObservableList<XYChart.Data> lagrangeInterpolation() {
        ObservableList<XYChart.Data> result = FXCollections.observableArrayList();

        PolynomialFunctionLagrangeForm.verifyInterpolationArray(dataArrayX, dataArrayY, false);

        PolynomialFunctionLagrangeForm form = new PolynomialFunctionLagrangeForm(dataArrayX, dataArrayY);

        double start = dataArrayX[0], end = dataArrayX[dataArrayX.length - 1];
        end += perscise;
        for(; start <= end; start += perscise)
            result.add(new XYChart.Data(start, form.value(start)));
        return result;
    }

    public static String getNameByType(int type) {
        return NAMES[type];
    }

    /**
     * Find all interpolations of Y in given X.
     *
     * @param x - argument to find y from
     * @return string result of all interpolations
     */
    public static String getAddByX(double x) {
        checkData();
        final String pattern = "%s: %f\n";
        StringBuilder result = new StringBuilder("РЕЗУЛЬТАТИ:\n");
        // linear
        double x0 = -1, y0 = 0, x1 = 0, y1 = 0;
        int i = -1;
        for (i = 0; i < dataArrayX.length - 1; i++) {
            if (x > dataArrayX[i]){
                x0 = dataArrayX[i];
                x1 = dataArrayX[i + 1];
                y0 = dataArrayY[i];
                y1 = dataArrayY[i + 1];
                break;
            }
        }
        if ((dataArrayX.length - 1) == i)
            return "Помилка даних";

        double a = (y1 - y0) / (x1 - x0);
        double b = y0 - x0 * a;
        double linearResult = Math.round((x * a + b) * 1000000.0) / 1000000.0;
        result.append(String.format(pattern, getNameByType(I_LINEAR), linearResult));

        // Newton
        PolynomialFunctionNewtonForm formNewton = new PolynomialFunctionNewtonForm(dataArrayA, dataArrayC);
        result.append(String.format(pattern, getNameByType(I_NEWTON), formNewton.value(x)));
        // Lagrange
        PolynomialFunctionLagrangeForm formLagrange = new PolynomialFunctionLagrangeForm(dataArrayX, dataArrayY);
        result.append(String.format(pattern, getNameByType(I_LAGRANGE), formLagrange.value(x)));
        // SPLINE
        SplineInterpolator interpolator = SplineInterpolator.createMonotoneCubicSpline(dataX, dataY);
        result.append(String.format(pattern, getNameByType(I_SPLINE), interpolator.interpolate(x)));


        return result.toString();

    }

}
