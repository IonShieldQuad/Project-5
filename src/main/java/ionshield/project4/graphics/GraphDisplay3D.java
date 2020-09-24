package ionshield.project4.graphics;

import ionshield.project4.math.InterpolationException;
import ionshield.project4.math.Interpolator3D;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.AbstractDrawable;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.axes.layout.renderers.FixedDecimalTickRenderer;
import org.jzy3d.plot3d.rendering.canvas.Quality;

public class GraphDisplay3D extends AbstractAnalysis {
    public static final Color COLOR_BG = new Color(16, 16, 16);
    public static final Color COLOR_MAIN = new Color(240, 240, 240);
    public static final Color COLOR_WIRE = new Color(255, 255, 255, 32);
    public static final Color COLOR_BASE = new Color(1, 1, 1, .65f);
    
    private Interpolator3D i3d;
    
    public GraphDisplay3D(Interpolator3D i3d) {
        this.i3d = i3d;
    }
    @Override
    public void init() throws Exception {
        // Define a function to plot
        Mapper mapper = new Mapper() {
            public double f(double x, double y) {
                try {
                    return i3d.evaluate(x, y);
                } catch (InterpolationException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        };
    
        // Define range and precision for the function to plot
        Range rangeX = new Range((float)i3d.lowerA(), (float)i3d.upperA());
        int stepsX = 100;
        Range rangeY = new Range((float)i3d.lowerB(), (float)i3d.upperB());
        int stepsY = 100;
    
        AbstractDrawable drawable;
        // Create the object to represent the surface
        final Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(rangeX, stepsX, rangeY, stepsY), mapper);
        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(),
                surface.getBounds().getZmin(), surface.getBounds().getZmax(), COLOR_BASE));
        surface.setWireframeDisplayed(true);
        surface.setWireframeWidth(1);
        surface.setWireframeColor(COLOR_WIRE);
        surface.setFaceDisplayed(true);
        drawable = surface;
    
        // Create a chart
        chart = AWTChartComponentFactory.chart(Quality.Advanced);
        chart.getAxeLayout().setXTickRenderer(new FixedDecimalTickRenderer(1));
        chart.getAxeLayout().setYTickRenderer(new FixedDecimalTickRenderer(1));
        chart.getAxeLayout().setZTickRenderer(new FixedDecimalTickRenderer(1));
        chart.getAxeLayout().setFaceDisplayed(false);
        chart.getAxeLayout().setMainColor(COLOR_MAIN);
        chart.getView().setBackgroundColor(COLOR_BG);
        chart.getScene().getGraph().add(drawable);
    
        chart.getAxeLayout().setXAxeLabel("Length");
        chart.getAxeLayout().setYAxeLabel("Time");
        chart.getAxeLayout().setZAxeLabel("Temperature");
    }
}
