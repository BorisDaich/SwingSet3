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
			LOG.fine(key+" - can not give a right answer as painter sould not be used outside of nimbus laf" 
					+", but do the best we can");
			// EUG: wie in class javax.swing.plaf.nimbus.DerivedColor#rederiveColor
			float[] tmp = Color.RGBtoHSB(hint.getRed(), hint.getGreen(), hint.getBlue(), null);
            // apply offsets
            tmp[0] = tmp[0] + hOffset;
            tmp[1] = clamp(tmp[1] + sOffset);
            tmp[2] = clamp(tmp[2] + bOffset);
            int alpha = clamp(hint.getAlpha() + aOffset);
            int argbValue = (Color.HSBtoRGB(tmp[0], tmp[1], tmp[2]) & 0xFFFFFF) | (alpha << 24);
            return new Color(argbValue);
		}
    }
    // EUG: see class javax.swing.plaf.nimbus.DerivedColor
    private static float clamp(float value) {
        if (value < 0) {
            value = 0;
        } else if (value > 1) {
            value = 1;
        }
        return value;
    }

    private static int clamp(int value) {
        if (value < 0) {
            value = 0;
        } else if (value > 255) {
            value = 255;
        }
        return value;
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

//	public boolean isInstanceOf(Object o) {
//		return o instanceof javax.swing.plaf.nimbus.NimbusIcon; // type javax.swing.plaf.nimbus.NimbusIcon is not visible
//	}
}
