package database;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.sql.Date;

import exception.JavaCheckException;

public class UniversityDatabase 
{
	private Tables tables;
	
	/**
	 * Create and populate the database
	 */
	
	public UniversityDatabase()
	{		
		String[] tableHeadings = {
				"Titles",
				"Student",
				"Lecturer",
				"RegistrationType",
				"StudentRegistration",
				"StudentContact",
				"NextOfKinContact",
				"LecturerContact",
				"Tutor"
		};
		
		tables = new Tables();
		
		System.out.println("Deleting tables...");
		
		//delete any current tables
		tables.deleteTables(tableHeadings);
		System.out.println("Tables deleted.");
		
		System.out.println("Creating tables...");
		
		//create tables
		tables.createTables(tableHeadings);
		System.out.println("Tables created");
		
		System.out.println("Populating tables...");
		
		//populate tables
		tables.populateTablesDummyData();
		System.out.println("Tables populated with dummy data.");
		tables.populateTable();
		System.out.println("Tables populated with actual data.");

	}
	
	public String removeSpaces(String input)
	{
		return input.trim().replaceAll(" +", " ");
	}
	
	/**
	 * Inputs details for a new student
	 * @param input Contains the details of the student
	 * @throws SQLException
	 * @throws NumberFormatException
	 */
	
	public void registerStudent(String[] input) throws SQLException, JavaCheckException
	{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try
		{	
			dbConn = tables.connect();
			
			dbConn.setAutoCommit(false);
			int studentID;
			try 
			{
				studentID = Integer.parseInt(input[0]);
			} 
			catch(NumberFormatException e)
			{
				throw new JavaCheckException("Student ID must be a number.");
			}
			
			if(studentID < 0)
			{
				throw new JavaCheckException("Student ID cannot be negative.");
			}
			
			String titleString = input[1];
			int titleID;
			stmt = dbConn.prepareStatement("SELECT titleID FROM Titles WHERE titleString = ?");
			stmt.setString(1,  titleString);
			rs = stmt.executeQuery();
			if (!rs.next()) {
				throw new JavaCheckException("This title does not exist in the database");
			} 
			else
			{
				titleID = rs.getInt(1);
			}
			String foreName = input[2];
			String familyName = input[3];
			String date = input[4];
			Date dateOfBirth = Date.valueOf(date);
			int yearOfStudy;
			try 
			{
				yearOfStudy = Integer.parseInt(input[5]);
			} 
			catch(NumberFormatException e)
			{
				throw new JavaCheckException("Year of Study must be a number.");
			};
			
			int registrationTypeID;
			try 
			{
				registrationTypeID = Integer.parseInt(input[6]);
			} 
			catch(NumberFormatException e)
			{
				throw new JavaCheckException("Registration Type ID must be a number.");
			}
			
			String eMailAddress = input[7];
			String postalAddress = input[8];
			String nextOfKinName = input[9];
			String nextOfKinEMailAddress = input[10];
			String nextOfKinPostalAddress = input[11];

			rs.close();
			stmt.close();
	
			stmt = dbConn.prepareStatement("INSERT INTO Student VALUES (?, ?, ?, ?, ?);");
			stmt.setInt(1, studentID);
			stmt.setInt(2, titleID);
			stmt.setString(3, foreName);
			stmt.setString(4, familyName);
			stmt.setDate(5, dateOfBirth);
			stmt.executeUpdate();
	
	
			stmt = dbConn.prepareStatement("INSERT INTO StudentRegistration VALUES (?, ?, ?);");
			stmt.setInt(1, studentID);
			stmt.setInt(2, yearOfStudy);
			stmt.setInt(3, registrationTypeID);
			stmt.executeUpdate();
	
	
			stmt = dbConn.prepareStatement("INSERT INTO StudentContact VALUES (?, ?, ?);");
			stmt.setInt(1, studentID);
			stmt.setString(2, eMailAddress);
			stmt.setString(3, postalAddress);
			stmt.executeUpdate();
	
	
			stmt = dbConn.prepareStatement("INSERT INTO NextOfKinContact VALUES (?, ?, ?, ?);");
			stmt.setInt(1, studentID);
			stmt.setString(2, nextOfKinName);
			stmt.setString(3, nextOfKinEMailAddress);
			stmt.setString(4, nextOfKinPostalAddress);
			stmt.executeUpdate();
			
			dbConn.commit();
			
			JOptionPane.showMessageDialog(null, "Student successfully registered.", "Message", JOptionPane.INFORMATION_MESSAGE);
			
		}
		catch(SQLException | NumberFormatException se)
		{
			dbConn.rollback();
			dbConn.setAutoCommit(true);
			throw se;
		}
		catch(IllegalArgumentException e)
		{
			dbConn.rollback();
			dbConn.setAutoCommit(true);
			throw new JavaCheckException("Field is of the wrong type");
		}
		finally
		{
			if(rs!=null)
			{
				rs.close();
			}
			if(stmt!=null)
			{
				stmt.close();
			}
			if(dbConn!=null)
			{
				dbConn.close();
			}
		}
	}
	
	/**
	 * Assigns a student to a tutor
	 * @param input Array containing the student id and the lecturer id
	 * @throws SQLException
	 * @throws NumberFormatException
	 * @throws JavaCheckException
	 */
	
	public void addTutor(String[] input) throws SQLException, NumberFormatException, JavaCheckException
	{	
		Connection dbConn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try
		{
			dbConn = tables.connect();
			//get student id
			int studentID;
			try 
			{
				studentID = Integer.parseInt(input[0]);
			} 
			catch(NumberFormatException e)
			{
				throw new JavaCheckException("Student ID must be a number.");
			}
			
			if(studentID < 0)
			{
				throw new JavaCheckException("Student ID cannot be negative.");
			}
			
			stmt = dbConn.prepareStatement("SELECT studentID FROM Student WHERE studentID = ?;");
			stmt.setInt(1, studentID);
			rs = stmt.executeQuery();
			if (!rs.next()) {
				throw new JavaCheckException("This student does not exist in the database");
			} 
			else
			{
				studentID = rs.getInt(1);
			}
			
			rs.close();
			stmt.close();
			
			int lecturerID;
			
			try 
			{
				lecturerID = Integer.parseInt(input[1]);
			} 
			catch(NumberFormatException e)
			{
				throw new JavaCheckException("Lecturer ID must be a number.");
			}
			
			if(lecturerID < 0)
			{
				throw new JavaCheckException("Lecturer ID cannot be negative.");
			}
			
			stmt = dbConn.prepareStatement("SELECT lecturerID FROM Lecturer WHERE lecturerID = ?;");
			stmt.setInt(1, lecturerID);
			rs = stmt.executeQuery();
			if (!rs.next()) 
			{
				throw new JavaCheckException("This lecturer does not exist in the database");
			} 
			else
			{
				lecturerID = rs.getInt(1);
			}
			
			rs.close();
			stmt.close();
			
			stmt = dbConn.prepareStatement("INSERT INTO Tutor VALUES (?, ?);");
			stmt.setInt(1, studentID);
			stmt.setInt(2, lecturerID);
			stmt.executeUpdate();

			JOptionPane.showMessageDialog(null, "Tutor successfully assigned.", "Message", JOptionPane.INFORMATION_MESSAGE);
		}
		catch(SQLException e)
		{
			throw e;
		}
		finally
		{
			if(rs!=null)
			{
				rs.close();
			}
			if(stmt!=null)
			{
				stmt.close();
			}
			if(dbConn!=null)
			{
				dbConn.close();
			}
		}
	}
	
	/**
	 * Collects data to form a student report
	 * @param input The student ID of the student
	 * @return An array containing all of the student's data
	 * @throws SQLException
	 * @throws NumberFormatException
	 * @throws JavaCheckException
	 */
	
	public String[] studentReport(String input) throws SQLException, NumberFormatException, JavaCheckException 
	{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try
		{
			dbConn = tables.connect();
			//get student id
			int studentID;;
			try 
			{
				studentID = Integer.parseInt(input);
			} //check if a number
			catch(NumberFormatException e)
			{
				throw new JavaCheckException("Student ID must be a number.");
			}
			
			//check if negative
			if(studentID < 0)
			{
				throw new JavaCheckException("Student ID cannot be negative.");
			}
			
			stmt = dbConn.prepareStatement("SELECT studentID FROM Student WHERE studentID = ?;");
			stmt.setInt(1, studentID);
			rs = stmt.executeQuery();
			if (!rs.next()) {
				throw new JavaCheckException("This student does not exist in the database");
			} 
			else
			{
				studentID = rs.getInt(1);
			}
			
			rs.close();
			stmt.close();
			
			//get student title
			stmt = dbConn.prepareStatement("SELECT titleString FROM Titles WHERE titleID = (SELECT titleID FROM Student WHERE studentID = ?);");
			stmt.setInt(1, studentID);
			rs = stmt.executeQuery();
			rs.next();
			String studentTitle = removeSpaces(rs.getString(1));
			
			//get student first name and family name
			stmt = dbConn.prepareStatement("SELECT foreName, familyName, dateOfBirth FROM Student WHERE studentID = ?;");
			stmt.setInt(1, studentID);
			rs = stmt.executeQuery();
			rs.next();
			String foreName = removeSpaces(rs.getString(1));
			String familyName = removeSpaces(rs.getString(2));
			String dateOfBirth = removeSpaces(rs.getString(3));
			
			//get year of study and registration type
			stmt = dbConn.prepareStatement("SELECT yearOfStudy, description FROM StudentRegistration, RegistrationType" + 
											" WHERE StudentRegistration.studentID = ? AND StudentRegistration.registrationTypeID = RegistrationType.RegistrationTypeID;");
			stmt.setInt(1, studentID);
			rs = stmt.executeQuery();
			rs.next();
			String yearOfStudy = removeSpaces(rs.getString(1));
			String registrationType = removeSpaces(rs.getString(2));
			
			//get email address and postal address
			stmt = dbConn.prepareStatement("SELECT eMailAddress, postalAddress FROM StudentContact" + 
					" WHERE studentID = ?;");
			stmt.setInt(1, studentID);
			rs = stmt.executeQuery();
			rs.next();
			String eMailAddress = removeSpaces(rs.getString(1));
			String postalAddress = removeSpaces(rs.getString(2));
			
			//get personal tutor
			String personalTutor;
			stmt = dbConn.prepareStatement("SELECT foreName, familyName FROM Lecturer WHERE lecturerID in (SELECT lecturerID FROM Tutor WHERE studentID = ?)");
			stmt.setInt(1, studentID);
			rs = stmt.executeQuery();
			if (!rs.next()) 
			{
				personalTutor = "No personal tutor";
			} 
			else
			{
				personalTutor = removeSpaces(rs.getString(1)) + " " + removeSpaces(rs.getString(2));
			}
			
			//get emergency contact name, email address and postal address
			stmt = dbConn.prepareStatement("SELECT name, eMailAddress, postalAddress FROM NextOfKinContact" + 
					" WHERE studentID = ? ;");
			stmt.setInt(1, studentID);
			rs = stmt.executeQuery();
			rs.next();
			String contactName = removeSpaces(rs.getString(1));
			String contactEMail = removeSpaces(rs.getString(2));
			String contactPostal = removeSpaces(rs.getString(3));
			
			String[] output = {studentTitle, foreName, familyName, dateOfBirth, input, yearOfStudy, registrationType, eMailAddress, postalAddress, personalTutor, contactName, contactEMail, contactPostal};
			return output;
		}
		catch(SQLException e)
		{
			throw e;
		}
		finally
		{
			if(rs!=null)
			{
				rs.close();
			}
			if(stmt!=null)
			{
				stmt.close();
			}
			if(dbConn!=null)
			{
				dbConn.close();
			}
		}
	}
	
	/**
	 * Collects the student reports of all the tutees of a certain lecturer
	 * @param input The lecturer's id
	 * @return An ArrayList containing an arrays of data for each student 
	 * @throws SQLException
	 * @throws NumberFormatException
	 * @throws JavaCheckException
	 */
	
	public ArrayList<String[]> lecturerReport(String input) throws SQLException, NumberFormatException, JavaCheckException
	{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try
		{
			dbConn = tables.connect();
			//get lecturer id
			int lecturerID;
			
			try 
			{
				lecturerID = Integer.parseInt(input);
			} 
			catch(NumberFormatException e)
			{
				throw new JavaCheckException("Lecturer ID must be a number.");
			}
			
			if(lecturerID < 0)
			{
				throw new JavaCheckException("The lecturer ID cannot be negative.");
			}
			
			stmt = dbConn.prepareStatement("SELECT lecturerID FROM Lecturer WHERE lecturerID = ?;");
			stmt.setInt(1, lecturerID);
			rs = stmt.executeQuery();
			if (!rs.next()) {
				throw new JavaCheckException("This lecturer does not exist in the database");
			} 
			else
			{
				lecturerID = rs.getInt(1);
			}
			
			rs.close();
			stmt.close();
			//get tutee ids
			stmt = dbConn.prepareStatement("SELECT studentID FROM Tutor WHERE lecturerID = ?;");
			stmt.setInt(1, lecturerID);
			rs = stmt.executeQuery();
			ArrayList<String> studentIDs = new ArrayList<String>();
			
			while(rs.next())
			{
				studentIDs.add(Integer.toString(rs.getInt(1)));
			}
			
			ArrayList<String[]> tuteeReports = new ArrayList<String[]>();
			
			for(int j = 0; j < studentIDs.size(); j++)
			{
				tuteeReports.add(studentReport(studentIDs.get(j)));
			}
			if (tuteeReports.size()==0 ) 
			{
			    throw new JavaCheckException("This lecturer either does not exist, or has no tutees.");
			}
			return tuteeReports;
		}
		catch(SQLException e)
		{
			throw e;
		}
		finally
		{
			if(rs!=null)
			{
				rs.close();
			}
			if(stmt!=null)
			{
				stmt.close();
			}
			if(dbConn!=null)
			{
				dbConn.close();
			}
		}
	}
}
