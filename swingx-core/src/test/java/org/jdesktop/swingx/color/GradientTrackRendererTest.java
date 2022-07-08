/*
 * Created on 12.04.2006
 * 
 */
package org.jdesktop.swingx.color;

import java.awt.Color;

import javax.swing.JComponent;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXMultiThumbSlider;
import org.jdesktop.swingx.multislider.TrackRenderer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class GradientTrackRendererTest extends InteractiveTestCase {
    @Test
    public void testClassCastException() {
    	TrackRenderer track = new GradientTrackRenderer();
    	JComponent comp = track.getRendererComponent(null);
    	//assertNull(comp); // nicht null?
    	assertNotNull(comp);
    	assertEquals(0, comp.getHeight());
    	assertEquals(0, comp.getWidth());
    	
    	JXMultiThumbSlider<Color> colorSlider = new JXMultiThumbSlider<Color>();
    	comp = track.getRendererComponent(colorSlider);
    	assertNotNull(comp);
    	
    	JXMultiThumbSlider<Integer> slider = new JXMultiThumbSlider<Integer>();
    	comp = track.getRendererComponent(slider);
    	// slider ohne Schieberegler führt noch nicht zur ClassCastException!!!
    	assertNotNull(comp);
    	
    	slider.getModel().addThumb(0, Integer.valueOf(0));
    	try {
        	comp = track.getRendererComponent(slider);
        	fail("expected ClassCastException");
    	} catch (ClassCastException e) {
    		// success because ClassCastException was expected
    	}
    }
}
