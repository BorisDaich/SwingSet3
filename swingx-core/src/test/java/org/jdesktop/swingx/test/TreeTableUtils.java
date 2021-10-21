package org.jdesktop.swingx.test;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

public class TreeTableUtils {
    private TreeTableUtils() {
        //does nothing
    }
    
    public static DefaultTreeTableModel convertDefaultTreeModel(DefaultTreeModel model) {
    	Vector<String> v = new Vector<String>();
    	v.add("A");
        DefaultTreeTableModel ttModel = new DefaultTreeTableModel(null, v);
        
        ttModel.setRoot(convertDefaultMutableTreeNode((DefaultMutableTreeNode) model.getRoot()));
        
        return ttModel;
    }
    
    private static DefaultMutableTreeTableNode convertDefaultMutableTreeNode(TreeNode node) {
    	DefaultMutableTreeTableNode ttNode = null;
    	Enumeration<TreeNode> children = null;
    	if(node instanceof DefaultMutableTreeNode) {
    		DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)node;
            ttNode = new DefaultMutableTreeTableNode(dmtn.getUserObject());
            children = dmtn.children();
    	} else {
    		return new DefaultMutableTreeTableNode();
    	}
        
        while (children.hasMoreElements()) {
            ttNode.add(convertDefaultMutableTreeNode(children.nextElement()));
        }
        
        return ttNode;
    }
}
