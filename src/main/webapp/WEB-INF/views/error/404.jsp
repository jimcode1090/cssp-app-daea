<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Sismo no encontrado · Sistema de Gestión de Sismos</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<header class="topbar"><div class="topbar-in"><a class="brand" href="${pageContext.request.contextPath}/sismos"><svg viewBox="0 0 32 32" aria-hidden="true"><rect width="32" height="32" rx="3" fill="#2C5F9E"/><polyline points="3,17 9,17 12,8 16,25 20,12 23,17 29,17" fill="none" stroke="#fff" stroke-width="2" stroke-linejoin="round" stroke-linecap="round"/></svg><span class="brand-name">CSSP</span><span class="brand-sub">Sistema de Gestión de Sismos</span></a><div class="userbox">Módulo Sismos · Primera entrega</div></div></header>
<main class="page narrow">
<div class="card"><div class="errbox">
    <div class="code">404</div>
    <h1>Sismo no encontrado</h1>
    <p><c:out value="${mensajeError}"/></p>
    <a class="btn primary" href="${pageContext.request.contextPath}/sismos">Volver al listado</a>
</div></div>
</main>
</body>
</html>
