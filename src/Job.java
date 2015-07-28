import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

class JobException extends Exception {

	public JobException(String message) {
		super(message);
	}

}

public class Job {

	public ArrayList<String> variablesList;
	public String email;
	public int count;

	public Job(ArrayList<String> variablesList, int jobCount)
			throws JobException {
		Pattern pattern = Pattern
				.compile("^[A-Za-z0-9](([_\\.\\-]?[a-zA-Z0-9]+)*)@([A-Za-z0-9]+)(([\\.\\-]?[a-zA-Z0-9]+)*)\\.([A-Za-z]{2,})$");
		this.email = variablesList.stream()
				.filter(v -> pattern.matcher(v.trim()).matches()).findFirst()
				.orElse(null);
		if (this.email == null)
			throw new JobException("Email was't found in the variables");

		variablesList.remove(this.email);
		this.variablesList = variablesList;
		this.count = jobCount;
	}

	private File generatePDF(String content, String attachmentsLocation) throws DocumentException, IOException {
		File coverFile = new File(attachmentsLocation + "CoverLetter.pdf");
		OutputStream file = new FileOutputStream(coverFile);
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, file);
		document.open();
		InputStream is = new ByteArrayInputStream(content.getBytes());
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
		document.close();
		file.close();
		return coverFile;
	}

	public void sendEmail(Session session, String subjectContent,
			String messageContent, String coverContent,
			boolean useCover, String fromEmail,
			ArrayList<File> attachmentsLocations, String attachmentsLocation)
			throws MessagingException, DocumentException, IOException {

		for (int i = 0; i < variablesList.size(); i++) {
			String regex = "%\\{" + i + "\\}%";
			messageContent = messageContent.replaceAll(regex,
					variablesList.get(i));
			subjectContent = subjectContent.replaceAll(regex,
					variablesList.get(i));
			if (useCover)
				coverContent = coverContent.replaceAll(regex,
						variablesList.get(i));
		}

		Message message = new MimeMessage(session);
		message.setHeader("Content-Type", "text/html; charset=UTF-8");
		message.setFrom(new InternetAddress(fromEmail));
		message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(email));
		message.setSubject(subjectContent);

		Multipart multipart = new MimeMultipart();

		// HTML Part
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(messageContent, "text/html");
		multipart.addBodyPart(messageBodyPart);

		// Cover part
		File coverFile = null;
		if (useCover) {
			coverFile = generatePDF(coverContent, attachmentsLocation);
			attachmentsLocations.add(coverFile);
		}
		// Attachments Part
		for (File aL : attachmentsLocations) {
			BodyPart attachmentsPart = new MimeBodyPart();
			DataSource source = new FileDataSource(aL);
			attachmentsPart.setDataHandler(new DataHandler(source));
			attachmentsPart.setFileName(aL.getName());
			multipart.addBodyPart(attachmentsPart);
		}

		message.setContent(multipart);
		Transport.send(message);

		if (useCover) {
			coverFile.delete();
			attachmentsLocations.remove(coverFile);
		}

	}
}
