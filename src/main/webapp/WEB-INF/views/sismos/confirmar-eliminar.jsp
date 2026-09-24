<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Confirmar eliminación · Sistema de Gestión de Sismos</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<header class="topbar"><div class="topbar-in"><a class="brand" href="${pageContext.request.contextPath}/sismos"><svg viewBox="0 0 32 32" aria-hidden="true"><rect width="32" height="32" rx="3" fill="#2C5F9E"/><polyline points="3,17 9,17 12,8 16,25 20,12 23,17 29,17" fill="none" stroke="#fff" stroke-width="2" stroke-linejoin="round" stroke-linecap="round"/></svg><span class="brand-name">CSSP</span><span class="brand-sub">Sistema de Gestión de Sismos</span></a><div class="userbox">Módulo Sismos · Primera entrega</div></div></header>
<main class="page narrow">
<div class="crumbs"><a href="${pageContext.request.contextPath}/sismos">Sismos</a> / Eliminar <c:out value="${sismo.codigo}"/></div>
<div class="page-head">
    <div><h1>Confirmar eliminación</h1><p class="lead">Revise el registro antes de continuar</p></div>
</div>

<div class="card">
<div class="card-h">Sismo a eliminar</div>
<div class="card-b flush">
<dl class="dl" style="margin:0">
    <dt>Código</dt><dd><b><c:out value="${sismo.codigo}"/></b></dd>
    <dt>Fecha</dt><dd><c:out value="${sismo.fechaHoraTexto}"/></dd>
    <dt>Magnitud</dt><dd><c:out value="${sismo.magnitud}"/></dd>
    <dt>Ubicación</dt><dd><c:out value="${sismo.departamento}"/></dd>
</dl>
</div>
</div>

<div class="alert warn"><b>Advertencia:</b> esta acción no se puede deshacer.</div>

<form method="post" action="${pageContext.request.contextPath}/sismos/eliminar" class="actions">
    <input type="hidden" name="codigo" value="<c:out value='${sismo.codigo}'/>">
    <button type="submit" class="btn danger">Confirmar</button>
    <a class="btn" href="${pageContext.request.contextPath}/sismos/detalle?codigo=${sismo.codigo}">Cancelar</a>
</form>
</main>
</body>
</html>
