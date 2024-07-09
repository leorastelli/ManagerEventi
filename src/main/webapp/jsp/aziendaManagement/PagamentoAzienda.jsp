<%@page session="false"%>
<%@page import="java.util.List"%>
<%@ page import="com.managereventi.managereventi.model.mo.*" %>

<%
    Boolean loggedOnObj = (Boolean) request.getAttribute("loggedOn");
    boolean loggedOn = (loggedOnObj != null) ? loggedOnObj : false;

    int i;
    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
    Azienda loggedAzienda = (Azienda) request.getAttribute("loggedAzienda");

    String applicationMessage = (String) request.getAttribute("applicationMessage");String menuActiveLink = "Home";
    List<Evento> eventi = (List<Evento>) request.getAttribute("eventi");
    List<Sponsorizzazione> sponsorizzazioni = (List<Sponsorizzazione>) request.getAttribute("sponsorizzazioni");
    //List<Sponsorizzazione> spaziAcquistati = getSpaziAziendaLoggata(loggedAzienda.getPartitaIVA());
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
    width: 60%;
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 20px;
    }

    section {
    grid-column: span 1;
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
    margin-bottom: 10px;
    box-sizing: border-box;
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

    footer {

        text-align: center;
        padding: 10px;
        background-color: #6fa3ef;
        color: #fff;
        clear: both;
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
    <form id="paymentForm">
        <section>
            <h2>Indirizzo di fatturazione</h2>
            <label for="nomeAzienda">Nome Azienda</label>
            <input type="text" id="nomeAzienda" name="nomeAzienda">
            <label for="indirizzo">Indirizzo</label>
            <input type="text" id="indirizzo" name="indirizzo">
            <label for="citta">Città</label>
            <input type="text" id="citta" name="citta">
            <label for="provincia">Provincia</label>
            <input type="text" id="provincia" name="provincia">
            <label for="cap">CAP</label>
            <input type="text" id="cap" name="cap">
            <label for="stato">Stato</label>
            <input type="text" id="stato" name="stato">
            <label for="telefono">Numero di telefono</label>
            <input type="tel" id="telefono" name="telefono">
            <label for="email">E-mail</label>
            <input type="email" id="email" name="email">
        </section>
        <section>
            <h2>Dettagli Pagamento</h2>
            <label for="nome">Nome</label>
            <input type="text" id="nome" name="nome">
            <label for="cognome">Cognome</label>
            <input type="text" id="cognome" name="cognome">
            <label for="numeroCarta">Numero Carta</label>
            <input type="text" id="numeroCarta" name="numeroCarta">
            <label for="scadenza">Data Scadenza</label>
            <input type="text" id="scadenza" name="scadenza">
            <label for="cvv">CVV</label>
            <input type="text" id="cvv" name="cvv">
        </section>
        <section>
            <h2>Riepilogo Ordine</h2>
            <p>Nome Evento </p>
            <p>Nome Azienda: <%= loggedAzienda.getNome() %></p>
                <% //for(Sponsorizzazione spazio : spaziAcquistati) { %>
            <p>Spazio <%= //spazio.getIdEvento() %> <span class="prezzo"><%= //spazio.getCosto() %> €</span></p>
                <% //} %>
            <p><strong>TOTALE</strong> <span class="prezzo">Calcola il totale qui</span></p>
        <button class="bottone-personalizzato" type="submit">Paga Ora</button>
    </form>
</main>
<footer>
    &copy; 2024 EventPrime - Italia IT | Cookie e Privacy Policy<br>
    Credits: Leonardo Rastelli e Anna Ferri
</footer>

</body>
</html>
