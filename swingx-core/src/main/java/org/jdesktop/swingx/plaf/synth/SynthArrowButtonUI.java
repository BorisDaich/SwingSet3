package org.jdesktop.swingx.plaf.synth;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.plaf.synth.SynthButtonUI;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;

import org.jdesktop.swingx.plaf.synth.SynthXComboBoxUI.SynthArrowButton;

// copied from inner class private static class SynthArrowButton.SynthArrowButtonUI
class SynthArrowButtonUI extends SynthButtonUI {

    protected void installDefaults(AbstractButton b) {
        super.installDefaults(b);
//        updateStyle(b); // updateStyle in SynthButtonUI is not visible TODO
    }
/* 
    void updateStyle(AbstractButton b) {
//        SynthContext context = getContext(b, SynthConstants.ENABLED);
    	SynthContext context = SynthContext.getContext(b, style, SynthConstants.ENABLED);
        SynthStyle oldStyle = style;
        style = SynthLookAndFeel.updateStyle(context, this);
        if (style != oldStyle) {
            if (b.getMargin() == null ||
                                (b.getMargin() instanceof UIResource)) {
                Insets margin = (Insets)style.get(context,getPropertyPrefix() +
                                                  "margin");

                if (margin == null) {
                    // Some places assume margins are non-null.
                    margin = SynthLookAndFeel.EMPTY_UIRESOURCE_INSETS;
                }
                b.setMargin(margin);
            }

            Object value = style.get(context, getPropertyPrefix() + "iconTextGap");
            if (value != null) {
                        LookAndFeel.installProperty(b, "iconTextGap", value);
            }

            value = style.get(context, getPropertyPrefix() + "contentAreaFilled");
            LookAndFeel.installProperty(b, "contentAreaFilled",
                                        value != null? value : Boolean.TRUE);

            if (oldStyle != null) {
                uninstallKeyboardActions(b);
                installKeyboardActions(b);
            }

        }
    }
*/    
    protected void paint(SynthContext context, Graphics g) {
        SynthArrowButton button = (SynthArrowButton)context.
                                  getComponent();
        SynthPainter painter = SynthUtils.getPainter(context);
        painter.paintArrowButtonForeground(
            context, g, 0, 0, button.getWidth(), button.getHeight(),
            button.getDirection());
    }

    // cannot override because it is private SynthButtonUI.paintBackground 
    void paintBackground(SynthContext context, Graphics g, JComponent c) {
        SynthPainter painter = SynthUtils.getPainter(context);
        painter.paintArrowButtonBackground(context, g, 0, 0,
                                            c.getWidth(), c.getHeight());
    }

    public void paintBorder(SynthContext context, Graphics g, int x,
                            int y, int w, int h) {
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
            // ScrollBar arrow buttons can be non-square when
            // the ScrollBar.squareButtons property is set to FALSE
            // and the ScrollBar.buttonSize property is non-null
            dim = (Dimension)
                context.getStyle().get(context, "ScrollBar.buttonSize");
        }
        if (dim == null) {
            // For all other cases (including Spinner, ComboBox), we will
            // fall back on the single ArrowButton.size value to create
            // a square return value
            int size =
                context.getStyle().getInt(context, "ArrowButton.size", 16);
            dim = new Dimension(size, size);
        }

        // handle scaling for sizeVarients for special case components. The
        // key "JComponent.sizeVariant" scales for large/small/mini
        // components are based on Apples LAF
        Container parent = context.getComponent().getParent();
        if (parent instanceof JComponent && !(parent instanceof JComboBox)) {
            Object scaleKey = ((JComponent)parent).
                                getClientProperty("JComponent.sizeVariant");
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
