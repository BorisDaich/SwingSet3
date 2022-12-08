/*
 * Copyright 2009 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package org.jdesktop.swingx.plaf.synth;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthUI;

import org.jdesktop.swingx.SwingXUtilities;
import org.jdesktop.swingx.plaf.basic.core.BasicXListUI;

/**
 * Extends BasicXListUI for synth,
 * copied from javax.swing.plaf.synth.SynthListUI, which extends BasicListUI
 * 
 * @author Jeanette Winzenburg
 */
public class SynthXListUI extends BasicXListUI implements PropertyChangeListener, SynthUI {

    private SynthStyle style;
//    @SuppressWarnings("unused")
    private boolean useListColors;
//    @SuppressWarnings("unused")
    private boolean useUIBorder;

	private static final Logger LOG = Logger.getLogger(SynthXListUI.class.getName());

    public SynthXListUI() {
    	LOG.info("------------------ ctor");
    }

    /**
     * Factory: Returns a new instance of SynthXListUI.  
     * SynthXListUI delegates are allocated one per JList.
     *
     * @return A new ListUI implementation for the Synth look and feel.
     */
    public static ComponentUI createUI(JComponent list) {
        return new SynthXListUI();
    }

    /**
     * {@inheritDoc} <p>
     * Overridden to fill background, Synth-style.
     */
    @Override
    public void update(Graphics g, JComponent c) {
/* in ComponentUI:
        if (c.isOpaque()) {
            g.setColor(c.getBackground());
            g.fillRect(0, 0, c.getWidth(),c.getHeight());
        }
        paint(g, c);
   ------------------------------
original javax.swing.plaf.synth.SynthListUI:
        SynthContext context = getContext(c);

        SynthLookAndFeel.update(context, g);
        context.getPainter().paintListBackground(context,
                          g, 0, 0, c.getWidth(), c.getHeight());
        paint(g, c);

 */
        SynthContext context = getContext(c);
        SynthStyle style = context.getStyle();
        LOG.info("???? c.isOpaque()="+c.isOpaque() + "style color BACKGROUND:"+style.getColor(context, ColorType.BACKGROUND));
        // painting of the background :
        // TODO tut nicht
        SynthUtils.update(context, g);
        paintBorder(context, g, 0, 0, c.getWidth(), c.getHeight());
        paint(g, c);
    }

    /**
     * Paints border with the context's style's painter.
     * Implemented for SynthUI interface.
     */
    @Override
    public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        SynthUtils.getPainter(context).paintListBorder(context, g, x, y, w, h);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void installListeners() {
        super.installListeners();
        list.addPropertyChangeListener(this);
    }

	@Override // implements PropertyChangeListener
	public void propertyChange(PropertyChangeEvent evt) {
/*
original javax.swing.plaf.synth.SynthListUI:
        if (SynthLookAndFeel.shouldUpdateStyle(e)) { // method not visible
            updateStyle((JList)e.getSource());
        }
 */
        if (SynthUtils.shouldUpdateStyle(evt)) {
            updateStyle();
        }
	}


    /**
     * {@inheritDoc} <p>
     * Overridden to update style if appropriate.
     */
    @Override
    protected PropertyChangeListener createPropertyChangeListener() {
        PropertyChangeListener l = new PropertyChangeHandler() {

            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (SynthUtils.shouldUpdateStyle(e)) {
                    updateStyle();
                }
                super.propertyChange(e);
            }
            
        };
        return l;
    }

    /**
     * {@inheritDoc} <p>
     * Overridden to install properties, Synth-style.
     */
    @Override
    protected void installDefaults() {
        // never happens - the delegate renderer is always not-null and
        // not a ui-resource
//        if (list.getCellRenderer() == null ||
//                 (list.getCellRenderer() instanceof UIResource)) {
//            list.setCellRenderer(new SynthListCellRenderer());
//        }
    	
        updateStyle(); // wird updateStyle nach hinten verschonen, bleiben die Zellen weiss!!!
        
		// TODO damit werden ABER nur die Zellen bemalt
		Color bg = list.getBackground();
		if (bg == null || bg instanceof UIResource) {
			LOG.info("reset List.background:" + bg + " with " + UIManager.getColor("List.background"));
			list.setBackground(UIManager.getColor("List.background"));
//          list.repaint(); // nicht notwendig - wird in setBackground gemacht
		}

    }

    private void updateStyle() {
        // compare local reference to style from factory
        // nothing to do if same
        if (style == getStyle()) return;
        // check if this is called from init or from later update
        // if from later updates, need to cleanup old
        boolean refresh = style != null;
        if (refresh) {
        	LOG.info("--------------refresh style--");
            style.uninstallDefaults(getContext(ENABLED));
        }
        // update local reference
        style = getStyle();
        // special case border
        installSynthBorder();
        // install defaults 
        style.installDefaults(getContext(ENABLED));
        // install selected properties
        SynthContext selectedContext = getContext(SELECTED);
        Color sbg = list.getSelectionBackground();
        if (sbg == null || sbg instanceof UIResource) {
            list.setSelectionBackground(style.getColor(selectedContext, ColorType.TEXT_BACKGROUND));
        }
        
        Color sfg = list.getSelectionForeground();
        if (sfg == null || sfg instanceof UIResource) {
            list.setSelectionForeground(style.getColor(selectedContext, ColorType.TEXT_FOREGROUND));
        }
        // install cell height
        int height = style.getInt(selectedContext, "List.cellHeight", -1);
        if (height != -1) {
            list.setFixedCellHeight(height);
        }
        // we do this because ... ??
        if (refresh) {
            uninstallKeyboardActions();
            installKeyboardActions();
        }
        // install currently unused properties of this delegate
        useListColors = style.getBoolean(selectedContext, "List.rendererUseListColors", true);
        useUIBorder = style.getBoolean(selectedContext, "List.rendererUseUIBorder", true);
        
    }

    /**
     * Installs a SynthBorder from the current style, if ui-installable.
     */
    protected void installSynthBorder() {
        if (SwingXUtilities.isUIInstallable(list.getBorder())) {
            list.setBorder(new SynthBorder(this, style.getInsets(getContext(ENABLED), null)));
        }
    }

    /**
     * {@inheritDoc} <p>
     * Overridden to uninstall properties, Synth-style, after calling super.
     */
    @Override
    protected void uninstallDefaults() {
        super.uninstallDefaults();
        style.uninstallDefaults(getContext(ENABLED));
        style = null;
    }

    
    /**
     * {@inheritDoc} <p>
     * 
     * Returns a context for the component's current state.
     * Implemented for SynthUI interface. <p>
     * 
     * PENDING JW: not entirely sure if allowed ... but need to replace SynthUI anyway?.
     * 
     * @throws IllegalArgumentException if the component is not controlled by this 
     *    delegate
     */
    @Override
    public SynthContext getContext(JComponent c) {
        if (c != list) throw new IllegalArgumentException("must be ui-delegate for component");
        return getContext(SynthUtils.getComponentState(list));
    }

    /**
     * Returns a Context with the given component state.
     * 
     * @param state
     * @return
     */
    private SynthContext getContext(int state) {
        return SynthUtils.getContext(list, getRegion(), style, state);
    }
    
    private Region getRegion() {
        return XRegion.getXRegion(list, true);
    }


    /**
     * Returns the style for this component from the style factory.
     * @return
     */
    private SynthStyle getStyle() {
    	LOG.info("get style from the style factory for "+list);
        return SynthLookAndFeel.getStyleFactory().getStyle(list, getRegion());
    }

//    private class SynthListCellRenderer extends DefaultListCellRenderer.UIResource {
//        public String getName() {
//            return "List.cellRenderer";
//        }
//
//        public void setBorder(Border b) {
//            if (useUIBorder || b instanceof SynthBorder) {
//                super.setBorder(b);
//            }
//        }
//
//        public Component getListCellRendererComponent(JList list, Object value,
//                  int index, boolean isSelected, boolean cellHasFocus) {
//            if (!useListColors && (isSelected || cellHasFocus)) {
//                SynthLookAndFeel.setSelectedUI((SynthLabelUI)SynthLookAndFeel.
//                             getUIOfType(getUI(), SynthLabelUI.class),
//                                   isSelected, cellHasFocus, list.isEnabled(), false);
//            }
//            else {
//                SynthLookAndFeel.resetSelectedUI();
//            }
//            
//            super.getListCellRendererComponent(list, value, index,
//                                               isSelected, cellHasFocus);
//            return this;
//        }
//
//        public void paint(Graphics g) {
//            super.paint(g);
//            SynthLookAndFeel.resetSelectedUI();
//        }
//    }


}
