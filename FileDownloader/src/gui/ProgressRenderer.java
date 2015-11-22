package gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.plaf.ProgressBarUI;
import javax.swing.plaf.metal.MetalProgressBarUI;
import javax.swing.table.TableCellRenderer;

/**
 * Idea from MadProgrammer at http://stackoverflow.com/questions/13753562/adding-progress-bar-to-each-table-cell-for-file-progress-java
 * @author Ashley Wyatt
 *
 */

public class ProgressRenderer extends JProgressBar implements TableCellRenderer
{
	
	public ProgressRenderer(int i, int j) {
		super(i, j);
	}
	
	/**
	 * Formats the colour and text of the progress bar and tells the JTable to render the progress bar
	 */

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) 
	{
		setStringPainted(true);
		
		if(value != null)
		{
			setString(((JProgressBar) value).getString());
			setMinimum(((JProgressBar) value).getMinimum());
			setMaximum(((JProgressBar) value).getMaximum());
			setValue(((JProgressBar) value).getValue());
			
			//if the progress bar is full
			if(getValue() == getMaximum())
			{
				//make green
				setForeground(Color.GREEN);
				setBackground(Color.WHITE);
				setString("Completed");
			} //if progress bar is empty
			else if(getValue() == getMinimum())
			{
				//set text to queued
				setForeground(Color.DARK_GRAY);
				setBackground(Color.WHITE);
				setString("Queued");
			}
			//if progress bar has been set to 1 and is not downloading we now that it has failed
			else if(getValue() == 1)
			{
				Color myRed = new Color(255,0,0);
				setForeground(myRed);
				setBackground(Color.WHITE);
				setString("Failed");
			}
			else //set progress bar to cyan downloading
			{
				setForeground(Color.CYAN);
				setBackground(Color.WHITE);
				setString("Downloading...");
			}
		}
		
		//make the text of the progress bar always black
		ProgressBarUI ui = new MetalProgressBarUI()
		{
			@Override
			protected Color getSelectionForeground()
			{
				return Color.BLACK;
			}
			
			@Override
			protected Color getSelectionBackground()
			{
				return Color.BLACK;
			}
		};
		setUI(ui);
		repaint();
		revalidate();
		
		return this;
	}

}
