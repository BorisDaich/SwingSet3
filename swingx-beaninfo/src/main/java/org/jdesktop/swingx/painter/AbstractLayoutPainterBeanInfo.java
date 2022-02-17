package org.jdesktop.swingx.painter;

import org.jdesktop.beans.BeanInfoSupport;
import org.jdesktop.beans.editors.EnumPropertyEditor;

/**
 * BeanInfo of AbstractLayoutPainter.
 *
 * @author Jan Stola
 */
public class AbstractLayoutPainterBeanInfo extends BeanInfoSupport {

	/** ctor */
    public AbstractLayoutPainterBeanInfo() {
        super(AbstractLayoutPainter.class);
    }
    
    /**
     * ctor 
     * @param clazz Class
     */
    public AbstractLayoutPainterBeanInfo(Class clazz) {
        super(clazz);
    }

    @Override
    protected void initialize() {
        setPropertyEditor(HorizontalAlignmentPropertyEditor.class, "horizontalAlignment");
        setPropertyEditor(VerticalAlignmentPropertyEditor.class, "verticalAlignment");
    }

    /**
     * TODO doc
     */
    public static final class HorizontalAlignmentPropertyEditor extends EnumPropertyEditor<AbstractLayoutPainter.HorizontalAlignment> {
    	/** ctor */
        public HorizontalAlignmentPropertyEditor() {
            super(AbstractLayoutPainter.HorizontalAlignment.class);
        }
    }

    /**
     * TODO doc
     */
    public static final class VerticalAlignmentPropertyEditor extends EnumPropertyEditor<AbstractLayoutPainter.VerticalAlignment> {
    	/** ctor */
        public VerticalAlignmentPropertyEditor() {
            super(AbstractLayoutPainter.VerticalAlignment.class);
        }
    }

}
