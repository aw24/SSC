package gui;

import javax.mail.MessagingException;
import javax.swing.JFrame;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;

import emailclient.EmailClient;
import emailclient.SendMessage;

public class NewMessage {

	private JFrame frmNewMessage;
	private JTextField subjectField;
	private JTextField ccField;
	private JTextField toField;
	private JTextArea textArea;
	private EmailClient client;
	private ArrayList<String> files;

	/**
	 * Create the application.
	 */
	public NewMessage(EmailClient c) 
	{
		client = c;
		files = new ArrayList<String>();
		initialize();	
	}

	/**
	 * Initialize the contents of the frame.
	 * Some of this code is generated by WindowBuilder
	 */
	private void initialize() 
	{
		frmNewMessage = new JFrame();
		frmNewMessage.setTitle("New Message");
		frmNewMessage.setSize(720,480);
		frmNewMessage.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmNewMessage.setVisible(true);
		frmNewMessage.setLocationRelativeTo(null);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		frmNewMessage.getContentPane().setLayout(gridBagLayout);
		
		JLabel lblTo = new JLabel("To:");
		GridBagConstraints gbc_lblTo = new GridBagConstraints();
		gbc_lblTo.anchor = GridBagConstraints.EAST;
		gbc_lblTo.insets = new Insets(10, 0, 5, 5);
		gbc_lblTo.gridx = 0;
		gbc_lblTo.gridy = 0;
		frmNewMessage.getContentPane().add(lblTo, gbc_lblTo);
		
		toField = new JTextField();
		GridBagConstraints gbc_toField = new GridBagConstraints();
		gbc_toField.gridwidth = 6;
		gbc_toField.insets = new Insets(10, 0, 5, 10);
		gbc_toField.fill = GridBagConstraints.HORIZONTAL;
		gbc_toField.gridx = 1;
		gbc_toField.gridy = 0;
		frmNewMessage.getContentPane().add(toField, gbc_toField);
		toField.setColumns(10);
		
		JLabel lblCc = new JLabel("CC");
		GridBagConstraints gbc_lblCc = new GridBagConstraints();
		gbc_lblCc.anchor = GridBagConstraints.EAST;
		gbc_lblCc.insets = new Insets(0, 0, 5, 5);
		gbc_lblCc.gridx = 0;
		gbc_lblCc.gridy = 1;
		frmNewMessage.getContentPane().add(lblCc, gbc_lblCc);
		
		ccField = new JTextField();
		GridBagConstraints gbc_ccField = new GridBagConstraints();
		gbc_ccField.gridwidth = 6;
		gbc_ccField.insets = new Insets(0, 0, 5, 10);
		gbc_ccField.fill = GridBagConstraints.HORIZONTAL;
		gbc_ccField.gridx = 1;
		gbc_ccField.gridy = 1;
		frmNewMessage.getContentPane().add(ccField, gbc_ccField);
		ccField.setColumns(10);
		
		JLabel lblSubject = new JLabel("Subject:");
		GridBagConstraints gbc_lblSubject = new GridBagConstraints();
		gbc_lblSubject.anchor = GridBagConstraints.EAST;
		gbc_lblSubject.insets = new Insets(0, 10, 5, 5);
		gbc_lblSubject.gridx = 0;
		gbc_lblSubject.gridy = 2;
		frmNewMessage.getContentPane().add(lblSubject, gbc_lblSubject);
		
		subjectField = new JTextField();
		GridBagConstraints gbc_subjectField = new GridBagConstraints();
		gbc_subjectField.gridwidth = 6;
		gbc_subjectField.insets = new Insets(0, 0, 5, 10);
		gbc_subjectField.fill = GridBagConstraints.HORIZONTAL;
		gbc_subjectField.gridx = 1;
		gbc_subjectField.gridy = 2;
		frmNewMessage.getContentPane().add(subjectField, gbc_subjectField);
		subjectField.setColumns(10);
		
		JLabel lblAttachedFile = new JLabel("");
		GridBagConstraints gbc_lblAttachedFile = new GridBagConstraints();
		gbc_lblAttachedFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblAttachedFile.gridx = 4;
		gbc_lblAttachedFile.gridy = 3;
		frmNewMessage.getContentPane().add(lblAttachedFile, gbc_lblAttachedFile);
		
		JButton btnAttach = new JButton("Attach file...");
		GridBagConstraints gbc_btnAttach = new GridBagConstraints();
		gbc_btnAttach.gridwidth = 2;
		gbc_btnAttach.insets = new Insets(0, 0, 5, 10);
		gbc_btnAttach.gridx = 5;
		gbc_btnAttach.gridy = 3;
		frmNewMessage.getContentPane().add(btnAttach, gbc_btnAttach);
		
		//attach a file
		btnAttach.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//create file chooser
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) 
				{
					//get the selected file
					File file = fileChooser.getSelectedFile();
					//add the path to an arraylist (this arraylist will get passed to the SendMessage class so that the files can actually be attached)
					files.add(file.getPath());
					//update label to show which files are attached
					if(lblAttachedFile.getText().equals(""))
					{
						lblAttachedFile.setText(file.getName());
					}
					else
					{
						lblAttachedFile.setText(lblAttachedFile.getText() + ", " + file.getName());
					}
				}
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 3;
		gbc_scrollPane.gridwidth = 7;
		gbc_scrollPane.insets = new Insets(0, 10, 5, 10);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 4;
		frmNewMessage.getContentPane().add(scrollPane, gbc_scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		JButton btnClose = new JButton("Close");
		GridBagConstraints gbc_btnClose = new GridBagConstraints();
		gbc_btnClose.insets = new Insets(0, 10, 10, 5);
		gbc_btnClose.gridx = 0;
		gbc_btnClose.gridy = 7;
		frmNewMessage.getContentPane().add(btnClose, gbc_btnClose);
		
		//close window if close button is pressed
		btnClose.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					frmNewMessage.dispose();
				}
			}
		);
		
		JButton btnSend = new JButton("Send");
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 10, 10);
		gbc_btnSend.gridx = 6;
		gbc_btnSend.gridy = 7;
		frmNewMessage.getContentPane().add(btnSend, gbc_btnSend);
		
		//send message
		btnSend.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String[] messageData = { ccField.getText(), toField.getText(), subjectField.getText(), textArea.getText()};
				try {
					SendMessage send = new SendMessage(messageData, files, client);
					frmNewMessage.dispose();
				} catch (MessagingException e1) {
					JOptionPane.showMessageDialog(null, "Message failed to send", "Error", JOptionPane.INFORMATION_MESSAGE);
					e1.printStackTrace();
				}
				
			}
		}
	);
		
	}

}
