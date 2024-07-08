<%@page session="false"%>
<%@page import="java.util.List"%>
<%@ page import="com.managereventi.managereventi.model.mo.*" %>

<%
    Boolean loggedOnObj = (Boolean) request.getAttribute("loggedOn");
    boolean loggedOn = (loggedOnObj != null) ? loggedOnObj : false;

    int i;
    Azienda loggedAzienda = (Azienda) request.getAttribute("loggedAzienda");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home Azienda";
    List<Evento> eventi = (List<Evento>) request.getAttribute("eventi");
    List<Sponsorizzazione> sponsorizzazioni = (List<Sponsorizzazione>) request.getAttribute("sponsorizzazioni");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Area Azienda - PrimEvent</title>
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
            width: 100%;
            margin-left: auto;

        }
        .section {
            margin-bottom: 30px;
        }

        form {
            display: grid;
            gap: 10px;
        }
        form label {
            margin-right: 50px;
            font-weight: bold;
            text-align: left;
        }

        section#dati-aziendali form input[type="text"]{
            padding: 5px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .bottone-personalizzato {
            background-color: #6fa3ef;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            width: 50%;
            align-items: center;
            display: block;
            margin: auto;
        }
        .bottone-personalizzato:hover {
            background-color: #007FFF; /* Colore di sfondo al passaggio del mouse */
        }

        .input {
            padding: 5px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        section#dati-aziendali, section#spazipubblicitari, section#acquista{
            margin-bottom: 30px;
            max-width: 600px;
            margin: 0 auto;

        }

        section#dati-aziendali h2, section#spazipubblicitari h2, section#acquista h2 {
            background-color: #A6FBFF;
            padding: 10px;
            border-radius: 5px;
            text-align: center;
            width: 825px;
        }

        section#dati-aziendali form, section#spazipubblicitari form, section#acquista form {
            display: grid;
            grid-template-columns: 1fr;
            gap: 2px;
            background-color: white;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
            width: 800px;
            margin: auto;
        }

        #spazipubblicitari input[type="text"] {
            border: none;
            background-color: transparent;
            color: #000;
            pointer-events: none;
        }

        .centrato {
            text-align: center;
            margin: auto;
            width: 100%;
            font-weight: normal;
        }

        .tendina {
            padding: 5px;
            border: 1px solid #ccc;
            border-radius: 5px;
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
            var inputs = section.querySelectorAll('input, textarea');
            inputs.forEach(input => {
                input.disabled = !input.disabled;
            });
        }

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
            <li <%=menuActiveLink.equals("Home Azienda") ? "class=\"active\"" : ""%>>
                <a href="Dispatcher?controllerAction=AziendaManagement.view">Home Azienda</a>
            </li>
            <li><a href="javascript:logoutForm.submit()">Logout</a></li>
            <% } else { %>
            <li <%= menuActiveLink.equals("Accedi") ? "class=\"active\"" : ""%>>
                <a href="Dispatcher?controllerAction=HomeManagement.gotoLogin">Accedi</a></li>
            <li <%=menuActiveLink.equals("Registrati")?"class=\"active\"":""%>>
                <a href="Dispatcher?controllerAction=UserManagement.gotoRegistration">Registrati</a>
                    <%}%>
        </ul>
    </nav>
</header>
<main>
    <% if (loggedOn) { %>
    <h1 class="centrato">Benvenuto nella tua area personale!</h1>
    <div class="sidebar">
        <a href="#dati-aziendali">Dati aziendali</a>
        <a href="#spazipubblicitari">Spazi pubblicitari acquistati</a>
        <a href="#acquista">Acquista altri spazi </a>
    </div>
    <div class="content">
        <section id="dati-aziendali">
            <h2>I miei dati personali</h2>
            <form method="post" action="Dispatcher" name="modifyForm">
                <label for="partitaIVA">Partita IVA: </label>
                <input type="text" id="partitaIVA" name="partitaIVA" value="<%= loggedAzienda.getPartitaIVA() %>" disabled> <br>
                <label for="nome">Nome Azienda: </label>
                <input type="text" id="nome" name="nome" value="<%= loggedAzienda.getNome() %>"> <br>
                <label for="indirizzo">Indirizzo: </label>
                <input type="text" id="indirizzo" name="indirizzo" value="<%= loggedAzienda.getIndirizzo()%>" > <br>
                <label for="citta">Citt&agrave;: </label>
                <input type="text" id="citta" name="citta" value="<%= loggedAzienda.getCitta()%>" > <br>
                <label for="provincia">Provincia: </label>
                <input type="text" id="provincia" name="provincia" value="<%= loggedAzienda.getProvincia()%>" > <br>
                <label for="cap">CAP: </label>
                <input type="text" id="cap" name="cap" value="<%= loggedAzienda.getCap()%>" > <br>
                <label for="stato">Stato: </label>
                <input type="text" id="stato" name="stato" value="<%= loggedAzienda.getStato()%>" > <br>
                <label for="telefono">Telefono: </label>
                <input type="text" id="telefono" name="telefono" value="<%= loggedAzienda.getTelefono()%>" > <br>
                <label for="email">Email: </label>
                <input type="text" id="email" name="email" value="<%= loggedAzienda.getEmail()%>" > <br>
                <label for="password">Password: </label>
                <input type="text" id="password" name="password" value="<%=loggedAzienda.getPassword()%>" > <br>

                <input type="hidden" name="controllerAction" value="AziendaManagement.modifyAzienda"/>
                <input type="submit" class="bottone-personalizzato" value="Salva modifiche" >
            </form>
        </section>

        <section id="spazipubblicitari" class="section">
            <h2>Spazi pubblicitari acquistati</h2>
            <% if (sponsorizzazioni != null && !sponsorizzazioni.isEmpty()) {%>
            <% for (i=0; i<sponsorizzazioni.size();i++) {%>
            <h3>Spazio n&deg; <%= i + 1 %></h3>
            <form method="post" action="Dispatcher" name="modifyForm">
                <label for="nome-evento">Evento contenente spazio: </label>
                <input type="text" id="nome-evento" name="nome-evento" value="<%= sponsorizzazioni.get(i).getIdEvento().getNome() %>" disabled>
                <label for="logo">Logo: </label>
                <input type="text" id="logo" name="logo" value="<%= sponsorizzazioni.get(i).getLogo() %>" disabled>
                <label for="costo">Costo: </label>
                <input type="text" id="costo" name="costo" value="<%= sponsorizzazioni.get(i).getCosto() %> &euro;" disabled >

                <form name="deleteSpazio" action="Dispatcher" method="post">
                    <input type="hidden" name="controllerAction" value="AziendaManagement.deleteSpazio"/>
                    <input type="hidden" name="idsapzio" value="<%=sponsorizzazioni.get(i).getPartitaIVA()%>"/>
                    <input type="submit" class="bottone-personalizzato" value="Elimina spazio">
                </form>
            </form>
            <% } %>
            <% } %>
        </section>

        <section id="acquista" class="section">
            <h2>Acquista spazi pubblicitari</h2>
            <h3 class="centrato" style="font-weight: normal; text-align: center; width: 825px;" >Scegli se decidere tu quale evento esibir&agrave; la tua azienda oppure affidati alla scelta casuale!</h3>
            <br>
            <label style="font-weight: bold" for="imglogo">Carica il logo che desideri esibire</label>
            <input class="input" type="file" id="imglogo"> <br>
            <img id="logoPreview" style="max-width: 200px; max-height: 200px">
            <br>
            <label>
                <input type="checkbox"> Scelta casuale dell'evento
            </label> <br>
            <label>
                <input type="checkbox" id="checkboxEvento"> Con scelta dell'evento a prezzo doppio
            </label>
            <select class="tendina" id="evento" name="evento" style="display: none">
                <option value="">Eventi</option>
                <% for (i=0; i< eventi.size(); i++) { %>
                <option value="<%= eventi.get(i) %>"><%= eventi.get(i) %></option>
                <% } %>
            </select>
            <br> <br>
            <button class="bottone-personalizzato">Procedi con l'acquisto</button>
        </section>
    </div>
    <% } else{ %>
    <p>Effettua il login per vedere i tuoi dati.</p>
    <% } %>
</main>
<script>
    window.onload = function() {
        document.getElementById('imglogo').addEventListener('change', function(event) {
            var file = event.target.files[0];
            var reader = new FileReader();
            reader.onloadend = function() {
                document.getElementById('logoPreview').src = reader.result;
            }
            if (file) {
                reader.readAsDataURL(file);
            } else {
                document.getElementById('logoPreview').src = "";
            }
        });

        document.getElementById('checkboxEvento').addEventListener('change', function() {
            var selectEvento = document.getElementById('evento');
            if (this.checked) {
                selectEvento.style.display = 'block';
            } else {
                selectEvento.style.display = 'none';
            }
        });
    };

</script>
<footer>
    &copy; 2024 EventPrime - Italia IT | Cookie e Privacy Policy<br>
    Credits: Leonardo Rastelli e Anna Ferri
</footer>
</body>
</html>
