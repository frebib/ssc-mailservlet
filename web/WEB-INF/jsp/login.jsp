<%@ page import="net.frebib.servlet.LoginError" %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Login Page</title>
        <%@include file="include/bootstrap.jsp" %>
    </head>
    <body>
        <div class="container">
            <div id="login-modal" class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false">
                <div class="modal-dialog modal-md">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h2 class="modal-title">Login to Email</h2>
                        </div>
                        <div class="modal-body">
                            <form id="login" class="" method="POST">
                                <div class="form-group">
                                    <label for="email" class="control-label">Email Address</label>
                                    <input class="form-control" name="email" id="email"/>
                                </div>
                                <div class="form-group">
                                    <label for="password" class="control-label">Password</label>
                                    <input class="form-control" name="password" id="password" type="password"/>
                                </div>
                                <div class="form-group">
                                    <label for="server" class="control-label">Server Address</label>
                                    <input class="form-control" name="server" id="server"/>
                                </div>
                                <div class="form-group">
                                    <label for="port" class="control-label">Server Port</label>
                                    <input class="form-control" name="port" id="port" type="number" value="587"/>
                                </div>
                                <div class="form-group">
                                    <button type="submit" class="btn btn-primary btn-lg center-block" style="margin-top: 25px">
                                        Login!
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <%
                Object o = session.getAttribute(LoginError.ERR_OBJ_ID);
                if (o != null) {
                    session.removeAttribute(LoginError.ERR_OBJ_ID);
                    LoginError err = (LoginError) o;%>
            <script>
                setTimeout(function() {
                    $('#error-modal').modal('show');
                    setTimeout(function() {
                        $('#login-modal').modal('show');
                        setTimeout(function() {
                            $('#error-modal').modal('hide');
                        }, 2000);
                    }, 2000);
                }, 500);
            </script>
            <div id="error-modal" class="modal fade" role="dialog" data-backdrop="false" data-keyboard="false">
                <div class="modal-dialog modal-sm" style="margin-top:80px;">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h2 class="modal-title error-title"><%= err.getTitle() %></h2>
                        </div>
                        <div class="modal-body">
                            <div id="error-message" style="text-align:center"><%= err.getMessage() %></div>
                            <button type="button" class="btn btn-primary center-block" style="margin-top: 20px"
                                    onclick="$('#error-modal').modal('hide');$('#login-modal').modal('show');">
                                Okay
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <% } else { %>
            <script>
                $('#login-modal').modal('show');
            </script>
            <% } %>
        </div>
    </body>
</html>
`