package org.jdesktop.swingx.icon;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.UIResource;

public class PauseIcon implements Icon, UIResource, SizingConstants {

    private BasicStroke stroke = new BasicStroke(2);

    private int width = SizingConstants.ACTION_ICON;
    private int height = SizingConstants.ACTION_ICON;
    private Color color;

    public PauseIcon() {
    }

    public PauseIcon(int size, Color color) {
    	width = size;
    	height = size;
    	this.color = color;
    }

    public PauseIcon(int size) {
    	this(size, null);
    }

    protected PauseIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    protected PauseIcon(Dimension size) {
    	this(Double.valueOf(size.getWidth()).intValue(), Double.valueOf(size.getHeight()).intValue());
    }

    // implements interface Icon:

    /**
     * {@inheritDoc}
     */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(color==null ? c.getForeground() : color);
		g2d.setStroke(stroke);
		g2d.fillRect(x + (2 * width / 9), y + 4, (2 * width / 9), height - 8);
		g2d.fillRect(x + 5 * width / 9, y + 4, (2 * width / 9), height - 8);
		g2d.dispose();
	}

	@Override
	public int getIconWidth() {
		return width;
	}

	@Override
	public int getIconHeight() {
		return height;
	}

	// visual test:
	private static final Logger LOG = Logger.getLogger(PauseIcon.class.getName());

	public static void main(String args[]) {
		UIDefaults uiDefaultsTable = UIManager.getDefaults();

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = frame.getContentPane();
		c.setSize(300, 300);
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		c.add(p);

//        JButton button = new JButton("ACTION_ICON size", new PauseIcon());
//        button.setText("ACTION_ICON size");
		JButton button = new JButton("<html>green<p>red when pressed</html>", new PauseIcon());
		button.setName("green"); // used in listener
//        button.setPressedIcon(red);
//        button.setRolloverIcon(yellow);
//        button.setDisabledIcon(outoforder);
//        button.setBorder(patchedBorder(button));
		p.add(button, BorderLayout.NORTH);
/*
        final String invalid = isValid() ? "" : ",invalid";
        final String hidden = visible ? "" : ",hidden";
        final String disabled = enabled ? "" : ",disabled";

 */
		LOG.info("button:" + button.toString() 
			+ "\nValid="+button.isValid()
			+ "\nVisible="+button.isVisible()
			+ "\nEnabled="+button.isEnabled()
			+ "\nSelected="+button.isSelected()
			+ "\nBorderPainted="+button.isBorderPainted()
			+ "\nFocusPainted="+button.isFocusPainted()
			+ "\nContentAreaFilled="+button.isContentAreaFilled()
			+ "\nLabel="+button.getLabel() // == return getText();
			);
		button.validate();
		button.setSelected(true);
		button.setBorderPainted(true);
		button.setFocusPainted(true);
		button.setContentAreaFilled(true);
		// "Button.disabledText", inactiveControlTextColor,  ==> new ColorUIResource(0x999999);
//		button.setBackground(Color.RED);
//		button.setForeground(Color.RED);
//		button.getLabel()

		JLabel label = new JLabel(new PauseIcon(SizingConstants.XL, Color.MAGENTA));
		p.add(label, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		
		LOG.info("button:" + button.toString() 
		+ "\nValid="+button.isValid()
		+ "\nVisible="+button.isVisible()
		+ "\nEnabled="+button.isEnabled()
		+ "\nSelected="+button.isSelected()
		+ "\nBorderPainted="+button.isBorderPainted()
		+ "\nFocusPainted="+button.isFocusPainted()
		+ "\nContentAreaFilled="+button.isContentAreaFilled()
		);
	}

}
