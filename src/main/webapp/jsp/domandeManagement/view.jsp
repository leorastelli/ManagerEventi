<%@ page import="com.managereventi.managereventi.model.mo.Utente" %>
<%@ page import="com.managereventi.managereventi.model.mo.Organizzatore" %>
<%@ page import="com.managereventi.managereventi.model.mo.Azienda" %>
<%@ page import="java.util.List" %>
<%@ page import="com.managereventi.managereventi.model.mo.Domanda" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Boolean loggedOnObj = (Boolean) request.getAttribute("loggedOn");
    boolean loggedOn = (loggedOnObj != null) ? loggedOnObj : false;

    int i;
    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
    Organizzatore loggedOrganizzatore = (Organizzatore) request.getAttribute("loggedOrganizzatore");
    Azienda loggedAzienda = (Azienda) request.getAttribute("loggedAzienda");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";
    List<Domanda> domande = (List<Domanda>) request.getAttribute("domande");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Domande - PrimEvent</title>
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
            margin: 20px;
            height: 100%;
        }

        .search-bar input[type="text"],
        .search-bar select {
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            margin-right: 10px;
        }
        #question {
            background-color: #fffdf3;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 10px;
            margin-bottom: 20px;
            width: 60%;
            align-content: center;
            align-items: center;
            text-align: center;
            margin-right: 20%;
            margin-left: 20%;

            display: grid;
            grid-template-columns: 1fr 1fr;
            grid-template-rows: auto auto;
            grid-template-areas:
            "utente utente1"
            "domanda risposta"
            "nuovarisp nuovarisp";

            box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
            flex-grow: 0.5;
        }

        #ut {
            grid-area: utente;
        }

        #domanda {
            grid-area: domanda;
        }

        #ut1 {
            grid-area: utente1;
        }

        #risposta {
            grid-area: risposta;
        }

        #insrisposta {
            grid-area: nuovarisp;
        }

        .question label {
            font-weight: bold;
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

        .centrato {
            text-align: center;
            margin: auto;
            width: 100%;
        }

        #inserimento {
            margin-bottom: 20px;
            align-content: center;
            align-items: center;
            text-align: center;

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

    </style>
</head>
<body>
<script>
    function toggleEdit(sectionId) {
        var section = document.getElementById(sectionId);
        var inputs = section.querySelectorAll('input');
        var checkboxes = section.querySelectorAll('input[type="checkbox"]');
        for (var i = 0; i < inputs.length; i++) {
            if (inputs[i].type !== 'checkbox') {
                inputs[i].disabled = !inputs[i].disabled;
            }
        }
        for (var i = 0; i < checkboxes.length; i++) {
            checkboxes[i].style.display = checkboxes[i].style.display === 'none' ? 'block' : 'none';
        }
    }

</script>
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
    <h1 class="centrato">Benvenuto nel nostro forum</h1> <br>
    <h2 class="centrato" style="font-weight: normal">Fai il login per aggiungere una domanda o rispondere a quelle degli altri utenti!</h2> <br>
    <%-- Form per inserire una nuova domanda, visibile solo se l'utente è autenticato --%>
    <% if (loggedUser != null) { %>
    <form id="inserimento" action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="DomandeManagement.addDomanda"/>
        <label for="descrizione" style="font-weight: bolder; font-size: 20px">Inserisci la tua domanda:</label> <br> <br>
        <textarea id="descrizione" name="descrizione" style="border-radius: 5px; box-shadow: 0 0 5px rgba(0, 0, 0, 0.3); width: 500px; height: 100px"></textarea><br>
        <input type="submit" value="Invia" class="bottone-personalizzato">
    </form>
    <% } %>
    <br>
    <%-- Visualizza le domande esistenti --%>
    <% if (domande != null && !domande.isEmpty()) { %>
    <% for (Domanda domanda : domande) { %>
    <section class="question" id="question">
        <p><label id="ut" >Utente:</label> <%= domanda.getUtente().getIdUtente() %></p>
        <p><label id="domanda">Domanda:</label> <%= domanda.getDescrizione() %></p>

        <%-- Visualizza le risposte esistenti --%>
        <% if (domanda.getRisposte() != null && !domanda.getRisposte().isEmpty()) { %>
            <% for (i = 0; i < domanda.getRisposte().size(); i++) { %>
                <p><label id="ut1" >Utente:</label> <%= domanda.getRisposte().get(i).getUtente().getIdUtente() %></p>
                <p><label id="risposta">Risposta:</label> <%= domanda.getRisposte().get(i).getDescrizione() %></p>
            <% } %>
        <% } %>
        <%-- Form per inserire una nuova risposta, visibile solo se l'utente è autenticato e non è l'autore della domanda --%>
        <% if (loggedUser != null && !loggedUser.getIdUtente().equals(domanda.getUtente().getIdUtente()) ) { %>
        <form id="insrisposta" action="Dispatcher" method="post">
            <input type="hidden" name="controllerAction" value="DomandeManagement.addRisposta"/>
            <input type="hidden" name="idDomanda" value="<%= domanda.getIdDomanda() %>"/>
            <label for="descrizioneRisposta<%= domanda.getIdDomanda() %>">Rispondi a questa domanda: </label><br>
            <textarea id="descrizioneRisposta<%= domanda.getIdDomanda() %>" name="descrizioneRisposta" style="width:500px; height:100px; border-radius: 5px"></textarea><br>
            <input type="submit" value="Invia" class="bottone-personalizzato">
        </form>
        <% } %>
    </section>
    <% } %>
<% } else { %>
    <p>Nessuna domanda disponibile.</p>
    <% } %>

</main>

<footer>
    &copy; 2024 PrimEvent - Italia IT | Cookie e Privacy Policy<br>
    Credits: Leonardo Rastelli e Anna Ferri
</footer>

</body>
</html>

