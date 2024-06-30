<%@page session="false"%>
<%@page import="com.managereventi.managereventi.model.mo.Utente"%>
<%@ page import="java.util.List" %>
<%@ page import="com.managereventi.managereventi.model.mo.Biglietto" %>

<%
    Boolean loggedOnObj = (Boolean) request.getAttribute("loggedOn");
    boolean loggedOn = (loggedOnObj != null) ? loggedOnObj : false;

    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Home";

%>

<!DOCTYPE html>
<html>
<head>
    <%@include file="/include/htmlHead.inc"%>
</head>
<body>
<script>
    function headerOnLoadHandler() {
        var usernameTextField = document.querySelector("#username");
        var usernameTextFieldMsg = "Lo username \xE8 obbligatorio.";
        var passwordTextField = document.querySelector("#password");
        var passwordTextFieldMsg = "La password \xE8 obbligatoria.";

        if (usernameTextField != undefined && passwordTextField != undefined ) {
            usernameTextField.setCustomValidity(usernameTextFieldMsg);
            usernameTextField.addEventListener("change", function () {
                this.setCustomValidity(this.validity.valueMissing ? usernameTextFieldMsg : "");
            });
            passwordTextField.setCustomValidity(passwordTextFieldMsg);
            passwordTextField.addEventListener("change", function () {
                this.setCustomValidity(this.validity.valueMissing ? passwordTextFieldMsg : "");
            });
        }
    }
</script>

<header class="clearfix"><!-- Defining the header section of the page -->

    <h1 class="logo"><!-- Defining the logo element -->
        ManagerEventi
    </h1>

    <form name="logoutForm" action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="HomeManagement.logout"/>
    </form>

    <nav><!-- Defining the navigation menu -->
        <ul>
            <li <%=menuActiveLink.equals("Home")?"class=\"active\"":""%>>
                <a href="Dispatcher?controllerAction=HomeManagement.view">Home</a>
            </li>
            <%if (!loggedOn) {%>
            <li <%=menuActiveLink.equals("Registrati")?"class=\"active\"":""%>>
                <a href="Dispatcher?controllerAction=UserManagement.view">Registrati</a>
            <%}%>
            <%if (loggedOn) {%>
            <li <%=menuActiveLink.equals("Home Utente")?"class=\"active\"":""%>>
                <a href="Dispatcher?controllerAction=UserManagement.view">Home Utente</a>
            </li>
            <li><a href="javascript:logoutForm.submit()">Logout</a></li>
            <%}%>
        </ul>
    </nav>

    <%if (!loggedOn) {%>
    <section id="login" class="clearfix">
        <form name="logonForm" action="Dispatcher" method="post">
            <label for="username">Utente</label>
            <input type="text" id="username"  name="username" maxlength="40" required>
            <label for="password">Password</label>
            <input type="password" id="password" name="password" maxlength="40" required>
            <input type="radio" id="userTypeUser" name="userType" value="utente">
            <label for="userTypeUser">Utente</label>

            <input type="radio" id="userTypeOrganizer" name="userType" value="organizzatore">
            <label for="userTypeOrganizer">Organizzatore</label>

            <input type="radio" id="userTypeCompany" name="userType" value="azienda">
            <label for="userTypeCompany">Azienda</label>

            <input type="hidden" name="controllerAction" value="HomeManagement.logon2"/>
            <input type="submit" value="Ok">
        </form>
    </section>
    <%}%>

</header>
<main>
    <%if (loggedOn) {%>
    Benvenuto <%=loggedUser.getNome()%> <%=loggedUser.getCognome()%>!<br/>
    Clicca sulla voce "Rubrica" del men&ugrave; per gestire i tuoi contatti.
    <%} else {%>
    Benvenuto.
    Fai il logon per gestire la tua rubrica.

    <%}%>

</main>
<%@include file="/include/footer.inc"%>
</html>
