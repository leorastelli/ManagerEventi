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
            margin: 20px;
        }

        .search-bar {
            display: flex;
            justify-content: center;
            margin-bottom: 20px;
        }
        .search-bar input[type="text"],
        .search-bar select {
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            margin-right: 10px;
        }
        .question {
            background-color: white;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
            margin-bottom: 20px;
        }

        .question label {
            font-weight: bold;
        }

        .bottone-personalizzato {
            background-color: #6fa3ef;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            display: inline-block;
            margin-right: 10px;
        }
        .centrato {
            text-align: center; /* Centra il testo orizzontalmente */
            margin: auto; /* Utile se vuoi centrare un blocco (es. div) orizzontalmente */
            width: 100%; /* Assicura che l'elemento occupi tutta la larghezza */
            /* Per centrare verticalmente potresti dover lavorare con altezza e display flex sul contenitore genitore */
        }
        footer {
            text-align: center;
            padding: 10px;
            background-color: #6fa3ef;
            color: #fff;
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
            <li <%=menuActiveLink.equals("Home Utente") ? "class=\"active\"" : ""%>>
                <a href="Dispatcher?controllerAction=UserManagement.view">Home Utente</a>
            </li>
            <li><a href="javascript:logoutForm.submit()">Logout</a></li>
            <% } else { %>
            <li <%= menuActiveLink.equals("Accedi") ? "class=\"active\"": ""%>>
                <a href="Dispatcher?controllerAction=HomeManagement.gotoLogin">Accedi</a></li>
            <li <%=menuActiveLink.equals("Registrati")?"class=\"active\"":""%>>
                <a href="Dispatcher?controllerAction=UserManagement.gotoRegistration">Registrati</a>
                    <%}%>
        </ul>
    </nav>
</header>

<main>
    <h1 class="centrato">Visualizza Domande</h1> <br>

    <%-- Form per inserire una nuova domanda, visibile solo se l'utente è autenticato --%>
    <% if (loggedUser != null) { %>
    <form action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="DomandeManagement.addDomanda"/>
        <label for="descrizione">Inserisci la tua domanda:</label><br>
        <textarea id="descrizione" name="descrizione" rows="4" cols="50"></textarea><br>
        <input type="submit" value="Invia" class="bottone-personalizzato">
    </form>
    <% } %>

    <%-- Visualizza le domande esistenti --%>
    <% if (domande != null && !domande.isEmpty()) { %>
    <% for (Domanda domanda : domande) { %>
    <div class="question">
        <p><label>Utente:</label> <%= domanda.getUtente().getIdUtente() %></p>
        <p><label>Descrizione:</label> <%= domanda.getDescrizione() %></p>

        <%-- Visualizza le risposte esistenti --%>
        <% if (domanda.getRisposte() != null && !domanda.getRisposte().isEmpty()) { %>
        <% for (i = 0; i < domanda.getRisposte().size(); i++) { %>
        <p><label>Risposta n:<%= i + 1 %>:</label> <%= domanda.getRisposte().get(i).getUtente().getIdUtente() %></p>
        <p><label>Descrizione:</label> <%= domanda.getRisposte().get(i).getDescrizione() %></p>
        <% } %>
        <% } %>

        <%-- Form per inserire una nuova risposta, visibile solo se l'utente è autenticato e non è l'autore della domanda --%>
        <% if (loggedUser != null && !loggedUser.getIdUtente().equals(domanda.getUtente().getIdUtente())) { %>
        <form action="Dispatcher" method="post">
            <input type="hidden" name="controllerAction" value="DomandeManagement.addRisposta"/>
            <input type="hidden" name="idDomanda" value="<%= domanda.getIdDomanda() %>"/>
            <label for="descrizioneRisposta<%= domanda.getIdDomanda() %>">Rispondi a questa domanda: </label><br>
            <textarea id="descrizioneRisposta<%= domanda.getIdDomanda() %>" name="descrizioneRisposta" rows="4" cols="50"></textarea><br>
            <input type="submit" value="Invia" class="bottone-personalizzato">
        </form>
        <% } %>
    </div>
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

