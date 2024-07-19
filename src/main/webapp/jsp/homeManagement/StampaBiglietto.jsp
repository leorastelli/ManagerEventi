<%@ page import="com.managereventi.managereventi.model.mo.Utente" %>
<%@ page import="com.managereventi.managereventi.model.mo.Biglietto" %>
<%@ page import="com.managereventi.managereventi.model.mo.Evento" %>
<%@ page import="com.managereventi.managereventi.model.mo.Esibizione" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    Utente loggedUser = (Utente) request.getAttribute("loggedUser");
    Biglietto biglietto = (Biglietto) request.getAttribute("biglietto");
    String qrcode = (String) request.getAttribute("qrcode");
    Evento evento = (Evento) request.getAttribute("evento");
    Esibizione esibizione = (Esibizione) request.getAttribute("esibizione");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Conferma Acquisto Biglietti</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f7f7f7; }
        .container { width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border: 1px solid #dddddd; }
        .header { text-align: center; padding: 10px 0; border-bottom: 1px solid #dddddd; }
        .header h1 { margin: 0; font-size: 24px; color: #333333; }
        .content { padding: 20px 0; }
        .ticket { border-bottom: 1px solid #dddddd; padding: 10px 0; }
        .ticket:last-child { border-bottom: none; }
        .ticket p { margin: 5px 0; }
        .print-button { text-align: center; margin-top: 20px; }
    </style>
    <script>
        function printPage() {
            window.print();
        }
    </script>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>Conferma Acquisto Biglietti</h1>
    </div>
    <div class="content">
        <div class="ticket">
            <p><strong>ID Biglietto:</strong> <%= biglietto.getIdBiglietto() %></p>
            <p><strong>ID Utente:</strong> <%= loggedUser.getIdUtente() %></p>
            <p><strong>Nome Evento:</strong> <%= evento.getNome() %></p>
            <p><strong>Nome Esibizione:</strong> <%= esibizione.getNome()%></p>
            <p><strong>Posto:</strong> <%= biglietto.getPosto() %></p>
            <p><strong>Prezzo:</strong> <%= biglietto.getPrezzo() %> &euro;</p>
            <p><strong>Tipo:</strong> <%= biglietto.getTipo() %></p>
            <p><strong>QR Code:</strong></p>
            <img src="data:image/png;base64,<%= qrcode %>" alt="QR Code">
        </div>
    </div>
    <div class="print-button">
        <button onclick="printPage()">Stampa</button>
    </div>
    <p>Grazie per aver scelto PrimeEventi!</p>
</div>
</body>
</html>
