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
 *
 */
package org.jdesktop.swingx.renderer;

import java.io.File;

import javax.swing.Icon;

import junit.framework.TestCase;

import org.jdesktop.swingx.icon.ArrowIcon;
import org.jdesktop.swingx.test.XTestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author Karl George Schaefer
 * @author EUG https://github.com/homebeaver (reorg)
 */
@RunWith(JUnit4.class)
public class IconValuesTest extends TestCase {

    @Test
    public void testRadianceIcon() {
        Icon icon = ArrowIcon.factory().createNewIcon();
        
        assertEquals(icon,
                IconValues.ICON.getIcon(icon));
    }
    
    @Test
    public void testFileIcon() {
        Icon icon = XTestUtils.loadDefaultIcon();
        
        assertEquals(icon,
                IconValues.ICON.getIcon(icon));
    }
    
    @Test
    public void testFileIconWithNonFile() {
        Object o = new Object();
        
        assertEquals(IconValues.NONE.getIcon(o),
                IconValues.FILE_ICON.getIcon(o));
    }
    
    //not asserting the output of file icon just that it isn't none
    @Test
    public void testFileIconWithFile() throws Exception {
        File f = File.createTempFile("ivt", "tmp");
        f.deleteOnExit();
        
        assertNotSame(IconValues.NONE.getIcon(f),
                IconValues.FILE_ICON.getIcon(f));
    }
}
