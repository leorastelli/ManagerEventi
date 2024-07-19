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
    Luogo luogo = (Luogo) request.getAttribute("luogo");
    String bigliettiString = String.join(",", biglietti);


    String numParterreStr = (String) request.getAttribute("numParterre");
    String numParterreVIPStr = (String) request.getAttribute("numParterreVIP");
    String numTribunaFrontStr = (String) request.getAttribute("numTribunaFront");
    String numTribunaSxStr = (String) request.getAttribute("numTribunasx");
    String numTribunaDxStr = (String) request.getAttribute("numTribunadx");
    String numPITstr = (String) request.getAttribute("numPit");
    String numPitGoldstr = (String) request.getAttribute("numPitGold");

    int numParterre = (numParterreStr != null) ? Integer.parseInt(numParterreStr) : 0;
    int numParterreVIP = (numParterreVIPStr != null) ? Integer.parseInt(numParterreVIPStr) : 0;
    int numTribunaFront = (numTribunaFrontStr != null) ? Integer.parseInt(numTribunaFrontStr) : 0;
    int numTribunaSx = (numTribunaSxStr != null) ? Integer.parseInt(numTribunaSxStr) : 0;
    int numTribunaDx = (numTribunaDxStr != null) ? Integer.parseInt(numTribunaDxStr) : 0;
    int numPIT = (numPITstr != null) ? Integer.parseInt(numPITstr) : 0;
    int numPitGold = (numPitGoldstr != null) ? Integer.parseInt(numPitGoldstr) : 0;

    int totParterre = numParterre * 50;
    int totParterreVIP = numParterreVIP * 100;
    int totTribunaFront = numTribunaFront * 70;
    int totTribunaSx = numTribunaSx * 90;
    int totTribunaDx = numTribunaDx * 90;

    int totPIT = numPIT * 50;
    int totPitGold = numPitGold * 100;

    int totale = 0;

    if ("Indoor".equals(luogo.getTipologia())) {
        totale = totParterre + totParterreVIP + totTribunaFront + totTribunaSx + totTribunaDx;
    }else if("Outdoor".equals(luogo.getTipologia())){
        totale = totPIT + totPitGold;
    }

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
            background-color:#ffb805;
        }

        main{
            margin: 20px;
            height: 700px;
            width: auto;
            text-align: center;
            padding: 20px;
            border: 1px solid #de32ff;
            background-color: #fffdf3;
            box-shadow: #de32ff 0 0 5px;
            border-radius: 10px;
            align-content: center;

        }


        .layout-esterno {
            margin-top: 20px;
            margin-bottom: 12px;
            height: calc(100% - 20px);
            width: 100%;
            display: flex;
            justify-content: center; /* Centra i contenuti orizzontalmente */
            align-items: start; /* Allinea i contenuti in alto */
            gap: 15px; /* Distanza tra la piantina e le tariffe */
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
            margin-right: 30%;
            margin-top: 0;
            margin-bottom: 1px;
            /*background-color: #fffdf3;
            border: 1px solid #ccc;
            box-shadow: dimgray 0 0 5px;*/
        }

        .resoconto {
            margin-top: 5%;
            width: 900px;
            align-content: center;
            text-align: left;
            background-color: #fffdf3;
            border-radius: 8px;
            box-shadow: dimgray 0 0 5px;
            padding-left: 20px;
            padding-right: 20px;
        }

        .tariffe {
            margin-top: 10%;
            width: 900px;
            align-content: center;
            text-align: left;
            background-color: #fffdf3;
            border-radius: 8px;
            box-shadow: dimgray 0 0 5px;
            padding-left: 20px;
            padding-right: 10px;
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


        .container1{
            display: grid;
            grid-template-columns: 1fr 1fr 1fr; /* 3 colonne */
            grid-template-rows: auto auto auto auto; /* 4 righe */
            gap: 20px; /* Spazio tra gli elementi */
            max-width: 400px; /* Larghezza massima */
            height: 200px; /* Altezza fissa */
            transform: scale(0.5); /* Scala il contenitore a un quarto della dimensione originale */
            transform-origin: center; /* Punto di origine per la trasformazione */
            position: relative; /* Per posizionare gli elementi interni */
            margin-right: 10%;
            margin-top: 0;
            margin-bottom: 1px;
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

        .centrato {
            text-align: center;
            margin: auto;
            width: 100%;
        }

        #palco1 {
            grid-column: 2 ; /* Seconda colonna */
            grid-row: 1; /* Prima riga */
            border: 1px solid #ccc;
            box-shadow: dimgray 0 0 5px;
            background-color: #fffdf3;
            border-radius: 10px;
            width: 200px;
            height: 200px;
            align-content: center;
        }

        #pit {
            grid-column: 1 / -1; /* Seconda colonna */
            grid-row: 3; /* Seconda riga */
            border: 1px solid #ccc;
            box-shadow: dimgray 0 0 5px;
            background-color: #fffdf3;
            border-radius: 10px;
            width: 600px;
            height: 250px;

        }

        #pitgold {
            grid-column: 1 / -1; /* Seconda colonna */
            grid-row: 2; /* Seconda riga */
            border: 1px solid #ccc;
            box-shadow: dimgray 0 0 5px;
            background-color: #fffdf3;
            border-radius: 10px;
            width: 600px;
            height: 150px;
        }

    </style>
</head>

<body>
<header>
</header>

<main>
    <h1 class="centrato">Overview biglietti acquistati</h1>
    <section class="layout-esterno">
        <% if ("Indoor".equals(luogo.getTipologia())) {%>
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

        <%}else if("Outdoor".equals(luogo.getTipologia())){%>
        <article class="tariffe">
            <h3>Tariffe:</h3>
            <h4>Pit 50 &euro;</h4>
            <h4>Pit GOLD 100 &euro;</h4>
        </article>

        <section class="container1">
            <table id="palco1">
                <tr>
                    <td style="text-align: center; font-size: 40px">PALCO</td>
                </tr>
            </table>
            <table id="pit" onclick="aggiornaNumeroPosti()">
                <tr>
                    <td style="text-align: center; font-size: 40px">PIT</td>
                </tr>
            </table>
            <table id="pitgold" onclick="aggiornaNumeroPosti()">
                <tr>
                    <td style="text-align: center; font-size: 40px">PIT GOLD</td>
                </tr>
            </table>
        </section>

        <%}%>

        <section class="resoconto">

            <% if("Indoor".equals(luogo.getTipologia())){ %>
            <h4>Biglietti venduti in parterre:</h4>
            <p > <%=numParterre%></p>
            <h4>Biglietti venduti in parterre VIP:</h4>
            <p ><%= numParterreVIP%></p>

            <h4>Biglietti venduti in tribuna frontale:</h4>
            <p ><%= numTribunaFront%></p>

            <h4>Biglietti venduti in tribuna laterale sinistra:</h4>
            <p ><%= numTribunaSx%></p>

            <h4>Biglietti venduti in tribuna laterale destra:</h4>
            <p ><%= numTribunaDx%></p>

            <h4>Totale incasso:</h4>
            <p ><%= totale%> &euro;</p>

            <%}else if("Outdoor".equals(luogo.getTipologia())){%>
            <h4>Biglietti venduti in Pit:</h4>
            <p > <%=numPIT%></p>
            <h4>Biglietti venduti in Pit GOLD:</h4>
            <p ><%= numPitGold%></p>
            <h4>Totale incasso:</h4>
            <p ><%= totale%> &euro;</p>

            <%}%>
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
</footer>
</body>
</html>
