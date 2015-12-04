package net.frebib.servlet;

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
            new LoginError("Logged Out", "You logged out successfully.<br/>Please log back in to send more emails")
                    .setError(req.getSession());
            SessionManager.getFromSession(req.getSession()).dispose(req.getSession());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            resp.sendRedirect("/login");
        }
    }
}
