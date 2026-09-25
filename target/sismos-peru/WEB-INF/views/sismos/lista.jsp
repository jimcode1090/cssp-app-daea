<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Listado de sismos · Sistema de Gestión de Sismos</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<header class="topbar"><div class="topbar-in"><a class="brand" href="${pageContext.request.contextPath}/sismos"><svg viewBox="0 0 32 32" aria-hidden="true"><rect width="32" height="32" rx="3" fill="#2C5F9E"/><polyline points="3,17 9,17 12,8 16,25 20,12 23,17 29,17" fill="none" stroke="#fff" stroke-width="2" stroke-linejoin="round" stroke-linecap="round"/></svg><span class="brand-name">CSSP</span><span class="brand-sub">Sistema de Gestión de Sismos</span></a><div class="userbox">Módulo Sismos · Primera entrega</div></div></header>
<main class="page">
<div class="page-head">
    <div><h1>Listado de sismos</h1><p class="lead">Reportes de sismos registrados en el sistema</p></div>
    <div class="actions"><a class="btn primary" href="${pageContext.request.contextPath}/sismos/nuevo">+ Nuevo sismo</a></div>
</div>

<c:if test="${eliminado}">
    <div class="alert ok"><b>Zona de mensajes:</b> El sismo <c:out value="${codigoEliminado}"/> fue eliminado correctamente.</div>
</c:if>

<div class="card">
<div class="card-b flush">
<table class="data">
<thead>
<tr><th>Código</th><th>Fecha y hora</th><th class="num">Magnitud</th><th class="num">Profundidad</th><th>Departamento</th><th>Estado</th><th>Acciones</th></tr>
</thead>
<tbody>
<c:forEach items="${sismos}" var="sismo">
<tr>
    <td class="code"><c:out value="${sismo.codigo}"/></td>
    <td><c:out value="${sismo.getFechaHoraTexto()}"/></td>
    <td class="num"><c:out value="${sismo.magnitud}"/></td>
    <td class="num"><c:out value="${sismo.profundidad}"/> km</td>
    <td><c:out value="${sismo.departamento}"/></td>
    <td>
        <c:choose>
            <c:when test="${sismo.estado == 'Registrado'}"><span class="badge registrado">Registrado</span></c:when>
            <c:when test="${sismo.estado == 'En evaluación'}"><span class="badge evaluacion">En evaluación</span></c:when>
            <c:when test="${sismo.estado == 'En seguimiento'}"><span class="badge seguimiento">En seguimiento</span></c:when>
            <c:otherwise><span class="badge cerrado"><c:out value="${sismo.estado}"/></span></c:otherwise>
        </c:choose>
    </td>
    <td>
        <div class="btn-row">
            <a class="btn sm" href="${pageContext.request.contextPath}/sismos/detalle?codigo=${sismo.codigo}">Ver</a>
            <a class="btn sm" href="${pageContext.request.contextPath}/sismos/editar?codigo=${sismo.codigo}">Editar</a>
            <a class="btn sm danger" href="${pageContext.request.contextPath}/sismos/eliminar?codigo=${sismo.codigo}">Eliminar</a>
        </div>
    </td>
</tr>
</c:forEach>
<c:if test="${empty sismos}">
    <tr><td colspan="7" class="vacio">No hay sismos registrados</td></tr>
</c:if>
</tbody>
</table>
<div class="tbl-foot">
    <span><c:out value="${totalSismos}"/> sismos registrados</span>
    <span>Datos de ejemplo cargados al iniciar la aplicación</span>
</div>
</div>
</div>
</main>
</body>
</html>
