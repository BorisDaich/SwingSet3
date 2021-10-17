package org.jdesktop.beans;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jdesktop.test.SerializableSupport.serialize;
import static org.jdesktop.test.matchers.Matchers.equivalentTo;
import static org.jdesktop.test.matchers.Matchers.property;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.awt.Insets;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.exceptions.verification.NoInteractionsWanted;

@SuppressWarnings("nls")
public abstract class AbstractBeanInfoTest<T> {
	
    protected Logger logger = Logger.getLogger(getClass().getName());
    
    protected T instance;
    private BeanInfo beanInfo;
    private Map<Class<?>, Object> listeners;
    
    @Before
    public void setUp() throws Exception {
        instance = createInstance();
        beanInfo = Introspector.getBeanInfo(instance.getClass());
        listeners = new HashMap<Class<?>, Object>();
        
        for (EventSetDescriptor descriptor : beanInfo.getEventSetDescriptors()) {
            Class<?> eventClass = descriptor.getListenerType();
            Object listener = mock(eventClass);
            
            descriptor.getAddListenerMethod().invoke(instance, listener);
            listeners.put(eventClass, listener);
        }
    }
    
    protected abstract T createInstance();
    
    @Test
    @Ignore("fails with IllegalArgumentException")
    public final void testBoundProperties() throws Exception {
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
            if (descriptor.isBound()) {
                if (descriptor.isHidden()) {
                    continue;
                }
                
                if (descriptor.getWriteMethod() == null) {
                    //special-case this read-only property
                    if ("UIClassID".equals(descriptor.getName())) {
                        return;
                    }
                    
                    fail("bound read-only property: " + descriptor.getName());
                }
                
                Class<?> propertyType = descriptor.getPropertyType();
                
                if (isUnhandledType(propertyType)) {
                    //TODO log?
                    continue;
                }
                
                Object defaultValue = descriptor.getReadMethod().invoke(instance);
                Object newValue = getNewValue(propertyType, defaultValue);
                
                descriptor.getWriteMethod().invoke(instance, newValue);
                
                PropertyChangeListener pcl = (PropertyChangeListener) listeners.get(PropertyChangeListener.class);
                Matcher<PropertyChangeEvent> m = property(descriptor.getName(), defaultValue, newValue);
                // can cast PropertyChangeEventMatcher to org.mockito.ArgumentMatchers because
                // class PropertyChangeEventMatcher implements ArgumentMatcher<Object>
                @SuppressWarnings("unchecked")
				ArgumentMatcher<PropertyChangeEvent> am = (ArgumentMatcher<PropertyChangeEvent>)m;
                verify(pcl).propertyChange(argThat(am));
                reset(pcl);
            }
        }
    }
    
    private boolean isUnhandledType(Class<?> type) {
        return type == null;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected Object getNewValue(Class<?> propertyType, Object defaultValue) {
        Object result = null;
        
        if (propertyType.isArray()) {
            int length = defaultValue == null ? 1 : ((Object[]) defaultValue).length + 1;
            result = Array.newInstance(propertyType.getComponentType(), length);
        } else if (propertyType.isEnum()) {
            EnumSet set = EnumSet.allOf((Class<? extends Enum>) propertyType);
            int size = set.size();
            
            if (size == 1) {
                result = defaultValue == null ? set.iterator().next() : null;
            } else {
                int ordinal = ((Enum) defaultValue).ordinal();
                ordinal = ordinal == size - 1 ? 0 : ordinal + 1;
                Iterator iter = set.iterator();
                
                for (int i = 0; i < ordinal + 1; i++) {
                    result = iter.next();
                }
            }
        } else if (propertyType.isPrimitive()) {
            //help short circuit all of these checks
            if (propertyType == boolean.class) {
                result = Boolean.FALSE.equals(defaultValue);
            } else if (propertyType == int.class) {
                result = ((Integer) defaultValue) + 1;
            } else if (propertyType == double.class) {
                result = ((Double) defaultValue) + 1d;
            } else if (propertyType == float.class) {
                result = ((Float) defaultValue) + 1f;
            }
        } else if (propertyType == String.class) {
            result = "original string: " + defaultValue;
        } else if (propertyType == Insets.class) {
            if (new Insets(0, 0, 0, 0).equals(defaultValue)) {
                result = new Insets(1, 1, 1, 1);
            } else {
                result = mock(propertyType);
            }
        } else {
            result = mock(propertyType, RETURNS_MOCKS);
        }
        
        return result;
    }

    /**
     * A simple serialization check. Ensures that the reconstituted object is equivalent to the
     * original.
     */
    @Test
    public void testSerialization() {
        if (!Serializable.class.isInstance(instance)) {
            return;
        }
        
        T serialized = serialize(instance);
        
        Matcher<T> m = equivalentTo(instance);
        assertThat(serialized, CoreMatchers.is(m));
    }
    
    @After
    public void tearDown() {
        for (Object listener : listeners.values()) {
            try {
                logger.log(Level.CONFIG, "listener:"+listener);
                // TODO need a way to handle components that have contained components,
                // like JXComboBox, that cause spurious container events
                verifyNoMoreInteractions(listener);
            } catch (NoInteractionsWanted logAndIgnore) {
                logger.log(Level.WARNING, "unexpected listener notification"+logAndIgnore);
/*
                logger.log(Level.WARNING, "unexpected listener notification", logAndIgnore) :
	org.jdesktop.beans.AbstractBeanInfoTest tearDown
WARNUNG: unexpected listener notification
org.mockito.exceptions.verification.NoInteractionsWanted: 
No interactions wanted here:
-> at org.jdesktop.beans.AbstractBeanInfoTest.tearDown(AbstractBeanInfoTest.java:178)
But found this interaction on mock 'propertyChangeListener':
-> at java.beans.PropertyChangeSupport.fire(PropertyChangeSupport.java:335)
***
For your reference, here is the list of all invocations ([?] - means unverified).
1. [?]-> at java.beans.PropertyChangeSupport.fire(PropertyChangeSupport.java:335)
2. [?]-> at java.beans.PropertyChangeSupport.fire(PropertyChangeSupport.java:335)

	at org.jdesktop.beans.AbstractBeanInfoTest.tearDown(AbstractBeanInfoTest.java:178)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:59)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:56)
	at org.junit.internal.runners.statements.RunAfters.invokeMethod(RunAfters.java:46)
	at org.junit.internal.runners.statements.RunAfters.evaluate(RunAfters.java:33)
	at org.junit.runners.ParentRunner$3.evaluate(ParentRunner.java:306)
	at org.junit.runners.BlockJUnit4ClassRunner$1.evaluate(BlockJUnit4ClassRunner.java:100)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:366)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:103)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:63)
	at org.junit.runners.ParentRunner$4.run(ParentRunner.java:331)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:79)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:329)
	at org.junit.runners.ParentRunner.access$100(ParentRunner.java:66)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:293)
	at org.junit.runners.ParentRunner$3.evaluate(ParentRunner.java:306)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:413)
	at org.apache.maven.surefire.junit4.JUnit4Provider.execute(JUnit4Provider.java:365)
	at org.apache.maven.surefire.junit4.JUnit4Provider.executeWithRerun(JUnit4Provider.java:273)
	at org.apache.maven.surefire.junit4.JUnit4Provider.executeTestSet(JUnit4Provider.java:238)
	at org.apache.maven.surefire.junit4.JUnit4Provider.invoke(JUnit4Provider.java:159)
	at org.apache.maven.surefire.booter.ForkedBooter.invokeProviderInSameClassLoader(ForkedBooter.java:384)
	at org.apache.maven.surefire.booter.ForkedBooter.runSuitesInProcess(ForkedBooter.java:345)
	at org.apache.maven.surefire.booter.ForkedBooter.execute(ForkedBooter.java:126)
	at org.apache.maven.surefire.booter.ForkedBooter.main(ForkedBooter.java:418)
 */
            }
        }
    }
}
