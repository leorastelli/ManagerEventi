<%--
  Created by IntelliJ IDEA.
  User: annaferri
  Date: 08/07/24
  Time: 15:08
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error Page - PrimEvent</title>
    <style>
        body, html {
            height: 100%;
            margin: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
        }

        main {
            text-align: center;
            padding: 20px;
            border: 1px solid #ddd;
            background-color: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        button {
            padding: 10px 20px;
            font-size: 16px;
            color: #fff;
            background-color: #48a6f5;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        button:hover {
            background-color: #3091e6;
        }
    </style>

</head>
<main>
    <h1>Si e&grave; verificato un errore nell'inserimento dei dati!</h1>
    <h3>Torna alla pagina di login e riprova</h3>
    <button onclick="goBack()">Torna indietro</button>
    <script>
        function goBack() {
            window.history.back();
        }
    </script>
</main>


