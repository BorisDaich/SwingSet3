package javax.swing.plaf.nimbus.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.Painter;
import javax.swing.plaf.nimbus.AbstractRegionPainter;

// copy of final class javax.swing.plaf.nimbus.ArrowButtonPainter extends AbstractRegionPainter 
//                                                                interface Painter<T>
//                                         AbstractRegionPainter implements Painter<JComponent>
public class MyArrowButtonPainter extends AbstractRegionPainter implements NimbusColors {

	private static final Logger LOG = Logger.getLogger(MyArrowButtonPainter.class.getName());
	
    /*
    in NimbusDefaults:
            //Initialize ArrowButton
            d.put("ArrowButton.contentMargins", new InsetsUIResource(0, 0, 0, 0));
            d.put("ArrowButton.size", Integer.valueOf(16));
            d.put("ArrowButton[Disabled].foregroundPainter", 
            	new LazyPainter("javax.swing.plaf.nimbus.ArrowButtonPainter", 
            	ArrowButtonPainter.FOREGROUND_DISABLED, 
            	new Insets(0, 0, 0, 0), new Dimension(10, 10), 
            	false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));
            d.put("ArrowButton[Enabled].foregroundPainter", 
            	new LazyPainter("javax.swing.plaf.nimbus.ArrowButtonPainter", 
            	ArrowButtonPainter.FOREGROUND_ENABLED, 
            	new Insets(0, 0, 0, 0), new Dimension(10, 10), 
            	false, AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES, 1.0, 1.0));

Zu custom state siehe NimbusStyle. 
For example, you want JButton to render differently if it's parent is a JToolbar. 
In Nimbus, you specify these custom states by including a special key in UIDefaults.
The following UIDefaults entries define three states for this button:

     JButton.States = Enabled, Disabled, Toolbar
     JButton[Enabled].backgroundPainter = somePainter
     JButton[Disabled].background = BLUE
     JButton[Toolbar].backgroundPainter = someOtherPaint



     */

    public static Painter<JComponent> factory(int state) {
    	PaintContext ctx = new PaintContext
    			( new Insets(0, 0, 0, 0), new Dimension(10, 10)
    			, false
    			, MyPaintContext.PaintContext.FIXED_SIZES 
    			, 1.0, 1.0);
    	return new MyArrowButtonPainter(ctx, state);
    }

// copy of final class javax.swing.plaf.nimbus.ArrowButtonPainter extends AbstractRegionPainter starts here :

	//package private integers representing the available states that
    //this painter will paint. These are used when creating a new instance
    //of ArrowButtonPainter to determine which region/state is being painted
    //by that instance.
    static final int BACKGROUND_ENABLED = 1;
    public static final int FOREGROUND_DISABLED = 2;
    public static final int FOREGROUND_ENABLED = 3;


    private int state; //refers to one of the static final ints above
    private PaintContext ctx;

    //the following 4 variables are reused during the painting code of the layers
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);
    private Ellipse2D ellipse = new Ellipse2D.Float(0, 0, 0, 0);

    //All Colors used for painting are stored here. Ideally, only those colors being used
    //by a particular instance of ArrowButtonPainter would be created. For the moment at least,
    //however, all are created for each instance.
    private Color color1 = MyPaintContext.decodeColor(NIMBUSBASE, "nimbusBase", 0.027408898f, -0.57391655f, 0.1490196f, 0);
    private Color color2 = MyPaintContext.decodeColor(NIMBUSBASE, "nimbusBase", -0.57865167f, -0.6357143f, -0.37254906f, 0);


    //Array of current component colors, updated in each paint call
    private Object[] componentColors;

    public MyArrowButtonPainter(PaintContext ctx, int state) {
        super();
        this.state = state;
        this.ctx = ctx;
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
//    	LOG.info("JComponent:"+c);
        //populate componentColors array with colors calculated in getExtendedCacheKeys call
        componentColors = extendedCacheKeys;
        //generate this entire method. Each state/bg/fg/border combo that has
        //been painted gets its own KEY and paint method.
        switch(state) {
            case FOREGROUND_DISABLED: paintForegroundDisabled(g); break;
            case FOREGROUND_ENABLED: paintForegroundEnabled(g); break;

        }
    }
        


    @Override
    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintForegroundDisabled(Graphics2D g) {
        path = decodePath1();
        g.setPaint(color1);
        g.fill(path);

    }

    private void paintForegroundEnabled(Graphics2D g) {
    	LOG.info("color2:"+color2);
        path = decodePath1();
        g.setPaint(color2);
        g.fill(path);

    }



    private Path2D decodePath1() {
        path.reset();
        path.moveTo(decodeX(1.8f), decodeY(1.2f));
        path.lineTo(decodeX(1.2f), decodeY(1.5f));
        path.lineTo(decodeX(1.8f), decodeY(1.8f));
        path.lineTo(decodeX(1.8f), decodeY(1.2f));
        path.closePath();
        return path;
    }

}
