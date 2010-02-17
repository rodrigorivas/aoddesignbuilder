package interfazaod.actions;


import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import beans.aodprofile.AODProfileBean;

public class ClassCellRenderer  extends JLabel implements ListCellRenderer {
	
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		if (list!=null && value!=null){
			if (value instanceof AODProfileBean){
				AODProfileBean bean = (AODProfileBean) value;
				setText(bean.getName());
			}
			else{
				setText(value.toString());
			}
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
		}
		return this;
	}

}