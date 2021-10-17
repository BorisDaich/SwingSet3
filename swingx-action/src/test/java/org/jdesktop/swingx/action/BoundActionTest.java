package org.jdesktop.swingx.action;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jdesktop.test.SerializableSupport.serialize;
import static org.jdesktop.test.matchers.Matchers.equivalentTo;

import javax.swing.JLabel;

import org.hamcrest.CoreMatchers;
import org.jdesktop.test.EDTRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EDTRunner.class)
public class BoundActionTest {
    private BoundAction action;
    
    @Before
    public void setUp() {
        action = new BoundAction();
    }
    
    @Test
    public void testSerialization() {
        BoundAction serialized = serialize(action);
        
        assertThat(serialized, CoreMatchers.is(equivalentTo(action)));
    }
    
    @Test
    public void testSerializationWithCallback() {
        action.registerCallback(new JLabel(), "updateUI");
        BoundAction serialized = serialize(action);
        
        assertThat(serialized, CoreMatchers.is(equivalentTo(action)));
        assertThat(serialized.getActionListeners().length, CoreMatchers.is(1));
    }
    
    @Test
    public void testSerializationForToggleWithCallback() {
        action.setStateAction(true);
        action.registerCallback(new JLabel(), "updateUI");
        BoundAction serialized = serialize(action);
        
        assertThat(serialized, CoreMatchers.is(equivalentTo(action)));
        assertThat(serialized.getItemListeners().length, CoreMatchers.is(1));
    }
    
    @Test
    public void testSerializationWithListener() {
        action.addActionListener(new BoundAction());
        BoundAction serialized = serialize(action);
        
        assertThat(serialized, CoreMatchers.is(equivalentTo(action)));
    }
}
