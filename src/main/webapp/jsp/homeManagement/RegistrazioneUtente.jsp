<!-- L'utente può registrarsi al sito inserendo i propri dati personali. Una volta registrato viene riportato
alla pagina di Home(?)-->


<%@page session="false"%>
<%@page import="com.managereventi.managereventi.model.mo.Utente"%>

<%
    String menuActiveLink = "Home";
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <%//@include file="/include/htmlHead.inc"%>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrazione</title>
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            font-family: Arial, sans-serif;
            background-color: #f8f8f8;
        }
        form {
            padding: 20px;
            border: 1px solid #ddd;
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            text-align: center;
            width: 30%;
        }
        h2 {
            margin-bottom: 20px;
        }
        input[type="text"], input[type="email"], input[type="password"] {
            width: calc(100% - 22px);
            padding: 10px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        input[type="checkbox"] {
            margin-right: 10px;
        }
        button {
            margin-top: 20px;
            padding: 10px 20px;
            border: none;
            background-color: #84D7FA;
            color: white;
            border-radius: 5px;
            cursor: pointer;
        }
        button:hover {
            background-color: #72c7e8;
        }
    </style>
</head>
<body onload="headerOnLoadHandler()">
<header>
    <h1 class="logo">ManagerEventi</h1>
    <form name="logoutForm" action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="HomeManagement.logout"/>
    </form>
    <nav>
        <ul>
            <li <%=menuActiveLink.equals("Home")?"class=\"active\"":""%>>
                <a href="Dispatcher?controllerAction=HomeManagement.view">Home</a>
            </li>
        </ul>
    </nav>
</header>
<!--//@include file="/include/footer.inc"-->

<form action="Dispatcher" method="post">
    <h2>Dati personali</h2>
    <input type="text" name="nome" placeholder="Nome" required>
    <input type="text" name="cognome" placeholder="Cognome" required>
    <input type="email" name="email" placeholder="E-mail" required>
    <input type="password" name="password" placeholder="Password" required>
    <input type="text" name="username" id="username" placeholder="Username" required>
    <label>
        <input type="checkbox" name="newsletter"> Acconsento a ricevere newsletter e aggiornamenti a fini commerciali
    </label>
    <input type="hidden" name="controllerAction" value="UserManagement.registration"/>
    <input type="submit" value="Registrati">
</form>

<script>
    function headerOnLoadHandler() {
        var usernameTextField = document.querySelector("#username");
        var usernameTextFieldMsg = "Lo username è obbligatorio.";
        var passwordTextField = document.querySelector("#password");
        var passwordTextFieldMsg = "La password è obbligatoria.";

        if (usernameTextField != undefined && passwordTextField != undefined ) {
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
</body>
</html>
