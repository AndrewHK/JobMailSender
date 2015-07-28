import java.awt.EventQueue;

import javax.mail.MessagingException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JTextArea;

import com.itextpdf.text.DocumentException;
import javax.swing.JPasswordField;

public class MainApp extends JFrame {

	private JPanel contentPane;
	private JTextField emailTF;
	private JTextField jobsTF;
	private JTextField msgTF;
	private JTextField attachsTF;
	private JTextField coverTF;
	private JTextField subjectTF;

	private JButton sendBtn;
	private JTextArea logTA;

	private MailSender mailSender;
	private ArrayList<Job> jobsList;
	private ArrayList<File> attachmentsLocations;
	private String attachDir;
	private JPasswordField passTF;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainApp frame = new MainApp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainApp() {
		setTitle("Mail Sender");
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 502, 680);
		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		emailTF = new JTextField();
		emailTF.setBounds(150, 31, 225, 28);
		contentPane.add(emailTF);
		emailTF.setColumns(10);

		JLabel lblGmail = new JLabel("Gmail");
		lblGmail.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblGmail.setBounds(17, 7, 61, 16);
		contentPane.add(lblGmail);

		JSeparator separator = new JSeparator();
		separator.setBounds(6, 99, 490, 16);
		contentPane.add(separator);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setToolTipText("Full Gmail Email");
		lblEmail.setBounds(64, 37, 61, 16);
		contentPane.add(lblEmail);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setToolTipText("Gmail Password");
		lblPassword.setBounds(64, 65, 61, 16);
		contentPane.add(lblPassword);

		JLabel lblFiles = new JLabel("Files");
		lblFiles.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblFiles.setBounds(16, 115, 61, 16);
		contentPane.add(lblFiles);

		JLabel lblJobsFile = new JLabel("Jobs File");
		lblJobsFile.setToolTipText("Extension should be .CSV");
		lblJobsFile.setBounds(63, 137, 61, 16);
		contentPane.add(lblJobsFile);

		JLabel lblMessageFile = new JLabel("Message File");
		lblMessageFile.setToolTipText("Extension should be .HTML");
		lblMessageFile.setBounds(63, 165, 80, 16);
		contentPane.add(lblMessageFile);

		jobsTF = new JTextField();
		jobsTF.setText("jobs/jobs.csv");
		jobsTF.setBounds(150, 131, 225, 28);
		contentPane.add(jobsTF);
		jobsTF.setColumns(10);

		msgTF = new JTextField();
		msgTF.setText("message/msg.html");
		msgTF.setBounds(150, 159, 225, 28);
		contentPane.add(msgTF);
		msgTF.setColumns(10);

		JButton jobsBtn = new JButton("Browse");
		jobsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnVal = fc.showOpenDialog(contentPane);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					jobsTF.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		jobsBtn.setBounds(376, 132, 117, 29);
		contentPane.add(jobsBtn);

		JButton msgBtn = new JButton("Browse");
		msgBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnVal = fc.showOpenDialog(contentPane);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					msgTF.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		msgBtn.setBounds(376, 160, 117, 29);
		contentPane.add(msgBtn);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(6, 193, 487, 16);
		contentPane.add(separator_1);

		JLabel lblAttachmentsDirectory = new JLabel("Attachments Dir");
		lblAttachmentsDirectory
				.setToolTipText("It will attach all files in this directory");
		lblAttachmentsDirectory.setBounds(16, 217, 127, 16);
		contentPane.add(lblAttachmentsDirectory);

		attachsTF = new JTextField();
		attachsTF.setText("attachments/");
		attachsTF.setColumns(10);
		attachsTF.setBounds(150, 211, 225, 28);
		contentPane.add(attachsTF);

		JButton attachsBtn = new JButton("Browse");
		attachsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(contentPane);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					attachsTF.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		attachsBtn.setBounds(376, 212, 117, 29);
		contentPane.add(attachsBtn);

		JLabel lblCoverLetter = new JLabel("Cover Letter File");
		lblCoverLetter.setToolTipText("Extension should be .HTML");
		lblCoverLetter.setBounds(17, 300, 117, 16);
		contentPane.add(lblCoverLetter);

		coverTF = new JTextField();
		coverTF.setText("cover/cover.html");
		coverTF.setColumns(10);
		coverTF.setBounds(151, 294, 225, 28);
		contentPane.add(coverTF);

		JButton coverBtn = new JButton("Browse");
		coverBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnVal = fc.showOpenDialog(contentPane);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					coverTF.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		coverBtn.setBounds(377, 295, 117, 29);
		contentPane.add(coverBtn);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(6, 245, 487, 16);
		contentPane.add(separator_2);

		JCheckBox userCoverCB = new JCheckBox("Use Cover Letter");
		userCoverCB
				.setToolTipText("Check if you want to attach a generated Cover Letter");
		userCoverCB.setBounds(17, 265, 191, 23);
		contentPane.add(userCoverCB);

		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(6, 328, 487, 16);
		contentPane.add(separator_3);

		JLabel lblSubject = new JLabel("Subject");
		lblSubject.setToolTipText("Email Subject");
		lblSubject.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblSubject.setBounds(17, 340, 61, 16);
		contentPane.add(lblSubject);

		subjectTF = new JTextField();
		subjectTF.setBounds(17, 368, 476, 28);
		contentPane.add(subjectTF);
		subjectTF.setColumns(10);

		JSeparator separator_4 = new JSeparator();
		separator_4.setBounds(6, 408, 490, 16);
		contentPane.add(separator_4);

		logTA = new JTextArea();
		logTA.setEditable(false);
		logTA.setToolTipText("Progress Log");
		logTA.setBounds(6, 436, 490, 180);
		contentPane.add(logTA);

		JButton loadBtn = new JButton("Load Jobs");
		loadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = emailTF.getText();
				char[] password = passTF.getPassword();

				String msgFile = msgTF.getText();
				String coverFile = coverTF.getText();
				attachDir = attachsTF.getText();
				boolean useCover = userCoverCB.isSelected();
				try {
					mailSender = new MailSender(username, password, msgFile,
							coverFile, useCover);
				} catch (IOException ex) {
					logText(ex.getMessage());
				}
				String jobsFile = jobsTF.getText();
				String subject = subjectTF.getText();

				attachmentsLocations = new ArrayList<File>(Arrays
						.asList(new File(attachDir)
								.listFiles(new FilenameFilter() {
									String[] extensions = { ".doc", ".docx",
											".pdf", ".jpg", ".jpeg", ".png",
											".rtf", ".txt", ".xls", ".xlsx",
											".ppt", ".pptx" };

									@Override
									public boolean accept(File dir, String name) {
										for (String ext : extensions) {
											if (name.endsWith(ext))
												return true;
										}
										return false;
									}
								})));

				mailSender.setEmailSubject(subject);

				jobsList = new ArrayList<Job>();
				int jobCount = 0;

				try (BufferedReader bR = new BufferedReader(new FileReader(
						jobsFile))) {
					String line;
					while ((line = bR.readLine()) != null) {
						ArrayList<String> variablesList = new ArrayList<String>(
								Arrays.asList(line.split(",")));

						Job job = new Job(variablesList, jobCount++);
						jobsList.add(job);
					}
					logText(jobCount + " job(s) were loaded successfully");
					sendBtn.setEnabled(true);
				} catch (IOException | JobException ex) {
					logText(ex.getMessage());
				}

			}
		});
		loadBtn.setToolTipText("Load the Jobs file");
		loadBtn.setBounds(264, 628, 117, 29);
		contentPane.add(loadBtn);

		sendBtn = new JButton("Send");
		sendBtn.setEnabled(false);
		sendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int jobCount = 0;
				for (Job job : jobsList) {
					logText("(" + job.count + ") Sending Email to " + job.email);
					try {
						job.sendEmail(mailSender.getCurrSession(),
								mailSender.getEmailSubject(),
								mailSender.getHtmlEmailMessage(),
								mailSender.getCoverContent(),
								mailSender.useCover(),
								mailSender.getUsername(), attachmentsLocations,
								attachDir);
						jobCount++;
						logText("Email sent\n-----------");
					} catch (MessagingException | DocumentException
							| IOException ex) {
						logText(ex.getMessage());
					}
				}
				logText(jobCount + " email(s) sent successfully");

			}
		});
		sendBtn.setToolTipText("Send Emails");
		sendBtn.setBounds(379, 628, 117, 29);
		contentPane.add(sendBtn);

		passTF = new JPasswordField();
		passTF.setBounds(150, 59, 225, 28);
		contentPane.add(passTF);
	}

	public void logText(String text) {
		logTA.append("\n" + text);
	}
}
