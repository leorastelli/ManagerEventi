<!--Qui vengono separati i vari tipi di utenti che vogliono iscriversi. Una volta spinto su una delle tre opzioni si viene reindirizzati alla 3 diverse pagine di registrazione -->
<%@page session="false"%>
<%@page import="com.managereventi.managereventi.model.mo.Utente"%>
<%@page import="com.managereventi.managereventi.model.mo.Organizzatore"%>
<%@page import="com.managereventi.managereventi.model.mo.Organizzatore"%>

<%
  boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
  Utente loggedUser = (Utente) request.getAttribute("loggedUser");
  Organizzatore loggedOrganizzatore = (Organizzatore) request.getAttribute("loggedOrganizzatore");
  String applicationMessage = (String) request.getAttribute("applicationMessage");
  String menuActiveLink = "Home";
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Accedi - PrimEvent</title>
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
            width: 300px;
        }
        h2 {
            margin-bottom: 20px;
        }
        label {
            display: flex;
            align-items: center;
            margin: 15px 0;
            font-size: 18px;
        }
        input[type="radio"] {
            width: 90px;
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
    <form name="FormRadio" action="Dispatcher" method="post">
        <h2>Accedi come</h2>
        <label>
            <input type="radio" id="userTypeUser" name="userType" value="utente">
            Utente
        </label>
        <label>
            <input type="radio" id="userTypeCompany" name="userType" value="azienda">
            Azienda
        </label>
        <label>
            <input type="radio" id="userTypeOrganizer" name="userType" value="organizzatore">
            Organizzatore
        </label>
        <input type="hidden" name="controllerAction" value="HomeManagement.usertype"/>
        <input type="submit" class="bottone-personalizzato" value="Accedi"> </input>
    </form>
</body>
</html>
