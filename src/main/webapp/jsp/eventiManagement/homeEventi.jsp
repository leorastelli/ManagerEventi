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
            background-color: #f4f4f4;
        }
        header {
            background-color: #6fa3ef;
            padding: 10px;
            color: #fff;
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
            color: white;
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
            width: 90%;
            max-width: 600px;
            cursor: pointer;
            position: relative;
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
            background-color: #6fa3ef;
            color: #fff;
            position: fixed;
            bottom: 0;
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
    <% if (loggedOrganizzatore != null){ %>

    <form action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="EventiManagement.gotoCreaEvento">
        <input type="submit" value="Crea Evento">
    </form>

    <%}%>

    <h1>Tutti gli Eventi</h1>
    <div class="search-sort">
        <input type="text" id="search" placeholder="Cerca evento per nome" oninput="filterEvents()">
        <select id="sort" onchange="sortEvents()">
            <option value="date">Ordina per data</option>
        </select>
    </div>
    <div id="eventList">
        <% for (i=0; i<eventi.size();i++){
           /* Blob logoBlob = eventi.get(i).getImmagine();

            // Ottieni la lunghezza del Blob (dimensione dell'array di byte)
            int blobLength = (int) logoBlob.length();

            // Leggi i dati del Blob in un array di byte
            byte[] logoBytes = logoBlob.getBytes(1, blobLength);
            // Assume che getLogo() restituisca un byte array del BLOB
            String base64Image = Base64.getEncoder().encodeToString(logoBytes);  base64Image */ %>
        <div class="event" data-date=<%=eventi.get(i).getDataInizio()%>>
            <img src="data:image/jpeg;base64," style="max-width: 200px; max-height: 200px" alt=<%=eventi.get(i).getNome()%>>
            <p> Da <%=eventi.get(i).getDataInizio()%> a <%=eventi.get(i).getDataFine()%></p>
            <form>
                <input type="hidden" name="controllerAction" value="EventiManagement.gotoEvento">
                <input type="hidden" name="idEvento" value=<%=eventi.get(i).getIdEvento()%>>
                <input type="submit" value="Vai all'evento">
            </form>
        </div>
        <%}%>
    </div>
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

        events.sort((a, b) => {
            if (sortValue === 'date') {
                return new Date(a.getAttribute('data-date')) - new Date(b.getAttribute('data-date'));
            } else if (sortValue === 'name') {
                const nameA = a.querySelector('img').alt.toLowerCase();
                const nameB = b.querySelector('img').alt.toLowerCase();
                return nameA.localeCompare(nameB);
            }
        });

        events.forEach(event => eventList.appendChild(event));
    }
</script>
</body>
</html>
