MailSender / Andrew Kamel

This application is used to gracefully spam companies looking for a new job.
It allows sending a personal email to each company and modify the message, attachments based on the company.

The User interface is quite straight forward

+Gmail 
	-Username : User name for the Gmail account to be used, Full email is needed.
	-Password : Password for the account.
+Files
	-Jobs : An CSV file that contains a comma separated variables, the email of the company is the only required variable that has to exist for each row.
	-Message : An HTML file that contains the text for the email. the variables can be replaced here by using %{VariableIndex}%
	example : let's assume each row in the Jobs file has the location,email and company name
	so, in order to use the location use %{0}% in the message file. In order to use the company name, use %{1}% (the email is not counted within the variable indeces)
+Attachments : A folder where all the files in would be attached to the email.
+Cover Letter : An HTML file that would be looked for variables, replace them then generate a PDF file and attach it to the email.
+Subject : The text to be used as a subject for the email, Variables can be included here as well.
Load Jobs : Load the jobs file and initialize needed resources
Send : Start sending to all the emails found.


Kindly check the attached samples for each of the used files.
Happy hunting !