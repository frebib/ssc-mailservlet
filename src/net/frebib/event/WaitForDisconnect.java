package net.frebib.event;

import net.frebib.servlet.SessionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(value = "/waitforexpiry", asyncSupported = true)
public class WaitForDisconnect extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/event-stream");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter writer = resp.getWriter();

        SessionManager sessionMgr = SessionManager.getFromSession(req.getSession());
        if (sessionMgr == null) {
            // Unlikely to happen but, you know, computers, man...
            writer.print("event:expire\n\ndata:There is no active session. Please log in again.");
            writer.flush();
            return;
        }

        else if (sessionMgr.isExpired()) {
            writer.print("event:expire\n\ndata:Your session has already expired. Please log in again.");
            writer.flush();
            return;
        }

        sessionMgr.onWarn(s -> {
                    writer.write("event:warn\ndata:" + s + "\n\n");
                    writer.flush();
                }).onExpire(s -> {
                    writer.write("event:expire\ndata:" + s + "\n\n");
                    writer.flush();
                    writer.close();
                    synchronized (this) {
                        this.notifyAll();
                    }
                });

        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) { } // Eh, whatever
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionManager sessionMgr = SessionManager.getFromSession(req.getSession());

        // Prevent session from dying
        if (sessionMgr != null)
            sessionMgr.keepAlive();
    }
}
