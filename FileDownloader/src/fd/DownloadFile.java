package fd;

import gui.ProgressRenderer;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

public class DownloadFile implements Runnable
{
	private String fileUrl;
	private String path;	
	private String fileName;
	private JTable table;
	private int number;
	
	public DownloadFile(String url, String name, String fullPath, JTable inputTable, int num)
	{
		path = fullPath;
		fileName = name;
		fileUrl = url;
		table = inputTable;
		number = num;
	}
	
	/**
	 * Fills the specified folder with the downloaded images/files
	 * @param sources The list of image urls
	 */
	
	public void run()
	{
		InputStream in = null;
		OutputStream out = null;
		
		try
		{
			//convert string to url
			URL url = new URL(fileUrl);
	
			//setup i/o streams
			in = url.openStream();
			out = new BufferedOutputStream(new FileOutputStream(path));
			
			//write to file
			for (int b; (b = in.read()) != -1;) 
			{
				out.write(b);
				SwingUtilities.invokeLater(new Runnable(){
					public void run()
					{
						ProgressRenderer progressBar = (ProgressRenderer) table.getModel().getValueAt(number, 3);
						int currentProgress = progressBar.getValue();
						progressBar.setValue(currentProgress + 1);
						table.getModel().setValueAt(progressBar, number, 3);
						table.repaint();
						table.revalidate();
					}
				});
			}
			
		}
		catch(IOException e)
		{
			System.out.println("Failed to download " + fileUrl);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(in!=null)
				{
					in.close();
				}
				if(out!=null)
				{
					out.close();
				}
			}
			catch(IOException e)
			{
				System.out.println("Failed to download " + fileUrl);
			}
		}
		
	}
	
}
