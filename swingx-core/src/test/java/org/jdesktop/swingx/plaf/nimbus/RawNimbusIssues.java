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
package org.jdesktop.swingx.plaf.nimbus;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JCheckBox;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.test.PropertyChangeReport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * 
 * @author Jeanette Winzenburg
 */
@RunWith(JUnit4.class)
public class RawNimbusIssues extends InteractiveTestCase {

    private static final Logger LOG = Logger.getLogger(RawNimbusIssues.class.getName());
    /* aus NimbusDefaults:

        addColor(d, "nimbusLightBackground", 255, 255, 255, 255);
     ...
     addColor(d, "Table.alternateRowColor", "nimbusLightBackground", 0.0f, 0.0f, -0.05098039f, 0, false);
   
     */
    private static final String ALTERNATE_ROW_COLOR = "Table.alternateRowColor";
    private static final String TABLE_BACKGROUND = "Table.background";
    
    @Test
    public void testBackground() {
        LOG.info("back color? " + UIManager.get(TABLE_BACKGROUND));
        JCheckBox box = new JCheckBox();
        LOG.info(" checkbox? " + box.getBackground());
    }
    
    /* returns DerivedColor
class DerivedColor extends Color 
...
    @Override public int getRGB() {
        return argbValue;
    }
     
     */
    private Color getALTERNATE_ROW_COLOR() {
        Color c = UIManager.getColor(ALTERNATE_ROW_COLOR);
        LOG.info("Color:" + c
        	+ "\n RGB="+c.getRGB()
        	);
        return c;
    }
    
    /**
     * Core Issue ??: Nimbus install must be complete on propertyChangeNotification
     *   from UIManager.
     *   
     * The error is to "derive" the color only after firing, that is when receiving
     * the notification of the change to itself (how weird is that?) and 
     * changing the color value without notifying listeners to the defaults.
     * 
     * Here we test the alternative: if the color is not yet completely installed
     * at the time of firing a lookAndFeel property change, then at least the 
     * color must fire a property change on derive (as is documented in the 
     * api doc, but not done)
     * 
     * @throws Exception
     */
    @Test
    public void testColorInCompleteFireChange() throws Exception {
        setLookAndFeel("Metal");
        final List<Color> colors = new ArrayList<Color>();
        final List<Integer> rgb = new ArrayList<Integer>();
        final PropertyChangeReport report = new PropertyChangeReport();
        PropertyChangeListener l = new PropertyChangeListener() {       
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                UIManager.getLookAndFeelDefaults().addPropertyChangeListener(report);
                colors.add(getALTERNATE_ROW_COLOR());
                rgb.add(colors.get(0).getRGB());              
            }
        };
        try {         
            UIManager.addPropertyChangeListener(l);
            setLookAndFeel("Nimbus");
            int rgbOrg = rgb.get(0);
            if (colors.get(0).getRGB() != rgbOrg) {
                assertTrue("lookandFeelDefaults must have fired color change notification", 
                        report.hasEvents());
            }
        } finally {
            removeListeners(l, report);
        }
    }
    
    /**
     * Core Issue ??: Nimbus install must be complete on propertyChangeNotification
     *   from UIManager.
     *   
     * The error is to "derive" the color only after firing, that is when receiving
     * the notification of the change to itself (how weird is that?) and 
     * changing the color value without notifying listeners to the defaults.
     * 
     * Here we test that the color is the same instance as when the lookAndFeel
     * property change and that it is unchanged if no event fired from the 
     * lookandfeelDefaults
     *   
     * @throws Exception
     */
    @Test
    public void testColorCompleteInstalled() throws Exception {
        setLookAndFeel("Metal");
        final List<Color> colors = new ArrayList<Color>();
        final List<Integer> rgb = new ArrayList<Integer>();
        final PropertyChangeReport report = new PropertyChangeReport();
        PropertyChangeListener l = new PropertyChangeListener() {
            
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                UIManager.getLookAndFeelDefaults().addPropertyChangeListener(report);
                colors.add(getALTERNATE_ROW_COLOR());
                rgb.add(colors.get(0).getRGB());
            	
            	LOG.info("PropertyChangeEvent PropertyName="+evt.getPropertyName()
            	+"\n OldValue:"+evt.getOldValue()
            	+"\n NewValue:"+evt.getNewValue()
            	+"\n alternateRowColor:"+UIManager.getColor(ALTERNATE_ROW_COLOR)
            	+"\n colors.size="+colors.size()
            	+"\n colors.0         ="+colors.get(0)
            	+"\n added rgb of colors="+rgb.get(0));
            }
        };
        try {          
            UIManager.addPropertyChangeListener(l);
            setLookAndFeel("Nimbus");
            // property lookAndFeel fired, PropertyChangeListener l triggered
            
            assertSame("Color installed when firing property change", 
                    colors.get(0), UIManager.getColor(ALTERNATE_ROW_COLOR));
            
            if (report.hasEvents()) {
            	LOG.info("LastEvent:"+report.getLastEvent());
            } else {
            	LOG.info("no events colors.get(0):"+colors.get(0));
            	PropertyChangeEvent evt = report.getLastEvent();
                int rgbOrg = rgb.get(0);
            	LOG.info("MultiCastEvents="+report.getMultiCastEventCount()
            		+ " LastEvent:"+evt // expected null
            		+ " expected rgb of colors="+rgbOrg
        		    + " is:"+colors.get(0).getRGB());
                assertEquals("Color must be unchanged compared to original", 
                        rgbOrg, colors.get(0).getRGB());
            }
        } finally {
            removeListeners(l, report);
        }
    }

    /**
     * Core issue: UIDefaults not firing on remove.
     * 
     * Reported with review id: 1610127
     */
    @Test
    public void testUIDefaultsNotificationRemove() {
        UIDefaults properties = new UIDefaults() {
        	// fire on remove:
            public Object remove(Object key) {
            	Object o = super.remove(key);
                if (key instanceof String) {
                    firePropertyChange((String)key, o, null);
                }
				return o;           	
            }     	
        };
        PropertyChangeReport report = new PropertyChangeReport();
        properties.addPropertyChangeListener(report);
        Object value = new Object();
        properties.put("somevalue", value);
        assertEquals(1, report.getEventCount("somevalue"));
        report.clear();
        Object o = properties.remove("somevalue");
        assertNull("sanity: value removed", properties.get("somevalue"));
        assertSame(value, o);
        // must fire?
        if(1==report.getEventCount("somevalue")) {
            LOG.info("UIDefaults fired");
            assertEquals("uidefaults must fire (here: remove)", 1, report.getEventCount("somevalue"));
        } else {
        	LOG.warning("UIDefaults.remove returns expected value but does not fire an event on remove");
        }
    }
    
    /**
     * Core issue: UIDefaults not firing on remove.
     * 
     * Reported with review id: 1610127
     * 
     * This tests the workaround: don't use remove but put(key, null) instead.
     */
    @Test
    public void testUIDefaultsNotificationPutNull() {
        UIDefaults properties = new UIDefaults();
        Object value = new Object();
        properties.put("somevalue", value);
        PropertyChangeReport report = new PropertyChangeReport();
        properties.addPropertyChangeListener(report);
        properties.put("somevalue", null);
        assertEquals("uidefaults must fire (here: put(.. null))", 1, report.getEventCount("somevalue"));
    }
    
    
    /**
     * Removes listeners installed on UIManager and lookAndFeelDefaults.
     * 
     * @param managerListener the listener the remove from the UIManager
     * @param lookAndFeelDefaultsListener the listener to remove from the 
     *    lookAndFeelDefaults
     */
    private void removeListeners(PropertyChangeListener managerListener, 
            PropertyChangeListener lookAndFeelDefaultsListener) {
        UIManager.removePropertyChangeListener(managerListener);
        UIManager.getLookAndFeelDefaults().removePropertyChangeListener(lookAndFeelDefaultsListener);
        
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        // force a new instance for each round
        setLookAndFeel("Nimbus");
        assertEquals("sanity: nothing loaded", null, UIManager.get("DatePickerUI"));
    }

}
