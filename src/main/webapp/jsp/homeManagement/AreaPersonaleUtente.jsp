<!--Area personale dell 'utente, con lista delle 3 aree ovvero dati personali, biglietti e abbonamenti. una volta spinto su una delle 3 aree si viene indirizzati all'area della pagina che le contiene. Ogni area ha un bottone di modifica che spingendolo permette di modificare i dati presenti-->
<%@page session="false"%>
<%@page import="com.managereventi.managereventi.model.mo.Utente"%>
<%@page import="java.util.List"%>
<%@page import="com.managereventi.managereventi.model.mo.Biglietto"%>
<%@page import="com.managereventi.managereventi.model.mo.Abbonamento"%>

<%
  boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
  Utente loggedUser = (Utente) request.getAttribute("loggedUser");
  List<Biglietto> biglietti = (List<Biglietto>) request.getAttribute("biglietti");
  List<Abbonamento> abbonamenti = (List<Abbonamento>) request.getAttribute("abbonamenti");
  String applicationMessage = (String) request.getAttribute("applicationMessage");
  String menuActiveLink = "Home";
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
        <h1>PrimEvent</h1>
        <nav>
            <ul>
                <li><a href="home.html">Home</a></li>
                <li><a href="login.html">Accedi</a></li>
                <li><a href="registrazione1.html">Iscriviti</a></li>
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
                <h2>I miei dati personali</h2>
                <p>Nome: <input type="text" value="<%= loggedUser.getNome() %>" disabled></p>
                <p>Cognome: <input type="text" value="<%= loggedUser.getCognome() %>" disabled></p>
                <p>Email: <input type="email" value="<%= loggedUser.getEmail() %>" disabled></p>
                <p>Password: <input type="password" value="********" disabled></p>
                <p><input type="checkbox" style="display: none;"> Modifica i dati personali</p>
                <button onclick="toggleEdit('dati-personali')">Modifica i dati personali</button>
            </div>

            <div id="biglietti" class="section">
                <h2>I miei biglietti</h2>
                <% for (Biglietto biglietto : biglietti) { %>
                <div class="ticket">
                    <img src="<%= biglietto.getImmagine() %>" alt="Concerto">
                    <div>
                        <p><%= biglietto.getNomeEvento() %></p>
                        <p><%= biglietto.getDataEvento() %></p>
                        <p>Nome: <%= biglietto.getNomeAcquirente() %></p>
                        <p>Tipo biglietto: <%= biglietto.getTipoBiglietto() %></p>
                        <p>Totale: <%= biglietto.getPrezzo() %> €</p>
                    </div>
                </div>
                <% } %>
                <button onclick="toggleEdit('biglietti')">Modifica nominativi</button>
            </div>

            <div id="abbonamenti" class="section">
                <h2>I miei abbonamenti</h2>
                <% for (Abbonamento abbonamento : abbonamenti) { %>
                <div class="subscription">
                    <img src="<%= abbonamento.getImmagine() %>" alt="Abbonamento">
                    <div>
                        <p><%= abbonamento.getNomeEvento() %></p>
                        <p><%= abbonamento.getDataInizio() %> - <%= abbonamento.getDataFine() %></p>
                        <p>Nome: <%= loggedUser.getNome() %> <%= loggedUser.getCognome() %></p>
                        <p>Tipo abbonamento: <%= abbonamento.getTipoAbbonamento() %></p>
                        <p>Totale: <%= abbonamento.getPrezzo() %> €</p>
                    </div>
                </div>
                <% } %>
                <button onclick="toggleEdit('abbonamenti')">Modifica abbonamenti</button>
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
