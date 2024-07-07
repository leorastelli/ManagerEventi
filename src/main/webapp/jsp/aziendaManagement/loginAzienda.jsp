<%--
  Created by IntelliJ IDEA.
  User: annaferri
  Date: 04/07/24
  Time: 23:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

</body>
</html>
<%@page session="false"%>
<%@page import="com.managereventi.managereventi.model.mo.Utente"%>
<%@ page import="com.managereventi.managereventi.model.mo.Organizzatore" %>
<%@page import="com.managereventi.managereventi.model.mo.Azienda"%>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    Organizzatore loggedOrganizzatore = (Organizzatore) request.getAttribute("loggedOrganizzatore");
    Azienda loggedAzienda = (Azienda) request.getAttribute("loggedAzienda");
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
            width: 350px;
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
        .bottone-personalizzato {
            background-color: #6fa3ef;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-top: 10px;
        }

    </style>
</head>
<body>
<script>
    function headerOnLoadHandler() {

        var passwordTextField = document.querySelector("#password");
        var passwordTextFieldMsg = "La password \xE8 obbligatoria.";
        var partitaIVATextField = document.querySelector("#partitaIVA");
        var partitaIVATextFieldMsg = "La Partita IVA è obbligatoria \xE8 obbligatorio.";

        if (partitaIVATextField !== undefined && passwordTextField !== undefined ) {
            partitaIVATextField.setCustomValidity(partitaIVATextFieldMsg);
            partitaIVATextField.addEventListener("change", function () {
                this.setCustomValidity(this.validity.valueMissing ? partitaIVATextFieldMsg : "");
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
        <label for="partitaIVA">Partita IVA</label>
        <input type="text" id="partitaIVA" name="PartitaIVA" required>

        <label for="password">Password</label>
        <input type="password" id="password" name="password" required>

        <br>
        <input type="hidden" name="controllerAction" value="OrganizzatoreManagement.logon"/>
        <input type="submit"  class="bottone-personalizzato" value="Accedi">
    </form>
</section>
</body>
</html>
