<%@page session="false"%>
<%@page import="java.util.List"%>
<%@ page import="com.managereventi.managereventi.model.mo.*" %>
<%@ page import="java.util.Base64" %>
<%@ page import="java.sql.Blob" %>

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
        .popup {
            display: none;
            position: fixed;
            z-index: 999;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            justify-content: center;
            align-items: center;
        }

        .popup-content {
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            text-align: center;
        }

        .popup-content h3 {
            margin-top: 0;
        }

        .popup-buttons {
            margin-top: 20px;
        }

        .popup-buttons button {
            margin: 0 10px;
        }

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
        }
        .sidebar {
            float: left;
            width: 20%;
        }
        .sidebar a {
            display: block;
            padding: 10px;
            margin: 10px 0;
            background-color: #de32ff;
            color: white;
            text-decoration: none;
            text-align: center;
            border-radius: 5px;
        }
        .sidebar a:hover {
            background-color: #00cc33;
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
            background-color: #00cc33;
        }

        section#dati-aziendali, section#spazipubblicitari, section#acquista{
            margin-bottom: 30px;
            max-width: 600px;
            margin: 0 auto;

        }

        section#dati-aziendali h2, section#spazipubblicitari h2, section#acquista h2 {
            padding: 10px;
            border-radius: 5px;
            text-align: center;
            width: 100%;
            margin-left: 15%;
        }

        section#dati-aziendali form, section#spazipubblicitari form, section#acquista form {
            display: grid;
            grid-template-columns: 1fr;
            gap: 2px;
            background-color: #fffdf3;
            padding: 20px;
            border: 1px solid #ccc;
            box-shadow: dimgray 0 0 5px;
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
            font-weight: bolder;
        }

        .tendina {
            padding: 5px;
            border: 1px solid #ccc;
            border-radius: 5px;
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
    <script>
        function toggleEdit(sectionId) {
            var section = document.getElementById(sectionId);
            var inputs = section.querySelectorAll('input, textarea');
            inputs.forEach(input => {
                input.disabled = !input.disabled;
            });
        }

        function showDeleteConfirmation() {
            var popup = document.getElementById('deleteConfirmationPopup');
            popup.style.display = 'flex';
        }

        function hideDeleteConfirmation() {
            var popup = document.getElementById('deleteConfirmationPopup');
            popup.style.display = 'none';
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
    <h1 class="centrato">Benvenuto nella tua area personale <%=loggedAzienda.getNome()%>!</h1>
    <div class="sidebar">
        <a href="#dati-aziendali">Dati aziendali</a>
        <a href="#spazipubblicitari">Spazi pubblicitari acquistati</a>
        <a href="#acquista">Acquista altri spazi </a>
        <a style="background-color: darkred" href="#" onclick="showDeleteConfirmation()">Elimina Account</a>
    </div>
    <div class="content">
        <section id="dati-aziendali">
            <h2>Dati aziendali</h2>
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
            <% for (i=0; i<sponsorizzazioni.size();i++) {

                Blob logoBlob = sponsorizzazioni.get(i).getLogo();

                // Ottieni la lunghezza del Blob (dimensione dell'array di byte)
                int blobLength = (int) logoBlob.length();

                // Leggi i dati del Blob in un array di byte
                byte[] logoBytes = logoBlob.getBytes(1, blobLength);
                // Assume che getLogo() restituisca un byte array del BLOB
                String base64Image = Base64.getEncoder().encodeToString(logoBytes);%>
            <h3>Spazio n&deg; <%= i + 1 %></h3>
            <form method="post" action="Dispatcher" name="modifyForm">
                <label for="nome-evento">Evento contenente spazio: </label>
                <input type="text" id="nome-evento" name="nome-evento" value="<%= sponsorizzazioni.get(i).getIdEvento().getNome() %>" disabled>
                <label>Logo: </label>
                <img src="data:image/jpeg;base64, <%= base64Image %>" style="max-width: 200px; max-height: 200px" alt="Logo dell'azienda">
                <label for="costo">Costo: </label>
                <input type="text" id="costo" name="costo" value="<%= sponsorizzazioni.get(i).getCosto() %> &euro;" disabled >

                <form name="deleteSpazio" action="Dispatcher" method="post">
                    <input type="hidden" name="controllerAction" value="AziendaManagement.deleteSpazio"/>
                    <input type="hidden" name="idsapzio" value="<%=sponsorizzazioni.get(i).getPartitaIVA()%>"/>
                    <input type="hidden" name="idevento" value="<%=sponsorizzazioni.get(i).getIdEvento().getIdEvento()%>"/>
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
            <form method="post" action="Dispatcher" name="acquistaForm" enctype="multipart/form-data">
                <br>
                <img id="logoPreview" style="max-width: 200px; max-height: 200px">
                <br>
                <label>
                    <input type="radio" id="checkbox2" name="scelta" value="casuale" required> Scelta casuale dell'evento
                </label>
                <br>
                <label>
                    <input type="radio" id="checkboxEvento" name="scelta" value="scelta" required> Con scelta dell'evento a prezzo doppio
                </label>
                <select class="tendina" id="evento" name="evento" style="display: none; width: fit-content">
                    <option value="">Eventi</option>
                    <% for (i=0; i< eventi.size(); i++) { %>
                    <option value="<%= eventi.get(i) %>"><%= eventi.get(i) %></option>
                    <% } %>
                </select>
                <br>
                <br>
                <input type="hidden" name="controllerAction" value="AziendaManagement.gotoPagamento" />
                <input type="submit" class="bottone-personalizzato" value="Procedi con l'acquisto">
            </form>

        </section>
    </div>
    <% } else{ %>
    <p>Effettua il login per vedere i tuoi dati.</p>
    <% } %>
</main>
<script>

        window.onload = function() {
        // Funzione per gestire il cambio di stato del radio button
        document.getElementById('checkboxEvento').addEventListener('change', function () {
            var selectEvento = document.getElementById('evento');
            if (this.checked) {
                selectEvento.style.display = 'block'; // Mostra il menu a tendina
                selectEvento.setAttribute('required', 'required'); // Rendi il menu a tendina required
            }
        });
            document.getElementById('checkbox2').addEventListener('change', function () {
                var selectEvento = document.getElementById('evento');
                if (this.checked) {
                    selectEvento.style.display = 'none'; // Mostra il menu a tendina
                    selectEvento.removeAttribute('required'); // Rendi il menu a tendina required
                }
            });

        // Gestione iniziale dello stato del menu a tendina al caricamento della pagina
        var selectEvento = document.getElementById('evento');
        if (document.getElementById('checkboxEvento').checked) {
        selectEvento.style.display = 'block'; // Mostra il menu a tendina se il radio button è già selezionato
        selectEvento.setAttribute('required', 'required'); // Rendi il menu a tendina required
         } else {
            selectEvento.style.display = 'none'; // Altrimenti nascondi il menu a tendina
            selectEvento.removeAttribute('required'); // E rimuovi il required
    }
    };
</script>


<footer>
    &copy; 2024 EventPrime - Italia IT | Cookie e Privacy Policy<br>
    Credits: Leonardo Rastelli e Anna Ferri
</footer>

<!-- Popup di conferma eliminazione -->
<div id="deleteConfirmationPopup" class="popup">
    <div class="popup-content">
        <h3>Sei sicuro di voler eliminare il tuo account?</h3>
        <div class="popup-buttons">
            <form name="deleteUserForm" action="Dispatcher" method="post">
                <input type="hidden" name="controllerAction" value="AziendaManagement.deleteAzienda"/>
                <input style="background-color: red" type="submit" value="Conferma">
            </form>
            <button style="background-color: #cccccc" onclick="hideDeleteConfirmation()">Esci</button>
        </div>
    </div>
</div>
</body>
</html>
