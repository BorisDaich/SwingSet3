/*
 * Copyright 2008 Sun Microsystems, Inc., 4150 Network Circle,
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
 */
package org.jdesktop.swingx.autocomplete;

import static java.awt.event.KeyEvent.CHAR_UNDEFINED;
import static java.awt.event.KeyEvent.KEY_PRESSED;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_END;
import static java.awt.event.KeyEvent.VK_HOME;
import static java.awt.event.KeyEvent.VK_PAGE_DOWN;
import static java.awt.event.KeyEvent.VK_PAGE_UP;
import static java.awt.event.KeyEvent.VK_UP;
import static java.lang.System.currentTimeMillis;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeThat;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;

import org.hamcrest.CoreMatchers;
import org.jdesktop.test.EDTRunner;
import org.jdesktop.test.categories.Visual;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

/**
 *
 * @author Karl George Schaefer
 */
@RunWith(EDTRunner.class)
public class AutoCompleteDecoratorTest  {
	
    private JComboBox<String> combo;
    
    @Before
    public void setUp() throws Exception {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        combo = new JComboBox<String>(new String[]{"Alpha", "Bravo", "Charlie", "Delta"});
    }
    
    /**
     * Issue #1570-swingx: decorating Jlist without converter throws NPE
     * test listAdapter
     */
    @Test
    public void testDefaultConverterListAdapterTwo() {
        JList<String> list = new JList<String>(combo.getModel());
        JTextComponent text = new JTextField();
        ListAdaptor adapter = new ListAdaptor(list, text);
        assertSame("default adapter in two-param constructor", 
                ObjectToStringConverter.DEFAULT_IMPLEMENTATION, adapter.stringConverter);
    }
    
    /**
     * Issue #1570-swingx: decorating Jlist without converter throws NPE
     * test listAdapter, three param constructor
     */
    @Test
    public void testDefaultConverterListAdapterThree() {
        JList<String> list = new JList<String>(combo.getModel());
        JTextComponent text = new JTextField();
        ListAdaptor adapter = new ListAdaptor(list, text, null);
        assertSame("default adapter in three-param constructor", 
                ObjectToStringConverter.DEFAULT_IMPLEMENTATION, adapter.stringConverter);
    }
    
    /**
     * Issue #1570-swingx: decorating Jlist without converter throws NPE
     * 
     */
    @Test
    public void testDefaultConverterJList() {
        JList<String> list = new JList<String>(combo.getModel());
        JTextComponent text = new JTextField();
        AutoCompleteDecorator.decorate(list, text);
        text.setText("A");
    }
    
    /**
     * Issue #1570-swingx: decorating list without converter throws NPE
     * completeness: test default converter for textComponent with List
     * 
     */
    @Test
    public void testDefaultConverterList() {
        List<?> list = Arrays.asList(new String[]{"Alpha", "Bravo", "Charlie", "Delta"});
        JTextComponent text = new JTextField();
        AutoCompleteDecorator.decorate(text, list, true);
        text.setText("A");
    }
    
    /**
     * Issue #1570-swingx: decorating list without converter throws NPE
     * completeness: test default converter for combo
     */
    @Test
    public void testDefaultConverterCombo() {
        AutoCompleteDecorator.decorate(combo);
        JTextComponent text = (JTextComponent) combo.getEditor().getEditorComponent();
        text.setText("A");
    }
    
    /**
     * SwingX Issue #299.
     */
    @Test
    public void testUndecorateComboBox() {
        combo.setEditable(false);
        AutoCompleteDecorator.decorate(combo);
        AutoCompleteDecorator.undecorate(combo);
        
        for (PropertyChangeListener l : combo.getPropertyChangeListeners("editor")) {
            assertThat(l, CoreMatchers.is(not(instanceOf(AutoComplete.PropertyChangeListener.class))));
        }
        
        assertThat(combo.getEditor(), CoreMatchers.is(not(instanceOf(AutoCompleteComboBoxEditor.class))));
        
        JTextComponent editorComponent = (JTextComponent) combo.getEditor().getEditorComponent();
        
        for (KeyListener l : editorComponent.getKeyListeners()) {
            assertThat(l, CoreMatchers.is(not(instanceOf(AutoComplete.KeyAdapter.class))));
        }
        
        for (InputMap map = editorComponent.getInputMap(); map != null; map = map.getParent()) {
            assertThat(map, CoreMatchers.is(not(instanceOf(AutoComplete.InputMap.class))));
        }
        
        assertThat(editorComponent.getActionMap().get("nonstrict-backspace"), CoreMatchers.is(nullValue()));
        
        for (FocusListener l : editorComponent.getFocusListeners()) {
            assertThat(l, CoreMatchers.is(not(instanceOf(AutoComplete.FocusAdapter.class))));
        }
        
        assertThat(editorComponent.getDocument(), CoreMatchers.is(not(instanceOf(AutoCompleteDocument.class))));
        
        for (ActionListener l : combo.getActionListeners()) {
            assertThat(l, CoreMatchers.is(not(instanceOf(ComboBoxAdaptor.class))));
        }
        
    }
    
    /**
     * SwingX Issue #299.
     */
    @Test
    public void testRedecorateComboBox() {
        AutoCompleteDecorator.decorate(combo);
        Component editor = combo.getEditor().getEditorComponent();
        
        int expectedFocusListenerCount = editor.getFocusListeners().length;
        int expectedKeyListenerCount = editor.getKeyListeners().length;
        int expectedPropListenerCount = combo.getPropertyChangeListeners("editor").length;
        int expectedActionListenerCount = combo.getActionListeners().length;
        
        
        AutoCompleteDecorator.decorate(combo);
        editor = combo.getEditor().getEditorComponent();
        
        assertThat(editor.getFocusListeners().length, CoreMatchers.is(expectedFocusListenerCount));
        assertThat(editor.getKeyListeners().length, CoreMatchers.is(expectedKeyListenerCount));
        assertThat(combo.getPropertyChangeListeners("editor").length, CoreMatchers.is(expectedPropListenerCount));
        assertThat(combo.getActionListeners().length, CoreMatchers.is(expectedActionListenerCount));
    }
    
    /**
     * SwingX Issue #299.
     */
    @Test
    public void testUndecorateList() {
        JList<String> list = new JList<String>();
        JTextField textField = new JTextField();
        AutoCompleteDecorator.decorate(list, textField);
        AutoCompleteDecorator.undecorate(list);
        
        for (ListSelectionListener l : list.getListSelectionListeners()) {
            assertThat(l, CoreMatchers.is(not(instanceOf(ListAdaptor.class))));
        }
    }
    
    /**
     * SwingX Issue #299.
     */
    @Test
    public void testRedecorateList() {
        JList<String> list = new JList<String>();
        JTextField textField = new JTextField();
        AutoCompleteDecorator.decorate(list, textField);
        
        int expectedListSelectionListenerCount = list.getListSelectionListeners().length;
        
        AutoCompleteDecorator.decorate(list, textField);
        
        assertThat(list.getListSelectionListeners().length, CoreMatchers.is(expectedListSelectionListenerCount));
    }
    
    /**
     * SwingX Issue #299.
     */
    @Test
    public void testUndecorateTextComponent() {
        JTextField textField = new JTextField();
        
        AutoCompleteDecorator.decorate(textField, Collections.emptyList(), true);
        
        AutoCompleteDecorator.undecorate(textField);
        
        assertThat(textField.getInputMap(), CoreMatchers.is(not(instanceOf(AutoComplete.InputMap.class))));
        assertThat(textField.getActionMap().get("nonstrict-backspace"), CoreMatchers.is(nullValue()));
        for (FocusListener l : textField.getFocusListeners()) {
            assertThat(l, CoreMatchers.is(not(instanceOf(AutoComplete.FocusAdapter.class))));
        }
        assertThat(textField.getDocument(), CoreMatchers.is(not(instanceOf(AutoCompleteDocument.class))));
    }
    
    /**
     * SwingX Issue #299.
     */
    @Test
    public void testRedecorateTextComponent() {
        JTextField textField = new JTextField();
        AutoCompleteDecorator.decorate(textField, Collections.emptyList(), true);
        
        int expectedFocusListenerLength = textField.getFocusListeners().length;
        
        AutoCompleteDecorator.decorate(textField, Collections.emptyList(), true);
        
        assertThat(textField.getFocusListeners().length, CoreMatchers.is(expectedFocusListenerLength));
    }
    
    @Test
    public void testDecoratingJTextPane() {
        List<String> strings = Arrays.asList("Alpha", "Bravo", "Charlie", "Delta");
        AutoCompleteDecorator.decorate(new JTextPane(), strings, true);
    }
    
    @Test
    public void testAddingItemsAfterDecorating() {
        AutoCompleteDecorator.decorate(combo);
        combo.addItem("Echo");
    }
    
    @Test
    public void testAddingItemsAfterDecoratingEmpty() {
        JComboBox<String> box = new JComboBox<String>();
        AutoCompleteDecorator.decorate(box);
        box.addItem("Alhpa");
    }
    
    @Test
    public void testRemovingItemsAfterDecorating() {
        AutoCompleteDecorator.decorate(combo);
        combo.removeAll();
    }
    
    /**
     * SwingX Issue #1322.
     */
    @Test
    @Category(Visual.class)
    public void testNonStrictCompletionWithKeyMovement() {
        assumeThat(GraphicsEnvironment.isHeadless(), CoreMatchers.is(false));
        
        combo.setEditable(true);
        AutoCompleteDecorator.decorate(combo);
        combo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("".equals(combo.getSelectedItem())) {
                    fail("received illegal value");
                }
            }
        });
        
        JFrame frame = new JFrame();
        frame.add(combo);
        frame.pack();
        frame.setVisible(true);
        
        assertThat((String) combo.getSelectedItem(), CoreMatchers.is("Alpha"));
        assumeThat(combo.isPopupVisible(), CoreMatchers.is(false));
        
        combo.processKeyEvent(new KeyEvent(combo, KEY_PRESSED, currentTimeMillis(), 0, VK_DOWN, CHAR_UNDEFINED));
        assertThat(combo.isPopupVisible(), CoreMatchers.is(true));
        
        combo.processKeyEvent(new KeyEvent(combo, KEY_PRESSED, currentTimeMillis(), 0, VK_DOWN, CHAR_UNDEFINED));
        assertThat((String) combo.getSelectedItem(), CoreMatchers.is("Bravo"));
        
        combo.processKeyEvent(new KeyEvent(combo, KEY_PRESSED, currentTimeMillis(), 0, VK_PAGE_DOWN, CHAR_UNDEFINED));
        assertThat((String) combo.getSelectedItem(), CoreMatchers.is("Delta"));
        
        combo.processKeyEvent(new KeyEvent(combo, KEY_PRESSED, currentTimeMillis(), 0, VK_UP, CHAR_UNDEFINED));
        assertThat((String) combo.getSelectedItem(), CoreMatchers.is("Charlie"));
        
        combo.processKeyEvent(new KeyEvent(combo, KEY_PRESSED, currentTimeMillis(), 0, VK_PAGE_UP, CHAR_UNDEFINED));
        assertThat((String) combo.getSelectedItem(), CoreMatchers.is("Alpha"));
        
        combo.processKeyEvent(new KeyEvent(combo, KEY_PRESSED, currentTimeMillis(), 0, VK_END, CHAR_UNDEFINED));
        assertThat((String) combo.getSelectedItem(), CoreMatchers.is("Delta"));
        
        combo.processKeyEvent(new KeyEvent(combo, KEY_PRESSED, currentTimeMillis(), 0, VK_HOME, CHAR_UNDEFINED));
        assertThat((String) combo.getSelectedItem(), CoreMatchers.is("Alpha"));
    }
}
