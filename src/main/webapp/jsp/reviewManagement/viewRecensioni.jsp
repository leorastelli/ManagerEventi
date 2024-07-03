<%@ page session="false" %>
<%@ page import="com.managereventi.managereventi.model.mo.Utente" %>
<%@ page import="java.util.List" %>
<%@ page import="com.managereventi.managereventi.model.mo.Recensione" %>


<%
    Boolean loggedOnObj = (Boolean) request.getAttribute("loggedOn");
    boolean loggedOn = (loggedOnObj != null) ? loggedOnObj : false;
    int i;
    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
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
        .review {
            background-color: white;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
            margin-bottom: 20px;
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

    function autoSubmit() {
        document.getElementById('filterForm').submit();
    }

</script>
<header>
    <h1>PrimEvent</h1>
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
    <h2>Cosa dicono di noi</h2>
    <div class="search-bar">
        <form action="Dispatcher" method="post">
            <input type="hidden" name="controllerAction" value="ReviewManagement.filter"/>

            <select name="nomeEvento">
                <option value=""></option>
                <% for (i=0; i< eventi.size(); i++) { %>
                <option value="<%= eventi.get(i) %>"><%= eventi.get(i) %></option>
                <% } %>
            </select>

            <select name="numeroStelle" onchange="autoSubmit()">
                <option value="">Tutte le stelle</option>
                <option value="1">1 stella</option>
                <option value="2">2 stelle</option>
                <option value="3">3 stelle</option>
                <option value="4">4 stelle</option>
                <option value="5">5 stelle</option>
            </select>
            <input type="submit" name="submitButton" value="Cerca">
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
