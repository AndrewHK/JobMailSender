import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class MailSender {

	private String username;
	private char[] password;
	
	public final String defaultExtension = ".html";

	private String messageFileDir;
	private String coverFileDir;
	
	private String emailSubject;
	private Session currSession;
	private String htmlEmailMessage;
	private String coverContent;
	private boolean useCover;


	public String getMessageFileDir() {
		return messageFileDir;
	}

	public void setMessageFileDir(String messageFileDir) {
		this.messageFileDir = messageFileDir;
	}

	public String getCoverFileDir() {
		return coverFileDir;
	}

	public void setCoverFileDir(String coverFileDir) {
		this.coverFileDir = coverFileDir;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password.toCharArray();
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public Session getCurrSession() {
		return currSession;
	}

	public void setCurrSession(Session currSession) {
		this.currSession = currSession;
	}

	public String getHtmlEmailMessage() {
		return htmlEmailMessage;
	}

	public void setHtmlEmailMessage(String htmlEmailMessage) {
		this.htmlEmailMessage = htmlEmailMessage;
	}

	public String getCoverContent() {
		return coverContent;
	}

	public void setCoverContent(String coverContent) {
		this.coverContent = coverContent;
	}

	public boolean useCover() {
		return useCover;
	}

	public void setUseCover(boolean useCover) {
		this.useCover = useCover;
	}

	public MailSender(String username, char[] password, String messageFile, String coverFile, boolean useCover) throws IOException {
		this.messageFileDir = messageFile.substring(0,messageFile.indexOf(defaultExtension));
		this.coverFileDir = coverFile.substring(0,coverFile.indexOf(defaultExtension));
		this.username = username;
		this.password = password;
		this.useCover = useCover;
		
		htmlEmailMessage = getHTMLText(messageFileDir);
		currSession = getSession();		
		
		if(useCover)
			coverContent = getHTMLText(coverFileDir);
	}

	private Session getSession() {
		Properties gmailProps = new Properties();
		gmailProps.put("mail.smtp.auth", "true");
		gmailProps.put("mail.smtp.starttls.enable", "true");
		gmailProps.put("mail.smtp.host", "smtp.gmail.com");
		gmailProps.put("mail.smtp.port", "587");

		return Session.getInstance(gmailProps, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, new String(password));
			}
		});
	}

	private String getHTMLText(String filepath) throws FileNotFoundException,
			IOException, UnsupportedEncodingException {
		File file = new File(filepath + defaultExtension);
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[(int) file.length()];
		fis.read(data);
		fis.close();
		return new String(data, "UTF-8");
	}
}
