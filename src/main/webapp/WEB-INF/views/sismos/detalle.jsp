<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Detalle del sismo · Sistema de Gestión de Sismos</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<header class="topbar"><div class="topbar-in"><a class="brand" href="${pageContext.request.contextPath}/sismos"><svg viewBox="0 0 32 32" aria-hidden="true"><rect width="32" height="32" rx="3" fill="#2C5F9E"/><polyline points="3,17 9,17 12,8 16,25 20,12 23,17 29,17" fill="none" stroke="#fff" stroke-width="2" stroke-linejoin="round" stroke-linecap="round"/></svg><span class="brand-name">CSSP</span><span class="brand-sub">Sistema de Gestión de Sismos</span></a><div class="userbox">Módulo Sismos · Primera entrega</div></div></header>
<main class="page narrow">
<div class="crumbs"><a href="${pageContext.request.contextPath}/sismos">Sismos</a> / <c:out value="${sismo.codigo}"/></div>
<div class="page-head">
    <div><h1>Detalle del sismo <c:out value="${sismo.codigo}"/></h1></div>
    <div class="actions">
        <a class="btn" href="${pageContext.request.contextPath}/sismos/editar?codigo=${sismo.codigo}">Editar</a>
        <a class="btn danger" href="${pageContext.request.contextPath}/sismos/eliminar?codigo=${sismo.codigo}">Eliminar</a>
        <a class="btn" href="${pageContext.request.contextPath}/sismos">Volver al listado</a>
    </div>
</div>

<c:if test="${sismoCreado}">
    <div class="alert ok">El sismo <c:out value="${sismo.codigo}"/> fue registrado correctamente.</div>
</c:if>
<c:if test="${sismoActualizado}">
    <div class="alert ok">El sismo <c:out value="${sismo.codigo}"/> fue actualizado correctamente.</div>
</c:if>

<div class="card">
<div class="card-h">Datos del sismo</div>
<div class="card-b flush">
<dl class="dl" style="margin:0">
    <dt>Código</dt><dd><b><c:out value="${sismo.codigo}"/></b></dd>
    <dt>Fecha y hora</dt><dd><c:out value="${sismo.fechaHoraTexto}"/></dd>
    <dt>Magnitud</dt><dd><c:out value="${sismo.magnitud}"/></dd>
    <dt>Profundidad</dt><dd><c:out value="${sismo.profundidad}"/> km</dd>
    <dt>Latitud</dt><dd><c:out value="${sismo.latitud}"/></dd>
    <dt>Longitud</dt><dd><c:out value="${sismo.longitud}"/></dd>
    <dt>Departamento</dt><dd><c:out value="${sismo.departamento}"/></dd>
    <dt>Referencia geográfica</dt><dd><c:out value="${sismo.referencia}"/></dd>
    <dt>Estado</dt>
    <dd>
        <c:choose>
            <c:when test="${sismo.estado == 'Registrado'}"><span class="badge registrado">Registrado</span></c:when>
            <c:when test="${sismo.estado == 'En evaluación'}"><span class="badge evaluacion">En evaluación</span></c:when>
            <c:when test="${sismo.estado == 'En seguimiento'}"><span class="badge seguimiento">En seguimiento</span></c:when>
            <c:otherwise><span class="badge cerrado"><c:out value="${sismo.estado}"/></span></c:otherwise>
        </c:choose>
    </dd>
</dl>
</div>
</div>
</main>
</body>
</html>
