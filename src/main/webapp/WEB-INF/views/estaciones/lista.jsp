<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Estaciones · Sistema de Gestión de Sismos</title>
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
<jsp:include page="/WEB-INF/views/_menu.jsp">
    <jsp:param name="activa" value="estaciones"/>
</jsp:include>
<main class="page">
    <div class="page-head">
        <div><h1>Estaciones de monitoreo</h1>
            <p class="lead">Red de estaciones registradas en el sistema</p></div>
        <div class="actions"><a class="btn primary" href="${pageContext.request.contextPath}/estaciones/nuevo">+ Nueva
            estación</a></div>
    </div>

    <div class="card">
        <div class="card-b flush">
            <table class="data">
                <thead>
                <tr>
                    <th>Código</th>
                    <th>Nombre</th>
                    <th>Departamento</th>
                    <th>Fecha de instalación</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${estaciones}" var="estacion">
                    <tr>
                        <td class="code"><c:out value="${estacion.codigo}"/></td>
                        <td><c:out value="${estacion.nombre}"/></td>
                        <td><c:out value="${estacion.provincia.departamento.nombre}"/></td>
                        <td><c:out value="${estacion.fechaInstalacionTexto}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${estacion.estado == 'ACTIVO'}"><span
                                        class="badge activa">Activa</span></c:when>
                                <c:otherwise><span class="badge inactiva">Inactiva</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <div class="btn-row">
                                <a class="btn sm"
                                   href="${pageContext.request.contextPath}/estaciones/detalle?codigo=${estacion.codigo}">Ver</a>
                                <a class="btn sm"
                                   href="${pageContext.request.contextPath}/estaciones/editar?codigo=${estacion.codigo}">Editar</a>
                                <form method="post" action="${pageContext.request.contextPath}/estaciones/estado">
                                    <input type="hidden" name="codigo" value="${estacion.codigo}">
                                    <button type="submit"
                                            class="btn sm ${estacion.estado == 'ACTIVO' ? 'danger' : ''}"><c:out
                                            value="${estacion.estado == 'ACTIVO' ? 'Desactivar' : 'Activar'}"/></button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</main>
</body>
</html>
