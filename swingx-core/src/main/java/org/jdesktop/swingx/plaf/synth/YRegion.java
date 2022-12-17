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
package org.jdesktop.swingx.plaf.synth;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.plaf.synth.Region;

/**
 * Extended Region to register custom component delegates.
 * 
 * like XRegion
 */
public class YRegion extends Region {

    static Map<String, YRegion> uiToYRegionMap = new HashMap<String, YRegion>();
    public static final Region YLIST = new YRegion("YList", null, false, "YListUI", LIST);
    
    /** the Region which identifies the base styles */
    private Region parent;
    
    /**
     * Creates a Region with the specified name.
     * 
     * @param name Name of the region
     * @param subregion Whether or not this is a subregion.
     * @param realUI String that will be returned from
     *           <code>component.getUIClassID</code>. 
     * @param parent the parent region which this is extending.          
     */
    public YRegion(String name, String dummyUI, boolean subregion, String realUI, Region parent) {
        super(name, dummyUI, subregion);
        this.parent = parent;
        if (realUI != null) {
            uiToYRegionMap.put(realUI, this);
        }
    }
    
    /**
     * Returns a region appropriate for the component. 
     * 
     * @param component the component to get the region for
     * @param useParent a boolean indicating whether or not to return a fallback
     *    of the Region, if available
     * @return a region for the component or null if not available.
     */
    public static Region getYRegion(JComponent component, boolean useParent) {
        YRegion region = uiToYRegionMap.get(component.getUIClassID());
        if (region != null) {
            return useParent && region.parent != null ? region.parent : region;
        }
        return region;
    }
}
