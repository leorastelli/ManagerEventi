<%@page session="false"%>
<%@page import="com.managereventi.managereventi.model.mo.Utente"%>
<%@ page import="com.managereventi.managereventi.model.mo.Organizzatore" %>
<%@ page import="com.managereventi.managereventi.model.mo.Azienda" %>
<%@ page import="com.managereventi.managereventi.model.mo.Evento" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.Blob" %>
<%@ page import="java.util.Base64" %>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
    Organizzatore loggedOrganizzatore = (Organizzatore) request.getAttribute("loggedOrganizzatore");
    Azienda loggedAzienda = (Azienda) request.getAttribute("loggedAzienda");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";
    int i;
    List<Evento> eventi = (List<Evento>) request.getAttribute("eventi");

%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PrimEvent - Tutti gli Eventi</title>
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
        .event {
            margin: 20px auto;
            width: 80%;
            height: 80%;
            max-width: 300px;
            max-height: 300px;
            position: relative;
            cursor: pointer;
            justify-content: space-around;
            transition: box-shadow 0.2s ease-in-out;
        }
        .event:hover {
            box-shadow: 0 0 10px 5px #ab00cc;
        }

        .event img {
            width: 300px;
            height: 300px;
        }
        .event p {
            position: absolute;
            text-align: right;
            bottom: 10px;
            right: 15px;
            margin: 2px;
            padding: 5px;
            color:white;
            text-shadow: 0 0 5px rgba(0,0,0,0.5);
            font-weight: bolder;
            font-size: 20px;
        }

        footer {
            clear: both;
            text-align: center;
            padding: 10px;
            background-color: #ffb805;
            color: #ab00cc;
            margin-top: 20px;
        }

        .search-sort {
            display: flex;
            justify-content: center;
            align-items: center;
            margin: 20px 0;
        }
        .search-sort input, .search-sort select {
            margin: 0 10px;
            padding: 5px;
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
    <h1>Tutti gli Eventi</h1>
    <section class="search-sort">
        <input type="text" id="search" placeholder="Cerca evento per nome" oninput="filterEvents()">
        <select id="sort" style="border-radius: 5px" onchange="sortEvents()">
            <option value="default">Ordina per default</option>
            <option value="date">Ordina per data</option>
        </select>
    </section>
    <section id="eventList">
        <% for (i=0; i<eventi.size();i++){
            Blob logoBlob = eventi.get(i).getImmagine();
            int blobLength = (int) logoBlob.length();
            byte[] logoBytes = logoBlob.getBytes(1, blobLength);
            String base64Image = Base64.getEncoder().encodeToString(logoBytes); %>
        <section class="event" data-nome="<%=eventi.get(i).getNome()%> "data-date="<%=eventi.get(i).getDataInizio()%>" data-id-evento="<%=eventi.get(i).getIdEvento()%>"> <!-- serve per ordinare gli eventi -->
            <p style="text-align: right; top: 0; right: 0;"><%=eventi.get(i).getNome()%></p>
            <img src="data:image/jpeg;base64, <%= base64Image%>" alt="<%=eventi.get(i).getNome()%>">
            <p> Da <%=eventi.get(i).getDataInizio()%> a <%=eventi.get(i).getDataFine()%></p>
        </section>

        <%}%>
    </section>

    <% if (loggedOrganizzatore != null){ %>

    <form action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="EventiManagement.gotoCreaEvento">
        <input type="submit" class="bottone-personalizzato" value="Crea Evento">
    </form>

    <%}%>

</main>
<footer>
    &copy; 2024 EventPrime - Italia IT | Cookie e Privacy Policy<br>
    Credits: Leonardo Rastelli e Anna Ferri
</footer>
<script>
    function filterEvents() {
        const searchValue = document.getElementById('search').value.toLowerCase();
        const events = document.querySelectorAll('.event');
        events.forEach(event => {
            const eventName = event.querySelector('img').alt.toLowerCase();
            event.style.display = eventName.includes(searchValue) ? 'block' : 'none';
        });
    }

    function sortEvents() {
        const sortValue = document.getElementById('sort').value;
        const eventList = document.getElementById('eventList');
        const events = Array.from(eventList.getElementsByClassName('event'));

        if (sortValue === 'date') {
            events.sort((a, b) => {
                return new Date(a.getAttribute('data-date')) - new Date(b.getAttribute('data-date'));
            });
        }

        if (sortValue === 'default') {
            events.sort((a, b) => {
                return a.getAttribute('data-nome') - b.getAttribute('data-nome');
            });

        }

        events.forEach(event => eventList.appendChild(event));
    }

    document.addEventListener("DOMContentLoaded", function() {
        var events = document.querySelectorAll('.event');

        events.forEach(function(event) {
            event.addEventListener('click', function() {
                var idEvento = this.getAttribute('data-id-evento');
                var controllerAction = "EventiManagement.gotoEvento";
                var url = "Dispatcher?controllerAction=" + controllerAction + "&idEvento=" + idEvento;
                window.location.href = url;
            });
        });
    });

</script>
</body>
</html>
