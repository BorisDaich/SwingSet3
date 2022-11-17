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

// copy of final class TreeCellPainter extends AbstractRegionPainter
public class MyTreeCellPainter extends AbstractRegionPainter implements NimbusColors {

	private static final Logger LOG = Logger.getLogger(MyTreeCellPainter.class.getName());
	
/*
in NimbusDefaults:
        d.put("Tree:TreeCell.contentMargins", new InsetsUIResource(0, 0, 0, 0));
        addColor(d, "Tree:TreeCell[Enabled].background", "nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);
        addColor(d, "Tree:TreeCell[Enabled+Focused].background", "nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);
        d.put("Tree:TreeCell[Enabled+Focused].backgroundPainter"
        , new LazyPainter("javax.swing.plaf.nimbus.TreeCellPainter"
        , TreeCellPainter.BACKGROUND_ENABLED_FOCUSED
        , new Insets(5, 5, 5, 5), new Dimension(100, 30)
        , false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0, 1.0));
        addColor(d, "Tree:TreeCell[Enabled+Selected].textForeground", 255, 255, 255, 255);
        d.put("Tree:TreeCell[Enabled+Selected].backgroundPainter"
        , new LazyPainter("javax.swing.plaf.nimbus.TreeCellPainter"
        , TreeCellPainter.BACKGROUND_ENABLED_SELECTED
        , new Insets(5, 5, 5, 5), new Dimension(100, 30)
        , false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0, 1.0));
        addColor(d, "Tree:TreeCell[Focused+Selected].textForeground", 255, 255, 255, 255);
        d.put("Tree:TreeCell[Focused+Selected].backgroundPainter"
        , new LazyPainter("javax.swing.plaf.nimbus.TreeCellPainter"
        , TreeCellPainter.BACKGROUND_SELECTED_FOCUSED
        , new Insets(5, 5, 5, 5), new Dimension(100, 30)
        , false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0, 1.0));
        d.put("Tree:\"Tree.cellRenderer\".contentMargins", new InsetsUIResource(0, 0, 0, 0));
        addColor(d, "Tree:\"Tree.cellRenderer\"[Disabled].textForeground", "nimbusDisabledText", 0.0f, 0.0f, 0.0f, 0);
 */
	public static Dimension canvasSize() {
		return new Dimension(100, 30);
	}
    public static Painter<JComponent> factory(int state) {
    	PaintContext ctx = new PaintContext
    			( new Insets(5, 5, 5, 5), canvasSize()
    			, false
    			, MyPaintContext.PaintContext.NO_CACHING 
    			, 1.0, 1.0);
    	return new MyTreeCellPainter(ctx, state);
    }

// copy of final class javax.swing.plaf.nimbus.TreeCellPainter extends AbstractRegionPainter starts here :

    //package private integers representing the available states that
    //this painter will paint. These are used when creating a new instance
    //of TreeCellPainter to determine which region/state is being painted
    //by that instance.
    static final int BACKGROUND_ENABLED = 1;
    public static final int BACKGROUND_ENABLED_FOCUSED = 2;
    public static final int BACKGROUND_ENABLED_SELECTED = 3;
    public static final int BACKGROUND_SELECTED_FOCUSED = 4;


    private int state; //refers to one of the static final ints above
    private PaintContext ctx;

    //the following 4 variables are reused during the painting code of the layers
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0, 0, 0, 0);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0, 0, 0, 0, 0, 0);
    private Ellipse2D ellipse = new Ellipse2D.Float(0, 0, 0, 0);

    //All Colors used for painting are stored here. Ideally, only those colors being used
    //by a particular instance of TreeCellPainter would be created. For the moment at least,
    //however, all are created for each instance.
    private Color color1 = MyPaintContext.decodeColor(NIMBUSFOCUS, "nimbusFocus", 0.0f, 0.0f, 0.0f, 0);
    private Color color2 = MyPaintContext.decodeColor(NIMBUS_SELECTIONBACKGROUND, "nimbusSelectionBackground", 0.0f, 0.0f, 0.0f, 0);


    //Array of current component colors, updated in each paint call
    private Object[] componentColors;

    public MyTreeCellPainter(PaintContext ctx, int state) {
        super();
        this.state = state;
        this.ctx = ctx;
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        //populate componentColors array with colors calculated in getExtendedCacheKeys call
        componentColors = extendedCacheKeys;
        //generate this entire method. Each state/bg/fg/border combo that has
        //been painted gets its own KEY and paint method.
        switch(state) {
            case BACKGROUND_ENABLED_FOCUSED: paintBackgroundEnabledAndFocused(g); break;
            case BACKGROUND_ENABLED_SELECTED: paintBackgroundEnabledAndSelected(g); break;
            case BACKGROUND_SELECTED_FOCUSED: paintBackgroundSelectedAndFocused(g); break;

        }
    }
        


    @Override
    protected final PaintContext getPaintContext() {
        return ctx;
    }

    private void paintBackgroundEnabledAndFocused(Graphics2D g) {
        path = decodePath1();
        g.setPaint(color1);
        g.fill(path);

    }

    private void paintBackgroundEnabledAndSelected(Graphics2D g) {
        rect = decodeRect1();
        g.setPaint(color2);
        g.fill(rect);

    }

    private void paintBackgroundSelectedAndFocused(Graphics2D g) {
        rect = decodeRect1();
        g.setPaint(color2);
        g.fill(rect);
        path = decodePath1();
        g.setPaint(color1);
        g.fill(path);

    }



    private Path2D decodePath1() {
        path.reset();
        path.moveTo(decodeX(0.0f), decodeY(0.0f));
        path.lineTo(decodeX(0.0f), decodeY(3.0f));
        path.lineTo(decodeX(3.0f), decodeY(3.0f));
        path.lineTo(decodeX(3.0f), decodeY(0.0f));
        path.lineTo(decodeX(0.24000001f), decodeY(0.0f));
        path.lineTo(decodeX(0.24000001f), decodeY(0.24000001f));
        path.lineTo(decodeX(2.7600007f), decodeY(0.24000001f));
        path.lineTo(decodeX(2.7600007f), decodeY(2.7599998f));
        path.lineTo(decodeX(0.24000001f), decodeY(2.7599998f));
        path.lineTo(decodeX(0.24000001f), decodeY(0.0f));
        path.lineTo(decodeX(0.0f), decodeY(0.0f));
        path.closePath();
        return path;
    }

    private Rectangle2D decodeRect1() {
            rect.setRect(decodeX(0.0f), //x
                         decodeY(0.0f), //y
                         decodeX(3.0f) - decodeX(0.0f), //width
                         decodeY(3.0f) - decodeY(0.0f)); //height
        return rect;
    }

}
