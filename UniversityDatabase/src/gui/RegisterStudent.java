package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import org.postgresql.util.PSQLException;

import database.UniversityDatabaseModel;
import exception.JavaCheckException;

public class RegisterStudent extends JPanel implements Observer
{
	private JTextField[] fields;
	private String[] labels = { "Student ID:", "Title:", "First Name:", "Family Name:", "Date of Birth:", "Year of Study:", "Registration Type:", "E-Mail Address:", "Postal Address:", "Next of Kin Name:", "Next of Kin E-Mail Address:", "Next of Kin Postal Address:" };

	public RegisterStudent(UniversityDatabaseModel model, JFrame frame)
	{
		super(new BorderLayout());
		
	    int[] widths = { 7, 4, 15, 15, 10,  1, 1, 20, 30, 20, 20, 30 };
	    String[] descs = { "e.g. 1436266", "e.g. Mr", "e.g. Barry", "e.g. Brown", "e.g. 1995-12-31", "1st Year = 1 etc.", "1 = Normal, 2 = Repeat, 3 = External", "Email address", "Postal address", "Emergency Contact name", "Emergency Contact Email", "Emergency Contact Postal Address" };
		
	    //make column with the correct number of rows
	    JPanel content = new JPanel(new BorderLayout());
	    add(content, BorderLayout.CENTER);
	    
	    JPanel labelPanel = new JPanel(new GridLayout(labels.length, 1));
	    JPanel fieldPanel = new JPanel(new GridLayout(labels.length, 1));
	    
	    content.add(labelPanel, BorderLayout.WEST);
	    content.add(fieldPanel, BorderLayout.CENTER);
	    
	    //create text inputs
	    fields = new JTextField[labels.length];

	    for (int i = 0; i < labels.length; i += 1) 
	    {
	    	fields[i] = new JTextField();
	    	if (i < descs.length)
	    	{
	    		fields[i].setToolTipText(descs[i]);
	    	}
	    	if (i < widths.length)
	    	{
	    		fields[i].setColumns(widths[i]);
	    	}
	    	JLabel lab = new JLabel(labels[i], JLabel.RIGHT);
	    	lab.setLabelFor(fields[i]);

	    	labelPanel.add(lab);
	    	JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    	p.add(fields[i]);
	    	fieldPanel.add(p);
	    }
	   
	    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    add(buttons, BorderLayout.SOUTH);
	    JButton submit = new JButton("Submit Form");
	    JButton close = new JButton("Close");

	    submit.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) 
		    {
	    		boolean worked = false;
	    	    try 
	    	  	{
					model.registerStudent(getInput());
					worked = true;
				} 
	    	    catch (IllegalArgumentException | SQLException | JavaCheckException e1) 
	    	  	{
	    	  		JOptionPane.showMessageDialog(null, e1.getMessage(), "Oops!", JOptionPane.INFORMATION_MESSAGE);
				}
	    	  	if(worked == true)
	    	  	{		    	  	
	    	  		frame.dispose();
	    	  	}
		    }
	    });
	    
	    close.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) 
		    {
	    		frame.dispose();
		    }
	    });
		    
	    buttons.add(submit);
	    buttons.add(close);
	}
	
	public String[] getInput() throws JavaCheckException
	{
		String[] input = new String[12]; 
		for(int i = 0; i < fields.length; i++)
		{	
			input[i] = fields[i].getText();	
			if(input[i].equals(""))
			{
				throw new JavaCheckException("Field " + labels[i].substring(0, labels[i].length()-1) + " is empty.");
			}
		}
		return input;
	}


	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
}
