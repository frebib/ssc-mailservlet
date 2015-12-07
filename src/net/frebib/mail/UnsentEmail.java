package net.frebib.mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains all the data for an email before it is sent by a {@link SendProvider}
 */
public class UnsentEmail {
    private Address[] to, cc, bcc;
    private String subject, body;
    private List<File> attachments;

    public UnsentEmail(Address[] to, String body, String subject) {
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.attachments = new ArrayList<>();
    }

    public void setCc(Address[] cc) {
        this.cc = cc;
    }
    public void setBcc(Address[] bcc) {
        this.bcc = bcc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<File> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<File> attachments) {
        this.attachments = attachments;
    }

    /**
     * Converts the {@code UnsentEmail} to a {@link Message} ready to be sent
     * @param session the mail {@link Session} to get the sender details from
     * @return a prepared {@link Message} containing all of the data from this {@code UnsentEmail}
     */
    public Message prepare(Session session) throws MessagingException, IOException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(session.getProperties().getProperty("mail.user")));
        message.setRecipients(Message.RecipientType.TO, to);
        message.setRecipients(Message.RecipientType.CC, cc);
        message.setRecipients(Message.RecipientType.BCC, bcc);
        message.setSubject(subject);

        Multipart mp = new MimeMultipart();

        BodyPart content = new MimeBodyPart();
        content.setContent(body, "text/plain");
        mp.addBodyPart(content);

        for (File f : attachments) {
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.attachFile(f);
            mp.addBodyPart(mbp);
        }

        message.setContent(mp);

        return message;
    }
}
