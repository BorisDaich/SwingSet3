package org.jdesktop.swingx.plaf.metal;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.ComboBoxEditor;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxButton;
import javax.swing.plaf.metal.MetalComboBoxEditor;
import javax.swing.plaf.metal.MetalComboBoxIcon;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import javax.swing.plaf.metal.OceanTheme;

import org.jdesktop.swingx.plaf.basic.BasicXComboBoxUI;

/**
 * Metal UI for JXComboBox
 */
// copied from javax.swing.plaf.metal.MetalXComboBoxUI
public class MetalXComboBoxUI extends BasicXComboBoxUI {

    static boolean usingOcean() {
    	MetalTheme currentTheme = MetalLookAndFeel.getCurrentTheme();
    	return currentTheme instanceof OceanTheme;
    }
    
    public static ComponentUI createUI(JComponent c) {
        return new MetalXComboBoxUI();
    }
    
    public MetalXComboBoxUI() {
		super();
    }

    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
    }

	public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
		// This is really only called if we're using ocean.
		if (g == null || bounds == null) {
			throw new NullPointerException("Must supply a non-null Graphics and Rectangle");
		} else if (usingOcean()) {
			bounds.x += 2;
			bounds.width -= 3;
			if (arrowButton != null) {
				Insets buttonInsets = arrowButton.getInsets();
				bounds.y += buttonInsets.top;
				bounds.height -= (buttonInsets.top + buttonInsets.bottom);
			} else {
				bounds.y += 2;
				bounds.height -= 4;
			}
		}
		super.paintCurrentValue(g, bounds, hasFocus);
	}

	public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
		// This is really only called if we're using ocean.
		if (g == null || bounds == null) {
			throw new NullPointerException("Must supply a non-null Graphics and Rectangle");
		} else if (usingOcean()) {
			g.setColor(MetalLookAndFeel.getControlDarkShadow());
			g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height - 1);
			g.setColor(MetalLookAndFeel.getControlShadow());
			g.drawRect(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 3);
			if (hasFocus && !isPopupVisible(comboBox) && arrowButton != null) {
				g.setColor(listBox.getSelectionBackground());
				Insets buttonInsets = arrowButton.getInsets();
				if (buttonInsets.top > 2) {
					g.fillRect(bounds.x + 2, bounds.y + 2, bounds.width - 3, buttonInsets.top - 2);
				}
				if (buttonInsets.bottom > 2) {
					g.fillRect(bounds.x + 2, bounds.y + bounds.height - buttonInsets.bottom,
						bounds.width - 3, buttonInsets.bottom - 2);
				}
			}
		} else {
			super.paintCurrentValueBackground(g, bounds, hasFocus);
		}
	}

    public int getBaseline(JComponent c, int width, int height) {
        int baseline;
        if (usingOcean() && height >= 4) {
            height -= 4;
            baseline = super.getBaseline(c, width, height);
            if (baseline >= 0) {
                baseline += 2;
            }
        }
        else {
            baseline = super.getBaseline(c, width, height);
        }
        return baseline;
    }

    protected ComboBoxEditor createEditor() {
        return new MetalComboBoxEditor.UIResource();
    }

    protected ComboPopup createPopup() {
        return super.createPopup();
    }

    /**
     * {@inheritDoc} <p>
     * Overridden to provide {@code MetalComboBoxButton}
     */
    @Override
    protected JButton createArrowButton() {
        boolean iconOnly = (comboBox.isEditable() || usingOcean());
        Icon icon = new MetalComboBoxIcon();
        @SuppressWarnings("unchecked")
		JButton button = new MetalComboBoxButton( (JComboBox<Object>)comboBox,
                                                  icon,
                                                  iconOnly, // paint only icon
                                                  currentValuePane,
                                                  listBox );
        button.setMargin( new Insets( 0, 1, 1, 3 ) ); // see getMinimumSize
        if (usingOcean()) {
            // Disabled rollover effect.
            button.putClientProperty("NoButtonRollover" //MetalBorders.NO_BUTTON_ROLLOVER,
                                     ,Boolean.TRUE);
        }
        updateButtonForOcean(button);
        return button;
    }

    private void updateButtonForOcean(JButton button) {
        if (usingOcean()) {
            // Ocean renders the focus in a different way, this would be redundant.
            button.setFocusPainted(comboBox.isEditable());
        }
    }

    // TODO in BasicXComboBoxUI inner class nicht definiert PropertyChangeHandler
//  public PropertyChangeListener createPropertyChangeListener() {
//      return new MetalPropertyChangeListener();
//  }
//
//  public class MetalPropertyChangeListener extends BasicXComboBoxUI.PropertyChangeHandler {
//      public MetalPropertyChangeListener() {}
//
//      public void propertyChange(PropertyChangeEvent e) {
//          super.propertyChange( e );
//          String propertyName = e.getPropertyName();
//
//          if ( propertyName == "editable" ) {
//              if(arrowButton instanceof MetalComboBoxButton) {
//                          MetalComboBoxButton button = (MetalComboBoxButton)arrowButton;
//                          button.setIconOnly( comboBox.isEditable() ||
//                                  MetalLookAndFeel.usingOcean() );
//              }
//                      comboBox.repaint();
//              updateButtonForOcean(arrowButton);
//          } else if ( propertyName == "background" ) {
//              Color color = (Color)e.getNewValue();
//              arrowButton.setBackground(color);
//              listBox.setBackground(color);
//
//          } else if ( propertyName == "foreground" ) {
//              Color color = (Color)e.getNewValue();
//              arrowButton.setForeground(color);
//              listBox.setForeground(color);
//          }
//      }
//  }

  // TODO BasicXComboBoxUI.ComboBoxLayoutManager fehlt
//  protected LayoutManager createLayoutManager() {
//      return new MetalComboBoxLayoutManager();
//  }
//
//  public class MetalComboBoxLayoutManager extends BasicXComboBoxUI.ComboBoxLayoutManager {
//      public MetalComboBoxLayoutManager() {}
//
//      public void layoutContainer( Container parent ) {
//          layoutComboBox( parent, this );
//      }
//      public void superLayout( Container parent ) {
//          super.layoutContainer( parent );
//      }
//  }
  
  // TODO
//  public void layoutComboBox( Container parent, MetalComboBoxLayoutManager manager ) {
//      if (comboBox.isEditable() && !(MetalLookAndFeel.getCurrentTheme() instanceof OceanTheme)) {
//          manager.superLayout( parent );
//          return;
//      }
//
//      if (arrowButton != null) {
//          if (MetalLookAndFeel.getCurrentTheme() instanceof OceanTheme) {
//              Insets insets = comboBox.getInsets();
//              int buttonWidth = arrowButton.getMinimumSize().width;
//              // javax.swing.plaf.metal.MetalUtils cannot be resolved / is not public
//              arrowButton.setBounds(MetalUtils.isLeftToRight(comboBox)
//                              ? (comboBox.getWidth() - insets.right - buttonWidth)
//                              : insets.left,
//                          insets.top, buttonWidth,
//                          comboBox.getHeight() - insets.top - insets.bottom);
//          }
//          else {
//              Insets insets = comboBox.getInsets();
//              int width = comboBox.getWidth();
//              int height = comboBox.getHeight();
//              arrowButton.setBounds( insets.left, insets.top,
//                                     width - (insets.left + insets.right),
//                                     height - (insets.top + insets.bottom) );
//          }
//      }
//
//      if (editor != null && MetalLookAndFeel.getCurrentTheme() instanceof OceanTheme) {
//          Rectangle cvb = rectangleForCurrentValue();
//          editor.setBounds(cvb);
//      }
//  }

  public void configureEditor() {
      super.configureEditor();
  }

  public void unconfigureEditor() {
      super.unconfigureEditor();
  }

  public Dimension getMinimumSize( JComponent c ) {
      if ( !isMinimumSizeDirty ) {
          return new Dimension( cachedMinimumSize );
      }

      Dimension size = null;

      if ( !comboBox.isEditable() && arrowButton != null) {
          Insets buttonInsets = arrowButton.getInsets();
          Insets insets = comboBox.getInsets();

          size = getDisplaySize();
          size.width += insets.left + insets.right;
          size.width += buttonInsets.right;
          size.width += arrowButton.getMinimumSize().width;
          size.height += insets.top + insets.bottom;
          size.height += buttonInsets.top + buttonInsets.bottom;
      }
      else if ( comboBox.isEditable() && arrowButton != null && editor != null ) {
          size = super.getMinimumSize( c );
          Insets margin = arrowButton.getMargin();
          size.height += margin.top + margin.bottom;
          size.width += margin.left + margin.right;
      }
      else {
          size = super.getMinimumSize( c );
      }

      cachedMinimumSize.setSize( size.width, size.height );
      isMinimumSizeDirty = false;

      return new Dimension( cachedMinimumSize );
  }

}
