package org.jdesktop.swingx.plaf.synth;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import javax.swing.DefaultButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthUI;

import org.jdesktop.swingx.SwingXUtilities;
import org.jdesktop.swingx.plaf.basic.BasicXComboBoxUI;

public class SynthXComboBoxUI extends BasicXComboBoxUI implements PropertyChangeListener, SynthUI {

	private static final Logger LOG = Logger.getLogger(SynthXComboBoxUI.class.getName());

	// factory
    public static ComponentUI createUI(JComponent c) {
    	LOG.info("UI factory for JComponent:"+c); // c of type JXComboBox expected
        return new SynthXComboBoxUI(c);
    }
    
    private SynthStyle style;
    private boolean useListColors;
    private boolean useUIBorder;

    // this ctor is implicit, used by factory
    public SynthXComboBoxUI(JComponent c) {
    	super();
    	LOG.info("---------->ctor<:");
    }
    
    private ButtonHandler buttonHandler;
    @Override
    public void installUI(JComponent c) {
        buttonHandler = new ButtonHandler();
        super.installUI(c);
    }

    @Override
    protected void installDefaults() {
		LOG.info("LookAndFeelDefaults "+UIManager.get(comboBox.getUIClassID())
		+ "\n font "+UIManager.getLookAndFeelDefaults().get(FONT)
		+ "\n background "+UIManager.getLookAndFeelDefaults().get(BACKGROUND)
		+ "\n foreground "+UIManager.getLookAndFeelDefaults().get(FOREGROUND)
		+ "\n border "+UIManager.getLookAndFeelDefaults().get(BORDER)
		+ "\n property opaque "+UIManager.getLookAndFeelDefaults().get("opaque")
		+ "\n selectionBackground "+UIManager.getLookAndFeelDefaults().get(SELECTION_BG)
		+ "\n selectionForeground "+UIManager.getLookAndFeelDefaults().get(SELECTION_FG)
		+ "\n disabledBackground "+UIManager.getLookAndFeelDefaults().get(DISABLED_BG)
		+ "\n disabledForeground "+UIManager.getLookAndFeelDefaults().get(DISABLED_FG)
		+ "\n property timeFactor "+UIManager.getLookAndFeelDefaults().get(TIME_FACTOR)
		+ "\n property squareButton "+UIManager.getLookAndFeelDefaults().get(SQUARE_BUTTON)
		+ "\n padding "+UIManager.getLookAndFeelDefaults().get(PADDING));
        updateStyle(comboBox);
    }

	@Override // implements interface SynthUI
	public SynthContext getContext(JComponent c) {
        if (c != comboBox) throw new IllegalArgumentException("must be ui-delegate for component");
        return getContext(SynthUtils.getComponentState(c));
	}

    private SynthContext getContext(int state) {
        return SynthUtils.getContext(comboBox, getRegion(), style, state);
    }

    private Region getRegion() {
        return XRegion.getXRegion(comboBox, true);
    }
    
    private SynthStyle getStyle() {
    	LOG.info("get style from the style factory \n for "+comboBox+" \n and Region:"+getRegion());
        return SynthLookAndFeel.getStyleFactory().getStyle(comboBox, getRegion());
    }
	
    protected void installSynthBorder() {
        if (SwingXUtilities.isUIInstallable(comboBox.getBorder())) {
        	comboBox.setBorder(new SynthBorder(this, style.getInsets(getContext(ENABLED), null)));
        }
    }

	@Override // implements interface SynthUI
	public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        SynthUtils.getPainter(context).paintListBorder(context, g, x, y, w, h);
	}

	@Override // implement PropertyChangeListener#propertyChange AND SynthUI#propertyChange
	public void propertyChange(PropertyChangeEvent evt) {
//		LOG.info("PropertyChangeEvent:"+evt);
        if (SynthUtils.shouldUpdateStyle(evt)) {
            updateStyle(this.comboBox);
        }	
	}

    private void updateStyle(JComponent c) {
        // compare local reference to style from factory - nothing to do if same
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
        // install defaults : font , background , foreground
        style.installDefaults(getContext(ENABLED));
        // install selected properties
        SynthContext selectedContext = getContext(SELECTED);
//        ----------------------------
		Color fg = comboBox.getForeground();
		LOG.info("--------"+(fg instanceof UIResource)+"---------- comboBox.fg:"+fg); 
		Color bg = comboBox.getBackground();
		LOG.info("--------"+(bg instanceof UIResource)+"---------- comboBox.bg:"+bg); 
		// DerivedColor(color=214,217,223 parent=control offsets=0.0,0.0,0.0,0 pColor=214,217,223
        if (bg == null || bg instanceof UIResource) {
        	comboBox.setBackground(UIManager.getColor("ComboBox.background")); // "control" #d6d9df (214,217,223)
//    		LOG.info("--------"+(bg instanceof UIResource)+"---------- bg:"+bg); 
        }
//      ----------------------------
//        Color sbg = comboBox.getSelectionBackground();
//        if (sbg == null || sbg instanceof UIResource) {
//        	comboBox.setSelectionBackground(style.getColor(selectedContext, ColorType.TEXT_BACKGROUND));
//        }
//        
//        Color sfg = list.getSelectionForeground();
//        if (sfg == null || sfg instanceof UIResource) {
//            list.setSelectionForeground(style.getColor(selectedContext, ColorType.TEXT_FOREGROUND));
//        }
//        // install cell height
//        int height = style.getInt(selectedContext, "List.cellHeight", -1);
//        if (height != -1) {
//            list.setFixedCellHeight(height);
//        }
        // we do this because ... ??
        if (refresh) {
//            uninstallKeyboardActions(); // in Basic*UI
//            installKeyboardActions();
        }
        // install currently unused properties of this delegate
        useListColors = style.getBoolean(selectedContext, "ComboBox.rendererUseListColors", true);
//        useUIBorder = style.getBoolean(selectedContext, "List.rendererUseUIBorder", true);
        
    }

    @Override
    protected void installListeners() {
        comboBox.addPropertyChangeListener(this); // ==> propertyChange
        comboBox.addMouseListener(buttonHandler);
//        editorFocusHandler = new EditorFocusHandler(comboBox); TODO
        super.installListeners();
    }

    @Override
    protected JButton createArrowButton() {
    	// not public : class javax.swing.plaf.synth.SynthArrowButton extends JButton implements SwingConstants, UIResource 
        SynthArrowButton button = new SynthArrowButton(SwingConstants.SOUTH);
        button.setName("ComboBox.arrowButton");
        button.setModel(buttonHandler);
        return button;
    }
    class SynthArrowButton extends JButton implements SwingConstants, UIResource {
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

//        private static class SynthArrowButtonUI extends SynthButtonUI {
//            protected void installDefaults(AbstractButton b) {
//                super.installDefaults(b);
////                updateStyle(b); // updateStyle in SynthButtonUI is not visible TODO
///*
//    void updateStyle(AbstractButton b) {
//        SynthContext context = getContext(b, SynthConstants.ENABLED);
//        SynthStyle oldStyle = style;
//        style = SynthLookAndFeel.updateStyle(context, this);
//        if (style != oldStyle) {
//            if (b.getMargin() == null ||
//                                (b.getMargin() instanceof UIResource)) {
//                Insets margin = (Insets)style.get(context,getPropertyPrefix() +
//                                                  "margin");
//
//                if (margin == null) {
//                    // Some places assume margins are non-null.
//                    margin = SynthLookAndFeel.EMPTY_UIRESOURCE_INSETS;
//                }
//                b.setMargin(margin);
//            }
//
//            Object value = style.get(context, getPropertyPrefix() + "iconTextGap");
//            if (value != null) {
//                        LookAndFeel.installProperty(b, "iconTextGap", value);
//            }
//
//            value = style.get(context, getPropertyPrefix() + "contentAreaFilled");
//            LookAndFeel.installProperty(b, "contentAreaFilled",
//                                        value != null? value : Boolean.TRUE);
//
//            if (oldStyle != null) {
//                uninstallKeyboardActions(b);
//                installKeyboardActions(b);
//            }
//
//        }
//    }
//
// */
//            }
//
//            protected void paint(SynthContext context, Graphics g) {
//                SynthArrowButton button = (SynthArrowButton)context.
//                                          getComponent();
//                SynthPainter painter = SynthUtils.getPainter(context);
//                painter.paintArrowButtonForeground(
//                    context, g, 0, 0, button.getWidth(), button.getHeight(),
//                    button.getDirection());
//            }
//
//            // cannot override because it is private SynthButtonUI.paintBackground 
//            void paintBackground(SynthContext context, Graphics g, JComponent c) {
//                SynthPainter painter = SynthUtils.getPainter(context);
//                painter.paintArrowButtonBackground(context, g, 0, 0,
//                                                    c.getWidth(), c.getHeight());
//            }
//
//            public void paintBorder(SynthContext context, Graphics g, int x,
//                                    int y, int w, int h) {
//                SynthPainter painter = SynthUtils.getPainter(context);
//                painter.paintArrowButtonBorder(context, g, x, y, w,h);
//            }
//
//            public Dimension getMinimumSize() {
//                return new Dimension(5, 5);
//            }
//
//            public Dimension getMaximumSize() {
//                return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
//            }
//
//            public Dimension getPreferredSize(JComponent c) {
//                SynthContext context = getContext(c);
//                Dimension dim = null;
//                if (context.getComponent().getName() == "ScrollBar.button") {
//                    // ScrollBar arrow buttons can be non-square when
//                    // the ScrollBar.squareButtons property is set to FALSE
//                    // and the ScrollBar.buttonSize property is non-null
//                    dim = (Dimension)
//                        context.getStyle().get(context, "ScrollBar.buttonSize");
//                }
//                if (dim == null) {
//                    // For all other cases (including Spinner, ComboBox), we will
//                    // fall back on the single ArrowButton.size value to create
//                    // a square return value
//                    int size =
//                        context.getStyle().getInt(context, "ArrowButton.size", 16);
//                    dim = new Dimension(size, size);
//                }
//
//                // handle scaling for sizeVarients for special case components. The
//                // key "JComponent.sizeVariant" scales for large/small/mini
//                // components are based on Apples LAF
//                Container parent = context.getComponent().getParent();
//                if (parent instanceof JComponent && !(parent instanceof JComboBox)) {
//                    Object scaleKey = ((JComponent)parent).
//                                        getClientProperty("JComponent.sizeVariant");
//                    if (scaleKey != null){
//                        if ("large".equals(scaleKey)){
//                            dim = new Dimension(
//                                    (int)(dim.width * 1.15),
//                                    (int)(dim.height * 1.15));
//                        } else if ("small".equals(scaleKey)){
//                            dim = new Dimension(
//                                    (int)(dim.width * 0.857),
//                                    (int)(dim.height * 0.857));
//                        } else if ("mini".equals(scaleKey)){
//                            dim = new Dimension(
//                                    (int)(dim.width * 0.714),
//                                    (int)(dim.height * 0.714));
//                        }
//                    }
//                }
//
//                return dim;
//            }
//        }

    }
    private boolean pressedWhenPopupVisible;
    private boolean buttonWhenNotEditable;
    private boolean shouldActLikeButton() {
        return buttonWhenNotEditable && !comboBox.isEditable();
    }

    /*
     * 
     */
    private final class ButtonHandler extends DefaultButtonModel implements MouseListener, PopupMenuListener {
        private boolean over;
        private boolean pressed;
        private void updatePressed(boolean p) {
            this.pressed = p && isEnabled();
            if (shouldActLikeButton()) {
                comboBox.repaint();
            }
        }
        private void updateOver(boolean o) {
            boolean old = isRollover();
            this.over = o && isEnabled();
            boolean newo = isRollover();
            if (shouldActLikeButton() && old != newo) {
                comboBox.repaint();
            }
        }

        // DefaultButtonModel Methods
        @Override
        public boolean isPressed() {
            boolean b = shouldActLikeButton() ? pressed : super.isPressed();
            return b || (pressedWhenPopupVisible && comboBox.isPopupVisible());
        }
        @Override
        public boolean isArmed() {
            boolean b = shouldActLikeButton() || (pressedWhenPopupVisible && comboBox.isPopupVisible());
            return b ? isPressed() : super.isArmed();
        }
        @Override
        public boolean isRollover() {
            return shouldActLikeButton() ? over : super.isRollover();
        }
        @Override
        public void setPressed(boolean b) {
            super.setPressed(b);
            updatePressed(b);
        }
        @Override
        public void setRollover(boolean b) {
            super.setRollover(b);
            updateOver(b);
        }

        // MouseListener
		@Override
		public void mouseClicked(java.awt.event.MouseEvent e) {}
		@Override
		public void mousePressed(java.awt.event.MouseEvent e) {
            updatePressed(true);
		}
		@Override
		public void mouseReleased(java.awt.event.MouseEvent e) {
            updatePressed(false);
		}
		@Override
		public void mouseEntered(java.awt.event.MouseEvent e) {
            updateOver(true);
		}
		@Override
		public void mouseExited(java.awt.event.MouseEvent e) {
            updateOver(false);
		}

        // PopupMenuListener
		@Override
		public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
		@Override
		public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
		@Override
		public void popupMenuCanceled(PopupMenuEvent e) {
            if (shouldActLikeButton() || pressedWhenPopupVisible) {
                comboBox.repaint();
            }
		}

    }

}
