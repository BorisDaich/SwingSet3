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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * implements a MultiThumbModel
 * @author jm158417 Joshua Marinacci joshy
 */
//                             interface MultiThumbModel<E> extends Iterable<Thumb<E>>
// AbstractMultiThumbModel<E> implements MultiThumbModel<E> 
public class DefaultMultiThumbModel<E> extends AbstractMultiThumbModel<E> {
    
    /** the collection of thumbs */
    protected List<Thumb<E>>thumbs = new ArrayList<Thumb<E>>();
    
    /** Creates a new instance of DefaultMultiThumbModel */
    public DefaultMultiThumbModel() {
        setMinimumValue(0.0f);
        setMaximumValue(1.0f);
    }

    /**
     * {@inheritDoc} <p>
     * returns the index of the newly added thumb
     */
    @Override
    public int addThumb(float value, E obj) {
        Thumb<E> thumb = new Thumb<E>(this);
        thumb.setPosition(value);
        thumb.setObject(obj);
        thumbs.add(thumb);
        int n = thumbs.size();
        ThumbDataEvent evt = new ThumbDataEvent(this,-1,thumbs.size()-1,thumb);
        for(ThumbDataListener tdl : thumbDataListeners) {
            tdl.thumbAdded(evt);
        }
        return n-1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertThumb(float value, E obj, int index) {
        Thumb<E> thumb = new Thumb<E>(this);
        thumb.setPosition(value);
        thumb.setObject(obj);
        thumbs.add(index,thumb);
        ThumbDataEvent evt = new ThumbDataEvent(this,-1,index,thumb);
        for(ThumbDataListener tdl : thumbDataListeners) {
            tdl.thumbAdded(evt);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeThumb(int index) {
        Thumb<E> thumb = thumbs.remove(index);
        ThumbDataEvent evt = new ThumbDataEvent(this,-1,index,thumb);
        for(ThumbDataListener tdl : thumbDataListeners) {
            tdl.thumbRemoved(evt);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getThumbCount() {
        return thumbs.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Thumb<E> getThumbAt(int index) {
        return thumbs.get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Thumb<E>> getSortedThumbs() {
        List<Thumb<E>> list = new ArrayList<Thumb<E>>();
        list.addAll(thumbs);
        Collections.sort(list, new Comparator<Thumb<E>>() {
            @Override
            public int compare(Thumb<E> o1, Thumb<E> o2) {
                float f1 = o1.getPosition();
                float f2 = o2.getPosition();
                if(f1<f2) {
                    return -1;
                }
                if(f1>f2) {
                    return 1;
                }
                return 0;
            }
        });
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Thumb<E>> iterator() {
        return thumbs.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getThumbIndex(Thumb<E> thumb) {
        return thumbs.indexOf(thumb);
    }
}
