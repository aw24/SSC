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
 * Iteratively creates a runnable for a url in order to download it into a specific folder
 * @author Ashley Wyatt
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

	/**
	 * Iterates through all url and attempts to downloads the files
	 * @param folderPath The path of the destination folder
	 * @param sources ArrayList of the urls
	 */
	
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
				
				//boolean which is passed into DownloadFile so we know whether the size is valid and so if the progress bar should be incremented or not
				boolean increment = true;
				
				final ProgressRenderer progress;
				
				//account for unknown or empty files
				if(sizeB == 0 || sizeB ==-1)
				{
					progress = new ProgressRenderer(0, 100);
					increment = false;
				}
				else
				{
					progress = new ProgressRenderer(0, sizeB);
				}
				
				//get name of file
				String markRemovedUrl = removeQuestionMark(fileUrl);
				String fileName = getFileName(markRemovedUrl).replaceFirst("[.][^.]+$", "");
				
				//get type of file
				String fileType = markRemovedUrl.substring(markRemovedUrl.lastIndexOf(".")+1);
				
				//get full path
				String fullPath = folderPath + "\\" + fileName + "." + fileType;
				
				//add the download into the JTable
				SwingUtilities.invokeLater(new Runnable() {
					public void run()
					{
						model.addRow(new Object[]{fileName, fileType, fileSize, progress});
						model.fireTableDataChanged();
					}
				});
				
				//write the file
				DownloadFile df = new DownloadFile(fileUrl, fullPath, model, progress, increment);
				pool.submit(df);
				
			}
			catch(IOException e)
			{
				e.printStackTrace();
				System.out.println("Bad URL. Ignored.");
			}
		}
	}
	
	//shut down the executor service pool
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
		//if there is a slash at the end then remove it
		if(index == url.length()-1)
		{
			url = url.substring(0, index);
		}
		//find the last slash
		index = url.lastIndexOf("/");
		//get the bit after the slash and return it
		url = url.substring(index+1, url.length());
		return url;
		
	}
	
	/**
	 * Removes the question mark and anything that follows from extensions
	 * @param s
	 * @return The input string but with the things removed
	 */
	
	public String removeQuestionMark(String s)
	{
		int index = s.lastIndexOf("?");
		if(index != -1)
		{
			s = s.substring(0, index);
		}
		return s;
	}

}
