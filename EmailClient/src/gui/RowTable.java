package gui;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Flags;
import javax.mail.MessagingException;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * Extension of JTable to colour rows
 * @author https://community.oracle.com/thread/2262939
 *
 */

public class RowTable extends JTable
{
	Map<Integer, Color> rowColor = new HashMap<Integer, Color>();
	
	public RowTable(TableModel model)
	{
		super(model);
	}
	
	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
	{
		Component c = super.prepareRenderer(renderer, row, column);
		if (!isRowSelected(row))
		{
			Color color = rowColor.get( row );
			c.setBackground(color == null ? getBackground() : color);
		}
		return c;
	}
		
	public void setRowColour(int row, Color color)
	{
		rowColor.put(row, color);
	}
}