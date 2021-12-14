package org.jdesktop.swingx.auth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class KeyChainIssues extends TestCase {

    private static final Logger LOG = Logger.getLogger(KeyChainIssues.class.getName());
	private static final String FILENAME = "KeyChainStore.txt";

    KeyChain kc;
    
    @Override
    @Before
    public void setUp() {
        FileInputStream fis = null;
		try {
	        File file = new File(FILENAME); // eclispe ws swingset\SwingSet3\swingx-core 
	        if (!file.exists()) {
	            file.createNewFile(); // throws IOException
	            LOG.info("created "+FILENAME);
	            fis = null;
	        } else {
	            fis = new FileInputStream(file); // throws FileNotFoundException
	            LOG.fine("existing "+FILENAME);
	        }
		} catch (FileNotFoundException e) {
            LOG.warning("new FileInputStream throws"+e);
		} catch (IOException e) {
            LOG.warning("file.createNewFile throws"+e);
		}
		
        try {
			kc = new KeyChain("test".toCharArray(), fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    @After
    public void tearDown() {
        FileOutputStream fos = null;
        File file = new File(FILENAME);
        try {
            fos = new FileOutputStream(file); // throws FileNotFoundException
			kc.store(fos); // throws IOException
            LOG.fine("KeyChain stored to "+FILENAME);
		} catch (FileNotFoundException e) {
            LOG.warning("new FileOutputStream throws"+e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	private static final String USERNAME = "bino";
	private static final String SERVERNAME = "un-ds.sfbay";
	private static final String PASSWORD = "test123";
    
    @Test
    public void testAddPassword() {
    	kc.addPassword(USERNAME, SERVERNAME, PASSWORD.toCharArray());
    	String pw = kc.getPassword(USERNAME, SERVERNAME);
        assertEquals(PASSWORD, pw);
    }
    
    @Test
    public void testNullServer() {
    	kc.addPassword(USERNAME, null, PASSWORD.toCharArray());
    	String pw = kc.getPassword(USERNAME, null);
        assertEquals(PASSWORD, pw);
    }

    @Test
    public void testRemovePassword() {
    	kc.removePassword(USERNAME, null);
    	String pw = kc.getPassword(USERNAME, null);
        assertNull("this Password was removed", pw);
        
    	kc.removePassword(USERNAME, SERVERNAME);
    	String spw = kc.getPassword(USERNAME, SERVERNAME);
        assertNull("this Password was removed", spw);
    }

    @Test
    public void testAddAndRemovePassword() {
    	kc.addPassword(USERNAME, SERVERNAME, PASSWORD.toCharArray());
    	kc.addPassword(USERNAME, null, PASSWORD.toCharArray());
    	
    	kc.removePassword(USERNAME, null);
    	String pw = kc.getPassword(USERNAME, null);
        assertNull("this Password was removed", pw);
        
    	kc.removePassword(USERNAME, SERVERNAME);
    	String spw = kc.getPassword(USERNAME, SERVERNAME);
        assertNull("this Password was removed", spw);
    }
    
    @Test
    public void test100() {
        LOG.info("More testing add ...");
        for (int i = 0; i < 100; i++) {
            kc.addPassword("" + i, SERVERNAME, ("" + i).toCharArray());
        }
        LOG.info("More testing get+remove...");
        for (int i = 0; i < 100; i++) {
        	String pw = kc.getPassword("" + i, SERVERNAME);
            assertEquals("" + i, pw);
        	kc.removePassword("" + i, SERVERNAME);
        }
    }

}
