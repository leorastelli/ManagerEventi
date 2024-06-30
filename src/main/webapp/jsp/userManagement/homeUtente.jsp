
<%@page session="false"%>
<%@page import="com.managereventi.managereventi.model.mo.Utente"%>
<%@ page import="java.util.List" %>
<%@ page import="com.managereventi.managereventi.model.mo.Biglietto" %>

<%
    Boolean loggedOnObj = (Boolean) request.getAttribute("loggedOn");
    boolean loggedOn = (loggedOnObj != null) ? loggedOnObj : false;

    int i;
    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";
    List<Biglietto> biglietti = (List<Biglietto>) request.getAttribute("biglietti");
%>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <section id="biglietti" class="clearfix">
        <%if (biglietti != null && !biglietti.isEmpty()){ %>
            <%for (i = 0; i < biglietti.size(); i++) {%>
        <article>
            <span class="phone"><%= biglietti.get(i).getIdBiglietto()%></span>
            <br/>

        </article>
        <%}}%>
</section>

</body>
</html>
