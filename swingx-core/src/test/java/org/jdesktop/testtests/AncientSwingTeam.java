package org.jdesktop.testtests;

import java.awt.Color;
import java.util.Arrays;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.ListModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.jdesktop.swingx.combobox.ListModelComboBoxWrapper;
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableModel;

/**
 * Standard Table with class-Infos. Taken from some old SwingSet2 TableDemo ... 
 * Can remove/add rows.
 */
@SuppressWarnings("serial")
public class AncientSwingTeam extends AbstractTableModel {

    public static final int COLOR_COLUMN = 2;
    public static final int INTEGER_COLUMN = 3;
    public static final int BOOLEAN_COLUMN = 4;

    /**
     * Creates and returns a listModel with items of type NamedColor.
     * @return a ListModel containing items of type NamedColor.
     */
    public static ListModel<Object> createNamedColorListModel() {
        final TableModel wrappee = new AncientSwingTeam();
        ListModel<Object> model = new AbstractListModel<Object>() {

            public Object getElementAt(int index) {
                return wrappee.getValueAt(index, 2);
            }

            public int getSize() {
                return wrappee.getRowCount();
            }
            
        };
        return model;
    };
    
    public static ComboBoxModel<Object> createNamedColorComboBoxModel() {
        return new ListModelComboBoxWrapper(createNamedColorListModel());
    }
    
    /**
     * 
     * Creates and returns a DefaultTreeModel with a String root and
     * children of type NamedColor wrapped into DefaultMutableTreeNodes.
     * @return a DefaultTreeModel containing items of type NamedColor.
     */
    public static DefaultTreeModel createNamedColorTreeModel() {
        final TableModel wrappee = new AncientSwingTeam();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Named Colors");
        for (int i = 0; i < wrappee.getRowCount(); i++) {
            root.add(new DefaultMutableTreeNode(wrappee.getValueAt(i, 2)));
        }
        return new DefaultTreeModel(root);
    };

    /**
     * 
     * Creates and returns a DefaultTreeModel with a String root and
     * children of type NamedColor wrapped into DefaultMutableTreeNodes.
     * @return a DefaultTreeModel containing items of type NamedColor.
     */
    public static TreeTableModel createNamedColorTreeTableModel() {
        final TableModel wrappee = new AncientSwingTeam();
        DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode("Named Colors");
        for (int i = 0; i < wrappee.getRowCount(); i++) {
            MutableTreeTableNode node = createNamedColorTreeTableNode(wrappee, i);
            root.add(node);
        }
        return new DefaultTreeTableModel(root, Arrays.asList(new String[] { "Color", "LastName" }));
    }

    private static MutableTreeTableNode createNamedColorTreeTableNode( final TableModel wrappee, final int i) {
        MutableTreeTableNode node = new AbstractMutableTreeTableNode() {

            public int getColumnCount() {
                return 2;
            }

            public Object getValueAt(int column) {
                switch (column) {
                case 0:
                    return wrappee.getValueAt(i, 2);
                case 1: 
                    return wrappee.getValueAt(i, 1);
                }
                return null;
            }

            @Override
            public Object getUserObject() {
                return getValueAt(0);
            }

            @Override
            public boolean isEditable(int column) {
                return true;
            }

            @Override
            public String toString() {
                if (getUserObject() == null) return "";
                return getUserObject().toString();
            }

        };
        return node;
    };
    
	NamedColor aqua = new NamedColor(new Color(127, 255, 212), "Aqua");
	NamedColor beige = new NamedColor(new Color(245, 245, 220), ("Beige"));
	NamedColor black = new NamedColor(Color.black, "Black");
	NamedColor blue = new NamedColor(new Color(0, 0, 222), "Blue");
	NamedColor eblue = new NamedColor(Color.blue, "Electric Blue");
	NamedColor jfcblue = new NamedColor(new Color(204, 204, 255), "JFC Primary");
	NamedColor jfcblue2 = new NamedColor(new Color(153, 153, 204), "JFC SEcondary");
	NamedColor cybergreen = new NamedColor(Color.green.darker().brighter(), "Cyber Green");
	NamedColor darkgreen = new NamedColor(new Color(0, 100, 75), "darkgreen");
	NamedColor forestgreen = new NamedColor(Color.green.darker(), "Forest Green");
	NamedColor gray = new NamedColor(Color.gray, "Gray");
	NamedColor green = new NamedColor(Color.green, "Green");
	NamedColor orange = new NamedColor(new Color(255, 165, 0), "Orange");
	NamedColor purple = new NamedColor(new Color(160, 32, 240), "Purple");
	NamedColor red = new NamedColor(Color.red, "Red");
	NamedColor rustred = new NamedColor(Color.red.darker(), "Rust Red");
	NamedColor sunpurple = new NamedColor(new Color(100, 100, 255), "Sun Purple");
	NamedColor suspectpink = new NamedColor(new Color(255, 105, 180), "Suspect Pink");
	NamedColor turquoise = new NamedColor(new Color(0, 255, 255), "Turquoise");
	NamedColor violet = new NamedColor(new Color(238, 130, 238), "Violet");
	NamedColor yellow = new NamedColor(Color.yellow, "Yellow");

	protected final String[] names = { "First Name", "Last Name", "Favorite Color", "No.", "Vegetarian" };
    protected final Object[][] data = {
        { "Mark", "Andrews", red, Integer.valueOf(2), Boolean.valueOf(true) },
        { "Tom", "Ball", blue, Integer.valueOf(99), Boolean.valueOf(false) },
        { "Alan", "Chung", green, Integer.valueOf(838), Boolean.valueOf(false) },
        { "Jeff", "Dinkins", turquoise, Integer.valueOf(8), Boolean.valueOf(true) },
        { "Amy", "Fowler", yellow, Integer.valueOf(3), Boolean.valueOf(false) },
        { "Brian", "Gerhold", green, Integer.valueOf(0), Boolean.valueOf(false) },
        { "James", "Gosling", suspectpink, Integer.valueOf(21), Boolean.valueOf(false) },
        { "David", "Karlton", red, Integer.valueOf(1), Boolean.valueOf(false) },
        { "Dave", "Kloba", yellow, Integer.valueOf(14), Boolean.valueOf(false) },
        { "Peter", "Korn", purple, Integer.valueOf(12), Boolean.valueOf(false) },
        { "Phil", "Milne", purple, Integer.valueOf(3), Boolean.valueOf(false) },
        { "Dave", "Moore", green, Integer.valueOf(88), Boolean.valueOf(false) },
        { "Hans", "Muller", rustred, Integer.valueOf(5), Boolean.valueOf(false) },

        { "Rick", "Levenson", blue, Integer.valueOf(2), Boolean.valueOf(false) },
        { "Tim", "Prinzing", blue, Integer.valueOf(22), Boolean.valueOf(false) },
        { "Chester", "Rose", black, Integer.valueOf(0), Boolean.valueOf(false) },
        { "Ray", "Ryan", gray, Integer.valueOf(77), Boolean.valueOf(false) },
        { "Georges", "Saab", red, Integer.valueOf(4), Boolean.valueOf(false) },
        { "Willie", "Walker", jfcblue, Integer.valueOf(4), Boolean.valueOf(false) },

        { "Kathy", "Walrath", blue, Integer.valueOf(8), Boolean.valueOf(false) },
        { "Arnaud", "Weber", green, Integer.valueOf(44), Boolean.valueOf(false) } 
    };

    protected int rowCount = data.length;

    public AncientSwingTeam() {

    }

     public AncientSwingTeam(int count) {
         rowCount = count;
     }

    public int getColumnCount() {
        return names.length;
    }

    public int getRowCount() {
        return rowCount;
    }

    /** reuses values internally */
    public Object getValueAt(int row, int col) {
        // following shows only every second value
        // if ((row + col) % 2 == 0) return null;
        return data[row % data.length][col];
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        data[row % data.length][col] = value;
        fireTableCellUpdated(row, col);
    }

    // The default implementations of these methods in
    // AbstractTableModel would work, but we can refine them.
    @Override
    public String getColumnName(int column) {
        return names[column];
    }

    /** returns class of column by asking class of value in first row. */
    @Override
    public Class<?> getColumnClass(int c) {
        Object value = null;
        if (getRowCount() > 0) {
            value = getValueAt(0, c);
        }
        if (value == null) {
            return Object.class;
        }
        return value.getClass();
    }

    /** everything is editable. */
    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    /**
     * insert length rows at rowIndex. PRE: rowIndex <= getRowCount()
     */
    public void insertRows(int rowIndex, int length) {
        rowCount += length;
        fireTableRowsInserted(rowIndex, rowIndex + length - 1);
    }

    /**
     * remove rows. NOTE: not tested
     */
    public void removeRows(int rowIndex, int length) {
        rowCount -= length;
        if (rowCount < 0) {
            length -= rowCount;
            rowCount = 0;
        }
        fireTableRowsDeleted(rowIndex, rowIndex + length - 1);
    }
    
    public static class NamedColor extends Color {
        String name;
        public NamedColor(Color color, String name) {
            super(color.getRGB());
            this.name = name;
        }
        
        public Color getTextColor() {
            int r = getRed();
            int g = getGreen();
            if(r > 240 || g > 240) {
                return Color.black;
            } else {
                return Color.white;
            }
        }
        @Override
        public String toString() {
            return name;
        }

    }    
} // end class SwingTeam
