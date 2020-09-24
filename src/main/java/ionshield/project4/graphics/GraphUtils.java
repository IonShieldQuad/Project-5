package ionshield.project4.graphics;

import java.awt.*;
import java.util.ArrayList;

public class GraphUtils {
    
    public static void drawLine(Line l, Graphics g, Color lineColor) {
        Line line = l;//new Line(getScale() * (int)Math.round(getWidth() * (l.x1 / 100.0) / getScale()), (int) Math.round(getHeight() * (1 - (l.y1 / 100.0)) / getScale()), getScale() * (int)Math.round(getWidth() * (l.x2 / 100.0) / getScale()), getScale() * (int)Math.round(getHeight() * (1 - (l.y2 / 100.0)) / getScale()));
        
        int dx = line.x2 - line.x1;
        int dy = line.y2 - line.y1;
        boolean inv = Math.abs(dy) >= Math.abs(dx);
        
        getPoints(dx, dy, true).forEach(p -> {
            float a = (float)Math.max(Math.min(p.a, 1.0), 0.0);
            //System.out.println("X:" + p.x + " Y:" + p.y + " A:" + a);
            //if (!altAlpha) {
                g.setColor(new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), (int)Math.min(255, Math.max(0, Math.round(Math.pow(a, 1 / 1.6) * 255)))));
            //}
            //else {
                //g.setColor(new Color((int) Math.floor(lineColor.getRed() * a), (int) Math.floor(lineColor.getGreen() * a), (int) Math.floor(lineColor.getBlue() * a)));
            //}
            if (inv) {
                g.fillRect(line.x1 + p.y * (int)Math.signum(dx), line.y1 + p.x * (int)Math.signum(dy), 1, 1);
            }
            else {
                g.fillRect(line.x1 + p.x * (int)Math.signum(dx), line.y1 + p.y * (int)Math.signum(dy), 1, 1);
            }
        });
    }
    
    private static java.util.List<Point> getPoints(int dx, int dy, boolean aa) {
        dx = Math.abs(dx);
        dy = Math.abs(dy);
        if (dy > dx) {
            return getPoints(dy, dx, aa);
        }
        java.util.List<Point> list = new ArrayList<>(dx);
        list.add(new Point(0, 0));
        
        int x = 0;
        int y = 0;
        int d = 2*dy - dx;
        
        for (x = 1; x <= dx; x++) {
            if (d >= 0) {
                if (aa) {
                    //System.out.println("X:" + x + " Y:" + y + " D:" + d + " DY/DX:" + (double)dy/dx + " A1:" + (((double)dy/dx) * x - y) + " A2:" + ((y + 1) - ((double)dy/dx) * x));
                    list.add(new Point(x, y + 1, ((double)dy/dx) * x - y));
                    list.add(new Point(x, y, (y + 1) - ((double)dy/dx) * x));
                }
                else {
                    list.add(new Point(x, y + 1));
                }
                y++;
                d += 2*(dy - dx);
            }
            else {
                if (aa) {
                    //System.out.println("X:" + x + " Y:" + y + " D:" + d + " DY/DX:" + (double)dy/dx + " A1:" + (((double)dy/dx) * x - y) + " A2:" + ((y + 1) - ((double)dy/dx) * x));
                    list.add(new Point(x, y + 1, ((double)dy/dx) * x - y));
                    list.add(new Point(x, y, (y + 1) - ((double)dy/dx) * x));
                }
                else {
                    list.add(new Point(x, y));
                }
                d += 2*dy;
            }
            //list.add(new Point(x, y));
        }
        
        return list;
    }
}
