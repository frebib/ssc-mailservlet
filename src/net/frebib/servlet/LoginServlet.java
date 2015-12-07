package net.frebib.servlet;

import net.frebib.mail.SMTPProvider;
import net.frebib.mail.SendProvider;
import net.frebib.view.PopupMessage;

import javax.mail.AuthenticationFailedException;
import javax.mail.NoSuchProviderException;
import javax.mail.Transport;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final String SENDER_ATTR = "send-provider";

    @Override
    /**
     * Handle get for /login
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        redirToLogin(req, resp);
    }

    /**
     * Handle post for /login
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SendProvider send;
        Properties props = new Properties();

        SessionManager sessionMgr = SessionManager.getFromSession(req.getSession());

        if (sessionMgr != null && !sessionMgr.isExpired()) {
            resp.sendRedirect("/compose");
            return;
        } else

        if (req.getParameter("server").isEmpty()) {
            PopupMessage.set("Provide a host!", "You must provide a sending server address.", "login", req.getSession());
        } else if (req.getParameter("email").isEmpty()) {
            PopupMessage.set("Provide an email address!", "You must provide an email address of the mail account you want" +
                    " to send from.", "login", req.getSession());
        } else if (req.getParameter("password").isEmpty()) {
            PopupMessage.set("Provide a password", "You must provide a login password", "login", req.getSession());
        } else {

            props.setProperty("mail.smtp.host", req.getParameter("server"));
            props.setProperty("mail.user", req.getParameter("email"));
            props.setProperty("mail.password", req.getParameter("password"));
            props.setProperty("mail.smtp.port", req.getParameter("port"));
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.auth", "true");

            try {
                send = new SMTPProvider(props);
                Transport t = send.connect();

                // Successfully connected.

                // Store the session & redirect
                // Set the warn/expiry times to 4/5 minutes respectively
                new SessionManager(send, 5 * 60, 4 * 60).storeInSession(req.getSession());
                SendServlet.clearValues(req.getSession());

                resp.sendRedirect("/compose");
                return;
            } catch (AuthenticationFailedException e) {
                PopupMessage.set("Error Authenticating!", "Either the username or password you supplied was " +
                        "incorrect.<br/>Please try again.", "login", req.getSession());
            } catch (NoSuchProviderException e) {
                PopupMessage.set("Invalid Hostname!", e.getMessage(), "login", req.getSession());
            } catch (Exception e) {
                PopupMessage.set("Error Connecting!", e.getMessage(), "login", req.getSession());
            }
        }

        redirToLogin(req, resp);
    }

    private void redirToLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").include(req, resp);
    }
}
