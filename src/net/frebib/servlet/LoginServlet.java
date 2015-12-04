package net.frebib.servlet;

import net.frebib.mail.SMTPProvider;
import net.frebib.mail.SendProvider;

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //resp.getOutputStream().println("Login page");
        redirToLogin(req, resp);
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (isLoggedIn(req)) {
            resp.sendRedirect("/compose");
            return;
        }

        SendProvider send;
        LoginError err = null;
        Properties props = new Properties();

        if (req.getParameter("host").isEmpty()) {
            err = new LoginError("Provide a host!", "You must provide a host.");
        } else if (req.getParameter("email").isEmpty()) {
            err = new LoginError("Provide an email address!", "You must provide an email address of " +
                    "the mail account you want to send from.");
        } else if (req.getParameter("password").isEmpty()) {
            err = new LoginError("Provide a password", "You must provide a login password");
        } else {

            props.setProperty("mail.smtp.host", req.getParameter("host"));
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
                req.getSession().setAttribute(SENDER_ATTR, send);
                resp.sendRedirect("/compose");
                return;
            } catch (AuthenticationFailedException e) {
                err = new LoginError("Error Authenticating!", "Either the username or password you supplied was incorrect." +
                        "<br/>Please try again.");
            } catch (NoSuchProviderException e) {
                err = new LoginError("Invalid Hostname!", e.getMessage());
            } catch (Exception e) {
                err = new LoginError("Error Connecting!", e.getMessage());
            }
        }

        // Redirect to login page and show error
        req.getSession().setAttribute(LoginError.ERR_OBJ_ID, err);
        redirToLogin(req, resp);
    }
    private boolean isLoggedIn(HttpServletRequest req) {
        Object o = req.getSession().getAttribute(SENDER_ATTR);
        return !(o == null || !(o instanceof SendProvider)) && ((SendProvider) o).isConnected();
    }

    private void redirToLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getServletConfig()
                .getServletContext()
                .getRequestDispatcher("/WEB-INF/jsp/login.jsp")
                .include(req, resp);
    }
}
