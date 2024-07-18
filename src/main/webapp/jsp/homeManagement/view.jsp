<!--Dalla Home è possibile accedere attraverso il tasto "Accedi", oppure iscriversi attraverso il tasto "Iscriviti"
Si vedono 3 grandi blocchi, Eventi, Recensioni e Lavora con noi ai quali si accede spingendoci su-->

<%@page session="false"%>
<%@page import="com.managereventi.managereventi.model.mo.Utente"%>
<%@ page import="com.managereventi.managereventi.model.mo.Organizzatore" %>
<%@ page import="com.managereventi.managereventi.model.mo.Azienda" %>

<%
  boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
  Utente loggedUser = (Utente) request.getAttribute("loggedUser");
  Organizzatore loggedOrganizzatore = (Organizzatore) request.getAttribute("loggedOrganizzatore");
  Azienda loggedAzienda = (Azienda) request.getAttribute("loggedAzienda");
  String applicationMessage = (String) request.getAttribute("applicationMessage");
  String menuActiveLink = "Home";
%>

<!DOCTYPE html>
<html lang="it">
<head>

  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>PrimEvent - Home</title>
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
      text-align: center;
      margin-top: 20px;
      height: 100%;
      width: auto;
    }

    .title {
      font-size: 45px;
      color: #ab00cc;
      text-align: center;
      margin: auto;
      width: 100%;
      justify-content: center;
      margin-top: 20px;
    }

    .image-box {
      margin: 20px auto;
      width: 80%;
      height: 80%;
      max-width: 500px;
      max-height: 500px;
      position: relative;
      cursor: pointer;
      justify-content: space-around;
      transition: box-shadow 0.2s ease-in-out;
    }
    .image-box:hover {
      box-shadow: #ab00cc 0 0 15px;
    }
    .image-box img {
      width: 500px;
      height: 500px;
    }
    .image-box p {
      position: absolute;
      text-align: right;
      bottom: 10px;
      right: 15px;
      margin: 2px;
      padding: 5px;
      color: white;
      font-weight: bolder;
      font-size: 40px;
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
        <% if (loggedUser != null) { %>
        <li <%=menuActiveLink.equals("Home Utente") ? "class=\"active\"" : ""%>>
          <a href="Dispatcher?controllerAction=UserManagement.view">Home Utente</a>
        </li>
        <li><a href="javascript:logoutForm.submit()">Logout</a></li>
        <% } else if (loggedOrganizzatore != null) { %>
        <li <%=menuActiveLink.equals("Home Organizzatore") ? "class=\"active\"" : ""%>>
          <a href="Dispatcher?controllerAction=OrganizzatoreManagement.view">Home Organizzatore</a>
        </li>
        <li><a href="javascript:logoutForm.submit()">Logout</a></li>
        <% } else if(loggedAzienda != null){ %>
        <li <%=menuActiveLink.equals("Home Azienda") ? "class=\"active\"" : ""%>>
          <a href="Dispatcher?controllerAction=AziendaManagement.view">Home Azienda</a>
        </li>
        <li><a href="javascript:logoutForm.submit()">Logout</a></li>
        <% }} else { %>
        <li <%=menuActiveLink.equals("Accedi") ? "class=\"active\"" : ""%>>
          <a href="Dispatcher?controllerAction=HomeManagement.gotoLogin">Accedi</a>
        </li>
        <li <%=menuActiveLink.equals("Registrati") ? "class=\"active\"" : ""%>>
          <a href="Dispatcher?controllerAction=HomeManagement.gotoRegistration">Registrati</a>
        </li>
        <% } %>
      </ul>
    </nav>
</header>
    <h1 class="title"> Scopri Eventi Unici!</h1>
<main>
  <section id="eventi" class="image-box" onclick="location.href='Dispatcher?controllerAction=EventiManagement.view'">
    <img src="images/eventi2.jpeg" alt="Eventi">
    <p>Eventi</p>
  </section>
  <section id="recensioni" class="image-box" onclick="location.href='Dispatcher?controllerAction=ReviewManagement.view'">
    <img src="images/recensioni2.png" alt="Cosa dicono di noi">
    <p>Cosa dicono di noi</p>
  </section>
  <section id="candidature" class="image-box" onclick="location.href='Dispatcher?controllerAction=CandidatureManagement.view'">
    <img src="images/candidarsi.jpeg" alt="Lavora con noi">
    <p>Lavora con noi</p>
  </section>
  <section id="domande" class="image-box" onclick="location.href='Dispatcher?controllerAction=DomandeManagement.view'">
    <img src="images/forum.jpeg" alt="Il nostro forum">
    <p>Il nostro forum</p>
  </section>
</main>
<footer>
  &copy; 2024 EventPrime - Italia IT | Cookie e Privacy Policy<br>
  Credits: Leonardo Rastelli e Anna Ferri
</footer>
</body>
</html>
