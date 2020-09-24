package ionshield.project4.math;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractInterpolator3D implements Interpolator3D {
    private final Map<Double, Interpolator> lines;
    
    public AbstractInterpolator3D(Map<Double, Interpolator> lines) {
        this.lines = new HashMap<>();
        this.lines.putAll(lines);
    }
    
    protected Map<Double, Interpolator> getLines() {
        return lines;
    }
    
    @Override
    public abstract double evaluate(double a, double b) throws InterpolationException;
    
    @Override
    public double lowerA() {
        return lines.values().stream().mapToDouble(Interpolator::lower).min().orElse(0);
    }
    @Override
    public double upperA() {
        return lines.values().stream().mapToDouble(Interpolator::upper).max().orElse(0);
    }
    
    @Override
    public double lowerB() {
        return lines.keySet().stream().mapToDouble(v -> v).min().orElse(0);
    }
    @Override
    public double upperB() {
        return lines.keySet().stream().mapToDouble(v -> v).max().orElse(0);
    }
    
    @Override
    public double lowerVal() {
        return lines.values().stream().mapToDouble(Interpolator::lowerVal).min().orElse(0);
    }
    @Override
    public double upperVal() {
        return lines.values().stream().mapToDouble(Interpolator::upperVal).max().orElse(0);
    }
}
