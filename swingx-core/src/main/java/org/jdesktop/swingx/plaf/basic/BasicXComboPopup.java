package org.jdesktop.swingx.plaf.basic;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboPopup;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXList;

@SuppressWarnings("serial") // Same-version serialization only
// BasicComboPopup implements ComboPopup
public class BasicXComboPopup extends BasicComboPopup {

	/* XXX
	 * wo soll diese prop definiert sein?
	 * hier. Dann müssen in BasicXComboBoxUI einige Methoden umgeschrieben werden
    public abstract void setPopupVisible( JComboBox<?> c, boolean v );
    public abstract boolean isPopupVisible( JComboBox<?> c );
	 */
    protected boolean popupVisible = false; // wg. BUG #57
    
//    public BasicXComboPopup(JXComboBox<Object> combo) {
//    	super(combo);
//    }
    /**
     * Constructs a new instance of {@code BasicXComboPopup}.
     *
     * @param combo an instance of {@code JComboBox}
     */
    public BasicXComboPopup(JComboBox<?> combo) {
    	super((JComboBox<Object>)combo);
    }

    protected JXComboBox<Object> getXComboBox() {
    	if (comboBox instanceof JXComboBox<?>) {
    		return (JXComboBox<Object>) comboBox;
    	}
    	return null;
    }
    
    static boolean isMenuShortcutKeyDown(InputEvent event) {
        return (event.getModifiersEx() &
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()) != 0;
    }
    
	@Override
	protected JList<Object> createList() {
		JXComboBox<?> xComboBox = getXComboBox();
		return new JXList<Object>(comboBox.getModel(), xComboBox==null ? false : xComboBox.hasRowSorter()) {
			@Override
            public void processMouseEvent(MouseEvent e)  {
				// isMenuShortcutKeyDown is not visible
                if (isMenuShortcutKeyDown(e))  {
                    // Fix for 4234053. Filter out the Control Key from the list.
                    // ie., don't allow CTRL key deselection.
                    Toolkit toolkit = Toolkit.getDefaultToolkit();
                    MouseEvent newEvent = new MouseEvent(
                                       (Component)e.getSource(), e.getID(), e.getWhen(),
                                       e.getModifiersEx() ^ toolkit.getMenuShortcutKeyMaskEx(),
                                       e.getX(), e.getY(),
                                       e.getXOnScreen(), e.getYOnScreen(),
                                       e.getClickCount(),
                                       e.isPopupTrigger(),
                                       MouseEvent.NOBUTTON);
                    // sun.awt.AWTAccessor is not accessible
//                    MouseEventAccessor meAccessor = sun.awt.AWTAccessor.getMouseEventAccessor();
//                    meAccessor.setCausedByTouchEvent(newEvent, meAccessor.isCausedByTouchEvent(e));
                    e = newEvent;
                }
                super.processMouseEvent(e);
            }
		};
	}

	@Override
	public void show() {
		JXComboBox<?> xComboBox = getXComboBox();
//    	int i = xComboBox==null ? comboBox.getSelectedIndex() : xComboBox.getSelectedIndex();
    	int i = comboBox.getSelectedIndex();
    	super.show();
    	// in super.show() ist abgehandelt der Fall : i == -1 ==> list.clearSelection()
    	if(i == -1 ) {
    		// bereits abgehandelt
    	} else {
    		int selectedIndex = xComboBox==null ? i 
    			: (xComboBox.hasRowSorter() ? xComboBox.getRowSorter().convertRowIndexToView(i) : i);
	    	list.setSelectedIndex( selectedIndex );
	    	list.ensureIndexIsVisible( selectedIndex );
    	}

	}
	
	@Override
    public void show(Component invoker, int x, int y) {
    	super.show(invoker, x, y);
    	popupVisible = isVisible(); // avoid rekusive call via setPopupVisible 
    }
	
	@Override
	protected void configureList() {
        list.setFont( comboBox.getFont() );
        list.setForeground( comboBox.getForeground() );
        list.setBackground( comboBox.getBackground() );
        list.setSelectionForeground( UIManager.getColor(BasicXComboBoxUI.SELECTION_FG) );
        list.setSelectionBackground( UIManager.getColor(BasicXComboBoxUI.SELECTION_BG) );
        list.setBorder( null );
        list.setCellRenderer( comboBox.getRenderer() );
        list.setFocusable( false );
        list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        // setListSelection(int) from the type BasicComboPopup is not visible
        //setListSelection( comboBox.getSelectedIndex() );
        int i = comboBox.getSelectedIndex();
//		LOG.config("SelectedIndex="+i + " SortOrder="+xComboBox.getSortOrder()+ " hasRowSorter="+xComboBox.hasRowSorter());
		// TODO remove code DONE 
//        if(i != -1 && (xComboBox.getSortOrder()==null || xComboBox.getSortOrder()==SortOrder.UNSORTED) ) {
////        	list.setSelectedIndex( i ); // <=== hier wird JRendererLabel initialisiert
////        	list.ensureIndexIsVisible( i );   	
//        } else if(i != -1 && (xComboBox.getSortOrder()==SortOrder.ASCENDING || xComboBox.getSortOrder()==SortOrder.DESCENDING)) {
////           	int selectedIndex = xComboBox.getRowSorter().convertRowIndexToView(i);
////        	list.setSelectedIndex( selectedIndex );
////        	list.ensureIndexIsVisible( selectedIndex );
//        } else if(i == -1) {
//        	list.clearSelection();
//        }
        if(i == -1) {
        	list.clearSelection();
        }
        installListListeners();
	}

	@Override
    protected void installListListeners() {
    	super.installListListeners(); // dort create+add MouseListener,MouseMotionListener,ListSelectionListener
    }

	protected void togglePopup() {
        if (popupVisible) {
            hide();
            setPopupVisible(isVisible());
        } else {
            show();
            setPopupVisible(popupVisible);
        }
	}
	public void setPopupVisible(boolean v) {
		if (v) {
			show();
			popupVisible = v;
			// XXX:
//			if(arrowButton instanceof BasicArrowButton) {
//				
//			}
		} else {
            hide();
            popupVisible = v;		
		}
//	public void setPopupVisible(JComboBox<?> c, boolean v) {
/*
        if (popup != null) {
        	LOG.fine("popup "+(v?"show":"hide")+" for "+c);
            if (v) {
                popup.show();
                popupVisible = v;
	            if(arrowButton instanceof BasicArrowButton) {
	            	BasicArrowButton basicArrowButton = (BasicArrowButton)arrowButton;
    	            basicArrowButton.setDirection(SwingConstants.NORTH);
	            } else if(arrowButton instanceof org.jdesktop.swingx.plaf.synth.SynthXComboBoxUI.SynthArrowButton) {
	            	org.jdesktop.swingx.plaf.synth.SynthXComboBoxUI.SynthArrowButton synthArrowButton = (org.jdesktop.swingx.plaf.synth.SynthXComboBoxUI.SynthArrowButton)arrowButton;
	            	synthArrowButton.setDirection(SwingConstants.NORTH);
	            } else {
	            	arrowButton.setIcon(isShowingPopupIcon==null?icon:isShowingPopupIcon);
	            }
            } else {
                popup.hide();
                popupVisible = v;
	            if(arrowButton instanceof BasicArrowButton) {
	            	BasicArrowButton basicArrowButton = (BasicArrowButton)arrowButton;
    	            basicArrowButton.setDirection(SwingConstants.SOUTH);
	            } else if(arrowButton instanceof org.jdesktop.swingx.plaf.synth.SynthXComboBoxUI.SynthArrowButton) {
	            	org.jdesktop.swingx.plaf.synth.SynthXComboBoxUI.SynthArrowButton synthArrowButton = (org.jdesktop.swingx.plaf.synth.SynthXComboBoxUI.SynthArrowButton)arrowButton;
	            	synthArrowButton.setDirection(SwingConstants.SOUTH);
	            } else {
	            	arrowButton.setIcon(icon);
	            }
            }
        }
		
 */
	}

/*
in BasicComboPopup gibt es 
- private Handler handler von Typ inner private class Handler implements ... MouseListener

 */

}
