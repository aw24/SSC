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
		
		//How many file types would you like to download?
		System.out.println("Enter the number of file types to be downloaded:");
		int count = in.nextInt();
		
		ArrayList<String> folderTypes = new ArrayList<String>();
		for(int i = 0; i < count; i++)
		{
			System.out.println("Enter file type:");
			folderTypes.add(in.nextLine());
			
		}
		
		FileDownloader downloader = new FileDownloader(url, folder, folderTypes);
		ArrayList<String> sources = downloader.fetchImageSources();
		downloader.fillFolder(sources);
	}
}
