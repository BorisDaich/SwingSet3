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
package org.jdesktop.swingx.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.logging.Logger;

import org.junit.Test;

/**
 * Unit test for {@code OS}.
 * 
 * @author Jeanette Winzenburg
 */
@SuppressWarnings("nls")
public class OSTest {
	
    private static final Logger LOG = Logger.getLogger(OSTest.class.getName());
    private static final String OS_NAME_PROP = "os.name";
    
   /**
     * Issue 1260-swingx: NPE in static initializer of OS
     */
    @Test
    public void testOSNPE() {
        String oldProperty = System.getProperty(OS_NAME_PROP);
        LOG.config("isLinux="+OS.isLinux() + ", isWindows="+OS.isWindows());
        try {
            if (oldProperty != null) {
                System.clearProperty(OS_NAME_PROP);
                assertNull(System.getProperty(OS_NAME_PROP));
            }
            boolean isLinux = OS.isLinux();
            LOG.config("System.clearProperty done: isLinux="+isLinux + ", isWindows="+OS.isWindows());
            
        } finally {
            if (oldProperty != null) {
                System.setProperty(OS_NAME_PROP, oldProperty);
                assertEquals(oldProperty, System.getProperty(OS_NAME_PROP));
            }
        }
    }
    

}
