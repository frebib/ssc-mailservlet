package net.frebib.view;

import javax.servlet.http.HttpSession;

public class PopupMessage {
    public static final String POPUP_MSG_ID = "send-error";

    private String title, message, forPage, action;
    private boolean autoDismis, autoStart = true;
    private MsgButtons buttons = MsgButtons.Ok;


    private PopupMessage(String title, String message, String forPage, boolean autoDismis) {
        this.title = title;
        this.message = message;
        this.forPage = forPage;
        this.autoDismis = autoDismis;
    }

    public static PopupMessage set(String title, String message, String forPage, HttpSession session) {
        return new PopupMessage(title, message, forPage, true).showPopup(session);
    }
    public static PopupMessage set(String title, String message, String forPage,
                                   boolean autoDismis, HttpSession session) {
        return new PopupMessage(title, message, forPage, autoDismis).showPopup(session);
    }

    public PopupMessage setButtons(MsgButtons buttons) {
        if (buttons == MsgButtons.YesNo)
            autoDismis = false;
        this.buttons = buttons;
        return this;
    }
    public PopupMessage setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
        return this;
    }

    public PopupMessage onConfirm(String action) {
        this.action = action;
        return this;
    }

    public PopupMessage setAutoDismis(boolean autoDismis) {
        this.autoDismis = autoDismis;
        return this;
    }

    public boolean isPage(String pageName) {
        return this.forPage.trim().equalsIgnoreCase(pageName.trim());
    }

    public String getMessage() {
        return message;
    }
    public String getTitle() {
        return title;
    }
    public String getForPage() {
        return forPage;
    }
    public String getAction() {
        return action;
    }
    public MsgButtons getButtons() {
        return buttons;
    }

    public boolean isAutoDismis() {
        return autoDismis;
    }
    public boolean isAutoStart() {
        return autoStart;
    }

    public PopupMessage showPopup(HttpSession session) {
        session.setAttribute(POPUP_MSG_ID, this);
        return this;
    }

    @Override
    public String toString() {
        return "ViewPopupMessage{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", forPage='" + forPage + '\'' +
                ", action='" + action + '\'' +
                ", buttons=" + buttons +
                ", autoDismis=" + autoDismis +
                ", autoStart=" + autoStart +
                '}';
    }
}
