package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import javax.swing.JOptionPane;

public class Tables 
{
	//set number of dummy students
	private int students = 100;
		
	/**
	 * Connects to the database
	 * @return Returns the connection to the database
	 */
	
	public Connection connect()
	{
		Connection dbConn = null;
		try
		{
			System.setProperty("jdbc.drivers","org.postgresql.Driver");
			dbConn = DriverManager.getConnection("jdbc:postgresql://dbteach2.cs.bham.ac.uk/amw466", "amw466", "rothitha");
		}
		catch(SQLException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage(), "Message", JOptionPane.INFORMATION_MESSAGE);
			e.printStackTrace();
		}
		return dbConn;
	}
	
	/**
	 * Creates the tables in the database
	 * @param tableHeadings Contains the heading for each table to be created
	 */
	
	public void createTables(String[] tableHeadings)
	{
		String[] tableContent = new String[9];
		
		//Titles
		tableContent[0] = 
				"titleID INTEGER, " + 
				"titleString CHAR(60) UNIQUE NOT NULL, " + 
				"PRIMARY KEY(titleID), " + 
				"CHECK(titleID > 0 AND titleID <=5)"
				;
		
		//Student
		tableContent[1] = 
				"studentID INTEGER, " + 
				"titleID INTEGER NOT NULL, " + 
				"foreName CHAR(60) NOT NULL, " + 
				"familyName CHAR(60), " +
				"dateOfBirth DATE NOT NULL, " +
				"PRIMARY KEY(studentID), " +
				"FOREIGN KEY(titleID) REFERENCES Titles(titleID) ON DELETE RESTRICT ON UPDATE RESTRICT," +
				"CHECK(studentID > 0 AND titleID > 0 AND titleID <= 5)"
				;
		
		//Lecturer
		tableContent[2] = 
				"lecturerID INTEGER, " + 
				"titleID INTEGER NOT NULL, " + 
				"foreName CHAR(60) NOT NULL, " + 
				"familyName CHAR(60), " + 
				"PRIMARY KEY(lecturerID), " + 
				"FOREIGN KEY (titleID) REFERENCES Titles(titleID) ON DELETE RESTRICT ON UPDATE RESTRICT," +
				"CHECK(lecturerID > 0 AND titleID > 0 AND titleID <=5)"
				;
		
		//RegistrationType
		tableContent[3] = 
				"registrationTypeID INTEGER, " + 
				"description CHAR(60) UNIQUE NOT NULL, " +
				"PRIMARY KEY(registrationTypeID), " +
				"CHECK(registrationTypeID >= 1 AND registrationTypeID <= 3)"
				;
		
		//StudentRegistration
		tableContent[4] = 
				"studentID INTEGER UNIQUE NOT NULL, " + 
				"yearOfStudy INTEGER NOT NULL, " + 
				"registrationTypeID INTEGER NOT NULL, " + 
				"FOREIGN KEY(studentID) REFERENCES Student(studentID) ON DELETE CASCADE ON UPDATE CASCADE,"  +
				"FOREIGN KEY (registrationTypeID) REFERENCES RegistrationType(registrationTypeID) ON DELETE RESTRICT ON UPDATE RESTRICT," +
				"CHECK(studentID > 0 AND yearOfStudy >= 1 AND yearOfStudy <= 5 AND registrationTypeID >= 1 AND registrationTypeID <=3)"
				;
		
		//StudentContact
		tableContent[5] =
				"studentID INTEGER UNIQUE NOT NULL, " + 
				"eMailAddress CHAR(60) UNIQUE NOT NULL, " +
				"postalAddress CHAR(60) NOT NULL, " +
				"FOREIGN KEY(studentID) REFERENCES Student(studentID) ON DELETE CASCADE ON UPDATE CASCADE," +
				"CHECK(studentID > 0 AND eMailAddress LIKE '_%@_%')" 
				;
			
		//NextOfKinContact
		tableContent[6] = 		
				"studentID INTEGER UNIQUE NOT NULL, " + 
				"name CHAR(60) NOT NULL, " +
				"eMailAddress CHAR(60) NOT NULL, " +
				"postalAddress CHAR(60) NOT NULL, " + 
				"FOREIGN KEY (studentID) REFERENCES Student(studentID) ON DELETE CASCADE ON UPDATE CASCADE," +
				"CHECK(studentID > 0 AND eMailAddress LIKE '_%@_%')" 
				;
		
		//LecturerContact
		tableContent[7] = 
				"lecturerID INTEGER UNIQUE NOT NULL, " + 
				"office CHAR(10) NOT NULL, " + 
				"eMailAddress CHAR(60) UNIQUE NOT NULL, " + 
				"FOREIGN KEY (lecturerID) REFERENCES Lecturer(lecturerID) ON DELETE CASCADE ON UPDATE CASCADE," +
				"CHECK(lecturerID > 0 AND eMailAddress LIKE '_%@_%')" 
				;
		
		//Tutor
		tableContent[8] = 
				"studentID INTEGER UNIQUE NOT NULL, " + 
				"lecturerID INTEGER NOT NULL, " + 
				"FOREIGN KEY (studentID) REFERENCES Student(studentID) ON DELETE CASCADE ON UPDATE CASCADE," +
				"FOREIGN KEY (lecturerID) REFERENCES Lecturer(lecturerID) ON DELETE CASCADE ON UPDATE CASCADE," +
				"CHECK(studentID > 0 AND lecturerID > 0)"
				;

		
		//create tables with constraints
		
		Connection dbConn = null;
		Statement stmt = null;
		
		try
		{			
			dbConn = connect();
			stmt = dbConn.createStatement();
			
			for(int i =0; i < tableHeadings.length; i++)
			{
				stmt.executeUpdate("CREATE TABLE " + tableHeadings[i] + " (" + tableContent[i] + ");");
			}
			
		} 
		catch (SQLException e) 
		{
			JOptionPane.showMessageDialog(null, e.getMessage(), "Message", JOptionPane.INFORMATION_MESSAGE);
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(stmt!=null)
				{
					stmt.close();
				}
				if(dbConn!=null)
				{
					dbConn.close();
				}
			} 
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Deletes any tables that might cause conflicts with the ones about to be created
	 * @param tableHeadings the headings of the tables which are going to be created
	 */
	
	public void deleteTables(String[] tableHeadings)
	{		
		Connection dbConn = null;
		Statement stmt = null;
		try 
		{
			dbConn = connect();
			stmt = dbConn.createStatement();
			for(int i =0; i < tableHeadings.length; i++)
			{
				stmt.executeUpdate("DROP TABLE IF EXISTS " + tableHeadings[i] + " CASCADE");
			}
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(stmt!=null)
				{
					stmt.close();
				}
				if(dbConn!=null)
				{
					dbConn.close();
				}
			} 
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}	
	
	/**
	 * Populates the database with synthetic values
	 */
	
	public void populateTablesDummyData()
	{		
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try
		{
			dbConn = connect();
			System.out.println("Populating titles...");
			
			//populate titles
			int titleID;
			String[] titleString = {
					"Mr",
					"Miss",
					"Mrs",
					"Ms",
					"Dr"		
			};
	
			for(int k =1; k < titleString.length+1; k++)
			{
				stmt = dbConn.prepareStatement("INSERT INTO Titles VALUES (?, ?);");
				stmt.setInt(1, k);
				stmt.setString(2, titleString[k-1]);
				stmt.executeUpdate();
			}
				
	
			
			System.out.println("Populating students...");
			
			for(int j = 1; j <= 200; j++)
			{
				//calculate student id
				int studentID = (1400000 + (j*3));
				
				//calculate title id
				Random rand = new Random();
				int randomNum = rand.nextInt((2 - 1) + 1) + 1;
				if(randomNum == 1)
				{
					titleID = 1;
				}
				else
				{
					titleID = 2; //assume all girl students have title 'Miss'
				}
				
				//create student forename
				String foreName = "Student" + j;
				
				//create familyName
				String familyName = "FamilyName" + j;
				
				//generate date
				int day = rand.nextInt((27 - 10) + 1) + 10;
				int month = rand.nextInt((12 - 1) + 1) + 1;
				int year = 1997 - (rand.nextInt((5 - 1) + 1) + 1);
				String dateOfBirth = year + "-" + month + "-" + day;
				Date date = new Date(0);
				date = Date.valueOf(dateOfBirth);
					
		
				stmt = dbConn.prepareStatement("INSERT INTO Student VALUES (?, ?, ?, ?, ?);");
				stmt.setInt(1, studentID);
				stmt.setInt(2, titleID);
				stmt.setString(3, foreName);
				stmt.setString(4, familyName);
				stmt.setDate(5, date);
				stmt.executeUpdate();
			}
	
			
			System.out.println("Populating lecturers...");
			
			//populate lecturers
			for(int l = 1; l <= 15; l++)
			{
				//calculate lecturer id
				int lecturerID = l;
				
				//calculate title id
				Random rand = new Random();
				int randomNum = rand.nextInt((2 - 1) + 1) + 1;
				if(randomNum == 1)
				{
					titleID = 1;
					randomNum = rand.nextInt((2 - 1) + 1) + 1;
					if(randomNum ==1)
					{
						titleID =1;
					}
					else
					{
						titleID = 5;	
					}
				}
				else
				{
					randomNum = rand.nextInt((5 - 2) + 1) + 2;
					titleID = randomNum; //assume all girl students have title 'Miss'
				}
				
				//create student forename
				String foreName = "Lecturer" + l;
				
				//create familyName
				String familyName = "FamilyName" + l;
				
				
				
				stmt = dbConn.prepareStatement("INSERT INTO Lecturer VALUES (?, ?, ?, ?);");
				stmt.setInt(1, lecturerID);
				stmt.setInt(2, titleID);
				stmt.setString(3, foreName);
				stmt.setString(4, familyName);
				stmt.executeUpdate();
				
			}	
			
			System.out.println("Populating registration type...");
			
			//populate registration type
			String[] description = {
					"normal",
					"repeat",
					"external"	
			};
			
			for(int m =1; m < description.length+1; m++)
			{
				stmt = dbConn.prepareStatement("INSERT INTO RegistrationType VALUES (?, ?);");
				stmt.setInt(1, m);
				stmt.setString(2, description[m-1]);
				stmt.executeUpdate();
			}
				
	
			
			System.out.println("Populating student registration...");
			
			//populate student registration
			for(int j = 1; j <= students; j++)
			{
	
				//calculate student id
				int studentID = (1400000 + (j*3));
				
				//generate year of study
				Random rand = new Random();
				int yearOfStudy = rand.nextInt((5 - 1) + 1) + 1;
				
				//generate registration type id
				int registrationTypeID = rand.nextInt((3 - 1) + 1) + 1;
				
				stmt = dbConn.prepareStatement("INSERT INTO StudentRegistration VALUES (?, ?, ?);");
				stmt.setInt(1, studentID);
				stmt.setInt(2, yearOfStudy);
				stmt.setInt(3, registrationTypeID);
				stmt.executeUpdate();
				
			}
			
			System.out.println("Populating student contact...");
			
			//populate student contact
			for(int j = 1; j <= students; j++)
			{
				//calculate student id
				int studentID = (1400000 + (j*3));
				
				String eMailAddress = "email" + j + "@address.com";
				
				String postalAddress = j + " Some Random Road, Blah Blah, Blah Blah Blah";
				
				stmt = dbConn.prepareStatement("INSERT INTO StudentContact VALUES (?, ?, ?);");
				stmt.setInt(1, studentID);
				stmt.setString(2, eMailAddress);
				stmt.setString(3, postalAddress);
				stmt.executeUpdate();
				
			}
			
			System.out.println("Populating next of kin contact...");
			
			//populate next of kin contact	
			for(int j = 1; j <= students; j++)
			{
				//calculate student id
				int studentID = (1400000 + (j*3));
				
				String name = "Name" + j;
				
				String eMailAddress = "someotheremail" + j + "@addresss.com" + j;
				
				String postalAddress = j + " Some Other Random Road, Blah Blah, Blah Blah Blah";
	
				stmt = dbConn.prepareStatement("INSERT INTO NextOfKinContact VALUES (?, ?, ?, ?);");
				stmt.setInt(1, studentID);
				stmt.setString(2, name);
				stmt.setString(3, eMailAddress);
				stmt.setString(4, postalAddress);
				stmt.executeUpdate();
				
			}
			
			System.out.println("Populating lecturer contact...");
			
			//populate lecturer contact	
			for(int l = 1; l <= 15; l++)
			{
				//calculate lecturer id
				int lecturerID = l;
				
				//create office
				String office = "Office" + l;
				
				//create email
				String eMailAddress = "lectureremail" + l + "@addresss.com" + l;
	
				stmt = dbConn.prepareStatement("INSERT INTO LecturerContact VALUES (?, ?, ?);");
				stmt.setInt(1, lecturerID);
				stmt.setString(2, office);
				stmt.setString(3, eMailAddress);
				stmt.executeUpdate();
			}
			
			System.out.println("Populating tutors...");
			int l = 1;
			//populate tutors
			for(int j = 1; j <= students; j++, l++)
			{
				//calculate student id
				int studentID = (1400000 + (j*3));
				
				//calculate lecturer id
				int lecturerID = l;
				
				stmt = dbConn.prepareStatement("INSERT INTO Tutor VALUES (?, ?);");
				stmt.setInt(1, studentID);
				stmt.setInt(2, lecturerID);
				stmt.executeUpdate();
				 
				if(l ==15)
				{
					l = 0;
				}
				
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				if(stmt!=null)
				{
					stmt.close();
				}
				if(dbConn!=null)
				{
					dbConn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Populates the database with believable entries
	 */
	
	public void populateTable()
	{	
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		
		
		try
		{
			dbConn = connect();
			
			int[] studentIDs = {1436266, 1465366, 1452752, 1353425, 1435235, 1432535, 1489643, 1463432, 1457893};
			int[] titles = {1, 1, 2, 1, 1, 1, 1, 1, 1};
			String[] foreNames = {"Ashley", "Charlie", "Georgia", "Melvyn", "Tom", "Joe", "Imran", "Anderson", "Eden" };
			String[] lastNames = {"Wyatt", "Street", "Keats", "Mathews", "Nicklin", "Faulls", "Miah", "Silva", "Hazard" };
			String[] dobs = {"1995-12-24", "1995-07-13", "1996-07-09", "1994-06-11", "1993-11-23", "1993-04-04", "1996-12-13", "1991-04-13", "1990-06-18"};
			int[] yearOfStudy = {2, 2, 2, 2, 2, 2, 1, 4, 3 };
			int[] registrationTypes = {1, 2, 3, 2, 1, 1, 1, 1, 3};
			String[] emails = {"ashleyw.awyatt@gmail.com", "charlie_street@virgin.com", "georgiakeats@bt.com", "melvynmattz@msn.com", "tomnick@gmail.com", 
								"joefaulls@gmail.co.uk", "imranmiah@yahoo.com", "asilva@gmail.com", "edenhazard@msn.com"};
			String[] addresses = {"267 Tiverton Road", "187 Hubert Road", "9 Sandy Road", "49 Bournbrook Road", "59 Dawlish Road", "5 Florence Square", "54 Harlybrooks Road", "34 Pershore Road", "42 Hartly Lane" };
			String[] lNames = {"Jeremy", "Jackie", "Barry", "Bob", "Felicity", "Felicity", "Felicity", "Jim", "Elliot"};
			String[] lEmails = {"jwyatt@msn.com", "jackie@gmail.com", "barry@msn.com", "bobbybob@gmail.com", "felicity@gmail.com", "felicity@gmail.com", 
								"felicity@gmail.com", "jimmyjimmyjim@gmail.com", "elliot@bt.com"};
			String[] lAddresses = {"125 Arlington Avenue", "Weynmouth", "43 Trinity Road", "342 Colgate Street", "64 Starling Street", "64 Starling Street", 
									"64 Starling Street", "53 Jim Road", "574 Britannia Road"};
			
			
			Date[] dates = new Date[dobs.length];
			for(int i = 0; i < dobs.length; i++)
			{
				dates[i] = Date.valueOf(dobs[i]);
			}
			
			System.out.println("Populating actual students...");

			stmt = dbConn.prepareStatement("INSERT INTO Student VALUES (?, ?, ?, ?, ?);");
			//populate students
			for(int i = 0; i < studentIDs.length; i++)
			{
				stmt.setInt(1, studentIDs[i]);
				stmt.setInt(2, titles[i]);
				stmt.setString(3, foreNames[i]);
				stmt.setString(4, lastNames[i]);
				stmt.setDate(5, dates[i]);
				stmt.executeUpdate();
			}				
			stmt.close();
		
			System.out.println("Populating actual lecturers...");
			
			stmt = dbConn.prepareStatement("INSERT INTO Lecturer VALUES (?, ?, ?, ?);");
			stmt.setInt(1, 16);
			stmt.setInt(2, 5);
			stmt.setString(3, "Mark");
			stmt.setString(4, "Lee");
			stmt.executeUpdate();
			stmt.setInt(1, 17);
			stmt.setInt(2, 1);
			stmt.setString(3, "Serge");
			stmt.setString(4, "Volker");
			stmt.executeUpdate();
		 
			System.out.println("Populating actual student registration...");
			
			stmt = dbConn.prepareStatement("INSERT INTO StudentRegistration VALUES (?, ?, ?);");
			for(int i = 0; i < studentIDs.length; i++)
			{
				stmt.setInt(1, studentIDs[i]);
				stmt.setInt(2, yearOfStudy[i]);
				stmt.setInt(3, registrationTypes[i]);
				stmt.executeUpdate();
			}

			System.out.println("Populating actual student contact...");

			stmt = dbConn.prepareStatement("INSERT INTO StudentContact VALUES (?, ?, ?);");
			for(int i = 0; i < studentIDs.length; i++)
			{
				stmt.setInt(1, studentIDs[i]);
				stmt.setString(2, emails[i]);
				stmt.setString(3, addresses[i]);
				stmt.executeUpdate();
			}
		
			System.out.println("Populating actual next of kin contact...");
			
			stmt = dbConn.prepareStatement("INSERT INTO NextOfKinContact VALUES (?, ?, ?, ?);");

			for(int i = 0; i < studentIDs.length; i++)
			{
				stmt.setInt(1, studentIDs[i]);
				stmt.setString(2, lNames[i]);
				stmt.setString(3, lEmails[i]);
				stmt.setString(4, lAddresses[i]);
				stmt.executeUpdate();
			}
				
			System.out.println("Populating actual lecturer contact...");
		
			stmt = dbConn.prepareStatement("INSERT INTO LecturerContact VALUES (?, ?, ?);");
			stmt.setInt(1, 16);
			stmt.setString(2, "U242");
			stmt.setString(3, "mark.lee@msn.com");
			stmt.executeUpdate();
			stmt.setInt(1, 17);
			stmt.setString(2, "UG04");
			stmt.setString(3, "serge.volker@gmail.com");
			stmt.executeUpdate();
			
			System.out.println("Populating actual tutors...");
		
			stmt = dbConn.prepareStatement("INSERT INTO Tutor VALUES (?, ?);");
			for(int i = 0; i < studentIDs.length-1; i++)
			{
				stmt.setInt(1, studentIDs[i]);
				stmt.setInt(2, 16);
				stmt.executeUpdate();
			}
			stmt.setInt(1, studentIDs[studentIDs.length-1]);
			stmt.setInt(2, 17);
			stmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(stmt!=null)
				{
					stmt.close();
				}
				if(dbConn!=null)
				{
					dbConn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
}
