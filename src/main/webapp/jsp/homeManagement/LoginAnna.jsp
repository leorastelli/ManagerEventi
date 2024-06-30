<%@page session="false"%>
<%@page import="com.managereventi.managereventi.model.mo.Utente"%>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - PrimEvent</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background-color: #f4f4f4;
        }
        form {
            border: 1px solid #ccc;
            padding: 20px;
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            text-align: center;
            width: 200px;
        }
        h2 {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin: 15px 0;
            font-size: 18px;
        }
        input[type="text"], input[type="password"] {
            calc(100% - 20px);
            padding: 10px;
            margin-bottom: 10px;
            font-size: 16px;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-sizing: border-box;
        }
        button {
            display: block;
            width: 100%;
            margin-top: 20px;
            padding: 10px 20px;
            font-size: 16px;
            color: white;
            background-color: #6fa3ef;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
    </style>
</head>
<body>
    <script>
        function headerOnLoadHandler() {
            var usernameTextField = document.querySelector("#username");
            var usernameTextFieldMsg = "Lo username \xE8 obbligatorio.";
            var passwordTextField = document.querySelector("#password");
            var passwordTextFieldMsg = "La password \xE8 obbligatoria.";

            if (usernameTextField !== undefined && passwordTextField !== undefined ) {
                usernameTextField.setCustomValidity(usernameTextFieldMsg);
                usernameTextField.addEventListener("change", function () {
                    this.setCustomValidity(this.validity.valueMissing ? usernameTextFieldMsg : "");
                });
                passwordTextField.setCustomValidity(passwordTextFieldMsg);
                passwordTextField.addEventListener("change", function () {
                    this.setCustomValidity(this.validity.valueMissing ? passwordTextFieldMsg : "");
                });
            }
        }
    </script>
    <section id="login" class="clearfix">
        <form name="logonForm" action="Dispatcher" method="post">
            <h2>Accedi</h2>
                <label for="username">Username</label>
                <input type="text" id="username" name="username" required>

                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>

                <input type="hidden" name="controllerAction" value="HomeManagement.logon"/>
                <input type="submit"></input>
        </form>
    </section>
</body>
</html>
