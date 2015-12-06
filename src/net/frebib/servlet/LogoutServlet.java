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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            SessionManager.getFromSession(req.getSession()).dispose(req.getSession());
            // Send a pretty popup message
            PopupMessage.set("Logged Out", "You logged out successfully.<br/>Please log back in to send more emails",
                    "login", req.getSession());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            resp.sendRedirect("/login");
        }
    }
}
