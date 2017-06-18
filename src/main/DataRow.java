package main;

import javafx.beans.property.SimpleDoubleProperty;

/**
 * Created by lex on 6/18/2017.
 */
public class DataRow {

    private SimpleDoubleProperty x = new SimpleDoubleProperty(0);
    private SimpleDoubleProperty y = new SimpleDoubleProperty(0);

    public DataRow() {
    }

    public DataRow(double x, double y){
        this.x.set(x);
        this.y.set(y);
    }

    public double getX() {
        return x.get();
    }

    public double getY() {
        return y.get();
    }

    public void setX(double x) {
        this.x.set(x);
    }

    public void setY(double y) {
        this.y.set(y);
    }

    public String toString(){
        return String.format("(%s; %s)", x.get(), y.get());
    }

}
