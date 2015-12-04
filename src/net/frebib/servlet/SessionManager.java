package net.frebib.servlet;

import net.frebib.event.SessionEvent;
import net.frebib.mail.SendProvider;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class SessionManager extends TimerTask {
    private static final String SESSION_MGR = "session-manager";

    private SendProvider sender;

    private Timer timer;
    private int expireTime, warnTime;
    private SessionEvent<String> onExpire, onWarn;

    private boolean hasTerminated;


    /**
     * Manages a SendProvider session and times out after a specified
     * time, disconnecting the session and notifying the listener
     *
     * @param sender     a {@link SendProvider} to manage
     * @param expireTime timeout to wait before disconnecting the session and notifying the listener
     */
    public SessionManager(SendProvider sender, int expireTime) {
        this.sender = sender;
        // Starts the timers
        keepAlive();
    }

    /**
     * Manages a SendProvider session and times out after a specified
     * time, disconnecting the session and notifying the listener with
     * an intermediary callback as a pre-disconnection warning.
     *
     * @param sender     a {@link SendProvider} to manage
     * @param warnTime   timeout to wait before sending a warning to the callback
     * @param expireTime timeout to wait before disconnecting the session and notifying the listener
     */
    public SessionManager(SendProvider sender, int expireTime, int warnTime) {
        this.sender = sender;
        this.warnTime = warnTime;
        this.expireTime = expireTime;
        // Starts the timers
        keepAlive();
    }

    /**
     * Sets the callback for the session expiry event
     * @param onExpire listener to call (if not null) when the timeout expires
     * @return this {@code SessionManager}
     */
    public SessionManager onExpire(SessionEvent<String> onExpire) {
        this.onExpire = onExpire;
        return this;
    }

    /**
     * Sets the callback for the session warning event
     * @param onWarn     listener to call (if not null) when the warning is due
     * @return this {@code SessionManager}
     */
    public SessionManager onWarn(SessionEvent<String> onWarn) {
        this.onWarn = onWarn;
        return this;
    }

    /**
     * Adds the current {@code SessionManager} object to a HttpSession
     * @param session session to store {@code this} in
     * @return this {@code SessionManager}
     */
    public SessionManager storeInSession(HttpSession session) {
        try {
            session.setAttribute(SESSION_MGR, this);
        } catch (Exception e) {
            // There is nothing we can really do
            // here so just error and continue
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public void run() {
        if (hasTerminated) return;
        hasTerminated = true;

        if (onExpire != null)
            onExpire.onEvent("Your session has expired.<br/>Please log in again");

        if (sender != null) {
            try {
                SendProvider sender = getSender();
                sender.getTransport().close();
                sender = null;
            } catch (MessagingException ex) {
                ex.printStackTrace();
            }
        }
        timer.cancel();
    }

    /**
     * Resets the timer back to the initial
     * value and continues to count down
     */
    public synchronized void keepAlive() {
        if (hasTerminated) return;

        // Re-create the timers
        if (timer != null) timer.cancel();
        timer = new Timer("SessionManager Timeout");

        TimerTask warn = new TimerTask() {
            @Override
            public void run() {
                if (onWarn != null) {
                    int remaining = expireTime - warnTime;
                    String time = String.format("%d:%02d", remaining/60, remaining%60);
                    onWarn.onEvent("You will be disconnected in " + time +" if no activity is made within that period");
                }
            }
        };
        timer.schedule(warn, warnTime * 1000);
        timer.schedule(this, expireTime * 1000);
    }

    public void dispose(HttpSession session) throws Exception {
        hasTerminated = true;
        timer.cancel();
        sender.getTransport().close();
        session.removeAttribute(SESSION_MGR);
    }

    /**
     * Gets whether the {@code SendProvider} session has expired
     * @return true if expired
     */
    public boolean isExpired() {
        return hasTerminated;
    }

    /**
     * Gets the {@link SendProvider} stored in the manager in not null
     *
     * @return the {@code SendProvider}, or null
     */
    public synchronized SendProvider getSender() {
        return sender;
    }

    /**
     * Returns true if a {@link HttpSession} object contains a {@code SessionManager}
     *
     * @param session the {@code HttpSession} to check
     * @return true if the session has a {@code SessionManager}
     */
    public static boolean hasSession(HttpSession session) {
        try {
            return session.getAttribute(SESSION_MGR) != null &&
                    session.getAttribute(SESSION_MGR) instanceof SessionManager;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the current {@code SessionManager} from a {@link HttpSession}
     * if it contains one, otherwise null
     *
     * @param session session to get {@code SessionManager from}
     * @return the {@code SessionManager} or null
     */
    public static SessionManager getFromSession(HttpSession session) {
        return hasSession(session) ? (SessionManager) session.getAttribute(SESSION_MGR) : null;
    }
}