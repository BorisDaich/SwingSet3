package org.jdesktop.swingx.plaf.synth;

import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.plaf.nimbus.NimbusStyle;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthPainter;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthUI;

public class SynthXContext extends SynthContext {

    private static final Logger LOG = Logger.getLogger(SynthXContext.class.getName());

    // copied from javax.swing.plaf.synth.SynthLookAndFeel.updateStyle
    static SynthStyle updateStyle(SynthXContext context, SynthUI ui) {
        SynthStyle newStyle = SynthLookAndFeel.getStyle(context.getComponent(), context.getRegion());
        SynthStyle oldStyle = context.getStyle();
        if (newStyle != oldStyle) {
        	if (oldStyle != null) {
        		oldStyle.uninstallDefaults(context);
        	}
        	context.setStyle(newStyle);
//        	newStyle.installDefaults(context, ui); // not visible
        	boolean isSubregion = context.getRegion().isSubregion();
        	if (!isSubregion) {
        		LOG.info("//////////////// TODO");
        	}
        	newStyle.installDefaults(context);
        }
        return newStyle;
    }

    static SynthXContext getContext(JComponent c, SynthStyle style, int state) {
        return getContext(c, SynthLookAndFeel.getRegion(c), style, state);
    }
    static SynthXContext getContext(JComponent component,
            Region region, SynthStyle style, int state) {
    	return new SynthXContext(component, region, style, state);
    }

    public SynthXContext(JComponent component, Region region, SynthStyle style, int state) {
    	super(component, region, style, state);
    	setStyle(style);
    }

    private SynthStyle style;
    // SynthContext#setStyle is not visible
    void setStyle(SynthStyle style) {
    	if(super.getStyle() == style) {
    		// unchanged
        	if(super.getStyle() instanceof NimbusStyle) {
        		NimbusStyle ns = (NimbusStyle)super.getStyle();
            	LOG.fine("unchanged SynthContext.style is "+ ns);
        	}
    	} else {
        	LOG.warning("SynthContext.style is "+super.getStyle() + " - set to "+style);
    	}
    	this.style = style;
    }

    public SynthStyle getStyle() {
    	if(super.getStyle()!=style) {
        	LOG.warning("SynthContext.style is "+super.getStyle() + " - SynthXContext is "+style);
    	}
        return style;
    }
    
    SynthPainter getPainter() {
        SynthPainter painter = getStyle().getPainter(this);
        return painter == null ? SynthUtils.NULL_PAINTER : painter;
    }

}
