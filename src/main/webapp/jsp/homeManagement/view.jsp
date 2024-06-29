<%@page session="false"%>
<%@page import="com.managereventi.managereventi.model.mo.Utente"%>

<%
  boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
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
          <%if (loggedOn) {%>
          <li <%=menuActiveLink.equals("Rubrica")?"class=\"active\"":""%>>
            <a href="Dispatcher?controllerAction=AddressBookManagement.view">Rubrica</a>
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
          <input type="hidden" name="controllerAction" value="HomeManagement.logon"/>
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
