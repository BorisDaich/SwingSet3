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
package org.jdesktop.swingx.decorator;

import static org.jdesktop.swingx.plaf.TableAddon.TABLE_ALTERNATE_ROW_COLOR;

import java.util.logging.Logger;

import javax.swing.UIManager;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXTable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Contains UIColorHighlighter/Addon tests (related to Nimbus, sigh).
 * 
 * PENDING JW: can't really test - addon is a static context: once contributed,
 * no way to uncontribute.
 */
@RunWith(JUnit4.class)
public class UIColorHighlighterTest extends InteractiveTestCase {
    
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(UIColorHighlighterTest.class.getName());
    
    private static String OS = System.getProperty("os.name");
    
    private static final String ALTERNATE_COLOR = TABLE_ALTERNATE_ROW_COLOR;
    
    /**
     * Sanity: testing assumption that raw core Nimbus has intalled the alternate 
     * color property, raw core Metal has not and a previous Nimbus install is
     * removed.
     *  
     * @throws Exception
     */
    @Test
    public void testAlternateRaw() throws Exception {
        if (!hasLookAndFeel("Nimbus")) {
            LOG.fine("cant run - no Nimbus");
            return;
        }
        setLookAndFeel("Metal");
        assertNull("alternateRowColor is null", UIManager.getColor(ALTERNATE_COLOR));
        setLookAndFeel("Nimbus");
        LOG.config(ALTERNATE_COLOR+" "+UIManager.getColor(ALTERNATE_COLOR));
        assertNotNull("Nimbus without addon has alternate", UIManager.getColor(ALTERNATE_COLOR));
        setLookAndFeel("Metal");
        assertNull("alternateRowColor is null", UIManager.getColor(ALTERNATE_COLOR));
    }

    /**
     * Test that UIColorHighlighter removes the alternate color installed 
     * by Nimbus.
     * 
     * @throws Exception
     */
//    @Test
//    public void testAlternateUIColorHighlighter() throws Exception {
//        if (!hasLookAndFeel("Nimbus")) {
//            LOG.fine("cant run - no Nimbus");
//            return;
//        }
//        setLookAndFeel("Nimbus");
//        assertNotNull("Nimbus without addon has alternate", UIManager.getColor(ALTERNATE_COLOR));
//        new UIColorHighlighter();
//        assertNull("Nimbus with addon has alternate removed but was: \n " 
//                + UIManager.getColor(ALTERNATE_COLOR), UIManager.getColor(ALTERNATE_COLOR));
//    }
    /**
     * Test that TableAddon removes the alternate color installed 
     * by Nimbus.
     * 
     * @throws Exception
     */
//    @Test TODO wieder aktivieren
    public void testAlternateXTable() throws Exception {
        LOG.config("LookAndFeel:"+UIManager.getLookAndFeel()+" Color(Table.alternateRowColor):"+UIManager.getColor(ALTERNATE_COLOR));
        if (!hasLookAndFeel("Nimbus")) {
            LOG.warning("cant run - no Nimbus");
            return;
        }
        setLookAndFeel("Nimbus");
        LOG.config("after setLAF to Nimbus:\nLookAndFeel:"+UIManager.getLookAndFeel()+" Color(Table.alternateRowColor):"+UIManager.getColor(ALTERNATE_COLOR));
        assertNotNull("Nimbus without addon has alternate color ", UIManager.getColor(ALTERNATE_COLOR));
        
        new JXTable();
        LOG.config(OS + " after new JXTable(); LookAndFeel:"+UIManager.getLookAndFeel()+" Color(Table.alternateRowColor):"+UIManager.getColor(ALTERNATE_COLOR));
        assertNull("Nimbus with addon has alternate removed but was: \n " 
                + UIManager.getColor(ALTERNATE_COLOR), UIManager.getColor(ALTERNATE_COLOR));
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }
    
    
}
