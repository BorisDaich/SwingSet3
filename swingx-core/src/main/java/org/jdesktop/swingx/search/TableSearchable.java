/*
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
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
 */
package org.jdesktop.swingx.search;

import java.awt.Rectangle;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.renderer.StringValue;

/**
 * An Searchable implementation for use in JXTable.
 * 
 * @author Jeanette Winzenburg
 * @author EUG https://github.com/homebeaver (use in JXTreeTable)
 */
public class TableSearchable extends AbstractSearchable {

    private static final Logger LOG = Logger.getLogger(TableSearchable.class.getName());

    /** The target JXTable. */
    protected JXTable table;

    /**
     * Instantiates a TableSearchable with the given table as target.
     * 
     * @param table the JXTable to search.
     */
    public TableSearchable(JXTable table) {
        this.table = table;
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * This implementation loops through the cells in a row to find a match.
     */
    @Override
    protected void findMatchAndUpdateState(Pattern pattern, int startRow, boolean backwards) {
        SearchResult matchRow = null;
        if (backwards) {
            // CHECK: off-one end still needed?
            // Probably not - the findXX don't have side-effects any longer
            // hmmm... still needed: even without side-effects we need to
            // guarantee calling the notfound update at the very end of the loop.
            for (int r = startRow; r >= -1 && matchRow == null; r--) {
                matchRow = findMatchBackwardsInRow(pattern, r);
                updateState(matchRow);
            }
        } else {
            for (int r = startRow; r <= getSize() && matchRow == null; r++) {
                matchRow = findMatchForwardInRow(pattern, r);
                updateState(matchRow);
            }
        }
        // KEEP - JW: Needed to update if loop wasn't entered!
        // the alternative is to go one off in the loop. Hmm - which is
        // preferable?
        // updateState(matchRow);
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Implemented to search for an extension in the cell given by row and foundColumn.
     */
    @Override
    protected SearchResult findExtendedMatch(Pattern pattern, int row) {
        return findMatchAt(pattern, row, lastSearchResult.foundColumn);
    }

    /**
     * Searches forward through columns of the given row. Starts at
     * lastFoundColumn or first column if lastFoundColumn < 0. returns an
     * appropriate SearchResult if a matching cell is found in this row or null
     * if no match is found. A row index out off range results in a no-match.
     * 
     * @param pattern <code>Pattern</code> that we will try to locate
     * @param row the row to search
     * @return an appropriate <code>SearchResult</code> if a matching cell is
     *         found in this row or null if no match is found
     */
    private SearchResult findMatchForwardInRow(Pattern pattern, int row) {
        int startColumn = (lastSearchResult.foundColumn < 0) ? 0 : lastSearchResult.foundColumn;
        if (isValidIndex(row)) {
            for (int column = startColumn; column < table.getColumnCount(); column++) {
                SearchResult result = findMatchAt(pattern, row, column);
                if (result != null)
                    return result;
            }
        }
        return null;
    }

    /**
     * Searches forward through columns of the given row. Starts at
     * lastFoundColumn or first column if lastFoundColumn < 0. returns an
     * appropriate SearchResult if a matching cell is found in this row or null
     * if no match is found. A row index out off range results in a no-match.
     * 
     * @param pattern <code>Pattern</code> that we will try to locate
     * @param row the row to search
     * @return an appropriate <code>SearchResult</code> if a matching cell is
     *         found in this row or null if no match is found
     */
    private SearchResult findMatchBackwardsInRow(Pattern pattern, int row) {
        int startColumn = (lastSearchResult.foundColumn < 0) ? table.getColumnCount() - 1 
        		: lastSearchResult.foundColumn;
        if (isValidIndex(row)) {
            for (int column = startColumn; column >= 0; column--) {
                SearchResult result = findMatchAt(pattern, row, column);
                if (result != null)
                    return result;
            }
        }
        return null;
    }

    /**
     * Matches the cell content at row/col against the given Pattern. Returns an
     * appropriate SearchResult if matching or null if no matching
     * 
     * @param pattern <code>Pattern</code> that we will try to locate
     * @param row a valid row index in view coordinates
     * @param column a valid column index in view coordinates
     * @return an appropriate <code>SearchResult</code> if matching or null
     */
    protected SearchResult findMatchAt(Pattern pattern, int row, int column) {
    	String text;
    	if(table instanceof JXTreeTable) {
    		JXTreeTable tt = (JXTreeTable)table;
    		text = tt.getStringAt(row, column);
    		Object renderer = tt.getCellRenderer(row, column);
    		if(renderer instanceof JXTree) {
    			JXTree xTree = (JXTree)renderer;
    			// for hierarchical column
    			StringValue sv = (StringValue)xTree.getCellRenderer();
    			TreePath path = xTree.getPathForRow(row);
    			text = sv.getString(path.getLastPathComponent());
    			LOG.fine("hierarchical column "+column + " row="+row + " text="+text);
    		}
    	} else {
            text = table.getStringAt(row, column);
    	}
        if ((text != null) && (text.length() > 0)) {
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
            	LOG.fine("matcher.find() is TRUE for column "+column+" row="+row);
                return createSearchResult(matcher, row, column);
            }
        }
        return null;
    }

    /**
     * 
     * {@inheritDoc}
     * <p>
     * 
     * Overridden to adjust the column index to -1.
     */
    @Override
    protected int adjustStartPosition(int startIndex, boolean backwards) {
        lastSearchResult.foundColumn = -1;
        return super.adjustStartPosition(startIndex, backwards);
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Overridden to loop through all columns in a row.
     */
    @Override
    protected int moveStartPosition(int startRow, boolean backwards) {
        if (backwards) {
            lastSearchResult.foundColumn--;
            if (lastSearchResult.foundColumn < 0) {
                startRow--;
            }
        } else {
            lastSearchResult.foundColumn++;
            // check tt.getColumnCount() == table.getColumnCount()
//            if(table instanceof JXTreeTable tt) {
//            	assert table.getColumnCount() == tt.getColumnCount();
//            }

            if (lastSearchResult.foundColumn >= table.getColumnCount()) {
                lastSearchResult.foundColumn = -1;
                startRow++;
            }
        }
        return startRow;
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Overridden to check the column index of last find.
     */
    @Override
    protected boolean isEqualStartIndex(final int startIndex) {
        return super.isEqualStartIndex(startIndex) && isValidColumn(lastSearchResult.foundColumn);
    }

    /**
     * Checks if row is in range: 0 <= row < getRowCount().
     * 
     * @param column the column index to check in view coordinates.
     * @return true if the column is in range, false otherwise
     */
    private boolean isValidColumn(int column) {
        return column >= 0 && column < table.getColumnCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getSize() {
        return table.getRowCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JXTable getTarget() {
        return table;
    }

    /**
     * Configures the match highlighter to the current match. Ensures that the
     * matched cell is visible, if there is a match.
     * 
     * PRE: markByHighlighter
     * 
     */
    protected void moveMatchByHighlighter() {
        AbstractHighlighter searchHL = getConfiguredMatchHighlighter();
        // no match
        if (!hasMatch()) {
            return;
        } else {
            ensureInsertedSearchHighlighters(searchHL);
            if(table instanceof JXTreeTable) {
            	JXTreeTable tt = (JXTreeTable)table;
            	tt.scrollCellToVisible(lastSearchResult.foundRow, lastSearchResult.foundColumn);
            } else {
                table.scrollCellToVisible(lastSearchResult.foundRow, lastSearchResult.foundColumn);
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Overridden to convert the column index in the table's view coordinate
     * system to model coordinate.
     * <p>
     * 
     * PENDING JW: this is only necessary because the SearchPredicate wants its
     * highlight column in model coordinates. But code comments in the
     * SearchPredicate seem to indicate that we probably want to revise that
     * (legacy?).
     */
    @Override
    protected int convertColumnIndexToModel(int viewColumn) {
        return getTarget().convertColumnIndexToModel(viewColumn);
    }

    /**
     * Moves the row selection to the matching cell and ensures its visibility,
     * if any. Does nothing if there is no match.
     * 
     */
    protected void moveMatchBySelection() {
        if (!hasMatch()) {
            return;
        }
        int row = lastSearchResult.foundRow;
        int column = lastSearchResult.foundColumn;
        if(table instanceof JXTreeTable) {
        	JXTreeTable tt = (JXTreeTable)table;
            tt.changeSelection(row, column, false, false);
            if (!tt.getAutoscrolls()) {
                // scrolling not handled by moving selection
                Rectangle cellRect = tt.getCellRect(row, column, true);
                if (cellRect != null) {
                    tt.scrollRectToVisible(cellRect);
                }
            }
        } else {
            table.changeSelection(row, column, false, false);
            if (!table.getAutoscrolls()) {
                // scrolling not handled by moving selection
                Rectangle cellRect = table.getCellRect(row, column, true);
                if (cellRect != null) {
                    table.scrollRectToVisible(cellRect);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     */
    @Override
    protected void moveMatchMarker() {
        if (markByHighlighter()) {
            moveMatchByHighlighter();
        } else { // use selection
            moveMatchBySelection();
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     */
    @Override
    protected void removeHighlighter(Highlighter searchHighlighter) {
        if(table instanceof JXTreeTable) {
        	JXTreeTable tt = (JXTreeTable)table;
        	Object renderer = tt.getTreeCellRenderer();
        	// javax.swing.tree.TreeCellRenderer is an Interface
        	// public class DefaultTreeRenderer ... implements TreeCellRenderer {
// das kann nicht sein:
//        	if(renderer instanceof JXTree xTree) {
//        		xTree.removeHighlighter(searchHighlighter);
//        	}
            tt.removeHighlighter(searchHighlighter);
        } else {
            table.removeHighlighter(searchHighlighter);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     */
    @Override
    protected Highlighter[] getHighlighters() {
        if(table instanceof JXTreeTable) {
        	JXTreeTable tt = (JXTreeTable)table;
            return tt.getHighlighters();
        } else {
            return table.getHighlighters();
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     */
    @Override
    protected void addHighlighter(Highlighter highlighter) {
        if(table instanceof JXTreeTable) {
        	JXTreeTable tt = (JXTreeTable)table;
        	Object renderer = tt.getTreeCellRenderer();
// das kann nicht sein (wie oben):
//        	if(renderer instanceof JXTree xTree) {
//        		xTree.addHighlighter(highlighter);
//        	}
            tt.addHighlighter(highlighter);
        } else {
            table.addHighlighter(highlighter);        	
        }
    }

}