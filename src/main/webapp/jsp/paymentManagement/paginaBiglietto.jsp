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
    Esibizione esibizione = (Esibizione) request.getAttribute("esibizione");
    Evento evento = (Evento) request.getAttribute("evento");
    request.setAttribute("esibizione", esibizione);
%>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pagina Biglietto - PrimEvent</title>
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

        main{
            margin: 20px;
        }
        .content {
            width: 100%;
            margin-left: auto;

        }

        .container {
            display: grid;
            grid-template-columns: 1fr 1fr 1fr; /* 3 colonne */
            grid-template-rows: auto auto auto auto; /* 4 righe */
            gap: 20px; /* Spazio tra gli elementi */
            max-width: 600px; /* Larghezza massima */
            height: 400px; /* Altezza fissa */
            transform: scale(0.25); /* Scala il contenitore a un quarto della dimensione originale */
            transform-origin: center; /* Punto di origine per la trasformazione */
            position: relative; /* Per posizionare gli elementi interni */
            margin-left: 22%;
            margin-right: 30%;
            /*background-color: #fffdf3;
            border: 1px solid #ccc;
            box-shadow: dimgray 0 0 5px;*/
        }

        #parterreVIP {
            grid-column: 2; /* Seconda colonna */
            grid-row: 2; /* Seconda riga */
            border: 1px solid black;
            background-color: white;
            width: 450px;
            height: 150px;
        }

        #tribuna-sx {
            grid-column: 1; /* Prima colonna */
            grid-row: 2 / 4; /* Dalla seconda alla terza riga */
            transform: rotate(-90deg);

        }

        #parterre {
            grid-column: 2; /* Seconda colonna */
            grid-row: 3; /* Seconda riga */
            border: 1px solid black;
            background-color: white;
            width: 450px;
            height: 250px;

        }

        #tribuna-dx {
            grid-column: 3; /* Terza colonna */
            grid-row: 2 / 4; /* Dalla seconda alla terza riga */
            transform: rotate(90deg);

        }

        #tribuna {
            grid-column: 1 / 4; /* Tutte e tre le colonne */
            grid-row: 4; /* Quarta riga */
        }

        #palco {
            grid-column: 2 / 3; /* Seconda colonna */
            grid-row: 1; /* Prima riga */
            border: 1px solid black;
            background-color: white;
            width: 450px;
            height: 200px;
            align-content: center;
        }


        button {
            width: 40px;
            height: 40px;
            border-radius: 5px;
            border: 1px solid black;
            background-color: white;
            color: black;
        }

        button.occupied {
            background-color: red;
            color: white;
        }

        button.selected{
            background-color: green;
            color: white;
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

        footer {
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
    <h1 class="centrato">Selezione il biglietto che preferisci!</h1>
    <form class="content" name="gotoForm" method="post" action="Dispatcher"> <br>
        <div class="container">
            <table id="palco">
                <tr>
                    <td style="text-align: center; font-size: 40px">PALCO</td>
                </tr>
            </table>
            <table id="parterre" onclick="aggiornaNumeroPosti()">
                <tr>
                    <td style="text-align: center; font-size: 40px">PARTERRE</td>
                </tr>
            </table>
            <table id="parterreVIP" onclick="aggiornaNumeroPosti()">
                <tr>
                    <td style="text-align: center; font-size: 40px">PARTERRE VIP</td>
                </tr>
            </table>
            <table id="tribuna"></table>

            <table id="tribuna-sx"></table>

            <table id="tribuna-dx"></table>
        </div>
        <h3>Seleziona la categoria di Parterre che preferisci e il numero di biglietti che desideri acquistare:</h3>
        <label for="numeroPosti">Numero di posti:</label>
        <button id="decrementa" style="border: none; font-size: 20px; font-weight: bolder">-</button>
        <input type="number" id="numeroPosti" style="font-size: 20px; border: none" min="0" max="6" readonly value="0">
        <button id="incrementa" style="border: none; font-size: 20px; font-weight: bolder">+</button>
        <br>
        <label for="categoria">Categoria:</label>
        <select id="categoria" name="categoria" onchange="aggiornaNumeroPosti()">
            <option value="parterre">Parterre</option>
            <option value="parterreVIP">Parterre VIP</option>
        </select>
        <br>
        <input type="hidden" name="controllerAction" value="BigliettiManagement.gotoPagamento">
        <input type="hidden" id="allSelectedSeats" name="allSelectedSeats">

        <input type="submit" class="bottone-personalizzato" value="Procedi con l'acquisto">

            <% } else { %>
        <p>Effettua il login per vedere i tuoi dati.</p>
            <% } %>
</main>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        var occupiedSeats = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
        var seatsPerRow = 9; // Numero di posti per fila nelle sezioni tribuna-sx, parterre, tribuna-dx
        var rows = 6; // Numero di file per ogni sezione

        var selectedSeats = []; // Array per memorizzare i posti selezionati

        var numeroPostiInput = document.getElementById('numeroPosti');
        document.getElementById('incrementa').addEventListener('click', function() {
            if (numeroPostiInput.value < numeroPostiInput.max) {
                numeroPostiInput.value = parseInt(numeroPostiInput.value) + 1;
                aggiornaNumeroPosti();
            }
        });

        document.getElementById('decrementa').addEventListener('click', function() {
            if (numeroPostiInput.value > numeroPostiInput.min) {
                numeroPostiInput.value = parseInt(numeroPostiInput.value) - 1;
                aggiornaNumeroPosti();
            }
        });

        function updateSelectedSeats(seatId) {
            if (!selectedSeats.includes(seatId)) {
                selectedSeats.push(seatId); // Aggiungi il posto all'array dei selezionati
                // Crea un input hidden e aggiungilo al form
                var input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'selectedSeats[]'; // Usa un array per il nome per facilitare la gestione lato server
                input.value = seatId;
                document.forms['gotoForm'].appendChild(input); // Assumi che il form si chiami 'gotoForm'
            }

            // Aggiorna un input hidden con tutti i posti selezionati, separati da virgola
            var allSelectedSeatsInput = document.getElementById('allSelectedSeats');
            if (!allSelectedSeatsInput) {
                allSelectedSeatsInput = document.createElement('input');
                allSelectedSeatsInput.type = 'hidden';
                allSelectedSeatsInput.id = 'allSelectedSeats';
                allSelectedSeatsInput.name = 'allSelectedSeats';
                document.forms['gotoForm'].appendChild(allSelectedSeatsInput);
            }
            allSelectedSeatsInput.value = selectedSeats.join(',');
        }

        var tribunaTable = document.getElementById('tribuna');
        var tribunaDxTable = document.getElementById('tribuna-dx');
        var tribunaSxTable = document.getElementById('tribuna-sx');

        // Modifica la funzione per accettare un parametro aggiuntivo: seatsPerRowForSection
        function populateSection(table, sectionId, seatsPerRowForSection) {
            for (var i = 0; i < rows; i++) {
                var row = document.createElement('tr');
                table.appendChild(row);
                for (var j = 0; j < seatsPerRowForSection; j++) {
                    var cell = document.createElement('td'); // Crea una cella
                    var button = document.createElement('button'); // Crea un bottone
                    button.id = (i * seatsPerRowForSection) + j + 1; // Imposta l'id del bottone
                    button.addEventListener('click', function() { // Aggiungi un listener per il click
                        if (this.classList.contains('occupied')) { // Se il posto è già occupato
                            alert('Seat already occupied'); // Mostra un alert
                        } else { // Altrimenti
                            this.classList.add('selected');
                        }
                        updateSelectedSeats(this.id); // Aggiorna i posti selezionati
                    });

                    cell.appendChild(button);
                    row.appendChild(cell);

                    if (occupiedSeats.includes((i * seatsPerRowForSection) + j + 1)) { // Se il posto è già occupato
                        button.classList.add('occupied'); // Aggiungi la classe occupied
                    }


                }
            }
        }

        // Calcola il numero totale di posti per fila nella tribuna come il triplo delle altre sezioni
        var seatsPerRowInTribuna = seatsPerRow * 2.5;

        // Popola ogni sezione passando il numero corretto di posti per fila
        populateSection(tribunaTable, 'tribuna', seatsPerRowInTribuna); // Usa il valore calcolato per tribuna
        populateSection(tribunaDxTable, 'tribuna-dx', seatsPerRow); // Usa il valore originale per tribuna-dx
        populateSection(tribunaSxTable, 'tribuna-sx', seatsPerRow); // Usa il valore originale per tribuna-sx



    });

</script>
<footer>
    &copy; 2024 EventPrime - Italia IT | Cookie e Privacy Policy<br>
    Credits: Leonardo Rastelli e Anna Ferri
</footer>
</body>
</html>
