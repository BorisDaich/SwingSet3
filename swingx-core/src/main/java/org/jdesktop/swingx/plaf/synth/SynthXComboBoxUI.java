package org.jdesktop.swingx.plaf.synth;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import javax.swing.ComboBoxEditor;
import javax.swing.DefaultButtonModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.plaf.nimbus.NimbusStyle;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthUI;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.SwingXUtilities;
import org.jdesktop.swingx.plaf.basic.BasicXComboBoxUI;
import org.jdesktop.swingx.plaf.basic.BasicXComboPopup;

/*
symetrisch zu javax.swing.plaf.synth.SynthComboBoxUI, Ableitung:

                          abstract class ComponentUI
                                          |
 +----------------------> abstract class ComboBoxUI
abstract class XComboBoxUI                |
 |                                        |
BasicXComboBoxUI           symetrisch zu BasicComboBoxUI
 |                                        |
SynthXComboBoxUI           symetrisch zu SynthComboBoxUI
 */
public class SynthXComboBoxUI extends BasicXComboBoxUI implements PropertyChangeListener, SynthUI {

	private static final Logger LOG = Logger.getLogger(SynthXComboBoxUI.class.getName());

	// factory
    public static ComponentUI createUI(JComponent c) {
//    	LOG.info("UI factory for JComponent:"+c); // c of type JXComboBox expected
        return new SynthXComboBoxUI(c);
    }
    
    private SynthStyle style;
    private boolean useListColors;
    Insets popupInsets;
    private boolean buttonWhenNotEditable;
    private boolean pressedWhenPopupVisible;
    private ButtonHandler buttonHandler;
    private EditorFocusHandler editorFocusHandler; // inner Class
    private boolean forceOpaque = false;
    
    public SynthXComboBoxUI() {}

    // this ctor is implicit, used by factory
    protected SynthXComboBoxUI(JComponent c) {
    	super();
//    	LOG.info("---------->ctor< fertig:");
    }
    
    @Override
    public void installUI(JComponent c) {
        buttonHandler = new ButtonHandler();
        
        /* super.installUI calls
- installDefaults                           1.Install default property values
- comboBox.setLayout(createLayoutManager()) 2.Install a LayoutManager
- createPopup + configure listBox + ...     3.Create/add any required sub-components
- installListeners
         */
        super.installUI(c);
    }

    /**
     * this method is called at installUI() time.
     * 
     * @see javax.swing.plaf.synth.SynthListUI#installDefaults
     */
    @Override
    protected void installDefaults() {
    	LOG.info("####### comboBox:"+comboBox);
    	if (comboBox.getRenderer() == null || (comboBox.getRenderer() instanceof UIResource)) {
        	LOG.warning("####### comboBox.Renderer:"+comboBox.getRenderer()
        	+ "\n cannot instantiate inner SynthComboBoxRenderer"
        	);
    	}
//    	super.installDefaults();
/* results for Nimbus:
INFORMATION: LookAndFeelDefaults org.jdesktop.swingx.plaf.synth.SynthXComboBoxUI
 font javax.swing.plaf.FontUIResource[family=SansSerif,name=sansserif,style=plain,size=12]
 background javax.swing.plaf.ColorUIResource[r=238,g=238,b=238]                         --- überschrieben in updateStyle
 foreground DerivedColor(color=0,0,0 parent=text offsets=0.0,0.0,0.0,0 pColor=0,0,0
 border javax.swing.border.EtchedBorder@1101322d
 property opaque null
 selectionBackground javax.swing.plaf.ColorUIResource[r=57,g=105,b=138]
 selectionForeground javax.swing.plaf.ColorUIResource[r=255,g=255,b=255]
 disabledBackground null
 disabledForeground null
 property timeFactor null
 property squareButton false
 padding javax.swing.plaf.InsetsUIResource[top=3,left=3,bottom=3,right=3]
 */
//    	padding = null; keine Insets
    	// update colors border , font , background , foreground
    	updateStyle(comboBox);
    }
    
    private void updateStyle(JComboBox<?> comboBox) {
        SynthStyle oldStyle = style;
    	if(style==null) {
    		style = getStyle();
    	}
        SynthXContext context = (SynthXContext)SynthUtils.getContext(comboBox, getRegion(), style, ENABLED);
/*        SynthLookAndFeel.updateStyle is not visible:
        SynthLookAndFeel.update(context, g);
//    static void update(SynthContext state, Graphics g) {
//        paintRegion(state, g, null);
//    }
        context.getPainter().paintComboBoxBackground(context, g, 0, 0,
                                                  c.getWidth(), c.getHeight());
        paint(context, g);
 */
        LOG.warning("NOT paintRegion - NOT paint");
        style = SynthXContext.updateStyle(context, this);
        if (style != oldStyle) {
            padding = (Insets)style.get(context, "ComboBox.padding");
            popupInsets = (Insets)style.get(context, "ComboBox.popupInsets");
            useListColors = style.getBoolean(context, "ComboBox.rendererUseListColors", true);
            buttonWhenNotEditable = style.getBoolean(context, "ComboBox.buttonWhenNotEditable", false);
            pressedWhenPopupVisible = style.getBoolean(context, "ComboBox.pressedWhenPopupVisible", false);
            squareButton = style.getBoolean(context, "ComboBox.squareButton", true);

            if (oldStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
            forceOpaque = style.getBoolean(context, "ComboBox.forceOpaque", false);
        }

        if(listBox != null) {
            SynthLookAndFeel.updateStyles(listBox);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void installListeners() {
        comboBox.addPropertyChangeListener(this);
        comboBox.addMouseListener(buttonHandler);
        editorFocusHandler = new EditorFocusHandler(comboBox);
        super.installListeners();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstallUI(JComponent c) {
    	// class SynthComboPopup extends BasicComboPopup is not visible!
    	// TODO ???
//        if (popup instanceof SynthComboPopup) {
//            ((SynthComboPopup)popup).removePopupMenuListener(buttonHandler);
//        }
        super.uninstallUI(c);
        buttonHandler = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void uninstallDefaults() {
        SynthContext context = getContext(comboBox, ENABLED);

        style.uninstallDefaults(context);
        style = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void uninstallListeners() {
        editorFocusHandler.unregister();
        comboBox.removePropertyChangeListener(this);
        comboBox.removeMouseListener(buttonHandler);
        buttonHandler.pressed = false;
        buttonHandler.over = false;
        super.uninstallListeners();
    }

    /**
     * {@inheritDoc}
     */
	@Override // implements interface SynthUI
	public SynthContext getContext(JComponent c) {
        if (c != comboBox) throw new IllegalArgumentException("must be ui-delegate for component");
        return getContext(SynthUtils.getComponentState(c));
	}

	@Override // implements interface SynthUI
	public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
// in javax.swing.plaf.synth.SynthComboBoxUI#paintBorder paintComboBoxBorder
		SynthUtils.getPainter(context).paintComboBoxBorder(context, g, x, y, w, h);
//		SynthUtils.getPainter(context).paintListBorder(context, g, x, y, w, h);
	}

    private SynthXContext getContext(JComponent c, int state) {
        return SynthXContext.getContext(c, style, state);
    }
    private SynthContext getContext(int state) {
        return SynthUtils.getContext(comboBox, getRegion(), style, state);
    }

    /**
     * {@inheritDoc}
     */
//    @Override
//    protected ComboPopup createPopup() {
//        SynthComboPopup p = new SynthComboPopup(comboBox);
//        p.addPopupMenuListener(buttonHandler);
//        return p;
//    }
/*
in super:
protected ComboPopup createPopup() {
	return new BasicComboPopup(cb) { // mit Überschreibungen:
		protected JList<Object> createList() { ...
		protected void togglePopup() { ...
		public void show() { ...
		public void show(Component invoker, int x, int y) { ...
		protected void configureList() {
 */
    @Override
    protected ComboPopup createPopup() {
//		JXComboBox<?> xComboBox = (JXComboBox<?>)comboBox;
    	// public javax.swing.plaf.basic.BasicComboPopup( JComboBox<Object> combo ) ...
    	// protected JComboBox<?> comboBox
    	// ==> the cast is safe
    	@SuppressWarnings("unchecked")
		JComboBox<Object> cb = (JComboBox<Object>) comboBox;
    	// class SynthComboPopup extends BasicComboPopup is not visible
        //SynthComboPopup p = new SynthComboPopup(comboBox);
    	SynthXComboPopup p = new SynthXComboPopup(cb);
        p.addPopupMenuListener(buttonHandler);
        return p;
    }

//  classes SynthComboBoxRenderer, SynthComboBoxEditor not visible
//    @Override
//    protected ListCellRenderer<Object> createRenderer() {
//        return new SynthComboBoxRenderer();
//    }
//    @Override
//    protected ComboBoxEditor createEditor() {
//        return new SynthComboBoxEditor();
//    }

    //
    // end UI Initialization
    //======================

    /**
     * {@inheritDoc}
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (SynthUtils.shouldUpdateStyle(e)) {
            updateStyle(comboBox);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JButton createArrowButton() {
    	// not public : class javax.swing.plaf.synth.SynthArrowButton extends JButton implements SwingConstants, UIResource 
        SynthArrowButton button = new SynthArrowButton(SwingConstants.SOUTH);
        button.setName("ComboBox.arrowButton");
        button.setModel(buttonHandler);
        return button;
    }

    //=================================
    // begin ComponentUI Implementation

    @Override
    public void update(Graphics g, JComponent c) {
        SynthContext context = getContext(c);

//        SynthLookAndFeel.update(context, g); // not visible
//        SynthLookAndFeel.paintRegion(state, g, null);
        LOG.warning("cannot paint region");
        if(context instanceof SynthXContext) {
        	SynthXContext xcontext = (SynthXContext)context;
        	xcontext.getPainter().paintComboBoxBackground(xcontext, g, 0, 0, c.getWidth(), c.getHeight());
        }
        paint(context, g);
    }
    
    @Override
    public void paint(Graphics g, JComponent c) {
        SynthContext context = getContext(c);
        paint(context, g);
    }
    protected void paint(SynthContext context, Graphics g) {
        hasFocus = comboBox.hasFocus();
        if ( !comboBox.isEditable() ) {
            Rectangle r = rectangleForCurrentValue();
            paintCurrentValue(g,r,hasFocus);
        }
        // Empty out the renderer pane, allowing renderers to be gc'ed.
        currentValuePane.removeAll();
    }

    /**
     * Paints the currently selected item.
     */
    @Override
    public void paintCurrentValue(Graphics g,Rectangle bounds,boolean hasFocus) {
        ListCellRenderer<Object> renderer = (ListCellRenderer<Object>)comboBox.getRenderer();

        Component c;
        c = renderer.getListCellRendererComponent(
                listBox, comboBox.getSelectedItem(), -1, false, false);

        // Fix for 4238829: should lay out the JPanel.
        boolean shouldValidate = false;
        if (c instanceof JPanel)  {
            shouldValidate = true;
        }

        if (c instanceof UIResource) {
            c.setName("ComboBox.renderer");
        }

        boolean force = forceOpaque && c instanceof JComponent;
        if (force) {
            ((JComponent)c).setOpaque(false);
        }

        int x = bounds.x, y = bounds.y, w = bounds.width, h = bounds.height;
        if (padding != null) {
            x = bounds.x + padding.left;
            y = bounds.y + padding.top;
            w = bounds.width - (padding.left + padding.right);
            h = bounds.height - (padding.top + padding.bottom);
        }

        currentValuePane.paintComponent(g, c, comboBox, x, y, w, h, shouldValidate);

        if (force) {
            ((JComponent)c).setOpaque(true);
        }
    }

    private boolean shouldActLikeButton() {
        return buttonWhenNotEditable && !comboBox.isEditable();
    }

//    @Override
//    protected Dimension getDefaultSize() {
//        SynthComboBoxRenderer r = new SynthComboBoxRenderer();
//        Dimension d = getSizeForComponent(r.getListCellRendererComponent(listBox, " ", -1, false, false));
//        return new Dimension(d.width, d.height);
//    }

    @SuppressWarnings("serial") // Superclass is not serializable across versions
    class SynthXComboPopup extends BasicXComboPopup {
        public SynthXComboPopup( JComboBox<Object> combo ) {
            super(combo);
        }
        private JXComboBox xComboBox() {
        	return (JXComboBox)comboBox;
        }
		@Override
		protected JList<Object> createList() {
			// autoCreateRowSorter ist in JXComboBox zunächst false
//			LOG.info("nimbus nimbus" //+xComboBox.getRowSorter().getClass()
//					+ "\n isSorted="+xComboBox().isSorted()
//					);
			JXList<Object> list = new JXList<Object>(comboBox.getModel(), xComboBox().hasRowSorter());
			return list;
		}
	    @Override
	    public void show() {
	    	int i = xComboBox().getSelectedIndex();
//	    	LOG.info("SelectedIndex="+i + " isSorted="+xComboBox().isSorted());
	    	// in super.show() :
	    	// - ist abgehandelt der Fall : i == -1 ==> list.clearSelection()
	    	// - call show(Component invoker, int x, int y)
	    	super.show();
	    	if(i == -1 ) {
	    		// bereits abgehandelt
	    	} else {
	    		int selectedIndex = xComboBox().hasRowSorter() ? xComboBox().getRowSorter().convertRowIndexToView(i) : i;
    	    	list.setSelectedIndex( selectedIndex );
    	    	list.ensureIndexIsVisible( selectedIndex );
	    	}
	    }
	    public void show(Component invoker, int x, int y) {
//	    	LOG.info("// isVisible="+isVisible()+" x="+x+",y="+y+" aus JPopupMenu Component invoker:"+invoker);
	    	super.show(invoker, x, y);
//	    	LOG.info("// set popupVisible to isVisible()="+isVisible());
	    	popupVisible = isVisible(); // avoid rekusive call via setPopupVisible 
	    }
	    protected void configureList() {
	        list.setFont( comboBox.getFont() );
	        list.setCellRenderer( comboBox.getRenderer() );
	        list.setFocusable( false );
	        list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
//	        list.setSelectionBackground( new Color(57,105,138) ); // nimbusSelectionBackground OK, aber zu dunkel
	        list.setSelectionBackground( new Color(115, 164, 209) ); // "nimbusFocus", approx JORDY_BLUE
//	        list.setSelectionBackground( UIManager.getColor(SELECTION_BG) );
//	    	LOG.info("=====?funktioniert nicht?=====" + " getSelectionBackground="+xComboBox().getSelectionBackground());
//	        list.setSelectionBackground(xComboBox().getSelectionBackground());
	        
	        int i = comboBox.getSelectedIndex();
	        if(i == -1 ) {
	        	list.clearSelection();
	    	} else {
	    		int selectedIndex = xComboBox().hasRowSorter() ? xComboBox().getRowSorter().convertRowIndexToView(i) : i;
    	    	list.setSelectedIndex( selectedIndex );
    	    	list.ensureIndexIsVisible( selectedIndex );
	        }
	        installListListeners();
	    }
	    protected void installListListeners() {
	        if ((listMouseListener = createListMouseListener()) != null) {
	            list.addMouseListener( listMouseListener );
	        }
	        if ((listMouseMotionListener = createListMouseMotionListener()) != null) {
	            list.addMouseMotionListener( listMouseMotionListener );
	        }
	        if ((listSelectionListener = createListSelectionListener()) != null) {
	            list.addListSelectionListener( listSelectionListener );
	        }
//	        list.addPropertyChangeListener(evt -> {
//	    		LOG.info(">>>PropertyChangeEvent:"+evt);
////	    		LOG.info(">>>"+propertyName+" PropertyChangeEvent:"+evt);
//	        });
	    }
    }

/*    // copied from inner SynthComboBoxUI.SynthComboBoxRenderer
    @SuppressWarnings("serial") // Superclass is not serializable across versions
    private class SynthComboBoxRenderer extends JLabel implements ListCellRenderer<Object>, UIResource {
        public SynthComboBoxRenderer() {
            super();
            setText(" ");
        }

        @Override
        public String getName() {
            String name = super.getName();
            return name == null ? "ComboBox.renderer" : name;
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                         int index, boolean isSelected, boolean cellHasFocus) {
            setName("ComboBox.listRenderer");
//            SynthLookAndFeel.resetSelectedUI(); // not visible
//            final Object SELECTED_UI_KEY = new StringBuilder("selectedUI");
//            AppContext.getAppContext().remove(SELECTED_UI_KEY);

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
                if (!useListColors) {
                    SynthLookAndFeel.setSelectedUI(
                         (SynthLabelUI)SynthLookAndFeel.getUIOfType(getUI(),
                         SynthLabelUI.class), isSelected, cellHasFocus,
                         list.isEnabled(), false);
                }
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            setFont(list.getFont());

            if (value instanceof Icon) {
                setIcon((Icon)value);
                setText("");
            } else {
                String text = (value == null) ? " " : value.toString();

                if ("".equals(text)) {
                    text = " ";
                }
                setText(text);
            }

            if (comboBox != null){
                setEnabled(comboBox.isEnabled());
                setComponentOrientation(comboBox.getComponentOrientation());
            }

            return this;
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            SynthLookAndFeel.resetSelectedUI();
        }
    }
 */

    private Region getRegion() {
        return XRegion.getXRegion(comboBox, true);
    }

    private NimbusStyle getStyle() {
    	LOG.info("get style from the style factory \n for "+comboBox+" \n and Region:"+getRegion());
//        return SynthLookAndFeel.getStyle(comboBox, getRegion());
        // gleichwertig:
//        return SynthLookAndFeel.getStyleFactory().getStyle(comboBox, getRegion());
        // besser
        return NimbusLookAndFeel.getStyle(comboBox, getRegion());
    }
	
    protected void installSynthBorder() {
        if (SwingXUtilities.isUIInstallable(comboBox.getBorder())) {
        	Border border = new SynthBorder(this, style.getInsets(getContext(ENABLED), null));
        	LOG.info("property Border should be replaced by the UI's default value:"+border);
        	comboBox.setBorder(border);
        }
    }
    
    // copied from inner SynthComboBoxUI.ButtonHandler
    @SuppressWarnings("serial") // Superclass is not serializable across versions
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

    // copied from inner class SynthComboBoxUI.EditorFocusHandler
	private static class EditorFocusHandler implements FocusListener, PropertyChangeListener {
		private JComboBox<?> comboBox;
		private ComboBoxEditor editor = null;
		private Component editorComponent = null;

		private EditorFocusHandler(JComboBox<?> comboBox) {
			this.comboBox = comboBox;
			editor = comboBox.getEditor();
			if (editor != null) {
				editorComponent = editor.getEditorComponent();
				if (editorComponent != null) {
					editorComponent.addFocusListener(this);
				}
			}
			comboBox.addPropertyChangeListener("editor", this);
		}

		public void unregister() {
			comboBox.removePropertyChangeListener("editor", this);
			if (editorComponent != null) {
				editorComponent.removeFocusListener(this);
			}
		}

		/** Invoked when a component gains the keyboard focus. */
		public void focusGained(FocusEvent e) {
			// repaint whole combo on focus gain
			comboBox.repaint();
		}

		/** Invoked when a component loses the keyboard focus. */
		public void focusLost(FocusEvent e) {
			// repaint whole combo on focus loss
			comboBox.repaint();
		}

		/**
		 * Called when the combos editor changes
		 *
		 * @param evt A PropertyChangeEvent object describing the event source and the
		 *            property that has changed.
		 */
		public void propertyChange(PropertyChangeEvent evt) {
			ComboBoxEditor newEditor = comboBox.getEditor();
			if (editor != newEditor) {
				if (editorComponent != null) {
					editorComponent.removeFocusListener(this);
				}
				editor = newEditor;
				if (editor != null) {
					editorComponent = editor.getEditorComponent();
					if (editorComponent != null) {
						editorComponent.addFocusListener(this);
					}
				}
			}
		}
	}

}
