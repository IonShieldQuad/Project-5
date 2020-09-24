package ionshield.project4.main;

import ionshield.project4.math.Interpolator;
import ionshield.project4.math.LinearInterpolator;
import ionshield.project4.math.PointDouble;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PipeReactor {
    private double length = 80;//m
    private double r =  8.31;//J/mol⋅deg;
    private double e1 = 251000;//J/mol
    private double e2 = 297000;//J/mol
    private double a1 = 2e11;
    private double a2 = 8e12;
    private double density = 1.4;//kg/m^3
    private double diameter = 0.1;//m
    private double dl = 0.5;//m
    private double molarMass = 0.02805;//kg/mol
    
    private Interpolator data1 = null;
    private Interpolator data2 = null;
    
    public void calculate(double cin, double m, double t) {
        double k1 = a1 * Math.exp(-e1 / (r * t));
        double k2 = a2 * Math.exp(-e2 / (r * t));
        double u = m / (density * Math.PI * diameter * diameter / 4);
    
        ArrayList<PointDouble> points1 = new ArrayList<>();
        ArrayList<PointDouble> points2 = new ArrayList<>();
        points1.add(new PointDouble(0, concentrationInMolesPerCubicMeter(cin)));
        points2.add(new PointDouble(0, 0));
        
        for (int i = 0; i <= length / dl; i++) {
            double l = i * dl;
            double c1 = points1.get(i).getY();
            double c2 = points2.get(i).getY();
            
            double dc1 = ((-k1 * c1) * density * Math.PI * diameter * diameter / 4) / m;
            double dc2 = ((k1 * c1 - k2 * c2) * density * Math.PI * diameter * diameter / 4) / m;
            
            double nc1 = c1 + dc1 * dl;
            double nc2 = c2 + dc2 * dl;
            
            points1.add(new PointDouble((l + dl) / length, nc1));
            points2.add(new PointDouble((l + dl) / length, nc2));
        }
        
        data1 = new LinearInterpolator(points1.stream().map(p -> new PointDouble(p.getX(), concentrationInFraction(p.getY()))).collect(Collectors.toList()));
        data2 = new LinearInterpolator(points2.stream().map(p -> new PointDouble(p.getX(), concentrationInFraction(p.getY()))).collect(Collectors.toList()));
    }
    
    public Interpolator getResult1() {
        return data1;
    }
    
    public Interpolator getResult2() {
        return data2;
    }
    
    public double concentrationInMolesPerCubicMeter(double c) {
        return c * density / molarMass;
    }
    
    public double concentrationInFraction(double c) {
        return c * molarMass / density;
    }
    
    public double getLength() {
        return length;
    }
    
    public void setLength(double length) {
        this.length = length;
    }
    
    public double getR() {
        return r;
    }
    
    public void setR(double r) {
        this.r = r;
    }
    
    public double getE1() {
        return e1;
    }
    
    public void setE1(double e1) {
        this.e1 = e1;
    }
    
    public double getE2() {
        return e2;
    }
    
    public void setE2(double e2) {
        this.e2 = e2;
    }
    
    public double getA1() {
        return a1;
    }
    
    public void setA1(double a1) {
        this.a1 = a1;
    }
    
    public double getA2() {
        return a2;
    }
    
    public void setA2(double a2) {
        this.a2 = a2;
    }
    
    public double getDensity() {
        return density;
    }
    
    public void setDensity(double density) {
        this.density = density;
    }
    
    public double getDiameter() {
        return diameter;
    }
    
    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }
    
    public double getDl() {
        return dl;
    }
    
    public void setDl(double dl) {
        this.dl = dl;
    }
    
    public double getMolarMass() {
        return molarMass;
    }
    
    public void setMolarMass(double molarMass) {
        this.molarMass = molarMass;
    }
}
