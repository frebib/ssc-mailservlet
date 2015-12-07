package net.frebib.servlet;

import net.frebib.view.PopupMessage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    /**
     * Logs the user out, resets the session and redirects to /login
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
    @Override
    /**
     * Logs the user out, resets the session and redirects to /login
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Send a pretty popup message
            PopupMessage.set("Logged Out", "You logged out successfully.<br/>Please log back in to send more emails",
                    "login", req.getSession());

            // Clear the logged-in mail session
            SessionManager sessionMgr = SessionManager.getFromSession(req.getSession());
            if (sessionMgr != null)
                sessionMgr.dispose(req.getSession());

            // Clear the web session
            //## !This will prevent the pretty message from showing, so don't!
            //req.getSession().invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Redirect back to the login page
            resp.sendRedirect("/login");
        }
    }
}
