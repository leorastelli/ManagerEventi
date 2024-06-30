<!--Qui vengono separati i vari tipi di utenti che vogliono iscriversi. Una volta spinto su una delle tre opzioni si viene reindirizzati alla 3 diverse pagine di registrazione -->
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
    <title>Iscriviti - PrimEvent</title>
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
            margin-right: 10px;
        }
        button {
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
    <form name="FormRadio" action="Dispatcher" method="post">
        <h2>Iscriviti come</h2>
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
        <input type="submit"> </input>
    </form>
</body>
</html>
