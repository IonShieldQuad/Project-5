package ionshield.project4.main;

import ionshield.project4.math.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class HeatSim {
    private double a = 1.3e-2;//m^2 / s
    private double length = 1;//m
    private double timeMax = 100;//s
    
    private double dx = 0.1;
    private double dt = 0.02;
    
    public Interpolator3D calculate(Function<Double, Double> t0X, Function<Double, Double> tInT, Function<Double, Double> tOutT) {
        Map<Double, Interpolator> lines = new HashMap<>();
        
        int sizeX = (int)Math.ceil(length / dx);
        int sizeT = (int)Math.ceil(timeMax / dt);
    
        double t = 0;
        List<PointDouble> points = new ArrayList<>(sizeX);
        List<PointDouble> pointsNext = new ArrayList<>(sizeX);
        //Initialize values along length
        for (int i = 0; i < sizeX; i++) {
            double x = i * dx;
            points.add(new PointDouble(x , t0X.apply(x)));
        }
        lines.put(t, new LinearInterpolator(points));
    
        //Integrate over time
        for (int j = 0; j < sizeT; j++) {
            t = j * dt;
            //Start value from function
            pointsNext.add(new PointDouble(0, tInT.apply(t)));
            //Calculate rest of the points from previous iteration
            for (int i = 1; i < sizeX - 1; i++) {
                double x = i * dx;
                
                double l = points.get(i - 1).getY();
                double m = points.get(i).getY();
                double r = points.get(i + 1).getY();
                
                double val = a * dt * (l - 2 * m + r) / (dx * dx) + m;
                
                pointsNext.add(new PointDouble(x,val));
            }
            //End value from function
            pointsNext.add(new PointDouble(length, tOutT.apply(t)));
            
            lines.put(t + dt, new LinearInterpolator(pointsNext));
            points = pointsNext;
            pointsNext = new ArrayList<>(sizeX);
        }
        
        return new LinearInterpolator3D(lines);
    }
    
    public double getA() {
        return a;
    }
    
    public void setA(double a) {
        this.a = a;
    }
    
    public double getLength() {
        return length;
    }
    
    public void setLength(double length) {
        this.length = length;
    }
    
    public double getTimeMax() {
        return timeMax;
    }
    
    public void setTimeMax(double timeMax) {
        this.timeMax = timeMax;
    }
    
    public double getDx() {
        return dx;
    }
    
    public void setDx(double dx) {
        this.dx = dx;
    }
    
    public double getDt() {
        return dt;
    }
    
    public void setDt(double dt) {
        this.dt = dt;
    }
}
