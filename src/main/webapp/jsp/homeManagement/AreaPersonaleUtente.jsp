<!--Area personale dell 'utente, con lista delle 3 aree ovvero dati personali, biglietti e abbonamenti. una volta spinto su una delle 3 aree si viene indirizzati all'area della pagina che le contiene. Ogni area ha un bottone di modifica che spingendolo permette di modificare i dati presenti-->
<%@page session="false"%>
<%@page import="com.managereventi.managereventi.model.mo.Utente"%>
<%@page import="java.util.List"%>
<%@page import="com.managereventi.managereventi.model.mo.Biglietto"%>
<%@page import="com.managereventi.managereventi.model.mo.Abbonamento"%>
<%@ page import="com.managereventi.managereventi.model.mo.Recensione" %>

<%
    Boolean loggedOnObj = (Boolean) request.getAttribute("loggedOn");
    boolean loggedOn = (loggedOnObj != null) ? loggedOnObj : false;

    int i;
    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";
    List<Biglietto> biglietti = (List<Biglietto>) request.getAttribute("biglietti");
    List<Abbonamento> abbonamenti = (List<Abbonamento>) request.getAttribute("abbonamenti");
    List<Recensione> recensioni = (List<Recensione>) request.getAttribute("recensioni");
    List<String> pastEvents = (List<String>) request.getAttribute("pastEvents");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Area Utente - PrimEvent</title>
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
        .sidebar {
            float: left;
            width: 20%;
        }
        .sidebar a {
            display: block;
            padding: 10px;
            margin: 10px 0;
            background-color: #ffffcc;
            color: black;
            text-decoration: none;
            text-align: center;
            border-radius: 5px;
        }
        .sidebar a:hover {
            background-color: #ffd700;
        }
        .content {
            float: left;
            width: 75%;
            margin-left: 5%;
        }
        .section {
            margin-bottom: 30px;
        }
        .section h2 {
            background-color: #ffffcc;
            padding: 10px;
            border-radius: 5px;
        }
        .ticket, .subscription {
            display: flex;
            border: 1px solid #ccc;
            border-radius: 5px;
            padding: 10px;
            margin: 10px 0;
            background-color: white;
        }
        .ticket img, .subscription img {
            max-width: 100px;
            margin-right: 20px;
        }
        footer {
            text-align: center;
            padding: 10px;
            background-color: #6fa3ef;
            color: #fff;
            clear: both;
            margin-top: 20px;
        }
    </style>
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
</head>
<body>

    <header>
        <form name="logoutForm" action="Dispatcher" method="post">
            <input type="hidden" name="controllerAction" value="HomeManagement.logout"/>
        </form>
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
                <li <%= menuActiveLink.equals("Accedi") ? "class=\"acrive\"": ""%>>
                    <a href="Dispatcher?controllerAction=HomeManagement.gotoLogin">Accedi</a></li>
                <li <%=menuActiveLink.equals("Registrati")?"class=\"active\"":""%>>
                    <a href="Dispatcher?controllerAction=UserManagement.gotoRegistration">Registrati</a>
                        <%}%>
            </ul>
        </nav>
    </header>

    <main>
        <% if (loggedOn) { %>
        <div class="sidebar">
            <a href="#dati-personali">I miei dati personali</a>
            <a href="#biglietti">I miei biglietti</a>
            <a href="#abbonamenti">I miei abbonamenti</a>
        </div>

        <div class="content">
            <div id="dati-personali" class="section">
                <form method="post" action="Dispatcher" name="modifyForm">
                    <input type="text" name="nome" value="<%= loggedUser.getNome() %>"  >
                    <input type="text" name="cognome" value="<%= loggedUser.getCognome() %>">
                    <input type="text" name="email" value="<%=loggedUser.getEmail()%>" >
                    <input type="text" name="password" value="<%=loggedUser.getPassword()%>" >
                    <input type="text" name="idutente" value="<%=loggedUser.getIdUtente()%>" >
                    <!--<button onclick="toggleEdit('dati-personali')">Modifica i dati personali</button>-->
                    <input type="hidden" name="controllerAction" value="UserManagement.modifyUtente"/>
                    <input type="submit" value="Salva modifiche" >
                </form>
            </div>

            <div id="biglietti" class="section">
                <h2>I miei biglietti</h2>
                <%if (biglietti != null && !biglietti.isEmpty()){ %>
                <% for (i=0; i<biglietti.size();i++){%>
                <div class="ticket">
                    <div>
                        <span class="nome"><%= biglietti.get(i).getIdEvento().getNome() %></span>
                        <span class="data"><%= biglietti.get(i).getIdEvento().getDataInizio() %></span>
                        <span class="nome">Nome: <%=biglietti.get(i).getIdUtente().getNome()%></span>
                        <span class="cognome">Cognome: <%=biglietti.get(i).getIdUtente().getCognome()%></span>
                        <span class="prezzo">Prezzo: <%=biglietti.get(i).getPrezzo()%> €</span>
                        <span class="tipo">Tipo: <%=biglietti.get(i).getTipo()%></span>
                        <form name="deletBiglietto" action="Dispatcher" method="post">
                            <input type="hidden" name="controllerAction" value="UserManagement.deleteBiglietto"/>
                            <input type="hidden" name="idBiglietto" value="<%=biglietti.get(i).getIdBiglietto()%>"/>
                            <input type="submit" value="Annulla Biglietto">
                        </form>
                    </div>
                </div>
                <% }} %>
                <button onclick="toggleEdit('biglietti')">Modifica nominativi</button>
            </div>

            <div id="abbonamenti" class="section">
                <h2>I miei Abbonamenti</h2>
                <%if (biglietti != null && !biglietti.isEmpty()){ %>
                <% for (i=0; i<abbonamenti.size();i++){%>
                <div class="ticket">
                    <div>
                        <span class="nome"><%= abbonamenti.get(i).getIdEvento().getNome() %></span>
                        <span class="data"><%= abbonamenti.get(i).getIdEvento().getDataInizio() %></span>
                        <span class="nome">Nome: <%=abbonamenti.get(i).getIdUtente().getNome()%></span>
                        <span class="cognome">Cognome: <%=abbonamenti.get(i).getIdUtente().getCognome()%></span>
                        <span class="prezzo">Prezzo: <%=abbonamenti.get(i).getPrezzo()%> €</span>
                        <span class="tipo">Tipo: <%=abbonamenti.get(i).getTipo()%></span>
                        <span class="entrate">Entrate: <%=abbonamenti.get(i).getEntrate()%></span>
                        <form name="deleteAbbonamento" action="Dispatcher" method="post">
                            <input type="hidden" name="controllerAction" value="UserManagement.deleteAbbonamento"/>
                            <input type="hidden" name="idAbbonamento" value="<%=abbonamenti.get(i).getIdAbbonamento()%>"/>
                            <input type="submit" value="Annulla Abbonamento">
                        </form>
                    </div>
                </div>
                <% } }%>
                <button onclick="toggleEdit('abbonamenti')">Modifica nominativi</button>
            </div>
        </div>
        <% } else { %>
        <p>Effettua il login per vedere i tuoi dati.</p>
        <% } %>

    </main>

    <footer>
        © 2024 EventPrime - Italia IT | Cookie e Privacy Policy<br>
        Credits: Leonardo Rastelli e Anna Ferri
    </footer>

</body>
</html>
