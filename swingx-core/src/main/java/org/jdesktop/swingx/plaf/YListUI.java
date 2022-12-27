package org.jdesktop.swingx.plaf;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeListener;

import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ListUI;

/**
 * Similar to {@code javax.swing.plaf.basic.ListUI} this abstract class
 * defines a pluggable look and feel delegate for {@code JYList} and {@code JXList}.
 * 
 * Most of the implementation is like in {@code javax.swing.plaf.basic.BasicListUI}
 * 
 * @see org.jdesktop.swingx.JYList
 * @see org.jdesktop.swingx.JXList
 */
public class YListUI extends ListUI {

	protected JList<Object> list = null;
	protected CellRendererPane rendererPane;
    // Listeners that this UI attaches to the JList
	protected FocusListener focusListener; // interface FocusListener extends EventListener
	protected MouseInputListener mouseInputListener;
	protected ListSelectionListener listSelectionListener;
	protected ListDataListener listDataListener;
	protected PropertyChangeListener propertyChangeListener;
	protected int[] cellHeights = null;
	protected int cellHeight = -1;
	protected int cellWidth = -1;

    /* The bits below define JList property changes that affect layout.
     * When one of these properties changes we set a bit in
     * updateLayoutStateNeeded.  The change is dealt with lazily, see
     * maybeUpdateLayoutState.  Changes to the JLists model, e.g. the
     * models length changed, are handled similarly, see DataListener.
     */
	// copied from javax.swing.plaf.basic.BasicListUI
    protected static final int modelChanged = 1 << 0;
    protected static final int selectionModelChanged = 1 << 1;
    protected static final int fontChanged = 1 << 2;
    protected static final int fixedCellWidthChanged = 1 << 3;
    protected static final int fixedCellHeightChanged = 1 << 4;
    protected static final int prototypeCellValueChanged = 1 << 5;
    protected static final int cellRendererChanged = 1 << 6;
    protected static final int layoutOrientationChanged = 1 << 7;
    protected static final int heightChanged = 1 << 8;
    protected static final int widthChanged = 1 << 9;
    protected static final int componentOrientationChanged = 1 << 10;
    
	protected int updateLayoutStateNeeded = modelChanged;

    // like javax.swing.plaf.basic.BasicListUI :
	protected int layoutOrientation;
	protected int listHeight;
	protected int listWidth;

    // Following ivars are used if the list is laying out horizontally
	protected int columnCount;
	protected int preferredHeight;
	protected int rowsPerColumn;

	protected long timeFactor = 1000L;
    protected boolean isFileList = false;
    protected boolean isLeftToRight = true;

    /**
     * {@inheritDoc}
     */
    @Override // implement abstract methods defined in abstract class ListUI
    public int locationToIndex(JList<?> list, Point location) {
        maybeUpdateLayoutState();
        return convertLocationToModel(location.x, location.y);
    }

    /**
     * {@inheritDoc}
     */
    @Override // implement abstract methods defined in abstract class ListUI
    public Point indexToLocation(JList<?> list, int index) {
        maybeUpdateLayoutState();
        Rectangle rect = getCellBounds(list, index, index);

        if (rect != null) {
            return new Point(rect.x, rect.y);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override // implement abstract methods defined in abstract class ListUI
    public Rectangle getCellBounds(JList<?> list, int index1, int index2) {
        maybeUpdateLayoutState();

        int minIndex = Math.min(index1, index2);
        int maxIndex = Math.max(index1, index2);

        if (minIndex >= list.getModel().getSize()) {
            return null;
        }

        Rectangle minBounds = getCellBounds(list, minIndex);

        if (minBounds == null) {
            return null;
        }
        if (minIndex == maxIndex) {
            return minBounds;
        }
        Rectangle maxBounds = getCellBounds(list, maxIndex);

        if (maxBounds != null) {
            if (layoutOrientation == JList.HORIZONTAL_WRAP) {
                int minRow = convertModelToRow(minIndex);
                int maxRow = convertModelToRow(maxIndex);

                if (minRow != maxRow) {
                    minBounds.x = 0;
                    minBounds.width = list.getWidth();
                }
            }
            else if (minBounds.x != maxBounds.x) {
                // Different columns
                minBounds.y = 0;
                minBounds.height = list.getHeight();
            }
            minBounds.add(maxBounds);
        }
        return minBounds;
    }

    protected ListModel<Object> getViewModel() {
    	return list.getModel();
    }
    protected int getElementCount() {
    	return list.getModel().getSize();
    }
    
    /**
     * {@inheritDoc} <p>
     * Initializes <code>super.list</code> with <code>JComponent c</code> by calling
     * protected void installDefaults()
     * protected void installListeners()
     * protected void installKeyboardActions()
     */
    @Override // overrides javax.swing.plaf.ComponentUI.installUI
    public void installUI(JComponent c) {
        @SuppressWarnings("unchecked")
        JList<Object> tmp = (JList<Object>)c;
        list = tmp;

        layoutOrientation = list.getLayoutOrientation();

        rendererPane = new CellRendererPane();
        list.add(rendererPane);

        columnCount = 1;

        updateLayoutStateNeeded = modelChanged;
        isLeftToRight = list.getComponentOrientation().isLeftToRight();

        installDefaults();
        installListeners();
        installKeyboardActions();
    }
    protected void installDefaults() {
    	// to be implemented in subclass
    }
    protected void installListeners() {
    	// to be implemented in subclass
    }
    protected void installKeyboardActions() {
    	// to be implemented in subclass
    }

    /**
     * Gets the bounds of the specified model index, returning the resulting bounds, 
     * or null if <code>index</code> is not valid.
     * @param list JList
     * @param index int
     * @return Rectangle
     */
    // exact copy from javax.swing.plaf.basic.BasicListUI , but not private => protected visiblility
    protected Rectangle getCellBounds(JList<?> list, int index) {
        maybeUpdateLayoutState();

        int row = convertModelToRow(index);
        int column = convertModelToColumn(index);

        if (row == -1 || column == -1) {
            return null;
        }

        Insets insets = list.getInsets();
        int x;
        int w = cellWidth;
        int y = insets.top;
        int h;
        switch (layoutOrientation) {
        case JList.VERTICAL_WRAP:
        case JList.HORIZONTAL_WRAP:
            if (isLeftToRight) {
                x = insets.left + column * cellWidth;
            } else {
                x = list.getWidth() - insets.right - (column+1) * cellWidth;
            }
            y += cellHeight * row;
            h = cellHeight;
            break;
        default:
            x = insets.left;
            if (cellHeights == null) {
                y += (cellHeight * row);
            }
            else if (row >= cellHeights.length) {
                y = 0;
            }
            else {
                for(int i = 0; i < row; i++) {
                    y += cellHeights[i];
                }
            }
            w = list.getWidth() - (insets.left + insets.right);
            h = getRowHeight(index);
            break;
        }
        return new Rectangle(x, y, w, h);
    }

    /**
     * Returns the closest location to the model index of the passed in location.
     * @param x int
     * @param y int
     * @return model index
     */
    // exact copy from javax.swing.plaf.basic.BasicListUI , but not private => package visibility
    int convertLocationToModel(int x, int y) {
        int row = convertLocationToRow(x, y, true);
        int column = convertLocationToColumn(x, y);

        if (row >= 0 && column >= 0) {
            return getModelIndex(column, row);
        }
        return -1;
    }
    /**
     * Returns the row at location x/y.
     *
     * @param x int
     * @param y0 int
     * @param closest If true and the location doesn't exactly match a
     *                particular location, this will return the closest row.
     * @return int row at location x/y
     */
    // exact copy from javax.swing.plaf.basic.BasicListUI , but not private => protected visibility
    protected int convertLocationToRow(int x, int y0, boolean closest) {
        int size = getElementCount();

        if (size <= 0) {
            return -1;
        }
        Insets insets = list.getInsets();
        if (cellHeights == null) {
            int row = (cellHeight == 0) ? 0 :
                           ((y0 - insets.top) / cellHeight);
            if (closest) {
                if (row < 0) {
                    row = 0;
                }
                else if (row >= size) {
                    row = size - 1;
                }
            }
            return row;
        }
        else if (size > cellHeights.length) {
            return -1;
        }
        else {
            int y = insets.top;
            int row = 0;

            if (closest && y0 < y) {
                return 0;
            }
            int i;
            for (i = 0; i < size; i++) {
                if ((y0 >= y) && (y0 < y + cellHeights[i])) {
                    return row;
                }
                y += cellHeights[i];
                row += 1;
            }
            return i - 1;
        }
    }
    /**
     * Returns the closest column to the passed in location.
     */
    // exact copy from javax.swing.plaf.basic.BasicListUI , but not private => protected visibility
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
     * Returns the model index for the specified display location.
     * If <code>column</code>x<code>row</code> is beyond the length of the model, 
     * this will return the model size - 1.
     */
    // copied from javax.swing.plaf.basic.BasicListUI , but not private => protected visibility
    protected int getModelIndex(int column, int row) {
        switch (layoutOrientation) {
        case JList.VERTICAL_WRAP:
            return Math.min(getElementCount() - 1, rowsPerColumn * column + Math.min(row, rowsPerColumn-1));
        case JList.HORIZONTAL_WRAP:
            return Math.min(getElementCount() - 1, row * columnCount + column);
        default:
            return row;
        }
    }

    /**
     * Returns the row that the model index <code>index</code> will be displayed in.
     */
    // copied from javax.swing.plaf.basic.BasicListUI , but not private => protected visibility
    protected int convertModelToRow(int index) {
        int size = getElementCount();

        if ((index < 0) || (index >= size)) {
            return -1;
        }

        if (layoutOrientation != JList.VERTICAL && columnCount > 1 && rowsPerColumn > 0) {
            if (layoutOrientation == JList.VERTICAL_WRAP) {
                return index % rowsPerColumn;
            }
            return index / columnCount;
        }
        return index;
    }
    
    /**
     * Returns the column that the model index <code>index</code> will be displayed in.
     */
    // copied from javax.swing.plaf.basic.BasicListUI , but not private => protected visibility
    protected int convertModelToColumn(int index) {
        int size = getElementCount();

        if ((index < 0) || (index >= size)) {
            return -1;
        }

        if (layoutOrientation != JList.VERTICAL && rowsPerColumn > 0 && columnCount > 1) {
            if (layoutOrientation == JList.VERTICAL_WRAP) {
                return index / rowsPerColumn;
            }
            return index % columnCount;
        }
        return 0;
    }

    /**
     * Returns the height of the cell at the passed in location.
     */
    // exact copy from javax.swing.plaf.basic.BasicListUI , but not private => protected visibility
    protected int getHeight(int column, int row) {
        if (column < 0 || column > columnCount || row < 0) {
            return -1;
        }
        if (layoutOrientation != JList.VERTICAL) {
            return cellHeight;
        }
        if (row >= list.getModel().getSize()) {
            return -1;
        }
        return (cellHeights == null) ? cellHeight :
                           ((row < cellHeights.length) ? cellHeights[row] : -1);
    }

    /**
     * If updateLayoutStateNeeded is non zero, call updateLayoutState() and reset
     * updateLayoutStateNeeded.  This method should be called by methods
     * before doing any computation based on the geometry of the list.
     * For example it's the first call in paint() and getPreferredSize().
     *
     * @see javax.swing.plaf.basic.BasicListUI#updateLayoutState
     */
    // exact copy from javax.swing.plaf.basic.BasicListUI
    protected void maybeUpdateLayoutState() {
        if (updateLayoutStateNeeded != 0) {
            updateLayoutState();
            updateLayoutStateNeeded = 0;
        }
    }

    /**
     * Recompute the value of cellHeight or cellHeights based
     * and cellWidth, based on the current font and the current
     * values of fixedCellWidth, fixedCellHeight, and prototypeCellValue.
     *
     * @see javax.swing.plaf.basic.BasicListUI#updateLayoutState
     */
    // copy from javax.swing.plaf.basic.BasicListUI + protected
    protected void updateLayoutState() {
        /* If both JList fixedCellWidth and fixedCellHeight have been
         * set, then initialize cellWidth and cellHeight, and set
         * cellHeights to null.
         */

        int fixedCellHeight = list.getFixedCellHeight();
        int fixedCellWidth = list.getFixedCellWidth();

        cellWidth = (fixedCellWidth != -1) ? fixedCellWidth : -1;

        if (fixedCellHeight != -1) {
            cellHeight = fixedCellHeight;
            cellHeights = null;
        }
        else {
            cellHeight = -1;
            cellHeights = new int[getElementCount()];
        }

        /* If either of  JList fixedCellWidth and fixedCellHeight haven't
         * been set, then initialize cellWidth and cellHeights by
         * scanning through the entire model.  Note: if the renderer is
         * null, we just set cellWidth and cellHeights[*] to zero,
         * if they're not set already.
         */

        if ((fixedCellWidth == -1) || (fixedCellHeight == -1)) {

            ListModel<Object> dataModel = getViewModel();
            int dataModelSize = dataModel.getSize();
            ListCellRenderer<Object> renderer = list.getCellRenderer();

            if (renderer != null) {
                for(int index = 0; index < dataModelSize; index++) {
                    Object value = dataModel.getElementAt(index);
                    Component c = renderer.getListCellRendererComponent(list, value, index, false, false);
                    rendererPane.add(c);
                    Dimension cellSize = c.getPreferredSize();
                    if (fixedCellWidth == -1) {
                        cellWidth = Math.max(cellSize.width, cellWidth);
                    }
                    if (fixedCellHeight == -1) {
                        cellHeights[index] = cellSize.height;
                    }
                }
            }
            else {
                if (cellWidth == -1) {
                    cellWidth = 0;
                }
                if (cellHeights == null) {
                    cellHeights = new int[dataModelSize];
                }
                for(int index = 0; index < dataModelSize; index++) {
                    cellHeights[index] = 0;
                }
            }
        }

        columnCount = 1;
        if (layoutOrientation != JList.VERTICAL) {
            updateHorizontalLayoutState(fixedCellWidth, fixedCellHeight);
        }
    }

    /**
     * Returns the height of the specified row based on the current layout.
     *
     * @param row a row
     * @return the specified row height or -1 if row isn't valid
     * @see #convertYToRow
     * @see #convertRowToY
     * @see #updateLayoutState
     */
    // exact copy from javax.swing.plaf.basic.BasicListUI
    private int getRowHeight(int row) {
        return getHeight(0, row);
    }

    /**
     * @see javax.swing.plaf.basic.BasicListUI#convertYToRow(int)
     */
    protected int convertYToRow(int y0) {
        return convertLocationToRow(0, y0, false);
    }
    /**
     * @see javax.swing.plaf.basic.BasicListUI#convertYToRow(int)
     */
    protected int convertRowToY(int row) {
        if (row >= getRowCount(0) || row < 0) {
            return -1;
        }
        Rectangle bounds = getCellBounds(list, row, row);
        return bounds.y;
    }

    /**
     * Returns the number of rows in the given column.
     */
    // copied from javax.swing.plaf.basic.BasicListUI , but not private => protected visibility
    protected int getRowCount(int column) {
        if (column < 0 || column >= columnCount) {
            return -1;
        }
        if (layoutOrientation == JList.VERTICAL || (column == 0 && columnCount == 1)) {
            return getElementCount();
        }
        if (column >= columnCount) {
            return -1;
        }
        if (layoutOrientation == JList.VERTICAL_WRAP) {
            if (column < (columnCount - 1)) {
                return rowsPerColumn;
            }
            return getElementCount() - (columnCount - 1) * rowsPerColumn;
        }
        // JList.HORIZONTAL_WRAP
        int diff = columnCount - (columnCount * rowsPerColumn - getElementCount());

        if (column >= diff) {
            return Math.max(0, rowsPerColumn - 1);
        }
        return rowsPerColumn;
    }

    /**
     * Invoked when the list is layed out horizontally (VERTICAL_WRAP or HORIZONTAL_WRAP) 
     * to determine how many columns to create.
     * <p>
     * This updates the <code>rowsPerColumn, </code><code>columnCount</code>,
     * <code>preferredHeight</code> and potentially <code>cellHeight</code>
     * instance variables.
     */
    // exact copy from javax.swing.plaf.basic.BasicListUI
    private void updateHorizontalLayoutState(int fixedCellWidth, int fixedCellHeight) {
        int visRows = list.getVisibleRowCount();
        int dataModelSize = list.getModel().getSize();
        Insets insets = list.getInsets();

        listHeight = list.getHeight();
        listWidth = list.getWidth();

        if (dataModelSize == 0) {
            rowsPerColumn = columnCount = 0;
            preferredHeight = insets.top + insets.bottom;
            return;
        }

        int height;

        if (fixedCellHeight != -1) {
            height = fixedCellHeight;
        }
        else {
            // Determine the max of the renderer heights.
            int maxHeight = 0;
            if (cellHeights.length > 0) {
                maxHeight = cellHeights[cellHeights.length - 1];
                for (int counter = cellHeights.length - 2;
                     counter >= 0; counter--) {
                    maxHeight = Math.max(maxHeight, cellHeights[counter]);
                }
            }
            height = cellHeight = maxHeight;
            cellHeights = null;
        }
        // The number of rows is either determined by the visible row
        // count, or by the height of the list.
        rowsPerColumn = dataModelSize;
        if (visRows > 0) {
            rowsPerColumn = visRows;
            columnCount = Math.max(1, dataModelSize / rowsPerColumn);
            if (dataModelSize > 0 && dataModelSize > rowsPerColumn &&
                dataModelSize % rowsPerColumn != 0) {
                columnCount++;
            }
            if (layoutOrientation == JList.HORIZONTAL_WRAP) {
                // Because HORIZONTAL_WRAP flows differently, the
                // rowsPerColumn needs to be adjusted.
                rowsPerColumn = (dataModelSize / columnCount);
                if (dataModelSize % columnCount > 0) {
                    rowsPerColumn++;
                }
            }
        }
        else if (layoutOrientation == JList.VERTICAL_WRAP && height != 0) {
            rowsPerColumn = Math.max(1, (listHeight - insets.top -
                                         insets.bottom) / height);
            columnCount = Math.max(1, dataModelSize / rowsPerColumn);
            if (dataModelSize > 0 && dataModelSize > rowsPerColumn &&
                dataModelSize % rowsPerColumn != 0) {
                columnCount++;
            }
        }
        else if (layoutOrientation == JList.HORIZONTAL_WRAP && cellWidth > 0 &&
                 listWidth > 0) {
            columnCount = Math.max(1, (listWidth - insets.left -
                                       insets.right) / cellWidth);
            rowsPerColumn = dataModelSize / columnCount;
            if (dataModelSize % columnCount > 0) {
                rowsPerColumn++;
            }
        }
        preferredHeight = rowsPerColumn * cellHeight + insets.top +
                              insets.bottom;
    }
    
}
