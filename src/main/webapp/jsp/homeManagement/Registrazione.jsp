<!--Qui vengono separati i vari tipi di utenti che vogliono iscriversi. Una volta spinto su una delle tre opzioni si viene reindirizzati alla 3 diverse pagine di registrazione -->
<%@page session="false"%>
<%@page import="com.managereventi.managereventi.model.mo.Utente"%>
<%@page import="com.managereventi.managereventi.model.mo.Organizzatore"%>
<%@page import="com.managereventi.managereventi.model.mo.Azienda"%>

<%
  boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
  boolean registration = (Boolean) request.getAttribute("registration");
  Utente loggedUser = (Utente) request.getAttribute("loggedUser");
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
            background-color: #fefefa;
        }
        form {
            border: 1px solid #ccc;
            box-shadow: dimgray 0 0 5px;
            padding: 20px;
            background: #fffdf3;
            border-radius: 10px;
            text-align: center;
            width: 350px;
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
            background-color: #de32ff;
            color: #fefefa;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            align-items: center;
            text-align: center;
            display: block;
            margin: auto;
            font-weight: bolder;
            width: fit-content;
        }

        .bottone-personalizzato:hover {
            background-color: #ab00cc;
        }
    </style>
</head>
<body>

    <% if (registration){ %>

        <form name="FormRadio" action="Dispatcher" method="post">
            <h2>Registrati come</h2>
            <label>
                <input type="radio" id="userTypeUserReg" name="userType" value="utente">
                Utente
            </label>
            <label>
                <input type="radio" id="userTypeCompanyReg" name="userType" value="azienda">
                Azienda
            </label>
            <label>
                <input type="radio" id="userTypeOrganizerReg" name="userType" value="organizzatore">
                Organizzatore
            </label>
            <input type="hidden" name="registration" value="<%= request.getAttribute("registration") %>">
            <input type="hidden" name="controllerAction" value="HomeManagement.usertype"/>
            <input type="submit" class="bottone-personalizzato" value="Accedi"> </input>
        </form>

    <% } else {%>
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

            <input type="hidden" name="registration" value="<%= request.getAttribute("registration") %>">
            <input type="hidden" name="controllerAction" value="HomeManagement.usertype"/>
            <input type="submit" class="bottone-personalizzato" value="Accedi"> </input>
        </form>
    <% } %>
</body>
</html>
