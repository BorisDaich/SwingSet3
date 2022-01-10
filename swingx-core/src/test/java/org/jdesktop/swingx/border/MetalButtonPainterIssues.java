package org.jdesktop.swingx.border;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.metal.MetalBorders;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;

// wg. https://github.com/homebeaver/SwingSet/issues/18
public class MetalButtonPainterIssues extends InteractiveTestCase {

	private static final Logger LOG = Logger.getLogger(MetalButtonPainterIssues.class.getName());

    JPanel buttonPanel = new JPanel();

    Vector<AbstractButton> buttons = new Vector<AbstractButton>();
    Vector<AbstractButton> currentControls = buttons;

    JXButton button;

    EmptyBorder border5 = new EmptyBorder(5,5,5,5);
    EmptyBorder border10 = new EmptyBorder(10,10,10,10);

    Insets insets0 = new Insets(0,0,0,0);
    Insets insets10 = new Insets(10,10,10,10);

	// The preferred size of the demo
    static private int PREFERRED_WIDTH = 680;
    static private int PREFERRED_HEIGHT = 600;

    // Premade convenience dimensions, for use wherever you need 'em.

    static private Dimension HGAP10 = new Dimension(10,1);
    static private Dimension VGAP10 = new Dimension(1,10);

    static private Dimension HGAP20 = new Dimension(20,1);
    static private Dimension VGAP20 = new Dimension(1,20);

	public static void main(String[] args) throws Exception {
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		MetalButtonPainterIssues test = new MetalButtonPainterIssues(createFrame(gc));
		test.show(test.getFrame());
        
		try {
			test.runInteractiveTests();
		} catch (Exception e) {
			System.err.println("exception when executing interactive tests:");
			e.printStackTrace();
		}
	}

    public static JXFrame createFrame(GraphicsConfiguration gc) {
    	return new JXFrame("Metal Button (woBorder) Painter Issue", gc, true); // true == exitOnClose
    }

    private JXFrame frame = null;
    public JXFrame getFrame() {
        return frame;
    }
    private JPanel panel = null;
    public JPanel getDemoPanel() {
        return panel;
    }

    // ctor
    public MetalButtonPainterIssues(JXFrame xframe) {
    	UIManager.put("swing.boldMetal", Boolean.FALSE);
    	frame = xframe;
    	
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        panel.add(buttonPanel);
        addButtons();
        currentControls = buttons;
    }

    Border loweredBorder = new CompoundBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED), new EmptyBorder(5,5,5,5));
    public JPanel createVerticalPanel(boolean threeD) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentY(Component.TOP_ALIGNMENT);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        if(threeD) {
            p.setBorder(loweredBorder);
        }
        return p;
    }

    public JPanel createHorizontalPanel(boolean threeD) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setAlignmentY(Component.TOP_ALIGNMENT);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        if(threeD) {
            p.setBorder(loweredBorder);
        }
        return p;
    }

    private Border patchedBorder(JXButton button) {
    	Border buttonBorder = button.getBorder();
        if(buttonBorder instanceof BorderUIResource.CompoundBorderUIResource) {
        	CompoundBorder cb = (CompoundBorder) buttonBorder; // cast OK, denn CompoundBorderUIResource subclass von CompoundBorder
        	Border ob = cb.getOutsideBorder();
        	Border ib = cb.getInsideBorder();
        	LOG.info("plaf.metal CompoundBorder Button.border : "+cb + " "+ob + " "+ib);
        	if(ob instanceof MetalBorders.ButtonBorder && ib instanceof BasicBorders.MarginBorder) {
        		ob = new MetalButtonBorder();
        		ib = new BasicMarginBorder();
        		((MetalButtonBorder)ob).setInsideBorder(ib);
            	return new BorderUIResource.CompoundBorderUIResource(ob, ib);
        	}
        	return cb;
        }
        return buttonBorder;
    }
    
    public void addButtons() {
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(border5);

        JPanel verticalPane = createVerticalPanel(true);
        verticalPane.setAlignmentY(Component.TOP_ALIGNMENT);
        buttonPanel.add(verticalPane);

        // Text Buttons
        JPanel p2 = createHorizontalPanel(false);
        verticalPane.add(p2);
        p2.setBorder(new CompoundBorder(new TitledBorder(null, "ButtonDemo.textbuttons",
                                                          TitledBorder.LEFT, TitledBorder.TOP), border5));

        String buttonText1 = "<html>First<p>button</p></html>";
        button = new JXButton(buttonText1);
        // wg. https://github.com/homebeaver/SwingSet/issues/18 :
/*
public Border JComponent.getBorder() ist als LazyValue "Button.border" definiert in uiDefaultsTable:
- die uiDefaultsTable hat ca 660 Einträge, uiDefaultsTable bekommt man mit 
	UIDefaults uiDefaultsTable = UIManager.getDefaults();

- LazyValue heißt, dass das value Objekt zum key="Button.border" beim ersten Aufruf von get(key) gebildet wird.
In javax.swing.plaf.metal.MetalLookAndFeel steht

        LazyValue buttonBorder =
            t -> MetalBorders.getButtonBorder();

Also wird beim bei get("Button.border") die static Methode MetalBorders.getButtonBorder() ausgeführt. 
Einmal, dannach ist die Tabelle an der Stelle initialisiert.

javax.swing.plaf.metal.MetalBorders.getButtonBorder() ist ein Singleton für die Factory:

    private static Border buttonBorder;
    public static Border getButtonBorder() {
        if (buttonBorder == null) {
            buttonBorder = new BorderUIResource.CompoundBorderUIResource(
                                                   new MetalBorders.ButtonBorder(),
                                                   new BasicBorders.MarginBorder());
        }
        return buttonBorder;
    }

public Border JComponent.getBorder() ruft bei "plaf.metal" also die lazy Factory der inner Class 
javax.swing.plaf.BorderUIResource$CompoundBorderUIResource

Daher wissen wir bei if(button.getBorder() instanceof javax.swing.plaf.BorderUIResource.CompoundBorderUIResource) 
dass es sich um "plaf.metal" handelt, den wir evtl korrigieren: patchedBorder(button)

 */
        button.setBorder(patchedBorder(button));
        p2.add(button);
        buttons.add(button);
        p2.add(Box.createRigidArea(HGAP10));

        button = new JXButton("button2");
        button.setBorder(patchedBorder(button));
        p2.add(button);
        buttons.add(button);
        p2.add(Box.createRigidArea(HGAP10));
        
        String buttonText3 = "<html><font size=2 color=red><bold>Three!</font></html>";
        button = new JXButton(buttonText3);
        button.setBorder(patchedBorder(button));
        p2.add(button);
        buttons.add(button);

        buttonPanel.add(Box.createHorizontalGlue());
        currentControls = buttons; // currentControls is global para for createControls!
        buttonPanel.add(createControls());       
    }

    /**
     * creates controler panel with checkBoxes for Display Options and radioButtons for Pad Amount
     */
    public JPanel createControls() {
        @SuppressWarnings("serial")
		JPanel controls = new JPanel() {
            public Dimension getMaximumSize() {
                return new Dimension(300, super.getMaximumSize().height);
            }
        };
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.setAlignmentY(Component.TOP_ALIGNMENT);
        controls.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel buttonControls = createHorizontalPanel(true);
        buttonControls.setAlignmentY(Component.TOP_ALIGNMENT);
        buttonControls.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel leftColumn = createVerticalPanel(false);
        leftColumn.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftColumn.setAlignmentY(Component.TOP_ALIGNMENT);

        buttonControls.add(leftColumn);
        buttonControls.add(Box.createRigidArea(HGAP20));

        controls.add(buttonControls);

        // Display Options
        JLabel l = new JLabel("controlpanel_label");
        leftColumn.add(l);

        JCheckBox bordered = new JCheckBox("paintborder");
        bordered.setActionCommand("PaintBorder");
        if (currentControls == buttons) {
                bordered.setSelected(true); // initial
        }

        bordered.addItemListener(e -> {
			JCheckBox cb = (JCheckBox) e.getSource(); // cb == e.getSource() == bordered
			boolean borderPainted = cb.isSelected();
			AbstractButton b;
			LOG.config("bordered currentControls.size()=" + currentControls.size());
			for (int i = 0; i < currentControls.size(); i++) {
				b = (AbstractButton) currentControls.elementAt(i);
				b.setBorderPainted(borderPainted);
//				LOG.info("LayoutManager:"+b.getParent().getLayout());
				b.getParent().invalidate();
			}
        });
        leftColumn.add(bordered);

        JCheckBox focused = new JCheckBox("paintfocus");
        focused.setActionCommand("PaintFocus");
        focused.setSelected(true);

        focused.addItemListener(e -> {
        	JCheckBox cb = (JCheckBox) e.getSource(); // cb == e.getSource() == focused
        	boolean focusPainted = cb.isSelected();
            AbstractButton b;
			LOG.config("focused currentControls.size()=" + currentControls.size());
            for(int i = 0; i < currentControls.size(); i++) {
                b = (AbstractButton) currentControls.elementAt(i);
                b.setFocusPainted(focusPainted);
                b.invalidate();
            }
        });
        leftColumn.add(focused);

        JCheckBox enabled = new JCheckBox("enabled");
        enabled.setActionCommand("Enabled");
        enabled.setSelected(true);

        enabled.addItemListener(e -> {
        	JCheckBox cb = (JCheckBox) e.getSource(); // cb == e.getSource() == enabled
        	boolean enable = cb.isSelected();
            Component c;
            for(int i = 0; i < currentControls.size(); i++) {
                c = (Component) currentControls.elementAt(i);
                if(c instanceof JXButton) {
                	JXButton b = (JXButton)c;
                	b.setEnabled(enable);
                }
                c.invalidate();
            }
        });
        leftColumn.add(enabled);

        JCheckBox filled = new JCheckBox("contentfilled");
        filled.setActionCommand("ContentFilled");
        filled.setSelected(true);

        filled.addItemListener(e -> {
        	JCheckBox cb = (JCheckBox) e.getSource(); // cb == e.getSource() == filled
        	boolean caFilled = cb.isSelected();
            AbstractButton b;
            for(int i = 0; i < currentControls.size(); i++) {
                b = (AbstractButton) currentControls.elementAt(i);
                b.setContentAreaFilled(caFilled);
                b.invalidate();
            }
        });
        leftColumn.add(filled);

        leftColumn.add(Box.createRigidArea(VGAP20));

        l = new JLabel("padamount_label");
        leftColumn.add(l);
        ButtonGroup group = new ButtonGroup();
        
        JRadioButton defaultPad = new JRadioButton("default");
        defaultPad.setSelected(true);
        defaultPad.addItemListener(e -> {
        	JRadioButton rb = (JRadioButton) e.getSource(); // rb == e.getSource() == defaultPad
        	if(rb.isSelected()) {
                AbstractButton b;
                LOG.config("defaultPad currentControls.size()="+currentControls.size());
                for(int i = 0; i < currentControls.size(); i++) {
                    b = (AbstractButton) currentControls.elementAt(i);
                    b.setMargin(null);
                    b.invalidate();
                }
        	}
        });
        group.add(defaultPad);
        leftColumn.add(defaultPad);

        JRadioButton zeroPad = new JRadioButton("zero");
        zeroPad.setActionCommand("ZeroPad");
        zeroPad.addItemListener(e -> {
        	JRadioButton rb = (JRadioButton) e.getSource(); // rb == e.getSource() == zeroPad
        	if(rb.isSelected()) {
                AbstractButton b;
                LOG.config("zeroPad currentControls.size()="+currentControls.size());
                for(int i = 0; i < currentControls.size(); i++) {
                    b = (AbstractButton) currentControls.elementAt(i);
                    b.setMargin(insets0);
                    b.invalidate();
                }
        	}
        });
        group.add(zeroPad);
        leftColumn.add(zeroPad);

        JRadioButton tenPad = new JRadioButton("ten");
        tenPad.setActionCommand("TenPad");

        tenPad.addItemListener(e -> {
        	JRadioButton rb = (JRadioButton) e.getSource(); // rb == e.getSource() == tenPad
        	if(rb.isSelected()) {
                AbstractButton b;
                for(int i = 0; i < currentControls.size(); i++) {
                    b = (AbstractButton) currentControls.elementAt(i);
                    b.setMargin(insets10);
                    b.getParent().invalidate();
                }
        	}
        });
        group.add(tenPad);
        leftColumn.add(tenPad);

//        leftColumn.add(Box.createRigidArea(VGAP20));
        return controls;
    }

    public Vector<AbstractButton> getCurrentControls() {
        return currentControls;
    }

}
