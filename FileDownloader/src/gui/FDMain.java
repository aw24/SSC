package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import fd.FileDownloader;

public class FDMain 
{
	public static void main(String args[]) throws IOException
	{
		Scanner in = new Scanner(System.in);
		
		//get the webpage url
		System.out.println("Enter the url of the site which you wish to download images from:");
		String url = in.nextLine();
		
		//get the folder path
		System.out.println("Enter the path of the folder in which you would like the images to be saved:");
		String folder = in.nextLine();
		
		FileDownloader downloader = new FileDownloader(url, folder);
		ArrayList<String> sources = downloader.fetchImageSources();
		downloader.fillFolder(sources);
	}
}
