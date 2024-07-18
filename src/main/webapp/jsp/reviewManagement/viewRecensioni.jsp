<%@ page session="false" %>
<%@ page import="com.managereventi.managereventi.model.mo.Utente" %>
<%@ page import="java.util.List" %>
<%@ page import="com.managereventi.managereventi.model.mo.Recensione" %>
<%@ page import="com.managereventi.managereventi.model.mo.Azienda" %>
<%@ page import="com.managereventi.managereventi.model.mo.Organizzatore" %>


<%
    Boolean loggedOnObj = (Boolean) request.getAttribute("loggedOn");
    boolean loggedOn = (loggedOnObj != null) ? loggedOnObj : false;
    int i;
    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
    Organizzatore loggedOrganizzatore = (Organizzatore) request.getAttribute("loggedOrganizzatore");
    Azienda loggedAzienda = (Azienda) request.getAttribute("loggedAzienda");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";
    List<Recensione> recensioni = (List<Recensione>) request.getAttribute("recensioni");
    List <String> eventi = (List<String>) request.getAttribute("eventi");

%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cosa dicono di noi - PrimEvent</title>
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
            height: calc(100% - 20px);
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

        .review {
            background-color: #fffdf3;
            padding: 20px;
            border: 1px solid #ccc;
            box-shadow: dimgray 0 0 5px;
            border-radius: 5px;
            margin-bottom: 20px;
            width: 40%;
            margin-left: 30%;
            margin-left: 30%;

        }

        .review label {
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

        footer {

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
    <h1 class="centrato">Scopri cosa dicono di noi!</h1> <br>
    <div class="search-bar">
        <form action="Dispatcher" method="post">
            <input type="hidden" name="controllerAction" value="ReviewManagement.filter"/>

            <select name="nomeEvento">
                <option value="">Tutti gli eventi</option>
                <% for (i=0; i< eventi.size(); i++) { %>
                <option value="<%= eventi.get(i) %>"><%= eventi.get(i) %></option>
                <% } %>
            </select>

            <select name="numeroStelle">
                <option value="">Tutte le stelle</option>
                <option value="1">1 stella</option>
                <option value="2">2 stelle</option>
                <option value="3">3 stelle</option>
                <option value="4">4 stelle</option>
                <option value="5">5 stelle</option>
            </select>
            <br><br>
            <input type="submit" name="submitButton" class="bottone-personalizzato" value="Cerca">
        </form>
    </div>
    <% if (recensioni != null && !recensioni.isEmpty()) { %>
    <% for (Recensione recensione : recensioni) { %>
    <div class="review">
        <p><label>Nome Evento:</label> <%= recensione.getIdEvento().getNome()%></p>
        <p><label>Nome Utente:</label> <%= recensione.getIdUtente().getNome() %></p>
        <p><label>Numero di Stelle:</label> <%= recensione.getStelle() %></p>
        <p><label>Descrizione:</label> <%= recensione.getDescrizione() %></p>
    </div>
    <% } %>
    <% } else { %>
    <p>Nessuna recensione disponibile.</p>
    <% } %>
</main>

<footer>
    &copy; 2024 EventPrime - Italia IT | Cookie e Privacy Policy<br>
    Credits: Leonardo Rastelli e Anna Ferri
</footer>

</body>
</html>
