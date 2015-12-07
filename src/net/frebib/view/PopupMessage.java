package net.frebib.view;

import javax.servlet.http.HttpSession;

/**
 * Contains all information for a popup message provided by HTML & Bootstrap or some other view
 */
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

    /**
     * Sets a new {@link PopupMessage} in the session
     * @param title title of the popup
     * @param message message in the popup
     * @param forPage page it should be displayed on
     * @param session {@link HttpSession} the message should be stored in
     * @return the {@code PopupMessage} that has been stored
     */
    public static PopupMessage set(String title, String message, String forPage, HttpSession session) {
        return new PopupMessage(title, message, forPage, true).showPopup(session);
    }

    /**
     * Sets a new {@link PopupMessage} in the session
     * @param title title of the popup
     * @param message message in the popup
     * @param forPage page it should be displayed on
     * @param autoDismis whether the popup should automatically dismiss itself after a timeout
     * @param session {@link HttpSession} the message should be stored in
     * @return the {@code PopupMessage} that has been stored
     */
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

    /**
     * An action to perform when the Yes/Ok button is pressed
     * @param action action to perform in a JavaScript string
     * @return the {@code PopupMessage}
     */
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

    /**
     * Stores the PopupMessage in the session ready to be shown
     * @param session {@link HttpSession} to store the message in
     * @return the {@code PopupMessage}
     */
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
