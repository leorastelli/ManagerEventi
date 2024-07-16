<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Seating Layout</title>
    <style>
        table {
            width: 30%;
            margin: auto;
        }

        td {
            text-align: center;
        }

        button {
            width: 10px;
            height: 10px;
            border-radius: 5px;
            border: 1px solid black;
            background-color: white;
            color: black;
        }

        button.occupied {
            background-color: red;
            color: white;
        }

        #buttonTable {
            border-collapse: collapse;
        }


        ody {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            background-color: #f0f0f0;
        }

        .container {
            display: flex;
            align-items: flex-start;
        }

        #parterre {
            width: 600px;
            height: 400px;
            border: 1px solid #333;
            position: relative;
            cursor: pointer;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 20px;
        }

        .menu {
            margin-left: 20px;
        }

        /* Stile per i posti selezionati */
        .posto {
            width: 20px;
            height: 20px;
            background-color: #55f;
            position: absolute;
            border-radius: 50%;
            pointer-events: none; /* Evita che i posti interferiscano con il click */
        }

    </style>
</head>
<body>

<table id="parterre" onclick="incrementaPosti()">
    Parterre
</table>
<table class="menu">
    <label for="numeroPosti">Numero di posti:</label>
    <input type="number" id="numeroPosti" min="0" max="100" value="0" onchange="aggiornaNumeroPosti()">
</table>

<table id="buttonTable">
    <!-- La tabella sarà popolata dinamicamente da JavaScript -->
</table>
<script>
    document.addEventListener('DOMContentLoaded', function() {

        //lista posti già occupati
        var occupiedSeats = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

        //numero di posti per fila
        var seatsPerRow = 10;

        //numero di file
        var rows = 10;

        //creo la tabella
        var table = document.getElementById('buttonTable');

        //creo le righe
        for (var i = 0; i < rows; i++) {
            var row = document.createElement('tr');
            table.appendChild(row);

            //creo le celle
            for (var j = 0; j < seatsPerRow; j++) {
                var cell = document.createElement('td');
                row.appendChild(cell);

                //creo il bottone
                var button = document.createElement('button');
                button.id = (i * seatsPerRow) + j + 1;
                button.addEventListener('click', function() {
                    if (this.classList.contains('occupied')) {
                        alert('Seat already occupied');
                    } else {
                        this.classList.add('occupied');
                    }
                });
                cell.appendChild(button);

                //controllo se il posto è già occupato
                if (occupiedSeats.includes((i * seatsPerRow) + j + 1)) {
                    button.classList.add('occupied');
                }
            }
        }


    });

    let numeroPostiSelezionati = 0;

    function incrementaPosti() {
        numeroPostiSelezionati++;
        aggiornaVisualizzazionePosti();
    }

    function aggiornaNumeroPosti() {
        numeroPostiSelezionati = parseInt(document.getElementById('numeroPosti').value, 10);
        aggiornaVisualizzazionePosti();
    }

    function aggiornaVisualizzazionePosti() {
        var parterre = document.getElementById('parterre');

        // Rimuovi tutti i posti già selezionati
        var postiSelezionati = document.querySelectorAll('.posto');
        postiSelezionati.forEach(function(posto) {
            parterre.removeChild(posto);
        });

        // Genera i posti selezionati
        for (var i = 0; i < numeroPostiSelezionati; i++) {
            var posto = document.createElement('div');
            posto.className = 'posto';

            // Posizione casuale nel parterre
            var posX = Math.random() * (parterre.clientWidth - 20);
            var posY = Math.random() * (parterre.clientHeight - 20);

            posto.style.left = posX + 'px';
            posto.style.top = posY + 'px';

            // Aggiungi il posto al parterre
            parterre.appendChild(posto);
        }
    }

</script>

</body>
</html>
