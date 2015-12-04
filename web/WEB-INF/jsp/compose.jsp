<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Compose an Email</title>
        <%@include file="include/bootstrap.jsp" %>
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
                            <form id="compose-form" class="modal-body">
                                <div class="form-horizontal form-head">
                                    <div class="form-group">
                                        <label for="to" class="col-md-2 control-label">To: </label>

                                        <div class="col-md-10 col-lg-9">
                                            <input id="to" name="to" type="email" class="form-control">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="cc" class="col-md-2 control-label">Cc: </label>

                                        <div class="col-md-10 col-lg-9">
                                            <input id="cc" name="cc" type="text" class="form-control">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="bcc" class="col-md-2 control-label">Bcc: </label>

                                        <div class="col-md-10 col-lg-9">
                                            <input id="bcc" name="cc" type="text" class="form-control">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="subj" class="col-md-2 control-label">Subject: </label>

                                        <div class="col-md-10 col-lg-9">
                                            <input id="subj" name="subj" type="text" class="form-control">
                                        </div>
                                    </div>
                                </div>
                                <br>

                                <div class="form-group">
                                    <textarea id="body" name="body" class="form-control" rows="10"></textarea>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button form="compose-form" type="button" class="btn btn-lg btn-danger pull-left">Log
                                Out</button>
                            <button form="compose-form" type="reset" class="btn btn-lg btn-default">Cancel</button>
                            <button form="compose-form" type="submit" class="btn btn-lg btn-primary">Send!</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
