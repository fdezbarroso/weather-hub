package user;

import android.app.ProgressDialog;
import android.os.StrictMode;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class mailManager {
    private String email = "informa.weatherhub@gmail.com";
    private String password = "gpossxylhudgsahm";
    private String subject;
    private String receiver;
    private String body;
    private Properties mProperties;
    private Session mSession;
    private MimeMessage mEmail;

    public mailManager(String receiver, String subject, String body) {
        this.subject = subject;
        this.receiver = receiver;
        this.body = body;
    }

    public void createEmail() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mProperties = new Properties();
        mProperties.put("mail.smtp.host", "smtp.gmail.com");
        mProperties.put("mail.smtp.auth", "true");
        mProperties.put("mail.smtp.starttls.enable", "true");
        try {
        mSession = Session.getDefaultInstance(mProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });
        mEmail = new MimeMessage(mSession);
        mEmail.setFrom(new InternetAddress(email));
        mEmail.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
        mEmail.setSubject(subject);
        mEmail.setContent(body, "text/html; charset=utf-8");
        Transport t = mSession.getTransport("smtp");
        t.connect("smtp.gmail.com", 587, email, password);
        t.sendMessage(mEmail, mEmail.getAllRecipients());
        t.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
