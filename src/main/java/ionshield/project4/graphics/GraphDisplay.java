package ionshield.project4.graphics;

import ionshield.project4.math.InterpolationException;
import ionshield.project4.math.Interpolator;
import ionshield.project4.math.PointDouble;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class GraphDisplay extends JPanel {
    private static final int MARGIN_X = 50;
    private static final int MARGIN_Y = 50;
    private static final double EXTRA_AMOUNT = 0.0;
    private static final Color GRID_COLOR = Color.GRAY;
    private static final Color[] GRAPH_COLORS = new Color[] {
            new Color(0xff7a81),
            new Color(0x9cff67),
            new Color(0x526aff),
            new Color(0xe6bc00),
            new Color(0xdf2a6f),
            new Color(0x01a343),
            
    };
    private static final Color[] GRAPH_HIGHLIGHT_COLORS = new Color[] {
            new Color(0x00ffff),
    };
    private static final Color POINT_COLOR = Color.YELLOW;
    private static final int POINT_SIZE = 5;
    private static final int PRECISION = 3;
    
    private List<Interpolator> interpolators = new ArrayList<>();
    private List<Interpolator> interpolatorsHighligthed = new ArrayList<>();
    
    private double lowerX;
    private double upperX;
    private double lowerY;
    private double upperY;
    
    private double minX = -Double.MAX_VALUE;
    private double maxX = +Double.MAX_VALUE;
    private double minY = -Double.MAX_VALUE;
    private double maxY = +Double.MAX_VALUE;
    
    public GraphDisplay() {
        super();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        calculateBounds();
        
        drawGrid(g);
        for (int i = 0; i < interpolators.size(); i++) {
            drawGraph(g, interpolators.get(i), GRAPH_COLORS[i % GRAPH_COLORS.length]);
        }
        for (int i = 0; i < interpolatorsHighligthed.size(); i++) {
            drawGraph(g, interpolatorsHighligthed.get(i), GRAPH_HIGHLIGHT_COLORS[i % GRAPH_HIGHLIGHT_COLORS.length]);
        }
        drawValues(g);
    }
    
    
    private void drawGraph(Graphics g, Interpolator interpolator, Color color) {
        g.setColor(color);
        int prev = 0;
        for (int i = 0; i < graphWidth(); i++) {
            try {
                PointDouble val = graphToValue(new PointDouble(i + MARGIN_X, 0));
                val = new PointDouble(val.getX(), interpolator.evaluate(val.getX()));
                val = valueToGraph(val);
                if (i != 0) {
                    GraphUtils.drawLine(new Line(MARGIN_X + i - 1, prev, (int) Math.round(val.getX()), (int)Math.round(val.getY())), g, color);
                    //g.drawLine(MARGIN_X + i - 1, prev, (int) Math.round(val.getX()), (int) Math.round(val.getY()));
                }
                prev = (int) Math.round(val.getY());
            } catch (InterpolationException ignored) {}
        }
    }
    
    private void drawGrid(Graphics g) {
        g.setColor(GRID_COLOR);
        g.drawLine(MARGIN_X, getHeight() - MARGIN_Y, getWidth() - MARGIN_X, getHeight() - MARGIN_Y);
        g.drawLine(MARGIN_X, MARGIN_Y + (int)(graphHeight() * (1 - EXTRA_AMOUNT)), getWidth() - MARGIN_X, MARGIN_Y + (int)(graphHeight() * (1 - EXTRA_AMOUNT)));
        g.drawLine(MARGIN_X, MARGIN_Y + (int)(graphHeight() * EXTRA_AMOUNT), getWidth() - MARGIN_X, MARGIN_Y + (int)(graphHeight() * EXTRA_AMOUNT));
        
        g.drawLine(MARGIN_X, getHeight() - MARGIN_Y, MARGIN_X, MARGIN_Y);
        g.drawLine(MARGIN_X + (int)(graphWidth() * EXTRA_AMOUNT), getHeight() - MARGIN_Y, MARGIN_X + (int)(graphWidth() * EXTRA_AMOUNT), MARGIN_Y);
        g.drawLine(MARGIN_X + (int)(graphWidth() * (1 - EXTRA_AMOUNT)), getHeight() - MARGIN_Y, MARGIN_X + (int)(graphWidth() * (1 - EXTRA_AMOUNT)), MARGIN_Y);
        
    }
    
    private void drawValues(Graphics g) {
        g.setColor(GRID_COLOR);
        g.drawString(BigDecimal.valueOf(lowerX()).setScale(PRECISION, RoundingMode.HALF_UP).toString(), MARGIN_X + (int)(graphWidth() * EXTRA_AMOUNT), getHeight() - MARGIN_Y / 2);
        g.drawString(BigDecimal.valueOf(upperX()).setScale(PRECISION, RoundingMode.HALF_UP).toString(), MARGIN_X + (int)(graphWidth() * (1 - EXTRA_AMOUNT)), getHeight() - MARGIN_Y / 2);
        g.drawString(BigDecimal.valueOf(lowerY()).setScale(PRECISION, RoundingMode.HALF_UP).toString(), MARGIN_X / 4, MARGIN_Y + (int)(graphHeight() * (1 - EXTRA_AMOUNT)));
        g.drawString(BigDecimal.valueOf(upperY()).setScale(PRECISION, RoundingMode.HALF_UP).toString(), MARGIN_X / 4, MARGIN_Y + (int)(graphHeight() * EXTRA_AMOUNT));
    }
    
    private int graphWidth() {
        return getWidth() - 2 * MARGIN_X;
    }
    
    private int graphHeight() {
        return getHeight() - 2 * MARGIN_Y;
    }
    
    private double lowerX() {
        return lowerX;
    }
    
    private double upperX() {
        return upperX;
    }
    
    private double lowerY() {
        return lowerY;
    }
    
    private double upperY() {
        return upperY;
    }
    
    public List<Interpolator> getInterpolators() {
        return interpolators;
    }
    
    public void setInterpolators(List<Interpolator> interpolators) {
        this.interpolators = interpolators;
    }
    
    public List<Interpolator> getInterpolatorsHighligthed() {
        return interpolatorsHighligthed;
    }
    
    public void setInterpolatorsHighligthed(List<Interpolator> interpolatorsHighligthed) {
        this.interpolatorsHighligthed = interpolatorsHighligthed;
    }
    
    private void calculateBounds() {
        if (interpolators == null) {
            interpolators = new ArrayList<>();
        }
        if (interpolatorsHighligthed == null) {
            interpolatorsHighligthed = new ArrayList<>();
        }
    
        List<Interpolator> all = new ArrayList<>(interpolators);
        all.addAll(interpolatorsHighligthed);
        
        Optional<Double> lowerX = all.stream().map(Interpolator::lower).min(Comparator.naturalOrder());
        Optional<Double> upperX = all.stream().map(Interpolator::upper).max(Comparator.naturalOrder());
        Optional<Double> lowerY = all.stream().map(Interpolator::lowerVal).min(Comparator.naturalOrder());
        Optional<Double> upperY = all.stream().map(Interpolator::upperVal).max(Comparator.naturalOrder());
        
        this.lowerX = Math.max(lowerX.isPresent() ? lowerX.get() : 0, minX);
        this.upperX = Math.min(upperX.isPresent() ? upperX.get() : 0, maxX);
        this.lowerY = Math.max(lowerY.isPresent() ? lowerY.get() : 0, minY);
        this.upperY = Math.min(upperY.isPresent() ? upperY.get() : 0, maxY);
    }
    
    private PointDouble valueToGraph(PointDouble point) {
        double valX = (point.getX() - lowerX()) / (upperX() - lowerX());
        double valY = (point.getY() - lowerY()) / (upperY() - lowerY());
        return new PointDouble(MARGIN_X + (int)((graphWidth() * EXTRA_AMOUNT) * (1 - valX) + (graphWidth() * (1 - EXTRA_AMOUNT)) * valX), getHeight() - MARGIN_Y - (int)((graphHeight() * EXTRA_AMOUNT) * (1 - valY) + (graphHeight() * (1 - EXTRA_AMOUNT)) * valY));
    }
    
    private PointDouble graphToValue(PointDouble point) {
        double valX = (point.getX() - (MARGIN_X + (graphWidth() * EXTRA_AMOUNT))) / ((MARGIN_X + (graphWidth() * (1 - EXTRA_AMOUNT))) - (MARGIN_X + (graphWidth() * EXTRA_AMOUNT)));
        double valY = (point.getY() - (MARGIN_Y + (graphHeight() * (1 - EXTRA_AMOUNT)))) / ((MARGIN_Y + (graphHeight() * EXTRA_AMOUNT)) - (MARGIN_Y + (graphHeight() * (1 - EXTRA_AMOUNT))));
        return new PointDouble(lowerX() * (1 - valX) + upperX() * valX, lowerY() * (1 - valY) + upperY() * valY);
    }
    
    public double getMinX() {
        return minX;
    }
    
    public void setMinX(double minX) {
        this.minX = minX;
    }
    
    public double getMaxX() {
        return maxX;
    }
    
    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }
    
    public double getMinY() {
        return minY;
    }
    
    public void setMinY(double minY) {
        this.minY = minY;
    }
    
    public double getMaxY() {
        return maxY;
    }
    
    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }
}
