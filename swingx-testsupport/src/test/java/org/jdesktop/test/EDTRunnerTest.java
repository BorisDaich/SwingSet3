package org.jdesktop.test;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EDTRunner.class)
public class EDTRunnerTest {
	
    private static final Logger LOG = Logger.getLogger(EDTRunnerTest.class.getName());

    /**
     * Ensure that the EDTRunner is using the EventDispatchThread.
     */
    @Test
    public void testForSwingThread() {
    	
    	// Show props:
    	LOG.info("java.version:"+System.getProperty("java.version"));
    	System.getProperties().forEach((p,v) -> {
    		System.out.println("Property:Value "+p+":"+v);
    	});
    	
        assertThat(SwingUtilities.isEventDispatchThread(), CoreMatchers.is(true));
    }
}
