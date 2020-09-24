package ionshield.project4.math;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LinearInterpolator3D extends AbstractInterpolator3D {
    
    public LinearInterpolator3D(Map<Double, Interpolator> lines) {
        super(lines);
    }
    
    @Override
    public double evaluate(double a, double b) throws InterpolationException {
        Map<Double, Interpolator> lines = getLines();
        List<Map.Entry<Double, Interpolator>> list = lines.entrySet().stream().sorted(Comparator.comparingDouble(Map.Entry::getKey)).collect(Collectors.toList());
    
        if (list.size() == 0) {
            return 0;
        }
        int lastIndex = list.size() - 1;
        if (b <= lowerB()) {
            if (list.size() == 1) {
                return list.get(0).getValue().evaluate(a);
            }
            PointDouble l = new PointDouble(list.get(0).getKey(), list.get(0).getValue().evaluate(a));
            PointDouble r = new PointDouble(list.get(0).getKey(), list.get(1).getValue().evaluate(a));
            double alpha = (b - l.getX()) / (r.getX() - l.getX());
            return alpha * r.getY() + (1 - alpha) * l.getY();
        }
        if (b >= upperB()) {
            if (list.size() == 1) {
                return list.get(lastIndex).getValue().evaluate(a);
            }
            PointDouble l = new PointDouble(list.get(lastIndex - 1).getKey(), list.get(lastIndex - 1).getValue().evaluate(a));
            PointDouble r = new PointDouble(list.get(lastIndex).getKey(), list.get(lastIndex).getValue().evaluate(a));
            double alpha = (b - l.getX()) / (r.getX() - l.getX());
            return alpha * r.getY() + (1 - alpha) * l.getY();
        }
        int i0 = 0;
        int i1 = lastIndex;
        int i = (int)Math.floor((i0 + i1) / 2.0);
        while (i0 <= i1) {
            int m = (int)Math.floor((i0 + i1) / 2.0);
            if (m + 1 > lastIndex) {
                i = m;
                break;
            }
            if (list.get(m).getKey() > b) {
                i1 = m - 1;
            }
            else {
                if (list.get(m + 1).getKey() < b) {
                    i0 = m + 1;
                }
                else {
                    i = m;
                    break;
                }
            }
        }
        PointDouble l = new PointDouble(list.get(i).getKey(), list.get(i).getValue().evaluate(a));
        PointDouble r = new PointDouble(list.get(i + 1).getKey(), list.get(i + 1).getValue().evaluate(a));
        double alpha = (b - l.getX()) / (r.getX() - l.getX());
    
        return alpha * r.getY() + (1 - alpha) * l.getY();
    }
}
