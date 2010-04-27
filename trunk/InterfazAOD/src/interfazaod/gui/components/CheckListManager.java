package interfazaod.gui.components;

import interfazaod.gui.cellrenderers.CheckListCellRenderer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import aodbuilder.aodLayer.aodprofile.beans.AODProfileBean;


public class CheckListManager extends MouseAdapter implements ListSelectionListener, ActionListener{ 
    private ListSelectionModel selectionModel = new DefaultListSelectionModel(); 
    private JList list = new JList(); 
    private JList parentList = new JList();
    int hotspot = new JCheckBox().getPreferredSize().width; 
 
    public CheckListManager(JList list, JList parent){ 
        this.list = list; 
        this.parentList = parent;
        list.setCellRenderer(new CheckListCellRenderer(list.getCellRenderer(), selectionModel)); 
        list.registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), JComponent.WHEN_FOCUSED); 
        list.addMouseListener(this); 
        selectionModel.addListSelectionListener(this); 
    } 
 
    public ListSelectionModel getSelectionModel(){ 
        return selectionModel; 
    } 
 
    private void toggleSelection(int index){ 
        if(index<0) 
            return; 
 
       AODProfileBean bean = (AODProfileBean) list.getSelectedValue();
       if(selectionModel.isSelectedIndex(index)) {
            selectionModel.removeSelectionInterval(index, index);
            bean.setSelected(false);
        }
        else{ 
            selectionModel.addSelectionInterval(index, index);
            bean.setSelected(true);
            if (parentList!=null){
	            AODProfileBean parent = (AODProfileBean)parentList.getSelectedValue();
	            if (!parent.isSelected()){
	            	parent.setSelected(true);
		            parentList.repaint();
	            }
            }
        }
    } 
 
    /*------------------------------[ MouseListener ]-------------------------------------*/ 
 
    public void mouseClicked(MouseEvent me){ 
        int index = list.locationToIndex(me.getPoint()); 
        if(index<0) 
            return; 
        if(me.getX()>list.getCellBounds(index, index).x+hotspot) 
            return; 
        toggleSelection(index); 
    } 
 
    /*-----------------------------[ ListSelectionListener ]---------------------------------*/ 
 
    public void valueChanged(ListSelectionEvent e){ 
        list.repaint(list.getCellBounds(e.getFirstIndex(), e.getLastIndex())); 
    } 
 
    /*-----------------------------[ ActionListener ]------------------------------*/ 
 
    public void actionPerformed(ActionEvent e){ 
        toggleSelection(list.getSelectedIndex()); 
    } 
    
} 