<%@page session="false"%>
<%@page import="java.util.List"%>
<%@ page import="com.managereventi.managereventi.model.mo.*" %>
<%@ page import="java.sql.Blob" %>
<%@ page import="java.util.Base64" %>
<%@ page import="java.text.SimpleDateFormat" %>

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

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String dataInizio = sdf.format(evento.getDataInizio());
    String dataFine = sdf.format(evento.getDataFine());

    int prezzo = 0;
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

            clear: both;
            text-align: center;
            padding: 10px;
            background-color: #ffb805;
            color: #ab00cc;
            margin-top: 20px;
        }

    </style>
    <script>
        function generaNumeroGiorni(dataInizio, dataFine) {
            let dataInizioDate = new Date(dataInizio);
            let dataFineDate = new Date(dataFine);

            if (dataInizioDate > dataFineDate) {
                console.error("La data di inizio deve essere precedente o uguale alla data di fine.");
                return [];
            }

            let giorniTotali = Math.floor((dataFineDate - dataInizioDate) / (1000 * 60 * 60 * 24));
            let listaGiorni = [];
            for (let i = 1; i <= giorniTotali; i++) {
                listaGiorni.push(i);
            }

            return listaGiorni;
        }


        window.onload = function() {
            let dataInizio = '<%= dataInizio %>';
            let dataFine = '<%= dataFine %>';
            let lista = generaNumeroGiorni(dataInizio, dataFine);

            let tendinaGiornate = document.getElementById('evento');
            lista.forEach(function(giorno) {
                let option = document.createElement('option');
                option.value = giorno;
                option.textContent = giorno;
                tendinaGiornate.appendChild(option);
            });

            let radioIntero = document.getElementById('radioIntero');
            let radioRidotto = document.getElementById('radioRidotto');
            let tendina = document.getElementById('evento');
            let prezzoTotaleLabel = document.getElementById('prezzoTotale');
            let prezzoHiddenInput = document.getElementById('prezzo');

            radioIntero.addEventListener('change', function() {
                if (this.checked) {
                    let giorniTotali = generaNumeroGiorni(dataInizio, dataFine).length + 1;
                    prezzo = giorniTotali * 60 - 20;
                    prezzoTotaleLabel.textContent = prezzo + "\u20AC" ;
                    prezzoHiddenInput.value = prezzo;
                    tendina.style.display = 'none';
                }
            });

            radioRidotto.addEventListener('change', function() {
                if (this.checked) {
                    tendina.style.display = 'block';
                }
            });

            tendina.addEventListener('change', function() {
                let giorniSelezionati = parseInt(this.value);
                prezzo = giorniSelezionati * 60;
                prezzoHiddenInput.value = prezzo;
                prezzoTotaleLabel.textContent = prezzo + "\u20AC";
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
            <li <%=menuActiveLink.equals("Home Utente") ? "class=\"active\"" : ""%>>
                <a href="Dispatcher?controllerAction=UserManagement.view">Home Utente</a>
            </li>
            <li><a href="javascript:logoutForm.submit()">Logout</a></li>
            <% } else { %>
            <li <%= menuActiveLink.equals("Accedi") ? "class=\"active\"": ""%>>
                <a href="Dispatcher?controllerAction=HomeManagement.gotoLogin">Accedi</a></li>
            <li <%=menuActiveLink.equals("Registrati")?"class=\"active\"":""%>>
                <a href="Dispatcher?controllerAction=UserManagement.gotoRegistration">Registrati</a>

            </li>
            <%}%>
        </ul>
    </nav>
</header>

<main>
    <% if (loggedOn) { %>
    <h1 class="centrato">Scegli l&apos;abbonamento giusto per te per l&apos;evento <%=evento.getNome()%>!</h1>
    <form class="content" name="gotoForm" method="post" action="Dispatcher"> <br>

        <%
            Blob logoBlob = evento.getImmagine();
            int blobLength = (int) logoBlob.length();
            byte[] logoBytes = logoBlob.getBytes(1, blobLength);
            String base64Image = Base64.getEncoder().encodeToString(logoBytes);
        %>
        <img src="data:image/jpeg;base64, <%= base64Image %>" style="max-width: 300px; max-height: 300px; align-items: center"><br>
        <h2>L&apos;acquisto di un abbonamento di permette di avere accesso diretto alla zona VIP</h2>

        <h3 style="font-weight: normal">Seleziona il tipo di abbonamento in base al numero di giornate:</h3>

        <label>
            <input type="radio" id="radioIntero" name="scelta" value="intero" style="font-weight: normal" required> Intero
        </label>
        <br>
        <label>
            <input type="radio" id="radioRidotto" name="scelta" value="ridotto" style="font-weight: normal" required> Ridotto
        </label>

        <select class="tendina" id="evento" name="numEntrate" style="display: none; width: fit-content">
            <option value="0">Numero giornate</option>
        </select>

        <br>
        <label for="prezzoTotale">Prezzo Totale: </label>
        <label id="prezzoTotale">0 &euro;</label>
        <br>
        <input type="hidden" name="controllerAction" value="PagamentoManagement.gotopagamentoAbbonamento" />
        <input type="hidden" name="idEvento" value="<%=evento.getIdEvento()%>" />
        <input type="hidden" id="prezzo" name="prezzo" value="<%=prezzo%>">
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