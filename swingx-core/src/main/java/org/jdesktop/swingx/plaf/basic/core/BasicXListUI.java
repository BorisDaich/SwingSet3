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
package org.jdesktop.swingx.plaf.basic.core;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import javax.swing.CellRendererPane;
import javax.swing.DefaultListCellRenderer;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.plaf.LookAndFeelUtils;
import org.jdesktop.swingx.plaf.basic.BasicYListUI;
import org.jdesktop.swingx.plaf.basic.core.DragRecognitionSupport.BeforeDrag;

/**
 * An extensible implementation of {@code ListUI} for JXList.
 * {@code BasicXListUI} instances cannot be shared between multiple lists.<p>
 * 
 * The heart of added functionality is to support sorting/filtering, that is keep 
 * model-selection and RowSorter state synchronized. The details are delegated to a ListSortUI, 
 * but this class is responsible to manage the sortUI on changes of list properties, model and 
 * view selection (same strategy as in JXTable).<p>
 * 
 * Note: this delegate is mostly a 1:1 copy of BasicListUI. The difference is that
 * it accesses the list elements and list elementCount exclusively through the 
 * JXList api. This allows a clean implementation of sorting/filtering.<p>
 * 
 * The differences (goal was to touch as little code as possible as this needs
 * to be updated on every change to core until that is changed to not access
 * the list's model directly, sigh) for core functionality:
 * <ul>
 * <li> extracted method for list.getModel().getSize (for the delegate class and 
 *      all contained static classes) and use that method exclusively
 * <li> similar for remaining list.getModel(): implemented wrapping listModel 
 *    which messages the list
 * <li> rename key for shared actionMap to keep core list actions separate 
 *    (just in case somebody wants both) - they point to the wrong delegate
 * <li> replaced references to SwingUtilities2 in sun packages by references to 
 *     pasted methods in SwingXUtilities
 * <li> replaced storage of shared Input/ActionMap in defaultLookup by direct
 *     storage in UIManager.         
 * </ul>
 * 
 * Differences to achieve extended functionality:
 * <ul>
 * <li> added methods to un/-installSortUI and call in un/installUI(component)
 * <li> changed PropertyChangeHandler to call a) hasHandledPropertyChange to 
 *  allow this class to replace super handler functinality and 
 *  b) updateSortUI after handling all not-sorter related properties.
 * <li> changed createPropertyChangeListener to return a PropertyChangeHandler
 * <li> changed ListDataHandler to check if event handled by SortUI and delegate
 *    to handler only if not
 * <li> changed createListDataListener to return a ListDataHandler
 * <li> changed ListSelectionHandler to check if event handled by SortUI and 
 *   delegate to handler only if not
 * </ul> changed createListSelectionListener to return a ListSelectionHandler
 * 
 * Differences for bug fixes (due to incorrectly extending super):
 * <ul>
 * <li> Issue #1495-swingx: getBaseline throughs NPE
 * </ul>
 * 
 * Note: extension of core (instead of implement from scratch) is to keep 
 * external (?) code working which expects a ui delegate of type BasicSomething.
 * LAF implementors with a custom ListUI extending BasicListUI should be able to
 * add support for JXList by adding a separate CustomXListUI extending this, same
 * as the default with parent changed. <b>Beware</b>: custom code must not 
 * call access the model directly or - if they insist - convert the row index to 
 * account for sorting/filtering! That's the whole point of this class.
 * 
 * @author Hans Muller
 * @author Philip Milne
 * @author Shannon Hickey (drag and drop)
 * @author EUG https://github.com/homebeaver (reorg)
 */
/*
JList hierarchie:
                 public abstract class javax.swing.plaf.ListUI extends ComponentUI
                                                          |
public class javax.swing.plaf.basic.BasicListUI extends ListUI

Instanziert wird die Klasse über die factory createUI, 
Die systematische/symetrische Ableitung wäre:

                                         ComponentUI
                                          |
YListUI ----------------- abstract class ListUI
 |                                        |
BasicYListUI               symetrisch zu BasicListUI
 |  |                                     |
 | BasicXListUI                           |
SynthYListUI               symetrisch zu SynthListUI

 */
public class BasicXListUI extends BasicYListUI {

    private static final Logger LOG = Logger.getLogger(BasicXListUI.class.getName());

    /**
     * Factory. Returns a new instance of BasicXListUI.  
     * BasicXListUI delegates are allocated one per JList.
     *
     * @param c JComponent
     * @return A new ListUI implementation for the Windows look and feel.
     */
    public static ComponentUI createUI(JComponent c) {
    	LOG.config("UI factory for JComponent:"+c);
        return new BasicXListUI(c);
    }

    // like in BasicListUI
    public static void loadActionMap(LazyActionMap map) {
    	BasicYListUI.loadActionMap(map);
    }

	public BasicXListUI(JComponent c) {
		super(c);
	}
	
	// vars in (super) BasicYListUI:
//  protected JList<Object> list = null; // defined in YListUI
//...

//-------------------- X-Wrapper
    
    private ListModel<Object> modelX;

    private ListSortUI sortUI;
    
    /**
     * Compatibility Wrapper: a synthetic model which delegates to list api and throws
     * @return ListModel
     */
	protected ListModel<Object> getViewModel() {
		if (modelX == null) {
			modelX = new ListModel<Object>() {

				@Override
				public int getSize() {
					if(list instanceof JXList<?> xlist) {
						return xlist.getElementCount();
					}
					return list.getModel().getSize();
				}

				@Override
				public Object getElementAt(int index) {
					if(list instanceof JXList<?> xlist) {
						return xlist.getElementAt(index);
					}
					return list.getModel().getElementAt(index);
				}

				@Override
				public void addListDataListener(ListDataListener l) {
					throw new UnsupportedOperationException("this is a synthetic model wrapper");
				}

				@Override
				public void removeListDataListener(ListDataListener l) {
					throw new UnsupportedOperationException("this is a synthetic model wrapper");

				}

			};
		}
		return modelX;
	}

    /**
     * @return no of elements
     */
    protected int getElementCount() {
    	if(list instanceof JXList<?> xlist) {
    		return xlist.getElementCount();
    	}
    	return list.getModel().getSize();
    }

    /**
     * get Element At index <code>viewIndex</code>
     * @param viewIndex the index
     * @return Object
     */
    protected Object getElementAt(int viewIndex) {
		if(list instanceof JXList<?> xlist) {
			return xlist.getElementAt(viewIndex);
		}
		return list.getModel().getElementAt(viewIndex);
    }


//--------------- api to support/control sorting/filtering
    
    /**
     * @return ListSortUI
     */
    protected ListSortUI getSortUI() {
        return sortUI;
    }
    
    /**
     * Installs SortUI if the list has a rowSorter. Does nothing if not.
     */
    protected void installSortUI() {
		if(list instanceof JXList<?> xlist) {
	        if (xlist.getRowSorter() == null) return;
	        sortUI = new ListSortUI(xlist, xlist.getRowSorter());
		}
    }
    
    /**
     * Dispose and null's the sortUI if installed. Does nothing if not.
     */
    protected void uninstallSortUI() {
        if (sortUI == null) return;
        sortUI.dispose();
        sortUI = null;
    }
    
    /**
     * Called from the PropertyChangeHandler.
     * 
     * @param property the name of the changed property.
     */
    protected void updateSortUI(String property) {
        if ("rowSorter".equals(property)) {
            updateSortUIToRowSorterProperty();
        }
    }
    /**
     * 
     */
    private void updateSortUIToRowSorterProperty() {
        uninstallSortUI();
        installSortUI();
    }
    
    /**
     * Returns a boolean indicating whether or not the event has been processed
     * by the sortUI. 
     * @param e ListDataEvent
     * @return event has been processed
     */
    protected boolean processedBySortUI(ListDataEvent e) {
        if (sortUI == null)
            return false;
        sortUI.modelChanged(e);
        updateLayoutStateNeeded = modelChanged;
        redrawList();
        return true;
    }
    
    /**
     * Returns a boolean indicating whether or not the event has been processed
     * by the sortUI. 
     * @param e ListSelectionEvent
     * @return event has been processed
     */
    protected boolean processedBySortUI(ListSelectionEvent e) {
        if (sortUI == null) return false;
        sortUI.viewSelectionChanged(e);
        list.repaint();
        return true;
    }

//--------------------- enhanced support
    /**
     * Invalidates the cell size cache and revalidates/-paints the list.
     * 
     */
    public void invalidateCellSizeCache() {
        updateLayoutStateNeeded |= 1;
        redrawList();
    }

//---------------------  core copy

    /**
     * Paint the rows that intersect the Graphics objects clipRect.  
     * This method calls paintCell as necessary.  
     * Subclasses may want to override these methods.
     *
     * @see #paintCell
     */
    public void paint(Graphics g, JComponent c) {
        Shape clip = g.getClip();
        paintImpl(g, c);
        g.setClip(clip);

        paintDropLine(g);
    }

    protected void paintImpl(Graphics g, JComponent c) {
        switch (layoutOrientation) {
        case JList.VERTICAL_WRAP:
            if (list.getHeight() != listHeight) {
                updateLayoutStateNeeded |= heightChanged;
                redrawList();
            }
            break;
        case JList.HORIZONTAL_WRAP:
            if (list.getWidth() != listWidth) {
                updateLayoutStateNeeded |= widthChanged;
                redrawList();
            }
            break;
        default:
            break;
        }
        maybeUpdateLayoutState();

        ListCellRenderer<Object> renderer = list.getCellRenderer();
        ListModel<Object> dataModel = getViewModel(); //list.getModel();
        ListSelectionModel selModel = list.getSelectionModel();
        int size;

        if ((renderer == null) || (size = dataModel.getSize()) == 0) {
            return;
        }

        // Determine how many columns we need to paint
        Rectangle paintBounds = g.getClipBounds();

        int startColumn, endColumn;
        if (c.getComponentOrientation().isLeftToRight()) {
            startColumn = convertLocationToColumn(paintBounds.x,
                                                  paintBounds.y);
            endColumn = convertLocationToColumn(paintBounds.x +
                                                paintBounds.width,
                                                paintBounds.y);
        } else {
            startColumn = convertLocationToColumn(paintBounds.x +
                                                paintBounds.width,
                                                paintBounds.y);
            endColumn = convertLocationToColumn(paintBounds.x,
                                                  paintBounds.y);
        }
        int maxY = paintBounds.y + paintBounds.height;
        int leadIndex = adjustIndex(list.getLeadSelectionIndex(), list);
        int rowIncrement = (layoutOrientation == JList.HORIZONTAL_WRAP) ?
                           columnCount : 1;


        for (int colCounter = startColumn; colCounter <= endColumn; colCounter++) {
            // And then how many rows in this columnn
            int row = convertLocationToRowInColumn(paintBounds.y, colCounter);
            int rowCount = getRowCount(colCounter);
            int index = getModelIndex(colCounter, row);
            Rectangle rowBounds = getCellBounds(list, index, index);

            if (rowBounds == null) {
                // Not valid, bail!
                return;
            }
            while (row < rowCount && rowBounds.y < maxY && index < size) {
                rowBounds.height = getHeight(colCounter, row);
                g.setClip(rowBounds.x, rowBounds.y, rowBounds.width, rowBounds.height);
                g.clipRect(paintBounds.x, paintBounds.y, paintBounds.width, paintBounds.height);
                paintCell(g, index, rowBounds, renderer, dataModel, selModel, leadIndex);
                rowBounds.y += rowBounds.height;
                index += rowIncrement;
                row++;
            }
        }
        // Empty out the renderer pane, allowing renderers to be gc'ed.
        rendererPane.removeAll();
    }

    /**
     * Returns the baseline.
     *
     * @throws NullPointerException {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     * @see javax.swing.JComponent#getBaseline(int, int)
     * @since 1.6
     */
    // modified code from javax.swing.plaf.basic.BasicListUI#getBaseline
    public int getBaseline(JComponent c, int width, int height) {
//        super.getBaseline(c, width, height);
        checkBaselinePrecondition(c, width, height);
        int rowHeight = list.getFixedCellHeight();
        UIDefaults lafDefaults = UIManager.getLookAndFeelDefaults();
        Component renderer = (Component)lafDefaults.get(BASELINE_COMPONENT_KEY);
        if (renderer == null) {
            @SuppressWarnings("unchecked")
            ListCellRenderer<Object> lcr = (ListCellRenderer<Object>)UIManager.get("List.cellRenderer");

            // fix for 6711072 some LAFs like Nimbus do not provide this
            // UIManager key and we should not through a NPE here because of it
            if (lcr == null) {
                lcr = new DefaultListCellRenderer();
            }
            renderer = lcr.getListCellRendererComponent(list, "a", -1, false, false);
            lafDefaults.put(BASELINE_COMPONENT_KEY, renderer);
        }
        renderer.setFont(list.getFont());
        // JList actually has much more complex behavior here.
        // If rowHeight != -1 the rowHeight is either the max of all cell
        // heights (layout orientation != VERTICAL), or is variable depending
        // upon the cell.  We assume a default size.
        // We could theoretically query the real renderer, but that would
        // not work for an empty model and the results may vary with 
        // the content.
        if (rowHeight == -1) {
            rowHeight = renderer.getPreferredSize().height;
        }
        return renderer.getBaseline(Integer.MAX_VALUE, rowHeight) + list.getInsets().top;
    }

    /**
     * Returns an enum indicating how the baseline of the component
     * changes as the size changes.
     *
     * @throws NullPointerException {@inheritDoc}
     * @see javax.swing.JComponent#getBaseline(int, int)
     * @since 1.6
     */
    public Component.BaselineResizeBehavior getBaselineResizeBehavior(
            JComponent c) {
        super.getBaselineResizeBehavior(c);
        return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
    }

    /* TODO cannot build table with javadoc :
     * <table>
     * <tr><th>Layout Orientation</th><th>Preferred Size</th></tr>
     * <tr>
     *   <td>JList.VERTICAL</td>
     *   <td>The preferredSize of the list is total height of the rows
     *       and the maximum width of the cells.  If JList.fixedCellHeight
     *       is specified then the total height of the rows is just
     *       (cellVerticalMargins + fixedCellHeight) * model.getSize() where
     *       rowVerticalMargins is the space we allocate for drawing
     *       the yellow focus outline.  Similarly if fixedCellWidth is
     *       specified then we just use that.
     *   </td>
     * </tr>
     * <tr>
     *   <td>JList.VERTICAL_WRAP</td>
     *   <td>If the visible row count is greater than zero, the preferredHeight
     *       is the maximum cell height * visibleRowCount. If the visible row
     *       count is &lt;= 0, the preferred height is either the current height
     *       of the list, or the maximum cell height, whichever is
     *       bigger. The preferred width is than the maximum cell width *
     *       number of columns needed. Where the number of columns needs is
     *       list.height / max cell height. Max cell height is either the fixed
     *       cell height, or is determined by iterating through all the cells
     *       to find the maximum height from the ListCellRenderer.
     *   </td>
     * </tr>
     * <tr>
     *   <td>JList.HORIZONTAL_WRAP</td>
     *   <td>If the visible row count is greater than zero, the preferredHeight
     *       is the maximum cell height * adjustedRowCount.  Where
     *       visibleRowCount is used to determine the number of columns.
     *       Because this lays out horizontally the number of rows is
     *       then determined from the column count.  For example, lets say
     *       you have a model with 10 items and the visible row count is 8.
     *       The number of columns needed to display this is 2, but you no
     *       longer need 8 rows to display this, you only need 5, thus
     *       the adjustedRowCount is 5.
     *       <p>If the visible row
     *       count is &lt;= 0, the preferred height is dictated by the 
     *       number of columns, which will be as many as can fit in the width
     *       of the <code>JList</code> (width / max cell width), with at
     *       least one column.  The preferred height then becomes the
     *       model size / number of columns * maximum cell height.
     *       Max cell height is either the fixed
     *       cell height, or is determined by iterating through all the cells
     *       to find the maximum height from the ListCellRenderer.
     *   </td>
     * </tr>
     * </table>
     */
    /**
     * The preferredSize of the list depends upon the layout orientation. 
     * The table describes the preferred size for each layout orientation:
     * <p>
     * TODO missing table - see code
     * <p>
     * The above specifies the raw preferred width and height. The resulting
     * preferred width is the above width + insets.left + insets.right and
     * the resulting preferred height is the above height + insets.top +
     * insets.bottom. Where the <code>Insets</code> are determined from
     * <code>list.getInsets()</code>.
     *
     * @param c The JList component.
     * @return The total size of the list.
     */
    public Dimension getPreferredSize(JComponent c) {
        maybeUpdateLayoutState();

        int lastRow = getElementCount() - 1;
        if (lastRow < 0) {
            return new Dimension(0, 0);
        }

        Insets insets = list.getInsets();
        int width = cellWidth * columnCount + insets.left + insets.right;
        int height;

        if (layoutOrientation != JList.VERTICAL) {
            height = preferredHeight;
        }
        else {
            Rectangle bounds = getCellBounds(list, lastRow);

            if (bounds != null) {
                height = bounds.y + bounds.height + insets.bottom;
            }
            else {
                height = 0;
            }
        }
        return new Dimension(width, height);
    }


    /**
     * Selected the previous row and force it to be visible.
     *
     * @see JList#ensureIndexIsVisible
     */
    protected void selectPreviousIndex() {
        int s = list.getSelectedIndex();
        if(s > 0) {
            s -= 1;
            list.setSelectedIndex(s);
            list.ensureIndexIsVisible(s);
        }
    }


    /**
     * Selected the previous row and force it to be visible.
     *
     * @see JList#ensureIndexIsVisible
     */
    protected void selectNextIndex()
    {
        int s = list.getSelectedIndex();
        if((s + 1) < getElementCount()) {
            s += 1;
            list.setSelectedIndex(s);
            list.ensureIndexIsVisible(s);
        }
    }


    /**
     * Registers the keyboard bindings on the <code>JList</code> that the
     * <code>BasicXListUI</code> is associated with. 
     * This method is called at installUI() time.
     *
     * @see #installUI
     */
    // modified code from javax.swing.plaf.basic.BasicListUI#installKeyboardActions
    protected void installKeyboardActions() {
        InputMap inputMap = getInputMap(JComponent.WHEN_FOCUSED);
        SwingUtilities.replaceUIInputMap(list, JComponent.WHEN_FOCUSED, inputMap);
        LazyActionMap.installLazyActionMap(list, BasicXListUI.class, "XList.actionMap");
    }

    // modified code from javax.swing.plaf.basic.BasicListUI#getInputMap
    // intentionally private
    InputMap getInputMap(int condition) {
        if (condition == JComponent.WHEN_FOCUSED) {
            // PENDING JW: side-effect when reverting to ui manager? revisit!
            InputMap keyMap = (InputMap) UIManager.get("List.focusInputMap");
            InputMap rtlKeyMap;
            if (isLeftToRight || ((rtlKeyMap = (InputMap) UIManager.get("List.focusInputMap.RightToLeft")) == null)) {
                    return keyMap;
            } else {
                rtlKeyMap.setParent(keyMap);
                return rtlKeyMap;
            }
        }
        return null;
    }

    /**
     * Unregisters keyboard actions installed from
     * <code>installKeyboardActions</code>.
     * This method is called at uninstallUI() time - subclassess should
     * ensure that all of the keyboard actions registered at installUI
     * time are removed here.
     *
     * @see #installUI
     */
    protected void uninstallKeyboardActions() {
        SwingUtilities.replaceUIActionMap(list, null);
        SwingUtilities.replaceUIInputMap(list, JComponent.WHEN_FOCUSED, null);
    }


    /**
     * Create and install the listeners for the JList, its model, and its
     * selectionModel.  This method is called at installUI() time.
     *
     * @see #installUI
     * @see #uninstallListeners
     */
    protected void installListeners() {
        TransferHandler th = list.getTransferHandler();
        if (th == null || th instanceof UIResource) {
            list.setTransferHandler(defaultTransferHandler);
            // default TransferHandler doesn't support drop
            // so we don't want drop handling
            if (list.getDropTarget() instanceof UIResource) {
                list.setDropTarget(null);
            }
        }

        focusListener = createFocusListener();
        mouseInputListener = createMouseInputListener();
        propertyChangeListener = createPropertyChangeListener();
        listSelectionListener = createListSelectionListener();
        listDataListener = createListDataListener();

        list.addFocusListener(focusListener);
        list.addMouseListener(mouseInputListener);
        list.addMouseMotionListener(mouseInputListener);
        list.addPropertyChangeListener(propertyChangeListener);
        list.addKeyListener(getHandler());
        // JW: here we really want the model
        ListModel<Object> model = list.getModel();
        if (model != null) {
            model.addListDataListener(listDataListener);
        }

        ListSelectionModel selectionModel = list.getSelectionModel();
        if (selectionModel != null) {
            selectionModel.addListSelectionListener(listSelectionListener);
        }
    }


    /**
     * Remove the listeners for the JList, its model, and its
     * selectionModel.  All of the listener fields, are reset to
     * null here.  This method is called at uninstallUI() time,
     * it should be kept in sync with installListeners.
     *
     * @see #uninstallUI
     * @see #installListeners
     */
    protected void uninstallListeners() {
        list.removeFocusListener(focusListener);
        list.removeMouseListener(mouseInputListener);
        list.removeMouseMotionListener(mouseInputListener);
        list.removePropertyChangeListener(propertyChangeListener);
        list.removeKeyListener(getHandler());

        ListModel<Object> model = list.getModel();
        if (model != null) {
            model.removeListDataListener(listDataListener);
        }

        ListSelectionModel selectionModel = list.getSelectionModel();
        if (selectionModel != null) {
            selectionModel.removeListSelectionListener(listSelectionListener);
        }

        focusListener = null;
        mouseInputListener  = null;
        listSelectionListener = null;
        listDataListener = null;
        propertyChangeListener = null;
        handler = null;
    }


    /**
     * Initialize JList properties, e.g. font, foreground, and background,
     * and add the CellRendererPane.  The font, foreground, and background
     * properties are only set if their current value is either null
     * or a UIResource, other properties are set if the current
     * value is null.
     *
     * @see #uninstallDefaults
     * @see #installUI
     * @see CellRendererPane
     */
    protected void installDefaults() {
        list.setLayout(null);

        LookAndFeel.installBorder(list, "List.border");

        LookAndFeel.installColorsAndFont(list, "List.background", "List.foreground", "List.font");

        LookAndFeel.installProperty(list, "opaque", Boolean.TRUE);

        if (list.getCellRenderer() == null) {
            @SuppressWarnings("unchecked")
            ListCellRenderer<Object> tmp = (ListCellRenderer<Object>)(UIManager.get("List.cellRenderer"));
            list.setCellRenderer(tmp);
        }

        Color sbg = list.getSelectionBackground();
        if (sbg == null || sbg instanceof UIResource) {
            list.setSelectionBackground(UIManager.getColor("List.selectionBackground"));
        }

        Color sfg = list.getSelectionForeground();
        if (sfg == null || sfg instanceof UIResource) {
            list.setSelectionForeground(UIManager.getColor("List.selectionForeground"));
        }

        Long l = (Long)UIManager.get("List.timeFactor");
        timeFactor = (l!=null) ? l.longValue() : 1000L;

        updateIsFileList();
    }

    protected void updateIsFileList() {
        boolean b = Boolean.TRUE.equals(list.getClientProperty("List.isFileList"));
        if (b != isFileList) {
            isFileList = b;
            Font oldFont = list.getFont();
            if (oldFont == null || oldFont instanceof UIResource) {
                Font newFont = UIManager.getFont(b ? "FileChooser.listFont" : "List.font");
                if (newFont != null && newFont != oldFont) {
                    list.setFont(newFont);
                }
            }
        }
    }


    /**
     * Set the JList properties that haven't been explicitly overridden to
     * null.  A property is considered overridden if its current value
     * is not a UIResource.
     *
     * @see #installDefaults
     * @see #uninstallUI
     * @see CellRendererPane
     */
    protected void uninstallDefaults() {
        LookAndFeel.uninstallBorder(list);
        if (list.getFont() instanceof UIResource) {
            list.setFont(null);
        }
        if (list.getForeground() instanceof UIResource) {
            list.setForeground(null);
        }
        if (list.getBackground() instanceof UIResource) {
            list.setBackground(null);
        }
        if (list.getSelectionBackground() instanceof UIResource) {
            list.setSelectionBackground(null);
        }
        if (list.getSelectionForeground() instanceof UIResource) {
            list.setSelectionForeground(null);
        }
        if (list.getCellRenderer() instanceof UIResource) {
            list.setCellRenderer(null);
        }
        if (list.getTransferHandler() instanceof UIResource) {
            list.setTransferHandler(null);
        }
    }

    /**
     * {@inheritDoc} <p>
     * Also installs <code>ListSortUI()</code>
     */
    public void installUI(JComponent c) {
    	super.installUI(c);
        installSortUI();
    }

    /**
     * {@inheritDoc} <p>
     * Also uninstalls <code>ListSortUI()</code>
     */
    public void uninstallUI(JComponent c) {
        uninstallSortUI();
        super.uninstallUI(c);
    }

    /**
     * Returns the closest row that starts at the specified y-location
     * in the passed in column.
     */
    // TODO in super protected und nutzen
    private int convertLocationToRowInColumn(int y, int column) {
        int x = 0;

        if (layoutOrientation != JList.VERTICAL) {
            if (isLeftToRight) {
                x = column * cellWidth;
            } else {
                x = list.getWidth() - (column+1)*cellWidth - list.getInsets().right;
            } 
        }
        return convertLocationToRow(x, y, true);
    }

    /**
     * Returns the number of rows in the given column.
     */
//  use it from super YListUI TODO
    protected int getRowCount(int column) {
        if (column < 0 || column >= columnCount) {
            return -1;
        }
        if (layoutOrientation == JList.VERTICAL ||
                  (column == 0 && columnCount == 1)) {
            return getElementCount();
        }
        if (column >= columnCount) {
            return -1;
        }
        if (layoutOrientation == JList.VERTICAL_WRAP) {
            if (column < (columnCount - 1)) {
                return rowsPerColumn;
            }
            return getElementCount() - (columnCount - 1) *
                        rowsPerColumn;
        }
        // JList.HORIZONTAL_WRAP
        int diff = columnCount - (columnCount * rowsPerColumn -
                                  getElementCount());

        if (column >= diff) {
            return Math.max(0, rowsPerColumn - 1);
        }
        return rowsPerColumn;
    }

    /**
     * Returns the model index for the specified display location.
     * If <code>column</code>x<code>row</code> is beyond the length of the
     * model, this will return the model size - 1.
     */
//  use it from super YListUI TODO
    protected int getModelIndex(int column, int row) {
        switch (layoutOrientation) {
        case JList.VERTICAL_WRAP:
            return Math.min(getElementCount() - 1, rowsPerColumn *
                            column + Math.min(row, rowsPerColumn-1));
        case JList.HORIZONTAL_WRAP:
            return Math.min(getElementCount() - 1, row * columnCount +
                            column);
        default:
            return row;
        }
    }

    /**
     * Returns the closest column to the passed in location.
     */
//  use it from super YListUI TODO
    protected int convertLocationToColumn(int x, int y) {
        if (cellWidth > 0) {
            if (layoutOrientation == JList.VERTICAL) {
                return 0;
            }
            Insets insets = list.getInsets();
            int col;
            if (isLeftToRight) {
                col = (x - insets.left) / cellWidth;
            } else { 
                col = (list.getWidth() - x - insets.right - 1) / cellWidth;
            }
            if (col < 0) {
                return 0;
            }
            else if (col >= columnCount) {
                return columnCount - 1;
            }
            return col;
        }
        return 0;
    }

    /**
     * Returns the row that the model index <code>index</code> will be
     * displayed in..
     */
//  use it from super YListUI TODO
    protected int convertModelToRow(int index) {
        int size = getElementCount();

        if ((index < 0) || (index >= size)) {
            return -1;
        }

        if (layoutOrientation != JList.VERTICAL && columnCount > 1 &&
                                                   rowsPerColumn > 0) {
            if (layoutOrientation == JList.VERTICAL_WRAP) {
                return index % rowsPerColumn;
            }
            return index / columnCount;
        }
        return index;
    }

    /**
     * Returns the column that the model index <code>index</code> will be
     * displayed in.
     */
//  use it from super YListUI TODO
    protected int convertModelToColumn(int index) {
        int size = getElementCount();

        if ((index < 0) || (index >= size)) {
            return -1;
        }

        if (layoutOrientation != JList.VERTICAL && rowsPerColumn > 0 &&
                                                   columnCount > 1) {
            if (layoutOrientation == JList.VERTICAL_WRAP) {
                return index / rowsPerColumn;
            }
            return index % columnCount;
        }
        return 0;
    }

    /**
     * If updateLayoutStateNeeded is non zero, call updateLayoutState() and reset
     * updateLayoutStateNeeded.  This method should be called by methods
     * before doing any computation based on the geometry of the list.
     * For example it's the first call in paint() and getPreferredSize().
     *
     * @see #updateLayoutState
     */
    protected void maybeUpdateLayoutState()
    {
        if (updateLayoutStateNeeded != 0) {
            updateLayoutState();
            updateLayoutStateNeeded = 0;
        }
    }

    protected BasicYListUI.Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    @Override
    protected ListSelectionListener createListSelectionListener() {
        return new ListSelectionHandler() {
        	@Override
        	public void valueChanged(ListSelectionEvent e) {
        		if (processedBySortUI(e)) return;
        		getHandler().valueChanged(e);
        	}
        };
    }

    @Override
    protected ListDataListener createListDataListener() {
        return new ListDataHandler() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                if (processedBySortUI(e)) return;
                getHandler().intervalAdded(e);
            }
            @Override
            public void intervalRemoved(ListDataEvent e) {
                if (processedBySortUI(e)) return;
                getHandler().intervalRemoved(e);
            }
            @Override
            public void contentsChanged(ListDataEvent e) {
                if (processedBySortUI(e)) return;
                getHandler().contentsChanged(e);
            }
        };
    }

    @Override
    protected PropertyChangeListener createPropertyChangeListener() {
        return new PropertyChangeHandler() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                getHandler().propertyChange(e);
                updateSortUI(e.getPropertyName());
            }
        };
    }

    /** Used by IncrementLeadSelectionAction. Indicates the action should
     * change the lead, and not select it. */
    private static final int CHANGE_LEAD = 0;
    /** Used by IncrementLeadSelectionAction. Indicates the action should
     * change the selection and lead. */
    private static final int CHANGE_SELECTION = 1;
    /** Used by IncrementLeadSelectionAction. Indicates the action should
     * extend the selection from the anchor to the next index. */
    private static final int EXTEND_SELECTION = 2;

    // PENDING JW: this is not a complete replacement of sun.UIAction ...
    protected static class Actions extends BasicYListUI.Actions {
        protected int getElementCount(JList<?> list) {
        	if(list instanceof JXList<?> xlist) {
        		return xlist.getElementCount();
        	}
        	return list.getModel().getSize();
        }

        protected Actions(String name) {
            super(name);
        }
        public void actionPerformed(ActionEvent e) {
            String name = getName();
            @SuppressWarnings("unchecked")
            JList<Object> list = (JList<Object>)e.getSource();
            BasicXListUI ui = (BasicXListUI)LookAndFeelUtils.getUIOfType(list.getUI(), BasicXListUI.class);

            if (name == SELECT_PREVIOUS_COLUMN) {
                changeSelection(list, CHANGE_SELECTION,
                                getNextColumnIndex(list, ui, -1), -1);
            }
            else if (name == SELECT_PREVIOUS_COLUMN_EXTEND) {
                changeSelection(list, EXTEND_SELECTION,
                                getNextColumnIndex(list, ui, -1), -1);
            }
            else if (name == SELECT_PREVIOUS_COLUMN_CHANGE_LEAD) {
                changeSelection(list, CHANGE_LEAD,
                                getNextColumnIndex(list, ui, -1), -1);
            }
            else if (name == SELECT_NEXT_COLUMN) {
                changeSelection(list, CHANGE_SELECTION,
                                getNextColumnIndex(list, ui, 1), 1);
            }
            else if (name == SELECT_NEXT_COLUMN_EXTEND) {
                changeSelection(list, EXTEND_SELECTION,
                                getNextColumnIndex(list, ui, 1), 1);
            }
            else if (name == SELECT_NEXT_COLUMN_CHANGE_LEAD) {
                changeSelection(list, CHANGE_LEAD,
                                getNextColumnIndex(list, ui, 1), 1);
            }
            else if (name == SELECT_PREVIOUS_ROW) {
                changeSelection(list, CHANGE_SELECTION,
                                getNextIndex(list, ui, -1), -1);
            }
            else if (name == SELECT_PREVIOUS_ROW_EXTEND) {
                changeSelection(list, EXTEND_SELECTION,
                                getNextIndex(list, ui, -1), -1);
            }
            else if (name == SELECT_PREVIOUS_ROW_CHANGE_LEAD) {
                changeSelection(list, CHANGE_LEAD,
                                getNextIndex(list, ui, -1), -1);
            }
            else if (name == SELECT_NEXT_ROW) {
                changeSelection(list, CHANGE_SELECTION,
                                getNextIndex(list, ui, 1), 1);
            }
            else if (name == SELECT_NEXT_ROW_EXTEND) {
                changeSelection(list, EXTEND_SELECTION,
                                getNextIndex(list, ui, 1), 1);
            }
            else if (name == SELECT_NEXT_ROW_CHANGE_LEAD) {
                changeSelection(list, CHANGE_LEAD,
                                getNextIndex(list, ui, 1), 1);
            }
            else if (name == SELECT_FIRST_ROW) {
                changeSelection(list, CHANGE_SELECTION, 0, -1);
            }
            else if (name == SELECT_FIRST_ROW_EXTEND) {
                changeSelection(list, EXTEND_SELECTION, 0, -1);
            }
            else if (name == SELECT_FIRST_ROW_CHANGE_LEAD) {
                changeSelection(list, CHANGE_LEAD, 0, -1);
            }
            else if (name == SELECT_LAST_ROW) {
                changeSelection(list, CHANGE_SELECTION, getElementCount(list) - 1, 1);
            }
            else if (name == SELECT_LAST_ROW_EXTEND) {
                changeSelection(list, EXTEND_SELECTION, getElementCount(list) - 1, 1);
            }
            else if (name == SELECT_LAST_ROW_CHANGE_LEAD) {
                changeSelection(list, CHANGE_LEAD, getElementCount(list) - 1, 1);
            }
            else if (name == SCROLL_UP) {
                changeSelection(list, CHANGE_SELECTION,
                                getNextPageIndex(list, -1), -1);
            }
            else if (name == SCROLL_UP_EXTEND) {
                changeSelection(list, EXTEND_SELECTION,
                                getNextPageIndex(list, -1), -1);
            }
            else if (name == SCROLL_UP_CHANGE_LEAD) {
                changeSelection(list, CHANGE_LEAD,
                                getNextPageIndex(list, -1), -1);
            }
            else if (name == SCROLL_DOWN) {
                changeSelection(list, CHANGE_SELECTION,
                                getNextPageIndex(list, 1), 1);
            }
            else if (name == SCROLL_DOWN_EXTEND) {
                changeSelection(list, EXTEND_SELECTION,
                                getNextPageIndex(list, 1), 1);
            }
            else if (name == SCROLL_DOWN_CHANGE_LEAD) {
                changeSelection(list, CHANGE_LEAD,
                                getNextPageIndex(list, 1), 1);
            }
            else if (name == SELECT_ALL) {
                selectAll(list);
            }
            else if (name == CLEAR_SELECTION) {
                clearSelection(list);
            }
            else if (name == ADD_TO_SELECTION) {
                int index = adjustIndex(
                    list.getSelectionModel().getLeadSelectionIndex(), list);

                if (!list.isSelectedIndex(index)) {
                    int oldAnchor = list.getSelectionModel().getAnchorSelectionIndex();
                    list.setValueIsAdjusting(true);
                    list.addSelectionInterval(index, index);
                    list.getSelectionModel().setAnchorSelectionIndex(oldAnchor);
                    list.setValueIsAdjusting(false);
                }
            }
            else if (name == TOGGLE_AND_ANCHOR) {
                int index = adjustIndex(
                    list.getSelectionModel().getLeadSelectionIndex(), list);

                if (list.isSelectedIndex(index)) {
                    list.removeSelectionInterval(index, index);
                } else {
                    list.addSelectionInterval(index, index);
                }
            }
            else if (name == EXTEND_TO) {
                changeSelection(
                    list, EXTEND_SELECTION,
                    adjustIndex(list.getSelectionModel().getLeadSelectionIndex(), list),
                    0);
            }
            else if (name == MOVE_SELECTION_TO) {
                changeSelection(
                    list, CHANGE_SELECTION,
                    adjustIndex(list.getSelectionModel().getLeadSelectionIndex(), list),
                    0);
            }
        }

        @Override
        public boolean isEnabled(Object c) {
        	return accept(c);
        }

        protected int getNextPageIndex(JList<?> list, int direction) {
            if (getElementCount(list) == 0) {
                return -1;
            }

            int index = -1;
            Rectangle visRect = list.getVisibleRect();
            ListSelectionModel lsm = list.getSelectionModel();
            int lead = adjustIndex(lsm.getLeadSelectionIndex(), list);
            Rectangle leadRect =
                (lead==-1) ? new Rectangle() : list.getCellBounds(lead, lead);
  
            if (list.getLayoutOrientation() == JList.VERTICAL_WRAP &&
                list.getVisibleRowCount() <= 0) {
                if (!list.getComponentOrientation().isLeftToRight()) {
                    direction = -direction;
                }
                // apply for horizontal scrolling: the step for next
                // page index is number of visible columns
                if (direction < 0) {
                    // left
                    visRect.x = leadRect.x + leadRect.width - visRect.width;
                    Point p = new Point(visRect.x - 1, leadRect.y);
                    index = list.locationToIndex(p);
                    Rectangle cellBounds = list.getCellBounds(index, index);
                    if (visRect.intersects(cellBounds)) {
                        p.x = cellBounds.x - 1;
                        index = list.locationToIndex(p);
                        cellBounds = list.getCellBounds(index, index);
                    }
                    // this is necessary for right-to-left orientation only
                    if (cellBounds.y != leadRect.y) {
                        p.x = cellBounds.x + cellBounds.width;
                        index = list.locationToIndex(p);
                    }
                }
                else {
                    // right
                    visRect.x = leadRect.x;
                    Point p = new Point(visRect.x + visRect.width, leadRect.y);
                    index = list.locationToIndex(p);
                    Rectangle cellBounds = list.getCellBounds(index, index);
                    if (visRect.intersects(cellBounds)) {
                        p.x = cellBounds.x + cellBounds.width;
                        index = list.locationToIndex(p);
                        cellBounds = list.getCellBounds(index, index);
                    }
                    if (cellBounds.y != leadRect.y) {
                        p.x = cellBounds.x - 1;
                        index = list.locationToIndex(p);
                    }
                }
            }
            else {
                if (direction < 0) {
                    // up
                    // go to the first visible cell
                    Point p = new Point(leadRect.x, visRect.y);
                    index = list.locationToIndex(p);
                    if (lead <= index) {
                        // if lead is the first visible cell (or above it)
                        // adjust the visible rect up
                        visRect.y = leadRect.y + leadRect.height - visRect.height;
                        p.y = visRect.y;
                        index = list.locationToIndex(p);
                        Rectangle cellBounds = list.getCellBounds(index, index);
                        // go one cell down if first visible cell doesn't fit
                        // into adjasted visible rectangle
                        if (cellBounds.y < visRect.y) {
                            p.y = cellBounds.y + cellBounds.height;
                            index = list.locationToIndex(p);
                            cellBounds = list.getCellBounds(index, index);
                        }
                        // if index isn't less then lead
                        // try to go to cell previous to lead
                        if (cellBounds.y >= leadRect.y) {
                            p.y = leadRect.y - 1;
                            index = list.locationToIndex(p);
                        }
                    }
                }
                else {
                    // down
                    // go to the last completely visible cell
                    Point p = new Point(leadRect.x,
                                        visRect.y + visRect.height - 1);
                    index = list.locationToIndex(p);
                    Rectangle cellBounds = list.getCellBounds(index, index);
                    // go up one cell if last visible cell doesn't fit
                    // into visible rectangle
                    if (cellBounds.y + cellBounds.height >
                        visRect.y + visRect.height) {
                        p.y = cellBounds.y - 1;
                        index = list.locationToIndex(p);
                        cellBounds = list.getCellBounds(index, index);
                        index = Math.max(index, lead);
                    }
                    
                    if (lead >= index) {
                        // if lead is the last completely visible index
                        // (or below it) adjust the visible rect down
                        visRect.y = leadRect.y;
                        p.y = visRect.y + visRect.height - 1;
                        index = list.locationToIndex(p);
                        cellBounds = list.getCellBounds(index, index);
                        // go one cell up if last visible cell doesn't fit
                        // into adjasted visible rectangle
                        if (cellBounds.y + cellBounds.height >
                            visRect.y + visRect.height) {
                            p.y = cellBounds.y - 1;
                            index = list.locationToIndex(p);
                            cellBounds = list.getCellBounds(index, index);
                        }
                        // if index isn't greater then lead
                        // try to go to cell next after lead
                        if (cellBounds.y <= leadRect.y) {
                            p.y = leadRect.y + leadRect.height;
                            index = list.locationToIndex(p);
                        }
                    }
                }
            }
            return index;
        }

        /**
         * When scroll down makes selected index the last completely visible
         * index. When scroll up makes selected index the first visible index.
         * Adjust visible rectangle respect to list's component orientation.
         */
        protected void adjustScrollPositionIfNecessary(JList<?> list, int index,
                                                     int direction) {
            if (direction == 0) {
                return;
            }
            Rectangle cellBounds = list.getCellBounds(index, index);
            Rectangle visRect = list.getVisibleRect();
            if (cellBounds != null && !visRect.contains(cellBounds)) {
                if (list.getLayoutOrientation() == JList.VERTICAL_WRAP &&
                    list.getVisibleRowCount() <= 0) {
                    // horizontal
                    if (list.getComponentOrientation().isLeftToRight()) {
                        if (direction > 0) {
                            // right for left-to-right
                            int x =Math.max(0,
                                cellBounds.x + cellBounds.width - visRect.width);
                            int startIndex =
                                list.locationToIndex(new Point(x, cellBounds.y));
                            Rectangle startRect = list.getCellBounds(startIndex,
                                                                     startIndex);
                            if (startRect.x < x && startRect.x < cellBounds.x) {
                                startRect.x += startRect.width;
                                startIndex =
                                    list.locationToIndex(startRect.getLocation());
                                startRect = list.getCellBounds(startIndex,
                                                               startIndex);
                            }
                            cellBounds = startRect;
                        }
                        cellBounds.width = visRect.width;
                    }
                    else {
                        if (direction > 0) {
                            // left for right-to-left
                            int x = cellBounds.x + visRect.width;
                            int rightIndex =
                                list.locationToIndex(new Point(x, cellBounds.y));
                            Rectangle rightRect = list.getCellBounds(rightIndex,
                                                                     rightIndex);
                            if (rightRect.x + rightRect.width > x &&
                                rightRect.x > cellBounds.x) {
                                rightRect.width = 0;
                            }
                            cellBounds.x = Math.max(0,
                                rightRect.x + rightRect.width - visRect.width);
                            cellBounds.width = visRect.width;
                        }
                        else {
                            cellBounds.x += Math.max(0,
                                cellBounds.width - visRect.width);
                            // adjust width to fit into visible rectangle
                            cellBounds.width = Math.min(cellBounds.width,
                                                        visRect.width);
                        }
                    }
                }
                else {
                    // vertical
                    if (direction > 0 && 
                             (cellBounds.y < visRect.y || 
                                cellBounds.y + cellBounds.height  
                                             > visRect.y + visRect.height)) { 
                        //down
                        int y = Math.max(0,
                            cellBounds.y + cellBounds.height - visRect.height);
                        int startIndex =
                            list.locationToIndex(new Point(cellBounds.x, y));
                        Rectangle startRect = list.getCellBounds(startIndex,
                                                                 startIndex);
                        if (startRect.y < y && startRect.y < cellBounds.y) {
                            startRect.y += startRect.height;
                            startIndex = 
                                list.locationToIndex(startRect.getLocation());
                            startRect =
                                list.getCellBounds(startIndex, startIndex);
                        }
                        cellBounds = startRect;
                        cellBounds.height = visRect.height;
                    }
                    else {
                        // adjust height to fit into visible rectangle
                        cellBounds.height = Math.min(cellBounds.height, visRect.height);
                    }
                }
                list.scrollRectToVisible(cellBounds);
            }
        }

        private int getNextColumnIndex(JList<?> list, BasicXListUI ui,
                                       int amount) {
            if (list.getLayoutOrientation() != JList.VERTICAL) {
                int index = adjustIndex(list.getLeadSelectionIndex(), list);
                int size = getElementCount(list);

                if (index == -1) {
                    return 0;
                } else if (size == 1) {
                    // there's only one item so we should select it
                    return 0;
                } else if (ui == null || ui.columnCount <= 1) {
                    return -1;
                }

                int column = ui.convertModelToColumn(index);
                int row = ui.convertModelToRow(index);

                column += amount;
                if (column >= ui.columnCount || column < 0) {
                    // No wrapping.
                    return -1;
                }
                int maxRowCount = ui.getRowCount(column);
                if (row >= maxRowCount) {
                    return -1;
                }
                return ui.getModelIndex(column, row);
            }
            // Won't change the selection.
            return -1;
        }

        private int getNextIndex(JList<?> list, BasicXListUI ui, int amount) {
            int index = adjustIndex(list.getLeadSelectionIndex(), list);
            int size = getElementCount(list);

            if (index == -1) {
                if (size > 0) {
                    if (amount > 0) {
                        index = 0;
                    }
                    else {
                        index = size - 1;
                    }
                }
            } else if (size == 1) { 
                // there's only one item so we should select it
                index = 0; 
            } else if (list.getLayoutOrientation() == JList.HORIZONTAL_WRAP) { 
                if (ui != null) {
                    index += ui.columnCount * amount;
                }
            } else {
                index += amount;
            }

            return index;
        }
    }

    protected class Handler extends BasicYListUI.Handler 
    implements FocusListener
    		 , KeyListener
    		 , ListDataListener
    		 , ListSelectionListener
    		 , MouseInputListener
    		 , PropertyChangeListener
    		 , BeforeDrag 
    {
        protected int getElementCount(JList<?> list) {
        	if(list instanceof JXList<?> xlist) {
        		return xlist.getElementCount();
        	}
        	return list.getModel().getSize();
        }
    }

    private static int adjustIndex(int index, JList<?> list) {
        return index < ((JXList<?>) list).getElementCount() ? index : -1;
    }

}
