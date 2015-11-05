package database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;

import exception.JavaCheckException;

public class UniversityDatabaseModel extends Observable
{
	private UniversityDatabase database;

	public UniversityDatabaseModel(UniversityDatabase database)
	{
		super();
		this.database = database;
	}
	
	public void registerStudent(String[] input) throws SQLException, IllegalArgumentException, JavaCheckException	
	{
		database.registerStudent(input);
	}
	
	public void addTutor(String[] input) throws SQLException, NumberFormatException, JavaCheckException	
	{
		database.addTutor(input);
	}
	
	public String[] studentReport(String input) throws SQLException, NumberFormatException, JavaCheckException
	{
		return database.studentReport(input);
	}
	
	public ArrayList<String[]> lecturerReport(String input) throws SQLException, NumberFormatException, JavaCheckException
	{
		return database.lecturerReport(input);
	}
}
