<%@page session="false"%>
<%@page import="java.util.List"%>
<%@ page import="com.managereventi.managereventi.model.mo.*" %>

<%
    Boolean loggedOnObj = (Boolean) request.getAttribute("loggedOn");
    boolean loggedOn = (loggedOnObj != null) ? loggedOnObj : false;

    int i;
    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";
    List<Candidature> candidature = (List<Candidature>) request.getAttribute("candidature");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lavora con Noi!</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
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
            max-width: 800px;
            margin: 20px auto;
            padding: 20px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            flex-grow: 1;
        }

        label {
            display: block;
            margin: 10px 0 5px;
            font-weight: bold;
        }

        input, select, textarea {
            width: 98%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            text-align: left;
        }

        textarea {
            resize: vertical;
        }
        .bottone-personalizzato {
            background-color: #6fa3ef;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            width: 20%;
            align-items: center;
            text-align: center;
            display: block;
            margin: auto;
        }
        .bottone-personalizzato:hover {
            background-color: #007FFF; /* Colore di sfondo al passaggio del mouse */
        }

        .centrato {
            text-align: center; /* Centra il testo orizzontalmente */
            margin: auto; /* Utile se vuoi centrare un blocco (es. div) orizzontalmente */
            width: 100%; /* Assicura che l'elemento occupi tutta la larghezza */
            /* Per centrare verticalmente potresti dover lavorare con altezza e display flex sul contenitore genitore */
        }

        .testo-centrato {
            text-align: center;
        }


        footer {
            position: fixed;
            bottom: 0;
            width: 100%;
            text-align: center;
            padding: 10px;
            background-color: #6fa3ef;
            color: #fff;
            margin-top: 20px;
        }

    </style>
    <script>
        function toggleEdit(sectionId) {
            var section = document.getElementById(sectionId);
            var inputs = section.querySelectorAll('input');
            var checkboxes = section.querySelectorAll('input[type="checkbox"]');
            for (var i = 0; i < inputs.length; i++) {
                if (inputs[i].type !== 'checkbox') {
                    inputs[i].disabled = !inputs[i].disabled;
                }
            }
            for (var i = 0; i < checkboxes.length; i++) {
                checkboxes[i].style.display = checkboxes[i].style.display === 'none' ? 'block' : 'none';
            }
        }

        document.getElementById('applicationForm').addEventListener('submit', function(event) {
            event.preventDefault();
            alert('Candidatura inviata con successo!');
        });

    </script>
</head>

<body>
<header>
    <h1>PrimEvent</h1>
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
            <li <%= menuActiveLink.equals("Accedi") ? "class=\"acrive\"": ""%>>
                <a href="Dispatcher?controllerAction=HomeManagement.gotoLogin">Accedi</a></li>
            <li <%=menuActiveLink.equals("Registrati")?"class=\"active\"":""%>>
                <a href="Dispatcher?controllerAction=UserManagement.gotoRegistration">Registrati</a>
                    <%}%>
        </ul>
    </nav>
</header>
<main>
        <h1 class="centrato"> Lavora con Noi! </h1>
        <p class="testo-centrato">Se desideri entrare a far parte del nostro Team, compila il modulo sottostante inserendo la posizione che preferisci ed inviaci la tua candidatura!</p>
        <form id="applicationForm" method="post" action="Dispatcher">
            <label for="position">Posizione Lavorativa</label>
            <select id="position" name="position">
                <option value="">Tutte le posizioni</option>
                <option value="marketing">Marketing</option>
                <option value="socialmedia">Social Media</option>
                <option value="eventmanager">Event Manager</option>
                <option value="photographer">Photographer</option>
                <option value="videomaker">Videomaker</option>
                <option value="graphicdesigner">Graphic Designer</option>
                <option value="uxuidesigner">UX/UI Designer</option>
                <option value="sales">Sales</option>
                <option value="content manager">Content Manager</option>
                <option value="developer">Developer</option>
                <option value="designer">Designer</option>
                <option value="manager">Manager</option>
            </select>

            <label for="nome">Nome: </label>
            <input type="text" id="nome" name="name" required> <br>

            <label for="cognome">Cognome: </label>
            <input type="text" id="cognome" name="surname" required> <br>

            <label for="email">Email: </label>
            <input type="email" id="email" name="email" required> <br>

            <label for="dataNascita">Data di Nascita: </label>
            <input type="date" id="dataNascita" name="birthdate" required> <br>

            <label for="citta">Citt&agrave; di Residenza: </label>
            <input type="text" id="citta" name="city" required> <br>

            <label for="telefono">Numero di telefono: </label>
            <input type="tel" id="telefono" name="phone" required> <br>

            <label for="descrizione">Breve presentazione, dicci chi sei e quali sono i tuoi punti di forza! </label>
            <textarea id="descrizione" name="description" rows="5" required></textarea>

            <input type="hidden" name="controllerAction" value="CandidatureManagement.addCandidatura"/>
            <input type="submit" class="bottone-personalizzato" value="Invia Candidatura">
            <!--<button type="submit">Invia candidatura</button>-->
        </form>
</main>
<footer>
    &copy; 2024 EventPrime - Italia IT | Cookie e Privacy Policy<br>
    Credits: Leonardo Rastelli e Anna Ferri
</footer>
<script>
   /* document.getElementById('applicationForm').addEventListener('submit', function(event) {
        event.preventDefault();
        alert('Candidatura inviata con successo!');
    });*/
</script>
</body>
</html>