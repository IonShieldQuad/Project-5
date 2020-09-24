package ionshield.project4.main;

public class Evaporator {
    private double r = 2.26 * 1000000;//J/kg
    private double kT = 5000;//W/m^2
    private double f = 10;//m^2
    private double cT = 4187;//J/kg*deg
    private double tR = 90;//deg
    
    private double mSec;
    private double mOut;
    private double cOut;
    
    public void calculate(double cIn, double mIn, double tP) {
        mSec = (kT * f * (tP - tR)) / (r - cT * tR);
        mOut = mIn - mSec;
        cOut = mIn * cIn / mOut;
    }
    
    public double getR() {
        return r;
    }
    
    public double getkT() {
        return kT;
    }
    
    public double getF() {
        return f;
    }
    
    public double getcT() {
        return cT;
    }
    
    public double gettR() {
        return tR;
    }
    
    public double getmSec() {
        return mSec;
    }
    
    public double getmOut() {
        return mOut;
    }
    
    public double getcOut() {
        return cOut;
    }
}
