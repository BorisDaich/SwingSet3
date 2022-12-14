package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.beans.BeanProperty;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
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

@SuppressWarnings("serial")
public class JYList<E> extends JList<E> {

    private static final Logger LOG = Logger.getLogger(JYList.class.getName());

    static {
        LookAndFeelAddons.contribute(new YListAddon());
    }

    /**
     * @see #getUIClassID
     * @see #readObject
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
//        return getBorder(c, ui, key, null); // in sun.swing.DefaultLookup 
//    }
//    public static Border getBorder(JComponent c, ComponentUI ui, String key, Border defaultValue) {
        //Object iValue = get(c, ui, key);
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

/*

in org.jdesktop.swingx.renderer gibt es noch
public class DefaultListRenderer<E> extends AbstractRenderer implements ListCellRenderer<E>
mit
public abstract class AbstractRenderer implements RolloverRenderer, StringValue, Serializable, UIDependent
zum Vergleich:
public class javax.swing.DefaultListCellRenderer extends JLabel implements ListCellRenderer<Object>, Serializable

dort gibt es protected ListCellContext cellContext - ListCellContext extends CellContext gedacht für (Table-, List-, Tree-)
        cellContext.installContext(list, value, index, 0, isSelected, cellHasFocus, true, true);
    public void installContext(JList<?> component, Object value, int row, int column,
            boolean selected, boolean focused, boolean expanded, boolean leaf) {

 */
    public class YListCellRenderer extends DefaultListCellRenderer {
        public YListCellRenderer() {
            super();
            setOpaque(true);
            setBorder(getNoFocusBorder());
            setName("List.cellRenderer");
        }
        private Border getNoFocusBorder() {
        	//LOG.info("use EtchedBorder ...");
        	return //new EmptyBorder(1, 1, 1, 1); 
        			BorderFactory.createEtchedBorder();
        }
        @Override // implements public interface ListCellRenderer<E>
        // original in super: not accessible: sun.swing.DefaultLookup 
		public Component getListCellRendererComponent(JList<?> list, Object value
				, int index, boolean isSelected, boolean cellHasFocus) {
//        	LOG.info("value="+value + ",index="+index + ",isSelected="+isSelected + ",cellHasFocus="+cellHasFocus);
/*
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
//			Component comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
//			LOG.info(">>>>>>>>>>>>"+comp);
			Border border = null;
			if (cellHasFocus) {
				if (isSelected) {
//					LOG.info("cellHasFocus+isSelected >>>>>TODO"); //TODO "List.focusSelectedCellHighlightBorder"
					border = JYList.getBorder(this, ui, "List.focusSelectedCellHighlightBorder");
					LOG.info("cellHasFocus+isSelected border:"+border);
				}
//				LOG.info("cellHasFocus+isNOTSelected >>>>>TODO"); //TODO "List.focusCellHighlightBorder"
				border = JYList.getBorder(this, ui, "List.focusCellHighlightBorder");
				LOG.info("cellHasFocus+isNOTSelected border:"+border);
			} else {
				border = getNoFocusBorder();
			}
			setBorder(border);
//			LOG.info("return "+this);
			return this;
		}
//        public void paint(Graphics g) {
//        	// JComponent.paint delegates to paintComponent, paintBorder, paintChildren
//        	super.paint(g);
//        }
        protected void paintComponent(Graphics g) {
        	super.paintComponent(g); // mit ui.update(scratchGraphics, this); ui ist MetallLabelUI
        }
        protected void paintBorder(Graphics g) {
            Border border = getBorder();
            LOG.info("-----DO "+this.getText()+" border.paintBorder "+border);
            if (border != null) {
                border.paintBorder(this, g, 0, 0, getWidth(), getHeight());
            }

        }
//        protected void paintChildren(Graphics g) {
//        	
//        }
    }
    
    private ListCellRenderer<? super E> cellRenderer;
    private int fixedCellWidth = -1;
    private int fixedCellHeight = -1;
    public ListCellRenderer<? super E> getCellRenderer() {
    	LOG.info("cellRenderer "+cellRenderer);
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
        LOG.info("cellRenderer old "+this.cellRenderer + " new "+cellRenderer);
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
    public void updateUI() {
    	if (getUIClassID() == super.getUIClassID()) {
    		LOG.info("JList super.updateUI()");
    		super.updateUI();
    		return;
    	}
    	LOG.info("getUIClassID():"+getUIClassID());
    	/*
    	 * BasicYListUI : wg. EtchedBorder, BG tut auch ohne
    	 * SynthYListUI : tut nix
    	 */
    	setCellRenderer(new YListCellRenderer()); // für SynthYListUI, bei BasicYListUI nicht notwendig
        if (!updateInProgress) {
            updateInProgress = true;
            try {
            	// expectedUIClass ListUI
            	ComponentUI ui = LookAndFeelAddons.getUI(this, ListUI.class);
            	LOG.info("ui:"+ui);
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
     * @param ui  the <code>ListUI</code> object
     * @see UIDefaults#getUI
     */
    @BeanProperty(hidden = true, visualUpdate = true, description
            = "The UI object that implements the Component's LookAndFeel.")
    public void setUI(ListUI newUI) {
    	LOG.info("ui:"+newUI);
//        super.setUI(ui); // wird bis JComponent.setUI(ComponentUI newUI) weitergeleitet,
        // dort: protected transient ComponentUI ui
    	if(ui==newUI) return;
        ComponentUI oldUI = ui;
        ui = newUI;
        if (ui != null) {
            ui.installUI(this);
        }
        firePropertyChange("UI", oldUI, newUI);
        revalidate();
        repaint();
    }
    public ListUI getUI() {
    	//LOG.info("?????ui:"+ui);
        return (ListUI)ui;
    }

}
