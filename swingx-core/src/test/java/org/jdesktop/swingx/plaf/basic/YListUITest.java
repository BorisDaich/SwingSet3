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
package org.jdesktop.swingx.plaf.basic;

import java.util.Arrays;
import java.util.logging.Logger;

import javax.swing.ActionMap;
import javax.swing.JList;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JYList;
import org.jdesktop.swingx.plaf.basic.core.LazyActionMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Contains base tests for extended ui-delegates of JYList UI.
 * Like XListUITest in org.jdesktop.swingx.plaf.basic.core
 * @author Jeanette Winzenburg
 * @author EUG https://github.com/homebeaver (reorg)
 */
@RunWith(JUnit4.class)
public class YListUITest extends InteractiveTestCase {
    
    private static final Logger LOG = Logger.getLogger(YListUITest.class.getName());
    
    /**
     * Issue #1495-swingx: NPE in getBaseline()
     */
    @Test
    public void testBaselineNPE() {
        JYList<Object> list = new JYList<Object>();
        list.getUI().getBaseline(list, 1, 1);
    }
    
    /**
     * Issue #1495-swingx: NPE in getBaseline()
     */
    @Test (expected = NullPointerException.class)
    public void testBaselineNPEFixThrowsOnNullComponent() {
        JYList<Object> list = new JYList<Object>();
        list.getUI().getBaseline(null, 1, 1);
    }

    /**
     * For SynthLF, mapping of ui-delegate happens statically in Region: for all
     * known regions it registers the SynthLookAndFeel as delegate factory. First
     * time around, Region.XLIST is not yet known - xdelegate used. Second time
     * around, the new region is known, factory replaced with SynthLF which can't
     * handle ... so need to hide XLIST from registration.<p>
     * 
     * PENDING JW: not entirely sure as to the why, could be special to our 
     * addon mechanism?
     * 
     * @throws Exception
     */
    @Test
    public void testSynthXUIFound() throws Exception {
        if (!hasLookAndFeel("Nimbus")) {
            LOG.info("can't run - no Nimbus");
            return;
        }
        LookAndFeel lf = UIManager.getLookAndFeel();
        try {
            setLookAndFeel("Nimbus");
            JYList<Object> list = new JYList<Object>();
            setLookAndFeel("Metal");
            list.updateUI();
            setLookAndFeel("Nimbus");
            list.updateUI();
        } finally {
            UIManager.setLookAndFeel(lf);
        }
    }

    @Test
    public void testExtendedClassID() {
        JYList<Object> list = new JYList<Object>();
        assertSame(JYList.uiClassID, list.getUIClassID());
    }
    
    @Test
    public void testHasExtendedUI() {
        JYList<Object> list = new JYList<Object>();
        assertTrue("ylist must have BasicYListUI instead of " + list.getUI().getClass().getSimpleName(),
                list.getUI() instanceof BasicYListUI);
        
    }
    
    /**
     * Test that there are different actions installed for base JList and JYList.
     */
    @Test
    public void testActionMaps() {
        JYList<Object> list = new JYList<Object>();
        JList<Object> core = new JList<Object>();
        ActionMap map = list.getActionMap();
        LOG.config("core list ActionMap.size="+core.getActionMap().size() + " list ActionMap.size="+list.getActionMap().size());
        ActionMap parentMap = map.getParent();
        if(parentMap instanceof LazyActionMap) {
        	LazyActionMap lam = (LazyActionMap)parentMap;
//        	Object[] keys = lam.keys();
            LOG.config("list ActionMap.parent.size="+lam.size());
        }
        Object key = list.getActionMap().getParent().keys()[0];
        assertTrue("sanity: key contained in core actionMap " + key, Arrays.asList(core.getActionMap().allKeys()).contains(key));
        assertSame("sanity: xlist share actions", list.getActionMap().get(key), new JYList<Object>().getActionMap().get(key));
        assertNotSame("core has different action", list.getActionMap().get(key), core.getActionMap().get(key));
    }
    
    /**
     * Test that ui-installed ActionMaps shared by different instances of JYList.
     */
    @Test
    public void testSharedActionMaps() {
        JYList<Object> list = new JYList<Object>();
        assertNotNull(list.getActionMap().getParent());
        assertSame(list.getActionMap().getParent(), new JYList<Object>().getActionMap().getParent());
    }
    
}
