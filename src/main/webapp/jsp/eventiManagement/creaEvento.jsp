<%@page session="false"%>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.Blob" %>
<%@ page import="java.util.Base64" %>
<%@ page import="com.managereventi.managereventi.model.mo.*" %>

<%
    Boolean loggedOnObj = (Boolean) request.getAttribute("loggedOn");
    boolean loggedOn = (loggedOnObj != null) ? loggedOnObj : false;
    Organizzatore loggedOrganizzatore = (Organizzatore) request.getAttribute("loggedOrganizzatore");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";
    int i;
    Sponsorizzazione spazio = (Sponsorizzazione) request.getAttribute("spazio");
    List<Luogo> luoghi = (List<Luogo>) request.getAttribute("luoghi");
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
            text-align: center;
            margin-top: 20px;
        }

        form {
            display: flex;
            justify-content: center;
            align-items: center;
            text-align: left;
            flex-wrap: wrap;
            margin-left: 20%;
            margin-right: 20%;

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
            margin: 10px 0 5px;
        }

        input {
            width: 100%;
            padding: 8px;
            box-sizing: border-box;
            border: #dddddd 1px solid;
            background-color: transparent;
            border-radius: 5px;
            margin-bottom: 2px;
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
    <form>
        <section id="evento">

            <h2>Inserisci dettagli evento</h2>
            <label for="nome">Nome Evento</label>
            <input type="text" id="nome" name="nome" required>
            <label for="descrizione">Descrizione dell'evento</label>
            <textarea id="descrizione" name="descrizione"> </textarea>
            <label for="datainizio">Data di inizio evento</label>
            <input type="date" id="datainizio" name="datainizio" required>
            <label for="datafine">Data di fine evento</label>
            <input type="date" id="datafine" name="datafine" required>
            <input class="input" type="file" id="imglogo" name="logo" accept="image/png, image/jpeg">
            <img id="logoPreview" style="max-width: 200px; max-height: 200px">
            <br>
            <button  class="bottone-personalizzato" id="aggiungiEsibizioneBtn">Aggiungi esibizione</button> <br>
                <section id="esibizione">
                    <h2 class="titolo" id="titoloEsibizione" style="display:none;">Inserisci dettagli esibizione</h2>
                    <label for="nomeEsibizione" style="display:none;">Nome esibizione</label>
                    <input type="text" id="nomeEsibizione" name="nomeEsibizione" style="display:none;">
                    <label for="descrizioneEsibizione" style="display:none;">Descrizione dell'esibizione</label>
                    <textarea id="descrizioneEsibizione" name="descrizioneEsibizione" style="display:none;"> </textarea>
                    <label for="durata" style="display:none;">Durata esibizione</label>
                    <input type="time" id="durata" name="durata" required style="display:none;">
                    <label for="Orainizio" style="display:none;">Ora di inizio</label>
                    <input type="time" id="Orainizio" name="orainizio" required style="display:none;">
                    <select class="tendina" name="luogo" style="display: none">
                        <option value="">Luogo</option>
                        <% for (i=0; i< luoghi.size(); i++) { %>
                        <option value="<%= luoghi.get(i) %>"><%= luoghi.get(i) %></option>
                        <% } %>
                    </select>
                    <label for="genere" style="display:none;">Genere </label>
                    <input type="text" id="genere" name="genere" required style="display:none;">
                    <input class="input" type="file" id="imglogoEsibizione" name="logoEsibizione" style="display:none;" accept="image/png, image/jpeg">
                    <img id="logoPreview1" style="max-width: 200px; max-height: 200px">
                    <br>

                </section>
            <input type="hidden" name="idorganizzatore" value="<%=loggedOrganizzatore.getIdOrganizzatore()%>">
            <input type="hidden" name="controllerAction" value="EventiManagement.creaEvento">
            <button type="submit" class="bottone-personalizzato">Pubblica evento</button>
        </section>
    </form>

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
        document.getElementById('imglogoEsibizione').addEventListener('change', function (event) {
            var file = event.target.files[0];
            var reader = new FileReader();
            reader.onloadend = function () {
                document.getElementById('logoPreview1').src = reader.result;
            }
            if (file) {
                reader.readAsDataURL(file);
            } else {
                document.getElementById('logoPreview1').src = "";
            }
        });
    }

    document.getElementById('aggiungiEsibizioneBtn').addEventListener('click', function() {
        var elements = document.querySelectorAll('#nomeEsibizione, #descrizioneEsibizione, [for=nomeEsibizione], [for=descrizioneEsibizione], [for=durata], [for=Orainizio], #durata, #Orainizio, .tendina, [for=genere], #genere, #imglogoEsibizione, #logoPreview1, .input, [for=imglogoEsibizione], [for=logoPreview1], .titolo, #titoloEsibizione');
        elements.forEach(function(el) {
            if(el.style.display === "none") {
                el.style.display = "block";
            } else {
                el.style.display = "none";
            }
        });
    });

</script>
</body>
</html>
