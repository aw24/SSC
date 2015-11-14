package fd;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

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
	private String webpageUrl;
	private String folderPath;
	
	public FileDownloader(String input, String folder)
	{
		webpageUrl = input;
		folderPath = folder;
	}
	
	/**
	 * Fetches the source urls of each image
	 * @return The arraylist of strings containing the source urls for each image
	 * @throws IOException
	 */
	
	public ArrayList<String> fetchImageSources() throws IOException
	{
		Document doc = Jsoup.connect(webpageUrl).get();
		
		//parse html for img elements
		Elements images = doc.getElementsByTag("img");
		
		//get page title
		//String title = doc.title();
		
		//get the urls of the individual images
		ArrayList<String> sources = new ArrayList<String>();
		for(Element image: images)
		{
			String source = image.absUrl("src");
			sources.add(source);
		}
		return sources;
	}
	
	public void fillFolder(ArrayList<String> sources)
	{
		try
		{
			for(int i = 0; i < sources.size(); i++)
			{	
				String imageUrl = sources.get(i);
				URL url = new URL(imageUrl);
				String name = getFileName(imageUrl);
				InputStream in = url.openStream();
				OutputStream out = new BufferedOutputStream(new FileOutputStream(folderPath + name));
				for (int b; (b = in.read()) != -1;) 
				{
					out.write(b);
				}
				out.close();
				in.close();
			}
		}
		catch(IOException e)
		{
			System.out.println("ERROR: " + e.getMessage());
		}	
	}
	
	public String getFileName(String input)
	{
		String url = input;
		System.out.println(url);
		int index = url.lastIndexOf("/");
		if(index == url.length()-1)
		{
			url = url.substring(0, index);
		}
		index = url.lastIndexOf("/");
		url = url.substring(index, url.length());
		return url;
		
	}
	
	public void setUrl(String input)
	{
		webpageUrl = input;
	}

}
