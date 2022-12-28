package org.jdesktop.swingx.plaf.basic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.text.Position;

import org.jdesktop.swingx.SwingXUtilities;
import org.jdesktop.swingx.plaf.LookAndFeelUtils;
import org.jdesktop.swingx.plaf.YListUI;
import org.jdesktop.swingx.plaf.basic.core.BasicTransferable;
import org.jdesktop.swingx.plaf.basic.core.DragRecognitionSupport;
import org.jdesktop.swingx.plaf.basic.core.DragRecognitionSupport.BeforeDrag;
import org.jdesktop.swingx.plaf.basic.core.LazyActionMap;

/*

JList hierarchie:
                 public abstract class javax.swing.plaf.ListUI extends ComponentUI
                                                          |
public class javax.swing.plaf.basic.BasicListUI extends ListUI

Instanziert wird die Klasse über die factory createUI, 
ähnlich zu ursprünglichem Ansatz: org.jdesktop.swingx.plaf.basic.core.BasicXListUI extends BasicListUI
Im Gegensatz zu BasicXListUI will ich mit BasicYListUI keine Erweiterungen vornehmen. Es soll lediglich
- Color "List.background" durch secondary3 Color der CurrentTheme ersetzt werden
- und NoFocusBorder geändert werden
Ziel ist, BasicXListUI von BasicYListUI abzuleiten. 
Dann gibt es eine klare Trennung der neuen features (sorting/filtering)

Die systematische/symetrische Ableitung wäre dann:

                                         ComponentUI
                                          |
YListUI ----------------- abstract class ListUI
 |                                        |
BasicYListUI               symetrisch zu BasicListUI
 |  |                                     |
 | BasicXListUI                           |
SynthYListUI |              symetrisch zu SynthListUI
   BasicXListUI

es ist nur scheinbar einfacher, direkt von javax.swing.plaf.basic.BasicListUI ableiten. Es wird unübersichtlich!
Also doch von YListUI ableiten ==> alles fast 1:1 von BasicListUI abkopieren
 in abstract class ListUI : implementation in super YListUI
    protected ListUI() {}
    public abstract int locationToIndex(JList<?> list, Point location);
    public abstract Point indexToLocation(JList<?> list, int index);
    public abstract Rectangle getCellBounds(JList<?> list, int index1, int index2);

 */
public class BasicYListUI extends YListUI {

    private static final Logger LOG = Logger.getLogger(BasicYListUI.class.getName());

	// factory
    public static ComponentUI createUI(JComponent c) {
    	LOG.config("UI factory for JComponent:"+c);
        return new BasicYListUI(c);
    }

    // like in BasicListUI
    public static void loadActionMap(LazyActionMap map) {
        map.put(new Actions(Actions.SELECT_PREVIOUS_COLUMN));
        map.put(new Actions(Actions.SELECT_PREVIOUS_COLUMN_EXTEND));
        map.put(new Actions(Actions.SELECT_PREVIOUS_COLUMN_CHANGE_LEAD));
        map.put(new Actions(Actions.SELECT_NEXT_COLUMN));
        map.put(new Actions(Actions.SELECT_NEXT_COLUMN_EXTEND));
        map.put(new Actions(Actions.SELECT_NEXT_COLUMN_CHANGE_LEAD));
        map.put(new Actions(Actions.SELECT_PREVIOUS_ROW));
        map.put(new Actions(Actions.SELECT_PREVIOUS_ROW_EXTEND));
        map.put(new Actions(Actions.SELECT_PREVIOUS_ROW_CHANGE_LEAD));
        map.put(new Actions(Actions.SELECT_NEXT_ROW));
        map.put(new Actions(Actions.SELECT_NEXT_ROW_EXTEND));
        map.put(new Actions(Actions.SELECT_NEXT_ROW_CHANGE_LEAD));
        map.put(new Actions(Actions.SELECT_FIRST_ROW));
        map.put(new Actions(Actions.SELECT_FIRST_ROW_EXTEND));
        map.put(new Actions(Actions.SELECT_FIRST_ROW_CHANGE_LEAD));
        map.put(new Actions(Actions.SELECT_LAST_ROW));
        map.put(new Actions(Actions.SELECT_LAST_ROW_EXTEND));
        map.put(new Actions(Actions.SELECT_LAST_ROW_CHANGE_LEAD));
        map.put(new Actions(Actions.SCROLL_UP));
        map.put(new Actions(Actions.SCROLL_UP_EXTEND));
        map.put(new Actions(Actions.SCROLL_UP_CHANGE_LEAD));
        map.put(new Actions(Actions.SCROLL_DOWN));
        map.put(new Actions(Actions.SCROLL_DOWN_EXTEND));
        map.put(new Actions(Actions.SCROLL_DOWN_CHANGE_LEAD));
        map.put(new Actions(Actions.SELECT_ALL));
        map.put(new Actions(Actions.CLEAR_SELECTION));
        map.put(new Actions(Actions.ADD_TO_SELECTION));
        map.put(new Actions(Actions.TOGGLE_AND_ANCHOR));
        map.put(new Actions(Actions.EXTEND_TO));
        map.put(new Actions(Actions.MOVE_SELECTION_TO));

        map.put(TransferHandler.getCutAction().getValue(Action.NAME),
                TransferHandler.getCutAction());
        map.put(TransferHandler.getCopyAction().getValue(Action.NAME),
                TransferHandler.getCopyAction());
        map.put(TransferHandler.getPasteAction().getValue(Action.NAME),
                TransferHandler.getPasteAction());
    }

	@SuppressWarnings("unchecked")
	public BasicYListUI(JComponent c) {
		super();
		JList<?> tmp = (JList<?>) c;
		super.list = (JList<Object>) tmp;
	}

	// vars in super:
//    protected JList<Object> list = null;
// ...

	// var like in javax.swing.plaf.basic.BasicListUI:
	protected Handler handler;

    protected static final int DROP_LINE_THICKNESS = 2;
    protected static final StringBuilder BASELINE_COMPONENT_KEY = new StringBuilder("List.baselineComponent");

    /**
     * Paint one List cell: compute the relevant state, get the "rubber stamp"
     * cell renderer component, and then use the {@code CellRendererPane} to paint it.
     * Subclasses may want to override this method rather than {@code paint()}.
     *
     * @param g an instance of {@code Graphics}
     * @param row a row
     * @param rowBounds a bounding rectangle to render to
     * @param cellRenderer a list of {@code ListCellRenderer}
     * @param dataModel a list model
     * @param selModel a selection model
     * @param leadIndex a lead index
     * @see #paint
     */
    // copied from javax.swing.plaf.basic.BasicListUI
	protected void paintCell(Graphics g, int row, Rectangle rowBounds, ListCellRenderer<Object> cellRenderer,
			ListModel<Object> dataModel, ListSelectionModel selModel, int leadIndex) {
        Object value = dataModel.getElementAt(row);
        boolean cellHasFocus = list.hasFocus() && (row == leadIndex);
        boolean isSelected = selModel.isSelectedIndex(row);
//    	LOG.info("row="+row + ",value:"+value 
//    			+ ",isSelected="+isSelected + ",cellHasFocus="+cellHasFocus + ",cellRenderer:"+cellRenderer);

        Component rendererComponent =
            cellRenderer.getListCellRendererComponent(list, value, row, isSelected, cellHasFocus);
//        Border border=null;
//        if(rendererComponent instanceof JYList<?>.YListCellRenderer yListCellRenderer) {
//        	border = yListCellRenderer.getBorder();       	
//        }
//    	LOG.info("row="+row + ",value:"+value 
//    			+ ",isSelected="+isSelected + ",cellHasFocus="+cellHasFocus + ",border:"+border);

        int cx = rowBounds.x;
        int cy = rowBounds.y;
        int cw = rowBounds.width;
        int ch = rowBounds.height;

        if (isFileList) {
            // Shrink renderer to preferred size. This is mostly used on Windows
            // where selection is only shown around the file name, instead of
            // across the whole list cell.
            int w = Math.min(cw, rendererComponent.getPreferredSize().width + 4);
            if (!isLeftToRight) {
                cx += (cw - w);
            }
            cw = w;
        }

        rendererPane.paintComponent(g, rendererComponent, list, cx, cy, cw, ch, true);
    }


    /**
     * {@inheritDoc} <p>
     * copied from javax.swing.plaf.basic.BasicListUI to call paintImpl
     */
    @Override // defined in ComponentUI
    public void paint(Graphics g, JComponent c) {
    	LOG.fine("JComponent:"+c);
        Shape clip = g.getClip();
        paintImpl(g, c);
        g.setClip(clip);

        paintDropLine(g);
    }

    // copied from javax.swing.plaf.basic.BasicListUI + LOGs
    protected void paintImpl(Graphics g, JComponent c) {
        switch (layoutOrientation) {
        case JList.VERTICAL_WRAP:
            if (list.getHeight() != listHeight) {
                updateLayoutStateNeeded |= heightChanged;
                redrawList();
            }
            break;
        case JList.HORIZONTAL_WRAP:
            if (list.getWidth() != listWidth) {
                updateLayoutStateNeeded |= widthChanged;
                redrawList();
            }
            break;
        default:
            break;
        }
        maybeUpdateLayoutState();

        ListCellRenderer<Object> renderer = list.getCellRenderer();
        ListModel<Object> dataModel = getViewModel();
        ListSelectionModel selModel = list.getSelectionModel();
        int size;

        if ((renderer == null) || (size = dataModel.getSize()) == 0) {
            return;
        }

        // Determine how many columns we need to paint
        Rectangle paintBounds = g.getClipBounds();

        int startColumn, endColumn;
        if (c.getComponentOrientation().isLeftToRight()) {
            startColumn = convertLocationToColumn(paintBounds.x,
                                                  paintBounds.y);
            endColumn = convertLocationToColumn(paintBounds.x +
                                                paintBounds.width,
                                                paintBounds.y);
        } else {
            startColumn = convertLocationToColumn(paintBounds.x +
                                                paintBounds.width,
                                                paintBounds.y);
            endColumn = convertLocationToColumn(paintBounds.x,
                                                  paintBounds.y);
        }
        int maxY = paintBounds.y + paintBounds.height;
        int leadIndex = adjustIndex(list.getLeadSelectionIndex(), list);
        int rowIncrement = (layoutOrientation == JList.HORIZONTAL_WRAP) ?
                           columnCount : 1;

        LOG.config("how many columns we need to paint? startColumn="+startColumn+",endColumn="+endColumn);
        for (int colCounter = startColumn; colCounter <= endColumn; colCounter++) {
            // And then how many rows in this columnn
            int row = convertLocationToRowInColumn(paintBounds.y, colCounter);
            int rowCount = getRowCount(colCounter);
            int index = getModelIndex(colCounter, row);
            Rectangle rowBounds = getCellBounds(list, index, index);
            LOG.config("how many rows in "+colCounter+" columnn? row="+row+",rowCount="+rowCount+",index="+index+",rowIncrement="+rowIncrement);
            if (rowBounds == null) {
                // Not valid, bail!
                return;
            }
            while (row < rowCount && rowBounds.y < maxY && index < size) {
                rowBounds.height = getHeight(colCounter, row);
                g.setClip(rowBounds.x, rowBounds.y, rowBounds.width, rowBounds.height);
                g.clipRect(paintBounds.x, paintBounds.y, paintBounds.width, paintBounds.height);
                paintCell(g, index, rowBounds, renderer, dataModel, selModel, leadIndex);
                rowBounds.y += rowBounds.height;
                index += rowIncrement;
                row++;
            }
        }
        // Empty out the renderer pane, allowing renderers to be gc'ed.
        rendererPane.removeAll();
    }

    // copied from javax.swing.plaf.basic.BasicListUI with change
	protected void paintDropLine(Graphics g) {
		JList.DropLocation loc = list.getDropLocation();
		if (loc == null || !loc.isInsert()) {
			return;
		}
        // PENDING JW: revisit ... side-effects?
		Color c = UIManager.getColor("List.dropLineColor");
		if (c != null) {
			g.setColor(c);
			Rectangle rect = getDropLineRect(loc);
			g.fillRect(rect.x, rect.y, rect.width, rect.height);
		}
	}

    // copied from javax.swing.plaf.basic.BasicListUI
    protected Rectangle getDropLineRect(JList.DropLocation loc) {
        int size = getElementCount();

        if (size == 0) {
            Insets insets = list.getInsets();
            if (layoutOrientation == JList.HORIZONTAL_WRAP) {
                if (isLeftToRight) {
                    return new Rectangle(insets.left, insets.top, DROP_LINE_THICKNESS, 20);
                } else {
                    return new Rectangle(list.getWidth() - DROP_LINE_THICKNESS - insets.right,
                                         insets.top, DROP_LINE_THICKNESS, 20);
                }
            } else {
                return new Rectangle(insets.left, insets.top,
                                     list.getWidth() - insets.left - insets.right,
                                     DROP_LINE_THICKNESS);
            }
        }

        Rectangle rect = null;
        int index = loc.getIndex();
        boolean decr = false;

        if (layoutOrientation == JList.HORIZONTAL_WRAP) {
            if (index == size) {
                decr = true;
            } else if (index != 0 && convertModelToRow(index)
                                         != convertModelToRow(index - 1)) {

                Rectangle prev = getCellBounds(list, index - 1);
                Rectangle me = getCellBounds(list, index);
                Point p = loc.getDropPoint();

                if (isLeftToRight) {
                    decr = Point2D.distance(prev.x + prev.width,
                                            prev.y + (int)(prev.height / 2.0),
                                            p.x, p.y)
                           < Point2D.distance(me.x,
                                              me.y + (int)(me.height / 2.0),
                                              p.x, p.y);
                } else {
                    decr = Point2D.distance(prev.x,
                                            prev.y + (int)(prev.height / 2.0),
                                            p.x, p.y)
                           < Point2D.distance(me.x + me.width,
                                              me.y + (int)(prev.height / 2.0),
                                              p.x, p.y);
                }
            }

            if (decr) {
                index--;
                rect = getCellBounds(list, index);
                if (isLeftToRight) {
                    rect.x += rect.width;
                } else {
                    rect.x -= DROP_LINE_THICKNESS;
                }
            } else {
                rect = getCellBounds(list, index);
                if (!isLeftToRight) {
                    rect.x += rect.width - DROP_LINE_THICKNESS;
                }
            }

            if (rect.x >= list.getWidth()) {
                rect.x = list.getWidth() - DROP_LINE_THICKNESS;
            } else if (rect.x < 0) {
                rect.x = 0;
            }

            rect.width = DROP_LINE_THICKNESS;
        } else if (layoutOrientation == JList.VERTICAL_WRAP) {
            if (index == size) {
                index--;
                rect = getCellBounds(list, index);
                rect.y += rect.height;
            } else if (index != 0 && convertModelToColumn(index)
                                         != convertModelToColumn(index - 1)) {

                Rectangle prev = getCellBounds(list, index - 1);
                Rectangle me = getCellBounds(list, index);
                Point p = loc.getDropPoint();
                if (Point2D.distance(prev.x + (int)(prev.width / 2.0),
                                     prev.y + prev.height,
                                     p.x, p.y)
                        < Point2D.distance(me.x + (int)(me.width / 2.0),
                                           me.y,
                                           p.x, p.y)) {

                    index--;
                    rect = getCellBounds(list, index);
                    rect.y += rect.height;
                } else {
                    rect = getCellBounds(list, index);
                }
            } else {
                rect = getCellBounds(list, index);
            }

            if (rect.y >= list.getHeight()) {
                rect.y = list.getHeight() - DROP_LINE_THICKNESS;
            }

            rect.height = DROP_LINE_THICKNESS;
        } else {
            if (index == size) {
                index--;
                rect = getCellBounds(list, index);
                rect.y += rect.height;
            } else {
                rect = getCellBounds(list, index);
            }

            if (rect.y >= list.getHeight()) {
                rect.y = list.getHeight() - DROP_LINE_THICKNESS;
            }

            rect.height = DROP_LINE_THICKNESS;
        }

        return rect;
    }

    /**
     * {@inheritDoc}
     */
    @Override // defined in ComponentUI , exact copy from javax.swing.plaf.basic.BasicListUI
    public int getBaseline(JComponent c, int width, int height) {
        super.getBaseline(c, width, height);
        checkBaselinePrecondition(c, width, height);
        int rowHeight = list.getFixedCellHeight();
        UIDefaults lafDefaults = UIManager.getLookAndFeelDefaults();
        Component renderer = (Component)lafDefaults.get(BASELINE_COMPONENT_KEY);
        if (renderer == null) {
            @SuppressWarnings("unchecked")
            ListCellRenderer<Object> lcr = (ListCellRenderer<Object>)UIManager.get("List.cellRenderer");

            // fix for 6711072 some LAFs like Nimbus do not provide this
            // UIManager key and we should not through a NPE here because of it
            if (lcr == null) {
                lcr = new DefaultListCellRenderer();
            }
            renderer = lcr.getListCellRendererComponent(list, "a", -1, false, false);
            lafDefaults.put(BASELINE_COMPONENT_KEY, renderer);
        }
        renderer.setFont(list.getFont());
        // JList actually has much more complex behavior here.
        // If rowHeight != -1 the rowHeight is either the max of all cell
        // heights (layout orientation != VERTICAL), or is variable depending
        // upon the cell.  We assume a default size.
        // We could theoretically query the real renderer, but that would
        // not work for an empty model and the results may vary with
        // the content.
        if (rowHeight == -1) {
            rowHeight = renderer.getPreferredSize().height;
        }
        return renderer.getBaseline(Integer.MAX_VALUE, rowHeight) + list.getInsets().top;
    }

    /**
     * Fix for Issue #1495: NPE on getBaseline.
     * 
     * As per contract, that methods needs to throw Exceptions on illegal
     * parameters. As we by-pass super, need to do the check and throw
     * ouerselves.
     * 
     * @param c JComponent
     * @param width expected &ge; 0
     * @param height expected &ge; 0
     * 
     * @throws IllegalArgumentException if width or height &lt; 0
     * @throws NullPointerException if c == null
     */
    protected void checkBaselinePrecondition(JComponent c, int width, int height) {
        if (c == null) {
            throw new NullPointerException("Component must be non-null");
        }
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Width and height must be >= 0");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override // defined in ComponentUI , exact copy from javax.swing.plaf.basic.BasicListUI
    public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent c) {
        super.getBaselineResizeBehavior(c);
        return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
    }

    /**
     * {@inheritDoc} <p>
     * The preferredSize of the list depends upon the layout orientation. 
     * Threre are 3 layouts {@code VERTICAL}, {@code HORIZONTAL_WRAP}, and {@code VERTICAL_WRAP}
     * 
     * @see javax.swing.JList#setLayoutOrientation(int)
     */
    @Override // defined in ComponentUI , copied from javax.swing.plaf.basic.BasicListUI
    public Dimension getPreferredSize(JComponent c) {
        maybeUpdateLayoutState();

        int lastRow = getElementCount() - 1;
        if (lastRow < 0) {
            return new Dimension(0, 0);
        }

        Insets insets = list.getInsets();
        int width = cellWidth * columnCount + insets.left + insets.right;
        int height;

        if (layoutOrientation != JList.VERTICAL) {
            height = preferredHeight;
        }
        else {
            Rectangle bounds = getCellBounds(list, lastRow);

            if (bounds != null) {
                height = bounds.y + bounds.height + insets.bottom;
            }
            else {
                height = 0;
            }
        }
        return new Dimension(width, height);
    }

    /**
     * Select the previous row and force it to be visible.
     *
     * @see javax.swing.JList#ensureIndexIsVisible
     */
    // exact copy from javax.swing.plaf.basic.BasicListUI
    protected void selectPreviousIndex() {
        int s = list.getSelectedIndex();
        if(s > 0) {
            s -= 1;
            list.setSelectedIndex(s);
            list.ensureIndexIsVisible(s);
        }
    }
    
    /**
     * Select the next row and force it to be visible.
     *
     * @see javax.swing.JList#ensureIndexIsVisible
     */
    // copy from javax.swing.plaf.basic.BasicListUI
    protected void selectNextIndex() {
        int s = list.getSelectedIndex();
        if((s + 1) < getElementCount()) {
            s += 1;
            list.setSelectedIndex(s);
            list.ensureIndexIsVisible(s);
        }
    }

    /**
     * this method is called at installUI() time.
     * 
     * @see javax.swing.plaf.basic.BasicListUI#installKeyboardActions
     */
    @Override // exact copy from javax.swing.plaf.basic.BasicListUI
    protected void installKeyboardActions() {
        InputMap inputMap = getInputMap(JComponent.WHEN_FOCUSED);
        SwingUtilities.replaceUIInputMap(list, JComponent.WHEN_FOCUSED, inputMap);
        LazyActionMap.installLazyActionMap(list, BasicYListUI.class, "YList.actionMap");
    }

    // copy from javax.swing.plaf.basic.BasicListUI with some changes
    InputMap getInputMap(int condition) {
        if (condition == JComponent.WHEN_FOCUSED) {
            InputMap keyMap = (InputMap) UIManager.get("List.focusInputMap");
            InputMap rtlKeyMap;
            if (isLeftToRight || ((rtlKeyMap = (InputMap) UIManager.get("List.focusInputMap.RightToLeft")) == null)) {
                return keyMap;
            } else {
                rtlKeyMap.setParent(keyMap);
                return rtlKeyMap;
            }
        }
        return null;
    }

    // exact copy from javax.swing.plaf.basic.BasicListUI
    protected void uninstallKeyboardActions() {
        SwingUtilities.replaceUIActionMap(list, null);
        SwingUtilities.replaceUIInputMap(list, JComponent.WHEN_FOCUSED, null);
    }

    /**
     * this method is called at installUI() time.
     * 
     * @see javax.swing.plaf.basic.BasicListUI#installListeners
     */
    @Override // exact copy from javax.swing.plaf.basic.BasicListUI
    protected void installListeners() {
        TransferHandler th = list.getTransferHandler();
        if (th == null || th instanceof UIResource) {
            list.setTransferHandler(defaultTransferHandler);
            // default TransferHandler doesn't support drop
            // so we don't want drop handling
            if (list.getDropTarget() instanceof UIResource) {
                list.setDropTarget(null);
            }
        }

        focusListener = createFocusListener();
        mouseInputListener = createMouseInputListener();
        propertyChangeListener = createPropertyChangeListener();
        listSelectionListener = createListSelectionListener();
        listDataListener = createListDataListener();

        list.addFocusListener(focusListener);
        list.addMouseListener(mouseInputListener);
        list.addMouseMotionListener(mouseInputListener);
        list.addPropertyChangeListener(propertyChangeListener);
        list.addKeyListener(getHandler());

        ListModel<Object> model = list.getModel();
        if (model != null) {
            model.addListDataListener(listDataListener);
        }

        ListSelectionModel selectionModel = list.getSelectionModel();
        if (selectionModel != null) {
            selectionModel.addListSelectionListener(listSelectionListener);
        }
    }

    // exact copy from javax.swing.plaf.basic.BasicListUI
    protected void uninstallListeners() {
        list.removeFocusListener(focusListener);
        list.removeMouseListener(mouseInputListener);
        list.removeMouseMotionListener(mouseInputListener);
        list.removePropertyChangeListener(propertyChangeListener);
        list.removeKeyListener(getHandler());

        ListModel<Object> model = list.getModel();
        if (model != null) {
            model.removeListDataListener(listDataListener);
        }

        ListSelectionModel selectionModel = list.getSelectionModel();
        if (selectionModel != null) {
            selectionModel.removeListSelectionListener(listSelectionListener);
        }

        focusListener = null;
        mouseInputListener  = null;
        listSelectionListener = null;
        listDataListener = null;
        propertyChangeListener = null;
        handler = null;
    }

    /**
     * this method is called at installUI() time.
     * 
     * @see javax.swing.plaf.basic.BasicListUI#installDefaults
     */
    @Override // exact copy from javax.swing.plaf.basic.BasicListUI
    protected void installDefaults() {
        list.setLayout(null);

        LookAndFeel.installBorder(list, "List.border");

        LookAndFeel.installColorsAndFont(list, "List.background", "List.foreground", "List.font");

        LookAndFeel.installProperty(list, "opaque", Boolean.TRUE);

        if (list.getCellRenderer() == null) {
            @SuppressWarnings("unchecked")
            ListCellRenderer<Object> tmp = (ListCellRenderer<Object>)(UIManager.get("List.cellRenderer"));
            list.setCellRenderer(tmp);
        }

        Color sbg = list.getSelectionBackground();
        if (sbg == null || sbg instanceof UIResource) {
            list.setSelectionBackground(UIManager.getColor("List.selectionBackground"));
        }

        Color sfg = list.getSelectionForeground();
        if (sfg == null || sfg instanceof UIResource) {
            list.setSelectionForeground(UIManager.getColor("List.selectionForeground"));
        }

        Long l = (Long)UIManager.get("List.timeFactor");
        timeFactor = (l!=null) ? l.longValue() : 1000L;

        updateIsFileList();
    }

    // copied from javax.swing.plaf.basic.BasicListUI
    protected void updateIsFileList() {
        boolean b = Boolean.TRUE.equals(list.getClientProperty("List.isFileList"));
        if (b != isFileList) {
            isFileList = b;
            Font oldFont = list.getFont();
            if (oldFont == null || oldFont instanceof UIResource) {
                Font newFont = UIManager.getFont(b ? "FileChooser.listFont" : "List.font");
                if (newFont != null && newFont != oldFont) {
                    list.setFont(newFont);
                }
            }
        }
    }

    // copied from javax.swing.plaf.basic.BasicListUI
    protected void uninstallDefaults() {
        LookAndFeel.uninstallBorder(list);
        if (list.getFont() instanceof UIResource) {
            list.setFont(null);
        }
        if (list.getForeground() instanceof UIResource) {
            list.setForeground(null);
        }
        if (list.getBackground() instanceof UIResource) {
            list.setBackground(null);
        }
        if (list.getSelectionBackground() instanceof UIResource) {
            list.setSelectionBackground(null);
        }
        if (list.getSelectionForeground() instanceof UIResource) {
            list.setSelectionForeground(null);
        }
        if (list.getCellRenderer() instanceof UIResource) {
            list.setCellRenderer(null);
        }
        if (list.getTransferHandler() instanceof UIResource) {
            list.setTransferHandler(null);
        }
    }

    /**
     * {@inheritDoc} <p>
     * Initializes <code>super.list</code> with <code>JComponent c</code> by calling
     * protected void installDefaults()
     * protected void installListeners()
     * protected void installKeyboardActions()
     */
    @Override // call it from super
    public void installUI(JComponent c) {
//    	LOG.info("---------->JComponent:"+c);
    	super.installUI(c);
    	assert super.list == c;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override // defined in ComponentUI , exact copy from javax.swing.plaf.basic.BasicListUI
    public void uninstallUI(JComponent c) {
        uninstallListeners();
        uninstallDefaults();
        uninstallKeyboardActions();

        cellWidth = cellHeight = -1;
        cellHeights = null;

        listWidth = listHeight = -1;

        list.remove(rendererPane);
        rendererPane = null;
        list = null;
    }

    /**
     * {@inheritDoc} <p>
     * with LOG
     */
    @Override
	protected Rectangle getCellBounds(JList<?> list, int index) {
    	LOG.fine("super.getCellBounds(superJList<?> list, int index="+index);
        return super.getCellBounds(list, index);
    }

    /**
     * Returns the closest row that starts at the specified y-location
     * in the passed in column.
     */
    // exact copy from javax.swing.plaf.basic.BasicListUI
    private int convertLocationToRowInColumn(int y, int column) {
        int x = 0;

        if (layoutOrientation != JList.VERTICAL) {
            if (isLeftToRight) {
                x = column * cellWidth;
            } else {
                x = list.getWidth() - (column+1)*cellWidth - list.getInsets().right;
            }
        }
        return convertLocationToRow(x, y, true);
    }
    
    // exact copy from javax.swing.plaf.basic.BasicListUI
    protected Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    /**
     * Mouse input, and focus handling for JList.  An instance of this
     * class is added to the appropriate java.awt.Component lists
     * at installUI() time.  Note keyboard input is handled with JComponent
     * KeyboardActions, see installKeyboardActions().
     *
     * @see #createMouseInputListener
     * @see #installKeyboardActions
     * @see #installUI
     */
    // inner class copied from javax.swing.plaf.basic.BasicListUI
    public class MouseInputHandler implements MouseInputListener {
        /**
         * Constructs a {@code MouseInputHandler}.
         */
        public MouseInputHandler() {}

        public void mouseClicked(MouseEvent e) {
            getHandler().mouseClicked(e);
        }

        public void mouseEntered(MouseEvent e) {
            getHandler().mouseEntered(e);
        }

        public void mouseExited(MouseEvent e) {
            getHandler().mouseExited(e);
        }

        public void mousePressed(MouseEvent e) {
            getHandler().mousePressed(e);
        }

        public void mouseDragged(MouseEvent e) {
            getHandler().mouseDragged(e);
        }

        public void mouseMoved(MouseEvent e) {
            getHandler().mouseMoved(e);
        }

        public void mouseReleased(MouseEvent e) {
            getHandler().mouseReleased(e);
        }
    }

    /**
     * Creates a delegate that implements {@code MouseInputListener}.
     * The delegate is added to the corresponding {@code java.awt.Component} listener
     * lists at {@code installUI()} time. Subclasses can override this method to return
     * a custom {@code MouseInputListener}, e.g.
     * <pre>
     * class MyListUI extends BasicListUI {
     *    protected MouseInputListener <b>createMouseInputListener</b>() {
     *        return new MyMouseInputHandler();
     *    }
     *    public class MyMouseInputHandler extends MouseInputHandler {
     *        public void mouseMoved(MouseEvent e) {
     *            // do some extra work when the mouse moves
     *            super.mouseMoved(e);
     *        }
     *    }
     * }
     * </pre>
     *
     * @return an instance of {@code MouseInputListener}
     * @see MouseInputHandler
     * @see #installUI
     */
    // exact copy from javax.swing.plaf.basic.BasicListUI
    protected MouseInputListener createMouseInputListener() {
        return getHandler();
    }

    /**
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses.
     */
    // inner class copied from javax.swing.plaf.basic.BasicListUI
    public class FocusHandler implements FocusListener {
        /**
         * Constructs a {@code FocusHandler}.
         */
        public FocusHandler() {}

        /**
         * Repaints focused cells.
         */
        protected void repaintCellFocus()
        {
            getHandler().repaintCellFocus();
        }

        /* The focusGained() focusLost() methods run when the JList
         * focus changes.
         */

        public void focusGained(FocusEvent e) {
            getHandler().focusGained(e);
        }

        public void focusLost(FocusEvent e) {
            getHandler().focusLost(e);
        }
    }

    // exact copy from javax.swing.plaf.basic.BasicListUI
    protected FocusListener createFocusListener() {
        return getHandler();
    }

    /**
     * The ListSelectionListener that's added to the JLists selection
     * model at installUI time, and whenever the JList.selectionModel property
     * changes.  When the selection changes we repaint the affected rows.
     *
     * @see #createListSelectionListener
     * @see #getCellBounds
     * @see #installUI
     */
    // inner class copied from javax.swing.plaf.basic.BasicListUI
    public class ListSelectionHandler implements ListSelectionListener {
        /**
         * Constructs a {@code ListSelectionHandler}.
         */
        public ListSelectionHandler() {}

        public void valueChanged(ListSelectionEvent e) {
            getHandler().valueChanged(e);
        }
    }

    /**
     * Creates an instance of {@code ListSelectionHandler} that's added to
     * the {@code JLists} by selectionModel as needed.  Subclasses can override
     * this method to return a custom {@code ListSelectionListener}, e.g.
     * <pre>
     * class MyListUI extends BasicListUI {
     *    protected ListSelectionListener <b>createListSelectionListener</b>() {
     *        return new MySelectionListener();
     *    }
     *    public class MySelectionListener extends ListSelectionHandler {
     *        public void valueChanged(ListSelectionEvent e) {
     *            // do some extra work when the selection changes
     *            super.valueChange(e);
     *        }
     *    }
     * }
     * </pre>
     *
     * @return an instance of {@code ListSelectionHandler}
     * @see ListSelectionHandler
     * @see #installUI
     */
    // exact copy from javax.swing.plaf.basic.BasicListUI
    protected ListSelectionListener createListSelectionListener() {
        return getHandler();
    }

    protected void redrawList() {
        list.revalidate();
        list.repaint();
    }

    /**
     * The {@code ListDataListener} that's added to the {@code JLists} model at
     * {@code installUI time}, and whenever the JList.model property changes.
     *
     * @see JList#getModel
     * @see #maybeUpdateLayoutState
     * @see #createListDataListener
     * @see #installUI
     */
    // inner class copied javax.swing.plaf.basic.BasicListUI
    public class ListDataHandler implements ListDataListener {
        /**
         * Constructs a {@code ListDataHandler}.
         */
        public ListDataHandler() {}

        public void intervalAdded(ListDataEvent e) {
            getHandler().intervalAdded(e);
        }


        public void intervalRemoved(ListDataEvent e)
        {
            getHandler().intervalRemoved(e);
        }


        public void contentsChanged(ListDataEvent e) {
            getHandler().contentsChanged(e);
        }
    }

    /**
     * Creates an instance of {@code ListDataListener} that's added to
     * the {@code JLists} by model as needed. Subclasses can override
     * this method to return a custom {@code ListDataListener}, e.g.
     * <pre>
     * class MyListUI extends BasicListUI {
     *    protected ListDataListener <b>createListDataListener</b>() {
     *        return new MyListDataListener();
     *    }
     *    public class MyListDataListener extends ListDataHandler {
     *        public void contentsChanged(ListDataEvent e) {
     *            // do some extra work when the models contents change
     *            super.contentsChange(e);
     *        }
     *    }
     * }
     * </pre>
     *
     * @return an instance of {@code ListDataListener}
     * @see ListDataListener
     * @see JList#getModel
     * @see #installUI
     */
    // exact copy from javax.swing.plaf.basic.BasicListUI
    protected ListDataListener createListDataListener() {
        return getHandler();
    }

    /**
     * The PropertyChangeListener that's added to the JList at
     * installUI time.  When the value of a JList property that
     * affects layout changes, we set a bit in updateLayoutStateNeeded.
     * If the JLists model changes we additionally remove our listeners
     * from the old model.  Likewise for the JList selectionModel.
     *
     * @see #maybeUpdateLayoutState
     * @see #createPropertyChangeListener
     * @see #installUI
     */
    // inner class copied from javax.swing.plaf.basic.BasicListUI
    public class PropertyChangeHandler implements PropertyChangeListener {
        /**
         * Constructs a {@code PropertyChangeHandler}.
         */
        public PropertyChangeHandler() {}

        public void propertyChange(PropertyChangeEvent e) {
            getHandler().propertyChange(e);
        }
    }

    /**
     * Creates an instance of {@code PropertyChangeHandler} that's added to
     * the {@code JList} by {@code installUI()}. Subclasses can override this method
     * to return a custom {@code PropertyChangeListener}, e.g.
     * <pre>
     * class MyListUI extends BasicListUI {
     *    protected PropertyChangeListener <b>createPropertyChangeListener</b>() {
     *        return new MyPropertyChangeListener();
     *    }
     *    public class MyPropertyChangeListener extends PropertyChangeHandler {
     *        public void propertyChange(PropertyChangeEvent e) {
     *            if (e.getPropertyName().equals("model")) {
     *                // do some extra work when the model changes
     *            }
     *            super.propertyChange(e);
     *        }
     *    }
     * }
     * </pre>
     *
     * @return an instance of {@code PropertyChangeHandler}
     * @see PropertyChangeListener
     * @see #installUI
     */
    // exact copy from javax.swing.plaf.basic.BasicListUI
    protected PropertyChangeListener createPropertyChangeListener() {
        return getHandler();
    }

    // copied from javax.swing.plaf.basic.BasicListUI
    private static final int CHANGE_LEAD = 0;
    private static final int CHANGE_SELECTION = 1;
    private static final int EXTEND_SELECTION = 2;
    
    // inner class copied from javax.swing.plaf.basic.BasicListUI with small modifications
    protected static class Actions extends org.jdesktop.swingx.plaf.UIAction {
        protected int getElementCount(JList<?> list) {
        	return list.getModel().getSize();
        }
        protected static final String SELECT_PREVIOUS_COLUMN =
                                    "selectPreviousColumn";
        protected static final String SELECT_PREVIOUS_COLUMN_EXTEND =
                                    "selectPreviousColumnExtendSelection";
        protected static final String SELECT_PREVIOUS_COLUMN_CHANGE_LEAD =
                                    "selectPreviousColumnChangeLead";
        protected static final String SELECT_NEXT_COLUMN = "selectNextColumn";
        protected static final String SELECT_NEXT_COLUMN_EXTEND =
                                    "selectNextColumnExtendSelection";
        protected static final String SELECT_NEXT_COLUMN_CHANGE_LEAD =
                                    "selectNextColumnChangeLead";
        protected static final String SELECT_PREVIOUS_ROW = "selectPreviousRow";
        protected static final String SELECT_PREVIOUS_ROW_EXTEND =
                                     "selectPreviousRowExtendSelection";
        protected static final String SELECT_PREVIOUS_ROW_CHANGE_LEAD =
                                     "selectPreviousRowChangeLead";
        protected static final String SELECT_NEXT_ROW = "selectNextRow";
        protected static final String SELECT_NEXT_ROW_EXTEND =
                                     "selectNextRowExtendSelection";
        protected static final String SELECT_NEXT_ROW_CHANGE_LEAD =
                                     "selectNextRowChangeLead";
        protected static final String SELECT_FIRST_ROW = "selectFirstRow";
        protected static final String SELECT_FIRST_ROW_EXTEND =
                                     "selectFirstRowExtendSelection";
        protected static final String SELECT_FIRST_ROW_CHANGE_LEAD =
                                     "selectFirstRowChangeLead";
        protected static final String SELECT_LAST_ROW = "selectLastRow";
        protected static final String SELECT_LAST_ROW_EXTEND =
                                     "selectLastRowExtendSelection";
        protected static final String SELECT_LAST_ROW_CHANGE_LEAD =
                                     "selectLastRowChangeLead";
        protected static final String SCROLL_UP = "scrollUp";
        protected static final String SCROLL_UP_EXTEND =
                                     "scrollUpExtendSelection";
        protected static final String SCROLL_UP_CHANGE_LEAD =
                                     "scrollUpChangeLead";
        protected static final String SCROLL_DOWN = "scrollDown";
        protected static final String SCROLL_DOWN_EXTEND =
                                     "scrollDownExtendSelection";
        protected static final String SCROLL_DOWN_CHANGE_LEAD =
                                     "scrollDownChangeLead";
        protected static final String SELECT_ALL = "selectAll";
        protected static final String CLEAR_SELECTION = "clearSelection";

        // add the lead item to the selection without changing lead or anchor
        protected static final String ADD_TO_SELECTION = "addToSelection";

        // toggle the selected state of the lead item and move the anchor to it
        protected static final String TOGGLE_AND_ANCHOR = "toggleAndAnchor";

        // extend the selection to the lead item
        protected static final String EXTEND_TO = "extendTo";

        // move the anchor to the lead and ensure only that item is selected
        protected static final String MOVE_SELECTION_TO = "moveSelectionTo";

        protected Actions(String name) {
            super(name);
        }
        public void actionPerformed(ActionEvent e) {
            String name = getName();
            @SuppressWarnings("unchecked")
            JList<Object> list = (JList<Object>)e.getSource();
            BasicYListUI ui = (BasicYListUI)LookAndFeelUtils.getUIOfType(list.getUI(), BasicYListUI.class);

            if (name == SELECT_PREVIOUS_COLUMN) {
                changeSelection(list, CHANGE_SELECTION,
                                getNextColumnIndex(list, ui, -1), -1);
            }
            else if (name == SELECT_PREVIOUS_COLUMN_EXTEND) {
                changeSelection(list, EXTEND_SELECTION,
                                getNextColumnIndex(list, ui, -1), -1);
            }
            else if (name == SELECT_PREVIOUS_COLUMN_CHANGE_LEAD) {
                changeSelection(list, CHANGE_LEAD,
                                getNextColumnIndex(list, ui, -1), -1);
            }
            else if (name == SELECT_NEXT_COLUMN) {
                changeSelection(list, CHANGE_SELECTION,
                                getNextColumnIndex(list, ui, 1), 1);
            }
            else if (name == SELECT_NEXT_COLUMN_EXTEND) {
                changeSelection(list, EXTEND_SELECTION,
                                getNextColumnIndex(list, ui, 1), 1);
            }
            else if (name == SELECT_NEXT_COLUMN_CHANGE_LEAD) {
                changeSelection(list, CHANGE_LEAD,
                                getNextColumnIndex(list, ui, 1), 1);
            }
            else if (name == SELECT_PREVIOUS_ROW) {
                changeSelection(list, CHANGE_SELECTION,
                                getNextIndex(list, ui, -1), -1);
            }
            else if (name == SELECT_PREVIOUS_ROW_EXTEND) {
                changeSelection(list, EXTEND_SELECTION,
                                getNextIndex(list, ui, -1), -1);
            }
            else if (name == SELECT_PREVIOUS_ROW_CHANGE_LEAD) {
                changeSelection(list, CHANGE_LEAD,
                                getNextIndex(list, ui, -1), -1);
            }
            else if (name == SELECT_NEXT_ROW) {
                changeSelection(list, CHANGE_SELECTION,
                                getNextIndex(list, ui, 1), 1);
            }
            else if (name == SELECT_NEXT_ROW_EXTEND) {
                changeSelection(list, EXTEND_SELECTION,
                                getNextIndex(list, ui, 1), 1);
            }
            else if (name == SELECT_NEXT_ROW_CHANGE_LEAD) {
                changeSelection(list, CHANGE_LEAD,
                                getNextIndex(list, ui, 1), 1);
            }
            else if (name == SELECT_FIRST_ROW) {
                changeSelection(list, CHANGE_SELECTION, 0, -1);
            }
            else if (name == SELECT_FIRST_ROW_EXTEND) {
                changeSelection(list, EXTEND_SELECTION, 0, -1);
            }
            else if (name == SELECT_FIRST_ROW_CHANGE_LEAD) {
                changeSelection(list, CHANGE_LEAD, 0, -1);
            }
            else if (name == SELECT_LAST_ROW) {
                changeSelection(list, CHANGE_SELECTION, getElementCount(list) - 1, 1);
            }
            else if (name == SELECT_LAST_ROW_EXTEND) { 
            	changeSelection(list, EXTEND_SELECTION, getElementCount(list) - 1, 1);
            }
            else if (name == SELECT_LAST_ROW_CHANGE_LEAD) {
                changeSelection(list, CHANGE_LEAD, getElementCount(list) - 1, 1);
            }
            else if (name == SCROLL_UP) {
                changeSelection(list, CHANGE_SELECTION,
                                getNextPageIndex(list, -1), -1);
            }
            else if (name == SCROLL_UP_EXTEND) {
                changeSelection(list, EXTEND_SELECTION,
                                getNextPageIndex(list, -1), -1);
            }
            else if (name == SCROLL_UP_CHANGE_LEAD) {
                changeSelection(list, CHANGE_LEAD,
                                getNextPageIndex(list, -1), -1);
            }
            else if (name == SCROLL_DOWN) {
                changeSelection(list, CHANGE_SELECTION,
                                getNextPageIndex(list, 1), 1);
            }
            else if (name == SCROLL_DOWN_EXTEND) {
                changeSelection(list, EXTEND_SELECTION,
                                getNextPageIndex(list, 1), 1);
            }
            else if (name == SCROLL_DOWN_CHANGE_LEAD) {
                changeSelection(list, CHANGE_LEAD,
                                getNextPageIndex(list, 1), 1);
            }
            else if (name == SELECT_ALL) {
                selectAll(list);
            }
            else if (name == CLEAR_SELECTION) {
                clearSelection(list);
            }
            else if (name == ADD_TO_SELECTION) {
                int index = adjustIndex(
                    list.getSelectionModel().getLeadSelectionIndex(), list);

                if (!list.isSelectedIndex(index)) {
                    int oldAnchor = list.getSelectionModel().getAnchorSelectionIndex();
                    list.setValueIsAdjusting(true);
                    list.addSelectionInterval(index, index);
                    list.getSelectionModel().setAnchorSelectionIndex(oldAnchor);
                    list.setValueIsAdjusting(false);
                }
            }
            else if (name == TOGGLE_AND_ANCHOR) {
                int index = adjustIndex(
                    list.getSelectionModel().getLeadSelectionIndex(), list);

                if (list.isSelectedIndex(index)) {
                    list.removeSelectionInterval(index, index);
                } else {
                    list.addSelectionInterval(index, index);
                }
            }
            else if (name == EXTEND_TO) {
                changeSelection(
                    list, EXTEND_SELECTION,
                    adjustIndex(list.getSelectionModel().getLeadSelectionIndex(), list),
                    0);
            }
            else if (name == MOVE_SELECTION_TO) {
                changeSelection(
                    list, CHANGE_SELECTION,
                    adjustIndex(list.getSelectionModel().getLeadSelectionIndex(), list),
                    0);
            }
        }

        @Override
        public boolean accept(Object c) {
            Object name = getName();
            if (name == SELECT_PREVIOUS_COLUMN_CHANGE_LEAD ||
                    name == SELECT_NEXT_COLUMN_CHANGE_LEAD ||
                    name == SELECT_PREVIOUS_ROW_CHANGE_LEAD ||
                    name == SELECT_NEXT_ROW_CHANGE_LEAD ||
                    name == SELECT_FIRST_ROW_CHANGE_LEAD ||
                    name == SELECT_LAST_ROW_CHANGE_LEAD ||
                    name == SCROLL_UP_CHANGE_LEAD ||
                    name == SCROLL_DOWN_CHANGE_LEAD) {

                // discontinuous selection actions are only enabled for
                // DefaultListSelectionModel
                return c != null && ((JList<?>)c).getSelectionModel()
                                        instanceof DefaultListSelectionModel;
            }

            return true;
        }

        protected void clearSelection(JList<?> list) {
            list.clearSelection();
        }

        protected void selectAll(JList<?> list) {
            int size = getElementCount(list);
            if (size > 0) {
                ListSelectionModel lsm = list.getSelectionModel();
                int lead = adjustIndex(lsm.getLeadSelectionIndex(), list);

                if (lsm.getSelectionMode() == ListSelectionModel.SINGLE_SELECTION) {
                    if (lead == -1) {
                        int min = adjustIndex(list.getMinSelectionIndex(), list);
                        lead = (min == -1 ? 0 : min);
                    }

                    list.setSelectionInterval(lead, lead);
                    list.ensureIndexIsVisible(lead);
                } else {
                    list.setValueIsAdjusting(true);

                    int anchor = adjustIndex(lsm.getAnchorSelectionIndex(), list);

                    list.setSelectionInterval(0, size - 1);

                    // this is done to restore the anchor and lead
                    SwingXUtilities.setLeadAnchorWithoutSelection(lsm, anchor, lead);

                    list.setValueIsAdjusting(false);
                }
            }
        }

        protected int getNextPageIndex(JList<?> list, int direction) {
            if (getElementCount(list) == 0) {
                return -1;
            }

            int index = -1;
            Rectangle visRect = list.getVisibleRect();
            ListSelectionModel lsm = list.getSelectionModel();
            int lead = adjustIndex(lsm.getLeadSelectionIndex(), list);
            Rectangle leadRect =
                (lead==-1) ? new Rectangle() : list.getCellBounds(lead, lead);

            if (leadRect == null) {
                return index;
            }
            if (list.getLayoutOrientation() == JList.VERTICAL_WRAP &&
                    list.getVisibleRowCount() <= 0) {
                if (!list.getComponentOrientation().isLeftToRight()) {
                    direction = -direction;
                }
                // apply for horizontal scrolling: the step for next
                // page index is number of visible columns
                if (direction < 0) {
                    // left
                    visRect.x = leadRect.x + leadRect.width - visRect.width;
                    Point p = new Point(visRect.x - 1, leadRect.y);
                    index = list.locationToIndex(p);
                    if (index == -1) {
                        return index;
                    }
                    Rectangle cellBounds = list.getCellBounds(index, index);
                    if (cellBounds != null && visRect.intersects(cellBounds)) {
                        p.x = cellBounds.x - 1;
                        index = list.locationToIndex(p);
                        if (index == -1) {
                            return index;
                        }
                        cellBounds = list.getCellBounds(index, index);
                    }
                    // this is necessary for right-to-left orientation only
                    if (cellBounds != null && cellBounds.y != leadRect.y) {
                        p.x = cellBounds.x + cellBounds.width;
                        index = list.locationToIndex(p);
                    }
                }
                else {
                    // right
                    visRect.x = leadRect.x;
                    Point p = new Point(visRect.x + visRect.width, leadRect.y);
                    index = list.locationToIndex(p);
                    if (index == -1) {
                        return index;
                    }
                    Rectangle cellBounds = list.getCellBounds(index, index);
                    if (cellBounds != null && visRect.intersects(cellBounds)) {
                        p.x = cellBounds.x + cellBounds.width;
                        index = list.locationToIndex(p);
                        if (index == -1) {
                            return index;
                        }
                        cellBounds = list.getCellBounds(index, index);
                    }
                    if (cellBounds != null && cellBounds.y != leadRect.y) {
                        p.x = cellBounds.x - 1;
                        index = list.locationToIndex(p);
                    }
                }
            }
            else {
                if (direction < 0) {
                    // up
                    // go to the first visible cell
                    Point p = new Point(leadRect.x, visRect.y);
                    index = list.locationToIndex(p);
                    if (lead <= index) {
                        // if lead is the first visible cell (or above it)
                        // adjust the visible rect up
                        visRect.y = leadRect.y + leadRect.height - visRect.height;
                        p.y = visRect.y;
                        index = list.locationToIndex(p);
                        if (index == -1) {
                            return index;
                        }
                        Rectangle cellBounds = list.getCellBounds(index, index);
                        // go one cell down if first visible cell doesn't fit
                        // into adjasted visible rectangle
                        if (cellBounds != null && cellBounds.y < visRect.y) {
                            p.y = cellBounds.y + cellBounds.height;
                            index = list.locationToIndex(p);
                            if (index == -1) {
                                return index;
                            }
                            cellBounds = list.getCellBounds(index, index);
                        }
                        // if index isn't less then lead
                        // try to go to cell previous to lead
                        if (cellBounds != null && cellBounds.y >= leadRect.y) {
                            p.y = leadRect.y - 1;
                            index = list.locationToIndex(p);
                        }
                    }
                }
                else {
                    // down
                    // go to the last completely visible cell
                    Point p = new Point(leadRect.x,
                            visRect.y + visRect.height - 1);
                    index = list.locationToIndex(p);
                    if (index == -1) {
                        return index;
                    }
                    Rectangle cellBounds = list.getCellBounds(index, index);
                    // go up one cell if last visible cell doesn't fit
                    // into visible rectangle
                    if (cellBounds != null &&
                            cellBounds.y + cellBounds.height >
                            visRect.y + visRect.height)
                    {
                        p.y = cellBounds.y - 1;
                        index = list.locationToIndex(p);
                        if (index == -1) {
                            return index;
                        }
                        cellBounds = list.getCellBounds(index, index);
                        index = Math.max(index, lead);
                    }

                    if (lead >= index) {
                        // if lead is the last completely visible index
                        // (or below it) adjust the visible rect down
                        visRect.y = leadRect.y;
                        p.y = visRect.y + visRect.height - 1;
                        index = list.locationToIndex(p);
                        if (index == -1) {
                            return index;
                        }
                        cellBounds = list.getCellBounds(index, index);
                        // go one cell up if last visible cell doesn't fit
                        // into adjasted visible rectangle
                        if (cellBounds != null &&
                                cellBounds.y + cellBounds.height >
                                visRect.y + visRect.height)
                        {
                            p.y = cellBounds.y - 1;
                            index = list.locationToIndex(p);
                            if (index == -1) {
                                return index;
                            }
                            cellBounds = list.getCellBounds(index, index);
                        }
                        // if index isn't greater then lead
                        // try to go to cell next after lead
                        if (cellBounds != null && cellBounds.y <= leadRect.y) {
                            p.y = leadRect.y + leadRect.height;
                            index = list.locationToIndex(p);
                        }
                    }
                }
            }

            return index;
        }

        protected void changeSelection(JList<?> list, int type,
                                     int index, int direction) {
            if (index >= 0 && index < getElementCount(list)) {
                ListSelectionModel lsm = list.getSelectionModel();

                // CHANGE_LEAD is only valid with multiple interval selection
                if (type == CHANGE_LEAD &&
                        list.getSelectionMode()
                            != ListSelectionModel.MULTIPLE_INTERVAL_SELECTION) {

                    type = CHANGE_SELECTION;
                }

                // IMPORTANT - This needs to happen before the index is changed.
                // This is because JFileChooser, which uses JList, also scrolls
                // the selected item into view. If that happens first, then
                // this method becomes a no-op.
                adjustScrollPositionIfNecessary(list, index, direction);

                if (type == EXTEND_SELECTION) {
                    int anchor = adjustIndex(lsm.getAnchorSelectionIndex(), list);
                    if (anchor == -1) {
                        anchor = 0;
                    }

                    list.setSelectionInterval(anchor, index);
                }
                else if (type == CHANGE_SELECTION) {
                    list.setSelectedIndex(index);
                }
                else {
                    // casting should be safe since the action is only enabled
                    // for DefaultListSelectionModel
                    if (lsm instanceof DefaultListSelectionModel dlsm)
                    	dlsm.moveLeadSelectionIndex(index);
                }
            }
        }

        /**
         * When scroll down makes selected index the last completely visible
         * index. When scroll up makes selected index the first visible index.
         * Adjust visible rectangle respect to list's component orientation.
         */
        protected void adjustScrollPositionIfNecessary(JList<?> list, int index,
                                                     int direction) {
            if (direction == 0) {
                return;
            }
            Rectangle cellBounds = list.getCellBounds(index, index);
            Rectangle visRect = list.getVisibleRect();
            if (cellBounds != null && !visRect.contains(cellBounds)) {
                if (list.getLayoutOrientation() == JList.VERTICAL_WRAP &&
                    list.getVisibleRowCount() <= 0) {
                    // horizontal
                    if (list.getComponentOrientation().isLeftToRight()) {
                        if (direction > 0) {
                            // right for left-to-right
                            int x =Math.max(0,
                                cellBounds.x + cellBounds.width - visRect.width);
                            int startIndex =
                                list.locationToIndex(new Point(x, cellBounds.y));
                            if (startIndex == -1) {
                                return;
                            }
                            Rectangle startRect = list.getCellBounds(startIndex,
                                                                     startIndex);
                            if (startRect != null &&
                                startRect.x < x && startRect.x < cellBounds.x) {
                                startRect.x += startRect.width;
                                startIndex =
                                    list.locationToIndex(startRect.getLocation());
                                if (startIndex == -1) {
                                    return;
                                }
                                startRect = list.getCellBounds(startIndex,
                                                               startIndex);
                            }
                            cellBounds = startRect;
                        }
                        if (cellBounds != null) {
                            cellBounds.width = visRect.width;
                        }
                    }
                    else {
                        if (direction > 0) {
                            // left for right-to-left
                            int x = cellBounds.x + visRect.width;
                            int rightIndex =
                                list.locationToIndex(new Point(x, cellBounds.y));
                            if (rightIndex == -1) {
                                return;
                            }
                            Rectangle rightRect = list.getCellBounds(rightIndex,
                                                                     rightIndex);
                            if (rightRect != null) {
                                if (rightRect.x + rightRect.width > x &&
                                        rightRect.x > cellBounds.x) {
                                    rightRect.width = 0;
                                }
                                cellBounds.x = Math.max(0,
                                        rightRect.x + rightRect.width - visRect.width);
                                cellBounds.width = visRect.width;
                            }
                        }
                        else {
                            cellBounds.x += Math.max(0,
                                cellBounds.width - visRect.width);
                            // adjust width to fit into visible rectangle
                            cellBounds.width = Math.min(cellBounds.width,
                                                        visRect.width);
                        }
                    }
                }
                else {
                    // vertical
                    if (direction > 0 &&
                            (cellBounds.y < visRect.y ||
                                    cellBounds.y + cellBounds.height
                                            > visRect.y + visRect.height)) {
                        //down
                        int y = Math.max(0,
                            cellBounds.y + cellBounds.height - visRect.height);
                        int startIndex =
                            list.locationToIndex(new Point(cellBounds.x, y));
                        if (startIndex == -1) {
                            return;
                        }
                        Rectangle startRect = list.getCellBounds(startIndex,
                                                                 startIndex);
                        if (startRect != null &&
                                startRect.y < y && startRect.y < cellBounds.y) {
                            startRect.y += startRect.height;
                            startIndex =
                                list.locationToIndex(startRect.getLocation());
                            if (startIndex == -1) {
                                return;
                            }
                            startRect =
                                list.getCellBounds(startIndex, startIndex);
                        }
                        cellBounds = startRect;
                        if (cellBounds != null) {
                            cellBounds.height = visRect.height;
                        }
                    }
                    else {
                        // adjust height to fit into visible rectangle
                        cellBounds.height = Math.min(cellBounds.height, visRect.height);
                    }
                }
                if (cellBounds != null) {
                    list.scrollRectToVisible(cellBounds);
                }
            }
        }

        private int getNextColumnIndex(JList<?> list, BasicYListUI ui,
                                       int amount) {
            if (list.getLayoutOrientation() != JList.VERTICAL) {
                int index = adjustIndex(list.getLeadSelectionIndex(), list);
                int size = list.getModel().getSize();

                if (index == -1) {
                    return 0;
                } else if (size == 1) {
                    // there's only one item so we should select it
                    return 0;
                } else if (ui == null || ui.columnCount <= 1) {
                    return -1;
                }

                int column = ui.convertModelToColumn(index);
                int row = ui.convertModelToRow(index);

                column += amount;
                if (column >= ui.columnCount || column < 0) {
                    // No wrapping.
                    return -1;
                }
                int maxRowCount = ui.getRowCount(column);
                if (row >= maxRowCount) {
                    return -1;
                }
                return ui.getModelIndex(column, row);
            }
            // Won't change the selection.
            return -1;
        }

        private int getNextIndex(JList<?> list, BasicYListUI ui, int amount) {
            int index = adjustIndex(list.getLeadSelectionIndex(), list);
            int size = getElementCount(list);

            if (index == -1) {
                if (size > 0) {
                    if (amount > 0) {
                        index = 0;
                    }
                    else {
                        index = size - 1;
                    }
                }
            } else if (size == 1) {
                // there's only one item so we should select it
                index = 0;
            } else if (list.getLayoutOrientation() == JList.HORIZONTAL_WRAP) {
                if (ui != null) {
                    index += ui.columnCount * amount;
                }
            } else {
                index += amount;
            }

            return index;
        }
    }

    // inner class copied from private javax.swing.plaf.basic.BasicListUI with small modifications
    protected class Handler 
    implements FocusListener
    		 , KeyListener
    		 , ListDataListener
    		 , ListSelectionListener
    		 , MouseInputListener
    		 , PropertyChangeListener
    		 , BeforeDrag 
    {
        //
        // KeyListener
        //
        private String prefix = "";
        private String typedString = "";
        private long lastTime = 0L;

        protected int getElementCount(JList<?> list) {
        	return list.getModel().getSize();
        }
		/**
		 * Invoked when a key has been typed.
		 *
		 * Moves the keyboard focus to the first element whose prefix matches the
         * sequence of alphanumeric keys pressed by the user with delay less
         * than value of <code>timeFactor</code> property (or 1000 milliseconds
         * if it is not defined). Subsequent same key presses move the keyboard
         * focus to the next object that starts with the same letter until another
         * key is pressed, then it is treated as the prefix with appropriate number
         * of the same letters followed by first typed another letter.
		 */
		public void keyTyped(KeyEvent e) {
			JList<?> src = (JList<?>) e.getSource();
			int elementCount = getElementCount(src);

			if(elementCount == 0 || e.isAltDown() || e.isControlDown() || e.isMetaDown() || isNavigationKey(e)) {
				// Nothing to select
				return;
			}
			boolean startingFromSelection = true;

			char c = e.getKeyChar();

			long time = e.getWhen();
			int startIndex = adjustIndex(src.getLeadSelectionIndex(), list);
			if (time - lastTime < timeFactor) {
				typedString += c;
				if ((prefix.length() == 1) && (c == prefix.charAt(0))) {
					// Subsequent same key presses move the keyboard focus to the next
					// object that starts with the same letter.
					startIndex++;
				} else {
					prefix = typedString;
				}
			} else {
				startIndex++;
				typedString = "" + c;
				prefix = typedString;
			}
			lastTime = time;

			if (startIndex < 0 || startIndex >= elementCount) {
				startingFromSelection = false;
				startIndex = 0;
			}
			int index = src.getNextMatch(prefix, startIndex, Position.Bias.Forward);
			if (index >= 0) {
				src.setSelectedIndex(index);
				src.ensureIndexIsVisible(index);
			} else if (startingFromSelection) { // wrap
				index = src.getNextMatch(prefix, 0, Position.Bias.Forward);
				if (index >= 0) {
					src.setSelectedIndex(index);
					src.ensureIndexIsVisible(index);
				}
			}
		}

		/**
		 * Invoked when a key has been pressed.
		 *
         * Checks to see if the key event is a navigation key to prevent
         * dispatching these keys for the first letter navigation.
		 */
		public void keyPressed(KeyEvent e) {
			if (isNavigationKey(e)) {
				prefix = "";
				typedString = "";
				lastTime = 0L;
			}
		}

		/**
         * Invoked when a key has been released.
         * See the class description for {@link KeyEvent} for a definition of
         * a key released event.
		 */
		public void keyReleased(KeyEvent e) {
		}

		/**
		 * Returns whether or not the supplied key event maps to a key that is used for
		 * navigation. This is used for optimizing key input by only passing non-
		 * navigation keys to the first letter navigation mechanism.
		 */
		private boolean isNavigationKey(KeyEvent event) {
			InputMap inputMap = list.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
			KeyStroke key = KeyStroke.getKeyStrokeForEvent(event);

			if (inputMap != null && inputMap.get(key) != null) {
				return true;
			}
			return false;
		}

        // 
        // PropertyChangeListener
        //
		public void propertyChange(PropertyChangeEvent e) {
			String propertyName = e.getPropertyName();

            /* If the JList.model property changes, remove our listener,
             * listDataListener from the old model and add it to the new one.
			 */
			if (propertyName == "model") {
				ListModel<?> oldModel = (ListModel<?>) e.getOldValue();
				ListModel<?> newModel = (ListModel<?>) e.getNewValue();
				if (oldModel != null) {
					oldModel.removeListDataListener(listDataListener);
				}
				if (newModel != null) {
					newModel.addListDataListener(listDataListener);
				}
				updateLayoutStateNeeded |= modelChanged;
				redrawList();
			}

            /* If the JList.selectionModel property changes, remove our listener,
			 * listSelectionListener from the old selectionModel and add it to the new one.
			 */
			else if (propertyName == "selectionModel") {
				ListSelectionModel oldModel = (ListSelectionModel) e.getOldValue();
				ListSelectionModel newModel = (ListSelectionModel) e.getNewValue();
				if (oldModel != null) {
					oldModel.removeListSelectionListener(listSelectionListener);
				}
				if (newModel != null) {
					newModel.addListSelectionListener(listSelectionListener);
				}
				updateLayoutStateNeeded |= modelChanged;
				redrawList();
			} else if (propertyName == "cellRenderer") {
				updateLayoutStateNeeded |= cellRendererChanged;
				redrawList();
//			} else if (propertyName == "font" || sun.swing.SwingUtilities2.isScaleChanged(e)) {
			} else if (propertyName == "font" ) {
				updateLayoutStateNeeded |= fontChanged;
				redrawList();
			} else if (propertyName == "prototypeCellValue") {
				updateLayoutStateNeeded |= prototypeCellValueChanged;
				redrawList();
			} else if (propertyName == "fixedCellHeight") {
				updateLayoutStateNeeded |= fixedCellHeightChanged;
				redrawList();
			} else if (propertyName == "fixedCellWidth") {
				updateLayoutStateNeeded |= fixedCellWidthChanged;
				redrawList();
            } else if (propertyName == "cellRenderer") {
                updateLayoutStateNeeded |= cellRendererChanged;
                redrawList();
			} else if (propertyName == "selectionForeground") {
				list.repaint();
			} else if (propertyName == "selectionBackground") {
				list.repaint();
			} else if ("layoutOrientation" == propertyName) {
				updateLayoutStateNeeded |= layoutOrientationChanged;
				layoutOrientation = list.getLayoutOrientation();
				redrawList();
			} else if ("visibleRowCount" == propertyName) {
				if (layoutOrientation != JList.VERTICAL) {
					updateLayoutStateNeeded |= layoutOrientationChanged;
					redrawList();
				}
			} else if ("componentOrientation" == propertyName) {
				isLeftToRight = list.getComponentOrientation().isLeftToRight();
				updateLayoutStateNeeded |= componentOrientationChanged;
				redrawList();

				InputMap inputMap = getInputMap(JComponent.WHEN_FOCUSED);
				SwingUtilities.replaceUIInputMap(list, JComponent.WHEN_FOCUSED, inputMap);
			} else if ("List.isFileList" == propertyName) {
				updateIsFileList(); // not visible
				redrawList();
			} else if ("dropLocation" == propertyName) {
				JList.DropLocation oldValue = (JList.DropLocation) e.getOldValue();
				repaintDropLocation(oldValue);
				repaintDropLocation(list.getDropLocation());
			}
		}

		private void repaintDropLocation(JList.DropLocation loc) {
			if (loc == null) {
				return;
			}

			Rectangle r;

			if (loc.isInsert()) {
				r = getDropLineRect(loc);
			} else {
				r = getCellBounds(list, loc.getIndex());
			}

			if (r != null) {
				list.repaint(r);
			}
		}

        //
        // ListDataListener
        //
		public void intervalAdded(ListDataEvent e) {
			updateLayoutStateNeeded = modelChanged;

			int minIndex = Math.min(e.getIndex0(), e.getIndex1());
			int maxIndex = Math.max(e.getIndex0(), e.getIndex1());

            /* Sync the SelectionModel with the DataModel.
			 */

			ListSelectionModel sm = list.getSelectionModel();
			if (sm != null) {
				sm.insertIndexInterval(minIndex, maxIndex - minIndex + 1, true);
			}

            /* Repaint the entire list, from the origin of
             * the first added cell, to the bottom of the
             * component.
			 */
			redrawList();
		}

		public void intervalRemoved(ListDataEvent e) {
			updateLayoutStateNeeded = modelChanged;

            /* Sync the SelectionModel with the DataModel.
			 */

			ListSelectionModel sm = list.getSelectionModel();
			if (sm != null) {
				sm.removeIndexInterval(e.getIndex0(), e.getIndex1());
			}

            /* Repaint the entire list, from the origin of
             * the first removed cell, to the bottom of the
             * component.
			 */

			redrawList();
		}

		public void contentsChanged(ListDataEvent e) {
			updateLayoutStateNeeded = modelChanged;
			redrawList();
		}

        //
        // ListSelectionListener
        //
		public void valueChanged(ListSelectionEvent e) {
			maybeUpdateLayoutState();
			int size = getElementCount(list);
			int firstIndex = Math.min(size - 1, Math.max(e.getFirstIndex(), 0));
			int lastIndex = Math.min(size - 1, Math.max(e.getLastIndex(), 0));

			Rectangle bounds = getCellBounds(list, firstIndex, lastIndex);

			if (bounds != null) {
				list.repaint(bounds.x, bounds.y, bounds.width, bounds.height);
			}
		}

        //
        // MouseListener
        //
        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        // Whether or not the mouse press (which is being considered as part
        // of a drag sequence) also caused the selection change to be fully
        // processed.
        private boolean dragPressDidSelection;

		public void mousePressed(MouseEvent e) {
			if (SwingXUtilities.shouldIgnore(e, list)) {
				return;
			}

			boolean dragEnabled = list.getDragEnabled();
			boolean grabFocus = true;

			// different behavior if drag is enabled
			if (dragEnabled) {
                // PENDING JW: this isn't aware of sorting/filtering - fix!
				int row = SwingXUtilities.loc2IndexFileList(list, e.getPoint());
				// if we have a valid row and this is a drag initiating event
				if (row != -1 && DragRecognitionSupport.mousePressed(e)) {
					dragPressDidSelection = false;

					if (e.isControlDown()) {
						// do nothing for control - will be handled on release
						// or when drag starts
						return;
					} else if (!e.isShiftDown() && list.isSelectedIndex(row)) {
						// clicking on something that's already selected
						// and need to make it the lead now
						list.addSelectionInterval(row, row);
						return;
					}

					// could be a drag initiating event - don't grab focus
					grabFocus = false;

					dragPressDidSelection = true;
				}
			} else {
				// When drag is enabled mouse drags won't change the selection
				// in the list, so we only set the isAdjusting flag when it's
				// not enabled
				list.setValueIsAdjusting(true);
			}

			if (grabFocus) {
				SwingXUtilities.adjustFocus(list);
			}

			adjustSelection(e);
		}

		private void adjustSelection(MouseEvent e) {
            // PENDING JW: this isn't aware of sorting/filtering - fix!
			int row = SwingXUtilities.loc2IndexFileList(list, e.getPoint());
			if (row < 0) {
// If shift is down in multi-select, we should do nothing.
// For single select or non-shift-click, clear the selection
				if (isFileList && e.getID() == MouseEvent.MOUSE_PRESSED
						&& (!e.isShiftDown() || list.getSelectionMode() == ListSelectionModel.SINGLE_SELECTION)) {
					list.clearSelection();
				}
			} else {
				int anchorIndex = adjustIndex(list.getAnchorSelectionIndex(), list);
				boolean anchorSelected;
				if (anchorIndex == -1) {
					anchorIndex = 0;
					anchorSelected = false;
				} else {
					anchorSelected = list.isSelectedIndex(anchorIndex);
				}

//				if (BasicGraphicsUtils.isMenuShortcutKeyDown(e)) {
				if (e.isControlDown()) {
					if (e.isShiftDown()) {
						if (anchorSelected) {
							list.addSelectionInterval(anchorIndex, row);
						} else {
							list.removeSelectionInterval(anchorIndex, row);
							if (isFileList) {
								list.addSelectionInterval(row, row);
								list.getSelectionModel().setAnchorSelectionIndex(anchorIndex);
							}
						}
					} else if (list.isSelectedIndex(row)) {
						list.removeSelectionInterval(row, row);
					} else {
						list.addSelectionInterval(row, row);
					}
				} else if (e.isShiftDown()) {
					list.setSelectionInterval(anchorIndex, row);
				} else {
					list.setSelectionInterval(row, row);
				}
			}
		}

		public void dragStarting(MouseEvent me) {
//			if (BasicGraphicsUtils.isMenuShortcutKeyDown(me)) {
			if (me.isControlDown()) {
                // PENDING JW: this isn't aware of sorting/filtering - fix!
				int row = SwingXUtilities.loc2IndexFileList(list, me.getPoint());
				list.addSelectionInterval(row, row);
			}
		}

		public void mouseDragged(MouseEvent e) {
			if (SwingXUtilities.shouldIgnore(e, list)) {
				return;
			}

			if (list.getDragEnabled()) {
				DragRecognitionSupport.mouseDragged(e, this);
				return;
			}

//			if (e.isShiftDown() || BasicGraphicsUtils.isMenuShortcutKeyDown(e)) {
			if (e.isShiftDown() || e.isControlDown()) {
				return;
			}

			int row = locationToIndex(list, e.getPoint());
			if (row != -1) {
				// 4835633.  Dragging onto a File should not select it.
				if (isFileList) {
					return;
				}
				Rectangle cellBounds = getCellBounds(list, row, row);
				if (cellBounds != null) {
					list.scrollRectToVisible(cellBounds);
					list.setSelectionInterval(row, row);
				}
			}
		}

		public void mouseMoved(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
			if (SwingXUtilities.shouldIgnore(e, list)) {
				return;
			}

			if (list.getDragEnabled()) {
				MouseEvent me = DragRecognitionSupport.mouseReleased(e);
				if (me != null) {
					SwingXUtilities.adjustFocus(list);
					if (!dragPressDidSelection) {
						adjustSelection(me);
					}
				}
			} else {
				list.setValueIsAdjusting(false);
			}
		}

        //
        // FocusListener
        //
		public void repaintCellFocus() {
			int leadIndex = adjustIndex(list.getLeadSelectionIndex(), list);
			if (leadIndex != -1) {
				Rectangle r = getCellBounds(list, leadIndex, leadIndex);
				if (r != null) {
					list.repaint(r.x, r.y, r.width, r.height);
				}
			}
		}

		/*
		 * The focusGained() focusLost() methods run when the JList focus changes.
		 */

		public void focusGained(FocusEvent e) {
			repaintCellFocus();
		}

		public void focusLost(FocusEvent e) {
			repaintCellFocus();
		}
	}

    // copied from javax.swing.plaf.basic.BasicListUI
    private static int adjustIndex(int index, JList<?> list) {
        return index < list.getModel().getSize() ? index : -1;
    }

    // copied from javax.swing.plaf.basic.BasicListUI
    protected static final TransferHandler defaultTransferHandler = new ListTransferHandler();

    @SuppressWarnings("serial")
	static class ListTransferHandler extends TransferHandler implements UIResource {
        protected Transferable createTransferable(JComponent c) {
            if (c instanceof JList<?> list) {
				List<?> selValues = list.getSelectedValuesList();
                if(selValues.isEmpty()) {
                	return null;
                }

                StringBuilder plainStr = new StringBuilder();
                StringBuilder htmlStr = new StringBuilder();

                htmlStr.append("<html>\n<body>\n<ul>\n");

                for (int i = 0; i < selValues.size(); i++) {
                	Object obj = selValues.get(i);
                    String val = ((obj == null) ? "" : obj.toString());
                    plainStr.append(val).append('\n');
                    htmlStr.append("  <li>").append(val).append('\n');
                }

                // remove the last newline
                plainStr.deleteCharAt(plainStr.length() - 1);
                htmlStr.append("</ul>\n</body>\n</html>");

                // Class sun.datatransfer.DataFlavorUtil.BasicTransferable is not visible
                return new BasicTransferable(plainStr.toString(), htmlStr.toString());
            }

            return null;
        }

        public int getSourceActions(JComponent c) {
            return COPY;
        }
    }
}
