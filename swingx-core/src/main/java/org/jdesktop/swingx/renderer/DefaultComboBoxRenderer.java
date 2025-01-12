package org.jdesktop.swingx.renderer;

import java.awt.Component;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXComboBox;

/*
 
 ersetzt javax.swing.plaf.basic.BasicComboBoxRenderer

public class BasicComboBoxRenderer extends JLabel
implements ListCellRenderer<Object>, Serializable {

wie DefaultListRenderer<E>

E == Object
 */
public class DefaultComboBoxRenderer<E> extends AbstractRenderer implements ListCellRenderer<E> {

	private static final long serialVersionUID = -441370707689161796L;
	private static final Logger LOG = Logger.getLogger(DefaultComboBoxRenderer.class.getName());

    public DefaultComboBoxRenderer() {
    	// null ==> createDefaultComponentProvider()
        this((ComponentProvider<?>) null);
    }
    /*
public abstract class ComponentProvider<T extends JComponent> implements Serializable, UIDependent {
public class LabelProvider extends ComponentProvider<JLabel> {
     */
    public DefaultComboBoxRenderer(ComponentProvider<?> componentProvider) {
        super(componentProvider);
		LOG.config("ctor ComponentProvider<?> componentProvider componentController="+componentController);
//        this.cellContext = new ListCellContext();
		this.cc = new ComboBoxContext();
    }

    static Border etchedNoFocusBorder = BorderFactory.createEtchedBorder();
    Border getEmptyNoFocusBorder() {
    	return new EmptyBorder(1, 1, 1, 1);
    }
    JXComboBox<?> cb;
    ComboBoxContext cc;
	@Override
	public Component getListCellRendererComponent(JList<? extends E> list, E value, 
			int index, boolean isSelected, boolean cellHasFocus) {

//      public void installContext(JComboBox<?> component, Object value, int row, int column,
//              boolean selected, boolean focused, boolean expanded, boolean leaf) {
		cc.installContext(cb, value, index, -1, isSelected, cellHasFocus, false, false);
        JComponent comp = componentController.getRendererComponent(cc);
// TODO auskommentieren:
		if(isSelected) {
			System.out.println("DefaultComboBoxRenderer getListCellRendererComponent: list:"
					+list // the JList we're painting
	    			+"\n value:"+value+"/"+(value==null?"null":value.getClass())
	    			+"\n index="+index+" , isSelected="+isSelected+" , cellHasFocus="+cellHasFocus
	    			+"\n DefaultComboBoxRenderer this.hashCode=@"+Integer.toHexString(this.hashCode())
	    				);
	    		
	        LOG.info(" ListCellRendererComponent:"+comp);
		}
        /* in componentController.getRendererComponent(context)
        if (context != null) {
            configureVisuals(context);
            configureContent(context);
        }

         */
        comp.setBorder(index == -1 ? getEmptyNoFocusBorder() : etchedNoFocusBorder);
//			if (isSelected) {
//				setBackground(list.getSelectionBackground());
//				setForeground(list.getSelectionForeground());
//			} else {
//				setBackground(list.getBackground());
//				setForeground(list.getForeground());
//			}
//
//			setFont(list.getFont());
//
//			if (value instanceof Icon) {
//				setIcon((Icon) value);
//			} else {
//				setText((value == null) ? "" : value.toString());
//			}
        
// component whose paint() method will render the specified value.
		return comp;
	}

	@Override
	protected ComponentProvider<?> createDefaultComponentProvider() {
		ComponentProvider<JLabel> labelProvider = new LabelProvider(createDefaultStringValue());
		LOG.info("ComponentProvider<JLabel> labelProvider:"+labelProvider);
		return labelProvider;
	}

    private StringValue createDefaultStringValue() {
        return MappedValues.STRING_OR_ICON_ONLY;
    }

}
