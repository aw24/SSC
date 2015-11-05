package gui;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import exception.JavaCheckException;

import javax.swing.*;

import database.UniversityDatabaseModel;

public class LecturerReport extends JPanel implements Observer
{
	private JTextField field;
	
	public LecturerReport(UniversityDatabaseModel model, JFrame frame)
	{
		super(new BorderLayout());
		
		String label = "Lecturer ID:";
	    int width =  10;
	    String desc = "The Lecturer ID of the lecturer for which you want a report.";
	    		   
	    //make column with the correct number of rows
	    JPanel content = new JPanel(new BorderLayout());
	    add(content, BorderLayout.CENTER);
	    
	    JPanel labelPanel = new JPanel(new GridLayout(1, 1));
	    JPanel fieldPanel = new JPanel(new GridLayout(1, 1));
	    
	    content.add(labelPanel, BorderLayout.WEST);
	    content.add(fieldPanel, BorderLayout.CENTER);
	    
	    //create text inputs
	    field = new JTextField();

		field.setToolTipText(desc);

		field.setColumns(width);
		
		JLabel lab = new JLabel(label, JLabel.RIGHT);
    	lab.setLabelFor(field);

    	labelPanel.add(lab);
    	JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	p.add(field);
    	fieldPanel.add(p);

	   
	    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    add(buttons, BorderLayout.SOUTH);
	    JButton submit = new JButton("Submit");
	    JButton close = new JButton("Close");
	    buttons.add(submit);
	    buttons.add(close);

	    submit.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) 
		    {
	    	    try 
	    	  	{
	    	    	ArrayList<String[]> data = model.lecturerReport(getInput());
	    	    	
	    	    	//remove submit button from button panel
	    	    	frame.dispose();
	    	    	
	    	    	JFrame newFrame = new JFrame("Lecturer Report");
	    	    	JPanel report = new JPanel(new GridLayout(10,1));
	    	    	newFrame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
	    	    	newFrame.getContentPane().add (report);
	    	    	
	    	    	JLabel year1 = new JLabel("1st Year");
	    	    	report.add(year1);
	    	    	
	    	    	JPanel year1Students = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    	    	report.add(year1Students);

	    	    	JLabel year2 = new JLabel("2nd Year");
	    	    	report.add(year2);
	    	    	
	    	    	JPanel year2Students = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    	    	report.add(year2Students);
	    	    	
	    	    	JLabel year3 = new JLabel("3rd Year");
	    	    	report.add(year3);
	    	    	
	    	    	JPanel year3Students = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    	    	report.add(year3Students);
	    	    	
	    	    	JLabel year4 = new JLabel("4th Year");
	    	    	report.add(year4);
	    	    	
	    	    	JPanel year4Students = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    	    	report.add(year4Students);
	    	    	
	    	    	JLabel year5 = new JLabel("5th Year");
	    	    	report.add(year5);
	    	    	
	    	    	JPanel year5Students = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    	    	report.add(year5Students);
	    	    	
	    	    	for(int i = 0; i < data.size(); i++)
	    	    	{
	    	    		String[] inputdata = data.get(i);
	    	    		String studentID = inputdata[4];
	    	    		
	    	    		if(inputdata[5].equals("1"))
	    	    		{
	    	    			year1Students.add(new JButton(studentID) {{
		    	    		    addActionListener(new ActionListener() {
		    	    		        public void actionPerformed(ActionEvent event) {
		    	    		            createStudentFrame(inputdata);
		    	    		        }
		    	    		    });
		    	    		}});
	    	    		}
	    	    		else if(inputdata[5].equals("2"))
	    	    		{
	    	    			year2Students.add(new JButton(studentID) {{
		    	    		    addActionListener(new ActionListener() {
		    	    		        public void actionPerformed(ActionEvent event) {
		    	    		            createStudentFrame(inputdata);
		    	    		        }
		    	    		    });
		    	    		}});
	    	    		}
	    	    		else if(inputdata[5].equals("3"))
	    	    		{
	    	    			year3Students.add(new JButton(studentID) {{
		    	    		    addActionListener(new ActionListener() {
		    	    		        public void actionPerformed(ActionEvent event) {
		    	    		            createStudentFrame(inputdata);
		    	    		        }
		    	    		    });
		    	    		}});
	    	    		}
	    	    		else if(inputdata[5].equals("4"))
	    	    		{
	    	    			year4Students.add(new JButton(studentID) {{
		    	    		    addActionListener(new ActionListener() {
		    	    		        public void actionPerformed(ActionEvent event) {
		    	    		            createStudentFrame(inputdata);
		    	    		        }
		    	    		    });
		    	    		}});
	    	    		}
	    	    		else
	    	    		{
	    	    			year5Students.add(new JButton(studentID) {{
		    	    		    addActionListener(new ActionListener() {
		    	    		        public void actionPerformed(ActionEvent event) {
		    	    		            createStudentFrame(inputdata);
		    	    		        }
		    	    		    });
		    	    		}});
	    	    		}	
	    	    	}
	    	    	
	    	    	newFrame.pack();
	    	    	newFrame.setLocationRelativeTo(null);
	    	    	newFrame.setVisible (true);
	    	    	
	    	    	
	    	    	JOptionPane.showMessageDialog(null, "Retrieved Lecturer report.", "Message", JOptionPane.INFORMATION_MESSAGE);
	    	    	
	    	    	
				} 
	    	  	catch (NumberFormatException | SQLException | JavaCheckException e1) 
	    	  	{
	    	  		JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
				} 
		    }
	    });
	    
	    close.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) 
		    {
	    		frame.dispose();
		    }
	    });
	}
	
	public void createStudentFrame(String[] data)
	{
		
		JFrame newFrame = new JFrame("Student Report");
    	JPanel report = new JPanel(new GridLayout(12, 2));
    	newFrame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
    	newFrame.getContentPane().add (report);
    	
    	//create the report
    	//title
    	JPanel titleHeading = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JLabel t = new JLabel("Title:");
    	t.setFont(new Font(getFont().getFontName(), Font.BOLD,getFont().getSize()));
    	titleHeading.add(t);
    	report.add(titleHeading);
    	
    	JPanel title = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	title.add(new JLabel(data[0]));
    	report.add(title);
    	
    	//first name
    	JPanel firstNameHeading = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JLabel f = new JLabel("First Name:");
    	f.setFont(new Font(getFont().getFontName(), Font.BOLD,getFont().getSize()));
    	firstNameHeading.add(f);
    	report.add(firstNameHeading);
    	
    	JPanel firstName = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	firstName.add(new JLabel(data[1]));
    	report.add(firstName);
    	
    	//family name
    	JPanel familyNameHeading = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JLabel fa = new JLabel("Last Name:");
    	fa.setFont(new Font(getFont().getFontName(), Font.BOLD,getFont().getSize()));
    	familyNameHeading.add(fa);
    	report.add(familyNameHeading);
    	
    	JPanel familyName = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	familyName.add(new JLabel(data[2]));
    	report.add(familyName);
    	
    	//DOB	    	    	
    	JPanel dOBHeading = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JLabel d = new JLabel("DOB:");
    	d.setFont(new Font(getFont().getFontName(), Font.BOLD,getFont().getSize()));
    	dOBHeading.add(d);
    	report.add(dOBHeading);
    	
    	JPanel dob = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	dob.add(new JLabel(data[3]));
    	report.add(dob);
    	
    	//student id
    	JPanel studentIDHeading = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JLabel s = new JLabel("Student ID:");
    	s.setFont(new Font(getFont().getFontName(), Font.BOLD,getFont().getSize()));
    	studentIDHeading.add(s);
    	report.add(studentIDHeading);
    	
    	JPanel studentID = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	studentID.add(new JLabel(data[4]));
    	report.add(studentID);
    	
    	//registration type
    	JPanel rtHeading = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JLabel r = new JLabel("Registration Type:");
    	r.setFont(new Font(getFont().getFontName(), Font.BOLD,getFont().getSize()));
    	rtHeading.add(r);
    	report.add(rtHeading);
    	
    	JPanel rt = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	rt.add(new JLabel(data[6]));
    	report.add(rt);
    	
    	//email
    	JPanel emailHeading = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JLabel em = new JLabel("Email Address:");
    	em.setFont(new Font(getFont().getFontName(), Font.BOLD,getFont().getSize()));
    	emailHeading.add(em);
    	report.add(emailHeading);
    	
    	JPanel email = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	email.add(new JLabel(data[7]));
    	report.add(email);
    	
    	//postal
    	JPanel postalHeading = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JLabel p = new JLabel("Postal Address:");
    	p.setFont(new Font(getFont().getFontName(), Font.BOLD,getFont().getSize()));
    	postalHeading.add(p);
    	report.add(postalHeading);
    	
    	JPanel postal = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	postal.add(new JLabel(data[8]));
    	report.add(postal);
    	
    	//emergency contact
    	JPanel ecHeading = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	ecHeading.add(new JLabel("<HTML><U>Emergency Contact</U></HTML>"));
    	report.add(ecHeading);
    	
    	JPanel empty = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	empty.add(new JLabel(""));
    	report.add(empty);
    	
    	//ec name
    	JPanel ecNameHeading = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JLabel ecn = new JLabel("Name:");
    	ecn.setFont(new Font(getFont().getFontName(), Font.BOLD,getFont().getSize()));
    	ecNameHeading.add(ecn);
    	report.add(ecNameHeading);
    	
    	JPanel ecName = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	ecName.add(new JLabel(data[10]));
    	report.add(ecName);
    	
    	//ec email
    	JPanel ecemailHeading = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JLabel ecm = new JLabel("Email Address:");
    	ecm.setFont(new Font(getFont().getFontName(), Font.BOLD,getFont().getSize()));
    	ecemailHeading.add(ecm);
    	report.add(ecemailHeading);
    	
    	JPanel ecEmail = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	ecEmail.add(new JLabel(data[11]));
    	report.add(ecEmail);
    	
    	//ec postal
    	JPanel ecpostalHeading = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JLabel ecp = new JLabel("Postal Address:");
    	ecp.setFont(new Font(getFont().getFontName(), Font.BOLD,getFont().getSize()));
    	ecpostalHeading.add(ecp);
    	report.add(ecpostalHeading);
    	
    	JPanel ecPostal = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	ecPostal.add(new JLabel(data[12]));
    	report.add(ecPostal);
    	


		new JLabel("First Name");
		new JLabel("Last Name");
		new JLabel("DOB");
		new JLabel("Student ID");
		new JLabel("Year of study");
		new JLabel("Registration Type");
		new JLabel("Email Address");
		new JLabel("Postal Address");
		new JLabel("Emergency Contact");
		new JLabel("Title");
		new JLabel("Title");
		
		newFrame.pack();
    	newFrame.setLocationRelativeTo(null);
    	newFrame.setVisible (true);
    	
	}
	
	public String getInput() 
	{ 
		return field.getText();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
}
