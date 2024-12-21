package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.beans.BeanProperty;
import java.util.Vector;
import java.util.logging.Logger;

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
import org.jdesktop.swingx.renderer.YListCellRenderer;

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
     * Returns {@code uiClassID}, the {@code UIDefaults} key used to look up 
     * the name of the class that defines the look and feel for this component.
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

    public JYList(ListModel<E> model) {
    	super(model);
    }
    public JYList(final E[] items) {
    	super(items);
    }
    public JYList(final Vector<? extends E> items) {
    	super(items);
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
    
    // cannot use cellRenderer from super, it is private
    private ListCellRenderer<? super E> cellRenderer;
    private int fixedCellWidth = -1;
    private int fixedCellHeight = -1;
    /**
     * {@inheritDoc} <p>
     * overridden to return private cellRenderer
     */
    @Override
    public ListCellRenderer<? super E> getCellRenderer() {
    	LOG.config("cellRenderer "+cellRenderer);
        return cellRenderer;
    }
    /**
     * {@inheritDoc} <p>
     * overridden to set private cellRenderer
     */
    @Override
    public void setCellRenderer(ListCellRenderer<? super E> cellRenderer) {
        ListCellRenderer<? super E> oldValue = this.cellRenderer;
        LOG.config("cellRenderer old "+this.cellRenderer + " new "+cellRenderer);
        this.cellRenderer = cellRenderer;

        /* If the cellRenderer has changed and prototypeCellValue was set, 
         * then recompute fixedCellWidth and fixedCellHeight.
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
            // fixedCellWidth/fixedCellHeight are not used

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
    		LOG.config("call method from super JList.updateUI()");
    		super.updateUI();
    		return;
    	}
    	LOG.config("getUIClassID():"+getUIClassID());

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

    /**
     * {@inheritDoc} <p>
     * returns the look and feel delegate for this component.
     */
    public ListUI getUI() {
        return (ListUI)ui;
    }

}
