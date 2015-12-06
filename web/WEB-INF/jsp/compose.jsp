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
            $(document).ready(function() {
                $('#compose-modal').modal('show');
            });
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
