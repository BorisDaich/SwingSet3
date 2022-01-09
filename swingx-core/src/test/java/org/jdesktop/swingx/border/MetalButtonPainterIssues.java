package org.jdesktop.swingx.border;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Panel;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

// wg. https://github.com/homebeaver/SwingSet/issues/18
public class MetalButtonPainterIssues extends Panel {

	private static final Logger LOG = Logger.getLogger(MetalButtonPainterIssues.class.getName());

//    JTabbedPane tab;
    JPanel buttonPanel = new JPanel();
//    JPanel checkboxPanel = new JPanel();
//    JPanel radioButtonPanel = new JPanel();

    Vector<AbstractButton> buttons = new Vector<AbstractButton>();
//    Vector<AbstractButton> checkboxes = new Vector<AbstractButton>();
//    Vector<AbstractButton> radiobuttons = new Vector<AbstractButton>();
//    private Vector<AbstractButton> togglebuttons = new Vector<AbstractButton>(); // not used

    // radiobuttons
    Vector<AbstractButton> currentControls = buttons;

    JButton button;
//    JCheckBox check;
//    JRadioButton radio;
//    private JToggleButton toggle;

    EmptyBorder border5 = new EmptyBorder(5,5,5,5);
    EmptyBorder border10 = new EmptyBorder(10,10,10,10);

    Insets insets0 = new Insets(0,0,0,0);
    Insets insets10 = new Insets(10,10,10,10);

	// The preferred size of the demo
    static private int PREFERRED_WIDTH = 680;
    static private int PREFERRED_HEIGHT = 600;

    // Premade convenience dimensions, for use wherever you need 'em.
    static Dimension HGAP2 = new Dimension(2,1);
    static Dimension VGAP2 = new Dimension(1,2);

    static Dimension HGAP5 = new Dimension(5,1);
    static Dimension VGAP5 = new Dimension(1,5);

    static Dimension HGAP10 = new Dimension(10,1);
    static Dimension VGAP10 = new Dimension(1,10);

    static Dimension HGAP15 = new Dimension(15,1);
    static Dimension VGAP15 = new Dimension(1,15);

    static Dimension HGAP20 = new Dimension(20,1);
    static Dimension VGAP20 = new Dimension(1,20);

    static Dimension HGAP25 = new Dimension(25,1);
    static Dimension VGAP25 = new Dimension(1,25);

    static Dimension HGAP30 = new Dimension(30,1);
    static Dimension VGAP30 = new Dimension(1,30);

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
    	GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
//    	OceanButtonBorderIssues demo = new OceanButtonBorderIssues(new SwingSet2(null, gc, false));
    	MetalButtonPainterIssues demo = new MetalButtonPainterIssues(createFrame(gc));
/*        demo.mainImpl();
    public void mainImpl() {
        JFrame frame = new JFrame(getName());
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(getDemoPanel(), BorderLayout.CENTER);
        getDemoPanel().setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        frame.pack();
        frame.setVisible(true);
    }
 */
        JFrame frame = new JFrame("Metal Button (woBorder) Painter Issue");
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(demo.getDemoPanel(), BorderLayout.CENTER);
        //demo.getDemoPanel().setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        frame.pack();
        frame.setVisible(true); 	
    }
    
    public static JFrame createFrame(GraphicsConfiguration gc) {
    	JFrame frame = new JFrame(gc);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	return frame;
    }

/*
new SwingSet2(null, gc, false) :
	frame = createFrame(gc);
    public static JFrame createFrame(GraphicsConfiguration gc) {
        JFrame frame = new JFrame(gc);
        if (numSSs == 0) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            WindowListener l = new WindowAdapter() {
            	@Override
                public void windowClosing(WindowEvent e) {
                    numSSs--;
                    swingSets.remove(this);
                }
            };
            frame.addWindowListener(l);
        }
        return frame;
    }

 */
    // Used only if swingset is an application
    private JFrame frame = null;
    private JPanel panel = null;
    public JPanel getDemoPanel() {
        return panel;
    }
    // ctor
    public MetalButtonPainterIssues(JFrame frame) {
        // Set the title for this demo, and an icon used to represent this demo inside SwingSet2.
/*        super(swingset, "ButtonDemo", ICON_PATH); 
        public DemoModule(SwingSet2 swingset, String resourceName, String iconPath) {
            UIManager.put("swing.boldMetal", Boolean.FALSE);
            panel = new JPanel();
            panel.setLayout(new BorderLayout());

            this.resourceName = resourceName;
            this.iconPath = iconPath;
            this.swingset = swingset;

            loadSourceCode();
        }
*/
    	UIManager.put("swing.boldMetal", Boolean.FALSE);
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
//        tab = new JTabbedPane();
//        tab.getModel().addChangeListener(e -> {
//            SingleSelectionModel model = (SingleSelectionModel) e.getSource();
//            if(model.getSelectedIndex() == 0) {
//                currentControls = buttons;
//            } else if(model.getSelectedIndex() == 1) {
//                currentControls = radiobuttons;
//            } else if(model.getSelectedIndex() == 2) {
//                currentControls = checkboxes;
//            } else {
//                currentControls = togglebuttons;
//            }
//        });

        JPanel demo = getDemoPanel();
        demo.setLayout(new BoxLayout(demo, BoxLayout.Y_AXIS));
//        demo.add(tab);
        demo.add(buttonPanel);
        addButtons();
//        addRadioButtons();
//        addCheckBoxes();
        currentControls = buttons;
    }

    Border loweredBorder = new CompoundBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED),
            new EmptyBorder(5,5,5,5));
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

    public void addButtons() {
//        tab.addTab("buttons", buttonPanel);
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

        String buttonText1 = "button1";
        button = new JButton(buttonText1);
        // wg. https://github.com/homebeaver/SwingSet/issues/18 :
//        if(button.getBorder() instanceof BorderUIResource.CompoundBorderUIResource) {
//        	BasicMarginBorder bmb = new BasicMarginBorder();
//        	button.setBorder(new CompoundBorder(new MetalButtonBorder(bmb), bmb));
//        }
        p2.add(button);
        buttons.add(button);
        p2.add(Box.createRigidArea(HGAP10));

        button = new JButton("button2");
//        if(button.getBorder() instanceof BorderUIResource.CompoundBorderUIResource) {
//        	BasicMarginBorder bmb = new BasicMarginBorder();
//        	button.setBorder(new CompoundBorder(new MetalButtonBorder(bmb), bmb));
//        }
        p2.add(button);
        buttons.add(button);
        p2.add(Box.createRigidArea(HGAP10));
        
        String buttonText3 = "<html><font size=2 color=red><bold>Three!</font></html>";
        button = new JButton(buttonText3);
//        if(button.getBorder() instanceof BorderUIResource.CompoundBorderUIResource) {
//        	BasicMarginBorder bmb = new BasicMarginBorder();
//        	button.setBorder(new CompoundBorder(new MetalButtonBorder(bmb), bmb));
//        }
        p2.add(button);
        buttons.add(button);

//        // Image Buttons
//        verticalPane.add(Box.createRigidArea(VGAP30));
//        JPanel p3 = createHorizontalPanel(false); // true == loweredBorder from super
//        verticalPane.add(p3);
//        createImageButtons(p3);
//        
//        // traffic lights buttons
//        verticalPane.add(Box.createRigidArea(VGAP30));
//        JPanel p4 = createHorizontalPanel(true);
//        verticalPane.add(p4);
//        createTrafficLightButtons(p4);
//
//        verticalPane.add(Box.createVerticalGlue());
        
        buttonPanel.add(Box.createHorizontalGlue());
        currentControls = buttons; // currentControls is global para for createControls!
        buttonPanel.add(createControls());       
    }

    /*
     * JPanel controls aka Controler ist im rechten Teil der Panel buttonPanel, radioButtonPanel, checkboxPanel positioniert.
     * Es beinhaltet Controler für
     * - JCheckBox'es : Display Options
     * - JRadioButton's : Pad Amount
     * - LayoutControlPanel+DirectionPanel mit 
     * - - Text Position
     * - - Content Alignment
     */
    /**
     * creates controler panel with checkBoxes for Display Options and radioButtons for Pad Amount
     * and LayoutControlPanel for Text Position and Content Alignment
     * 
     * @see LayoutControlPanel
     * @see DirectionPanel
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

//        JPanel rightColumn = new LayoutControlPanel(this);

        buttonControls.add(leftColumn);
        buttonControls.add(Box.createRigidArea(HGAP20));
//        buttonControls.add(rightColumn);
//        buttonControls.add(Box.createRigidArea(HGAP20));

        controls.add(buttonControls);

        // Display Options
        JLabel l = new JLabel("controlpanel_label");
        leftColumn.add(l);

        JCheckBox bordered = new JCheckBox("paintborder");
        bordered.setActionCommand("PaintBorder");
//        bordered.setMnemonic(getMnemonic("ButtonDemo.paintborder_mnemonic"));
//        bordered.setToolTipText(getString("ButtonDemo.paintborder_tooltip"));
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
				b.invalidate();
			}
        });
        leftColumn.add(bordered);

        JCheckBox focused = new JCheckBox("paintfocus");
        focused.setActionCommand("PaintFocus");
//        focused.setToolTipText(getString("ButtonDemo.paintfocus_tooltip"));
//        focused.setMnemonic(getMnemonic("ButtonDemo.paintfocus_mnemonic"));
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
//        enabled.setMnemonic(getMnemonic("ButtonDemo.enabled_mnemonic"));
//        enabled.setToolTipText(getString("ButtonDemo.enabled_tooltip"));
        enabled.setSelected(true);

        enabled.addItemListener(e -> {
        	JCheckBox cb = (JCheckBox) e.getSource(); // cb == e.getSource() == enabled
        	boolean enable = cb.isSelected();
            Component c;
            for(int i = 0; i < currentControls.size(); i++) {
                c = (Component) currentControls.elementAt(i);
                if(c instanceof JButton) {
                	JButton b = (JButton)c;
//                	if("green".equals(b.getName())) { // set the green light out of order
//                		b.setIcon(enable ? green : outoforder);
//                	} else if("flipflop".equals(b.getName())) { // set the flipflop light out of order
//                		b.setIcon(enable ? green : outoforder);
//                	} else if("red".equals(b.getName())) { 
//                		// red light remains red!
//                	} else {
                		c.setEnabled(enable);
//                	}
                }
                c.invalidate();
            }
        });
        leftColumn.add(enabled);

        JCheckBox filled = new JCheckBox("contentfilled");
        filled.setActionCommand("ContentFilled");
//        filled.setMnemonic(getMnemonic("ButtonDemo.contentfilled_mnemonic"));
//        filled.setToolTipText(getString("ButtonDemo.contentfilled_tooltip"));
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
//        defaultPad.setMnemonic(getMnemonic("ButtonDemo.default_mnemonic"));
//        defaultPad.setToolTipText(getString("ButtonDemo.default_tooltip"));
        defaultPad.addItemListener(e -> {
        	JRadioButton rb = (JRadioButton) e.getSource(); // rb == e.getSource() == defaultPad
        	if(rb.isSelected()) {
                AbstractButton b;
                LOG.config("defaultPad currentControls.size()="+currentControls.size());
                for(int i = 0; i < currentControls.size(); i++) {
//                    LOG.info("i="+i + "currentControl:"+currentControls.elementAt(i));
                    b = (AbstractButton) currentControls.elementAt(i);
                    b.setMargin(null);
                    b.invalidate();
                }
        	}
        });
        group.add(defaultPad);
        defaultPad.setSelected(true);
        leftColumn.add(defaultPad);

        JRadioButton zeroPad = new JRadioButton("zero");
        zeroPad.setActionCommand("ZeroPad");
//        zeroPad.setMnemonic(getMnemonic("ButtonDemo.zero_mnemonic"));
//        zeroPad.setToolTipText(getString("ButtonDemo.zero_tooltip"));
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
//        tenPad.setMnemonic(getMnemonic("ButtonDemo.ten_mnemonic"));
//        tenPad.setToolTipText(getString("ButtonDemo.ten_tooltip"));

        tenPad.addItemListener(e -> {
        	JRadioButton rb = (JRadioButton) e.getSource(); // rb == e.getSource() == tenPad
        	if(rb.isSelected()) {
                AbstractButton b;
                for(int i = 0; i < currentControls.size(); i++) {
                    b = (AbstractButton) currentControls.elementAt(i);
                    b.setMargin(insets10);
                    b.invalidate();
                }
        	}
        });
        group.add(tenPad);
        leftColumn.add(tenPad);

        leftColumn.add(Box.createRigidArea(VGAP20));
        return controls;
    }

    public Vector<AbstractButton> getCurrentControls() {
        return currentControls;
    }

}
