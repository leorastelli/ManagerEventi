
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registrazione Utente</title>
</head>
<body>
    <section id="register" class="clearfix">
        <h1>Registrazione Utente</h1>
        <form action="Dispatcher" method="post">
            <p>
                <label for="username">Username:</label>
                <input type="text" id="username" name="username" required>
            </p>
            <p>
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </p>
            <p>
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
            </p>
            <p>
                <label for="fullname">Nome:</label>
                <input type="text" id="fullname" name="nome" required>
            </p>
            <p>
                <label for="surname">Cognome:</label>
                <input type="text" id="surname" name="cognome" required>
            <p>
                <input type="hidden" name="controllerAction" value="UserManagement.registration"/>
                <input type="submit" value="Registrati">
            </p>
        </form>
    </section>
</body>
</html>

