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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JCheckBox;

public class EmailMain {

	private JFrame frmEmailClient;
	private EmailClient client;
	private EmailHelper helper;
	private JScrollPane scrollPane;
	private ArrayList<Message> displayedMessages;
	private ArrayList<Message> hiddenMessages;
	private HashMap<String, String> flags;
	private JTextField textField;
	private JPanel mainPanel;
	private JCheckBox chckbxUnread;
	private JCheckBox chckbxRead;
	private DefaultTableModel model;
	private JTable currentTable;
	
	/**
	 * Create the application.
	 */
	public static void main(String[] args)
	{
		EmailMain email = new EmailMain();
	}
	
	public EmailMain() 
	{
		//create the main frame
		frmEmailClient = new JFrame("Email Client");
		frmEmailClient.setTitle("Inbox");
		
		//create other classes so can access functions
		client = new EmailClient();
		helper = new EmailHelper();
		
		//initialise arraylist of messages
		hiddenMessages = new ArrayList<Message>();
		displayedMessages = new ArrayList<Message>();
		
		//make a connection and retrieve the messages
		displayedMessages = client.getInbox();
		
		//initialise hashmap which will contain data about custom flags
		flags = new HashMap<String, String>();
		
		//run main gui
		create();
		
		frmEmailClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEmailClient.pack();
		frmEmailClient.setVisible(true);
		frmEmailClient.setSize(720, 480);
		frmEmailClient.setLocationRelativeTo(null);
	}
	
	public void createTable(ArrayList<Message> messages)
	{
		//constructs the actual JTable
		constructTable(messages);
		//adds the listeners to each row
		helper.addListeners(currentTable, messages);
	}
	
	/**
	 * Removes the current JTable and adds the new one
	 * @param emails The new email list
	 */
	
	public void reproduceTable(ArrayList<Message> emails)
	{
		//recreate the table with the new input messages
		mainPanel.remove(1);
		createTable(emails);
		JScrollPane scrollPane2 = new JScrollPane(currentTable);
		mainPanel.add(scrollPane2);
		mainPanel.revalidate();
		mainPanel.repaint();	
	}
	
	
	/**
	 * Inserts the values from the messages into the table
	 * @param messages The list of messages to go into the table
	 */
	
	public void constructTable(ArrayList<Message> messages)
	{
		//create table headings
		String[] columnNames = {"Date", "Subject", "Custom flags"}; 
		System.out.println("Looking through messages...");
		
		//add the data to the rows from the message array
		Object[][] rowData = new Object[messages.size()][3];
		int count = 0;
		for(Message message : messages)
		{
			try 
			{
				rowData[count][0] = message.getReceivedDate();
				rowData[count][1] = message.getSubject();
				rowData[count][2] = helper.assignCustomFlags(message, flags);
			}
			catch (MessagingException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			count++;
		}
		
		//create the table using the table headings and input row data
		currentTable = new JTable(rowData, columnNames);
		currentTable.setBorder(new EmptyBorder(5, 5, 5, 5));
		currentTable.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		//make the table uneditable
		model = new DefaultTableModel(rowData, columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		System.out.println("Finished.");
		currentTable.setModel(model);
	}
	
	
	/**
	 * When a checkbox has been ticked, this method calculates which messages should be displayed.
	 * @param flag The flag that the checkbox is referring to. e.g. The read checkbox refers to Flags.Flag.SEEN 
	 * @param selected A boolean to show if the checkbox is selected or not
	 * @param negation A boolean that shows if the checkbox needs the flag to be applied or not
	 */
	
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

		helper.sort(toGoInTable);
		reproduceTable(toGoInTable);
	}
	
	/**
	 * When the refresh button is pressed, new messages are retrieved.
	 */
	
	public void refresh()
	{
		//regenerate scrollable JTable
		ArrayList<Message> currentMessages = new ArrayList<Message>();
		ArrayList<Message> newMessages = new ArrayList<Message>();
		
		currentMessages.addAll(hiddenMessages);
		currentMessages.addAll(displayedMessages);
		
		//if there is currently messages in the inbox
		if(currentMessages.size()>=1)
		{
			try
			{
				//get the new messages
				newMessages = client.getInbox();
				
				//remove any messages that are already in the inbox
				Collections.reverse(newMessages);
				for(int i = 0; i < currentMessages.size(); i++)
				{
					newMessages.remove(0);
				}
				//add all the remaining messages to the displayed messages array
				displayedMessages.addAll(newMessages);
				//add new rows for the new messages
				helper.addRows(model, newMessages, flags);
			}
			catch(IndexOutOfBoundsException e1)
			{
				newMessages = client.getInbox();
				createTable(newMessages);
			}
		}
		else
		{
			//if there are currently no messages
			newMessages = client.getInbox();
			createTable(newMessages);
		}
		model.fireTableRowsInserted(0, 0);
		
		//show all inbox messages
		chckbxRead.setSelected(true);
		chckbxUnread.setSelected(true);
		displayMessages(Flags.Flag.SEEN, chckbxRead.isSelected(), true);	
		displayMessages(Flags.Flag.SEEN, chckbxRead.isSelected(), false);	
	
		//notify user that the refresh has completed successfully
		JOptionPane.showMessageDialog(null, "Inbox successfully refreshed!", "Message", JOptionPane.INFORMATION_MESSAGE);
	}
	
	
	/**
	 * Actually creates the GUI
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
					reproduceTable(helper.displaySearchedMessages(displayedMessages, textField.getText()));	
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
					refresh();
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
		
		JButton btnManageCustomFlags = new JButton("Manage Custom Flags");
		btnManageCustomFlags.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_btnManageCustomFlags = new GridBagConstraints();
		gbc_btnManageCustomFlags.insets = new Insets(0, 0, 5, 5);
		gbc_btnManageCustomFlags.gridx = 3;
		gbc_btnManageCustomFlags.gridy = 1;
		topPanel.add(btnManageCustomFlags, gbc_btnManageCustomFlags);
		btnManageCustomFlags.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					ManageCustomFlags manage = new ManageCustomFlags(flags);
				}
			}
		);
		
		chckbxRead = new JCheckBox("Read");
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
					if(!chckbxUnread.isSelected() && !chckbxRead.isSelected())
					{
						hiddenMessages.addAll(displayedMessages);
						displayedMessages = new ArrayList<Message>();
						createTable(displayedMessages);
					}
					displayMessages(Flags.Flag.SEEN, chckbxRead.isSelected(), false);			
				}
			}
		);
		
		JButton btnResetInbox = new JButton("Reset Inbox");
		btnResetInbox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_btnResetInbox = new GridBagConstraints();
		gbc_btnResetInbox.insets = new Insets(0, 0, 5, 5);
		gbc_btnResetInbox.gridx = 3;
		gbc_btnResetInbox.gridy = 2;
		topPanel.add(btnResetInbox, gbc_btnResetInbox);
		btnResetInbox.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					ArrayList<Message> newMessages = client.getInbox();
					reproduceTable(newMessages);			
				}
			}
		);
		
		chckbxUnread = new JCheckBox("Unread");
		GridBagConstraints gbc_chckbxUnread = new GridBagConstraints();
		gbc_chckbxUnread.anchor = GridBagConstraints.WEST;
		gbc_chckbxUnread.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxUnread.gridx = 1;
		gbc_chckbxUnread.gridy = 3;
		topPanel.add(chckbxUnread, gbc_chckbxUnread);
		chckbxUnread.setSelected(true);
		chckbxUnread.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					if(!chckbxUnread.isSelected() && !chckbxRead.isSelected())
					{
						hiddenMessages.addAll(displayedMessages);
						displayedMessages = new ArrayList<Message>();
						createTable(displayedMessages);
					}
					displayMessages(Flags.Flag.SEEN, chckbxUnread.isSelected(), true);			
				}
			}
		);
		
		JButton btnExit = new JButton("Exit");
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_btnExit = new GridBagConstraints();
		gbc_btnExit.insets = new Insets(0, 0, 10, 0);
		gbc_btnExit.gridx = 3;
		gbc_btnExit.gridy = 3;
		topPanel.add(btnExit, gbc_btnExit);
		btnExit.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					frmEmailClient.dispose();
					client.close();
					System.exit(0);
				}
			}
		);
		//the top panel is now finished
		
		//add the scrollable table to the main panel
		currentTable = new JTable();
		createTable(displayedMessages);
		scrollPane = new JScrollPane(currentTable);
		mainPanel.add(scrollPane);
	}
}
