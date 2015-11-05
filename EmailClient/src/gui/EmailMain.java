package gui;

import emailclient.EmailClient;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JRadioButton;

public class EmailMain {

	private JFrame frmEmailClient;
	private JScrollPane scrollPane;
	private Message[] messages;
	private JTextField textField;
	private EmailClient client;
	private JPanel mainPanel;
	
	/**
	 * Create the application.
	 */
	public static void main(String[] args)
	{
		//get the messages
		EmailMain email = new EmailMain();
	}
	
	public EmailMain() 
	{
		//create the main frame
		frmEmailClient = new JFrame("Email Client");
		frmEmailClient.setTitle("Inbox");
		//make a connection and retrieve the messages
		client = new EmailClient();
		messages = client.getInbox();
		create();
		frmEmailClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEmailClient.pack();
		frmEmailClient.setVisible(true);
		frmEmailClient.setSize(720, 480);
		frmEmailClient.setLocationRelativeTo(null);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void create()
	{		
		mainPanel = new JPanel(new BorderLayout());
		frmEmailClient.getContentPane().add(mainPanel);
		
		//add the top panel to the main panel
		JPanel topPanel = new JPanel(new FlowLayout());
		mainPanel.add(topPanel, BorderLayout.NORTH);
		
		//fill the top panel with the buttons and text fields etc.
		JLabel lblShow = new JLabel("Show:");
		lblShow.setHorizontalAlignment(SwingConstants.CENTER);
		lblShow.setFont(new Font("Tahoma", Font.PLAIN, 16));
		topPanel.add(lblShow);
		
		JRadioButton rdbtnRead = new JRadioButton("Read");
		topPanel.add(rdbtnRead);
		
		JLabel lblSearch = new JLabel("Search:");
		lblSearch.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSearch.setHorizontalAlignment(SwingConstants.CENTER);
		topPanel.add(lblSearch);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField.setColumns(20);
		topPanel.add(textField);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setFont(new Font("Tahoma", Font.PLAIN, 16));
		topPanel.add(btnRefresh);
		btnRefresh.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					//regenerate scrollable JTable
					mainPanel.remove(1);
					messages = client.getInbox();
					JTable table2 = createTable(messages);
					JScrollPane scrollPane2 = new JScrollPane(table2);
					mainPanel.add(scrollPane2);
					mainPanel.revalidate();
					mainPanel.repaint();
					JOptionPane.showMessageDialog(null, "Inbox successfully refreshed!", "Error", JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
		);
		
		JButton btnCompose = new JButton("Compose");
		btnCompose.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnCompose.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					NewMessage newMessage = new NewMessage(client);				
				}
				
			}
		);
		topPanel.add(btnCompose);
		//the top panel is now finished
		
		//add the scrollable table to the main panel
		JTable table = createTable(messages);
		scrollPane = new JScrollPane(table);
		mainPanel.add(scrollPane);
	}
	
	public JLabel align(JLabel label)
	{
		label.setHorizontalAlignment(SwingConstants.LEFT);
		return label;
		
	}
	
	public JTable createTable(Message[] messages)
	{
		JTable table = null;
		try 
		{
			int count = 0;
			//create table headings
			String[] columnNames = {"Date", "Subject"}; 
			System.out.println("Looking through messages");
			//add the data to the rows from the message array
			Object[][] rowData = new Object[messages.length][2];
			for(Message message : messages)
			{
				rowData[count][0] = message.getReceivedDate();
				rowData[count][1] = message.getSubject();
					
				System.out.println("Added an Email");
				count++;
			}
			//create the table using the table headings and input row data
			table = new JTable(rowData, columnNames);
			table.setBorder(new EmptyBorder(5, 5, 5, 5));
			table.setFont(new Font("Tahoma", Font.PLAIN, 16));
			
			//make the table uneditable
			DefaultTableModel model = new DefaultTableModel(rowData, columnNames) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			table.setModel(model);
	
			//make it so that when a row is clicked, then the email corresponding to the row is opened
			table.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					JTable table = (JTable) e.getSource();
					//find the row that was clicked
					int row = table.rowAtPoint(e.getPoint());
					//if double clicked
					if(e.getClickCount() ==2) {
						//get the email subject of the row that was clicked
						String messageSubject = (String) table.getModel().getValueAt(row,1);
						Date messageDate = (Date) table.getModel().getValueAt(row,0);
						for(int i = 0; i < messages.length; i++)
						{
							
							try {
								//find the email which contained the date and subject that was in the table
								if(messages[i].getSubject().equals(messageSubject) && messages[i].getSentDate().equals(messageDate))
								{
									//get content of email
									String content = "";
									if(messages[i].getContentType().contains("TEXT/PLAIN")) 
									{
										content = messages[i].getContent().toString();
									}
									else 
									{
										Multipart multipart = (Multipart) messages[i].getContent();
										for (int j = 0; j < multipart.getCount(); j++) {
											BodyPart bodyPart = multipart.getBodyPart(j);
											//display parts of the email which are text
											if(bodyPart.getContentType().contains("TEXT/PLAIN")) 
											{
												content = bodyPart.getContent().toString();
											}

										}
									}
									MessageDisplay display = new MessageDisplay(messages[i].getSubject(), messages[i].getFrom(), messages[i].getRecipients(RecipientType.TO),messages[i].getRecipients(RecipientType.CC), content);
									break;
								}
							} catch (MessagingException | IOException e1) {
								e1.printStackTrace();
							}
							
						}
					}
				}
			});
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return table;	
	}
}
