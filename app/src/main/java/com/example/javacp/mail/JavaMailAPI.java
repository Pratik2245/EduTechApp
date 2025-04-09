package com.example.javacp.mail;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailAPI extends AsyncTask<Void, Void, String> {

    private Context context;
    private String email, subject, message;
    private final String senderEmail = "your-email@gmail.com"; // your email
    private final String senderPassword = "your-app-password"; // use App Password here

    public JavaMailAPI(Context context, String email, String subject, String message) {
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            });

            MimeMessage mm = new MimeMessage(session);
            mm.setFrom(new InternetAddress(senderEmail));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mm.setSubject(subject);
            mm.setText(message);

            Transport.send(mm);
            return "Success";

        } catch (MessagingException e) {
            e.printStackTrace();
            return "Failed: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if ("Success".equals(result)) {
            Toast.makeText(context, "Email Sent", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        }
    }
}
