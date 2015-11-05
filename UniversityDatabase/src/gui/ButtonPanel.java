package gui;

import javax.swing.*;

import database.UniversityDatabaseModel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observer;
import java.util.Observable;

public class ButtonPanel extends JPanel implements Observer
{
	private static final long serialVersionUID = 1L;
	private final JButton registerStudent, addTutor, studentReport, lecturerReport;
	private final ButtonGroup buttonGroup;

	public ButtonPanel(UniversityDatabaseModel model)
	{
		super();
		
		registerStudent = new JButton("Register a Student");
		addTutor = new JButton("Add a new tutor");
		studentReport = new JButton("Student report");
		lecturerReport = new JButton("Lecturer report");
		
		registerStudent.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					JFrame frame = new JFrame ("Register a student");
		            frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
		            frame.getContentPane().add (new RegisterStudent(model, frame));
		            frame.pack();
		            frame.setLocationRelativeTo(null);
		            frame.setVisible (true);					
				}
				
			}
		);
		
		addTutor.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					JFrame frame = new JFrame ("Assign a Tutor");
		            frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
		            frame.getContentPane().add (new AddTutor(model, frame));
		            frame.pack();
		            frame.setLocationRelativeTo(null);
		            frame.setVisible (true);					
				}
				
			}
		);
		
		studentReport.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					JFrame frame = new JFrame ("Student Report");
		            frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
		            frame.getContentPane().add (new StudentReport(model, frame));
		            frame.pack();
		            frame.setLocationRelativeTo(null);
		            frame.setVisible (true);					
				}
				
			}
		);
		
		lecturerReport.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					JFrame frame = new JFrame ("Lecturer Report");
		            frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
		            frame.getContentPane().add (new LecturerReport(model,frame));
		            frame.pack();
		            frame.setLocationRelativeTo(null);
		            frame.setVisible (true);					
				}
				
			}
		);
		
		JPanel title = new JPanel();
		JPanel buttons = new JPanel();
		
		buttonGroup = new ButtonGroup();
		buttonGroup.add(registerStudent);
		buttonGroup.add(addTutor);
		buttonGroup.add(studentReport);
		buttonGroup.add(lecturerReport);
		
		buttons.add(registerStudent);
		buttons.add(addTutor);
		buttons.add(studentReport);
		buttons.add(lecturerReport);	
		
		JLabel heading = new JLabel("University Database");
		heading.setFont (heading.getFont ().deriveFont (64.0f));
		title.add(heading);
		add(title, BorderLayout.NORTH);
		add(buttons, BorderLayout.CENTER);
	}

	@Override
	public void update(Observable obs, Object obj) 
	{
		// TODO Auto-generated method stub
		repaint();
	}
}
