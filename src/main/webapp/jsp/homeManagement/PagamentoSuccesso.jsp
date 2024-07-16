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
            background-color: #fefefa;
        }

        main {
            text-align: center;
            padding: 20px;
            border: 1px solid #ddd;
            background-color: #fffdf3;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
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
            background-color: #ab00cc;
        }

    </style>

</head>
<main>
    <h1>Pagamento avvenuto con Successo!</h1>
    <h3>Torna alla Home di login e riprova</h3>
    <form method="post" action="Dispatcher">
        <input type="hidden" name="controllerAction" value="HomeManagement.view">
        <input type="submit" class="bottone-personalizzato" value="Torna alla Home">
    </form>


</main>


