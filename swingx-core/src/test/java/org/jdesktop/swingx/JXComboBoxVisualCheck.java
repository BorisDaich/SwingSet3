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

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.ColorUIResource;

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
//    	setLAF("Nimb");
        
        JXComboBoxVisualCheck test = new JXComboBoxVisualCheck();
        
		try {
			test.runInteractiveTests();
//			test.runInteractiveTests("interactiveJXComboBoxEditing");
//			test.runInteractiveTests("interactiveJXComboBoxColorEditing");
			
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
    	, "your text"
    	};
    ComboBoxEditor ed;
    /**
     * logs the combo box editor
     * you can change the edited item ==> then it is added to the combo box model
     */
    public void interactiveJXComboBoxEditing() {        
    	JComboBox<String> cellsLayout = new JXComboBox<String>(LIST_LAYOUT_ORIENTATION);
    	JPanel panel = new JPanel();
    	panel.add(cellsLayout);
        JXFrame frame = wrapInFrame(panel, "shows editable JXComboBox with Strings");
        cellsLayout.setSelectedIndex(1);
        cellsLayout.setName("cellsLayout");
    	cellsLayout.addActionListener(ae -> {
        	LOG.info("JComboBox ActionEvent "+ae);
        	int i = cellsLayout.getSelectedIndex();
        	if(i>-1) {
            	cellsLayout.setSelectedIndex(i);
            	LOG.info("ComboBoxEditor "+ed + " the edited item("+i+"):"+ ed.getItem());
//        		cellsLayout.setEditable(i>=3); //LIST_LAYOUT_ORIENTATION.length-1);
        	}
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
//            	int i = cellsLayout.getSelectedIndex();
            	ComboBoxModel<String> m = cellsLayout.getModel();
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
        show(frame, 400, 300);
    }
    
    public static Color[] colors = 
    	{ Color.WHITE
        , Color.YELLOW 
    	, Color.BLACK
    	// TODO colorcb.getSelectedIndex() bzw. colorcb.setSelectedItem findet das Objekt in Nimbus nicht
    	// in Metal OK!
    	, new ColorUIResource(102, 102, 153) // Metal primary1  
    	, new Color(102, 102, 153)
    	};
    public class RedColorMsg extends Color {
    	String m;
    	public RedColorMsg(String msg) {
    		super(Color.RED.getRGB());
    		m = msg;
    	}
    	public String toString() {
    		return m + " " + Color.class.getName() + "[r=" + getRed() + ",g=" + getGreen() + ",b=" + getBlue() + "]";
    	}
    }
    // see public class BasicComboBoxEditor implements ComboBoxEditor,FocusListener {
    public class ColorComboBoxEditor implements ComboBoxEditor {

        protected JTextField editor;
        private Object oldValue;

        public ColorComboBoxEditor(Color color) {
            editor = createEditorComponent(color);
        }
        protected JTextField createEditorComponent(Color color) {
            JTextField editor = new JTextField(color.toString(),33);
            editor.setBorder(null);
            editor.setBackground(color);
            return editor;
        }

		public Component setEditorComponent(Object o) {
			if(o instanceof Color color) {
				editor.setText(color.toString());
	            editor.setBackground(color);
			} else {
				LOG.warning("??????????? Object not Color:"+o);
			}
			return editor;
		}
		
		@Override
		public Component getEditorComponent() {
			return editor;
		}

		@Override
		public void setItem(Object anObject) {
			String text = "";
			if(anObject instanceof Color color) {
		        if ( anObject != null )  {
		            text = anObject.toString();
		            if (text == null) {
		                text = "";
		            }
		            oldValue = anObject;
//		            editor.setText(text);
//		            editor.setBackground(color);
		        }
		        if (! text.equals(editor.getText())) { // kommt nicht dran
		            editor.setText(text);
		            editor.setBackground(color);
		        }
			} else {
		        if (! text.equals(editor.getText())) {
		            editor.setText(text);
		        }
			}
		}

		@Override
		public Object getItem() {
	        Object newText = editor.getText();
	        Object newValue = editor.getBackground();
	        if(!newValue.toString().endsWith(newText.toString())) {
//	        if(!newText.equals(newValue)) {
	        	newValue = newText;
	        	LOG.warning("Object newValue:"+newValue);
//	        	return new RedColorMsg(""+newText);
	        }

	        if (oldValue != null && !(oldValue instanceof Color))  {
	            // The original value is not a color. Should return the value in it's original type.
	            if (newValue.equals(oldValue.toString()))  {
	                return oldValue;
	            } else {
	            	LOG.warning("??????????? Object oldValue:"+oldValue);
	                // Must take the value from the editor and get the value and cast it to the new type.
//	                Class<?> cls = oldValue.getClass();
//	                try {
//	                    Method method = MethodUtil.getMethod(cls, "valueOf", new Class<?>[]{String.class});
//	                    newValue = MethodUtil.invoke(method, oldValue, new Object[] { editor.getText()});
//	                } catch (Exception ex) {
//	                    // Fail silently and return the newValue (a String object)
//	                }
	            }
	        }
	        return newValue;
		}

		@Override
		public void selectAll() {
	        editor.selectAll();
	        editor.requestFocus();
		}

		@Override
		public void addActionListener(ActionListener l) {
	        editor.addActionListener(l);
		}

		@Override
		public void removeActionListener(ActionListener l) {
	        editor.removeActionListener(l);
		}
		
//		// implement java.awt.event.FocusEvent
//		@Override
//		public void focusGained(FocusEvent e) {
//			LOG.warning("??????????? FocusEvent:"+e);
//		}
//		// implement java.awt.event.FocusEvent
//		@Override
//		public void focusLost(FocusEvent e) {
//			LOG.warning("??????????? FocusEvent:"+e);
//		}
    	
    }
    ComboBoxEditor colorEd; // = new ColorComboBoxEditor(Color.BLACK);
    /**
     * a combo box editor for colors (the background is painted with the color selected)
     * you can change the edited item ==> then it is added to the combo box model
     */
    public void interactiveJXComboBoxColorEditing() {        
    	JComboBox<Color> colorcb = new JXComboBox<Color>(colors);
        JXFrame frame = wrapInFrame(colorcb, "shows editable JXComboBox with Colors");
        colorcb.setSelectedIndex(3);
        colorcb.setEditor(new ColorComboBoxEditor(colors[3]));
        colorcb.setName("colorcb");
    	colorcb.addActionListener(ae -> {
        	LOG.info("JComboBox ActionEvent "+ae);
//        	colorcb.setSelectedIndex(colorcb.getSelectedIndex());
        	colorcb.setSelectedItem(colorcb.getSelectedItem());
//        	((ColorComboBoxEditor)colorEd).setEditorComponent(colorcb.getSelectedItem());
        	LOG.info("ComboBoxEditor "+colorEd + " the edited item:"+ colorEd.getItem());
//        	colorEd.setItem(colorcb.getSelectedItem());
        });
    	colorcb.setEditable(true);
    	colorEd = colorcb.getEditor(); // not updeted when switch LaF
    	colorEd.addActionListener(ae -> {
        	LOG.info("ComboBoxEditor ActionEvent "+ae);
        	Object item = colorEd.getItem();
        	LOG.info("??? ComboBoxEditor "+colorEd + " the edited item:"+ item);
        	if(colorcb.getSelectedItem().equals(item)) {
        		LOG.info("SelectedItem =============== edited Item");
        		// nothing to do
        	} else {
            	LOG.info("\n the selected item:"+ colorcb.getSelectedItem().getClass() +" "+colorcb.getSelectedItem()
            	+ "\n the edited item type:"+ item.getClass() + " "+item);
            	int i = colorcb.getSelectedIndex();
            	ComboBoxModel<Color> m = colorcb.getModel();
            	if(m instanceof DefaultComboBoxModel<Color> dm) {
            		if(item instanceof Color itemToadd ) {
                		dm.addElement(itemToadd);
                		dm.setSelectedItem(itemToadd);
                	} else {
                		LOG.warning("TODO item "+item);
                		Color reditem = new RedColorMsg(""+item);
                		dm.addElement(reditem);
                		dm.setSelectedItem(reditem);
            		}
            	}
        	}
    	});
        addComponentOrientationToggle(frame);
        show(frame, 300, 300);
    }
    
    public void testDummy() { }

}
