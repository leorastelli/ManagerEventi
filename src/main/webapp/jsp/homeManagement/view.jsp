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
  <%@include file="/include/htmlHead.inc"%>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>PrimEvent - Home</title>
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
      text-align: center;
      margin-top: 20px;
    }
    h1 {
      font-size: 2em;
    }
    .title {
      font-size: 45px;
    }
    .image-box {
      margin: 20px auto;
      width: 90%;
      height: 90%;
      max-width: 600px;
      max-height: 600px;
      position: relative;
      cursor: pointer;
    }
    .image-box img {
      width: 670px;
      height: 670px;
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
      position: fixed;
      bottom: 0;
      width: 100%;
      clear: both;
      text-align: center;
      padding: 10px;
      background-color: #6fa3ef;
      color: #fff;
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
<main>
  <h1 class="title"> Scopri Eventi Unici!</h1>
  <div class="image-box" onclick="location.href='Dispatcher?controllerAction=EventiManagement.view'">
    <img src="images/evento.jpg" alt="Eventi">
    <p>Eventi</p>
  </div>
  <div class="image-box" onclick="location.href='Dispatcher?controllerAction=ReviewManagement.view'">
    <img src="images/recensioni.jpeg" alt="Cosa dicono di noi">
    <p>Cosa dicono di noi</p>
  </div>
  <div class="image-box" onclick="location.href='Dispatcher?controllerAction=CandidatureManagement.view'">
    <img src="images/lavoraconnoi.jpg" alt="Lavora con Noi">
    <p>Lavora con Noi</p>
  </div>
  <div class="image-box" onclick="location.href='Dispatcher?controllerAction=DomandeManagement.view'">
    <img src="images/domande.jpg" alt="Domande degli utenti">
    <p>Domande degli utenti</p>
  </div>
</main>
<footer>
  &copy; 2024 EventPrime - Italia IT | Cookie e Privacy Policy<br>
  Credits: Leonardo Rastelli e Anna Ferri
</footer>
</body>
</html>
