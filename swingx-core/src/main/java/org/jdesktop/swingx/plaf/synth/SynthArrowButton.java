package org.jdesktop.swingx.plaf.synth;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthButtonUI;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;
import javax.swing.plaf.synth.SynthStyle;

// copied from not public class javax.swing.plaf.synth.SynthArrowButton
@SuppressWarnings("serial") // Superclass is not serializable across versions
public class SynthArrowButton extends JButton implements SwingConstants, UIResource {
	
    private int direction;

    public SynthArrowButton(int direction) {
        super();
        super.setFocusable(false);
        setDirection(direction);
        setDefaultCapable(false);
    }

    public String getUIClassID() {
        return "ArrowButtonUI";
    }

    public void updateUI() {
        setUI(new SynthArrowButtonUI());
    }

    public void setDirection(int dir) {
        direction = dir;
        putClientProperty("__arrow_direction__", Integer.valueOf(dir));
        repaint();
    }

    public int getDirection() {
        return direction;
    }
    
    public void setFocusable(boolean focusable) {}

    // copied from private inner class
    //      static class javax.swing.plaf.synth.SynthArrowButton.SynthArrowButtonUI
    private static class SynthArrowButtonUI extends SynthButtonUI {

    	private SynthStyle style; // in super private
    	
        protected void installDefaults(AbstractButton b) {
            super.installDefaults(b);
            updateStyle(b); // not visible in SynthButtonUI
        }

        // ab is SynthArrowButton
        private Region getRegion(AbstractButton ab) {
            Region r = XRegion.getXRegion(ab, true);
            return r==null ? Region.ARROW_BUTTON : r;
        }
        
        // copied from non visible super method SynthButtonUI#updateStyle
        // and do some changes
        void updateStyle(AbstractButton b) {
//          SynthContext context = getContext(b, SynthConstants.ENABLED);
        	Region r = getRegion(b);
            SynthXContext context = (SynthXContext)SynthUtils.getContext(b, r, style, ENABLED);
            SynthStyle oldStyle = style;
//          style = SynthLookAndFeel.updateStyle(context, this);
            style = SynthXContext.updateStyle(context, this);
            if (style != oldStyle) {
                if (b.getMargin() == null || (b.getMargin() instanceof UIResource)) {
                    Insets margin = (Insets)style.get(context,getPropertyPrefix() + "margin");
                    if (margin == null) {
//                      margin = SynthLookAndFeel.EMPTY_UIRESOURCE_INSETS;
                        margin = new InsetsUIResource(0, 0, 0, 0);
                    }
                    b.setMargin(margin);
                }

                Object value = style.get(context, getPropertyPrefix() + "iconTextGap");
                if (value != null) {
                	LookAndFeel.installProperty(b, "iconTextGap", value);
                }

                value = style.get(context, getPropertyPrefix() + "contentAreaFilled");
                LookAndFeel.installProperty(b, "contentAreaFilled",
                		value != null ? value : Boolean.TRUE);

                if (oldStyle != null) {
                    uninstallKeyboardActions(b);
                    installKeyboardActions(b);
                }

            }
        }

        protected void paint(SynthContext context, Graphics g) {
            SynthArrowButton button = (SynthArrowButton)context.getComponent();
            SynthPainter painter = SynthUtils.getPainter(context);
            painter.paintArrowButtonForeground(
                context, g, 0, 0, button.getWidth(), button.getHeight(),
                button.getDirection()
                );
        }

        // cannot override because it is private SynthButtonUI.paintBackground 
        void paintBackground(SynthContext context, Graphics g, JComponent c) {
            SynthPainter painter = SynthUtils.getPainter(context);
            painter.paintArrowButtonBackground(
            	context, g, 0, 0, c.getWidth(), c.getHeight()
            	);
        }

        public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
            SynthPainter painter = SynthUtils.getPainter(context);
            painter.paintArrowButtonBorder(context, g, x, y, w,h);
        }

        public Dimension getMinimumSize() {
            return new Dimension(5, 5);
        }

        public Dimension getMaximumSize() {
            return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }

        public Dimension getPreferredSize(JComponent c) {
            SynthContext context = getContext(c);
            Dimension dim = null;
            if (context.getComponent().getName() == "ScrollBar.button") {
                dim = (Dimension)context.getStyle().get(context, "ScrollBar.buttonSize");
            }
            if (dim == null) {
                int size = context.getStyle().getInt(context, "ArrowButton.size", 16);
                dim = new Dimension(size, size);
            }

            Container parent = context.getComponent().getParent();
            if (parent instanceof JComponent && !(parent instanceof JComboBox)) {
                Object scaleKey = ((JComponent)parent).getClientProperty("JComponent.sizeVariant");
                if (scaleKey != null){
                    if ("large".equals(scaleKey)){
                        dim = new Dimension(
                                (int)(dim.width * 1.15),
                                (int)(dim.height * 1.15));
                    } else if ("small".equals(scaleKey)){
                        dim = new Dimension(
                                (int)(dim.width * 0.857),
                                (int)(dim.height * 0.857));
                    } else if ("mini".equals(scaleKey)){
                        dim = new Dimension(
                                (int)(dim.width * 0.714),
                                (int)(dim.height * 0.714));
                    }
                }
            }

            return dim;
        }

    }

}
