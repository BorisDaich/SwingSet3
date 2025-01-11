package org.jdesktop.swingx.plaf.synth;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import javax.swing.DefaultButtonModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.synth.ColorType;
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

public class SynthXComboBoxUI extends BasicXComboBoxUI implements PropertyChangeListener, SynthUI {

	private static final Logger LOG = Logger.getLogger(SynthXComboBoxUI.class.getName());

	// factory
    public static ComponentUI createUI(JComponent c) {
//    	LOG.info("UI factory for JComponent:"+c); // c of type JXComboBox expected
        return new SynthXComboBoxUI(c);
    }
    
    private SynthStyle style;
    private boolean useListColors;
    private boolean useUIBorder;

    // this ctor is implicit, used by factory
    protected SynthXComboBoxUI(JComponent c) {
    	super();
//    	LOG.info("---------->ctor< fertig:");
    }
    
    private ButtonHandler buttonHandler;
    @Override
    public void installUI(JComponent c) {
        buttonHandler = new ButtonHandler();
        
        /* super.installUI calls
         * - installDefaults
         * - comboBox.setLayout(createLayoutManager()); 
         * - createPopup + configure listBox + installComponents
         * - installListeners
         */
        super.installUI(c);
    }

    @Override
    protected void installDefaults() {
    	super.installDefaults();
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
    
    /*
     * LayoutManager is implemented in superclass inner BasicXComboBoxUI.Handler
     */
    protected LayoutManager createLayoutManager() {
        return super.createLayoutManager();
    }   

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
		JXComboBox<?> xComboBox = (JXComboBox<?>)comboBox;
    	// public javax.swing.plaf.basic.BasicComboPopup( JComboBox<Object> combo ) ...
    	// protected JComboBox<?> comboBox
    	// ==> the cast is safe
    	@SuppressWarnings("unchecked")
		JComboBox<Object> cb = (JComboBox<Object>) comboBox;
    	// class SynthComboPopup extends BasicComboPopup is not visible
//        SynthComboPopup p = new SynthComboPopup(comboBox);
    	SynthXComboPopup p = new SynthXComboPopup(cb);
        p.addPopupMenuListener(buttonHandler);
        return p;
    }
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

    private void updateStyle(JComponent c) {
        // compare local reference to style from factory - nothing to do if same
    	SynthStyle oldStyle = style;
    	style = getStyle(); // style from factory
    	if(oldStyle==style) return;
    	
        // check if this is called from init or from later update
        // if from later updates, need to cleanup old
        boolean refresh = oldStyle != null;
        if (refresh) {
//        	LOG.info("--------------refresh style--");
        	oldStyle.uninstallDefaults(getContext(ENABLED));
        }
    	
        // special case border
        installSynthBorder();
        // install defaults : font , background , foreground
        style.installDefaults(getContext(ENABLED));
//        ----------------------------
		Color fg = comboBox.getForeground();
//		LOG.info("--------"+(fg instanceof UIResource)+"---------- comboBox.fg:"+fg); 
		Color bg = comboBox.getBackground();
//		LOG.info("--------"+(bg instanceof UIResource)+"---------- comboBox.bg:"+bg); 
		// DerivedColor(color=214,217,223 parent=control offsets=0.0,0.0,0.0,0 pColor=214,217,223
        if (bg == null || bg instanceof UIResource) {
//        	comboBox.setBackground(UIManager.getColor("ComboBox.background")); // "control" #d6d9df (214,217,223)
//    		LOG.info("--------"+(bg instanceof UIResource)+"---------- bg:"+bg); 
        }
//      ----------------------------
        // install selected properties
        SynthContext selectedContext = getContext(SELECTED);
//        LOG.info("--------"+style.getColor(selectedContext, ColorType.TEXT_BACKGROUND));
        Color sbg = ((JXComboBox)comboBox).getSelectionBackground();
        if (sbg == null || sbg instanceof UIResource) {
//            LOG.info("--setSelectionBackground to "+style.getColor(selectedContext, ColorType.TEXT_BACKGROUND));
        	((JXComboBox)comboBox).setSelectionBackground(style.getColor(selectedContext, ColorType.TEXT_BACKGROUND));
        }
        
        Color sfg = ((JXComboBox)comboBox).getSelectionForeground();
        if (sfg == null || sfg instanceof UIResource) {
//            LOG.info("--setSelectionForeground to "+style.getColor(selectedContext, ColorType.TEXT_FOREGROUND));
        	((JXComboBox)comboBox).setSelectionForeground(style.getColor(selectedContext, ColorType.TEXT_FOREGROUND));
        }
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
//        LOG.info("--useListColors="+useListColors);
//        useUIBorder = style.getBoolean(selectedContext, "List.rendererUseUIBorder", true);
        
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
//    	LOG.info("get style from the style factory \n for "+comboBox+" \n and Region:"+getRegion());
        return SynthLookAndFeel.getStyleFactory().getStyle(comboBox, getRegion());
    }
	
    protected void installSynthBorder() {
        if (SwingXUtilities.isUIInstallable(comboBox.getBorder())) {
        	Border border = new SynthBorder(this, style.getInsets(getContext(ENABLED), null));
        	LOG.info("property Border should be replaced by the UI's default value:"+border);
        	comboBox.setBorder(border);
        }
    }

	@Override // implements interface SynthUI
	public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h) {
        SynthUtils.getPainter(context).paintListBorder(context, g, x, y, w, h);
	}

	@Override // implement PropertyChangeListener#propertyChange AND SynthUI#propertyChange
	public void propertyChange(PropertyChangeEvent evt) {
//        String propertyName = evt.getPropertyName();
//		LOG.info(">>>"+propertyName+" PropertyChangeEvent:"+evt);
        if (SynthUtils.shouldUpdateStyle(evt)) {
            updateStyle(this.comboBox);
        }	
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
