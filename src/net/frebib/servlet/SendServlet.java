package net.frebib.servlet;

import net.frebib.mail.UnsentEmail;
import net.frebib.view.MsgButtons;
import net.frebib.view.PopupMessage;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/send")
public class SendServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesh = req.getSession();

        String to = req.getParameter("to");
        String cc = req.getParameter("cc");
        String bcc = req.getParameter("bcc");
        String subj = req.getParameter("subj");
        String body = req.getParameter("body");

        storeEmailValues(sesh, to, cc, bcc, subj, body);

        PopupMessage err = null;
        List<Address> addrs, ccs = null, bccs = null;

        String[] split = to.split("[^\\w\\-!#$%&'*+-/=?^_`{|}~@]+", -1);
        addrs = new ArrayList<Address>(split.length);
        for (String aSplit : split) {
            try {
                addrs.add(new InternetAddress(aSplit));
            } catch (AddressException e) {
                if (aSplit.isEmpty())
                    continue;
                err = PopupMessage.set("Invalid Recipient Address", '\"' + aSplit + "\" is not a valid email address." +
                        " Please correct and try sending again", "compose", sesh);
                break;
            }
        }
        if (addrs.size() < 1)
            err = PopupMessage.set("Empty Recipient Address", "Please supply at least one email recipient before " +
                    "trying to send again", "compose", sesh).setAutoDismis(false);
        if (err == null && !cc.trim().isEmpty()) {
            split = cc.split("[^\\w\\-!#$%&'*+-/=?^_`{|}~@]+", -1);
            ccs = new ArrayList<Address>();
            for (String s : split) {
                try {
                    ccs.add(new InternetAddress(s));
                } catch (AddressException e) {
                    err = PopupMessage.set("Invalid Cc Address", '\"' + s + "\" is not a valid email address. " +
                            "Please correct and try sending again", "compose", sesh);
                    break;
                }
            }
        }
        if (err == null && !bcc.trim().isEmpty()) {
            split = bcc.split("[^\\w\\-!#$%&'*+-/=?^_`{|}~@]+", -1);
            bccs = new ArrayList<Address>();
            for (String s : split) {
                try {
                    bccs.add(new InternetAddress(s));
                } catch (AddressException e) {
                    err = PopupMessage.set("Invalid Bcc Address", '\"' + s + "\" is not a valid email address." +
                            " Please correct and try sending again", "compose", sesh);
                    break;
                }
            }
        }
        if (err != null) {
            resp.sendRedirect("/compose");
            return;
        }

        boolean emptyBody = "true".equalsIgnoreCase(String.valueOf(sesh.getAttribute("emptyBody"))) ||
                "true".equalsIgnoreCase(String.valueOf(req.getParameter("emptyBody")));
        boolean emptySubj = "true".equalsIgnoreCase(String.valueOf(sesh.getAttribute("emptySubj"))) ||
                "true".equalsIgnoreCase(String.valueOf(req.getParameter("emptySubj")));

        sesh.setAttribute("emptyBody", emptyBody);
        sesh.setAttribute("emptySubj", emptySubj);


        if (body.isEmpty() && !emptyBody) {
            PopupMessage.set("Empty Body", "Are you sure you want to send the message with an empty body?",
                    "compose", sesh).setButtons(MsgButtons.YesNo)
                    .onConfirm("$('form').get(0).setAttribute('action', $('form').attr('action') + '?emptyBody=true');" +
                            "$('form').get(0).submit()");
        } else if (subj.isEmpty() && !emptySubj) {
            PopupMessage.set("Empty Subject", "Are you sure you want to send the message with an empty subject?",
                    "compose", sesh).setButtons(MsgButtons.YesNo)
                    .onConfirm("$('form').get(0).setAttribute('action', $('form').attr('action') + '?emptySubj=true');" +
                            "$('form').get(0).submit()");
        } else {
            try {
                UnsentEmail unsent = new UnsentEmail(addrs.toArray(new Address[addrs.size()]), body, subj);
                if (ccs != null && ccs.size() > 0) unsent.setCc(ccs.toArray(new Address[ccs.size()]));
                if (bccs != null && bccs.size() > 0) unsent.setBcc(bccs.toArray(new Address[bccs.size()]));

                SessionManager sessionMgr = SessionManager.getFromSession(sesh);
                if (sessionMgr != null) {
                    sessionMgr.getSender().send(unsent);

                    // Clear message attributes after sending
                    clearValues(sesh);
                } else {
                    // This is unlikely to happen
                    PopupMessage.set("An error occured!", "There was an error sending your message. " +
                            "You are probably logged out. Please login and try again", "compose", sesh);
                }

                PopupMessage.set("Email Sent!", "Your message to <span style=\"font-style: italic;\">" + addrs.get(0)
                        .toString() + "</span> has been sent!", "compose", sesh);
            } catch (NullPointerException | MessagingException e) {
                PopupMessage.set("Unknown Error", "There was an unspecified error sending the email." +
                        "Sorry about that", "compose", sesh);
            }
        }
        resp.sendRedirect("/compose");
    }

    public static void clearValues(HttpSession session) {
        storeEmailValues(session, "", "", "", "", "");
    }

    public static void storeEmailValues(HttpSession session, String to, String cc, String bcc, String subj, String body) {
        session.setAttribute("to", to);
        session.setAttribute("cc", cc);
        session.setAttribute("bcc", bcc);
        session.setAttribute("subj", subj);
        session.setAttribute("body", body);
    }
}
