<%@ page import="net.frebib.servlet.SessionManager" %>
<%@ page import="net.frebib.view.PopupMessage" %>
<%
    // Catch errors with not being logged in and expired sessions
    try {
        SessionManager mgr = SessionManager.getFromSession(session);
        if (mgr == null) {
            PopupMessage.set("You must log in!", "You need to log in first before" +
                    " you can compose any emails, silly!", "login", session);
            response.sendRedirect("/login");
        } else if (mgr.isExpired() || !mgr.getSender().isConnected()) {
            PopupMessage.set("Session Expired!", "You have been inactive too long and your" +
                    " session has expired.<br/>Please log in and try again", "login", session);

            mgr.dispose(session);
            response.sendRedirect("/login");
        }

    } catch (Exception e) {
        response.sendRedirect("/login");
    }
%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Compose an Email</title>
        <%@include file="include/bootstrap.jsp" %>
        <script>
            var lastTime = new Date();

            $(document).ready(function() {
                $('#compose-modal').modal('show');

                var evt = new EventSource("/waitforexpiry");
                var expirePopup = $('#server-message.modal');

                evt.addEventListener("warn", function(e) {
                    expirePopup.find('.modal-title').text("Warning");
                    expirePopup.find('.popup-message').html(e.data);
                    expirePopup.modal('show');
                }, false);
                evt.addEventListener("expire", function(e) {
                    expirePopup.find('.modal-title').text("Disconnected");
                    expirePopup.find('.popup-message').html(e.data);
                    expirePopup.find('button').text("Logout");
                    $('#compose-modal').find('input, button, textarea').prop('disabled', true);
                    expirePopup.click(function() { window.location='/logout'; })
                               .modal('show');
                    evt.close();
                });

                // Callback to server and reset disconnect countdown
                $('body').bind('click dblclick mousedown keydown keypress', function(e) {
                    var diff = new Date() - getLastTime();
                    // If time greater than minute
                    if (diff >= 1000 * 60) {
                        // Update lastTime and reset counter
                        setTimeNow();
                        $.post("/waitforexpiry");
                    }
                });
            });

            function getLastTime () {
                return lastTime;
            }
            function setTimeNow() {
                lastTime = new Date();
            }
        </script>
    </head>
    <body>
        <div class="container">
            <div id="compose-modal" class="modal fade show" role="dialog" data-backdrop="static" data-keyboard="false">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h3 class="modal-title">Compose an Email</h3>
                        </div>
                        <div class="modal-body">
                            <form id="compose-form" class="modal-body" action="/send" method="post">
                                <div class="form-horizontal form-head">
                                    <div class="form-group">
                                        <label for="to" class="col-md-2 control-label">To: </label>

                                        <div class="col-md-9">
                                            <input id="to" name="to" type="text" class="form-control"
                                                   value="<%= session.getAttribute("to") %>">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="cc" class="col-md-2 control-label">Cc: </label>

                                        <div class="col-md-9">
                                            <input id="cc" name="cc" type="text" class="form-control"
                                                   value="<%= session.getAttribute("cc")  %>">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="bcc" class="col-md-2 control-label">Bcc: </label>

                                        <div class="col-md-9">
                                            <input id="bcc" name="bcc" type="text" class="form-control"
                                                   value="<%= session.getAttribute("bcc") %>">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="subj" class="col-md-2 control-label">Subject: </label>

                                        <div class="col-md-9">
                                            <input id="subj" name="subj" type="text" class="form-control"
                                                   value="<%= session.getAttribute("subj") %>">
                                        </div>
                                    </div>
                                </div>
                                <br>

                                <div class="form-group">
                                    <textarea id="body" name="body" class="form-control" rows="12"><%=
                                        session.getAttribute("body")
                                    %></textarea>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button form="compose-form" type="button" class="btn btn-lg btn-danger pull-left"
                                    onclick="window.location='/logout'">Log Out</button>
                            <button form="compose-form" type="reset" class="btn btn-lg btn-default">Cancel</button>
                            <button form="compose-form" type="submit" class="btn btn-lg btn-primary">Send!</button>
                        </div>
                    </div>
                </div>
            </div>
            <div id="server-message" class="modal fade" role="dialog" data-backdrop="false"
                 data-keyboard="false">
                <div class="modal-dialog modal-sm" style="margin-top:80px;">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h2 class="modal-title error-title"></h2>
                        </div>
                        <div class="modal-body">
                            <div class="popup-message" style="text-align:center"></div>
                                <button type="button" class="btn btn-primary center-block" style="margin-top: 20px"
                                        onclick="$('#server-message').modal('hide');">Okay</button>
                        </div>
                    </div>
                </div>
            </div>
            <%
                Object o = session.getAttribute(PopupMessage.POPUP_MSG_ID);
                if (o != null && o instanceof PopupMessage) {
                    if (((PopupMessage) o).isPage("compose")) { %>
                        <%@include file="include/popupmodal.jsp" %>
            <%      }
                }
            %>
        </div>
    </body>
</html>
