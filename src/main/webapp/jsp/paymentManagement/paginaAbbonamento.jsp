<%@page session="false"%>
<%@page import="java.util.List"%>
<%@ page import="com.managereventi.managereventi.model.mo.*" %>
<%@ page import="java.sql.Blob" %>
<%@ page import="java.util.Base64" %>

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
    Evento evento = (Evento) request.getAttribute("evento");
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

        .content {
            width: 100%;
            margin-left: auto;

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
            background-color: #fceb00;
            color: black;
        }

        .centrato {
            text-align: center;
            margin: auto;
            width: 100%;
        }

        .tendina {
            padding: 5px;
            border: 1px solid #ccc;
            border-radius: 5px;
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
    <script>
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
            <li <%= menuActiveLink.equals("Accedi") ? "class=\"active\"": ""%>>
                <a href="Dispatcher?controllerAction=HomeManagement.gotoLogin">Accedi</a></li>
            <li <%=menuActiveLink.equals("Registrati")?"class=\"active\"":""%>>
                <a href="Dispatcher?controllerAction=UserManagement.gotoRegistration">Registrati</a>
                    <%}%>
        </ul>
    </nav>
</header>

<main>
    <% if (loggedOn) { %>
    <h1 class="centrato">Scegli l’abbonamento giusto per te per l’evento <%=evento.getNome()%>!</h1>
    <form class="content">

        <%
            Blob logoBlob = evento.getImmagine();
            int blobLength = (int) logoBlob.length();
            byte[] logoBytes = logoBlob.getBytes(1, blobLength);
            String base64Image = Base64.getEncoder().encodeToString(logoBytes);
        %>
        <img src="data:image/jpeg;base64, <%= base64Image %>" style="max-width: 300px; max-height: 300px; align-content: center"><br>
        <h2>L’acquisto di un abbonamento di permette di avere accesso diretto alla zona VIP</h2>

        <h3>Seleziona il tipo di abbonamento in base al numero di giornate:</h3>

        <label>
            <input type="radio" name="scelta" value="casuale" required> Intero
        </label>
        <br>
        <label>
            <input type="radio" id="checkboxEvento" name="scelta" value="scelta" required> Ridotto
        </label>
        <select class="tendina" id="evento" name="evento" style="display: none; width: fit-content">
            <option value="">Numero giornate</option>
            <% for (i=0; i < ; i++) { %>
            <option value="<%= .get(i) %>"><%= .get(i) %></option>
            <% } %>
        </select>
        <br>
        <br>
        <input type="hidden" name="controllerAction" value="PagamentoManagement.gotoPagamento" />
        <input type="submit" class="bottone-personalizzato" value="Procedi con l'acquisto">

    </form>
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