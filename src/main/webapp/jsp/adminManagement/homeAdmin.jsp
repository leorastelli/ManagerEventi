<%@page session="false"%>
<%@page import="java.util.List"%>
<%@ page import="com.managereventi.managereventi.model.mo.*" %>
<%@ page import="java.sql.Blob" %>
<%@ page import="java.util.Base64" %>

<%
    Boolean loggedOnObj = (Boolean) request.getAttribute("loggedOn");
    boolean loggedOn = (loggedOnObj != null) ? loggedOnObj : false;

    int i;
    Organizzatore loggedOrganizzatore = (Organizzatore) request.getAttribute("loggedOrganizzatore");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home Organizzatore";
    List<Esibizione> esibizioni = (List<Esibizione>) request.getAttribute("esibizioni");
    List<Evento> eventi = (List<Evento>) request.getAttribute("eventi");
    List<Sponsorizzazione> sponsorizzazioni = (List<Sponsorizzazione>) request.getAttribute("sponsorizzazioni");
    List<Recensione> recensioni = (List<Recensione>) request.getAttribute("recensioni");
    List<Candidature> candidature = (List<Candidature>) request.getAttribute("candidature");
    List<String> abbonamentiVenduti = (List<String>) request.getAttribute("abbonamentiVenduti");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Area Organizzatore - PrimEvent</title>
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
        .sidebar {
            float: left;
            width: 20%;
        }
        .sidebar a {
            display: block;
            padding: 10px;
            margin: 10px 0;
            background-color: #de32ff;
            color: white;
            text-decoration: none;
            text-align: center;
            border-radius: 5px;
        }
        .sidebar a:hover {
            background-color: #00bfff;
        }
        .content {
            width: 100%;
            margin-left: auto;

        }
        .section {
            margin-bottom: 30px;
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
            background-color: #00bfff;
        }

        section#dati-personali, section#eventi, section#esibizioni, section#recensioni, section#spazi-pubblicitari, section#candidature {
            margin-bottom: 30px;
            max-width: 1000px;
            margin: 0 auto;

        }

        section#eventi h2, section#esibizioni h2, section#recensioni h2, section#spazi-pubblicitari h2, section#candidature h2, section#newsletter h2{
            padding: 10px;
            border-radius: 5px;
            text-align: center;
            width: 90%;
           margin-left: 5%;
        }

        section#dati-personali h2{
            padding: 10px;
            border-radius: 5px;
            text-align: center;
            width: 90%;
        }
        section#candidature h3, section#esibizioni h3, section#eventi h3, section#recensioni h3, section#spazi-pubblicitari h3{
            font-weight: bold;
            margin-left: 11.8%;
            margin-right: 20%;
        }

        section#eventi form, section#esibizioni form, section#recensioni form, section#spazi-pubblicitari form, section#candidature form{
            display: grid;
            grid-template-columns: 1fr;
            gap: 2px;
            background-color: #fffdf3;
            padding: 20px;
            border: 1px solid #ccc;
            box-shadow: dimgray 0 0 5px;
            border-radius: 5px;
            width: 800px;
            margin-left: 11.8%;
            margin-right: 20%;
        }

        section#newsletter form{
            display: grid;
            grid-template-columns: 1fr;
            gap: 2px;
            background-color: #fffdf3;
            padding: 20px;
            border: 1px solid #ccc;
            box-shadow: dimgray 0 0 5px;
            border-radius: 5px;
            width: 800px;
            margin-left: 22.7%;
            margin-right: 20%;
        }

        section#dati-personali form{
            display: grid;
            grid-template-columns: 1fr;
            gap: 2px;
            background-color: #fffdf3;
            padding: 20px;
            border: 1px solid #ccc;
            box-shadow: dimgray 0 0 5px;
            border-radius: 5px;
            width: 800px;
            margin: auto;
        }
        section#dati-personali form label, section#esibizioni form label, section#recensioni form label, section#eventi form label, section#spazi-pubblicitari form label, section#candidature form label{
            margin-right: 50px;
            font-weight: bold;
            text-align: left;
            gap: 10px;
        }

        section#dati-personali form input[type="text"], section#dati-personali form textarea, section#eventi form input[type="text"], section#eventi form textarea, section#esibizioni form input[type="text"], section#esibizioni form textarea, section#recensioni form input[type="text"], section#recensioni form textarea, section#newsletter form textarea, section#spazi-pubblicitari form input[type="text"], section#candidature form input[type="text"], section#newsletter textarea{
            padding: 5px;
            border: 1px solid #ccc;
            border-radius: 5px;
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
    <script>
        function toggleEdit(sectionId) {
            var section = document.getElementById(sectionId);
            var inputs = section.querySelectorAll('input, textarea');
            inputs.forEach(input => {
                input.disabled = !input.disabled;
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
            <li <%=menuActiveLink.equals("Home Organizzatore") ? "class=\"active\"" : ""%>>
                <a href="Dispatcher?controllerAction=OrganizzatoreManagement.view">Home Organizzatore</a>
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
    <h1 class="centrato">Benvenuto nella tua area personale!</h1>
    <div class="sidebar">
        <a href="#dati-personali">I miei dati personali</a>
        <a href="#esibizioni">Overview esibizioni</a>
        <a href="#eventi">Overview eventi</a>
        <a href="#candidature">Candidature</a>
        <a href="#spazi-pubblicitari">Spazi pubblicitari</a>
        <a href="#recensioni">Le recensioni ai tuoi eventi</a>
        <a href="#newsletter">Invia una Newsletter</a>
    </div>
    <div class="content">
        <section id="dati-personali">
            <h2>I miei dati personali</h2>
            <form method="post" action="Dispatcher" name="modifyForm">
                <label for="nome">Nome: </label>
                <input type="text" id="nome" name="nome" value="<%= loggedOrganizzatore.getNome() %>"> <br>
                <label for="cognome">Cognome: </label>
                <input type="text" id="cognome" name="cognome" value="<%= loggedOrganizzatore.getCognome() %>"> <br>
                <label for="email">Email: </label>
                <input type="text" id="email" name="email" value="<%= loggedOrganizzatore.getEmail()%>" > <br>
                <label for="password">Password: </label>
                <input type="text" id="password" name="password" value="<%=loggedOrganizzatore.getPassword()%>" > <br>
                <label for="idorganizzatore">Username: </label>
                <input type="text" id="idorganizzatore" name="idorganizzatore" value="<%=loggedOrganizzatore.getIdOrganizzatore()%>" disabled> <br>
                <label for="codiceautorizzazione">Codice di Autorizzazione: </label>
                <input type="text" id="codiceautorizzazione" name="codiceaut" value="<%=loggedOrganizzatore.getCodiceAutorizzazione()%>" disabled > <br>
                <input type="hidden" name="controllerAction" value="OrganizzatoreManagement.modifyOrganizzatore"/>
                <input type="submit" class="bottone-personalizzato" value="Salva modifiche" >
            </form>
        </section>
        <section id="esibizioni" class="section">
            <h2>Overview esibizioni</h2>
            <%if (esibizioni != null && !esibizioni.isEmpty()) { %>
            <% for (i=0; i<esibizioni.size();i++) {%>
            <h3>Esibizione n&deg; <%= i + 1 %></h3>
            <form method="post" action="Dispatcher" name="modifyForm">
                <label for="codice-esibizione">Codice esibizione: </label>
                <input type="text" id="codice-esibizione" name="codice-esibizione" value="<%= esibizioni.get(i).getIdEsibizione() %>" disabled> <br>
                <label for="nome-esibizione">Nome esibizione: </label>
                <input type="text" id="nome-esibizione" name="nome-esibizione" value="<%= esibizioni.get(i).getNome() %>"> <br>
                <label for="durata">Durata: </label>
                <input type="text" id="durata" name="durata" value="<%= esibizioni.get(i).getDurata() %>" > <br>
                <label for="ora-inizio">Ora inizio: </label>
                <input type="text" id="ora-inizio" name="ora-inizio" value="<%= esibizioni.get(i).getOraInizio() %>" > <br>
                <label for="genere">Genere: </label>
                <input type="text" id="genere" name="genere" value="<%= esibizioni.get(i).getGenere() %>" > <br>
                <label for="codiceevento">Codice evento al quale appartiene: </label>
                <input type="text" id="codiceevento" name="codiceevento" value="<%= esibizioni.get(i).getIdEvento().getIdEvento()%>" disabled > <br>
                <label for="codice-luogo" >Codice luogo nel quale si svolge: </label>
                <input type="text" id="codice-luogo" name="codice-luogo" value="<%= esibizioni.get(i).getIdLuogo().getIdLuogo() %>" disabled> <br>
                <label for="descrizione">Descrizione: </label>
                <input type="text" id="descrizione" name="descrizione" value="<%= esibizioni.get(i).getDescrizione() %>" > <br>

                <input type="hidden" name="IdEsibizione" value="<%=esibizioni.get(i).getIdEsibizione()%>"/>

                <button type="submit" class="bottone-personalizzato" name="controllerAction" value="OrganizzatoreManagement.infoEsibizione">
                    Info esibizione
                </button>


                <!-- Bottone per eliminare l'esibizione -->
                <button type="submit" class="bottone-personalizzato" name="controllerAction" value="OrganizzatoreManagement.deleteEsibizione">
                    Elimina esibizione
                </button>

                <!-- Bottone per modificare l'esibizione -->
                <button type="submit" class="bottone-personalizzato" name="controllerAction" value="OrganizzatoreManagement.modifyEsibizione">
                    Modifica esibizione
                </button>

            </form>
            <% } %>
            <% } %>
            <br>
        </section>
        <section id="eventi" class="section">
            <h2>Overview eventi</h2>
            <% if (eventi != null && !eventi.isEmpty()) {%>
            <% for (i=0; i<eventi.size();i++) {%>
            <h3>Evento n&deg; <%= i + 1 %></h3>
            <form method="post" action="Dispatcher" name="modifyForm">
                <label for="codice-evento">Codice evento: </label>
                <input type="text" id="codice-evento" name="codice-evento" value="<%= eventi.get(i).getIdEvento() %>" disabled> <br>
                <label for="nome-evento">Nome evento: </label>
                <input type="text" id="nome-evento" name="nome-evento" value="<%= eventi.get(i).getNome() %>"> <br>
                <label for="data-inizio">Data di inizio: </label>
                <input type="date" id="data-inizio" name="data-inizio" value="<%= eventi.get(i).getDataInizio() %>" readonly> <br>
                <label for="data-fine">Data di fine: </label>
                <input type="date" id="data-fine" name="data-fine" value="<%= eventi.get(i).getDataFine() %>" readonly > <br>
                <label for="descrizione-evento" >Descrizione:  </label>
                <input type="text" id="descrizione-evento" name="descrizione-evento" value="<%= eventi.get(i).getDescrizione() %>" > <br>
                <label for="abbonamenti">Abbonamenti Venduti: </label>
                <input type="text" id="abbonamenti" name="abbonamenti" value="<%= abbonamentiVenduti.get(i)%>" readonly>
                <input type="hidden" name="idEvento" value="<%=eventi.get(i).getIdEvento()%>">
                <!-- Bottone per eliminare l'esibizione -->
                <button type="submit" class="bottone-personalizzato" name="controllerAction" value="OrganizzatoreManagement.deleteEvento">
                    Elimina evento
                </button>
                <!-- Bottone per modificare l'esibizione -->
                <button type="submit" class="bottone-personalizzato" name="controllerAction" value="OrganizzatoreManagement.modifyEvento">
                    Modifica evento
                </button>

            </form>
            <% } %>
            <% } %>
            <br>
        </section>

        <section id="candidature" class="section" >
            <h2>Overview Candidature</h2>
            <form method="post" action="Dispatcher" name="cercaCandidatura">
                <select id="position"style=" box-sizing: border-box; border: #dddddd 1px solid; padding: 8px; border-radius: 5px; width: fit-content" name="position">
                    <option value="" >Tutte le posizioni</option>
                    <option value="socialmedia" >Social Media </option>
                    <option value="photographer" >Photographer </option>
                    <option value="videomaker" >Videomaker </option>
                    <option value="content manager">Content Manager</option>
                    <option value="barman">Barman</option>
                    <option value="Security">Security</option>
                    <option value="Cassiere">Cassiere</option>
                </select> <br>
                <input type="submit" class="bottone-personalizzato" value="Cerca candidature">
                <input type="hidden" name="controllerAction" value="OrganizzatoreManagement.searchCandidature"/>
            </form>
            <%if (candidature != null && !candidature.isEmpty()) { %>
            <% for (i=0; i<candidature.size();i++) {%>
            <h3>Candidato n&deg; <%= i + 1 %></h3>
            <form method="post" action="Dispatcher" name="Candidature">
                <%--@declare id="posizione"--%>
                <%--@declare id="telefono"--%><%--@declare id="descrizione-candidatura"--%>
                <%--@declare id="messaggio"--%>
                <label for="posizione">Posizione lavorativa: </label> <span class="posizione"><%= candidature.get(i).getPosizione() %></span> <br>
                <label for="nome">Nome candidato/a: </label> <span class="nome"><%= candidature.get(i).getNome() %></span> <br>
                <label for="cognome">Cognome candidato/a: </label> <span class="cognome"><%= candidature.get(i).getCognome() %></span> <br>
                <label for="email">Email candidato/a: </label> <span class="email"><%= candidature.get(i).getEmail() %></span> <br>
                    <input type="hidden" name="email" value="<%= candidature.get(i).getEmail() %>">
                <label for="telefono">Telefono candidato/a: </label> <span class="telefono"><%= candidature.get(i).getTelefono() %></span> <br>
                <label for="descrizione-candidatura">Descrizione: </label> <span class="descrizione-candidatura"><%= candidature.get(i).getDescrizione() %></span> <br>
                <label for="messaggio">Invia un messaggio al candidato/a: </label>
                <textarea name="mailtext" placeholder="Digita qui"></textarea>

                    <input type="submit" class="bottone-personalizzato" value="Invia e-mail">
                    <input type="hidden" name="controllerAction" value="OrganizzatoreManagement.sendEmailCandidato"/>
            </form>
            <% } %>
            <% } %>
        </section>

        <section id="spazi-pubblicitari" class="section">
            <h2>Spazi pubblicitari</h2>
            <%if (sponsorizzazioni != null && !sponsorizzazioni.isEmpty()) { %>
            <% for (i=0; i<sponsorizzazioni.size();i++) {
                Blob logoBlob = sponsorizzazioni.get(i).getLogo();

                // Ottieni la lunghezza del Blob (dimensione dell'array di byte)
                int blobLength = (int) logoBlob.length();

                // Leggi i dati del Blob in un array di byte
                byte[] logoBytes = logoBlob.getBytes(1, blobLength);
                // Assume che getLogo() restituisca un byte array del BLOB
                String base64Image = Base64.getEncoder().encodeToString(logoBytes);%>
            <h3>Spazio n&deg; <%= i + 1 %></h3>
            <form>
                <%--@declare id="partitaiva"--%><%--@declare id="logo"--%><%--@declare id="costo"--%>
                <label for="partitaIVA">Partita IVA dell'azienda: </label> <span class="partitaIVA"><%= sponsorizzazioni.get(i).getPartitaIVA().getPartitaIVA() %></span>
                <label for="codice-evento">Codice evento contenente lo spazio: </label> <span class="codice-evento"><%= sponsorizzazioni.get(i).getIdEvento().getIdEvento() %></span>
               <!-- <label for="logo">Logo: </label>--> <img src="data:image/jpeg;base64, <%= base64Image %>" style="max-width: 200px; max-height: 200px" alt="Logo dell'azienda">
                <label for="costo">Costo: </label> <span class="costo"><%= sponsorizzazioni.get(i).getCosto() %></span>
            </form>
            <% } %>
            <% } %>
        </section>

        <section id="recensioni" class="section">
            <h2>Le recensioni ai tuoi eventi</h2>
            <%if (recensioni != null && !recensioni.isEmpty()) { %>
            <% for (i=0; i<recensioni.size();i++) {%>
            <h3>Recensione n&deg; <%= i + 1 %></h3>
            <form method="post" action="Dispatcher">
                <%--@declare id="stelle"--%>
                <label for="nome-evento">Nome Evento: </label> <span class="nome-evento"><%= recensioni.get(i).getIdEvento().getNome() %></span>
                <label for="nome">Nome Utente: </label> <span class="nome"><%= recensioni.get(i).getIdUtente().getNome() %></span>
                <label for="stelle">Numero stelle: </label> <span class="stelle"><%= recensioni.get(i).getStelle() %></span>
                <label for="descrizione">Descrizione: </label> <span class="descrizione"><%= recensioni.get(i).getDescrizione() %></span>

                    <form name="deleteRecensione" action="Dispatcher" method="post">
                        <input type="hidden" name="controllerAction" value="OrganizzatoreManagement.deleteRecensione"/>
                        <input type="hidden" name="idRecensione" value="<%=recensioni.get(i).getIdRecensione()%>"/>
                        <input type="submit" class="bottone-personalizzato" value="Elimina recensione">
                    </form>
            </form>
            <% } %>
            <% } %>
        </section>

        <section id="newsletter" class="section">
            <h2>Invia una newsletter</h2>
            <form method="post" action="Dispatcher" name="Newsletter">
                <label for="messaggio">Inserisci il testo della newsletter: </label>
                <br>
                <textarea style="border-radius: 5px; height: 50px" name="mailtext" placeholder="Digita qui"></textarea>
                <br>
                <input type="submit" class="bottone-personalizzato" value="Invia">
                <input type="hidden" name="controllerAction" value="OrganizzatoreManagement.sendNewsletter"/>
            </form>


        </section>
    </div>
    <% } else{ %>
    <p>Effettua il login per vedere i tuoi dati.</p>
    <% } %>
</main>
<footer>
    &copy; 2024 EventPrime - Italia IT | Cookie e Privacy Policy<br>
    Credits: Leonardo Rastelli e Anna Ferri
</footer>
</body>
</html>