package emailclient;

import java.util.ArrayList;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMessage 
{
	private MimeMessage message;
	
	public SendMessage(String[] messageData, ArrayList<String> files, EmailClient client) throws AddressException, MessagingException
	{
		//get session
		Session session = client.getSession();
		
		//add textual data to the message
		message = new MimeMessage(session);
		message.setFrom(new InternetAddress(client.getUserName()));
		message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(messageData[0]));
		message.setRecipients(Message.RecipientType.CC,
				InternetAddress.parse(messageData[1]));
		message.setSubject(messageData[2]);
		
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setText(messageData[3]);
		
		Multipart multipart = new MimeMultipart();
		String[] attachments = files.toArray(new String[files.size()]);
		multipart.addBodyPart(messageBodyPart);
		messageBodyPart = new MimeBodyPart();
		
		//add the file attachments to the multipart
		for(int i = 0; i < attachments.length; i++)
		{
			System.out.println(attachments[i]);
			DataSource source = new FileDataSource(attachments[i]);

	        messageBodyPart.setDataHandler(new DataHandler(source));

	        messageBodyPart.setFileName(attachments[i]);

	        multipart.addBodyPart(messageBodyPart);
		}
		
		//add the multipart to the message and save changes
		message.setContent(multipart);
		message.saveChanges();

		//send message			
		Transport tr = session.getTransport("smtp");	// Get Transport object from session		
		tr.connect("imap.googlemail.com", client.getUserName(), client.getPassword()); // We need to connect
		tr.sendMessage(message, message.getAllRecipients()); // Send message
	}

}
