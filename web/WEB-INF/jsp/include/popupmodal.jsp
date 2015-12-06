            <%@ page import="net.frebib.view.PopupMessage" %>
            <%
                PopupMessage msg = (PopupMessage) request.getSession().getAttribute(PopupMessage.POPUP_MSG_ID);
                session.removeAttribute(PopupMessage.POPUP_MSG_ID);
                if (msg != null) {
                    if (msg.isAutoStart()) {
            %>
            <script>

                setTimeout(function() {
                    $('#popup-modal').modal('show');
                    setTimeout(function() {
                        $('.modal:not(#popup-modal, #server-message)').modal('show');
            <%          if (msg.isAutoDismis()) { %>
                        setTimeout(function() {
                            $('#popup-modal').modal('hide');
                        }, 2000);
            <%          } %>
                    }, 2000);
                }, 500);
            </script><%
                }
            %><div id="popup-modal" class="modal fade" role="dialog" data-backdrop="false" data-keyboard="false">
                <div class="modal-dialog modal-sm" style="margin-top:80px;">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h2 class="modal-title error-title"><%= msg.getTitle() %></h2>
                        </div>
                        <div class="modal-body">
                            <div id="popup-message" style="text-align:center"><%= msg.getMessage() %></div>
                            <%
                                switch (msg.getButtons()) {
                                    case Ok: %>
                                <button type="button" class="btn btn-primary center-block" style="margin-top: 20px"
                                        onclick="$('.modal:not(#popup-modal, #server-message)').modal('show');" data-dismiss="modal">
                                    Okay
                                </button>
                                <%      break;
                                    case YesNo: %>
                                <div style="margin-top: 20px;text-align:center;">
                                    <button type="button" class="btn btn-default" style="display:inline-block;margin-right: 60px;"
                                            onclick="$('.modal:not(#popup-modal, #server-message)').modal('show');" data-dismiss="modal">
                                        No
                                    </button>
                                    <button type="button" class="btn btn-primary center-block" style="display:inline-block;"
                                            onclick="$('.modal:not(#popup-modal, #server-message)').modal('show');<%=
                                            msg.getAction()
                                             %>"
                                            >
                                        Yes
                                    </button>
                                </div>
                                <%      break;
                                }   %> <%-- End of switch statement--%>
                        </div>
                    </div>
                </div>
            </div>
            <% } %>