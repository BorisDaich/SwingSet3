package org.jdesktop.swingx.plaf.synth;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthUI;

import org.jdesktop.swingx.SwingXUtilities;
import org.jdesktop.swingx.plaf.basic.BasicYListUI;

public class SynthYListUI extends BasicYListUI implements PropertyChangeListener, SynthUI {

	private static final Logger LOG = Logger.getLogger(SynthYListUI.class.getName());

	// factory
    public static ComponentUI createUI(JComponent c) {
    	LOG.info("UI factory for JComponent:"+c);
        return new SynthYListUI(c);
    }

    private SynthStyle style;
    private boolean useListColors;
    private boolean useUIBorder;

    public SynthYListUI(JComponent c) {
    	super(c);
    }

	@Override // implement SynthUI#paintBorder
	public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        SynthUtils.getPainter(context).paintListBorder(context, g, x, y, w, h);
	}

	@Override // implement PropertyChangeListener#propertyChange AND SynthUI#propertyChange
	public void propertyChange(PropertyChangeEvent evt) {
		LOG.info("evt:"+evt);
        if (SynthUtils.shouldUpdateStyle(evt)) {
            updateStyle(this.list);
        }	
	}

    /**
     * {@inheritDoc}
     */
    @Override // overrides to LOG
    public void installUI(JComponent c) {
//    	LOG.info("================---------->JComponent:"+c);
    	super.installUI(c);
//        installDefaults();
//        installListeners();
//        installKeyboardActions();
    }

    /**
     * this method is called at installUI() time.
     * 
     * @see javax.swing.plaf.synth.SynthListUI#installDefaults
     */
    @Override
    protected void installDefaults() {
    	LOG.info("####### list:"+list);
//    	if(list instanceof JYList ylist) {
//    		LOG.info("?????CellRenderer:"+ylist.getCellRenderer());
//    	}
    	if (list.getCellRenderer() == null || (list.getCellRenderer() instanceof UIResource)) {
    		LOG.info("CellRenderer:"+list.getCellRenderer()+" --- list.setCellRenderer(new SynthListCellRenderer()); // not visible");
    		// private inner class javax.swing.plaf.synth.SynthListUI.SynthListCellRenderer
    		list.setCellRenderer(new SynthListCellRenderer());
    	}
    	updateStyle(list);
    }

    /**
     * this method is called at installUI() time.
     * 
     * @see javax.swing.plaf.synth.SynthListUI#installListeners
     */
    @Override
    protected void installListeners() {
        super.installListeners();
        list.addPropertyChangeListener(this); // see propertyChange method
    }

    /**
     * this method is called at installUI() time.
     * 
     * @see javax.swing.plaf.basic.BasicListUI#installKeyboardActions
     */
    @Override
    protected void installKeyboardActions() {
        super.installKeyboardActions();    	
    }
    
    /**
     * Returns the style for this component from the style factory.
     * @return
     */
    private SynthStyle getStyle() {
    	LOG.info("get style from the style factory \n for "+list+" \n and Region:"+getRegion());
        return SynthLookAndFeel.getStyleFactory().getStyle(list, getRegion());
    }

    /**
     * Installs a SynthBorder from the current style, if ui-installable.
     */
    protected void installSynthBorder() {
        if (SwingXUtilities.isUIInstallable(list.getBorder())) {
            list.setBorder(new SynthBorder(this, style.getInsets(getContext(ENABLED), null)));
        }
    }

    private void updateStyle(JComponent c) {
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
//        ----------------------------
		Color bg = list.getBackground();
		LOG.info("--------"+(bg instanceof UIResource)+"---------- bg:"+bg); // pColor=255,255,255
        if (bg == null || bg instanceof UIResource) {
        	// replace default white bg with property color:
//            list.setBackground(style.getColor(selectedContext, ColorType.BACKGROUND));
            list.setBackground(UIManager.getColor("List.background"));
        }
//      ----------------------------
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
        
/* original code:
        SynthContext context = getContext(list, ENABLED); // method getContext is not visible
        SynthStyle oldStyle = style;

        style = SynthLookAndFeel.updateStyle(context, this); // method updateStyle is not visible

        if (style != oldStyle) {
            context.setComponentState(SELECTED); // method setComponentState is not visible
            Color sbg = list.getSelectionBackground();
            if (sbg == null || sbg instanceof UIResource) {
                list.setSelectionBackground(style.getColor(context, ColorType.TEXT_BACKGROUND));
            }

            Color sfg = list.getSelectionForeground();
            if (sfg == null || sfg instanceof UIResource) {
                list.setSelectionForeground(style.getColor(context, ColorType.TEXT_FOREGROUND));
            }

            useListColors = style.getBoolean(context, "List.rendererUseListColors", true);
            useUIBorder = style.getBoolean(context, "List.rendererUseUIBorder", true);

            int height = style.getInt(context, "List.cellHeight", -1);
            if (height != -1) {
                list.setFixedCellHeight(height);
            }
            if (oldStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
 */
    }

    @Override
    public SynthContext getContext(JComponent c) {
        if (c != list) throw new IllegalArgumentException("must be ui-delegate for component");
        return getContext(SynthUtils.getComponentState(list));
    }

    private SynthContext getContext(int state) {
        return SynthUtils.getContext(list, getRegion(), style, state);
    }

    private Region getRegion() {
        return YRegion.getYRegion(list, true);
    }

    /**
     * The {@code DefaultListCellRenderer} installed by the UI.
     */
    @SuppressWarnings("serial") // Superclass is not serializable across versions
    class SynthListCellRenderer extends DefaultListCellRenderer.UIResource {
    	
    	public SynthListCellRenderer() {
    		super();
    		LOG.info("ctor done");
    	}
        @Override 
        public String getName() {
            return "List.cellRenderer";
        }

        @Override 
        public void setBorder(Border b) {
            if (useUIBorder || b instanceof SynthBorder) {
                super.setBorder(b);
            }
        }

        @Override 
        public Component getListCellRendererComponent(JList<?> list, Object value,
                  int index, boolean isSelected, boolean cellHasFocus) {
            if (!useListColors && (isSelected || cellHasFocus)) {
            	LOG.info("then...");
//                SynthLookAndFeel.setSelectedUI((SynthLabelUI)SynthLookAndFeel.
//                             getUIOfType(getUI(), SynthLabelUI.class),
//                                   isSelected, cellHasFocus, list.isEnabled(), false);
            }
            else {
//                SynthLookAndFeel.resetSelectedUI(); // not visible
            	LOG.info("else...");
            }

            super.getListCellRendererComponent(list, value, index,
                                               isSelected, cellHasFocus);
            return this;
        }

        @Override public void paint(Graphics g) {
            super.paint(g);
//            SynthLookAndFeel.resetSelectedUI(); // not visible
        }
    }

}
