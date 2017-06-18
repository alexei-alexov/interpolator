package main;

import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

/**
 * Created by lex on 6/18/2017.
 */
public class Interpolator {



    public static final int I_LINEAR = 0;
    public static final int I_NEWTON = 1;
    public static final int I_LAGRANGE = 2;
    public static final int I_SPLINE = 3;

    public static ArrayList<DataRow> interpolate(int type, ObservableList<DataRow> data) {

        switch(type) {
            case I_LINEAR:
                return linearIntepolation(data);
            case I_NEWTON:
            case I_LAGRANGE:
            case I_SPLINE:
                throw new NotImplementedException();
            default: return null;
        }


    }

    private static ArrayList<DataRow> linearIntepolation(ObservableList<DataRow> data) {
        ArrayList<DataRow> result = new ArrayList<>();



        return result;
    }
}
