/*
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
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
package org.jdesktop.swingx.multislider;

import java.util.List;

/**
 * @author jm158417 Joshua Marinacci joshy
 */
public interface MultiThumbModel<E> extends Iterable<Thumb<E>> {
    
	/**
	 * @return minimum Value
	 */
    public float getMinimumValue();
    /**
     * TODO doc
     * @param min minimum Value
     */
    public void setMinimumValue(float min);

	/**
	 * @return maximum Value
	 */
    public float getMaximumValue();
    /**
     * TODO doc
     * @param max maximum Value
     */
    public void setMaximumValue(float max);
    
    /**
     * TODO doc
     * @param value float
     * @param obj generic
     * @return int
     */
    public int addThumb(float value, E obj);
    /**
     * TODO doc
     * @param value float
     * @param obj generic
     * @param index of thumb
     */
    public void insertThumb(float value, E obj, int index);
    /**
     * remover
     * @param index of thumb
     */
    public void removeThumb(int index);
    /**
     * Count
     * @return no of thumbs
     */
    public int getThumbCount();
    /**
     * getter
     * @param index of thumb
     * @return thumb
     */
    public Thumb<E> getThumbAt(int index);
    /**
     * finder
     * @param thumb generic Thumb
     * @return index
     */
    public int getThumbIndex(Thumb<E> thumb);
    /**
     * TODO maven-javadoc-plugin 3.3.2 needs a doc here
     * @return List of Thumbs
     */
    public List<Thumb<E>> getSortedThumbs();
    
    /**
     * TODO doc
     * @param thumb TODO doc
     */
    public void thumbPositionChanged(Thumb<E> thumb);
    
    /**
     * TODO doc
     * @param thumb TODO doc
     */
    public void thumbValueChanged(Thumb<E> thumb);
    
    /**
     * Adds a listener for the <code>ThumbDataEvent</code>
     * posted after the thumb data changes.
     *
     * @param   listener the listener to add
     * @see     #removeThumbDataListener
     */
    public void addThumbDataListener(ThumbDataListener listener);
    /**
     * Removes a listener previously added with <code>addThumbDataListener</code>.
     *
     * @see     #addThumbDataListener
     * @param   listener the listener to remove
     */
    public void removeThumbDataListener(ThumbDataListener listener);
}
