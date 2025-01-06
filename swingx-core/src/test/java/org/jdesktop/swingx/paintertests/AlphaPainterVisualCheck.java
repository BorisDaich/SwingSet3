package org.jdesktop.swingx.paintertests;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.AlphaPainter;
import org.jdesktop.swingx.painter.BusyPainter;
import org.jdesktop.swingx.painter.PinstripePainter;

/**
 *
 * @author Eugen Hanussek https://github.com/homebeaver
 */
public class AlphaPainterVisualCheck extends InteractiveTestCase {
	
    /**
     * Do nothing, make the test runner happy
     * (would output a warning without a test fixture).
     *
     */
    public void testDummy() {
        
    }

    public static void main(String[] args) throws Exception {
         AlphaPainterVisualCheck test = new AlphaPainterVisualCheck();
         
         try {
             test.runInteractiveTests();
         } catch (Exception e) {
             System.err.println("exception when executing interactive tests:");
             e.printStackTrace();
         }
     }
    
    public void interactiveAlphaPainterCheck() {
        JXPanel panel = new JXPanel();
        panel.setPreferredSize(new Dimension(200, 200));
        // AlphaPainter<T> extends CompoundPainter<T>
        AlphaPainter<Component> alpha = new AlphaPainter<Component>();
        alpha.setAlpha(0.7f);

        BusyPainter bp = new BusyPainter(96);
        bp.setFrame(1);
        bp.setPaintCentered(true);
        bp.setBaseColor(Color.RED);
        bp.setDirection(BusyPainter.Direction.LEFT);

//        PinstripePainter(Paint paint, double angle, double stripeWidth, double spacing)
        alpha.setPainters(
        		new PinstripePainter(Color.GREEN, 45, 20, 20),
        		new PinstripePainter(Color.YELLOW, 90, 10, 10),
        		bp );
        
        panel.setBackgroundPainter(alpha);
        
        showInFrame(panel, "Alpha Painter Check");
    }
}
