<%@page session="false"%>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.Blob" %>
<%@ page import="java.util.Base64" %>
<%@ page import="com.managereventi.managereventi.model.mo.*" %>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    Boolean isuserabbonatoObj = (Boolean) request.getAttribute("isuserabbonato");
    boolean isuserabbonato = (isuserabbonatoObj != null) ? isuserabbonatoObj : false;
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

        .immagine {
            margin: 20px auto;
            width: 300px;
            height: 300px;
            position: relative;
            cursor: pointer;
            justify-content: space-around;
            transition: box-shadow 0.2s ease-in-out;
        }

        .immagine:hover {
            box-shadow: 0 0 10px 5px #ab00cc;
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

        .adv img {
            width: 100px;
            height: auto;
            margin-right: 10%;
            margin-left: 10%;
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
        .esibizione {
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 250px; /* Assicurati che ci sia spazio sufficiente per ogni esibizione */
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
        .bottone-pers {
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

        .bottone-pers:hover {
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
    <h1>Evento <%=evento.getNome()%></h1>
    <br>
    <label style="font-size: 20px;color:black; text-shadow: dimgray 0 0 1px;"> <%=evento.getDescrizione()%></label> <br>
    <br>
    <label style="font-size: 20px">Data inizio evento: <%=evento.getDataInizio()%></label> <br>
    <label style="font-size: 20px">Data fine evento: <%=evento.getDataFine()%></label> <br>

    <h1>Esibizioni</h1>
    <div class="search-sort">
        <input type="text" id="search" placeholder="Cerca per genere" oninput="filterEvents()">
    </div>
    <div id="esibizioniList">
        <% for (i=0; i<esibizioni.size();i++){
            Blob logoBlob = esibizioni.get(i).getImmagine();
            int blobLength = (int) logoBlob.length();
            byte[] logoBytes = logoBlob.getBytes(1, blobLength);
            String base64Image = Base64.getEncoder().encodeToString(logoBytes); %>
        <section class="esibizione">
            <figure class="esibizione1" data-genere=" <%=esibizioni.get(i).getGenere()%>"   data-idesibizione="<%=esibizioni.get(i).getIdEsibizione()%>" data-idevento="<%=evento.getIdEvento()%>" data-date="<%=esibizioni.get(i).getDataEsibizione()%>" data-nome="<%=esibizioni.get(i).getNome()%>">
                <figcaption class="date" ><%=esibizioni.get(i).getDataEsibizione()%></figcaption>
                <img class="immagine" src="data:image/jpeg;base64, <%= base64Image%>" style="max-width: 200px; max-height: 200px" alt=<%=esibizioni.get(i).getNome()%>>
                <figcaption style="margin-left: 20px">
                    Artista: <%=esibizioni.get(i).getNome()%><br>
                    Luogo: <%=esibizioni.get(i).getIdLuogo().getNome()%><br>
                    Ora di inizio: <%=esibizioni.get(i).getOraInizio()%>
                </figcaption>
            </figure>
        </section>
    </div>
    <%}%>

    <% if(!isuserabbonato && loggedUser != null){ %>
    <form action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="PagamentoManagement.gotoAbbonamento">
        <input type="hidden" name="idEvento" value=<%= evento.getIdEvento()%>>
        <input type="submit" class="bottone-personalizzato" value="Acquista abbonamento">
    </form>
    <%}%>
    <br>
    <% if (loggedOrganizzatore != null && loggedOrganizzatore.getIdOrganizzatore().equals(evento.getOrganizzatore().getIdOrganizzatore())){ %>
    <form action="Dispatcher" method="post">
        <input type="hidden" name="idEvento" value=<%= evento.getIdEvento()%>>
        <input type="hidden" name="controllerAction" value="EventiManagement.gotoCreaEsibizione">
        <input type="submit" class="bottone-pers" value="Aggiungi esibizione">
    </form>
    <%}%>

</main>
<footer>
    <section id="adv" >
        <% for(Sponsorizzazione sponsorizzazione : sponsorizzazioni) {
            Blob logoBlob = sponsorizzazione.getLogo();
            int blobLength = (int) logoBlob.length();
            byte[] logoBytes = logoBlob.getBytes(1, blobLength);
            String base64Image = Base64.getEncoder().encodeToString(logoBytes);
        %>
        <img src="data:image/jpeg;base64, <%= base64Image %>" style="width: 200px; border-radius: 50%; height: 200px">
        <% } %>
    </section>
    <br>
    &copy; 2024 EventPrime - Italia IT | Cookie e Privacy Policy<br>
    Credits: Leonardo Rastelli e Anna Ferri
</footer>
<script>
    function filterEvents() {
        const searchValue = document.getElementById('search').value.toLowerCase();
        const esibizioni = document.querySelectorAll('.esibizione'); // Seleziona le sezioni .esibizione

        esibizioni.forEach(esibizione => {
            const eventGenre = esibizione.querySelector('.esibizione1').getAttribute('data-genere').toLowerCase();
            esibizione.style.display = eventGenre.includes(searchValue) ? 'flex' : 'none'; // Modifica la visibilità della sezione .esibizione
        });
    }


    document.addEventListener("DOMContentLoaded", function() { //quando la pagina è caricata completamente esegue la funzione che segue
        var events = document.querySelectorAll('.esibizione1'); //  prende tutti gli elementi con classe event e li mette in un array

        events.forEach(function(event) {
            event.addEventListener('click', function() {
                var idEsibizione = this.getAttribute('data-idesibizione');
                var idEvento = this.getAttribute('data-idevento');
                var controllerAction = "EsibizioneManagement.view";
                var url = "Dispatcher?controllerAction=" + controllerAction + "&idEsibizione=" + idEsibizione + "&idEvento=" + idEvento;
                window.location.href = url;
            });
        });
    });
</script>
</body>
</html>
