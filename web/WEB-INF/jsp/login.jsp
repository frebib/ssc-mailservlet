<%@ page import="net.frebib.servlet.LoginError" %>
<%@ page import="net.frebib.servlet.LoginServlet" %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Login Page</title>
        <%@include file="include/bootstrap.jsp" %>
    </head>
    <body>
        <%
            Object o = session.getAttribute(LoginError.ERR_OBJ_ID);
            if (o != null) {
                session.removeAttribute(LoginError.ERR_OBJ_ID);
                LoginError err = (LoginError) o;%>
        <div id="error-container" class="modal-dialog">
            <div id="error-title"><%= err.getTitle() %></div>
            <div id="error-message"><%= err.getMessage() %></div>
        </div>
        <% } %>

        <div class="container center-block">
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
                    <label for="host" class="control-label">Server Address</label>
                    <input class="form-control" name="host" id="host"/>
                </div>
                <div class="form-group">
                    <label for="port" class="control-label">Server Port</label>
                    <input class="form-control" name="port" id="port" type="number" value="587"/>
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary btn-lg btn-block">Login!</button>
                </div>
            </form>
        </div>
    </body>
</html>
`