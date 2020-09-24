package ionshield.project4.graphics;

public class Point {
    public int x;
    public int y;
    public double a = 1;
    
    public Point() {}
    public Point(int x, int y) {
    
        this.x = x;
        this.y = y;
    }
    public Point(int x, int y, double a) {
        
        this.x = x;
        this.y = y;
        this.a = a;
    }
}
