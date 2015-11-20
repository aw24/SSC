package gui;

import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Idea from http://stackoverflow.com/questions/13753562/adding-progress-bar-to-each-table-cell-for-file-progress-java
 * @author MadProgrammer
 *
 */

public class ProgressRenderer extends JProgressBar implements TableCellRenderer
{

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) 
	{
		int progress = 0;
		if(value instanceof Float)
		{
			progress = Math.round(((Float) value) *100f);
		}
		else if (value instanceof Integer)
		{
			progress = (int) value;
		}
		setValue(progress);
		return this;
	}

}
