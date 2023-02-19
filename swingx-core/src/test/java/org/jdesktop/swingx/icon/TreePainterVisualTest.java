package org.jdesktop.swingx.icon;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Painter;
import javax.swing.SwingUtilities;
import javax.swing.plaf.nimbus.test.MyTreeCellEditorPainter;
import javax.swing.plaf.nimbus.test.MyTreeCellPainter;
import javax.swing.plaf.nimbus.test.MyTreePainter;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.painter.AbstractAreaPainter;
import org.jdesktop.swingx.painter.ImagePainter;
import org.jdesktop.swingx.renderer.CheckBoxProvider;
import org.jdesktop.swingx.renderer.ComponentProvider;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.renderer.WrappingIconPanel;
import org.jdesktop.swingx.renderer.WrappingProvider;

import com.jhlabs.image.InvertFilter;

@SuppressWarnings("serial")
public class TreePainterVisualTest extends JXPanel {

	private static final Logger LOG = Logger.getLogger(TreePainterVisualTest.class.getName());
	
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater( () -> {
        	InteractiveTestCase.setLAF("Nimbus"); // start with nimbus
        	createAndShowGUI();
        });
    }

    /**
     * Create the GUI and show it.  
     * For thread safety, this method should be invoked from the event dispatch thread.
     */
    private static void createAndShowGUI() {
		JXFrame frame = new JXFrame("TreePainter Visual Test", true); // exitOnClose
		JXPanel demo = new TreePainterVisualTest(); //frame);
        frame = InteractiveTestCase.wrapInFrame(frame, demo);
		frame.setStartPosition(StartPosition.CenterInScreen);

        //Display the window.
    	frame.pack();
    	frame.setVisible(true);
    }

    private DefaultTreeRenderer sharedRenderer;
    private ComponentProvider<?> provider;

    private TreePainterVisualTest() {
    	super(new BorderLayout());
    	super.setBorder(BorderFactory.createLoweredBevelBorder());

        provider = new CheckBoxProvider(StringValues.TO_STRING);
        sharedRenderer = new DefaultTreeRenderer(new WrappingProvider(provider));

        JXTree tree = new JXTree();
        tree.setCellRenderer(sharedRenderer);
        JScrollPane scroller = new JScrollPane(tree);
        
        // <snip> JXTree rollover
        // enable and register a highlighter
        tree.setRolloverEnabled(true);
        tree.addHighlighter(createRolloverIconHighlighter());
        // </snip>
        
        super.add(scroller, BorderLayout.WEST);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        super.add(p, BorderLayout.CENTER);

		PainterIcon treeLeafEnabledIcon = new PainterIcon(SizingConstants.BUTTON_ICON, SizingConstants.BUTTON_ICON);
		treeLeafEnabledIcon.setPainter(MyTreePainter.factory(MyTreePainter.LEAFICON_ENABLED));
		JXButton treeLeafEnabledButton = new JXButton("treeLeafEnabled", treeLeafEnabledIcon);
		p.add(treeLeafEnabledButton);
      
		PainterIcon treeClosedEnabledIcon = new PainterIcon(SizingConstants.BUTTON_ICON, SizingConstants.BUTTON_ICON);
		treeClosedEnabledIcon.setPainter(MyTreePainter.factory(MyTreePainter.CLOSEDICON_ENABLED));
		JXButton treeClosedEnabledButton = new JXButton("treeClosedEnabled", treeClosedEnabledIcon);
		p.add(treeClosedEnabledButton);

		PainterIcon treeOpenEnabledIcon = new PainterIcon(SizingConstants.BUTTON_ICON, SizingConstants.BUTTON_ICON);
		treeOpenEnabledIcon.setPainter(MyTreePainter.factory(MyTreePainter.OPENICON_ENABLED));
		JXButton treeOpenEnabledButton = new JXButton("treeOpenEnabled", treeOpenEnabledIcon);
		p.add(treeOpenEnabledButton);

		PainterIcon treeCollapsedEnabledIcon = new PainterIcon(MyTreePainter.canvasSize());
		treeCollapsedEnabledIcon.setPainter(MyTreePainter.factory(MyTreePainter.COLLAPSEDICON_ENABLED));
		JXButton treeCollapsedEnabledButton = new JXButton("treeCollapsedEnabled", treeCollapsedEnabledIcon);
		p.add(treeCollapsedEnabledButton);

		PainterIcon treeCollapsedEnabledSelectedIcon = new PainterIcon(MyTreePainter.canvasSize());
		treeCollapsedEnabledSelectedIcon.setPainter(MyTreePainter.factory(MyTreePainter.COLLAPSEDICON_ENABLED_SELECTED));
		JXButton treeCollapsedEnabledSelectedButton = new JXButton("treeCollapsedEnabledSelected", treeCollapsedEnabledSelectedIcon);
		p.add(treeCollapsedEnabledSelectedButton);

		PainterIcon treeExpandedEnabledIcon = new PainterIcon(MyTreePainter.canvasSize());
		treeExpandedEnabledIcon.setPainter(MyTreePainter.factory(MyTreePainter.EXPANDEDICON_ENABLED));
		JXButton treeExpandedEnabledButton = new JXButton("treeExpandedEnabled", treeExpandedEnabledIcon);
		p.add(treeExpandedEnabledButton);

		PainterIcon treeExpandedEnabledSelectedIcon = new PainterIcon(MyTreePainter.canvasSize());
		treeExpandedEnabledSelectedIcon.setPainter(MyTreePainter.factory(MyTreePainter.EXPANDEDICON_ENABLED_SELECTED));
		JXButton treeExpandedEnabledSelectedButton = new JXButton("treeExpandedEnabledSelected", treeExpandedEnabledSelectedIcon);
		p.add(treeExpandedEnabledSelectedButton);

		PainterIcon treeCellBgEnabledFocusedIcon = new PainterIcon(MyTreeCellPainter.canvasSize());
		treeCellBgEnabledFocusedIcon.setPainter(MyTreeCellPainter.factory(MyTreeCellPainter.BACKGROUND_ENABLED_FOCUSED));
		JXButton treeCellBgEnabledFocusedButton = new JXButton("treeCellBgEnabledFocused", treeCellBgEnabledFocusedIcon);
		p.add(treeCellBgEnabledFocusedButton);

		PainterIcon treeCellBgEnabledSelectedIcon = new PainterIcon(MyTreeCellPainter.canvasSize());
		treeCellBgEnabledSelectedIcon.setPainter(MyTreeCellPainter.factory(MyTreeCellPainter.BACKGROUND_ENABLED_SELECTED));
		JXButton treeCellBgEnabledSelectedButton = new JXButton("treeCellBgEnabledSelected", treeCellBgEnabledSelectedIcon);
		p.add(treeCellBgEnabledSelectedButton);

		PainterIcon treeCellBgSelectedFocusedIcon = new PainterIcon(MyTreeCellPainter.canvasSize());
		treeCellBgSelectedFocusedIcon.setPainter(MyTreeCellPainter.factory(MyTreeCellPainter.BACKGROUND_SELECTED_FOCUSED));
		JXButton treeCellBgSelectedFocusedButton = new JXButton("treeCellBgSelectedFocused", treeCellBgSelectedFocusedIcon);
		p.add(treeCellBgSelectedFocusedButton);

		PainterIcon treeCellEditorBgEnabledIcon = new PainterIcon(MyTreeCellEditorPainter.canvasSize());
		treeCellEditorBgEnabledIcon.setPainter(MyTreeCellEditorPainter.factory(MyTreeCellEditorPainter.BACKGROUND_ENABLED));
		JXButton treeCellEditorBgEnabledButton = new JXButton("treeCellEditorBgEnabled", treeCellEditorBgEnabledIcon);
		p.add(treeCellEditorBgEnabledButton);

		PainterIcon treeCellEditorBgEnabledFocusedIcon = new PainterIcon(MyTreeCellEditorPainter.canvasSize());
		treeCellEditorBgEnabledFocusedIcon.setPainter(MyTreeCellEditorPainter.factory(MyTreeCellEditorPainter.BACKGROUND_ENABLED_FOCUSED));
		JXButton treeCellEditorBgEnabledFocusedButton = new JXButton("treeCellEditorBgEnabledFocused", treeCellEditorBgEnabledFocusedIcon);
		p.add(treeCellEditorBgEnabledFocusedButton);
    }
    
    // <snip> JXTree rollover
    // custom implementation of Highlighter which highlights by changing the node icon on rollover
    private Highlighter createRolloverIconHighlighter() {

        AbstractHighlighter hl = new AbstractHighlighter(HighlightPredicate.ROLLOVER_CELL) {
            /**
             * {@inheritDoc} <p>
             * 
             * Implemented to highlight by setting the node icon.
             */
            @Override // muss implementiert werden
            // JXTree tree : funktioniert es
            // JXTreetable tree : nicht                                  ==> TODO testen
            protected Component doHighlight(Component component, ComponentAdapter adapter) {
            	Icon icon = ((WrappingIconPanel) component).getIcon();
            	PainterIcon painterIcon = new PainterIcon(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
            	BufferedImage image = null;
            	if(icon instanceof RadianceIcon ri) {
            		image = ri.toImage(1);
            	} else if(icon instanceof ImageIcon ii) {
            		image = (BufferedImage)ii.getImage();
            	} else {
            		LOG.warning("no highlighting for "+icon);
            	}
                AbstractAreaPainter<Component> delegate = new ImagePainter(image);
                delegate.setFilters(new InvertFilter());
                painterIcon.setPainter((Painter<Component>)delegate);
                ((WrappingIconPanel) component).setIcon(painterIcon);
                return component;
            }
            // </snip>
            
            /**
             * {@inheritDoc} <p>
             * 
             * Implementated to return true if the component is a WrappingIconPanel,
             * a panel implemenation specialized for rendering tree nodes.
             * 
             */
            @Override
            protected boolean canHighlight(Component component, ComponentAdapter adapter) {
                return component instanceof WrappingIconPanel;
            }
            
        };
        return hl;
    }
    
}
