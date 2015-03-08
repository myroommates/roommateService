package util.email.business;

import models.email.MailConfig;
import util.email.messages.EmailMessage;
import util.exception.MyRuntimeException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import play.Configuration;
import play.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;

@Component
public class EmailSender implements ApplicationContextAware {

    // Application context aware
    private static ApplicationContext ctx = null;

    public static ApplicationContext getApplicationContext() {
        return ctx;
    }

    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        // Assign the ApplicationContext into a static method
        EmailSender.ctx = ctx;
    }

    private static final String MAIL_SMTP_AUTH_KEY = "mail.smtp.auth";
    private static final String MAIL_SMTP_STARTTLS_KEY = "mail.smtp.starttls.enable";
    private static final String MAIL_SMTP_HOST_KEY = "mail.smtp.host";
    private static final String MAIL_SMTP_PORT_KEY = "mail.smtp.port";

    public EmailSender() throws IOException {
        MailConfig.loadConfigurations();
    }


    /**
     * Private helper invoked by the actors that sends the email
     *
     * @param email the email message
     */

    public void sendEmail(EmailMessage email) throws MessagingException, UnsupportedEncodingException {
        Logger.info("Sending email ...");
        final String username = MailConfig.username;
        // mail.smpt.password must be define in conf/application.conf
        final String password = Configuration.root().getString("mail.smtp.password");

        Properties props = new Properties();
        props.put(MAIL_SMTP_AUTH_KEY, MailConfig.smtpAuth);
        props.put(MAIL_SMTP_STARTTLS_KEY, MailConfig.starttlsEnable);
        props.put(MAIL_SMTP_HOST_KEY, MailConfig.smtpHost);
        props.put(MAIL_SMTP_PORT_KEY, MailConfig.smtpPort);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        Logger.info("Sending email ...1 : "+password+"/"+MailConfig.fromAddress);

        try {

            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(MailConfig.fromAddress));
            for (String address : email.getToAddress()) {
                mimeMessage.addRecipients(Message.RecipientType.TO, InternetAddress.parse(address));
            }
            if (email.getToCC() != null) {
                for (String cc : email.getToCC()) {
                    mimeMessage.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
                }
            }
            mimeMessage.setSubject(MimeUtility.encodeText(email.getSubject(), "utf-8", "B"));

            Logger.info("Sending email ...2");

            // main body part -> email content
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(email.getContent(), "text/html; charset=utf-8");

            // multipart
            Multipart multipart = new MimeMultipart();

            Logger.info("Sending email ...3");

            // add mainbodypart to multipart
            multipart.addBodyPart(messageBodyPart);

            // check if attachments
            if (email.getAttachmentFilenameList() != null) {
                // for each attachment
                for (Map.Entry<String, ByteArrayOutputStream> fileName : email.getAttachmentFilenameList().entrySet())
                    try {
                        addAttachment(multipart, fileName.getKey(), fileName.getValue());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                // end for
            } // end if

            Logger.info("Sending email ...4");

            // check if attachments
            if (email.getByteArrayinputStreamList() != null) {
                // for each attachment
                for (Map.Entry<String, ByteArrayInputStream> fileName : email.getByteArrayinputStreamList().entrySet())
                    try {
                        addAttachment(multipart, fileName.getKey(), fileName.getValue());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                // end for
            } // end if


            Logger.info("Sending email ...5");

            // add multipart to message
            mimeMessage.setContent(multipart);

            // send message
            Transport.send(mimeMessage);

            Logger.info("Email Successfully sent to " + email.getToAddress());

        } catch (MessagingException e) {
            Logger.info("Sending email fail : "+e.getMessage());
            e.printStackTrace();
            throw e;
        }

    }

    // add attachment
    private static void addAttachment(Multipart multipart, String name, ByteArrayOutputStream out) throws IOException, MessagingException {

        if (out == null || name == null) {
            throw new MyRuntimeException("out or name is null:" + name + "/" + out);
        }
        Logger.info(out.toByteArray() + "");

        MimeBodyPart attachmentPart = new MimeBodyPart();

        Logger.info(new ByteArrayInputStream(out.toByteArray()) + "");

        DataSource aAttachment = new ByteArrayDataSource(new ByteArrayInputStream(out.toByteArray()), "application/octet-stream");
        attachmentPart.setDataHandler(new DataHandler(aAttachment));
        //attachmentPart.attachFile(name);
        attachmentPart.setFileName(name);
        multipart.addBodyPart(attachmentPart);
    }

    // add attachment based on ByteArrayInputStream
    private static void addAttachment(Multipart multipart, String name, ByteArrayInputStream in) throws IOException, MessagingException {

        if (in == null || name == null) {
            throw new MyRuntimeException("in or name is null:" + name + "/" + in);
        }

        Logger.info(in.toString());

        int length = in.available();
        byte[] buff = new byte[length];
        in.read(buff);

        Logger.info("lenght:" + length);

        Field f = null;
        byte[] buf = null;
        try {
            f = ByteArrayInputStream.class.getDeclaredField("buf");
            f.setAccessible(true);
            buf = (byte[]) f.get(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        MimeBodyPart attachmentPart = new MimeBodyPart();
        DataSource aAttachment = new ByteArrayDataSource(buf, "application/octet-stream");
        attachmentPart.setDataHandler(new DataHandler(aAttachment));
        attachmentPart.setFileName(name);
        multipart.addBodyPart(attachmentPart);
    }
}
