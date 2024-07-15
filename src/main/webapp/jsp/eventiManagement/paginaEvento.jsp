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
    List<Azienda> aziende = (List<Azienda>) request.getAttribute("aziende");
    List<Sponsorizzazione> sponsorizzazioni = (List<Sponsorizzazione>) request.getAttribute("sponsorizzazioni");
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
        .date {
            text-align: right;
            padding-right: 10px;
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
    <h1>Evento <%=evento.getNome()%></h1>
    <label>Descrizione: <%=evento.getDescrizione()%></label> <br>
    <label>Data inizio: <%=evento.getDataInizio()%></label> <br>
    <label>Data fine: <%=evento.getDataFine()%></label> <br>

    <h1>Tutte le esibizioni</h1>
    <div class="search-sort">
        <input type="text" id="search" placeholder="Cerca esibizione per genere" oninput="filterEvents()">
        <select id="sort" onchange="sortEvents()">
            <option value="date">Ordina per data</option>
        </select>
    </div>
    <div id="esibizioniList">
        <% for (i=0; i<esibizioni.size();i++){
            Blob logoBlob = esibizioni.get(i).getImmagine();
            int blobLength = (int) logoBlob.length();
            byte[] logoBytes = logoBlob.getBytes(1, blobLength);
            String base64Image = Base64.getEncoder().encodeToString(logoBytes); %>
        <section class="esibizione">
            <figure data-date="<%=esibizioni.get(i).getIdEvento().getDataInizio()%>>">
                <figcaption class="date">DOM<br><strong>30</strong><br>GIU 2024</figcaption>
                <img src="data:image/jpeg;base64, <%= base64Image%>" style="max-width: 200px; max-height: 200px" alt=<%=esibizioni.get(i).getNome()%>>
                <figcaption>
                    Artista: <%=esibizioni.get(i).getNome()%><br>
                    Luogo: <%=esibizioni.get(i).getIdLuogo().getIdLuogo()%><br>
                    Ora di inizio: <%=esibizioni.get(i).getOraInizio()%>
                </figcaption>
            </figure>
        </section>
        <form>
            <input type="hidden" name="controllerAction" value="EsibizioniManagement.gotoEsibizione">
            <input type="hidden" name="idEsibizione" value=<%=esibizioni.get(i).getIdEsibizione()%>>
            <input type="submit" value="Vai all'esibizione">
        </form>
    </div>
    <%}%>
    <form action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="EsibizioniManagement.gotoEvento">
        <input type="submit" value="Acquista abbonamento">
    </form>
    <% if (loggedOrganizzatore != null && loggedOrganizzatore.getIdOrganizzatore().equals(evento.getOrganizzatore().getIdOrganizzatore())){ %>
    <form action="Dispatcher" method="post">
        <input type="hidden" name="idEvento" value=<%= evento.getIdEvento()%>>
        <input type="hidden" name="controllerAction" value="EsibizioneManagement.gotoCreaEsibizione">
        <input type="submit" value="Aggiungi esibizione">
    </form>
    <%}%>

</main>
<footer>
    <section id="adv" >
        <% for(Sponsorizzazione sponsorizzazione : sponsorizzazioni) {
            Blob logoBlob = sponsorizzazioni.get(i).getLogo();
            int blobLength = (int) logoBlob.length();
            byte[] logoBytes = logoBlob.getBytes(1, blobLength);
            String base64Image = Base64.getEncoder().encodeToString(logoBytes);
        %>
        <img src="data:image/jpeg;base64, <%= base64Image %>">
        <% } %>
    </section>

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
