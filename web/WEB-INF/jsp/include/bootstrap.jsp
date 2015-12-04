        <%-- Bootstrap stuff --%>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" type="text/css" href="css/style.css">
        <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="css/bootstrap-theme.css">
        <script type="text/javascript" src="js/jquery-2.1.4.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <%-- Open all modal dialogs on page load--%>
        <script>
            $(document).ready(function() {
                $.ajax({
                    url: "http://www.splashbase.co/api/v1/images/random?images_only=true",
                    success: function(e) {
                        $("body").prepend('<img src="' + e.url + '" class="bg-img" />');
                    }
                });
            });
        </script>

