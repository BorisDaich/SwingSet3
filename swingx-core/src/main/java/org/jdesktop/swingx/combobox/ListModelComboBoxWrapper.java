package org.jdesktop.swingx.combobox;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

@SuppressWarnings("serial")
public class ListModelComboBoxWrapper extends AbstractListModel<Object> implements ComboBoxModel<Object> {
	
    private ListModel<?> delegate;
    
    private Object selectedItem;
    
    public ListModelComboBoxWrapper(ListModel<?> delegate) {
        this.delegate = delegate;
    }
    
    /**
     * {@inheritDoc}
     */ 
    @Override
    public int getSize() {
        return delegate.getSize();
    }

    /**
     * {@inheritDoc}
     */ 
    @Override
    public Object getElementAt(int index) {
        return delegate.getElementAt(index);
    }

    /**
     * {@inheritDoc}
     */ 
    @Override
    public void addListDataListener(ListDataListener l) {
        super.addListDataListener(l);
        delegate.addListDataListener(l);
    }

    /**
     * {@inheritDoc}
     */ 
    @Override
    public void removeListDataListener(ListDataListener l) {
        delegate.removeListDataListener(l);
        super.removeListDataListener(l);
    }

    /**
     * {@inheritDoc}
     */ 
    @Override // implements interface ComboBoxModel<E>
    public void setSelectedItem(Object anItem) {
        if ((selectedItem != null && !selectedItem.equals(anItem))
                || selectedItem == null && anItem != null) {
            selectedItem = anItem;
            
            fireContentsChanged(this, -1, -1);
        }
    }

    /**
     * {@inheritDoc}
     */ 
    @Override // implements interface ComboBoxModel<E>
    public Object getSelectedItem() {
        return selectedItem;
    }
}
