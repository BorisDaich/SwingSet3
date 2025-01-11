/*
 * Copyright 2008 Sun Microsystems, Inc., 4150 Network Circle,
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
package org.jdesktop.swingx.renderer;

import java.awt.Color;

import javax.swing.JComboBox;

/**
 * ComboBox specific <code>CellContext</code>.
 */
/*

es gibt keinen ctor - nur implizit. Also new ComboBoxContext()
 */
@SuppressWarnings("serial")
public class ComboBoxContext extends CellContext {

    /**
     * Sets state of the cell's context. Note that the component might be null
     * to indicate a cell without a concrete context. All accessors must cope
     * with.
     * 
     * @param component the component the cell resides on, might be null
     * @param value the content value of the cell
     * @param row the cell's row index in view coordinates
     * @param column the cell's column index in view coordinates
     * @param selected the cell's selected state
     * @param focused the cell's focused state
     * @param expanded the cell's expanded state
     * @param leaf the cell's leaf state
     */
    public void installContext(JComboBox<?> component, Object value, int row, int column,
            boolean selected, boolean focused, boolean expanded, boolean leaf) {
        this.component = component;
        installState(value, row, column, selected, focused, expanded, leaf);
/* in super: installContext
        this.value = value;
        this.row = row;
        this.column = column;
        this.selected = selected;
        this.focused = focused;
        this.expanded = expanded;
        this.leaf = leaf;
 */
        this.dropOn = checkDropOnState();
    }

    /**
     * 
     */
    private boolean checkDropOnState() {
        if ((getComponent() == null)) {
            return false;
        }
        return false;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public JComboBox<?> getComponent() {
        return (JComboBox<?>) super.getComponent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Color getSelectionBackground() {
        Color selection = null;
        if (isDropOn()) {
            selection = getDropCellBackground();
            if (selection != null) return selection;
        }
        return selection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Color getSelectionForeground() {
        Color selection = null;
        if (isDropOn()) {
            selection = getDropCellForeground();
            if (selection != null) return selection;
        }
        return selection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getUIPrefix() {
        return "ComboBox.";
    }
     
}