package org.jdesktop.swingx.plaf;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.awt.Dimension;
import java.awt.Insets;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.hamcrest.CoreMatchers;
import org.jdesktop.swingx.prompt.BuddySupport;
import org.jdesktop.test.EDTRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EDTRunner.class)
public class BuddyLayoutAndBorderTest {
	
    private static final Logger LOG = Logger.getLogger(BuddyLayoutAndBorderTest.class.getName());

	private BuddyLayoutAndBorder blab;
	private JTextField textField;

	@Before
	public void setup() {
		textField = new JTextField();
		blab = new BuddyLayoutAndBorder();
		blab.install(textField);
	}

	@Test
	public void testBorder() throws Exception {
		Border newBorder = BorderFactory.createEmptyBorder();
		textField.setBorder(newBorder);
		assertNotSame("Border should have been wrapped.", newBorder, textField.getBorder());
	}

	@Test
	public void testUninstall() throws Exception {
		blab.uninstall();
		Border newBorder = BorderFactory.createEmptyBorder();
		textField.setBorder(newBorder);
		assertSame("Border should NOT have been wrapped.", newBorder, textField.getBorder());
	}

	@Test
	public void testPreferredWidth() throws Exception {
		JButton btn = new JButton("hey");
		/*

@see https://github.com/homebeaver/SwingSet/issues/6
Bei der Berechnung textField.getPreferredSize() wird in BasicTextUI.getPreferredSize()
caretMargin addiert (=1 , DEFAULT_CARET_MARGIN)

		 */
		int txtWidth = textField.getPreferredSize().width - 1; // minus caretMargin
		Dimension dim = textField.getMinimumSize();

		// PS: para textField wird in preferredLayoutSize gar nicht verwendet!!!
		LOG.info("textField.MinimumSize="+dim+" assertSame: expected (txtWidth-caretMargin)="+txtWidth + " is="+blab.preferredLayoutSize(textField).width);
		assertSame(txtWidth, blab.preferredLayoutSize(textField).width);

		BuddySupport.addLeft(btn, textField);

		assertSame(String.format("preferred layout size should be %d", txtWidth),
				txtWidth, blab.preferredLayoutSize(textField).width);

		btn.setVisible(false);
		assertSame(txtWidth, blab.preferredLayoutSize(textField).width);
	}

	@Test
	public void testBorderInsets() throws Exception {
		JButton btn = new JButton("hey");
		int left = blab.getBorderInsets(textField).left;
		int btnWidth = btn.getPreferredSize().width;

		BuddySupport.addLeft(btn, textField);
		assertSame(btn, BuddySupport.getLeft(textField).get(0));

		assertSame(left + btnWidth, blab.getBorderInsets(textField).left);

		btn.setVisible(false);
		assertSame(left, blab.getBorderInsets(textField).left);
	}
	
	@Test
	public void testGetBorderInsetsWithNullDelegate() {
	    textField.setBorder(null);
	    blab.install(textField);
	    assertThat(blab.getRealBorderInsets(), CoreMatchers.is(new Insets(0, 0, 0, 0)));
	}
}
