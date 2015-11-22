package fd;

import gui.ProgressRenderer;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class DownloadFile implements Runnable
{
	private String fileUrl;
	private String path;	
	private ProgressRenderer progressBar;
	private DefaultTableModel model;
	
	public DownloadFile(String url, String fullPath, DefaultTableModel tmodel, ProgressRenderer progress)
	{
		path = fullPath;
		fileUrl = url;
		progressBar = progress;
		model = tmodel;
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
						int currentProgress = progressBar.getValue();
						progressBar.setValue(currentProgress + 1);
						progressBar.setString("Downloading...");
						progressBar.repaint();
						progressBar.revalidate();
						model.fireTableDataChanged();
					}
				});
			}
			
		}
		catch(IOException e)
		{
			System.out.println("Failed to download " + fileUrl);
			progressBar.setString("Failed");
			progressBar.repaint();
			progressBar.revalidate();
			model.fireTableDataChanged();
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
				progressBar.setString("Failed");
				progressBar.repaint();
				progressBar.revalidate();
				model.fireTableDataChanged();
			}
		}
		
	}
	
}
