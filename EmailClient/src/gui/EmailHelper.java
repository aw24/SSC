package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Flags.Flag;
import javax.mail.Message.RecipientType;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class EmailHelper 
{
	/**
	 * Insert new row(s) at the top of the JTable
	 * @param messages The list of messages to be inserted
	 */
	
	public void addRows(DefaultTableModel model, ArrayList<Message> messages, HashMap<String, String> flags)
	{
		for(Message message : messages)
		{
			try 
			{
				model.insertRow(0,new Object[]{message.getReceivedDate(), message.getSubject(), assignCustomFlags(message, flags)});
			}
			catch (MessagingException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			System.out.println("Added an Email");
		}
	}
	
	/**
	 * Adds the listeners to the table rows in the new JTable so that the message row that is clicked causes the message to be displayed in a new frame
	 * @param messages The messages that are in the table
	 * @return
	 */
	
	
	public void addListeners(JTable currentTable, ArrayList<Message> messages)
	{
		//make it so that when a row is clicked, then the email corresponding to the row is opened
		currentTable.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				JTable source = (JTable) e.getSource();
				//find the row that was clicked
				int row = source.rowAtPoint(e.getPoint());
				//if double clicked
				if(e.getClickCount() ==2) {
					//get the email subject of the row that was clicked
					String messageSubject = (String) source.getModel().getValueAt(row,1);
					System.out.println(messageSubject);
					Date messageDate = (Date) source.getModel().getValueAt(row,0);
					System.out.println(messageDate.toString());
					for(int i = 0; i < messages.size(); i++)
					{
						Message current = messages.get(i);
						try {
							if(current.getSubject().equals("hey there"))
							{
								System.out.println("Added listener");
							}
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
	}
	
	/**
	 * Assigns custom flags to messages if they meet the criteria
	 * @param message The message to be checked
	 * @param flags The hashmap containing the custom flags and their criteria
	 * @return
	 */
	
	public String assignCustomFlags(Message message, HashMap<String, String> flags)
	{
		ArrayList<String> customFlags = new ArrayList<String>();
		
		Iterator iterator = flags.entrySet().iterator();
		while(iterator.hasNext())
		{
			HashMap.Entry<String, String> pair = (HashMap.Entry<String,String>)iterator.next(); 
			String flagName = pair.getKey();
			String ifContains = pair.getValue();
			boolean match = search(message, ifContains);
			if(match == true)
			{
				customFlags.add(flagName); 
			}
		}
		
		String containedFlags = "";
		
		for(int i = 0; i < customFlags.size(); i++)
		{
			if(i ==0)
			{
				containedFlags += customFlags.get(i);
			}
			else
			{
				containedFlags += ", " + customFlags.get(i);
			}
		}
		
		return containedFlags;
	}
	
	/**
	 * Searches an individual message for a keyword
	 * @param current The message
	 * @param searchterm The keyword
	 * @return
	 */
	
	public boolean search(Message current, String searchterm)
	{
		boolean read = true;
		boolean match = false;
		try 
		{
			Flags flags = current.getFlags();
			//decide whether the message should be displayed -- this is based upon whether it has a certain flag and if the checkbox was ticked
			if(!flags.contains(Flags.Flag.SEEN))
			{
				read = false;
			}
			if(current.getSubject().toLowerCase().contains((searchterm).toLowerCase()) || getEmailBody(current).toLowerCase().contains(searchterm.toLowerCase()))
			{
				match = true;
				System.out.println("Found a match!");
			}
			if(read == false)
			{
				current.setFlag(Flags.Flag.SEEN, false);
			}
		} 
		catch (MessagingException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return match;
	}
	
	/**
	 * Searches message subjects and contents currently in the inbox for a certain keyword that the user defined in the textfield
	 * @param messages The messages currently in the inbox
	 * @param searchterm The keyword entered by the user
	 * @return
	 */

	public ArrayList<Message> searchMessages(ArrayList<Message> messages, String searchterm)
	{
		ArrayList<Message> found = new ArrayList<Message>();
		System.out.println("Searching...");
		for(int i = 0; i < messages.size(); i++)
		{
			Message current = messages.get(i);
			if(search(current, searchterm)==true)
			{
				found.add(current);
			}
		}
		return found;
	}
	
	/**
	 * Sort the current inbox so that the most recent message are at the top 
	 * @param messages The messages currently in the inbox.
	 * @return
	 */
	
	public void sort(ArrayList<Message> messages)
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
	}
	
	/**
	 * Gets the body content of the email
	 * @param current The email of which you want to get the body content of
	 * @return
	 */
	
	
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
	 * Displays the messages that returned a match for the keyword entered by the user
	 * @param searchword The keyword entered into the textfield by the user
	 */
	
	public ArrayList<Message> displaySearchedMessages(ArrayList<Message> displayedMessages, String searchword)
	{
		ArrayList<Message> found = searchMessages(displayedMessages, searchword);
		sort(found);
		return found;
	}
	
	
	public JLabel align(JLabel label)
	{
		label.setHorizontalAlignment(SwingConstants.LEFT);
		return label;
		
	}
}
