<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Detalle de estación · Sistema de Gestión de Sismos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<header class="topbar">
    <div class="topbar-in"><a class="brand" href="${pageContext.request.contextPath}/sismos">
        <svg viewBox="0 0 32 32" aria-hidden="true">
            <rect width="32" height="32" rx="3" fill="#2C5F9E"/>
            <polyline points="3,17 9,17 12,8 16,25 20,12 23,17 29,17" fill="none" stroke="#fff" stroke-width="2"
                      stroke-linejoin="round" stroke-linecap="round"/>
        </svg>
        <span class="brand-name">CSSP</span><span class="brand-sub">Sistema de Gestión de Sismos</span></a></div>
</header>
<main class="page narrow">
    <div class="crumbs"><a href="${pageContext.request.contextPath}/estaciones">Estaciones</a> / <c:out
            value="${estacion.codigo}"/></div>
    <div class="page-head">
        <div><h1>Detalle de la estación <c:out value="${estacion.codigo}"/></h1></div>
        <div class="actions">
            <a class="btn" href="${pageContext.request.contextPath}/estaciones/editar?codigo=${estacion.codigo}">Editar</a>
            <a class="btn" href="${pageContext.request.contextPath}/estaciones">Volver al listado</a>
        </div>
    </div>

    <div class="card">
        <div class="card-h">Datos de la estación</div>
        <div class="card-b flush">
            <dl class="dl" style="margin:0">
                <dt>Código</dt>
                <dd><b><c:out value="${estacion.codigo}"/></b></dd>
                <dt>Nombre</dt>
                <dd><c:out value="${estacion.nombre}"/></dd>
                <dt>Departamento</dt>
                <dd><c:out value="${estacion.provincia.departamento.nombre}"/></dd>
                <dt>Provincia</dt>
                <dd><c:out value="${estacion.provincia.nombre}"/></dd>
                <dt>Latitud</dt>
                <dd><c:out value="${estacion.latitud}"/></dd>
                <dt>Longitud</dt>
                <dd><c:out value="${estacion.longitud}"/></dd>
                <dt>Fecha de instalación</dt>
                <dd><c:out value="${estacion.fechaInstalacionTexto}"/></dd>
                <dt>Estado</dt>
                <dd>
                    <c:choose>
                        <c:when test="${estacion.estado == 'ACTIVO'}"><span class="badge activa">Activa</span></c:when>
                        <c:otherwise><span class="badge inactiva">Inactiva</span></c:otherwise>
                    </c:choose>
                </dd>
            </dl>
        </div>
    </div>

    <c:if test="${not empty sismos}">
        <div class="card">
            <div class="card-h">Sismos asociados</div>
            <div class="card-b flush">
                <table class="data">
                    <thead>
                    <tr>
                        <th>Código</th>
                        <th>Fecha y hora</th>
                        <th class="num">Magnitud</th>
                        <th>Acciones</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${sismos}" var="sismo">
                        <tr>
                            <td class="code"><c:out value="${sismo.codigo}"/></td>
                            <td><c:out value="${sismo.fechaHoraTexto}"/></td>
                            <td class="num"><c:out value="${sismo.magnitud}"/></td>
                            <td>
                                <a class="btn sm"
                                   href="${pageContext.request.contextPath}/sismos/detalle?codigo=${sismo.codigo}">Ver</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:if>
</main>
</body>
</html>
