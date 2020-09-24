package ionshield.project4.main;

import ionshield.project4.math.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class HeatExchanger {
    private double kT = 6500;//W / m^3*deg
    private double cT = 4190;//J / kg*deg
    private double density = 1000;//kg / m^3
    private double tT = 80;//deg C
    private double length = 1;//m
    private double diameter = 0.05;//m
    private double u = 0.2;//m/s
    private double timeMax = 5;//s
    
    private double da = 0.3;
    private double db = 0.2;
    
    public Interpolator3D calculate(Function<Double, Double> t0L, Function<Double, Double> tInT) {
        Map<Double, Interpolator> lines = new HashMap<>();
        double timeAvg = length / u;
    
        //Integration I
        for (double b = -timeAvg / 2; b < 0; b += db) {
            List<PointDouble> points = new ArrayList<>();
            points.add(new PointDouble(-b , t0L.apply(-2 * u * b)));
            for (double a = -b; a < b + timeAvg; a += da) {
                double t = points.get(points.size() - 1).getY();
                double dtda = dtda(t);
                double nt = t + dtda * da;
                points.add(new PointDouble(a + da, nt));
            }
            lines.put(b, new LinearInterpolator(points));
        }
    
        //Integration II
        for (double b = 0; b < (timeMax - timeAvg) / 2; b += db) {
            List<PointDouble> points = new ArrayList<>();
            points.add(new PointDouble(b , tInT.apply(2 * b)));
            for (double a = b; (a < b + timeAvg) && (a < -b + timeMax); a += da) {
                double t = points.get(points.size() - 1).getY();
                double dtda = dtda(t);
                double nt = t + dtda * da;
                points.add(new PointDouble(a + da, nt));
            }
            lines.put(b, new LinearInterpolator(points));
        }
    
        //Integration III
        for (double b = (timeMax - timeAvg) / 2; b < timeMax / 2; b += db) {
            List<PointDouble> points = new ArrayList<>();
            points.add(new PointDouble(b , tInT.apply(2 * b)));
            for (double a = b; (a < b + timeAvg) && (a < -b + timeMax); a += da) {
                double t = points.get(points.size() - 1).getY();
                double dtda = dtda(t);
                double nt = t + dtda * da;
                points.add(new PointDouble(a + da, nt));
            }
            lines.put(b, new LinearInterpolator(points));
        }
        
        Interpolator3D interpolator3D = new LinearInterpolator3D(lines);
        //return interpolator3D;
        return new Interpolator3D() {
            @Override
            public double evaluate(double l, double t) throws InterpolationException {
                double a = ((l / u) + t) / 2;
                double b = t - a;
                return interpolator3D.evaluate(a, b);
            }
    
            @Override
            public double lowerA() {
                return 0;
            }
    
            @Override
            public double upperA() {
                return length;
            }
    
            @Override
            public double lowerB() {
                return 0;
            }
    
            @Override
            public double upperB() {
                return timeMax;
            }
    
            @Override
            public double lowerVal() {
                return interpolator3D.lowerVal();
            }
    
            @Override
            public double upperVal() {
                return interpolator3D.upperVal();
            }
        };
    }
    
    private double dtda (double currT) {
        return (tT - currT) * (4 * kT) / (cT * density * diameter);
    }
    
    public double getkT() {
        return kT;
    }
    
    public void setkT(double kT) {
        this.kT = kT;
    }
    
    public double getcT() {
        return cT;
    }
    
    public void setcT(double cT) {
        this.cT = cT;
    }
    
    public double getDensity() {
        return density;
    }
    
    public void setDensity(double density) {
        this.density = density;
    }
    
    public double gettT() {
        return tT;
    }
    
    public void settT(double tT) {
        this.tT = tT;
    }
    
    public double getLength() {
        return length;
    }
    
    public void setLength(double length) {
        this.length = length;
    }
    
    public double getDiameter() {
        return diameter;
    }
    
    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }
    
    public double getU() {
        return u;
    }
    
    public void setU(double u) {
        this.u = u;
    }
    
    public double getTimeMax() {
        return timeMax;
    }
    
    public void setTimeMax(double timeMax) {
        this.timeMax = timeMax;
    }
    
    public double getDa() {
        return da;
    }
    
    public void setDa(double da) {
        this.da = da;
    }
    
    public double getDb() {
        return db;
    }
    
    public void setDb(double db) {
        this.db = db;
    }
}
