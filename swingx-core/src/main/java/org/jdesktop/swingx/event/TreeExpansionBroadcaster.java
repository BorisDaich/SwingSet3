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
 */
package org.jdesktop.swingx.event;

import java.util.logging.Logger;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;

import org.jdesktop.swingx.JXTreeTable;

/**
 * Helper to listen to TreeExpansion events and notify with a remapped source.
 * 
 * @author Jeanette Winzenburg
 */
/*
 * EUG: 
 * used in JXTreeTable
 */
public class TreeExpansionBroadcaster implements TreeExpansionListener {

    private static final Logger LOG = Logger.getLogger(TreeExpansionBroadcaster.class.getName());

    private Object source;

    private EventListenerList listeners;

    public TreeExpansionBroadcaster(Object source) {
    	LOG.info("ctor source:"+source);
        this.source = source;
    }

    public void addTreeExpansionListener(TreeExpansionListener l) {
    	LOG.info("add(TreeExpansionListener.class listener:"+l);
        getEventListenerList().add(TreeExpansionListener.class, l);
    	LOG.info("XXX now there are #listeners in EventListenerList:"+listeners.getListenerCount());
    }

    public void removeTreeExpansionListener(TreeExpansionListener l) {
        if (!hasListeners()) return;
        listeners.remove(TreeExpansionListener.class, l);
    }

    /**
     * @return
     */
    private EventListenerList getEventListenerList() {
        if (listeners == null) {
            listeners = new EventListenerList();
        }
        return listeners;
    }

    // -------------------- implements TreeExpansionListener
    @Override
    public void treeExpanded(TreeExpansionEvent event) {
        if (!hasListeners()) return;
    	LOG.info("XXX now fireTreeExpanded(retarget(event:"+event);
        fireTreeExpanded(retarget(event));
    }

    @Override
    public void treeCollapsed(TreeExpansionEvent event) {
        if (!hasListeners()) return;
        fireTreeCollapsed(retarget(event));
    }

    /**
     * @param event
     */
    private void fireTreeExpanded(TreeExpansionEvent event) {
        TreeExpansionListener[] ls = listeners.getListeners(TreeExpansionListener.class);
        for (int i = ls.length - 1; i >= 0; i--) {
            ls[i].treeExpanded(event);
        }
    }

    /**
     * @param event
     */
    private void fireTreeCollapsed(TreeExpansionEvent event) {
        TreeExpansionListener[] ls = listeners.getListeners(TreeExpansionListener.class);
        for (int i = ls.length - 1; i >= 0; i--) {
            ls[i].treeCollapsed(event);
        }
    }

    /**
     * @param event
     * @return
     */
    private TreeExpansionEvent retarget(TreeExpansionEvent event) {
        return new TreeExpansionEvent(source, event.getPath());
    }

    /**
     * @return
     */
    private boolean hasListeners() {
        return listeners != null && listeners.getListenerCount() > 0;
    }

}
