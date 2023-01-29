package com.example.sms2gmail.service;


import android.content.Context;
import android.widget.Toast;

import com.example.sms2gmail.R;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class GmailEmailService {

    final int SENDING_EMAIL  = R.string.sending_gmail;
    final int FORWARDING_EMAIL  = R.string.forwarding_email;
    final int GMAIL_APP_PASS  = R.string.gmail_app_pass;

    private final String senderEmail;
    private final String forwardEmail;
    private final String gmailAppPass;
    PreferenceRepository preferenceRepository;

    public GmailEmailService(PreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
        this.senderEmail = preferenceRepository.getSettings(SENDING_EMAIL);
        this.forwardEmail = preferenceRepository.getSettings(FORWARDING_EMAIL);
        this.gmailAppPass = preferenceRepository.getSettings(GMAIL_APP_PASS);;
    }


    public void sendEmail(Context context, String message){
        try {
            String stringHost = "smtp.gmail.com";
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, gmailAppPass);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(forwardEmail));

            mimeMessage.setSubject("SMS from SMS2EMAIL app");
            mimeMessage.setText(message);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            Toast.makeText(context, "SMS Forwarded!", Toast.LENGTH_SHORT).show();

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}