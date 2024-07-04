<!--Dalla Home è possibile accedere attraverso il tasto "Accedi", oppure iscriversi attraverso il tasto "Iscriviti"
Si vedono 3 grandi blocchi, Eventi, Recensioni e Lavora con noi ai quali si accede spingendoci su-->

<%@page session="false"%>
<%@page import="com.managereventi.managereventi.model.mo.Utente"%>

<%
  boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
  Utente loggedUser = (Utente) request.getAttribute("loggedUser");
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
    .image-box {
      margin: 20px auto;
      width: 80%;
      max-width: 400px;
      position: relative;
      cursor: pointer;
    }
    .image-box img {
      width: 100%;
      height: auto;
    }
    .image-box p {
      position: absolute;
      bottom: 10px;
      right: 10px;
      margin: 0;
      padding: 5px;
      background-color: rgba(0, 0, 0, 0.5);
      color: white;
      font-size: 1.2em;
    }
    footer {
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

  <h1>Scopri Eventi Unici!</h1>
  <div class="image-box" onclick="location.href='pagamento.html'">
    <img src="images/evento.jpg" alt="Eventi">
    <p>Eventi</p>
  </div>
  <div class="image-box" onclick="location.href='Dispatcher?controllerAction=ReviewManagement.view'">
    <img src="images/recensioni.jpeg" alt="Cosa dicono di noi">
    <p>Cosa dicono di noi</p>
  </div>
  <div class="image-box" onclick="location.href='Dispatcher?controllerAction=candidatureManagement.view'">
    <img src="images/lavoraconnoi.jpg" alt="Lavora con Noi">
    <p>Lavora con Noi</p>
  </div>
</main>
<footer>
  &copy; 2024 EventPrime - Italia IT | Cookie e Privacy Policy<br>
  Credits: Leonardo Rastelli e Anna Ferri
</footer>
</body>
</html>
