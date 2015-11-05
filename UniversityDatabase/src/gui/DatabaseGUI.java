package gui;
import javax.swing.*;

import database.UniversityDatabase;

public class DatabaseGUI
{
	private static final int WIDTH = 800;
	private static final int HEIGHT = 200;
	
	public static void main(String[] args)
	{
		DatabaseGUI gui = new DatabaseGUI();
		JFrame frame = new JFrame("University Database");

		frame.setSize(WIDTH,HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		UniversityDatabase database = new UniversityDatabase();
		DatabaseComponent comp = new DatabaseComponent(database);
		frame.add(comp);
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);
		gui.alterLook();
		

	}


	private void alterLook()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			System.err.println("Failed to alter GUI");
		}
	}

}
