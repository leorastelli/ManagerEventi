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
            background-color: #A6FBFF;
            color: black;
            text-decoration: none;
            text-align: center;
            border-radius: 5px;
        }
        .sidebar a:hover {
            background-color: #007FFF;
        }
        .content {
            float: left;
            width: 75%;
            margin-left: 5%;
        }

        .section h2 {
            background-color: #007FFF;
            padding: 10px;
            border-radius: 5px;
        }

        .ticket img, .subscription img {
            max-width: 100px;
            margin-right: 20px;
        }

        section#biglietti, section#abbonamenti {
            margin-bottom: 30px;
            max-width: 600px;
            margin: 0 auto;
        }
        section#dati-personali{
            margin-bottom: 30px;
            max-width: 600px;
            margin: 0 auto;
        }

        section#dati-personali h2, section#biglietti h2, section#abbonamenti h2, section#recensioni h2{
            background-color: #A6FBFF;
            padding: 10px;
            border-radius: 5px;
        }

        section#dati-personali form {
            display: grid;
            grid-template-columns: 1fr;
            gap: 2px;
            background-color: white;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        section#dati-personali form label {
            margin-right: 50px;
            font-weight: bold;
            text-align: left;
        }

        section#dati-personali form input[type="text"] {
            padding: 5px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        section#biglietti div, section#abbonamenti div{
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 1px;
            background-color: white;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        section#biglietti div label, section#abbonamenti div label {
            font-weight: bold;
            text-align: left;
            display: grid;
            gap: 2px;
        }

        .bottone-personalizzato {
            background-color: #6fa3ef;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-right: 10px;
            width: 50%;
            align-items: center;
            display: block;
            margin: auto;
        }

        .bottone-personalizzato:hover {
            background-color: #007FFF; /* Colore di sfondo al passaggio del mouse */
        }

        .bottone-pers {
            background-color: #6fa3ef;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            width: 100%;
            display: inline-block;
            margin-right: 10px;
            text-align: center;
        }

        .bottone-pers:hover {
            background-color: #007FFF; /* Colore di sfondo al passaggio del mouse */
        }

        section#biglietti form {
            display: grid;
            justify-content: space-between;
            align-items: center;
            grid-gap: 1px;
        }

        section#recensioni {
            margin-bottom: 30px;
            max-width: 600px;
            margin: 0 auto;
        }

        section#recensioni form {
            display: grid;
            grid-template-columns: 1fr;
            gap: 2px;
            background-color: white;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        section#recensioni form label {
            font-weight: bold;
            margin-bottom: 5px;
        }

        section#recensioni form select,
        section#recensioni form textarea,
        section#recensioni form input[type="submit"] {
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        section#recensioni form textarea {
            resize: vertical;
            min-height: 100px;
        }

        section#recensioni .star {
            font-size: 2rem;
            cursor: pointer;
            color: #ccc;
        }

        section#recensioni .star.selected {
            color: gold;
        }

        footer {
            position: fixed;
            bottom: 0;
            width: 100%;
            clear: both;
            text-align: center;
            padding: 10px;
            background-color: #6fa3ef;
            color: #fff;
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

        document.addEventListener("DOMContentLoaded", function() {
            const stars = document.querySelectorAll('.star');
            stars.forEach(star => {
                star.addEventListener('click', function() {
                    const rating = this.getAttribute('data-rating');
                    document.getElementById('ratingValue').value = rating;
                    stars.forEach(s => {
                        s.classList.remove('selected');
                    });
                    this.classList.add('selected');
                    this.previousElementSibling.classList.add('selected');
                    if (this.previousElementSibling.previousElementSibling) {
                        this.previousElementSibling.previousElementSibling.classList.add('selected');
                    }
                    if (this.previousElementSibling.previousElementSibling.previousElementSibling) {
                        this.previousElementSibling.previousElementSibling.previousElementSibling.classList.add('selected');
                    }
                    if (this.previousElementSibling.previousElementSibling.previousElementSibling.previousElementSibling) {
                        this.previousElementSibling.previousElementSibling.previousElementSibling.previousElementSibling.classList.add('selected');
                    }
                });
            });
        });


    </script>
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
        <a href="#recensioni">Scrivi la tua opinione</a>
    </div>

    <div class="content">

        <section id="dati-personali">
            <h2>I miei dati personali</h2>
            <form method="post" action="Dispatcher" name="modifyForm">
                <label for="nome">Nome </label>
                <input type="text" id="nome" name="nome" value="<%= loggedUser.getNome() %>"  > <br>
                <label for="cognome">Cognome </label>
                <input type="text" id="cognome" name="cognome" value="<%= loggedUser.getCognome() %>"> <br>
                <label for="email">Email </label>
                <input type="text" id="email" name="email" value="<%=loggedUser.getEmail()%>" > <br>
                <label for="password">Password </label>
                <input type="text" id="password" name="password" value="<%=loggedUser.getPassword()%>" > <br>
                <label for="idutente">Username </label>
                <input type="text" id="idutente" name="idutente" value="<%=loggedUser.getIdUtente()%>" disabled> <br>
                <!--<button onclick="toggleEdit('dati-personali')">Modifica i dati personali</button>-->
                <input type="hidden" name="controllerAction" value="UserManagement.modifyUtente"/>
                <input type="submit" class="bottone-personalizzato" value="Salva modifiche" >
            </form>
        </section>

        <section id="biglietti" >
            <h2>I miei biglietti</h2>
            <%if (biglietti != null && !biglietti.isEmpty()){ %>
            <% for (i=0; i<biglietti.size();i++){%>
            <h3>Biglietto n&deg; <%= i + 1 %></h3>
            <div>
                <%--@declare id="data"--%><%--@declare id="prezzo"--%><%--@declare id="tipo"--%>
                <%--@declare id="ora"--%>
                <label for="nome">Nome Esibizione </label> <span class="nome"><%= biglietti.get(i).getIdEsibizione().getNome() %></span>
                <label for="ora">Ora Inizio Esibizione </label> <span class="ora"><%= biglietti.get(i).getIdEsibizione().getOraInizio() %></span>
                <label for="data">Data Inizio Evento </label> <span class="data"><%= biglietti.get(i).getIdEvento().getDataInizio() %></span>
                <label for="nome">Nome </label> <span class="nome"><%=biglietti.get(i).getIdUtente().getNome()%></span>
                <label for="cognome">Cognome </label> <span class="cognome"><%=biglietti.get(i).getIdUtente().getCognome()%></span>
                <label for="prezzo">Prezzo </label> <span class="prezzo"><%=biglietti.get(i).getPrezzo()%> &euro;</span>
                <label for="tipo">Tipo </label> <span class="tipo"><%=biglietti.get(i).getTipo()%></span>
            </div>

            <form name="deletBiglietto" action="Dispatcher" method="post">
                <input type="hidden" name="controllerAction" value="UserManagement.deleteBiglietto"/>
                <input type="hidden" name="idBiglietto" value="<%=biglietti.get(i).getIdBiglietto()%>"/>
                <input type="submit" class="bottone-pers" value="Annulla Biglietto">
            </form>
            <% }} %>
            <br><button class="bottone-personalizzato" onclick="toggleEdit('biglietti')">Modifica nominativi</button>
        </section>

        <section id="abbonamenti" >
            <h2>I miei Abbonamenti</h2>
            <%if (biglietti != null && !biglietti.isEmpty()){ %>
            <% for (i=0; i<abbonamenti.size();i++){%>
            <h3>Abbonamento n&deg; <%= i + 1 %></h3>
            <div>
                <%--@declare id="entrate"--%>
                <label for="nome">Nome Evento </label> <span class="nome"><%= abbonamenti.get(i).getIdEvento().getNome() %></span>
                <label for="data">Data Inizio </label> <span class="data"><%= abbonamenti.get(i).getIdEvento().getDataInizio() %></span>
                <label for="nome">Nome </label> <span class="nome"><%=abbonamenti.get(i).getIdUtente().getNome()%></span>
                <label for="cognome">Cognome </label> <span class="cognome"><%=abbonamenti.get(i).getIdUtente().getCognome()%></span>
                <label for="prezzo">Prezzo </label> <span class="prezzo"><%=abbonamenti.get(i).getPrezzo()%> &euro;</span>
                <label for="tipo">Tipo </label> <span class="tipo"><%=abbonamenti.get(i).getTipo()%></span>
                <label for="entrate">Entrate</label> <span class="entrate"><%=abbonamenti.get(i).getEntrate()%></span>
            </div>
            <form name="deleteAbbonamento" action="Dispatcher" method="post">
                <input type="hidden" name="controllerAction" value="UserManagement.deleteAbbonamento"/>
                <input type="hidden" name="idAbbonamento" value="<%=abbonamenti.get(i).getIdAbbonamento()%>"/>
                <input type="submit" class="bottone-personalizzato" value="Annulla Abbonamento">
            </form>
            <% } }%>
            <br><button class="bottone-personalizzato" onclick="toggleEdit('abbonamenti')">Modifica nominativi</button>
        </section>

        <section id="recensioni">
            <h2>Scrivi la tua opinione</h2>
            <form action="Dispatcher" method="post">
                <%--@declare id="event"--%>
                <label for="event">Seleziona evento </label>
                <select name="nomeEvento">
                    <option value=""></option>
                    <% for (i=0; i< pastEvents.size(); i++) { %>
                    <option value="<%= pastEvents.get(i) %>"><%= pastEvents.get(i) %></option>
                    <% } %>
                </select>
                <label for="stelle">Assegna un numero di stelle</label>
                <div class = "star.selected" id="stelle" style="display: flex;">
                    <span class="star" data-rating="1">&#9733;</span>
                    <span class="star" data-rating="2">&#9733;</span>
                    <span class="star" data-rating="3">&#9733;</span>
                    <span class="star" data-rating="4">&#9733;</span>
                    <span class="star" data-rating="5">&#9733;</span>
                </div>
                <input type="hidden" name="rating" id="ratingValue" value="">
                <textarea name="descrizione" placeholder="Aggiungi una descrizione"></textarea>
                <input type="hidden" name="controllerAction" value="UserManagement.submitReview"/>
                <input type="submit" class="bottone-personalizzato" value="Invia Recensione">
            </form>
        </section>
        <br><br>

    </div>
    <% } else { %>
    <p>Effettua il login per vedere i tuoi dati.</p>
    <% } %>

</main>

<footer>
    &copy; 2024 EventPrime - Italia IT | Cookie e Privacy Policy<br>
    Credits: Leonardo Rastelli e Anna Ferri
</footer>

</body>
</html>