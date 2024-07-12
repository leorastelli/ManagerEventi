<%@page session="false"%>
<%@page import="java.util.List"%>
<%@ page import="com.managereventi.managereventi.model.mo.*" %>
<%@ page import="java.io.InputStream" %>

<%
    Boolean loggedOnObj = (Boolean) request.getAttribute("loggedOn");
    boolean loggedOn = (loggedOnObj != null) ? loggedOnObj : false;
    int i;
    Organizzatore loggedOrganizzatore = (Organizzatore) request.getAttribute("loggedOrganizzatore");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";
    Sponsorizzazione spazio = (Sponsorizzazione) request.getAttribute("spazio");
    List<Luogo> luoghi = (List<Luogo>) request.getAttribute("luoghi");
%>
<html>
<style>
    body {
        font-family: Arial, sans-serif;
        margin: 0;
        padding: 0;
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
        display: flex;
        justify-content: space-around;
        padding: 20px;
        background-color: white;
    }

    form {
        display: grid;
        grid-template-columns: 1fr 1fr;
        grid-template-rows: auto auto;
        grid-template-areas:
        "indirizzo pagamento"
        "indirizzo riepilogo";
        column-gap: 10%;
        width: 90%;
    }

    section {
        width: 100%;
    }

    section h2 {
        margin-top: 0;
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
        display: block;
        margin: 0 auto;
        width: fit-content;
        background-color: #6fa3ef;
        color: white;
        padding: 10px 20px;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        align-items: center;
        text-align: center;
    }

    #indirizzo-fatturazione {
        grid-area: indirizzo;
    }

    #dettagli-pagamento {
        grid-area: pagamento;
        justify-self: end;
        margin-bottom: 20px;
    }

    #riepilogo-ordine {
        grid-area: riepilogo;
        justify-self: end;
        align-self: end;
    }

    .bottone-personalizzato:hover {
        background-color: #007FFF;
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
    <form id="paymentForm" name="pagamentoForm" method="post" action="Dispatcher" enctype="multipart/form-data">
        <section id="indirizzo-fatturazione">
            <h2>Indirizzo di fatturazione</h2>
            <label for="nome">Nome Evento</label>
            <input type="text" id="nome" name="nome" required>
            <label for="descrizione">Descrizione dell'evento</label>
            <textarea id="descrizione" name="descrizione"> </textarea>
            <label for="datainizio">Data di inizio evento</label>
            <input type="date" id="datainizio" name="datainizio" required>
            <label for="datafine">Data di fine evento</label>
            <input type="date" id="datafine" name="datafine" required>
            <label for="numes">Numero di esibizione</label>
            <input type="text" id="numes" name="numes" required>
            <input class="input" type="file" id="imglogo" name="logo" accept="image/png, image/jpeg">
            <img id="logoPreview" style="max-width: 200px; max-height: 200px">
            <button id="aggiungiEsibizioneBtn">Aggiungi esibizione</button>
                <label for="nomeEsibizione" style="display:none;">Nome esibizione</label>
                <input type="text" id="nomeEsibizione" name="nomeEsibizione" style="display:none;">
                <label for="descrizioneEsibizione" style="display:none;">Descrizione dell'esibizione</label>
                <textarea id="descrizioneEsibizione" name="descrizione" style="display:none;"> </textarea>
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
            <input type="hidden" name="idorganizzatore" value="<%=loggedOrganizzatore.getIdOrganizzatore()%>">
            <input type="hidden" name="controllerAction" value="EventiManagement.creaEvento">
            <button type="submit" class="bottone-personalizzato">Pubblica evento</button>
        </section>
    </form>
</main>
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

    document.getElementById('aggiungiEsibizioneBtn').addEventListener('click', function() {
        var elements = document.querySelectorAll('#nomeEsibizione, #descrizioneEsibizione, #datainizio, #datafine, [for=nomeEsibizione], [for=descrizioneEsibizione], [for=durata], [for=datafine]');
        elements.forEach(function(el) {
            if(el.style.display === "none") {
                el.style.display = "block";
            } else {
                el.style.display = "none";
            }
        });
    });
</script>
<footer>
    &copy; 2024 EventPrime - Italia IT | Cookie e Privacy Policy<br>
    Credits: Leonardo Rastelli e Anna Ferri
</footer>

</body>
</html>
