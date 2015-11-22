package fd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * The class responsible for parsing the HTML and getting the url links. Also takes into account the filter when getting the urls.
 * @author Ashley Wyatt
 *
 */

public class FileUrls 
{
	
	private String webpageUrl;
	private ArrayList<String> allowedExtensions;
	
	public FileUrls(String url, ArrayList<String> fileTypes)
	{
		webpageUrl = url;
		allowedExtensions = fileTypes;
	}
	
	/**
	 * Fetches the source urls of each image
	 * @return The arraylist of strings containing the source urls for each image
	 * @throws IOException
	 */
	
	public ArrayList<String> fetchImageSources() throws IOException
	{
		//get the html
		Document doc = Jsoup.connect(webpageUrl).get();
		
		//set up regex
		String fileExtensions = extensionRegex();
		System.out.println(fileExtensions);
		
		//parse html for link elements
		Elements images = doc.select("img[src~=(?i)\\.(" + fileExtensions + ")]");
		Elements files = doc.select("a[href~=(?i)\\.(" + fileExtensions + ")]");
		
		//get the urls of the individual images
		ArrayList<String> sources = new ArrayList<String>();
		
		System.out.println(webpageUrl);
		
		//for each file url
		for(Element file: files)
		{
			String source = file.attr("href");
			System.out.println(source);
			
			//if it does not contain http then add the file name onto the url
			if(source.indexOf("http://")==-1)
			{
				if(!source.contains(webpageUrl))
				{
					if(webpageUrl.charAt(webpageUrl.length()-1) != '/' && source.charAt(0) != '/')//if no separator
					{
						source = webpageUrl + "/" +  source;
					}
					else
					{
						source = webpageUrl + source;
					}
				}
			}
			//add sources of files which are of the specified file type
			sources.add(source);
		}
		
		//for each image url
		for (Element image : images) 
		{
			String source = image.attr("src");
			//if it does not contain http then add the file name onto the url
			if(source.indexOf("http://")==-1)
			{
				if(webpageUrl.charAt(webpageUrl.length()-1) != '/' && source.charAt(0) != '/')//if no separator
				{
					source = webpageUrl + "/" +  source;
				}
				else
				{
					source = webpageUrl + source;
				}
			}
			//add sources of files which are of the specified file type
			sources.add(source);
		}
		
		// remove duplicates from sources
		HashSet<String> tempSet = new HashSet<>();
		tempSet.addAll(sources);
		sources.clear();
		sources.addAll(tempSet);
		
		return sources;
		
	}
	
	/**
	 * Adds the user chosen file extensions to the regex
	 * @return a string containing the regex to be inserted
	 */
	
	public String extensionRegex()
	{
		String acc = "";
		for(int i = 0; i < allowedExtensions.size(); i++)
		{
			if(i ==0)
			{
				acc += allowedExtensions.get(i);
			}
			else
			{
				acc += "|" + allowedExtensions.get(i);
			}
		}
		return acc;
	}

	
}
