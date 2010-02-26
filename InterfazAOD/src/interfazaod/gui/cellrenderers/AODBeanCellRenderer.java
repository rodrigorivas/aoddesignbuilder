package interfazaod.gui.cellrenderers;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import aodbuilder.beans.aodprofile.*;


public class AODBeanCellRenderer extends JLabel implements ListCellRenderer {
	
	public AODBeanCellRenderer() {
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		if (list != null && value != null) {
			if (value instanceof AODProfileAdvice){
				AODProfileAdvice adv = (AODProfileAdvice)value;
				setText(adv.getType().toString());
			}
			else if (value instanceof AODProfileClass) {
				AODProfileBean bean = (AODProfileBean) value;
				setText(bean.getName());
			} else {
				setText(value.toString());
			}
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
				setFont(list.getFont().deriveFont(Font.BOLD));
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
				setFont(list.getFont());
			}
		}
		return this;
	}

}