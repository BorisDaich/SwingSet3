package org.jdesktop.swingx.combobox;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXFrame;

public class EnumComboBoxModelVisualCheck extends EnumComboBoxModelUnitTest {
    public static void main(String[] args) throws Exception {
        EnumComboBoxModelUnitTest test = new EnumComboBoxModelVisualCheck();
        try {
            test.runInteractiveTests();
//            test.runInteractiveTests("interactiveSelectedItem");
        } catch (Exception e) {
            System.err.println("exception when executing interactive tests:");
            e.printStackTrace();
        }
    }

    /**
     * Issue #303-swingx: EnumComboBoxModel getSelectedItem throws
     * ClassCastException.  Fixed.
     * <p>
     * A visual example using default {@code toString} implementation.
     */
    public void interactiveSelectedItem() {
        EnumComboBoxModel<MyEnum1> enumModel = new EnumComboBoxModel<MyEnum1>(
                MyEnum1.class);
        JXComboBox<MyEnum1> box = new JXComboBox<MyEnum1>(enumModel);
        box.setEditable(true);
        JXFrame frame = wrapInFrame(box, "enum combo");
        frame.setVisible(true);
    }

    /**
     * Issue #303-swingx: EnumComboBoxModel getSelectedItem throws
     * ClassCastException.  Fixed.
     * <p>
     * A visual example using a custom {@code toString} implementation.
     */
    public void interactiveSelectedItemWithCustomToString() {
        EnumComboBoxModel<MyEnum2> enumModel = new EnumComboBoxModel<MyEnum2>(
                MyEnum2.class);
        JXComboBox box = new JXComboBox(enumModel);
        box.setEditable(true);
        JXFrame frame = wrapInFrame(box, "enum with custom toString combo");
        frame.setVisible(true);
    }
}
