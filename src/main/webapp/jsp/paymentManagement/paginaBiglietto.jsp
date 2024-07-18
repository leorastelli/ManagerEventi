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
    List<String> biglietti = (List<String>) request.getAttribute("biglietti");
    Esibizione esibizione = (Esibizione) request.getAttribute("esibizione");
    Evento evento = (Evento) request.getAttribute("evento");

    String bigliettiString = String.join(",", biglietti);
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

        .layout-esterno {
            margin-top: 20px;
            display: flex;
            justify-content: center; /* Centra i contenuti orizzontalmente */
            align-items: start; /* Allinea i contenuti in alto */
            gap: 10px; /* Distanza tra la piantina e le tariffe */
        }

        .container {
            display: grid;
            grid-template-columns: 1fr 1fr 1fr; /* 3 colonne */
            grid-template-rows: auto auto auto auto; /* 4 righe */
            gap: 20px; /* Spazio tra gli elementi */
            max-width: 400px; /* Larghezza massima */
            height: 200px; /* Altezza fissa */
            transform: scale(0.5); /* Scala il contenitore a un quarto della dimensione originale */
            transform-origin: center; /* Punto di origine per la trasformazione */
            position: relative; /* Per posizionare gli elementi interni */
            margin-right: 52%;
            margin-top: 0;
            margin-bottom: 1px;
            /*background-color: #fffdf3;
            border: 1px solid #ccc;
            box-shadow: dimgray 0 0 5px;*/
        }

        .tariffe {
            margin-top: 10%;
            width: 500px;
            align-content: center;
            text-align: left;
            background-color: #fffdf3;
            border-radius: 8px;
            box-shadow: dimgray 0 0 5px;
            padding-left: 20px;
        }

        #parterreVIP {
            grid-column: 2; /* Seconda colonna */
            grid-row: 2; /* Seconda riga */
            border: 1px solid #ccc;
            box-shadow: dimgray 0 0 5px;
            background-color: #fffdf3;
            border-radius: 10px;
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
            border: 1px solid #ccc;
            box-shadow: dimgray 0 0 5px;
            background-color: #fffdf3;
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
            border: 1px solid #ccc;
            box-shadow: dimgray 0 0 5px;
            background-color: #fffdf3;
            border-radius: 10px;
            width: 450px;
            height: 200px;
            align-content: center;
        }


        button {
            width: 40px;
            height: 40px;
            border: 1px solid #ccc;
            box-shadow: dimgray 0 0 5px;
            background-color: #fffdf3;
            border-radius: 5px;
        }

        button.occupied {
            background-color: darkred;
            color: white;
        }

        button.selected{
            background-color: green;
            color: white;
        }

        select{
            width: 98%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            text-align: left;
        }
        input[type=number] {
            -moz-appearance: textfield;
            outline: none; /* Rimuove il bordo azzurro */
        }
        input::-webkit-outer-spin-button,
        input::-webkit-inner-spin-button {
            -webkit-appearance: none;
            margin: 0;
        }
        input:focus {
            outline: none; /* Rimuove il bordo azzurro quando l'input è in focus */
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

        .bottone-scelta {
            width: 40px;
            height: 40px;
            border-radius: 5px;
            border: 1px solid black;
            background-color: #ffb805;
            color: black;
            margin: 0 5px;
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
    <h1 class="centrato">Seleziona il biglietto che preferisci!</h1>
    <br><br>
    <p class="centrato">Seleziona i posti numerati che desideri acquistare direttamente dalla piantina e i posti in parterre dal men&ugrave; sottostante</p>
    <form class="content" name="gotoForm" method="post" action="Dispatcher">
        <section class="layout-esterno">
            <article class="tariffe">
                <h3>Tariffe:</h3>
                <h4>Parterre 50 &euro;</h4>
                <h4>Parterre VIP 100 &euro;</h4>
                <h4>Tribuna frontale 70 &euro;</h4>
                <h4>Tribuna laterale destra 90 &euro;</h4>
                <h4>Tribuna laterale sinistra 90 &euro;</h4>
            </article>
            <section class="container">
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
            </section>

        </section>
        <br>
        <h3 style="margin-top: 10%; margin-left: 26%">Seleziona la categoria di posti che desideri acquistare:</h3>
        <select id="categoria" name="categoria" style="width: fit-content; margin-left: 26%" onchange="aggiornaNumeroPosti()">
            <option value="1">Parterre</option>
            <option value="2">Parterre VIP</option>
        </select>
        <br>
        <h3 style="margin-left: 26%">Seleziona il numero di posti che desideri acquistare:</h3>
        <label style="margin-left: 26%" for="numeroPosti">Numero di posti </label>
        <button class="bottone-scelta" id="decrementa" style="border: none; font-size: 20px; font-weight: bolder">-</button>
        <input type="number" id="numeroPosti" name="numPosti" style="margin-bottom:30px; font-size: 20px; border: none" min="0" max="6" value="0" readonly>
        <button class="bottone-scelta" id="incrementa" style="border: none; font-size: 20px; font-weight: bolder">+</button>
        <input type="hidden" name="controllerAction" value="PagamentoManagement.gotoPagamentoBiglietto">
        <input type="hidden" id="allSelectedSeats1" name="allSelectedSeats1">
        <input type="hidden" name="idEsibizione" value="<%=esibizione.getIdEsibizione()%>">
        <input type="hidden" name="idEvento" value="<%=evento.getIdEvento()%>">
        <input type="submit" style="margin-top: 20px" class="bottone-personalizzato" value="Procedi con l'acquisto">
    </form>

            <% } else { %>
        <p>Effettua il login per vedere i tuoi dati.</p>
            <% } %>
</main>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        var occupiedSeats = [<%=bigliettiString%>];
        var seatsPerRow = 9; // Numero di posti per fila nelle sezioni tribuna-sx, parterre, tribuna-dx
        var rows = 6; // Numero di file per ogni sezione

        var selectedSeats = []; // Array per memorizzare i posti selezionati
        const selectPosti = document.getElementById('numeroPosti');

        var numeroPostiInput = document.getElementById('numeroPosti');
        document.getElementById('incrementa').addEventListener('click', function(event) {
            event.preventDefault();
            if (numeroPostiInput.value < numeroPostiInput.max) {
                numeroPostiInput.value = parseInt(numeroPostiInput.value) + 1;
                aggiornaNumeroPosti();
            }
        });

        document.getElementById('decrementa').addEventListener('click', function(event) {
            event.preventDefault();
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
                input.id =seatId; // Assegna un ID univoco all'input nascosto
                document.forms['gotoForm'].appendChild(input); // Assumi che il form si chiami 'gotoForm'
            } else {
                // Trova l'indice del posto nell'array
                var index = selectedSeats.indexOf(seatId);
                if (index > -1) {
                    selectedSeats.splice(index, 1); // Rimuovi il posto dall'array
                }

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
            var startId;
            switch (sectionId) {
                case 'tribuna-sx':
                    startId = 1;
                    break;
                case 'tribuna-dx':
                    startId = 101;
                    break;
                case 'tribuna':
                    startId = 201;
                    break;
                default:
                    startId = 1; // Default case, se non corrisponde a nessuna delle sezioni specificate
            }

            for (var i = 0; i < rows; i++) {
                var row = document.createElement('tr');
                table.appendChild(row);
                for (var j = 0; j < seatsPerRowForSection; j++) {
                    var cell = document.createElement('td');
                    var button = document.createElement('button');
                    var seatId = startId++; // Incrementa startId per ogni posto creato
                    button.id = seatId; // Assegna l'ID incrementato al bottone
                    //button.textContent = seatId; // Opzionale: mostra l'ID sul bottone per facilitare il riconoscimento
                    button.addEventListener('click', function(event) {
                        event.preventDefault();
                        if (this.classList.contains('occupied')) {
                            alert('Seat already occupied');
                        } else {
                            this.classList.toggle('selected');
                            updateSelectedSeats(this.id);
                        }
                    });

                    cell.appendChild(button);
                    row.appendChild(cell);

                    if (occupiedSeats.includes(seatId)) {
                        button.classList.add('occupied');
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

        document.querySelector('form[name="gotoForm"]').addEventListener('submit', function(event) {
            var selectPostiValue = document.getElementById('numeroPosti').value;
            // Controlla se selectedSeats è vuoto (contiene solo una stringa vuota) o selectPostiValue è 0
            if (selectPostiValue === '0' && selectedSeats.length === 0) {
                event.preventDefault(); // Ferma il submit del form
                alert('Devi selezionare almeno un posto o indicare il numero di posti desiderati.');
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