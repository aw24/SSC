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

@SuppressWarnings("serial")
public class ProgressRenderer extends JProgressBar implements TableCellRenderer
{
	
	public ProgressRenderer(int i, int j) {
		super(i, j);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) 
	{
		if(value != null)
		{
			setString(((JProgressBar) value).getString());
			setMinimum(((JProgressBar) value).getMinimum());
			setMaximum(((JProgressBar) value).getMaximum());
			setValue(((JProgressBar) value).getValue());
		}
		return this;
	}

}
