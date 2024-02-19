//import java.util.Properties;
//import javax.mail.*;
//import javax.mail.internet.*;
//
//public class EmailSender {
//
//    public static void sendEmail(String recipientEmail, String subject, String body) throws MessagingException {
//        // Set up mail server properties
//        Properties properties = new Properties();
//        properties.put("mail.smtp.host", "your_mail_host");
//        properties.put("mail.smtp.port", "your_mail_port");
//        properties.put("mail.smtp.auth", "true"); // Enable authentication
//        properties.put("mail.smtp.starttls.enable", "true"); // Enable TLS encryption
//
//        // Set up authentication
//        Authenticator authenticator = new Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication("your_email", "your_password");
//            }
//        };
//
//        // Create a mail session
//        Session session = Session.getInstance(properties, authenticator);
//
//        // Create a message
//        Message message = new MimeMessage(session);
//        message.setFrom(new InternetAddress("your_email"));
//        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
//        message.setSubject(subject);
//        message.setText(body);
//
//        // Send the message
//        Transport.send(message);
//        System.out.println("Email sent successfully.");
//    }
//
//    public static void main(String[] args) {
//        try {
//            sendEmail("recipient@example.com", "Test Email", "This is a test email sent from Java.");
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }
//}
