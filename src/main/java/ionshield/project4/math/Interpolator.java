package ionshield.project4.math;

import java.util.function.Function;

public interface Interpolator extends Function<Double, Double> {
    double lower();
    double upper();
    double lowerVal();
    double upperVal();
    double evaluate(double value) throws InterpolationException;
    
    @Override
    default Double apply(Double aDouble) {
        try {
            return evaluate(aDouble);
        } catch (InterpolationException e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}
