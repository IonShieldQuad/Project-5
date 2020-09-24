package ionshield.project4.main;

import ionshield.project4.graphics.GraphDisplay3D;
import ionshield.project4.math.Interpolator3D;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.maths.Rectangle;
import org.mariuszgromada.math.mxparser.Function;

import javax.swing.*;

public class MainWindow {
    private JPanel rootPanel;
    private JTextArea log;
    private JButton calculateButton;
    private JTextField t0Field;
    private JTextField tInField;
    private JTextField dxField;
    private JTextField dtField;
    private JTextField lengthField;
    private JTextField aField;
    private JTextField tMaxField;
    private JTextField tOutField;
    
    public static final String TITLE = "Project-5";
    
    private MainWindow() {
        initComponents();
    }
    
    private void initComponents() {
        calculateButton.addActionListener(e -> calculate());
    }
    
    
    
    private void calculate() {
        try {
            log.setText("");
    
            Function t0 = new Function(t0Field.getText());
            Function tIn = new Function(tInField.getText());
            Function tOut = new Function(tOutField.getText());
            if (!(t0.checkSyntax() && tIn.checkSyntax() && tOut.checkSyntax())) {
                throw new NumberFormatException("Invalid function syntax");
            }
            
            double dx = Double.parseDouble(dxField.getText());
            double dt = Double.parseDouble(dtField.getText());
            double length = Double.parseDouble(lengthField.getText());
            double a = Double.parseDouble(aField.getText());
            double tMax = Double.parseDouble(tMaxField.getText());
            
            HeatSim sim = new HeatSim();
            sim.setA(a);
            sim.setDt(dt);
            sim.setDx(dx);
            sim.setLength(length);
            sim.setTimeMax(tMax);
            
    
            Interpolator3D i3d = sim.calculate(t0::calculate, tIn::calculate, tOut::calculate);
    
            GraphDisplay3D ui = new GraphDisplay3D(i3d);
            AnalysisLauncher.open(ui, new Rectangle(600, 600));
            
        }
        catch (NumberFormatException e) {
            log.append("\nInvalid input format");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public static void main(String[] args) {
        JFrame frame = new JFrame(TITLE);
        MainWindow gui = new MainWindow();
        frame.setContentPane(gui.rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
