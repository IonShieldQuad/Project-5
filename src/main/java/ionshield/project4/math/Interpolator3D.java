package ionshield.project4.math;

import java.util.function.BiFunction;

public interface Interpolator3D extends BiFunction<Double, Double, Double> {
    double evaluate(double a, double b) throws InterpolationException;
    double lowerA();
    double upperA();
    double lowerB();
    double upperB();
    double lowerVal();
    double upperVal();
    
    @Override
    default Double apply(Double a, Double b) {
        try {
            return evaluate(a, b);
        } catch (InterpolationException e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}
