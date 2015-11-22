package fd;
import gui.ProgressRenderer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;


/**
 * Gets the HTML from a webpage and parses it for images. These images are then downloaded
 * @author User Ashley Wyatt
 *
 */

public class FileDownloader
{
	private DefaultTableModel model;
	private ExecutorService pool;
	private int i;
	
	public FileDownloader(DefaultTableModel inputModel, int threads)
	{
		pool = Executors.newFixedThreadPool(threads);
		model = inputModel;
	}

	
	public void downloadFiles(String folderPath, ArrayList<String> sources)
	{
		for(i = 0; i< sources.size(); i++)
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
				
				final ProgressRenderer progress;
				
				if(sizeB == 0 || sizeB ==-1)
				{
					progress = new ProgressRenderer(0, 100);
					progress.setStringPainted(true);
				}
				else
				{
					progress = new ProgressRenderer(0, sizeB);
					progress.setStringPainted(true);
				}
				
				//get name of file
				String fileName = getFileName(fileUrl).replaceFirst("[.][^.]+$", "");
				
				//get type of file
				String fileType = fileUrl.substring(fileUrl.lastIndexOf(".")+1);
				
				//get full path
				String fullPath = folderPath + "\\" + fileName + "." + fileType;
				
				
				SwingUtilities.invokeLater(new Runnable() {
					public void run()
					{
						model.addRow(new Object[]{fileName, fileType, fileSize, progress});
						model.fireTableDataChanged();
					}
				});
				
				DownloadFile df = new DownloadFile(fileUrl, fullPath, model, progress);
				pool.submit(df);
				
			}
			catch(IOException e)
			{
				System.out.println("Bad URL. Ignored.");
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
