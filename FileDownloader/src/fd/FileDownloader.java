package fd;
import gui.ProgressRenderer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Gets the HTML from a webpage and parses it for images. These images are then downloaded
 * @author User Ashley Wyatt
 *
 */

public class FileDownloader
{
	private DefaultTableModel model;
	private JTable table;
	private ExecutorService pool;
	
	public FileDownloader(JTable inputTable, DefaultTableModel inputModel, int threads)
	{
		pool = Executors.newFixedThreadPool(threads);
		model = inputModel;
		table = inputTable;
	}

	
	public void downloadFiles(String folderPath, ArrayList<String> sources)
	{
		for(int i = 0; i< sources.size(); i++)
		{
			try
			{
				//get url of file
				String fileUrl = sources.get(i);	
				URL url = new URL(fileUrl);
				
				//get size of file
				int sizeB = url.openConnection().getContentLength();
				final double sizeKB = Math.round((double)((100*sizeB)/1024))/100;
				String fileSize = sizeKB + " KB";
				
				//get name of file
				String fileName = getFileName(fileUrl).replaceFirst("[.][^.]+$", "");
				
				//get type of file
				String fileType = fileUrl.substring(fileUrl.lastIndexOf(".")+1);
				
				//create progress bar
				ProgressRenderer progress = new ProgressRenderer();
				
				//get full path
				String fullPath = folderPath + fileName;
				
				SwingUtilities.invokeLater(new Runnable() {
					public void run()
					{
						model.addRow(new Object[]{fileName, fileType, fileSize,progress});
						model.fireTableDataChanged();
					}
				});
				
				DownloadFile df = new DownloadFile(fileUrl, fileName, fullPath, table, i);
				pool.submit(df);
				
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void shutDown()
	{
		pool.shutdown();
	}
	
	
	/**
	 * Gets the file name from the url
	 * @param input image url
	 * @return extracted file name
	 */
	
	public String getFileName(String input)
	{
		String url = input;
		int index = url.lastIndexOf("/");
		if(index == url.length()-1)
		{
			url = url.substring(0, index);
		}
		index = url.lastIndexOf("/");
		url = url.substring(index+1, url.length());
		return url;
		
	}

}
