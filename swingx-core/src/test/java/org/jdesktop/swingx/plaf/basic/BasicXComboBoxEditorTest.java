package org.jdesktop.swingx.plaf.basic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.awt.Component;

import javax.swing.JTextField;

import org.junit.Before;
import org.junit.Test;


public class BasicXComboBoxEditorTest {
	private BasicXComboBoxEditor comboBoxEditor;
	
	@Before
	public void setUp() {
		comboBoxEditor = new BasicXComboBoxEditor();
	}
	
	@Test
	public void testEditorComponents() throws Exception {
		Component editor = comboBoxEditor.getEditorComponent();
		assertNotNull(editor);
		if(editor instanceof JTextField) {
			JTextField tf = (JTextField)editor;
			assertEquals(9, tf.getColumns());
			assertEquals("", tf.getText());
		} else {
			fail("editorComponent expected to be JTextField");
		}
	}

}
