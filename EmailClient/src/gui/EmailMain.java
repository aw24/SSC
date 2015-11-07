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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JRadioButton;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JCheckBox;

public class EmailMain {

	private JFrame frmEmailClient;
	private JScrollPane scrollPane;
	private ArrayList<Message> displayedMessages;
	private ArrayList<Message> hiddenMessages;
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
		displayedMessages = client.getInbox();
		create();
		frmEmailClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEmailClient.pack();
		frmEmailClient.setVisible(true);
		frmEmailClient.setSize(720, 480);
		frmEmailClient.setLocationRelativeTo(null);
	}

	public ArrayList<Message> search(ArrayList<Message> messages, String searchterm)
	{
		ArrayList<Message> found = new ArrayList<Message>();
		System.out.println("Searching...");
		for(int i = 0; i < messages.size(); i++)
		{
			Message current = messages.get(i);
			try {
				if(current.getSubject().toLowerCase().contains((searchterm).toLowerCase()) || getEmailBody(current).toLowerCase().contains(searchterm.toLowerCase()))
				{
					found.add(current);
					System.out.println("Found a match!");
				}
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return found;
	}
	
	public ArrayList<Message> sort(ArrayList<Message> messages)
	{
		try
		{
			if(messages.size() > 1)
			{
				for(int i = 0; i < messages.size(); i++)
				{
					for(int j = 0; j < messages.size()-1; j++)
					{
						if(messages.get(j).getReceivedDate().before(messages.get(j+1).getReceivedDate()))
						{
							Collections.swap(messages, j, j+1);
						}
					}
				}
			}
		}
		catch
		(MessagingException e)
		{
			
		}
		return messages;
	}
	
	public void displaySearchedMessages(String searchword)
	{
		ArrayList<Message> found = search(displayedMessages, searchword);
		found = sort(found);
		reproduceTable(found);
		
	}
	
	public void displayMessages(Flag flag, boolean selected, boolean negation)
	{
		ArrayList<Message> toBeDisplayed = new ArrayList<Message>();
		ArrayList<Message> toBeHidden = new ArrayList<Message>();
		//the arraylist of messages of which we will be checking the flags of
		ArrayList<Message> messages = new ArrayList<Message>();

		if(selected == true)
		{
			//if checkbox was ticked then we shall look at the messages which are not currently being displayed
			messages = hiddenMessages;
		}
		else
		{
			//else if the checkbox was not ticked then we shall look at the messages which are currently being displayed
			messages = displayedMessages;
		}

		for(int i = 0; i < messages.size(); i++)
		{
			//for each message
			Message current = messages.get(i);

			try 
			{
				//get its flags
				Flags flags = current.getFlags();
				//decide whether the message should be displayed -- this is based upon whether it has a certain flag and if the checkbox was ticked
				if(flags.contains(flag) && selected == false && negation == true || flags.contains(flag) && selected == true && negation == false|| !flags.contains(flag) && selected == false && negation == false || !flags.contains(flag) && selected == true && negation == true)
				{
					//add the message to an arraylist of messages which will be displayed
					toBeDisplayed.add(current);
				}
				else
				{
					//add the message to an arraylist of messages which will not be displayed
					toBeHidden.add(current);
				}
			} 
			catch (MessagingException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		ArrayList<Message> toGoInTable = new ArrayList<Message>();
		ArrayList<Message> toBeLeftOut = new ArrayList<Message>();
		//if the checkbox was ticked
		if(selected == true)
		{
			//the messages which will be going in the table are the ones which were either already being displayed, or are new ones which have met the requirements
			toGoInTable.addAll(displayedMessages);
			toGoInTable.addAll(toBeDisplayed);
			
			displayedMessages = toGoInTable;
			hiddenMessages = toBeHidden;
		}
		else //if the checkbox was not ticked
		{
			//the messages going into the table are the ones that still meet the requirements
			toGoInTable.addAll(toBeDisplayed);
			displayedMessages = toGoInTable;
			//the rest will be hidden
			toBeLeftOut.addAll(toBeHidden);
			if(hiddenMessages!=null)
			{
				toBeLeftOut.addAll(hiddenMessages);
			}
			hiddenMessages = toBeLeftOut;
		}

		toGoInTable = sort(toGoInTable);
		reproduceTable(toGoInTable);
	}
	
	public void reproduceTable(ArrayList<Message> emails)
	{
		//recreate the table with the new input messages
		mainPanel.remove(1);
		JTable table = createTable(emails);
		JScrollPane scrollPane2 = new JScrollPane(table);
		mainPanel.add(scrollPane2);
		mainPanel.revalidate();
		mainPanel.repaint();	
	}
	
	public JLabel align(JLabel label)
	{
		label.setHorizontalAlignment(SwingConstants.LEFT);
		return label;
		
	}
	
	public JTable constructTable(ArrayList<Message> messages)
	{
		JTable table = null;
	
		//create table headings
		String[] columnNames = {"Date", "Subject"}; 
		System.out.println("Looking through messages");
		
		//add the data to the rows from the message array
		Object[][] rowData = new Object[messages.size()][2];
		int count = 0;
		for(Message message : messages)
		{
			try 
			{
				rowData[count][0] = message.getReceivedDate();
				rowData[count][1] = message.getSubject();
			}
			catch (MessagingException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
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
		
		return table;
	}
	
	public JTable createTable(ArrayList<Message> messages)
	{
		JTable table = constructTable(messages);
	
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
					System.out.println(messageSubject);
					Date messageDate = (Date) table.getModel().getValueAt(row,0);
					System.out.println(messageDate.toString());
					for(int i = 0; i < messages.size(); i++)
					{
						Message current = messages.get(i);
						try {
							//find the email which contained the date and subject that was in the table
							if(current.getSubject().equals(messageSubject) && current.getReceivedDate().equals(messageDate))
							{
								System.out.println("Found");
								String content = getEmailBody(current);
								MessageDisplay display = new MessageDisplay(current.getSubject(), current.getFrom(), current.getRecipients(RecipientType.TO),current.getRecipients(RecipientType.CC), content);
								break;
							}
						} catch (MessagingException e1) {
							e1.printStackTrace();
						}
						
					}
				}
			}
		});

		return table;	
	}
	
	public String getEmailBody(Message current)
	{
		//get content of email
		String content = "";
		try {
			if(current.getContentType().contains("TEXT/PLAIN")) 
			{
				content = current.getContent().toString();
			}
			else 
			{
				Multipart multipart = (Multipart) current.getContent();
				for (int j = 0; j < multipart.getCount(); j++) {
					BodyPart bodyPart = multipart.getBodyPart(j);
					//display parts of the email which are text
					if(bodyPart.getContentType().contains("TEXT/PLAIN")) 
					{
						content = bodyPart.getContent().toString();
					}

				}
			}
		} catch (MessagingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void create()
	{		
		mainPanel = new JPanel(new BorderLayout());
		frmEmailClient.getContentPane().add(mainPanel);
		
		//add the top panel to the main panel
		JPanel topPanel = new JPanel();
		mainPanel.add(topPanel, BorderLayout.NORTH);
		GridBagLayout gbl_topPanel = new GridBagLayout();
		gbl_topPanel.columnWidths = new int[]{45, 51, 53, 286, 85, 99, 0};
		gbl_topPanel.rowHeights = new int[]{29, 0, 0, 0, 0};
		gbl_topPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_topPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		topPanel.setLayout(gbl_topPanel);
		
		//fill the top panel with the buttons and text fields etc.
		JLabel lblShow = new JLabel("Show:");
		lblShow.setHorizontalAlignment(SwingConstants.CENTER);
		lblShow.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_lblShow = new GridBagConstraints();
		gbc_lblShow.anchor = GridBagConstraints.WEST;
		gbc_lblShow.insets = new Insets(10, 10, 5, 5);
		gbc_lblShow.gridx = 0;
		gbc_lblShow.gridy = 0;
		topPanel.add(lblShow, gbc_lblShow);
		
		JLabel lblSearch = new JLabel("Search:");
		lblSearch.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSearch.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblSearch = new GridBagConstraints();
		gbc_lblSearch.anchor = GridBagConstraints.WEST;
		gbc_lblSearch.insets = new Insets(10, 0, 5, 5);
		gbc_lblSearch.gridx = 2;
		gbc_lblSearch.gridy = 0;
		topPanel.add(lblSearch, gbc_lblSearch);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField.setColumns(20);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.anchor = GridBagConstraints.WEST;
		gbc_textField.insets = new Insets(10, 0, 5, 5);
		gbc_textField.gridx = 3;
		textField.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					displaySearchedMessages(textField.getText());	
				}
				
			}
		);
		gbc_textField.gridy = 0;
		topPanel.add(textField, gbc_textField);
		
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
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_btnRefresh = new GridBagConstraints();
		gbc_btnRefresh.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnRefresh.insets = new Insets(10, 0, 5, 5);
		gbc_btnRefresh.gridx = 4;
		gbc_btnRefresh.gridy = 0;
		topPanel.add(btnRefresh, gbc_btnRefresh);
		btnRefresh.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					//regenerate scrollable JTable
					mainPanel.remove(1);
					displayedMessages = client.getInbox();
					JTable table2 = createTable(displayedMessages);
					JScrollPane scrollPane2 = new JScrollPane(table2);
					mainPanel.add(scrollPane2);
					mainPanel.revalidate();
					mainPanel.repaint();
					JOptionPane.showMessageDialog(null, "Inbox successfully refreshed!", "Error", JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
		);
		GridBagConstraints gbc_btnCompose = new GridBagConstraints();
		gbc_btnCompose.insets = new Insets(10, 0, 5, 10);
		gbc_btnCompose.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnCompose.gridx = 5;
		gbc_btnCompose.gridy = 0;
		topPanel.add(btnCompose, gbc_btnCompose);
		
		JCheckBox chckbxRecent = new JCheckBox("Recent");
		chckbxRecent.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_chckbxRecent = new GridBagConstraints();
		gbc_chckbxRecent.anchor = GridBagConstraints.WEST;
		gbc_chckbxRecent.insets = new Insets(10, 0, 5, 5);
		gbc_chckbxRecent.gridx = 1;
		gbc_chckbxRecent.gridy = 0;
		topPanel.add(chckbxRecent, gbc_chckbxRecent);
		chckbxRecent.setSelected(true);
		chckbxRecent.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					displayMessages(Flags.Flag.RECENT, chckbxRecent.isSelected(), false);			
				}
			}
		);
		chckbxRecent.setEnabled(false);
		
		JCheckBox chckbxOld = new JCheckBox("Old");
		GridBagConstraints gbc_chckbxOld = new GridBagConstraints();
		gbc_chckbxOld.anchor = GridBagConstraints.WEST;
		gbc_chckbxOld.insets = new Insets(0, 0, 10, 5);
		gbc_chckbxOld.gridx = 1;
		gbc_chckbxOld.gridy = 1;
		topPanel.add(chckbxOld, gbc_chckbxOld);
		chckbxOld.setSelected(true);
		chckbxOld.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					displayMessages(Flags.Flag.RECENT, chckbxOld.isSelected(), true);			
				}
			}
		);
		chckbxOld.setEnabled(false);
		
		JCheckBox chckbxRead = new JCheckBox("Read");
		GridBagConstraints gbc_chckbxRead = new GridBagConstraints();
		gbc_chckbxRead.anchor = GridBagConstraints.WEST;
		gbc_chckbxRead.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxRead.gridx = 1;
		gbc_chckbxRead.gridy = 2;
		topPanel.add(chckbxRead, gbc_chckbxRead);
		chckbxRead.setSelected(true);
		chckbxRead.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					displayMessages(Flags.Flag.SEEN, chckbxRead.isSelected(), false);			
				}
			}
		);
		
		JCheckBox chckbxUnread = new JCheckBox("Unread");
		GridBagConstraints gbc_chckbxUnread = new GridBagConstraints();
		gbc_chckbxUnread.anchor = GridBagConstraints.WEST;
		gbc_chckbxUnread.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxUnread.gridx = 1;
		gbc_chckbxUnread.gridy = 3;
		topPanel.add(chckbxUnread, gbc_chckbxUnread);
		chckbxUnread.setSelected(true);
		chckbxUnread.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					displayMessages(Flags.Flag.SEEN, chckbxUnread.isSelected(), true);			
				}
			}
		);
		
		
		//the top panel is now finished
		
		//add the scrollable table to the main panel
		JTable table = createTable(displayedMessages);
		scrollPane = new JScrollPane(table);
		mainPanel.add(scrollPane);
	}
}
