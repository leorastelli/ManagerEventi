<%@page session="false"%>
<%@page import="java.util.List"%>
<%@ page import="com.managereventi.managereventi.model.mo.*" %>
<%@ page import="java.io.InputStream" %>

<%
    Boolean loggedOnObj = (Boolean) request.getAttribute("loggedOn");
    boolean loggedOn = (loggedOnObj != null) ? loggedOnObj : false;
    int i;
    Azienda loggedAzienda = (Azienda) request.getAttribute("loggedAzienda");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";
    Sponsorizzazione spazio = (Sponsorizzazione) request.getAttribute("spazio");
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
            <label for="nomeAzienda">Nome azienda</label>
            <input type="text" id="nomeAzienda" name="nomeAzienda" required>
            <label for="indirizzo">Indirizzo</label>
            <input type="text" id="indirizzo" name="indirizzo" required>
            <label for="citta">Citt&agrave;</label>
            <input type="text" id="citta" name="citta" required>
            <label for="provincia">Provincia</label>
            <input type="text" id="provincia" name="provincia" required>
            <label for="cap">CAP</label>
            <input type="text" id="cap" name="cap" required>
            <label for="stato">Stato</label>
            <input type="text" id="stato" name="stato" required>
            <label for="telefono">Numero di telefono</label>
            <input type="tel" id="telefono" name="telefono" required>
            <label for="email">E-mail</label>
            <input type="email" id="email" name="email" required>
        </section>
        <section id="dettagli-pagamento">
            <h2>Dettagli Pagamento</h2>
            <label for="nome">Nome</label>
            <input type="text" id="nome" name="nome" required>
            <label for="cognome">Cognome</label>
            <input type="text" id="cognome" name="cognome" required>
            <label for="numeroCarta">Numero carta</label>
            <input type="text" id="numeroCarta" name="numeroCarta" pattern="\d{16}" maxlength="16" required>
            <label for="scadenza">Data di scadenza</label>
            <input type="month" id="scadenza" name="scadenza" required>
            <label for="cvv">CVV</label>
            <input type="text" id="cvv" name="cvv" required maxlength="3" pattern="\d{3}" >
        </section>
        <section id="riepilogo-ordine">
            <h2>Riepilogo Ordine</h2>
            <p><strong>Nome evento: </strong><%= spazio.getIdEvento().getNome()%> </p>
            <p><strong>Nome azienda: </strong><%= loggedAzienda.getNome() %></p>
            <p><strong>Spazio: </strong><%= spazio.getIdEvento().getNome() %> <span class="prezzo"><%= spazio.getCosto() %> &euro;</span></p>
            <label style="font-weight: bold" for="imglogo">Carica il logo che desideri esibire</label>
            <input class="input" type="file" id="imglogo" name="logo" accept="image/png, image/jpeg">
            <img id="logoPreview" style="max-width: 200px; max-height: 200px">
            <p><strong>Totale: </strong> <span class="prezzo">Calcola il totale qui</span></p>
            <input type="hidden" name="controllerAction" value="AziendaManagement.pagamento" />
            <input type="hidden" name="partitaIVA" value="<%=  spazio.getPartitaIVA().getPartitaIVA()%> ">
            <input type="hidden" name="idEvento" value="<%= spazio.getIdEvento().getIdEvento()%>">
            <input type="hidden" name="costo" value="<%= spazio.getCosto() %>">
            <input type="submit" value="Paga ora" class="bottone-personalizzato">
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
</script>
<footer>
    &copy; 2024 EventPrime - Italia IT | Cookie e Privacy Policy<br>
    Credits: Leonardo Rastelli e Anna Ferri
</footer>

</body>
</html>
