package net.frebib.mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import java.io.IOException;

/**
 * Provides a method to connect to a mail server and send emails
 */
public interface SendProvider {

    /**
     * Connects to the mail server
     * @return the {@link Transport} for sending emails
     * @throws MessagingException thrown when connecting fails
     */
    Transport connect() throws MessagingException;

    /**
     * Returns the mail {@link Session}
     * @return the mail {@code Session}
     */
    Session getSession();

    /**
     * Returns the send {@link Transport}
     * @return the {@code Transport}
     */
    Transport getTransport();

    /**
     * Gets whether the send session is connected
     * @return true if connected
     */
    boolean isConnected();

    /**
     * Pushes an {@link UnsentEmail} to the {@code SendProvider} and requests it to be sent
     * @param email email to send
     * @return the {@link Message} object linking to the sent email
     */
    Message send(UnsentEmail email) throws IOException, MessagingException;
}
