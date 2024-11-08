<%@page session="false"%>

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
            background-color: #fefefa;
        }
        form {
            padding: 20px;
            border: 1px solid #ddd;
            background-color: #fffdf3;
            border-radius: 10px;
            box-shadow: dimgray 0 0 5px;
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
            background-color: #de32ff;
            color: #fefefa;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            width: fit-content;
            align-items: center;
            text-align: center;
            display: block;
            margin: auto;
            font-weight: bolder;
        }

        .bottone-personalizzato:hover {
            background-color: #00bfff;
        }

    </style>
</head>
<body onload="headerOnLoadHandler()">
<header></header>
<!--//@include file="/include/footer.inc"-->

<form action="Dispatcher" method="post">
    <h2>Dati personali</h2>
    <input type="text" name="nome" placeholder="Nome" required>
    <input type="text" name="cognome" placeholder="Cognome" required>
    <input type="email" name="email" placeholder="E-mail" required>
    <input type="password" name="password" placeholder="Password" required>
    <input type="text" name="username" id="username" placeholder="Username" required>
    <input type="text" name="codiceaut" id="codiceaut" placeholder="Codice Autorizzazione" required>

    <input type="hidden" name="controllerAction" value="OrganizzatoreManagement.registration"/>
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

        if (usernameTextField != undefined && passwordTextField != undefined ) {
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
        }
    }
</script>
</body>
</html>
