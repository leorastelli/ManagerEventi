<%@page session="false"%>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.Blob" %>
<%@ page import="java.util.Base64" %>
<%@ page import="com.managereventi.managereventi.model.mo.*" %>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
    Organizzatore loggedOrganizzatore = (Organizzatore) request.getAttribute("loggedOrganizzatore");
    Azienda loggedAzienda = (Azienda) request.getAttribute("loggedAzienda");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";
    int i;
    Evento evento = (Evento) request.getAttribute("evento");
    List<Esibizione> esibizioni = (List<Esibizione>) request.getAttribute("esibizioni");
    List<String> luoghi = (List<String>) request.getAttribute("luoghi");
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
            padding: 12px;
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
            height: 100%;
        }

        #creaEsibizioneForm {
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        section {
            width: 100%;
        }

        section h2 {
            margin-top: 0;
            text-align: center;
        }

        label {
            display: block;
            width: 500px;
            margin-right: 20%;
            margin-left: 20%;
            /*margin-bottom: 10px;*/
            text-align: left;
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
        input {
            width: 500px;
            margin-right: 20%;
            margin-left: 20%;
            padding: 8px;
            box-sizing: border-box;
            border: #dddddd 1px solid;
            background-color: transparent;
            border-radius: 5px;
            /*margin-top: 15px;*/
            /*margin-bottom: 10px;*/
        }

        .tendina {
            width: fit-content;
            margin-right: 23.5%;
            padding: 8px;
            box-sizing: border-box;
            border: #dddddd 1px solid;
            background-color: transparent;
            border-radius: 5px;
            /*margin-top: 15px;*/
            /*margin-bottom: 10px;*/
        }

        #descrizioneEsibizione {
            border: #dddddd 1px solid;
            margin-left: 20%;
            margin-right: 20%;
            width:494px;
            height:60px;
            border-radius: 5px;
            background-color: transparent;
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
            background-color: #00bfff;
        }

        footer {
            clear: both;
            text-align: center;
            padding: 12px;
            background-color: #ffb805;
            color: #ab00cc;
            margin-top: 20px;
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
            <li <%=menuActiveLink.equals("Home Organizzatore") ? "class=\"active\"" : ""%>>
                <a href="Dispatcher?controllerAction=OrganizzatoreManagement.view">Home Organizzatore</a>
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
    <section id="esibizione">
        <form id="creaEsibizioneForm" name="creaEsibizioneForm" action="Dispatcher" method="post" enctype="multipart/form-data">
            <h2 class="titolo" id="titoloEsibizione" >Inserisci dettagli esibizione</h2>
            <label for="nomeEsibizione" >Nome esibizione</label>
            <input type="text" id="nomeEsibizione" name="nomeEsibizione" required><br>
            <label for="descrizioneEsibizione" >Descrizione dell'esibizione</label>
            <textarea id="descrizioneEsibizione" name="descrizioneEsibizione" required> </textarea><br>
            <label for="dataEsibizione" >Data di inizio esibizione</label>
            <input type="date" id="dataEsibizione" name="dataEsibizione" required ><br>
            <label for="durata" >Durata esibizione</label>
            <input type="time" id="durata" name="durata" required ><br>
            <label for="Orainizio" >Ora di inizio</label>
            <input type="time" id="Orainizio" name="orainizio" required><br>
            <select class="tendina" name="luogo" id="tendina" required>
                <option value="">Seleziona luogo</option>
                <% for (i=0; i< luoghi.size(); i++) { %>
                    <option value="<%= luoghi.get(i) %>"><%= luoghi.get(i) %></option>
                <% } %>
            </select><br>
            <label for="genere" >Genere </label>
            <input type="text" id="genere" name="genere" required><br>
            <input class="input" type="file" id="imglogo" name="logoEsibizione"  accept="image/png, image/jpeg" required>
            <img id="logoPreview" style="max-width: 200px; max-height: 200px" alt="">
            <br>
                <input type="hidden" name="idorganizzatore" value="<%=loggedOrganizzatore.getIdOrganizzatore()%>">
                <input type="hidden" name="idevento" value="<%=evento.getIdEvento()%>">
                <input type="hidden" id="datainizio" name="datainizio" value="<%=evento.getDataInizio()%>">
                <input type="hidden" id="datafine" name="datafine" value="<%=evento.getDataFine()%>">
                <input type="hidden" name="controllerAction" value="EventiManagement.creaEsibizione">
                <input type="submit" class="bottone-personalizzato" value="Pubblica Esibizione">
        </form>
    </section>
</main>
<footer>
    &copy; 2024 EventPrime - Italia IT | Cookie e Privacy Policy<br>
    Credits: Leonardo Rastelli e Anna Ferri
</footer>
<script>
    window.onload = function() {
        document.getElementById('imglogo').addEventListener('change', function (event) {
            var file = event.target.files[0];
            var reader = new FileReader();
            reader.onloadend = function () {
                document.getElementById('logoPreview').src = reader.result;
            }
            if (file) {
                reader.readAsDataURL(file);
            } else {
                document.getElementById('logoPreview').src = "";
            }
        });
    }

    document.getElementById('creaEsibizioneForm').addEventListener('submit', function(event) {
        var datainizio = new Date(document.getElementById('datainizio').value);
        var datafine = new Date(document.getElementById('datafine').value);
        var dataEsibizione = new Date(document.getElementById('dataEsibizione').value);

        if (dataEsibizione < datainizio || dataEsibizione > datafine) {
            event.preventDefault();
            alert('La data dell\'esibizione deve essere compresa tra la' + datainizio +  'e' + datafine);
        }
    });
</script>
</body>
</html>
