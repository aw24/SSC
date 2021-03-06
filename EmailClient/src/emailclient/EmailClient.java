package emailclient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import com.sun.mail.imap.IMAPFolder;

/**
 * Application to search and filter an inbox aswell as composing new emails
 * @author Ashley Wyatt
 *
 */

public class EmailClient 
{
	private String username;
	private String password;
	private Session session;
	private IMAPFolder folder;
	private Store store;
		
	/**
	 * Ask for password to begin
	 */
	
	public EmailClient()
	{
		username = "ssc.emailer@gmail.com";
		password = "";	        

		//get password
		JPasswordField pwd = new JPasswordField(10);  
		int action = JOptionPane.showConfirmDialog(null, pwd,"Enter Password",JOptionPane.OK_CANCEL_OPTION);  
		if(action < 0) 
		{
			JOptionPane.showMessageDialog(null,"Cancel, X or escape key selected"); 
			System.exit(0); 
		}
		else 
		{
			password = new String(pwd.getPassword());  
		}
	}
	
	public String getUserName()
	{
		return username;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public Session getSession()
	{
		return session;
	}
	
	/**
	 * Establish the mail session and connect
	 * @return
	 * @throws MessagingException
	 */
	
	public Store store() throws MessagingException
	{
		Store store = null;
		
		//set mail user properties
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		
		props.setProperty("mail.user", username);
		props.setProperty("mail.password", password);
		
		props.put("mail.smtp.auth",  "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port",  "587");
		
		//establish mail session
		session = Session.getDefaultInstance(props);

		// Step 2: Get the Store object from the mail session
		// A store needs to connect to the IMAP server  
		store = session.getStore("imaps");
		store.connect("imap.googlemail.com",username, password);
		return store;
	}
		
	/**
	 * Get the message
	 * @return
	 */
	
	public ArrayList<Message> getInbox()
	{
		folder = null;
		store = null;
		Message[] messages = null;
		
		try
		{
			store = store();
			//select inbox folder
			folder = (IMAPFolder) store.getFolder("inbox"); 
	
			//open inbox;
			if(!folder.isOpen())
				folder.open(Folder.READ_WRITE);
	
			messages = folder.getMessages();
		}
		catch(MessagingException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
			e.printStackTrace();
		}
		
		//reverse message array so that they are output in date order
		ArrayList<Message> outputMessages = new ArrayList<Message>(Arrays.asList(messages));
		Collections.reverse(outputMessages);
		return outputMessages;
	}
	
	public void close()
	{
		try
		{
			if(folder!=null && folder.isOpen())
			{
				folder.close(true);
			}
			if(store!=null)
			{
				store.close();
			}
		}
		catch(MessagingException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
			e.printStackTrace();
		}

	}

}
