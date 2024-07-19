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
    Organizzatore loggedOrganizzatore = (Organizzatore) request.getAttribute("loggedOrganizzatore");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";
    List<String> biglietti = (List<String>) request.getAttribute("biglietti");
    Esibizione esibizione = (Esibizione) request.getAttribute("esibizione");
    Evento evento = (Evento) request.getAttribute("evento");
    String bigliettiString = String.join(",", biglietti);


    String numParterreStr = (String) request.getAttribute("numParterre");
    String numParterreVIPStr = (String) request.getAttribute("numParterreVIP");
    String numTribunaFrontStr = (String) request.getAttribute("numTribunaFront");
    String numTribunaSxStr = (String) request.getAttribute("numTribunaSx");
    String numTribunaDxStr = (String) request.getAttribute("numTribunaDx");

    int numParterre = (numParterreStr != null) ? Integer.parseInt(numParterreStr) : 0;
    int numParterreVIP = (numParterreVIPStr != null) ? Integer.parseInt(numParterreVIPStr) : 0;
    int numTribunaFront = (numTribunaFrontStr != null) ? Integer.parseInt(numTribunaFrontStr) : 0;
    int numTribunaSx = (numTribunaSxStr != null) ? Integer.parseInt(numTribunaSxStr) : 0;
    int numTribunaDx = (numTribunaDxStr != null) ? Integer.parseInt(numTribunaDxStr) : 0;

    int totParterre = numParterre * 50;
    int totParterreVIP = numParterreVIP * 100;
    int totTribunaFront = numTribunaFront * 70;
    int totTribunaSx = numTribunaSx * 90;
    int totTribunaDx = numTribunaDx * 90;

    int totale = totParterre + totParterreVIP + totTribunaFront + totTribunaSx + totTribunaDx;
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
</header>

<main>
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

            <section>
                <h3>Biglietti venduti a Parterre:</h3>
                <p > <%=numParterre%></p>
                <h3>Biglietti venduti a Parterre VIP</h3>
                <p ><%= numParterreVIP%></p>

                <h3>Biglietti venduti a Tribuna Frontale</h3>
                <p ><%= numTribunaFront%></p>

                <h3>Biglietti venduti a Tribuna Laterale Sinistra</h3>
                <p ><%= numTribunaSx%></p>

                <h3>Biglietti venduti a Tribuna Laterale Destra</h3>
                <p ><%= numTribunaDx%></p>

                <h3>Totale incasso:</h3>
                <p ><%= totale%> &euro;</p>


            </section>

        </section>
        <br>

</main>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        var occupiedSeats = [<%=bigliettiString%>];
        var seatsPerRow = 9; // Numero di posti per fila nelle sezioni tribuna-sx, parterre, tribuna-dx
        var rows = 6; // Numero di file per ogni sezione

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

    });



</script>
<footer>
    &copy; 2024 EventPrime - Italia IT | Cookie e Privacy Policy<br>
    Credits: Leonardo Rastelli e Anna Ferri
</footer>
</body>
</html>