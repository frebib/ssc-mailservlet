package net.frebib.mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import java.io.IOException;

public interface SendProvider {
    Transport connect() throws MessagingException;
    Session getSession();
    Transport getTransport();
    boolean isConnected();
    Message send(UnsentEmail email) throws IOException, MessagingException;
}
