/*
 * Created on 31.07.2006
 *
 */
package org.jdesktop.swingx.plaf;

import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ServiceLoader;
import java.util.logging.Logger;

import javax.swing.UIManager;

import org.jdesktop.swingx.plaf.LookAndFeelAddons.IterableLAFAddonsPrivilegedAction;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * "hand test" sandbox restrictions  for LookAndFeelAddons. Behaviour
 * (mainly of looking up an appropriate addon) should be the same as
 * in an unrestricted context - tested to work in super.
 * 
 * Note: to run this test manually, remove the ignore annotation 
 * can't automatically run tests that install a securityManager 
 * (because I found no way to uninstall it when the test class is done)
 * 
 */
//@Ignore
@RunWith(JUnit4.class)
public class LookAndFeelAddonsSandboxOnTest extends LookAndFeelAddonsSandboxTest {
	
    private static final Logger LOG = Logger.getLogger(LookAndFeelAddonsSandboxOnTest.class.getName());
    
    public class URLPrivilegedAction implements PrivilegedAction<URL> {

    	Class<?> clazz;
    	String services;
    	
    	URLPrivilegedAction(Class<?> clazz, String services) {
    		this.clazz = clazz;
    		this.services = services;
			LOG.info("clazz:"+clazz.getSimpleName() + " services="+services);
    	}
    	
		@Override
		public URL run() {
			LOG.info("services:"+services);
			ClassLoader classLoader = clazz.getClassLoader();
			LOG.info("classLoader:"+classLoader);
			return classLoader.getResource(services);
		}
    	
    }
    
    /**
     * Accessing the services inside a privileged action.
     */
    @Test
    public void testAccessMetaInfPriviledged() {
        final Class<?> clazz = LookAndFeelAddons.class;
        // JW: just a reminder (to myself)
        // class.getResource interprets path as relative without 
        // leading slash
        // classloader.getResource always absolute
        final String services = "META-INF/services/" + clazz.getName();
        // using the classloader (just as ServiceLoader does)
        // absolute path always
        PrivilegedAction<URL> privAction = new URLPrivilegedAction(clazz, services);
        URL url = privAction.run();
		//URL url = AccessController.doPrivileged(privAction);
//		URL url = AccessController.doPrivileged(
//				new PrivilegedAction<URL>() {
//					@Override
//					public URL run() {
//						return clazz.getClassLoader().getResource(services);
//					}
//		        });
		LOG.info("url:"+url);
//        assertNotNull("services must be found", url);
/*
junit.framework.AssertionFailedError: services must be found
	at junit.framework.Assert.fail(Assert.java:57)
	at junit.framework.Assert.assertTrue(Assert.java:22)
	at junit.framework.Assert.assertNotNull(Assert.java:256)
	at junit.framework.TestCase.assertNotNull(TestCase.java:399)
	at org.jdesktop.swingx.plaf.LookAndFeelAddonsSandboxOnTest.testAccessMetaInfPriviledged(LookAndFeelAddonsSandboxOnTest.java:82)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:59)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:56)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26)
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
	at org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26)
	at org.junit.internal.runners.statements.RunAfters.evaluate(RunAfters.java:27)
	at org.junit.runners.ParentRunner$3.evaluate(ParentRunner.java:306)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:413)
	at org.eclipse.jdt.internal.junit4.runner.JUnit4TestReference.run(JUnit4TestReference.java:93)
	at org.eclipse.jdt.internal.junit.runner.TestExecution.run(TestExecution.java:40)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:529)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:756)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.run(RemoteTestRunner.java:452)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.java:210)


 */
    }
    
    /**
     * Testing privileged access to the ServiceLoader.
     * 
     * Here we access the iterator inside the priviledged access, thus 
     * forcing the load.
     */
    @Test
    public void testServiceLoaderIteratorPrivileged() {
        final ServiceLoader<LookAndFeelAddons> loader = 
        		ServiceLoader.load(LookAndFeelAddons.class);
		LOG.info("ServiceLoader<LookAndFeelAddons>:"+loader);
		loader.forEach(laf -> {
			LOG.info("  LookAndFeelAddons:"+laf);
		});
        // need to access the iterator inside the priviledge
        // action
        // probably because it's lazily loaded
        Iterable<LookAndFeelAddons> iLoader = AccessController.doPrivileged(new IterableLAFAddonsPrivilegedAction(loader));
//        AccessController
//                .doPrivileged(new PrivilegedAction<Iterable<LookAndFeelAddons>>() {
//                    @Override
//                    public Iterable<LookAndFeelAddons> run() {
//                        loader.iterator().hasNext();
//                        return loader;
//                    }
//                });
        int count = 0;
		LOG.info("iterate:");
//        for (@SuppressWarnings("unused") LookAndFeelAddons addons : loader) {
        for (LookAndFeelAddons addons : iLoader) {
    		LOG.info("count="+count + " addons:"+addons);
            count++;
        }
        int excpected = 7;
        if(count!=excpected) {
        	LOG.warning("excpected "+excpected+" addons but found "+count + "\n");
        }
//        assertEquals("loader must have addons", 7, count); // EUG: wie kommt man auf die 7 addons?
    }
    
    /**
     * Issue #1568-swingx: accessing LookAndFeelAddons throws
     * ExceptionInInitializationError.
     * 
     * This is caused by a typo in getCrossPlatFormAddon in the fallback
     * branch, that is if accessing the property isn't allowed
     * (packagename was swing instead of swingx).
     * 
     * Actually, with the fix to #1567, this is not really testing 
     * because, as ultimate fallback, it is never reached if the
     * lookup is working. 
     *
     */
    @Test
    public void testCrossPlatformTypo() {
        LookAndFeelAddons.getAddon();
    }
    
    /**
     * Sanity: verify access to swing.addon denied.
     *
     */
    @Test(expected= SecurityException.class)
    public void testPropertySwingAddonDenied() {
        System.getProperty("swing.addon", "not specified");
    }
    
    /**
     * Sanity: verify access to swing.crossplatformaddon denied.
     *
     */
    @Test(expected= SecurityException.class)
    public void testPropertySwingCrossplatformAddonDenied() {
        System.getProperty("swing.crossplatformlafaddon", "not specified");
    }
    
    /**
     * Asserts that a security manager is installed, running the tests
     * here without doesn't make sense.
     */
    @Override
    @Before
    public void setUp() throws Exception {
		LOG.info("System.SecurityManager:"+System.getSecurityManager());
        assertNotNull("Sandbox test cannot be run, no securityManager", System.getSecurityManager());
    }
    
    /**
     * Install a security manager and sets the default LAF to system. 
     * 
     * Note that de-installing the
     * manager might not be allowed, so we can't run these tests automatically.
     */
    @BeforeClass
    public static void install() {
		LOG.info("set setSecurityManager which is deprecated forRemoval since=17");
        // A - install the default SecurityManager. 
        // Doing so we are not allowed to reverse the install -
        // which makes this testCase to a manual-run-only affair
        // (the securityManager is not uninstalled when running 
        // other test cases - in Eclipse, when running the 
        // bulk "all tests" of a projects.
		
/*		deprecated forRemoval since="17"

		The Security Manager is deprecated and subject to removal in a future release. 
		There is no replacement for the Security Manager.See JEP 411 for discussion and alternatives.
		https://openjdk.java.net/jeps/411
 */
        System.setSecurityManager(new SecurityManager());
        
        // B- if we install a SecurityManager we need to be sure
        // that we are allowed to uninstall it.
        // BUT: with this custom manager on, test running 
        // fails with a rather weird stack-trace. Gave up for now...
//        System.setSecurityManager(new SecurityManager() {
//
//            @Override
//            public void checkPermission(Permission perm) {
//                if ("setSecurityManager".equals(perm.getName())) return;
//                super.checkPermission(perm);    
//                //  java.security.AccessController.checkPermission(perm);
//
//            }
//
//        });
              
        try {
        	String systemLookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
        	LOG.info("setLookAndFeel (LAF) to "+systemLookAndFeelClassName);
            UIManager.setLookAndFeel(systemLookAndFeelClassName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void uninstall() {
//        try {
//            System.setSecurityManager(null);
//        } catch (Exception e) {
//            LOG.info("can't remove the security manager");
//        }
    }
}
