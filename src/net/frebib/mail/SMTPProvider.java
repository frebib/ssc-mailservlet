package net.frebib.mail;

import javax.mail.*;
import java.io.IOException;
import java.util.Properties;

/**
 * Implements a {@link SendProvider} to send emails via the {@code SMTP} protocol
 */
public class SMTPProvider implements SendProvider {
    private final Properties props;
    private final Session session;
    private Transport transport;

    /**
     * Creates an {@code SMTPPRovider} with the {@link Properties} containing login details
     * @param props login details properties
     */
    public SMTPProvider(Properties props) throws NoSuchProviderException {
        if (props == null)
            throw new IllegalArgumentException("Properties cannot be null");

        props.setProperty("mail.smtp.timeout", "10000");
        this.props = props;
        this.session = Session.getInstance(props);
        this.transport = session.getTransport("smtp");
    }

    @Override
    public Transport connect() throws MessagingException {
        if (!transport.isConnected())
            transport.connect(props.getProperty("mail.smtp.host"),
                              Integer.parseInt(props.getProperty("mail.smtp.port")),
                              props.getProperty("mail.user"),
                              props.getProperty("mail.password")
            );
        return transport;
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public Transport getTransport() {
        return transport;
    }

    @Override
    public boolean isConnected() {
        return transport != null && transport.isConnected();
    }

    @Override
    public Message send(UnsentEmail email) throws IOException, MessagingException {
        Message msg = email.prepare(session);
        transport.sendMessage(msg, msg.getAllRecipients());
        return msg;
    }
}
