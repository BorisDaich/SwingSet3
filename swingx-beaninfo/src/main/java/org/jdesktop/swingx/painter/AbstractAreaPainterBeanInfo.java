package org.jdesktop.swingx.painter;

import org.jdesktop.beans.BeanInfoSupport;
import org.jdesktop.beans.editors.EnumPropertyEditor;
import org.jdesktop.beans.editors.Paint2PropertyEditor;

/**
 * BeanInfo of AbstractAreaPainter.
 *
 * @author Jan Stola
 */
public class AbstractAreaPainterBeanInfo extends BeanInfoSupport {    

	/** ctor */
    public AbstractAreaPainterBeanInfo() {
        super(AbstractAreaPainter.class);
    }
    /**
     * TODO doc
     * @param clazz Class
     */
    public AbstractAreaPainterBeanInfo(Class clazz) {
        super(clazz);
    }

    @Override
    protected void initialize() {
        setPropertyEditor(StylePropertyEditor.class, "style");
        setPropertyEditor(Paint2PropertyEditor.class, "fillPaint", "borderPaint");
    }

    /**
     * TODO doc
     */
    public static final class StylePropertyEditor extends EnumPropertyEditor<AbstractAreaPainter.Style> {
    	/** ctor */
        public StylePropertyEditor() {
            super(AbstractAreaPainter.Style.class);
        }
    }

}
