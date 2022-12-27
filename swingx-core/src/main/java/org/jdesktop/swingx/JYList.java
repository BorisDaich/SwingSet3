package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.beans.BeanProperty;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ListUI;

import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.YListAddon;

/**
 * JYList extends JList without extending features. The intent is to change
 * - the Color "List.background" with secondary3 Color of the CurrentTheme
 * - and use a Border for cells
 * <p>
 * JXList extends JYList with new features (sorting/filtering)
 * 
 * @author EUG https://github.com/homebeaver
 *
 * @param <E> the type of the elements of this list
 */
@SuppressWarnings("serial")
public class JYList<E> extends JList<E> {

    private static final Logger LOG = Logger.getLogger(JYList.class.getName());

    static {
        LookAndFeelAddons.contribute(new YListAddon());
    }

    /**
     * UI Class ID
     * @see #getUIClassID
     * @see javax.swing.JComponent#readObject
     */
    public static final String uiClassID = "YListUI";

    /**
     * Returns {@code uiClassID}, the <code>UIDefaults</code> key used to look
     * up the name of the {@code javax.swing.plaf.ListUI} class that defines
     * the look and feel for this component.
     *
     * @return the string {@code uiClassID}
     * @see JComponent#getUIClassID
     * @see UIDefaults#getUI
     */
    /*
     * JComponent#getUIClassID : Subclasses of JComponent that support pluggable look and feel 
     * should override this method to return a UIDefaults key that maps to the ComponentUI subclass 
     * that defines their look and feel.
     * 
     * UIDefaults#getUI(JComponent target) : Creates an ComponentUI implementation for the specified component. 
     * In other words create the look and feel specific delegate object for target.This is done in two steps: 
     * • Look up the name of the ComponentUI implementation class under the value returned by target.getUIClassID().
     * • Use the implementation classes static createUI() method to construct a look and feel delegate. 
     */
    @BeanProperty(bound = false)
    public String getUIClassID() {
        return uiClassID;
    }

    public JYList(ListModel<E> dataModel) {
    	super(dataModel);
    }
    public JYList(final E[] listData) {
    	super(listData);
    }
    public JYList(final Vector<? extends E> listData) {
    	super(listData);
    }
    public JYList() {
    	super();
    }

    public static Border getBorder(JComponent c, ComponentUI ui, String key) {
    	Object iValue = UIManager.get(key, c.getLocale());
        if (iValue == null || !(iValue instanceof Border)) {
            return null;
        }
        return (Border)iValue;
    }
    public static Color getColor(JComponent c, ComponentUI ui, String key) {
    	Object iValue = UIManager.get(key, c.getLocale());
        if (iValue == null || !(iValue instanceof Border)) {
            return null;
        }
        return (Color)iValue;
    }

    public class YListCellRenderer extends DefaultListCellRenderer {
        public YListCellRenderer() {
            super();
            setOpaque(true);
            setBorder(getNoFocusBorder());
            setName("List.cellRenderer");
        }
        /* in (super) DefaultListCellRenderer ist die Methode getNoFocusBorder private:
    protected static Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;
    private Border getNoFocusBorder() {
        Border border = DefaultLookup.getBorder(this, ui, "List.cellNoFocusBorder");
        if (System.getSecurityManager() != null) {
            if (border != null) return border;
            return SAFE_NO_FOCUS_BORDER;
        } else {
            if (border != null && (noFocusBorder == null || noFocusBorder == DEFAULT_NO_FOCUS_BORDER)) {
                return border;
            }
            return noFocusBorder;
        }
    }
         */
        private Border getNoFocusBorder() {
        	Border border = JYList.getBorder(this, ui, "List.cellNoFocusBorder");
        	if (border != null) return border;
        	return DefaultListCellRenderer.noFocusBorder;
        }
        @Override // implements public interface ListCellRenderer<E>
        // original in super: not accessible: sun.swing.DefaultLookup 
		public Component getListCellRendererComponent(JList<?> list, Object value
				, int index, boolean isSelected, boolean cellHasFocus) {
        	LOG.finer("index="+index + ",isSelected="+isSelected + ",cellHasFocus="+cellHasFocus+",value="+value);
/* Bsp:
INFORMATION: value=Jane Doe   ,index=0,isSelected=true ,cellHasFocus=false
INFORMATION: value=John Smith ,index=1,isSelected=false,cellHasFocus=false
INFORMATION: value=Hans Muller,index=2,isSelected=false,cellHasFocus=false
INFORMATION: value=Jane Doe   ,index=3,isSelected=false,cellHasFocus=false			
 */
            setComponentOrientation(list.getComponentOrientation());

            Color bg = null;
            Color fg = null;

            JList.DropLocation dropLocation = list.getDropLocation();
            if (dropLocation != null
                    && !dropLocation.isInsert()
                    && dropLocation.getIndex() == index) {

                bg = JYList.getColor(this, ui, "List.dropCellBackground");
                fg = JYList.getColor(this, ui, "List.dropCellForeground");

                isSelected = true;
            }
            if (isSelected) {
                setBackground(bg == null ? list.getSelectionBackground() : bg);
                setForeground(fg == null ? list.getSelectionForeground() : fg);
            }
            else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            if (value instanceof Icon) {
                setIcon((Icon)value);
                setText("");
            }
            else {
                setIcon(null);
                setText((value == null) ? "" : value.toString());
            }

            setEnabled(list.isEnabled());
            setFont(list.getFont());
			Border border = null;
			if (cellHasFocus) {
				if (isSelected) {
					border = JYList.getBorder(this, ui, "List.focusSelectedCellHighlightBorder");
					LOG.info("cellHasFocus+isSelected border:"+border);
				}
				border = JYList.getBorder(this, ui, "List.focusCellHighlightBorder");
				LOG.info("cellHasFocus+isNOTSelected border:"+border);
			} else {
				border = getNoFocusBorder();
			}
			setBorder(border);
			return this;
		}
// for info: JComponent#paint     
//        public void paint(Graphics g) {
//        	// JComponent.paint delegates to paintComponent, paintBorder, paintChildren
//        	super.paint(g);
//        }
        protected void paintComponent(Graphics g) {
        	super.paintComponent(g); // mit ui.update(scratchGraphics, this); ui ist MetallLabelUI
        }
        protected void paintBorder(Graphics g) {
            Border border = getBorder();
            LOG.fine("-----DO "+this.getText()+" border.paintBorder "+border);
            if (border != null) {
                border.paintBorder(this, g, 0, 0, getWidth(), getHeight());
            }

        }
    }
    
    private ListCellRenderer<? super E> cellRenderer;
    private int fixedCellWidth = -1;
    private int fixedCellHeight = -1;
    public ListCellRenderer<? super E> getCellRenderer() {
    	LOG.config("cellRenderer "+cellRenderer);
/* Dez. 11, 2022 9:49:51 AM org.jdesktop.swingx.JYList getCellRenderer
// INFORMATION: cellRenderer org.jdesktop.swingx.JYList$YListCellRenderer[List.cellRenderer,-87,-20,0x0,invalid,alignmentX=0.0,alignmentY=0.0,border=javax.swing.border.EtchedBorder@2446c1df,flags=25165832,maximumSize=,minimumSize=,preferredSize=,defaultIcon=,disabledIcon=,horizontalAlignment=LEADING,horizontalTextPosition=TRAILING,iconTextGap=4,labelFor=,text=Alan Chung,verticalAlignment=CENTER,verticalTextPosition=CENTER]
INFORMATION: cellRenderer org.jdesktop.swingx.JYList
                                        ,The x position
	$YListCellRenderer[List.cellRenderer,-87,-20,0x0,invalid
	// ab hier von JComponent:
	,alignmentX=0.0,alignmentY=0.0
	,border=javax.swing.border.EtchedBorder@3dd8e12e
	,flags=25165832,maximumSize=,minimumSize=,preferredSize=
	// ab hier von JLabel:
	,defaultIcon=,disabledIcon=,horizontalAlignment=LEADING,horizontalTextPosition=TRAILING
	,iconTextGap=4,labelFor=,text=Alan Chung,verticalAlignment=CENTER,verticalTextPosition=CENTER]
 */
        return cellRenderer;
    }
    public void setCellRenderer(ListCellRenderer<? super E> cellRenderer) {
        ListCellRenderer<? super E> oldValue = this.cellRenderer;
        LOG.config("cellRenderer old "+this.cellRenderer + " new "+cellRenderer);
        this.cellRenderer = cellRenderer;

        /* If the cellRenderer has changed and prototypeCellValue
         * was set, then recompute fixedCellWidth and fixedCellHeight.
         */
        if ((cellRenderer != null) && !cellRenderer.equals(oldValue)) {
            updateFixedCellSize();
        }

        firePropertyChange("cellRenderer", oldValue, cellRenderer);
    }
    private void updateFixedCellSize() {
        ListCellRenderer<? super E> cr = getCellRenderer();
        E value = getPrototypeCellValue();

        if ((cr != null) && (value != null)) {
            Component c = cr.getListCellRendererComponent(this, value, 0, false, false);

            /* The ListUI implementation will add Component c to its private
             * CellRendererPane however we can't assume that's already
             * been done here.  So we temporarily set the one "inherited"
             * property that may affect the renderer components preferred size:
             * its font.
             */
            Font f = c.getFont();
            c.setFont(getFont());

            Dimension d = c.getPreferredSize();
            fixedCellWidth = d.width;
            fixedCellHeight = d.height;

            c.setFont(f);
        }
    }

    private transient boolean updateInProgress;
    /**
     * {@inheritDoc} <p>
     * copied from super to call private paintImpl
     */
    public void updateUI() {
    	if (getUIClassID() == super.getUIClassID()) {
    		LOG.info("call method from super JList.updateUI()");
    		super.updateUI();
    		return;
    	}
    	LOG.info("getUIClassID():"+getUIClassID());

    	setCellRenderer(new YListCellRenderer());
        if (!updateInProgress) {
            updateInProgress = true;
            try {
            	// expectedUIClass: ListUI
            	ComponentUI ui = LookAndFeelAddons.getUI(this, ListUI.class);
            	LOG.config("ui:"+ui);
                setUI((ListUI)ui);

                ListCellRenderer<? super E> renderer = getCellRenderer();
                if (renderer instanceof Component) {
                    SwingUtilities.updateComponentTreeUI((Component)renderer);
                }
            } finally {
                updateInProgress = false;
            }
        }
    }

    /**
     * Sets the {@code ListUI}, the look and feel object that
     * renders this component.
     *
     * @param newUI  the <code>ListUI</code> object
     * @see UIDefaults#getUI
     */
    @BeanProperty(hidden = true, visualUpdate = true, description
            = "The UI object that implements the Component's LookAndFeel.")
    public void setUI(ListUI newUI) {
    	LOG.config("newUI:"+newUI);
    	if(ui==newUI) return;
        ComponentUI oldUI = ui;
        ui = newUI;
        if (ui != null) {
            ui.installUI(this); // calls BasicYListUI#installUI resp. SynthYListUI#installUI
        }
        firePropertyChange("UI", oldUI, newUI);
        revalidate();
        repaint();
    }
    public ListUI getUI() {
        return (ListUI)ui;
    }

}
