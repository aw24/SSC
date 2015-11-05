package gui;
import javax.swing.JPanel;

import database.UniversityDatabase;
import database.UniversityDatabaseModel;

import java.awt.BorderLayout;

public class DatabaseComponent extends JPanel
{
	public DatabaseComponent(UniversityDatabase database)
	{
		UniversityDatabaseModel model = new UniversityDatabaseModel(database);
		
		ButtonPanel buttons = new ButtonPanel(model);
		
		setLayout(new BorderLayout());
		add(buttons, BorderLayout.CENTER);
	}
}
