package fd;

import gui.ProgressRenderer;


import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 * A runnable which is created for each file is that to be written
 * @author Ashley Wyatt
 *
 */

public class DownloadFile implements Runnable
{
	private String fileUrl;
	private String path;	
	private ProgressRenderer progressBar;
	private DefaultTableModel model;
	private boolean increment;
	
	public DownloadFile(String url, String fullPath, DefaultTableModel tmodel, ProgressRenderer progress, boolean increment)
	{
		path = fullPath;
		fileUrl = url;
		progressBar = progress;
		model = tmodel;
		this.increment = increment;
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
			
			//print current thread
			System.out.println(Thread.currentThread().getName());
			
			//write to file
			for (int b; (b = in.read()) != -1;) 
			{
				out.write(b);
				SwingUtilities.invokeLater(new Runnable(){
					public void run()
					{
						//increments the progress bar after each byte is written
						if(increment)
						{
							int currentProgress = progressBar.getValue();
							progressBar.setValue(currentProgress + 1);
							progressBar.repaint();
							progressBar.revalidate();
							model.fireTableDataChanged();
						}
					}
				});
			}
			
			//if the file writing is complete then can just set the progress bar to its max value
			//this is done as a precaution to account for any rounded file sizes that may cause a problem
			SwingUtilities.invokeLater(new Runnable(){
				public void run()
				{
					progressBar.setValue(progressBar.getMaximum());
				}
			});
			
		}
		catch(IOException e)
		{
			//if it is a bad link then set the status to 'Failed' and print out which url failed
			System.out.println("Failed to download " + fileUrl);
			progressBar.setValue(1);
			progressBar.repaint();
			progressBar.revalidate();
			model.fireTableDataChanged();
		}
		finally
		{
			//close the streams
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
				System.out.println("Failed to close streams after: " + fileUrl);
			}
		}
		
	}
	
}
