package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import database.UniversityDatabaseModel;
import exception.JavaCheckException;

public class AddTutor extends JPanel implements Observer
{
	//private JLabel studentID, titleID, foreName, familyName, dateOfBirth, yearOfStudy, registrationTypeID, eMailAddress, postalAddress, nokName, nokeMailAddress, nokPostalAddress;
	private JTextField[] fields;
	
	public AddTutor(UniversityDatabaseModel model, JFrame frame)
	{
		super(new BorderLayout());
		
		String[] labels = { "Student ID", "Lecturer ID" };
	    int[] widths = { 10, 10};
	    String[] descs = { "Student ID", "Enter the ID of the lecturer which you want the student to be assigned to" };
	    
	    JPanel content = new JPanel(new BorderLayout());
	    add(content, BorderLayout.NORTH);
	    
	    //make column with the correct number of rows
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
	    JButton submit = new JButton("Submit");
	    JButton close = new JButton("Close");

	    submit.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) 
		    {
	    		boolean worked = false;
	    	    try 
	    	  	{
					model.addTutor(getInput());
					worked = true;
				} 
	    	  	catch (NumberFormatException | SQLException | JavaCheckException e1) 
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
	
	public String[] getInput() 
	{
		String[] input = new String[12]; 
		for(int i = 0; i < fields.length; i++)
		{
			input[i] = fields[i].getText();
		}
		return input;
	}


	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
}
