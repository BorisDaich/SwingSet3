package javax.swing.plaf.nimbus.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.AbstractRegionPainter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class MyPaintContext extends AbstractRegionPainter {

	private static final Logger LOG = Logger.getLogger(MyPaintContext.class.getName());
	
	// The type AbstractRegionPainter.PaintContext.CacheMode is not visible !!!
	// so mache ich not visible AbstractRegionPainter.PaintContext sichtbar:
    protected static class PaintContext extends AbstractRegionPainter.PaintContext {

    	static CacheMode NO_CACHING = CacheMode.NO_CACHING;
    	static CacheMode FIXED_SIZES = CacheMode.FIXED_SIZES;
    	static CacheMode NINE_SQUARE_SCALE = CacheMode.NINE_SQUARE_SCALE;
        public PaintContext(Insets insets, Dimension canvasSize, boolean inverted,
                CacheMode cacheMode, double maxH, double maxV) {
        	super(insets, canvasSize, inverted, cacheMode, maxH, maxV);
        }

    }

    private MyPaintContext() {
        super();
    }

    static Color decodeColor(Color hint, String key, float hOffset, float sOffset, float bOffset, int aOffset) {
		if (UIManager.getLookAndFeel() instanceof NimbusLookAndFeel) {
			MyPaintContext p = new MyPaintContext();
			return p.decodeColor(key, hOffset, sOffset, bOffset, aOffset); // final
		} else {
			LOG.warning(key+" - can not give a right answer as painter sould not be used outside of nimbus laf" 
					+", but do the best we can");
			return hint.getHSBColor(hOffset,sOffset,bOffset);
		}
    }

	@Override
	protected PaintContext getPaintContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
		// TODO Auto-generated method stub
		
	}

}
