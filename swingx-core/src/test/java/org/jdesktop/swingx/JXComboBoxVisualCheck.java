/*
 * Copyright 2010 Sun Microsystems, Inc., 4150 Network Circle,
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
package org.jdesktop.swingx;

import java.util.logging.Logger;

import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;

/**
 * @author kschaefer
 * @author EUG https://github.com/homebeaver (interactiveJXComboBoxEditing)
 */
public class JXComboBoxVisualCheck extends InteractiveTestCase {
    
    private static final Logger LOG = Logger.getLogger(JXComboBoxVisualCheck.class.getName());
    
    private ComboBoxModel<Object> model;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setUp() {
        model = new DefaultComboBoxModel<Object>(new JComboBox<Object>().getActionMap().allKeys());
    }
    
    public static void main(String[] args) {
//        setSystemLF(true);
    	setLAF("Nimb");
        
        JXComboBoxVisualCheck test = new JXComboBoxVisualCheck();
        
		try {
			test.runInteractiveTests();
		} catch (Exception e) {
			System.err.println("exception when executing interactive tests:");
			e.printStackTrace();
		}
    }

    /**
     * key selection respects StringValue <p>
     * type l to select "alice"
     */
    public void interactiveSelectWithKey() {
        JXComboBox<?> box = new JXComboBox<Object>(new Object[] {"alice", "berta", "carola"});
        @SuppressWarnings("serial")
		StringValue sv = new StringValue() {

            @Override
            public String getString(Object value) {
                String temp = StringValues.TO_STRING.getString(value);
                if (temp.length() > 1) {
                    temp = temp.charAt(1) + "-" + temp; // shows 2nd letter + value, i.e. l-alice
                }
                return temp;
            }
            
        };
        
        box.setRenderer(new DefaultListRenderer<Object>(sv));
        showInFrame(box, "navigation");
    }

    /**
     * shows alternate striping
     */
    public void interactiveTestComboBoxAlternateHighlighter1() {
        JXComboBox<Object> combo = new JXComboBox<Object>(model);
        combo.addHighlighter(HighlighterFactory.createSimpleStriping(HighlighterFactory.LINE_PRINTER));

        showInFrame(combo, "AlternateRowHighlighter - lineprinter");
    }
    
    public static String[] LIST_LAYOUT_ORIENTATION = 
    	{ "VERTICAL - a single column" 
    	, "VERTICAL_WRAP - flowing vertically then horizontally" 
    	, "HORIZONTAL_WRAP - flowing horizontally then vertically"
    	};
    ComboBoxEditor ed;
    /**
     * logs the combo box editor
     * you can change the edited item ==> then it is added to the combo box model
     */
    public void interactiveJXComboBoxEditing() {        
    	JComboBox<String> cellsLayout = new JXComboBox<String>(LIST_LAYOUT_ORIENTATION);
        JXFrame frame = wrapInFrame(cellsLayout, "shows editable JXComboBox with Strings");
        cellsLayout.setSelectedIndex(1);
        cellsLayout.setName("cellsLayout");
    	cellsLayout.addActionListener(ae -> {
        	cellsLayout.setSelectedIndex(cellsLayout.getSelectedIndex());
        	LOG.info("JComboBox ActionEvent "+ae);
        	LOG.info("ComboBoxEditor "+ed + " the edited item:"+ ed.getItem());       	
        });
    	cellsLayout.setEditable(true);
    	ed = cellsLayout.getEditor(); // not updeted when switch LaF
    	ed.addActionListener(ae -> {
        	LOG.info("ComboBoxEditor ActionEvent "+ae);
        	LOG.info("??? ComboBoxEditor "+ed + " the edited item:"+ ed.getItem());
        	if(cellsLayout.getSelectedItem().equals(ed.getItem())) {
        		LOG.info("SelectedItem =============== edited Item");
        		// nothing to do
        	} else {
            	LOG.info("\n the selected item:"+ cellsLayout.getSelectedItem().getClass() +" "+cellsLayout.getSelectedItem()
            	+ "\n the edited item type:"+ ed.getItem().getClass() + " "+ed.getItem());
            	int i = cellsLayout.getSelectedIndex();
            	ComboBoxModel<String> m =cellsLayout.getModel();
//            	// m implements interface MutableComboBoxModel:
//            	if(m instanceof MutableComboBoxModel<String> mm) {
//            		mm.removeElementAt(i);
//            		mm.insertElementAt((String)ed.getItem(), i);
//            	}
            	if(m instanceof DefaultComboBoxModel<String> dm) {
//            		//dm.removeElementAt(i);
//            		dm.addElement((String)ed.getItem());
////            		cellsLayout.setSelectedIndex(dm.getSize()); // IllegalArgumentException: setSelectedIndex: 4 out of bounds
//            		dm.setSelectedItem(ed.getItem());
//            		cellsLayout.updateUI(); // NPE
            		String itemToadd = (String)ed.getItem();
            		dm.addElement(itemToadd);
            		dm.setSelectedItem(itemToadd);
            	}
        	}
    	});
        addComponentOrientationToggle(frame);
        show(frame, 300, 300);
    }
    
    public void testDummy() { }

}
