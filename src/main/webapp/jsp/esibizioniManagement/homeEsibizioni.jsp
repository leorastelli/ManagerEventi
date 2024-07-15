<%@page session="false"%>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.Blob" %>
<%@ page import="java.util.Base64" %>
<%@ page import="com.managereventi.managereventi.model.mo.*" %>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
    Organizzatore loggedOrganizzatore = (Organizzatore) request.getAttribute("loggedOrganizzatore");
    Azienda loggedAzienda = (Azienda) request.getAttribute("loggedAzienda");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";
    int i;
    Evento evento = (Evento) request.getAttribute("evento");
    Esibizione esibizione = (Esibizione) request.getAttribute("esibizione");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PrimEvent - Pagina Esibizione</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #fefefa;
        }
        header {
            background-color: #ffb805;
            padding: 10px;
            color: #ab00cc;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        header h1 {
            margin: 0;
            font-family: 'Arial Black', sans-serif;
            font-size: 24px;
        }
        nav {
            display: flex;
            align-items: center;
        }
        nav ul {
            display: flex;
            list-style: none;
            padding: 0;
            margin: 0;
        }
        nav ul li {
            margin-left: 15px;
        }
        nav a {
            color: #ab00cc;
            text-decoration: none;
        }
        main {
            text-align: center;
            margin-top: 20px;
        }
        h1 {
            font-size: 2em;
        }

        .event img {
            width: 100%;
        }
        .event p {
            margin: 0;
            padding: 10px;
            background-color: rgba(0, 0, 0, 0.5);
            color: white;
            position: absolute;
            bottom: 0;
            width: 100%;
            text-align: center;
            font-size: 1.2em;
        }

        footer {
            width: 100%;
            text-align: center;
            padding: 10px;
            background-color: #ffb805;
            color: #ab00cc;
            position: fixed;
            bottom: 0;
        }

        .search-sort input, .search-sort select {
            margin: 0 10px;
            padding: 5px;
        }

        .adv img {
            width: 100px;
            height: auto;
            margin: 10px;
        }

        figure {
            display: grid;
            grid-template-columns: 1fr auto 1fr;
            align-items: center;
            margin: 20px 0;
            cursor: pointer;
        }
        figure img {
            grid-column: 2;
            max-width: 100%;
        }
        figcaption {
            text-align: left;
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
            background-color: #ab00cc;
        }

    </style>
</head>
<body>
<header>
    <h1>PrimEvent</h1>
    <form name="logoutForm" action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="HomeManagement.logout"/>
    </form>
    <nav>
        <ul>
            <li <%=menuActiveLink.equals("Home") ? "class=\"active\"" : ""%>>
                <a href="Dispatcher?controllerAction=HomeManagement.view">Home</a>
            </li>
            <% if (loggedOn) { %>
            <% if (loggedUser != null) { %>
            <li <%=menuActiveLink.equals("Home Utente") ? "class=\"active\"" : ""%>>
                <a href="Dispatcher?controllerAction=UserManagement.view">Home Utente</a>
            </li>
            <li><a href="javascript:logoutForm.submit()">Logout</a></li>
            <% } else if (loggedOrganizzatore != null) { %>
            <li <%=menuActiveLink.equals("Home Organizzatore") ? "class=\"active\"" : ""%>>
                <a href="Dispatcher?controllerAction=OrganizzatoreManagement.view">Home Organizzatore</a>
            </li>
            <li><a href="javascript:logoutForm.submit()">Logout</a></li>
            <% } else if(loggedAzienda != null){ %>
            <li <%=menuActiveLink.equals("Home Azienda") ? "class=\"active\"" : ""%>>
                <a href="Dispatcher?controllerAction=AziendaManagement.view">Home Azienda</a>
            </li>
            <li><a href="javascript:logoutForm.submit()">Logout</a></li>
            <% }} else { %>
            <li <%=menuActiveLink.equals("Accedi") ? "class=\"active\"" : ""%>>
                <a href="Dispatcher?controllerAction=HomeManagement.gotoLogin">Accedi</a>
            </li>
            <li <%=menuActiveLink.equals("Registrati") ? "class=\"active\"" : ""%>>
                <a href="Dispatcher?controllerAction=HomeManagement.gotoRegistration">Registrati</a>
            </li>
            <% } %>
        </ul>
    </nav>
</header>
<main>
    <h1>Esibizione <%=esibizione.getNome()%></h1>
    <%
        Blob logoBlob = esibizione.getImmagine();
        int blobLength = (int) logoBlob.length();
        byte[] logoBytes = logoBlob.getBytes(1, blobLength);
        String base64Image = Base64.getEncoder().encodeToString(logoBytes);
    %>
    <img src="data:image/jpeg;base64, <%= base64Image %>" style="max-width: 300px; max-height: 300px; align-content: center"><br>
    <label style="font-size: 20px"><%=esibizione.getDescrizione()%></label> <br>
    <label style="font-size: 20px">Ora di inizio esibizione: <%=esibizione.getOraInizio()%></label> <br>
    <label style="font-size: 20px">Data esibizione: <%=esibizione.getDataEsibizione()%></label> <br>
    <br>
    <form action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="EsibizioniManagement.gotoBiglietti">
        <input type="submit" class="bottone-personalizzato" value="Acquista biglietto">
    </form>
</main>
<footer>
    &copy; 2024 EventPrime - Italia IT | Cookie e Privacy Policy<br>
    Credits: Leonardo Rastelli e Anna Ferri
</footer>
</body>
</html>
