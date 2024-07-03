<%@ page session="false" %>
<%@ page import="com.managereventi.managereventi.model.mo.Utente" %>
<%@ page import="java.util.List" %>
<%@ page import="com.managereventi.managereventi.model.mo.Recensione" %>

<%
  Boolean loggedOnObj = (Boolean) request.getAttribute("loggedOn");
  boolean loggedOn = (loggedOnObj != null) ? loggedOnObj : false;

  Utente loggedUser = (Utente) request.getAttribute("loggedUser");
  List<Recensione> recensioni = (List<Recensione>) request.getAttribute("recensioni");
%>

<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Cosa dicono di noi - PrimEvent</title>
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
      text-align: center;
    }
    header h1 {
      margin: 0;
      font-family: 'Arial Black', sans-serif;
      font-size: 24px;
    }
    nav {
      display: flex;
      justify-content: center;
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
      margin: 20px;
    }
    .search-bar {
      display: flex;
      justify-content: center;
      margin-bottom: 20px;
    }
    .search-bar input[type="text"],
    .search-bar select {
      padding: 10px;
      border: 1px solid #ccc;
      border-radius: 5px;
      margin-right: 10px;
    }
    .review {
      background-color: white;
      padding: 20px;
      border: 1px solid #ccc;
      border-radius: 5px;
      margin-bottom: 20px;
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
  <nav>
    <ul>
      <li><a href="homeManagement/view.jsp">Home</a></li>
      <li><a href="homeManagement/Registrazione.jsp">Accedi</a></li>
      <li><a href="homeManagement/Registrazione.jsp">Iscriviti</a></li>
    </ul>
  </nav>
</header>

<main>
  <h2>Cosa dicono di noi</h2>
  <div class="search-bar">
    <form action="Dispatcher" method="get">
      <input type="hidden" name="controllerAction" value="ReviewManagement.filterReviews"/>
      <input type="text" name="nomeEvento" placeholder="Cerca recensione per evento">
      <select name="numeroStelle">
        <option value="">Tutte le stelle</option>
        <option value="1">1 stella</option>
        <option value="2">2 stelle</option>
        <option value="3">3 stelle</option>
        <option value="4">4 stelle</option>
        <option value="5">5 stelle</option>
      </select>
      <input type="submit" value="Cerca">
    </form>
  </div>

  <% if (recensioni != null && !recensioni.isEmpty()) { %>
  <% for (Recensione recensione : recensioni) { %>
  <div class="review">
    <p><strong>Nome Evento:</strong> <%= recensione.getIdEvento().getNome() %></p>
    <p><strong>Nome Utente:</strong> <%= recensione.getIdUtente().getNome() %></p>
    <p><strong>Numero di Stelle:</strong> <%= recensione.getStelle() %></p>
    <p><strong>Descrizione:</strong> <%= recensione.getDescrizione() %></p>
  </div>
  <% } %>
  <% } else { %>
  <p>Nessuna recensione disponibile.</p>
  <% } %>
</main>

<footer>
  &copy; 2024 EventPrime - Italia IT | Cookie e Privacy Policy<br>
  Credits: Leonardo Rastelli e Anna Ferri
</footer>

</body>
</html>
