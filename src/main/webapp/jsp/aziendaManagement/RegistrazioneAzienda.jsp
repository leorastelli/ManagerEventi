<%@page session="false"%>
<%@page import="com.managereventi.managereventi.model.mo.Azienda"%>

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
        .bottone-personalizzato {
            background-color: #6fa3ef;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-top: 10px;
        }
        button:hover {
            background-color: #72c7e8;
        }
    </style>
</head>
<body onload="headerOnLoadHandler()">
<header></header>
<!--//@include file="/include/footer.inc"-->

<form action="Dispatcher" method="post">
    <h2>Dati Aziendali</h2>
    <input type="text" name="partitaIVA" id="partitaIVA" placeholder="PartitaIVA" required>
    <input type="text" name="nome" placeholder="Nome" required>
    <input type="text" name="indirizzo" placeholder="Indirizzo" required>
    <input type="text" name="citta" placeholder="Citt&agrave;" required>
    <input type="text" name="provincia" placeholder="Provincia" required>
    <input type="text" name="cap" placeholder="CAP" required>
    <input type="text" name="stato" placeholder="Stato" required>
    <input type="text" name="telefono" placeholder="Telefono" required>
    <input type="email" name="email" placeholder="E-mail" required>
    <input type="password" name="password" id="password" placeholder="Password" required>
    <input type="hidden" name="controllerAction" value="AziendaManagement.registration"/>
    <input type="submit" class="bottone-personalizzato" value="Registrati">
</form>

<script>
    function headerOnLoadHandler() {
        var usernameTextField = document.querySelector("#username");
        var usernameTextFieldMsg = "Lo username è obbligatorio.";
        var passwordTextField = document.querySelector("#password");
        var passwordTextFieldMsg = "La password è obbligatoria.";
        var codiceautTextField = document.querySelector("#codiceaut");
        var codiceautTextFieldMsg = "Il codice autorizzazione è obbligatorio.";
        var partitaIVATextField = document.querySelector("#partitaIVA");
        var partitaIVATextFieldMsg = "La partita IVA è obbligatoria.";

        if (usernameTextField !== undefined && passwordTextField !== undefined ) {
            usernameTextField.setCustomValidity(usernameTextFieldMsg);
            usernameTextField.addEventListener("change", function () {
                this.setCustomValidity(this.validity.valueMissing ? usernameTextFieldMsg : "");
            });
            passwordTextField.setCustomValidity(passwordTextFieldMsg);
            passwordTextField.addEventListener("change", function () {
                this.setCustomValidity(this.validity.valueMissing ? passwordTextFieldMsg : "");
            });
            codiceautTextField.setCustomValidity(codiceautTextFieldMsg);
            codiceautTextField.addEventListener("change", function () {
                this.setCustomValidity(this.validity.valueMissing ? codiceautTextFieldMsg : "");
            });
            partitaIVATextField.setCustomValidity(partitaIVATextFieldMsg);
            partitaIVATextField.addEventListener("change", function () {
                this.setCustomValidity(this.validity.valueMissing ? partitaIVATextFieldMsg : "");
            });
        }
    }
</script>
</body>
</html>
