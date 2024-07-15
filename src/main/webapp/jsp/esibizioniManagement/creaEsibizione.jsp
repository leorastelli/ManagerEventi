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
    List<Esibizione> esibizioni = (List<Esibizione>) request.getAttribute("esibizioni");
    List<Luogo> luoghi = (List<Luogo>) request.getAttribute("luoghi");
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
            clear: both;
            text-align: center;
            padding: 10px;
            background-color: #ffb805;
            color: #ab00cc;
            margin-top: 20px;
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
    <h2>Inserisci i dettagli dell'esibizione che desideri aggiungere</h2>
    <section id="esibizione">
        <h2 class="titolo" id="titoloEsibizione" style="display:none;">Inserisci dettagli esibizione</h2>
        <label for="nomeEsibizione" style="display:none;">Nome esibizione</label>
        <input type="text" id="nomeEsibizione" name="nomeEsibizione" style="display:none;">
        <label for="descrizioneEsibizione" style="display:none;">Descrizione dell'esibizione</label>
        <textarea id="descrizioneEsibizione" name="descrizioneEsibizione" style="display:none;"> </textarea>
        <label for="durata" style="display:none;">Durata esibizione</label>
        <input type="time" id="durata" name="durata" required style="display:none;">
        <label for="Orainizio" style="display:none;">Ora di inizio</label>
        <input type="time" id="Orainizio" name="orainizio" required style="display:none;">
        <select class="tendina" name="luogo" style="display: none">
            <option value="">Luogo</option>
            <% for (i=0; i< luoghi.size(); i++) { %>
            <option value="<%= luoghi.get(i) %>"><%= luoghi.get(i) %></option>
            <% } %>
        </select>
        <label for="genere" style="display:none;">Genere </label>
        <input type="text" id="genere" name="genere" required style="display:none;">
        <input class="input" type="file" id="imglogoEsibizione" name="logoEsibizione" style="display:none;" accept="image/png, image/jpeg">
        <img id="logoPreview1" style="max-width: 200px; max-height: 200px">
        <br>
    </section>
    <input type="hidden" name="idorganizzatore" value="<%=loggedOrganizzatore.getIdOrganizzatore()%>">
    <input type="hidden" name="controllerAction" value="EsibizioniManagement.gotoCreaEsibizione">
    <button type="submit" class="bottone-personalizzato">Pubblica esibizione</button>
</main>
<footer>
    &copy; 2024 EventPrime - Italia IT | Cookie e Privacy Policy<br>
    Credits: Leonardo Rastelli e Anna Ferri
</footer>
</body>
</html>
